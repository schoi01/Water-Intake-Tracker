package persistence;

import model.Water;
import model.WaterRecord;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

public class JsonWriterTest extends JsonTest {

    @Test
    void testWriterInvalidFile() {
        try {
            WaterRecord wr = new WaterRecord(40);
            JsonWriter writer = new JsonWriter("./data/my\0illegal:fileName.json");
            writer.open();
            fail("IOException was expected");
        } catch (IOException e) {
            // pass
        }
    }

    @Test
    void testWriterEmptyWaterRecord() {
        try {
            WaterRecord wr = new WaterRecord(0);
            JsonWriter writer = new JsonWriter("./data/testWriterEmptyWaterRecord.json");
            writer.open();
            writer.write(wr);
            writer.close();

            JsonReader reader = new JsonReader("./data/testWriterEmptyWaterRecord.json");
            wr = reader.read();
            assertEquals(0,wr.getGoal());
            assertEquals(0,wr.length());
        } catch (IOException e) {
            fail("Caught IOException when shouldn't have");
        }
    }

    @Test
    void testWriterGeneralWaterRecord() {
        try {
            WaterRecord wr = new WaterRecord(1300);
            wr.addWater(new Water(200,"5:10"));
            wr.addWater(new Water(500,"3:30"));
            JsonWriter writer = new JsonWriter("./data/testWriterGeneralWaterRecord.json");
            writer.open();
            writer.write(wr);
            writer.close();

            JsonReader reader = new JsonReader("./data/testWriterGeneralWaterRecord.json");
            wr = reader.read();
            assertEquals(1300,wr.getGoal());
            assertEquals(2,wr.length());
            List<Water> waterList = wr.getLog();
            checkWaterInput(200,"5:10",waterList.get(0));
            checkWaterInput(500,"3:30",waterList.get(1));
        } catch (IOException e) {
            fail("Caught IOException when shouldn't have");
        }
    }
}
