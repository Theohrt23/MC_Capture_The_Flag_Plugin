package util;

import entity.Flag;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.plugin.Plugin;

public class Utils {

    public static final String PLUGIN_NAME = "Capture_The_Flag";

    public static final String PLUGIN_PREFIX = "[§eCTF§f]";

    public static void changeFlagColor(Plugin plugin, Flag flag){
        Bukkit.getScheduler().runTask(plugin, () -> {
            World world = flag.getWorld();
            Location location = new Location(world,flag.getX(),flag.getY(),flag.getZ());
            world.strikeLightning(location);

            Material wool;
            if (flag.getColors().equals(TeamColors.RED_TEAM)){
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
