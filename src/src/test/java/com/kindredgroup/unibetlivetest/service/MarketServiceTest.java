package com.kindredgroup.unibetlivetest.service;


import com.kindredgroup.unibetlivetest.types.SelectionState;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.stream.Stream;

import static com.kindredgroup.unibetlivetest.types.SelectionState.CLOSED;
import static com.kindredgroup.unibetlivetest.types.SelectionState.OPENED;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

@SpringBootTest
class MarketServiceTest extends AbstractUnibetLiveServiceTest {

    private final MarketService service = new MarketService();

    static Stream<Arguments> selectionState() {
        return Stream.of(
                Arguments.of(OPENED, true),
                Arguments.of(CLOSED, false)
        );
    }

    @ParameterizedTest
    @MethodSource("selectionState")
    void isOpenTest(SelectionState selectionState, boolean expected) {
        assertThat(service.isOpen(market(selectionState)), is(expected));
    }

}
