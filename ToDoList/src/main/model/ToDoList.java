package model;

import exception.InvalidLabelException;
import org.json.JSONArray;
import org.json.JSONObject;
import persistence.Savable;

import java.util.ArrayList;
import java.util.List;

// References:
// 1. learn about toJson method from JsonSerializationDemo.java
//    link: https://github.students.cs.ubc.ca/CPSC210/JsonSerializationDemo.git
//    Date: 29.10.2020

//Representing  a list of tasks
public class ToDoList implements Savable {
    private String name;
    private List<Task> uncompleted;
    private List<Task> isCompleted;

    //EFFECTS: Initialize inCompleted and isCompleted as empty lists
    //         set the name of this to-do list as "My to-do list"
    public ToDoList() {
        name = "My to-do list";
        uncompleted = new ArrayList<>();
        isCompleted = new ArrayList<>();
    }


    //MODIFIES: this
    //EFFECTS: adds a new uncompleted task t of given label to unCompleted;
    //         caseNum is its position in unCompleted
    public void addUncompleted(String label) {
        Task task = null;
        try {
            task = new Task(label);
            int length = uncompleted.size() + 1;
            task.setCaseNum(length);
            uncompleted.add(task);
        } catch (InvalidLabelException e) {
            System.out.println("The task label is invalid!");
        }

    }

    //MODIFIES: this
    //EFFECTS: adds a new completed task t of given label to isCompleted;
    //         caseNum is its position in isCompleted
    public void addCompleted(String label) {
        Task newTask = null;
        try {
            newTask = new Task(label);
            int length = isCompleted.size() + 1;
            newTask.setCaseNum(length);
            newTask.setIsCompleted();
            isCompleted.add(newTask);
        } catch (InvalidLabelException e) {
            System.out.println("The task label is invalid!");
        }

    }


    //REQUIRES: unCompleted is not empty
    //MODIFIES: t,this
    //EFFECTS: completes and mark first uncompleted task t as unstarred
    //         sets caseNum of t as position in isCompleted
    //         sets caseNum of rest tasks in unCompleted as their position in unCompleted
    public void completeFirst() {
        Task first = uncompleted.get(0);
        uncompleted.remove(first);

        for (Task t : uncompleted) {
            int num = t.getCaseNum() - 1;
            t.setCaseNum(num);
        }

        first.setIsCompleted();
        first.setUnstarred();
        int length = isCompleted.size() + 1;
        first.setCaseNum(length);
        isCompleted.add(first);
    }


    //REQUIRES: unCompleted has a length >= caseNum+1
    //MODIFIES: t, this
    //EFFECTS: stars the uncompleted task t of given caseNum and rearrange it to the front
    public void star(int caseNum) {
        int index = caseNum - 1;
        Task star = uncompleted.get(index);
        star.setIsStarred();
        star.setCaseNum(1);
        uncompleted.remove(star);
        uncompleted.add(0,star);

        for (int i = 0; i < uncompleted.size(); i++) {
            Task t = uncompleted.get(i);
            t.setCaseNum(i + 1);
        }

    }

    //REQUIRES: unCompleted has a length >= caseNum
    //MODIFIES: this
    //EFFECTS: removes (caseNum - 1)th uncompleted task
    public void deleteUncompleted(int caseNum) {
        int index = caseNum - 1;
        uncompleted.remove(index);
        for (int i = index; i < uncompleted.size(); i++) {
            uncompleted.get(i).setCaseNum(i + 1);
        }
    }

    //REQUIRES: isCompleted has a length >= caseNum
    //MODIFIES: this
    //EFFECTS: removes (caseNum - 1)th completed task
    public void deleteCompleted(int caseNum) {
        int index = caseNum - 1;
        isCompleted.remove(index);
        for (int i = index;i < isCompleted.size();i++) {
            isCompleted.get(i).setCaseNum(i + 1);
        }
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    //EFFECTS: returns tasks that are complete
    public List<Task> getIsCompleted() {
        return isCompleted;
    }

    //EFFECTS: returns tasks that are uncompleted
    public List<Task> getUncompleted() {
        return uncompleted;
    }

    //EFFECTS: returns the number of completed task in this
    public int numOfCompleted() {
        return isCompleted.size();
    }

    //EFFECTS: returns the number of uncompleted task in this
    public int numOfUncompleted() {
        return uncompleted.size();
    }

    //EFFECTS: returns the number of starred task in unCompleted to-do list
    public int numOfStarred() {
        int count = 0;
        for (Task t : uncompleted) {
            if (t.getIsStarred()) {
                count++;
            }
        }
        return count;
    }


    @Override
    // EFFECTS:  returns JSON representation of to-do list
    public JSONObject toJson() {
        JSONObject json = new JSONObject();
        json.put("name",name);

        json.put("uncompleted",uncompletedToJson());
        json.put("completed",completedToJson());
        return json;
    }

    // EFFECTS: returns uncompleted tasks in to-do list as a JSON Array
    private JSONArray uncompletedToJson() {

        JSONArray jsonArray = new JSONArray();

        for (Task t : uncompleted) {
            jsonArray.put(t.toJson());
        }
        return jsonArray;
    }

    // EFFECTS: returns completed tasks in to-do list as a JSON Array
    private JSONArray completedToJson() {
        JSONArray jsonArray = new JSONArray();

        for (Task t : isCompleted) {
            jsonArray.put(t.toJson());
        }
        return jsonArray;
    }


}
