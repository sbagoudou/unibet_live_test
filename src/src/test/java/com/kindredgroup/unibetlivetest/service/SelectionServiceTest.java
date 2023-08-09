package com.kindredgroup.unibetlivetest.service;


import com.kindredgroup.unibetlivetest.exception.CustomException;
import com.kindredgroup.unibetlivetest.repository.SelectionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.util.List;

import static com.kindredgroup.unibetlivetest.types.SelectionState.CLOSED;
import static com.kindredgroup.unibetlivetest.types.SelectionState.OPENED;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
class SelectionServiceTest extends AbstractUnibetLiveServiceTest {

    private SelectionService service;

    @Mock
    private SelectionRepository repository;

    @BeforeEach
    void setUp() {
        service = new SelectionService(repository);
    }

    @Test
    void assertReadyTest() {
        assertDoesNotThrow(() -> service.assertReady(selection(OPENED, null), BigDecimal.ONE, List.of()));
    }

    @Test
    void assertReadyTest_selectionClosed() {
        assertThrows(CustomException.class, () -> service.assertReady(selection(CLOSED, null), BigDecimal.ONE, List.of()));
    }

    @Test
    void assertReadyTest_differentOdd() {
        assertThrows(CustomException.class, () -> service.assertReady(selection(OPENED, null), BigDecimal.TEN, List.of()));
    }

    @Test
    void assertReadyTest_ongoingBet() {
        assertThrows(CustomException.class, () -> service.assertReady(selection(OPENED, null), BigDecimal.TEN, List.of(bet(OPENED, null))));
    }

}
