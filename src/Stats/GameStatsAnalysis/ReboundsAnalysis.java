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
public class ReboundsAnalysis {

    private Player player;
    private ArrayList<PlayerGameStats> playerGameLogs;
    private SimpleRegression reboundsPerMinuteRegression, minutesPerGameRegression;
    private DescriptiveStatistics reboundsPerMinuteStatistics;
    private int gameNumber;
    private double estimatedReboundsPerMinute, estimatedMinutes;
    private final static double FANTASY_POINTS_PER_REBOUND = 1.2;

    public ReboundsAnalysis(Player player, ArrayList<PlayerGameStats> playerGameLogs) {

        this.player = player;
        this.playerGameLogs = playerGameLogs;

        reboundsPerMinuteStatistics = new DescriptiveStatistics();
        reboundsPerMinuteRegression = new SimpleRegression();
        minutesPerGameRegression = new SimpleRegression();

        gameNumber = 1;
        for(PlayerGameStats game: playerGameLogs) {
            reboundsPerMinuteStatistics.addValue(game.getRebounds() / game.getMinutes());
            reboundsPerMinuteRegression.addData(gameNumber, game.getRebounds() / game.getMinutes());
            minutesPerGameRegression.addData(gameNumber, game.getMinutes());
            gameNumber++;
        }

        estimatedReboundsPerMinute = Math.min(reboundsPerMinuteStatistics.getMax(), reboundsPerMinuteRegression.predict(gameNumber));
        estimatedMinutes = Math.min(40.0, minutesPerGameRegression.predict(gameNumber));

    }

    public double getEstimatedPointsFromRebounds() throws IOException {

        double stdDevAwayNcaaTotRebAverage = NcaaTeamToTeamStats.getStandardDeviationsAwayFromMeanTotalReboundingPercentage(player.getOpposingTeam());

        return (estimatedReboundsPerMinute -(0.5*stdDevAwayNcaaTotRebAverage*reboundsPerMinuteStatistics.getStandardDeviation()))*estimatedMinutes*FANTASY_POINTS_PER_REBOUND;
       // return estimatedReboundsPerMinute*estimatedMinutes*FANTASY_POINTS_PER_REBOUND;
    }
}
