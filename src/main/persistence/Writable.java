package persistence;

import org.json.JSONObject;

// Interface
public interface Writable {

    // EFFECTS: returns this as JSON object
    JSONObject toJson();
}
