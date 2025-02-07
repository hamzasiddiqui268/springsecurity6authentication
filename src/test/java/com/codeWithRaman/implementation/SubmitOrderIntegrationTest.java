package com.codeWithRaman.implementation;

import com.codeWithRaman.implementation.model.Bottle;
import com.codeWithRaman.implementation.model.Order;
import com.codeWithRaman.implementation.model.OrderItem;
import com.codeWithRaman.implementation.repository.BeverageRepository;
import com.codeWithRaman.implementation.repository.OrderItemRepository;
import com.codeWithRaman.implementation.repository.OrderRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.security.test.context.support.WithMockUser;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;
import org.springframework.transaction.annotation.Transactional;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc

public class SubmitOrderIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private BeverageRepository beverageRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrderItemRepository orderItemRepository;

    private MockHttpSession session;

    @BeforeEach
    public void setUp() {
        // Clear repositories
        orderItemRepository.deleteAll();
        orderRepository.deleteAll();
        beverageRepository.deleteAll();

        // Add test data
        Bottle bottle = new Bottle();
        bottle.setName("Cola");
        bottle.setPrice(1.5);
        bottle.setVolume(0.5);
        bottle.setAlcoholic(false);
        bottle.setSupplier("Coca-Cola");
        bottle.setInStock(100);
        bottle.setBeveragePic("http://example.com/cola.jpg"); // Set a valid URL for beveragePic
        beverageRepository.save(bottle);

        // Prepare session basket
        session = new MockHttpSession();
        List<OrderItem> basket = new ArrayList<>();
        OrderItem orderItem = new OrderItem();
        orderItem.setBeverage(bottle);
        orderItem.setPrice(bottle.getPrice());
        orderItem.setQuantity(1);
        basket.add(orderItem);
        session.setAttribute("basket", basket);
    }


    @Test
    @WithMockUser(username = "user", password = "user123", roles = {"USER"})
    public void testSubmitOrderIntegration() throws Exception {
        mockMvc.perform(post("/submit-order").session(session))
                .andExpect(status().isOk())
                .andExpect(view().name("order-confirmation"))
                .andExpect(model().attributeExists("totalPrice", "orderItems"))
                .andExpect(model().attribute("totalPrice", is(1.5)))
                .andExpect(model().attribute("orderItems", hasSize(1)));

        // Convert Iterable to List
        List<Order> orders = StreamSupport.stream(orderRepository.findAll().spliterator(), false)
                .collect(Collectors.toList());

        assert orders.size() == 1;

        Order savedOrder = orders.get(0);
        assert savedOrder.getPrice() == 1.5;

        List<OrderItem> savedItems = orderItemRepository.findAll();
        assert savedItems.size() == 1;
        assert savedItems.get(0).getBeverage().getName().equals("Cola");
    }
}
