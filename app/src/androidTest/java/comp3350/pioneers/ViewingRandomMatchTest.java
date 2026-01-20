package comp3350.pioneers;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.*;

import static org.hamcrest.Matchers.anyOf;

import android.view.View;
import static org.hamcrest.Matchers.not;
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
import comp3350.pioneers.persistence.all.IMatchPersistence;
import comp3350.pioneers.presentation.MainActivity;
@RunWith(AndroidJUnit4.class)
@LargeTest
public class ViewingRandomMatchTest {

    private final String testUser = "Person1";
    private final String pass = "password1";
    private final String randomUser = "Alexander";

    @Rule
    public ActivityScenarioRule<MainActivity> activity = new ActivityScenarioRule<>(MainActivity.class);

    @Before
    public void setUp(){
        activity.getScenario().onActivity(activity ->{
            Services.initializeDb(activity.getApplicationContext() ,"/TestDB");
            IAllUsersPersistence userDB = Services.getUserDB();
            IMatchPersistence matchDB = Services.getMatchDB();

            if(!userDB.searchForUser(testUser)){
                userDB.addUserToDatabase(new User(testUser, pass));
            }
            if (!userDB.searchForUser(randomUser)) {
                userDB.addUserToDatabase(new User(randomUser , "pass1"));
                //S('Alexander','Alexander1','Sports fan and foodie','Sports','Cooking','Fitness','Reading', TRUE)");
                userDB.updateUserBio(randomUser , "Sports fan and foodie");
                userDB.updateUserInterests(randomUser,
                        "Sports",
                        "Cooking",
                        "Fitness",
                        "Reading");


            }

            matchDB.removeMatch(testUser, randomUser);
            matchDB.removeMatch(randomUser,testUser);
            matchDB.removeReject(testUser,randomUser);
            matchDB.removeReject(randomUser,testUser);
        });
        activity.getScenario().recreate();
    }
    @Test
    public void testviewAndReactRandomMatch() throws InterruptedException{

        //try to login
        onView(withId(R.id.editTextUsername)).perform(typeText(testUser), closeSoftKeyboard());
        onView(withId(R.id.editTextPassword)).perform(typeText(pass), closeSoftKeyboard());
        onView(withId(R.id.loginButton)).perform(click());

        Thread.sleep(2000);// sleep for 2sec sometimes login need extra time

        //press explore
        onView(withId(R.id.exploreButton)).perform(click());
        Thread.sleep(1000);

        // random profile checking if it matches
        onView(withId(R.id.nameTextView)).check(matches(withText(randomUser)));
        onView(withId(R.id.bioTextView)).check(matches(withText("Sports fan and foodie")));
        onView(withId(R.id.interest1TextView)).check(matches(withText("Sports")));
        onView(withId(R.id.interest2TextView)).check(matches(withText("Cooking")));
        onView(withId(R.id.interest3TextView)).check(matches(withText("Fitness")));
        onView(withId(R.id.interest4TextView)).check(matches(withText("Reading")));

        //clicking yes button to add as a match
        onView(withId(R.id.yesButton)).perform(click());
        Thread.sleep(1000);

        //after yes explore page has new random or shows the no user message
        onView(withId(R.id.nameTextView)).check(matches(anyOf(withText("You have reached the end check back later"), isDisplayed())));

    }

}
