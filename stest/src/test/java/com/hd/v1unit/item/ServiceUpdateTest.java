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
@DisplayName("Service Update Test")
@ExtendWith(MockitoExtension.class)
public class ServiceUpdateTest {
    @Mock
    ItemRepository itemRepository;
    @InjectMocks
    ItemService itemService;

    long id;
    String name, updateName;
    long price, updatePrice;

    @BeforeEach
    public void setup(){
        id = 1L;
        name = "pants1";
        price = 1000L;

        updateName = "pants2";
        updatePrice = 2000L;
    }

    @Test
    @DisplayName("Item Update")
    @Order(1)
    public void test1(){
        ItemEntity itemEntity = ItemEntity.builder()
                .id(id)
                .name(name)
                .price(price)
                .build();
        ItemEntity updateItemEntity = ItemEntity.builder()
                .id(id)
                .name(updateName)
                .price(updatePrice)
                .build();

        when(itemRepository.findById(any())).thenReturn(Optional.of(itemEntity));
        when(itemRepository.save(any())).thenReturn(updateItemEntity);

        ItemEntity result = itemService.modify(updateItemEntity);

        assertThat(result).isEqualTo(updateItemEntity);
        assertThat(result.getId()).isEqualTo(id);
        assertThat(result.getName()).isEqualTo(updateName);
        assertThat(result.getPrice()).isEqualTo(updatePrice);
    }

    @Test
    @DisplayName("Id Not Found Exception")
    @Order(2)
    public void test2(){
        ItemEntity itemEntity = ItemEntity.builder()
                .id(id)
                .name(name)
                .price(price)
                .build();

        /*when(itemRepository.findById(any())).thenReturn(
                Optional.empty()
        );*/

        assertThatThrownBy(()->itemService.modify(itemEntity))
                .isInstanceOf(IdNotFoundException.class)
                .hasMessageEndingWith(ErrorCode.ID_NOT_FOUND.getErrorMessage());
    }
}
