package persistence;

import model.Task;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/***************************************************************************************
 *    Title: <JsonSerializationDemo>
 *    Author: <Paul Carter>
 *    Date: <29.10.2020>
 *    Availability: <https://github.students.cs.ubc.ca/CPSC210/JsonSerializationDemo.git>
 ***************************************************************************************/

public class JsonTest {

    // Represents method to check a task
    protected void checkTask(Task t, String label,int caseNum,Boolean isCompleted, Boolean isStarred) {
        assertEquals(label,t.getLabel());
        assertEquals(caseNum,t.getCaseNum());
        assertEquals(isCompleted,t.getIsCompleted());
        assertEquals(isStarred,t.getIsStarred());
    }
}
