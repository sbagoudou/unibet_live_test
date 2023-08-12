package com.kindredgroup.unibetlivetest.service;

import com.kindredgroup.unibetlivetest.entity.Bet;
import com.kindredgroup.unibetlivetest.entity.Customer;
import com.kindredgroup.unibetlivetest.entity.Selection;
import com.kindredgroup.unibetlivetest.repository.BetRepository;
import com.kindredgroup.unibetlivetest.repository.CustomerRepository;
import com.kindredgroup.unibetlivetest.types.BetState;
import com.kindredgroup.unibetlivetest.types.SelectionResult;
import com.kindredgroup.unibetlivetest.types.SelectionState;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Log4j2
@Service
@RequiredArgsConstructor
public class BetService {

    private final BetRepository betRepository;
    private final CustomerRepository customerRepository;

    /**
     * Returns all bets
     *
     * @return a list of {@link Bet}
     */
    public List<Bet> findAll() {
        return betRepository.findAll();
    }

    /**
     * Creates a new bet for the specified {@link Customer} in the specified {@link Selection}
     *
     * @param customer  the target customer
     * @param selection the target selection
     * @param amount    amount to bet
     */
    public void addBet(Customer customer, Selection selection, BigDecimal amount) {
        var bet = new Bet();
        bet.setAmount(amount);
        bet.setDate(new Date());
        bet.setSelection(selection);
        bet.setCustomer(customer);
        betRepository.save(bet);
    }

    public int closeBets() {
        return betRepository.findAll()
                .stream()
                .filter(bet -> bet.getBetState() == null)
                .filter(bet -> SelectionState.CLOSED.equals(bet.getSelection().getState()))
                .mapToInt(this::closeBet)
                .sum();
    }

    /**
     * Sets a state to the {@link Bet} and updates {@link Customer} balance in case of victory
     *
     * @param bet the target bet
     * @return 1 if operation is successful, 0 otherwise
     */
    public int closeBet(Bet bet) {
        try {
            var betState = convertSelectionStateToBetState(bet.getSelection().getResult());
            bet.setBetState(betState);
            betRepository.save(bet);

            if (BetState.WON.equals(betState)) {
                var customer = bet.getCustomer();
                var prize = bet.getAmount().multiply(bet.getSelection().getCurrentOdd());
                customer.setBalance(customer.getBalance().add(prize));
                customerRepository.save(customer);
            }
            return 1;
        } catch (Exception e) {
            log.error("Error while closing bet {}", bet.getId(), e);
        }
        return 0;
    }

    private BetState convertSelectionStateToBetState(SelectionResult result) {
        return switch (result) {
            case WON -> BetState.WON;
            case LOST -> BetState.LOST;
        };
    }
}
