package Stats;

/**
 * Created by Brett Anderson.
 *
 * TeamStats class holds all of the advanced team stats
 * to be used for player fantasy output estimations.
 */
public class TeamStats {
    private String teamName;
    private int gamesPlayed;
    private int gamesWon;
    private int gamesLost;
    private double simpleRatingSystem;
    private double strengthOfSchedule;
    private int homeWins;
    private int homeLosses;
    private int awayWins;
    private int awayLosses;
    private int pointsScored;
    private int pointsScoredAgainst;
    private double pace;
    private double offensiveRating;
    private double freeThrowAttemptRate;
    private double threePointAttemptRate;
    private double trueShootingPercentage;
    private double totalReboundPercentage;
    private double assistPercentage;
    private double stealPercentage;
    private double blockPercentage;
    private double effectiveFieldGoalPercentage;
    private double turnOverPercentage;
    private double offensiveReboundPercentage;
    private double freeThrowsPerFieldGoalAttempt;

    public TeamStats(String teamName, int gamesPlayed, int gamesWon, int gamesLost, double simpleRatingSystem,
                     double strengthOfSchedule, int homeWins, int homeLosses, int awayWins, int awayLosses, int pointsScored,
                     int pointsScoredAgainst, double pace, double offensiveRating, double freeThrowAttemptRate,
                     double threePointAttemptRate, double trueShootingPercentage, double totalReboundPercentage,
                     double assistPercentage, double stealPercentage, double blockPercentage, double effectiveFieldGoalPercentage,
                     double turnOverPercentage, double offensiveReboundPercentage, double freeThrowsPerFieldGoalAttempt) {
        this.teamName = teamName;
        this.gamesPlayed = gamesPlayed;
        this.gamesWon = gamesWon;
        this.gamesLost = gamesLost;
        this.simpleRatingSystem = simpleRatingSystem;
        this.strengthOfSchedule = strengthOfSchedule;
        this.homeWins = homeWins;
        this.homeLosses = homeLosses;
        this.awayWins = awayWins;
        this.awayLosses = awayLosses;
        this.pointsScored = pointsScored;
        this.pointsScoredAgainst = pointsScoredAgainst;
        this.pace = pace;
        this.offensiveRating = offensiveRating;
        this.freeThrowAttemptRate = freeThrowAttemptRate;
        this.threePointAttemptRate = threePointAttemptRate;
        this.trueShootingPercentage = trueShootingPercentage;
        this.totalReboundPercentage = totalReboundPercentage;
        this.assistPercentage = assistPercentage;
        this.stealPercentage = stealPercentage;
        this.blockPercentage = blockPercentage;
        this.effectiveFieldGoalPercentage = effectiveFieldGoalPercentage;
        this.turnOverPercentage = turnOverPercentage;
        this.offensiveReboundPercentage = offensiveReboundPercentage;
        this.freeThrowsPerFieldGoalAttempt = freeThrowsPerFieldGoalAttempt;
    }

    public String getTeamName() {
        return teamName;
    }

    public int getGamesPlayed() {
        return gamesPlayed;
    }

    public int getGamesWon() {
        return gamesWon;
    }

    public int getGamesLost() {
        return gamesLost;
    }

    public double getSimpleRatingSystem() {
        return simpleRatingSystem;
    }

    public double getStrengthOfSchedule() {
        return strengthOfSchedule;
    }

    public int getHomeWins() {
        return homeWins;
    }

    public int getHomeLosses() {
        return homeLosses;
    }

    public int getAwayWins() {
        return awayWins;
    }

    public int getAwayLosses() {
        return awayLosses;
    }

    public int getPointsScored() {
        return pointsScored;
    }

    public int getPointsScoredAgainst() {
        return pointsScoredAgainst;
    }

    public double getPace() {
        return pace;
    }

    public double getOffensiveRating() {
        return offensiveRating;
    }

    public double getFreeThrowAttemptRate() {
        return freeThrowAttemptRate;
    }

    public double getThreePointAttemptRate() {
        return threePointAttemptRate;
    }

    public double getTrueShootingPercentage() {
        return trueShootingPercentage;
    }

    public double getTotalReboundPercentage() {
        return totalReboundPercentage;
    }

    public double getAssistPercentage() {
        return assistPercentage;
    }

    public double getStealPercentage() {
        return stealPercentage;
    }

    public double getBlockPercentage() {
        return blockPercentage;
    }

    public double getEffectiveFieldGoalPercentage() {
        return effectiveFieldGoalPercentage;
    }

    public double getTurnOverPercentage() {
        return turnOverPercentage;
    }

    public double getOffensiveReboundPercentage() {
        return offensiveReboundPercentage;
    }

    public double getFreeThrowsPerFieldGoalAttempt() {
        return freeThrowsPerFieldGoalAttempt;
    }

    public double getOpposingTeamPointsPerGame(){
        return pointsScoredAgainst/gamesPlayed;
    }
}
