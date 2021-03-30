package br.com.supernova.beerstock.service;

import br.com.supernova.beerstock.builder.BeerDTOBuilder;
import br.com.supernova.beerstock.dto.BeerDTO;
import br.com.supernova.beerstock.exception.BeerAlreadyRegisteredException;
import br.com.supernova.beerstock.exception.BeerNotFoundException;
import br.com.supernova.beerstock.exception.BeerStockExceededException;
import br.com.supernova.beerstock.mapper.BeerMapper;
import br.com.supernova.beerstock.model.Beer;
import br.com.supernova.beerstock.repository.BeerRepository;
import org.apiguardian.api.API;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith({MockitoExtension.class})
@API(status= API.Status.STABLE,
        since="5.0")
public class BeerServiceTest {
    private static final Long INVALID_ID = 1L;
    private BeerMapper beerMapper = BeerMapper.INSTANCE;

    @Mock
    private BeerRepository beerRepository;

    @InjectMocks
    private BeerService beerService;

    @Test
    void whenBeerInformedThenItShouldBeCreated() throws BeerAlreadyRegisteredException {
        BeerDTO inspectBeerDTO = BeerDTOBuilder.builder().build().toBeerDTO();
        Beer inspectBeer = beerMapper.toModel(inspectBeerDTO);

        // WHEN - MOCKITO
        when(beerRepository.findByName(inspectBeerDTO.getName())).thenReturn(Optional.empty());
        when(beerRepository.save(inspectBeer)).thenReturn(inspectBeer);

        // THEN
        BeerDTO createBeerDTO = beerService.createBeer(inspectBeerDTO);

        // HAMCREST - IMPORT STATIC
        assertThat(createBeerDTO.getId(), is(equalTo(inspectBeerDTO.getId())));
        assertThat(createBeerDTO.getName(), is(equalTo(inspectBeerDTO.getName())));
        assertThat(createBeerDTO.getQuantity(), is(equalTo(inspectBeerDTO.getQuantity())));

        /* JUNIT JUPITER
         * assertEquals(inspectBeerDTO.getId(), createBeerDTO.getId());
         * assertEquals(inspectBeerDTO.getName(), createBeerDTO.getName());
         */

    }

    @Test
    void whenAlreadyRegisteredBeerInformedThenAnExceptionShouldBeThrown() {
        BeerDTO inspectBeerDTO = BeerDTOBuilder.builder().build().toBeerDTO();
        Beer inspectDuplicatedBeer = beerMapper.toModel(inspectBeerDTO);

        // WHEN
        when(beerRepository.findByName(inspectBeerDTO.getName())).thenReturn(Optional.of(inspectDuplicatedBeer));

        // THEN
        assertThrows(BeerAlreadyRegisteredException.class, () -> beerService.createBeer(inspectBeerDTO));
        
    }

    @Test
    void whenANameBeerInformedThenItShouldBeReturnedBeer() throws BeerNotFoundException {
        BeerDTO inspectNamedBeerDTO = BeerDTOBuilder.builder().build().toBeerDTO();
        Beer inspectNamedBeer = beerMapper.toModel(inspectNamedBeerDTO);

        // WHEN
        when(beerRepository.findByName(inspectNamedBeer.getName())).thenReturn(Optional.of(inspectNamedBeer));

        // THEN
        BeerDTO findByNamedBeer = beerService.findByName(inspectNamedBeerDTO.getName());

        // HAMCREST
        assertThat(findByNamedBeer.getName(), is(equalTo(inspectNamedBeerDTO.getName())));

        // JUNIT JUPITER
        // assertEquals(inspectNamedBeerDTO.getName(), findByNamedBeer.getName());
    }

    @Test
    void whenAnInvalidNameBeerIsInformedThenAnExceptionItShouldBeThrown(){
        BeerDTO inspectNamedBeerDTO = BeerDTOBuilder.builder().build().toBeerDTO();

        // WHEN
        when(beerRepository.findByName(inspectNamedBeerDTO.getName())).thenReturn(Optional.empty());

        // THEN
        assertThrows(BeerNotFoundException.class, () -> beerService.findByName(inspectNamedBeerDTO.getName()));
    }

