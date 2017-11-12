package ca.ualberta.cs.routinekeen.Controllers;

import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

import ca.ualberta.cs.routinekeen.Models.HabitHistory;
import ca.ualberta.cs.routinekeen.Models.HabitEvent;
import ca.ualberta.cs.routinekeen.Models.Photo;
/**
 * Created by Mikee V on 2017-11-08.
 */

public class HabitHistoryController implements Observer{
    private HabitHistoryController(){}
    private static HabitHistory habitHistory = null;

    public static HabitHistory getHabitHistory(){
        if (habitHistory == null) {
            habitHistory = IOManager.getManager().loadHabitHistory();
        }
        return habitHistory;
    }

    public void addObvToHistory(){
        getHabitHistory().addObserver(this);
    }

    public static void saveHabitHistory(){
        IOManager.getManager().saveHabitHistory(getHabitHistory());
    }

    @Override
    public void update(Observable observable, Object data) {

    }

}