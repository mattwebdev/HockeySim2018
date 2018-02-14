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
        this.awayGoalie = awayGoalie;
        this.homeGoalie = homeGoalie;
        this.aLine = aLine;
        this.hLine = hLine;
        aLinePrev = 0;
        hLinePrev = 0;
        gameLog = new GameStats(home.get(0).getTeamID(), away.get(0).getTeamID());
        playerShiftCount = new int[4];
        for(int i=0; i < playerShiftCount.length; ++i){
            playerShiftCount[i] = TOTAL_SHIFTS * playerShiftDistribution[i];
        }
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
        while(randLine == aLinePrev || randLine == aLine || playerShiftCount[randLine-1] == 0){
            randLine = rand.nextInt(4)+1;
        }
        return randLine;
    }
    public void update(){
        if(count % 9 ==0 ){
            int tempALine = aLine;
            int tempHLine = hLine;
            hLine = nextPossessorLine();
            aLine = nextEnemyLine();
            hLinePrev = tempHLine;
            aLinePrev = tempALine;
            changeLines(TeamDb.getline(homeOnIce.get(0).getTeamID(), hLine),
                    TeamDb.getline(awayOnIce.get(0).getTeamID(), aLine));
        }
        currentState = currentState.next();
        count++;
    }

}
