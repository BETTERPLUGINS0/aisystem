/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  net.md_5.bungee.api.chat.ClickEvent$Action
 *  org.bukkit.command.CommandSender
 *  org.bukkit.entity.Player
 */
package com.andrei1058.bedwars.commands.bedwars.subcmds.sensitive;

import com.andrei1058.bedwars.api.BedWars;
import com.andrei1058.bedwars.api.command.ParentCommand;
import com.andrei1058.bedwars.api.command.SubCommand;
import com.andrei1058.bedwars.api.language.Language;
import com.andrei1058.bedwars.arena.Arena;
import com.andrei1058.bedwars.arena.Misc;
import com.andrei1058.bedwars.arena.SetupSession;
import com.andrei1058.bedwars.commands.bedwars.MainCommand;
import com.andrei1058.bedwars.configuration.Permissions;
import java.util.List;
import net.md_5.bungee.api.chat.ClickEvent;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Reload
extends SubCommand {
    public Reload(ParentCommand parent, String name) {
        super(parent, name);
        this.setPriority(11);
        this.showInList(true);
        this.setPermission(Permissions.PERMISSION_RELOAD);
        this.setDisplayInfo(Misc.msgHoverClick("\u00a76 \u25aa \u00a77/" + this.getParent().getName() + " " + this.getSubCommandName() + "       \u00a78 - \u00a7ereload messages", "\u00a7fReload messages.\n\u00a7cNot recommended!", "/" + this.getParent().getName() + " " + this.getSubCommandName(), ClickEvent.Action.RUN_COMMAND));
    }

    @Override
    public boolean execute(String[] args, CommandSender s) {
        if (s instanceof Player ? !MainCommand.isLobbySet((Player)s) : !MainCommand.isLobbySet(null)) {
            return true;
        }
        for (Language l : Language.getLanguages()) {
            l.reload();
            s.sendMessage("\u00a76 \u25aa \u00a77" + l.getLangName() + " reloaded!");
        }
        return true;
    }

    @Override
    public List<String> getTabComplete() {
        return null;
    }

    @Override
    public boolean canSee(CommandSender s, BedWars api) {
        if (s instanceof Player) {
            Player p = (Player)s;
            if (Arena.isInArena(p)) {
                return false;
            }
            if (SetupSession.isInSetupSession(p.getUniqueId())) {
                return false;
            }
        }
        return this.hasPermission(s);
    }
}

