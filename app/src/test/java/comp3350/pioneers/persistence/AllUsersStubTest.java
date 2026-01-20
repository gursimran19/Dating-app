package comp3350.pioneers.persistence;


import comp3350.pioneers.objects.*;
import comp3350.pioneers.persistence.all.stubs.AllUsersPersistenceStub;
import comp3350.pioneers.persistence.all.IAllUsersPersistence;



import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;


public class AllUsersStubTest {

    private IAllUsersPersistence stubTest;
    private User user1;

    @Before
    public void setUp() {
        stubTest = new AllUsersPersistenceStub();
        user1 = new User("Karl","firstStreet");
    }

    @Test
    public void testGetUserData() {
        String[] testString = new String[2];
        testString = stubTest.getUserData("Jimmy");
        assertEquals("Jimmy",testString[0]);
        assertEquals("mycat",testString[1]);

        //Testing users not in the database
        testString = stubTest.getUserData("NotInDatabase");

        assertNull(testString[0]);
        assertNull(testString[1]);

    }


    @Test
    public void testSearchForUser(){
        //Testing for users in database
        assertTrue(stubTest.searchForUser("Jimmy"));
        assertTrue(stubTest.searchForUser("Bobby"));

        //Testing for users Not in the database
        assertFalse(stubTest.searchForUser("Jimbo"));
        assertFalse(stubTest.searchForUser("Bobbyyyyy"));
    }


    @Test
    public void testAddToDatabase(){

        stubTest.addUserToDatabase(user1);
        assertTrue(stubTest.searchForUser("Karl"));

    }

    @Test
    public void testGetUserBio(){

        String[] bio1 = stubTest.getUserBio("Jimmy");
        assertEquals("A bio",bio1[0]);
        assertEquals("coffee",bio1[1]);
        assertEquals("biking",bio1[2]);
        assertEquals("cars",bio1[3]);
        assertEquals("cats",bio1[4]);

        String[] bio2 = stubTest.getUserBio("Not in DB");
        assertNull(bio2[0]);
        assertNull(bio2[1]);
        assertNull(bio2[2]);
        assertNull(bio2[3]);
        assertNull(bio2[4]);

    }

    @Test
    public void testGetUserMatchType(){
        boolean match1 = stubTest.getUserMatchType("Jimmy");
        boolean match2 = stubTest.getUserMatchType("Bobby");

        assertFalse(match1);
        assertTrue(match2);
        assertFalse(stubTest.getUserMatchType("not in db"));

    }


    @Test
    public void testUpdateUserBio(){
        User newUser = new User("Sally","12345");
        stubTest.addUserToDatabase(newUser);
        stubTest.updateUserBio("Sally","I love goooold");
        stubTest.updateUserInterests("Sally", "burgers","fries","chili","fingers");
        assertTrue(stubTest.updateUserBio("sally","The new Bio"));

        String[] bio1 = stubTest.getUserBio("sally");
        assertEquals(bio1[0],"The new Bio");
    }

    @Test
    public void testUpdateUserInterests(){
        User newUser = new User("Joe","123456");
        stubTest.addUserToDatabase(newUser);
        stubTest.updateUserBio("Joe","Yah baby");
        stubTest.updateUserInterests("Joe", "1","2","3","4");

        assertTrue(stubTest.updateUserInterests("Joe","new1", "new2", "new3","new4"));
        String[] bio1 = stubTest.getUserBio("Joe");
        assertEquals(bio1[1],"new1");
        assertEquals(bio1[2],"new2");
        assertEquals(bio1[3],"new3");
        assertEquals(bio1[4],"new4");
    }

    @Test
    public void testUpdateMatchingType(){
        User newUser = new User("Brad","12");
        stubTest.addUserToDatabase(newUser);
        stubTest.updateUserBio("Brad","wooop");
        stubTest.updateUserInterests("Brad", "1","2","3","4");

        assertTrue(stubTest.updateMatchingType("Brad",true));
        assertTrue(stubTest.getUserMatchType("Brad"));
        assertFalse(stubTest.getUserMatchType("not in db"));

    }



    @After
    public void clean(){
        stubTest = null;
    }


}




