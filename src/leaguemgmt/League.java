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
    public static void main(String[] args){
        SQLiteJDBCDriverConnection.createNewDatabase();
        Random rand = new Random();
        ArrayList<ArrayList<Integer>> schedule;
        Game g1 = new Game(rand.nextInt(15)+1, rand.nextInt(15)+1);
        Schedule s = new Schedule(15);
        ArrayList<ArrayList<Integer>> day;
        int gamecount = 0;
        int divgamecount = 0;

        //System.out.println(s.teamsToPlay(4).size());
        for(int i=0; i<s.getTotalDays(); ++i) {
            day = s.getMatchupsOnDay(i);
            for (ArrayList<Integer> matchup : day) {
                //System.out.println("-----------");
                if(matchup.get(0) == 2 || matchup.get(1) ==2){
                    gamecount++;
                }
                if((matchup.get(0) == 2 && matchup.get(1) == 3) || (matchup.get(1) == 2 && matchup.get(0) ==3)){
                    divgamecount++;
                }
                //System.out.println(matchup.get(0));
                //System.out.println(matchup.get(1));
            }
            //System.out.println("*************");
        }
        System.out.println("Team 4 played " + gamecount + "games");
        System.out.println("Team 4 played " + divgamecount + "games against Team 3");
        System.out.println("simming");
        g1.sim();
    }
}
