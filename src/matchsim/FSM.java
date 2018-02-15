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
    private static int TOTAL_SHIFTS = 80;

    private State currentState;
    private ArrayList<Player> homeOnIce;
    private ArrayList<Player> awayOnIce;
    private Player homeGoalie;
    private Player awayGoalie;

    private ArrayList<ArrayList<Player>> homeRoster;
    private ArrayList<ArrayList<Player>> awayRoster;
    private Player[] lastTwoPasses;
    private double[] playerShiftDistribution = {.31,.27,.23,.19};
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
        /*
            PlayerShiftCount is the approx number of shifts needed to fulfil the
            set distribution of shifts
            *i.e. first line players have 31% of shifts, so out of 80
            shifts they must have 26*
         */
        for(int i=0; i < playerShiftCount.length; ++i){
            playerShiftCount[i] = (int)Math.round((TOTAL_SHIFTS * playerShiftDistribution[i]));
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
     */
    public int nextPossessorLine(){
        ArrayList<Integer> available = new ArrayList<Integer>();
        available.add(1); available.add(2); available.add(3); available.add(4);
        Random rand = new Random();
        int randLine =  rand.nextInt(available.size())+1;
        if(playerShiftCount[randLine-1] == 0)
            available.remove(Integer.valueOf(randLine));
        while(randLine == hLine &&  playerShiftCount[randLine-1] ==0){
            //System.out.println("looking");
            if(available.size()==1){
                randLine=rand.nextInt(4)+1;
            }
            else
                randLine = rand.nextInt(available.size())+1;
        }
        playerShiftCount[randLine-1] = playerShiftCount[randLine-1] - 1;
        return randLine;
    }
    public int nextEnemyLine(){
        ArrayList<Integer> available = new ArrayList<Integer>();
        available.add(1); available.add(2); available.add(3); available.add(4);
        Random rand = new Random();
        int randLine =  rand.nextInt(available.size())+1;
        if(playerShiftCount[randLine-1] == 0)
            available.remove(Integer.valueOf(randLine));
        while(randLine == aLine && playerShiftCount[randLine-1] ==0){
            //System.out.println("looking" + randLine);
            if(available.size()==1){
                randLine=rand.nextInt(4)+1;
            }
            else
                randLine = rand.nextInt(available.size())+1;
        }
        playerShiftCount[randLine-1] = playerShiftCount[randLine-1] - 1;
        return randLine;
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
        System.out.println(Arrays.toString(playerShiftCount));
    }

}
