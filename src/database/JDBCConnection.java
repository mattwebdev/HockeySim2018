package database;

import startup.PlayerGen;
import startup.TeamGen;

import java.sql.*;

public class JDBCConnection {
    /**
     * Connect to a sample database
     */
    private static Connection connection = null;

    public static void dropDatabase(){
        if(connection != null){
            try {
                Statement statement = connection.createStatement();
                String sql = "CREATE DATABASE hockeydb";
                //To delete database: sql = "DROP DATABASE DBNAME";
                statement.executeUpdate(sql);
            }
            catch(SQLException e){
                e.printStackTrace();
            }

        }
    }
    public static Connection getConnection() {
        if(connection != null){
            return connection;
        }
        else{
            return null;
        }
    }
    public static void createNewDatabase() {

        String path = "jdbc:postgresql://localhost/";

        String name = "postgres";

        String url = path + name;
        boolean errorFound = false;

        try {
            connection = DriverManager.getConnection(url, "postgres", "admin");

            Statement statement = connection.createStatement();
            String sql = "CREATE DATABASE hockeydb";
            //To delete database: sql = "DROP DATABASE DBNAME";

            statement.executeUpdate(sql);
            System.out.println("Database created!");

        }
        catch(SQLException sqlException){
            if (sqlException.getErrorCode() == 0)
            {
                // Database already exists error
                System.out.println(sqlException.getMessage());

            }
            else {
                errorFound = true;
                sqlException.printStackTrace();
            }
        }

        if(!errorFound){
            try {
                connection = DriverManager.getConnection(path + "hockeydb", "postgres", "admin");

                createTables();
                doInitialRoutines(15);
            }
            catch(SQLException e){
                System.out.println("Error generating default setup");
                e.printStackTrace();
            }
        }
    }
    private static void doInitialRoutines(int teams){
        PlayerGen.populatePlayerDB(450);
        for(int i=1; i<=teams; ++i){
            TeamGen.buildTeam(i);
            TeamDb.assignLinesDefault(i);
        }
        //TeamGen.buildTeam(1);
        //TeamGen.buildTeam(2);
        //TeamDb.assignLinesDefault(1);
        //TeamDb.assignLinesDefault(2);
        PlayerStatsDb.insertPlayerStats(450);
    }
    private static void createTables(){

        String sql = "CREATE TABLE Game( HomeID INTEGER, AwayID INTEGER," +
                " GameLog TEXT, HomeGoals INTEGER, AwayGoals INTEGER, HomeShots" +
                " INTEGER, AwayShots INTEGER )";
        String sql2 = "CREATE TABLE Player( PlayerID SERIAL PRIMARY KEY, Name TEXT," +
                " Potential INTEGER, Position TEXT, Age INTEGER, TeamID INTEGER, Faceoff INTEGER, " +
                "OffensiveSkills INTEGER CHECK(OffensiveSkills >= 0 AND OffensiveSkills <= 100), " +
                "DefensiveSkills INTEGER  CHECK(DefensiveSkills >=0 AND DefensiveSkills <= 100)," +
                " GoalieSkills INTEGER  CHECK(GoalieSkills >= 0 AND GoalieSkills <= 100) )";
        String sql3 = "CREATE TABLE Team( TeamID INTEGER PRIMARY KEY, Wins INTEGER, Loses INTEGER, Ties INTEGER," +
                " LW1 INTEGER," +
                " C1 INTEGER, RW1 INTEGER, LW2 INTEGER, C2" +
                " INTEGER, RW2 INTEGER,LW3 INTEGER,C3 INTEGER, RW3 INTEGER, LW4 INTEGER," +
                " C4 INTEGER, RW4 INTEGER , LD1 INTEGER, RD1 INTEGER, LD2 INTEGER, RD2 INTEGER," +
                " LD3 INTEGER, RD3 INTEGER, G1 INTEGER, G2 INTEGER)";
        String sql4 = "CREATE TABLE PlayerStats( PlayerID INTEGER PRIMARY KEY , GamesPlayed INTEGER, Goals INTEGER," +
                " Assists INTEGER, Shots INTEGER, Points INTEGER)";
        String sql5 = "CREATE TABLE Schedule (Year INTEGER, HomeID INTEGER, AwayID INTEGER, Day INTEGER)";
        try {
            Statement stmt = connection.createStatement();
            // create a new table
            stmt.execute(sql);
            stmt.execute(sql2);
            stmt.execute(sql3);
            stmt.execute(sql4);
            stmt.execute(sql5);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
    /*public static void main(String[] args) {
        connect();
    }*/
}
