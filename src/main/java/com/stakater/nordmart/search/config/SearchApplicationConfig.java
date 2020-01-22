package com.stakater.nordmart.search.config;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.stakater.nordmart.search.dto.product.ProductCommand;
import com.stakater.nordmart.search.facade.ProductCommandFacade;
import org.apache.kafka.clients.admin.AdminClientConfig;
import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.kstream.KStream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.annotation.EnableKafkaStreams;
import org.springframework.kafka.annotation.KafkaStreamsDefaultConfiguration;
import org.springframework.kafka.config.KafkaStreamsConfiguration;
import org.springframework.kafka.core.KafkaAdmin;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@EnableKafka
@EnableKafkaStreams
@Configuration
public class SearchApplicationConfig {
    private static final Logger logger = LoggerFactory.getLogger(SearchApplicationConfig.class);

    @Value("${stakater-nordmart-search.kafka.product.topic.create.on-startup}")
    private Boolean createProductTopicOnStartup;
    @Value("${stakater-nordmart-search.kafka.product.topic.name}")
    private String productTopicName;
    @Value("${stakater-nordmart-search.kafka.product.topic.number-of-partitions}")
    private Integer productTopicNumberOfPartitions;
    @Value("${stakater-nordmart-search.kafka.product.topic.replication-factor}")
    private Short productTopicReplicationFactor;
    @Value("${spring.application.name}")
    private String applicationName;
    @Value("${stakater-nordmart-search.cors.allowed.hosts}")
    private String[] corsAllowedHosts;
    @Value("${stakater-nordmart-search.kafka.bootstrap-servers}")
    private List<String> bootstrapServers;

    @Autowired
    private ProductCommandFacade productCommandFacade;

    @ConditionalOnProperty(name = "stakater-nordmart-search.kafka.product.topic.create.on-startup",
                           havingValue = "true")
    @Bean
    public NewTopic productsTopic() {
        return new NewTopic(productTopicName, productTopicNumberOfPartitions,
                productTopicReplicationFactor);
    }

    @ConditionalOnProperty(name = "stakater-nordmart-search.kafka.product.topic.create.on-startup",
            havingValue = "true")
    @Bean
    public KafkaAdmin kafkaAdmin() {
        Map<String, Object> configs = new HashMap<>();
        configs.put(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        return new KafkaAdmin(configs);
    }

    @Bean(name = KafkaStreamsDefaultConfiguration.DEFAULT_STREAMS_CONFIG_BEAN_NAME)
    public KafkaStreamsConfiguration kStreamsConfigs() {
        logger.debug("Kafka bootstrap servers: " + bootstrapServers);
        Map<String, Object> props = new HashMap<>();
        props.put(StreamsConfig.APPLICATION_ID_CONFIG, applicationName);
        props.put(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, Serdes.Integer().getClass().getName());
        props.put(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, Serdes.String().getClass().getName());
        props.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        props.put(StreamsConfig.DEFAULT_PRODUCTION_EXCEPTION_HANDLER_CLASS_CONFIG, "com.stakater.nordmart.search.config.CustomProductionExceptionHandler");
        return new KafkaStreamsConfiguration(props);
    }

    @Bean
    public ObjectMapper objectMapper() {
        return new ObjectMapper();
    }

    @Bean
    public KStream kStream(StreamsBuilder kStreamBuilder) {
        KStream<String, String> stream = kStreamBuilder.stream(productTopicName);
        stream
            .foreach((key, message) -> {
                try {
                    ProductCommand productCommand = objectMapper().readValue(message, ProductCommand.class);
                    productCommandFacade.processProductCommand(productCommand);
                } catch (JsonProcessingException e) {
                    logger.error("Could not convert JSON message " + message + " into object.", e);
                }
            });

        return stream;
    }

    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**").allowedOrigins(corsAllowedHosts);
            }
        };
    }
}
