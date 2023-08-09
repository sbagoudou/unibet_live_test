package com.kindredgroup.unibetlivetest.api;

import com.kindredgroup.unibetlivetest.entity.Customer;
import com.kindredgroup.unibetlivetest.entity.Selection;
import com.kindredgroup.unibetlivetest.exception.CustomException;
import com.kindredgroup.unibetlivetest.exception.ExceptionHttpTranslator;
import com.kindredgroup.unibetlivetest.service.BetService;
import com.kindredgroup.unibetlivetest.service.CustomerService;
import com.kindredgroup.unibetlivetest.service.SelectionService;
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

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class BetApiTest {

    @Autowired
    private MockMvc mockMvc;

    @Mock
    private CustomerService customerService;
    @Mock
    private SelectionService selectionService;
    @Mock
    private BetService betService;

    @BeforeEach
    void setup() {
        BetApi eventApi = new BetApi(customerService, selectionService, betService);
        mockMvc = MockMvcBuilders.standaloneSetup(eventApi)
                .setControllerAdvice(new ExceptionHttpTranslator())
                .build();
    }

    @Test
    void addBetTest() throws Exception {
        when(customerService.findCustomerByPseudo(any())).thenReturn(new Customer());
        doNothing().when(customerService).useBalance(any(), any());
        when(selectionService.get(any())).thenReturn(new Selection());
        doNothing().when(selectionService).assertReady(any(), any(), any());

        this.mockMvc.perform(post("/api/v1/bets/add")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(getBetPostParam()))
                .andExpect(status().isOk());
    }

    @Test
    void addBetTest_closedSelection() throws Exception {
        when(customerService.findCustomerByPseudo(any())).thenReturn(new Customer());
        doNothing().when(customerService).useBalance(any(), any());
        when(selectionService.get(any())).thenReturn(new Selection());
        doThrow(new CustomException("", ExceptionType.SELECTION_CLOSED)).when(selectionService).assertReady(any(), any(), any());

        this.mockMvc.perform(post("/api/v1/bets/add")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(getBetPostParam()))
                .andExpect(status().isPreconditionRequired());
    }

    private String getBetPostParam() {
        return """
                 {
                    "selectionId" : 1,
                    "odd": 1,
                    "amount": 10
                 }
                """;
    }
}
