package bramcraft.net.tiertaggerbramnet;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.entity.Player;

public class TierPlaceholder extends PlaceholderExpansion {

    private final Tiertaggerbramnet plugin;

    public TierPlaceholder(Tiertaggerbramnet plugin) {
        this.plugin = plugin;
    }

    @Override
    public String getIdentifier() {
        return "tier";
    }

    @Override
    public String getAuthor() {
        return "bramcraft";
    }

    @Override
    public String getVersion() {
        return "1.0";
    }

    @Override
    public String onPlaceholderRequest(Player player, String identifier) {
        // %tier% or %tier_tier%
        if (identifier == null || identifier.equals("") || identifier.equals("tier")) {
            if (player == null) return "";
            String raw = plugin.getTierForPlayer(player);
            if (raw.equals("unknown")) return "N/A";
            return plugin.formatTier(raw);
        }
        return null;
    }
}
