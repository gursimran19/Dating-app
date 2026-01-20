/**
 * This class interacts with the persistance layer to fetch the user data
 * Builds and store the DSO object for the gui access
 */
package comp3350.pioneers.business;
import android.util.Log;

import java.util.List;

import comp3350.pioneers.persistence.all.IAllUsersPersistence;
import comp3350.pioneers.objects.*;

public class UserManager {

    private IAllUsersPersistence userDB = null;
    private MatchManager matchManager;


    public UserManager(MatchManager matchManager){
        this.userDB = Services.getUserDB();
        this.matchManager = matchManager;
    }

    public UserManager(IAllUsersPersistence userDB , MatchManager matchManager){
        this.userDB = userDB;
        this.matchManager = matchManager;
    }

    /**
     * Builds and stores the user DSO object globally for the application to access
     */
    public boolean buildUserDSO(String username, String password){
        //making sure valid input
        List<User> allUsers = userDB.getAllUsers();


        if (username == null || username.isEmpty() || password == null || password.isEmpty()) {
            return false;
        }

        //create the user object
        User userDSO;
        try {
            userDSO = new User(username, password);
        } catch (IllegalArgumentException e) {
            return false;
        }

        boolean success = false;

        if(userDB.searchForUser(username)){
            try {
                //getting user from database
                String[] userBio = userDB.getUserBio(username);
                boolean matchType = userDB.getUserMatchType(username);

                //set bio if its not null
                if (userBio != null) {
                    userDSO.setUserBio(userBio);
                }

                userDSO.setUserMatchType(matchType);
                List<Match> userMatches = matchManager.getUserMatches(username);
                userDSO.setMatches(userMatches);

                Services.addUserDSO(userDSO);

                success = true;
            } catch (Exception e) {
                System.out.println("UserManager Error in buildUserDSO: " + e.getMessage());
                success = false;
            }
        }

        return success;
    }





    /*
    Returns:
    string[0] - interest1
    string[1] - interest2
    string[2] - interest3
    string[3] - interest4


     */
    public String[] getInterestsFromDSO(){
        User dso = Services.getUserDSO();
        return dso.getUserInterests();
    }


    public boolean updateUserBio(String username, String newbio, String interest1, String interest2, String interest3, String interest4){

        boolean success = false;
        User dso = Services.getUserDSO();
        String[] toUpdate = new String[5];
        if(username != null && newbio != null && interest1 != null && interest2 != null && interest3 != null && interest4 != null){
            toUpdate[0] = newbio;
            toUpdate[1] = interest1;
            toUpdate[2] = interest2;
            toUpdate[3] = interest3;
            toUpdate[4] = interest4;

            dso.setUserBio(toUpdate);
            success = userDB.updateUserBio(username,newbio);
            success = userDB.updateUserInterests(username, interest1,interest2,interest3,interest4);

        }

        return success;
    }

    public boolean updateUserMatchingType(String username, boolean romantic){
        boolean success = false;

        if(username != null && !username.isEmpty()){
            User userDso = Services.getUserDSO();

            success = userDB.updateMatchingType(username,romantic);
            if(success){
                userDso.setUserMatchType(romantic);
            }

        }

        return success;
    }


    public boolean resetUserDso(){

        boolean success = false;
        Services.addUserDSO(null);

        if(Services.getUserDSO() == null){
            success = true;
        }

        return success;
    }


    // get random match from database
    public Match getRandomMatch(){
        User curr = Services.getUserDSO();

        return matchManager.getRandomMatch(curr.getUsername());
    }

}