package br.com.supernova.beerstock.util;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

public class JsonAsString {

	public static String conversionToString(Object bookDTO) {
		try {
			ObjectMapper objMapper = new ObjectMapper();
			objMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
			objMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
			objMapper.registerModule(new JavaTimeModule());
			
			return objMapper.writeValueAsString(bookDTO);
			
		} catch(Exception e) {
			throw new RuntimeException(e);
		}
	}
}
