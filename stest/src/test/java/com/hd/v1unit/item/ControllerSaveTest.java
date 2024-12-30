package com.hd.v1unit.item;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hd.common.exception.ErrorCode;
import com.hd.common.exception.NameDuplicateException;
import com.hd.v1.item.controller.ItemController;
import com.hd.v1.item.dto.ItemRequestDto;
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

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Slf4j
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@DisplayName("Controller Save Test")
@WebMvcTest(controllers = ItemController.class)
@Import(value = com.hd.common.dto.Response.class)
public class ControllerSaveTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    ItemService itemService;

    private ObjectMapper objectMapper;

    @BeforeEach
    public void setup(){
        objectMapper = new ObjectMapper();
    }

    @Test
    @Order(1)
    @DisplayName("Item Save Test")
    public void test1() throws Exception {
        //given
        ItemRequestDto requestDto = ItemRequestDto.builder()
                .name("p1")
                .price(10000L)
                .build();
        String body = objectMapper.writeValueAsString(requestDto);
        //stub
        given(itemService.save(any())).willReturn(ItemEntity.builder()
                .id(100L)
                .name("p1")
                .price(10000L)
                .build());
        //when
        ResultActions resultActions = mockMvc.perform(post("/api/item/add")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(body));
        //then
        resultActions.andExpect(status().isOk());
        resultActions.andExpect(jsonPath("$.data.id").value(100L));
        resultActions.andExpect(jsonPath("$.data.name").value("p1"));
        resultActions.andExpect(jsonPath("$.data.price").value(10000L));
        resultActions.andDo(print());
    }


    @Test
    @Order(2)
    @DisplayName("Validated Name Test")
    public void test2() throws Exception {
        String name = null;

        ItemRequestDto requestDto = ItemRequestDto.builder()
                .name(name)
                .price(10000L)
                .build();
        String body = objectMapper.writeValueAsString(requestDto);

        ResultActions resultActions = mockMvc.perform(post("/api/item/add")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(body));

        resultActions.andExpect(status().isOk())
                .andExpect(jsonPath("$.state").value(400));
        resultActions.andExpect(jsonPath("$.error.[0].message").value("Name cannot be empty"));
        resultActions.andDo(print());
    }

    @Test
    @Order(3)
    @DisplayName("Validated Price Test")
    public void test3() throws Exception {
        ItemRequestDto requestDto = ItemRequestDto.builder()
                .name("p3")
                .price(1L)
                .build();

        String body = objectMapper.writeValueAsString(requestDto);

        ResultActions resultActions = mockMvc.perform(post("/api/item/add")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(body));

        resultActions.andExpect(status().isOk())
                .andExpect(jsonPath("$.state").value(400));
        resultActions.andExpect(jsonPath("$.error.[0].message").value("10 이상이어야 함"));
        resultActions.andDo(print());
    }

    @Test
    @Order(4)
    @DisplayName("Duplicated Name Test")
    public void test4() throws Exception {
        ItemRequestDto requestDto = ItemRequestDto.builder()
                .name("p1")
                .price(10000L)
                .build();
        String body = objectMapper.writeValueAsString(requestDto);

        given(itemService.save(any())).willThrow(
                new NameDuplicateException(ErrorCode.NAME_DUPLICATED.getErrorMessage(), ErrorCode.NAME_DUPLICATED)
        );

        ResultActions resultActions = mockMvc.perform(post("/api/item/add")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(body));

        resultActions.andExpect(status().isOk())
                .andExpect(jsonPath("$.state").value(500))
                .andExpect(jsonPath("$.message").value(ErrorCode.NAME_DUPLICATED.getErrorMessage()))
                .andExpect(jsonPath("$.data.errorCode").value(ErrorCode.NAME_DUPLICATED.getErrorCode()));
        resultActions.andDo(print());
    }
}
