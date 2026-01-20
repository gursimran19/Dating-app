package comp3350.pioneers.persistence.all;

import comp3350.pioneers.objects.*;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class AllUsersDB implements IAllUsersPersistence {

    private final String dbPath;
    private static final String TAG = "AllUsersDB";

    public AllUsersDB(String dbPath) {
        this.dbPath = dbPath;
    }

    private Connection getConnection() throws SQLException {

        return DriverManager.getConnection("jdbc:hsqldb:file:" + dbPath + ";shutdown=true", "SA", "");

    }


    public String[] getUserData(String user) {
        String[] userData = new String[2];
        userData[0] = null;
        userData[1] = null;

        try (final Connection connection = getConnection())  {


            if(connection == null) {
                System.out.println("Database connection is null");
                return userData;
            }

            String sql = "SELECT userName, password FROM allUsers WHERE userName = ?";
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setString(1, user);
            ResultSet rs = stmt.executeQuery();

            if(rs.next()) {
                userData[0] = rs.getString("userName");
                userData[1] = rs.getString("password");
            }
            stmt.close();
            rs.close();

        } catch (SQLException e) {
            System.out.println("Error in getUserData: " + e.getMessage());
        }
        return userData;
    }

    public boolean searchForUser(String user) {
        boolean exists = false;
        //Connection connection = null;
        //PreparedStatement stmt = null;
        //ResultSet rs = null;

        try (final Connection connection = getConnection()) {


            if(connection == null) {
                System.out.println("Database connection error in searchForUser. Database is null");
                return false;
            }

            String sql = "SELECT userName from allUsers WHERE userName = ?";
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setString(1, user);
            ResultSet rs = stmt.executeQuery();

            exists = rs.next();
            rs.close();
            stmt.close();

        } catch(SQLException e) {
            System.out.println("Error in searchForUser: " + e.getMessage());
        }

        return exists;
    }

    public boolean addUserToDatabase(User user) {
        //
        //PreparedStatement stmt = null;
        boolean success = false;

        try (Connection connection = getConnection()) {

            System.out.println("Trying to register user: " + user.getUsername());


            if(connection == null) {
                System.out.println("Database Connection is null in addUserToDatabase");
                return false;
            }

            String sql = "INSERT INTO allUsers (userName, password) VALUES (?, ?)";
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setString(1, user.getUsername());
            stmt.setString(2, user.getPassword());

            int rowsChanged = stmt.executeUpdate();
            if(rowsChanged > 0) {
                System.out.println("Registration successful for: " + user.getUsername());
                success = true;
            }

            connection.commit();
            stmt.close();

        } catch (SQLException e) {
            System.out.println("Error in addUser to database: " + e.getMessage());
        }

        return success;
    }

    // The rest of your methods remain the same...
    // I'm keeping implementation for essential methods and can add more if needed

    public boolean updateUserBio(String username, String bio) {
        boolean updateStatus = false;

        try (Connection connection = getConnection()) {

            if(connection == null) {
                System.out.println("Database Connection is Null in updateUserBio");
                return false;
            }

            String sql = "UPDATE allUsers SET bio = ? WHERE userName = ?";
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setString(1, bio);
            stmt.setString(2, username);

            int updatedRows = stmt.executeUpdate();
            if(updatedRows > 0) {
                updateStatus = true;
                connection.commit();
            }
            stmt.close();

        } catch (SQLException e) {
            System.out.println("Error in updateUserBio: " + e.getMessage());
        }
        return updateStatus;
    }

    public boolean updateUserInterests(String username, String interest1, String interest2, String interest3, String interest4) {
        boolean updateSuccess = false;

        try (Connection connection = getConnection()) {


            if (connection == null) {
                System.out.println("Database connection is null in updateUserInterests");
                return false;
            }

            String sql = "UPDATE allUsers SET interest1 = ?, interest2 = ?, interest3 = ?, interest4 = ? WHERE userName = ?";
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setString(1, interest1);
            stmt.setString(2, interest2);
            stmt.setString(3, interest3);
            stmt.setString(4, interest4);
            stmt.setString(5, username);

            int rowsUpdated = stmt.executeUpdate();
            if (rowsUpdated > 0) {
                updateSuccess = true;
                connection.commit();
                System.out.println("Successfully updated interests for user: " + username);
            } else {
                System.out.println("No rows updated. User may not exist: " + username);
            }
            stmt.close();

        } catch (SQLException e) {
            System.out.println("Error updating user interests: " + e.getMessage());
        }

        return updateSuccess;
    }

    public boolean updateMatchingType(String userName, boolean romantic) {
        boolean updateStatus = false;

        try (Connection connection = getConnection()) {


            if(connection == null) {
                System.out.println("Database Connection is null in updateMatchingType");
                return false;
            }

            String sql = "UPDATE allUsers SET matchType = ? WHERE userName = ?";
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setBoolean(1, romantic);
            stmt.setString(2, userName);

            int rowsUpdated = stmt.executeUpdate();
            if(rowsUpdated > 0) {
                updateStatus = true;
                connection.commit();
            }

        } catch (SQLException e) {
            System.out.println("Error in updateMatchingType: " + e.getMessage());
        }

        return updateStatus;
    }

    public boolean getUserMatchType(String username) {
        boolean matchType = false;

        try (Connection connection = getConnection()) {


            if(connection == null) {
                System.out.println("Database Connection is null in getUserMatchType");
                return false;
            }

            String sql = "SELECT matchType FROM allUsers WHERE userName = ?";
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setString(1, username);

            ResultSet rs = stmt.executeQuery();
            if(rs.next()) {
                matchType = rs.getBoolean("matchType");
            }

            stmt.close();
            rs.close();

        } catch (SQLException e) {
            System.out.println("Error in getUserMatchType: " + e.getMessage());
        }

        return matchType;
    }

    public String[] getUserBio(String username) {
        String[] userBio = new String[5];


        try (Connection connection = getConnection()) {
            //connection = getConnection();

            if(connection == null) {
                System.out.println("Database Connection is null in getUserBio");
                return null;
            }

            String sql = "SELECT bio, interest1, interest2, interest3, interest4 FROM allUsers WHERE userName = ?";
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();

            if(rs.next()) {
                userBio[0] = rs.getString("bio");
                userBio[1] = rs.getString("interest1");
                userBio[2] = rs.getString("interest2");
                userBio[3] = rs.getString("interest3");
                userBio[4] = rs.getString("interest4");
            }

        } catch (SQLException e) {
            System.out.println("Error in getUserBio: " + e.getMessage());
        }
        return userBio;
    }
    @Override
    public List<User> getAllUsers() {
        List<User> users = new ArrayList<>();
        Connection connection = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            connection = getConnection();
            if (connection != null) {
                String sql = "SELECT userName, password FROM allUsers";
                stmt = connection.prepareStatement(sql);
                rs = stmt.executeQuery();

                while (rs.next()) {
                    String username = rs.getString("userName");
                    String password = rs.getString("password");
                    users.add(new User(username, password));
                }
            }
        } catch (SQLException e) {
            System.out.println("Error in getAllUsers: " + e.getMessage());
        } finally {
            try {
                if (stmt != null) stmt.close();
                if (connection != null) connection.close();
                if (rs != null) rs.close();
            } catch (SQLException e2) {
                System.out.println("Error closing DB in getAllUsers: " + e2.getMessage());
            }
        }

        return users;
    }

    public boolean removeUserFromDB(String username){
        boolean success = false;
        try (Connection connection = getConnection()) {

            if(connection == null) {
                System.out.println("Database Connection is null in getUserBio");
                return false;
            }

            String sql = "DELETE FROM allUsers WHERE userName = ?";
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setString(1, username);
            int rowsAffected = stmt.executeUpdate();
            if(rowsAffected > 0){
                success = true;
            }


        } catch (SQLException e) {
            System.out.println("Error in removeUserFromDB: " + e.getMessage());
        }
        return success;

    }





    // This method is essential - it creates your tables if they don't exist
