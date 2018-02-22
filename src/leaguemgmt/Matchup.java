package leaguemgmt;

public class Matchup{
    private int homeTeamID;
    private int awayTeamID;
    public Matchup(int home, int away){
        homeTeamID = home;
        awayTeamID = away;
    }
    public int getHomeTeamID(){
        return homeTeamID;
    }
    public int getAwayTeamID(){
        return awayTeamID;
    }

    @Override
    public String toString() {
        return "Home:" + homeTeamID + "  Away:" + awayTeamID + "\n";
    }
}