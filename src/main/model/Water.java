package model;

import org.json.JSONObject;
import persistence.Writable;

// Represents each water-intake input with the amount of water (in mL) and time of the consumption (in 24-hr format)
public class Water implements Writable {
    private int amount;
    private String time;

    // REQUIRES: currentTime has a non-zero length
    // EFFECTS: amount of each water-intake in mL is set to waterAmount;
    //          time at which one drinks water in a 24-hr format is set
    //          to currentTime.
    public Water(int waterAmount, String currentTime) {
        amount = waterAmount;
        time = currentTime;
    }

    // getter
    public int getAmount() {
        return amount;
    }

    // getter
    public String getTime() {
        return time;
    }

    // setter
    public int setAmount(int num) {
        return amount = num;
    }

    // setter
    public String setTime(String t) {
        return time = t;
    }

    // EFFECTS: returns a string representation of water log
    @Override
    public String toString() {
        return "[ " + amount + " mL at " + time + " ]";
    }

    @Override
    public JSONObject toJson() {
        JSONObject json = new JSONObject();
        json.put("amount",amount);
        json.put("time",time);
        return json;
    }
}