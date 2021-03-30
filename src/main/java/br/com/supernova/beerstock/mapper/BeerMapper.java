package br.com.supernova.beerstock.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import br.com.supernova.beerstock.dto.BeerDTO;
import br.com.supernova.beerstock.model.Beer;

@Mapper
public interface BeerMapper {

	BeerMapper INSTANCE = Mappers.getMapper(BeerMapper.class);
	
	Beer toModel(BeerDTO beerDTO);
	
	BeerDTO toDTO(Beer beer);
}
