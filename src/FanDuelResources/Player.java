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
public class Player implements Comparable<Player> {

    private String name;
    private int price;
    private String espnPlayerId;
    private String position;
    private double pointsPerGame;
    private ArrayList<PlayerGameStats> gameStats;
    private String teamNameAbbr;
    private String gameSite;
    private String teamName;
    private String opposingTeam;

    public Player(FanDuelPlayer player, String espnId, String teamName, String opposingTeam,
                  ArrayList<PlayerGameStats> playerGameStats) throws IOException {
        this.name = player.getName();
        this.price = player.getSalary();
        this.position = player.getPosition();
        this.teamNameAbbr = player.getTeamNameAbbr();
        this.espnPlayerId = espnId;
        this.gameSite = player.getGameSite();
        this.teamName = teamName;
        this.opposingTeam = opposingTeam;
        this.gameStats = playerGameStats;
    }

    public int getSalary(){
        return price;
    }

    public double getEstimatedPoints(){
        //TODO: Estimate this with the statistical tool.
        return pointsPerGame;
    }

    public ArrayList<PlayerGameStats> getGameStats(){
        return gameStats;
    }

    public String getPosition() {
        return position;
    }

    public String getName() {
        return name;
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
        return name.compareTo(otherPlayer.getName());
    }

    public String getTeamNameAbbr() {
        return teamNameAbbr;
    }

    public String getTeamName() {
        return teamName;
    }

    public String getOpposingTeam() {
        return opposingTeam;
    }

    public void setPointsPerGame(double pointsPerGame){
        this.pointsPerGame = pointsPerGame;
    }
}
