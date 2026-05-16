package com.cloud.business.config;

import com.cloud.business.demo.DemoDataProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(DemoDataProperties.class)
public class DemoDataConfig {
}
