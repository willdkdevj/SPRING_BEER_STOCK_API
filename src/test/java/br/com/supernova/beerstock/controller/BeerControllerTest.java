package br.com.supernova.beerstock.controller;

import br.com.supernova.beerstock.builder.BeerDTOBuilder;
import br.com.supernova.beerstock.dto.BeerDTO;
import br.com.supernova.beerstock.dto.QuantityDTO;
import br.com.supernova.beerstock.exception.BeerNotFoundException;
import br.com.supernova.beerstock.exception.BeerStockExceededException;
import br.com.supernova.beerstock.service.BeerService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.servlet.view.json.MappingJackson2JsonView;

import java.util.Collections;

import static br.com.supernova.beerstock.util.JsonAsString.*;
import static org.hamcrest.core.Is.is;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
public class BeerControllerTest {

    private static final String BEER_API_URL_PATH = "/api/v1/beers";
    private static final Long VALID_BEER_ID = 1L;
    private static final Long INVALID_BEER_ID = 2L;
    private static final String BEER_API_SUBPATH_INCREMENT_URL = "/add";
    private static final String BEER_API_SUBPATH_DECREMENT_URL = "/subtract";

    private MockMvc mockMvc;

    @Mock
    private BeerService beerService;

    @InjectMocks
    private BeerController beerController;

