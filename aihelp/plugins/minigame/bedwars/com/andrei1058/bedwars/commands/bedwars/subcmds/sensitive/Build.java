/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  net.md_5.bungee.api.chat.ClickEvent$Action
 *  org.bukkit.command.CommandSender
 *  org.bukkit.command.ConsoleCommandSender
 *  org.bukkit.entity.Player
 */
package com.andrei1058.bedwars.commands.bedwars.subcmds.sensitive;

import com.andrei1058.bedwars.api.BedWars;
import com.andrei1058.bedwars.api.command.ParentCommand;
import com.andrei1058.bedwars.api.command.SubCommand;
import com.andrei1058.bedwars.arena.Arena;
import com.andrei1058.bedwars.arena.Misc;
import com.andrei1058.bedwars.arena.SetupSession;
import com.andrei1058.bedwars.commands.bedwars.MainCommand;
import com.andrei1058.bedwars.configuration.Permissions;
import com.andrei1058.bedwars.listeners.BreakPlace;
import java.util.List;
import net.md_5.bungee.api.chat.ClickEvent;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

public class Build
extends SubCommand {
    public Build(ParentCommand parent, String name) {
        super(parent, name);
        this.setPriority(9);
        this.showInList(true);
        this.setPermission(Permissions.PERMISSION_BUILD);
        this.setDisplayInfo(Misc.msgHoverClick("\u00a76 \u25aa \u00a77/" + this.getParent().getName() + " " + this.getSubCommandName() + "         \u00a78 - \u00a7ebuild permission", "\u00a7fEnable or disable build session \n\u00a7fso you can break or place blocks.", "/" + this.getParent().getName() + " " + this.getSubCommandName(), ClickEvent.Action.RUN_COMMAND));
    }

    @Override
    public boolean execute(String[] args, CommandSender s) {
        if (s instanceof ConsoleCommandSender) {
            return false;
        }
        Player p = (Player)s;
        if (!MainCommand.isLobbySet(p)) {
            return true;
        }
        if (BreakPlace.isBuildSession(p)) {
            p.sendMessage("\u00a76 \u25aa \u00a77You can't place and break blocks anymore!");
            BreakPlace.removeBuildSession(p);
        } else {
            p.sendMessage("\u00a76 \u25aa \u00a77You can place and break blocks now.");
            BreakPlace.addBuildSession(p);
        }
        return true;
    }

    @Override
    public List<String> getTabComplete() {
        return null;
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
}

