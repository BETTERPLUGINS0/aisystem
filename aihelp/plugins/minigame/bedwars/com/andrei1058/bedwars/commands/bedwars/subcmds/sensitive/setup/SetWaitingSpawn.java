/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.Bukkit
 *  org.bukkit.command.CommandSender
 *  org.bukkit.command.ConsoleCommandSender
 *  org.bukkit.entity.Player
 */
package com.andrei1058.bedwars.commands.bedwars.subcmds.sensitive.setup;

import com.andrei1058.bedwars.BedWars;
import com.andrei1058.bedwars.api.command.ParentCommand;
import com.andrei1058.bedwars.api.command.SubCommand;
import com.andrei1058.bedwars.api.server.SetupType;
import com.andrei1058.bedwars.arena.SetupSession;
import com.andrei1058.bedwars.configuration.Permissions;
import java.util.List;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

public class SetWaitingSpawn
extends SubCommand {
    public SetWaitingSpawn(ParentCommand parent, String name) {
        super(parent, name);
        this.setArenaSetupCommand(true);
        this.setPermission(Permissions.PERMISSION_SETUP_ARENA);
    }

    @Override
    public boolean execute(String[] args, CommandSender s) {
        if (s instanceof ConsoleCommandSender) {
            return false;
        }
        Player p = (Player)s;
        SetupSession ss = SetupSession.getSession(p.getUniqueId());
        if (ss == null) {
            s.sendMessage("\u00a7c \u25aa \u00a77You're not in a setup session!");
            return true;
        }
        p.sendMessage("\u00a76 \u25aa \u00a77Waiting spawn set for \u00a7e" + ss.getWorldName() + "\u00a77!");
        ss.getConfig().saveArenaLoc("waiting.Loc", p.getLocation());
        if (ss.getSetupType() == SetupType.ASSISTED) {
            Bukkit.dispatchCommand((CommandSender)s, (String)(BedWars.mainCmd + " autocreateteams"));
        } else {
            Bukkit.dispatchCommand((CommandSender)s, (String)BedWars.mainCmd);
        }
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
        if (!SetupSession.isInSetupSession(p.getUniqueId())) {
            return false;
        }
        return this.hasPermission(s);
    }
}

