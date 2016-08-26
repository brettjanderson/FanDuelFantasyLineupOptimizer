package Stats;

import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * Created by Brett Anderson
 *
 * Takes a players Game Statistics and performs
 * different analyses in order to predict future
 * performance.
 */

public class PlayerAnalysisTool {

    public static double estimatePlayerFantasyOutput(ArrayList<PlayerGameStats> gameStats, String site){

        int numberOfGames;
        GameTrendEstimate sixGameTrendEstimate = null, eightGameEstimate = null, fourGameTrendEstimate = null;
        SeasonTrendEstimate seasonTrendEstimate = null;
        ProductionPerMinuteTrendEstimate productionPerMinuteTrendEstimate = null;

        //removeOutliers(gameStats);
        removeOutliersBasedOnPointsPerMinute(gameStats);

        numberOfGames = gameStats.size();

        if(numberOfGames == 0) return 0.0;

        productionPerMinuteTrendEstimate = new ProductionPerMinuteTrendEstimate(gameStats);

        if(numberOfGames >= 8) {
            eightGameEstimate = new GameTrendEstimate(gameStats.subList(numberOfGames - 8, numberOfGames - 1), 8);
            return productionPerMinuteTrendEstimate.getEstimate()*0.7 + eightGameEstimate.getEstimate()*0.3;
        } else if(numberOfGames >= 6) {
            sixGameTrendEstimate = new GameTrendEstimate(gameStats.subList(numberOfGames - 6, numberOfGames - 1), 6);
            return productionPerMinuteTrendEstimate.getEstimate() * 0.75 + sixGameTrendEstimate.getEstimate() * 0.2;
        } else if(numberOfGames >= 4) {
            fourGameTrendEstimate = new GameTrendEstimate(gameStats.subList(numberOfGames - 4, numberOfGames - 1), 4);
            return productionPerMinuteTrendEstimate.getEstimate() * 0.85 + fourGameTrendEstimate.getEstimate() * 0.15;
        } else {
            seasonTrendEstimate = new SeasonTrendEstimate(gameStats);
            return seasonTrendEstimate.getEstimate() * .10 + productionPerMinuteTrendEstimate.getEstimate() * 0.90;
        }

    }

    private static void removeOutliers(ArrayList<PlayerGameStats> gameStats){

        DescriptiveStatistics descStats = new DescriptiveStatistics();

        for(PlayerGameStats game : gameStats){
            descStats.addValue(game.getFantasyValueForGame());
        }

        double stdDev = descStats.getStandardDeviation();
        double mean = descStats.getMean();
        double iqr = descStats.getPercentile(75) - descStats.getPercentile(25);


        Iterator<PlayerGameStats> iterator = gameStats.iterator();
        PlayerGameStats current;


        while(iterator.hasNext()){
            current = iterator.next();

            if(current.getFantasyValueForGame() > (mean + 1.5*stdDev) || current.getFantasyValueForGame() < (mean - 1.5*stdDev)){
                iterator.remove();
            }
        }
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

            if(current.getFantasyValueForGame()/current.getMinutes() > (mean + 1.5*iqr) || current.getFantasyValueForGame()/current.getMinutes() < (mean - 1.5*iqr)){
                iterator.remove();
            }
        }
    }
}
