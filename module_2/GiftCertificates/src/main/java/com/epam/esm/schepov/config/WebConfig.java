package com.epam.esm.schepov.config;

import com.epam.esm.schepov.persistence.config.DataSourceConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ComponentScans;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.TransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;


@Configuration
@EnableWebMvc
@EnableTransactionManagement
@ComponentScans({
        @ComponentScan("com.epam.esm.schepov"),
        @ComponentScan("com.epam.esm.schepov.persistence"),
        @ComponentScan("com.epam.esm.schepov.service"),
        @ComponentScan("com.epam.esm.schepov.core")
})
public class WebConfig implements WebMvcConfigurer {

    private final DataSourceConfig dataSourceConfig;

    @Autowired
    public WebConfig(DataSourceConfig dataSourceConfig) {
        this.dataSourceConfig = dataSourceConfig;
    }

    @Override
    public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
        converters.add(new MappingJackson2HttpMessageConverter());
    }

    @Bean
    public TransactionManager transactionManager(){
        return new DataSourceTransactionManager(dataSourceConfig.dataSource());
    }

}
