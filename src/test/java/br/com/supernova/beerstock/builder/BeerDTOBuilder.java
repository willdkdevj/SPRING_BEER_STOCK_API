package br.com.supernova.beerstock.builder;

import br.com.supernova.beerstock.dto.BeerDTO;
import br.com.supernova.beerstock.enums.BeerTypeEnum;
import lombok.Builder;

@Builder
public class BeerDTOBuilder {
	
	@Builder.Default
	private final Long id = 1L;
	
	@Builder.Default
	private final String name = "Heinecken";
	
	@Builder.Default
	private final String brand = "FEMSA";
	
	@Builder.Default
	private final Integer stockMax = 50;
	
	@Builder.Default
	private final Integer quantity = 10;
	
	@Builder.Default
	private final BeerTypeEnum type = BeerTypeEnum.LAGER;
	
	public BeerDTO toBeerDTO() {
		return new BeerDTO(id, name, brand, stockMax, quantity, type);
	}
}

