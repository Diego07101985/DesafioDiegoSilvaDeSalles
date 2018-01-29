package org.challengeplanets.conf;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

import com.mongodb.MongoClient;

@Configuration
@EnableMongoRepositories(basePackages = "org.challengeplanets.repositorios.mongo")
public class MongoConfiguration {

	@Bean
	@Profile("dev")
	public MongoTemplate mongoTemplate() throws Exception {
		return new MongoTemplate(new MongoClient("localhost"), "dev_database");
	}
}
