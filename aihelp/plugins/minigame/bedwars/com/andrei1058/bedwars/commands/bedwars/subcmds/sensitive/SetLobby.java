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

import com.andrei1058.bedwars.BedWars;
import com.andrei1058.bedwars.api.command.ParentCommand;
import com.andrei1058.bedwars.api.command.SubCommand;
import com.andrei1058.bedwars.arena.Arena;
import com.andrei1058.bedwars.arena.Misc;
import com.andrei1058.bedwars.arena.SetupSession;
import com.andrei1058.bedwars.commands.bedwars.MainCommand;
import com.andrei1058.bedwars.configuration.Permissions;
import java.util.List;
import net.md_5.bungee.api.chat.ClickEvent;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

public class SetLobby
extends SubCommand {
    public SetLobby(ParentCommand parent, String name) {
        super(parent, name);
        this.setPriority(1);
        this.showInList(true);
        this.setPermission(Permissions.PERMISSION_SETUP_ARENA);
        this.setDisplayInfo(Misc.msgHoverClick("\u00a76 \u25aa \u00a77/" + MainCommand.getInstance().getName() + " " + this.getSubCommandName() + (BedWars.config.getLobbyWorldName().isEmpty() ? " \u00a7c(not set)" : " \u00a7a(set)"), "\u00a7aSet the main lobby. \u00a7fThis is required but\n\u00a7fif you are going to use the server in \u00a7eBUNGEE \u00a7fmode\n\u00a7fthe lobby location will \u00a7enot \u00a7fbe used.\n\u00a7eType again to replace the old spawn location.", "/" + this.getParent().getName() + " " + this.getSubCommandName(), ClickEvent.Action.RUN_COMMAND));
    }

    @Override
    public boolean execute(String[] args, CommandSender s) {
        if (s instanceof ConsoleCommandSender) {
            return false;
        }
        Player p = (Player)s;
        if (SetupSession.isInSetupSession(p.getUniqueId())) {
            p.sendMessage("\u00a76 \u25aa \u00a74This command can't be used in arenas. It is meant for the main lobby!");
            return true;
        }
        BedWars.config.saveConfigLoc("lobbyLoc", p.getLocation());
        p.sendMessage("\u00a76 \u25aa \u00a77Lobby location set!");
        BedWars.config.reload();
        BedWars.setLobbyWorld(p.getLocation().getWorld().getName());
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
        if (Arena.isInArena(p)) {
            return false;
        }
        if (SetupSession.isInSetupSession(p.getUniqueId())) {
            return false;
        }
        if (!BedWars.getLobbyWorld().isEmpty()) {
            return false;
        }
        return this.hasPermission(s);
    }
}

