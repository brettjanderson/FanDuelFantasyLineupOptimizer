package Stats.GameStatsAnalysis;

import FanDuelResources.Player;
import Stats.PlayerGameStats;
import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;
import org.apache.commons.math3.stat.regression.SimpleRegression;

import java.util.ArrayList;

/**
 * Created by Brett Anderson.
 */
public class BlocksAnalysis {

    private Player player;
    private SimpleRegression blocksPerMinuteRegression, minutesPerGameRegression;
    private DescriptiveStatistics blocksPerMinuteStatistics;
    private int gameNumber;
    private double estimatedBlocksPerMinute, estimatedMinutes;

    public BlocksAnalysis(Player player, ArrayList<PlayerGameStats> playerGameLogs) {

        this.player = player;

        blocksPerMinuteRegression = new SimpleRegression();
        minutesPerGameRegression = new SimpleRegression();

        blocksPerMinuteStatistics = new DescriptiveStatistics();
        gameNumber = 1;

        for(PlayerGameStats game : playerGameLogs){

            blocksPerMinuteRegression.addData(gameNumber, game.getBlocks()/game.getMinutes());
            blocksPerMinuteStatistics.addValue(game.getBlocks()/game.getMinutes());
            minutesPerGameRegression.addData(gameNumber, game.getMinutes());
            gameNumber++;
        }

        estimatedBlocksPerMinute = Math.min(blocksPerMinuteStatistics.getMax(), blocksPerMinuteRegression.predict(gameNumber));
        estimatedMinutes = Math.min(40.0, minutesPerGameRegression.predict(gameNumber));

    }

    public double getEstimatedPointsFromBlocks(){

        return estimatedBlocksPerMinute*estimatedMinutes*2.0;

    }
}

