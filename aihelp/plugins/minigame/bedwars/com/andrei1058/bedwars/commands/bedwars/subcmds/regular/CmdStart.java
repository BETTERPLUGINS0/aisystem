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
import com.andrei1058.bedwars.api.arena.GameState;
import com.andrei1058.bedwars.api.arena.IArena;
import com.andrei1058.bedwars.api.command.ParentCommand;
import com.andrei1058.bedwars.api.command.SubCommand;
import com.andrei1058.bedwars.api.language.Language;
import com.andrei1058.bedwars.api.language.Messages;
import com.andrei1058.bedwars.arena.Arena;
import com.andrei1058.bedwars.arena.SetupSession;
import com.andrei1058.bedwars.commands.bedwars.MainCommand;
import com.andrei1058.bedwars.configuration.Permissions;
import java.util.List;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

public class CmdStart
extends SubCommand {
    public CmdStart(ParentCommand parent, String name) {
        super(parent, name);
        this.setPriority(15);
        this.showInList(true);
        this.setDisplayInfo(MainCommand.createTC("\u00a76 \u25aa \u00a77/" + MainCommand.getInstance().getName() + " " + this.getSubCommandName() + " \u00a78 - \u00a7eforce start an arena", "/" + this.getParent().getName() + " " + this.getSubCommandName(), "\u00a7fForcestart an arena.\n\u00a7fPermission: \u00a7c" + Permissions.PERMISSION_FORCESTART));
    }

    @Override
    public boolean execute(String[] args, CommandSender s) {
        if (s instanceof ConsoleCommandSender) {
            return false;
        }
        Player p = (Player)s;
        IArena a = Arena.getArenaByPlayer(p);
        if (a == null) {
            p.sendMessage(Language.getMsg(p, Messages.COMMAND_FORCESTART_NOT_IN_GAME));
            return true;
        }
        if (!a.isPlayer(p)) {
            p.sendMessage(Language.getMsg(p, Messages.COMMAND_FORCESTART_NOT_IN_GAME));
            return true;
        }
        if (!p.hasPermission(Permissions.PERMISSION_ALL) && !p.hasPermission(Permissions.PERMISSION_FORCESTART)) {
            p.sendMessage(Language.getMsg(p, Messages.COMMAND_FORCESTART_NO_PERM));
            return true;
        }
        if (a.getStatus() == GameState.playing) {
            return true;
        }
        if (a.getStatus() == GameState.restarting) {
            return true;
        }
        if (a.getStartingTask() == null) {
            if (args.length == 1 && args[0].equalsIgnoreCase("debug") && s.isOp()) {
                a.changeStatus(GameState.starting);
                BedWars.debug = true;
            } else {
                return true;
            }
        }
        if (a.getStartingTask().getCountdown() < 5) {
            return true;
        }
        a.getStartingTask().setCountdown(5);
        p.sendMessage(Language.getMsg(p, Messages.COMMAND_FORCESTART_SUCCESS));
        return true;
    }

    @Override
    public List<String> getTabComplete() {
        return null;
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    @Override
    public boolean canSee(CommandSender s, com.andrei1058.bedwars.api.BedWars api) {
        if (s instanceof ConsoleCommandSender) {
            return false;
        }
        Player p = (Player)s;
        IArena a = Arena.getArenaByPlayer(p);
        if (a == null) return false;
        GameState status = a.getStatus();
        if (status != GameState.waiting && status != GameState.starting) return false;
        if (!a.isPlayer(p)) {
            return false;
        }
        if (!SetupSession.isInSetupSession(p.getUniqueId())) return s.hasPermission(Permissions.PERMISSION_FORCESTART);
        return false;
    }
}

