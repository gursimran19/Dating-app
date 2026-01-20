package comp3350.pioneers;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

import comp3350.pioneers.business.UserManagerTest;
import comp3350.pioneers.objects.UserTest;
import comp3350.pioneers.objects.MatchTest;
import comp3350.pioneers.business.LogicManagerTest;
import comp3350.pioneers.business.MatchManagerTest;
import comp3350.pioneers.persistence.AllUsersDBTest;
import comp3350.pioneers.persistence.AllUsersStubTest;
import comp3350.pioneers.persistence.MatchDBTest;

@RunWith(Suite.class)
@Suite.SuiteClasses({
        //Business
        LogicManagerTest.class,
        MatchManagerTest.class,
        UserManagerTest.class,

        //Objects
        UserTest.class,
        MatchTest.class,

        //Persistence
        AllUsersStubTest.class,
        MatchDBTest.class,
        AllUsersDBTest.class


})
public class AllUnitTests {

}