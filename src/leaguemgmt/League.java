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

        Schedule s = new Schedule(15);
        ArrayList<ArrayList<Integer>> day;
        int gamecount = 0;
        int divgamecount = 0;

        //System.out.println(s.teamsToPlay(4).size());
        Game g1 = new Game(1, 15);
        g1.sim();
        //System.out.println("simming");

    }
}
