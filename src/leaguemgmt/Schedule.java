package leaguemgmt;

import java.lang.reflect.Array;
import java.util.*;

public class Schedule {
    private static int SCHEDULE_DAYS = 100;
    /*
        Total num of games = 5 * 4 + 2 * 10
     */
    private static int NUM_INTERDIV_GAMES = 5;
    private static int NUM_LEAGUE_GAMES = 2;
    private static int SCHEDULE_UPPER_BOUND = 4;
    private int numTeams;
    ArrayList<ArrayList<Integer>> teamsToPlay;
    ArrayList<ArrayList<ArrayList<Integer>>> totalSchedule;
    public Schedule(int teams){
        numTeams = teams;
        totalSchedule = new ArrayList<>();
        teamsToPlay = new ArrayList<ArrayList<Integer>>();
        for(int i=0; i<teams; ++i)
            teamsToPlay.add(new ArrayList<Integer>());
        for(int i=0; i<teams; ++i)
            teamsToPlay.set(i, teamsToPlay(i+1));
        generateNewSchedule();
    }
    public ArrayList<Integer> getMatchupsLeft(int teamid){
        return teamsToPlay.get(teamid);
    }
    public int getTotalDays(){
        return totalSchedule.size();
    }
    public ArrayList<ArrayList<Integer>> getMatchupsOnDay(int day){
        return totalSchedule.get(day);
    }
    public ArrayList<Integer> teamsToPlay(int teamid){
        ArrayList<Integer> teamsToPlay = new ArrayList<>();
        int division = (int) (teamid-1) / 5;
        Integer[] div1 = {1,2,3,4,5};
        Integer[] div2 = {6,7,8,9,10};
        Integer[] div3 = {11,12,13,14,15};
        for(int i=0; i<5;++i){
            switch(division){
                case 0:
                    Collections.addAll(teamsToPlay, div1);
                    teamsToPlay.remove(Integer.valueOf(teamid));
                    break;
                case 1:
                    Collections.addAll(teamsToPlay, div2);
                    teamsToPlay.remove(Integer.valueOf(teamid));
                    break;
                case 2:
                    Collections.addAll(teamsToPlay, div3);
                    teamsToPlay.remove(Integer.valueOf(teamid));
                    break;
            }
        }
        for(int i=0; i<2; ++i){
            switch(division){
                case 0:
                    Collections.addAll(teamsToPlay, div2);
                    Collections.addAll(teamsToPlay, div3);
                    break;
                case 1:
                    Collections.addAll(teamsToPlay, div1);
                    Collections.addAll(teamsToPlay, div3);
                    break;
                case 2:
                    Collections.addAll(teamsToPlay, div1);
                    Collections.addAll(teamsToPlay, div2);
                    break;
            }
        }
        return teamsToPlay;
    }
    private void generateNewSchedule(){
        /*
            Constraints:
                No three days in a row
         */
        int kcount =0;
        Random rand = new Random();
        Set completedTeams = new HashSet<Integer>();
        for(int i=0; i< SCHEDULE_DAYS; ++i){
            int gamesScheduled = (int)(rand.nextGaussian() * .5 + SCHEDULE_UPPER_BOUND); //(SCHEDULE_UPPER_BOUND)+1;
            ArrayList<ArrayList<Integer>> daySchedule = new ArrayList<>();
            for(int k=0; k<gamesScheduled; ++k) { //for each game in the day
                ArrayList<Integer> matchup = new ArrayList<>();
                int randomTeam = rand.nextInt(15) + 1;
                while (teamsToPlay.get(randomTeam-1).size() == 0) { //if team has no more games find new team
                    completedTeams.add(randomTeam);
                    if(completedTeams.size() == numTeams){
                        return;
                    }
                    randomTeam = rand.nextInt(15) + 1;
                }
                ArrayList<Integer> randomTeamScheduledGames = teamsToPlay.get(randomTeam-1);
                //find random opponent from teamstoplay arraylist
                int opponentTeam = randomTeamScheduledGames.get(rand.nextInt(randomTeamScheduledGames.size()));
                matchup.add(randomTeam);
                matchup.add(opponentTeam);
                teamsToPlay.get(randomTeam-1).remove(Integer.valueOf(opponentTeam));
                teamsToPlay.get(opponentTeam-1).remove(Integer.valueOf(randomTeam));
                daySchedule.add(matchup);
            }
            totalSchedule.add(daySchedule);
        }
        System.out.println("Schedule size" + totalSchedule.size());
    }
}
