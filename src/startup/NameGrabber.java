package startup;

import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.util.Random;

public class NameGrabber {
    private static int getNameBound(String nationality){
        switch(nationality){
            case "russian": return 46;
            case "canadian": return 221;
            case "american": return 221;
            case "swiss": return 30;
            case "finnish": return 30;
            case "slovak" : return 30;
            case "swedish": return 64;
            case "czech": return 33;
        }
        return -1;
    }
    public static String getName(String nationality) {
        Random rand = new Random();
        String pathnamef = "namegen/" + nationality + "_f.txt";
        String pathnamel = "namegen/" + nationality + "_l.txt";
        int readcountf = rand.nextInt(getNameBound(nationality));
        int readcountl = rand.nextInt(getNameBound(nationality));
        int countl = 0;
        int countf = 0;
        String firstname = "default";
        String lastname = "default";
        File filef = new File(pathnamef);
        File filel = new File(pathnamel);
        try {
            BufferedReader brf = new BufferedReader(new FileReader(filef));
            BufferedReader brl = new BufferedReader(new FileReader(filel));
            while ((firstname = brf.readLine()) != null){
                if(countf == readcountf)
                    break;
                else
                    countf++;
            }
            while((lastname = brl.readLine()) != null){
                if(countl == readcountl)
                    break;
                else
                    countl++;
            }
        }
        catch(Exception e){
            System.out.println("No file found or IOException");
        }
        return firstname + " " + lastname;
    }
}
