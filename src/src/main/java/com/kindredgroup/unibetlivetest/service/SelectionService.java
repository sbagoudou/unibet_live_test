package com.kindredgroup.unibetlivetest.service;

import com.kindredgroup.unibetlivetest.entity.Bet;
import com.kindredgroup.unibetlivetest.entity.Selection;
import com.kindredgroup.unibetlivetest.exception.CustomException;
import com.kindredgroup.unibetlivetest.repository.SelectionRepository;
import com.kindredgroup.unibetlivetest.types.ExceptionType;
import com.kindredgroup.unibetlivetest.types.SelectionState;
import com.kindredgroup.unibetlivetest.utils.Helpers;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.IntStream;

import static java.lang.String.format;

@RequiredArgsConstructor
@Component
@Log4j2
public class SelectionService {

    private final SelectionRepository selectionRepository;

    public Selection get(Long id) {
        return selectionRepository.findById(id)
                .orElseThrow(() -> new CustomException(format("selection with id %s not found", id), ExceptionType.SELECTION_NOT_FOUND));
    }

    /**
     * 1. Récupère toute les selections ouvertes
     * 2. Mis à jour la cote aléatoirement
     */

    public int updateOddsRandomly() {
        final List<Selection> selectionsOpened = selectionRepository.getSelectionByStateEquals(SelectionState.OPENED);
        return selectionsOpened.isEmpty() ? 0 : selectionsOpened
                .stream()
                .map(selection -> selection.setCurrentOdd(Helpers.updateOddRandomly(selection.getCurrentOdd())))
                .map(selectionRepository::save)
                .toList()
                .size();
    }

    /**
     * 1. Récupère toute les selections ouvertes
     * 2. Ferme 5 sélections aléatoirement.
     */

    public int closeOddsRandomly() {
        final List<Selection> selectionsOpened = selectionRepository.getSelectionByStateEquals(SelectionState.OPENED);

        return selectionsOpened.isEmpty() ? 0 : IntStream
                .range(0, 5)
                .mapToObj(i -> selectionRepository.save(
                        selectionsOpened.get(Helpers.getRandomIndex(0, selectionsOpened.size()))
                                .setState(SelectionState.CLOSED)
                                .setResult(Helpers.setResultRandomly())))
                .toList()
                .size();
    }

    /**
     * Asserts the selection is ready to take a bet
     * - Selection is not closed
     * - Odd is the same
     * - User does not already have a bet in the current selection
     *
     * @param selection the target {@link Selection}
     * @param odd       odd value to compare with current odd
     * @param bets      current bets of user
     */
    public void assertReady(Selection selection, BigDecimal odd, List<Bet> bets) {
        if (SelectionState.CLOSED.equals(selection.getState())) {
            throw new CustomException("selection is closed", ExceptionType.SELECTION_CLOSED);
        }
        if (odd.compareTo(selection.getCurrentOdd()) != 0) {
            throw new CustomException("Odd changed", ExceptionType.ODD_CHANGED);
        }
        if (bets.stream().anyMatch(bet -> bet.getSelection().getId().equals(selection.getId()))) {
            throw new CustomException("Ongoing bet", ExceptionType.ONGOING_BET);
        }
    }

}
