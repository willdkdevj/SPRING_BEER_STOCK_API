package br.com.supernova.beerstock.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class BeerAlreadyRegisteredException extends Exception {
	
	public BeerAlreadyRegisteredException(String message) {
		super(String.format("Beer with name %s already registered in the system", message));
	}
}
