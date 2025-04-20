package marvtechnology.commandlimit;

import org.bukkit.Bukkit;
import org.bukkit.command.PluginCommand;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;

public class CommandLimiter extends JavaPlugin {
    private ConfigManager configManager;

    @Override
    public void onEnable() {

       
        if (!getDataFolder().exists() && !getDataFolder().mkdirs()) {
            getLogger().warning("プラグインフォルダの作成に失敗しました！");
        }

      
        saveDefaultConfig();
        if (!new File(getDataFolder(), "messages.yml").exists()) {
            saveResource("messages.yml", false);
        }

        this.configManager = new ConfigManager(this);

      
        Bukkit.getPluginManager().registerEvents(new CommandListener(this), this);

        
        PluginCommand command = getCommand("commandlimit");
        if (command != null) {
            command.setExecutor(new CommandHandler(this));
        }

        getLogger().info("CommandLimiter プラグインが正常に読み込まれました。");
    }

    public ConfigManager getConfigManager() {
        return configManager;
    }
}



