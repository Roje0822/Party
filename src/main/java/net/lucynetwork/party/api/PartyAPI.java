package net.lucynetwork.party.api;

import net.lucynetwork.lucycore.data.Config;
import net.lucynetwork.party.PartyMain;
import org.bukkit.entity.Player;

import java.util.List;

public class PartyAPI {

    private String name;


    public List<String> getPartyMembers(String name) {
        Config config = new Config("party/" + name, PartyMain.getPlugin());
        return config.getStringList("party.member");
    }


    public String getPlayerParty(Player player) {
        Config config = new Config("data/" + player.getUniqueId(), PartyMain.getPlugin());
        return config.getString("party");
    }

}
