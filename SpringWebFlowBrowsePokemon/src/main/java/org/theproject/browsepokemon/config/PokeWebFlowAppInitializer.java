package org.theproject.browsepokemon.config;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.servlet.support.AbstractAnnotationConfigDispatcherServletInitializer;

public class PokeWebFlowAppInitializer extends AbstractAnnotationConfigDispatcherServletInitializer {
    
    private static Logger logger = LoggerFactory.getLogger(PokeWebFlowAppInitializer.class);

    @Override
    public void onStartup(ServletContext servletContext) throws ServletException {
        AnnotationConfigWebApplicationContext appContext = new AnnotationConfigWebApplicationContext();
        appContext.register(ApplicationContextConfig.class);

        super.onStartup(servletContext);
    }

    @Override
    protected Class<?>[] getRootConfigClasses() {
        return new Class<?>[]{ ApplicationContextConfig.class };        
    }

    @Override
    protected Class<?>[] getServletConfigClasses() {
        return new Class<?>[]{ WebConfig.class };        
    }

    @Override
    protected String[] getServletMappings() {
        return new String[] { "/flows/*" };        
    }

}
