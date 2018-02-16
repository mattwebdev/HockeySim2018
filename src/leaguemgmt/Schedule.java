package leaguemgmt;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

public class Schedule {
    private static int SCHEDULE_DAYS = 7 * 14;
    /*
        Total num of games = 5 * 4 + 2 * 10
     */
    private static int NUM_INTERDIV_GAMES = 5;
    private static int NUM_LEAGUE_GAMES = 2;
    private static int SCHEDULE_UPPER_BOUND = 10;
    private int numTeams;
    ArrayList<ArrayList<ArrayList<Integer>>> totalSchedule;
    public Schedule(int teams){
        numTeams = teams;
        totalSchedule = new ArrayList<>();
        generateNewSchedule();
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
        Random rand = new Random();
        for(int i=0; i< SCHEDULE_DAYS; ++i){
            int gamesScheduled = rand.nextInt(SCHEDULE_UPPER_BOUND);
            ArrayList<ArrayList<Integer>> daySchedule = new ArrayList<>();
            for(int k=0; k<gamesScheduled; ++k) { //for each game in the day
                ArrayList<Integer> matchup = new ArrayList<>();
                int randomTeam = rand.nextInt(15) + 1;
                while (teamsToPlay(randomTeam).size() == 0) { //if team has no more games find new team
                    System.out.println("no more games" + randomTeam);
                    randomTeam = rand.nextInt(15) + 1;
                }
                ArrayList<Integer> randomTeamScheduledGames = teamsToPlay(randomTeam);
                //find random opponent from teamstoplay arraylist
                int opponentTeam = randomTeamScheduledGames.get(rand.nextInt(randomTeamScheduledGames.size()));
                matchup.add(randomTeam);
                matchup.add(opponentTeam);
                teamsToPlay(randomTeam).remove(Integer.valueOf(opponentTeam));
                teamsToPlay(opponentTeam).remove(Integer.valueOf(randomTeam));
                daySchedule.add(matchup);
            }
            totalSchedule.add(daySchedule);
        }
    }
}
