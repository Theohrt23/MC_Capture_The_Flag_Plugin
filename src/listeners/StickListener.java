package listeners;


import entity.Flag;
import entity.Spawn;
import messages.StickMessage;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import util.TeamColors;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static util.Utils.PLUGIN_PREFIX;

public class StickListener implements Listener {

    private final File pluginFolder;

    private final Plugin plugin;

    private final PluginManager pluginManager;

    private boolean isEventLocked = false;

    public StickListener(File file, Plugin plugin){
        this.pluginFolder = file;
        this.plugin = plugin;
        this.pluginManager = plugin.getServer().getPluginManager();
    }

    @EventHandler
    public void onPlayerStickInteract(PlayerInteractEvent event) {
        if (isEventLocked) {
            event.setCancelled(true);
            return;
        }
        Player player = event.getPlayer();
        ItemStack item = player.getInventory().getItemInMainHand();
        Location blockLocation = event.getClickedBlock().getLocation();

        if (item.getType() == Material.STICK && item.getItemMeta().getDisplayName().equals("§cCTF Stick") && blockLocation != null) {
            if (event.getAction().equals(Action.LEFT_CLICK_BLOCK)) {
                isEventLocked = true;
                Flag flag = new Flag();
                flag.setX(blockLocation.getBlockX());
                flag.setY(blockLocation.getBlockY());
                flag.setZ(blockLocation.getBlockZ());
                flag.setWorld(player.getWorld());

                player.sendMessage(StickMessage.getChooseTeamColorMessage());
                event.setCancelled(true);

                this.selectTeam(player, flag);
            }
            if (event.getAction().equals(Action.RIGHT_CLICK_BLOCK)){
                isEventLocked = true;
                player.sendMessage(StickMessage.getSelectRedSpawnPointMessage());
                event.setCancelled(true);

                Spawn spawn = new Spawn();
                spawn.setWorld(player.getWorld());
                this.selectSpawnPoint(TeamColors.RED_TEAM, player, spawn);
            }
        }
    }

    private void selectSpawnPoint(TeamColors teamColors, Player player, Spawn spawn){
        pluginManager.registerEvents(new Listener() {
            @EventHandler
            public void onPlayerStickInteract(PlayerInteractEvent event) {
                Location blockLocation = event.getClickedBlock().getLocation();
                if (blockLocation != null && teamColors.equals(TeamColors.RED_TEAM)){
                    spawn.setXRedSpawn(blockLocation.getBlockX());
                    spawn.setYRedSpawn(blockLocation.getBlockY() + 1);
                    spawn.setZRedSpawn(blockLocation.getBlockZ());
                    player.sendMessage(StickMessage.getSelectBlueSpawnPointMessage());
                    event.setCancelled(true);
                    HandlerList.unregisterAll(this);
                    selectSpawnPoint(TeamColors.BLUE_TEAM, player, spawn);
                }else if (blockLocation != null && teamColors.equals(TeamColors.BLUE_TEAM)){
                    spawn.setXBlueSpawn(blockLocation.getBlockX());
                    spawn.setYBlueSpawn(blockLocation.getBlockY() + 1);
                    spawn.setZBlueSpawn(blockLocation.getBlockZ());
                    player.sendMessage(StickMessage.getDoneMessage());
                    HandlerList.unregisterAll(this);
                    saveSpawn(spawn);
                    event.setCancelled(true);
                    isEventLocked = false;
                }
            }
        }, plugin);
    }

    private void selectTeam(Player player, Flag flag) {
        pluginManager.registerEvents(new Listener() {
            @EventHandler
            public void onAsyncPlayerChat(AsyncPlayerChatEvent event) {
                if (event.getPlayer().equals(player)) {
                    String message = event.getMessage().toLowerCase();
                    if (message.equalsIgnoreCase(TeamColors.BLUE_TEAM.getColor())) {
                        flag.setColors(TeamColors.BLUE_TEAM);
                        player.sendMessage(StickMessage.getChooseBlueMessage());
                        event.setCancelled(true);
                        HandlerList.unregisterAll(this);
                        selectName(player,flag);
                    }else if (message.equalsIgnoreCase(TeamColors.RED_TEAM.getColor())) {
                        flag.setColors(TeamColors.RED_TEAM);
                        player.sendMessage(StickMessage.getChooseRedMessage());
                        event.setCancelled(true);
                        HandlerList.unregisterAll(this);
                        selectName(player, flag);
                    }else if (message.equalsIgnoreCase("cancel")){
                            player.sendMessage(StickMessage.getCancelFlagCreationMessage());
                            event.setCancelled(true);
                            HandlerList.unregisterAll(this);
                            isEventLocked = false;
                    }else {
                        player.sendMessage(StickMessage.getErrorTypeMessage());
                        event.setCancelled(true);
                    }
                }
            }
        }, plugin);
    }

