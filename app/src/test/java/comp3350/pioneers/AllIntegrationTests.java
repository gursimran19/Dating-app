package comp3350.pioneers;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

import comp3350.pioneers.integrationTests.MatchManagerIntegration;
import comp3350.pioneers.integrationTests.UserManagerIntegration;
import comp3350.pioneers.objects.UserTest;
import comp3350.pioneers.objects.MatchTest;
import comp3350.pioneers.business.LogicManagerTest;
import comp3350.pioneers.business.MatchManagerTest;
import comp3350.pioneers.persistence.AllUsersStubTest;
import comp3350.pioneers.persistence.MatchDBTest;

@RunWith(Suite.class)
@Suite.SuiteClasses({
        MatchManagerIntegration.class,
        UserManagerIntegration.class
})



public class AllIntegrationTests {
}
