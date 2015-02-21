import java.io.*;

/**
 * Created by Brettness on 2/19/15.
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
    private BufferedWriter writer;

    public PlayerGameStats(String date, String points, String assists, String rebounds,
                           String blocks, String steals, String site, String turnovers,
                           Player player) throws IOException {
        this.date = date;
        this.points = Double.parseDouble(points);
        this.assists = Double.parseDouble(assists);
        this.rebounds = Double.parseDouble(rebounds);
        this.blocks = Double.parseDouble(blocks);
        this.steals = Double.parseDouble(steals);
        this.turnovers = Double.parseDouble(turnovers);
        this.site = site;
        this.espnId = player.getEspnPlayerId();

        saveGameToFile();

    }

    public void saveGameToFile() throws IOException {
        File playerGamesFile = new File(espnId);


        if(!playerGamesFile.exists()){
            playerGamesFile.createNewFile();
        }

        writer = new BufferedWriter(new FileWriter(playerGamesFile));
        writer.flush();
        writer.write(espnId);
        writer.newLine();
        writer.write(date);
        writer.newLine();
        writer.write(site);
        writer.newLine();
        writer.write(String.valueOf(minutes));
        writer.newLine();
        writer.write(String.valueOf(points));
        writer.newLine();
        writer.write(String.valueOf(assists));
        writer.newLine();
        writer.write(String.valueOf(rebounds));
        writer.newLine();
        writer.write(String.valueOf(blocks));
        writer.newLine();
        writer.write(String.valueOf(steals));
        writer.newLine();
        writer.write(String.valueOf(turnovers));
        writer.newLine();
        writer.flush();
        writer.close();
    }

}
