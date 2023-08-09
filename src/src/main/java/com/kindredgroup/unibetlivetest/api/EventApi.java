package com.kindredgroup.unibetlivetest.api;

import com.kindredgroup.unibetlivetest.entity.Event;
import com.kindredgroup.unibetlivetest.entity.Market;
import com.kindredgroup.unibetlivetest.entity.Selection;
import com.kindredgroup.unibetlivetest.service.EventService;
import com.kindredgroup.unibetlivetest.types.SelectionState;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Tag(name = "Event")
@RestController
@Log4j2
@RequestMapping(Urls.BASE_PATH)
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class EventApi {

    private final EventService eventService;

    @Operation(summary = "Get an event",
            operationId = "get")
    @GetMapping(value = Urls.EVENT,
            produces = APPLICATION_JSON_VALUE)
    public Event get(@PathVariable Long id) {
        return eventService.get(id);
    }

    @Operation(summary = "List all events from database",
            operationId = "listEvents",
            responses = {
                    @ApiResponse(responseCode = "200", description = "ok"),
                    @ApiResponse(responseCode = "204", description = "No live"),
                    @ApiResponse(responseCode = "400", description = "Bad Request"),
                    @ApiResponse(responseCode = "500", description = "Internal server error")
            })
    @GetMapping(value = Urls.EVENTS,
            produces = APPLICATION_JSON_VALUE)
    public List<Event> listEvents(@RequestParam(required = false, defaultValue = "false") boolean isLive) {
        return eventService.findAll(isLive);
    }

    @Operation(summary = "List all selections from an event",
            operationId = "getSelections",
            responses = {
                    @ApiResponse(responseCode = "200", description = "ok"),
                    @ApiResponse(responseCode = "204", description = "No selection for target live"),
                    @ApiResponse(responseCode = "400", description = "Bad Request"),
                    @ApiResponse(responseCode = "500", description = "Internal server error")
            })
    @GetMapping(value = Urls.SELECTIONS,
            produces = APPLICATION_JSON_VALUE)
    public List<Selection> getSelections(@PathVariable Long id,
                                         @RequestParam(required = false) SelectionState selectionState) {
        return eventService.getSelections(eventService.get(id), selectionState);
    }

    @Operation(summary = "List all markets from an event",
            operationId = "getMarkets")
    @GetMapping(value = Urls.EVENT_MARKETS,
            produces = APPLICATION_JSON_VALUE)
    public List<Market> getMarkets(@PathVariable Long id) {
        return eventService.get(id).getMarkets();
    }

}
