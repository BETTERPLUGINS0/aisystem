/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.Bukkit
 *  org.bukkit.command.CommandSender
 *  org.bukkit.command.ConsoleCommandSender
 *  org.bukkit.entity.Player
 *  org.bukkit.plugin.Plugin
 */
package com.andrei1058.bedwars.commands.bedwars.subcmds.regular;

import com.andrei1058.bedwars.BedWars;
import com.andrei1058.bedwars.api.command.ParentCommand;
import com.andrei1058.bedwars.api.command.SubCommand;
import com.andrei1058.bedwars.api.language.Language;
import com.andrei1058.bedwars.api.language.Messages;
import com.andrei1058.bedwars.arena.Arena;
import com.andrei1058.bedwars.arena.SetupSession;
import com.andrei1058.bedwars.commands.bedwars.MainCommand;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

public class CmdLang
extends SubCommand {
    public CmdLang(ParentCommand parent, String name) {
        super(parent, name);
        this.setPriority(18);
        this.showInList(false);
        this.setDisplayInfo(MainCommand.createTC("\u00a76 \u25aa \u00a77/" + MainCommand.getInstance().getName() + " " + this.getSubCommandName(), "/" + this.getParent().getName() + " " + this.getSubCommandName(), "\u00a7fChange your language."));
    }

    /*
     * Enabled aggressive block sorting
     */
    @Override
    public boolean execute(String[] args, CommandSender s) {
        if (s instanceof ConsoleCommandSender) {
            return false;
        }
        Player p = (Player)s;
        if (Arena.getArenaByPlayer(p) != null) {
            return false;
        }
        if (args.length == 0) {
            p.sendMessage(Language.getMsg(p, Messages.COMMAND_LANG_LIST_HEADER));
            Iterator<Language> iterator = Language.getLanguages().iterator();
            while (true) {
                if (!iterator.hasNext()) {
                    p.sendMessage(Language.getMsg(p, Messages.COMMAND_LANG_USAGE));
                    return true;
                }
                Language l = iterator.next();
                p.sendMessage(Language.getMsg(p, Messages.COMMAND_LANG_LIST_FORMAT).replace("{iso}", l.getIso()).replace("{name}", l.getLangName()));
            }
        }
        if (!Language.isLanguageExist(args[0])) {
            p.sendMessage(Language.getMsg(p, Messages.COMMAND_LANG_SELECTED_NOT_EXIST));
            return true;
        }
        if (Arena.getArenaByPlayer(p) != null) {
            p.sendMessage(Language.getMsg(p, Messages.COMMAND_LANG_USAGE_DENIED));
            return true;
        }
        if (Language.setPlayerLanguage(p.getUniqueId(), args[0])) {
            Bukkit.getScheduler().runTaskLater((Plugin)BedWars.plugin, () -> p.sendMessage(Language.getMsg(p, Messages.COMMAND_LANG_SELECTED_SUCCESSFULLY)), 3L);
            return true;
        }
        p.sendMessage(Language.getMsg(p, Messages.COMMAND_LANG_LIST_HEADER));
        Iterator<Language> iterator = Language.getLanguages().iterator();
        while (true) {
            if (!iterator.hasNext()) {
                p.sendMessage(Language.getMsg(p, Messages.COMMAND_LANG_USAGE));
                return true;
            }
            Language l = iterator.next();
            p.sendMessage(Language.getMsg(p, Messages.COMMAND_LANG_LIST_FORMAT).replace("{iso}", l.getIso()).replace("{name}", l.getLangName()));
        }
    }

    @Override
    public List<String> getTabComplete() {
        ArrayList<String> tab = new ArrayList<String>();
        for (Language lang : Language.getLanguages()) {
            tab.add(lang.getIso());
        }
        return tab;
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
        return this.hasPermission(s);
    }
}

