package comp3350.pioneers;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.assertion.ViewAssertions.matches;

import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import comp3350.pioneers.business.Services;
import comp3350.pioneers.objects.User;
import comp3350.pioneers.persistence.all.IAllUsersPersistence;
import comp3350.pioneers.presentation.MainActivity;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class LoginAcceptanceTest {
    private final String username = "testUser";
    private final String pass = "password";

    @Rule
    public ActivityScenarioRule<MainActivity> activityRule = new ActivityScenarioRule<>(MainActivity.class);

    @Before
    public void setUp(){
        activityRule.getScenario().onActivity(activity -> {
            Services.initializeDb(activity.getApplicationContext(), "/TestDB");

            IAllUsersPersistence userDB = Services.getUserDB();

            if (!userDB.searchForUser(username)) {
                userDB.addUserToDatabase(new User(username, pass));
            }
        });
    }
    @Test
    public void testLogin() throws InterruptedException {
        //testing login
        //putting a usernam
        onView(withId(R.id.editTextUsername)).perform(typeText(username), closeSoftKeyboard());
        // putting password
        onView(withId(R.id.editTextPassword)).perform(typeText(pass), closeSoftKeyboard());
        //press login after putting in credentials
        onView(withId(R.id.loginButton)).perform(click());
        Thread.sleep(2000);
        // if it takes u to mainpage indicates test successfull
        onView(withId(R.id.exploreButton)).check(matches(isDisplayed()));

    }

}
