package org.theproject.browsepokemon.config;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.servlet.support.AbstractAnnotationConfigDispatcherServletInitializer;
import org.theproject.browsepokemon.model.PokedexDAOImpl;

public class PokeWebFlowAppInitializer extends AbstractAnnotationConfigDispatcherServletInitializer {
    
    private static Logger logger = LoggerFactory.getLogger(PokeWebFlowAppInitializer.class);

    @Override
    public void onStartup(ServletContext servletContext) throws ServletException {
        
        logger.info("*** In onStartup...");
        
        AnnotationConfigWebApplicationContext appContext = new AnnotationConfigWebApplicationContext();
        appContext.register(ApplicationContextConfig.class);
        
        // TODO Auto-generated method stub
        super.onStartup(servletContext);
    }

    @Override
    protected Class<?>[] getRootConfigClasses() {
        
        logger.info("*** In getRootConfigClasses...");
        
        return new Class<?>[]{ ApplicationContextConfig.class };        
    }

    @Override
    protected Class<?>[] getServletConfigClasses() {
        
        logger.info("*** In getServletConfigClasses...");
        
        return new Class<?>[]{ WebConfig.class };        
    }

    @Override
    protected String[] getServletMappings() {
        
        logger.info("*** In getServletMappings...");
        
        return new String[] { "/flows/*" };        
    }

}
