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
public class AssistsAnalysis {

    private Player player;
    private ArrayList<PlayerGameStats> playerGameLogs;
    private SimpleRegression assistsPerMinuteRegression, minutesPerGameRegression;
    private DescriptiveStatistics assistsPerMinuteStatistics;
    private int gameNumber;
    private double estimatedAssistsPerMinute, estimatedMinutes;

    public AssistsAnalysis(Player player, ArrayList<PlayerGameStats> playerGameStats) {

        this.player = player;
        this.playerGameLogs = playerGameStats;

        assistsPerMinuteStatistics = new DescriptiveStatistics();
        assistsPerMinuteRegression = new SimpleRegression();
        minutesPerGameRegression = new SimpleRegression();

        gameNumber = 1;
        for(PlayerGameStats game: playerGameLogs) {
            assistsPerMinuteStatistics.addValue(game.getAssists() / game.getMinutes());
            assistsPerMinuteRegression.addData(gameNumber, game.getAssists() / game.getMinutes());
            minutesPerGameRegression.addData(gameNumber, game.getMinutes());
            gameNumber++;
        }

        estimatedAssistsPerMinute = Math.min(assistsPerMinuteStatistics.getMax(), assistsPerMinuteRegression.predict(gameNumber));
        estimatedMinutes = Math.min(40.0, minutesPerGameRegression.predict(gameNumber));

    }

    public double getEstimatedPointsFromAssists() throws IOException {

//        double stdDevsAwayFromNcaaAverageSos = NcaaTeamToTeamStats.getStandardDeviationsAwayFromMeanStrengthOfSchedule(player.getOpposingTeam());
        double stdDevsAwayFromNcaaAverageTeamDefense = NcaaTeamToTeamStats.getStandardDeviationsAwayFromMeanTeamDefense(player.getOpposingTeam());

        return ((estimatedAssistsPerMinute -(0.5*stdDevsAwayFromNcaaAverageTeamDefense*assistsPerMinuteStatistics.getStandardDeviation()))*estimatedMinutes)*1.5;
        //return estimatedAssistsPerMinute*estimatedMinutes*1.5;
    }

}
