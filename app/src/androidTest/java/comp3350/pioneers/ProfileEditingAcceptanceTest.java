package comp3350.pioneers;

import static androidx.test.espresso.Espresso.onData;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.replaceText;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withSpinnerText;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

import static org.hamcrest.Matchers.anything;

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
public class ProfileEditingAcceptanceTest {
    private final String user = "testuser";
    private final String pass = "password";

    @Rule
    public ActivityScenarioRule<MainActivity> activityRule = new ActivityScenarioRule<>(MainActivity.class);

    @Before
    public void setUp() {
        activityRule.getScenario().onActivity(activity -> {
            Services.initializeDb(activity.getApplicationContext(), "/TestDB");
            IAllUsersPersistence userDB = Services.getUserDB();

            if (!userDB.searchForUser(user)) {
                userDB.addUserToDatabase(new User(user, pass));
            }
        });
    }

    @Test
    public void testProfileEditingFlow() throws InterruptedException {
        // Login
        onView(withId(R.id.editTextUsername)).perform(typeText(user), closeSoftKeyboard());
        onView(withId(R.id.editTextPassword)).perform(typeText(pass), closeSoftKeyboard());
        onView(withId(R.id.loginButton)).perform(click());

        Thread.sleep(1000);

        // click Edit Profile
        onView(withId(R.id.editProfileButton)).perform(click());

        // clear already existing bio
        onView(withId(R.id.bioEditText)).perform(replaceText(""), closeSoftKeyboard());
        // Update Bio
        onView(withId(R.id.bioEditText)).perform(click(),typeText("Adding test Bio!!"), closeSoftKeyboard());


        // Select interests
        onView(withId(R.id.interest1Spinner)).perform(click());
        onData(anything()).atPosition(1).perform(click());
        onView(withId(R.id.interest2Spinner)).perform(click());
        onData(anything()).atPosition(2).perform(click());
        onView(withId(R.id.interest3Spinner)).perform(click());
        onData(anything()).atPosition(3).perform(click());
        onView(withId(R.id.interest4Spinner)).perform(click());
        onData(anything()).atPosition(4).perform(click());

        // click save button to save  changes
        onView(withId(R.id.saveButton)).perform(click());
        Thread.sleep(1000);
        // check if the updates got saved
        onView(withId((R.id.bioEditText))).check(matches((withText("Adding test Bio!!"))));
        onView(withId(R.id.interest1Spinner)).check(matches(withSpinnerText("Music")));
        onView(withId(R.id.interest2Spinner)).check(matches(withSpinnerText("Sports")));
        onView(withId(R.id.interest3Spinner)).check(matches(withSpinnerText("Gaming")));
        onView(withId(R.id.interest4Spinner)).check(matches(withSpinnerText("Movies")));
        // click back button
        onView(withId(R.id.goBackButton)).perform(click());
        Thread.sleep(1000);

        // Verify back on Main Page
        onView(withId(R.id.editProfileButton)).check(matches(isDisplayed()));
    }
}
