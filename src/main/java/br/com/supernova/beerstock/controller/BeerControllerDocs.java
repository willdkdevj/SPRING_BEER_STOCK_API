package br.com.supernova.beerstock.controller;

import java.util.List;

import org.springframework.web.bind.annotation.PathVariable;

import br.com.supernova.beerstock.dto.BeerDTO;
import br.com.supernova.beerstock.exception.BeerAlreadyRegisteredException;
import br.com.supernova.beerstock.exception.BeerNotFoundException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@Api("Beer Stock Manager Interface")
public interface BeerControllerDocs {
	
	@ApiOperation(value = "Operation for beer creation")
	@ApiResponses(value = {
			@ApiResponse(code = 201, message = "Beer successfully created"),
			@ApiResponse(code = 400, message = "It was not possible to register the informed beer")
	})
	BeerDTO createBeer(BeerDTO beerDTO) throws BeerAlreadyRegisteredException;
	
	@ApiOperation(value = "Operation to locate beer")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Beer found successfully"),
			@ApiResponse(code = 404, message = "Could not find beer reported")
	})
	BeerDTO findByName(@PathVariable String name) throws BeerNotFoundException;
	
	@ApiOperation(value = "Operation to exclude beer")
	@ApiResponses(value = {
			@ApiResponse(code = 204, message = "Beer registration successfully deleted"),
			@ApiResponse(code = 404, message = "Could not find the beer registration for deletion")
	})
	void deleteById(@PathVariable Long id) throws BeerNotFoundException;

	@ApiOperation(value = "Operation to list beers in stock")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Beer catalog successfully returned")
	})
	List<BeerDTO> listBeers();

	@ApiOperation(value = "Operation for beer update")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Beer successfully updated"),
			@ApiResponse(code = 404, message = "Could not find the beer reported to update")
	})
	BeerDTO updateBeer(Long id, BeerDTO beerDTO) throws BeerNotFoundException;
}
