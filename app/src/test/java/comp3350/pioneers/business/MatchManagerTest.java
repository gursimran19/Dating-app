package comp3350.pioneers.business;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;

import comp3350.pioneers.objects.Match;
import comp3350.pioneers.persistence.all.IAllUsersPersistence;
import comp3350.pioneers.persistence.all.IMatchPersistence;

public class MatchManagerTest {

    @Mock
    private IAllUsersPersistence mockUserDB;

    @Mock
    private IMatchPersistence mockMatchDB;

    @Mock
    private Match mockMatch;

    private MatchManager matchManager;

    private static final String CURRENT_USER = "testUser";
    private static final String MATCH_USER = "matchUser";

    @Before
    public void setUp() {
        MockitoAnnotations.openMocks(this); // initializes all the mock objects
        matchManager = new MatchManager(mockUserDB, mockMatchDB);
    }

    @Test
    public void testRejectUser_Success() {
        //here checking that the rejectUser() is working
        when(mockMatchDB.checkForReject(CURRENT_USER, MATCH_USER)).thenReturn(false);
        when(mockMatchDB.addReject(CURRENT_USER, MATCH_USER)).thenReturn(true);


        boolean result = matchManager.rejectUser(CURRENT_USER, MATCH_USER);

        assertTrue(result);
        verify(mockMatchDB).checkForReject(CURRENT_USER, MATCH_USER);
        verify(mockMatchDB).addReject(CURRENT_USER, MATCH_USER);
    }

    @Test
    public void testRejectUser_AlreadyRejected() {
        //this is checking handling a user that's already been rejected
        when(mockMatchDB.checkForReject(CURRENT_USER, MATCH_USER)).thenReturn(true);


        boolean result = matchManager.rejectUser(CURRENT_USER, MATCH_USER);


        assertTrue(result);
        verify(mockMatchDB).checkForReject(CURRENT_USER, MATCH_USER);
        verify(mockMatchDB, never()).addReject(anyString(), anyString());
    }

    @Test
    public void testRejectUser_InvalidInput() {
        //tests handling invalid inputs
        assertFalse(matchManager.rejectUser(null, MATCH_USER));
        assertFalse(matchManager.rejectUser(CURRENT_USER, null));

        //testing empty inputs
        assertFalse(matchManager.rejectUser("", MATCH_USER));
        assertFalse(matchManager.rejectUser(CURRENT_USER, ""));
    }

    @Test
    public void testCreateMatch_Success() {
        //tests successful match creation
        when(mockMatchDB.checkForMatch(CURRENT_USER, MATCH_USER)).thenReturn(false);
        when(mockMatchDB.addMatch(CURRENT_USER, MATCH_USER)).thenReturn(true);


        boolean result = matchManager.createMatch(CURRENT_USER, MATCH_USER);


        assertTrue(result);
        verify(mockMatchDB).checkForMatch(CURRENT_USER, MATCH_USER);
        verify(mockMatchDB).addMatch(CURRENT_USER, MATCH_USER);
    }

    @Test
    public void testCreateMatch_AlreadyMatched() {
        //tests handling users who are already matched
        when(mockMatchDB.checkForMatch(CURRENT_USER, MATCH_USER)).thenReturn(true);

        boolean result = matchManager.createMatch(CURRENT_USER, MATCH_USER);

        assertTrue(result);
        verify(mockMatchDB).checkForMatch(CURRENT_USER, MATCH_USER);
        verify(mockMatchDB, never()).addMatch(anyString(), anyString());
    }

    @Test
    public void testGetUserMatches() {
        //test to check retrieving a users matches
        List<Match> expectedMatches = new ArrayList<>();
        expectedMatches.add(mockMatch);
        when(mockMatchDB.getAllMatches(CURRENT_USER)).thenReturn(expectedMatches);

        List<Match> result = matchManager.getUserMatches(CURRENT_USER);

        assertEquals(expectedMatches, result);
        verify(mockMatchDB).getAllMatches(CURRENT_USER);
    }

    @Test
    public void testGetMatchInfo() {
        //tests retrieving match information
        when(mockMatch.getUserName()).thenReturn(MATCH_USER);
        when(mockMatch.getBio()).thenReturn("Test bio");
        when(mockMatch.getInterest1()).thenReturn("Interest 1");
        when(mockMatch.getInterest2()).thenReturn("Interest 2");
        when(mockMatch.getInterest3()).thenReturn("Interest 3");
        when(mockMatch.getInterest4()).thenReturn("Interest 4");

        String[] result = matchManager.getMatchInfo(mockMatch);

        assertEquals(6, result.length);
        assertEquals(MATCH_USER, result[0]);
        assertEquals("Test bio", result[1]);
        assertEquals("Interest 1", result[2]);
        assertEquals("Interest 2", result[3]);
        assertEquals("Interest 3", result[4]);
        assertEquals("Interest 4", result[5]);
    }

