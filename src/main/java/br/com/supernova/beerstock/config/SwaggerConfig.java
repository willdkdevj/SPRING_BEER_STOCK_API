package br.com.supernova.beerstock.config;

import com.google.common.base.Predicate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import springfox.documentation.RequestHandler;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
public class SwaggerConfig {
	
	private static final String API_TITLE = "Beer Stock API";
	private static final String API_DESCRIPTION = "Implementation of an API to map transactions between the database and the application";
	private static final String CONTACT_NAME = "William Derek Dias";
	private static final String CONTACT_GITHUB = "https://github.com/willdkdevj";
	private static final String CONTACT_EMAIL = "williamdkdevops@gmail.com";
	
	@Bean
	public Docket api() {
		return new Docket(DocumentationType.SWAGGER_2)
				.select()
				.apis(apis()) // RequestHandlerSelectors.any()
				.paths(PathSelectors.any())
				.build()
				.apiInfo(constructorApiInfo());
	}

	private Predicate<RequestHandler> apis(){
		return RequestHandlerSelectors.basePackage("br.com.supernova.beerstock");
	}

	public ApiInfo constructorApiInfo() {
		return new ApiInfoBuilder()
				.title(API_TITLE)
				.description(API_DESCRIPTION)
				.version("1.0.0")
				.contact(new Contact(CONTACT_NAME, CONTACT_GITHUB, CONTACT_EMAIL))
				.build();		
	}
	

}
