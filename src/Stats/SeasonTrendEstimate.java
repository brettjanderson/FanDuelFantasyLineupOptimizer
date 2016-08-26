package Stats;

import org.apache.commons.math3.stat.regression.SimpleRegression;

import java.util.List;

/**
 * Created by Brettness on 2/24/15.
 */
public class SeasonTrendEstimate {

    private SimpleRegression regression;
    private double estimate;

    public SeasonTrendEstimate(List<PlayerGameStats> gameStats) {

        regression = new SimpleRegression();
        int gameNumber = 0;

        for(PlayerGameStats game: gameStats){
            regression.addData(gameNumber, game.getFantasyValueForGame());
            gameNumber++;
        }

        estimate = regression.predict(gameNumber);
    }

    public double getEstimate(){
        return estimate;
    }
}
