/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  de.simonsator.partyandfriends.api.pafplayers.OnlinePAFPlayer
 *  de.simonsator.partyandfriends.api.pafplayers.PAFPlayerManager
 *  de.simonsator.partyandfriends.api.party.PartyManager
 *  de.simonsator.partyandfriends.api.party.PlayerParty
 *  org.bukkit.entity.Player
 *  org.jetbrains.annotations.NotNull
 */
package com.andrei1058.bedwars.support.party;

import com.andrei1058.bedwars.api.party.Party;
import de.simonsator.partyandfriends.api.pafplayers.OnlinePAFPlayer;
import de.simonsator.partyandfriends.api.pafplayers.PAFPlayerManager;
import de.simonsator.partyandfriends.api.party.PartyManager;
import de.simonsator.partyandfriends.api.party.PlayerParty;
import java.util.ArrayList;
import java.util.List;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class PAF
implements Party {
    private PlayerParty getPAFParty(Player p) {
        if (PAFPlayerManager.getInstance() == null) {
            return null;
        }
        return PartyManager.getInstance().getParty(PAFPlayerManager.getInstance().getPlayer(p));
    }

    @Override
    public boolean hasParty(Player p) {
        return this.getPAFParty(p) != null;
    }

    @Override
    public int partySize(Player p) {
        PlayerParty party = this.getPAFParty(p);
        if (party == null) {
            return 0;
        }
        return party.getAllPlayers().size();
    }

    @Override
    public boolean isOwner(Player p) {
        OnlinePAFPlayer pafPlayer = PAFPlayerManager.getInstance().getPlayer(p);
        PlayerParty party = PartyManager.getInstance().getParty(pafPlayer);
        if (party == null) {
            return false;
        }
        return party.isLeader(pafPlayer);
    }

    @Override
    public List<Player> getMembers(Player owner) {
        ArrayList<Player> playerList = new ArrayList<Player>();
        PlayerParty party = this.getPAFParty(owner);
        if (party == null) {
            return playerList;
        }
        for (OnlinePAFPlayer players : party.getAllPlayers()) {
            playerList.add(players.getPlayer());
        }
        return playerList;
    }

    @Override
    public void createParty(Player owner, Player ... members) {
        OnlinePAFPlayer pafPlayer = PAFPlayerManager.getInstance().getPlayer(owner);
        PlayerParty party = PartyManager.getInstance().createParty(pafPlayer);
        party.setPrivateState(false);
        for (Player p1 : members) {
            party.addPlayer(PAFPlayerManager.getInstance().getPlayer(p1));
        }
        party.setPrivateState(true);
    }

    @Override
    public void addMember(Player owner, Player member) {
        PlayerParty party = PAFPlayerManager.getInstance().getPlayer(owner).getParty();
        party.setPrivateState(false);
        party.addPlayer(PAFPlayerManager.getInstance().getPlayer(member));
        party.setPrivateState(true);
    }

    @Override
    public void removeFromParty(Player member) {
        PAFPlayerManager.getInstance().getPlayer(member).getParty().leaveParty(PAFPlayerManager.getInstance().getPlayer(member));
    }

    @Override
    public void disband(Player owner) {
        PartyManager.getInstance().deleteParty(PartyManager.getInstance().getParty(PAFPlayerManager.getInstance().getPlayer(owner)));
    }

    @Override
    public boolean isMember(Player owner, Player check) {
        PlayerParty party = this.getPAFParty(owner);
        if (party == null) {
            return false;
        }
        return party.isInParty(PAFPlayerManager.getInstance().getPlayer(check));
    }

    @Override
    public void removePlayer(Player owner, Player target) {
        this.getPAFParty(owner).leaveParty(PAFPlayerManager.getInstance().getPlayer(target));
    }

    @Override
    public Player getOwner(Player member) {
        return this.getPAFParty(member).getLeader().getPlayer();
    }

    @Override
    public void promote(@NotNull Player owner, @NotNull Player target) {
        this.getPAFParty(owner).setLeader(PAFPlayerManager.getInstance().getPlayer(target));
    }

    @Override
    public boolean isInternal() {
        return false;
    }
}

