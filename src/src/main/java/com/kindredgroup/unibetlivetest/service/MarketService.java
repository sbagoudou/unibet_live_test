package com.kindredgroup.unibetlivetest.service;

import com.kindredgroup.unibetlivetest.entity.Market;
import com.kindredgroup.unibetlivetest.entity.Selection;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import static com.kindredgroup.unibetlivetest.types.SelectionState.OPENED;

@Log4j2
@Service
@RequiredArgsConstructor
public class MarketService {

    /**
     * Checks if the market has at least one {@link Selection} in {@link OPENED} state
     *
     * @param market the target market
     * @return true if the market is open
     */
    public boolean isOpen(Market market) {
        return market.getSelections()
                .stream()
                .anyMatch(selection -> OPENED.equals(selection.getState()));
    }

}
