package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class GameDb {
    public static void addGameToDatabase(int homeID, int awayID, String log,
                                         int homeGoals, int awayGoals, int homeShots, int awayShots){
        String sql = "INSERT INTO Game" +
                "(homeID,AwayID, GameLog,HomeGoals,AwayGoals,HomeShots,AwayShots) " +
                "VALUES(?,?,?,?,?,?,?)";
        String url = "jdbc:sqlite:C:/sqlite/dbs/hockeyDb.db";
        try(Connection conn = DriverManager.getConnection(url)){
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, homeID);
            pstmt.setInt(2, awayID);
            pstmt.setString(3, log);
            pstmt.setInt(4, homeGoals);
            pstmt.setInt(5, awayGoals);
            pstmt.setInt(6, homeShots);
            pstmt.setInt(7, awayShots);

            pstmt.executeUpdate();
            System.out.println("Successfully added Game table record");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

}
