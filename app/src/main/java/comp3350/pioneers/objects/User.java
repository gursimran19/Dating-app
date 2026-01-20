package comp3350.pioneers.objects;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class User {

    private final String username;
    private final String password;
    private String bio;
    private String interest1;
    private String interest2;
    private String interest3;
    private String interest4;
    private boolean matchType;
    private List<Match> matches; // list of the matches


    //make user
    public User(String username, String password) {
        if (username == null || password == null) {
            throw new IllegalArgumentException("Username and password cannot be null");
        }
        this.username = username;
        this.password = password;
        this.matches = new ArrayList<>();
    }


    //Used to set both the user bio and all of the interests
    //I should have split them up into interests and bio but it is too late for such things
    public void setUserBio(String[] bio){
        if(bio.length == 5){
            this.bio = bio[0];
            this.interest1 = bio[1];
            this.interest2 = bio[2];
            this.interest3 = bio[3];
            this.interest4 = bio[4];
        }

    }

    public void setUserMatchType(boolean matchType){
        this.matchType = matchType;
    }
    //Returns ONlY the users biography blurb
    public String getUserBio(){
        return bio;
    }

    //Returns an array of the users interests
    public String[] getUserInterests(){
        String[] interests = new String[4];
        interests[0] = interest1;
        interests[1] = interest2;
        interests[2] = interest3;
        interests[3] = interest4;

        return interests;
    }


    //return username
    public String getUsername() {
        return username;
    }

    //return email
    public String getPassword() {
        return password;
    }

    public boolean getUserMatchType(){return matchType;}

    public List<Match> getMatches() {
        return matches;
    }

    public void setMatches(List<Match> matches) {
        this.matches = matches;
    }



}