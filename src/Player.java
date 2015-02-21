import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by Brett Anderson
 *
 * Player class represents a player built specifically
 * from the FanDuel list.
 */
public class Player implements Comparable<Player> {

    private FanDuelFantasyLineupOptimizer stats;
    private String name;
    private int price;
    private String espnPlayerId;
    private String position;
    private double pointsPerGame;
    private ArrayList<PlayerGameStats> gameStats;
    private String teamNameAbbr;

    public Player(String name, int price, double pointsPerGame, String position, String teamNameAbbr) throws IOException {
        this.name = name;
        this.price = price;
        this.pointsPerGame = pointsPerGame;
        this.position = position;
        this.teamNameAbbr = teamNameAbbr;

        //TODO: Needs to be in another class. Decouple.
        this.espnPlayerId = PlayerDataResourcesController.getEspnPlayerId(this);
        //gameStats = DataScraper.getGameStatsForPlayer(this);
    }

    public int getSalary(){
        return price;
    }

    public double getEstimatedPoints(){
        //TODO: Estimate this with the statistical tool.
        return pointsPerGame;
    }

    public String getPosition() {
        return position;
    }

    public String getName() {
        return name;
    }

    public void setEspnPlayerId(String id){
        espnPlayerId = id;
    }

    public String getEspnPlayerId() {
        return espnPlayerId;
    }

    @Override
    public String toString(){
        return name + " " + position + " " +  pointsPerGame + " " + price;
    }

    @Override
    public int compareTo(Player otherPlayer) {
        return (int) Math.round(price/pointsPerGame - otherPlayer.getSalary()/otherPlayer.getEstimatedPoints());
    }

    public String getTeamNameAbbr() {
        return teamNameAbbr;
    }
}
