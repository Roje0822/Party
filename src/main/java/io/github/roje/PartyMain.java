package io.github.roje;

import io.github.roje.Listener.PlayerJoinListener;
import io.github.roje.command.PartyCmd;
import io.github.roje.command.tabcomplete.PartyTabCompletor;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public class PartyMain extends JavaPlugin {

    private static PartyMain plugin;

    @Override
    public void onEnable() {

        init();
    }

    private void init() {

        //Config
        plugin = this;
        saveDefaultConfig();

        //Commands
        getCommand("party").setExecutor(new PartyCmd());
        getCommand("party").setTabCompleter(new PartyTabCompletor());

        //Events
        getServer().getPluginManager().registerEvents(new PlayerJoinListener(), this);

    }

    public static PartyMain getInstance() {
        return plugin;
    }
}
