package Stats.GameStatsAnalysis;

import FanDuelResources.Player;
import Stats.NcaaTeamToTeamStats;
import Stats.PlayerGameStats;
import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;
import org.apache.commons.math3.stat.regression.SimpleRegression;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by Brett Anderson.
 */
public class StealsAnalysis {

    private SimpleRegression stealsPerMinuteRegression, minutesPerGameRegression;
    private DescriptiveStatistics stealsPerMinuteStatistics;
    private int gameNumber;
    private double estimatedStealsPerMinute, estimatedMinutes;
    private Player player;

    public StealsAnalysis(Player player, ArrayList<PlayerGameStats> gameStats) {

        this.player = player;

        stealsPerMinuteStatistics = new DescriptiveStatistics();
        stealsPerMinuteRegression = new SimpleRegression();
        minutesPerGameRegression = new SimpleRegression();

        gameNumber = 1;
        for (PlayerGameStats game : gameStats) {
            stealsPerMinuteStatistics.addValue(game.getSteals() / game.getMinutes());
            stealsPerMinuteRegression.addData(gameNumber, game.getSteals() / game.getMinutes());
            minutesPerGameRegression.addData(gameNumber, game.getMinutes());
            gameNumber++;
        }

        estimatedStealsPerMinute = Math.min(stealsPerMinuteStatistics.getMax(), stealsPerMinuteRegression.predict(gameNumber));
        estimatedMinutes = Math.min(40.0, minutesPerGameRegression.predict(gameNumber));
    }

    public double getEstimatedStealsPoints() throws IOException {

        double stdDevAwayNcaaTurnoverPercentageAverage = NcaaTeamToTeamStats.getStandardDeviationsAwayFromMeanTurnoverPercentage(player.getOpposingTeam());

        return (estimatedStealsPerMinute + (0.5*stdDevAwayNcaaTurnoverPercentageAverage*stealsPerMinuteStatistics.getStandardDeviation()))*estimatedMinutes*2.0;
        //return estimatedStealsPerMinute*estimatedMinutes*2.0;
    }
}
