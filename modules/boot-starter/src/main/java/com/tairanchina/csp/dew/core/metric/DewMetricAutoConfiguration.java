package com.tairanchina.csp.dew.core.metric;

import com.tairanchina.csp.dew.core.DewConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import javax.servlet.Filter;

@Configuration
@EnableConfigurationProperties(DewConfig.class)
@ConditionalOnProperty(prefix = "dew.metric", name = "enabled", havingValue = "true", matchIfMissing = true)
public class DewMetricAutoConfiguration {

    private static final Logger logger = LoggerFactory.getLogger(DewMetricAutoConfiguration.class);

    private DewConfig dewConfig;

    public DewMetricAutoConfiguration(DewConfig dewConfig) {
        this.dewConfig = dewConfig;
    }

    @PostConstruct
    public void init() {
        logger.info("Load Auto Configuration : {}", this.getClass().getName());
    }

    @Bean
    @ConditionalOnClass(Filter.class)
    public DewFilter dewFilter() {
        return new DewFilter();
    }

    @Bean
    public FilterRegistrationBean testFilterRegistration(@Autowired DewFilter dewFilter) {
        FilterRegistrationBean registration = new FilterRegistrationBean();
        registration.setFilter(dewFilter);
        registration.addUrlPatterns("/*");
        registration.setName("dewFilter");
        registration.setOrder(1);
        return registration;
    }

    @Bean
    @ConditionalOnBean(DewFilter.class)
    public DewMetrics dewMetrics() {
        return new DewMetrics(dewConfig);
    }
}
