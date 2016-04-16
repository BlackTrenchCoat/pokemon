package org.theproject.browsepokemon.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.DefaultServletHandlerConfigurer;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.webflow.definition.registry.FlowDefinitionRegistry;
import org.springframework.webflow.executor.FlowExecutor;
import org.springframework.webflow.mvc.servlet.FlowHandlerAdapter;
import org.springframework.webflow.mvc.servlet.FlowHandlerMapping;

@Configuration
@EnableWebMvc // replaces <mvc:annotation-driven>
public class WebMvcConfig extends WebMvcConfigurerAdapter {

    @Autowired
    FlowExecutor flowExecutor;

    @Autowired
    FlowDefinitionRegistry flowRegistry;

    @Bean
    public FlowHandlerAdapter flowHandlerAdapter() {
        final FlowHandlerAdapter flowHandlerAdapter = new FlowHandlerAdapter();
        flowHandlerAdapter.setFlowExecutor(flowExecutor);
        flowHandlerAdapter.setSaveOutputToFlashScopeOnRedirect(true);
        return flowHandlerAdapter;
    }

    @Bean
    public FlowHandlerMapping flowHandlerMapping() {
        final FlowHandlerMapping flowHandlerMapping = new FlowHandlerMapping();
        flowHandlerMapping.setOrder(-1);
        flowHandlerMapping.setFlowRegistry(flowRegistry);
        return flowHandlerMapping;
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/resources/**")
                .addResourceLocations("/, classpath:/META-INF/web-resources/");
    }

    // Replaces <mvc:default-servlet-handler />
    // http://docs.spring.io/spring/docs/current/spring-framework-reference/html/mvc.html#mvc-default-servlet-handler
    @Override
    public void configureDefaultServletHandling(DefaultServletHandlerConfigurer configurer) {
        configurer.enable();
    }
    
    // Map paths directly to view names without controller processing. Use the
    // view-name attribute if necessary: by convention the view name equals the
    // path without the leading slash
    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/").setViewName("intro");
        registry.addViewController("/login");
        registry.addViewController("/logoutSuccess");
    }
}
