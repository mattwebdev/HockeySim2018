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
        Player p1 = new Player("John", 55,1, 80, 60);
        Player p2 = new Player("Tom", 95,2, 60, 40);
        Player p3 = new Player("Sam", 50,1, 75, 50);
        Player p4 = new Player("Nathan", 50,1, 80, 35);
        Player p5 = new Player("Anders", 50,1, 60, 85);
        Player p6 = new Player("Michael", 75,1, 40, 90);
        Player p7 = new Player("Aaron", 35,2, 60,30);
        Player p8 = new Player("Erik", 80,2, 50,40);
        Player p9 = new Player("Mikko", 20,2, 40,65);
        Player p10 = new Player("Theo", 86,2, 50, 50);

        Player p11 = new Player("Jaro", "G", 86,1, 50, 50, 90);
        Player p12 = new Player("Timmy", "G", 86,2, 50, 50, 70);

        ArrayList<Player> home = new ArrayList<Player>();
        home.add(p1);
        home.add(p3);
        home.add(p4);
        home.add(p5);
        home.add(p6);
        ArrayList<Player> away = new ArrayList<Player>();
        away.add(p2);
        away.add(p7);
        away.add(p8);
        away.add(p9);
        away.add(p10);
        SQLiteJDBCDriverConnection.createNewDatabase();
        //SQLiteJDBCDriverConnection.connect();
        //PlayerGen.populatePlayerDB(100);
        //TeamGen.buildTeam(1);
        // TeamGen.buildTeam(2);
        //TeamDb.assignLinesDefault(1);
        //TeamDb.assignLinesDefault(2);
        Player homeGoalie = PlayerDb.getPlayer(TeamDb.getPlayerAtPosition(1,"G1"));
        Player awayGoalie = PlayerDb.getPlayer(TeamDb.getPlayerAtPosition(2, "G1"));
        FSM gamesim = new FSM(TeamDb.getline(1, 1), TeamDb.getline(2,1),
            homeGoalie, awayGoalie,1, 1);
        //FSM gamesim = new FSM(home, away, p11, p12);
        /*
            Connect to SQLite database
         */

        gamesim.setState(new Faceoff(home, away, gamesim));
        for(int i=0; i<= 720; i++)
            gamesim.update();
        gamesim.exportGameLogtoFile();
        GameDb.addGameToDatabase(gamesim.getGameStats().getHomeID(),
                gamesim.getGameStats().getAwayID(),
                gamesim.getGameStats().getGamelog(),
                gamesim.getGameStats().getHomeGoals(),
                gamesim.getGameStats().getAwayGoals(),
                gamesim.getGameStats().getHomeShotsT1(),
                gamesim.getGameStats().getAwayShotsT2());
        System.out.print(gamesim.getGameStats().getGamelog());
    }
}
