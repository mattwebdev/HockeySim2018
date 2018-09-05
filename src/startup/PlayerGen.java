package startup;
import database.PlayerDb;
import development.Development;
import player.Player;

import javax.naming.Name;
import java.util.ArrayList;
import java.util.Random;

public class PlayerGen {
    private static final int HIGHEST_POTENTIAL = 200;
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
        int age;
        int potential = 0;

        nationality = generateNationality();
        name = generateName(nationality);
        position = generatePosition();
        age = generateAge();
        faceoff = generateFaceoffSkills(position, age);
        offensiveSkills = generateOffensiveSkills(position, age);
        defensiveSkills = generateDefensiveSkills(position, age);
        potential = generatePotential(offensiveSkills, defensiveSkills, age);
        goalieSkills =generateGoalieSkills(position);

        Player p = new Player(potential, name, position, age, faceoff, 0, offensiveSkills, defensiveSkills, goalieSkills );
        PlayerDb.insertPlayer(p);
        populatePlayerDB(numPlayers-1);
    }
    private static int generatePotential(int offensiveSkills, int defensiveSkills, int age){
        Random rand = new Random();
        int currPotential = offensiveSkills + defensiveSkills;
        int futurePotential = rand.nextInt(HIGHEST_POTENTIAL- currPotential) + currPotential;


        return (int)(futurePotential * Development.getPercentPotentialAtAge(age));
    }
    private static int generateAge(){
        Random rand = new Random();
        int age = (int)(rand.nextGaussian()*5 + 27);
        if(age < 18){
            age = 18;
        }
        return age;
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
    private static int generateFaceoffSkills(String position, int age){
        Random rand = new Random();
        int faceoffSkills = 0;
        if(position == "C"){
            /*
                nextGaussian * 15 + 60
                60 mean
                15 std dev
             */
            faceoffSkills =  (int)(rand.nextGaussian() * 10 + 60);
        }
        else if(position == "G"){
            faceoffSkills= 0;
        }
        else{
            faceoffSkills = (int)(rand.nextGaussian() * 10 + 40);
        }
        return (int)(faceoffSkills * Development.getPercentSkillAtAge(age)) ;
    }
    private static int generateOffensiveSkills(String position, int age){
        Random rand = new Random();
        int offSkills = 0;
        if(position == "LW" || position == "RW" || position == "C"){
            offSkills =  (int) (rand.nextGaussian() * 10 + 60);
        }
        else if(position == "G"){
            offSkills = 0;
        }
        else{
            offSkills = (int) (rand.nextGaussian() * 10 +40);
        }
        int dist = (int)rand.nextGaussian()*5 + 27;
        return (int)(offSkills * Development.getPercentSkillAtAge(age)) ;
    }
    private static int generateDefensiveSkills(String position, int age){
        Random rand = new Random();
        int defSkills = 0;
        if(position == "LW" || position == "RW" || position == "C"){
            defSkills = (int) (rand.nextGaussian() * 10+ 40);
        }
        else if(position == "G"){
            defSkills=  0;
        }
        else{
            defSkills =  (int) (rand.nextGaussian() * 10+ 60);
        }

        return (int)(defSkills * Development.getPercentSkillAtAge(age)) ;
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
