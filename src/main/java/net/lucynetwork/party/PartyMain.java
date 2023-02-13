package net.lucynetwork.party;

import net.lucynetwork.lucycore.data.Config;
import net.lucynetwork.party.command.PartyCmd;
import net.lucynetwork.party.command.PartyTabComplete;
import org.bukkit.plugin.java.JavaPlugin;

public class PartyMain extends JavaPlugin {

    public static PartyMain plugin;


    @Override
    public void onEnable() {

        init();
    }


    private void init() {

        plugin = this;

        //command
        getCommand("파티").setExecutor(new PartyCmd());
        getCommand("파티").setTabCompleter(new PartyTabComplete());

        //config
        Config config = new Config("config", this);
        config.loadDefaultConfig();
    }

    public static JavaPlugin getPlugin() {
        return plugin;
    }


}
