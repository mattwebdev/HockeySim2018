package matchsim;

import database.PlayerStatsDb;
import player.Player;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Random;

public class Possession implements State {
    /*
    Constants controlling the success percentage of each event
     */
    private static final int BASE_RETAIN_SUCCESS = 60;
    private static final int BASE_RETAIN_VARIATION = 40;

    private static final int BASE_PASS_SUCCESS = 70;
    private static final int BASE_PASS_VARIATION = 30;

    private static final int BASE_SHOOT_SUCCESS = 15;
    private static final int BASE_SHOOT_VARIATION = 10;

    private static final int BASE_CLEAR_SUCCESS = 40;
    private static final int BASE_CLEAR_VARIATION = 40;

    private static final int BASE_PENALTY_CHANCE = 2;

    /*
    possessorTeam and enemyTeam is the current line on each for each team
     */
    private ArrayList<Player> possessorTeam;
    private ArrayList<Player> enemyTeam;
    private Random rand;
    private Player possessor;
    private int xpos;
    private int retainChance;
    private int passChance;
    private int clearChance;
    private int shootChance;
    private int penaltyChance = 2;
    private FSM gamesim;

    public Possession(FSM gamesim, Player possessor, int xpos, ArrayList<Player> possessorTeam, ArrayList<Player> enemyTeam){
        this.possessorTeam = possessorTeam;
        this.enemyTeam = enemyTeam;
        this.possessor = possessor;
        this.gamesim = gamesim;
        this.xpos = xpos;
        rand = new Random();
    }
    /*
    These two functions are for facilitating line changes from the FSM class
     */
    public int getPossessorTeam(){
        return possessor.getTeamID();
    }
    public void setLines(ArrayList<Player> change1, ArrayList<Player> change2){
        if(possessorTeam.get(0).getTeamID() == change1.get(0).getTeamID()) {
            this.possessorTeam = change1;
            this.possessor = possessorTeam.get(rand.nextInt(possessorTeam.size()));
            this.enemyTeam = change2;
        }
        else{
            this.possessorTeam = change2;
            this.possessor = possessorTeam.get(rand.nextInt(possessorTeam.size()));
            this.enemyTeam = change1;
        }
    }
    /*
    Sets the success value for each event
     */
    private boolean isRetainSuccess(){
        int randnum = rand.nextInt(100);
        int avgOffensiveSkillsPossessor = 0;
        int avgDefensiveSkillsEnemy = 0;
        for(Player p : possessorTeam){
            avgOffensiveSkillsPossessor += p.getOffensiveSkills();
        }
        for(Player p : enemyTeam){
            avgDefensiveSkillsEnemy += p.getDefensiveSkills();
        }
        avgOffensiveSkillsPossessor/=5;
        avgDefensiveSkillsEnemy/= 5;
        double retainSuccess = BASE_RETAIN_SUCCESS + (((double)avgOffensiveSkillsPossessor-avgDefensiveSkillsEnemy)/100)*BASE_RETAIN_VARIATION;
        if(randnum < retainSuccess)
            return true;
        else
            return false;
    }
    private boolean isPassSuccess(){
        int randnum = rand.nextInt(100);
        int avgOffensiveSkillsPossessor = 0;
        int avgDefensiveSkillsEnemy = 0;
        for(Player p : possessorTeam){
            avgOffensiveSkillsPossessor += p.getOffensiveSkills();
        }
        for(Player p : enemyTeam){
            avgDefensiveSkillsEnemy += p.getDefensiveSkills();
        }
        avgOffensiveSkillsPossessor/=5;
        avgDefensiveSkillsEnemy/= 5;
        double passSuccess = BASE_PASS_SUCCESS + (((double)avgOffensiveSkillsPossessor-avgDefensiveSkillsEnemy) / 100)*BASE_PASS_VARIATION;
        if(randnum < passSuccess)
            return true;
        else
            return false;
    }
    private boolean isShotSuccess(){
        int randnum = rand.nextInt(100);
        int avgOffensiveSkillsPossessor = 0;
        int avgDefensiveSkillsEnemy = 0;
        for(Player p : possessorTeam){
            avgOffensiveSkillsPossessor += p.getOffensiveSkills();
        }
        for(Player p : enemyTeam){
            avgDefensiveSkillsEnemy += p.getDefensiveSkills();
        }
        avgOffensiveSkillsPossessor/=5;
        avgDefensiveSkillsEnemy/= 5;
        int goalieSkillsEnemy = gamesim.getGoalie(enemyTeam.get(0).getTeamID()).getGoalieSkills();
        double shootSuccess = BASE_SHOOT_SUCCESS +
                ((double)(avgOffensiveSkillsPossessor-(avgDefensiveSkillsEnemy+goalieSkillsEnemy)/2)/100)*BASE_SHOOT_VARIATION;
        //System.out.println(randnum + " " + shootSuccess + " " + possessor.getTeamID());
        if(randnum < shootSuccess) {
            return true;
        }
        else
            return false;
    }
    private boolean isClearSuccess(){
        int randnum = rand.nextInt(100);
        int avgOffensiveSkillsPossessor = 0;
        int avgDefensiveSkillsEnemy = 0;
        for(Player p : possessorTeam){
            avgOffensiveSkillsPossessor += p.getOffensiveSkills();
        }
        for(Player p : enemyTeam){
            avgDefensiveSkillsEnemy += p.getDefensiveSkills();
        }
        avgOffensiveSkillsPossessor/=5;
        avgDefensiveSkillsEnemy/= 5;
        double clearSuccess = BASE_CLEAR_SUCCESS + (((double)avgOffensiveSkillsPossessor-avgDefensiveSkillsEnemy)/50)/BASE_CLEAR_VARIATION;
        if(randnum < clearSuccess)
            return true;
        else
            return false;
    }
    /*
    Sets probability of each event transition
     */
    private void setChances(){
        switch(xpos){
            case -2:
                retainChance = 28;
                passChance = 20;
                clearChance = 50;
                shootChance = 0;
                penaltyChance = 2;
                break;
            case -1:
                retainChance = 35;
                passChance = 35;
                clearChance = 25;
                shootChance = 3;
                penaltyChance = 2;
                break;
            case 0:
                retainChance = 35;
                passChance = 39;
                clearChance = 15;
                shootChance = 10;
                penaltyChance = 1;
                break;
            case 1:
                retainChance = 30;
                passChance = 40;
                clearChance = 15;
                shootChance = 14;
                penaltyChance = 1;
                break;
            case 2:
                retainChance = 43;
                passChance = 50;
                clearChance = 0;
                shootChance = 16;
                penaltyChance = 1;
                break;
        }
    }
    /*
    Determines the next event based off of probability/chance value
     */
    public State next(){
        /*
        potential states:
            1) retain puck
            2) pass puck
            3) clear chance
            4) take penalty
            5) shoot puck
         */
        int probability = rand.nextInt(100);
        setChances();
        //if possessor retains the puck
        if(probability < retainChance){
            boolean success = isRetainSuccess();
            if(success) {
                String out = possessor.getName() + " retains puck";
                gamesim.writeGameLog(out);
                //System.out.print(possessor.getName() + " retains puck");
                if (xpos < 2) {
                    if(rand.nextInt(100) < 50)
                        return new Possession(gamesim, possessor, xpos + 1, possessorTeam, enemyTeam);
                    else
                        return new Possession(gamesim, possessor, xpos, possessorTeam, enemyTeam);
                }
                else
                    return new Possession(gamesim,possessor, xpos, possessorTeam, enemyTeam);
            }
            else {
                String out = possessor.getName() + " does not retain puck.";
                gamesim.writeGameLog(out);
                gamesim.clearLastTwoPasses();
                return new Possession(gamesim,enemyTeam.get(rand.nextInt(enemyTeam.size())), xpos, enemyTeam, possessorTeam);
            }
        }
        //if possessor passes the puck
        else if(probability >= retainChance && probability < retainChance+passChance){
            boolean success = isPassSuccess();
            if(success){
                //System.out.print(possessor.getName() + " passes puck");
                String out = possessor.getName() + " passes puck";
                gamesim.writeGameLog(out);
                int newPos = rand.nextInt(5)-2;
                gamesim.addLastTwoPasses(possessor);
                return new Possession(gamesim,possessorTeam.get(rand.nextInt(enemyTeam.size())), newPos, possessorTeam, enemyTeam);
            }
            else{
                String out = possessor.getName() + " fails passing puck";
                gamesim.writeGameLog(out);
                //System.out.print(possessor.getName() + " fails passing puck");
                gamesim.clearLastTwoPasses();
                return new Possession(gamesim,enemyTeam.get(rand.nextInt(enemyTeam.size())), xpos, enemyTeam, possessorTeam);
            }
        }
        //if possessor clears the puck
        else if(probability >= retainChance+passChance && probability < retainChance+passChance+clearChance){
            boolean success = isClearSuccess();
            if(success){
                //System.out.print(possessor.getName() + " clears puck.");
                String out = possessor.getName() + " clears puck.";
                gamesim.writeGameLog(out);
                int recoverPercentage = rand.nextInt(50);
                if(recoverPercentage < 50) {
                    gamesim.addLastTwoPasses(possessor);
                    return new Possession(gamesim, possessorTeam.get(rand.nextInt(enemyTeam.size())), xpos + 1, possessorTeam, enemyTeam);
                }
                else
                    return new Possession(gamesim,enemyTeam.get(rand.nextInt(enemyTeam.size())), xpos + 1, enemyTeam, possessorTeam);
            }
            else{
                String out = possessor.getName() + " fails clearing puck.";
                gamesim.writeGameLog(out);
                gamesim.clearLastTwoPasses();
                //System.out.print(possessor.getName() + " fails clearing puck.");
                return new Possession(gamesim,enemyTeam.get(rand.nextInt(enemyTeam.size())), xpos, enemyTeam, possessorTeam);
            }
        }
        //if possessor shoots
        else if(probability >= retainChance+passChance+clearChance && probability < retainChance+passChance+clearChance+shootChance){
            boolean success = isShotSuccess();
            if(success){
                gamesim.getGameStats().incrementGoals(possessor.getTeamID());
                gamesim.writeGameLog(possessor.getName() + "scores!");
                if(gamesim.getLastTwoPasses()[0] != null && gamesim.getLastTwoPasses()[1] != null) {
                    PlayerStatsDb.updateAssists(gamesim.getLastTwoPasses()[0].getPlayerID());
                    PlayerStatsDb.updateAssists(gamesim.getLastTwoPasses()[1].getPlayerID());
                }
                if(gamesim.getLastTwoPasses()[0] != null && gamesim.getLastTwoPasses()[1] == null)
                    PlayerStatsDb.updateAssists(gamesim.getLastTwoPasses()[0].getPlayerID());
                //System.out.print(possessor.getName() + " scores!");
                PlayerStatsDb.updateShots(possessor.getPlayerID());
                PlayerStatsDb.updateGoals(possessor.getPlayerID());
                return new Faceoff(possessorTeam, enemyTeam,gamesim);
            }
            else{
                PlayerStatsDb.updateShots(possessor.getPlayerID());
                gamesim.getGameStats().incrementShots(possessor.getTeamID());
                gamesim.writeGameLog(possessor.getName() + " misses net!");
                gamesim.clearLastTwoPasses();
                return new Possession(gamesim,enemyTeam.get(rand.nextInt(enemyTeam.size())), 2, enemyTeam, possessorTeam);
            }
        }
        else{ //is a penalty
            //System.out.print(possessor.getName() + " takes a penalty");
            gamesim.getGameStats().incrementPenalties(possessor.getTeamID());
            gamesim.setPenalty(possessor.getTeamID());
            gamesim.clearLastTwoPasses();
            return new Faceoff(possessorTeam, enemyTeam, gamesim);
        }
    }
}
