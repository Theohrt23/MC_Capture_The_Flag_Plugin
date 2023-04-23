package messages;

import org.bukkit.ChatColor;

import static util.Utils.PLUGIN_PREFIX;

public class StickMessage {

    private static final String RED = ChatColor.RED + "red" + ChatColor.WHITE;

    private static final String BLUE = ChatColor.RED + "blue" + ChatColor.WHITE;

    public static String getChooseTeamColorMessage() {
        return PLUGIN_PREFIX + " Please choose team color:" + RED + " or " + BLUE + ". Type in the chat your answer. If you want to cancel type "
                + ChatColor.RED + "cancel" + ChatColor.WHITE + ".";
    }

    public static String getSelectRedSpawnPointMessage() {
        return PLUGIN_PREFIX + " Please define a spawn point. Begin with " + RED + " team. Click on a block with the stick.";
    }

    public static String getSelectBlueSpawnPointMessage() {
        return PLUGIN_PREFIX + " Done. Now set spawn point for " + BLUE + " team. Click on a block with the stick.";
    }

    public static String getDoneMessage() {
        return PLUGIN_PREFIX + " Done.";
    }

    public static String getChooseRedMessage() {
        return PLUGIN_PREFIX + " You choose " + RED + ".";
    }

    public static String getChooseBlueMessage() {
        return PLUGIN_PREFIX + " You choose " + BLUE + ".";
    }

    public static String getCancelFlagCreationMessage() {
        return PLUGIN_PREFIX + " You cancel the flag creation.";
    }

    public static String getErrorTypeMessage() {
        return PLUGIN_PREFIX + " Invalid team color. Please choose " + RED + " or " + BLUE + ".";
    }

    public static String getSelectNameMessage() {
        return PLUGIN_PREFIX + " Please choose a name for this flag. Type your answer in the chat.";
    }

    public static String getConfirmNameMessage(String name) {
        return PLUGIN_PREFIX + " You choose this name: " + ChatColor.AQUA + name + ChatColor.WHITE + ".";
    }
}
