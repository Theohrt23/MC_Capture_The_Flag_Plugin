package messages;

import entity.Flag;
import org.bukkit.ChatColor;
import util.TeamColors;

import static util.Utils.PLUGIN_PREFIX;

public class FlagCaptureMessage {

    public static String getBeginCaptureMessage(TeamColors capturingTeam, Flag flag){
        return PLUGIN_PREFIX + setNameFlagColors(capturingTeam) + " " + capturingTeam.name() + ChatColor.AQUA + " begin to capture the flag " + setNameFlagColors(flag.getColors()) + flag.getName() + ".";
    }

    public static String getCapturedFlagMessage(TeamColors capturingTeam, Flag flag){
        return PLUGIN_PREFIX + ChatColor.AQUA + " The flag " + setNameFlagColors(flag.getColors()) + flag.getName() + ChatColor.AQUA + " was capture by " + setNameFlagColors(capturingTeam) + capturingTeam.name() + ".";
    }

    public static String getStoppedCaptureMessage(Flag flag){
        return PLUGIN_PREFIX + ChatColor.AQUA + " The flag capture " + setNameFlagColors(flag.getColors()) + flag.getName() + ChatColor.AQUA + " is stopped.";
    }

    public static String getTimeBeforeEndCaptureMessage(Flag flag, int timeLeft) {
        return PLUGIN_PREFIX + timeLeft + ChatColor.AQUA + " seconds left before the capture of " + setNameFlagColors(flag.getColors()) + flag.getName() + ChatColor.AQUA + " flag.";
    }

    private static ChatColor setNameFlagColors(TeamColors teamColors){
        if (teamColors.equals(TeamColors.RED_TEAM)){
            return ChatColor.RED;
        }else {
            return ChatColor.BLUE;
        }
    }
}
