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
public class PointsAnalysis {

    private Player player;
    private ArrayList<PlayerGameStats> playerGameLogs;
    private SimpleRegression pointsPerMinuteRegression, minutesPerGameRegression;
    private DescriptiveStatistics pointsPerMinuteStatistics;
    private int gameNumber;
    private double estimatedPointsPerMinute, estimatedMinutes;

    public PointsAnalysis(Player player, ArrayList<PlayerGameStats> playerGameLogs) {

        this.player = player;
        this.playerGameLogs = playerGameLogs;

        pointsPerMinuteStatistics = new DescriptiveStatistics();
        pointsPerMinuteRegression = new SimpleRegression();
        minutesPerGameRegression = new SimpleRegression();

        gameNumber = 1;
        for(PlayerGameStats game: playerGameLogs) {
            pointsPerMinuteStatistics.addValue(game.getPoints() / game.getMinutes());
            pointsPerMinuteRegression.addData(gameNumber, game.getPoints() / game.getMinutes());
            minutesPerGameRegression.addData(gameNumber, game.getMinutes());
            gameNumber++;
        }

        estimatedPointsPerMinute = Math.min(pointsPerMinuteStatistics.getMax(), pointsPerMinuteRegression.predict(gameNumber));
        estimatedMinutes = Math.min(40.0, minutesPerGameRegression.predict(gameNumber));

    }

    public double getEstimatedPointsFromPoints() throws IOException {

        //double percentileSos = NcaaTeamToTeamStats.getStrengthOfSchedulePercentile(player.getOpposingTeam());
        double stdDevAwayOppTeamDefense = NcaaTeamToTeamStats.getStandardDeviationsAwayFromMeanTeamDefense(player.getOpposingTeam());

        return (estimatedPointsPerMinute - (0.5*stdDevAwayOppTeamDefense*pointsPerMinuteStatistics.getStandardDeviation()))*estimatedMinutes;

        //return estimatedPointsPerMinute*estimatedMinutes;
    }

}
