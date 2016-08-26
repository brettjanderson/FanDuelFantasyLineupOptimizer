/**
 * Created by Brett Anderson.
 *
 * Main Class
 *
 *
 */

import Adapters.FanDuelPlayerAdapter;
import Data.DataScraper;
import Data.PlayerDataResourcesController;
import Exceptions.ScrapingExceptionHandler;
import FanDuelResources.FanDuelPlayer;
import FanDuelResources.Player;
import PlayerAnalysis.PlayerKnapSackAnalysisTool;
import PlayerAnalysis.RosterSolution;
import Stats.TeamStats;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Hashtable;
import java.util.Iterator;

public class FanDuelFantasyLineupOptimizer {

    public static void main(String[] args) throws IOException, InterruptedException {

        ArrayList<FanDuelPlayer> fanDuelPlayerList = DataScraper.getFanDuelPlayerList();
        ArrayList<Player> playerList;

        Collections.sort(fanDuelPlayerList);
        removeLowScoringPlayers(fanDuelPlayerList);

        for(FanDuelPlayer p : fanDuelPlayerList) {
            System.out.println(p);
        }

        Hashtable<String, TeamStats> advancedTeamStats = DataScraper.getAdvancedTeamStats();
        System.out.println("HERE");
        System.out.println("Loaded Players From FanDuel");

        playerList = FanDuelPlayerAdapter.buildPlayerListFromFanDuelPlayers(fanDuelPlayerList);

        if(!ScrapingExceptionHandler.getPlayersNotFound().isEmpty()){
            for(FanDuelPlayer player: ScrapingExceptionHandler.getPlayersNotFound()){
                System.out.println(player + " NOT FOUND!");
            }
            System.exit(0);
        }

        if(!ScrapingExceptionHandler.getTeamNamesNotFound().isEmpty()){
            for(String teamName: ScrapingExceptionHandler.getTeamNamesNotFound()){
                System.out.println(teamName + " NOT FOUND!");
            }
            System.exit(0);
        }


//
//        System.out.println("=======================");
//        ScrapingExceptionHandler.printPlayersNotFound();
//        System.out.println("=======================");
//
////        EstimationPredictionModelVerfication.VerifyPlayerEstimations(playerList);
//
        for(Player p : playerList) {
            System.out.println(p);
        }

        PlayerDataResourcesController.closeWriters();
        RosterSolution optimalLineup = PlayerKnapSackAnalysisTool.getOptimumRoster(playerList);

        System.out.println("=======================");
        System.out.println("Optimal Lineup");
        System.out.println("=======================");

        System.out.println(optimalLineup);
        System.out.println("=======================");


        System.out.println(PlayerKnapSackAnalysisTool.recursiveCalls);

    }

    private static void removeLowScoringPlayers(ArrayList<FanDuelPlayer> playerList){

        Iterator<FanDuelPlayer> iterator = playerList.iterator();
        FanDuelPlayer current;

        while(iterator.hasNext()){
            current = iterator.next();

            if(current.getAveragePointsPerGame() < 8.0){
                iterator.remove();
            }
        }
    }

 }
