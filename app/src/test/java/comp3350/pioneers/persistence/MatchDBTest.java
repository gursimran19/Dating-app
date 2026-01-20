package comp3350.pioneers.persistence;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import java.io.File;
import java.util.List;

import comp3350.pioneers.business.MatchManager;
import comp3350.pioneers.business.Services;
import comp3350.pioneers.objects.Match;
import comp3350.pioneers.persistence.all.IAllUsersPersistence;
import comp3350.pioneers.persistence.all.IMatchPersistence;

import static org.mockito.Mockito.*;

import android.content.Context;

public class MatchDBTest {
    private IMatchPersistence matchDb;
    private IAllUsersPersistence allUserDb;

    @Before
    public void setup(){
        Context mockContext = mock(Context.class);
        File mockFile = mock(File.class);
        when(mockContext.getFilesDir()).thenReturn(mockFile);
        when(mockFile.getAbsolutePath()).thenReturn("/data/user/0/comp3350.pioneers/files");

        Services.initializeDb(mockContext, "/TestDB");
        matchDb = Services.getMatchDB();
        allUserDb = Services.getUserDB();

    }

    @Test
    public void testAddMatch(){
        //No testing for values already in Match table as user calls checkForMatch before adding to table
        assertTrue(matchDb.addMatch("Sally","Jacob"));
        assertTrue(matchDb.checkForMatch("Sally","Jacob")); // Checking to see they are now in match table
        assertFalse(matchDb.addMatch("Sally","Jacob")); //Already Matched
        assertTrue(matchDb.addMatch("Alexander", "Lucas"));

    }

    @Test
    public void testAddReject(){
        assertTrue(matchDb.addReject("Sally","James"));
        assertTrue(matchDb.checkForReject("Sally","James")); //Checking they have been inserted into table
        assertFalse(matchDb.addReject("Sally","James")); //Already Rejected
        assertTrue(matchDb.addReject("Alexander","Mia"));
    }


    @Test
    public void testCheckForMatch(){
        assertTrue(matchDb.checkForMatch("Sally","Olivia")); //Match of users in database
        assertFalse(matchDb.checkForMatch("Not in db", "Olivia")); //User not in db but match is
        assertFalse(matchDb.checkForMatch("Sally","Not in Db")); //User in db but match is not
        assertFalse(matchDb.checkForMatch("not in db", "also not in db")); //Neither in db
    }


    @Test
    public void checkForReject(){
        assertTrue(matchDb.checkForReject("Sally","Daniel"));
        assertFalse(matchDb.checkForReject("Not in db","Olivia"));
        assertFalse(matchDb.checkForReject("Sally","not in db"));
        assertFalse(matchDb.checkForReject("not in db", "also not in db"));

    }

    @Test
    public void testGetAllMatches(){
        List<Match> matchList = matchDb.getAllMatches("Sally");
        assertNotNull(matchList);
        assertFalse(matchList.isEmpty()); //making sure matchlist isn't empty
        MatchManager man = new MatchManager(allUserDb,matchDb);
        Match temp = man.getMatchFromList("Emma",matchList); //Checking to see if One of emmas matches is in the list
        assertNotNull(temp);
        assertEquals("Emma", temp.getUserName());
        assertEquals("Passionate about good food and conversation",temp.getBio());

        temp = man.getMatchFromList("Noah",matchList);
        assertNotNull(temp);
        assertEquals("Noah",temp.getUserName());
        assertEquals("Adventurer seeking meaningful connections",temp.getBio());
        matchList = matchDb.getAllMatches("not in db");
        assertTrue(matchList.isEmpty());

    }

    @Test
    public void testGetRandomMatch(){

        Match randMatch = matchDb.getRandomMatch("Sally");
        assertNotNull(randMatch);
        String randoName = randMatch.getUserName();
        assertFalse(matchDb.checkForMatch("Sally", randoName)); //Checking that user not already matched
        assertFalse(matchDb.checkForReject("Sally",randoName)); //Checking user not already rejected

    }

    @Test
    public void testGetMatchInfo(){
        assertNotNull(matchDb.getMatchInfo("Sally","Benjamin"));
        assertNull(matchDb.getMatchInfo("Not in db","Benjamin"));
        assertNull(matchDb.getMatchInfo("Sally","Not in Db"));
        assertNull(matchDb.getMatchInfo("Not in db", "not in db"));

    }

    @Test
    public void testUpdateChat(){

        assertTrue(matchDb.updateChat("Sally", "Olivia", "Hey there"));
        assertEquals("Hey there", matchDb.getMatchChat("Sally","Olivia"));

    }

    @After
    public void tearDown(){
        matchDb.removeMatch("Sally","Jacob");
        matchDb.removeMatch("Alexander","Lucas");
        matchDb.removeReject("Sally","James");
        matchDb.removeReject("Alexander","Mia");
    }

}
