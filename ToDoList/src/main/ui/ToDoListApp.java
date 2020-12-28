package ui;

import model.Task;
import model.ToDoList;
import persistence.JsonReader;
import persistence.JsonWriter;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.Scanner;

// References:
// 1. learn about user interface structure from JsonSerializationDemo.java
//    link: https://github.students.cs.ubc.ca/CPSC210/JsonSerializationDemo.git
//    Date: 29.10.2020

public class ToDoListApp {
    private static final String JSON_STORE = "./data/ToDoList.json";
    private Scanner input;
    private ToDoList toDoList;
    private JsonWriter writer;
    private JsonReader reader;


    //EFFECTS: run the ToDoList application
    public ToDoListApp() {
        runToDoApp();
    }

    public static void main(String[] args) {
        new ToDoListApp();
    }

    // MODIFIES: this
    // EFFECTS: processes user input
    public void runToDoApp() {
        boolean keepGoing = true;
        String command;

        init();

        while (keepGoing) {
            displayMenu();
            command = input.next();
            command = command.toLowerCase();

            if (command.equals("q")) {
                keepGoing = false;
            } else {
                processCommand(command);
            }
        }

    }

    // MODIFIES: this
    // EFFECTS: processes user command
    private void processCommand(String command) {
        if (command.equals("add")) {
            doAdd();
        } else if (command.equals("del")) {
            doDelete();
        } else if (command.equals("com")) {
            doComplete();
        } else if (command.equals("star")) {
            doStar();
        } else if (command.equals("num")) {
            doNum();
        } else if (command.equals("info")) {
            doDisplay();
        } else if (command.equals("s")) {
            doSave();
        } else if (command.equals("l")) {
            doLoad();
        } else if (command.equals("name")) {
            doName();
        } else {
            System.out.println("Selection not valid...");
        }
    }

    // MODIFIES: this
    // EFFECTS: initializes ToDoList
    private void init() {
        toDoList = new ToDoList();
        input = new Scanner(System.in);
        writer = new JsonWriter(JSON_STORE);
        reader = new JsonReader(JSON_STORE);
    }

    //EFFECTS: displays menu of options to user
    private void displayMenu() {
        System.out.println("\nSelect from:");
        System.out.println("\tadd -> add a task");
        System.out.println("\tdel -> delete a task");
        System.out.println("\tcom -> complete the first task");
        System.out.println("\tstar -> star a task");
        System.out.println("\tnum -> see number of completed, uncompleted and starred tasks ");
        System.out.println("\tinfo -> see tasks in your list");
        System.out.println("\ts -> save to-do list to file");
        System.out.println("\tl -> load to-do list from file");
        System.out.println("\tname -> set name of to-do list");
        System.out.println("\tq -> quit");
    }

    //MODIFIES: toDoList
    //EFFECTS: adds a task to to-do list
    public void doAdd() {
        System.out.println("Enter the label of your task:");
        input.nextLine();
        String label = input.nextLine();
        toDoList.addUncompleted(label);
        System.out.println(label + " is added!");
        doDisplay();
    }

    //MODIFIES: toDoList
    //EFFECTS: deletes the task of given position
    public void doDelete() {
        System.out.println("Enter 0 to delete an uncompleted task");
        System.out.println("Enter 1 to delete a completed task");
        int type = input.nextInt();

        System.out.println("Enter the case number of the task you want to delete");
        int i = input.nextInt();

        if (type == 1) {
            if (i < toDoList.numOfCompleted() + 1) {
                toDoList.deleteCompleted(i);
            } else {
                System.out.println("There is no task at that position!");
            }
        } else {
            if (i < toDoList.numOfUncompleted() + 1) {
                toDoList.deleteUncompleted(i);
            } else {
                System.out.println("There is no task at that position!");
            }
        }
        doDisplay();
    }

    //MODIFIES: toDoList,t
    //EFFECTS: completes the first uncompleted task t
    public void doComplete() {
        Task first = toDoList.getUncompleted().get(0);
        String label = first.getLabel();
        toDoList.completeFirst();
        System.out.println(label + " is completed!");
        doDisplay();
    }

    //MODIFIES: toDoList, t
    //EFFECTS: stars task t at the given position
    public void doStar() {
        System.out.println("Enter the case Number of the task you want to star:");
        int caseNum = input.nextInt();
        if (caseNum < toDoList.numOfUncompleted() + 1) {
            toDoList.star(caseNum);
            Task starred = toDoList.getUncompleted().get(0);
            System.out.println(starred.getLabel() + " is starred!");
        } else {
            System.out.println("There is no task at that position!");
        }
        doDisplay();
    }

    //EFFECTS: prints the information about the toDoList
    public void doNum() {
        int numUnCompleted = toDoList.numOfUncompleted();
        int numCompleted = toDoList.numOfCompleted();
        int numStarred = toDoList.numOfStarred();
        System.out.println("The number of remaining uncompleted tasks: " + numUnCompleted);
        System.out.println("The number of completed tasks: " + numCompleted);
        System.out.println("The number of starred tasks: " + numStarred);
    }

    // EFFECTS: saves the current to-do list to file
    public void doSave() {
        try {
            writer.open();
            writer.write(toDoList);
            writer.close();
            System.out.println("ToDoList saved to" + JSON_STORE);
        } catch (FileNotFoundException e) {
            System.out.println("Unable to save to file" + JSON_STORE);
        }
    }

    // EFFECTS: loads to-do list from file
    public void doLoad() {
        try {
            toDoList = reader.read();
            System.out.println("ToDoList loaded from" + JSON_STORE);
        } catch (IOException e) {
            System.out.println("Unable to load file from" + JSON_STORE);
        }
    }

    // EFFECTS: set name of to-do list
    public void doName() {
        System.out.println("Your to-do list is named 'My to-do list' by default");
        System.out.println("Enter the name of your to-do list");
        input.nextLine();
        String name = input.nextLine();
        toDoList.setName(name);
        System.out.println("ToDoList named as " + name);
    }

    // EFFECTS: shows label and case number of tasks in to-do list
    public void doDisplay() {
        List<Task> uncompleted = toDoList.getUncompleted();
        List<Task> completed = toDoList.getIsCompleted();

        System.out.println("\nName of your list: " + toDoList.getName());
        System.out.println("\nUncompleted tasks in your list:");
        for (Task ut : uncompleted) {
            int caseNum = ut.getCaseNum();
            String label = ut.getLabel();
            System.out.println("\t" + caseNum + " " + label);
        }

        System.out.println("Completed tasks in your list: ");
        for (Task t : completed) {
            int caseNum = t.getCaseNum();
            String label = t.getLabel();
            System.out.println("\t" + caseNum + " " + label);
        }
    }

}
