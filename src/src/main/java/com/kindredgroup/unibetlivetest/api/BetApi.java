package com.kindredgroup.unibetlivetest.api;

import com.kindredgroup.unibetlivetest.dto.BetInput;
import com.kindredgroup.unibetlivetest.entity.Bet;
import com.kindredgroup.unibetlivetest.service.BetService;
import com.kindredgroup.unibetlivetest.service.CustomerService;
import com.kindredgroup.unibetlivetest.service.SelectionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static com.kindredgroup.unibetlivetest.api.CustomerApi.UNIBEST_CUSTOMER;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Tag(name = "Bet")
@RestController
@RequestMapping(Urls.BASE_PATH)
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class BetApi {

    private final CustomerService customerService;
    private final SelectionService selectionService;
    private final BetService betService;

    @Operation(summary = "List all bets from database",
            operationId = "listBets",
            responses = {
                    @ApiResponse(responseCode = "200", description = "ok"),
                    @ApiResponse(responseCode = "204", description = "No live"),
                    @ApiResponse(responseCode = "400", description = "Bad Request"),
                    @ApiResponse(responseCode = "500", description = "Internal server error")
            })
    @GetMapping(value = Urls.BETS,
            produces = APPLICATION_JSON_VALUE)
    public List<Bet> listBets() {
        return betService.findAll();
    }

    @PostMapping(Urls.ADD_BET)
    @Operation(summary = "Submit a bet",
            operationId = "addBet",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Bet submitted"),
                    @ApiResponse(responseCode = "400", description = "Bad request"),
                    @ApiResponse(responseCode = "409", description = "Conflict, ongoing bet"),
                    @ApiResponse(responseCode = "500", description = "Internal server error"),
                    @ApiResponse(responseCode = "600", description = "Insufficient amount"),
                    @ApiResponse(responseCode = "601", description = "Odd changed"),
                    @ApiResponse(responseCode = "602", description = "Selection is closed")
            })
    @ResponseStatus(HttpStatus.OK)
    @Transactional
    public void addBet(@RequestBody @Valid BetInput input) {
        var customer = customerService.findCustomerByPseudo(UNIBEST_CUSTOMER);
        var amount = input.getAmount();
        customerService.useBalance(customer, amount);

        var selection = selectionService.get(input.getSelectionId());
        selectionService.assertReady(selection, input.getOdd(), customer.getBets());

        betService.addBet(customer, selection, input.getAmount());
    }

}
