package comp3350.pioneers;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import comp3350.pioneers.R;
import comp3350.pioneers.business.Services;
import comp3350.pioneers.presentation.MainActivity;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class AccountMakeTest {

    private static final String TEST_USERNAME_UNIQUE = "uniqueuser";
    private static final String TEST_USERNAME_DUPLICATE = "duplicateuser";
    private static final String TEST_USERNAME_EMPTY_PASSWORD = "emptypassword";
    private static final String TEST_PASSWORD = "password123";

    @Rule
    public ActivityScenarioRule<MainActivity> activityRule = new ActivityScenarioRule<>(MainActivity.class);

    @Before
    public void setUp() {

        activityRule.getScenario().onActivity(activity -> {

            Services.initializeDb(activity.getApplicationContext(),"/TestDB");
        });
    }

    @Test
    public void testSuccessfulAccountCreation() {
        //enter username in the username field
        onView(withId(R.id.editTextUsername))
                .perform(typeText(TEST_USERNAME_UNIQUE), closeSoftKeyboard());

        //enter password in the password field
        onView(withId(R.id.editTextPassword))
                .perform(typeText(TEST_PASSWORD), closeSoftKeyboard());

        //click the signup button
        onView(withId(R.id.buttonSignup)).perform(click());

        //verify that we're taken to the main page after successful registration
        try {
            Thread.sleep(1000); // Wait for navigation
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        //check that the exploreButton is displayed
        onView(withId(R.id.exploreButton)).check(matches(isDisplayed()));
    }

    @Test
    public void testDuplicateAccountCreation() {
        //this test is making a user, then log out, then come back and try creating it again
        onView(withId(R.id.editTextUsername))
                .perform(typeText(TEST_USERNAME_DUPLICATE), closeSoftKeyboard());
        onView(withId(R.id.editTextPassword))
                .perform(typeText(TEST_PASSWORD), closeSoftKeyboard());
        onView(withId(R.id.buttonSignup)).perform(click());

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        onView(withId(R.id.logOutButton)).perform(click());

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // Try to create the same account again
        onView(withId(R.id.editTextUsername))
                .perform(typeText(TEST_USERNAME_DUPLICATE), closeSoftKeyboard());
        onView(withId(R.id.editTextPassword))
                .perform(typeText(TEST_PASSWORD), closeSoftKeyboard());
        onView(withId(R.id.buttonSignup)).perform(click());

        //verify we're still on the login page
        onView(withId(R.id.loginButton)).check(matches(isDisplayed()));
    }

    @Test
    public void testEmptyFieldsAccountCreation() {
        //this is testing with the fields empty for sign up
        onView(withId(R.id.buttonSignup)).perform(click());

        onView(withId(R.id.loginButton)).check(matches(isDisplayed()));
    }

    @Test
    public void testEmptyUsernameAccountCreation() {
        //leave username empty but fill password
        onView(withId(R.id.editTextPassword))
                .perform(typeText(TEST_PASSWORD), closeSoftKeyboard());

        onView(withId(R.id.buttonSignup)).perform(click());

        onView(withId(R.id.loginButton)).check(matches(isDisplayed()));
    }

    @Test
    public void testEmptyPasswordAccountCreation() {
        //fill username but leave password empty
        onView(withId(R.id.editTextUsername))
                .perform(typeText(TEST_USERNAME_EMPTY_PASSWORD), closeSoftKeyboard());

        onView(withId(R.id.buttonSignup)).perform(click());

        onView(withId(R.id.loginButton)).check(matches(isDisplayed()));
    }
}