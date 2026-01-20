package comp3350.pioneers.persistence.all;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

/*
This sets up the database
I know the Schema is hard coded. I tried for days to get android studio to read the database from assets
I went to Saulo for help and we could not figure it out
Please have mercy when Marking

 */


public class DatabaseHelper {

    public static void initializeDatabase(String dbPath){
       try {
            Class.forName("org.hsqldb.jdbc.JDBCDriver");
            System.out.print("the db path in databaseHelper is :" + dbPath);
            try(Connection connection = DriverManager.getConnection("jdbc:hsqldb:file:" + dbPath + ";shutdown=true", "SA", "")){


                initializeAllUsers(connection);
                initializeAllMatches(connection);
                initializeAllRejects(connection);
                populateDatabase(connection);

            } catch (SQLException e){
                System.out.println("Errror in initializeDatabase " + e.getMessage());
            }
        } catch(Exception e){
            System.out.println("Error in outer catch " + e.getMessage());

        }
    }

    private static void initializeAllUsers(Connection connection) throws SQLException{
        try (Statement stmt = connection.createStatement()){
            stmt.executeUpdate(    "CREATE TABLE IF NOT EXISTS allUsers(" +
                    "    userName VARCHAR(50) NOT NULL PRIMARY KEY, " +
                    "    password VARCHAR(50) NOT NULL, " +
                    "    bio VARCHAR(200) DEFAULT 'Empty' NOT NULL, " +
                    "    interest1 VARCHAR(50) DEFAULT 'Empty' NOT NULL, " +
                    "    interest2 VARCHAR(50) DEFAULT 'Empty' NOT NULL, " +
                    "    interest3 VARCHAR(50) DEFAULT 'Empty' NOT NULL, " +
                    "    interest4 VARCHAR(50) DEFAULT 'Empty' NOT NULL, " +
                    "    matchType BOOLEAN DEFAULT TRUE" +
                    ")");
        }
    }

    private static void initializeAllMatches(Connection connection) throws SQLException{
        try(Statement stmt = connection.createStatement()){
            stmt.executeUpdate("CREATE TABLE IF NOT EXISTS allMatches (" +
                    "    userName VARCHAR(50) NOT NULL, " +
                    "    matchName VARCHAR(50) NOT NULL, " +
                    "    chat VARCHAR(10000) DEFAULT '' NOT NULL, " +
                    "    PRIMARY KEY (userName, matchName)" +
                    ")");
        }
    }

    private static void initializeAllRejects(Connection connection) throws SQLException{
        try(Statement stmt = connection.createStatement()){
            stmt.executeUpdate("CREATE TABLE IF NOT EXISTS allRejects(" +
                    "    userName VARCHAR(50) NOT NULL, " +
                    "    rejectName VARCHAR(50) NOT NULL, " +
                    "    PRIMARY KEY (userName, rejectName)" +
                    ")");
        }
    }


