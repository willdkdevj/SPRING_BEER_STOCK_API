package br.com.supernova.beerstock.controller;

import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import br.com.supernova.beerstock.dto.BeerDTO;
import br.com.supernova.beerstock.dto.QuantityDTO;
import br.com.supernova.beerstock.service.BeerService;

import lombok.AllArgsConstructor;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.HttpStatus;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import br.com.supernova.beerstock.exception.BeerAlreadyRegisteredException;
import br.com.supernova.beerstock.exception.BeerNotFoundException;
import br.com.supernova.beerstock.exception.BeerStockExceededException;

@RestController
@RequestMapping("/api/v1/beers")
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class BeerController implements BeerControllerDocs {

	private final BeerService beerService;
	
	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public BeerDTO createBeer(@RequestBody @Valid BeerDTO beerDTO) throws BeerAlreadyRegisteredException {
		return beerService.createBeer(beerDTO);
	}
	
	@GetMapping("/{nameBeer}")
	public BeerDTO findByName(@PathVariable String nameBeer) throws BeerNotFoundException {
		return beerService.findByName(nameBeer);		
	}

	@PatchMapping("/{id}/add")
	public BeerDTO addBeerStock(@PathVariable Long id,@RequestBody @Valid QuantityDTO quantity) throws BeerNotFoundException, BeerStockExceededException {
		return beerService.addBeerStock(id, quantity.getQuantity());
	}

	@PatchMapping("/{id}/subtract")
	public BeerDTO withdrawBeersFromStock(@PathVariable Long id,@RequestBody @Valid QuantityDTO quantity) throws BeerNotFoundException, BeerStockExceededException {
		return beerService.withdrawBeersFromStock(id, quantity.getQuantity());
	}

	@GetMapping
	public List<BeerDTO> listBeers() {
		return beerService.listBeers();
	}

	@DeleteMapping("/{id}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void deleteById(@PathVariable Long id) throws BeerNotFoundException {
		beerService.deleteById(id);
	}

}
