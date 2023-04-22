package items;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class StickItem {

    private ItemStack createStick;

    private ItemMeta createStickMeta;

    private void stickBuilder() {
        this.createStickMeta.setDisplayName("§cCTF Stick");
        this.createStickMeta.addEnchant(Enchantment.ARROW_INFINITE,1,true);
        this.createStick.setItemMeta(this.createStickMeta);
    }

    public StickItem() {
        this.createStick = new ItemStack(Material.STICK, 1);
        this.createStickMeta = this.createStick.getItemMeta();
        this.stickBuilder();
    }

    public ItemStack getCreateStick() {
        return this.createStick;
    }
}
