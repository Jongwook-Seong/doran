package com.sjw.doran.memberservice.integration;

import com.sjw.doran.memberservice.client.ResilientOrderServiceClient;
import com.sjw.doran.memberservice.dto.MemberDto;
import com.sjw.doran.memberservice.entity.Basket;
import com.sjw.doran.memberservice.entity.Category;
import com.sjw.doran.memberservice.entity.Member;
import com.sjw.doran.memberservice.kafka.common.Topic;
import com.sjw.doran.memberservice.mapper.BasketMapper;
import com.sjw.doran.memberservice.mapper.MemberMapper;
import com.sjw.doran.memberservice.mapper.order.DeliveryMapper;
import com.sjw.doran.memberservice.mapper.order.OrderMapper;
import com.sjw.doran.memberservice.mongodb.delivery.DeliveryDocumentRepository;
import com.sjw.doran.memberservice.mongodb.item.ItemDocument;
import com.sjw.doran.memberservice.mongodb.item.ItemDocumentRepository;
import com.sjw.doran.memberservice.mongodb.member.MemberDocumentRepository;
import com.sjw.doran.memberservice.mongodb.order.OrderDocumentRepository;
import com.sjw.doran.memberservice.repository.BasketRepository;
import com.sjw.doran.memberservice.repository.MemberRepository;
import com.sjw.doran.memberservice.service.BasketItemService;
import com.sjw.doran.memberservice.service.BasketService;
import com.sjw.doran.memberservice.service.MemberService;
import com.sjw.doran.memberservice.service.impl.BasketServiceImpl;
import com.sjw.doran.memberservice.service.impl.MemberServiceImpl;
import com.sjw.doran.memberservice.util.MessageUtil;
import com.sjw.doran.memberservice.vo.request.BasketItemCreateRequest;
import com.sjw.doran.memberservice.vo.response.item.ItemSimpleWithCountResponse;
import lombok.extern.slf4j.Slf4j;
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

@Slf4j
@Testcontainers
@ActiveProfiles("test")
@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@ContextConfiguration(initializers = MemberServiceIntegrationTest.IntegrationTestInitializer.class)
public class MemberServiceIntegrationTest {

    MemberService memberService;
    BasketService basketService;

    static final List<String> userUuidList = new ArrayList<>();
    static final List<String> itemUuidList = new ArrayList<>();
    static final List<ItemDocument> itemDocuments = new ArrayList<>();

    @Autowired MemberRepository memberRepository;
    @Autowired BasketRepository basketRepository;
    @Autowired BasketItemService basketItemService;
    @Autowired MemberDocumentRepository memberDocumentRepository;
    @Autowired OrderDocumentRepository orderDocumentRepository;
    @Autowired DeliveryDocumentRepository deliveryDocumentRepository;
    @Autowired ItemDocumentRepository itemDocumentRepository;
    @Autowired ResilientOrderServiceClient resilientOrderServiceClient;
    @Autowired ApplicationEventPublisher applicationEventPublisher;
    @Autowired MemberMapper memberMapper;
    @Autowired BasketMapper basketMapper;
    @Autowired OrderMapper orderMapper;
    @Autowired DeliveryMapper deliveryMapper;
    @Autowired MessageUtil messageUtil;

    private static final DockerComposeContainer<?> DOCKER_COMPOSE =
            new DockerComposeContainer<>(new File("src/test/resources/docker-compose.yml"))
                    .withExposedService("mysql", 3306, Wait.forLogMessage(".*ready for connections.*", 1))
                    .withExposedService("mongodb", 27017, Wait.forLogMessage(".*ready for connections.*", 1))
                    .withExposedService("redis", 6379, Wait.forLogMessage(".*Ready to accept connections.*", 1));

    @Container
    public static final ConfluentKafkaContainer kafkaContainer = new ConfluentKafkaContainer("confluentinc/cp-kafka:7.6.0");

    private static void createKafkaTopics() {
        Properties config = new Properties();
        config.put(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaContainer.getBootstrapServers());

        try (AdminClient adminClient = AdminClient.create(config)) {
            NewTopic memberTopic = new NewTopic(Topic.MEMBER_TOPIC, 1, (short) 1);
            NewTopic baksetTopic = new NewTopic(Topic.BASKET_TOPIC, 1, (short) 1);
            NewTopic orderTopic = new NewTopic(Topic.ORDER_TOPIC, 1, (short) 1);
            NewTopic deliveryTopic = new NewTopic(Topic.DELIVERY_TOPIC, 1, (short) 1);
            NewTopic itemTopic = new NewTopic(Topic.ITEM_TOPIC, 1, (short) 1);
            adminClient.createTopics(List.of(memberTopic, baksetTopic, orderTopic, deliveryTopic, itemTopic));
        }
    }

