import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Iterator;

/**
 * Created by Brett Anderson
 *
 * Performs a knapsack solver in order to get an optimum lineup
 * or roster for a given set of players.
 */
public class PlayerKnapSackAnalysisTool {

    public static long recursiveCalls = 0;
    public static int minimumSalary = 5000;
    /**
     * Method solves the roster knapsack problem.
     *
     * @param players List of available players
     * @return A RosterSolution.
     */
    public static RosterSolution getOptimumRoster(ArrayList<Player> players){

        removePlayersWithAverageFantasyPointsBelowValue(players, 21.0);
        return recursiveKnapSackSolution(players, 60000, null);

    }

    private static RosterSolution recursiveKnapSackSolution(ArrayList<Player> availablePlayers, int remainingMoney, RosterSolution solution){

        if(solution == null) solution = new RosterSolution();
        recursiveCalls++;
        //availablePlayers = (ArrayList<Player>) availablePlayers.clone();
        //solution = (RosterSolution) solution.clone();

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

    private static void removePlayersWithAverageFantasyPointsBelowValue(ArrayList<Player> playerList, double value) {

        Iterator<Player> iterator = playerList.iterator();

        while(iterator.hasNext()){
            Player player = iterator.next();

            if(player.getEstimatedPoints() < value)
                iterator.remove();
        }
    }
}
