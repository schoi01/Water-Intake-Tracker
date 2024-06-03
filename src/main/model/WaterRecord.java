package model;

import org.json.JSONArray;
import org.json.JSONObject;
import persistence.Writable;

import java.util.ArrayList;
import java.util.List;

// Represents a record of water logs
public class WaterRecord implements Writable {
    private final List<Water> log;
    private int goal;

    public WaterRecord() {
        log = new ArrayList<>();
        goal = 0;
    }

    // EFFECTS: log is empty
    public WaterRecord(int goal) {
        this.goal = goal;
        log = new ArrayList<>();
    }

    // MODIFIES: this
    // EFFECTS: adds water input to the log if it is not in the log. Otherwise, do nothing
    public void addWater(Water w) {
        if (!log.contains(w)) {
            log.add(w);
        }
        EventLog.getInstance().logEvent(new Event("---> Added water input: " + w));
    }

    // MODIFIES: this
    // EFFECTS: removes water from the log
    public void removeWater(Water w) {
        log.remove(w);
        EventLog.getInstance().logEvent(new Event("---> Removed water input: " + w));

    }

    // REQUIRES: hour and minute must only consist of integers
    //           - hour must be from [1,12]
    //           - minute must be from [00,59]
    // EFFECTS: returns a new water log with the given amount and time
    public Water waterLog(int amount, String hour, String minute) {
        String time = hour + ":" + minute;
        Water waterInput = new Water(amount, time);
        return waterInput;
    }

    // MODIFIES: this
    // EFFECTS: takes the water amount of the current water log and subtract it from goal amount
    //          and if it is bigger or equal to 0, return new goal
    //          and if it is smaller than 0, return goal as 0
    public int progress(Water w) {
        goal = (goal - w.getAmount());

        if (goal > 0) {
            return goal;
        } else {
            return (goal = 0);
        }
    }

    // EFFECTS: adds up all water amounts listed in log so far and returns the value
    public int addTotalWaterAmount() {
        int total = 0;
        for (Water w : log) {
            total += w.getAmount();
        }
        return total;
    }

    // EFFECTS: returns the number of currently recorded water in the log.
    public int length() {
        return log.size();
    }

    // EFFECTS: returns true if the log is empty. Otherwise, false.
    public boolean isEmpty() {
        return log.isEmpty();
    }

    // getter
    public int getGoal() {
        return goal;
    }

    // getter
    public List<Water> getLog() {
        return log;
    }

    // setter
    public int setGoal(int num) {
        goal = num;
        EventLog.getInstance().logEvent(new Event("---> Remaining goal: " + goal + " mL"));
        return goal;
    }

    @Override
    public JSONObject toJson() {
        JSONObject json = new JSONObject();
        json.put("log", waterInputsToJson());
        json.put("goal", goal);
        return json;
    }

    // EFFECTS: returns water inputs in log as a JSON array
    private JSONArray waterInputsToJson() {
        JSONArray jsonArray = new JSONArray();

        for (Water w : log) {
            jsonArray.put(w.toJson());
        }

        return jsonArray;
    }
}