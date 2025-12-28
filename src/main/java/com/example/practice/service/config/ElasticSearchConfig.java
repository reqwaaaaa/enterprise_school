package com.example.practice.service.config;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.json.jackson.JacksonJsonpMapper;
import co.elastic.clients.transport.ElasticsearchTransport;
import co.elastic.clients.transport.rest_client.RestClientTransport;
import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.elasticsearch.client.elc.ElasticsearchConfiguration;

@Configuration
public class ElasticSearchConfig extends ElasticsearchConfiguration {

  @Override
  public ClientConfiguration clientConfiguration() {
    return ClientConfiguration.builder()
            .connectedTo("localhost:9200")  // 或您的 esUrl 变量
            // .withBasicAuth("username", "password")  // 基本认证
            // .usingSsl()  // 启用 HTTPS
            // .withConnectTimeout(Duration.ofSeconds(5))
            // .withSocketTimeout(Duration.ofSeconds(30))
            .build();
  }

  // 可选：自定义低级 RestClient（不推荐，除非特殊需求）
  // @Bean
  // public RestClient restClient() {
  //     return RestClient.builder(new HttpHost("localhost", 9200, "http")).build();
  // }
}