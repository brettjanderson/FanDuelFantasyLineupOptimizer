import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Hashtable;
import java.util.Scanner;
import java.util.StringTokenizer;

/**
 * Created by Brett Anderson
 *
 * PlayerDataResourcesController class handles the storage of player data
 * once it is scraped from ESPN.
 *
 */
public class PlayerDataResourcesController {

    private static File espnIdsFile = new File("PlayerEspnIDs");
    private static Hashtable espnIdsTable = new Hashtable<String, EspnPlayer>();
    private static Scanner espnIdsFileReader;
    private static BufferedWriter bufferedWriterToLogFile;

    public static String getEspnPlayerId(Player p) throws IOException {

        //Initialize the ESPN Player IDs Hash Table, if not initialized already.
        if(espnIdsTable.isEmpty()) initializeEspnPlayerIdsHashTable();

        EspnPlayer playerLog = (EspnPlayer) espnIdsTable.get(p.getName().replaceAll(" ", "-"));

        if(playerLog != null)
            return playerLog.getEspnId();
        else return null;
    }

    private static void initializeEspnPlayerIdsHashTable() throws IOException {

        espnIdsTable = new Hashtable<String, EspnPlayer>();
        espnIdsFileReader  = new Scanner(espnIdsFile);
        StringTokenizer tokenizer;

        while(espnIdsFileReader.hasNextLine()){
            String name, position, espnId;
            String playerInfo = espnIdsFileReader.nextLine();
            tokenizer = new StringTokenizer(playerInfo);

            name = tokenizer.nextToken();
            position = tokenizer.nextToken();
            espnId = tokenizer.nextToken();

            espnIdsTable.put(name, new EspnPlayer(name, position, espnId));

        }

    }

    public static void logPlayerAndEspnIdFromAddress(Player player, String gameLogAddress) throws IOException {
        if(espnIdsTable.isEmpty()) initializeEspnPlayerIdsHashTable();

        String[] gameLogAddressSplit = gameLogAddress.split("/");
        String nameWithDashes = player.getName().replace(" ", "-");
        String espnId = gameLogAddressSplit[gameLogAddressSplit.length-2];
        EspnPlayer espnPlayer = new EspnPlayer(nameWithDashes, player.getPosition(), espnId);

        espnIdsTable.put(nameWithDashes, espnPlayer);
        writeEntryToFile(espnPlayer);
    }

    private static void writeEntryToFile(EspnPlayer player) throws IOException {

        if(bufferedWriterToLogFile == null) bufferedWriterToLogFile = new BufferedWriter(new FileWriter("PlayerEspnIDs", true));
        bufferedWriterToLogFile.write(player.getName() + " " + player.getPosition() + " " + player.getEspnId());
        bufferedWriterToLogFile.newLine();
        bufferedWriterToLogFile.flush();

    }

}
