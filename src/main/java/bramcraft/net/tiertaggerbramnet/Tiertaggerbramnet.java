package bramcraft.net.tiertaggerbramnet;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.CommandExecutor;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public final class Tiertaggerbramnet extends JavaPlugin implements Listener, CommandExecutor {

    private Map<String, String> playerTiers = new HashMap<>();

    @Override
    public void onLoad() {
        if (Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null) {
            new TierPlaceholder(this).register();
            getLogger().info("PlaceholderAPI found - %tier% registered.");
        } else {
            getLogger().warning("PlaceholderAPI not found - placeholder disabled.");
        }
    }

    @Override
    public void onEnable() {
        Bukkit.getPluginManager().registerEvents(this, this);
        getCommand("tier").setExecutor(this);

        loadTierlist();
        startAutoReload();
    }

    private void loadTierlist() {
        getServer().getScheduler().runTaskAsynchronously(this, () -> {
            try {
                TierScraper scraper = new TierScraper();
                Map<String, String> tiers = scraper.fetchTiers();
                this.playerTiers = tiers;
                getLogger().info("Tierlist loaded. " + tiers.size() + " players found.");
            } catch (IOException e) {
                getLogger().warning("Failed to load tierlist: " + e.getMessage());
            }
        });
    }

    private void startAutoReload() {
        long delay = 20L * 60; // 60 seconds
        getServer().getScheduler().runTaskTimerAsynchronously(this, () -> {
            try {
                TierScraper scraper = new TierScraper();
                Map<String, String> tiers = scraper.fetchTiers();
                this.playerTiers = tiers;
            } catch (IOException e) {
                getLogger().warning("Failed to auto-refresh tierlist: " + e.getMessage());
            }
        }, delay, delay);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 1 && args[0].equalsIgnoreCase("reload")) {
            if (!sender.hasPermission("tiertagger.reload")) {
                sender.sendMessage(ChatColor.RED + "You don't have permission to reload the tierlist.");
                return true;
            }
            sender.sendMessage(ChatColor.YELLOW + "Reloading tierlist...");
            loadTierlist();
            sender.sendMessage(ChatColor.GREEN + "Tierlist reload requested.");
            return true;
        }

        if (args.length == 1) {
            String targetName = args[0];
            String raw = playerTiers.getOrDefault(targetName, "unknown");
            if (raw.equals("unknown")) {
                sender.sendMessage(ChatColor.RED + targetName + " is not listed in the tierlist.");
                return true;
            }
            sender.sendMessage(ChatColor.AQUA + targetName + "'s tier: " + ChatColor.YELLOW + formatTier(raw));
            return true;
        }

        if (sender instanceof Player player) {
            String raw = getTierForPlayer(player);
            if (raw.equals("unknown")) {
                player.sendMessage(ChatColor.RED + "You're not listed in the tierlist.");
            } else {
                player.sendMessage(ChatColor.GREEN + "Your tier: " + ChatColor.YELLOW + formatTier(raw));
            }
        } else {
            sender.sendMessage("Use /tier <player> or /tier reload.");
        }
        return true;
    }

    public String getTierForPlayer(Player player) {
        return playerTiers.getOrDefault(player.getName(), "unknown");
    }

    public String formatTier(String raw) {
        boolean isLow = raw.endsWith("_low");
        String tierNumber = raw.replace("tier", "").replace("_low", "").replace("_high", "");
        String prefix = isLow ? "LT" : "HT";
        return prefix + tierNumber;
    }

    public Map<String, String> getPlayerTiers() {
        return this.playerTiers;
    }
}