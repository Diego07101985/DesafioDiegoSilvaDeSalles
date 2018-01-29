package org.challengeplanets.builder;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import org.challengeplanets.models.Planet;

public class PlanetBuilder {

	private List<Planet> planets = new ArrayList<Planet>();
	
	private List<String> namePlanets = Arrays.asList("Alderaan","Dagobah","Bespin","Endor","Naboo");

	private PlanetBuilder(Planet planet) {
		planets.add(planet);
	}

	private PlanetBuilder() {

	}

	public static PlanetBuilder newPlanet() {
		return new PlanetBuilder();
	}

	public static PlanetBuilder newPlanet(String name, String climate, String region) {
		Planet planet = create(name, climate, region);
		return new PlanetBuilder(planet);
	}

	private static Planet create(String name, String climate, String region) {
		Planet planet = new Planet();
		planet.setName(name);
		planet.setClimate(climate);
		planet.setRegion(region);
	
		return planet;
	}

	public PlanetBuilder buildFivePlanets() {
		for (int i = 0; i < 5; i++) {
			planets.add(create(namePlanets.get(i),"Quente","Regiao"));
		}
		return this;
	}

	public Planet buildOne() {
		return planets.get(0);
	}

	public List<Planet> buildAll() {
		return planets;
	}
}