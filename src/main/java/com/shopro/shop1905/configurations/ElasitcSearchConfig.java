package com.shopro.shop1905.configurations;

import java.io.IOException;

import javax.net.ssl.SSLContext;

import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.elasticsearch.client.RestClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.json.jackson.JacksonJsonpMapper;
import co.elastic.clients.transport.ElasticsearchTransport;
import co.elastic.clients.transport.TransportUtils;
import co.elastic.clients.transport.rest_client.RestClientTransport;

@Configuration
@EnableElasticsearchRepositories(basePackages = "com.shopro.shop1905.repositories")
class ElasitcSearchConfig {
        @Autowired

        @Value("${elasticsearch.host}")
        private String host;
        @Value("${elasticsearch.port}")
        private int port;
        @Value("${elasticsearch.fingerprint}")
        String fingerprint;

        @Bean
        ElasticsearchClient client() throws IOException {
                SSLContext sslContext = TransportUtils
                                .sslContextFromCaFingerprint(fingerprint);

                BasicCredentialsProvider credsProv = new BasicCredentialsProvider();
                credsProv.setCredentials(
                                AuthScope.ANY, new UsernamePasswordCredentials("elastic", "0hvMARb=Q8FBteX8jb*c"));

                RestClient restClient = RestClient
                                .builder(new HttpHost(host, port, "https"))
                                .setHttpClientConfigCallback(hc -> hc
                                                .setSSLContext(sslContext)
                                                .setDefaultCredentialsProvider(credsProv))
                                .build();

                // Create the transport and the API client
                ElasticsearchTransport transport = new RestClientTransport(restClient, new JacksonJsonpMapper());
                ElasticsearchClient client = new ElasticsearchClient(transport);
                return client;
        }
}