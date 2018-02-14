package startup;
import database.PlayerDb;
import player.Player;

import javax.naming.Name;
import java.util.ArrayList;
import java.util.Random;

public class PlayerGen {
    public static void populatePlayerDB(int numPlayers){
        if(numPlayers == 0){
            return;
        }
        String name = "";
        String position;
        String nationality;
        int faceoff;
        int teamID;
        int offensiveSkills;
        int defensiveSkills;
        int goalieSkills;

        nationality = generateNationality();
        name = generateName(nationality);
        position = generatePosition();
        faceoff = generateFaceoffSkills(position);
        offensiveSkills = generateOffensiveSkills(position);
        defensiveSkills = generateDefensiveSkills(position);
        goalieSkills =generateGoalieSkills(position);

        /*System.out.println(name + "\n" + nationality + "\n" + position + "\nfaceoffs:" +
                            faceoff + "\noffensive:"+ offensiveSkills + "\ndefensive:"+ defensiveSkills + "\ngoalie:"+ goalieSkills );
        */
        Player p = new Player(name, position, faceoff, 0, offensiveSkills, defensiveSkills, goalieSkills );
        PlayerDb.insertPlayer(p);
        populatePlayerDB(numPlayers-1);
    }
    private static int generateGoalieSkills(String position){
        Random rand = new Random();
        if(position == "G"){
            return (int)(rand.nextGaussian() * 10 + 60);
        }
        else{
            return 0;
        }
    }
    private static int generateFaceoffSkills(String position){
        Random rand = new Random();
        if(position == "C"){
            /*
                nextGaussian * 15 + 60
                60 mean
                15 std dev
             */
            return (int)(rand.nextGaussian() * 10 + 60);
        }
        else if(position == "G"){
            return 0;
        }
        else{
            return (int)(rand.nextGaussian() * 10 + 40);
        }
    }
    private static int generateOffensiveSkills(String position){
        Random rand = new Random();
        if(position == "LW" || position == "RW" || position == "C"){
            return (int) (rand.nextGaussian() * 10 + 60);
        }
        else if(position == "G"){
            return 0;
        }
        else{
            return (int) (rand.nextGaussian() * 10 + 40);
        }
    }
    private static int generateDefensiveSkills(String position){
        Random rand = new Random();
        if(position == "LW" || position == "RW" || position == "C"){
            return (int) (rand.nextGaussian() * 10+ 40);
        }
        else if(position == "G"){
            return 0;
        }
        else{
            return (int) (rand.nextGaussian() * 10+ 60);
        }
    }
    private static String generatePosition(){
        Random rand = new Random();
        int positionprob = rand.nextInt(100);
        if(positionprob < 20){
            return "C";
        }
        else if(positionprob < 40){
            return "LW";
        }
        else if(positionprob < 60){
            return "RW";
        }
        else if(positionprob < 75){
            return "LD";
        }
        else if(positionprob < 90){
            return "RD";
        }
        else if(positionprob < 100){
            return "G";
        }
        else{
            return "N/A";
        }
    }
    private static String generateNationality(){
        Random rand = new Random();
        int nationalityprob = rand.nextInt(100);
        if(nationalityprob < 52)
            return "Canadian";
        else if(nationalityprob < 78)
            return "American";
        else if(nationalityprob < 85)
            return "Swedish";
        else if(nationalityprob < 89)
            return "Finnish";
        else if(nationalityprob < 94)
            return "Russian";
        else if(nationalityprob < 98)
            return "Czech";
        else
            return "Slovak";
    }
    private static String generateName(String nationality){
        if(nationality == "Canadian")
            return NameGrabber.getName("american");
        return NameGrabber.getName(nationality.toLowerCase());
    }
}
