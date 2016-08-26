package Stats;

import org.apache.commons.math3.stat.regression.SimpleRegression;
import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;

import java.util.List;

/**
 * Created by Brettness on 2/25/15.
 */
public class ProductionPerMinuteTrendEstimate {

    private SimpleRegression regression;
    private double estimate;

    public ProductionPerMinuteTrendEstimate(List<PlayerGameStats> gameStats) {

        regression = new SimpleRegression();
        DescriptiveStatistics stats = new DescriptiveStatistics();
        int gameNumber = 0;

        for(PlayerGameStats game: gameStats){
            if(game.getMinutes() > 0.0)
                regression.addData(gameNumber, game.getFantasyValueForGame()/game.getMinutes());
            else continue;
            stats.addValue(game.getMinutes());
            gameNumber++;
        }

        estimate = regression.predict(gameNumber)*stats.getMean();
        //estimate = regression.predict(gameNumber);
    }

    public double getEstimate(){
        return estimate;
    }
}
