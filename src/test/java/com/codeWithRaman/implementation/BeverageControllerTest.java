package com.codeWithRaman.implementation;

import com.codeWithRaman.implementation.model.Bottle;
import com.codeWithRaman.implementation.model.Crate;
import com.codeWithRaman.implementation.service.BeverageService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.test.web.servlet.MockMvc;
import com.codeWithRaman.implementation.controller.DashboardController;
import java.util.Arrays;
import org.springframework.boot.test.context.TestConfiguration;
import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.transaction.annotation.Transactional;

@WebMvcTest(DashboardController.class)

public class BeverageControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private BeverageService beverageService;

    @TestConfiguration
    static class TestConfig {
        @Bean
        @Primary
        public BeverageService beverageService() {
            return Mockito.mock(BeverageService.class); // Provide a mock for the service
        }
    }

    @BeforeEach
    public void setUp() {
        Bottle bottle = new Bottle();
        bottle.setId(1L);
        bottle.setName("Cola");
        bottle.setPrice(1.5);
        bottle.setVolume(0.5);
        bottle.setAlcoholic(false);
        bottle.setSupplier("Coca-Cola");

        Crate crate = new Crate();
        crate.setId(2L);
        crate.setName("Cola Crate");
        crate.setPrice(10.0);
        crate.setNoOfBottles(6);
        crate.setCratesInStock(50);

        Mockito.when(beverageService.getAllBeverages()).thenReturn(Arrays.asList(bottle, crate));
    }

    @Test
    @WithMockUser(username = "user", password = "user123", roles = {"USER"})
    public void testShowAllBeverages() throws Exception {
        mockMvc.perform(get("/"))
                .andExpect(status().isOk())
                .andExpect(view().name("index"))
                .andExpect(model().attributeExists("beverages"))
                .andExpect(model().attribute("beverages", hasSize(2)))
                .andExpect(model().attribute("beverages", contains(
                        hasProperty("name", is("Cola")),
                        hasProperty("name", is("Cola Crate"))
                )));
    }
}
