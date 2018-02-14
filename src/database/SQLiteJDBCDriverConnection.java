package database;

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
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
    private static void createTables(){
        String url = "jdbc:sqlite:C://sqlite/dbs/hockeyDb.db";
        String sql = "CREATE TABLE Game( HomeID INTEGER, AwayID INTEGER," +
                " GameLog TEXT, HomeGoals INTEGER, AwayGoals INTEGER, HomeShots" +
                " INTEGER, AwayShots INTEGER )";
        String sql2 = "CREATE TABLE Player( PlayerID INTEGER PRIMARY KEY AUTOINCREMENT, Name TEXT," +
                " Position TEXT, TeamID INTEGER, Faceoff INTEGER, OffensiveSkills" +
                " INTEGER, DefensiveSkills INTEGER, GoalieSkills INTEGER )";
        try (Connection conn = DriverManager.getConnection(url);
             Statement stmt = conn.createStatement()) {
            // create a new table
            stmt.execute(sql);
            stmt.execute(sql2);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
    /*public static void main(String[] args) {
        connect();
    }*/
}
