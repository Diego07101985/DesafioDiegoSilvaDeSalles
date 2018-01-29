
Esse projeto foi construido usando o framework Spring Boot


O Spring Boot inicia executando  Application
Esse projeto usa alem de Spring Boot o Spring Test
O teste é clean executa obtem o resultado e limpa o database ele esta configurado
 com profile test que pode ser visto no MongoConfigurationTest.java

Configuração do database do mongo 

O Sitema roda na porta 9000

Busca Planeta Por nome
http://localhost:9000/planet/findPlanetAndFilmsByName?namePlanet={nomePlaneta}
Busca Planeta Por Id
http://localhost:9000/planet/findPlanetById/{id}
Busca Por todos os planetas
http://localhost:9000/planet/findAllPlanets
Deletar Planeta
http://localhost:9000/planet/deletePlanet
Body
    {
        "_id": {devePossuirOIdDoPlaneta},
        "name": "",
        "climate": "",
        "region": "",
        "numberOfAppearancesInMovies": 0
        
    }
 Criar Planeta  
 http://localhost:9000/planet/createPlanet
 Body
    {
      "name": "Hoth",
  	  "climate": "quente",
    	  "region": "norte",
    }
    

Eu mostrei os ids por conveniencia
comentando o codigo abaixo que fica no RestConfiguration  test-mongo
	@Override
    public RepositoryRestConfiguration config() {
        RepositoryRestConfiguration config = super.config();
        config.exposeIdsFor(Planet.class);
        return config; 
    }
    





