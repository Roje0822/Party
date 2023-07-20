package io.github.roje.command;

import io.github.roje.PartyMain;
import io.github.roje.util.PartyUtil;
import io.github.roje.util.StringUtil;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.regex.Pattern;

import static io.github.roje.data.InviteMap.inviteMap;

public class PartyCmd implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, @NotNull String[] args) {

        if (!(sender instanceof Player player)) {
            System.out.println("§c버킷에서는 명령어를 사용할 수 없습니다");
            return true;
        }

        final StringUtil stringUtil = new StringUtil(player);

        if (args.length == 0) {
            //TODO usage
            for (String message : stringUtil.getUsage()) {
                player.sendMessage(ChatColor.translateAlternateColorCodes('&',message));
            }
            return true;
        }

        PartyUtil partyUtil;
        String partyName;


        switch (args[0]) {

            case "생성" -> {
                //TODO 파티생성

                partyUtil = new PartyUtil(player);
                if (partyUtil.isPartyExist(player)) {
                    //TODO party exist
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&',stringUtil.getConfig("error.party_exist")));
                    return true;
                }
                if (args.length != 2) {
                    //TODO no party name
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&',stringUtil.getConfig("error.no_party_name")));
                    return true;
                }
                if (Pattern.matches("[a-zA-Z1-9]", args[1])) {
                    //TODO wrong party name
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&',stringUtil.getConfig("error.party_name_wrong")));
                    return true;
                }
                if (partyUtil.isPartyNameExist(args[1])) {
                    //TODO party name exist
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&',stringUtil.getConfig("error.party_name_exist")));
                    return true;
                }

                partyName = args[1];
                partyUtil.createParty(partyName);
                player.sendMessage(ChatColor.translateAlternateColorCodes('&',stringUtil.getConfig("message.party_create"))
                        .replace("{name}", partyName));
                return true;
            }

            case "해산" -> {
                //TODO 파티해산

                partyUtil = new PartyUtil(player);
                if (!partyUtil.isPartyExist(player)) {
                    //TODO no party
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&',stringUtil.getConfig("error.no_party")));
                    return true;
                }
                if (!partyUtil.isPlayerAdmin()) {
                    //TODO no admin
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&',stringUtil.getConfig("error.no_admin")));
                    return true;
                }

                partyUtil.dismissParty(partyUtil.getPlayerParty());
                player.sendMessage(ChatColor.translateAlternateColorCodes('&',stringUtil.getConfig("message.party_dismiss"))
                        .replace("{name}", partyUtil.getPlayerParty()));
                return true;
            }

            case "초대" -> {
                //TODO 파티초대

                partyUtil = new PartyUtil(player);
                if (!partyUtil.isPartyExist(player)) {
                    //TODO no party
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&',stringUtil.getConfig("error.no_party")));
                    return true;
                }
                if (args.length != 2) {
                    //TODO no player
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&',stringUtil.getConfig("error.no_player")));
                    return true;
                }
                if (!partyUtil.isPlayerAdmin()) {
                    //TODO no admin
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&',stringUtil.getConfig("error.no_admin")));
                    return true;
                }
                Player target = Bukkit.getPlayer(args[1]);
                if (target == player) {
                    //TODO player invite
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&',stringUtil.getConfig("error.invite_self")));
                    return true;
                }
                if (!partyUtil.isTargetPartyExist(target)) {
                    //TODO target party exist
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&',stringUtil.getConfig("error.target_party_exist")));
                    return true;
                }
                if (inviteMap.get(player) != null) {
                    //TODO party invite exist
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&',stringUtil.getConfig("error.target_invite_already")));
                    return true;
                }

                inviteMap.put(target, partyUtil.getPlayerParty());
                player.sendMessage(ChatColor.translateAlternateColorCodes('&',stringUtil.getConfig("message.invite"))
                        .replace("{player}", target.getName()));

                Bukkit.getScheduler().runTaskLater(PartyMain.getInstance(), () -> {
                    //30초뒤
                    inviteMap.remove(target);
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&',stringUtil.getConfig("message.invite_expire")));
                }, 30 * 20L);

                return true;

            }

            case "수락" -> {
                //TODO accept

                partyUtil = new PartyUtil(player);
                if (inviteMap.get(player) == null) {
                    //TODO no invite
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&',stringUtil.getConfig("error.no_invite")));
                    return true;
                }

                partyUtil.joinParty(inviteMap.get(player));
                inviteMap.remove(player);
                player.sendMessage(ChatColor.translateAlternateColorCodes('&',stringUtil.getConfig("message.accept_invite")));
                return true;
            }

            case "거절" -> {
                if (inviteMap.get(player) == null) {
                    //TODO no invite
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&',stringUtil.getConfig("error.no_invite")));
                    return true;
                }
                inviteMap.remove(player);
                player.sendMessage(ChatColor.translateAlternateColorCodes('&',stringUtil.getConfig("message.reject_invite")));
                return true;
            }

            case "강퇴" -> {
                //TODO 파티강퇴

                partyUtil = new PartyUtil(player);
                if (!partyUtil.isPartyExist(player)) {
                    //TODO no party
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&',stringUtil.getConfig("error.no_party")));
                    return true;
                }
                if (args.length != 2) {
                    //TODO no player
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&',stringUtil.getConfig("error.no_player")));
                    return true;
                }
                if (partyUtil.isPlayerAdmin()) {
                    //TODO no admin
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&',stringUtil.getConfig("error.no_admin")));
                    return true;
                }
                Player target = Bukkit.getPlayer(args[1]);
                if (player == target) {
                    //TODO kick self
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&',stringUtil.getConfig("error.no_kick_self")));
                    return true;
                }

                partyUtil.kickPartyPlayer(target);
                player.sendMessage(ChatColor.translateAlternateColorCodes('&',stringUtil.getConfig("message.kick"))
                        .replace("{player}", target.getName()));
                player.sendMessage(ChatColor.translateAlternateColorCodes('&',stringUtil.getConfig("message.kicked")));
                return true;
            }

            default -> {
                //TODO usage
                for (String message : stringUtil.getUsage()) {
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&',message));
                }
                return true;
            }
        }
    }
}
