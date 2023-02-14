package net.lucynetwork.party.command;

import net.lucynetwork.party.data.PartyData;
import net.lucynetwork.party.data.StringData;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitScheduler;

import java.util.List;
import java.util.UUID;

import static net.lucynetwork.party.data.PartyMapData.invitePartyNameMap;

public class PartyCmd implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

        if (sender instanceof Player) {

            Player player = ((Player) sender).getPlayer();

            String partyName;
            StringData config = new StringData();
            PartyData partyData;
            Player target;

            if (args.length == 0) {
                partyData = new PartyData(player);
                if (!partyData.isPlayerPartyExist()) {
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', config.noparty()));
                    return true;
                }
                partyData = new PartyData(player);

                player.sendMessage(ChatColor.translateAlternateColorCodes('&', config.infoPartyPlayer()
                        .replace("{player}", player.getName())));
                player.sendMessage(ChatColor.translateAlternateColorCodes('&', config.infoPartyName()
                        .replace("{partyname}", partyData.getPlayerParty())));
                player.sendMessage(ChatColor.translateAlternateColorCodes('&', config.infoPartyOwner()
                        .replace("{partyowner}", Bukkit.getOfflinePlayer(UUID.fromString(partyData.getPartyOwner())).getName())));
                if (partyData.isPartyCoOwnerExist()) {
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', config.infoPartySubOwner()
                            .replace("{subpartyowner}", Bukkit.getOfflinePlayer(UUID.fromString(partyData.getCoOwner())).getName())));
                }
                List<String> partyMembers = partyData.getPartyMembers();
                partyMembers.remove(partyData.getPartyOwner());
                partyMembers.remove(partyData.getCoOwner());
                partyMembers.forEach(member -> player.sendMessage(ChatColor.translateAlternateColorCodes('&', config.infoPartyMember()
                        .replace("{partymember}", Bukkit.getOfflinePlayer(UUID.fromString(member)).getName()))));
                return true;
            } else {

                switch (args[0]) {

                    case "생성" -> {
                        if (args.length == 1) {
                            player.sendMessage(ChatColor.translateAlternateColorCodes('&', config.enterPartyName()));
                            return true;
                        }

                        partyName = args[1];
                        partyData = new PartyData(player, partyName);
                        String match =  "^[ㄱ-ㅎ가-힣a-zA-Z0-9]*$";

                        if (!partyName.matches(match)) {
                            player.sendMessage(ChatColor.translateAlternateColorCodes('&', config.wrongPartyName()));
                            return true;
                        }
                        if (partyData.isPartyNameExist()) {
                            player.sendMessage(ChatColor.translateAlternateColorCodes('&', config.partyNameExist()
                                    .replace("{name}", partyName)));
                            return true;
                        }
                        if (partyData.isPlayerPartyExist()) {
                            player.sendMessage(ChatColor.translateAlternateColorCodes('&', config.playerPartyExist()));
                            return true;
                        }

                        partyData.createParty();
                        player.sendMessage(ChatColor.translateAlternateColorCodes('&', config.createParty()
                                .replace("{name}", partyName)));
                        return true;
                    }



                    case "초대" -> {

                        if (args.length == 1) {
                            for (String usage : config.usage()) {
                                player.sendMessage(ChatColor.translateAlternateColorCodes('&', usage));
                            }
                            return true;
                        }
                        try {
                            target = player.getServer().getPlayer(args[1]);
                            partyData = new PartyData(target);
                        } catch (Exception e) {
                            player.sendMessage(ChatColor.translateAlternateColorCodes('&', config.playerNotFound())
                                    .replace("{name}", args[1]));
                            return true;
                        }

                        if (player.getName().equals(target.getName())) {
                            player.sendMessage(ChatColor.translateAlternateColorCodes('&', config.inviteSelf()));
                            return true;
                        }
                        if (partyData.isPlayerPartyExist()) {
                            player.sendMessage(ChatColor.translateAlternateColorCodes('&', config.targetPartyExist())
                                    .replace("{target}", target.getName()));
                            return true;
                        }
                        partyData = new PartyData(player);
                        if (!partyData.isPlayerPartyExist()) {
                            player.sendMessage(ChatColor.translateAlternateColorCodes('&', config.noparty()));
                            return true;
                        }
                        if (invitePartyNameMap.containsKey(target)) {
                            player.sendMessage(ChatColor.translateAlternateColorCodes('&', config.inviteAlready())
                                    .replace("{target}", target.getName()));
                            return true;
                        }

                        partyData.inviteParty(target);
                        target.sendMessage(ChatColor.translateAlternateColorCodes('&', config.invitedParty()
                                        .replace("{name}", player.getName()))
                                .replace("{party}", partyData.getPlayerParty()));
                        player.sendMessage(ChatColor.translateAlternateColorCodes('&', config.inviteParty()
                                        .replace("{target}", target.getName()))
                                .replace("{party}", partyData.getPlayerParty()));

                        BukkitScheduler scheduler = Bukkit.getServer().getScheduler();
                        scheduler.runTaskLater(Bukkit.getPluginManager().getPlugin("Party"), () -> {
                            if (!invitePartyNameMap.containsKey(target)) return;

                            invitePartyNameMap.remove(target);
                            player.sendMessage(ChatColor.translateAlternateColorCodes('&', config.expire()
                                    .replace("{target}", target.getName())));
                            target.sendMessage(ChatColor.translateAlternateColorCodes('&', config.expired()
                                    .replace("{target}", target.getName())));

                        }, 20 * config.inviteTime());
                        return true;
                    }


                    case "수락" -> {

                        if (!invitePartyNameMap.containsKey(player)) {
                            player.sendMessage(ChatColor.translateAlternateColorCodes('&', config.notinvite()));
                            return true;
                        }
                        partyData = new PartyData(player);

                        player.sendMessage(ChatColor.translateAlternateColorCodes('&', config.acceptParty()
                                .replace("{name}", invitePartyNameMap.get(player))));
                        partyData.joinParty();
                        return true;
                    }


                    case "거절" -> {
                        if (!invitePartyNameMap.containsKey(player)) {
                            player.sendMessage(ChatColor.translateAlternateColorCodes('&', config.notinvite()));
                            return true;
                        }

                        partyData = new PartyData(player);
                        partyData.rejectParty();
                        player.sendMessage(ChatColor.translateAlternateColorCodes('&', config.rejectParty())
                                .replace("{name}", invitePartyNameMap.get(player))
                                .replace("{target}", player.getName()));
                        return true;
                    }


                    case "나가기" -> {
                        partyData = new PartyData(player);
                        if (!partyData.isPlayerPartyExist()) {
                            player.sendMessage(ChatColor.translateAlternateColorCodes('&', config.noparty()));
                            return true;
                        }
                        if (partyData.isPartyOwner()) {
                            player.sendMessage(ChatColor.translateAlternateColorCodes('&', config.ownerLeave()));
                            return true;
                        }

                        player.sendMessage(ChatColor.translateAlternateColorCodes('&', config.leaveParty()
                                .replace("{name}", partyData.getPlayerParty())));
                        partyData.leaveParty();
                        return true;
                    }


                    case "강퇴", "추방" -> {
                        if (args.length == 1) {
                            for (String usage : config.usage()) {
                                player.sendMessage(ChatColor.translateAlternateColorCodes('&', usage));
                            }
                            return true;
                        }
                        if (Bukkit.getPlayer(args[1]) == null) {
                            player.sendMessage(ChatColor.translateAlternateColorCodes('&', config.playerNotFound()));
                            return true;
                        }
                        if (!new PartyData(player).isPlayerPartyExist()) {
                            player.sendMessage(ChatColor.translateAlternateColorCodes('&', config.noparty()));
                            return true;
                        }

                        target = player.getServer().getPlayer(args[1]);
                        partyData = new PartyData(target);

                        if (target == player) {
                            player.sendMessage(ChatColor.translateAlternateColorCodes('&', config.kickSelf()));
                            return true;
                        }
                        if (target.getUniqueId().toString().equals(partyData.getPartyOwner()) || target.getUniqueId().toString().equals(partyData.getCoOwner())) {
                            player.sendMessage(ChatColor.translateAlternateColorCodes('&', config.kickPermission()));
                            return true;
                        }

                        if (!partyData.isInThisParty()) {
                            player.sendMessage(ChatColor.translateAlternateColorCodes('&', config.notInParty()
                                            .replace("{target}", target.getName()))
                                    .replace("{name}", new PartyData(player).getPlayerParty()));
                            return true;
                        }

                        if (target.getUniqueId().toString() == partyData.getPartyOwner() || target.getUniqueId().toString() == partyData.getCoOwner()) {
                            player.sendMessage(ChatColor.translateAlternateColorCodes('&', config.noPermission()));
                            return true;
                        }
                        partyData.kickParty();

                        player.sendMessage(ChatColor.translateAlternateColorCodes('&', config.kickParty()
                                        .replace("{target}", target.getName()))
                                .replace("{name}", new PartyData(player).getPlayerParty()));
                        target.sendMessage(ChatColor.translateAlternateColorCodes('&', config.kickedParty()
                                        .replace("{target}", target.getName()))
                                .replace("{name}", new PartyData(player).getPlayerParty()));
                        return true;
                    }


                    case "해체" -> {
                        partyData = new PartyData(player);

                        if (!partyData.isPlayerPartyExist()) {
                            player.sendMessage(ChatColor.translateAlternateColorCodes('&', config.noparty()));
                            return true;
                        }
                        if (!partyData.isPartyOwner()) {
                            player.sendMessage(ChatColor.translateAlternateColorCodes('&', config.noPermission()));
                            return true;
                        }
                        List<String> partyMemberUUID = partyData.getPartyMembers();
                        for (String uuid : partyMemberUUID) {
                            Player partyMember = Bukkit.getPlayer(UUID.fromString(uuid));
                            partyMember.sendMessage(ChatColor.translateAlternateColorCodes('&', config.disbandParty()
                                    .replace("{name}", partyData.getPlayerParty())));
                        }

                        partyData.disbandParty();

                        return true;
                    }

                    case "부파티장" -> {


                        if (args[1].equals("선임")) {
                            if (args.length == 2) {
                                for (String usage : config.usage()) {
                                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', usage));
                                }
                                return true;
                            }
                            if (Bukkit.getPlayer(args[2]) == null) {
                                player.sendMessage(ChatColor.translateAlternateColorCodes('&', config.playerNotFound()));
                                return true;
                            }
                            if (!new PartyData(player).isPlayerPartyExist()) {
                                player.sendMessage(ChatColor.translateAlternateColorCodes('&', config.noparty()));
                                return true;
                            }
                            target = player.getServer().getPlayer(args[2]);
                            partyData = new PartyData(player);
                            if (partyData.isPartyCoOwnerExist()) {
                                player.sendMessage(ChatColor.translateAlternateColorCodes('&', config.coOwnerExist()));
                                return true;
                            }
                            if (!partyData.isPartyOwner()) {
                                player.sendMessage(ChatColor.translateAlternateColorCodes('&', config.noPermission()));
                                return true;
                            }
                            if (partyData.isPartyCoOwnerExist()) {
                                player.sendMessage(ChatColor.translateAlternateColorCodes('&', config.coOwnerExist()));
                                return true;
                            }
                            if (target == player) {
                                player.sendMessage(ChatColor.translateAlternateColorCodes('&', config.coOwnerSelf()));
                                return true;
                            }
                            if (!new PartyData(target).isInThisParty()) {
                                player.sendMessage(ChatColor.translateAlternateColorCodes('&', config.notInParty()
                                                .replace("{target}", target.getName()))
                                        .replace("{name}", new PartyData(player).getPlayerParty()));
                                return true;
                            }
                            partyData.electCoOwner(target.getUniqueId());

                            player.sendMessage(ChatColor.translateAlternateColorCodes('&', config.electCoOwner()
                                            .replace("{target}", target.getName()))
                                    .replace("{name}", partyData.getPlayerParty()));
                            return true;
                        }
                        if (args[1].equals("해임")) {
                            if (args.length == 1) {
                                for (String usage : config.usage()) {
                                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', usage));
                                }
                                return true;
                            }
                            partyData = new PartyData(player);
                            if (!partyData.isPartyOwner()) {
                                player.sendMessage(ChatColor.translateAlternateColorCodes('&', config.noPermission()));
                                return true;
                            }
                            if (!partyData.isPartyCoOwnerExist()) {
                                player.sendMessage(ChatColor.translateAlternateColorCodes('&', config.noPermission()));
                                return true;
                            }

                            Player coOwner = Bukkit.getPlayer(UUID.fromString(partyData.getCoOwner()));
                            partyData.dismissCoOwner();

                            player.sendMessage(ChatColor.translateAlternateColorCodes('&', config.dismissCoOwner()
                                            .replace("{target}", coOwner.getName()))
                                    .replace("{name}", partyData.getPlayerParty()));
                        }
                    }

                    default -> {
                        for (String usage : config.usage()) {
                            player.sendMessage(ChatColor.translateAlternateColorCodes('&', usage));
                        }
                        return true;
                    }
                }
            }
        } else {
            sender.sendMessage("§c플레이어만 사용 가능한 명령어입니다.");
            return true;
        }
        return false;
    }
}

