package bramcraft.net.tiertaggerbramnet;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class TierScraper {

    public Map<String, String> fetchTiers() throws IOException {
        Map<String, String> playerTiers = new HashMap<>();

        // Download and parse HTML
        Document doc = Jsoup.connect("https://www.bramcraft.net/tierlist/").get();

        // Loop through each tier div
        Elements tierDivs = doc.select(".tierlist .tier");
        for (Element tierDiv : tierDivs) {
            String[] parts = tierDiv.className().split(" ");
            String tier = parts.length > 1 ? parts[1] : "unknown";

            // Select all players (excluding .empty)
            Elements players = tierDiv.select(".player:not(.empty)");
            for (Element player : players) {
                String playerName = player.text().trim();
                if (!playerName.equals("–") && !playerName.isEmpty()) {
                    boolean isLow = player.hasClass("low");
                    String finalTier = tier + (isLow ? "_low" : "_high");
                    playerTiers.put(playerName, finalTier);
                }
            }
        }

        return playerTiers;
    }
}
