package org.theproject.browsepokemon.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import({WebMvcConfig.class, WebFlowConfig.class})
public class WebConfig {
}
