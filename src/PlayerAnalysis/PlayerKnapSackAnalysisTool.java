package PlayerAnalysis;

import FanDuelResources.Player;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

/**
 * Created by Brett Anderson
 *
 * Performs a knapsack solver in order to get an optimum lineup
 * or roster for a given set of players.
 */
public class PlayerKnapSackAnalysisTool {

    public static int minimumSalary = 4500;
    private static HashMap<String, OptimalRosterProblem> solutionHashMap;
    public static long recursiveCalls = 0;

    /**
     * Method solves the roster knapsack problem.
     *
     * @param players List of available players
     * @return A PlayerAnalysis.RosterSolution.
     */
    public static RosterSolution getOptimumRoster(ArrayList<Player> players){
        if(solutionHashMap == null)
            solutionHashMap = new HashMap<String, OptimalRosterProblem>();

        removePlayersWithEstimatedFantasyPointsBelowValue(players, 30.0);
        removePlayersWithNonNumberEstimates(players);
        //removePlayersWithAveragePricePerFantasyPointAboveValue(players, 350.0);
        return recursiveKnapSackSolution(players, 60000, null);

    }

    private static RosterSolution recursiveKnapSackSolution(ArrayList<Player> availablePlayers, int remainingMoney, RosterSolution solution){

        if(solution == null) solution = new RosterSolution();
        recursiveCalls++;

        if(recursiveCalls % 1009 == 0) System.out.print("Doing Knapsack ." + "\r");
        if(recursiveCalls % 2003 == 0) System.out.print("Doing Knapsack . ." + "\r");
        if(recursiveCalls % 3001 == 0) System.out.print("Doing Knapsack . . ." + "\r");

        if(solution.isFilled()) return solution;
        if(remainingMoney < minimumSalary) return new RosterSolution();
        if(availablePlayers.isEmpty()) return new RosterSolution();

        if(solution.hasMaxForwards())
            removePlayersWithPosition(availablePlayers, "F");

        if(solution.hasMaxGuards())
            removePlayersWithPosition(availablePlayers, "G");

        RosterSolution newRosterSolution = solution.getCopy();
        RosterSolution sameRosterSolution = solution.getCopy();

        ArrayList<Player> newPlayerList = (ArrayList<Player>) availablePlayers.clone();
        ArrayList<Player> samePlayerList = (ArrayList<Player>) availablePlayers.clone();


        Player playerTaken;
        if(!newPlayerList.isEmpty())
            playerTaken = removeFirstAffordablePlayer(newPlayerList, remainingMoney);
        else return new RosterSolution();

        if(playerTaken == null) return new RosterSolution();

        newRosterSolution.addPlayer(playerTaken);

        newRosterSolution = recursiveKnapSackSolution(newPlayerList, remainingMoney - playerTaken.getSalary(), newRosterSolution);

        samePlayerList.remove(0);
        sameRosterSolution = recursiveKnapSackSolution(samePlayerList, remainingMoney, sameRosterSolution);

        return findHigherValueSolution(sameRosterSolution, newRosterSolution);
    }

    private static RosterSolution recursiveKnapSackSolutionDP(ArrayList<Player> availablePlayers, int remainingMoney, RosterSolution solution){

        if(solution == null) solution = new RosterSolution();

        recursiveCalls++;

        if(solutionHashMap.containsKey(solution.getHashMapKey())){
            //return solutionHashMap.remove(solution.getHashMapKey());
        }

        if(solution.isFilled()){
            //solutionHashMap.put(solution.getHashMapKey(), solution);
            return solution;
        }

        if(remainingMoney < minimumSalary) return new RosterSolution();
        if(availablePlayers.isEmpty()) return new RosterSolution();

        if(solution.hasMaxForwards())
            removePlayersWithPosition(availablePlayers, "F");

        if(solution.hasMaxGuards())
            removePlayersWithPosition(availablePlayers, "G");

        RosterSolution newRosterSolution = solution.getCopy();
        RosterSolution sameRosterSolution = solution.getCopy();

        ArrayList<Player> newPlayerList = (ArrayList<Player>) availablePlayers.clone();
        ArrayList<Player> samePlayerList = (ArrayList<Player>) availablePlayers.clone();


        Player playerTaken;
        if(!newPlayerList.isEmpty())
            playerTaken = removeFirstAffordablePlayer(newPlayerList, remainingMoney);
        else return new RosterSolution();

        if(playerTaken == null) return new RosterSolution();

        newRosterSolution.addPlayer(playerTaken);
        String solutionKeyOne = newRosterSolution.getHashMapKey();
        newRosterSolution = recursiveKnapSackSolution(newPlayerList, remainingMoney - playerTaken.getSalary(), newRosterSolution);
        //solutionHashMap.put(solutionKeyOne, newRosterSolution);

        samePlayerList.remove(0);
        String solutionKeyTwo = sameRosterSolution.getHashMapKey();
        sameRosterSolution = recursiveKnapSackSolution(samePlayerList, remainingMoney, sameRosterSolution);
        //solutionHashMap.put(solutionKeyTwo, sameRosterSolution);

        return findHigherValueSolution(sameRosterSolution, newRosterSolution);
    }

    private static Player removeFirstAffordablePlayer(ArrayList<Player> playerList, int remainingMoney) {

        Iterator<Player> iterator = playerList.iterator();

        while(iterator.hasNext()){
            Player player = iterator.next();

            if(player.getSalary() < remainingMoney){
                iterator.remove();
                return player;
            }
            iterator.remove();
        }
        return null;
    }

    private static int findMinSalary(ArrayList<Player> playerList){
        int min = Integer.MAX_VALUE;

        for(Player p : playerList){
            if(p.getSalary() < min) min = p.getSalary();
        }
        return min;
    }

    private static void removePlayersWithPosition(ArrayList<Player> playerList, String position) {

        Iterator<Player> iterator = playerList.iterator();

        while(iterator.hasNext()){
            Player player = iterator.next();

            if(player.getPosition().equals(position))
                iterator.remove();
        }
    }

    private static RosterSolution findHigherValueSolution(RosterSolution one, RosterSolution two){

        if(one.getTotalValue() > two.getTotalValue()) return one;
        else return two;

    }

    private static void removePlayersWithEstimatedFantasyPointsBelowValue(ArrayList<Player> playerList, double value) {

        Iterator<Player> iterator = playerList.iterator();

        while(iterator.hasNext()){
            Player player = iterator.next();

            if(player.getEstimatedPoints() < value)
                iterator.remove();
        }
    }

    private static void removePlayersWithAveragePricePerFantasyPointAboveValue(ArrayList<Player> players, double value) {

        Iterator<Player> iterator = players.iterator();

        while(iterator.hasNext()){
            Player player = iterator.next();

            if(player.getSalary()/player.getEstimatedPoints() > value)
                iterator.remove();
        }
    }

    private static void removePlayersWithNonNumberEstimates(ArrayList<Player> players) {
        Iterator<Player> iterator = players.iterator();

        while(iterator.hasNext()){
            Player player = iterator.next();

            if(Double.isNaN(player.getEstimatedPoints()))
                iterator.remove();
        }
    }
}
