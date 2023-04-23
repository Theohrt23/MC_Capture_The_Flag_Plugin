package util;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.*;

public class CustomScoreboard {

    public static void createScoreboard(Player player, int red_point, int blue_point, int GAME_TIME) {
        ScoreboardManager manager = Bukkit.getScoreboardManager();

        Scoreboard scoreboard = manager.getNewScoreboard();

        Objective obj = scoreboard.registerNewObjective("game", "dummy", ChatColor.YELLOW + "CastleSiege");
        obj.setDisplaySlot(DisplaySlot.SIDEBAR);

        Score killsScore = obj.getScore(ChatColor.YELLOW + "Kills:");
        killsScore.setScore(0);

        Score deathsScore = obj.getScore(ChatColor.YELLOW + "Deaths:");
        deathsScore.setScore(0);

        Score scoreRed = obj.getScore(ChatColor.RED + "Red points:");
        scoreRed.setScore(red_point);

        Score scoreBlue = obj.getScore(ChatColor.BLUE + "Blue points:");
        scoreBlue.setScore(blue_point);

        Score score = obj.getScore(ChatColor.GREEN + "Minutes left:");
        score.setScore(GAME_TIME / 60);

        player.setScoreboard(scoreboard);
    }

    public static void updateScoreboardTimeLeft(Player player, int timeLeft){
        Score score = player.getScoreboard().getObjective("game").getScore(ChatColor.GREEN + "Minutes left:");
        score.setScore(timeLeft / 60);
    }

    public static void updateScoreboardTeamsPoint(Player player, int red_point, int blue_point){
        Score scoreRed = player.getScoreboard().getObjective("game").getScore(ChatColor.RED + "Red points:");
        scoreRed.setScore(red_point);

        Score scoreBlue = player.getScoreboard().getObjective("game").getScore(ChatColor.BLUE + "Blue points:");
        scoreBlue.setScore(blue_point);
    }
}
