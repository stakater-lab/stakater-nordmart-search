package com.stakater.nordmart.search;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.KafkaListenerEndpointRegistry;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.test.EmbeddedKafkaBroker;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.kafka.test.utils.KafkaTestUtils;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.testcontainers.elasticsearch.ElasticsearchContainer;

@ExtendWith(SpringExtension.class)
@EnableKafka
@SpringBootTest(
		properties = "spring.kafka.bootstrap-servers=${spring.embedded.kafka.brokers}",
		webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
		classes = SearchApplication.class
)
@ActiveProfiles(profiles = "test")
@EmbeddedKafka(
		ports = {9092},
		partitions = 1,
		topics = { "productsTopic"})
public class BaseTest {
	@Value("${server.host}")
	protected String serverHost;
	@LocalServerPort
	protected String serverPort;
	@Value("${server.servlet.contextPath}")
	protected String serverContextPath;

	protected static final String CONTROLLER_PATH = "/api/v1/product-search";

	@Value("${stakater-nordmart-search.kafka.product.topic.name}")
	protected String productTopicName;

	@Autowired
 	private EmbeddedKafkaBroker embeddedKafka;

	@Autowired
	private static KafkaListenerEndpointRegistry kafkaListenerEndpointRegistry;

	protected KafkaTemplate<String, String> kafkaTemplate;

	@BeforeEach
	public void beforeEach() {
		DefaultKafkaProducerFactory<Object, Object> producerFactory
				= new DefaultKafkaProducerFactory<>(KafkaTestUtils.producerProps(embeddedKafka));

		kafkaTemplate = new KafkaTemplate(producerFactory);
		kafkaTemplate.setDefaultTopic(productTopicName);
	}

	@BeforeAll
	public static void setUp() {
		ElasticsearchContainer container =
				new ElasticsearchContainer("docker.elastic.co/elasticsearch/elasticsearch:7.5.1")
						.withExposedPorts(9200);
		container.start();
		System.setProperty("ELASTICSEARCH_PORT", String.valueOf(container.getMappedPort(9200)));
	}

	protected String getTestServerUrl() {
		return serverHost + ":" + serverPort + serverContextPath;
	}
}
