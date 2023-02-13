package net.lucynetwork.party.command;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.Collections;
import java.util.List;

public class PartyTabComplete implements TabCompleter {
    @Override
    public List<String> onTabComplete(CommandSender sender, Command cmd, String label, String[] args) {

        if (args.length == 1) {
            return List.of("생성", "나가기", "해체", "추방", "초대", "수락", "거절");
        } else if (args.length == 2) {
            if (args[0].equals("생성")) return List.of("<파티이름>");
            if (args[0].equals("추방") || args[0].equals("초대")) {
                for (Player player : Bukkit.getOnlinePlayers()) {
                    return List.of(player.getName());
                }
            }
        }

        return Collections.emptyList();
    }
}
