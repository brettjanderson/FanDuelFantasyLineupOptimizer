package Stats;

import org.apache.commons.math3.fitting.PolynomialCurveFitter;
import org.apache.commons.math3.fitting.WeightedObservedPoints;

import java.util.List;

/**
 * Created by Brettness on 2/24/15.
 */
public class SeasonStatsCurveFitTrend {

    private WeightedObservedPoints observedPoints;
    private PolynomialCurveFitter curveFitter;
    private double[] coeff;
    private int gameNumber;

    public SeasonStatsCurveFitTrend(List<PlayerGameStats> playerGameStats){

        observedPoints = new WeightedObservedPoints();
        curveFitter = PolynomialCurveFitter.create(4);
        gameNumber = 0;

        for(PlayerGameStats game: playerGameStats){
            observedPoints.add(gameNumber, game.getFantasyValueForGame());
            gameNumber++;
        }
    }

    public double getEstimate(){
        coeff = curveFitter.fit(observedPoints.toList());
        return coeff[0] + coeff[1]*(gameNumber) + coeff[2]*Math.pow(gameNumber,2)+ coeff[3]*Math.pow(gameNumber,3) +  coeff[4]*Math.pow(gameNumber,4);
    }
}
