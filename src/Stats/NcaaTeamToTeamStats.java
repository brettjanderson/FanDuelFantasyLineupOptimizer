package Stats;

import Data.DataScraper;
import org.apache.commons.math3.distribution.NormalDistribution;
import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;

import java.io.IOException;
import java.util.Hashtable;
import java.util.Set;

/**
 * Created by Brett Anderson.
 */
public class NcaaTeamToTeamStats {

    private static Hashtable<String, TeamStats> allTeamStats;
    private static DescriptiveStatistics strengthOfScheduleDescriptiveStatistics, teamDefenseDescriptiveStatistics;
    private static DescriptiveStatistics totalReboundingPercentageDescriptiveStatistics, teamTurnoversDescriptiveStatistics;
    private static DescriptiveStatistics stealPercentageDescriptiveStatistics;
    private static boolean initialized = false;

    public static void initNcaaTeamToTeamStats(Hashtable<String, TeamStats> allTeamStatsInit) {
        allTeamStats = allTeamStatsInit;
        strengthOfScheduleDescriptiveStatistics = new DescriptiveStatistics();
        teamDefenseDescriptiveStatistics = new DescriptiveStatistics();
        totalReboundingPercentageDescriptiveStatistics = new DescriptiveStatistics();
        teamTurnoversDescriptiveStatistics = new DescriptiveStatistics();
        stealPercentageDescriptiveStatistics = new DescriptiveStatistics();

        Set<String> allTeamStatsKeySet = allTeamStats.keySet();

        for(String key: allTeamStatsKeySet){
            strengthOfScheduleDescriptiveStatistics.addValue(allTeamStats.get(key).getStrengthOfSchedule());
            teamDefenseDescriptiveStatistics.addValue(allTeamStats.get(key).getOpposingTeamPointsPerGame());
            totalReboundingPercentageDescriptiveStatistics.addValue(allTeamStats.get(key).getTotalReboundPercentage());
            teamTurnoversDescriptiveStatistics.addValue(allTeamStats.get(key).getTurnOverPercentage());
            stealPercentageDescriptiveStatistics.addValue(allTeamStats.get(key).getStealPercentage());
        }

        initialized = true;
    }

    public static double getStandardDeviationsAwayFromMeanStrengthOfSchedule(String teamName) throws IOException {

        if(!initialized) initNcaaTeamToTeamStats(DataScraper.getAdvancedTeamStats());

        TeamStats teamStats = allTeamStats.get(teamName.toLowerCase());
        double sos = teamStats.getStrengthOfSchedule();
        double sosStdDev = strengthOfScheduleDescriptiveStatistics.getStandardDeviation();
        double averageSos = strengthOfScheduleDescriptiveStatistics.getMean();

        return (sos - averageSos) / sosStdDev;
    }

    public static double getStandardDeviationsAwayFromMeanTeamDefense(String teamName) throws IOException {

        if(!initialized) initNcaaTeamToTeamStats(DataScraper.getAdvancedTeamStats());

        TeamStats teamStats = allTeamStats.get(teamName.toLowerCase());
        double teamDefense = teamStats.getOpposingTeamPointsPerGame();
        double teamDefenseStdDev = teamDefenseDescriptiveStatistics.getStandardDeviation();
        double averageTeamDefense = teamDefenseDescriptiveStatistics.getMean();

        return (teamDefense - averageTeamDefense) / teamDefenseStdDev;
    }

    public static double getStandardDeviationsAwayFromMeanTotalReboundingPercentage(String teamName) throws IOException {

        if(!initialized) initNcaaTeamToTeamStats(DataScraper.getAdvancedTeamStats());

        TeamStats teamStats = allTeamStats.get(teamName.toLowerCase());
        double totalReboundingPercentage = teamStats.getTotalReboundPercentage();
        double totalRebStdDev = totalReboundingPercentageDescriptiveStatistics.getStandardDeviation();
        double averageTotalReboundingPercentage = totalReboundingPercentageDescriptiveStatistics.getMean();

        return (totalReboundingPercentage-averageTotalReboundingPercentage)/totalRebStdDev;

    }

