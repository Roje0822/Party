package io.github.roje.Listener;

import com.roje.rojelib.data.Config;
import io.github.roje.PartyMain;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class PlayerJoinListener implements Listener {

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        Config config = new Config("data/" + player.getUniqueId(), PartyMain.getInstance());
        if (!config.isFileExist()) config.setString("party",null);
    }
}
