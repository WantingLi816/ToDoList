package persistence;

import org.json.JSONObject;

// Represent an interface of which superclasses can be saved
public interface Savable {
    //EFFECTS: returns this as JSON object
    JSONObject toJson();
}
