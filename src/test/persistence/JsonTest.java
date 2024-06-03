package persistence;

import model.Water;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class JsonTest {

    protected void checkWaterInput(int amount, String time, Water water) {
        assertEquals(amount, water.getAmount());
        assertEquals(time, water.getTime());

    }
}
