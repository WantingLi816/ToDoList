package persistence;


import model.ToDoList;
import org.junit.jupiter.api.Test;

import java.io.FileNotFoundException;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

// References:
// 1. learn about tests for JSON writer from JsonSerializationDemo.java
//    link: https://github.students.cs.ubc.ca/CPSC210/JsonSerializationDemo.git
//    Date: 29.10.2020

// Represents tests of whether JsonWriter can write a to-do list into file
public class JsonWriterTest extends JsonTest{

    @Test
    void testWriterInvalidFile() {
        try {
            ToDoList list = new ToDoList();
            JsonWriter writer = new JsonWriter("./data/my\0illegal:fileName.json");
            writer.open();
            fail("FileNotFoundException was expected");
        } catch (FileNotFoundException e) {
            //expected
        }
    }

    @Test
    void testWriterEmptyToDoList() {
        try {
            ToDoList list = new ToDoList();
            JsonWriter writer = new JsonWriter("./data/testWriterEmptyToDoList.json");
            writer.open();
            writer.write(list);
            writer.close();

            JsonReader reader = new JsonReader("./data/testWriterEmptyToDoList.json");
            list = reader.read();
            assertEquals("My to-do list",list.getName());
            assertEquals(0,list.numOfUncompleted());
            assertEquals(0,list.numOfCompleted());
        } catch (IOException e){
            fail("IOException should not have been thrown");
        }
    }


    @Test
    void testWriterGeneralToDoList() {
        try {
            ToDoList list = new ToDoList();
            list.setName("Week7 schedule");
            list.addUncompleted("CPSC 210 project");
            list.addUncompleted("Math 200 Webwork");
            list.addUncompleted("Essay");
            list.completeFirst();
            list.star(1);

            JsonWriter writer = new JsonWriter("./data/testWriterGeneralToDoList.json");
            writer.open();
            writer.write(list);
            writer.close();

            JsonReader reader = new JsonReader("./data/testWriterGeneralToDoList.json");
             list = reader.read();
             assertEquals("Week7 schedule",list.getName());
             assertEquals(2,list.numOfUncompleted());
             assertEquals(1,list.numOfCompleted());
             assertEquals(1,list.numOfStarred());


            checkTask(list.getIsCompleted().get(0),"CPSC 210 project", 1,true,false);
            checkTask(list.getUncompleted().get(0),"Math 200 Webwork",1,false,true);
            checkTask(list.getUncompleted().get(1),"Essay",2,false,false);
        } catch (IOException e) {
            fail("IOEException should not have been thrown");
        }

    }


}
