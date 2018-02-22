package development;

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

    }
}
