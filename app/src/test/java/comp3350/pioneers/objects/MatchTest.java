/*
This file tests the Match class functionality.
 */

package comp3350.pioneers.objects;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class MatchTest {
    private Match match;
    private static final String TEST_USERNAME = "testUser";

    @Before
    public void setUp() {
        match = new Match(TEST_USERNAME);
    }

    @Test
    public void testConstructor() {
        //verify the object was created correctly
        assertEquals("Username should be set correctly", TEST_USERNAME, match.getUserName());
        assertTrue("Match should be active by default", match.isActive());
        assertEquals("Chat should be empty by default", "", match.getChat());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorWithNullUsername() {
        //this should throw an IllegalArgumentException
        new Match(null);
    }

    @Test
    public void testSetAndGetBio() {
        String testBio = "This is a test bio";
        match.setBio(testBio);
        assertEquals("Bio should be set and retrieved correctly", testBio, match.getBio());
    }

    @Test
    public void testSetAndGetInterests() {
        String interest1 = "Reading";
        String interest2 = "Gaming";
        String interest3 = "Hiking";
        String interest4 = "Cooking";

        match.setInterest1(interest1);
        match.setInterest2(interest2);
        match.setInterest3(interest3);
        match.setInterest4(interest4);

        assertEquals("Interest1 should be set and retrieved correctly", interest1, match.getInterest1());
        assertEquals("Interest2 should be set and retrieved correctly", interest2, match.getInterest2());
        assertEquals("Interest3 should be set and retrieved correctly", interest3, match.getInterest3());
        assertEquals("Interest4 should be set and retrieved correctly", interest4, match.getInterest4());
    }

    @Test
    public void testSetAndGetMatchType() {
        //default should be false
        assertFalse("Match type should be false by default", match.getMatchType());

        //test setting to true
        match.setMatchType(true);
        assertTrue("Match type should be set to true", match.getMatchType());

        //test setting back to false
        match.setMatchType(false);
        assertFalse("Match type should be set to false", match.getMatchType());
    }

    @Test
    public void testSetAndGetActive() {
        //default should be true (from constructor)
        assertTrue("isActive should be true by default", match.isActive());

        //test setting to false
        match.setActive(false);
        assertFalse("isActive should be set to false", match.isActive());

        //test setting back to true
        match.setActive(true);
        assertTrue("isActive should be set to true", match.isActive());
    }

    @Test
    public void testSetAndGetChat() {
        String testChat = "This is a test chat message";
        match.setChat(testChat);
        assertEquals("Chat should be set and retrieved correctly", testChat, match.getChat());
    }

    @Test
    public void testAddChatMessage() {
        String sender = "User1";
        String message = "Hello there!";
        String expectedFormat = "[" + sender + "]: " + message + "\n";

        //add a message to an empty chat
        match.addChatMessage(sender, message);
        assertEquals("Message should be added correctly to empty chat",
                expectedFormat, match.getChat());

        //add another message
        String sender2 = "User2";
        String message2 = "Hi!";
        String expectedFormat2 = "[" + sender2 + "]: " + message2 + "\n";
        match.addChatMessage(sender2, message2);

        //chat should now contain both messages
        assertEquals("Chat should contain both messages",
                expectedFormat + expectedFormat2, match.getChat());
    }

    @Test
    public void testInterestSetterBug() {
        //this test checks for a potential bug in the interest setter methods
        //where interest2, interest3, and interest4 setters all modify interest1

        String interest1 = "Reading";
        String interest2 = "Gaming";
        String interest3 = "Hiking";
        String interest4 = "Cooking";

        match.setInterest1(interest1);
        assertEquals("Interest1 should be set correctly", interest1, match.getInterest1());

        match.setInterest2(interest2);
        //both should be set to interest2 due to the bug
        assertEquals("Interest1 should not change when setting interest2", interest1, match.getInterest1());
        assertEquals("Interest2 should be set correctly", interest2, match.getInterest2());

        match.setInterest3(interest3);
        //interest1 should not change
        assertEquals("Interest1 should not change when setting interest3", interest1, match.getInterest1());
        assertEquals("Interest3 should be set correctly", interest3, match.getInterest3());

        match.setInterest4(interest4);
        //interest1 should not change
        assertEquals("Interest1 should not change when setting interest4", interest1, match.getInterest1());
        assertEquals("Interest4 should be set correctly", interest4, match.getInterest4());
    }
}