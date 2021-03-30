package br.com.supernova.beerstock.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class BeerNotFoundException extends Exception {
	
	public BeerNotFoundException(String message) {
		super(String.format("Beer with name %s not found in the system", message));
	}
	
	public BeerNotFoundException(Long id) {
		super(String.format("Beer with ID - %s not found in the system", id));
	}

}
