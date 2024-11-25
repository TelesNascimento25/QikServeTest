package com.qikserve.checkout;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.qikserve.checkout.model.Basket;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@ActiveProfiles("wiremock-client")
public class CheckoutIntegrationTest {

//    @Autowired
//    private MockMvc mockMvc;

//    @Autowired
//    private ObjectMapper objectMapper;


//    @Test
//    void calculateBasketTotal_withPromotions_shouldReturnCorrectTotalAndSavings() throws Exception {
//        // Given
//        BasketItemRequest item1 = new BasketItemRequest("PWWe3w1SDU", 3); // Amazing Burger! (compre 2, leve 3)
//        BasketItemRequest item2 = new BasketItemRequest("Dwt5F7KAhi", 2); // Amazing Pizza! (2 por 17.99)
//        BasketItemRequest item3 = new BasketItemRequest("C8GDyLrHJb", 1); // Amazing Salad! (10% de desconto)
//        BasketItemRequest item4 = new BasketItemRequest("4MB7UfpTQs", 2); // Boring Fries
//        List<BasketItemRequest> basketItems = List.of(item1, item2, item3, item4);
//
//
//        //Cria uma cesta e adiciona os itens.
//        Long basketId = createBasket();
//        addBasketItems(basketId, basketItems);
//
//        // When & Then
//        mockMvc.perform(get("/baskets/{basketId}/checkout", basketId))
//               .andExpect(status().isOk())
//               .andExpect(jsonPath("$.total").value(37.96))
//               .andExpect(jsonPath("$.status").value("CHECKED_OUT"))
//               .andExpect(jsonPath("$.basketItems[0].finalPrice").value(19.98))
//               .andExpect(jsonPath("$.basketItems[1].finalPrice").value(17.99))
//               .andExpect(jsonPath("$.basketItems[2].finalPrice").value(4.49))
//               .andExpect(jsonPath("$.basketItems[3].finalPrice").value(3.98));
//
//        mockMvc.perform(get("/baskets/{basketId}/savings", basketId))
//               .andExpect(status().isOk())
//               .andExpect(jsonPath("$.totalPrice").value(42.46))
//               .andExpect(jsonPath("$.promotionalPrice").value(4.50))
//               .andExpect(jsonPath("$.finalPrice").value(37.96));
//    }
//
//    private Long createBasket() throws Exception {
//        String response = mockMvc.perform(post("/baskets"))
//                                 .andExpect(status().isCreated())
//                                 .andReturn().getResponse().getContentAsString();
//        return objectMapper.readValue(response, Basket.class).getId();
//
//    }
//
//
//    private void addBasketItems(Long basketId, List<BasketItemRequest> basketItems) throws Exception {
//        for (BasketItemRequest item : basketItems) {
//            mockMvc.perform(post("/baskets/{basketId}/item", basketId)
//                           .contentType(MediaType.APPLICATION_JSON)
//                           .content(objectMapper.writeValueAsString(item)))
//                   .andExpect(status().isCreated());
//        }
//    }
}



