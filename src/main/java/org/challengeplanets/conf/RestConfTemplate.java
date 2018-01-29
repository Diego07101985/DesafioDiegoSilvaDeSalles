package org.challengeplanets.conf;

import java.util.Collections;

import org.challengeplanets.conf.interceptor.UserAgentInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;


@Configuration
public class RestConfTemplate {

	@Bean
	public RestTemplate restTemplate() {
		RestTemplate restTemplate = new RestTemplate();
		restTemplate.setInterceptors(Collections.singletonList(new UserAgentInterceptor()));
		
		return restTemplate;
	}

}
