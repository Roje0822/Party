package io.github.roje.util;

import com.roje.rojelib.data.Config;
import io.github.roje.PartyMain;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.List;

public class PartyUtil {

    private Player player;
    private Config config;
    private Config playerConfig;

    public PartyUtil(Player player) {
        this.player = player;
    }

    public void createParty(String name) {
        config = new Config("party/" + name, PartyMain.getInstance());
        config.setString("admin", player.getName());
        config.setStringList("member", List.of(player.getName()));
        getPlayerConfig(player).setString("party", name);
        getPlayerConfig(player).setString("position", "admin");
    }

    public void dismissParty(String name) {
        config = new Config("party/" + name, PartyMain.getInstance());
        for (String member : config.getStringList("member")) {
            getPlayerConfig(Bukkit.getPlayer(member)).setString("party", null);
            getPlayerConfig(Bukkit.getPlayer(member)).setString("position", null);
        }
        config.deleteFile();
    }

    public String getPlayerParty() {
        return getPlayerConfig(player).getString("party");
    }

    public String getTargetParty(Player target) {
        return getPlayerConfig(target).getString("party");
    }

    public boolean isPartyExist(Player target) {
        return getTargetParty(target) != null;
    }

    public boolean isPlayerAdmin() {
        return getPlayerConfig(player).getString("position").equals("admin");
    }

    public boolean isPartyNameExist(String name) {
        config = new Config("party/" + name, PartyMain.getInstance());
        return config.isFileExist();
    }

    public boolean isTargetPartyExist(Player target) {
        return getPlayerConfig(target).getString("party") != null;
    }

    public void kickPartyPlayer(Player player) {
        config = new Config("party/" + getPlayerParty(), PartyMain.getInstance());
        List<String> member = config.getStringList("member");
        member.remove(player.getName());
        config.setStringList("member", member);
        getPlayerConfig(player).setString("party", null);
        getPlayerConfig(player).setString("position", null);
    }

    public void joinParty(String name) {
        getPlayerConfig(player).setString("party", name);
        getPlayerConfig(player).setString("position", "member");
        config = new Config("party/" + name, PartyMain.getInstance());
        List<String> member = config.getStringList("member");
        member.add(player.getName());
        config.setStringList("member", member);
    }

    public List<String> getPartyMember(String name) {
        config = new Config("party/" + name, PartyMain.getInstance());
        return config.getStringList("member");
    }

    private Config getPlayerConfig(Player player) {
        playerConfig = new Config("data/" + player.getUniqueId(), PartyMain.getInstance());
        return playerConfig;
    }
}
