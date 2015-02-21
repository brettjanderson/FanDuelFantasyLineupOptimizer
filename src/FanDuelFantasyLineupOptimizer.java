/**
 * Created by Brett Anderson.
 *
 * Main Class
 *
 *
 */
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;

public class FanDuelFantasyLineupOptimizer {

    static int minSalary;

    public static void main(String[] args) throws IOException {


        ArrayList<Player> playerList;

        playerList = DataScraper.getFanDuelPlayerList();

        minSalary = findMinSalary(playerList);
        Collections.sort(playerList);

        for(Player p : playerList) {
            DataScraper.getGameLogLink(p);
            System.out.println(p);
        }

        ScrapingExceptionHandler.printPlayersNotFound();

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