    public static double getStandardDeviationsAwayFromMeanTurnoverPercentage(String teamName) throws IOException {

        if(!initialized) initNcaaTeamToTeamStats(DataScraper.getAdvancedTeamStats());

        TeamStats teamStats = allTeamStats.get(teamName.toLowerCase());

        double turnOverPercentage = teamStats.getTurnOverPercentage();
        double turnOverPercentageStdDev = teamTurnoversDescriptiveStatistics.getStandardDeviation();
        double averageTurnOverPercentage = teamTurnoversDescriptiveStatistics.getMean();

        return (turnOverPercentage-averageTurnOverPercentage)/turnOverPercentageStdDev;
    }

    public static double getStandardDeviationsAwayFromMeanStealPercentage(String teamName) throws IOException {

        if(!initialized) initNcaaTeamToTeamStats(DataScraper.getAdvancedTeamStats());

        TeamStats teamStats = allTeamStats.get(teamName.toLowerCase());

        double stealPercentage = teamStats.getStealPercentage();
        double averageStealPercentage = stealPercentageDescriptiveStatistics.getMean();
        double stealPercentageStdDev = stealPercentageDescriptiveStatistics.getStandardDeviation();

        return (stealPercentage-averageStealPercentage)/stealPercentageStdDev;

    }

    public static double getTeamDefensePercentile(String teamName) throws IOException {

        if(!initialized) initNcaaTeamToTeamStats(DataScraper.getAdvancedTeamStats());

        TeamStats teamStats = allTeamStats.get(teamName.toLowerCase());
        NormalDistribution normal = new NormalDistribution(teamDefenseDescriptiveStatistics.getMean(), teamDefenseDescriptiveStatistics.getStandardDeviation());
        double percentile = normal.cumulativeProbability(teamStats.getOpposingTeamPointsPerGame());

        return percentile;

    }

    public static double getStrengthOfSchedulePercentile(String teamName) throws IOException {

        if(!initialized) initNcaaTeamToTeamStats(DataScraper.getAdvancedTeamStats());

        TeamStats teamStats = allTeamStats.get(teamName.toLowerCase());
        NormalDistribution normal = new NormalDistribution(strengthOfScheduleDescriptiveStatistics.getMean(), strengthOfScheduleDescriptiveStatistics.getStandardDeviation());

        return normal.cumulativeProbability(teamStats.getStrengthOfSchedule());

    }

    public static double getTeamTotalReboundingPercentagePercentile(String teamName) throws IOException {

        if(!initialized) initNcaaTeamToTeamStats(DataScraper.getAdvancedTeamStats());

        TeamStats teamStats = allTeamStats.get(teamName.toLowerCase());
        NormalDistribution normal = new NormalDistribution(totalReboundingPercentageDescriptiveStatistics.getMean(), totalReboundingPercentageDescriptiveStatistics.getStandardDeviation());

        return normal.cumulativeProbability(teamStats.getTotalReboundPercentage());
    }

    public static double getTeamTurnoverPercentagePercentile(String teamName) throws IOException {

        if(!initialized) initNcaaTeamToTeamStats(DataScraper.getAdvancedTeamStats());

        TeamStats teamStats = allTeamStats.get(teamName.toLowerCase());
        NormalDistribution normal = new NormalDistribution();

        return normal.cumulativeProbability(teamStats.getTurnOverPercentage());
    }

    public static double getOppTeamStealPercentagePercentile(String teamName) throws IOException {

        if(!initialized) initNcaaTeamToTeamStats(DataScraper.getAdvancedTeamStats());

        TeamStats teamStats = allTeamStats.get(teamName.toLowerCase());
        NormalDistribution normal = new NormalDistribution(stealPercentageDescriptiveStatistics.getMean(), stealPercentageDescriptiveStatistics.getStandardDeviation());

        return normal.cumulativeProbability(teamStats.getStealPercentage());
    }
}
