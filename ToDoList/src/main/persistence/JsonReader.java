package persistence;

import model.ToDoList;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Stream;

// References:
// 1. learn about JSON reader from JsonSerializationDemo.java
//    link: https://github.students.cs.ubc.ca/CPSC210/JsonSerializationDemo.git
//    Date: 29.10.2020

// Represents a reader that reads a to-do list from JSON data stored in file
public class JsonReader {
    private String source;

    // EFFECTS: constructs reader to read from source file
    public JsonReader(String source) {
        this.source = source;
    }


    // EFFECTS: reads to-do list from file and returns it;
    //          throws IOException if an error occurs reading data from file
    public ToDoList read() throws IOException {
        String json = readFile(source);
        JSONObject jsonObject = new JSONObject(json);
        return parseToDoList(jsonObject);
    }

    // EFFECTS: reads source file as string and returns it
    private String readFile(String source) throws IOException {
        StringBuilder contentBuilder = new StringBuilder();

        try (Stream<String> stream = Files.lines(Paths.get(source), StandardCharsets.UTF_8)) {
            stream.forEach(s -> contentBuilder.append(s));
        }

        return contentBuilder.toString();
    }

    // EFFECTS: parses to-do list from JSON object and returns it
    private ToDoList parseToDoList(JSONObject jsonObject) {
        ToDoList list = new ToDoList();
        String name = jsonObject.getString("name");
        list.setName(name);
        addCompletedTasks(list,jsonObject);
        addUncompletedTasks(list,jsonObject);
        return list;
    }

    // MODIFIES: list
    // EFFECTS: parses uncompleted tasks from JSON object and add them to to-do list
    private void addUncompletedTasks(ToDoList list,JSONObject jsonObject) {
        JSONArray uncompleted = jsonObject.getJSONArray("uncompleted");
        for (Object t : uncompleted) {
            JSONObject nextTask = (JSONObject) t;
            addTask(list,nextTask);
        }
    }

    // MODIFIES :list
    // EFFECTS: parses completed tasks from JSON object and add them to to-do list
    private void addCompletedTasks(ToDoList list, JSONObject jsonObject) {
        JSONArray completed = jsonObject.getJSONArray("completed");
        for (Object t : completed) {
            JSONObject nextTask = (JSONObject) t;
            addTask(list,nextTask);
        }
    }

    // MODIFIES: list
    // EFFECTS: parses a task from JSON object and add it to to-do list
    private void addTask(ToDoList list, JSONObject nextTask) {
        String label = nextTask.getString("label");
        Boolean isCompleted = nextTask.getBoolean("isCompleted");
        Boolean isStarred = nextTask.getBoolean("isStarred");

        list.addUncompleted(label);

        if (isStarred) {
            list.star(1);
        }
        if (isCompleted) {
            list.completeFirst();
        }
    }
}
