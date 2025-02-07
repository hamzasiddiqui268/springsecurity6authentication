package com.codeWithRaman.implementation;

import com.codeWithRaman.implementation.model.Bottle;
import com.codeWithRaman.implementation.controller.BasketController;
import com.codeWithRaman.implementation.model.OrderItem;
import com.codeWithRaman.implementation.service.BeverageService;
import com.codeWithRaman.implementation.service.OrderItemService;
import com.codeWithRaman.implementation.service.OrderService;
import jakarta.servlet.http.HttpSession;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.transaction.annotation.Transactional;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(BasketController.class)

public class BasketControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private BeverageService beverageService;

    @Autowired
    private OrderItemService orderItemService;

    @Autowired
    private OrderService orderService;

    private MockHttpSession session;

    @TestConfiguration
    static class TestConfig {
        @Bean
        @Primary
        public BeverageService mockBeverageService() {
            return Mockito.mock(BeverageService.class);
        }

        @Bean
        @Primary
        public OrderItemService mockOrderItemService() {
            return Mockito.mock(OrderItemService.class);
        }

        @Bean
        @Primary
        public OrderService mockOrderService() {
            return Mockito.mock(OrderService.class);
        }
    }

    @BeforeEach
    public void setUp() {
        session = new MockHttpSession();

        // Mock a bottle
        Bottle bottle = new Bottle();
        bottle.setId(1L);
        bottle.setName("Cola");
        bottle.setPrice(1.5);
        bottle.setVolume(0.5);
        bottle.setAlcoholic(false);
        bottle.setSupplier("Coca-Cola");

        // Mock an order item
        OrderItem orderItem = new OrderItem();
        orderItem.setId(1L);
        orderItem.setBeverage(bottle);
        orderItem.setPrice(1.5);
        orderItem.setQuantity(1);

        // Add the order item to the session basket
        List<OrderItem> basket = new ArrayList<>();
        basket.add(orderItem);
        session.setAttribute("basket", basket);

        // Mock service responses
        Mockito.when(beverageService.getBeverageById(1L)).thenReturn(bottle);
        Mockito.when(orderItemService.saveOrderItem(Mockito.any(OrderItem.class))).thenReturn(orderItem);
    }

    @Test
    @WithMockUser(username = "user", password = "user123", roles = {"USER"})
    public void testShowBasket() throws Exception {
        mockMvc.perform(get("/basket").session(session))
                .andExpect(status().isOk())
                .andExpect(view().name("basket"))
                .andExpect(model().attributeExists("items"))
                .andExpect(model().attribute("items", hasSize(1)))
                .andExpect(model().attribute("items", contains(
                        hasProperty("beverage", hasProperty("name", is("Cola")))
                )))
                .andExpect(model().attribute("totalPrice", 1.5));
    }

    @Test
    @WithMockUser(username = "user", password = "user123", roles = {"USER"})
    public void testAddToBasket() throws Exception {
        mockMvc.perform(post("/add-to-basket")
                        .param("beverageId", "1")
                        .session(session))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/basket"));

        List<OrderItem> basket = (List<OrderItem>) session.getAttribute("basket");
        assert basket != null;
        assert basket.size() == 2; // Original item + new item added
    }

    @Test
    @WithMockUser(username = "user", password = "user123", roles = {"USER"})
    public void testSubmitOrder_EmptyBasket() throws Exception {
        session.removeAttribute("basket");

        mockMvc.perform(post("/submit-order").session(session))
                .andExpect(status().isOk())
                .andExpect(view().name("basket"))
                .andExpect(model().attributeExists("error"))
                .andExpect(model().attribute("error", is("Your basket is empty. Add items to your basket before placing an order.")));
    }

    @Test
    @WithMockUser(username = "user", password = "user123", roles = {"USER"})
    public void testSubmitOrder_WithItems() throws Exception {
        mockMvc.perform(post("/submit-order").session(session))
                .andExpect(status().isOk())
                .andExpect(view().name("order-confirmation"))
                .andExpect(model().attributeExists("totalPrice", "orderItems"))
                .andExpect(model().attribute("totalPrice", 1.5))
                .andExpect(model().attribute("orderItems", hasSize(1)));

        Mockito.verify(orderService).saveOrder(Mockito.any());
    }
}
