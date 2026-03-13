/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  net.md_5.bungee.api.chat.BaseComponent
 *  net.md_5.bungee.api.chat.ClickEvent
 *  net.md_5.bungee.api.chat.ClickEvent$Action
 *  net.md_5.bungee.api.chat.TextComponent
 *  org.bukkit.Bukkit
 *  org.bukkit.command.CommandSender
 *  org.bukkit.command.ConsoleCommandSender
 *  org.bukkit.command.defaults.BukkitCommand
 *  org.bukkit.entity.Player
 */
package com.andrei1058.bedwars.commands.party;

import com.andrei1058.bedwars.BedWars;
import com.andrei1058.bedwars.api.language.Language;
import com.andrei1058.bedwars.api.language.Messages;
import java.util.HashMap;
import java.util.UUID;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.command.defaults.BukkitCommand;
import org.bukkit.entity.Player;

public class PartyCommand
extends BukkitCommand {
    private static HashMap<UUID, UUID> partySessionRequest = new HashMap();

    public PartyCommand(String name) {
        super(name);
    }

    public boolean execute(CommandSender s, String c, String[] args) {
        if (s instanceof ConsoleCommandSender) {
            return true;
        }
        Player p = (Player)s;
        if (args.length == 0 || args[0].equalsIgnoreCase("help")) {
            this.sendPartyCmds(p);
            return true;
        }
        switch (args[0].toLowerCase()) {
            case "invite": {
                if (args.length == 1) {
                    p.sendMessage(Language.getMsg(p, Messages.COMMAND_PARTY_INVITE_USAGE));
                    return true;
                }
                if (BedWars.getParty().hasParty(p) && !BedWars.getParty().isOwner(p)) {
                    p.sendMessage(Language.getMsg(p, Messages.COMMAND_PARTY_INSUFFICIENT_PERMISSIONS));
                    return true;
                }
                if (Bukkit.getPlayer((String)args[1]) != null && Bukkit.getPlayer((String)args[1]).isOnline()) {
                    if (p == Bukkit.getPlayer((String)args[1])) {
                        p.sendMessage(Language.getMsg(p, Messages.COMMAND_PARTY_INVITE_DENIED_CANNOT_INVITE_YOURSELF));
                        return true;
                    }
                    p.sendMessage(Language.getMsg(p, Messages.COMMAND_PARTY_INVITE_SENT).replace("{playername}", p.getName()).replace("{player}", args[1]));
                    TextComponent tc = new TextComponent(Language.getMsg(p, Messages.COMMAND_PARTY_INVITE_SENT_TARGET_RECEIVE_MSG).replace("{player}", p.getName()));
                    tc.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/party accept " + p.getName()));
                    Bukkit.getPlayer((String)args[1]).spigot().sendMessage((BaseComponent)tc);
                    if (partySessionRequest.containsKey(p.getUniqueId())) {
                        partySessionRequest.replace(p.getUniqueId(), Bukkit.getPlayer((String)args[1]).getUniqueId());
                        break;
                    }
                    partySessionRequest.put(p.getUniqueId(), Bukkit.getPlayer((String)args[1]).getUniqueId());
                    break;
                }
                p.sendMessage(Language.getMsg(p, Messages.COMMAND_PARTY_INVITE_DENIED_PLAYER_OFFLINE).replace("{player}", args[1]));
                break;
            }
            case "accept": {
                if (args.length < 2) {
                    return true;
                }
                if (BedWars.getParty().hasParty(p)) {
                    p.sendMessage(Language.getMsg(p, Messages.COMMAND_PARTY_ACCEPT_DENIED_ALREADY_IN_PARTY));
                    return true;
                }
                if (Bukkit.getPlayer((String)args[1]) == null || !Bukkit.getPlayer((String)args[1]).isOnline()) {
                    p.sendMessage(Language.getMsg(p, Messages.COMMAND_PARTY_INVITE_DENIED_PLAYER_OFFLINE).replace("{player}", args[1]));
                    return true;
                }
                if (!partySessionRequest.containsKey(Bukkit.getPlayer((String)args[1]).getUniqueId())) {
                    p.sendMessage(Language.getMsg(p, Messages.COMMAND_PARTY_ACCEPT_DENIED_NO_INVITE));
                    return true;
                }
                if (partySessionRequest.get(Bukkit.getPlayer((String)args[1]).getUniqueId()).equals(p.getUniqueId())) {
                    partySessionRequest.remove(Bukkit.getPlayer((String)args[1]).getUniqueId());
                    if (BedWars.getParty().hasParty(Bukkit.getPlayer((String)args[1]))) {
                        BedWars.getParty().addMember(Bukkit.getPlayer((String)args[1]), p);
                        for (Player on : BedWars.getParty().getMembers(Bukkit.getPlayer((String)args[1]))) {
                            on.sendMessage(Language.getMsg(p, Messages.COMMAND_PARTY_ACCEPT_SUCCESS).replace("{playername}", p.getName()).replace("{player}", p.getDisplayName()));
                        }
                    } else {
                        BedWars.getParty().createParty(Bukkit.getPlayer((String)args[1]), p);
                        for (Player on : BedWars.getParty().getMembers(Bukkit.getPlayer((String)args[1]))) {
                            on.sendMessage(Language.getMsg(p, Messages.COMMAND_PARTY_ACCEPT_SUCCESS).replace("{playername}", p.getName()).replace("{player}", p.getDisplayName()));
                        }
                    }
                    break;
                }
                p.sendMessage(Language.getMsg(p, Messages.COMMAND_PARTY_ACCEPT_DENIED_NO_INVITE));
                break;
            }
            case "leave": {
                if (!BedWars.getParty().hasParty(p)) {
                    p.sendMessage(Language.getMsg(p, Messages.COMMAND_PARTY_GENERAL_DENIED_NOT_IN_PARTY));
                    return true;
                }
                if (BedWars.getParty().isOwner(p)) {
                    p.sendMessage(Language.getMsg(p, Messages.COMMAND_PARTY_LEAVE_DENIED_IS_OWNER_NEEDS_DISBAND));
                    return true;
                }
                BedWars.getParty().removeFromParty(p);
                break;
            }
            case "disband": {
                if (!BedWars.getParty().hasParty(p)) {
                    p.sendMessage(Language.getMsg(p, Messages.COMMAND_PARTY_GENERAL_DENIED_NOT_IN_PARTY));
                    return true;
                }
                if (!BedWars.getParty().isOwner(p)) {
                    p.sendMessage(Language.getMsg(p, Messages.COMMAND_PARTY_INSUFFICIENT_PERMISSIONS));
                    return true;
                }
                BedWars.getParty().disband(p);
                break;
            }
            case "remove": {
                if (args.length == 1) {
                    p.sendMessage(Language.getMsg(p, Messages.COMMAND_PARTY_REMOVE_USAGE));
                    return true;
                }
                if (BedWars.getParty().hasParty(p) && !BedWars.getParty().isOwner(p)) {
                    p.sendMessage(Language.getMsg(p, Messages.COMMAND_PARTY_INSUFFICIENT_PERMISSIONS));
                    return true;
                }
                Player target = Bukkit.getPlayer((String)args[1]);
                if (target == null) {
                    p.sendMessage(Language.getMsg(p, Messages.COMMAND_PARTY_REMOVE_DENIED_TARGET_NOT_PARTY_MEMBER).replace("{player}", args[1]));
                    return true;
                }
                if (!BedWars.getParty().isMember(p, target)) {
                    p.sendMessage(Language.getMsg(p, Messages.COMMAND_PARTY_REMOVE_DENIED_TARGET_NOT_PARTY_MEMBER).replace("{player}", args[1]));
                    return true;
                }
                BedWars.getParty().removePlayer(p, target);
                break;
            }
            case "promote": {
                if (!BedWars.getParty().hasParty(p)) {
                    p.sendMessage(Language.getMsg(p, Messages.COMMAND_PARTY_GENERAL_DENIED_NOT_IN_PARTY));
                    return true;
                }
                if (!BedWars.getParty().isOwner(p)) {
                    p.sendMessage(Language.getMsg(p, Messages.COMMAND_PARTY_INSUFFICIENT_PERMISSIONS));
                    return true;
                }
                if (args.length == 1) {
                    this.sendPartyCmds(p);
                    return true;
                }
                Player target1 = Bukkit.getPlayer((String)args[1]);
                if (!BedWars.getParty().isMember(p, target1)) {
                    p.sendMessage(Language.getMsg(p, Messages.COMMAND_PARTY_REMOVE_DENIED_TARGET_NOT_PARTY_MEMBER).replace("{player}", args[1]));
                    return true;
                }
                BedWars.getParty().promote(p, target1);
                for (Player p1 : BedWars.getParty().getMembers(p)) {
                    if (p1.equals((Object)p)) {
                        p1.sendMessage(Language.getMsg(p1, Messages.COMMAND_PARTY_PROMOTE_SUCCESS).replace("{player}", args[1]));
                        continue;
                    }
                    if (p1.equals((Object)target1)) {
                        p1.sendMessage(Language.getMsg(p1, Messages.COMMAND_PARTY_PROMOTE_OWNER));
                        continue;
                    }
                    p1.sendMessage(Language.getMsg(p1, Messages.COMMAND_PARTY_PROMOTE_NEW_OWNER).replace("{player}", args[1]));
                }
                break;
            }
            case "info": 
            case "list": {
                if (!BedWars.getParty().hasParty(p)) {
                    p.sendMessage(Language.getMsg(p, Messages.COMMAND_PARTY_GENERAL_DENIED_NOT_IN_PARTY));
                    return true;
                }
                Player owner = BedWars.getParty().getOwner(p);
                p.sendMessage(Language.getMsg(p, Messages.COMMAND_PARTY_INFO_OWNER).replace("{owner}", owner.getName()));
                p.sendMessage(Language.getMsg(p, Messages.COMMAND_PARTY_INFO_PLAYERS));
                for (Player p1 : BedWars.getParty().getMembers(owner)) {
                    p.sendMessage(Language.getMsg(p, Messages.COMMAND_PARTY_INFO_PLAYER).replace("{player}", p1.getName()));
                }
                break;
            }
            default: {
                this.sendPartyCmds(p);
            }
        }
        return false;
    }

    private void sendPartyCmds(Player p) {
        for (String s : Language.getList(p, Messages.COMMAND_PARTY_HELP)) {
            p.sendMessage(s);
        }
    }
}

