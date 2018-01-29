package org.challengeplanets.repositorios.mongo;
import org.challengeplanets.models.Planet;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.transaction.annotation.Transactional;

@RepositoryRestResource(collectionResourceRel = "planet", path = "planet")
public interface PlanetRepository extends MongoRepository<Planet, String> {
		
	Planet findByName(@Param("name") String name);
	
	Planet findBy_id(@Param("id") String id);
	
    boolean existsByName(@Param("name") String name);
    
    Long deletePlanetBy_id(@Param("id")String id);         

}