package leaguemgmt;

import database.SQLiteJDBCDriverConnection;
import database.ScheduleDb;
import database.TeamDb;
import development.Development;
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


        //for(int i=0; i<82; ++i)
            //Development.developPlayer(345);
        /*for(int i=0; i<1; ++i){
            for(Matchup m: sched.get(i)){
                Game g = new Game(m.getHomeTeamID(), m.getAwayTeamID());
                g.sim();
            }
        }*/
        Schedule sch = new Schedule(8, 2018);
        ArrayList<ArrayList<Matchup>> s = sch.getSchedule();
        for(ArrayList<Matchup> game: s){
            for(Matchup m: game){
                System.out.println(m.getHomeTeamID() + " vs " + m.getAwayTeamID());
                System.out.println("\n");
            }
        }

    }
}

