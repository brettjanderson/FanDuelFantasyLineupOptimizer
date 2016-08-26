package Data;

import EspnResources.EspnPlayer;
import FanDuelResources.FanDuelPlayer;

import java.io.*;
import java.util.Hashtable;
import java.util.Scanner;
import java.util.StringTokenizer;

/**
 * Created by Brett Anderson
 *
 * Data.PlayerDataResourcesController class handles the storage of player data
 * once it is scraped from ESPN and Sports-Reference.
 */
public class PlayerDataResourcesController {

    private static Hashtable<Integer, String> teamAbbrTable = new Hashtable<Integer, String>();
    private static Hashtable<String, EspnPlayer> espnIdsTable = new Hashtable<String, EspnPlayer>();
    private static Hashtable<String, String> espnToSrTable = new Hashtable<String, String>();

    private static File espnIdsFile = new File("PlayerEspnIDs");
    private static File teamAbbrFile = new File("TeamAbbreviationsTable");
    private static File espnToSrTeamNameFile = new File("EspnToSportsReferenceTeamNameLookup");

    private static Scanner espnIdsFileReader;
    private static Scanner teamAbbrTableScanner;
    private static Scanner espnToSrScanner;

    private static BufferedWriter bufferedWriterToAbbrFile;
    private static BufferedWriter bufferedWriterToLogFile;
    private static BufferedWriter bufferedWriterToEspnToSrFile;

    public static String getEspnPlayerId(FanDuelPlayer p) throws IOException, InterruptedException {

        //Initialize the ESPN FanDuelResources.Player IDs Hash Table, if not initialized already.
        if(espnIdsTable.isEmpty()) initializeEspnPlayerIdsHashTable();

        EspnPlayer playerLog = (EspnPlayer) espnIdsTable.get(p.getName().replaceAll(" ", "-"));
        String espnId;

        if(playerLog != null)
            return playerLog.getEspnId();
        else{
            espnId = DataScraper.searchForEspnId(p);
            if(espnId == null){
                ManualDataEntry manualDataEntry = new ManualDataEntry(p.getName(), "Espn Player Id ---- Team : " + p.getTeamNameAbbr());
                espnId = manualDataEntry.getEnteredInformation();
                manualDataEntry.closeWindow();
                writeEntryToFile(new EspnPlayer(p.getName(), p.getPosition(), espnId));
            }
        }
        return espnId;
    }

    private static void initializeEspnPlayerIdsHashTable() throws IOException {

        espnIdsTable = new Hashtable<String, EspnPlayer>(2000, 0.80f);
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

    public static void logPlayerAndEspnIdFromAddress(FanDuelPlayer player, String gameLogAddress) throws IOException {
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

    public static String getTeamName(String teamAbbr) throws IOException, InterruptedException  {

        if(teamAbbrTable == null || teamAbbrTable.isEmpty()) initializeTeamAbbrTable();

        String teamName = null;

        if(teamAbbrTable.containsKey(teamAbbr.hashCode()))
            teamName = teamAbbrTable.get(teamAbbr.hashCode());
        else{
            ManualDataEntry dataEntry = new ManualDataEntry(teamAbbr, "Sports Reference Team Name Format");
            teamName = dataEntry.getEnteredInformation();
            dataEntry.closeWindow();
            writeTeamAbbrAndTeamNameToFile(teamAbbr, teamName);
        }

        return teamName;
    }

    private static void initializeTeamAbbrTable() throws FileNotFoundException {

        teamAbbrTable = new Hashtable<Integer, String>();
        teamAbbrTableScanner = new Scanner(teamAbbrFile);
        StringTokenizer tokenizer;

        while(teamAbbrTableScanner.hasNextLine()) {
            String abbr, teamName;
            String playerInfo = teamAbbrTableScanner.nextLine();
            tokenizer = new StringTokenizer(playerInfo);

            abbr = tokenizer.nextToken();
            teamName = tokenizer.nextToken();

            teamAbbrTable.put(abbr.hashCode(), teamName);
        }
    }

    public static void writeTeamAbbrAndTeamNameToFile(String abbr, String teamName) throws IOException {

        if(teamAbbrTable == null || teamAbbrTable.isEmpty()) initializeTeamAbbrTable();

        if(teamAbbrTable.containsKey(abbr.hashCode())) return;

        if(bufferedWriterToAbbrFile == null)  bufferedWriterToAbbrFile = new BufferedWriter(new FileWriter("TeamAbbreviationsTable", true));

        bufferedWriterToAbbrFile.newLine();
        bufferedWriterToAbbrFile.write(abbr + " " + teamName);
        bufferedWriterToAbbrFile.flush();

        teamAbbrTable.put(abbr.hashCode(), abbr);
    }

    public static String getSrTeamName(String espnTeamName) throws IOException, InterruptedException {

        if(espnToSrTable == null || espnToSrTable.isEmpty()) initializeEspnToSrTable();

        String srTeamName = null;

        if(espnToSrTable.containsKey(espnTeamName))
            srTeamName = espnToSrTable.get(espnTeamName);
        else{
            ManualDataEntry manualDataEntry = new ManualDataEntry(espnTeamName, "Sports Reference Team Name From Espn Team Name");
            srTeamName = manualDataEntry.getEnteredInformation();
            manualDataEntry.closeWindow();
            writeEspnAndSrTeamNameToFile(espnTeamName, srTeamName);
        }

        return srTeamName;
    }

    private static void writeEspnAndSrTeamNameToFile(String espnTeamName, String srTeamName) throws IOException {

        if(espnToSrTable == null || espnToSrTable.isEmpty()) initializeEspnToSrTable();

        if(espnToSrTable.containsKey(espnTeamName)) return;

        if(bufferedWriterToEspnToSrFile == null) bufferedWriterToEspnToSrFile = new BufferedWriter(new FileWriter("EspnToSportsReferenceTeamNameLookup", true));

        bufferedWriterToEspnToSrFile.newLine();
        bufferedWriterToEspnToSrFile.write(espnTeamName + " " + srTeamName);
        bufferedWriterToEspnToSrFile.flush();

    }

    private static void initializeEspnToSrTable() throws FileNotFoundException {

        espnToSrTable = new Hashtable<String, String>();
        espnToSrScanner = new Scanner(espnIdsFile);
        StringTokenizer tokenizer;

        while(espnToSrScanner.hasNextLine()){
            String espnName, srTeamName;
            String line = espnToSrScanner.nextLine();
            tokenizer = new StringTokenizer(line);

            espnName = tokenizer.nextToken();
            srTeamName = tokenizer.nextToken();

            espnToSrTable.put(espnName, srTeamName);
        }



    }

    public static void closeWriters() throws IOException {

        if(bufferedWriterToAbbrFile != null) bufferedWriterToAbbrFile.close();
        if(bufferedWriterToEspnToSrFile != null) bufferedWriterToEspnToSrFile.close();
        if(bufferedWriterToLogFile != null) bufferedWriterToLogFile.close();

    }

}
