package net.lucynetwork.party.data;

import net.lucynetwork.lucycore.data.Config;
import net.lucynetwork.party.PartyMain;

import java.util.Collections;
import java.util.List;

public class StringData {

    private Config config = new Config("config", PartyMain.getPlugin());
    private String prefix = config.getString("prefix");

    public List<String> usage() {
        return config.getStringList("messages.usage");
    }


    // 파티 생성
    public String createParty() {
        return prefix + config.getString("messages.party.createparty");
    }


    // 파티 참가
    public String joinParty() {
        return prefix + config.getString("messages.party.joinparty");
    }


    // 파티 나가기
    public String leaveParty() {
        return prefix + config.getString("messages.party.leaveparty");
    }


    // 파티 해체
    public String disbandParty() {
        return prefix + config.getString("messages.party.disbandparty");
    }


    // 파티 추방 (파티장)
    public String kickParty() {
        return prefix + config.getString("messages.party.kickparty");
    }


    // 파티 추방당함 (target)
    public String kickedParty() {
        return prefix + config.getString("messages.party.kickedparty");
    }


    // 파티 초대
    public String inviteParty() {
        return prefix + config.getString("messages.party.inviteparty");
    }


    // 파티 초대 받음
    public String invitedParty() {
        return prefix + config.getString("messages.party.invitedparty");
    }


    // 파티 초대 거절
    public String rejectParty() {
        return prefix + config.getString("messages.party.rejectparty");
    }


    // 파티 초대 수락
    public String acceptParty() {
        return prefix + config.getString("messages.party.acceptparty");
    }

    // 파티 정보
    public String infoPartyPlayer() {
        return config.getString("messages.party.infoparty.player");
    }


    public String infoPartyName() {
        return config.getString("messages.party.infoparty.name");
    }


    public String infoPartyOwner() {
        return config.getString("messages.party.infoparty.partyowner");
    }


    public String infoPartySubOwner() {
        return config.getString("messages.party.infoparty.subpartyowner");
    }


    public String infoPartyMember() {
        return config.getString("messages.party.infoparty.partymember");
    }


    public String electCoOwner() {
        return config.getString("messages.party.electcoowner");
    }


    public String dismissCoOwner() {
        return config.getString("messages.party.dismisscoowner");
    }







    // 초대 되지 않음
    public String notinvite() {
        return prefix + config.getString("messages.errors.notinvite");
    }



    // 파티가 없음
    public String noparty() {
        return prefix + config.getString("messages.errors.noparty");
    }


    // 파티이름 입력
    public String enterPartyName() {
        return prefix + config.getString("messages.errors.enterpartyname");
    }


    // 잘못된 파티 이름
    public String wrongPartyName() {
        return prefix + config.getString("messages.errors.wrongpartyname");
    }


    // 이미 파티가 있음
    public String playerPartyExist() {
        return prefix + config.getString("messages.errors.ispartyexist");
    }


    // 이미 파티가 있음 (target)
    public String targetPartyExist() {
        return prefix + config.getString("messages.errors.targetpartyexist");
    }


    // 그 파티이름이 이미 존재함
    public String partyNameExist() {
        return prefix + config.getString("messages.errors.partynameexist");
    }


    // 스스로 초대함
    public String inviteSelf() {
        return prefix + config.getString("messages.errors.inviteself");
    }


    // 플레이어를 찾을 수 없음 (초대 or 추방)
    public String playerNotFound() {
        return prefix + config.getString("messages.errors.playernotfound");
    }

    // 초대 만료 (player)
    public String expire() {
        return prefix + config.getString("messages.errors.expire");
    }


    // 초대 만료됨 (target)
    public String expired() {
        return prefix + config.getString("messages.errors.expired");
    }


    // 이미 초대됨
    public String inviteAlready() {
        return prefix + config.getString("messages.errors.invitealready");
    }


    // 해당 파티에 타겟이 가입되어있지 않음
    public String notInParty() {
        return prefix + config.getString("messages.errors.notinparty");
    }


    public String kickSelf() {
        return prefix + config.getString("messages.errors.kickself");
    }


    public String kickPermission() {
        return prefix + config.getString("messages.errors.kickpermission");
    }


    public int inviteTime() {
        return config.getInt("time.invite");
    }


    public String ownerLeave() {
        return prefix + config.getString("messages.errors.ownerleave");
    }


    public String coOwnerExist() {
        return prefix + config.getString("messages.errors.coownerexist");
    }


    public String coOwnerSelf() {
        return prefix + config.getString("messages.errors.coownerself");
    }


    public String noPermission() {
        return prefix + config.getString("messages.errors.nopermission");
    }



}
