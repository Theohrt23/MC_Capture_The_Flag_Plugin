package listeners;

import org.bukkit.*;
import org.bukkit.event.Listener;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerMoveEvent;

import entity.Flag;
import org.bukkit.plugin.Plugin;
import util.TeamColors;

public class FlagCapture implements Listener {

    private final Flag flag;

    private final Plugin plugin;

    private final int captureRadius = 5;
    private int numPlayers = 0;
    private TeamColors capturingTeam = null;
    private boolean isCapturing = false;
    private int captureTime = 0;
    private int captureTimeRequired = 1800;

    public FlagCapture(Flag flag, Plugin plugin) {
        this.flag = flag;
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        Location playerLoc = player.getLocation();
        Location flagLoc = new Location(flag.getWorld(), flag.getX(), flag.getY(), flag.getZ());

        if (playerLoc.getWorld().equals(flag.getWorld()) && playerLoc.distance(flagLoc) <= captureRadius) {

            TeamColors playerTeam;
            if (player.getEquipment().getHelmet().getType().equals(Material.BLUE_WOOL)){
                playerTeam = TeamColors.BLUE_TEAM;
            }else {
                playerTeam = TeamColors.RED_TEAM;
            }

            if (playerTeam != null && playerTeam != flag.getColors()) {
                if (capturingTeam == null) {
                    capturingTeam = playerTeam;
                    Bukkit.broadcastMessage(ChatColor.AQUA + "[CAPTURE] " + capturingTeam.name() + ChatColor.WHITE + " begin to capture the flag " + flag.getColors().getColor() + flag.getName());
                    isCapturing = true;
                } else if (capturingTeam == playerTeam) {
                    numPlayers++;
                } else {
                    numPlayers--;
                }
            }
        } else {
            if (capturingTeam != null) {
                numPlayers--;
                if (numPlayers == 0) {
                    capturingTeam = null;
                    Bukkit.broadcastMessage(ChatColor.AQUA + "[CAPTURE] The flag capture " + flag.getColors().getColor() + flag.getName() + ChatColor.WHITE + " is stopped");
                    isCapturing = false;
                    captureTime = 0;
                }
            }
        }

        if (isCapturing) {
            captureTime++;
            int playersMultiplier = (int) (1 + (numPlayers * 0.15));
            if (captureTime >= captureTimeRequired / playersMultiplier) {
                flag.setColors(capturingTeam);
                Bukkit.broadcastMessage(ChatColor.AQUA + "[CAPTURE] The flag " + flag.getName() + ChatColor.WHITE + " was capture by " + capturingTeam.getColor() + capturingTeam.name());
                changeFlagColor(capturingTeam);
                capturingTeam = null;
                isCapturing = false;
                captureTime = 0;
            }
        }
    }

    public void changeFlagColor(TeamColors teamColors){
        Bukkit.getScheduler().runTask(plugin, () -> {
            World world = flag.getWorld();

            Material wool;
            if (teamColors.equals(TeamColors.RED_TEAM)){
                wool = Material.RED_WOOL;
            }else {
                wool = Material.BLUE_WOOL;
            }

            world.getBlockAt(flag.getX()+1, flag.getY()+6, flag.getZ()).setType(wool);
            world.getBlockAt(flag.getX()+2, flag.getY()+6, flag.getZ()).setType(wool);
            world.getBlockAt(flag.getX()+3, flag.getY()+6, flag.getZ()).setType(wool);
            world.getBlockAt(flag.getX()+4, flag.getY()+6, flag.getZ()).setType(wool);
            world.getBlockAt(flag.getX()+1, flag.getY()+7, flag.getZ()).setType(wool);
            world.getBlockAt(flag.getX()+2, flag.getY()+7, flag.getZ()).setType(wool);
            world.getBlockAt(flag.getX()+3, flag.getY()+7, flag.getZ()).setType(wool);
            world.getBlockAt(flag.getX()+4, flag.getY()+7, flag.getZ()).setType(wool);
        });
    }
}
