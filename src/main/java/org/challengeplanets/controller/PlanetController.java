package org.challengeplanets.controller;

import java.util.ArrayList;
import java.util.List;

import javax.validation.Valid;

import org.challengeplanets.dto.PlanetDTO;
import org.challengeplanets.models.Planet;
import org.challengeplanets.repositorios.mongo.PlanetRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.MediaTypes;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.config.EnableHypermediaSupport;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
@RequestMapping(value = "/planet")
@EnableHypermediaSupport(type = EnableHypermediaSupport.HypermediaType.HAL)
public class PlanetController {

	private static final String URL = "https://swapi.co/api/planets/?search=";

	@Autowired
	private RestTemplate restTemplate;

	@Autowired
	private PlanetRepository planetRepository;
	

	@RequestMapping(path = "/findPlanetAndFilmsByName", method = RequestMethod.GET, produces = MediaTypes.HAL_JSON_VALUE)
	@ResponseBody
	public ResponseEntity<Resource<Planet>> findPlanetForName(@RequestParam("namePlanet") String namePlanet) {
		
		Planet planet = planetRepository.findByName(namePlanet);
		if(planet==null) {
			return new ResponseEntity<Resource<Planet>>(HttpStatus.NOT_FOUND);
		}
		List<Link> links = addHateoasLinks(restTemplate.getForObject(URL + planet.getName(), PlanetDTO.class));
		return ResponseEntity.ok(new Resource<Planet>(planet, links));
	}
	
	@RequestMapping(path = "/findPlanetById/{id}", method = RequestMethod.GET, produces = MediaTypes.HAL_JSON_VALUE)
	@ResponseBody
	public ResponseEntity<Resource<Planet>> findPlanetById(@PathVariable String id) {
		Planet planet = planetRepository.findBy_id(id);
		if(planet==null) {
			return new ResponseEntity<Resource<Planet>>(HttpStatus.NOT_FOUND);
		}
		List<Link> links = addHateoasLinks(restTemplate.getForObject(URL + planet.getName(), PlanetDTO.class));
		return ResponseEntity.ok(new Resource<Planet>(planet, links));
	}
	
	
	@RequestMapping(path = "/findAllPlanets", method = RequestMethod.GET, produces = MediaTypes.HAL_JSON_VALUE)
	@ResponseBody
	public ResponseEntity<List<Resource<Planet>>> findAllPlanets() {
		List<Planet> planets = planetRepository.findAll();
		List<Resource<Planet>> listaResourcePlanets = new ArrayList<Resource<Planet>>();
		planets.forEach( planet ->{
			List<Link> links =  addHateoasLinks(restTemplate.getForObject(URL + planet.getName(), PlanetDTO.class));
			Resource<Planet> resourcePlanet = new Resource<Planet>(planet,links);
			listaResourcePlanets.add(resourcePlanet);
		});
		
		return ResponseEntity.ok(listaResourcePlanets);
	}


	@RequestMapping(value = "/createPlanet", method = { RequestMethod.POST}, produces = MediaTypes.HAL_JSON_VALUE)
	public ResponseEntity<Resource<Planet>> createPlanet(@Valid @RequestBody Planet planet) {
		if (planetRepository.existsByName(planet.getName())) {
			return new ResponseEntity<Resource<Planet>>(HttpStatus.CONFLICT);
		}
		List<Link> links = addHateoasLinks(restTemplate.getForObject(URL + planet.getName(), PlanetDTO.class));
		planet.setNumberOfAppearancesInMovies(links.size());
		planetRepository.save(planet);

		return new ResponseEntity<Resource<Planet>>(new Resource<Planet>(planet, links),HttpStatus.CREATED);
	}
	
	
	@RequestMapping(value = "/deletePlanet", method = { RequestMethod.DELETE}, produces = MediaTypes.HAL_JSON_VALUE)
	public ResponseEntity<Resource<Planet>> deletePlanet(@Valid @RequestBody Planet planet) {
		if (!planetRepository.existsByName(planet.getName())) {
			return new ResponseEntity<Resource<Planet>>(HttpStatus.NOT_FOUND);
		}
		planetRepository.deletePlanetBy_id(planet.get_id());

		return new ResponseEntity<Resource<Planet>>(new Resource<Planet>(planet),HttpStatus.OK);
	}
	
	@RequestMapping(value = "/updatePlanet", method = {RequestMethod.PUT}, produces = MediaTypes.HAL_JSON_VALUE)
	public ResponseEntity<Resource<Planet>> updatePlanet(@Valid @RequestBody Planet planet) {
		if (!planetRepository.existsByName(planet.getName())) {
			return new ResponseEntity<Resource<Planet>>(HttpStatus.NOT_FOUND);
		}
		
		List<Link> links = addHateoasLinks(restTemplate.getForObject(URL + planet.getName(), PlanetDTO.class));
		planet.setNumberOfAppearancesInMovies(links.size());
		planetRepository.save(planet);

		return ResponseEntity.ok(new Resource<Planet>(planet, links));
	}

	public List<Link> addHateoasLinks(PlanetDTO planetDTO) {
		List<Link> links = new ArrayList<Link>();
		planetDTO.getResults().forEach(result -> {
			result.getFilms().forEach(film -> {
				links.add(new Link(film, "films"));
			});
		});
		return links;
	}

}
