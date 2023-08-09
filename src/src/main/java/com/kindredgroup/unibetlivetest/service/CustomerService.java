package com.kindredgroup.unibetlivetest.service;

import com.kindredgroup.unibetlivetest.entity.Customer;
import com.kindredgroup.unibetlivetest.exception.CustomException;
import com.kindredgroup.unibetlivetest.repository.CustomerRepository;
import com.kindredgroup.unibetlivetest.types.ExceptionType;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

import static java.lang.String.format;

@Log4j2
@Service
@RequiredArgsConstructor
public class CustomerService {

    private final CustomerRepository customerRepository;

    public Customer findCustomerByPseudo(String pseudo) {
        return customerRepository.getCustomerByPseudo(pseudo).orElseThrow(() -> new CustomException(format("customer %s not found", pseudo), ExceptionType.CUSTOMER_NOT_FOUND));
    }

    /**
     * Updates the customer's balance by subtracting the amount to bet
     * First ensure that the balance is sufficient
     *
     * @param customer the target {@link Customer}
     * @param amount   the amount to bet
     */
    public void useBalance(Customer customer, BigDecimal amount) {
        if (amount.compareTo(customer.getBalance()) > 0) {
            throw new CustomException("insufficient amount", ExceptionType.INSUFFICIENT_AMOUNT);
        }

        customer.setBalance(customer.getBalance().subtract(amount));
        customerRepository.save(customer);
    }
}
