package database;

import player.Player;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;

public class TeamDb {
    private static void initializeLines(int teamid){
        String sql = "INSERT INTO Team" +
                     "( TeamID, LW1, C1, RW1," +
                " LW2, C2, RW2," +
                " LW3, C3, RW3," +
                " LW4, C4, RW4," +
                " LD1, RD1," +
                " LD2, RD2," +
                " LD3, RD3," +
                " G1, G2) VALUES " +
                "(?, NULL, NULL, NULL, " +
                "NULL, NULL, NULL," +
                "NULL, NULL, NULL," +
                "NULL, NULL, NULL," +
                "NULL, NULL," +
                "NULL, NULL," +
                "NULL, NULL," +
                "NULL, NULL)";

        String url = "jdbc:sqlite:C:/sqlite/dbs/hockeyDb.db";
        try (Connection conn = DriverManager.getConnection(url)){
            PreparedStatement pstmt = conn.prepareStatement(sql);

            // set the corresponding param
            pstmt.setInt(1, teamid);
            // update
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
    public static void assignLinesDefault(int teamid){
        initializeLines(teamid);
        String[] openPositions = {"LW1", "C1", "RW1",
                                  "LW2", "C2", "RW2",
                                  "LW3", "C3", "RW3",
                                  "LW4", "C4", "RW4",
                                  "LD1", "RD1",
                                  "LD2", "RD2",
                                  "LD3", "RD3", "G1", "G2"};
        ArrayList<Integer> playerids = PlayerDb.getTeamPlayers(teamid);
        ArrayList<Integer> assigned = new ArrayList<>();
        int previd= -1;
        for(String s : openPositions){
            int currRating = 0;
            int prevRating = 0;
            for(int pid: playerids){
                if(!assigned.contains(pid)){
                    Player p = PlayerDb.getPlayer(pid);
                    if(s.contains(p.getPosition())){
                        currRating = p.getOffensiveSkills() + p.getDefensiveSkills();
                        if(p.getPosition().equals( "G")){
                            currRating = p.getGoalieSkills();
                        }
                    }
                    if(currRating > prevRating){
                        updateTeamAssignment(teamid, pid, s);
                        prevRating = currRating;
                        previd = pid;
                    }
                }
            }
            assigned.add(previd);
        }

    }
    public static void updateTeamAssignment(int teamid, int pid, String pos){
        String sql = "UPDATE Team SET " + pos + " = ? WHERE teamid = ?";

        String url = "jdbc:sqlite:C:/sqlite/dbs/hockeyDb.db";
        try (Connection conn = DriverManager.getConnection(url)){
            PreparedStatement pstmt = conn.prepareStatement(sql);

            // set the corresponding param
            pstmt.setInt(1, pid);
            pstmt.setInt(2, teamid);
            // update
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
    public static ArrayList<Player> getline(int lineNum){
        return new ArrayList<Player>();
    }
}
