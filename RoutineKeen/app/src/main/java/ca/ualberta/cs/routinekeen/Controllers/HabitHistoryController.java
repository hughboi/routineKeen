package ca.ualberta.cs.routinekeen.Controllers;


import android.net.Network;
import android.util.Pair;

import java.util.LinkedList;
import java.util.Queue;

import ca.ualberta.cs.routinekeen.Exceptions.NetworkUnavailableException;
import ca.ualberta.cs.routinekeen.Helpers.MarkedForStatus;
import ca.ualberta.cs.routinekeen.Models.HabitHistory;
import ca.ualberta.cs.routinekeen.Models.HabitEvent;

public class HabitHistoryController {
    private HabitHistoryController(){}
    private static HabitHistory habitHistory = null;
    private static IOManager ioManager = IOManager.getManager();
    private static Queue<String> offlineDeletedEventIds = new LinkedList<String>();
    private static Queue<Pair<HabitEvent,Integer>> offlineUpdatedEvents = new LinkedList<>();
    private static Queue<Pair<HabitEvent,Integer>> offlineAddedEvents = new LinkedList<>();

    public static void initHabitHistory() {
            String userID = UserSingleton.getCurrentUser().getUserID();
            habitHistory = ioManager.loadUserHabitHistory(userID);
    }

    public static HabitHistory getHabitHistory(){
        if (habitHistory == null) {
            initHabitHistory();
        }
        return habitHistory;
    }

    public static void addHabitEvent(HabitEvent event){
        String userID = UserSingleton.getCurrentUser().getUserID();
        event.setAssociatedUserID(userID);
        String assignedEventID = null;
        try{
            assignedEventID = ioManager.addHabitEvent(event);
        } catch (NetworkUnavailableException e){
            // mark the event to be added/updated to elastic search
            // upon reconnection with an available network
            event.setMarkedForStatus(MarkedForStatus.ADD);
        }
        event.setEventID(assignedEventID);
        getHabitHistory().addHabitEvent(event);
        ioManager.saveHabitHistory(getHabitHistory());
    }

    public static void updateHabitEvent(){

    }

    public static void removeHabitEvent(HabitEvent event){
        try{
            ioManager.deleteHabitEvent(event.getEventID());
        } catch (NetworkUnavailableException e){
            // enqueue the delete event for execution when connection available
            offlineDeletedEventIds.add(event.getEventID());
        }
        getHabitHistory().removeHabitEvent(event);
        ioManager.saveHabitHistory(getHabitHistory());
    }

    public static HabitEvent getHabitEvent(int position)
    {
        return habitHistory.getHabitEvent(position);
    }

    public static void executeOfflineQueuedRequests(){
        for(HabitEvent event: habitHistory.getEvents()){
            if(event.getMarkedForStatus() == MarkedForStatus.ADD){
                try{
                    event.setEventID(ioManager.addHabitEvent(event));
                } catch (NetworkUnavailableException e){
                    continue;
                }
                event.setMarkedForStatus(MarkedForStatus.NONE);
            }

            if (event.getMarkedForStatus() == MarkedForStatus.UPDATE){
                try{
                    ioManager.updateHabitEvent(event);
                } catch (NetworkUnavailableException e){
                    continue;
                }
                event.setMarkedForStatus(MarkedForStatus.NONE);
            }
        }

        for(int i = 0; i < offlineDeletedEventIds.size(); i++){
            String eventID = offlineDeletedEventIds.peek();
            try{
                ioManager.deleteHabitEvent(eventID);
            } catch (NetworkUnavailableException e){
                continue;
            }
            offlineDeletedEventIds.poll();
        }
    }

    public static void saveHabitHistory(){
        ioManager.saveHabitHistory(habitHistory);
    }
}
