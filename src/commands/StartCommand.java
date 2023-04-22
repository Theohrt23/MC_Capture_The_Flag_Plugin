package commands;

import entity.Flag;
import entity.Spawn;
import listeners.FlagCapture;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scoreboard.*;
import util.ReadYamlFiles;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

import static util.Utils.PLUGIN_PREFIX;

public class StartCommand implements CommandExecutor {
    private final int COUNTDOWN_TIME = 3;
    private final int GAME_TIME = 30 * 60;

    private Plugin plugin;

    private PluginManager pluginManager;

    private File pluginFolder;

    public StartCommand(Plugin plugin, File pluginFolder) {
        this.plugin = plugin;
        this.pluginFolder = pluginFolder;
        this.pluginManager = plugin.getServer().getPluginManager();
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = ((Player) sender).getPlayer();
            Spawn spawn;
            List<Flag> flagList;

            try {
                flagList = ReadYamlFiles.getFlagListByWorld(player.getWorld(), new File(this.pluginFolder, "flag_location.yml"));
            } catch (FileNotFoundException e) {
                throw new RuntimeException(e);
            }

            try {
                spawn = ReadYamlFiles.getSpawnByWorld(player.getWorld(), new File(this.pluginFolder, "spawn_location.yml"));
            } catch (FileNotFoundException e) {
                throw new RuntimeException(e);
            }

            pluginManager.registerEvents(new FlagCapture(), plugin);

            new BukkitRunnable() {
                int count = COUNTDOWN_TIME;

                @Override
                public void run() {
                    if (count > 0) {
                        player.sendMessage(PLUGIN_PREFIX + ChatColor.GREEN + " Game start in " + count + " secondes.");
                        count--;
                    } else {
                        player.sendMessage(PLUGIN_PREFIX + ChatColor.GREEN + " Game start now !");
                        this.cancel();

                        teleportPlayers(spawn);
                    }
                }
            }.runTaskTimer(plugin, 0L, 20L);

            new BukkitRunnable() {
                int timeLeft = GAME_TIME;

                @Override
                public void run() {
                    if (timeLeft == 0) {
                        cancel();
                        return;
                    }

                    Score score = player.getScoreboard().getObjective("game").getScore(ChatColor.GREEN + "Minutes left:");
                    score.setScore(timeLeft / 60);

                    timeLeft--;
                }
            }.runTaskTimer(plugin, 0L, 20L);

        }
        return true;
    }

    public void teleportPlayers(Spawn spawn) {
        List<Player> players = new ArrayList<>(Bukkit.getOnlinePlayers());

        List<Player> redTeam = new ArrayList<>();
        List<Player> blueTeam = new ArrayList<>();
        for (int i = 0; i < players.size(); i++) {
            if (i % 2 == 0) {
                redTeam.add(players.get(i));
            } else {
                blueTeam.add(players.get(i));
            }
        }

        Location destination = new Location(spawn.getWorld(), spawn.getXRedSpawn(), spawn.getYRedSpawn(), spawn.getZRedSpawn());
        for (Player p : redTeam) {
            p.teleport(destination);
            p.getInventory().setHelmet(new ItemStack(Material.RED_WOOL));
            createScoreboard(p);
        }

        destination = new Location(spawn.getWorld(), spawn.getXBlueSpawn(), spawn.getYBlueSpawn(), spawn.getZBlueSpawn());
        for (Player p : blueTeam) {
            p.teleport(destination);
            p.getInventory().setHelmet(new ItemStack(Material.BLUE_WOOL));
            createScoreboard(p);
        }
    }

    public void createScoreboard(Player player) {
        ScoreboardManager manager = Bukkit.getScoreboardManager();
        Scoreboard scoreboard = manager.getNewScoreboard();

        Objective obj = scoreboard.registerNewObjective("game", "dummy", ChatColor.YELLOW + "CastleSiege");
        obj.setDisplaySlot(DisplaySlot.SIDEBAR);

        Score killsScore = obj.getScore(ChatColor.YELLOW + "Kills:");
        killsScore.setScore(0);

        Score deathsScore = obj.getScore(ChatColor.YELLOW + "Deaths:");
        deathsScore.setScore(0);

        Score score = obj.getScore(ChatColor.GREEN + "Minutes left:");
        score.setScore(GAME_TIME / 60);

        player.setScoreboard(scoreboard);
    }
}