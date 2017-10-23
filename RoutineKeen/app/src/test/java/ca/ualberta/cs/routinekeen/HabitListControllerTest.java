package ca.ualberta.cs.routinekeen;
import java.util.Date;

import ca.ualberta.cs.routinekeen.Models.Habit;

import static org.junit.Assert.*;

/**
 * Created by Saddog on 10/21/2017.
 */

public class HabitListControllerTest {
    public void testHabitListController() throws Exception {
        HabitListController testController = new HabitListController();
        String habitTitle = "test habit title";
        String habitReason = "test habit reason";
        Date testDate = new Date();
        Habit testHabit = new Habit(habitTitle,habitReason,testDate);
        testController.getHabitList().add(testHabit);
        assertTrue(testController.getHabitList().size().equal(1));
        assertTrue(testController.getHabitList().getHabit(habitTitle).equal(testHabit));

    }
}
