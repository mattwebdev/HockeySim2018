package matchsim;

import player.Player;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.util.ArrayList;

public class FSM {
    private State currentState;
    private ArrayList<Player> homeOnIce;
    private ArrayList<Player> awayOnIce;
    private Player homeGoalie;
    private Player awayGoalie;

    private int[] playerShiftDistributionF = {31,27,23,19};
    private int[] playerShiftDistributionD = {36,33,30};
    private int onPowerPlay;
    private int count= 0;
    private GameStats gameLog;


    public FSM(ArrayList<Player> home, ArrayList<Player> away, Player homeGoalie, Player awayGoalie){
        homeOnIce = home;
        awayOnIce = away;
        this.awayGoalie = awayGoalie;
        this.homeGoalie = homeGoalie;
        gameLog = new GameStats(home.get(0).getTeamID(), away.get(0).getTeamID());
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
    public ArrayList<Player> nextPossessorTeam(){
        return homeOnIce;
    }
    public ArrayList<Player> nextEnemyTeam(){
        return awayOnIce;
    }
    public void update(){
        if(count % 7 ==0 )
            changeLines(nextPossessorTeam(), nextEnemyTeam());
        currentState = currentState.next();
        count++;
    }

}
