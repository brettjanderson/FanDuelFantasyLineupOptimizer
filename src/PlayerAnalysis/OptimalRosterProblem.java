package PlayerAnalysis;

import FanDuelResources.Player;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by Brett Anderson.
 */
public class OptimalRosterProblem {

    private ArrayList<Player> playersLeft;
    private int salaryLeft;
    private int forwardsNeeded;
    private int guardsNeeeded;
    private ArrayList<Player> solution;

    public OptimalRosterProblem(ArrayList<Player> playersLeft, int salaryLeft, int forwardsNeeded, int guardsNeeeded) {
        solution = new ArrayList<Player>();
        this.playersLeft = playersLeft;
        this.salaryLeft = salaryLeft;
        this.forwardsNeeded = forwardsNeeded;
        this.guardsNeeeded = guardsNeeeded;
    }

    @Override
    public String toString(){

        Collections.sort(playersLeft);

        StringBuilder sb = new StringBuilder();
        sb.append(salaryLeft);
        sb.append("-");
        sb.append(forwardsNeeded);
        sb.append(guardsNeeeded);
        sb.append("-");

        for(Player p : playersLeft){

            sb.append(p.getName());

        }
        return sb.toString();
    }

    public void setSolution(ArrayList<Player> solution){

        this.solution = solution;

    }

    public ArrayList<Player> getSolution(){
        return solution;
    }
}