    /*
     * Before each test the Mock MVC object is created
     */
    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(beerController)
                .setCustomArgumentResolvers(new PageableHandlerMethodArgumentResolver())
                .setViewResolvers((s, locale) -> new MappingJackson2JsonView())
                .build();
    }

    @Test
    void whenPOSTIsCalledThenABeerIsCreated() throws Exception {
        BeerDTO beerDTO =BeerDTOBuilder.builder().build().toBeerDTO();

        // WHEN
        when(beerService.createBeer(beerDTO)).thenReturn(beerDTO);

        // THEN
        mockMvc.perform(post(BEER_API_URL_PATH)
                .contentType(MediaType.APPLICATION_JSON)
                .content(conversionToString(beerDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name", is(beerDTO.getName())))
                .andExpect(jsonPath("$.brand", is(beerDTO.getBrand())))
                .andExpect(jsonPath("$.stockMax", is(beerDTO.getStockMax())))
                .andExpect(jsonPath("$.quantity", is(beerDTO.getQuantity())))
                .andExpect(jsonPath("$.type", is(beerDTO.getType().toString())));
    }

    @Test
    void whenPOSTIsCalledThenAnExceptionThrown() throws Exception {
        BeerDTO beerDTO =BeerDTOBuilder.builder().build().toBeerDTO();
        beerDTO.setBrand(null);

        // THEN
        mockMvc.perform(post(BEER_API_URL_PATH)
                .contentType(MediaType.APPLICATION_JSON)
                .content(conversionToString(beerDTO)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void whenGETIsCalledThenABeerIsReturned() throws Exception {
        BeerDTO beerNameReturnedDTO = BeerDTOBuilder.builder().build().toBeerDTO();

        // WHEN
        when(beerService.findByName(beerNameReturnedDTO.getName())).thenReturn(beerNameReturnedDTO);

        // THEN
        mockMvc.perform(get(BEER_API_URL_PATH + "/" + beerNameReturnedDTO.getName())
                .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.name", is(beerNameReturnedDTO.getName())))
                    .andExpect(jsonPath("$.brand", is(beerNameReturnedDTO.getBrand())))
                    .andExpect(jsonPath("$.stockMax", is(beerNameReturnedDTO.getStockMax())))
                    .andExpect(jsonPath("$.quantity", is(beerNameReturnedDTO.getQuantity())))
                    .andExpect(jsonPath("$.type", is(beerNameReturnedDTO.getType().toString())));

    }

    @Test
    void whenGETIsCalledThenAnNotFoundExceptionIsThrown() throws Exception {
        BeerDTO beerNameReturnedDTO = BeerDTOBuilder.builder().build().toBeerDTO();

        // WHEN
        when(beerService.findByName(beerNameReturnedDTO.getName())).thenThrow(BeerNotFoundException.class);

        // THEN
        mockMvc.perform(get(BEER_API_URL_PATH + "/" + beerNameReturnedDTO.getName())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());

    }

    @Test
    void whenGETBeersListIsCalledThenOKStatusIsReturned() throws Exception {
        BeerDTO beerNameReturnedDTO = BeerDTOBuilder.builder().build().toBeerDTO();

        // WHEN
        when(beerService.listBeers()).thenReturn(Collections.singletonList(beerNameReturnedDTO));

        // THEN
        mockMvc.perform(get(BEER_API_URL_PATH)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name", is(beerNameReturnedDTO.getName())))
                .andExpect(jsonPath("$[0].brand", is(beerNameReturnedDTO.getBrand())))
                .andExpect(jsonPath("$[0].stockMax", is(beerNameReturnedDTO.getStockMax())))
                .andExpect(jsonPath("$[0].quantity", is(beerNameReturnedDTO.getQuantity())))
                .andExpect(jsonPath("$[0].type", is(beerNameReturnedDTO.getType().toString())));

    }

    @Test
    void whenDeleteIsCalledThenNoContentStatusIsReturned() throws Exception {
        BeerDTO beerIDDeletedDTO = BeerDTOBuilder.builder().build().toBeerDTO();

        // WHEN
        doNothing().when(beerService).deleteById(beerIDDeletedDTO.getId());

        // THEN
        mockMvc.perform(delete(BEER_API_URL_PATH + "/" + beerIDDeletedDTO.getId())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

    }

    @Test
    void whenAInvalidIdDeleteIsCalledThenNotFoundStatusIsReturned() throws Exception {
        // WHEN
        doThrow(BeerNotFoundException.class).when(beerService).deleteById(INVALID_BEER_ID);

        // THEN
        mockMvc.perform(delete(BEER_API_URL_PATH + "/" + INVALID_BEER_ID)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());

    }

    @Test
    void whenPathIsCalledToAddBeersThenOKStatusIsReturned() throws Exception {
        QuantityDTO quantityDTO = QuantityDTO.builder()
                .quantity(10)
                .build();
        BeerDTO beerDTO = BeerDTOBuilder.builder().build().toBeerDTO();
        beerDTO.setQuantity(beerDTO.getQuantity() + quantityDTO.getQuantity());

        // WHEN
        when(beerService.addBeerStock(VALID_BEER_ID, quantityDTO.getQuantity())).thenReturn(beerDTO);

        // THEN
        mockMvc.perform(patch(BEER_API_URL_PATH + "/" + VALID_BEER_ID + BEER_API_SUBPATH_INCREMENT_URL)
            .contentType(MediaType.APPLICATION_JSON)
                .content(conversionToString(quantityDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is(beerDTO.getName())))
                .andExpect(jsonPath("$.brand", is(beerDTO.getBrand())))
                .andExpect(jsonPath("$.stockMax", is(beerDTO.getStockMax())))
                .andExpect(jsonPath("$.quantity", is(beerDTO.getQuantity())))
                .andExpect(jsonPath("$.type", is(beerDTO.getType().toString())));
    }

    @Test
    void whenPathIsCalledWithTheAmountOfBeersExceededThenBadRequestStatusIsReturned() throws Exception {
        QuantityDTO quantityDTO = QuantityDTO.builder().quantity(100).build();
        BeerDTO beerDTO = BeerDTOBuilder.builder().build().toBeerDTO();
        beerDTO.setQuantity(beerDTO.getQuantity() + quantityDTO.getQuantity());

        // WHEN
        when(beerService.addBeerStock(VALID_BEER_ID, quantityDTO.getQuantity())).thenThrow(BeerStockExceededException.class);

        // THEN
        mockMvc.perform(patch(BEER_API_URL_PATH + "/" + VALID_BEER_ID + BEER_API_SUBPATH_INCREMENT_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(conversionToString(quantityDTO)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void whenPathIsCalledWithInvalidIdBeersExceededThenNotFoundStatusIsReturned() throws Exception {
        QuantityDTO quantityDTO = QuantityDTO.builder().quantity(10).build();

        // WHEN
        when(beerService.addBeerStock(INVALID_BEER_ID, quantityDTO.getQuantity())).thenThrow(BeerNotFoundException.class);

        // THEN
        mockMvc.perform(patch(BEER_API_URL_PATH + "/" + INVALID_BEER_ID + BEER_API_SUBPATH_INCREMENT_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(conversionToString(quantityDTO)))
                .andExpect(status().isNotFound());
    }

    @Test
    void whenPathIsCalledToSubtractBeersThenOKStatusIsReturned() throws Exception {
        QuantityDTO quantityDTO = QuantityDTO.builder()
                .quantity(10)
                .build();
        BeerDTO beerDTO = BeerDTOBuilder.builder().build().toBeerDTO();
        beerDTO.setQuantity(beerDTO.getQuantity() - quantityDTO.getQuantity());

        // WHEN
        when(beerService.withdrawBeersFromStock(VALID_BEER_ID, quantityDTO.getQuantity())).thenReturn(beerDTO);

        // THEN
        mockMvc.perform(patch(BEER_API_URL_PATH + "/" + VALID_BEER_ID + BEER_API_SUBPATH_DECREMENT_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(conversionToString(quantityDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is(beerDTO.getName())))
                .andExpect(jsonPath("$.brand", is(beerDTO.getBrand())))
                .andExpect(jsonPath("$.stockMax", is(beerDTO.getStockMax())))
                .andExpect(jsonPath("$.quantity", is(beerDTO.getQuantity())))
                .andExpect(jsonPath("$.type", is(beerDTO.getType().toString())));
    }

    @Test
    void whenPathIsCalledWithTheAmountOfBeersBelowTheStockThenBadRequestStatusIsReturned() throws Exception {
        QuantityDTO quantityDTO = QuantityDTO.builder().quantity(100).build();
        BeerDTO beerDTO = BeerDTOBuilder.builder().build().toBeerDTO();
        beerDTO.setQuantity(beerDTO.getQuantity() + quantityDTO.getQuantity());

        // WHEN
        when(beerService.withdrawBeersFromStock(VALID_BEER_ID, quantityDTO.getQuantity())).thenThrow(BeerStockExceededException.class);

        // THEN
        mockMvc.perform(patch(BEER_API_URL_PATH + "/" + VALID_BEER_ID + BEER_API_SUBPATH_DECREMENT_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(conversionToString(quantityDTO)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void whenPathIsCalledWithAnInvalidIDBeersSubtractThenNotFoundIsReturned() throws Exception {
        QuantityDTO quantityDTO = QuantityDTO.builder().quantity(100).build();
        BeerDTO beerDTO = BeerDTOBuilder.builder().build().toBeerDTO();
        beerDTO.setQuantity(beerDTO.getQuantity() + quantityDTO.getQuantity());

        // WHEN
        when(beerService.withdrawBeersFromStock(INVALID_BEER_ID, quantityDTO.getQuantity())).thenThrow(BeerNotFoundException.class);

        // THEN
        mockMvc.perform(patch(BEER_API_URL_PATH + "/" + INVALID_BEER_ID + BEER_API_SUBPATH_DECREMENT_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(conversionToString(quantityDTO)))
                .andExpect(status().isNotFound());
    }
}
