package player;

public class Player {
    private String name;
    private Contract currentContract;
    private String position;
    private char freeAgentStatus;
    private int playerID;
    private int faceoff;
    private int teamID;
    private int offensiveSkills;
    private int defensiveSkills;
    private int goalieSkills;
    private int age;

    public int getPlayerID(){
        return playerID;
    }
    public int getOffensiveSkills() {
        return offensiveSkills;
    }
    public int getGoalieSkills(){
        return goalieSkills;
    }
    public int getDefensiveSkills() {
        return defensiveSkills;
    }
    public Player(int playerID,String name, String position, int age, int faceoff, int teamID, int offensiveSkills, int defensiveSkills, int goalieskills){
        this.playerID = playerID;
        this.position = position;
        this.name = name;
        this.age = age;
        this.faceoff = faceoff;
        this.teamID = teamID;
        this.offensiveSkills = offensiveSkills;
        this.defensiveSkills = defensiveSkills;
        this.goalieSkills = goalieskills;
    }
    public Player(String name, String position, int age, int faceoff, int teamID, int offensiveSkills, int defensiveSkills, int goalieskills){
        this.name = name;
        this.position = position;
        this.faceoff = faceoff;
        this.age = age;
        this.teamID = teamID;
        this.offensiveSkills = offensiveSkills;
        this.defensiveSkills = defensiveSkills;
        this.goalieSkills = goalieskills;
    }
    public String getPosition(){
        return position;
    }
    public String getName(){
        return name;
    }
    public int getTeamID(){
        return teamID;
    }
    public int getFaceoff() {
        return faceoff;
    }
    public int getAge(){ return age;}
    public void setFaceoff(int faceoffAbility) {
        this.faceoff = faceoff;
    }
}
