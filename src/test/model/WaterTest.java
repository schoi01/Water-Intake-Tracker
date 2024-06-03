package model;

import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class WaterTest {
    private Water testWater;

    @BeforeEach
    void runBefore() {
        testWater = new Water(20, "8:30");
    }

    @Test
    void testConstructor() {
        assertEquals(20, testWater.getAmount());
        assertEquals("8:30", testWater.getTime());
    }

    @Test
    void testSetters() {
        testWater.setAmount(20);
        testWater.setTime("2:00");

        assertEquals(20,testWater.getAmount());
        assertEquals("2:00",testWater.getTime());
    }

    @Test
    void testToString() {
        assertTrue(testWater.toString().contains("[ 20 mL at 8:30 ]"));
    }

    @Test
    void testToJson() {
        JSONObject json = testWater.toJson();
        assertTrue(json.has("amount"));
        assertTrue(json.has("time"));
    }

}

