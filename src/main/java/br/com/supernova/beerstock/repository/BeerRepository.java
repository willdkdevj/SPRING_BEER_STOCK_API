package br.com.supernova.beerstock.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.supernova.beerstock.model.Beer;

public interface BeerRepository extends JpaRepository<Beer, Long> {
	
	Optional<Beer> findByName(String name);

}
