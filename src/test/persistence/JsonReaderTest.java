package persistence;

import model.Water;
import model.WaterRecord;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.fail;

public class JsonReaderTest extends JsonTest {

    @Test
    void testReaderNonExistentFile() {
        JsonReader reader = new JsonReader("./data/noSuchFile.json");
        try {
            WaterRecord wr = reader.read();
            fail("IOException expected");
        } catch (IOException e) {
            // pass
        }
    }

    @Test
    void testReaderEmptyWaterRecord() {
        JsonReader reader = new JsonReader("./data/testReaderEmptyWaterRecord.json");
        try {
            WaterRecord wr = reader.read();
            assertEquals(0,wr.getGoal());
            assertEquals(0,wr.length());
        } catch (IOException e) {
            fail("Couldn't read from file");
        }
    }

    @Test
    void testReaderGeneralWaterRecord() {
        JsonReader reader = new JsonReader("./data/testReaderGeneralWaterRecord.json");
        try {
            WaterRecord wr = reader.read();
            assertEquals(1300,wr.getGoal());
            assertEquals(2,wr.length());
            List<Water> waterList = wr.getLog();
            checkWaterInput(500,"3:30",waterList.get(0));
            checkWaterInput(200,"5:10",waterList.get(1));
        } catch (IOException e) {
            fail("Couldn't read from file");
        }
    }
}
