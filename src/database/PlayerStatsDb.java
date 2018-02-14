package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class PlayerStatsDb {
    public static void insertPlayerStats(int numPlayers){
        for(int count = 1 ; count <= numPlayers; ++count) {
            String sql = "INSERT INTO PlayerStats" +
                    "(PlayerID, Goals,Assists,Shots,Points) " +
                    "VALUES(?,0,0,0,0)";
            String url = "jdbc:sqlite:C:/sqlite/dbs/hockeyDb.db";
            try (Connection conn = DriverManager.getConnection(url)) {
                PreparedStatement pstmt = conn.prepareStatement(sql);
                pstmt.setInt(1, count);

                pstmt.executeUpdate();
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }
        }
    }
    public static void updateGoals(int pid){
        String sql = "UPDATE PlayerStats SET Goals = Goals+1"
                + " WHERE PlayerID = ?";
        String sql2 = "UPDATE PlayerStats SET Points = Points+1"
                + " WHERE PlayerID = ?";
        String url = "jdbc:sqlite:C:/sqlite/dbs/hockeyDb.db";
        try (Connection conn = DriverManager.getConnection(url)){
            PreparedStatement pstmt = conn.prepareStatement(sql);
            PreparedStatement pstmt2 = conn.prepareStatement(sql2);
            // set the corresponding param
            pstmt.setInt(1, pid);
            pstmt2.setInt(1,pid);
            // update
            pstmt.executeUpdate();
            pstmt2.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
    public static void updateAssists(int pid){
        String sql = "UPDATE PlayerStats SET Assists = Assists+1"
                + " WHERE PlayerID = ?";
        String sql2 = "UPDATE PlayerStats SET Points = Points+1"
                + " WHERE PlayerID = ?";
        String url = "jdbc:sqlite:C:/sqlite/dbs/hockeyDb.db";
        try (Connection conn = DriverManager.getConnection(url)){
            PreparedStatement pstmt = conn.prepareStatement(sql);
            PreparedStatement pstmt2 = conn.prepareStatement(sql2);
            // set the corresponding param
            pstmt.setInt(1, pid);
            pstmt2.setInt(1, pid);
            // update
            pstmt.executeUpdate();
            pstmt2.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
    public static void updateShots(int pid){
        String sql = "UPDATE PlayerStats SET Shots = Shots+1"
                + " WHERE PlayerID = ?";

        String url = "jdbc:sqlite:C:/sqlite/dbs/hockeyDb.db";
        try (Connection conn = DriverManager.getConnection(url)){
            PreparedStatement pstmt = conn.prepareStatement(sql);

            // set the corresponding param
            pstmt.setInt(1, pid);
            // update
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
}
