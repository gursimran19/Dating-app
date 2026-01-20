package comp3350.pioneers.persistence.all;
/*
    interface for the stub database

    Author: Karl Shewchuk

 */
import java.util.List;

import comp3350.pioneers.objects.*;
public interface IAllUsersPersistence {


    /*
    Parameters:
        String[] userName - the username of the person trying to log in

        Returns: A string array where the values are
            [0] - the user name
            [1] - the user password
            this will be extended as the project continues
        If user is not in database, it will return a string array with NULL values
     */
    String[] getUserData(String user);



    /*
        searchForUser()
            Used to search for a user by username in the database
        Parameters:
            String user
                The username to search for
        Return
            true if found in database. False if not in database
     */
    boolean searchForUser(String user);


    //Adds a user Class to the database
    /*
        addUserToDatabase:
            Adds a user Class to the database
        Parameters: User user
            The user object containing information about the user to be added to the database
        Returns: true on success
        CALLER SHOULD INSURE USER NOT ALREADY IN DB BEFORE ADDING.
        Db will catch it but it's safer if you check first
     */
    boolean addUserToDatabase(User user);

    /*
        Retrieves the bio information for a user.
        Returns a string array in the format
        [0] bio
        [1] interest 1
        [2] interest 2
        [3] Interest 3
        [4] Interest 4
        Returns Null values in string if user not in DB
     */
    String[] getUserBio(String username);


    //Returns the Match type of a user
    //False for friendship
    //True for Romantic
    boolean getUserMatchType(String username);

    /*
    Used to update the user's bio in the database.
    Returns True on success
 */
    boolean updateUserBio(String username, String bio);


    /*
        Used to update the interests of the user.
        Parameters: string[] interests
            The string array containing the interests of the user. Must be passed in this format
            [0] - The User Name
            [1] - Interest 1
            [2] - Interest 2
            [3] - Interest 3
            [4] - Interest 4

        Returns True on success

     */
    boolean updateUserInterests(String username,String interest1, String interest2,String interest3, String interest4);


    /*
        Used to update the users use intent for the app
        Parameters: boolean romantic
            True if user selects they are looking for a romantic
            False if user is looking for friendship

       Returns True on success
     */
    boolean updateMatchingType(String userName, boolean romantic);

    //void closeDatabase();

    List<User> getAllUsers();

    //Used to remove a user from the DB. So far only used for testing, but future iterations would use
    //To delete your account
    public boolean removeUserFromDB(String username);

}
