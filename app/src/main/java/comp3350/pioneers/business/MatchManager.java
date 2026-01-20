package comp3350.pioneers.business;

import java.util.ArrayList;
import java.util.List;

import comp3350.pioneers.objects.Match;
import comp3350.pioneers.persistence.all.IAllUsersPersistence;
import comp3350.pioneers.persistence.all.IMatchPersistence;

//this class manages match-related operations including creating matches, handling rejections, and retrieving match information.

public class MatchManager {

    private final IAllUsersPersistence userDB;
    private final IMatchPersistence matchDB;

    public MatchManager(){
        this.userDB = Services.getUserDB();
        this.matchDB = Services.getMatchDB();
    }

    //constructor for the MatchManager class
    public MatchManager(IAllUsersPersistence userDB, IMatchPersistence matchDB) {
        this.userDB = userDB;
        this.matchDB = matchDB;
}

    //adds a user to the rejection list when current user clicks No
    public boolean rejectUser(String currentUsername, String rejectedUsername) {
        boolean success = false;

        if (currentUsername != null && !currentUsername.isEmpty()
                && rejectedUsername != null && !rejectedUsername.isEmpty()) {

            //check if already rejected to avoid duplicates
            if (!matchDB.checkForReject(currentUsername, rejectedUsername)) {
                //add to reject database
                success = matchDB.addReject(currentUsername, rejectedUsername);
            } else {
                //already rejected, consider this a success
                success = true;
            }
        }

        return success;
    }

    // This also needs to create the newly matched person Match object and add it to the array list in Services
    //creates a match directly when current user clicks Yes

    public boolean createMatch(String currentUsername, String matchedUsername) {
        boolean success = false;

        if (currentUsername != null && !currentUsername.isEmpty()
                && matchedUsername != null && !matchedUsername.isEmpty()) {

            //check if already matched to avoid duplicates
            if (!matchDB.checkForMatch(currentUsername, matchedUsername)) {
                //add to match database
                success = matchDB.addMatch(currentUsername, matchedUsername);
            } else {
                //already matched, consider this a success
                success = true;
            }
        }

        return success;
    }

    //retrieves all matches for the current user

    public List<Match> getUserMatches(String currentUsername) {
        List<Match> matches = new ArrayList<>();

        if (currentUsername != null && !currentUsername.isEmpty()) {
            //get matches from the database
            matches = matchDB.getAllMatches(currentUsername);

        }

        return matches;
    }


    //This is good, but it should just be getting all of the info from Match match object, doesn't need to get anything from the database
    //gets the match information for a specific matched user
    public String[] getMatchInfo(Match match) {
        // Format of the returned array:
        // [0]: username
        // [1]: bio
        // [2-5]: interests
        String[] matchInfo = new String[6];

        if (match != null) {
            //get the username
            matchInfo[0] = match.getUserName();

            //get the bio
            matchInfo[1] = match.getBio();

            //get the interests
            matchInfo[2] = match.getInterest1();
            matchInfo[3] = match.getInterest2();
            matchInfo[4] = match.getInterest3();
            matchInfo[5] = match.getInterest4();
        }

        return matchInfo;
    }




    //get chat
    public String getMatchChat(Match match) {
        String chat = "";

        if (match != null) {
            //get chat from the match object
            chat = match.getChat();
        }

        return chat;
    }

    public boolean addChatMessage(Match match, String sender, String message) {
        boolean success = false;

        if (match != null && sender != null && !sender.isEmpty()
                && message != null && !message.isEmpty()) {

            if ("Empty".equals(match.getChat())) {
                match.setChat(""); // clear empty when a message is sent
            }

            // add message to local match object
            match.addChatMessage(sender, message);

            String updatedChat = match.getChat();
            String receiver = match.getUserName(); // the person you're chatting with

            // Update chat in *both* directions

            boolean first = matchDB.updateChat(sender, receiver, updatedChat);
            boolean second = matchDB.updateChat(receiver, sender, updatedChat);

           // success = first && second;
            success = first;

            if (first && second) {
                System.out.println("Chat saved for: " + sender + " and " + receiver);
            }
            else{
                System.out.println("Chat saved for sender only - one sided match");
            }
        }

        return success;
    }



    //checks if a potential match has already been rejected
    public boolean isRejected(String currentUsername, String potentialMatch) {
        boolean rejected = false;

        if (currentUsername != null && !currentUsername.isEmpty()
                && potentialMatch != null && !potentialMatch.isEmpty()) {

            rejected = matchDB.checkForReject(currentUsername, potentialMatch);
        }

        return rejected;
    }

    //checks if a potential match is already a match
    public boolean isMatched(String currentUsername, String potentialMatch) {
        boolean matched = false;

        if (currentUsername != null && !currentUsername.isEmpty()
                && potentialMatch != null && !potentialMatch.isEmpty()) {

            matched = matchDB.checkForMatch(currentUsername, potentialMatch);
        }

        return matched;
    }


    //This will accept the arrayList of match objects, and a name of the person we want to find, and return their
    //Match object.
    //This will be used when a person clicks on a match in the MatchesList gui page to get that persons info to populate the ViewMatch gui
    public Match getMatchFromList(String nameToFind, List<Match> matchList) {
        Match fromList = null;

        if (nameToFind != null && matchList != null) {
            for (Match match : matchList) {
                if (match != null && nameToFind.equals(match.getUserName())) {
                    fromList = match;
                    break;
                }
            }
        }

        return fromList;
    }
    public Match getRandomMatch(String username){
        Match rando = null;

        if(username!= null){
            rando = matchDB.getRandomMatch(username);
        }
        return rando;
    }

    public Match getMatchInfo(String currentUsername, String matchUsername) {
        return matchDB.getMatchInfo(currentUsername, matchUsername);
    }

    // Remove match when reported
    public boolean reportMatch(String user1 , String user2){

        boolean rejectUser1 = matchDB.removeMatch(user1,user2);
        boolean rejectUser2 = matchDB.removeMatch(user2, user1);

        if(rejectUser1 && rejectUser2){
            System.out.println("Match removed for both users : " + user1 +" " + user2);
        } else if (!rejectUser1){
            System.out.println("Can not remove match from " + user1 +" only matched one side ");
        } else if (!rejectUser2) {
            System.out.println("Can not remove match from " + user2 +" only matched one side ");
        } else{
            System.out.println("Failed to remove the match");
        }
        
        return rejectUser1 && rejectUser2;

    }


}
