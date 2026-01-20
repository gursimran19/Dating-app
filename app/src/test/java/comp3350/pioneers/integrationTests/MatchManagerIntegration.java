package comp3350.pioneers.integrationTests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import android.content.Context;

import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.util.List;

import comp3350.pioneers.business.MatchManager;
import comp3350.pioneers.business.Services;
import comp3350.pioneers.objects.Match;
import comp3350.pioneers.persistence.all.IAllUsersPersistence;
import comp3350.pioneers.persistence.all.IMatchPersistence;

public class MatchManagerIntegration {


    private MatchManager matchManager;
    private IAllUsersPersistence userDB;
    private IMatchPersistence matchDB;

    @Before
    public void setUp() {
        Context mockContext = mock(Context.class);
        File mockFile = mock(File.class);
        when(mockContext.getFilesDir()).thenReturn(mockFile);
        when(mockFile.getAbsolutePath()).thenReturn("/data/user/0/comp3350.pioneers/files");
        Services.initializeDb(mockContext,"/TestDB");
        matchDB = Services.getMatchDB();
        userDB = Services.getUserDB();
        matchManager = new MatchManager(userDB,matchDB);
    }

    @Test
    public void testRejectUser() {
        assertTrue(matchManager.rejectUser("Sally", "Evelyn"));
        assertTrue(matchManager.isRejected("Sally", "Evelyn"));
        assertTrue(matchManager.rejectUser("Sally", "Evelyn")); // already in rejects.
        matchDB.removeMatch("Sally", "Evelyn"); //Clean up

    }

    //createMatch
    @Test
    public void testCreateMatch(){
        assertTrue(matchManager.createMatch("Sally","Evelyn"));
        assertTrue(matchManager.createMatch("Sally","Evelyn")); //already a match.
        assertFalse(matchManager.createMatch("",""));
        assertFalse(matchManager.createMatch(null,null));
        //Clean up
        assertTrue(matchDB.removeMatch("Sally","Evelyn"));

    }
    //getUserMatches
@Test
    public void testGetUserMatches(){

        List<Match> matches = matchManager.getUserMatches("Sally");
        assertNotNull(matches);
        assertFalse(matches.isEmpty());

        matches = matchManager.getUserMatches("");
        assertTrue(matches.isEmpty());
        matches = matchManager.getUserMatches(null);
        assertTrue(matches.isEmpty());


    }

//getMatchInfo
    @Test
    public void testGetMatchInfo(){
        Match testMatch = matchManager.getMatchInfo("Sally","Benjamin");
        String[] matchInfo = matchManager.getMatchInfo(testMatch);
        assertEquals("Benjamin",matchInfo[0]);
        assertEquals("Tech geek with a taste for adventure",matchInfo[1]);
        assertEquals("Technology",matchInfo[2]);
        assertEquals("Gaming",matchInfo[3]);
        assertEquals("Photography",matchInfo[4]);
        assertEquals("Travel",matchInfo[5]);
        // ('Benjamin','Benjamin1','Tech geek with a taste for adventure','Technology','Gaming','Photography','Travel', TRUE)");

        String[] empty = matchManager.getMatchInfo(null);
        assertNull(empty[0]);
        assertNull(empty[1]);
        assertNull(empty[2]);
        assertNull(empty[3]);
        assertNull(empty[4]);
        assertNull(empty[5]);
    }
//getMatchChat

    @Test
    public void testGetMatchChat(){
        matchManager.createMatch("Sally", "Ethan");
        matchManager.createMatch("Ethan","Sally");
        Match testMatch = matchManager.getMatchInfo("Sally","Ethan");
        matchManager.addChatMessage(testMatch,"Sally","Hi there!");
        assertEquals("[Sally]: Hi there!\n",matchManager.getMatchChat(testMatch));
        //Checking to see the other user can see the chat
        testMatch = matchManager.getMatchInfo("Ethan", "Sally");
        assertEquals("[Sally]: Hi there!\n",matchManager.getMatchChat(testMatch));

        assertEquals("",matchManager.getMatchChat(null));


        // clean up

    }
//addChatMessage

    @Test
    public void testAddChatMessage(){
        matchManager.createMatch("Sally", "Ethan");
        matchManager.createMatch("Ethan","Sally");
        Match testMatch = matchManager.getMatchInfo("Sally","Ethan");
        assertTrue(matchManager.addChatMessage(testMatch,"Sally","Hello!"));

        assertFalse(matchManager.addChatMessage(null,"Sallly","Ethan"));
        assertFalse(matchManager.addChatMessage(testMatch,"", "hello"));
        assertFalse(matchManager.addChatMessage(testMatch,null, "hello"));

        //Clean up
        matchDB.removeMatch("Sally","Ethan");
        matchDB.removeMatch("Ethan","Sally");
    }
//isRejected

    @Test
    public void testIsRejected(){
        assertFalse(matchManager.isRejected("Sally","Benjamin"));
        assertTrue(matchManager.isRejected("Sally", "Charlotte"));
        assertFalse(matchManager.isRejected("Not in db", "Charlotte"));
        assertFalse(matchManager.isRejected("Sally","not in db"));
        assertFalse(matchManager.isRejected(null,null));

    }
//isMatched
    @Test
    public void testIsMatched(){
        assertTrue(matchManager.isMatched("Sally","Benjamin"));
        assertFalse(matchManager.isMatched("Sally","Charlotte"));
        assertFalse(matchManager.isMatched(null,null));
        assertFalse(matchManager.isMatched("Sally","Not in db"));
        assertFalse(matchManager.isMatched("Not in Db","Sally"));
        assertFalse(matchManager.isMatched("not in db", "not in db"));

    }
//getMatchFromList

    @Test
    public void testGetMatchFromList(){
        List<Match> matches = matchManager.getUserMatches("Sally");
        Match match = matchManager.getMatchFromList("Benjamin",matches);

        assertEquals("Benjamin",match.getUserName());
        assertEquals("Tech geek with a taste for adventure",match.getBio());
        assertEquals("Technology",match.getInterest1());
        assertEquals("Gaming", match.getInterest2());
        assertEquals("Photography",match.getInterest3());
        assertEquals("Travel",match.getInterest4());

        assertNull(matchManager.getMatchFromList("Benjamin",null));
        assertNull(matchManager.getMatchFromList(null,null));
        assertNull(matchManager.getMatchFromList(null,matches));

    }
//getRandomMatch

    @Test
    public void testGetRandomMatch(){

        Match match = matchManager.getRandomMatch("Sally");
        assertNotNull(match);
        //Making sure the match retrieved was not already matched or rejected
        assertFalse(matchManager.isMatched("Sally",match.getUserName()));
        assertFalse(matchManager.isRejected("Sally",match.getUserName()));

    }
    @Test
    public void testviewRandomMatch(){

        String user = "Person1";
        Match random = matchManager.getRandomMatch(user);

        assertNotNull("Match should exists" , random);
        assertNotNull("Match should have a username " , random.getUserName());
        assertNotEquals("Should not return user as a match" , user , random.getUserName());

    }


}
