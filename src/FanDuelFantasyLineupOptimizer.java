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

        Collections.sort(playerList);

        for(Player p : playerList) {
            DataScraper.getGameLogLink(p);
            System.out.println(p);
        }

        ScrapingExceptionHandler.printPlayersNotFound();

        RosterSolution optimalLineup = PlayerKnapSackAnalysisTool.getOptimumRoster(playerList);

        System.out.println("=======================");
        System.out.println("Optimal Lineup");
        System.out.println("=======================");

        System.out.println(optimalLineup);



    }

 }
