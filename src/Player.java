import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by Brett on 2/18/15.
 */
public class Player implements Comparable<Player> {

    private PlayerStats stats;
    private String name;
    private int price;
    private String espnPlayerId;
    private String position;
    private double pointsPerGame;
    private ArrayList<PlayerGameStats> gameStats;

    public Player(String name, int price, double pointsPerGame, String position) throws IOException {
        this.name = name;
        this.price = price;
        this.pointsPerGame = pointsPerGame;
        this.position = position;
        this.espnPlayerId = PlayerDataResourcesController.getPlayerEspnId(this);
        gameStats = PlayerDataResourcesController.getGameStatsForPlayer(this);
    }

    public int getSalary(){
        return price;
    }

    public String toString(){
        return name + " " + position + " " +  pointsPerGame + " " + price;
    }

    public double getEstimatedPoints(){
        //TODO: Better way to estimate this.
        return pointsPerGame;
    }

    @Override
    public int compareTo(Player otherPlayer) {
        return (int) Math.round(price/pointsPerGame - otherPlayer.getSalary()/otherPlayer.getEstimatedPoints());
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

}