    @Test
    void whenBeerAddedToStockThenItShouldBeIncrement() throws BeerNotFoundException, BeerStockExceededException {
        BeerDTO inspectBeerQuantityDTO = BeerDTOBuilder.builder().build().toBeerDTO();
        Beer inspectBeerQuantity = beerMapper.toModel(inspectBeerQuantityDTO);

        // WHEN
        when(beerRepository.findById(inspectBeerQuantityDTO.getId())).thenReturn(Optional.of(inspectBeerQuantity));
        when(beerRepository.save(inspectBeerQuantity)).thenReturn(inspectBeerQuantity);

        int quantityToAdd = 10;
        int inspectBeerQuantityToAdd = inspectBeerQuantityDTO.getQuantity() + quantityToAdd;
        BeerDTO beerAddedToStock = beerService.addBeerStock(inspectBeerQuantityDTO.getId(), quantityToAdd);

        assertThat(inspectBeerQuantityToAdd, equalTo(beerAddedToStock.getQuantity()));
        assertThat(inspectBeerQuantityToAdd, lessThanOrEqualTo(inspectBeerQuantityDTO.getStockMax()));
    }

    @Test
    void whenBeerAddedToStockThenAnExceptionThrown() {
        BeerDTO inspectBeerQuantityDTO = BeerDTOBuilder.builder().build().toBeerDTO();
        Beer inspectBeerQuantity = beerMapper.toModel(inspectBeerQuantityDTO);

        // WHEN
        when(beerRepository.findById(inspectBeerQuantityDTO.getId())).thenReturn(Optional.of(inspectBeerQuantity));

        // THEN
        int quantityToAdd = 41;
        assertThrows(BeerStockExceededException.class, () -> beerService.addBeerStock(inspectBeerQuantityDTO.getId(), quantityToAdd));

    }

    @Test
    void whenInvalidIdOfABeerToBeAddedToStockThenAnExceptionThrown() {
        BeerDTO inspectBeerQuantityDTO = BeerDTOBuilder.builder().build().toBeerDTO();

        // WHEN
        when(beerRepository.findById(inspectBeerQuantityDTO.getId())).thenReturn(Optional.empty());

        // THEN
        int quantityToAdd = 100;
        assertThrows(BeerNotFoundException.class, () -> beerService.addBeerStock(inspectBeerQuantityDTO.getId(), quantityToAdd));

    }

    @Test
    void whenTheSumOfBeersExceedsStockThenAnExceptionThrown() {
        BeerDTO inspectBeerQuantityDTO = BeerDTOBuilder.builder().build().toBeerDTO();
        Beer inspectBeerQuantity = beerMapper.toModel(inspectBeerQuantityDTO);

        // WHEN
        when(beerRepository.findById(inspectBeerQuantityDTO.getId())).thenReturn(Optional.of(inspectBeerQuantity));

        // THEN
        int quantityToAdd = 50;
        assertThrows(BeerStockExceededException.class, () -> beerService.addBeerStock(inspectBeerQuantityDTO.getId(), quantityToAdd));

    }

    @Test
    void whenBeerIdInformedThenItShouldBeDeleted() throws BeerNotFoundException {
        BeerDTO inspectDeletedIdBeerDTO = BeerDTOBuilder.builder().build().toBeerDTO();
        Beer inspectDeletedIdBeer = beerMapper.toModel(inspectDeletedIdBeerDTO);

        // WHEN
        when(beerRepository.findById(inspectDeletedIdBeerDTO.getId())).thenReturn(Optional.of(inspectDeletedIdBeer));
        doNothing().when(beerRepository).deleteById(inspectDeletedIdBeerDTO.getId());

        // THEN
        beerService.deleteById(inspectDeletedIdBeerDTO.getId());
        verify(beerRepository, times(1)).findById(inspectDeletedIdBeerDTO.getId());
        verify(beerRepository, times(1)).deleteById(inspectDeletedIdBeerDTO.getId());

    }

    @Test
    void whenInvalidIdBeerInformedThenAnExceptionItShouldBeThrown() {
        BeerDTO inspectNamedBeerDTO = BeerDTOBuilder.builder().build().toBeerDTO();

        // WHEN
        when(beerRepository.findById(inspectNamedBeerDTO.getId())).thenReturn(Optional.empty());

        // THEN
        assertThrows(BeerNotFoundException.class, () -> beerService.deleteById(inspectNamedBeerDTO.getId()));
    }

