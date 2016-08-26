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
public class TurnoversAnalysis {

    private Player player;
    private ArrayList<PlayerGameStats> playerGameLogs;
    private SimpleRegression turnoversPerMinuteRegression, minutesPerGameRegression;
    private DescriptiveStatistics turnoversPerMinuteStatistics;
    private int gameNumber;
    private double estimatedTurnoversPerMinute, estimatedMinutes;
    private final static double FANTASY_POINTS_PER_TURNOVER = -1.0;

    public TurnoversAnalysis(Player player, ArrayList<PlayerGameStats> gameStats) {

        this.player = player;

        turnoversPerMinuteStatistics = new DescriptiveStatistics();
        turnoversPerMinuteRegression = new SimpleRegression();
        minutesPerGameRegression = new SimpleRegression();

        gameNumber = 1;
        for (PlayerGameStats game : gameStats) {
            turnoversPerMinuteStatistics.addValue(game.getSteals() / game.getMinutes());
            turnoversPerMinuteRegression.addData(gameNumber, game.getSteals() / game.getMinutes());
            minutesPerGameRegression.addData(gameNumber, game.getMinutes());
            gameNumber++;
        }

        estimatedTurnoversPerMinute = Math.min(turnoversPerMinuteStatistics.getMax(), turnoversPerMinuteRegression.predict(gameNumber));
        estimatedMinutes = Math.min(40.0, minutesPerGameRegression.predict(gameNumber));
    }

    public double getEstimatesPointsFromTurnovers() throws IOException {

        double stdDevAwayNcaaAverageStealPercentage = NcaaTeamToTeamStats.getStandardDeviationsAwayFromMeanStealPercentage(player.getOpposingTeam());

        return (estimatedTurnoversPerMinute + (0.5*stdDevAwayNcaaAverageStealPercentage*turnoversPerMinuteStatistics.getStandardDeviation()))*estimatedMinutes*FANTASY_POINTS_PER_TURNOVER;
        //return estimatedTurnoversPerMinute*estimatedMinutes*FANTASY_POINTS_PER_TURNOVER;
    }


}
