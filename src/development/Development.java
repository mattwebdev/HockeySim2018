package development;

import database.PlayerDb;
import player.Player;

import java.util.Random;

public class Development {
    /*
        This is a quadratic function for modeling skill decay by age
     */
    public static double getPercentSkillAtAge(int age){
        //This is the quadratic function to model skill decline over age
        double A = -.0354;
        double B = .07309;
        double C = -.001438;
        double totalPercentSkill = A + B*age + C*age*age;
        return totalPercentSkill;
    }
    public static double getPercentPotentialAtAge(int age){
        //This is the quadratic function to model skill decline over age
        double A = .0129;
        double B = .085655;
        double C = -.001865;
        double totalPercentSkill = A + B*age + C*age*age;
        return totalPercentSkill;
    }
    public static void developPlayer(int pid){
        Player p = PlayerDb.getPlayer(pid);
        Random rand = new Random();
        int pointsLeft = p.getPotential() - (p.getOffensiveSkills()-p.getDefensiveSkills());
        int pAge = p.getAge();
        int increase =((pointsLeft/(28-pAge)));
        int percIncrease = ((100* increase/pointsLeft));;
        int prob = rand.nextInt(100);
        if(prob < Math.abs(percIncrease)){
            int whichStat = rand.nextInt(2);
            if(whichStat == 0){
                if(percIncrease < 0)
                    PlayerDb.updatePlayerStats(pid, -1, 0);
                else
                    PlayerDb.updatePlayerStats(pid, 1, 0);

            }
            else{
                if(percIncrease < 0)
                    PlayerDb.updatePlayerStats(pid, 0, -1);
                else
                    PlayerDb.updatePlayerStats(pid, 0, 1);
            }
        }
    }
}
