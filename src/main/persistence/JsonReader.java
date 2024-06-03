package persistence;

import model.Water;
import model.WaterRecord;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Stream;

// Represents a reader that reads water record from JSON data stored in file
// Code based on given sample application - JsonSerializationDemo
public class JsonReader {
    private String source;

    // EFFECTS: constructs reader to read from source file
    public JsonReader(String source) {
        this.source = source;
    }

    // EFFECTS: reads water record from file and returns it;
    //          throws IOException if an error occurs reading data from file
    public WaterRecord read() throws IOException {
        String jsonData = readFile(source);
        JSONObject jsonObject = new JSONObject(jsonData);
        return parseWaterRecord(jsonObject);
    }

    // EFFECTS: reads source file as string and returns it
    private String readFile(String source) throws IOException {
        StringBuilder contentBuilder = new StringBuilder();

        try (Stream<String> stream = Files.lines(Paths.get(source), StandardCharsets.UTF_8)) {
            stream.forEach(s -> contentBuilder.append(s));
        }

        return contentBuilder.toString();
    }

    // EFFECTS: parses water record from JSON and returns it
    private WaterRecord parseWaterRecord(JSONObject jsonObject) {
        int goal = jsonObject.getInt("goal");
        WaterRecord wr = new WaterRecord(goal);
        addWaterInputs(wr,jsonObject);
        return wr;
    }

    // MODIFIES: wr
    // EFFECTS: parses water inputs in log from JSON object and adds them to water record
    private void addWaterInputs(WaterRecord wr,JSONObject jsonObject) {
        JSONArray jsonArray = jsonObject.getJSONArray("log");
        for (Object json : jsonArray) {
            JSONObject nextWaterInput = (JSONObject) json;
            addWaterInput(wr,nextWaterInput);
        }
    }

    // MODIFIES: wr
    // EFFECTS: parses water input from JSON object and adds it to water record
    private void addWaterInput(WaterRecord wr, JSONObject jsonObject) {
        int amount = jsonObject.getInt("amount");
        String time = jsonObject.getString("time");
        Water water = new Water(amount,time);
        wr.addWater(water);
    }
}
