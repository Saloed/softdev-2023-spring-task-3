package com.yooondooo.WORDLY;

import org.junit.Test;
import static org.junit.Assert.*;

public class ExampleUnitTest {
    @Test
    public void test1() {
        String s1 = "шишка";
        String s2 = "ШАПКА";
        HandlerTest handlerTest = new HandlerTest(s1,s2);
        assertFalse(handlerTest.check());
    }
    @Test
    public void test2() {
        String s1 = "шишка";
        String s2 = "ШИШКА";
        HandlerTest handlerTest = new HandlerTest(s1,s2);
        assertTrue(handlerTest.check());
    }
}