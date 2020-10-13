package com.schepov.mjc.one.utils;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class StringUtilsTest {

    @Test
    void isPositiveNumberZeroTest() {
        assertFalse(StringUtils.isPositiveNumber("00.00e234"));
    }
}