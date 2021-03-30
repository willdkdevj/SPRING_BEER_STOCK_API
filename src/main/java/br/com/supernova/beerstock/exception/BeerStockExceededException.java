package br.com.supernova.beerstock.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class BeerStockExceededException extends Exception {
	
	public BeerStockExceededException(Long id, int quantity) {
		super(String.format("It's not possible to add the amount of %s beers to ID - %s. Maximum stock exceeded!", quantity, id));
	}

}
