package persistence;


import model.ToDoList;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;

// References:
// 1. learn about JSON reader from JsonSerializationDemo.java
//    link: https://github.students.cs.ubc.ca/CPSC210/JsonSerializationDemo.git
//    Date: 29.10.2020

// Represents a writer that writes JSON representation of to-do list to file
public class JsonWriter {
    private static final int TAB = 4;
    private PrintWriter writer;
    private String destination;

    // EFFECTS: Initializes the writer with the given destination
    public JsonWriter(String destination) {
        this.destination = destination;
    }

    // MODIFIES: this
    // EFFECTS: opens the file at destination;
    //          If the file cannot be opened, throws FileNotFoundException;
    //          opens the writer
    public void open() throws FileNotFoundException {
        writer = new PrintWriter(new File(destination));
    }

    // MODIFIES: this
    // EFFECTS: writes JSON representation of to-do list to file
    public void write(ToDoList list) {
        JSONObject jsonObject = list.toJson();
        saveToFile(jsonObject.toString(TAB));
    }

    // MODIFIES: this
    // EFFECTS: writes string to file
    public void saveToFile(String json) {
        writer.print(json);
    }


    // MODIFIES: this
    // EFFECTS: closes writer
    public void close() {
        writer.close();
    }
}
