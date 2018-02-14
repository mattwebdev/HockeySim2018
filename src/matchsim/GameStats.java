package matchsim;

public class GameStats {
    private int T1ID;
    private int T2ID;
    private int numGoalsT1 = 0;
    private int numGoalsT2 = 0;
    private int numPenaltiesT1 = 0;
    private int numPenaltiesT2 = 0;
    private int numShotsT1 = 0;
    private int numShotsT2 = 0;

    private String gamelog;

    GameStats(int homeID, int awayID){
        T1ID = homeID;
        T2ID = awayID;
        gamelog = "";
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
