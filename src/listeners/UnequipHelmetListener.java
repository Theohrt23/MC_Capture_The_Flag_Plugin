package listeners;

import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerArmorStandManipulateEvent;
import org.bukkit.inventory.ItemStack;

import static util.Utils.PLUGIN_PREFIX;

public class UnequipHelmetListener implements Listener {

    @EventHandler
    public void onPlayerArmorStandManipulate(PlayerArmorStandManipulateEvent event) {
        ItemStack helmet = event.getPlayerItem();
        if (helmet != null && (helmet.getType() == Material.BLUE_WOOL || helmet.getType() == Material.RED_WOOL)) {
            event.setCancelled(true);
            event.getPlayer().sendMessage(PLUGIN_PREFIX + " You cannot remove your helmet!");
        }
    }

}
