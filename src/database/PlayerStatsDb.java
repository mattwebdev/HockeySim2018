package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class PlayerStatsDb {
    public static void insertPlayerStats(int numPlayers){
        for(int count = 1 ; count <= numPlayers; ++count) {
            String sql = "INSERT INTO PlayerStats" +
                    "(PlayerID, GamesPlayed, Goals,Assists,Shots,Points) " +
                    "VALUES(?,0,0,0,0,0)";

            Connection conn = JDBCConnection.getConnection();
            if(conn == null){
                System.out.println("Error connecting to db");
                return;
            }
            try{
                PreparedStatement pstmt = conn.prepareStatement(sql);
                pstmt.setInt(1, count);

                pstmt.executeUpdate();
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }
        }
    }
    public static void updateGames(int pid){
        String sql = "UPDATE PlayerStats SET GamesPlayed = GamesPlayed+1"
                + " WHERE PlayerID = ?";

        Connection conn = JDBCConnection.getConnection();
        if(conn == null){
            System.out.println("Error connecting to db");
            return;
        }
        try{
            PreparedStatement pstmt = conn.prepareStatement(sql);

            // set the corresponding param
            pstmt.setInt(1, pid);
            // update
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
    public static void updateGoals(int pid, int goals){
        String sql = "UPDATE PlayerStats SET Goals = Goals+?"
                + " WHERE PlayerID = ?";
        String sql2 = "UPDATE PlayerStats SET Points = Points+?"
                + " WHERE PlayerID = ?";

        Connection conn = JDBCConnection.getConnection();
        if(conn == null){
            System.out.println("Error connecting to db");
            return;
        }
        try{
            PreparedStatement pstmt = conn.prepareStatement(sql);
            PreparedStatement pstmt2 = conn.prepareStatement(sql2);
            // set the corresponding param
            pstmt.setInt(1, goals);
            pstmt.setInt(2,pid);
            pstmt2.setInt(1,goals);
            pstmt2.setInt(2,pid);
            // update
            pstmt.executeUpdate();
            pstmt2.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
    public static void updateAssists(int pid, int assists){
        String sql = "UPDATE PlayerStats SET Assists = Assists+?"
                + " WHERE PlayerID = ?";
        String sql2 = "UPDATE PlayerStats SET Points = Points+?"
                + " WHERE PlayerID = ?";

        Connection conn = JDBCConnection.getConnection();
        if(conn == null){
            System.out.println("Error connecting to db");
            return;
        }
        try{
            PreparedStatement pstmt = conn.prepareStatement(sql);
            PreparedStatement pstmt2 = conn.prepareStatement(sql2);
            // set the corresponding param
            pstmt.setInt(1, assists);
            pstmt.setInt(2,pid);
            pstmt2.setInt(1, assists);
            pstmt2.setInt(2,pid);
            // update
            pstmt.executeUpdate();
            pstmt2.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
    public static void updateShots(int pid, int shots){
        String sql = "UPDATE PlayerStats SET Shots = Shots+?"
                + " WHERE PlayerID = ?";

        Connection conn = JDBCConnection.getConnection();
        if(conn == null){
            System.out.println("Error connecting to db");
            return;
        }
        try{
            PreparedStatement pstmt = conn.prepareStatement(sql);

            // set the corresponding param
            pstmt.setInt(1, shots);
            pstmt.setInt(2, pid);
            // update
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
}
