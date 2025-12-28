package com.example.practice.service.config;

import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Knife4j + SpringDoc OpenAPI 配置
 * 用于生成和管理 API 文档，支持 Knife4j 美化界面
 */
@Configuration
public class Knife4jConfig {

  @Bean
  public GroupedOpenApi schoolEnterpriseApi() {
    return GroupedOpenApi.builder()
            // 分组名称（在 Knife4j 界面显示）
            .group("校企慧平台")
            // 扫描的 Controller 包路径（请根据实际项目调整）
            .packagesToScan("com.example.practice.controller")
            // 扫描的路径（通常全部）
            .pathsToMatch("/**")
            .build();
  }

  // 可选：如果项目有多个模块，可以再定义其他分组
  // @Bean
  // public GroupedOpenApi adminApi() {
  //     return GroupedOpenApi.builder()
  //             .group("后台管理")
  //             .packagesToScan("com.example.practice.admin.controller")
  //             .pathsToMatch("/admin/**")
  //             .build();
  // }
}