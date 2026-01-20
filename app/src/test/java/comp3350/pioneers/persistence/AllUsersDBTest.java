package comp3350.pioneers.persistence;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import android.content.Context;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

import java.io.File;
import java.util.List;

import comp3350.pioneers.business.Services;
import comp3350.pioneers.objects.User;
import comp3350.pioneers.persistence.all.IAllUsersPersistence;

public class AllUsersDBTest {
    private IAllUsersPersistence allUserDb;
    @Before
    public void setUp(){
        Context mockContext = mock(Context.class);
        File mockFile = mock(File.class);
        when(mockContext.getFilesDir()).thenReturn(mockFile);
        when(mockFile.getAbsolutePath()).thenReturn("/data/user/0/comp3350.pioneers/files");

        Services.initializeDb(mockContext, "/TestDB");

        allUserDb = Services.getUserDB();


    }

    @Test
    public void testGetUserData(){
        String[] test = allUserDb.getUserData("Sally");
        assertEquals("Sally",test[0]);
        assertEquals("sallypass",test[1]);
        test = allUserDb.getUserData("notindb");
        assertNull(test[0]);
        assertNull(test[1]);

    }

//
//    /*
//        searchForUser()
//            Used to search for a user by username in the database
//        Parameters:
//            String user
//                The username to search for
//        Return
//            true if found in database. False if not in database
//     */
//    boolean searchForUser(String user);
    @Test
    public void testSearchForUser(){
        assertTrue(allUserDb.searchForUser("Sally"));
        assertFalse(allUserDb.searchForUser("not in db"));
        //Not much to do as userManager does error handling
    }

//
//
//    //Adds a user Class to the database
//    /*
//        addUserToDatabase:
//            Adds a user Class to the database
//        Parameters: User user
//            The user object containing information about the user to be added to the database
//        Returns: true on success
//        CALLER SHOULD INSURE USER NOT ALREADY IN DB BEFORE ADDING.
//        Db will catch it but it's safer if you check first
//     */
//    boolean addUserToDatabase(User user);

    @Test
    public void testAddUserToDatabase(){

        User testUser = new User("Test1","testpass");
        assertTrue(allUserDb.addUserToDatabase(testUser));
        String[] testData = allUserDb.getUserData("Test1");
        assertEquals("Test1",testData[0]);
        assertEquals("testpass",testData[1]);
        assertFalse(allUserDb.addUserToDatabase(testUser)); //already in db

        //clean up
        allUserDb.removeUserFromDB("Test1");

    }




//
//    /*
//        Retrieves the bio information for a user.
//        Returns a string array in the format
//        [0] bio
//        [1] interest 1
//        [2] interest 2
//        [3] Interest 3
//        [4] Interest 4
//        Returns Null values in string if user not in DB
//     */
//    String[] getUserBio(String username);

    @Test
    public void testGetUserBio(){
        String[] testBio = allUserDb.getUserBio("Sally");
        assertEquals("I love gold",testBio[0]);
        assertEquals("Music",testBio[1]);
        assertEquals("Sports",testBio[2]);
        assertEquals("Gaming",testBio[3]);
        assertEquals("Reading",testBio[4]);

        testBio = allUserDb.getUserBio("Not in Db");
        assertNull(testBio[0]);
        assertNull(testBio[1]);
        assertNull(testBio[2]);
        assertNull(testBio[3]);
        assertNull(testBio[4]);
        //'Sally','sallypass','I love gold','Music','Sports','Gaming','Reading', TRUE)")


    }

//
//
//    //Returns the Match type of a user
//    //False for friendship
//    //True for Romantic
//    boolean getUserMatchType(String username);

    @Test
    public void testGetUserMatchType(){
        assertTrue(allUserDb.getUserMatchType("Sally"));
        assertFalse(allUserDb.getUserMatchType("Frank"));
        assertFalse(allUserDb.getUserMatchType("Not in db"));
    }



//
//    /*
//    Used to update the user's bio in the database.
//    Returns True on success
// */
//    boolean updateUserBio(String username, String bio);

    @Test
    public void testUpdateUserBio(){
        User testUser = new User("Test1","testpass");
        allUserDb.addUserToDatabase(testUser);
        assertTrue(allUserDb.updateUserBio("Test1","Test Bio"));
        String[] test = allUserDb.getUserBio("Test1");
        assertEquals("Test Bio", test[0]);
        //clean up
        allUserDb.removeUserFromDB("Test1");
    }
//
//
//    /*
//        Used to update the interests of the user.
//        Parameters: string[] interests
//            The string array containing the interests of the user. Must be passed in this format
//            [0] - The User Name
//            [1] - Interest 1
//            [2] - Interest 2
//            [3] - Interest 3
//            [4] - Interest 4
//
//        Returns True on success
//
//     */
//    boolean updateUserInterests(String username,String interest1, String interest2,String interest3, String interest4);


    @Test
    public void testUpdateUserInterests(){
        User testUser = new User("Test1","testpass");
        allUserDb.addUserToDatabase(testUser);
        assertTrue(allUserDb.updateUserInterests("Test1","int1","int2","int3","int4"));
        String[] testBio = allUserDb.getUserBio("Test1");
        assertEquals("int1",testBio[1]);
        assertEquals("int2",testBio[2]);
        assertEquals("int3",testBio[3]);
        assertEquals("int4",testBio[4]);

        assertFalse(allUserDb.updateUserInterests("Not in Db","int1","int2","int3","int4"));
        //Clean up
        allUserDb.removeUserFromDB("Test1");

    }
//
//    /*
//        Used to update the users use intent for the app
//        Parameters: boolean romantic
//            True if user selects they are looking for a romantic
//            False if user is looking for friendship
//
//       Returns True on success
//     */
//    boolean updateMatchingType(String userName, boolean romantic);

    @Test
    public void testUpdateMatchingType(){
        User testUser = new User("Test1","testpass");
        allUserDb.addUserToDatabase(testUser);
        assertTrue(allUserDb.updateMatchingType("Test1",false));
        assertFalse(allUserDb.getUserMatchType("Test1"));
        assertTrue(allUserDb.updateMatchingType("Test1",true));
        assertTrue(allUserDb.getUserMatchType("Test1"));


        allUserDb.removeUserFromDB("Test1");
    }
//
//    //void closeDatabase();
//
//    List<User> getAllUsers();

    @Test
    public void testGetAllUsers(){
        List<User> allUsers = allUserDb.getAllUsers();
        assertNotNull(allUsers);
        assertFalse(allUsers.isEmpty());

    }
}
