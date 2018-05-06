package org.theproject.y2012.config;

import javax.sql.DataSource;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;

@Configuration
public class DataSourceConfig {
	
	@Bean
	@Profile("test")
	public DataSource dataSourceForTest() {
		EmbeddedDatabaseBuilder builder = new EmbeddedDatabaseBuilder();
		return builder.setName("embedded")
				.setType(EmbeddedDatabaseType.HSQL)
				.addScript("classpath:/pokemon-2012-schema.sql")
				.addScript("classpath:/pokemon-2012-data.sql").build();
	}

}
