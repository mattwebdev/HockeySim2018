package database;

import player.Player;

import java.sql.*;
import java.util.ArrayList;
import java.util.Random;

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
    public static int getPlayerAtPosition(int teamid, String pos){
        String sql = "SELECT " + pos + " FROM Team WHERE teamid = ?";
        String url = "jdbc:sqlite:C:/sqlite/dbs/hockeyDb.db";
        try (Connection conn = DriverManager.getConnection(url)){
            PreparedStatement pstmt = conn.prepareStatement(sql);

            // set the corresponding param
            pstmt.setInt(1, teamid);
            // execute
            ResultSet rs = pstmt.executeQuery();
            int pid = rs.getInt(pos);
            return pid;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return 0;
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
    public static ArrayList<Player> getline(int teamid, int lineNum){
        ArrayList<Player> line = new ArrayList<>();
        String cPos = "C" + lineNum;
        String lwPos = "LW" + lineNum;
        String rwPos = "RW" + lineNum;
        String ldPos, rdPos;
        if(lineNum == 4){
            Random rand = new Random();
            int prob = rand.nextInt();
            if(prob < 33){
                ldPos = "LD1";
                rdPos = "RD1";
            }
            else if(prob < 66){
                ldPos = "LD2";
                rdPos = "RD2";
            }
            else {
                ldPos = "LD3";
                rdPos = "RD3";
            }
        }
        else{
            ldPos = "LD" + lineNum;
            rdPos = "RD" + lineNum;
        }
        int cPID = getPlayerAtPosition(teamid, cPos);
        int lwPID = getPlayerAtPosition(teamid, lwPos);
        int rwPID = getPlayerAtPosition(teamid, rwPos);
        int ldPID = getPlayerAtPosition(teamid, ldPos);
        int rdPID = getPlayerAtPosition(teamid, rdPos);
        Player lw = PlayerDb.getPlayer(lwPID);
        Player center = PlayerDb.getPlayer(cPID);
        Player rw = PlayerDb.getPlayer(rwPID);
        Player ld = PlayerDb.getPlayer(ldPID);
        Player rd = PlayerDb.getPlayer(rdPID);
        line.add(center);
        line.add(lw);
        line.add(rw);
        line.add(ld);
        line.add(rd);
        return line;
    }
    public static ArrayList<ArrayList<Player>> getLines(int teamid){
        ArrayList<ArrayList<Player>> lines = new ArrayList<ArrayList<Player>>();
        for(int i=1; i<=4; ++i){
            ArrayList<Player> pArr =getline(teamid, i);
            lines.add(pArr);
        }
        return lines;
    }
}
