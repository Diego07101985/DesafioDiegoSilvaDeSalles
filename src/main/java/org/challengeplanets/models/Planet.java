package org.challengeplanets.models;

import java.io.Serializable;

import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.NotBlank;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;

@Data
@Document(collection = "planets")
public class Planet implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	private String _id;

	@NotBlank
	@Size(max = 100)
	@Indexed(unique = true)
	private String name;

	private String climate;

	private String region;
	
	private int numberOfAppearancesInMovies;
	
}
