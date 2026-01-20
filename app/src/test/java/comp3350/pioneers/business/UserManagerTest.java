package comp3350.pioneers.business;
import static org.mockito.Mockito.*;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.MockitoAnnotations;
import org.mockito.internal.matchers.Matches;
import org.mockito.MockedStatic;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mockStatic;
import static org.junit.Assert.*;
import java.util.ArrayList;
import java.util.List;

import comp3350.pioneers.objects.Match;
import comp3350.pioneers.objects.User;
import comp3350.pioneers.persistence.all.IAllUsersPersistence;
import comp3350.pioneers.persistence.all.IMatchPersistence;

public class UserManagerTest {

    @Mock
    private IAllUsersPersistence mockUserDB;
    @Mock
    private IMatchPersistence mockMatchDB;
    @Mock
    private MatchManager matchMan;
    @Mock
    private Match mockMatch;
    @Mock
    private Services mockService;
    @Mock
    private User mockUser;

    private UserManager userMan;


    @Before
    public void setUp(){

        MockitoAnnotations.openMocks(this);
        matchMan = new MatchManager(mockUserDB,mockMatchDB);
        userMan = new UserManager(mockUserDB ,matchMan);

    }

    @Test
    public void testBuildUserDSO(){
        List<User> mockedList = mock(ArrayList.class);
        List<Match> mockedMatches = mock(ArrayList.class);
        when(mockUserDB.getAllUsers()).thenReturn(mockedList);
        when(mockUserDB.searchForUser("Sally")).thenReturn(true);
        String[] testBio = {"Bio", "interest1","interest2","interest3","interest4"};

        when(mockUserDB.getUserBio("Sally")).thenReturn(testBio);
        when(mockUserDB.getUserMatchType("Sally")).thenReturn(true);
        when(matchMan.getUserMatches("Sally")).thenReturn(mockedMatches);
        assertTrue(userMan.buildUserDSO("Sally","sallypass"));
        assertFalse(userMan.buildUserDSO("",""));
        assertFalse(userMan.buildUserDSO(null,null));

    }

    @Test
    public void testUpdateUserBio(){
        when(mockUserDB.updateUserBio("Sally","New Bio")).thenReturn(true);
        when(mockUserDB.updateUserInterests("Sally","1","2","3","4")).thenReturn(true);
        assertTrue(userMan.updateUserBio("Sally","New Bio","1","2","3","4"));
        assertFalse(userMan.updateUserBio(null,null,null,null,null,null));
        assertFalse(userMan.updateUserBio("","","","","",""));


    }

    @Test
    public void updateUserMatchingType(){

        assertFalse(userMan.updateUserMatchingType("",true));
        assertFalse(userMan.updateUserMatchingType(null,true));
        //UserManager updateUserMachingType() uses a static function from Services, cant mock it apparently?

    }

@Test
    public void testResetUserDSO(){



    }


}
