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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
import org.testcontainers.containers.output.Slf4jLogConsumer;
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
@ContextConfiguration(initializers = MemberServiceIntegration2Test.IntegrationTestInitializer.class)
public class MemberServiceIntegration2Test {

    MemberService memberService;

    static final List<String> userUuidList = new ArrayList<>();
    static final List<String> itemUuidList = new ArrayList<>();
    static final List<ItemDocument> itemDocuments = new ArrayList<>();

    @Autowired MemberRepository memberRepository;
    @Autowired BasketRepository basketRepository;
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
            adminClient.createTopics(List.of(memberTopic, baksetTopic));
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
    }

    @BeforeAll
    static void beforeAll() {
        for (int i = 0; i < 10; i++) {
            userUuidList.add(UUID.randomUUID().toString());
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
        Logger logger = LoggerFactory.getLogger("MongoDBContainer");
        DOCKER_COMPOSE.withLogConsumer("mongodb", new Slf4jLogConsumer(logger));

        DOCKER_COMPOSE.stop();
        kafkaContainer.stop();
    }

    public static class IntegrationTestInitializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {
        @Override
        public void initialize(ConfigurableApplicationContext applicationContext) {
            Map<String, String> properties = new HashMap<>();
            setKafkaProperties(properties);
//            setMySqlProperties(properties);
//            setMongoDBProperties(properties);
            setRedisProperties(properties);
            TestPropertyValues.of(properties).applyTo(applicationContext);
        }

        private void setKafkaProperties(Map<String, String> properties) {
            properties.put("spring.kafka.bootstrap-servers", kafkaContainer.getBootstrapServers());
        }

        private void setMySqlProperties(Map<String, String> properties) {
            String mySqlHost = DOCKER_COMPOSE.getServiceHost("mysql", 3306);
            int mySqlPort = DOCKER_COMPOSE.getServicePort("mysql", 3306);
            properties.put("spring.datasource.url", "jdbc:tc:mysql://" + mySqlHost + ":" + mySqlPort + "/test-member");
            properties.put("spring.datasource.username", "doranms");
            properties.put("spring.datasource.password", "doranms1234!");
        }

        private void setMongoDBProperties(Map<String, String> properties) {
            String mongoDbHost = DOCKER_COMPOSE.getServiceHost("mongodb", 27017);
            int mongoDbPort = DOCKER_COMPOSE.getServicePort("mongodb", 27017);
            properties.put("spring.data.mongodb.uri", "mongodb://testmember:1234@" + mongoDbHost + ":" + mongoDbPort + "/doranms?authSource=admin");
        }

        private void setRedisProperties(Map<String, String> properties) {
            String redisHost = DOCKER_COMPOSE.getServiceHost("redis", 6379);
            Integer redisPort = DOCKER_COMPOSE.getServicePort("redis", 6379);
            properties.put("spring.data.redis.host", redisHost);
            properties.put("spring.data.redis.port", redisPort.toString());
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
    @Order(3)
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
