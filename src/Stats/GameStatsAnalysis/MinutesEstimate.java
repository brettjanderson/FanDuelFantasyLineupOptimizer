package Stats.GameStatsAnalysis;

import FanDuelResources.Player;
import Stats.PlayerGameStats;
import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;
import org.apache.commons.math3.stat.regression.SimpleRegression;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by Brettness on 3/13/15.
 */
public class MinutesEstimate {

    private Player player;
    private ArrayList<PlayerGameStats> playerGameLogs;
    private SimpleRegression minutesRegression, minutesPerGameRegression;
    private DescriptiveStatistics minutesStatistics;
    private int gameNumber;
    private double estimatedAssistsPerMinute, estimatedMinutes;

    public MinutesEstimate(Player player, ArrayList<PlayerGameStats> playerGameStats) {

        this.player = player;
        this.playerGameLogs = playerGameStats;

        minutesStatistics = new DescriptiveStatistics();
        minutesPerGameRegression = new SimpleRegression();

        gameNumber = 1;
        for(PlayerGameStats game: playerGameLogs) {
            minutesStatistics.addValue(game.getMinutes());
            minutesPerGameRegression.addData(gameNumber, game.getMinutes());
            gameNumber++;
        }

        estimatedMinutes = Math.min(38.0, minutesPerGameRegression.predict(gameNumber));

    }

    public double getEstimatedMinutes() throws IOException {

        return estimatedMinutes;

    }

}
