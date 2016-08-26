package Data;

import Exceptions.ScrapingExceptionHandler;
import FanDuelResources.FanDuelPlayer;
import Stats.PlayerGameStats;
import Stats.TeamStats;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.Hashtable;

/**
 * Created by Brett Anderson
 *
 * The Data.DataScraper class is responsible for gathering
 * pertinent data from ESPN and FanDuel.
 */
public class DataScraper {

    private static final String FANDUEL_PLAYER_LIST_TABLE_URL = "http://localhost:8888/rosterTable/";
    private static Hashtable<String, TeamStats> advancedTeamStats;

    /**
     * Parses the FanDuel list of players for a particular league.
     * NOTE: As of now, this requires the copy and pasting of the table html into a localhost
     * server page.
     *
     * @return ArrayList of player objects from FanDuel league list of available players.
     * @throws IOException
     */
    public static ArrayList<FanDuelPlayer> getFanDuelPlayerList() throws IOException, InterruptedException {

        ArrayList<FanDuelPlayer> playerList = new ArrayList<FanDuelPlayer>();
        //Document searchResultsPage = Jsoup.connect(FANDUEL_PLAYER_LIST_TABLE_URL).get();
        ManualDataEntryLong fanDuelList = new ManualDataEntryLong("FanDuel Html", "Insert HTML");
        Document searchResultsPage = Jsoup.parse(fanDuelList.getEnteredInformation());
        fanDuelList.closeWindow();
        Elements playerTable = searchResultsPage.getElementsByTag("tbody");

        for(Element row : playerTable.get(0).children()){

            String name, position, teamAbbr, site;
            int price;
            double pointsPerGame;

            name = row.getElementsByAttributeValue("class", "player-name").text();
            if(name.contains("GTD")) name = name.substring(0, name.length()-3);
            if(name.endsWith("O")){
                continue; // Don't include the players who are out.
                //name = name.substring(0,name.length()-1);
            }

            price = Integer.parseInt(row.getElementsByAttributeValue("class", "player-salary").text().substring(1).replaceAll(",",""));

            position = row.getElementsByAttributeValue("class", "player-position").text();
            pointsPerGame = Double.parseDouble(row.getElementsByAttributeValue("class", "player-fppg").text());
            teamAbbr = row.getElementsByTag("b").text();
            site = row.getElementsByAttributeValue("class", "player-fixture").text();

            boolean home = site.contains("@"+teamAbbr);
            boolean away = site.contains(teamAbbr + "@");

            String opposingTeam;

            if(home) opposingTeam = site.replace("@"+teamAbbr, "");
            else opposingTeam = site.replace(teamAbbr+"@", "");

            if(pointsPerGame <= 0.0) continue; // Don't include players with less than or equal to 0.0 fantasy points per game

            if(away) site = "away";
            if(home) site = "home";

            playerList.add(new FanDuelPlayer(name, price, pointsPerGame, position, teamAbbr, site, opposingTeam));

        }

        return playerList;

    }

    /**
     * Method to go search espn's website for a particular player's Game Log Url.
     *
     * @param player FanDuelResources.Player to get ESPN Game Log link for.
     * @return The ESPN Game Log Url
     * @throws IOException
     */
    public static String getGameLogLink(FanDuelPlayer player) throws IOException, InterruptedException {

        //Check to see if the Espn ID has been logged
        //Return the link if it has
        String espnId = PlayerDataResourcesController.getEspnPlayerId(player);
        if(espnId != null) return "http://espn.go.com/mens-college-basketball/player/gamelog/_/id/"+ espnId;
        else {
            //searchForEspnId(player);
        }
        return null;
    }

    public static String searchForEspnId(FanDuelPlayer player) throws IOException, InterruptedException {

//        String loggedId = PlayerDataResourcesController.getEspnPlayerId(player);
//        if(loggedId != null) return loggedId;

        //Construct URL for ESPN search
        String url;
        String gameLogUrl = null;
        String [] names = player.getName().split(" ");
        url = "http://search.espn.go.com/" + names[0] + "-" + names[1];

        //Get Search Page
        Document espnNameSearchPage = Jsoup.connect(url).get();

        //Find the link to the player's game log
        //TODO: Figure out a better way to handle ESPN search issues.
        try {

            Elements gameLogLinks = espnNameSearchPage.getElementsMatchingText("Game Log");
            gameLogUrl = gameLogLinks.last().attr("href");

            if(!gameLogUrl.contains("mens-college-basketball")){
                ScrapingExceptionHandler.addPlayerNotFound(player);// If player isn't a college basketball player, add to not found list.
                return null;
            }
            else
                PlayerDataResourcesController.logPlayerAndEspnIdFromAddress(player, gameLogUrl); //Log data for use later on.

        } catch (NullPointerException e){
            //Handles problems locating a particular players game log page.
            ScrapingExceptionHandler.addPlayerNotFound(player);
            return null;
        }

        return PlayerDataResourcesController.getEspnPlayerId(player);
    }

