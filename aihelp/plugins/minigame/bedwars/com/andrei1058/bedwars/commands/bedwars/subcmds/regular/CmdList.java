/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  net.md_5.bungee.api.chat.BaseComponent
 *  net.md_5.bungee.api.chat.ClickEvent
 *  net.md_5.bungee.api.chat.ClickEvent$Action
 *  net.md_5.bungee.api.chat.ComponentBuilder
 *  net.md_5.bungee.api.chat.HoverEvent
 *  net.md_5.bungee.api.chat.HoverEvent$Action
 *  net.md_5.bungee.api.chat.TextComponent
 *  org.bukkit.ChatColor
 *  org.bukkit.command.CommandSender
 *  org.bukkit.command.ConsoleCommandSender
 *  org.bukkit.entity.Player
 */
package com.andrei1058.bedwars.commands.bedwars.subcmds.regular;

import com.andrei1058.bedwars.BedWars;
import com.andrei1058.bedwars.api.arena.team.TeamColor;
import com.andrei1058.bedwars.api.command.ParentCommand;
import com.andrei1058.bedwars.api.command.SubCommand;
import com.andrei1058.bedwars.api.language.Language;
import com.andrei1058.bedwars.api.language.Messages;
import com.andrei1058.bedwars.api.server.SetupType;
import com.andrei1058.bedwars.arena.Arena;
import com.andrei1058.bedwars.arena.Misc;
import com.andrei1058.bedwars.arena.SetupSession;
import com.andrei1058.bedwars.commands.bedwars.MainCommand;
import java.util.List;
import java.util.Objects;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

