/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  de.simonsator.partyandfriends.spigot.api.pafplayers.PAFPlayer
 *  de.simonsator.partyandfriends.spigot.api.pafplayers.PAFPlayerManager
 *  de.simonsator.partyandfriends.spigot.api.party.PartyManager
 *  de.simonsator.partyandfriends.spigot.api.party.PlayerParty
 *  org.bukkit.Bukkit
 *  org.bukkit.entity.Player
 *  org.jetbrains.annotations.NotNull
 */
package com.andrei1058.bedwars.support.party;

import com.andrei1058.bedwars.api.party.Party;
import de.simonsator.partyandfriends.spigot.api.pafplayers.PAFPlayer;
import de.simonsator.partyandfriends.spigot.api.pafplayers.PAFPlayerManager;
import de.simonsator.partyandfriends.spigot.api.party.PartyManager;
import de.simonsator.partyandfriends.spigot.api.party.PlayerParty;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class PAFBungeecordRedisApi
implements Party {
    private PlayerParty getPAFParty(Player p) {
        if (PAFPlayerManager.getInstance() == null) {
            return null;
        }
        return PartyManager.getInstance().getParty(PAFPlayerManager.getInstance().getPlayer(p.getUniqueId()));
    }

    @Override
    public boolean hasParty(Player p) {
        return this.getPAFParty(p) != null;
    }

    @Override
    public int partySize(Player p) {
        return this.getMembers(p).size();
    }

    @Override
    public boolean isOwner(Player p) {
        PAFPlayer pafPlayer = PAFPlayerManager.getInstance().getPlayer(p.getUniqueId());
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
        for (PAFPlayer players : party.getAllPlayers()) {
            Player bukkitPlayer = Bukkit.getPlayer((UUID)players.getUniqueId());
            if (bukkitPlayer == null) continue;
            playerList.add(bukkitPlayer);
        }
        return playerList;
    }

    @Override
    public void createParty(Player owner, Player ... members) {
    }

    @Override
    public void addMember(Player owner, Player member) {
    }

    @Override
    public void removeFromParty(Player member) {
    }

    @Override
    public void disband(Player owner) {
    }

    @Override
    public boolean isMember(Player owner, Player check) {
        PlayerParty party = this.getPAFParty(owner);
        if (party == null) {
            return false;
        }
        return party.isInParty(PAFPlayerManager.getInstance().getPlayer(check.getUniqueId()));
    }

    @Override
    public void removePlayer(Player owner, Player target) {
    }

    @Override
    public Player getOwner(Player member) {
        return Bukkit.getPlayer((UUID)this.getPAFParty(member).getLeader().getUniqueId());
    }

    @Override
    public void promote(@NotNull Player owner, @NotNull Player target) {
    }

    @Override
    public boolean isInternal() {
        return false;
    }
}

