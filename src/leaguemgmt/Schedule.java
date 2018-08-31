package leaguemgmt;

import com.sun.jdi.IntegerValue;

import java.lang.reflect.Array;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.*;

import database.ScheduleDb;
import leaguemgmt.Matchup;

public class Schedule {
    private int year;
    private int numTeams;
    private ArrayList<Matchup> gamesToPlay;
    private ArrayList<ArrayList<Matchup>> schedule;
    private ArrayList<ArrayList<Integer>> division;
    private static int CONFERENCE_NUM = 2;
    private static int DIVISION_NUM = 4;


    public Schedule(int teams, int year){
        this.year = year;
        numTeams = teams;
        division = new ArrayList<>();
        gamesToPlay = new ArrayList<>();
        schedule = new ArrayList<>();
        //createDivisons();
        System.out.println("creating games to play");
        createGamesToPlay();
        System.out.println("created games to play");
        createSchedule();
        System.out.println("created schedule");
        ScheduleDb.addSchedToDb(schedule, year);
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
            int matchupsToday = (int)rand.nextGaussian() * 2 + 8;
            //todaysTeams tracks teams that have already played today
            ArrayList<Integer> todaysTeams = new ArrayList<>();
            for(int i=0; i< matchupsToday; ++i){
                int index = 0;
                if(gamesToPlay.size() > 0) {
                    //Check if team has already played today, if so skip
                    while(todaysTeams.contains(gamesToPlay.get(index).getHomeTeamID()) ||
                            (todaysTeams.contains(gamesToPlay.get(index).getAwayTeamID()))){
                        System.out.println("wut" + index + " " + gamesToPlay.size());
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
    /*
        Creates all possible combination matchups
     */
    public void createGamesToPlay(){
        /*
            Creates the possible matches league-wide
         */
        int numDivision = numTeams/DIVISION_NUM;
        int numConference = numTeams/CONFERENCE_NUM;

        LinkedList<Integer> roundRobin = new LinkedList<>();
        LinkedList<Integer> conferenceRoundRobin = new LinkedList<>();
        LinkedList<Integer> divisionalRoundRobin = new LinkedList<>();


        for(int i=1; i<= numTeams; ++i){
            roundRobin.add(i);
        }
        getMatchupsFromRoundRobin(roundRobin, 2);
        System.out.println("1");
        int numTeamConference = 1;
        for(int i=1; i<= CONFERENCE_NUM; ++i){
            for(int j=1; j<= numConference; ++j) {
                conferenceRoundRobin.add(numTeamConference);
                numTeamConference++;
            }
            getMatchupsFromRoundRobin(conferenceRoundRobin,3);
            conferenceRoundRobin.clear();
        }
        System.out.println("12");
        int numTeamDivision = 1;
        for(int i=1; i<= DIVISION_NUM; ++i){
            for(int j=1; j<= numDivision; ++j) {
                divisionalRoundRobin.add(numTeamDivision);
                numTeamDivision++;
            }
            getMatchupsFromRoundRobin(divisionalRoundRobin, 4);
            divisionalRoundRobin.clear();
        }

        System.out.println("3");
        /*
            Creates division-wide matchups
         */
    }
    private void getMatchupsFromRoundRobin(LinkedList<Integer> roundRobin, int numRematches){
        int beginning = roundRobin.get(0);
        boolean isHome = true;
        for (int j = 0; j < numRematches; ++j){
            do {
                for (int i = 0; i < roundRobin.size() / 2; ++i) {
                    int home = roundRobin.get(i);
                    int away = roundRobin.get(roundRobin.size() - 1 - i);
                    Matchup m;
                    if(isHome) {
                        m = new Matchup(home, away);
                    }
                    else{
                        m = new Matchup(away, home);
                    }
                    if (home != away)
                        gamesToPlay.add(m);

                }
                roundRobin.add(0, roundRobin.get(roundRobin.size() - 1));
                roundRobin.remove(roundRobin.size() - 1);
            } while (roundRobin.get(0) != beginning);
            isHome = !isHome;
        }
    }
    /*
        Attempt to create 4 divisions. Allows for unequal divisions
     */

}
