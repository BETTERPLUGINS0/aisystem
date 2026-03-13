/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.Bukkit
 *  org.bukkit.entity.Player
 *  org.bukkit.event.Event
 */
package com.andrei1058.bedwars.arena.team;

import com.andrei1058.bedwars.BedWars;
import com.andrei1058.bedwars.api.arena.IArena;
import com.andrei1058.bedwars.api.arena.team.ITeam;
import com.andrei1058.bedwars.api.events.gameplay.TeamAssignEvent;
import com.andrei1058.bedwars.arena.Arena;
import java.util.ArrayList;
import java.util.Collections;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;

public class LegacyTeamAssigner {
    private LegacyTeamAssigner() {
    }

    public static void assignTeams(IArena arena) {
        ArrayList<Player> skip = new ArrayList<Player>();
        ArrayList<Player> owners = new ArrayList<Player>();
        for (Player p : arena.getPlayers()) {
            if (!BedWars.getParty().hasParty(p) || !BedWars.getParty().isOwner(p)) continue;
            owners.add(p);
        }
        Collections.shuffle(arena.getTeams());
        for (Player p : arena.getPlayers()) {
            if (!owners.contains(p)) continue;
            for (ITeam iTeam : arena.getTeams()) {
                if (skip.contains(p) || iTeam.getSize() + BedWars.getParty().partySize(p) > arena.getMaxInTeam()) continue;
                skip.add(p);
                p.closeInventory();
                TeamAssignEvent e = new TeamAssignEvent(p, iTeam, arena);
                Bukkit.getPluginManager().callEvent((Event)e);
                if (!e.isCancelled()) {
                    iTeam.addPlayers(p);
                }
                for (Player mem : BedWars.getParty().getMembers(p)) {
                    IArena ia;
                    if (mem == p || (ia = Arena.getArenaByPlayer(mem)) == null || !ia.equals(arena)) continue;
                    TeamAssignEvent ee = new TeamAssignEvent(p, iTeam, arena);
                    Bukkit.getPluginManager().callEvent((Event)ee);
                    if (!e.isCancelled()) {
                        iTeam.addPlayers(mem);
                    }
                    skip.add(mem);
                    mem.closeInventory();
                }
            }
        }
        for (Player p : arena.getPlayers()) {
            if (skip.contains(p)) continue;
            ITeam addhere = arena.getTeams().get(0);
            for (ITeam t : arena.getTeams()) {
                if (t.getMembers().size() >= arena.getMaxInTeam() || t.getMembers().size() >= addhere.getMembers().size()) continue;
                addhere = t;
            }
            TeamAssignEvent teamAssignEvent = new TeamAssignEvent(p, addhere, arena);
            Bukkit.getPluginManager().callEvent((Event)teamAssignEvent);
            if (!teamAssignEvent.isCancelled()) {
                addhere.addPlayers(p);
            }
            p.closeInventory();
        }
    }
}

