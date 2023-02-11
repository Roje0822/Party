package net.lucynetwork.party.data;

import net.lucynetwork.party.PartyMain;
import net.starly.core.data.Config;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.UUID;

import static net.lucynetwork.party.data.PartyMapData.invitePartyNameMap;

public class PartyData {

    private Player player;
    private String name;
    private Config partyData;
    private Config playerData;
    private List<String> partyMembers;
    private StringData config = new StringData();


    public PartyData(Player player, String name) {
        this.player = player;
        this.name = name;
        this.playerData = new Config("data/" + player.getUniqueId(), PartyMain.getPlugin());
        this.partyData = new Config("party/" + name, PartyMain.getPlugin());
        this.partyMembers = partyData.getStringList("party.member");
    }


    public PartyData(Player player) {
        this.player = player;
        this.playerData = new Config("data/" + player.getUniqueId(), PartyMain.getPlugin());
        this.partyData = new Config("party/" + playerData.getString("party"), PartyMain.getPlugin());
        this.partyMembers = partyData.getStringList("party.member");
    }


    /**
     * 파티를 생성합니다
     */
    public void createParty() {
        partyData.setStringList("party.member", List.of(player.getUniqueId().toString()));
        partyData.setString("party.owner", player.getUniqueId().toString());
        playerData.setString("party", name);
    }


    /**
     * 파티에 참가합니다
     */
    public void joinParty() {
        partyData = new Config("party/" + invitePartyNameMap.get(player), PartyMain.getPlugin());
        partyMembers = partyData.getStringList("party.member");
        partyMembers.add(player.getUniqueId().toString());
        partyData.setStringList("party.member", partyMembers);
        playerData.setString("party", invitePartyNameMap.get(player));
        invitePartyNameMap.remove(player);
    }


    /**
     * 파티를 나갑니다
     */
    public void leaveParty() {
        partyMembers.remove("" + player.getUniqueId());
        partyData.setStringList( "party.member", partyMembers);
        playerData.setString("party", null);
    }


    /**
     * 파티를 해체합니다
     */
    public void disbandParty() {
        List<String> partyPlayer = partyData.getStringList("party.member");
        partyPlayer.forEach(uuid -> {
            Config playerData = new Config("data/" + uuid, PartyMain.getPlugin());
            playerData.setString("party", null);
        });
        partyData.delete();
    }

    /**
     * 부파티장 선임
     */
    public void electCoOwner(UUID uuid) {
        partyData.setString("party.coowner", uuid.toString());
    }

    /**
     * 부파티장 해임
     */
    public void removeCoOwner() {
        partyData.setString("party.coowner", null);
    }


    public String getCoOwner() {
        return partyData.getString("party.coowner");
    }


    public Boolean isCoOwner() {
        return partyData.getString("party.coowner").equals(player.getUniqueId().toString());
    }


    /**
     * 파티에 초대합니다
     */
    public void inviteParty(Player target) {
        invitePartyNameMap.put(target, getPlayerParty());
    }


    public void rejectParty() {
        invitePartyNameMap.remove(player);
    }


    /**
     * 파티에서 추방합니다
     */
    public void kickParty() {
        partyMembers.remove("" + player.getUniqueId());
        partyData.setStringList("party.member", partyMembers);
        playerData.setString("party", null);
    }


    /**
     * @return 해당 파티에 플레이어가 존재하는지 여부
     */
    public boolean isInThisParty() {
        partyMembers = partyData.getStringList("party.member");
        return partyMembers.contains("" + player.getUniqueId());
    }


    /**
     * @return 플레이어의 파티가 존재하는지 여부
     */
    public boolean isPlayerPartyExist() {
        if (playerData.getString("party") == null) {
            return false;
        } else {
            return true;
        }
    }


    /**
     * @return 플레이어가 파티의 주인인지 여부
     */
    public boolean isPartyOwner() {
        if (partyData.getString("party.owner").equals("" + player.getUniqueId())) {
            return true;
        } else {
            return false;
        }
    }


    /**
     * @return 파티이름이 이미 존재하는가
     */
    public boolean isPartyNameExist() {
        partyData = new Config("party/" + name, PartyMain.getPlugin());

        return partyData.isFileExist();
    }


    /**
     * @return 플레이어의 파티
     */
    public String getPlayerParty() {
        return playerData.getString("party");
    }


    public String getPartyOwner() {
        return partyData.getString("party.owner");
    }


    public List<String> getPartyMembers() {
        return partyData.getStringList("party.member");
    }


    /**
     * 파티 삭제
     */
    public void deleteParty() {
        partyData.delete();
    }


    public Boolean isPartyCoOwnerExist() {
        if (partyData.getString("party.coowner") == null) {
            return false;
        } else {
            return true;
        }
    }


}
