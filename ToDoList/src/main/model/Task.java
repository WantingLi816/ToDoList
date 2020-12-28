package model;

import exception.InvalidLabelException;
import org.json.JSONObject;
import persistence.Savable;

import java.util.Objects;

// References:
// 1. learn about toJson method from JsonSerializationDemo.java
//    link: https://github.students.cs.ubc.ca/CPSC210/JsonSerializationDemo.git
//    Date: 29.10.2020

// Representing a task with a label, completion status, starring status and a caseNum
public class Task implements Savable {
    private String label;
    private Boolean isCompleted;
    private Boolean isStarred;
    private int caseNum;


    // EFFECTS: sets the label of this task; Initialize isCompleted and isStarred as false;
    //          throws InvalidLabelException when taskLabel has a zero length
    public Task(String taskLabel) throws InvalidLabelException {
        if (taskLabel.length() == 0) {
            throw new InvalidLabelException();
        }
        this.label = taskLabel;
        this.isCompleted = false;
        this.isStarred = false;
        this.caseNum = 1;
    }


    //MODIFIES: this
    //EFFECTS: marks this task as completed
    public void setIsCompleted() {
        this.isCompleted = true;
    }

    //MODIFIES: this
    //EFFECTS: marks this task as starred
    public void setIsStarred() {
        this.isStarred = true;
    }

    public void setUnstarred() {
        this.isStarred = false;
    }


    // MODIFIES: this
    // EFFECTS: sets caseNum of this task as num
    public void setCaseNum(int num) {
        this.caseNum = num;
    }

    public String getLabel() {
        return label;
    }

    public boolean getIsCompleted() {
        return this.isCompleted;
    }

    public boolean getIsStarred() {
        return this.isStarred;
    }

    public int getCaseNum() {
        return caseNum;
    }

    @Override
    // EFFECTS: returns JSON representation of task
    public JSONObject toJson() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("caseNum",caseNum);
        jsonObject.put("label",label);
        jsonObject.put("isCompleted",isCompleted);
        jsonObject.put("isStarred",isStarred);
        return jsonObject;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Task task = (Task) o;
        return Objects.equals(label.toLowerCase(), task.label.toLowerCase());
    }

    @Override
    public int hashCode() {
        return Objects.hash(label);
    }
}
