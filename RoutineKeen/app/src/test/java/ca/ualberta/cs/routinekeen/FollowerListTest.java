package ca.ualberta.cs.routinekeen;
import org.junit.Test;

import ca.ualberta.cs.routinekeen.Models.User;

import static org.junit.Assert.*;

public class FollowerListTest {
  @Test
  
  public void testFollowerList() throws Exception {
    String testUniqueUserName = "Test User name";
    User testUser = new User(testUniqueUserName);
    assertTrue(testUser.getUsername().equals(testUniqueUserName));
    
    String testUniqueFollowerName = "Test follower name";
    User testFollower = new User(testUniqueFollowerName);
    assertTrue(testFollower.getUsername().equals(testUniqueFollowerName));
    
    //testUser.getFollowerList().add(testFollower);
    // get(0) hardcoded for testing purposes
    //assertTrue(testUser.getFollowerList().get(0).equals(testUniqueFollowerName));
    
    //testUser.getFollowerList().remove(testFollower);
    //assertEquals(testUser.getFollowerList().size(), 0);
    
  }
  
}
