package model;

import exception.InvalidLabelException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

// Represents tests of a task
class TaskTest {
    private Task testTask;
    private Task a;
    private Task b;

    @BeforeEach
    public void runBeforeValidLabel() {
        try {
            testTask = new Task("CPSC210 project");
            a = new Task("cpsc121");
            b = new Task("cpsc121");
        } catch (InvalidLabelException e) {
            fail("No exception expected, InvalidLabelException caught");
        }
    }

    @Test
    public void testInvalidTaskLabel() {
        try {
            Task c = new Task("");
            fail("InvalidLabelException expected, no exception caught");
        } catch (InvalidLabelException e) {
            //expected
        }
    }

    @Test
    public void testConstructor() {
        assertEquals("CPSC210 project", testTask.getLabel());
        assertFalse(testTask.getIsCompleted());
        assertFalse(testTask.getIsStarred());
        assertEquals(0,testTask.getCaseNum());
    }

    @Test
    public void testSetIsCompleted() {
        testTask.setIsCompleted();
        assertTrue(testTask.getIsCompleted());
    }

    @Test
    public void testSetIsStarred() {
        testTask.setIsStarred();
        assertTrue(testTask.getIsStarred());
    }

    @Test
    public void testUnstarred() {
        testTask.setIsStarred();
        testTask.setUnstarred();
        assertFalse(testTask.getIsStarred());
    }

    @Test
    public void testSetCaseNum() {
        testTask.setCaseNum(3);
        assertEquals(3,testTask.getCaseNum());
    }

    @Test
    public void testEquals() {

        b.setIsCompleted();
        b.setIsStarred();
        assertTrue(a.equals(a));
        assertFalse(a.equals(null));
        assertFalse(a.equals("s"));
        assertTrue(a.equals(b));
    }

    @Test
    public void testHashcode() {
        b.setIsCompleted();
        b.setIsStarred();
        assertEquals(a.hashCode(),b.hashCode());
    }

}