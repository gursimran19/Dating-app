package comp3350.pioneers;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

import androidx.test.espresso.contrib.RecyclerViewActions;
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

import android.view.View;
import android.view.ViewGroup;
import androidx.test.espresso.UiController;
import androidx.test.espresso.ViewAction;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class MatchViewTest {

    private static final String TEST_USERNAME = "matchuser";
    private static final String TEST_PASSWORD = "password123";
    private static final String TEST_MATCH_USERNAME = "matcheduser";

    @Rule
    public ActivityScenarioRule<MainActivity> activityRule = new ActivityScenarioRule<>(MainActivity.class);

    @Before
    public void setUp() {
        activityRule.getScenario().onActivity(activity -> {

            Services.initializeDb(activity.getApplicationContext(),"/TestDB");

            createTestUserWithMatches(activity);
        });
    }

    //helper method to set up test data
    private void createTestUserWithMatches(MainActivity activity) {
        //get database references
        IAllUsersPersistence userDB = Services.getUserDB();
        IMatchPersistence matchDB = Services.getMatchDB();

        //create test user if not exists
        if (!userDB.searchForUser(TEST_USERNAME)) {
            User testUser = new User(TEST_USERNAME, TEST_PASSWORD);
            userDB.addUserToDatabase(testUser);

            //set up user bio and interests
            String[] bioData = {"Test bio", "Music", "Sports", "Reading", "Gaming"};
            userDB.updateUserBio(TEST_USERNAME, bioData[0]);
            userDB.updateUserInterests(TEST_USERNAME, bioData[1], bioData[2], bioData[3], bioData[4]);
        }

        //create test match user if not exists
        if (!userDB.searchForUser(TEST_MATCH_USERNAME)) {
            User matchUser = new User(TEST_MATCH_USERNAME, "matchpass");
            userDB.addUserToDatabase(matchUser);

            //set up match bio and interests
            String[] matchBioData = {"I am a test match", "Travel", "Movies", "Cooking", "Art"};
            userDB.updateUserBio(TEST_MATCH_USERNAME, matchBioData[0]);
            userDB.updateUserInterests(TEST_MATCH_USERNAME, matchBioData[1], matchBioData[2], matchBioData[3], matchBioData[4]);
        }

        // Create a match between the two users if not already matched
        if (!matchDB.checkForMatch(TEST_USERNAME, TEST_MATCH_USERNAME)) {
            matchDB.addMatch(TEST_USERNAME, TEST_MATCH_USERNAME);
        }
    }

    @Test
    public void testViewAllMatches() {
        //this test is to view if the user has all the matches it should have
        //login with the test user
        onView(withId(R.id.editTextUsername))
                .perform(typeText(TEST_USERNAME), closeSoftKeyboard());
        onView(withId(R.id.editTextPassword))
                .perform(typeText(TEST_PASSWORD), closeSoftKeyboard());
        onView(withId(R.id.loginButton)).perform(click());

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        //verify we're on the main page
        onView(withId(R.id.viewMatchesButton)).check(matches(isDisplayed()));

        //click on "View Matches" button
        onView(withId(R.id.viewMatchesButton)).perform(click());

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        //verify we're on the matches list page
        onView(withId(R.id.titleTextView)).check(matches(isDisplayed()));
        onView(withId(R.id.titleTextView)).check(matches(withText("Your Matches")));

        //verify that the recycler view is displayed (not the empty view)
        onView(withId(R.id.matchesRecyclerView)).check(matches(isDisplayed()));

        //verify our test match appears in the list
        onView(withText(TEST_MATCH_USERNAME)).check(matches(isDisplayed()));

        //click on the "View Profile" button for our match
        onView(withId(R.id.matchesRecyclerView))
                .perform(RecyclerViewActions.actionOnItem(
                        hasDescendant(withText(TEST_MATCH_USERNAME)),
                        clickChildViewWithId(R.id.viewProfileButton)));

        // Wait for navigation to match profile
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        //verify we're on the match profile page and seeing correct data
        onView(withId(R.id.nameTextView)).check(matches(withText(TEST_MATCH_USERNAME)));
        onView(withId(R.id.bioTextView)).check(matches(withText("I am a test match")));

        //test the back button
        onView(withId(R.id.backButton)).perform(click());

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        //verify we're back on the matches list
        onView(withId(R.id.titleTextView)).check(matches(withText("Your Matches")));
    }

    @Test
    public void testEmptyMatchesList() {
        //this test is to test when a user has no matches in their match list
        //similar logic to the previous test
        final String EMPTY_USER = "emptyuser";
        final String EMPTY_PASS = "emptypass";

        activityRule.getScenario().onActivity(activity -> {
            //add user with no matches
            IAllUsersPersistence userDB = Services.getUserDB();
            if (!userDB.searchForUser(EMPTY_USER)) {
                User emptyUser = new User(EMPTY_USER, EMPTY_PASS);
                userDB.addUserToDatabase(emptyUser);
            }
        });

        onView(withId(R.id.editTextUsername))
                .perform(typeText(EMPTY_USER), closeSoftKeyboard());
        onView(withId(R.id.editTextPassword))
                .perform(typeText(EMPTY_PASS), closeSoftKeyboard());
        onView(withId(R.id.loginButton)).perform(click());

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        onView(withId(R.id.viewMatchesButton)).perform(click());

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        onView(withId(R.id.emptyMatchesText)).check(matches(isDisplayed()));
        onView(withId(R.id.emptyMatchesText)).check(matches(withText("No matches found yet")));
    }

    //helper matcher for finding a child view in a RecyclerView item
    public static Matcher<View> hasDescendant(final Matcher<View> matcher) {
        return new TypeSafeMatcher<View>() {
            @Override
            public void describeTo(Description description) {
                description.appendText("has descendant: ");
                matcher.describeTo(description);
            }

            @Override
            public boolean matchesSafely(View view) {
                if (view instanceof ViewGroup) {
                    ViewGroup parent = (ViewGroup) view;
                    for (int i = 0; i < parent.getChildCount(); i++) {
                        View child = parent.getChildAt(i);
                        if (matcher.matches(child) || matchesSafely(child)) {
                            return true;
                        }
                    }
                }
                return matcher.matches(view);
            }
        };
    }

    //helper method to click a child view in a RecyclerView item
    public static ViewAction clickChildViewWithId(final int id) {
        return new ViewAction() {
            @Override
            public Matcher<View> getConstraints() {
                return null;
            }

            @Override
            public String getDescription() {
                return "Click on a child view with specified id.";
            }

            @Override
            public void perform(UiController uiController, View view) {
                View v = view.findViewById(id);
                v.performClick();
            }
        };
    }
}