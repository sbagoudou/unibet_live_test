package com.kindredgroup.unibetlivetest.service;

import com.kindredgroup.unibetlivetest.entity.Event;
import com.kindredgroup.unibetlivetest.entity.Selection;
import com.kindredgroup.unibetlivetest.exception.CustomException;
import com.kindredgroup.unibetlivetest.repository.EventRepository;
import com.kindredgroup.unibetlivetest.types.ExceptionType;
import com.kindredgroup.unibetlivetest.types.SelectionState;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.util.List;

import static java.lang.String.format;

@Log4j2
@Service
@RequiredArgsConstructor
public class EventService {

    private final EventRepository eventRepository;
    private final MarketService marketService;

    /**
     * Returns all events
     *
     * @param isLive tells if only live event must be returned
     * @return a list of {@link Event}
     * @throws CustomException if no event is found
     */
    public List<Event> findAll(boolean isLive) {
        var events = eventRepository.findAll();

        if (isLive) {
            events = events.stream()
                    .filter(this::isLive)
                    .toList();
        }

        if (events.isEmpty()) {
            throw new CustomException("no live available", ExceptionType.NO_LIVE);
        }

        return events;
    }

    /**
     * Get an event by its id
     *
     * @param id the wanted event id
     * @return an {@link Event}
     * @throws CustomException if no event is found with the given id
     */
    public Event get(Long id) {
        return eventRepository.findById(id)
                .orElseThrow(() -> new CustomException(format("event with id %s not found", id), ExceptionType.EVENT_NOT_FOUND));
    }

    /**
     * Get the selections of the specified event
     *
     * @param event          target event
     * @param selectionState optional state to filter selections
     * @return a list of {@link Selection}
     */
    public List<Selection> getSelections(Event event, SelectionState selectionState) {
        return event.getMarkets()
                .stream()
                .map(market ->
                        market.getSelections()
                                .stream()
                                .filter(selection -> selectionState == null || selectionState.equals(selection.getState()))
                                .toList()
                )
                .flatMap(List::stream)
                .toList();
    }

    /**
     * Checks if the event has at least one open market
     *
     * @param event the Event
     * @return true if the event is live
     */
    private boolean isLive(Event event) {
        return !event.getMarkets()
                .stream()
                .filter(marketService::isOpen)
                .toList()
                .isEmpty();
    }
}
