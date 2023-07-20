package io.github.roje.command.tabcomplete;

import io.github.roje.util.PartyUtil;
import org.bukkit.Bukkit;
import org.bukkit.Server;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.util.StringUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class PartyTabCompletor implements TabCompleter {
    @Nullable
    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        List<String> result = new ArrayList<>();
        Player player = (Player) sender;
        PartyUtil partyUtil;
        List<String> member;
        if (args.length == 1) {
            result.add("생성");
            result.add("해산");
            result.add("초대");
            result.add("수락");
            result.add("거절");
            result.add("강퇴");
            return StringUtil.copyPartialMatches(args[0], result, new ArrayList<>());
        } else if (args.length == 2 && args[0].equalsIgnoreCase("강퇴")) {
            partyUtil = new PartyUtil(player);
            result = partyUtil.getPartyMember(partyUtil.getPlayerParty());
            result.remove(player.getName());
            return StringUtil.copyPartialMatches(args[1], result, new ArrayList<>());
        } else if (args.length == 2 && args[0].equalsIgnoreCase("초대")) {
            partyUtil = new PartyUtil(player);
            member = partyUtil.getPartyMember(partyUtil.getPlayerParty());
            List<String> list = Collections.singletonList(Bukkit.getServer().getOnlinePlayers().toString());
            list.remove(member);
            return StringUtil.copyPartialMatches(args[1], list, new ArrayList<>());
        }

        return Collections.emptyList();
    }
}
