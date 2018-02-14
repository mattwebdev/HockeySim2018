package player;


import java.util.ArrayList;
import java.util.HashMap;

public class Contract {
    private int aav;
    HashMap<Integer, Integer> contractStructure;

    public Contract(){
        contractStructure = new HashMap<Integer, Integer>();
        aav = 0;
    }
    public void addYear(int contractYear, int salary){
        contractStructure.put(contractYear, salary);
    }
    private void calculateAav(){
        int count = 0;
        for(Integer sal : contractStructure.values()){
            count++;
            aav += sal;
        }
    }
    public int getAav(){
        return aav;
    }
}
