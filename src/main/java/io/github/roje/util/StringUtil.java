package io.github.roje.util;

import com.roje.rojelib.data.Config;
import io.github.roje.PartyMain;
import org.bukkit.entity.Player;

import java.util.List;

public class StringUtil {

    private final Config config = new Config("config", PartyMain.getInstance());
    private Player player;

    public StringUtil(Player player) {
        this.player = player;
    }

    public String getConfig(String path) {
        return (getPrefix() + " " + config.getString(path));
    }

    public List<String> getUsage() {
        return config.getStringList("usage");
    }

    private String getPrefix() {
        return config.getString("prefix");
    }
}
