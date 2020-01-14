package com.stakater.nordmart.search;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.KafkaListenerEndpointRegistry;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.test.EmbeddedKafkaBroker;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.kafka.test.utils.KafkaTestUtils;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import pl.allegro.tech.embeddedelasticsearch.EmbeddedElastic;
import pl.allegro.tech.embeddedelasticsearch.PopularProperties;

import java.util.concurrent.TimeUnit;

@EnableKafka
@SpringBootTest(
		properties = "spring.kafka.bootstrap-servers=${spring.embedded.kafka.brokers}"
)
@EmbeddedKafka(
		ports = {9092},
		partitions = 1,
		topics = { "productsTopic"})
@ContextConfiguration(initializers = SearchApplicationTests.Initializer.class)
public class SearchApplicationTests {
	private static Integer port = 9200;
	private static final String ELASTIC_VERSION = "6.5.4";
	private static EmbeddedElastic embeddedElastic;
	private static final String TEST_SERVER_URL = "http://localhost:8081";
	private static final String CONTROLLER_PATH = "/v1/product-search";

	@Autowired
	private WebApplicationContext context;

	@Autowired
 	private EmbeddedKafkaBroker embeddedKafka;

	@Autowired
	private static KafkaListenerEndpointRegistry kafkaListenerEndpointRegistry;

	private MockMvc mvc;
	private KafkaTemplate<String, String> kafkaTemplate;

	@BeforeEach
	public void beforeEach() {
		DefaultKafkaProducerFactory<Object, Object> producerFactory
				= new DefaultKafkaProducerFactory<>(KafkaTestUtils.producerProps(embeddedKafka));

		kafkaTemplate = new KafkaTemplate(producerFactory);
		kafkaTemplate.setDefaultTopic("productsTopics");

		this.mvc = MockMvcBuilders.webAppContextSetup(this.context).build();
	}

	@BeforeAll
	public static void setUp() throws Exception {
		embeddedElastic = EmbeddedElastic.builder()
				.withElasticVersion(ELASTIC_VERSION)
				.withSetting(PopularProperties.HTTP_PORT, port)
				.withSetting(PopularProperties.CLUSTER_NAME, "my_cluster")
				.withStartTimeout(60, TimeUnit.SECONDS)
				.build()
				.start();
	}

	@Test
	void contextLoads() {
	}

	static class Initializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {
		@Override
		public void initialize(
				ConfigurableApplicationContext configurableApplicationContext) {
			TestPropertyValues.of("elasticsearch.port=" + port)
					.applyTo(configurableApplicationContext.getEnvironment());
		}
	}

	@AfterAll
	public static void afterClass() {
		embeddedElastic.stop();
	}

}
