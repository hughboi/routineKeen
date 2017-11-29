package ca.ualberta.cs.routinekeen.Controllers;

import android.net.Network;
import android.util.Log;

import org.apache.commons.lang3.ObjectUtils;

import java.util.ArrayList;
import java.util.Calendar;

import ca.ualberta.cs.routinekeen.Exceptions.NetworkUnavailableException;
import ca.ualberta.cs.routinekeen.Models.Habit;
import ca.ualberta.cs.routinekeen.Models.HabitList;
import ca.ualberta.cs.routinekeen.Models.User;

/**
 * Controller used to access and modify Habits in the current user's HabitList
 * @author  RoutineKeen
 * @see     IOManager
 * @see     HabitList
 * @version 1.0.0
 */
public class HabitListController{
    private static ArrayList typeList = new ArrayList<String>();
    private static HabitList habitList = null;
    private static IOManager ioManager = IOManager.getManager();
    private HabitListController(){}

    public static void initHabitList() throws NetworkUnavailableException{
        try{
            String userID = UserSingleton.getCurrentUser().getUserID();
            habitList = ioManager.loadUserHabitList(userID);
        } catch (NetworkUnavailableException e){
            throw new NetworkUnavailableException("Network unavailable. " +
                    "Please restart the application with a valid connection.");
        }
    }

    /**
     * Returns a list of all habits belonging to the current user.
     * @return  A HabitList containing all the user's habits
     * @see     HabitList
     */
    public static HabitList getHabitList(){
        return habitList;
    }

    public static ArrayList getTypeList(){
        if(habitList != null){
            for (Habit habit : habitList.getHabits()){
                if (typeList.indexOf(habit.getHabitTitle()) == -1){
                    typeList.add(habit.getHabitTitle());
                }
            }
        }
        return typeList;
    }

    /**
     * Returns a list of all habits belonging to the current user that are scheduled for the current
     * day of the week.
     * @return  A HabitList containing today's habits
     * @see     HabitList
     */
    public static HabitList getTodaysHabits() {
        String today = "";
        int day = Calendar.getInstance().get(Calendar.DAY_OF_WEEK);
        switch (day) {
            case Calendar.SUNDAY:   today = "Sun";  break;
            case Calendar.MONDAY:   today = "Mon";  break;
            case Calendar.TUESDAY:  today = "Tue";  break;
            case Calendar.WEDNESDAY:today = "Wed";  break;
            case Calendar.THURSDAY: today = "Thu";  break;
            case Calendar.FRIDAY:   today = "Fri";  break;
            case Calendar.SATURDAY: today = "Sat";  break;
            default:                today = null;   break;
        }
        /*
         Taken from: https://stackoverflow.com/questions/5574673/what-is-the-easiest-way-to-get-
         the-current-day-of-the-week-in-android
         Date: Nov 13, 2017
        */

        HabitList returnList = new HabitList();
        for (Habit x : getHabitList().getHabits()) {
            if (x.getScheduledHabitDays().contains(today)) {
                returnList.addHabit(x);
            }
        }

        return returnList;
    }

    /**
     * Adds the provided habit to the current user's HabitList
     * @param   habit The Habit to be added
     * @see     Habit
     * @see     HabitList
     */
    public static boolean addHabit(Habit habit){
        try{
            ioManager.addHabit(habit);
        } catch (NetworkUnavailableException e){
            return false;
        }
        getHabitList().addHabit(habit);
        saveHabitList();
        return true;
    }

    /**
     * Updates the habit edited and saves it to the network
     * and local storage
     * @param title The title of the updated/unchanged habit
     * @param reason The reason for the updated/unchanged habit
     * @param schedDays The scheduled days for the updated/unchanged habit
     * @param position The position of the habit within the habit list
     */
    public static boolean updateHabit(String title, String reason,
                                   ArrayList<String> schedDays, int position) {
        Habit habitToUpdate = getHabitList().getHabitByPosition(position);
        habitToUpdate.setHabitReason(reason);
        habitToUpdate.setHabitTitle(title);
        habitToUpdate.setScheduledHabitDays(schedDays);
        try{
            ioManager.updateHabit(habitToUpdate);
        } catch (NetworkUnavailableException e){
            return false;
        }
        getHabitList().updateHabit(title, reason, schedDays, position);
        saveHabitList();
        return true;
    }

    /**
     * Deletes the habit from the local and network storage.
     * @param position The position of the habit within the habit list
     * @return void
     */
    public static boolean deleteHabit(int position){
        try{
            ioManager.deleteHabitByType(getHabitList()
                     .getHabitByPosition(position).getHabitTitle());
        } catch (NetworkUnavailableException e){
            return false;
        }
        String title = getHabitList().getHabitByPosition(position).getHabitTitle();
        typeList.remove(title);
        getHabitList().removeHabitByPosition(position);
        saveHabitList();
        return true;
    }

    /**
     * Saves changed to the user's HabitList. Called after additions, deletions, or edits are made
     * to the HabitList
     * @see     IOManager
     * @see     HabitList
     */
    public static void saveHabitList() {
        ioManager.saveHabitList(getHabitList());
    }
}
