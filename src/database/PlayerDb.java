package database;

import player.Player;

import java.sql.*;
import java.util.ArrayList;

public class PlayerDb {
    /*
    PlayerID INTEGER AUTOINCREMENT, Name TEXT," +
                " Position TEXT, TeamID INTEGER, Faceoff INTEGER, OffensiveSkills" +
                " INTEGER, DefensiveSkills INTEGER, GoalieSkills INTEGER

   public Player(String name, String position, int faceoff, int teamID, int offensiveSkills, int defensiveSkills, int goalieskills){
     */

    public static void insertPlayer(Player p){
        String sql = "INSERT INTO Player" +
                "(Name, Position,TeamID,Faceoff,OffensiveSkills,DefensiveSkills, GoalieSkills) " +
                "VALUES(?,?,?,?,?,?,?)";
        String url = "jdbc:sqlite:C:/sqlite/dbs/hockeyDb.db";
        try(Connection conn = DriverManager.getConnection(url)){
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, p.getName());
            pstmt.setString(2, p.getPosition());
            pstmt.setInt(3, p.getTeamID());
            pstmt.setInt(4, p.getFaceoff());
            pstmt.setInt(5, p.getOffensiveSkills());
            pstmt.setInt(6, p.getDefensiveSkills());
            pstmt.setInt(7, p.getGoalieSkills());

            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
    public static Player getPlayer(int pid){
        String sql = "SELECT PlayerID, Name, Position, TeamID, Faceoff, OffensiveSkills, DefensiveSkills, GoalieSkills "
                + "FROM Player WHERE PlayerID= ?";
        String url = "jdbc:sqlite:C:/sqlite/dbs/hockeyDb.db";
        try(Connection conn = DriverManager.getConnection(url)) {
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1,pid);

            ResultSet rs = pstmt.executeQuery();
            Player p = new Player(rs.getInt("PlayerID"),rs.getString("Name"),
                    rs.getString("Position"), rs.getInt("Faceoff"),
                    rs.getInt("TeamID"), rs.getInt("OffensiveSkills"),
                    rs.getInt("DefensiveSkills"), rs.getInt("GoalieSkills"));
            return p;
        }
        catch(SQLException e){
            System.out.println(e.getMessage());
        }
        return null;
    }
    public static void updatePlayerTeam(int playerID, int newTeamID){
        String sql = "UPDATE Player SET TeamID = ?"
                + "WHERE PlayerID = ?";

        String url = "jdbc:sqlite:C:/sqlite/dbs/hockeyDb.db";
        try (Connection conn = DriverManager.getConnection(url)){
            PreparedStatement pstmt = conn.prepareStatement(sql);

            // set the corresponding param
            pstmt.setInt(1, newTeamID);
            pstmt.setInt(2, playerID);
            // update
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
    public static ArrayList<Integer> getUnassignedPlayers(){
        ArrayList<Integer> unassigned = new ArrayList<Integer>();
        String sql = "SELECT PlayerID "
                + "FROM Player WHERE TeamID = 0";

        String url = "jdbc:sqlite:C:/sqlite/dbs/hockeyDb.db";
        try(Connection conn = DriverManager.getConnection(url)){
            PreparedStatement pstmt  = conn.prepareStatement(sql);

            // using prepared statements for query
            //pstmt.setDouble(1,someval);
            //
            ResultSet rs  = pstmt.executeQuery();

            // loop through the result set
            while (rs.next()) {
                unassigned.add(rs.getInt("PlayerID"));
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return unassigned;
    }
    public static ArrayList<Integer> getTeamPlayers(int teamid){
        ArrayList<Integer> unassigned = new ArrayList<Integer>();
        String sql = "SELECT PlayerID "
                + "FROM Player WHERE TeamID = ?";

        String url = "jdbc:sqlite:C:/sqlite/dbs/hockeyDb.db";
        try(Connection conn = DriverManager.getConnection(url)){
            PreparedStatement pstmt  = conn.prepareStatement(sql);

            // using prepared statements for query
            pstmt.setDouble(1,teamid);
            //
            ResultSet rs  = pstmt.executeQuery();

            // loop through the result set
            while (rs.next()) {
                unassigned.add(rs.getInt("PlayerID"));
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return unassigned;
    }
}