    @BeforeEach
    void beforeEach() {
        memberService = new MemberServiceImpl(
                memberRepository,
                basketRepository,
                memberDocumentRepository,
                orderDocumentRepository,
                deliveryDocumentRepository,
                itemDocumentRepository,
                resilientOrderServiceClient,
                applicationEventPublisher,
                memberMapper,
                basketMapper,
                orderMapper,
                deliveryMapper,
                messageUtil
        );

        basketService = new BasketServiceImpl(
                memberRepository,
                basketRepository,
                basketItemService
        );
    }

    @BeforeAll
    static void beforeAll() {
        for (int i = 0; i < 10; i++) {
            userUuidList.add(UUID.randomUUID().toString());
        }
        for (int i = 0; i < 20; i++) {
            itemUuidList.add(UUID.randomUUID().toString());
            itemDocuments.add(new ItemDocument((long) i, itemUuidList.get(i), "item" + i, "url" + i, Category.BOOK, 1000));
        }
        try {
            DOCKER_COMPOSE.start();
            kafkaContainer.start();
            createKafkaTopics();
        } catch (Exception e) {
            System.err.println("Container setup failed: " + e.getMessage());
            System.err.println(kafkaContainer.getLogs());
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
    @DisplayName("회원 생성")
    void createMemberTest() {
        // given
        for (String userUuid : userUuidList) {
            Member member = new Member(userUuid, UUID.randomUUID().toString().substring(0, 8));
            memberService.saveMember(member);
        }

        // when
        List<MemberDto> members = memberService.findMembers();

        // then
        for (int i = 0; i < members.size(); i++) {
            MemberDto member = members.get(i);
            log.info("member = {}", member);
            assertThat(member.getUserUuid()).isIn(userUuidList);
        }
    }

    @Test
    @Order(2)
    @Transactional
    @Rollback(false)
    @DisplayName("ItemDocumentRepository 아이템 데이터 생성")
    void setItemDocumentRepository() {
        itemDocumentRepository.saveAll(itemDocuments);
    }

    @Test
    @Order(3)
    @Transactional
    @Rollback(false)
    @DisplayName("장바구니 아이템 추가")
    void addBasketItemTest() throws InterruptedException {
        Thread.sleep(1000);

        int i = 0;
        for (String userUuid : userUuidList) {
            for (int j = 0; j < 2; j++) {
                BasketItemCreateRequest request = BasketItemCreateRequest.builder()
                        .itemUuid(itemUuidList.get(i++))
                        .count(1)
                        .build();
                basketService.addBasketItem(userUuid, request);
            }
        }

        for (String userUuid : userUuidList) {
            Member member = memberService.findMember(userUuid);
            Basket basket = basketService.findBasket(member);
            List<ItemSimpleWithCountResponse> userBasket = basketItemService.findAllByBasket(basket);
            for (ItemSimpleWithCountResponse response : userBasket) {
                assertThat(response.getItemUuid()).isIn(itemUuidList);
            }
        }
    }

    @Test
    @Order(4)
    @Transactional
    @Rollback(false)
    @DisplayName("장바구니 아이템 삭제")
    void deleteBasketItemTest() throws InterruptedException {
        Thread.sleep(1000);

        for (String userUuid : userUuidList) {
            Member member = memberService.findMember(userUuid);
            Basket basket = basketService.findBasket(member);
            List<ItemSimpleWithCountResponse> userBasket = basketItemService.findAllByBasket(basket);
            for (ItemSimpleWithCountResponse response : userBasket) {
                basketService.deleteBasketItem(userUuid, response.getItemUuid());
            }

            List<ItemSimpleWithCountResponse> userBasketCheck = basketItemService.findAllByBasket(basket);
        }
    }

    @Test
    @Order(5)
    @Transactional
    @DisplayName("장바구니 아이템 삭제 확인")
    void deleteBasketItemCheck() throws InterruptedException {
        Thread.sleep(1000);

        for (String userUuid : userUuidList) {
            Member member = memberService.findMember(userUuid);
            Basket basket = basketService.findBasket(member);
            List<ItemSimpleWithCountResponse> userBasket = basketItemService.findAllByBasket(basket);
            assertThat(userBasket).isEmpty();
        }
    }

    @Test
    @Order(6)
    @Transactional
    @Rollback(false)
    @DisplayName("회원 삭제")
    void deleteMemberTest() throws InterruptedException {
        // given
        Thread.sleep(1000);

        // when
        List<MemberDto> members = memberService.findMembers();

        // then
        assertThat(members).isNotEmpty();
        for (String userUuid : userUuidList) {
            memberService.deleteMember(userUuid);
        }
    }

    @Test
    @Order(7)
    @Transactional
    @DisplayName("회원 삭제 확인")
    void deleteMemberCheck() throws InterruptedException {
        // given
        Thread.sleep(1000);

        // when
        List<MemberDto> members = memberService.findMembers();

        // then
        assertThat(members).isEmpty();
    }
}