    @Test
    public void testGetMatchChat() {
        //tests retrieving match chat
        when(mockMatch.getChat()).thenReturn("Test chat");

        String result = matchManager.getMatchChat(mockMatch);

        assertEquals("Test chat", result);
        verify(mockMatch).getChat();
    }

    @Test
    public void testAddChatMessage() {
        //tests adding a message to a matches chat
        String sender = CURRENT_USER;
        String message = "Hello!";
        String updatedChat = "updatedChat";
        when(mockMatch.getUserName()).thenReturn(MATCH_USER);
        when(mockMatch.getChat()).thenReturn(updatedChat);
        when(mockMatchDB.updateChat(sender, MATCH_USER, updatedChat)).thenReturn(true);


        boolean result = matchManager.addChatMessage(mockMatch, sender, message);

        assertTrue(result);
        verify(mockMatch).addChatMessage(sender, message);
        verify(mockMatchDB).updateChat(sender, MATCH_USER, updatedChat);
    }

    @Test
    public void testGetMatchFromList() {
        //find a match in a list
        List<Match> matchList = new ArrayList<>();
        when(mockMatch.getUserName()).thenReturn(MATCH_USER);
        matchList.add(mockMatch);

        Match result = matchManager.getMatchFromList(MATCH_USER, matchList);

        assertEquals(mockMatch, result);
    }



    @Test
    public void testAddChatMessage_EmptyChat() {
        //test the case when chat is "Empty"
        String sender = CURRENT_USER;
        String message = "Hello!";
        String updatedChat = "testUser: Hello!";

        when(mockMatch.getUserName()).thenReturn(MATCH_USER);
        when(mockMatch.getChat()).thenReturn("Empty").thenReturn(updatedChat);
        when(mockMatchDB.updateChat(sender, MATCH_USER, updatedChat)).thenReturn(true);
        when(mockMatchDB.updateChat(MATCH_USER, sender, updatedChat)).thenReturn(true);

        boolean result = matchManager.addChatMessage(mockMatch, sender, message);

        assertTrue(result);
        verify(mockMatch).setChat("");
        verify(mockMatch).addChatMessage(sender, message);
        verify(mockMatchDB).updateChat(sender, MATCH_USER, updatedChat);
    }

    @Test
    public void testAddChatMessage_InvalidInputs() {
        //test invalid inputs for addChatMessage
        assertFalse(matchManager.addChatMessage(null, CURRENT_USER, "message"));
        assertFalse(matchManager.addChatMessage(mockMatch, null, "message"));
        assertFalse(matchManager.addChatMessage(mockMatch, "", "message"));
        assertFalse(matchManager.addChatMessage(mockMatch, CURRENT_USER, null));
        assertFalse(matchManager.addChatMessage(mockMatch, CURRENT_USER, ""));
    }

    @Test
    public void testIsRejected_True() {
        //test when a user is rejected
        when(mockMatchDB.checkForReject(CURRENT_USER, MATCH_USER)).thenReturn(true);

        boolean result = matchManager.isRejected(CURRENT_USER, MATCH_USER);

        assertTrue(result);
        verify(mockMatchDB).checkForReject(CURRENT_USER, MATCH_USER);
    }

    @Test
    public void testIsRejected_False() {
        //test when a user is not rejected
        when(mockMatchDB.checkForReject(CURRENT_USER, MATCH_USER)).thenReturn(false);

        boolean result = matchManager.isRejected(CURRENT_USER, MATCH_USER);

        assertFalse(result);
        verify(mockMatchDB).checkForReject(CURRENT_USER, MATCH_USER);
    }

    @Test
    public void testIsRejected_InvalidInput() {
        //test invalid inputs for isRejected
        assertFalse(matchManager.isRejected(null, MATCH_USER));
        assertFalse(matchManager.isRejected(CURRENT_USER, null));
        assertFalse(matchManager.isRejected("", MATCH_USER));
        assertFalse(matchManager.isRejected(CURRENT_USER, ""));
    }

    @Test
    public void testIsMatched_True() {
        //test when users are matched
        when(mockMatchDB.checkForMatch(CURRENT_USER, MATCH_USER)).thenReturn(true);

        boolean result = matchManager.isMatched(CURRENT_USER, MATCH_USER);

        assertTrue(result);
        verify(mockMatchDB).checkForMatch(CURRENT_USER, MATCH_USER);
    }

    @Test
    public void testIsMatched_False() {
        //test when users are not matched
        when(mockMatchDB.checkForMatch(CURRENT_USER, MATCH_USER)).thenReturn(false);

        boolean result = matchManager.isMatched(CURRENT_USER, MATCH_USER);

        assertFalse(result);
        verify(mockMatchDB).checkForMatch(CURRENT_USER, MATCH_USER);
    }

    @Test
    public void testIsMatched_InvalidInput() {
        //test invalid inputs for isMatched
        assertFalse(matchManager.isMatched(null, MATCH_USER));
        assertFalse(matchManager.isMatched(CURRENT_USER, null));
        assertFalse(matchManager.isMatched("", MATCH_USER));
        assertFalse(matchManager.isMatched(CURRENT_USER, ""));
    }

