package marvtechnology.commandlimit;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Logger;

public class ConfigManager {
    private final CommandLimiter plugin;
    private FileConfiguration config;
    private FileConfiguration messages;
    private final Map<String, Map<String, String>> translations = new HashMap<>();
    private final Logger logger;

    public ConfigManager(CommandLimiter plugin) {
        this.plugin = plugin;
        this.logger = plugin.getLogger();
        loadConfig();
    }

    public void loadConfig() {
        // 設定フォルダが存在しない場合は作成
        File dataFolder = plugin.getDataFolder();
        if (!dataFolder.exists() && !dataFolder.mkdirs()) {
            logger.warning("プラグインフォルダの作成に失敗しました！");
        }

        // 設定ファイルのロード
        File configFile = new File(dataFolder, "config.yml");
        if (!configFile.exists()) {
            plugin.saveResource("config.yml", false);
        }
        config = YamlConfiguration.loadConfiguration(configFile);

        // メッセージファイルのロード
        File messagesFile = new File(dataFolder, "messages.yml");
        if (!messagesFile.exists()) {
            plugin.saveResource("messages.yml", false);
        }
        messages = YamlConfiguration.loadConfiguration(messagesFile);

        // メッセージの翻訳データをロード
        translations.clear();

        if (!messages.contains("messages") || messages.getConfigurationSection("messages") == null) {
            logger.warning("messages.yml に 'messages' セクションが見つかりません。");
            return;
        }

        Set<String> keys = messages.getConfigurationSection("messages").getKeys(false);
        for (String key : keys) {
            Map<String, String> localizedMessages = new HashMap<>();
            if (!messages.contains("messages." + key) ||
                    messages.getConfigurationSection("messages." + key) == null) {
                continue;
            }

            Set<String> langKeys = messages.getConfigurationSection("messages." + key).getKeys(false);
            for (String lang : langKeys) {
                localizedMessages.put(lang,
                        messages.getString("messages." + key + "." + lang, "Unknown Message"));
            }
            translations.put(key, localizedMessages);
        }
    }

    public void reloadConfig() {
        loadConfig();
    }

    public List<String> getAllowedCommands() {
        return config.getStringList("allowed-commands");
    }

    public String getMessage(String key, String lang) {
        return translations
                .getOrDefault(key, new HashMap<>())
                .getOrDefault(lang, "§c[Message Not Found]");
    }
}








