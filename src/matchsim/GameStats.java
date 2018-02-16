package matchsim;

import database.PlayerStatsDb;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class GameStats {
    private int T1ID;
    private int T2ID;
    private int numGoalsT1 = 0;
    private int numGoalsT2 = 0;
    private int numPenaltiesT1 = 0;
    private int numPenaltiesT2 = 0;
    private int numShotsT1 = 0;
    private int numShotsT2 = 0;

    private ArrayList<Integer> goalScorersPIDS;
    private ArrayList<Integer> primaryAssistersPIDS;
    private ArrayList<Integer> secondaryAssistersPIDS;
    private HashMap<Integer, Integer> numShots; //index = pid

    private String gamelog;

    GameStats(int homeID, int awayID){
        goalScorersPIDS = new ArrayList<>();
        primaryAssistersPIDS = new ArrayList<>();
        secondaryAssistersPIDS = new ArrayList<>();
        numShots = new HashMap<>();
        T1ID = homeID;
        T2ID = awayID;
        gamelog = "";
    }
    public void addGoalScorer(int pid){
        goalScorersPIDS.add(pid);
    }
    public void addPrimaryAssister(int pid){
        primaryAssistersPIDS.add(pid);
    }
    public void addSecondaryAssister(int pid){
        secondaryAssistersPIDS.add(pid);
    }
    public void addShot(int pid){
        if(numShots.get(pid) == null)
            numShots.put(pid, 1);
        else
            numShots.put(pid, numShots.get(pid)+1);
    }
    public void updateDatabaseWithPlayerStats(){
        for(int pid: goalScorersPIDS)
            PlayerStatsDb.updateGoals(pid, 1);
        for(int pid: primaryAssistersPIDS)
            PlayerStatsDb.updateAssists(pid, 1);
        for(int pid: secondaryAssistersPIDS)
            PlayerStatsDb.updateAssists(pid,1);
        for(Map.Entry<Integer,Integer> entry: numShots.entrySet()){
            int pid = entry.getKey();
            int shots = entry.getValue();
            PlayerStatsDb.updateShots(pid, shots);
        }
    }
    public int getHomeID(){
        return T1ID;
    }
    public int getAwayID(){
        return T2ID;
    }
    private String formatSecondsIntoTime(int secCount){
        int totalSecs = secCount * 5;
        int minuteVal = totalSecs / 60;
        int secondVal = totalSecs % 60;
        return "" + minuteVal + ":" + String.format("%02d",secondVal);
    }
    public void writeGameLog(int secCount, String s){
        gamelog = gamelog + formatSecondsIntoTime(secCount) + " --- " + s + "\n";
    }
    public String getGamelog(){
        return gamelog;
    }
    public void incrementGoals(int teamid){
        if(teamid == T1ID)
            numGoalsT1++;
        else
            numGoalsT2++;
    }
    public void incrementShots(int teamid){
        if(teamid == T1ID)
            numShotsT1++;
        else
            numShotsT2++;
    }
    public void incrementPenalties(int teamid){
        if(teamid == T1ID)
            numShotsT1+=2;
        else
            numShotsT2+=2;
    }
    public int getNumPenaltiesT1(){
        return numPenaltiesT1;
    }
    public int getNumPenaltiesT2(){
        return numPenaltiesT2;
    }
    public int getHomeShotsT1(){
        return numShotsT1;
    }
    public int getAwayShotsT2(){
        return numShotsT2;
    }
    public int getHomeGoals(){
        return numGoalsT1;
    }
    public int getAwayGoals(){
        return numGoalsT2;
    }
}
