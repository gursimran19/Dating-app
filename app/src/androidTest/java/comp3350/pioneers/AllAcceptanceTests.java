package comp3350.pioneers;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({
        AccountMakeTest.class,
        ChatAcceptanceTest.class,
        InterestPickingTest.class,
        LoginAcceptanceTest.class,
        MatchViewTest.class,
        ProfileEditingAcceptanceTest.class,
        ViewingRandomMatchTest.class,
        ViewMatchAcceptanceTest.class

})




public class AllAcceptanceTests {
}
