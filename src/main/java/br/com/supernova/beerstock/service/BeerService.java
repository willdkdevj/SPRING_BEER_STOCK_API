package br.com.supernova.beerstock.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.supernova.beerstock.dto.BeerDTO;
import br.com.supernova.beerstock.exception.BeerAlreadyRegisteredException;
import br.com.supernova.beerstock.exception.BeerNotFoundException;
import br.com.supernova.beerstock.exception.BeerStockExceededException;
import br.com.supernova.beerstock.mapper.BeerMapper;
import br.com.supernova.beerstock.model.Beer;
import br.com.supernova.beerstock.repository.BeerRepository;

@Service
@AllArgsConstructor(onConstructor=@__(@Autowired))
public class BeerService {
	
	private final BeerRepository beerRepository;
	private final BeerMapper beerMapper = BeerMapper.INSTANCE;

	public BeerDTO createBeer(BeerDTO beerDTO) throws BeerAlreadyRegisteredException {
		checkIfThereIsaRecord(beerDTO.getName());
		Beer beer = beerMapper.toModel(beerDTO);
		Beer savedBeer = beerRepository.save(beer);
		return beerMapper.toDTO(savedBeer);
	}
	
	public BeerDTO findByName(String nameBeer) throws BeerNotFoundException {
		Beer foundBeer = beerRepository.findByName(nameBeer)
				.orElseThrow(
						() -> new BeerNotFoundException(nameBeer));
		return beerMapper.toDTO(foundBeer);
	}
	
	public BeerDTO addBeerStock(Long id, int quantity) throws BeerNotFoundException, BeerStockExceededException {
		Beer checkAmountOfBeerInTheStock = checkIfExists(id);
		int quantityAfterAdd = quantity + checkAmountOfBeerInTheStock.getQuantity();
		if(quantityAfterAdd <= checkAmountOfBeerInTheStock.getStockMax()) {
			checkAmountOfBeerInTheStock.setQuantity(quantityAfterAdd);
			Beer AddBeersInTheStock = beerRepository.save(checkAmountOfBeerInTheStock);
			return beerMapper.toDTO(AddBeersInTheStock);
		}
		throw new BeerStockExceededException(id, quantity);
	}
	
	public List<BeerDTO> listBeers() {
		return beerRepository.findAll()
				.stream()
				.map(beerMapper::toDTO)
				.collect(Collectors.toList());
	}
	
	public void deleteById(Long id) throws BeerNotFoundException {
		checkIfExists(id);
		beerRepository.deleteById(id);
	}

	public BeerDTO withdrawBeersFromStock(Long id, int quantityToDecrement) throws BeerNotFoundException, BeerStockExceededException {
		/*
		 * 	SECOND STEP - CREATED THE METHOD IN SERVICE AND IMPLEMENTED FIRST LOGIC TO PASS
		 *
		 * BeerDTO beerDTO = new BeerDTO(id, "Budweiser", "AMBEV", 80, 10, BeerTypeEnum.LAGER);
		 * beerDTO.setQuantity(beerDTO.getQuantity() - quantityToDecrement);
		 * return beerDTO;
		 */

		/*
		 * THIRD STEP - FIND THE BEER IN THE REPOSITORY USING WHEN
		 *
		 * Optional<Beer> beerLocated = beerRepository.findById(id);
		 * if(beerLocated.isPresent()){
		 *	Beer beer = beerLocated.get();
		 *	beer.setQuantity(beer.getQuantity() - quantityToDecrement);
		 *	return beerMapper.toDTO(beer);
		 * }
		 * throw new BeerNotFoundException(id);
		 */

		/*
		 * FOURTH STEP - ENSURE THAT THE AMOUNT OF BEERS TO BE REMOVED IS NOT LESS THAN ZERO,
		 * ALLOWING YOU TO UPDATE YOUR STOCK
		 */
		Beer beerLocated = checkIfExists(id);
		int quantityToBeWithdrawn = beerLocated.getQuantity() - quantityToDecrement;
		if(quantityToBeWithdrawn >= 0){
			beerLocated.setQuantity(quantityToBeWithdrawn);
			Beer beersDecremented = beerRepository.save(beerLocated);
			return beerMapper.toDTO(beersDecremented);
		}
		throw new BeerStockExceededException(id, quantityToDecrement);
	}

	public BeerDTO updateBeer(Long id, BeerDTO beerDTO) throws BeerNotFoundException {
		Beer ifExists = checkIfExists(id);
		Beer beer = beerMapper.toModel(beerDTO);
		beer.setId(ifExists.getId());
		Beer savedBeer = beerRepository.save(beer);
		return beerMapper.toDTO(savedBeer);
	}
	
	private void checkIfThereIsaRecord(String nameBeer) throws BeerAlreadyRegisteredException {
		Optional<Beer> optSavedBeer = beerRepository.findByName(nameBeer);
		if(optSavedBeer.isPresent()){
			throw new BeerAlreadyRegisteredException(nameBeer);
		}
	}
	
	private Beer checkIfExists(Long id) throws BeerNotFoundException {
		return beerRepository.findById(id).orElseThrow(
				() -> new BeerNotFoundException(id));
	}


}
