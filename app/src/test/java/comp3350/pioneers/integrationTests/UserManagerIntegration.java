package comp3350.pioneers.integrationTests;
import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import android.content.Context;

import org.junit.Before;
import org.junit.Test;

import java.io.File;

import comp3350.pioneers.business.MatchManager;
import comp3350.pioneers.business.Services;
import comp3350.pioneers.objects.Match;
import comp3350.pioneers.objects.User;
import comp3350.pioneers.persistence.all.IAllUsersPersistence;
import comp3350.pioneers.business.UserManager;
import comp3350.pioneers.persistence.all.IMatchPersistence;



public class UserManagerIntegration {


    private UserManager userMan;
    private MatchManager matchMan;
    private IAllUsersPersistence userDB;
    private IMatchPersistence matchDB;




    @Before
    public void setUp(){
        Context mockContext = mock(Context.class);
        File mockFile = mock(File.class);
        when(mockContext.getFilesDir()).thenReturn(mockFile);
        when(mockFile.getAbsolutePath()).thenReturn("/data/user/0/comp3350.pioneers/files");
        Services.initializeDb(mockContext,"/TestDB");
        userDB = Services.getUserDB();
        matchDB = Services.getMatchDB();
        matchMan = new MatchManager(userDB,matchDB);
        userMan = new UserManager(userDB, matchMan);

    }


    //buildUserDSO
    @Test
    public void buildUserDSO(){

        assertTrue(userMan.buildUserDSO("Sally","sallypass"));
        assertFalse(userMan.buildUserDSO("Notindb","notindb"));
        assertFalse(userMan.buildUserDSO(null,null));
        assertFalse(userMan.buildUserDSO("",""));

    }

    //        updateUserBio
    @Test
    public void testUpdateUserBio(){
        User testUser = new User("TestPerson","TestPassword");
        userDB.addUserToDatabase(testUser);
        assertTrue(userMan.updateUserBio("TestPerson","new bio","jumping","flying","drinking","sailing"));
        String[] bio = userDB.getUserBio("TestPerson");
        assertEquals("new bio",bio[0]);
        assertEquals("jumping",bio[1]);
        assertEquals("flying",bio[2]);
        assertEquals("drinking",bio[3]);
        assertEquals("sailing",bio[4]);
        assertFalse(userMan.updateUserBio("TestPerson",null,null,null,null,null));
        assertFalse(userMan.updateUserBio(null,null,null,null,null,null));
        assertFalse(userMan.updateUserBio("TestPerson",null,"jumping","flying","drinking","sailing"));
        assertFalse(userMan.updateUserBio("TestPerson","new bio",null,"flying","drinking","sailing"));
        assertFalse(userMan.updateUserBio("TestPerson","new bio","jumping",null,"drinking","sailing"));
        assertFalse(userMan.updateUserBio("TestPerson","new bio","jumping","flying",null,"sailing"));
        assertFalse(userMan.updateUserBio("TestPerson","new bio","jumping","flying","drinking",null));

        //clean up
        assertTrue(userDB.removeUserFromDB("TestPerson"));

    }
    //        updateUserMatchingType
    @Test
    public void testUpdateUserMatchingType(){
        User testUser = new User("TestPerson","TestPassword");
        Services.addUserDSO(testUser);
        userDB.addUserToDatabase(testUser);
        assertTrue(userDB.getUserMatchType("TestPerson"));
        assertTrue(userMan.updateUserMatchingType("TestPerson", false)); //Default is true so we will change it
        assertFalse(userDB.getUserMatchType("TestPerson"));
        assertTrue(userMan.updateUserMatchingType("TestPerson", true)); //flip it back
        assertTrue(userDB.getUserMatchType("TestPerson"));

        assertFalse(userMan.updateUserMatchingType("", true));
        assertFalse(userMan.updateUserMatchingType(null,true));
        //Clean up
        userDB.removeUserFromDB("TestPerson");

    }



//        resetUserDso
    @Test
    public void testResetUserDso(){
        User testUser = new User("TestPerson", "TestPassword");
        Services.addUserDSO(testUser);
        assertTrue(userMan.resetUserDso());
        assertNull(Services.getUserDSO());

    }


//        getRandomMatch
    @Test
    public void testGetRandomMatch(){
        userMan.buildUserDSO("Sally","sallypass");
        Match rando = userMan.getRandomMatch();
        assertNotNull(rando);
        assertFalse(rando.getUserName().isEmpty());

        Services.addUserDSO(null);
    }

}
