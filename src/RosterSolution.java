/**
 * Created by Brett Anderson.
 *
 * Roster Solution class is used to keep track of a
 * viable roster for evaluation during the knapsack
 * optimizer.
 */
import java.util.ArrayList;

public class RosterSolution {

    private ArrayList<Player> roster;
    private int numberOfForwards;
    private int numberOfGaurds;
    private int totalSalary;
    private final int NEEDED_GUARDS = 4;
    private final int NEEDED_FORWARDS = 5;


    public RosterSolution(){
        roster = new ArrayList<Player>();
        totalSalary = 0;
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

    @Override
    public String toString() {

        String rosterString = "";

        for(Player player: roster){
            rosterString += player.toString() + "\n";
        }

        return rosterString;
    }
}
