package marvtechnology.commandlimit;

import org.bukkit.Bukkit;
import org.bukkit.command.PluginCommand;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File; // ← これが必要

public class CommandLimiter extends JavaPlugin {
    private ConfigManager configManager;

    @Override
    public void onEnable() {

        // 設定フォルダを作成（失敗時のみ警告を表示）
        if (!getDataFolder().exists() && !getDataFolder().mkdirs()) {
            getLogger().warning("プラグインフォルダの作成に失敗しました！");
        }

        // 設定ファイルの読み込み
        saveDefaultConfig();
        if (!new File(getDataFolder(), "messages.yml").exists()) {
            saveResource("messages.yml", false);
        }

        // 設定マネージャーの初期化
        this.configManager = new ConfigManager(this);

        // イベントリスナーの登録
        // Folia判定用の isFolia フィールドは削除 or 不要な場合は使わないようにします
        Bukkit.getPluginManager().registerEvents(new CommandListener(this), this);

        // コマンド登録
        PluginCommand command = getCommand("commandlimit");
        if (command != null) {
            command.setExecutor(new CommandHandler(this));
        }

        // **シンプルなログを1行だけ表示**
        getLogger().info("CommandLimiter プラグインが正常に読み込まれました。");
    }

    public ConfigManager getConfigManager() {
        return configManager;
    }
}



