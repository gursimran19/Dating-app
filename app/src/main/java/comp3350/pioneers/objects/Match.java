package comp3350.pioneers.objects;

import java.util.Objects;

// This class represents a match between two users in the system.
//stores info about two matches and a string for chat

public class Match {

    private final String userName;      //current user's username
    //private final String matchedWith;   //username of the matched person
    private boolean isActive;           //if the match is active or not
    private String bio;
    private String interest1;
    private String interest2;
    private String interest3;
    private String interest4;
    private boolean matchType;
    private String chat;                //string to store chat messages
    //example chat format:  [Bobby]: Hey there!\n[Jimmy]: Hi Bobby, how are you?\n[Bobby]: I'm good, thanks for asking!\n
    //Create a new match between two users, using usernames

    public Match(String userName) {
        if (userName == null) {
            throw new IllegalArgumentException("Usernames cannot be null");
        }
        this.userName = userName;
        this.chat = "";
        this.isActive = true;
    }


    public String getUserName() {
        return userName;
    }

    public String getChat() {
        return chat;
    }

    public void setChat(String chat) {
        this.chat = chat;
    }

    // add a message to the chat

    public void addChatMessage(String sender, String message) {
        // Format: [sender]: message\n
        String formattedMessage = "[" + sender + "]: " + message + "\n";
        this.chat = this.chat + formattedMessage;
    }

    //check if match is active
    public boolean isActive() {
        return isActive;
    }

    //set whether the match is active

    public void setActive(boolean active) {
        isActive = active;
    }

    public void setMatchType(boolean matchType){
        this.matchType = matchType;
    }
    public void setBio(String bioV ){ this.bio = bioV;}
    public void setInterest1(String interest1){
        this.interest1 = interest1;
    }
    public void setInterest2(String interest){
        this.interest2 = interest;
    }
    public void setInterest3(String interest){
        this.interest3 = interest;
    }
    public void setInterest4(String interest){
        this.interest4 = interest;
    }

    public boolean getMatchType() {return matchType;}

    public String getBio(){
        return bio;
    }
    public String getInterest1(){
        return interest1;
    }
    public String getInterest2(){
        return interest2;
    }
    public String getInterest3(){
        return interest3;
    }
    public String getInterest4(){
        return interest4;
    }
    //can add hashing and an equal method to make sure its unique
}