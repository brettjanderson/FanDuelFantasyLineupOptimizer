package PlayerAnalysis; /**
 * Created by Brett Anderson.
 *
 * Roster Solution class is used to keep track of a
 * viable roster for evaluation during the knapsack
 * optimizer.
 */
import FanDuelResources.Player;

import java.util.ArrayList;
import java.util.Collections;

public class RosterSolution implements Comparable<RosterSolution> {

    private ArrayList<Player> roster;
    private int numberOfForwards;
    private int numberOfGaurds;
    private int totalSalary;
    private final int NEEDED_GUARDS = 4;
    private final int NEEDED_FORWARDS = 5;
    private double totalValue;


    public RosterSolution(){
        roster = new ArrayList<Player>();
        totalSalary = 0;
        numberOfGaurds = 0;
        numberOfForwards = 0;
    }

    public void addPlayer(Player player){

        if(player.getPosition().equals("G"))
            numberOfGaurds++;

        if(player.getPosition().equals("F"))
            numberOfForwards++;

        roster.add(player);
        totalSalary += player.getSalary();

    }

    public int getNumberOfForwards() {
        return numberOfForwards;
    }

    public int getNumberOfGaurds() {
        return numberOfGaurds;
    }

    public int getNumberOfPlayers(){
        return roster.size();
    }

    public double getTotalValue() {

        double value = 0.0;

        for(Player p: roster)
            value += p.getEstimatedPoints();

        return value;
    }

    public boolean isFilled(){
        return (numberOfForwards + numberOfGaurds) == (NEEDED_FORWARDS + NEEDED_GUARDS);
    }

    public int compareTo(RosterSolution otherSolution) {
        return (int) Math.round(getTotalValue() - otherSolution.getTotalValue());
    }

    public ArrayList<Player> getRoster(){
        return roster;
    }

    public boolean hasMaxForwards(){
        return numberOfForwards == NEEDED_FORWARDS;
    }

    public boolean hasMaxGuards(){
        return numberOfGaurds == NEEDED_GUARDS;
    }

    @Override
    public String toString() {

        String rosterString = "";

        for(Player player: roster){
            rosterString += player.toString() + "\n";
        }

        return rosterString;
    }

    public RosterSolution getCopy()  {

        RosterSolution copy = new RosterSolution();

        for(Player player: roster){
            copy.addPlayer(player);
        }

        return copy;
    }

    public String getHashMapKey(){
        String key = "";

        Collections.sort(roster);

        for(Player player : roster){
            key += (player.getEspnPlayerId() + "-");
        }

        key += ("-"+(60000-totalSalary));

        return key;

    }
}
