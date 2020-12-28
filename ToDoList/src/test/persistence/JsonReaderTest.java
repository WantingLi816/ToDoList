package persistence;

import model.ToDoList;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

// References:
// 1. learn about tests for JSON reader from JsonSerializationDemo.java
//    link: https://github.students.cs.ubc.ca/CPSC210/JsonSerializationDemo.git
//    Date: 29.10.2020

public class JsonReaderTest extends JsonTest{

    // Represents tests of whether JsonReader can read from json file
    @Test
    void testReaderNonExistentFile() {
        JsonReader reader = new JsonReader("./data/noSuchFile.json");
        try {
            ToDoList toDoList = reader.read();
            fail("IOException expected");
        } catch (IOException e) {
            // pass
        }
    }

    @Test
    void testReaderEmptyToDoList() {
        try {
            JsonReader reader = new JsonReader("./data/testWriterEmptyToDoList.json");
            ToDoList list = reader.read();
            assertEquals("My to-do list",list.getName());
            assertEquals(0,list.numOfUncompleted());
            assertEquals(0,list.numOfCompleted());
        } catch (IOException e) {
            fail("IOException caught, no exception expected");
        }
    }

    @Test
    void testReaderGeneralToDoTest() {
        try {
            JsonReader reader = new JsonReader("./data/testWriterGeneralToDoList.json");
            ToDoList list = reader.read();
            assertEquals("Week7 schedule",list.getName());
            assertEquals(2,list.numOfUncompleted());
            assertEquals(1,list.numOfCompleted());
            assertEquals(1,list.numOfStarred());

            checkTask(list.getIsCompleted().get(0),"CPSC 210 project",1,true,false);
            checkTask(list.getUncompleted().get(0),"Math 200 Webwork",1,false,true);
            checkTask(list.getUncompleted().get(1),"Essay",2,false,false);
        } catch (IOException e) {
            fail("IOException caught, no exception expected");
        }
    }
}
