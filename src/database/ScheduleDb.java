package database;

import leaguemgmt.Matchup;
import player.Player;

import java.sql.*;
import java.util.ArrayList;

public class ScheduleDb {
    public static void addSchedToDb(ArrayList<ArrayList<Matchup>> schedule, int year){
        String sql = "INSERT INTO Schedule" +
                "(Year, HomeID, AwayID, Day)" +
                "VALUES(?,?,?,?)";
        String url = "jdbc:sqlite:C:/sqlite/dbs/hockeyDb.db";
        try(Connection conn = DriverManager.getConnection(url)){
            conn.setAutoCommit(false);
            PreparedStatement pstmt = conn.prepareStatement(sql);
            int day = 1;
            for(ArrayList<Matchup> daysMatchup: schedule){
                for(Matchup m : daysMatchup){
                    int homeid = m.getHomeTeamID();
                    int awayid = m.getAwayTeamID();
                    pstmt.setInt(1,year);
                    pstmt.setInt(2,homeid);
                    pstmt.setInt(3,awayid);
                    pstmt.setInt(4,day);

                    pstmt.executeUpdate();
                }
                day++;
            }
            conn.commit();
            System.out.println("DONE adding sched");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
    public static ArrayList<Matchup> getGamesOnDate(int day, int year){
        ArrayList<Matchup> daysGames = new ArrayList<>();
        String sql = "SELECT HomeID, AwayID "
                + "FROM Schedule WHERE Day= ? AND Year = ?";
        String url = "jdbc:sqlite:C:/sqlite/dbs/hockeyDb.db";
        try(Connection conn = DriverManager.getConnection(url)) {
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1,day);
            pstmt.setInt(2, year);
            ResultSet rs = pstmt.executeQuery();
            while(rs.next()){
                daysGames.add(new Matchup(rs.getInt("HomeID"), rs.getInt("AwayID")));
            }
        }
        catch(SQLException e){
            System.out.println(e.getMessage());
        }
        return daysGames;
    }
}
