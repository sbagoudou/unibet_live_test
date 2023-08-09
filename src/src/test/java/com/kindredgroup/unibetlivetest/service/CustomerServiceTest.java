package com.kindredgroup.unibetlivetest.service;


import com.kindredgroup.unibetlivetest.entity.Customer;
import com.kindredgroup.unibetlivetest.exception.CustomException;
import com.kindredgroup.unibetlivetest.repository.CustomerRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest
class CustomerServiceTest extends AbstractUnibetLiveServiceTest {

    private CustomerService service;

    @Mock
    private CustomerRepository repository;

    @BeforeEach
    void setUp() {
        service = new CustomerService(repository);
    }

    @Test
    void useBalanceTest() {
        when(repository.save(any(Customer.class))).thenReturn(new Customer());
        service.useBalance(customer(), BigDecimal.ONE);
        verify(repository).save(any(Customer.class));
    }

    @Test
    void useBalanceTest_sameAmountAsBalance() {
        when(repository.save(any(Customer.class))).thenReturn(new Customer());
        service.useBalance(customer(), BigDecimal.TEN);
        verify(repository).save(any(Customer.class));
    }

    @Test
    void useBalanceTest_insufficientBalance() {
        assertThrows(CustomException.class, () -> service.useBalance(customer(), BigDecimal.valueOf(11)));
    }

}
