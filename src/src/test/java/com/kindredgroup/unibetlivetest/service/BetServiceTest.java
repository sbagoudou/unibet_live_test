package com.kindredgroup.unibetlivetest.service;


import com.kindredgroup.unibetlivetest.entity.Bet;
import com.kindredgroup.unibetlivetest.entity.Customer;
import com.kindredgroup.unibetlivetest.repository.BetRepository;
import com.kindredgroup.unibetlivetest.repository.CustomerRepository;
import com.kindredgroup.unibetlivetest.types.BetState;
import com.kindredgroup.unibetlivetest.types.SelectionResult;
import com.kindredgroup.unibetlivetest.types.SelectionState;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest
class BetServiceTest extends AbstractUnibetLiveServiceTest {

    private BetService service;

    @Mock
    private BetRepository repository;
    @Mock
    private CustomerRepository customerRepository;
    @Captor
    private ArgumentCaptor<Bet> betCaptor;
    @Captor
    private ArgumentCaptor<Customer> customerCaptor;

    @BeforeEach
    void setUp() {
        service = new BetService(repository, customerRepository);
    }

    @Test
    void closeBetsTest() {
        when(repository.findAll()).thenReturn(List.of(bet(SelectionState.CLOSED, SelectionResult.WON)));

        var result = service.closeBets();

        verify(repository).save(betCaptor.capture());
        verify(customerRepository).save(customerCaptor.capture());

        assertAll(
                () -> assertThat(result, equalTo(1)),
                () -> assertThat(betCaptor.getValue().getBetState(), equalTo(BetState.WON)),
                () -> assertThat(customerCaptor.getValue().getBalance(), equalTo(BigDecimal.valueOf(20)))
        );
    }

    @Test
    void closeBetsTest_lost() {
        when(repository.findAll()).thenReturn(List.of(bet(SelectionState.CLOSED, SelectionResult.LOST)));

        var result = service.closeBets();

        verify(repository).save(betCaptor.capture());
        verify(customerRepository, never()).save(any());

        assertAll(
                () -> assertThat(result, equalTo(1)),
                () -> assertThat(betCaptor.getValue().getBetState(), equalTo(BetState.LOST))
        );
    }

    @Test
    void closeBetsTest_selectionNotClosed() {
        when(repository.findAll()).thenReturn(List.of(bet(SelectionState.OPENED, null)));

        var result = service.closeBets();

        verify(repository, never()).save(any());
        verify(customerRepository, never()).save(any());

        assertThat(result, equalTo(0));
    }

}
