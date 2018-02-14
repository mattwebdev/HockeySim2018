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
    public Player(String name, String position, int faceoff, int teamID, int offensiveSkills, int defensiveSkills, int goalieskills){
        this.position = position;
        this.name = name;
        this.faceoff = faceoff;
        this.teamID = teamID;
        this.offensiveSkills = offensiveSkills;
        this.defensiveSkills = defensiveSkills;
        this.goalieSkills = goalieskills;
    }
    public Player(String name, int faceoff, int teamID, int offensiveSkills, int defensiveSkills){
        this.name = name;
        this.faceoff = faceoff;
        this.teamID = teamID;
        this.offensiveSkills = offensiveSkills;
        this.defensiveSkills = defensiveSkills;
        this.goalieSkills = 0;
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

    public void setFaceoff(int faceoffAbility) {
        this.faceoff = faceoff;
    }
}
