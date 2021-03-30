package br.com.supernova.beerstock.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum BeerTypeEnum {
	LAGER("Lager"),
	MALZBIER("Malzbier"),
	WITBIER("Witbier"),
	PILSEN("Pilsen"),
	ALE("Ale"),
	IPA("Ipa"),
	STOUT("Stout");

	private final String description;
}
