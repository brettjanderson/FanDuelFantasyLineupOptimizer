package Stats;

import java.io.BufferedWriter;
import java.io.IOException;

/**
 * Created by Brett Anderson.
 */
public class PlayerGameStats {

    private final String date;
    private double points;
    private double rebounds;
    private double minutes;
    private double assists;
    private double blocks;
    private double steals;
    private double turnovers;
    private String site;
    private String espnId;
    private String opponent;
    private BufferedWriter writer;

    public PlayerGameStats(String date, String opponent, String points, String assists, String rebounds,
                           String blocks, String steals, String site, String turnovers,
                           String espnId, String minutes) throws IOException {
        this.date = date;
        this.points = Double.parseDouble(points);
        this.assists = Double.parseDouble(assists);
        this.rebounds = Double.parseDouble(rebounds);
        this.blocks = Double.parseDouble(blocks);
        this.steals = Double.parseDouble(steals);
        this.turnovers = Double.parseDouble(turnovers);
        this.minutes = Double.parseDouble(minutes);
        this.site = site;
        this.opponent = opponent;
        this.espnId = espnId;
    }

    public double getAssists() {
        return assists;
    }

    public double getPoints() { return points; }

    public double getRebounds() { return rebounds; }

    public double getBlocks() { return blocks; }

    public double getSteals() { return steals; }

    public double getTurnovers() { return turnovers; }

    public double getFantasyValueForGame(){

        double fantasyOutput = 0;

        fantasyOutput += points;
        fantasyOutput += (rebounds*1.2);
        fantasyOutput += (assists*1.5);
        fantasyOutput += (blocks*2);
        fantasyOutput += (steals*2);
        fantasyOutput -= turnovers;

        return fantasyOutput;
    }

    public String getSite() {
        return site;
    }

    public double getMinutes() {
        return minutes;
    }
}
