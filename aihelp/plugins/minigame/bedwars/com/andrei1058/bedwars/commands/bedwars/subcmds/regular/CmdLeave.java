/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.command.CommandSender
 *  org.bukkit.command.ConsoleCommandSender
 *  org.bukkit.entity.Player
 */
package com.andrei1058.bedwars.commands.bedwars.subcmds.regular;

import com.andrei1058.bedwars.BedWars;
import com.andrei1058.bedwars.api.arena.IArena;
import com.andrei1058.bedwars.api.command.ParentCommand;
import com.andrei1058.bedwars.api.command.SubCommand;
import com.andrei1058.bedwars.api.server.ServerType;
import com.andrei1058.bedwars.arena.Arena;
import com.andrei1058.bedwars.arena.Misc;
import com.andrei1058.bedwars.arena.SetupSession;
import com.andrei1058.bedwars.commands.bedwars.MainCommand;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

public class CmdLeave
extends SubCommand {
    private static HashMap<UUID, Long> delay = new HashMap();

    public CmdLeave(ParentCommand parent, String name) {
        super(parent, name);
        this.setPriority(20);
        this.showInList(false);
        this.setDisplayInfo(MainCommand.createTC("\u00a76 \u25aa \u00a77/" + MainCommand.getInstance().getName() + " leave", "/" + this.getParent().getName() + " " + this.getSubCommandName(), "\u00a7fLeave an arena."));
    }

    @Override
    public boolean execute(String[] args, CommandSender s) {
        if (s instanceof ConsoleCommandSender) {
            return false;
        }
        Player p = (Player)s;
        if (CmdLeave.cancel(p.getUniqueId())) {
            return true;
        }
        CmdLeave.update(p.getUniqueId());
        IArena a = Arena.getArenaByPlayer(p);
        Misc.moveToLobbyOrKick(p, a, a != null && a.isSpectator(p.getUniqueId()));
        return true;
    }

    @Override
    public List<String> getTabComplete() {
        return null;
    }

    @Override
    public boolean canSee(CommandSender s, com.andrei1058.bedwars.api.BedWars api) {
        if (s instanceof ConsoleCommandSender) {
            return false;
        }
        Player p = (Player)s;
        if (BedWars.getServerType() == ServerType.SHARED && !Arena.isInArena(p)) {
            return false;
        }
        if (SetupSession.isInSetupSession(p.getUniqueId())) {
            return false;
        }
        return this.hasPermission(s);
    }

    private static boolean cancel(UUID player) {
        return delay.getOrDefault(player, 0L) > System.currentTimeMillis();
    }

    private static void update(UUID player) {
        if (delay.containsKey(player)) {
            delay.replace(player, System.currentTimeMillis() + 2500L);
            return;
        }
        delay.put(player, System.currentTimeMillis() + 2500L);
    }
}

