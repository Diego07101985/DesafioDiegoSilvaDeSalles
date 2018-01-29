package org.challengeplanets.conf;

import org.challengeplanets.models.Planet;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.rest.core.config.RepositoryRestConfiguration;
import org.springframework.data.rest.webmvc.config.RepositoryRestMvcConfiguration;
import org.springframework.hateoas.config.EnableEntityLinks;
import org.springframework.hateoas.config.EnableHypermediaSupport;

import com.fasterxml.jackson.databind.ObjectMapper;

@Configuration
@EnableEntityLinks
@EnableHypermediaSupport(type = EnableHypermediaSupport.HypermediaType.HAL)
public class RestConfiguration extends RepositoryRestMvcConfiguration {

	@Override
    public RepositoryRestConfiguration config() {
        RepositoryRestConfiguration config = super.config();
        config.exposeIdsFor(Planet.class);
        return config; 
    }
	  @Override
	    @Bean
	    @Primary
	    public ObjectMapper halObjectMapper() {
	        return super.halObjectMapper();
	    }
}