/**
 * Created by Brettness on 2/18/15.
 */

import org.jsoup.*;

import javax.xml.crypto.Data;
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;

public class PlayerStats {

    static ArrayList<Player> playersTaken = new ArrayList<Player>();
    static int minSalary;

    public static void main(String[] args) throws IOException {


        ArrayList<Player> playerList;

        playerList = DataScraper.getPlayerList();

        minSalary = findMinSalary(playerList);
        Collections.sort(playerList);

        for(Player p : playerList) {
            DataScraper.getGameLogLink(p);
            System.out.println(p);
        }

        ProblemsCatcher.printPlayersNotFound();

    }

    public static RosterSolution getOptimumSolution(ArrayList<Player> playersList, int salaryLeft) {
        return null;
    }

    public static int findMinSalary(ArrayList<Player> playerList){
        int min = Integer.MAX_VALUE;

        for(Player p : playerList){
            if(p.getSalary() < min) min = p.getSalary();
        }

        return min;
    }
 }
