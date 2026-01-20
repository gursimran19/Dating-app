package comp3350.pioneers.persistence.all;


import java.util.List;

import comp3350.pioneers.objects.Match;


public interface IMatchPersistence {
    //Adds the currentUser and newMatch to the MatchDB
    //Returns true on success
    //See diagram for how that table is going to look.
    boolean addMatch(String currentUsername, String newMatch);

    //Adds the currentUsername and rejectedUsername to the database
    //Returns true on success
    boolean addReject(String currentUsername, String rejectedUsername);

    //Checks database to see if the currentUsername and potentialMatch are already in the matched DB
    //True if already in matched DB, false if not.
    boolean checkForMatch(String currentUsername, String potentialMatch);

    //Checks database to see if the currentUsername and potentialReject are already in the reject DB
    //Returns true is already in reject DB, false if not
    boolean checkForReject(String currentUsername, String potentialReject);

    //Returns an arraylist of Match objects for each users match
    List<Match> getAllMatches(String username);

    //Retrieve a random user from the database that the user has not rejected or matched with
    Match getRandomMatch(String currentUsername);


    //Retrieves a single match object for the matchName
    Match getMatchInfo(String username, String matchName);

    boolean updateChat(String username, String matchName, String chat);

    //Removes A user and their match from the Match table
    boolean removeMatch(String username, String matchName);

    //Removes a user and their reject from the reject table
    boolean removeReject(String username, String rejectName);

    String getMatchChat(String username, String matchName);

}
