package com.epam.esm.schepov.config;

import com.epam.esm.schepov.formatter.DateFormatter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ComponentScans;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;


@Configuration
@EnableWebMvc
@ComponentScans({
        @ComponentScan("com.epam.esm.schepov"),
        @ComponentScan("com.epam.esm.schepov.persistence"),
        @ComponentScan("com.epam.esm.schepov.service"),
        @ComponentScan("com.epam.esm.schepov.core")
})
public class WebConfig implements WebMvcConfigurer {

//    private final ApplicationContext applicationContext;
//
//    @Autowired
//    public WebConfig(ApplicationContext applicationContext){
//        this.applicationContext = applicationContext;
//    }

//    @Bean
//    public SpringResourceTemplateResolver templateResolver(){
//        SpringResourceTemplateResolver templateResolver = new SpringResourceTemplateResolver();
//        templateResolver.setApplicationContext(applicationContext);
//        templateResolver.setPrefix("/WEB-INF/views/");
//        templateResolver.setSuffix(".html");
//        return templateResolver;
//    }
//
//    @Bean
//    public SpringTemplateEngine templateEngine(){
//        SpringTemplateEngine templateEngine = new SpringTemplateEngine();
//        templateEngine.setEnableSpringELCompiler(true);
//        templateEngine.setTemplateResolver(templateResolver());
//        return templateEngine;
//    }
//
//    @Override
//    public void configureViewResolvers(ViewResolverRegistry registry) {
//        ThymeleafViewResolver viewResolver = new ThymeleafViewResolver();
//        viewResolver.setTemplateEngine(templateEngine());
//        registry.viewResolver(viewResolver);
//    }

//    @Override
//    public void addFormatters(FormatterRegistry formatterRegistry){
//        formatterRegistry.addFormatter(dateFormatter());
//    }
//
    @Bean
    public ResourceBundleMessageSource formatterSettingsSource(){
        ResourceBundleMessageSource messageSource = new ResourceBundleMessageSource();
        messageSource.setBasename("formatter");
        return messageSource;
    }

    @Bean
    public DateFormatter dateFormatter(){
        return new DateFormatter(formatterSettingsSource());
    }


}
