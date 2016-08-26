package Exceptions;

import FanDuelResources.FanDuelPlayer;

import java.util.ArrayList;

/**
 * Created by Brett Anderson.
 *
 * The Exceptions.ScrapingExceptionHandler class helps to
 * log and handle problems with scraping data on FanDuel or
 * ESPN.
 */
public class ScrapingExceptionHandler {

    private static ArrayList<FanDuelPlayer> playersNotFound = new ArrayList<FanDuelPlayer>();
    private static ArrayList<String> teamNamesNotFound = new ArrayList<String>();

    public static void addPlayerNotFound(FanDuelPlayer p){
        playersNotFound.add(p);
    }

    public static void printPlayersNotFound() {
        for(FanDuelPlayer p: playersNotFound) System.out.println(p.getName() + " " + p.getPosition() + " " + p.getTeamNameAbbr());
    }

    public static void addTeamNameNotFound(String teamNameAbbr) {
        teamNamesNotFound.add(teamNameAbbr);
    }

    public static ArrayList<FanDuelPlayer> getPlayersNotFound() {
        return playersNotFound;
    }

    public static ArrayList<String> getTeamNamesNotFound() {
        return teamNamesNotFound;
    }
}
