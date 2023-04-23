package commands;

import com.sun.istack.internal.NotNull;
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
import util.TeamColors;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

import static util.Utils.PLUGIN_PREFIX;

public class StartCommand implements CommandExecutor {
    private final int COUNTDOWN_TIME = 3;
    private final int GAME_TIME = 30 * 60;

    private int red_point = 2500;

    private int blue_point = 2500;

    private int minute_count = 0;

    private final Plugin plugin;

    private final PluginManager pluginManager;

    private final File pluginFolder;

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
            List<FlagCapture> flagCaptureList = new ArrayList<>();

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

            for (Flag flag : flagList){
                System.out.println("initialize flag " + flag.getName() + " on " + flag.getWorld().getName() + " at " + flag.getX() + flag.getY() + flag.getZ() + " for the " + flag.getColors() + " team.");
                FlagCapture flagCapture = new FlagCapture(flag,plugin);
                flagCaptureList.add(flagCapture);
                pluginManager.registerEvents(flagCapture, plugin);
            }

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

                    for(FlagCapture flagCapture : flagCaptureList){
                        flagCapture.checkCapturing();
                    }

                    Score score = player.getScoreboard().getObjective("game").getScore(ChatColor.GREEN + "Minutes left:");
                    score.setScore(timeLeft / 60);

                    if(minute_count == 60){
                        for (Flag f : flagList){
                            if (f.getColors().equals(TeamColors.RED_TEAM)){
                                red_point -= 20;
                                if (red_point < 0) {
                                    red_point = 0;
                                }
                            }else {
                                blue_point -= 20;
                                if (blue_point < 0) {
                                    blue_point = 0;
                                }
                            }
                        }

                        Score scoreRed = player.getScoreboard().getObjective("game").getScore(ChatColor.RED + "Red points:");
                        scoreRed.setScore(red_point);

                        Score scoreBlue = player.getScoreboard().getObjective("game").getScore(ChatColor.BLUE + "Blue points:");
                        scoreBlue.setScore(blue_point);

                        if (red_point == 0){
                            Bukkit.broadcastMessage(ChatColor.AQUA + "[CAPTURE] The flag capture was win by " + ChatColor.RED + " RED_TEAM");
                            cancel();
                        }
                        if (blue_point == 0){
                            Bukkit.broadcastMessage(ChatColor.AQUA + "[CAPTURE] The flag capture was win by " + ChatColor.BLUE + " BLUE_TEAM");
                            cancel();
                        }
                        minute_count = 0;
                    }

                    minute_count++;
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

        Score scoreRed = obj.getScore(ChatColor.RED + "Red points:");
        scoreRed.setScore(red_point);

        Score scoreBlue = obj.getScore(ChatColor.BLUE + "Blue points:");
        scoreBlue.setScore(blue_point);

        Score score = obj.getScore(ChatColor.GREEN + "Minutes left:");
        score.setScore(GAME_TIME / 60);

        player.setScoreboard(scoreboard);
    }
}