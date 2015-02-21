/**
 * Created by Brettness on 2/19/15.
 */

import org.jsoup.*;

import java.io.IOException;
import java.util.ArrayList;

public class DataScraper {

    public static ArrayList<Player> getPlayerList() throws IOException {

        String url = "http://localhost:8888/rosterTable/";
        ArrayList<Player> playerList = new ArrayList<Player>();
        org.jsoup.nodes.Document searchResultsPage = Jsoup.connect(url).get();
        org.jsoup.select.Elements playerTable = searchResultsPage.getElementsByTag("tbody");

        for(org.jsoup.nodes.Element row : playerTable.get(0).children()){

            String name, position;
            int price;
            double pointsPerGame;

            name = row.getElementsByAttributeValue("class", "player-name").text();
            if(name.contains("GTD")) name = name.substring(0, name.length()-3);
            if(name.endsWith("O")){
                //continue;
                name = name.substring(0,name.length()-1);
            }

            price = Integer.parseInt(row.getElementsByAttributeValue("class", "player-salary").text().substring(1).replaceAll(",",""));

            position = row.getElementsByAttributeValue("class", "player-position").text();
            pointsPerGame = Double.parseDouble(row.getElementsByAttributeValue("class", "player-fppg").text());

            playerList.add(new Player(name, price, pointsPerGame, position));

        }

        return playerList;

    }


    public static String getGameLogLink(Player player) throws IOException {

        //Check to see if the Espn ID has been logged
        //Return the link if it has
        String espnId = PlayerDataResourcesController.getPlayerEspnId(player);
        if(espnId != null) return "http://espn.go.com/mens-college-basketball/player/gamelog/_/id/"+ espnId;

        //Construct URL for ESPN search
        String url;
        String gameLogUrl = null;
        String [] names = player.getName().split(" ");
        url = "http://search.espn.go.com/" + names[0] + "-" + names[1];

        //Get Search Page
        org.jsoup.nodes.Document espnNameSearchPage = Jsoup.connect(url).get();

        //Find the link to the player's game log

        try {
            org.jsoup.select.Elements gameLogLinks = espnNameSearchPage.getElementsMatchingText("Game Log");
            gameLogUrl = gameLogLinks.last().attr("href");
            if(!gameLogUrl.contains("mens-college-basketball")) ProblemsCatcher.addPlayerNotFound(player);
            else PlayerDataResourcesController.logPlayerAndEspnIdFromAddress(player, gameLogUrl);

        } catch (NullPointerException e){
            ProblemsCatcher.addPlayerNotFound(player);
        }


        return gameLogUrl;
    }

}
