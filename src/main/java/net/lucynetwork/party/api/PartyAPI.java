package net.lucynetwork.party.api;

import net.lucynetwork.lucycore.data.Config;
import net.lucynetwork.party.PartyMain;
import org.bukkit.entity.Player;

import java.util.List;

public class PartyAPI {

    private final Player player;
    private final Config config;

    public PartyAPI(Player player) {
        this.player = player;
        this.config = new Config("data/" + player.getUniqueId(), PartyMain.getPlugin());
    }

    public PartyAPI(Player player, String name) {
        this.player = player;
        this.config = new Config("party/" + name, PartyMain.getPlugin());
    }


    public List<String> getPartyMembers() {
        return config.getStringList("party.member");
    }


    public String getPlayerParty() {
        return config.getString("party");
    }
}
