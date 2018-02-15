package matchsim;

import database.TeamDb;
import player.Player;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

public class FSM {
    private State currentState;
    private ArrayList<Player> homeOnIce;
    private ArrayList<Player> awayOnIce;
    private Player homeGoalie;
    private Player awayGoalie;
    int[] h_lineshifts;
    int[] a_lineshifts;
    private ArrayList<ArrayList<Player>> homeRoster;
    private ArrayList<ArrayList<Player>> awayRoster;
    private Player[] lastTwoPasses;
    private double[] playerShiftDistribution = {.31,.27,.23,.19};
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
        h_lineshifts = new int[4];
        a_lineshifts = new int[4];
        gameLog = new GameStats(home.get(0).getTeamID(), away.get(0).getTeamID());
        lastTwoPasses = new Player[2];
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
    /*
        Gets Away Team on Ice
     */
    public ArrayList<Player> getAwayOnIce() {
        return awayOnIce;
    }
    /*
        Gets Home Team on Ice
     */
    public ArrayList<Player> getHomeOnIce() {
        return homeOnIce;
    }
    /*
        Exports gameStats
     */
    public GameStats getGameStats(){
        return gameLog;
    }
    /*
        Exports a particular game log to a log.txt
     */
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
    /*
        A call to this function writes a certain event to the log with a timestamp
     */
    public void writeGameLog(String s){
        gameLog.writeGameLog(count,s);
    }
    /*
        Gets the goalie for a specific teamID
     */
    public Player getGoalie(int teamID){
        if(teamID == awayGoalie.getTeamID())
            return awayGoalie;
        else
            return homeGoalie;
    }
    /*
        *Incomplete*: Sets penalty state that alterates FSM probabilities
     */
    public void setPenalty(int teamval){ onPowerPlay = teamval;
    }
    /*
        Used to set a state (used exclusively to set Faceoffs atm)
     */
    public void setState(State s){
        currentState = s ;
    }
    /*
        Changes the lines in a specific state
     */
    public void changeLines(ArrayList<Player> possessorTeam, ArrayList<Player> enemyTeam){
        currentState.setLines(possessorTeam, enemyTeam);
    }
    /*
        Calculates what the next line should be based on the 2 last lines put out
        {.31,.27,.23,.19};
     */
    public int nextPossessorLine(){
        int chosenLine = -1;
        Random rand = new Random();
        do{
            int randLine = rand.nextInt(100);
            if (randLine < 35) {
                chosenLine = 1;
            } else if (randLine < 35 + 27) {
                chosenLine = 2;
            } else if (randLine < 35 + 27 + 20) {
                chosenLine = 3;
            } else {
                chosenLine = 4;
            }
        }while(chosenLine == hLine);
        h_lineshifts[chosenLine-1]++;
        return chosenLine;
    }
    public int nextEnemyLine(){
        int chosenLine = -1;
        Random rand = new Random();
        do {
            int randLine = rand.nextInt(100);
            if (randLine < 35) {
                chosenLine = 1;
            } else if (randLine < 35 + 25) {
                chosenLine = 2;
            } else if (randLine < 35 + 25 + 20) {
                chosenLine = 3;
            } else {
                chosenLine = 4;
            }
        }while(chosenLine == aLine);
        a_lineshifts[chosenLine-1]++;
        return chosenLine;
    }
    /*
        Updates each individual state and checks for major events (line changes and end of periods)
     */
    public void update(){
        //checks for line change interval (every 45 secs is a linechange)
        if(count % 9 ==0 && count != 0){
            int tempALine = aLine;
            int tempHLine = hLine;
            hLine = nextPossessorLine();
            aLine = nextEnemyLine();
            hLinePrev = tempHLine;
            aLinePrev = tempALine;
            changeLines(homeRoster.get(hLine-1), awayRoster.get(aLine-1));
        }
        currentState = currentState.next();
        /*
            Next three if statements control log output and states
            when the seconds counter reaches the end of a period
         */
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
        count++; //increment time after each update
    }

}
