package com.hd;

import com.hd.ex.ExClass;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

@Slf4j
@DisplayName("AssertJ 2 Test")
public class Asserts3 {

    @Test
    @DisplayName("AssertJ3_Test_1")
    public void test1() {
        assertThatThrownBy(() -> ExClass.func())
        .isInstanceOf(RuntimeException.class);
    }

    @Test
    @DisplayName("AssertJ3_Test_2")
    public void test2() {
        Throwable throwable = catchThrowable(() -> ExClass.func());
        assertThat(throwable).isInstanceOf(RuntimeException.class);
        assertThat(throwable.getMessage())
        .isEqualTo("Some Exception");

    }

    @Test
    @DisplayName("AssertJ3_Test_3")
    public void test3() {
        assertThatThrownBy(() -> ExClass.func())
        .isInstanceOf(RuntimeException.class)
                .hasMessage("Some Exception")
                .hasMessageEndingWith("n")
                .hasStackTraceContaining("Some");

    }
}
