package comp3350.pioneers.business;

import android.app.Application;
import android.content.Context;

import java.util.List;

import comp3350.pioneers.objects.Match;
import comp3350.pioneers.objects.User;
import comp3350.pioneers.persistence.all.AllUsersDB;
import comp3350.pioneers.persistence.all.DatabaseHelper;
import comp3350.pioneers.persistence.all.IAllUsersPersistence;
import comp3350.pioneers.persistence.all.IMatchPersistence;
import comp3350.pioneers.persistence.all.MatchDB;


/*
Services used to set up and initialize functionality for the Application and store singleton variables


 */
public class Services extends Application {

    private static IAllUsersPersistence allUsersDB = null;
    private static IMatchPersistence matchDB = null;
    private static User userDSO = null;
    private static String dbPath;
    private static String dbName;
    private static List<Match> matchList;
    //private static String dbPathName;

    public static User getUserDSO(){
        return userDSO;
    }

    //After someone logs in. UserManager class should build the user object and then store it here
    //to be accessed globally
    public static void addUserDSO (User userToAdd) {
        userDSO = userToAdd;
    }

    public static IAllUsersPersistence getUserDB(){
        if(allUsersDB == null){
            allUsersDB = new AllUsersDB(dbPath);
        }
        return allUsersDB;
    }

    public static IMatchPersistence getMatchDB(){
        if(matchDB == null){
            matchDB = new MatchDB(dbPath);
        }
        return matchDB;
    }
    public static void initializeDb(Context context, String dbName){

        dbPath = context.getFilesDir().getAbsolutePath() + dbName;
        System.out.println("path in initializeDB is " + dbPath);

        DatabaseHelper.initializeDatabase(dbPath);
    }

}