    @Test
    public void testGetRandomMatch() {
        //test getting a random match
        when(mockMatchDB.getRandomMatch(CURRENT_USER)).thenReturn(mockMatch);

        Match result = matchManager.getRandomMatch(CURRENT_USER);

        assertEquals(mockMatch, result);
        verify(mockMatchDB).getRandomMatch(CURRENT_USER);
    }

    @Test
    public void testGetRandomMatch_NoMatch() {
        //test getting a random match when none exists
        when(mockMatchDB.getRandomMatch(CURRENT_USER)).thenReturn(null);

        Match result = matchManager.getRandomMatch(CURRENT_USER);

        assertNull(result);
        verify(mockMatchDB).getRandomMatch(CURRENT_USER);
    }

    @Test
    public void testGetRandomMatch_InvalidInput() {
        //test invalid input for getRandomMatch
        Match result = matchManager.getRandomMatch(null);

        assertNull(result);
        verify(mockMatchDB, never()).getRandomMatch(anyString());
    }

    @Test
    public void testGetMatchInfo_WithUsernames() {
        //test the overloaded getMatchInfo method that takes usernames
        when(mockMatchDB.getMatchInfo(CURRENT_USER, MATCH_USER)).thenReturn(mockMatch);

        Match result = matchManager.getMatchInfo(CURRENT_USER, MATCH_USER);

        assertEquals(mockMatch, result);
        verify(mockMatchDB).getMatchInfo(CURRENT_USER, MATCH_USER);
    }

    @Test
    public void testGetMatchFromList_NotFound() {
        //test getting a match from list when it doesn't exist
        List<Match> matchList = new ArrayList<>();
        when(mockMatch.getUserName()).thenReturn("differentUser");
        matchList.add(mockMatch);

        Match result = matchManager.getMatchFromList(MATCH_USER, matchList);

        assertNull(result);
    }

    @Test
    public void testGetMatchFromList_InvalidInput() {
        //test invalid inputs for getMatchFromList
        List<Match> matchList = new ArrayList<>();
        matchList.add(mockMatch);

        Match result1 = matchManager.getMatchFromList(null, matchList);
        Match result2 = matchManager.getMatchFromList(MATCH_USER, null);

        assertNull(result1);
        assertNull(result2);
    }

    @Test
    public void testReportMatch_Success() {
        //test successful report/removal of a match
        when(mockMatchDB.removeMatch(CURRENT_USER, MATCH_USER)).thenReturn(true);
        when(mockMatchDB.removeMatch(MATCH_USER, CURRENT_USER)).thenReturn(true);

        boolean result = matchManager.reportMatch(CURRENT_USER, MATCH_USER);

        assertTrue(result);
        verify(mockMatchDB).removeMatch(CURRENT_USER, MATCH_USER);
        verify(mockMatchDB).removeMatch(MATCH_USER, CURRENT_USER);
    }

    @Test
    public void testReportMatch_PartialFailure() {
        //test when one side of the match removal fails
        when(mockMatchDB.removeMatch(CURRENT_USER, MATCH_USER)).thenReturn(true);
        when(mockMatchDB.removeMatch(MATCH_USER, CURRENT_USER)).thenReturn(false);

        boolean result = matchManager.reportMatch(CURRENT_USER, MATCH_USER);

        assertFalse(result);
        verify(mockMatchDB).removeMatch(CURRENT_USER, MATCH_USER);
        verify(mockMatchDB).removeMatch(MATCH_USER, CURRENT_USER);
    }

    @Test
    public void testGetMatchInfo_NullMatch() {
        //test getMatchInfo with null match
        String[] result = matchManager.getMatchInfo(null);

        assertNotNull(result);
        assertEquals(6, result.length);
        //all elements should be null
        for (String element : result) {
            assertNull(element);
        }
    }

    @Test
    public void testGetMatchChat_NullMatch() {
        //test getMatchChat with null match
        String result = matchManager.getMatchChat(null);

        assertEquals("", result);
    }

    @Test
    public void testAddChatMessage_OneSidedMatch() {
        //test when chat update succeeds only for one user (one-sided match)
        String sender = CURRENT_USER;
        String message = "Hello!";
        String updatedChat = "testUser: Hello!";

        when(mockMatch.getUserName()).thenReturn(MATCH_USER);
        when(mockMatch.getChat()).thenReturn(updatedChat);
        when(mockMatchDB.updateChat(sender, MATCH_USER, updatedChat)).thenReturn(true);
        when(mockMatchDB.updateChat(MATCH_USER, sender, updatedChat)).thenReturn(false);

        boolean result = matchManager.addChatMessage(mockMatch, sender, message);

        assertTrue(result); //current implementation returns true if at least first update succeeds
        verify(mockMatch).addChatMessage(sender, message);
        verify(mockMatchDB).updateChat(sender, MATCH_USER, updatedChat);
        verify(mockMatchDB).updateChat(MATCH_USER, sender, updatedChat);
    }
}