    private static void populateDatabase(Connection connection) throws SQLException{
        try(Statement stmt = connection.createStatement()){
            System.out.println("POPULATEING DATABASEE");
            stmt.executeUpdate("INSERT INTO allUsers (userName, password, bio, interest1, interest2, interest3, interest4, matchType) VALUES('Person1','password1','A basic User Bio','skiing','coffee','beer','wine', TRUE)");
            stmt.executeUpdate("INSERT INTO allUsers (userName, password, bio, interest1, interest2, interest3, interest4, matchType) VALUES('Frank','Ocean','A tornado flew around my room','Music','rnb','beer','wine', FALSE)" );
            stmt.executeUpdate("INSERT INTO allUsers (userName, password, bio, interest1, interest2, interest3, interest4, matchType) VALUES('Sally','sallypass','I love gold','Music','Sports','Gaming','Reading', TRUE)");
            stmt.executeUpdate("INSERT INTO allUsers (userName, password, bio, interest1, interest2, interest3, interest4, matchType) VALUES('Emily','Emily1','Love to explore new places','Travel','Photography','Cooking','Music', FALSE)");
            stmt.executeUpdate("INSERT INTO allUsers (userName, password, bio, interest1, interest2, interest3, interest4, matchType) VALUES('Michael','Michael1','Tech enthusiast and coffee lover','Technology','Gaming','Music','Movies', TRUE)");
            stmt.executeUpdate("INSERT INTO allUsers (userName, password, bio, interest1, interest2, interest3, interest4, matchType) VALUES('Sophia','Sophia1','Artist with a passion for travel','Art','Travel','Fashion','Photography', FALSE)");
            stmt.executeUpdate("INSERT INTO allUsers (userName, password, bio, interest1, interest2, interest3, interest4, matchType) VALUES('Alexander','Alexander1','Sports fan and foodie','Sports','Cooking','Fitness','Reading', TRUE)");
            stmt.executeUpdate("INSERT INTO allUsers (userName, password, bio, interest1, interest2, interest3, interest4, matchType) VALUES('Olivia','Olivia1','Dreamer and bookworm','Reading','Art','Music','Animals', FALSE)");
            stmt.executeUpdate("INSERT INTO allUsers (userName, password, bio, interest1, interest2, interest3, interest4, matchType) VALUES('William','William1','Outdoor enthusiast seeking adventure','Nature','Sports','Travel','Photography', TRUE)");
            stmt.executeUpdate("INSERT INTO allUsers (userName, password, bio, interest1, interest2, interest3, interest4, matchType) VALUES('Emma','Emma1','Passionate about good food and conversation','Cooking','Music','Reading','Art', FALSE)");
            stmt.executeUpdate("INSERT INTO allUsers (userName, password, bio, interest1, interest2, interest3, interest4, matchType) VALUES('Daniel','Daniel1','Engineer by day, musician by night','Music','Technology','Fitness','Movies', TRUE)");
            stmt.executeUpdate("INSERT INTO allUsers (userName, password, bio, interest1, interest2, interest3, interest4, matchType) VALUES('Ava','Ava1','Finding joy in the small things','Dance','Nature','Art','Reading', FALSE)");
            stmt.executeUpdate("INSERT INTO allUsers (userName, password, bio, interest1, interest2, interest3, interest4, matchType) VALUES('James','James1','Sports enthusiast who loves to travel','Sports','Travel','Fitness','Gaming', TRUE)");
            stmt.executeUpdate("INSERT INTO allUsers (userName, password, bio, interest1, interest2, interest3, interest4, matchType) VALUES('Charlotte','Charlotte1','Creative soul with a love for animals','Animals','Art','Photography','Dance', FALSE)");
            stmt.executeUpdate("INSERT INTO allUsers (userName, password, bio, interest1, interest2, interest3, interest4, matchType) VALUES('Benjamin','Benjamin1','Tech geek with a taste for adventure','Technology','Gaming','Photography','Travel', TRUE)");
            stmt.executeUpdate("INSERT INTO allUsers (userName, password, bio, interest1, interest2, interest3, interest4, matchType) VALUES('Mia','Mia1','Ocean lover and environmental advocate','Nature','Photography','Animals','Travel', FALSE)");
            stmt.executeUpdate("INSERT INTO allUsers (userName, password, bio, interest1, interest2, interest3, interest4, matchType) VALUES('Ethan','Ethan1','Nature enthusiast and history buff','Nature','Reading','Movies','Technology', TRUE)");
            stmt.executeUpdate("INSERT INTO allUsers (userName, password, bio, interest1, interest2, interest3, interest4, matchType) VALUES('Amelia','Amelia1','Balancing career and creativity','Art','Dance','Fashion','Fitness', FALSE)");
            stmt.executeUpdate("INSERT INTO allUsers (userName, password, bio, interest1, interest2, interest3, interest4, matchType) VALUES('Jacob','Jacob1','Always looking for the next challenge','Sports','Gaming','Fitness','Technology', TRUE)");
            stmt.executeUpdate("INSERT INTO allUsers (userName, password, bio, interest1, interest2, interest3, interest4, matchType) VALUES('Harper','Harper1','Dreamer with a practical side','Reading','Music','Art','Fashion', FALSE)");
            stmt.executeUpdate("INSERT INTO allUsers (userName, password, bio, interest1, interest2, interest3, interest4, matchType) VALUES('Lucas','Lucas1','Gaming enthusiast with a passion for coding','Gaming','Technology','Movies','Music', TRUE)");
            stmt.executeUpdate("INSERT INTO allUsers (userName, password, bio, interest1, interest2, interest3, interest4, matchType) VALUES('Evelyn','Evelyn1','Finding beauty in everyday moments','Photography','Nature','Art','Reading', FALSE)");
            stmt.executeUpdate("INSERT INTO allUsers (userName, password, bio, interest1, interest2, interest3, interest4, matchType) VALUES('Noah','Noah1','Adventurer seeking meaningful connections','Travel','Nature','Sports','Reading', TRUE)");


            stmt.executeUpdate("INSERT INTO allMatches (userName, matchName) VALUES('Sally', 'Benjamin')");
            stmt.executeUpdate("INSERT INTO allMatches (userName, matchName) VALUES('Sally', 'Emma')");
            stmt.executeUpdate("INSERT INTO allMatches (userName, matchName) VALUES('Sally', 'Michael')");
            stmt.executeUpdate("INSERT INTO allMatches (userName, matchName) VALUES('Sally', 'Olivia')");
            stmt.executeUpdate("INSERT INTO allMatches (userName, matchName) VALUES('Sally', 'Noah')");
            stmt.executeUpdate("INSERT INTO allMatches (userName, matchName) VALUES('Sally', 'Person1')");

            stmt.executeUpdate("INSERT INTO allMatches (userName, matchName) VALUES('Benjamin', 'Sally')");
            stmt.executeUpdate("INSERT INTO allMatches (userName, matchName) VALUES('Emma', 'Sally')");
            stmt.executeUpdate("INSERT INTO allMatches (userName, matchName) VALUES('Michael', 'Sally')");
            stmt.executeUpdate("INSERT INTO allMatches (userName, matchName) VALUES('Olivia', 'Sally')");
            stmt.executeUpdate("INSERT INTO allMatches (userName, matchName) VALUES('Noah', 'Sally')");
            stmt.executeUpdate("INSERT INTO allMatches (userName, matchName) VALUES('Person1', 'Sally')");

            stmt.executeUpdate("INSERT INTO allRejects (userName, rejectName) VALUES('Sally', 'Charlotte')");
            stmt.executeUpdate("INSERT INTO allRejects (userName, rejectName) VALUES('Sally', 'Daniel')");
            stmt.executeUpdate("INSERT INTO allRejects (userName, rejectName) VALUES('Sally', 'Sophia')");
            stmt.executeUpdate("INSERT INTO allRejects (userName, rejectName) VALUES('Sally', 'Jacob')");
            stmt.executeUpdate("INSERT INTO allRejects (userName, rejectName) VALUES('Sally', 'Harper')");

            stmt.executeUpdate("INSERT INTO allMatches (userName, matchName) VALUES('Alexander', 'Mia')");
            stmt.executeUpdate("INSERT INTO allMatches (userName, matchName) VALUES('Alexander', 'William')");
            stmt.executeUpdate("INSERT INTO allMatches (userName, matchName) VALUES('Alexander', 'Charlotte')");
            stmt.executeUpdate("INSERT INTO allMatches (userName, matchName) VALUES('Alexander', 'Harper')");
            stmt.executeUpdate("INSERT INTO allMatches (userName, matchName) VALUES('Alexander', 'Ethan')");


            stmt.executeUpdate("INSERT INTO allMatches (userName, matchName) VALUES('Mia', 'Alexander')");
            stmt.executeUpdate("INSERT INTO allMatches (userName, matchName) VALUES('William', 'Alexander')");
            stmt.executeUpdate("INSERT INTO allMatches (userName, matchName) VALUES('Charlotte', 'Alexander')");
            stmt.executeUpdate("INSERT INTO allMatches (userName, matchName) VALUES('Harper', 'Alexander')");
            stmt.executeUpdate("INSERT INTO allMatches (userName, matchName) VALUES('Ethan', 'Alexander')");


            stmt.executeUpdate("INSERT INTO allRejects (userName, rejectName) VALUES('Alexander', 'Emma')");
            stmt.executeUpdate("INSERT INTO allRejects (userName, rejectName) VALUES('Alexander', 'Lucas')");
            stmt.executeUpdate("INSERT INTO allRejects (userName, rejectName) VALUES('Alexander', 'Daniel')");
            stmt.executeUpdate("INSERT INTO allRejects (userName, rejectName) VALUES('Alexander', 'Olivia')");
            stmt.executeUpdate("INSERT INTO allRejects (userName, rejectName) VALUES('Alexander', 'Amelia')");

            stmt.executeUpdate("INSERT INTO allMatches (userName,matchName) VALUES ('Person1','Sally')");
            stmt.executeUpdate("INSERT INTO allRejects (userName,rejectName) VALUES ('Frank', 'Sally')");

            stmt.executeUpdate("INSERT INTO allMatches (userName,matchName) VALUES ('Person1','Emily')");
            stmt.executeUpdate("INSERT INTO allMatches (userName,matchName) VALUES ('Person1','Emma')");
            stmt.executeUpdate("INSERT INTO allMatches (userName,matchName) VALUES ('Person1','Mia')");
            stmt.executeUpdate("INSERT INTO allMatches (userName,matchName) VALUES ('Person1','Jacob')");

            stmt.executeUpdate("INSERT INTO allMatches (userName,matchName) VALUES ('Emily','Person1')");
            stmt.executeUpdate("INSERT INTO allMatches (userName,matchName) VALUES ('Emma','Person1')");
            stmt.executeUpdate("INSERT INTO allMatches (userName,matchName) VALUES ('Mia','Person1')");
            stmt.executeUpdate("INSERT INTO allMatches (userName,matchName) VALUES ('Jacob','Person1')");

            stmt.executeUpdate("INSERT INTO allRejects (userName,rejectName) VALUES ('Person1', 'Noah')");
            stmt.executeUpdate("INSERT INTO allRejects (userName,rejectName) VALUES ('Person1', 'Lucas')");
            stmt.executeUpdate("INSERT INTO allRejects (userName,rejectName) VALUES ('Person1', 'Harper')");

            stmt.executeUpdate("INSERT INTO allMatches (userName, matchName) VALUES('Mia', 'Person1')");

        }
    }
}
