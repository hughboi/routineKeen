package ca.ualberta.cs.routinekeen.Controllers;

import android.content.Context;

import ca.ualberta.cs.routinekeen.Models.HabitHistory;
import ca.ualberta.cs.routinekeen.Models.HabitList;
import ca.ualberta.cs.routinekeen.Models.User;
import ca.ualberta.cs.routinekeen.Models.UserList;

/**
 * Created by hughc on 2017-11-10.
 */

public class IOManager {
    private LocalDataManager localDM;
    private NetworkDataManager networkDm;
    private Context context;

    static private IOManager ioManager = null;

    public static void initManager(Context context) {
        if (context == null) {
            throw new RuntimeException("Missing Context");
        }
        ioManager = new IOManager(context);
    }

    public static IOManager getManager() {
        if (ioManager == null) {
            throw new RuntimeException("Did not initialize IOManager");
        }
        return ioManager;
    }

    public IOManager(Context context) {
        this.context = context;
        LocalDataManager.InitManager(context);
        localDM = LocalDataManager.getManager();
    }

    public UserList loadUserList() {
        return localDM.loadUserList();
    }

    public void loadSharedPrefs(String... prefs){
        localDM.loadSharedPrefs(prefs);
    }

    public void saveUserList(UserList userList) {
        localDM.saveUserList(userList);
    }

    public void saveHabitList(HabitList habitList) {
        localDM.saveHabitList(habitList);
    }

    public HabitList loadHabitList() {
        return ( localDM.loadHabitList() );
    }

    public HabitHistory loadHabitHistory() {
        return localDM.loadHabitHistory();
    }

    public void saveHabitHistory(HabitHistory habitHistory) {
        localDM.saveHabitHistory(habitHistory);
    }
}