import commands.CreateCommand;
import commands.StartCommand;
import listeners.DeathListener;
import listeners.StickListener;
import org.bukkit.plugin.java.JavaPlugin;


import java.io.File;

import static util.Utils.PLUGIN_NAME;

public class Main extends JavaPlugin {

    @Override
    public void onEnable() {
        System.out.println(PLUGIN_NAME + " loaded");
        this.initializeCommands();
        this.initializeEvents();
        this.getServer().getPluginManager().registerEvents(new StickListener(getPluginFolder(),this), this);
    }

    @Override
    public void onDisable() {
        System.out.println(PLUGIN_NAME + " unloaded");
    }

    private File getPluginFolder() {
        File pluginFolder = getDataFolder();

        if (!pluginFolder.exists()) {
            pluginFolder.mkdir();
        }

        return pluginFolder;
    }

    private void initializeCommands(){
        this.getCommand("ctfcreate").setExecutor(new CreateCommand());
        this.getCommand("ctfstart").setExecutor(new StartCommand(this,this.getPluginFolder()));
    }

    private void initializeEvents(){
        this.getServer().getPluginManager().registerEvents(new StickListener(getPluginFolder(),this), this);
        this.getServer().getPluginManager().registerEvents(new DeathListener(),this);
    }

}
