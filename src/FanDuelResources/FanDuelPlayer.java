package FanDuelResources;

import Stats.PlayerGameStats;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by Brett Anderson
 *
 * FanDuelResources.Player class represents a player built specifically
 * from the FanDuel list.
 */
public class FanDuelPlayer implements Comparable<FanDuelPlayer> {

    private String opposingTeamAbbr;
    private String name;
    private int price;
    private String position;
    private double pointsPerGame;
    private ArrayList<PlayerGameStats> gameStats;
    private String teamNameAbbr;
    private String gameSite;

    public FanDuelPlayer(String name, int price, double pointsPerGame, String position, String teamNameAbbr, String gameSite, String opposingTeamAbbr) throws IOException {
        this.name = name;
        this.price = price;
        this.pointsPerGame = pointsPerGame;
        this.position = position;
        this.teamNameAbbr = teamNameAbbr;
        this.gameSite = gameSite;
        this.opposingTeamAbbr = opposingTeamAbbr;
    }

    public int getSalary(){
        return price;
    }

    public double getAveragePointsPerGame(){ return pointsPerGame; }

    public String getPosition() {
        return position;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString(){ return name + " " + position + " " +  pointsPerGame + " " + price + " " + gameSite; }

    @Override
    public int compareTo(FanDuelPlayer otherPlayer) {
        //return Double.compare(price / pointsPerGame, otherPlayer.getSalary() / otherPlayer.getAveragePointsPerGame());
        return name.compareTo(otherPlayer.getName());
    }

    public String getTeamNameAbbr() {
        return teamNameAbbr;
    }

    public String getGameSite() { return gameSite; }

    public String getOpposingTeamAbbr() {
        return opposingTeamAbbr;
    }
}