package com.roje.party;

import com.roje.party.command.PartyCmd;
import org.bukkit.Bukkit;
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


        //config
        plugin = this;
        saveConfig();

    }


    public static JavaPlugin getPlugin() {
        return plugin;
    }
}
