/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  com.alessiodp.parties.api.Parties
 *  com.alessiodp.parties.api.interfaces.PartiesAPI
 *  com.alessiodp.parties.api.interfaces.Party
 *  com.alessiodp.parties.api.interfaces.PartyPlayer
 *  org.bukkit.Bukkit
 *  org.bukkit.entity.Player
 *  org.jetbrains.annotations.NotNull
 */
package com.andrei1058.bedwars.support.party;

import com.alessiodp.parties.api.Parties;
import com.alessiodp.parties.api.interfaces.PartiesAPI;
import com.alessiodp.parties.api.interfaces.PartyPlayer;
import com.andrei1058.bedwars.BedWars;
import com.andrei1058.bedwars.api.party.Party;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class PartiesAdapter
implements Party {
    private final PartiesAPI api = Parties.getApi();
    private static final int requiredRankToSelect = BedWars.config.getInt("party-settings.alessioDP-choose-arena-rank");

    @Override
    public boolean hasParty(Player p) {
        PartyPlayer pp = this.api.getPartyPlayer(p.getUniqueId());
        return pp != null && pp.isInParty();
    }

    @Override
    public int partySize(Player p) {
        com.alessiodp.parties.api.interfaces.Party party;
        PartyPlayer partyPlayer;
        if (this.hasParty(p) && (partyPlayer = this.api.getPartyPlayer(p.getUniqueId())) != null && partyPlayer.getPartyId() != null && null != (party = this.api.getParty(partyPlayer.getPartyId()))) {
            return party.getOnlineMembers().size();
        }
        return 0;
    }

    @Override
    public boolean isOwner(Player p) {
        PartyPlayer pp = this.api.getPartyPlayer(p.getUniqueId());
        if (pp == null || pp.getPartyId() == null) {
            return false;
        }
        return pp.getRank() >= requiredRankToSelect;
    }

    @Override
    public List<Player> getMembers(Player p) {
        PartyPlayer pp;
        ArrayList<Player> players = new ArrayList<Player>();
        if (this.hasParty(p) && null != (pp = this.api.getPartyPlayer(p.getUniqueId())) && pp.getPartyId() != null) {
            com.alessiodp.parties.api.interfaces.Party party = this.api.getParty(pp.getPartyId());
            for (PartyPlayer member : party.getOnlineMembers()) {
                players.add(Bukkit.getPlayer((UUID)member.getPlayerUUID()));
            }
        }
        return players;
    }

    @Override
    public void createParty(Player owner, Player ... members) {
        com.alessiodp.parties.api.interfaces.Party party;
        boolean created;
        PartyPlayer partyOwner;
        if (!this.api.isBungeeCordEnabled() && null != (partyOwner = this.api.getPartyPlayer(owner.getUniqueId())) && !partyOwner.isInParty() && (created = this.api.createParty(null, partyOwner)) && null != partyOwner.getPartyId() && null != (party = this.api.getParty(partyOwner.getPartyId()))) {
            for (Player player1 : members) {
                PartyPlayer partyPlayer = this.api.getPartyPlayer(player1.getUniqueId());
                if (null == partyPlayer || partyPlayer.isInParty()) continue;
                party.addMember(partyPlayer);
            }
        }
    }

    @Override
    public void addMember(Player owner, Player member) {
        PartyPlayer partyMember;
        com.alessiodp.parties.api.interfaces.Party party;
        PartyPlayer partyPlayer;
        if (!this.api.isBungeeCordEnabled() && null != (partyPlayer = this.api.getPartyPlayer(owner.getUniqueId())) && null != partyPlayer.getPartyId() && null != (party = this.api.getParty(partyPlayer.getPartyId())) && null != (partyMember = this.api.getPartyPlayer(member.getUniqueId()))) {
            party.addMember(partyMember);
        }
    }

    @Override
    public void removeFromParty(Player member) {
        com.alessiodp.parties.api.interfaces.Party party;
        PartyPlayer partyMember = this.api.getPartyPlayer(member.getUniqueId());
        if (null != partyMember && null != partyMember.getPartyId() && null != (party = this.api.getParty(partyMember.getPartyId()))) {
            party.removeMember(partyMember);
        }
    }

    @Override
    public void disband(Player owner) {
        com.alessiodp.parties.api.interfaces.Party party;
        PartyPlayer partyMember = this.api.getPartyPlayer(owner.getUniqueId());
        if (null != partyMember && null != partyMember.getPartyId() && null != (party = this.api.getParty(partyMember.getPartyId()))) {
            party.delete();
        }
    }

    @Override
    public boolean isMember(Player owner, Player check) {
        if (!this.hasParty(owner) || !this.hasParty(check)) {
            return false;
        }
        return this.api.areInTheSameParty(owner.getUniqueId(), check.getUniqueId());
    }

    @Override
    public void removePlayer(Player owner, Player target) {
        PartyPlayer targetPlayer;
        com.alessiodp.parties.api.interfaces.Party party;
        PartyPlayer player = this.api.getPartyPlayer(owner.getUniqueId());
        if (null != player && null != player.getPartyId() && null != (party = this.api.getParty(player.getPartyId())) && null != (targetPlayer = this.api.getPartyPlayer(target.getUniqueId()))) {
            party.removeMember(targetPlayer);
        }
    }

    @Override
    public Player getOwner(Player member) {
        PartyPlayer partyPlayer = this.api.getPartyPlayer(member.getUniqueId());
        return Bukkit.getPlayer((UUID)Objects.requireNonNull(this.api.getParty(Objects.requireNonNull(partyPlayer).getPartyId())).getLeader());
    }

    @Override
    public void promote(@NotNull Player owner, @NotNull Player target) {
    }

    @Override
    public boolean isInternal() {
        return false;
    }
}

