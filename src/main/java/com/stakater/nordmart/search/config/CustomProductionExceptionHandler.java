package com.stakater.nordmart.search.config;

import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.streams.errors.ProductionExceptionHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

public class CustomProductionExceptionHandler implements ProductionExceptionHandler {
    private final Logger logger = LoggerFactory.getLogger(CustomProductionExceptionHandler.class);

    @Override
    public ProductionExceptionHandlerResponse handle(final ProducerRecord<byte[], byte[]> record,
                                                     final Exception exception) {
        logger.error("Kafka message marked as processed although it failed. Message: [{}], destination topic: [{}]",
                new String(record.value()), record.topic());
        logger.error("Kafka message marked as processed although it failed, exception", exception);
        return ProductionExceptionHandlerResponse.CONTINUE;
    }

    @Override
    public void configure(final Map<String, ?> configs) {
    }

}