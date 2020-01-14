package com.stakater.nordmart.search.config;

import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.elasticsearch.client.ClientConfiguration;
import org.springframework.data.elasticsearch.client.RestClients;
import org.springframework.data.elasticsearch.config.AbstractElasticsearchConfiguration;

@Configuration
public class ElasticsearchClientConfig extends AbstractElasticsearchConfiguration {
    @Value("${skakater-nordmart-search.elasticsearch.host}")
    private String elasticsearchHost;
    @Value("${skakater-nordmart-search.elasticsearch.port}")
    private String elasticsearchPort;
    @Value("${skakater-nordmart-search.elasticsearch.username}")
    private String elasticsearchUsername;
    @Value("${skakater-nordmart-search.elasticsearch.password}")
    private String elasticsearchPassword;

    @Override
    public RestHighLevelClient elasticsearchClient() {
        final ClientConfiguration clientConfiguration = ClientConfiguration.builder()
                .connectedTo(elasticsearchHost + ":" + elasticsearchPort)
                .withBasicAuth(elasticsearchUsername, elasticsearchPassword)
                .build();

        return RestClients.create(clientConfiguration).rest();
    }
}
