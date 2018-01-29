package org.challengeplanets.controller;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;

import org.challengeplanets.builder.PlanetBuilder;
import org.challengeplanets.controller.PlanetController;
import org.challengeplanets.models.Planet;
import org.challengeplanets.repositorios.mongo.PlanetRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.HttpHeaders;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import com.google.gson.Gson;

@RunWith(SpringRunner.class)
@WebMvcTest(value = PlanetController.class, secure = false)
@ActiveProfiles("test-mongo")
@ContextConfiguration(classes = { PlanetRepository.class })
@ComponentScan("org.challengeplanets")
public class PlanetControllerTest {

	@Autowired
	private MockMvc mvc;

	@Autowired
	private MongoTemplate mongoTemplate;

	@Autowired
	private PlanetController planetController;

	private List<Planet> planets;

	@Before
	public void setup() {
		mongoTemplate.dropCollection(Planet.class);
		planets = PlanetBuilder.newPlanet().buildFivePlanets().buildAll();
		planets.stream().forEach(planet -> {
			planetController.createPlanet(planet);
		});
	}

	@Test
	public void deveCriarNovoPlaneta() throws Exception {

		HttpHeaders headers = new HttpHeaders();
		headers.add(HttpHeaders.ACCEPT, MediaTypes.HAL_JSON_VALUE);

		Planet planet = new Planet();
		planet.setName("Coruscant");
		planet.setClimate("Quente");
		planet.setRegion("Norte");
		Gson gson = new Gson();
		String json = gson.toJson(planet);

		this.mvc.perform(post("/planet/createPlanet").headers(headers).contentType(MediaTypes.HAL_JSON).content(json))
				.andExpect(content().contentTypeCompatibleWith(MediaTypes.HAL_JSON)).andExpect(status().isCreated())
				.andExpect(jsonPath("$.name").value(planet.getName()))
				.andExpect(jsonPath("$.climate").value(planet.getClimate()))
				.andExpect(jsonPath("$.region").value(planet.getRegion()))
				.andExpect(jsonPath("$.numberOfAppearancesInMovies").value(4))
				.andExpect(jsonPath("$._links.films").isNotEmpty());
	}

	@Test
	public void deveObterUmPlanetaPorNome() throws Exception {
		HttpHeaders headers = new HttpHeaders();
		headers.add(HttpHeaders.ACCEPT, MediaTypes.HAL_JSON_VALUE);

		this.mvc.perform(
				get("/planet/findPlanetAndFilmsByName").headers(headers).param("namePlanet", planets.get(0).getName()))
				.andExpect(content().contentTypeCompatibleWith(MediaTypes.HAL_JSON)).andExpect(status().isOk())
				.andExpect(jsonPath("$._id").value(planets.get(0).get_id()))
				.andExpect(jsonPath("$.name").value(planets.get(0).getName()))
				.andExpect(jsonPath("$.climate").value(planets.get(0).getClimate()))
				.andExpect(jsonPath("$.region").value(planets.get(0).getRegion()))
				.andExpect(jsonPath("$.numberOfAppearancesInMovies").value(2))
				.andExpect(jsonPath("$._links.films").isNotEmpty());
	}

	@Test
	public void naoDeveObterUmPlanetaSemNome() throws Exception {
		HttpHeaders headers = new HttpHeaders();
		headers.add(HttpHeaders.ACCEPT, MediaTypes.HAL_JSON_VALUE);

		this.mvc.perform(get("/planet/findPlanetAndFilmsByName").headers(headers).param("namePlanet", ""))
				.andExpect(status().isNotFound());
	}
	
	@Test
	public void naoDeveObterUmPlanetaComIDIncorreto() throws Exception {
		HttpHeaders headers = new HttpHeaders();
		headers.add(HttpHeaders.ACCEPT, MediaTypes.HAL_JSON_VALUE);

		this.mvc.perform(get("/planet/findPlanetById/"+"").headers(headers))
				.andExpect(status().isNotFound());
	}

	@Test
	public void deveObterUmPlanetaPorId() throws Exception {
		HttpHeaders headers = new HttpHeaders();
		headers.add(HttpHeaders.ACCEPT, MediaTypes.HAL_JSON_VALUE);

		this.mvc.perform(get("/planet/findPlanetById/"+planets.get(0).get_id()).headers(headers))
				.andExpect(content().contentTypeCompatibleWith(MediaTypes.HAL_JSON)).andExpect(status().isOk())
				.andExpect(jsonPath("$._id").value(planets.get(0).get_id()))
				.andExpect(jsonPath("$.name").value(planets.get(0).getName()))
				.andExpect(jsonPath("$.climate").value(planets.get(0).getClimate()))
				.andExpect(jsonPath("$.region").value(planets.get(0).getRegion()))
				.andExpect(jsonPath("$.numberOfAppearancesInMovies").value(2))
				.andExpect(jsonPath("$._links.films").isNotEmpty());
	}

	@Test
	public void deveObterTodosOsPlanetas() throws Exception {

		HttpHeaders headers = new HttpHeaders();
		headers.add(HttpHeaders.ACCEPT, MediaTypes.HAL_JSON_VALUE);

		this.mvc.perform(get("/planet/findAllPlanets").headers(headers).param("id", planets.get(0).get_id()))
				.andExpect(content().contentTypeCompatibleWith(MediaTypes.HAL_JSON)).andExpect(status().isOk())
				.andExpect(jsonPath("$", hasSize(5)));
	}

	@Test
	public void deveDeletarPlaneta() throws Exception {

		HttpHeaders headers = new HttpHeaders();
		headers.add(HttpHeaders.ACCEPT, MediaTypes.HAL_JSON_VALUE);
		Gson gson = new Gson();
		String json = gson.toJson(planets.get(0));

		this.mvc.perform(delete("/planet/deletePlanet").headers(headers).contentType(MediaTypes.HAL_JSON).content(json))
				.andExpect(content().contentTypeCompatibleWith(MediaTypes.HAL_JSON)).andExpect(status().isOk());

	}
}