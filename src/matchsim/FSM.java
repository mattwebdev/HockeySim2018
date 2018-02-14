package matchsim;

import database.TeamDb;
import player.Player;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Random;

public class FSM {
    private static int TOTAL_SHIFTS = 80;

    private State currentState;
    private ArrayList<Player> homeOnIce;
    private ArrayList<Player> awayOnIce;
    private Player homeGoalie;
    private Player awayGoalie;

    private ArrayList<ArrayList<Player>> homeRoster;
    private ArrayList<ArrayList<Player>> awayRoster;
    private Player[] lastTwoPasses;
    private int[] playerShiftDistribution = {31,27,23,19};
    private int[] playerShiftCount;
    private int onPowerPlay;
    private int count= 0;
    private int aLinePrev;
    private int hLinePrev;
    private int aLine;
    private int hLine;
    private GameStats gameLog;


    public FSM(ArrayList<Player> home, ArrayList<Player> away, Player homeGoalie, Player awayGoalie, int hLine, int aLine){
        homeOnIce = home;
        awayOnIce = away;
        homeRoster = TeamDb.getLines(home.get(0).getTeamID());
        awayRoster = TeamDb.getLines(away.get(0).getTeamID());
        this.awayGoalie = awayGoalie;
        this.homeGoalie = homeGoalie;
        this.aLine = aLine;
        this.hLine = hLine;
        aLinePrev = 0;
        hLinePrev = 0;
        gameLog = new GameStats(home.get(0).getTeamID(), away.get(0).getTeamID());
        playerShiftCount = new int[4];
        lastTwoPasses = new Player[2];
        for(int i=0; i < playerShiftCount.length; ++i){
            playerShiftCount[i] = TOTAL_SHIFTS * playerShiftDistribution[i];
        }
    }
    /*
    Tracks Assists stats. lastTwoPasses[0] is primary assist, lastTwoPasses[1] is secondary
     */
    public void clearLastTwoPasses(){
        lastTwoPasses[0] = null;
        lastTwoPasses[1] = null;
    }
    public Player[] getLastTwoPasses(){
        return lastTwoPasses;
    }
    public void addLastTwoPasses(Player p){
        if(lastTwoPasses[0] == null)
            lastTwoPasses[0] = p;
        else if(lastTwoPasses[0] != null && lastTwoPasses[1] == null) {
            lastTwoPasses[1] = lastTwoPasses[0];
            lastTwoPasses[0] = p;
        }
        else if(lastTwoPasses[0] != null && lastTwoPasses[1] != null) {
            lastTwoPasses[1] = lastTwoPasses[0];
            lastTwoPasses[0] = p;
        }
    }
    public ArrayList<Player> getAwayOnIce() {
        return awayOnIce;
    }

    public ArrayList<Player> getHomeOnIce() {
        return homeOnIce;
    }

    public GameStats getGameStats(){
        return gameLog;
    }
    public void exportGameLogtoFile(){
        File newTextFile = new File("log.txt");
        try{
            FileWriter fw = new FileWriter(newTextFile);
            fw.write(gameLog.getGamelog());
            fw.close();
        }
        catch(IOException iox){
            iox.printStackTrace();
        }

    }
    public void writeGameLog(String s){
        gameLog.writeGameLog(count,s);
    }
    public Player getGoalie(int teamID){
        if(teamID == awayGoalie.getTeamID())
            return awayGoalie;
        else
            return homeGoalie;
    }
    public void setPenalty(int teamval){ onPowerPlay = teamval;
    }
    public void setState(State s){
        currentState = s ;
    }
    public void changeLines(ArrayList<Player> possessorTeam, ArrayList<Player> enemyTeam){
        currentState.setLines(possessorTeam, enemyTeam);
    }
    public int nextPossessorLine(){
        Random rand = new Random();
        int randLine = rand.nextInt(4) + 1;
        while(randLine == hLinePrev |randLine == hLine || playerShiftCount[randLine-1] == 0){
            randLine = rand.nextInt(4)+1;
        }
        return randLine;
    }
    public int nextEnemyLine(){
        Random rand = new Random();
        int randLine = rand.nextInt(4) + 1;
        while(randLine == aLinePrev || randLine == aLine|| playerShiftCount[randLine-1] == 0){
            randLine = rand.nextInt(4)+1;
        }
        return randLine;
    }
    public void update(){
        if(count % 9 ==0 && count != 0){
            int tempALine = aLine;
            int tempHLine = hLine;
            hLine = nextPossessorLine();
            aLine =nextEnemyLine();
            hLinePrev = tempHLine;
            aLinePrev = tempALine;
            changeLines(homeRoster.get(hLine-1), awayRoster.get(aLine-1));
        }
        currentState = currentState.next();
        if(count / 240 == 1 && count % 240 == 0){
            gameLog.writeGameLog(count, "1ST PERIOD ENDS\n");
            this.setState(new Faceoff(homeOnIce, awayOnIce, this));
        }
        if(count / 240 == 2 && count % 240 == 0){
            gameLog.writeGameLog(count, "2ND PERIOD ENDS\n");
            this.setState(new Faceoff(homeOnIce, awayOnIce, this));
        }
        if(count / 240 == 3 && count % 240 == 0){
            gameLog.writeGameLog(count, "END OF GAME\n");
        }
        count++;
    }

}
