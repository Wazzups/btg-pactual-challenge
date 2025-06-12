package com.wazzups.btgpactual.unit;

import com.wazzups.btgpactual.controller.OrderController;
import com.wazzups.btgpactual.service.OrderQueryService;
import java.math.BigDecimal;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.server.ResponseStatusException;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = OrderController.class)
public class OrderControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private OrderQueryService queryService;

    @Test
    void getOrderTotalSuccess() throws Exception {
        Long orderId = 1L;
        BigDecimal total = BigDecimal.valueOf(99.99);

        given(queryService.getOrderTotal(eq(orderId))).willReturn(total);

        mockMvc.perform(get("/orders/{orderId}/total", orderId))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.total").value(99.99));
    }

    @Test
    void getOrderTotalNotFound() throws Exception {
        Long orderId = 404L;
        given(queryService.getOrderTotal(orderId))
            .willThrow(new ResponseStatusException(HttpStatus.NOT_FOUND, "Not found orderId: " + orderId));

        mockMvc.perform(get("/orders/{orderId}/total", orderId))
            .andExpect(status().isNotFound());
    }
}
