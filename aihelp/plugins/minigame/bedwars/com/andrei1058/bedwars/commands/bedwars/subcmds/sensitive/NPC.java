/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  net.citizensnpcs.api.CitizensAPI
 *  net.citizensnpcs.api.npc.NPC
 *  net.md_5.bungee.api.ChatColor
 *  net.md_5.bungee.api.chat.BaseComponent
 *  net.md_5.bungee.api.chat.ClickEvent$Action
 *  org.bukkit.Location
 *  org.bukkit.block.Block
 *  org.bukkit.command.CommandSender
 *  org.bukkit.command.ConsoleCommandSender
 *  org.bukkit.entity.ArmorStand
 *  org.bukkit.entity.Entity
 *  org.bukkit.entity.EntityType
 *  org.bukkit.entity.Player
 *  org.bukkit.util.BlockIterator
 *  org.jetbrains.annotations.NotNull
 *  org.jetbrains.annotations.Nullable
 */
package com.andrei1058.bedwars.commands.bedwars.subcmds.sensitive;

import com.andrei1058.bedwars.BedWars;
import com.andrei1058.bedwars.api.command.ParentCommand;
import com.andrei1058.bedwars.api.command.SubCommand;
import com.andrei1058.bedwars.arena.Arena;
import com.andrei1058.bedwars.arena.Misc;
import com.andrei1058.bedwars.arena.SetupSession;
import com.andrei1058.bedwars.configuration.Permissions;
import com.andrei1058.bedwars.support.citizens.JoinNPC;
import com.google.common.base.Joiner;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import net.citizensnpcs.api.CitizensAPI;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ClickEvent;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.util.BlockIterator;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class NPC
extends SubCommand {
    private final List<BaseComponent> MAIN_USAGE = Arrays.asList(Misc.msgHoverClick("\u00a7f\n\u00a7c\u25aa \u00a77Usage: \u00a7e/" + BedWars.mainCmd + " " + this.getSubCommandName() + " add", "\u00a7fUse this command to create a join NPC.\n\u00a7fClick to see the syntax.", "/" + this.getParent().getName() + " " + this.getSubCommandName() + " add", ClickEvent.Action.RUN_COMMAND), Misc.msgHoverClick("\u00a7c\u25aa \u00a77Usage: \u00a7e/" + BedWars.mainCmd + " " + this.getSubCommandName() + " remove", "\u00a7fStay in front of a NPC in order to remove it.", "/" + this.getParent().getName() + " " + this.getSubCommandName() + " remove", ClickEvent.Action.SUGGEST_COMMAND));
    private final List<BaseComponent> ADD_USAGE = Arrays.asList(Misc.msgHoverClick("f\n\u00a7c\u25aa \u00a77Usage: \u00a7e\u00a7o/" + this.getParent().getName() + " " + this.getSubCommandName() + " add <skin> <arenaGroup> <\u00a77line1\u00a79\\n\u00a77line2\u00a7e>\n\u00a77You can use \u00a7e{players} \u00a77for the players count in this arena \u00a77group.", "Click to use.", "/" + this.getParent().getName() + " " + this.getSubCommandName() + " add", ClickEvent.Action.SUGGEST_COMMAND));

    public NPC(ParentCommand parent, String name) {
        super(parent, name);
        this.showInList(true);
        this.setPriority(12);
        this.setPermission(Permissions.PERMISSION_NPC);
        this.setDisplayInfo(Misc.msgHoverClick("\u00a76 \u25aa \u00a77/" + this.getParent().getName() + " " + this.getSubCommandName() + "         \u00a78   - \u00a7ecreate a join NPC", "\u00a7fCreate a join NPC  \n\u00a7fClick for more details.", "/" + this.getParent().getName() + " " + this.getSubCommandName(), ClickEvent.Action.RUN_COMMAND));
    }

    @Override
    public boolean execute(String[] args, CommandSender s) {
        if (s instanceof ConsoleCommandSender) {
            return false;
        }
        if (!JoinNPC.isCitizensSupport()) {
            return false;
        }
        Player p = (Player)s;
        if (args.length < 1) {
            for (BaseComponent bc : this.MAIN_USAGE) {
                p.spigot().sendMessage(bc);
            }
            return true;
        }
        if (args[0].equalsIgnoreCase("add")) {
            if (args.length < 4) {
                for (BaseComponent bc : this.ADD_USAGE) {
                    p.spigot().sendMessage(bc);
                }
                return true;
            }
            List npcs = BedWars.config.getYml().get("join-npc-locations") != null ? BedWars.config.getYml().getStringList("join-npc-locations") : new ArrayList();
            String name = Joiner.on(" ").join(args).replace(args[0] + " " + args[1] + " " + args[2] + " ", "");
            net.citizensnpcs.api.npc.NPC npc = JoinNPC.spawnNPC(p.getLocation(), name, args[2], args[1], null);
            assert (npc != null);
            npcs.add(BedWars.config.stringLocationConfigFormat(p.getLocation()) + "," + args[1] + "," + name + "," + args[2] + "," + npc.getId());
            String NPC_SET = "\u00a7a\u00a7c\u25aa \u00a7bNPC: %name% \u00a7bwas set!";
            p.sendMessage(NPC_SET.replace("%name%", name.replace("&", "\u00a7").replace("\\\\n", " ")));
            p.sendMessage("\u00a7a\u00a7c\u25aa \u00a7bTarget groups: " + String.valueOf(ChatColor.GOLD) + args[2]);
            BedWars.config.set("join-npc-locations", npcs);
        } else if (args[0].equalsIgnoreCase("remove")) {
            List e = p.getNearbyEntities(4.0, 4.0, 4.0);
            String NO_NPCS = "\u00a7c\u25aa \u00a7bThere isn't any NPC nearby.";
            if (e.isEmpty()) {
                p.sendMessage(NO_NPCS);
                return true;
            }
            if (BedWars.config.getYml().get("join-npc-locations") == null) {
                String NO_SET = "\u00a7c\u25aa \u00a7bThere isn't any NPC set yet!";
                p.sendMessage(NO_SET);
                return true;
            }
            net.citizensnpcs.api.npc.NPC npc = NPC.getTarget(p);
            if (npc == null) {
                p.sendMessage(NO_NPCS);
                return true;
            }
            List locations = BedWars.config.getYml().getStringList("join-npc-locations");
            for (Integer id : JoinNPC.npcs.keySet()) {
                if (id.intValue() != npc.getId()) continue;
                for (String loc : BedWars.config.getYml().getStringList("join-npc-locations")) {
                    if (!loc.split(",")[4].equalsIgnoreCase(String.valueOf(npc.getId()))) continue;
                    locations.remove(loc);
                }
            }
            JoinNPC.npcs.remove(npc.getId());
            for (Entity e2 : npc.getEntity().getNearbyEntities(0.0, 3.0, 0.0)) {
                if (e2.getType() != EntityType.ARMOR_STAND) continue;
                e2.remove();
            }
            BedWars.config.set("join-npc-locations", locations);
            npc.destroy();
            String NPC_REMOVED = "\u00a7c\u25aa \u00a7bThe target NPC was removed!";
            p.sendMessage(NPC_REMOVED);
        } else {
            for (BaseComponent bc : this.MAIN_USAGE) {
                p.spigot().sendMessage(bc);
            }
        }
        return true;
    }

    @Override
    public List<String> getTabComplete() {
        return Arrays.asList("remove", "add");
    }

    @NotNull
    public static ArmorStand createArmorStand(Location loc) {
        ArmorStand a = (ArmorStand)loc.getWorld().spawn(loc, ArmorStand.class);
        a.setGravity(false);
        a.setVisible(false);
        a.setCustomNameVisible(false);
        a.setMarker(true);
        return a;
    }

    @Nullable
    public static net.citizensnpcs.api.npc.NPC getTarget(Player player) {
        BlockIterator iterator = new BlockIterator(player.getWorld(), player.getLocation().toVector(), player.getEyeLocation().getDirection(), 0.0, 100);
        while (iterator.hasNext()) {
            Block item = iterator.next();
            for (Entity entity : player.getNearbyEntities(100.0, 100.0, 100.0)) {
                int acc = 2;
                for (int x = -acc; x < acc; ++x) {
                    for (int z = -acc; z < acc; ++z) {
                        for (int y = -acc; y < acc; ++y) {
                            net.citizensnpcs.api.npc.NPC npc;
                            if (!entity.getLocation().getBlock().getRelative(x, y, z).equals((Object)item) || !entity.hasMetadata("NPC") || (npc = CitizensAPI.getNPCRegistry().getNPC(entity)) == null) continue;
                            return npc;
                        }
                    }
                }
            }
        }
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
        return this.hasPermission(s);
    }
}

