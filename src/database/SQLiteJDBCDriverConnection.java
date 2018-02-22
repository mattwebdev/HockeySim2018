package database;

import startup.PlayerGen;
import startup.TeamGen;

import java.io.File;
import java.sql.*;

public class SQLiteJDBCDriverConnection {
    /**
     * Connect to a sample database
     */
    public static void connect() {
        Connection conn = null;
        try {
            // db parameters
            String url = "jdbc:sqlite:C:/sqlite/dbs/hockeyDb.db";
            // create a connection to the database
            conn = DriverManager.getConnection(url);

            System.out.println("Connection to SQLite has been established.");

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } finally {
            try {
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException ex) {
                System.out.println(ex.getMessage());
            }
        }
    }
    public static void createNewDatabase() {
        String filepath = "C:/sqlite/dbs/hockeyDb.db";
        File fPath = new File(filepath);
        String url = "jdbc:sqlite:" + filepath;

        if(fPath.exists()){
            System.out.println("Directory already exists");
            return;
        }
        try (Connection conn = DriverManager.getConnection(url)) {
            if (conn != null) {
                DatabaseMetaData meta = conn.getMetaData();
                System.out.println("The driver name is " + meta.getDriverName());
                System.out.println("A new database has been created.");
                createTables();
                doInitalRoutines(15);
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
    private static void doInitalRoutines(int teams){
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
        String url = "jdbc:sqlite:C://sqlite/dbs/hockeyDb.db";
        String sql = "CREATE TABLE Game( HomeID INTEGER, AwayID INTEGER," +
                " GameLog TEXT, HomeGoals INTEGER, AwayGoals INTEGER, HomeShots" +
                " INTEGER, AwayShots INTEGER )";
        String sql2 = "CREATE TABLE Player( PlayerID INTEGER PRIMARY KEY AUTOINCREMENT, Name TEXT," +
                " Position TEXT, Age INTEGER, TeamID INTEGER, Faceoff INTEGER, OffensiveSkills" +
                " INTEGER, DefensiveSkills INTEGER, GoalieSkills INTEGER )";
        String sql3 = "CREATE TABLE Team( TeamID INTEGER PRIMARY KEY, Wins INTEGER, Loses INTEGER, Ties INTEGER," +
                " LW1 INTEGER," +
                " C1 INTEGER, RW1 INTEGER, LW2 INTEGER, C2" +
                " INTEGER, RW2 INTEGER,LW3 INTEGER,C3 INTEGER, RW3 INTEGER, LW4 INTEGER," +
                " C4 INTEGER, RW4 , LD1 INTEGER, RD1 INTEGER, LD2 INTEGER, RD2 INTEGER," +
                " LD3 INTEGER, RD3 INTEGER, G1 INTEGER, G2 INTEGER)";
        String sql4 = "CREATE TABLE PlayerStats( PlayerID INTEGER PRIMARY KEY , GamesPlayed INTEGER, Goals INTEGER," +
                " Assists INTEGER, Shots INTEGER, Points INTEGER)";
        try (Connection conn = DriverManager.getConnection(url);
             Statement stmt = conn.createStatement()) {
            // create a new table
            stmt.execute(sql);
            stmt.execute(sql2);
            stmt.execute(sql3);
            stmt.execute(sql4);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
    /*public static void main(String[] args) {
        connect();
    }*/
}
