package marvtechnology.commandlimit;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Locale;
import java.util.concurrent.CompletableFuture;

public class CommandHandler implements CommandExecutor {
    private final CommandLimiter plugin;

    public CommandHandler(CommandLimiter plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command,
                             @NotNull String label, @NotNull String[] args) {

        if (!(sender instanceof Player)) {
            sender.sendMessage("This command must be used by a player.");
            return true;
        }

        Player player = (Player) sender;
        String lang = getPlayerLanguage(player);

        if (!player.hasPermission("commandlimit.admin")) {
            player.sendMessage(plugin.getConfigManager().getMessage("no_permission", lang));
            return true;
        }

        if (args.length == 1 && args[0].equalsIgnoreCase("reload")) {
            CompletableFuture.runAsync(() -> {
                plugin.getConfigManager().reloadConfig();
                Bukkit.getScheduler().runTask(plugin, () ->
                        player.sendMessage(plugin.getConfigManager().getMessage("config_reloaded", lang))
                );
            });
            return true;
        }

        player.sendMessage("Usage: /commandlimit reload");
        return true;
    }

    private String getPlayerLanguage(Player player) {
        // Folia のバージョンによっては Optional<Locale> を返す場合があります
        // 必要に応じて player.locale().orElse(Locale.ENGLISH) などに書き換えてください
        Locale locale = player.locale();

        // この行を削除またはコメントアウトすると、コンソール出力されなくなる
        // plugin.getLogger().info("プレイヤーのロケール: " + locale);

        // "ja", "zh", "en" などを判定
        String language = locale.getLanguage().toLowerCase();
        if (language.startsWith("ja")) return "ja";
        if (language.startsWith("zh")) return "zh";
        if (language.startsWith("en")) return "en";
        return "en";
    }
}

