/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.command.CommandSender
 *  org.bukkit.command.ConsoleCommandSender
 *  org.bukkit.entity.Player
 */
package com.andrei1058.bedwars.commands.bedwars.subcmds.regular;

import com.andrei1058.bedwars.api.BedWars;
import com.andrei1058.bedwars.api.arena.GameState;
import com.andrei1058.bedwars.api.arena.IArena;
import com.andrei1058.bedwars.api.command.ParentCommand;
import com.andrei1058.bedwars.api.command.SubCommand;
import com.andrei1058.bedwars.arena.Arena;
import com.andrei1058.bedwars.arena.Misc;
import com.andrei1058.bedwars.arena.SetupSession;
import com.andrei1058.bedwars.commands.bedwars.MainCommand;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

public class CmdStats
extends SubCommand {
    private static ConcurrentHashMap<UUID, Long> statsCoolDown = new ConcurrentHashMap();

    public CmdStats(ParentCommand parent, String name) {
        super(parent, name);
        this.setPriority(16);
        this.showInList(false);
        this.setDisplayInfo(MainCommand.createTC("\u00a76 \u25aa \u00a77/" + MainCommand.getInstance().getName() + " " + this.getSubCommandName(), "/" + this.getParent().getName() + " " + this.getSubCommandName(), "\u00a7fOpens the stats GUI."));
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    @Override
    public boolean execute(String[] args, CommandSender s) {
        if (s instanceof ConsoleCommandSender) {
            return false;
        }
        Player p = (Player)s;
        IArena a = Arena.getArenaByPlayer(p);
        if (a != null && a.getStatus() != GameState.starting && a.getStatus() != GameState.waiting && !a.isSpectator(p)) {
            return false;
        }
        if (statsCoolDown.containsKey(p.getUniqueId())) {
            if (System.currentTimeMillis() - 3000L < statsCoolDown.get(p.getUniqueId())) return true;
            statsCoolDown.replace(p.getUniqueId(), System.currentTimeMillis());
        } else {
            statsCoolDown.put(p.getUniqueId(), System.currentTimeMillis());
        }
        Misc.openStatsGUI(p);
        return true;
    }

    @Override
    public List<String> getTabComplete() {
        return new ArrayList<String>();
    }

    @Override
    public boolean canSee(CommandSender s, BedWars api) {
        if (s instanceof ConsoleCommandSender) {
            return false;
        }
        Player p = (Player)s;
        if (Arena.isInArena(p)) {
            return false;
        }
        if (SetupSession.isInSetupSession(p.getUniqueId())) {
            return false;
        }
        return this.hasPermission(s);
    }

    public static ConcurrentHashMap<UUID, Long> getStatsCoolDown() {
        return statsCoolDown;
    }
}

