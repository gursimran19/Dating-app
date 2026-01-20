package comp3350.pioneers.objects;


import comp3350.pioneers.objects.*;


import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

//testing the user class
public class UserTest {

    private User user;

    @Before
    public void setUp() {
        //setting up the user class
        user = new User("admin@example.com", "password");
    }

    @Test
    public void testGetUsername() {
        //making sure the username matches up
        assertEquals("admin@example.com", user.getUsername());
    }

    @Test
    public void testGetPassword() {
        //test that the email matches up
        assertEquals("password", user.getPassword());
    }



    @Test(expected = IllegalArgumentException.class)
    public void userNameNull() {
        //make sure it does not accept null usernames
        new User(null, "password");
    }

    @Test(expected = IllegalArgumentException.class)
    public void passwordNull() {
        ///make sure it does not accept null email
        new User("admin@example.com", null);
    }
}