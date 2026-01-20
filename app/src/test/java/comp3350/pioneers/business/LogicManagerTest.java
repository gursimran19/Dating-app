/*
This file tests logic for signup and login using various test methods.

Author : Abhijeet Singh
 */

package comp3350.pioneers.business;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import comp3350.pioneers.persistence.all.stubs.AllUsersPersistenceStub;

public class LogicManagerTest {
    private LogicManager logicManager;

    @Before
    public void setUp() {
        logicManager = new LogicManager(new AllUsersPersistenceStub());
    }
    @Test
    public void testSignUpSuccess() {
        assertTrue(" User should be registered successfully! ", logicManager.registerUser("newUser", "admin@34"));
    }

    @Test
    public void testSignUpForEmptyUsernameOrPassword() {
        assertFalse(" Registration should fail with empty username! ", logicManager.registerUser("", "admin@34"));
        assertFalse(" Registration should fail with empty password! ", logicManager.registerUser("user1", ""));
    }

    @Test
    public void testSignUpUserAlreadyExists() {
        logicManager.registerUser("existingUser", "admin@34");
        assertFalse(" User registration should fail for existing user! ", logicManager.registerUser("existingUser", "nPass"));
    }

    @Test
    public void testSuccessfulLogin() {
        logicManager.registerUser("testUser", "testPass");
        assertTrue(" Login should succeed with correct credentials! ", logicManager.validateUser("testUser", "testPass"));
    }
    @Test
    public void testLoginWrongPassword() {
        logicManager.registerUser("testUser", "cPass");
        assertFalse(" Login should fail with wrong password! ", logicManager.validateUser("testUser", "wPass"));
    }
    @Test
    public void testLoginUserDoesNotExist() {
        assertFalse(" Login should fail the user that do not exist! ", logicManager.validateUser("User", "Pass"));
    }

    @Test
    public void testGetUserName(){
        assertTrue(logicManager.validateUser("Jimmy", "mycat"));
        assertEquals("Jimmy",logicManager.getUserName());

    }
}
