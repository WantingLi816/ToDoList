package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class ToDoListTest {
    private ToDoList testList;


    @BeforeEach
    public void runBefore() {
        testList = new ToDoList();
    }


    @Test
    public void testConstructor() {
        assertEquals(0,testList.numOfUncompleted());
        assertEquals(0,testList.numOfCompleted());
        assertEquals("My to-do list",testList.getName());
    }

    @Test
    public void testAddUncompletedOne() {
        testList.addUncompleted("MATH200_quiz");

        assertEquals(1,testList.numOfUncompleted());
        assertEquals(0,testList.numOfCompleted());
        Task math = testList.getUncompleted().get(0);
        assertEquals("MATH200_quiz",math.getLabel());
        assertEquals(1,math.getCaseNum());
    }

    @Test
    public void testAddCompletedOne() {
        testList.addCompleted("do laundry");

        assertEquals(0,testList.numOfUncompleted());
        assertEquals(1,testList.numOfCompleted());
        Task laundry = testList.getIsCompleted().get(0);
        assertEquals("do laundry",laundry.getLabel());
        assertEquals(1,laundry.getCaseNum());
    }


    @Test
    public void testAddMultiple() {
        testList.addUncompleted("CPSC210_project");
        testList.addUncompleted("MATH200_quiz");
        testList.addUncompleted("MATH221_webwork");

        assertEquals(3,testList.numOfUncompleted());
        assertEquals(0,testList.numOfCompleted());
        Task cpsc210 = testList.getUncompleted().get(0);
        Task math200 = testList.getUncompleted().get(1);
        Task math221 = testList.getUncompleted().get(2);
        assertEquals("CPSC210_project",cpsc210.getLabel());
        assertEquals(1,cpsc210.getCaseNum());
        assertEquals("MATH200_quiz",math200.getLabel());
        assertEquals(2,math200.getCaseNum());
        assertEquals("MATH221_webwork",math221.getLabel());
        assertEquals(3,math221.getCaseNum());
    }

    @Test
    public void testDeleteUnCompleted() {
        testList.addUncompleted("CPSC210_project");
        testList.addUncompleted("MATH200_quiz");
        testList.deleteUncompleted(1);

        assertEquals(1,testList.numOfUncompleted());
        Task remaining = testList.getUncompleted().get(0);
        assertEquals("MATH200_quiz",remaining.getLabel());
        assertEquals(1,remaining.getCaseNum());

    }

    @Test
    public void testDeleteCompleted() {
        testList.addUncompleted("CPSC210_project");
        testList.addUncompleted("MATH200_quiz");
        testList.completeFirst();
        testList.completeFirst();
        testList.deleteCompleted(1);

        assertEquals(0,testList.numOfUncompleted());
        assertEquals(1,testList.numOfCompleted());
        Task remaining = testList.getIsCompleted().get(0);
        assertEquals(1,remaining.getCaseNum());
        assertEquals("MATH200_quiz",remaining.getLabel());
    }


    @Test
    public void testSetName() {
        testList.setName("lista");
        assertEquals("lista",testList.getName());
    }


    @Test
    public void testCompleteFirst() {
        testList.addUncompleted("CPSC210_project");
        testList.addUncompleted("MATH200_quiz");
        testList.completeFirst();

        assertEquals(1,testList.numOfUncompleted());
        assertEquals(1,testList.numOfCompleted());

        Task a = testList.getUncompleted().get(0);
        Task b = testList.getIsCompleted().get(0);
        assertEquals("MATH200_quiz",a.getLabel());
        assertEquals(1,a.getCaseNum());
        assertEquals("CPSC210_project",b.getLabel());
        assertEquals(1,b.getCaseNum());
        assertTrue(b.getIsCompleted());
        assertFalse(b.getIsStarred());
    }

    @Test
    void testCompleteFirstTwice() {
        testList.addUncompleted("CPSC210_project");
        testList.addUncompleted("MATH200_quiz");
        testList.addUncompleted("term essay");
        testList.completeFirst();
        testList.completeFirst();

        assertEquals(1,testList.numOfUncompleted());
        assertEquals(2,testList.numOfCompleted());

        Task a = testList.getUncompleted().get(0);
        Task b = testList.getIsCompleted().get(0);
        Task c = testList.getIsCompleted().get(1);
        assertEquals("term essay",a.getLabel());
        assertEquals(1,a.getCaseNum());
        assertEquals("CPSC210_project",b.getLabel());
        assertEquals(1,b.getCaseNum());
        assertEquals("MATH200_quiz",c.getLabel());
        assertEquals(2,c.getCaseNum());
    }


    @Test
    public void testStarFirst() {
        testList.addUncompleted("CPSC210_project");
        testList.addUncompleted("MATH200_quiz");
        testList.addUncompleted("MATH221_webwork");
        testList.star(1);

        Task first = testList.getUncompleted().get(0);
        assertEquals("CPSC210_project",first.getLabel());
        assertEquals(1,first.getCaseNum());
        assertTrue(first.getIsStarred());
    }

    @Test
    public void testStarMiddle() {
        testList.addUncompleted("CPSC210_project");
        testList.addUncompleted("MATH200_quiz");
        testList.addUncompleted("MATH221_webwork");
        testList.star(2);

        Task first = testList.getUncompleted().get(0);
        Task second = testList.getUncompleted().get(1);
        Task third = testList.getUncompleted().get(2);
        assertEquals("MATH200_quiz",first.getLabel());
        assertEquals(1,first.getCaseNum());
        assertTrue(first.getIsStarred());
        assertEquals("CPSC210_project",second.getLabel());
        assertEquals(2,second.getCaseNum());
        assertFalse(second.getIsStarred());
        assertEquals("MATH221_webwork",third.getLabel());
        assertEquals(3,third.getCaseNum());
        assertFalse(third.getIsStarred());

    }

    @Test
    public void  testNumOfStarred() {
        testList.addUncompleted("CPSC210_project");
        testList.addUncompleted("MATH200_quiz");
        assertEquals(0,testList.numOfStarred());

        testList.star(1);
        assertEquals(1,testList.numOfStarred());
        testList.completeFirst();
        assertEquals(0,testList.numOfStarred());
    }

    @Test
    public void testCatchException() {
        testList.addUncompleted("");
        testList.addCompleted("");
    }


}
