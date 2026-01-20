/**
 * LogicManager.java
 *
 *  This class handles the user authentication and registration by interacting with database and gui.
 *
 *  Author: Gursimran Singh
 */
package comp3350.pioneers.business;

import android.util.Log;
import comp3350.pioneers.objects.User;
import comp3350.pioneers.persistence.all.IAllUsersPersistence;


public class LogicManager {
    private final IAllUsersPersistence userDB;//Accessing database
    private String userName;
    /*
     * Constructor intializing the logic manager
     */

    public LogicManager(){
        userDB = Services.getUserDB();
    }
    public LogicManager(IAllUsersPersistence userDB) {
        this.userDB = userDB;
    }


    // Validates user login by checking checking the password and username in database
    public boolean validateUser(String username, String password) {
        if (username.isEmpty() || password.isEmpty()) {
            return false; // Prevent empty inputs
        }

        String[] userData = userDB.getUserData(username);
        userName = username;
        //Log.d("Login try ", username);
        boolean sucess = userData[0] != null && userData[1].equals(password);
        if (sucess) {
            System.out.println("Login Successful");
        }
        return sucess;

    }

    // Registers the user if not already in the database
    public boolean registerUser(String username, String password) {
        /*check if the username or password field is empty*/
        if (username.isEmpty() || password.isEmpty()) {
            return false;
        }
        /*if user with the same username already exists return false so no success*/
        if (userDB.searchForUser(username)) {
            return false;
        }
        System.out.println("Trying to register user: " + username);
        /*if everything is correct add user to the database*/
        User newUser = new User(username, password);
        boolean result = userDB.addUserToDatabase(newUser);
        return result;
    }

    public String getUserName(){
        return userName;
    }




}