   private void selectName(Player player, Flag flag){
        player.sendMessage(StickMessage.getSelectNameMessage());
        pluginManager.registerEvents(new Listener() {
            @EventHandler
            public void onAsyncPlayerChat(AsyncPlayerChatEvent event) {
                if (event.getPlayer().equals(player)) {
                    String message = event.getMessage().toLowerCase();
                    flag.setName(message);
                    player.sendMessage(StickMessage.getConfirmNameMessage(message));

                    event.setCancelled(true);
                    HandlerList.unregisterAll(this);
                    buildFlag(flag,player);
                }
            }
        }, plugin);
    }

    private void buildFlag(Flag flag, Player player){
        saveFlag(flag);
        createFlag(flag, player);
        isEventLocked = false;
    }

    private void createFlag(Flag flag, Player player) {
        World world = flag.getWorld();

        Material wool;

        int X = flag.getX();
        int Y = flag.getY();
        int Z = flag.getZ();

        if (flag.getColors().equals(TeamColors.BLUE_TEAM)){
            wool = Material.BLUE_WOOL;
        }else {
            wool = Material.RED_WOOL;
        }

        Bukkit.getScheduler().runTask(plugin, () -> {
            for (int i = 1 ; i <= 7 ; i++){
                world.getBlockAt(X, Y+i, Z).setType(Material.SPRUCE_FENCE);
                if (i == 6 || i == 7){
                    world.getBlockAt(X+1, Y+i, Z).setType(wool);
                    world.getBlockAt(X+2, Y+i, Z).setType(wool);
                    world.getBlockAt(X+3, Y+i, Z).setType(wool);
                    world.getBlockAt(X+4, Y+i, Z).setType(wool);
                }
            }
        });

        player.sendMessage(PLUGIN_PREFIX + " Flag has been created.");
    }

    private void saveFlag(Flag flag) {
        File locationFile = new File(this.pluginFolder, "flag_location.yml");
        YamlConfiguration flagConfig;
        if (locationFile.exists()) {
            flagConfig = YamlConfiguration.loadConfiguration(locationFile);
        } else {
            flagConfig = new YamlConfiguration();
        }

        String flagName = flag.getName();
        String worldName = flag.getWorld().getName();

        List<Integer> coordinates = new ArrayList<>();
        coordinates.add(flag.getX());
        coordinates.add(flag.getY());
        coordinates.add(flag.getZ());

        List<String> colors = new ArrayList<>();
        colors.add(flag.getColors().getColor());

        flagConfig.set(String.format("%s.world", flagName), worldName);
        flagConfig.set(String.format("%s.coordinates", flagName), coordinates);
        flagConfig.set(String.format("%s.color", flagName), colors);

        try {
            flagConfig.save(locationFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void saveSpawn(Spawn spawn) {
        File locationFile = new File(this.pluginFolder, "spawn_location.yml");
        YamlConfiguration flagConfig;
        if (locationFile.exists()) {
            flagConfig = YamlConfiguration.loadConfiguration(locationFile);
        } else {
            flagConfig = new YamlConfiguration();
        }

        String worldName = spawn.getWorld().getName();
        ConfigurationSection worldSection = flagConfig.getConfigurationSection(worldName);
        if (worldSection != null) {
            worldSection.set("red.coordinates", Arrays.asList(spawn.getXRedSpawn(), spawn.getYRedSpawn(), spawn.getZRedSpawn()));
            worldSection.set("blue.coordinates", Arrays.asList(spawn.getXBlueSpawn(), spawn.getYBlueSpawn(), spawn.getZBlueSpawn()));
        } else {
            List<Integer> coordinatesRed = Arrays.asList(spawn.getXRedSpawn(), spawn.getYRedSpawn(), spawn.getZRedSpawn());
            List<Integer> coordinatesBlue = Arrays.asList(spawn.getXBlueSpawn(), spawn.getYBlueSpawn(), spawn.getZBlueSpawn());
            flagConfig.set(String.format("%s.%s.coordinates", worldName, "red"), coordinatesRed);
            flagConfig.set(String.format("%s.%s.coordinates", worldName, "blue"), coordinatesBlue);
        }

        try {
            flagConfig.save(locationFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
