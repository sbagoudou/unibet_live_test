package com.kindredgroup.unibetlivetest.api;

import com.kindredgroup.unibetlivetest.entity.Customer;
import com.kindredgroup.unibetlivetest.service.CustomerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Tag(name = "Customer")
@RestController
@Log4j2
@RequestMapping(Urls.BASE_PATH)
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class CustomerApi {

    public static final String UNIBEST_CUSTOMER = "unibest";

    private final CustomerService customerService;

    @Operation(summary = "Get the current customer",
            operationId = "fetchCustomer",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Customer found"),
                    @ApiResponse(responseCode = "404", description = "Customer not found")
            })
    @GetMapping(value = Urls.CURRENT_CUSTOMER,
            produces = APPLICATION_JSON_VALUE)
    public Customer fetchCustomer() {
        return customerService.findCustomerByPseudo(UNIBEST_CUSTOMER);
    }


}