//    private void initializeDatabase() {
//        Connection connection = null;
//        Statement stmt = null;
//
//        try {
//            // Make sure HSQLDB JDBC driver is loaded
//            Class.forName("org.hsqldb.jdbc.JDBCDriver");
//
//            connection = getConnection();
//            stmt = connection.createStatement();
//
//            // Create the allUsers table if it doesn't exist
//            stmt.execute(
//                    "CREATE TABLE IF NOT EXISTS allUsers(" +
//                            "userName VARCHAR(50) NOT NULL PRIMARY KEY," +
//                            "password VARCHAR(50) NOT NULL," +
//                            "bio VARCHAR(200) DEFAULT 'Empty' NOT NULL," +
//                            "interest1 VARCHAR(50) DEFAULT 'Empty' NOT NULL," +
//                            "interest2 VARCHAR(50) DEFAULT 'Empty' NOT NULL," +
//                            "interest3 VARCHAR(50) DEFAULT 'Empty' NOT NULL," +
//                            "interest4 VARCHAR(50) DEFAULT 'Empty' NOT NULL," +
//                            "matchType BOOLEAN DEFAULT TRUE" +
//                            ")"
//            );
//
//            // Create the matches table if it doesn't exist
//            stmt.execute(
//                    "CREATE TABLE IF NOT EXISTS matches(" +
//                            "userName VARCHAR(50)," +
//                            "matchedWith VARCHAR(50)," +
//                            "chat VARCHAR(2000)" +
//                            ")"
//            );
//
//            // Print database version for debugging
//            DatabaseMetaData metaData = connection.getMetaData();
//            System.out.println("Database initialized successfully. Version: " + metaData.getDatabaseProductVersion());
//
//            // Commit changes to ensure tables are created
//            connection.commit();
//
//        } catch (SQLException e) {
//            System.err.println("Error initializing database: " + e.getMessage());
//            e.printStackTrace();
//        } catch (ClassNotFoundException e) {
//            System.err.println("HSQLDB JDBC driver not found: " + e.getMessage());
//            e.printStackTrace();
//        } finally {
//            try {
//                if (stmt != null) stmt.close();
//                if (connection != null) connection.close();
//            } catch (SQLException e) {
//                System.err.println("Error closing resources: " + e.getMessage());
//            }
//        }
//    }
}