    @Test
    void whenABeerListCalledThenItShouldBeReturned() throws BeerNotFoundException {
        BeerDTO inspectBeersListDTO = BeerDTOBuilder.builder().build().toBeerDTO();
        Beer inspectBeersList = beerMapper.toModel(inspectBeersListDTO);

        // WHEN
        when(beerRepository.findAll()).thenReturn(Collections.singletonList(inspectBeersList));

        // THEN
        List<BeerDTO> beersListReturned = beerService.listBeers();
        assertThat(beersListReturned, is(not(empty())));
        assertThat(beersListReturned.get(0), is(equalTo(inspectBeersListDTO)));

    }

    @Test
    void whenABeerListEmptyIsCalledThenBlankPageShouldBeReturned() throws BeerNotFoundException {
        // WHEN
        when(beerRepository.findAll()).thenReturn(Collections.EMPTY_LIST);

        // THEN
        List<BeerDTO> beersListReturned = beerService.listBeers();
        assertThat(beersListReturned, is(empty()));

    }

    /*
     * FIRST STEP - PERFORM TEST, BUT WITHOUT IMPLEMENTING THE METHOD
     */
    @Test
    void whenBeerIsTakenOutOfStockThenItMustBeDecremented() throws BeerNotFoundException, BeerStockExceededException {
        BeerDTO inspectDecrementedBeersDTO = BeerDTOBuilder.builder().build().toBeerDTO();
        Beer inspectDecrementedBeers = beerMapper.toModel(inspectDecrementedBeersDTO);

        /*
         * THIRD STEP - FIND THE BEER IN THE REPOSITORY USING WHEN
         * FOURTH STEP - ENSURE THAT THE AMOUNT OF BEERS TO BE REMOVED IS NOT LESS THAN ZERO,
         * ALLOWING YOU TO UPDATE YOUR STOCK
         */

        /* WHEN
         * THIRD STEP
         */
        when(beerRepository.findById(inspectDecrementedBeersDTO.getId())).thenReturn(Optional.of(inspectDecrementedBeers));
        // FOURTH STEP
        when(beerRepository.save(inspectDecrementedBeers)).thenReturn(inspectDecrementedBeers);

        int quantityToDecrement = 10;
        int quantityLimit = 0;
        int inspectBeerQuantityToDecrement = inspectDecrementedBeersDTO.getQuantity() - quantityToDecrement;

        // THEN
        BeerDTO beersToWithdrawFromStock = beerService.withdrawBeersFromStock(inspectDecrementedBeersDTO.getId(), quantityToDecrement);

        assertThat(inspectBeerQuantityToDecrement, equalTo(beersToWithdrawFromStock.getQuantity()));
        assertThat(inspectBeerQuantityToDecrement, greaterThanOrEqualTo(quantityLimit));
    }

    @Test
    void whenBeerIsTakenOutOfStockButWithInvalidIdThenAnExceptionShouldBeThrown() {
        BeerDTO inspectDecrementedBeersDTO = BeerDTOBuilder.builder().build().toBeerDTO();

        when(beerRepository.findById(inspectDecrementedBeersDTO.getId())).thenReturn(Optional.empty());

        int quantityToDecrement = 1;
        assertThrows(BeerNotFoundException.class, () -> beerService.withdrawBeersFromStock(inspectDecrementedBeersDTO.getId(), quantityToDecrement));

    }

    @Test
    void whenTheQuantityExceedsTheMinimumStockLimitThenAnExceptionShouldBeThrown() {
        BeerDTO inspectDecrementedBeersDTO = BeerDTOBuilder.builder().build().toBeerDTO();
        Beer inspectDecrementedBeers = beerMapper.toModel(inspectDecrementedBeersDTO);

        when(beerRepository.findById(inspectDecrementedBeersDTO.getId())).thenReturn(Optional.of(inspectDecrementedBeers));

        int quantityToDecrement = 11;
        assertThrows(BeerStockExceededException.class, () -> beerService.withdrawBeersFromStock(inspectDecrementedBeersDTO.getId(), quantityToDecrement));

    }
}
