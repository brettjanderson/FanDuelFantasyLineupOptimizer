package Adapters;

import Data.DataScraper;
import Data.PlayerDataResourcesController;
import FanDuelResources.FanDuelPlayer;
import FanDuelResources.Player;
import Stats.PlayerGameStats;
import Stats.PlayerPointEstimator;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by Brettness on 2/24/15.
 */
public class FanDuelPlayerAdapter {

    public static ArrayList<Player> buildPlayerListFromFanDuelPlayers(ArrayList<FanDuelPlayer> fanDuelList) throws IOException, InterruptedException {

        ArrayList<Player> playerList = new ArrayList<Player>();

        for(FanDuelPlayer fPlayer: fanDuelList){

            String espnId = PlayerDataResourcesController.getEspnPlayerId(fPlayer);
            String teamName = PlayerDataResourcesController.getTeamName(fPlayer.getTeamNameAbbr());
            String opposingTeamName = PlayerDataResourcesController.getTeamName(fPlayer.getOpposingTeamAbbr());
            ArrayList<PlayerGameStats> playerGameStats = DataScraper.getGameStatsForPlayer(espnId, fPlayer.getName());

            Player p = new Player(fPlayer, espnId, teamName, opposingTeamName, playerGameStats);

            double pointsEstimation = PlayerPointEstimator.estimateFantasyPoints(playerGameStats, p);

            p.setPointsPerGame(pointsEstimation);

            playerList.add(p);
        }

        return playerList;
    }
}
