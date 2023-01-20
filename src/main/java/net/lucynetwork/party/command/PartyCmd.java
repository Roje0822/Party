package net.lucynetwork.party.command;

import net.lucynetwork.party.api.PartyAPI;
import net.lucynetwork.party.data.PartyData;
import net.lucynetwork.party.data.StringData;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitScheduler;

import static net.lucynetwork.party.data.PartyMapData.invitePartyNameMap;

public class PartyCmd implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

        if (sender instanceof Player player) {

            String partyName;
            StringData config = new StringData();
            PartyData partyData;
            Player target;

            if (args.length == 0) {
                partyData = new PartyData(player);
                player.sendMessage(player.getName() + "님의 파티 정보");
                player.sendMessage("파티 이름: " + partyData.getPlayerParty());
                player.sendMessage("파티장: " + partyData.getPartyOwner());
                player.sendMessage("파티원: " + partyData.getPartyMembers());
            }

            switch (args[0]) {

                case "생성" -> {
                    if (args.length == 1) {
                        for (String usage : config.usage()) {
                            player.sendMessage(ChatColor.translateAlternateColorCodes('&', usage));
                        }
                        return true;
                    }

                    partyName = args[1];
                    partyData = new PartyData(player, partyName);

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


                    if (partyData.isPlayerPartyExist()) {
                        player.sendMessage(ChatColor.translateAlternateColorCodes('&', config.targetPartyExist())
                                .replace("{target}", target.getName()));
                        return true;
                    }
                    partyData = new PartyData(player);
                    if (!partyData.isPartyOwner()) {
                        player.sendMessage(ChatColor.translateAlternateColorCodes('&', config.notOwner()));
                        return true;
                    }
                    if (target.getName().equals(player.getName())) {
                        player.sendMessage(ChatColor.translateAlternateColorCodes('&', config.inviteSelf()));
                        System.out.println(2);
                        return true;
                    }
                    if (invitePartyNameMap.containsKey(target)) {
                        player.sendMessage(ChatColor.translateAlternateColorCodes('&', config.inviteAlready())
                                .replace("{target}", target.getName()));
                        System.out.println(4);
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
                        player.sendMessage(config.notinvite());
                        return true;
                    }
                    partyData = new PartyData(player);

                    partyData.joinParty();
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', config.acceptParty()
                            .replace("{name}", invitePartyNameMap.get(player))));
                    return true;
                }

                case "거절" -> {
                    if (!invitePartyNameMap.containsKey(player)) {
                        player.sendMessage(config.notinvite());
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
                        player.sendMessage(config.noparty());
                        return true;
                    }

                    partyData.leaveParty();
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', config.leaveParty()
                            .replace("{name}", partyData.getPlayerParty())));
                    return true;
                }

                case "강퇴" -> {
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
                    if (!new PartyData(player).isPartyOwner()) {
                        player.sendMessage(config.notOwner());
                        return true;
                    }

                    target = player.getServer().getPlayer(args[1]);
                    partyData = new PartyData(target);

                    if (!partyData.isInThisParty()) {
                        player.sendMessage(ChatColor.translateAlternateColorCodes('&', config.notInParty()
                                        .replace("{target}", target.getName()))
                                .replace("{name}", new PartyData(player).getPlayerParty()));
                        return true;
                    }
                    if (target == player) {
                        player.sendMessage(ChatColor.translateAlternateColorCodes('&', config.kickSelf()));
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
                        player.sendMessage(ChatColor.translateAlternateColorCodes('&', config.notOwner()));
                        return true;
                    }
                    partyData.disbandParty();
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', config.disbandParty()
                            .replace("{name}", partyData.getPlayerParty())));
                    return true;
                }
                default -> {
                    for (String usage : config.usage()) {
                        player.sendMessage(ChatColor.translateAlternateColorCodes('&', usage));
                    }
                }
                case "테스트" -> {
                    PartyAPI partyAPI = new PartyAPI();
                    partyAPI.getPartyMembers(partyAPI.getPlayerParty(player)).forEach(player::sendMessage);
                    System.out.println(partyAPI.getPlayerParty(player));
                }
            }
        } else {
            sender.sendMessage("플레이어만 사용 가능한 명령어입니다.");
            return true;
        }
        return false;
    }
}

