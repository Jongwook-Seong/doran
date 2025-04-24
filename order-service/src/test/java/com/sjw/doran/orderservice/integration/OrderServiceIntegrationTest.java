package com.sjw.doran.orderservice.integration;

import com.sjw.doran.orderservice.client.ResilientItemServiceClient;
import com.sjw.doran.orderservice.entity.Address;
import com.sjw.doran.orderservice.entity.TransceiverInfo;
import com.sjw.doran.orderservice.kafka.common.Topic;
import com.sjw.doran.orderservice.mapper.*;
import com.sjw.doran.orderservice.mongodb.delivery.DeliveryDocumentRepository;
import com.sjw.doran.orderservice.mongodb.item.ItemDocumentRepository;
import com.sjw.doran.orderservice.mongodb.order.OrderDocumentRepository;
import com.sjw.doran.orderservice.repository.DeliveryTrackingRepository;
import com.sjw.doran.orderservice.repository.OrderItemRepository;
import com.sjw.doran.orderservice.repository.OrderRepository;
import com.sjw.doran.orderservice.service.OrderService;
import com.sjw.doran.orderservice.service.impl.OrderServiceImpl;
import com.sjw.doran.orderservice.util.MessageUtil;
import com.sjw.doran.orderservice.vo.ItemSimpleInfo;
import com.sjw.doran.orderservice.vo.request.OrderCreateRequest;
import com.sjw.doran.orderservice.vo.response.OrderDetailResponse;
import org.apache.kafka.clients.admin.AdminClient;
import org.apache.kafka.clients.admin.AdminClientConfig;
import org.apache.kafka.clients.admin.NewTopic;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.containers.DockerComposeContainer;
import org.testcontainers.containers.wait.strategy.Wait;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.kafka.ConfluentKafkaContainer;

import java.io.File;
import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;

@Testcontainers
@ActiveProfiles("test")
@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@ContextConfiguration(initializers = OrderServiceIntegrationTest.IntegrationTestInitializer.class)
public class OrderServiceIntegrationTest {

    OrderService orderService;

    static final List<String> userUuidList = new ArrayList<>();
    static final List<String> orderUuidList = new ArrayList<>();
    static final List<String> itemUuidList = new ArrayList<>();
    static final List<OrderCreateRequest> orderCreateRequests = new ArrayList<>();

    @Autowired OrderRepository orderRepository;
    @Autowired OrderItemRepository orderItemRepository;
    @Autowired DeliveryTrackingRepository deliveryTrackingRepository;
    @Autowired OrderDocumentRepository orderDocumentRepository;
    @Autowired DeliveryDocumentRepository deliveryDocumentRepository;
    @Autowired ItemDocumentRepository itemDocumentRepository;
    @Autowired ResilientItemServiceClient resilientItemServiceClient;
    @Autowired ApplicationEventPublisher applicationEventPublisher;
    @Autowired OrderMapper orderMapper;
    @Autowired DeliveryMapper deliveryMapper;
    @Autowired DeliveryTrackingMapper deliveryTrackingMapper;
    @Autowired TransceiverInfoMapper transceiverInfoMapper;
    @Autowired AddressMapper addressMapper;
    @Autowired ItemMapper itemMapper;
    @Autowired MessageUtil messageUtil;

    private static final DockerComposeContainer<?> DOCKER_COMPOSE =
            new DockerComposeContainer<>(new File("src/test/resources/docker-compose.yml"))
                    .withExposedService("mysql", 3306, Wait.forLogMessage(".*ready for connections.*", 1))
                    .withExposedService("mongodb", 27017, Wait.forLogMessage(".*ready for connections.*", 1));

    @Container
    public static final ConfluentKafkaContainer kafkaContainer = new ConfluentKafkaContainer("confluentinc/cp-kafka:7.6.0");

    private static void createKafkaTopics() {
        Properties config = new Properties();
        config.put(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaContainer.getBootstrapServers());

        try (AdminClient adminClient = AdminClient.create(config)) {
            NewTopic orderTopic = new NewTopic(Topic.ORDER_TOPIC, 1, (short) 1);
            NewTopic deliveryTopic = new NewTopic(Topic.DELIVERY_TOPIC, 1, (short) 1);
            NewTopic itemTopic = new NewTopic(Topic.ITEM_TOPIC, 1, (short) 1);
            adminClient.createTopics(List.of(orderTopic, deliveryTopic, itemTopic));
        }
    }

    @BeforeEach
    void beforeEach() {
        orderService = new OrderServiceImpl(
                orderRepository,
                orderItemRepository,
                deliveryTrackingRepository,
                orderDocumentRepository,
                deliveryDocumentRepository,
                itemDocumentRepository,
                resilientItemServiceClient,
                applicationEventPublisher,
                orderMapper,
                deliveryMapper,
                deliveryTrackingMapper,
                transceiverInfoMapper,
                addressMapper,
                itemMapper,
                messageUtil
        );
    }

    @BeforeAll
    static void beforeAll() {
        for (int i = 0; i < 10; i++) {
            userUuidList.add(UUID.randomUUID().toString());
            orderUuidList.add(UUID.randomUUID().toString());
            itemUuidList.add(UUID.randomUUID().toString());
        }
        try {
            DOCKER_COMPOSE.start();
            kafkaContainer.start();
            createKafkaTopics();
        } catch (Exception e) {
            System.err.println("Container setup failed: " + e.getMessage());
            System.err.println("Kafka Container Logs : " + kafkaContainer.getLogs());
            throw new RuntimeException(e);
        }
    }

    @AfterAll
    static void afterAll() {
        DOCKER_COMPOSE.stop();
        kafkaContainer.stop();
    }

    public static class IntegrationTestInitializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {
        @Override
        public void initialize(ConfigurableApplicationContext applicationContext) {
            Map<String, String> properties = new HashMap<>();
            setKafkaProperties(properties);
            TestPropertyValues.of(properties).applyTo(applicationContext);
        }

        private void setKafkaProperties(Map<String, String> properties) {
            properties.put("spring.kafka.bootstrap-servers", kafkaContainer.getBootstrapServers());
        }
    }

    @Test
    @Order(1)
    @Transactional
    @Rollback(false)
    @DisplayName("주문 생성")
    void createOrderTest() throws InterruptedException {
        // given
        for (int i = 0; i < 10; i++) {
            ItemSimpleInfo itemSimpleInfo = new ItemSimpleInfo(itemUuidList.get(i), 1000, 1);
            TransceiverInfo transceiverInfo = new TransceiverInfo("orderer" + i, "receiver" + i, "010-0000-000" + i);
            Address address = new Address("city" + i, "street" + i, "details" + i, "zipcode" + i);
            orderCreateRequests.add(new OrderCreateRequest(List.of(itemSimpleInfo), transceiverInfo, address));
        }

        // when
        for (int i = 0; i < 10; i++) {
            orderService.createOrder(userUuidList.get(i), orderCreateRequests.get(i));
        }
    }

    @Test
    @Order(2)
    @Transactional
    @Rollback(false)
    @DisplayName("주문 생성 확인")
    void createOrderCheck() throws InterruptedException {
        Thread.sleep(1000);

        // then
        for (int i = 0; i < 10; i++) {
            OrderDetailResponse orderDetail = orderService.getOrderDetail(userUuidList.get(i), orderUuidList.get(i));
            assertThat(orderDetail.getOrderItemSimpleList().get(0).getItemUuid()).isEqualTo(itemUuidList.get(i));
        }
    }
}
