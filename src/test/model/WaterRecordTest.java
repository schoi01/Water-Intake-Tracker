package model;

import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class WaterRecordTest {
    WaterRecord testLog;
    WaterRecord testLogEmptyParam;

    Water w1;
    Water w2;
    Water w3;

    @BeforeEach
    void runBefore() {
        testLogEmptyParam = new WaterRecord();
        testLog = new WaterRecord(100);
        w1 = new Water(30, "8:00");
        w2 = new Water(25, "13:00");
        w3 = new Water(50, "8:30");
    }

    @Test
    void TestConstructorEmptyParam() {
        assertTrue(testLogEmptyParam.isEmpty());
        assertEquals(0,testLogEmptyParam.getGoal());
    }

    @Test
    void testSetGoal() {
        testLogEmptyParam.setGoal(20);
        assertEquals(20,testLogEmptyParam.getGoal());
    }

    @Test
    void testAddWaterEmptyLog() {
        assertTrue(testLog.isEmpty());
        testLog.addWater(w1);
        assertEquals(1, testLog.length());
    }

    @Test
    void testKeepAddingWater() {
        assertTrue(testLog.isEmpty());
        testLog.addWater(w1);
        assertEquals(1, testLog.length());
        testLog.addWater(w3);
        assertEquals(2, testLog.length());
    }

    @Test
    void testAddWaterAlreadyThere() {
        testLog.addWater(w1);
        assertEquals(1, testLog.length());
        testLog.addWater(w1);
        assertEquals(1, testLog.length());
    }

    @Test
    void testRemoveWaterEmptyLog() {
        assertTrue(testLog.isEmpty());
        testLog.removeWater(w1);
        assertTrue(testLog.isEmpty());
    }

    @Test
    void testRemoveWaterGeneralLog() {
        assertTrue(testLog.isEmpty());
        testLog.addWater(w1);
        assertEquals(1, testLog.length());
        testLog.removeWater(w1);
        assertTrue(testLog.isEmpty());
    }

    @Test
    void testRemoveWaterGeneralLogNotThere() {
        testLog.addWater(w1);
        testLog.addWater(w2);
        assertEquals(2,testLog.length());
        testLog.removeWater(w1);
        List<Water> list = testLog.getLog();
        assertEquals(1,list.size());

        Water w = list.get(0);
        assertEquals(25,w.getAmount());
        assertEquals("13:00",w.getTime());
    }

    @Test
    void testWaterLog() {
        int am = 50;
        String hr = "3";
        String min = "30";

        Water w = testLog.waterLog(am,hr,min);
        assertEquals(50,w.getAmount());
        assertEquals("3:30",w.getTime());
    }

    @Test
    void testProgressGoalAboveZero() {
        assertEquals(100,testLog.getGoal());
        testLog.progress(w1);
        assertEquals(70,testLog.getGoal());
    }

    @Test
    void testProgressGoalBelowZero() {
        assertEquals(100,testLog.getGoal());
        testLog.progress(new Water(120,"4:30"));
        assertEquals(0,testLog.getGoal());
    }

    @Test
    void testAddTotalWaterAmountEmptyLog() {
        assertEquals(0, testLog.addTotalWaterAmount());
    }

    @Test
    void testAddTotalWaterAmountGeneralLog() {
        testLog.addWater(w1);
        testLog.addWater(w2);

        assertEquals(55,testLog.addTotalWaterAmount());
    }

    @Test
    void testToJson() {
        JSONObject json = testLog.toJson();
        assertTrue(json.has("log"));
        assertTrue(json.has("goal"));
        assertEquals(100,json.get("goal"));
    }

    @Test
    void testWaterInputsToJson() {
        testLog.addWater(w1);
        JSONObject json = testLog.toJson();
        JSONArray jsonArray = json.getJSONArray("log");
        assertEquals(1,jsonArray.length());
    }


}