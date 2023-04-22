package listeners;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.scoreboard.Score;
public class DeathListener implements Listener {

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        Player victim = event.getEntity();
        Player killer = victim.getKiller();

        if (killer != null) {
            Score killerKillsScore = killer.getScoreboard().getObjective("game").getScore(ChatColor.YELLOW + "Kills:");
            killerKillsScore.setScore(killerKillsScore.getScore()+1);
        }

        Score victimDeathsScore = victim.getScoreboard().getObjective("game").getScore(ChatColor.YELLOW + "Deaths:");
        victimDeathsScore.setScore(victimDeathsScore.getScore()+1);
    }
}
