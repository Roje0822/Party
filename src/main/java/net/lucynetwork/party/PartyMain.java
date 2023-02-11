package net.lucynetwork.party;

import net.lucynetwork.party.command.PartyCmd;
import net.lucynetwork.party.command.PartyTabComplete;
import net.starly.core.data.Config;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class PartyMain extends JavaPlugin {

    public static PartyMain plugin;


    @Override
    public void onEnable() {

        init();
    }


    private void init() {

        //command
        getCommand("파티").setExecutor(new PartyCmd());
        getCommand("파티").setTabCompleter(new PartyTabComplete());

        //event

        //config
        plugin = this;
        Config config = new Config("config", this);
        config.loadDefaultConfig();
    }

    public static JavaPlugin getPlugin() {
        return plugin;
    }



    @EventHandler
    public void sexKarina(PlayerJoinEvent event) {

        if(event.getPlayer().hasPlayedBefore()) {

        }
    }
}
