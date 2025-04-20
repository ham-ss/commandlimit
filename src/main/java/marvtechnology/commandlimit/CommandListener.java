package marvtechnology.commandlimit;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerCommandSendEvent;

import java.util.HashSet;
import java.util.Locale;
import java.util.Set;

public class CommandListener implements Listener {
    private final CommandLimiter plugin;

    public CommandListener(CommandLimiter plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onCommandTabComplete(PlayerCommandSendEvent event) {
        Player player = event.getPlayer();

        if (player.isOp() || player.hasPermission("commandlimit.bypass")) {
            return;
        }

        Set<String> allowedCommands = new HashSet<>(plugin.getConfigManager().getAllowedCommands());
        event.getCommands().removeIf(cmd -> !allowedCommands.contains(cmd));
    }

    @EventHandler
    public void onCommandPreProcess(PlayerCommandPreprocessEvent event) {
        Player player = event.getPlayer();

        String command = event.getMessage().split(" ")[0].substring(1).toLowerCase();

        if (player.isOp() || player.hasPermission("commandlimit.bypass")) {
            return;
        }

        Set<String> allowedCommands = new HashSet<>(plugin.getConfigManager().getAllowedCommands());
        if (!allowedCommands.contains(command)) {
            event.setCancelled(true);
            String lang = getPlayerLanguage(player);
            player.sendMessage(plugin.getConfigManager().getMessage("command_blocked", lang));
        }
    }

    
    private String getPlayerLanguage(Player player) {

        Locale locale = player.locale();

        String language = locale.getLanguage().toLowerCase();
        if (language.startsWith("ja")) return "ja";
        if (language.startsWith("zh")) return "zh";
        if (language.startsWith("en")) return "en";
        return "en";
    }
}