    public static ArrayList<PlayerGameStats> getGameStatsForPlayer(String espnId, String playerName) throws IOException {

        System.out.print("Getting Game Stats For: " + playerName + " " + espnId + "\r");
        ArrayList<PlayerGameStats> games = new ArrayList<PlayerGameStats>();
        String url = new StringBuilder().append("http://espn.go.com/mens-college-basketball/player/gamelog/_/id/").append(espnId).toString();
        boolean connectionFailed = false;
        int connectionFailedNumber = 0;
        Document gameLogPage = null;

        do {
            try {
                gameLogPage = Jsoup.connect(url).get();
                connectionFailed = false;
            } catch (SocketTimeoutException s) {
                connectionFailed = true;
                connectionFailedNumber++;
                System.out.println("Trying to connect to: " + playerName + " stats page." );
            }
        } while (connectionFailed);

        Elements tables = gameLogPage.getElementsByTag("tbody");

        if(tables.size() == 0){
            return new ArrayList<PlayerGameStats>(); //TODO: Handle this a different way.
        }

        Element gameLogTable = tables.last();
        Elements gameLogRows = null;

        try {
            gameLogRows = gameLogTable.getElementsByTag("tr");
        } catch (NullPointerException n) {
            System.out.println("null exception for player: " + playerName);
        }

        for (int i = 2; i < gameLogRows.size(); i++) {
            Element e = gameLogRows.get(i);
            Elements cells = e.getElementsByTag("td");
            Elements links = cells.get(1).getElementsByTag("a");
            String opponent = null;

            if (e.text().contains("Postponed") || cells.get(3).text().contains("Did not play")) continue;

            String site;
            String date = cells.get(0).text();
            if(links.size() > 1) opponent = links.get(1).text();
            String mins = cells.get(3).text();
            String rebounds = cells.get(10).text();
            String assists = cells.get(11).text();
            String blocks = cells.get(12).text();
            String steals = cells.get(13).text();
            String turnovers = cells.get(15).text();
            String points = cells.get(16).text();
            if (cells.get(1).text().contains("vs")) site = "home";
            else site = "away";

            //System.out.println("added game for: " + player.getName());

            games.add(new PlayerGameStats(date, opponent, points, assists, rebounds, blocks, steals, site, turnovers, espnId, mins));
        }
        return games;
    }

    public static Hashtable<String,TeamStats> getAdvancedTeamStats() throws IOException {

        if(advancedTeamStats != null){
            return advancedTeamStats;
        }

        advancedTeamStats = new Hashtable<String, TeamStats>();

        String advancedStatsUrl = "http://www.sports-reference.com/cbb/seasons/2015-advanced-school-stats.html";
        Document advancedStatsPage = Jsoup.connect(advancedStatsUrl).get();

        Elements table = advancedStatsPage.getElementsByTag("tbody");
        Element tableBody = table.get(0);
        Elements tableRows = tableBody.getElementsByTag("tr");

        for(Element row: tableRows){

            Elements cellData = row.getElementsByTag("td");

            if(cellData.size() == 0) continue;

            String teamName = cellData.get(1).text().replaceAll(" ", "-").replace("(", "")
                    .replace(")", "").replace(".", "").replace("'", "").replace("&", "");

            int gamesPlayed = Integer.parseInt(cellData.get(2).text());
            int gamesWon = Integer.parseInt(cellData.get(3).text());
            int gamesLost = Integer.parseInt(cellData.get(4).text());
            double simpleRating = Double.parseDouble(cellData.get(6).text());
            double sos = Double.parseDouble(cellData.get(7).text());
            int homeWins = Integer.parseInt(cellData.get(10).text());
            int homeLosses = Integer.parseInt(cellData.get(11).text());
            int awayWins = Integer.parseInt(cellData.get(12).text());
            int awayLosses = Integer.parseInt(cellData.get(13).text());
            int pointsScored = Integer.parseInt(cellData.get(14).text());
            int oppScoredPoints = Integer.parseInt(cellData.get(15).text());
            double pace = Double.parseDouble(cellData.get(17).text());
            double offRating = Double.parseDouble(cellData.get(18).text());
            double freeThrowAttRate = Double.parseDouble(cellData.get(19).text());
            double threePointAttRate = Double.parseDouble(cellData.get(20).text());
            double trueShootingPercentage = Double.parseDouble(cellData.get(21).text());
            double totalReboundPercentage = Double.parseDouble(cellData.get(22).text())/100;
            double assistPercentage = Double.parseDouble(cellData.get(23).text())/100;
            double stealPercentage = Double.parseDouble(cellData.get(24).text())/100;
            double blockPercentage = Double.parseDouble(cellData.get(25).text())/100;
            double effectiveFieldGoalPercentage = Double.parseDouble(cellData.get(26).text());
            double turnOverPercentage = Double.parseDouble(cellData.get(27).text())/100;
            double offensiveReboundPercentage = Double.parseDouble(cellData.get(28).text())/100;
            double freeThrowsPerFieldGoalAttempt = Double.parseDouble(cellData.get(29).text());

            TeamStats teamStats = new TeamStats(teamName,gamesPlayed, gamesWon, gamesLost, simpleRating,
                    sos, homeWins, homeLosses, awayWins, awayLosses, pointsScored, oppScoredPoints, pace,
                    offRating, freeThrowAttRate, threePointAttRate, trueShootingPercentage, totalReboundPercentage,
                    assistPercentage, stealPercentage, blockPercentage, effectiveFieldGoalPercentage, turnOverPercentage,
                    offensiveReboundPercentage, freeThrowsPerFieldGoalAttempt);

            advancedTeamStats.put(teamStats.getTeamName().toLowerCase(), teamStats);
        }

        return advancedTeamStats;
    }
}
