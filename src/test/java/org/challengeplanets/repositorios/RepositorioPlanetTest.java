package org.challengeplanets.repositorios;

import java.util.List;

import org.challengeplanets.Application;
import org.challengeplanets.builder.PlanetBuilder;
import org.challengeplanets.conf.MongoConfiguration;
import org.challengeplanets.models.Planet;
import org.challengeplanets.repositorios.mongo.PlanetRepository;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = { Application.class, PlanetRepository.class,
		MongoConfiguration.class })
@ActiveProfiles("test-mongo")
public class RepositorioPlanetTest {

	@Autowired
	private MongoTemplate mongoTemplate;

	@Autowired
	private PlanetRepository repositoryPlanets;

	List<Planet> planets;

	@Before
	public void clearCollection() {
		mongoTemplate.dropCollection(Planet.class);
		planets = PlanetBuilder.newPlanet().buildFivePlanets().buildAll();
		planets.stream().forEach(repositoryPlanets::save);
	}

	@Test
	public void deveRetornarPlanetasQuantidadeDePlanetas() {

		planets.stream().forEach(repositoryPlanets::save);

		Assert.assertEquals(repositoryPlanets.findAll().size(), planets.size());
	}
	
	@Test
	public void deveVerficarExistenciaDeUmPlanetaPorNome() {

		Assert.assertTrue(repositoryPlanets.existsByName(planets.get(0).getName()));
	}
	
	@Test
	public void deveBuscarPlanetaPorId() {
		Planet planet = repositoryPlanets.findBy_id(planets.get(0).get_id());
		Assert.assertTrue(planet.getName().equals(planets.get(0).getName()));
		Assert.assertTrue(planet.getClimate().equals(planets.get(0).getClimate()));
		Assert.assertTrue(planet.getRegion().equals(planets.get(0).getRegion()));
	}
	
	
	@Test
	public void deveBuscarPlanetaPorNome() {
		Planet planet = repositoryPlanets.findByName(planets.get(0).getName());
		Assert.assertTrue(planet.getName().equals(planets.get(0).getName()));
		Assert.assertTrue(planet.getClimate().equals(planets.get(0).getClimate()));
		Assert.assertTrue(planet.getRegion().equals(planets.get(0).getRegion()));
	}
}
