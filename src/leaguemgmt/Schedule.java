package leaguemgmt;

import com.sun.jdi.IntegerValue;

import java.lang.reflect.Array;
import java.util.*;
import leaguemgmt.Matchup;

public class Schedule {
    private int numTeams;
    private ArrayList<Matchup> gamesToPlay;
    private ArrayList<ArrayList<Matchup>> schedule;
    private ArrayList<ArrayList<Integer>> division;
    private static final int NUM_DIVISIONS = 4;
    private static final int NUM_WEEKLY_GAMES = 3;
    private static final int TOTAL_NUM_TEAM_GAMES = 40;
    public Schedule(int teams){
        numTeams = teams;
        division = new ArrayList<>();
        gamesToPlay = new ArrayList<>();
        schedule = new ArrayList<>();
        //createDivisons();
        createGamesToPlay();
        createSchedule();
    }
    public ArrayList<Integer> getDatesTeamPlays(int teamid){
        ArrayList<Integer> daysPlaying = new ArrayList<>();
        for(int i=0; i< schedule.size();++i){
            for(int k=0; k<schedule.get(i).size(); ++k) {
                Matchup m = schedule.get(i).get(k);
                if(m.getAwayTeamID() ==teamid || m.getHomeTeamID() == teamid){
                    daysPlaying.add(i);
                }
            }
        }
        return daysPlaying;
    }
    public ArrayList<ArrayList<Matchup>> getSchedule(){
        return schedule;
    }
    /*
        Creates a schedule with no two games in a day
     */
    public void createSchedule(){
        Random rand = new Random();
        while(gamesToPlay.size()!= 0){
            ArrayList<Matchup> daysGames = new ArrayList<>();
            int matchupsToday = (int)rand.nextGaussian() * 2 + 3;
            //todaysTeams tracks teams that have already played today
            ArrayList<Integer> todaysTeams = new ArrayList<>();
            for(int i=0; i< matchupsToday; ++i){
                int index = 0;
                if(gamesToPlay.size() > 0) {
                    //Check if team has already played today, if so skip
                    while(todaysTeams.contains(gamesToPlay.get(index).getHomeTeamID()) ||
                            (todaysTeams.contains(gamesToPlay.get(index).getAwayTeamID()))){
                        if (index < gamesToPlay.size()-1)
                            index++;
                    }
                    daysGames.add(gamesToPlay.get(index));
                    todaysTeams.add(gamesToPlay.get(index).getHomeTeamID());
                    todaysTeams.add(gamesToPlay.get(index).getAwayTeamID());
                    gamesToPlay.remove(index);
                }
            }
            schedule.add(daysGames);
        }
    }
    public ArrayList<Matchup> getGamesToPlay(){
        return gamesToPlay;
    }
    private ArrayList<Integer> getTeamsInDivision(int teamid){
        for(int i=0; i<division.size(); ++i){
            if(division.get(i).contains(Integer.valueOf(teamid))){
                return division.get(i);
            }
        }
        return null;
    }
    /*
        Creates all possible combination matchups
     */
    public void createGamesToPlay(){
        /*
            Creates the possible matches league-wide
         */
        LinkedList<Integer> roundRobin = new LinkedList<>();
        for(int i=1; i<= numTeams*2; ++i) {
            if(i > numTeams){
                roundRobin.add(i-numTeams);
            }
            else {
                roundRobin.add(i);
            }
        }
        do{
            for(int i=0; i<roundRobin.size()/2;++i){
                int home =roundRobin.get(i);
                int away = roundRobin.get(roundRobin.size()-1-i);
                Matchup m = new Matchup(home,away);
                if(home != away)
                    gamesToPlay.add(m);

            }
            roundRobin.add(0,roundRobin.get(roundRobin.size()-1));
            roundRobin.remove(roundRobin.size()-1);
        }while(roundRobin.get(0) != 1);
        /*
            Creates division-wide matchups
         */
    }
    /*
        Attempt to create 4 divisions. Allows for unequal divisions
     */
    public void createDivisons(){
        int leftOverTeams = numTeams%NUM_DIVISIONS;
        for(int i=0; i<NUM_DIVISIONS; ++i){
            ArrayList<Integer> teamsInDivision = new ArrayList<>();
            for(int k=0; k< (int)numTeams/NUM_DIVISIONS; ++k){
                teamsInDivision.add(k+1);
            }
            division.add(teamsInDivision);
        }
        //alllocate leftover teams. indexcount will never go over size of divisions
        int indexcount = 0;
        for(int i=0; i<leftOverTeams; ++i){
            division.get(indexcount).add((((int)numTeams/NUM_DIVISIONS) * NUM_DIVISIONS) + i+1);
            indexcount++;
        }
    }
}
