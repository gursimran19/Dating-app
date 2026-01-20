package comp3350.pioneers;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.Espresso.pressBack;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
//import static androidx.test.espresso.intent.Intents.intended;
//import static androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static androidx.test.espresso.matcher.ViewMatchers.*;

import android.content.Intent;
import android.view.View;

import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;
//import androidx.test.espresso.intent.Intents;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import comp3350.pioneers.business.Services;
import comp3350.pioneers.objects.Match;
import comp3350.pioneers.objects.User;
import comp3350.pioneers.persistence.all.IMatchPersistence;
import comp3350.pioneers.persistence.all.IAllUsersPersistence;
import comp3350.pioneers.presentation.MainActivity;
import comp3350.pioneers.presentation.ViewMatch;
import comp3350.pioneers.presentation.ChatPage;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class ViewMatchAcceptanceTest {
    private static final String testUser = "test";
    private static final String pass = "test1";
    private static final String matchedUser = "Alexander";

    @Rule
    public ActivityScenarioRule<MainActivity> activity =
            new ActivityScenarioRule<>(MainActivity.class);


    @Before
    public void setUp() {
        activity.getScenario().onActivity(activity -> {
            Services.initializeDb(activity.getApplicationContext(), "/TestDB");
            
            IAllUsersPersistence userDB = Services.getUserDB();
            IMatchPersistence matchDB = Services.getMatchDB();

            if (!userDB.searchForUser(testUser)) {
                userDB.addUserToDatabase(new User(testUser, pass));
            }
            if (!userDB.searchForUser(matchedUser)) {
                userDB.addUserToDatabase(new User(matchedUser, "pass1"));
                userDB.updateUserBio(matchedUser, "Sports fan and foodie");
                userDB.updateUserInterests(matchedUser, "Sports", "Cooking", "Fitness", "Reading");
            }
            matchDB.addMatch(testUser, matchedUser);
        });
        activity.getScenario().recreate();
    }


    //test-1:Verify match profile details are displayed right
    @Test
    public void testViewCorrectMatchDetails() throws InterruptedException {
        //try to login
        onView(withId(R.id.editTextUsername)).perform(typeText(testUser), closeSoftKeyboard());
        onView(withId(R.id.editTextPassword)).perform(typeText(pass), closeSoftKeyboard());
        onView(withId(R.id.loginButton)).perform(click());
        Thread.sleep(1000);
        //press explore
        onView(withId(R.id.viewMatchesButton)).perform(click());
        Thread.sleep(1000);

        onView(withId(R.id.viewProfileButton)).perform(click());


        onView(withId(R.id.nameTextView)).check(matches(withText(matchedUser)));
        onView(withId(R.id.bioTextView)).check(matches(withText("Sports fan and foodie")));
        onView(withId(R.id.interest1TextView)).check(matches(withText("Sports")));
        onView(withId(R.id.interest2TextView)).check(matches(withText("Cooking")));
        onView(withId(R.id.interest3TextView)).check(matches(withText("Fitness")));
        onView(withId(R.id.interest4TextView)).check(matches(withText("Reading")));
        Thread.sleep(1000);
    }
}
