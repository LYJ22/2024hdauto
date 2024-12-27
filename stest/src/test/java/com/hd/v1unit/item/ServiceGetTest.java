package com.hd.v1unit.item;

import com.hd.common.exception.DataNotFoundException;
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

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@DisplayName("Service Get Test")
@ExtendWith(MockitoExtension.class)
public class ServiceGetTest {
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
    @DisplayName("Get Item")
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

        ItemEntity result = itemService.get(itemEntity.getId());

        assertThat(result).isEqualTo(itemEntity);
        assertThat(result.getId()).isEqualTo(id);
        assertThat(result.getName()).isEqualTo(name);
        assertThat(result.getPrice()).isEqualTo(price);
    }

    @Test
    @DisplayName("Id Not Found")
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

        assertThatThrownBy(() -> itemService.get(undefinedId))
                .isInstanceOf(IdNotFoundException.class)
                .hasMessageContaining(ErrorCode.ID_NOT_FOUND.getErrorMessage());
    }

    @Test
    @Order(3)
    @DisplayName("Get Items List")
    public void test3(){
        ItemEntity itemEntity = ItemEntity.builder()
                .id(id)
                .name(name)
                .price(price)
                .build();
        List<ItemEntity> itemEntities = Arrays.asList(itemEntity);

        when(itemRepository.findAll()).thenReturn(itemEntities);

        List<ItemEntity> result = itemService.getall();

        assertThat(result).isNotEmpty();
        assertThat(result).isEqualTo(itemEntities);
    }

    @Test
    @Order(4)
    @DisplayName("Data Not Found")
    public void test4(){
        /*List<ItemEntity> itemEntities = Arrays.asList();

        when(itemRepository.findAll()).thenReturn(itemEntities);*/

        assertThatThrownBy(() -> itemService.getall())
                .isInstanceOf(DataNotFoundException.class)
                .hasMessageEndingWith(ErrorCode.DATA_DOSE_NOT_EXIST.getErrorMessage());
    }
}
