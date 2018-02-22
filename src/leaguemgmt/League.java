package leaguemgmt;

import database.SQLiteJDBCDriverConnection;
import database.TeamDb;
import matchsim.Game;
import startup.TeamGen;
import team.Team;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

public class League {
    public static void main(String[] args) {
        SQLiteJDBCDriverConnection.createNewDatabase();
        Random rand = new Random();
        ArrayList<ArrayList<Integer>> schedule;

        ArrayList<ArrayList<Integer>> day;
        int gamecount = 0;
        int divgamecount = 0;

        //System.out.println(s.teamsToPlay(4).size());
        Schedule s = new Schedule(15);
        ArrayList<ArrayList<Matchup>> sched = s.getSchedule();
        for(int i=0; i<sched.size(); ++i){
            for(Matchup m: sched.get(i)){
                Game g = new Game(m.getHomeTeamID(), m.getAwayTeamID());
                g.sim();
            }
        }
    }
}

