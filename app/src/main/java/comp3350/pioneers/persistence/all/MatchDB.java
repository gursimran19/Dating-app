package comp3350.pioneers.persistence.all;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import comp3350.pioneers.objects.Match;

public class MatchDB implements IMatchPersistence {
    private final String dbPath;
    private static final String TAG = "AllUsersDB";



    public MatchDB(String dbPath) {
        this.dbPath = dbPath;
    }


    private Connection getConnection() throws SQLException {
        //Log.e(TAG, "The path Name is:" + dbPath);
        return DriverManager.getConnection("jdbc:hsqldb:file:" + dbPath + ";shutdown=true", "SA", "");
    }


    public boolean addMatch(String currentUsername, String newMatch){

        boolean success = false;

        try(final Connection connection = getConnection()){

            if(connection == null){
                return false;
            }

            String sql = "INSERT INTO allMatches (userName, matchName) values (?,?) ";
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setString(1, currentUsername);
            stmt.setString(2, newMatch);
            int rowsChanged = stmt.executeUpdate();
            if(rowsChanged > 0){
                System.out.println("Added " + currentUsername + " and " + newMatch + "to match db");
                success = true;
            }
            connection.commit();


        } catch (SQLException e){
            System.out.println("Sql exception in addMatch " + e.getMessage());
        }
        return success;
    }

    //Adds the currentUsername and rejectedUsername to the database
    //Returns true on success
    public boolean addReject(String currentUsername, String rejectedUsername){

        boolean success = false;

        try (final Connection connection = getConnection()){

            if(connection == null){
                return false;
            }

            String sql = "INSERT INTO allRejects (userName, rejectName) values (?,?) ";
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setString(1, currentUsername);
            stmt.setString(2,rejectedUsername);
            int rowsChanged = stmt.executeUpdate();
            if(rowsChanged > 0){
                System.out.println("Added " + currentUsername + " and " + rejectedUsername + "to reject db");
                success = true;
            }
            connection.commit();


        } catch (SQLException e){
            System.out.println("Sql exception in addReject " + e.getMessage());
        }
        return success;

    }

    //Checks database to see if the currentUsername and potentialMatch are already in the matched DB
    //True if already in matched DB, false if not.
    public boolean checkForMatch(String currentUsername, String potentialMatch){

        boolean exists = false;

        try (final Connection connection = getConnection()){

            if(connection == null){
                return false;
            }

            String sql = "SELECT * from allMatches WHERE userName = ? AND matchName = ? ";
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setString(1, currentUsername);
            stmt.setString(2,potentialMatch);
            ResultSet rs = stmt.executeQuery();

            exists = rs.next();

        } catch (SQLException e){
            System.out.println("Sql exception in addMatch " + e.getMessage());
        }

        return exists;
    }

    //Checks database to see if the currentUsername and potentialReject are already in the reject DB
    //Returns true is already in reject DB, false if not
    public boolean checkForReject(String currentUsername, String potentialReject){
        boolean exists = false;

        try (final Connection connection = getConnection()){

            if(connection == null){
                return false;
            }
            /*Updated matchName to rejectName*/
            String sql = "SELECT * from allRejects WHERE userName = ? AND rejectName = ? ";
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setString(1, currentUsername);
            stmt.setString(2,potentialReject);
            ResultSet rs = stmt.executeQuery();

            exists = rs.next();

        } catch (SQLException e){
            System.out.println("Sql exception in checkForReject " + e.getMessage());
        }

        return exists;
    }

    //Returns an arraylist of Match objects for each users match
    public List<Match> getAllMatches(String username){
        boolean exists = false;
        ArrayList<Match> matchList = new ArrayList<>();


        Match temp = null;
        try (final Connection connection = getConnection()){

            String sql = "SELECT u.*, m.chat FROM allUsers u JOIN allMatches m ON u.userName = m.matchName WHERE m.userName = ?";

            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setString(1,username);
            ResultSet rs = stmt.executeQuery();
            //debug statement
            System.out.println("Executing getAllMatches for: " + username);


            while(rs.next()){
                //debug statement
                System.out.println("Match found in DB: " + rs.getString("userName"));

                temp = new Match(rs.getString("userName"));
                //updated the bio line
                temp.setBio(rs.getString("bio"));
                temp.setChat(rs.getString("chat"));
                temp.setInterest1(rs.getString("interest1"));
                temp.setInterest2(rs.getString("interest2"));
                temp.setInterest3(rs.getString("interest3"));
                temp.setInterest4(rs.getString("interest4"));
                temp.setMatchType(rs.getBoolean("matchType"));

                matchList.add(temp);
                System.out.println("Retrieved raw chat string from DB: " + rs.getString("chat"));

            }

        } catch (SQLException e){
            System.out.println("Sql exception in checkForReject " + e.getMessage());
        }

        return matchList;
    }

