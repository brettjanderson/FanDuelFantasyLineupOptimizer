package Stats;

import FanDuelResources.Player;
import Stats.GameStatsAnalysis.MinutesEstimate;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by Brett Anderson.
 */
public class PlayerPointEstimator {


    public static double estimateFantasyPoints(ArrayList<PlayerGameStats> gameStats, Player player) throws IOException {

        //double estimate = 0.0;

//        AssistsAnalysis assistsEstimator = new AssistsAnalysis(player, gameStats);
//        PointsAnalysis pointsEstimator = new PointsAnalysis(player, gameStats);
//        ReboundsAnalysis reboundsEstimator = new ReboundsAnalysis(player, gameStats);
//        BlocksAnalysis blocksEstimator = new BlocksAnalysis(player, gameStats);
//        StealsAnalysis stealsEstimator = new StealsAnalysis(player, gameStats);
//        TurnoversAnalysis turnoversEstimator = new TurnoversAnalysis(player, gameStats);
//
//        estimate += assistsEstimator.getEstimatedPointsFromAssists();
//        estimate += pointsEstimator.getEstimatedPointsFromPoints();
//        estimate += reboundsEstimator.getEstimatedPointsFromRebounds();
//        estimate += blocksEstimator.getEstimatedPointsFromBlocks();
//        estimate += stealsEstimator.getEstimatedStealsPoints();
//        estimate += turnoversEstimator.getEstimatesPointsFromTurnovers();

        MinutesEstimate estimate = new MinutesEstimate(player, gameStats);

        return estimate.getEstimatedMinutes();
    }
}
