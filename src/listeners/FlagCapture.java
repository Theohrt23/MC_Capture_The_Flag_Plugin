package listeners;

import messages.FlagCaptureMessage;
import org.bukkit.*;
import org.bukkit.event.Listener;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerMoveEvent;

import entity.Flag;
import org.bukkit.plugin.Plugin;
import util.FlagChange;
import util.TeamColors;

public class FlagCapture implements Listener {

    private final Flag flag;

    private final Plugin plugin;

    private final int captureRadius = 5;
    private int numPlayers = 0;
    private TeamColors capturingTeam = null;
    private boolean isCapturing = false;
    private int captureTime = 0;
    private final int captureTimeRequired = 100;

    public FlagCapture(Flag flag, Plugin plugin) {
        this.flag = flag;
        this.plugin = plugin;
        FlagChange.changeFlagColor(plugin,flag);
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
                    Bukkit.broadcastMessage(FlagCaptureMessage.getBeginCaptureMessage(capturingTeam,flag));
                    isCapturing = true;
                } else if (capturingTeam == playerTeam) {
                    numPlayers++;
                } else {
                    numPlayers--;
                }
            }
        } else {
            verifyIfPlayerCaptureThisFlag();
        }
    }

    public void checkCapturing() {
        if (isCapturing) {
            captureTime++;
            if (captureTime == 20 || captureTime == 40 || captureTime == 60 || captureTime == 80){
                Bukkit.broadcastMessage(FlagCaptureMessage.getTimeBeforeEndCaptureMessage(flag,captureTimeRequired - captureTime));
                if (captureTime == 20){
                    FlagChange.changeToFirstFlagState(plugin,flag);
                }
                if (captureTime == 40){
                    FlagChange.changeToSecondFlagState(plugin,flag);
                }
                if (captureTime == 60){
                    FlagChange.changeToThirdFlagState(plugin,flag);
                }
                if (captureTime == 80){
                    FlagChange.changeToFourFlagState(plugin,flag);
                }
            }
            if (captureTime >= captureTimeRequired) {
                flag.setColors(capturingTeam);
                Bukkit.broadcastMessage(FlagCaptureMessage.getCapturedFlagMessage(capturingTeam,flag));
                FlagChange.changeFlagColor(plugin,flag);
                capturingTeam = null;
                isCapturing = false;
                captureTime = 0;
            }
        }
    }

    private void verifyIfPlayerCaptureThisFlag(){
        if (capturingTeam != null) {
            numPlayers--;
            if (numPlayers == 0) {
                capturingTeam = null;
                Bukkit.broadcastMessage(FlagCaptureMessage.getStoppedCaptureMessage(flag));
                FlagChange.changeFlagColor(plugin,flag);
                isCapturing = false;
                captureTime = 0;
            }
        }
    }
}
