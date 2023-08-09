package com.kindredgroup.unibetlivetest.service;


import com.kindredgroup.unibetlivetest.entity.Event;
import com.kindredgroup.unibetlivetest.repository.EventRepository;
import com.kindredgroup.unibetlivetest.types.SelectionState;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.stream.Stream;

import static com.kindredgroup.unibetlivetest.types.SelectionState.CLOSED;
import static com.kindredgroup.unibetlivetest.types.SelectionState.OPENED;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

@SpringBootTest
class EventServiceTest extends AbstractUnibetLiveServiceTest {

    private EventService service;

    @Mock
    private EventRepository repository;

    @Mock
    private MarketService marketService;

    @BeforeEach
    void setUp() {
        service = new EventService(repository, marketService);
    }

    static Stream<Arguments> selectionState() {
        return Stream.of(
                Arguments.of(OPENED, OPENED, 1),
                Arguments.of(OPENED, CLOSED, 0),
                Arguments.of(OPENED, null, 1),
                Arguments.of(CLOSED, OPENED, 0),
                Arguments.of(CLOSED, CLOSED, 1),
                Arguments.of(CLOSED, null, 1)
        );
    }

    @ParameterizedTest
    @MethodSource("selectionState")
    void getSelectionsTest(SelectionState eventSelectionState, SelectionState filterSelectionState, int expectedSize) {
        assertThat(service.getSelections(event(eventSelectionState), filterSelectionState).size(),
                is(expectedSize));
    }

    private Event event(SelectionState selectionState) {
        var event = new Event();
        event.setMarkets(List.of(market(selectionState)));
        return event;
    }

}
