import java.util.ArrayList;

/**
 * Created by Brettness on 2/19/15.
 */
public class RosterSolution {

    private ArrayList<Player> roster;
    private int numberOfForwards;
    private int numberOfGaurds;
    private int totalSalary;


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
}
