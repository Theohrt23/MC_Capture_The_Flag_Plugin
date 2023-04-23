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

    private static ChatColor setNameFlagColors(TeamColors teamColors){
        if (teamColors.equals(TeamColors.RED_TEAM)){
            return ChatColor.RED;
        }else {
            return ChatColor.BLUE;
        }
    }
}
