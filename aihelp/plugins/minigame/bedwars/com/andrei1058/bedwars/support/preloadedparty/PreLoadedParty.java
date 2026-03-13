/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.Bukkit
 *  org.bukkit.entity.Player
 */
package com.andrei1058.bedwars.support.preloadedparty;

import com.andrei1058.bedwars.BedWars;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class PreLoadedParty {
    private String owner;
    private List<Player> members = new ArrayList<Player>();
    private static ConcurrentHashMap<String, PreLoadedParty> preLoadedParties = new ConcurrentHashMap();

    public PreLoadedParty(String owner) {
        PreLoadedParty plp = PreLoadedParty.getPartyByOwner(owner);
        if (plp != null) {
            plp.clean();
        }
        this.owner = owner;
        preLoadedParties.put(owner, this);
    }

    public static PreLoadedParty getPartyByOwner(String owner) {
        return preLoadedParties.getOrDefault(owner, null);
    }

    public void addMember(Player player) {
        this.members.add(player);
    }

    public void teamUp() {
        if (this.owner == null) {
            return;
        }
        Player owner = Bukkit.getPlayer((String)this.owner);
        if (owner == null) {
            return;
        }
        if (!owner.isOnline()) {
            return;
        }
        for (Player player : this.members) {
            if (player.getName().equalsIgnoreCase(this.owner)) continue;
            BedWars.getParty().addMember(owner, player);
        }
        preLoadedParties.remove(this.owner);
    }

    public static ConcurrentHashMap<String, PreLoadedParty> getPreLoadedParties() {
        return preLoadedParties;
    }

    public void clean() {
        preLoadedParties.remove(this.owner);
    }
}

