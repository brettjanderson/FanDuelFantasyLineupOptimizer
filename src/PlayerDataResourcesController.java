import org.jsoup.nodes.Element;
import sun.rmi.runtime.Log;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Scanner;
import java.util.StringTokenizer;
import org.jsoup.*;

/**
 * Created by Brettness on 2/19/15.
 */
public class PlayerDataResourcesController {

    private static File espnIdsFile = new File("PlayerEspnIDs");
    private static Hashtable espnIdsTable = new Hashtable<String, LoggedPlayer>();
    private static Scanner espnIdsFileReader;
    private static BufferedWriter bufferedWriterToLogFile;

    public static String getPlayerEspnId(Player p) throws IOException {

        if(espnIdsTable.isEmpty()) initializeHashTable();

        LoggedPlayer playerLog = (LoggedPlayer) espnIdsTable.get(p.getName().replaceAll(" ", "-"));

        if(playerLog != null)
            return playerLog.getEspnId();
        else return null;
    }

    private static void initializeHashTable() throws IOException {

        espnIdsTable = new Hashtable<String, LoggedPlayer>();
        espnIdsFileReader  = new Scanner(espnIdsFile);
        StringTokenizer tokenizer;

        while(espnIdsFileReader.hasNextLine()){
            String name, position, espnId;
            String playerInfo = espnIdsFileReader.nextLine();
            tokenizer = new StringTokenizer(playerInfo);

            name = tokenizer.nextToken();
            position = tokenizer.nextToken();
            espnId = tokenizer.nextToken();

            espnIdsTable.put(name, new LoggedPlayer(name, position, espnId));

        }

    }

    public static void logPlayerAndEspnIdFromAddress(Player player, String gameLogAddress) throws IOException {
        if(espnIdsTable.isEmpty()) initializeHashTable();

        String[] gameLogAddressSplit = gameLogAddress.split("/");
        String nameWithDashes = player.getName().replace(" ", "-");
        String espnId = gameLogAddressSplit[gameLogAddressSplit.length-2];
        LoggedPlayer loggedPlayer = new LoggedPlayer(nameWithDashes, player.getPosition(), espnId);

        espnIdsTable.put(nameWithDashes, loggedPlayer);
        writeEntryToFile(loggedPlayer);
    }

    private static void writeEntryToFile(LoggedPlayer player) throws IOException {

        if(bufferedWriterToLogFile == null) bufferedWriterToLogFile = new BufferedWriter(new FileWriter("PlayerEspnIDs", true));
        bufferedWriterToLogFile.write(player.getName() + " " + player.getPosition() + " " + player.getEspnId());
        bufferedWriterToLogFile.newLine();
        bufferedWriterToLogFile.flush();

    }

    public static ArrayList<PlayerGameStats> getGameStatsForPlayer(Player player) throws IOException {

        ArrayList<PlayerGameStats> games = new ArrayList<PlayerGameStats>();
        String url = "http://espn.go.com/mens-college-basketball/player/gamelog/_/id/" + player.getEspnPlayerId();
        boolean connectionFailed = false;
        int connectionFailedNumber = 0;
        org.jsoup.nodes.Document gameLogPage = null;
        do {
            try {
                gameLogPage = Jsoup.connect(url).get();
            } catch (SocketTimeoutException s) {
                connectionFailed = true;
                connectionFailedNumber++;
                System.out.println(connectionFailedNumber);
            }
        } while (connectionFailed);

        org.jsoup.select.Elements tables = gameLogPage.getElementsByTag("tbody");
        org.jsoup.nodes.Element gameLogTable = tables.last();
        org.jsoup.select.Elements gameLogRows = null;

        try {
            gameLogRows = gameLogTable.getElementsByTag("tr");
        } catch (NullPointerException n) {
            System.out.println("null exeption for player: " + player.getName());
        }

        for (int i = 2; i < gameLogRows.size(); i++) {
            Element e = gameLogRows.get(i);
            org.jsoup.select.Elements cells = e.getElementsByTag("td");

            if (e.text().contains("Postponed") || cells.get(3).text().contains("Did not play")) continue;

            String site;
            String date = cells.get(0).text();
            String mins = cells.get(3).text();
            String rebounds = cells.get(10).text();
            String assists = cells.get(11).text();
            String blocks = cells.get(12).text();
            String steals = cells.get(13).text();
            String turnovers = cells.get(15).text();
            String points = cells.get(16).text();
            if (cells.get(1).text().contains("vs")) site = "home";
            else site = "away";

            System.out.println("added game for: " + player.getName());

            games.add(new PlayerGameStats(date, points, assists, rebounds, blocks, steals, site, turnovers, player));
        }
        return games;
    }
}
