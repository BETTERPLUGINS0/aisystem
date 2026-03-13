/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.entity.Player
 *  org.jetbrains.annotations.Contract
 *  org.jetbrains.annotations.NotNull
 *  org.jetbrains.annotations.Nullable
 */
package com.andrei1058.bedwars.support.party;

import com.andrei1058.bedwars.api.language.Language;
import com.andrei1058.bedwars.api.language.Messages;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class Internal
implements com.andrei1058.bedwars.api.party.Party {
    private static List<Party> parites = new ArrayList<Party>();

    @Override
    public boolean hasParty(Player p) {
        for (Party party : Internal.getParites()) {
            if (!party.members.contains(p)) continue;
            return true;
        }
        return false;
    }

    @Override
    public int partySize(Player p) {
        for (Party party : Internal.getParites()) {
            if (!party.members.contains(p)) continue;
            return party.members.size();
        }
        return 0;
    }

    @Override
    public boolean isOwner(Player p) {
        for (Party party : Internal.getParites()) {
            if (!party.members.contains(p) || party.owner != p) continue;
            return true;
        }
        return false;
    }

    @Override
    public List<Player> getMembers(Player owner) {
        for (Party party : Internal.getParites()) {
            if (!party.members.contains(owner)) continue;
            return party.members;
        }
        return null;
    }

    @Override
    public void createParty(Player owner, Player ... members) {
        Party p = new Party(owner);
        p.addMember(owner);
        for (Player mem : members) {
            p.addMember(mem);
        }
    }

    @Override
    public void addMember(Player owner, Player member) {
        if (owner == null || member == null) {
            return;
        }
        Party p = this.getParty(owner);
        if (p == null) {
            return;
        }
        p.addMember(member);
    }

    @Override
    public void removeFromParty(Player member) {
        for (Party p : new ArrayList<Party>(Internal.getParites())) {
            if (p.owner == member) {
                this.disband(member);
                continue;
            }
            if (!p.members.contains(member)) continue;
            for (Player mem : p.members) {
                mem.sendMessage(Language.getMsg(mem, Messages.COMMAND_PARTY_LEAVE_SUCCESS).replace("{playername}", member.getName()).replace("{player}", member.getDisplayName()));
            }
            p.members.remove(member);
            if (p.members.isEmpty() || p.members.size() == 1) {
                this.disband(p.owner);
                parites.remove(p);
            }
            return;
        }
    }

    @Override
    public void disband(Player owner) {
        Party pa = this.getParty(owner);
        if (pa == null) {
            return;
        }
        for (Player p : pa.members) {
            p.sendMessage(Language.getMsg(p, Messages.COMMAND_PARTY_DISBAND_SUCCESS));
        }
        pa.members.clear();
        parites.remove(pa);
    }

    @Override
    public boolean isMember(Player owner, Player check) {
        for (Party p : parites) {
            if (p.owner != owner || !p.members.contains(check)) continue;
            return true;
        }
        return false;
    }

    @Override
    public void removePlayer(Player owner, Player target) {
        Party p = this.getParty(owner);
        if (p != null && p.members.contains(target)) {
            for (Player mem : p.members) {
                mem.sendMessage(Language.getMsg(mem, Messages.COMMAND_PARTY_REMOVE_SUCCESS).replace("{player}", target.getName()));
            }
            p.members.remove(owner);
            if (p.members.isEmpty() || p.members.size() == 1) {
                this.disband(p.owner);
                parites.remove(p);
            }
        }
    }

    @Override
    public Player getOwner(Player member) {
        for (Party party : Internal.getParites()) {
            if (!party.members.contains(member)) continue;
            return party.owner;
        }
        return null;
    }

    @Override
    public void promote(@NotNull Player owner, @NotNull Player target) {
        Party p = this.getParty(owner);
        if (p != null) {
            p.owner = target;
        }
    }

    @Override
    public boolean isInternal() {
        return true;
    }

    @Nullable
    private Party getParty(Player owner) {
        for (Party p : Internal.getParites()) {
            if (p.getOwner() != owner) continue;
            return p;
        }
        return null;
    }

    @NotNull
    @Contract(pure=true)
    public static List<Party> getParites() {
        return Collections.unmodifiableList(parites);
    }

    static class Party {
        private List<Player> members = new ArrayList<Player>();
        private Player owner;

        public Party(Player p) {
            this.owner = p;
            parites.add(this);
        }

        public Player getOwner() {
            return this.owner;
        }

        void addMember(Player p) {
            this.members.add(p);
        }
    }
}

