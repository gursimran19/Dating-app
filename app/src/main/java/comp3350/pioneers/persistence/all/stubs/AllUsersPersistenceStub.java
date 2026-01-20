package comp3350.pioneers.persistence.all.stubs;
import comp3350.pioneers.objects.*;
import comp3350.pioneers.persistence.all.IAllUsersPersistence;

import java.util.ArrayList;
import java.util.List;



/*
    Class is used to hold a stub/dummy database with a few simple entries
    Author: Karl Shewchuk

 */
public class AllUsersPersistenceStub implements IAllUsersPersistence {

    private List<String[]> allUsers;



    public String[] getUserBio(){
        return new String[3];

    }



    public AllUsersPersistenceStub(){


        this.allUsers = new ArrayList<>();
        String[] user1 = {"Jimmy", "mycat","A bio","coffee","biking","cars", "cats" , "false"};
        String[] user2 = {"Bobby","12345", " another Bio", "running", " biking" , "drinking", "smoking", "true"};

        allUsers.add(user1);
        allUsers.add(user2);
    }

    //Does a linear search of the stub database.
    //Returns a string[] with user info if found
    //Returns a string[] with null values if not found

    public String[] getUserData(String user){
        //assert user == null : "User was null";


        boolean found = false;
        String[] foundUser = new String[2];
        String[] tempUser = new String[2];
        int i = 0;
        int size = this.allUsers.size();

        while(!found && i < size ){
            tempUser = allUsers.get(i);
            if(tempUser[0].compareToIgnoreCase(user) == 0){
                foundUser = tempUser;
                found = true;
            }
            i++;
        }

        return foundUser;
    }

    //The search code is duplicated for now, but once we switch to a database with SQL we wont need
    //the linear search in getUserData()
    //Logic layer checks for empty string
    /*
    *   searchForUser(String user)
    *       Parameters:
    *           String User - The user name we are searching the database for
    *       Logic layer does error checking (looking for empty strings, etc)
    *
    *   Returns: True if found, false if not in database
    *
    *
     */

    public boolean searchForUser(String user){
        boolean found = false;
        String[] tempUser;
        int i,size;

        i = 0;
        size = this.allUsers.size();

        while(!found && i < size){
            tempUser = this.allUsers.get(i);
            if(tempUser[0].compareToIgnoreCase(user) == 0){
                found = true;
            }
            i++;
        }
        return found;
    }

    /*
     *   addUserToDatabase()
     *       Parameters:
     *          User user - A user object
     *       Retrieves fields from the user object and adds them to the database
     *
     *   Returns: True if success, false if failure
     *
     *
     */
   /* public boolean addUserToDatabase(User user){
        boolean addSuccess = true;
        String[] toAdd = new String[2];

        toAdd[0] = user.getUsername();
        toAdd[1] = user.getPassword();

        allUsers.add(toAdd);


        return addSuccess;
    }*/
    public boolean addUserToDatabase(User user){
        if (searchForUser(user.getUsername())) {
            System.out.println("User already exists");
            return false;  // User already exists, so return false
        }
        String[] interests = user.getUserInterests();
        String match = String.valueOf(user.getUserMatchType());
        String[] toAdd = {user.getUsername(), user.getPassword(), user.getUserBio(), interests[0], interests[1], interests[2],interests[3] , match };
        allUsers.add(toAdd);
        System.out.println("User added successfully: " + user.getUsername());

        return true;  // User was added successfully
    }



    public boolean updateUserBio(String username, String bio){

        boolean found = false;
        String[] tempUser;
        int i,size;

        i = 0;
        size = this.allUsers.size();

        while(!found && i < size){
            tempUser = this.allUsers.get(i);
            if(tempUser[0].compareToIgnoreCase(username) == 0){
            tempUser[2] = bio;
            allUsers.set(i,tempUser);
            found = true;
            }
            i++;
        }
        return found;

    }


    public boolean updateUserInterests(String username,String interest1, String interest2,String interest3, String interest4){
        boolean found = false;
        String[] tempUser;
        int i,size;

        i = 0;
        size = this.allUsers.size();
        //String[] user2 = {"Bobby","12345", " another Bio", "running", " biking" , "drinking", "smoking", "TRUE"};
        while(!found && i < size){
            tempUser = this.allUsers.get(i);
            if(tempUser[0].compareToIgnoreCase(username) == 0){
                tempUser[3] = interest1;
                tempUser[4] = interest2;
                tempUser[5] = interest3;
                tempUser[6] = interest4;
                allUsers.set(i,tempUser);
                found = true;
            }
            i++;
        }
        return found;

    }


    public boolean updateMatchingType(String userName, boolean romantic){
        String matchType = Boolean.toString(romantic);
        boolean found = false;
        String[] tempUser;
        int i,size;

        i = 0;
        size = this.allUsers.size();
        //String[] user2 = {"Bobby","12345", " another Bio", "running", " biking" , "drinking", "smoking", "TRUE"};
        while(!found && i < size){
            tempUser = this.allUsers.get(i);
            if(tempUser[0].compareToIgnoreCase(userName) == 0){
                tempUser[7] = matchType;
                allUsers.set(i,tempUser);
                found = true;
            }
            i++;
        }
        return found;

    }

    public boolean getUserMatchType(String username){

        boolean found = false;
        String[] tempUser;
        boolean matchType = false;
        int i,size;

        i = 0;
        size = this.allUsers.size();
        //String[] user2 = {"Bobby","12345", " another Bio", "running", " biking" , "drinking", "smoking", "TRUE"};
        while(!found && i < size){
            tempUser = this.allUsers.get(i);
            if(tempUser[0].compareToIgnoreCase(username) == 0){

                matchType = Boolean.parseBoolean(tempUser[7]);
                found = true;
            }
            i++;
        }
        return matchType;


    }

    public String[] getUserBio(String username){

        boolean found = false;
        String[] tempUser;
        String[] result = new String[5];
        int i,size;

        i = 0;
        size = this.allUsers.size();
        //String[] user2 = {"Bobby","12345", " another Bio", "running", " biking" , "drinking", "smoking", "TRUE"};
        while(!found && i < size){
            tempUser = this.allUsers.get(i);
            if(tempUser[0].compareToIgnoreCase(username) == 0){
                result[0] = tempUser[2];
                result[1] = tempUser[3];
                result[2] = tempUser[4];
                result[3] = tempUser[5];
                result[4] = tempUser[6];

                found = true;
            }
            i++;
        }
        return result;
    }
    @Override
    public List<User> getAllUsers() {
        List<User> all = new ArrayList<>();
        for (String[] data : this.allUsers) {
            if (data != null && data.length >= 2) {
                all.add(new User(data[0], data[1])); // username and password
            }
        }
        return all;
    }


    @Override
    public boolean removeUserFromDB(String username) {
        return false;
    }
}