public class CmdList
extends SubCommand {
    public CmdList(ParentCommand parent, String name) {
        super(parent, name);
        this.setPriority(11);
        this.showInList(true);
        this.setDisplayInfo(Misc.msgHoverClick("\u00a76 \u25aa \u00a77/" + MainCommand.getInstance().getName() + " " + this.getSubCommandName() + "         \u00a78 - \u00a7e view player cmds", "\u00a7fView player commands.", "/" + this.getParent().getName() + " " + this.getSubCommandName(), ClickEvent.Action.RUN_COMMAND));
    }

    @Override
    public boolean execute(String[] args, CommandSender s) {
        if (s instanceof ConsoleCommandSender) {
            return false;
        }
        Player p = (Player)s;
        if (SetupSession.isInSetupSession(p.getUniqueId())) {
            SetupSession ss = SetupSession.getSession(p.getUniqueId());
            Objects.requireNonNull(ss).getConfig().reload();
            boolean waitingSpawn = ss.getConfig().getYml().get("waiting.Loc") != null;
            boolean pos1 = ss.getConfig().getYml().get("waiting.Pos1") != null;
            boolean pos2 = ss.getConfig().getYml().get("waiting.Pos2") != null;
            boolean pos = pos1 && pos2;
            StringBuilder spawnNotSetNames = new StringBuilder();
            StringBuilder bedNotSet = new StringBuilder();
            StringBuilder shopNotSet = new StringBuilder();
            StringBuilder killDropsNotSet = new StringBuilder();
            StringBuilder upgradeNotSet = new StringBuilder();
            StringBuilder spawnNotSet = new StringBuilder();
            StringBuilder generatorNotSet = new StringBuilder();
            int teams = 0;
            if (ss.getConfig().getYml().get("Team") != null) {
                for (String team : ss.getConfig().getYml().getConfigurationSection("Team").getKeys(true)) {
                    if (ss.getConfig().getYml().get("Team." + team + ".Color") == null) continue;
                    ChatColor color = TeamColor.getChatColor(ss.getConfig().getYml().getString("Team." + team + ".Color"));
                    if (ss.getConfig().getYml().get("Team." + team + ".Spawn") == null) {
                        spawnNotSet.append(color).append("\u258b");
                        spawnNotSetNames.append(color).append(team).append(" ");
                    }
                    if (ss.getConfig().getYml().get("Team." + team + ".Bed") == null) {
                        bedNotSet.append(color).append("\u258b");
                    }
                    if (ss.getConfig().getYml().get("Team." + team + ".Shop") == null) {
                        shopNotSet.append(color).append("\u258b");
                    }
                    if (ss.getConfig().getYml().get("Team." + team + ".kill-drops-loc") == null) {
                        killDropsNotSet.append(color).append("\u258b");
                    }
                    if (ss.getConfig().getYml().get("Team." + team + ".Upgrade") == null) {
                        upgradeNotSet.append(color).append("\u258b");
                    }
                    if (ss.getConfig().getYml().get("Team." + team + ".Iron") == null || ss.getConfig().getYml().get("Team." + team + ".Gold") == null) {
                        generatorNotSet.append(color).append("\u258b");
                    }
                    ++teams;
                }
            }
            int emGen = 0;
            int dmGen = 0;
            if (ss.getConfig().getYml().get("generator.Emerald") != null) {
                emGen = ss.getConfig().getYml().getStringList("generator.Emerald").size();
            }
            if (ss.getConfig().getYml().get("generator.Diamond") != null) {
                dmGen = ss.getConfig().getYml().getStringList("generator.Diamond").size();
            }
            String group = String.valueOf(ChatColor.RED) + "(NOT SET)";
            String posMsg = pos1 && !pos2 ? String.valueOf(ChatColor.RED) + "(POS 2 NOT SET)" : (!pos1 && pos2 ? String.valueOf(ChatColor.RED) + "(POS 1 NOT SET)" : (pos1 ? String.valueOf(ChatColor.GREEN) + "(SET)" : String.valueOf(ChatColor.GRAY) + "(NOT SET) " + String.valueOf(ChatColor.ITALIC) + "OPTIONAL"));
            String g2 = ss.getConfig().getYml().getString("group");
            if (g2 != null && !g2.equalsIgnoreCase("default")) {
                group = String.valueOf(ChatColor.GREEN) + "(" + g2 + ")";
            }
            int maxInTeam = ss.getConfig().getInt("maxInTeam");
            String setWaitingSpawn = ss.dot() + String.valueOf(waitingSpawn ? ChatColor.STRIKETHROUGH : "") + "setWaitingSpawn" + String.valueOf(ChatColor.RESET) + " " + (waitingSpawn ? String.valueOf(ChatColor.GREEN) + "(SET)" : String.valueOf(ChatColor.RED) + "(NOT SET)");
            String waitingPos = ss.dot() + String.valueOf(pos ? ChatColor.STRIKETHROUGH : "") + "waitingPos 1/2" + String.valueOf(ChatColor.RESET) + " " + posMsg;
            String setSpawn = ss.dot() + String.valueOf(spawnNotSet.length() == 0 ? ChatColor.STRIKETHROUGH : "") + "setSpawn <teamName>" + String.valueOf(ChatColor.RESET) + " " + (spawnNotSet.length() == 0 ? String.valueOf(ChatColor.GREEN) + "(ALL SET)" : String.valueOf(ChatColor.RED) + "(Remaining: " + String.valueOf(spawnNotSet) + String.valueOf(ChatColor.RED) + ")");
            String setBed = ss.dot() + String.valueOf(bedNotSet.toString().length() == 0 ? ChatColor.STRIKETHROUGH : "") + "setBed" + String.valueOf(ChatColor.RESET) + " " + (bedNotSet.length() == 0 ? String.valueOf(ChatColor.GREEN) + "(ALL SET)" : String.valueOf(ChatColor.RED) + "(Remaining: " + String.valueOf(bedNotSet) + String.valueOf(ChatColor.RED) + ")");
            String setShop = ss.dot() + String.valueOf(shopNotSet.toString().length() == 0 ? ChatColor.STRIKETHROUGH : "") + "setShop" + String.valueOf(ChatColor.RESET) + " " + (shopNotSet.length() == 0 ? String.valueOf(ChatColor.GREEN) + "(ALL SET)" : String.valueOf(ChatColor.RED) + "(Remaining: " + String.valueOf(shopNotSet) + String.valueOf(ChatColor.RED) + ")");
            String setKillDrops = ss.dot() + String.valueOf(killDropsNotSet.toString().length() == 0 ? ChatColor.STRIKETHROUGH : "") + "setKillDrops" + String.valueOf(ChatColor.RESET) + " " + (shopNotSet.length() == 0 ? String.valueOf(ChatColor.GREEN) + "(ALL SET)" : String.valueOf(ChatColor.RED) + "(Remaining: " + String.valueOf(killDropsNotSet) + String.valueOf(ChatColor.RED) + ")");
            String setUpgrade = ss.dot() + String.valueOf(upgradeNotSet.toString().length() == 0 ? ChatColor.STRIKETHROUGH : "") + "setUpgrade" + String.valueOf(ChatColor.RESET) + " " + (upgradeNotSet.length() == 0 ? String.valueOf(ChatColor.GREEN) + "(ALL SET)" : String.valueOf(ChatColor.RED) + "(Remaining: " + String.valueOf(upgradeNotSet) + String.valueOf(ChatColor.RED) + ")");
            String addGenerator = ss.dot() + "addGenerator " + (String)(generatorNotSet.toString().length() == 0 ? "" : String.valueOf(ChatColor.RED) + "(Remaining: " + String.valueOf(generatorNotSet) + String.valueOf(ChatColor.RED) + ") ") + String.valueOf(ChatColor.YELLOW) + "(" + String.valueOf(ChatColor.DARK_GREEN) + "E" + emGen + " " + String.valueOf(ChatColor.AQUA) + "D" + dmGen + String.valueOf(ChatColor.YELLOW) + ")";
            String setSpectatorSpawn = ss.dot() + String.valueOf(ss.getConfig().getYml().get("spectator-loc") == null ? "" : ChatColor.STRIKETHROUGH) + "setSpectSpawn" + String.valueOf(ChatColor.RESET) + " " + (ss.getConfig().getYml().get("spectator-loc") == null ? String.valueOf(ChatColor.RED) + "(NOT SET)" : String.valueOf(ChatColor.GRAY) + "(SET)");
            s.sendMessage("");
            s.sendMessage(String.valueOf(ChatColor.GRAY) + String.valueOf(ChatColor.BOLD) + MainCommand.getDot() + String.valueOf(ChatColor.GOLD) + BedWars.plugin.getDescription().getName() + " v" + BedWars.plugin.getDescription().getVersion() + String.valueOf(ChatColor.GRAY) + "- " + String.valueOf(ChatColor.GREEN) + ss.getWorldName() + " commands");
            p.spigot().sendMessage((BaseComponent)Misc.msgHoverClick(setWaitingSpawn, String.valueOf(ChatColor.WHITE) + "Set the place where players have\n" + String.valueOf(ChatColor.WHITE) + "to wait before the game starts.", "/" + this.getParent().getName() + " setWaitingSpawn", ss.getSetupType() == SetupType.ASSISTED ? ClickEvent.Action.RUN_COMMAND : ClickEvent.Action.SUGGEST_COMMAND));
            p.spigot().sendMessage((BaseComponent)Misc.msgHoverClick(waitingPos, String.valueOf(ChatColor.WHITE) + "Make it so the waiting lobby will disappear at start.\n" + String.valueOf(ChatColor.WHITE) + "Select it as a world edit region.", "/" + this.getParent().getName() + " waitingPos ", ClickEvent.Action.SUGGEST_COMMAND));
            if (ss.getSetupType() == SetupType.ADVANCED) {
                p.spigot().sendMessage((BaseComponent)Misc.msgHoverClick(setSpectatorSpawn, String.valueOf(ChatColor.WHITE) + "Set where to spawn spectators.", "/" + this.getParent().getName() + " setSpectSpawn", ClickEvent.Action.RUN_COMMAND));
            }
            p.spigot().sendMessage((BaseComponent)Misc.msgHoverClick(ss.dot() + "autoCreateTeams " + String.valueOf(ChatColor.YELLOW) + "(auto detect)", String.valueOf(ChatColor.WHITE) + "Create teams based on islands colors.", "/" + this.getParent().getName() + " autoCreateTeams", ClickEvent.Action.SUGGEST_COMMAND));
            p.spigot().sendMessage((BaseComponent)Misc.msgHoverClick(ss.dot() + "createTeam <name> <color> " + String.valueOf(ChatColor.YELLOW) + "(" + teams + " CREATED)", String.valueOf(ChatColor.WHITE) + "Create a team.", "/" + this.getParent().getName() + " createTeam ", ClickEvent.Action.SUGGEST_COMMAND));
            p.spigot().sendMessage((BaseComponent)Misc.msgHoverClick(ss.dot() + "removeTeam <name>", String.valueOf(ChatColor.WHITE) + "Remove a team by name.", "/" + BedWars.mainCmd + " removeTeam ", ClickEvent.Action.SUGGEST_COMMAND));
            p.spigot().sendMessage((BaseComponent)Misc.msgHoverClick(setSpawn, String.valueOf(ChatColor.WHITE) + "Set a team spawn.\n" + String.valueOf(ChatColor.WHITE) + "Teams without a spawn set:\n" + spawnNotSetNames.toString(), "/" + this.getParent().getName() + " setSpawn ", ClickEvent.Action.SUGGEST_COMMAND));
            p.spigot().sendMessage((BaseComponent)Misc.msgHoverClick(setBed, String.valueOf(ChatColor.WHITE) + "Set a team's bed location.\n" + String.valueOf(ChatColor.WHITE) + "You don't have to specify the team name.", "/" + this.getParent().getName() + " setBed", ss.getSetupType() == SetupType.ASSISTED ? ClickEvent.Action.RUN_COMMAND : ClickEvent.Action.SUGGEST_COMMAND));
            p.spigot().sendMessage((BaseComponent)Misc.msgHoverClick(setShop, String.valueOf(ChatColor.WHITE) + "Set a team's NPC.\n" + String.valueOf(ChatColor.WHITE) + "You don't have to specify the team name.\n" + String.valueOf(ChatColor.WHITE) + "It will be spawned only when the game starts.", "/" + this.getParent().getName() + " setShop", ss.getSetupType() == SetupType.ASSISTED ? ClickEvent.Action.RUN_COMMAND : ClickEvent.Action.SUGGEST_COMMAND));
            p.spigot().sendMessage((BaseComponent)Misc.msgHoverClick(setUpgrade, String.valueOf(ChatColor.WHITE) + "Set a team's upgrade NPC.\n" + String.valueOf(ChatColor.WHITE) + "You don't have to specify the team name.\n" + String.valueOf(ChatColor.WHITE) + "It will be spawned only when the game starts.", "/" + this.getParent().getName() + " setUpgrade", ss.getSetupType() == SetupType.ASSISTED ? ClickEvent.Action.RUN_COMMAND : ClickEvent.Action.SUGGEST_COMMAND));
            if (ss.getSetupType() == SetupType.ADVANCED) {
                p.spigot().sendMessage((BaseComponent)Misc.msgHoverClick(setKillDrops, String.valueOf(ChatColor.WHITE) + "Set a the location where to drop\n" + String.valueOf(ChatColor.WHITE) + "enemy items after you kill them.", "/" + this.getParent().getName() + " setKillDrops ", ClickEvent.Action.SUGGEST_COMMAND));
            }
            String genHover = (ss.getSetupType() == SetupType.ADVANCED ? String.valueOf(ChatColor.WHITE) + "Add a generator spawn point.\n" + String.valueOf(ChatColor.YELLOW) + "/" + this.getParent().getName() + " addGenerator <Iron/ Gold/ Emerald, Diamond>" : String.valueOf(ChatColor.WHITE) + "Add a generator spawn point.\n" + String.valueOf(ChatColor.YELLOW) + "Stay in on a team island to set a team generator") + "\n" + String.valueOf(ChatColor.WHITE) + "Stay on a diamond block to set the diamond generator.\n" + String.valueOf(ChatColor.WHITE) + "Stay on a emerald block to set an emerald generator.";
            p.spigot().sendMessage((BaseComponent)Misc.msgHoverClick(addGenerator, genHover, "/" + this.getParent().getName() + " addGenerator ", ss.getSetupType() == SetupType.ASSISTED ? ClickEvent.Action.RUN_COMMAND : ClickEvent.Action.SUGGEST_COMMAND));
            p.spigot().sendMessage((BaseComponent)Misc.msgHoverClick(ss.dot() + "removeGenerator", genHover, "/" + this.getParent().getName() + " removeGenerator", ss.getSetupType() == SetupType.ASSISTED ? ClickEvent.Action.RUN_COMMAND : ClickEvent.Action.SUGGEST_COMMAND));
            if (ss.getSetupType() == SetupType.ADVANCED) {
                p.spigot().sendMessage((BaseComponent)Misc.msgHoverClick(ss.dot() + "setMaxInTeam <int> (IS SET TO " + maxInTeam + ")", String.valueOf(ChatColor.WHITE) + "Set the max team size.", "/" + BedWars.mainCmd + " setMaxInTeam ", ClickEvent.Action.SUGGEST_COMMAND));
                p.spigot().sendMessage((BaseComponent)Misc.msgHoverClick(ss.dot() + "arenaGroup " + group, String.valueOf(ChatColor.WHITE) + "Set the arena group.", "/" + BedWars.mainCmd + " arenaGroup ", ClickEvent.Action.SUGGEST_COMMAND));
            } else {
                p.spigot().sendMessage((BaseComponent)Misc.msgHoverClick(ss.dot() + "setType <type> " + group, String.valueOf(ChatColor.WHITE) + "Add the arena to a group.", "/" + this.getParent().getName() + " setType", ClickEvent.Action.RUN_COMMAND));
            }
            p.spigot().sendMessage((BaseComponent)Misc.msgHoverClick(ss.dot() + "save", String.valueOf(ChatColor.WHITE) + "Save arena and go back to lobby", "/" + this.getParent().getName() + " save", ClickEvent.Action.SUGGEST_COMMAND));
        } else {
            TextComponent credits = new TextComponent(String.valueOf(ChatColor.BLUE) + String.valueOf(ChatColor.BOLD) + MainCommand.getDot() + " " + String.valueOf(ChatColor.GOLD) + BedWars.plugin.getName() + " " + String.valueOf(ChatColor.GRAY) + "v" + BedWars.plugin.getDescription().getVersion() + " by andrei1058");
            credits.setClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, BedWars.link));
            credits.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(String.valueOf(ChatColor.GRAY) + "Arenas: " + (Arena.getArenas().size() == 0 ? String.valueOf(ChatColor.RED) + "0" : String.valueOf(ChatColor.GREEN) + Arena.getArenas().size())).create()));
            ((Player)s).spigot().sendMessage((BaseComponent)credits);
            for (String string : Language.getList((Player)s, Messages.COMMAND_MAIN)) {
                s.sendMessage(string);
            }
        }
        return true;
    }

    @Override
    public List<String> getTabComplete() {
        return null;
    }

    @Override
    public boolean canSee(CommandSender s, com.andrei1058.bedwars.api.BedWars api) {
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

