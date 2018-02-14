package matchsim;

import database.GameDb;
import database.PlayerDb;
import database.SQLiteJDBCDriverConnection;
import database.TeamDb;
import player.Player;
import startup.PlayerGen;
import startup.TeamGen;
import team.Team;

import java.util.ArrayList;
/*
AWAY  | HOME
-2 -1 0 1 2
 */
public class Game {
    public static void main(String[] args){
        /*
        < Center, LW, RW, LD, RD >
         */
        SQLiteJDBCDriverConnection.createNewDatabase();
        //SQLiteJDBCDriverConnection.connect();
        //PlayerGen.populatePlayerDB(100);
        //TeamGen.buildTeam(1);
        // TeamGen.buildTeam(2);
        //TeamDb.assignLinesDefault(1);
        //TeamDb.assignLinesDefault(2);
        Player homeGoalie = PlayerDb.getPlayer(TeamDb.getPlayerAtPosition(1,"G1"));
        Player awayGoalie = PlayerDb.getPlayer(TeamDb.getPlayerAtPosition(2, "G1"));

        //FSM gamesim = new FSM(home, away, p11, p12);
        /*
            Connect to SQLite database
         */
        for(int k=0; k<8; k++) {
            FSM gamesim = new FSM(TeamDb.getline(1, 1), TeamDb.getline(2,1),
                    homeGoalie, awayGoalie,1, 1);
            gamesim.setState(new Faceoff(gamesim.getHomeOnIce(), gamesim.getAwayOnIce(), gamesim));
            for (int i = 0; i <= 720; i++)
                gamesim.update();
                //gamesim.exportGameLogtoFile();
                GameDb.addGameToDatabase(gamesim.getGameStats().getHomeID(),
                        gamesim.getGameStats().getAwayID(),
                        gamesim.getGameStats().getGamelog(),
                        gamesim.getGameStats().getHomeGoals(),
                        gamesim.getGameStats().getAwayGoals(),
                        gamesim.getGameStats().getHomeShotsT1(),
                        gamesim.getGameStats().getAwayShotsT2());
        }
        //System.out.print(gamesim.getGameStats().getGamelog());
    }
}
