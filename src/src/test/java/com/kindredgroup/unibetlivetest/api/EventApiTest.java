package com.kindredgroup.unibetlivetest.api;

import com.kindredgroup.unibetlivetest.entity.Event;
import com.kindredgroup.unibetlivetest.exception.CustomException;
import com.kindredgroup.unibetlivetest.exception.ExceptionHttpTranslator;
import com.kindredgroup.unibetlivetest.service.EventService;
import com.kindredgroup.unibetlivetest.types.ExceptionType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class EventApiTest {

    @Autowired
    private MockMvc mockMvc;

    @Mock
    private EventService eventService;

    @BeforeEach
    void setup() {
        EventApi eventApi = new EventApi(eventService);
        mockMvc = MockMvcBuilders.standaloneSetup(eventApi)
                .setControllerAdvice(new ExceptionHttpTranslator())
                .build();
    }

    @Test
    void listEventsTest() throws Exception {
        when(eventService.findAll(anyBoolean())).thenReturn(List.of(new Event().setName("test name").setId(1L)));
        this.mockMvc.perform(get("/api/v1/events"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id", equalTo(1)))
                .andExpect(jsonPath("$[0].name", equalTo("test name")));
    }

    @Test
    void listEventsTest_noLive() throws Exception {
        when(eventService.findAll(anyBoolean())).thenThrow(new CustomException("no live", ExceptionType.NO_LIVE));
        this.mockMvc.perform(get("/api/v1/events?islive=true"))
                .andExpect(status().isNoContent())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }
}