    public String getMatchChat(String username, String matchName){
        String newChat = "";


        try(final Connection connection = getConnection()){
            String sql = "SELECT chat FROM allMatches WHERE userName = ? AND matchName = ?";
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setString(1,username);
            stmt.setString(2,matchName);
            ResultSet rs = stmt.executeQuery();

            if(rs.next()){
                newChat = rs.getString("chat");
            } else {
                System.out.println("Chat not retrieved");
            }

        } catch (SQLException e){
            System.out.println("SQL exception in getMatchChat " + e.getMessage());
        }


        return newChat;
    }

    //Retrieve a random user from the database that the user has not rejected or matched with
    public Match getRandomMatch(String currentUsername){
        boolean exists = false;
        Match randomMatch = null;

        try (final Connection connection = getConnection()){

            String sql = "SELECT u.* FROM allUsers u WHERE u.userName NOT IN (SELECT m.matchName FROM allMatches m WHERE m.userName = ?) " +
                    "AND u.userName NOT IN (SELECT r.rejectName FROM allRejects r WHERE r.userName = ?) " +
                    "AND u.userName != ?";
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setString(1, currentUsername);
            stmt.setString(2, currentUsername);
            stmt.setString(3, currentUsername);

            ResultSet rs = stmt.executeQuery();

            if(rs.next()){
                randomMatch = new Match(rs.getString("userName"));
                randomMatch.setBio(rs.getString("bio")); // Added set bio was missing
                randomMatch.setInterest1(rs.getString("interest1"));
                randomMatch.setInterest2(rs.getString("interest2"));
                randomMatch.setInterest3(rs.getString("interest3"));
                randomMatch.setInterest4(rs.getString("interest4"));
                randomMatch.setMatchType(rs.getBoolean("matchType"));
                randomMatch.setChat(rs.getString("chat"));// moved chat to end was giving issues
            }

        } catch (SQLException e){
            System.out.println("Sql exception in getRandomUser " + e.getMessage());

        }
        return randomMatch;
    }

    public Match getMatchInfo(String username, String matchName)
    {
        Match retrievedMatch = null;

        if(checkForMatch(username,matchName)){
            try(final Connection connection = getConnection()){
                String sql = "SELECT * From allUsers WHERE userName = ?";
                PreparedStatement stmt = connection.prepareStatement(sql);
                stmt.setString(1,matchName);
                ResultSet rs = stmt.executeQuery();

                if(rs.next()){
                    retrievedMatch = new Match(rs.getString("userName"));
                    retrievedMatch.setBio(rs.getString("bio"));
                    retrievedMatch.setInterest1(rs.getString("interest1"));
                    retrievedMatch.setInterest2(rs.getString("interest2"));
                    retrievedMatch.setInterest3(rs.getString("interest3"));
                    retrievedMatch.setInterest4(rs.getString("interest4"));
                    retrievedMatch.setMatchType(rs.getBoolean("matchType"));
                    retrievedMatch.setChat(getMatchChat(username,matchName));
                }


            } catch (SQLException e){
                System.out.println("SQL exception in getMatchInfo " + e.getMessage());
            }
        }
        return retrievedMatch;
    }

    public boolean updateChat(String username, String matchName, String chat){

        boolean success = false;
        try(final Connection connection = getConnection()){
            String sql = " UPDATE allMatches SET chat = ? WHERE userName = ? AND matchName = ?";
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setString(1,chat);
            stmt.setString(2,username);
            stmt.setString(3,matchName);
            System.out.println("Chat content being saved: " + chat);

            if(stmt.executeUpdate() > 0){
                System.out.println("Chat updated successfully");
                success = true;
            }
        } catch (SQLException e){
            System.out.println("Error updating chat in UpdateChat " + e.getMessage());
            return false;
        }
    return success;
    }


    public boolean removeMatch(String username, String matchName){
        boolean success = false;

        try(final Connection connection = getConnection()){
            String sql = "DELETE FROM allMatches WHERE userName = ? AND matchName = ?";
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setString(1,username);
            stmt.setString(2,matchName);

            if(stmt.executeUpdate() > 0){
                success = true;
            }

        } catch (SQLException e){
            System.out.println("Error in removeMatch " + e.getMessage());
        }

        return success;
    }


    public boolean removeReject(String username, String rejectName){
        boolean success = false;

        try(final Connection connection = getConnection()){
            String sql = "DELETE FROM allRejects WHERE userName = ? AND rejectName = ?";
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setString(1,username);
            stmt.setString(2,rejectName);

            if(stmt.executeUpdate() > 0){
                success = true;
            }

        } catch (SQLException e){
            System.out.println("Error in removeMatch " + e.getMessage());
        }

        return success;
    }
}
