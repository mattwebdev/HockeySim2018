package matchsim;

import player.Player;

import java.util.ArrayList;
import java.util.Random;

public class Faceoff implements State{
    /*
    Constants controlling the base success rate of a faceoff and the individual variation based on skill
     */
    private static final int BASE_FACEOFF = 50;
    private static final int FACEOFF_VARIATION = 10;

    private ArrayList<Player> home;
    private ArrayList<Player> away;
    private Player homeParticipant;
    private Player awayParticipant;
    private Random rand;
    private FSM gamesim;

    public Faceoff(ArrayList<Player> home, ArrayList<Player> away, FSM gamesim){
        this.home = home;
        this.away = away;
        this.gamesim = gamesim;
        homeParticipant = home.get(0);
        awayParticipant = away.get(0);
        rand = new Random();
    }
    /*
    Changes line out at a faceoff
     */
    public void setLines(ArrayList<Player> possessorTeam, ArrayList<Player> enemyTeam){
        this.home = possessorTeam;
        homeParticipant = home.get(0);
        this.away = enemyTeam;
        awayParticipant = away.get(0);
    }
    /*
    No team is possessing in a Faceoff
     */
    public int getPossessorTeam(){
        return -1;
    }
    /*
    Determines the next event (either Home wins or Away wins) and transitions state
     */
    public State next(){
        int r = rand.nextInt(100);
        double probabilityHomeWins = BASE_FACEOFF + FACEOFF_VARIATION*(((double)homeParticipant.getFaceoff()- awayParticipant.getFaceoff())/50);
        if(r < probabilityHomeWins){
            gamesim.writeGameLog(homeParticipant.getName() + "(Home) won the faceoff");
            //System.out.println(homeParticipant.getName() + "(Home) won the faceoff");
            return new Possession(gamesim, home.get(rand.nextInt(home.size())), 0, home, away);
        }
        else{
            gamesim.writeGameLog(awayParticipant.getName() + "(Away) won the faceoff");
            //System.out.println(awayParticipant.getName() + "(Away) won the faceoff");
            return new Possession(gamesim, away.get(rand.nextInt(away.size())), 0,away, home);
        }
    }
}
