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
import com.andrei1058.bedwars.api.arena.team.ITeamAssigner;
import com.andrei1058.bedwars.api.events.gameplay.TeamAssignEvent;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;

public class TeamAssigner
implements ITeamAssigner {
    private final LinkedList<Player> skip = new LinkedList();

    @Override
    public void assignTeams(IArena arena) {
        if (arena.getPlayers().size() > arena.getMaxInTeam() && arena.getMaxInTeam() > 1) {
            LinkedList<List> teams = new LinkedList<List>();
            for (Player player : arena.getPlayers()) {
                List<Player> members = BedWars.getParty().getMembers(player);
                if (members == null || (members = new ArrayList<Player>(members)).isEmpty()) continue;
                members.removeIf(member -> !arena.isPlayer((Player)member));
                if (members.isEmpty()) continue;
                teams.add(members);
            }
            if (!teams.isEmpty()) {
                for (ITeam team : arena.getTeams()) {
                    teams.sort(Comparator.comparingInt(List::size));
                    if (((List)teams.get(0)).isEmpty()) break;
                    for (int i = 0; i < arena.getMaxInTeam() && team.getMembers().size() < arena.getMaxInTeam() && ((List)teams.get(0)).size() > i; ++i) {
                        Player toAdd = (Player)((List)teams.get(0)).remove(0);
                        TeamAssignEvent e = new TeamAssignEvent(toAdd, team, arena);
                        Bukkit.getPluginManager().callEvent((Event)e);
                        if (e.isCancelled()) continue;
                        toAdd.closeInventory();
                        team.addPlayers(toAdd);
                        this.skip.add(toAdd);
                    }
                }
            }
        }
        block3: for (Player remaining : arena.getPlayers()) {
            if (this.skip.contains(remaining)) continue;
            for (ITeam team : arena.getTeams()) {
                if (team.getMembers().size() >= arena.getMaxInTeam()) continue;
                TeamAssignEvent e = new TeamAssignEvent(remaining, team, arena);
                Bukkit.getPluginManager().callEvent((Event)e);
                if (e.isCancelled()) continue block3;
                remaining.closeInventory();
                team.addPlayers(remaining);
                continue block3;
            }
        }
    }
}

