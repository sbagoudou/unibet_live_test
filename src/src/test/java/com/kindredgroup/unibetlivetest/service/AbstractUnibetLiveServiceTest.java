package com.kindredgroup.unibetlivetest.service;

import com.kindredgroup.unibetlivetest.entity.Bet;
import com.kindredgroup.unibetlivetest.entity.Customer;
import com.kindredgroup.unibetlivetest.entity.Market;
import com.kindredgroup.unibetlivetest.entity.Selection;
import com.kindredgroup.unibetlivetest.types.SelectionResult;
import com.kindredgroup.unibetlivetest.types.SelectionState;

import java.math.BigDecimal;
import java.util.List;

public abstract class AbstractUnibetLiveServiceTest {

    protected Customer customer() {
        var customer = new Customer();
        customer.setBalance(BigDecimal.TEN);
        return customer;
    }

    protected Selection selection(SelectionState state, SelectionResult selectionResult) {
        var selection = new Selection();
        selection.setId(1L);
        selection.setCurrentOdd(BigDecimal.ONE);
        selection.setState(state);
        selection.setResult(selectionResult);
        return selection;
    }

    protected Bet bet(SelectionState selectionState, SelectionResult selectionResult) {
        var bet = new Bet();
        bet.setAmount(BigDecimal.TEN);
        bet.setSelection(selection(selectionState, selectionResult));
        bet.setCustomer(customer());
        return bet;
    }

    protected Market market(SelectionState selectionState) {
        var market = new Market();
        market.setSelections(List.of(selection(selectionState, null)));
        return market;
    }
}
