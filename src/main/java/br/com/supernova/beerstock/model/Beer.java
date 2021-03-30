package br.com.supernova.beerstock.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import br.com.supernova.beerstock.enums.BeerTypeEnum;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class Beer implements Serializable {
	
	@Id
	@GeneratedValue
	@ApiModelProperty(notes = "Unique entity identifier", required = true)
	private Long id;
	
	@Column(nullable = false, unique = true)
	@ApiModelProperty(notes = "Unique value where it cannot be null", required = true)
	private String name;
	
	@Column(nullable = false)
	@ApiModelProperty(notes = "Value cannot be null", required = true)
	private String brand;
	
	@Column(nullable = false)
	@ApiModelProperty(notes = "Value cannot be null", required = true)
	private int stockMax;
	
	@Column(nullable = false)
	@ApiModelProperty(notes = "Value cannot be null", required = true)
	private int quantity;
	
	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	@ApiModelProperty(notes = "Value cannot be null", required = true)
	private BeerTypeEnum type;

}
