package Stats.Verification;

import FanDuelResources.Player;
import Stats.GameTrendEstimate;
import Stats.PlayerGameStats;
import Stats.ProductionPerMinuteTrendEstimate;
import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * Created by Brettness on 2/25/15.
 */
public class EstimationPredictionModelVerfication {

    private static DescriptiveStatistics listOfDeviationsProductionPerMinute;
    private static DescriptiveStatistics listOfDeviationsEightGameTrend;
    private static DescriptiveStatistics mixOfEightAndPointsPerMinute;
    private static DescriptiveStatistics sevenGamePPMTrendEstim;

    public static void VerifyPlayerEstimations(ArrayList<Player> playerList){

        listOfDeviationsProductionPerMinute = new DescriptiveStatistics();
        listOfDeviationsEightGameTrend = new DescriptiveStatistics();
        mixOfEightAndPointsPerMinute = new DescriptiveStatistics();
        sevenGamePPMTrendEstim = new DescriptiveStatistics();

        System.out.println("Running Verification Test:");

        for(Player p : playerList){

            ArrayList<PlayerGameStats> playerGameStats = (ArrayList<PlayerGameStats>) p.getGameStats().clone();
            removeOutliersBasedOnPointsPerMinute(playerGameStats);

            if(playerGameStats.size() < 10) continue;

            PlayerGameStats lastGame = playerGameStats.get(playerGameStats.size()-1);
            String site = lastGame.getSite();

            GameTrendEstimate sevenGameEst = new GameTrendEstimate(playerGameStats.subList(playerGameStats.size() - 8, playerGameStats.size() - 1), 7);

            ProductionPerMinuteTrendEstimate sevenGamePPM = new ProductionPerMinuteTrendEstimate(playerGameStats.subList(playerGameStats.size()-8, playerGameStats.size()-1));
            sevenGamePPMTrendEstim.addValue(Math.abs(sevenGamePPM.getEstimate() - lastGame.getFantasyValueForGame()));

            ProductionPerMinuteTrendEstimate ppmEstimate = new ProductionPerMinuteTrendEstimate(playerGameStats.subList(0,playerGameStats.size()-2));
            listOfDeviationsProductionPerMinute.addValue(Math.abs(ppmEstimate.getEstimate() - lastGame.getFantasyValueForGame()));

            GameTrendEstimate eightGameEstimate = new GameTrendEstimate(playerGameStats.subList(playerGameStats.size()-9, playerGameStats.size()-1), 8);
            listOfDeviationsEightGameTrend.addValue(Math.abs(eightGameEstimate.getEstimate()- lastGame.getFantasyValueForGame()));

            double mixOfEightGameAndPPMEstimate = 0.35*eightGameEstimate.getEstimate() + 0.65*ppmEstimate.getEstimate();
            mixOfEightAndPointsPerMinute.addValue(Math.abs(mixOfEightGameAndPPMEstimate-lastGame.getFantasyValueForGame()));

        }

        System.out.println("RESULTS:");
        System.out.println("=====================");
        System.out.println("Points Per Minute Prediction Average Deviation: " + listOfDeviationsProductionPerMinute.getMean());
        System.out.println("Eight Game Trend Prediction Average Deviation: " + listOfDeviationsEightGameTrend.getMean());
        System.out.println("Mix of Eight Game Trend(35%) Sand PPM(65%) Average Deviation: " + mixOfEightAndPointsPerMinute.getMean());
        System.out.println("Seven Game PPM Average Deviation: " + sevenGamePPMTrendEstim.getMean());
    }
    private static void removeOutliersBasedOnPointsPerMinute(ArrayList<PlayerGameStats> gameStats){

        DescriptiveStatistics descStats = new DescriptiveStatistics();

        for(PlayerGameStats game : gameStats){
            descStats.addValue(game.getFantasyValueForGame()/game.getMinutes());
        }

        double stdDev = descStats.getStandardDeviation();
        double mean = descStats.getMean();
        double iqr = descStats.getPercentile(75) - descStats.getPercentile(25);


        Iterator<PlayerGameStats> iterator = gameStats.iterator();
        PlayerGameStats current;


        while(iterator.hasNext()){
            current = iterator.next();

            if(current.getFantasyValueForGame()/current.getMinutes() > (mean + 2*iqr) || current.getFantasyValueForGame()/current.getMinutes() < (mean - 2*iqr)){
                iterator.remove();
            }
        }
    }


}
