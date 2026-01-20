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
import org.hamcrest.Description;
import android.view.View;
import android.view.ViewGroup;
import androidx.test.espresso.UiController;
import androidx.test.espresso.ViewAction;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
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
public class ChatAcceptanceTest {

    private static final String testUser = "testUser";
    private static final String pass = "password";
    private static final String match = "match";

    @Rule
    public ActivityScenarioRule<MainActivity> activityRule = new ActivityScenarioRule<>(MainActivity.class);

    @Before
    public void setup() {
        activityRule.getScenario().onActivity(activity -> {
            Services.initializeDb(activity.getApplicationContext(), "/TestDB");
            IAllUsersPersistence userDB = Services.getUserDB();
            IMatchPersistence matchDB = Services.getMatchDB();

            if (!userDB.searchForUser(testUser)) {
                userDB.addUserToDatabase(new User(testUser, pass));
            }

            if (!userDB.searchForUser(match)) {
                userDB.addUserToDatabase(new User(match, "matchpass"));
            }

            if (!matchDB.checkForMatch(testUser, match)) {
                matchDB.addMatch(testUser, match);
            }
        });
    }

    @Test
    public void testChatMessageSends() {
        onView(withId(R.id.editTextUsername)).perform(typeText(testUser), closeSoftKeyboard());
        onView(withId(R.id.editTextPassword)).perform(typeText(pass), closeSoftKeyboard());
        onView(withId(R.id.loginButton)).perform(click());

        sleep();

        onView(withId(R.id.viewMatchesButton)).perform(click());
        sleep();

        onView(withText(match)).check(matches(isDisplayed()));

        onView(withId(R.id.matchesRecyclerView))
                .perform(RecyclerViewActions.actionOnItem(
                        hasDescendant(withText(match)),
                        clickChildViewWithId(R.id.viewProfileButton)));

        sleep();

        onView(withId(R.id.chatButton)).perform(click());
        sleep();

        String testMessage = "Hello test";
        onView(withId(R.id.messageEditText)).perform(typeText(testMessage), closeSoftKeyboard());
        onView(withId(R.id.sendButton)).perform(click());
        sleep();

        onView(withId(R.id.chatMessagesTextView)).check(matches(withText(org.hamcrest.Matchers.containsString(testMessage))));
    }

    private void sleep() {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

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

}
