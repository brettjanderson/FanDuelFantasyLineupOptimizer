import org.jsoup.*;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import java.io.IOException;
import java.net.SocketTimeoutException;
import java.util.ArrayList;

/**
 * Created by Brett Anderson
 *
 * The DataScraper class is responsible for gathering
 * pertinent data from ESPN and FanDuel.
 */
public class DataScraper {

    private static final String FANDUEL_PLAYER_LIST_TABLE_URL = "http://localhost:8888/rosterTable/";

    /**
     * Parses the FanDuel list of players for a particular league.
     * NOTE: As of now, this requires the copy and pasting of the table html into a localhost
     * server page.
     *
     * @return ArrayList of player objects from FanDuel league list of available players.
     * @throws IOException
     */
    public static ArrayList<Player> getFanDuelPlayerList() throws IOException {

        ArrayList<Player> playerList = new ArrayList<Player>();
        Document searchResultsPage = Jsoup.connect(FANDUEL_PLAYER_LIST_TABLE_URL).get();
        Elements playerTable = searchResultsPage.getElementsByTag("tbody");

        for(Element row : playerTable.get(0).children()){

            String name, position;
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

            if(pointsPerGame <= 0.0) continue; // Don't include players with less than or equal to 0.0 fantasy points per game

            playerList.add(new Player(name, price, pointsPerGame, position));

        }

        return playerList;

    }


    /**
     * Method to go search espn's website for a particular player's Game Log Url.
     *
     * @param player Player to get ESPN Game Log link for.
     * @return The ESPN Game Log Url
     * @throws IOException
     */
    public static String getGameLogLink(Player player) throws IOException {

        //Check to see if the Espn ID has been logged
        //Return the link if it has
        String espnId = PlayerDataResourcesController.getEspnPlayerId(player);
        if(espnId != null) return "http://espn.go.com/mens-college-basketball/player/gamelog/_/id/"+ espnId;

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

            if(!gameLogUrl.contains("mens-college-basketball"))  // If player isn't a college basketball player, add to not found list.
                ScrapingExceptionHandler.addPlayerNotFound(player);

            else
                PlayerDataResourcesController.logPlayerAndEspnIdFromAddress(player, gameLogUrl); //Log data for use later on.

        } catch (NullPointerException e){

            //Handles problems locating a particular players game log page.
            ScrapingExceptionHandler.addPlayerNotFound(player);
        }


        return gameLogUrl;
    }

    public static ArrayList<PlayerGameStats> getGameStatsForPlayer(Player player) throws IOException {

        ArrayList<PlayerGameStats> games = new ArrayList<PlayerGameStats>();
        String url = "http://espn.go.com/mens-college-basketball/player/gamelog/_/id/" + player.getEspnPlayerId();
        boolean connectionFailed = false;
        int connectionFailedNumber = 0;
        Document gameLogPage = null;
        do {
            try {
                gameLogPage = Jsoup.connect(url).get();
            } catch (SocketTimeoutException s) {
                connectionFailed = true;
                connectionFailedNumber++;
                System.out.println(connectionFailedNumber);
            }
        } while (connectionFailed);

        Elements tables = gameLogPage.getElementsByTag("tbody");
        Element gameLogTable = tables.last();
        Elements gameLogRows = null;

        try {
            gameLogRows = gameLogTable.getElementsByTag("tr");
        } catch (NullPointerException n) {
            System.out.println("null exeption for player: " + player.getName());
        }

        for (int i = 2; i < gameLogRows.size(); i++) {
            Element e = gameLogRows.get(i);
            Elements cells = e.getElementsByTag("td");

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
