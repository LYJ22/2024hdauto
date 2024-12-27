package com.hd.v1unit.item;

import com.hd.common.exception.ErrorCode;
import com.hd.common.exception.IdNotFoundException;
import com.hd.v1.item.entity.ItemEntity;
import com.hd.v1.item.repository.ItemRepository;
import com.hd.v1.item.service.ItemService;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import static org.assertj.core.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@DisplayName("Service Delete Test")
@ExtendWith(MockitoExtension.class)
public class ServiceDeleteTest {
    @Mock
    ItemRepository itemRepository;
    @InjectMocks
    ItemService itemService;

    long id;
    String name;
    long price;

    @BeforeEach
    public void setup(){
        id = 1L;
        name = "pants1";
        price = 1000L;
    }

    @Test
    @DisplayName("Delete Item")
    @Order(1)
    public void test1(){
        ItemEntity itemEntity = ItemEntity.builder()
                .id(id)
                .name(name)
                .price(price)
                .build();

        when(itemRepository.findById(any())).thenReturn(
                Optional.of(itemEntity)
        );

        Long result = itemService.remove(id);

        assertThat(result).isEqualTo(id);
    }

    @Test
    @DisplayName("Id Not Found Exception")
    @Order(2)
    public void test2(){
        Long undefinedId = 2L;

        /*ItemEntity itemEntity = ItemEntity.builder()
                .id(id)
                .name(name)
                .price(price)
                .build();

        when(itemRepository.findById(any())).thenReturn(
                Optional.empty()
        );*/

        assertThatThrownBy(()->itemService.remove(undefinedId))
                .isInstanceOf(IdNotFoundException.class)
                .hasMessageContaining(ErrorCode.ID_NOT_FOUND.getErrorMessage());
    }
}
