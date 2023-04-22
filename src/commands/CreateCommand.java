package commands;

import items.StickItem;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

public class CreateCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if (commandSender instanceof Player) {
            Player player = ((Player) commandSender);
            Inventory inventory = player.getInventory();

            inventory.addItem( new StickItem().getCreateStick());
            return true;
        }

        return false;
    }
}