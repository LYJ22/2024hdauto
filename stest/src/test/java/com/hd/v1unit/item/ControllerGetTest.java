package com.hd.v1unit.item;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hd.common.exception.ErrorCode;
import com.hd.common.exception.IdNotFoundException;
import com.hd.v1.item.controller.ItemController;
import com.hd.v1.item.entity.ItemEntity;
import com.hd.v1.item.service.ItemService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Slf4j
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@DisplayName("Controller Get Test")
@WebMvcTest(controllers = ItemController.class)
@Import(value = com.hd.common.dto.Response.class)
public class ControllerGetTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    ItemService itemService;

    /*private ObjectMapper objectMapper;

    @BeforeEach
    public void setUp() {
        objectMapper = new ObjectMapper();
    }*/

    @Test
    @Order(1)
    @DisplayName("Item Get By Id Test")
    public void test1() throws Exception {
        Long id = 1L;

        given(itemService.get(any())).willReturn(ItemEntity.builder()
                .id(1L)
                .name("p1")
                .price(10000L)
                .build());

        ResultActions resultActions = mockMvc.perform(get("/api/item/get/" + id));

        resultActions.andExpect(status().isOk())
                .andExpect(jsonPath("$.data.id").value(1L))
                .andExpect(jsonPath("$.data.name").value("p1"))
                .andExpect(jsonPath("$.data.price").value(10000L));
        resultActions.andDo(print());
    }

    @Test
    @Order(2)
    @DisplayName("Id Not Found Test")
    public void test2() throws Exception {
        Long id = 5L;

        given(itemService.get(any())).willThrow(
                new IdNotFoundException(ErrorCode.ID_NOT_FOUND.getErrorMessage(), ErrorCode.ID_NOT_FOUND)
        );

        ResultActions resultActions = mockMvc.perform(get("/api/item/get/" + id));

        resultActions.andExpect(status().isOk())
                .andExpect(jsonPath("$.state").value(500))
                .andExpect(jsonPath("$.message").value(ErrorCode.ID_NOT_FOUND.getErrorMessage()));
        resultActions.andDo(print());
    }

    @Test
    @Order(3)
    @DisplayName("Item Get All Test")
    public void test3() throws Exception {
        List<ItemEntity> itemEntities = List.of(
            ItemEntity.builder().id(1L).name("p1").price(10000L).build(),
            ItemEntity.builder().id(2L).name("p2").price(20000L).build()
        );

        given(itemService.getall()).willReturn(itemEntities);

        ResultActions resultActions = mockMvc.perform(get("/api/item/get"));

        resultActions.andExpect(status().isOk())
                .andExpect(jsonPath("$.data.[0].id").value(1L))
                .andExpect(jsonPath("$.data.[0].name").value("p1"))
                .andExpect(jsonPath("$.data.[1].id").value(2L))
                .andExpect(jsonPath("$.data.[1].name").value("p2"));
        resultActions.andDo(print());
    }
}
