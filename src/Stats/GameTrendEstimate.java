package Stats;

import org.apache.commons.math3.stat.regression.SimpleRegression;

import java.util.List;

/**
 * Created by Brettness on 2/24/15.
 */
public class GameTrendEstimate {
    private double estimate;
    private SimpleRegression regression;
    private int numberOfGames;

    public GameTrendEstimate(List<PlayerGameStats> gameStats, int numberOfGames) {

        this.estimate = 0;
        this.numberOfGames = numberOfGames;
        int gameNumber = 1;
        regression = new SimpleRegression();

        for(PlayerGameStats game : gameStats){
            regression.addData(gameNumber, game.getFantasyValueForGame());
            gameNumber++;
        }
        estimate = regression.predict(numberOfGames+1);
    }

    public double getEstimate() {
        return estimate;
    }
}
