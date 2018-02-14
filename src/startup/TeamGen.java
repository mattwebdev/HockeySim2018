package startup;

import database.PlayerDb;
import player.Player;

import java.util.ArrayList;
import java.util.Random;

public class TeamGen {
    public static void buildTeam(int teamid){
        int goalieCount = 2;
        int lwCount = 4;
        int rwCount = 4;
        int centersCount = 4;
        int ldCount = 3;
        int rdCount = 3;
        int scratchCount = 3;

        ArrayList<Integer> unassigned = PlayerDb.getUnassignedPlayers();
        Random rand = new Random();
        while((goalieCount+lwCount+rwCount+centersCount+ldCount+rdCount+scratchCount) > 0){
            int unassignedindex = rand.nextInt(unassigned.size());
            int pid = unassigned.get(unassignedindex);
            Player p = PlayerDb.getPlayer(pid);
            String pos = p.getPosition();
            switch(pos){
                case "C":
                    if(centersCount > 0){
                        centersCount--;
                        unassigned.remove(unassignedindex);
                        PlayerDb.updatePlayerTeam(pid, teamid);
                    }
                    else if(scratchCount > 0){
                        scratchCount--;
                        unassigned.remove(unassignedindex);
                        PlayerDb.updatePlayerTeam(pid,teamid);
                    }
                    break;
                case "LW":
                    if(lwCount > 0){
                        lwCount--;
                        unassigned.remove(unassignedindex);
                        PlayerDb.updatePlayerTeam(pid, teamid);
                    }
                    else if(scratchCount > 0){
                        scratchCount--;
                        unassigned.remove(unassignedindex);
                        PlayerDb.updatePlayerTeam(pid,teamid);
                    }
                    break;
                case "RW":
                    if(rwCount > 0){
                        rwCount--;
                        unassigned.remove(unassignedindex);
                        PlayerDb.updatePlayerTeam(pid, teamid);
                    }
                    else if(scratchCount > 0){
                        scratchCount--;
                        unassigned.remove(unassignedindex);
                        PlayerDb.updatePlayerTeam(pid,teamid);
                    }
                    break;
                case "LD":
                    if(ldCount > 0){
                        ldCount--;
                        unassigned.remove(unassignedindex);
                        PlayerDb.updatePlayerTeam(pid, teamid);
                    }
                    else if(scratchCount > 0){
                        scratchCount--;
                        unassigned.remove(unassignedindex);
                        PlayerDb.updatePlayerTeam(pid,teamid);
                    }
                    break;
                case "RD":
                    if(rdCount > 0){
                        rdCount--;
                        unassigned.remove(unassignedindex);
                        PlayerDb.updatePlayerTeam(pid, teamid);
                    }
                    else if(scratchCount > 0){
                        scratchCount--;
                        unassigned.remove(unassignedindex);
                        PlayerDb.updatePlayerTeam(pid,teamid);
                    }
                    break;
                case "G":
                    if(goalieCount > 0){
                        goalieCount--;
                        unassigned.remove(unassignedindex);
                        PlayerDb.updatePlayerTeam(pid, teamid);
                    }
                    break;
            }
        }
    }
}
