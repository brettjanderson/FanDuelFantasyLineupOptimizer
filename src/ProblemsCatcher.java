import java.util.ArrayList;

/**
 * Created by Brettness on 2/19/15.
 */
public class ProblemsCatcher {

    private static ArrayList<Player> playersNotFound = new ArrayList<Player>();

    public static void addPlayerNotFound(Player p){

        playersNotFound.add(p);

    }

    public static void printPlayersNotFound() {

        for(Player p: playersNotFound) System.out.println(p.getName() + " " + p.getPosition());

    }
}
