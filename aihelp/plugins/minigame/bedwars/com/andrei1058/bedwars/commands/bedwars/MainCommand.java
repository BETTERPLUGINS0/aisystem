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
 *  org.bukkit.Bukkit
 *  org.bukkit.Location
 *  org.bukkit.command.CommandSender
 *  org.bukkit.command.ConsoleCommandSender
 *  org.bukkit.command.defaults.BukkitCommand
 *  org.bukkit.entity.Player
 */
package com.andrei1058.bedwars.commands.bedwars;

import com.andrei1058.bedwars.BedWars;
import com.andrei1058.bedwars.api.command.ParentCommand;
import com.andrei1058.bedwars.api.command.SubCommand;
import com.andrei1058.bedwars.api.language.Language;
import com.andrei1058.bedwars.api.language.Messages;
import com.andrei1058.bedwars.api.server.ServerType;
import com.andrei1058.bedwars.arena.SetupSession;
import com.andrei1058.bedwars.commands.bedwars.subcmds.regular.CmdGUI;
import com.andrei1058.bedwars.commands.bedwars.subcmds.regular.CmdJoin;
import com.andrei1058.bedwars.commands.bedwars.subcmds.regular.CmdLang;
import com.andrei1058.bedwars.commands.bedwars.subcmds.regular.CmdLeave;
import com.andrei1058.bedwars.commands.bedwars.subcmds.regular.CmdList;
import com.andrei1058.bedwars.commands.bedwars.subcmds.regular.CmdStart;
import com.andrei1058.bedwars.commands.bedwars.subcmds.regular.CmdStats;
import com.andrei1058.bedwars.commands.bedwars.subcmds.regular.CmdTeleporter;
import com.andrei1058.bedwars.commands.bedwars.subcmds.regular.CmdTpStaff;
import com.andrei1058.bedwars.commands.bedwars.subcmds.regular.CmdUpgrades;
import com.andrei1058.bedwars.commands.bedwars.subcmds.sensitive.ArenaGroup;
import com.andrei1058.bedwars.commands.bedwars.subcmds.sensitive.ArenaList;
import com.andrei1058.bedwars.commands.bedwars.subcmds.sensitive.Build;
import com.andrei1058.bedwars.commands.bedwars.subcmds.sensitive.CloneArena;
import com.andrei1058.bedwars.commands.bedwars.subcmds.sensitive.DelArena;
import com.andrei1058.bedwars.commands.bedwars.subcmds.sensitive.DisableArena;
import com.andrei1058.bedwars.commands.bedwars.subcmds.sensitive.EnableArena;
import com.andrei1058.bedwars.commands.bedwars.subcmds.sensitive.Level;
import com.andrei1058.bedwars.commands.bedwars.subcmds.sensitive.NPC;
import com.andrei1058.bedwars.commands.bedwars.subcmds.sensitive.Reload;
import com.andrei1058.bedwars.commands.bedwars.subcmds.sensitive.SetLobby;
import com.andrei1058.bedwars.commands.bedwars.subcmds.sensitive.SetupArena;
import com.andrei1058.bedwars.commands.bedwars.subcmds.sensitive.setup.AddGenerator;
import com.andrei1058.bedwars.commands.bedwars.subcmds.sensitive.setup.AutoCreateTeams;
import com.andrei1058.bedwars.commands.bedwars.subcmds.sensitive.setup.CreateTeam;
import com.andrei1058.bedwars.commands.bedwars.subcmds.sensitive.setup.RemoveGenerator;
import com.andrei1058.bedwars.commands.bedwars.subcmds.sensitive.setup.RemoveTeam;
import com.andrei1058.bedwars.commands.bedwars.subcmds.sensitive.setup.Save;
import com.andrei1058.bedwars.commands.bedwars.subcmds.sensitive.setup.SetBed;
import com.andrei1058.bedwars.commands.bedwars.subcmds.sensitive.setup.SetBuildHeight;
import com.andrei1058.bedwars.commands.bedwars.subcmds.sensitive.setup.SetKillDropsLoc;
import com.andrei1058.bedwars.commands.bedwars.subcmds.sensitive.setup.SetMaxInTeam;
import com.andrei1058.bedwars.commands.bedwars.subcmds.sensitive.setup.SetShop;
import com.andrei1058.bedwars.commands.bedwars.subcmds.sensitive.setup.SetSpawn;
import com.andrei1058.bedwars.commands.bedwars.subcmds.sensitive.setup.SetSpectatorPos;
import com.andrei1058.bedwars.commands.bedwars.subcmds.sensitive.setup.SetType;
import com.andrei1058.bedwars.commands.bedwars.subcmds.sensitive.setup.SetUpgrade;
import com.andrei1058.bedwars.commands.bedwars.subcmds.sensitive.setup.SetWaitingSpawn;
import com.andrei1058.bedwars.commands.bedwars.subcmds.sensitive.setup.WaitingPos;
import com.andrei1058.bedwars.support.citizens.JoinNPC;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.command.defaults.BukkitCommand;
import org.bukkit.entity.Player;

public class MainCommand
extends BukkitCommand
implements ParentCommand {
    private static List<SubCommand> subCommandList = new ArrayList<SubCommand>();
    private static MainCommand instance;
    public static char dot;

    public MainCommand(String name) {
        super(name);
        this.setAliases(Arrays.asList("bedwars", "bedwars1058"));
        instance = this;
        new CmdJoin(this, "join");
        new CmdLeave(this, "leave");
        new CmdLang(this, "lang");
        new CmdTeleporter(this, "teleporter");
        if (BedWars.getServerType() != ServerType.BUNGEE) {
            new CmdGUI(this, "gui");
        }
        new CmdStats(this, "stats");
        new CmdStart(this, "forceStart");
        new CmdStart(this, "start");
        if (BedWars.getServerType() != ServerType.BUNGEE) {
            new SetLobby(this, "setLobby");
        }
        new SetupArena(this, "setupArena");
        new ArenaList(this, "arenaList");
        new DelArena(this, "delArena");
        new EnableArena(this, "enableArena");
        new DisableArena(this, "disableArena");
        new CloneArena(this, "cloneArena");
        new ArenaGroup(this, "arenaGroup");
        new Build(this, "build");
        new Level(this, "level");
        new Reload(this, "reload");
        new CmdList(this, "cmds");
        new AutoCreateTeams(this, "autoCreateTeams");
        new SetWaitingSpawn(this, "setWaitingSpawn");
        new SetSpectatorPos(this, "setSpectSpawn");
        new CreateTeam(this, "createTeam");
        new WaitingPos(this, "waitingPos");
        new RemoveTeam(this, "removeTeam");
        new SetMaxInTeam(this, "setMaxInTeam");
        new SetBuildHeight(this, "setMaxBuildHeight");
        new SetSpawn(this, "setSpawn");
        new SetBed(this, "setBed");
        new SetShop(this, "setShop");
        new SetUpgrade(this, "setUpgrade");
        new AddGenerator(this, "addGenerator");
        new RemoveGenerator(this, "removeGenerator");
        new SetType(this, "setType");
        new Save(this, "save");
        if (JoinNPC.isCitizensSupport() && BedWars.getServerType() != ServerType.BUNGEE) {
            new NPC(this, "npc");
        }
        new CmdTpStaff(this, "tp");
        new CmdUpgrades(this, "upgradesmenu");
        new SetKillDropsLoc(this, "setKillDrops");
    }

    public boolean execute(CommandSender s, String st, String[] args) {
        if (args.length == 0) {
            if (s.isOp() || s.hasPermission(BedWars.mainCmd + ".*")) {
                if (s instanceof Player) {
                    if (SetupSession.isInSetupSession(((Player)s).getUniqueId())) {
                        Bukkit.dispatchCommand((CommandSender)s, (String)(this.getName() + " cmds"));
                    } else {
                        s.sendMessage("");
                        s.sendMessage("\u00a78\u00a7l" + dot + " \u00a76" + BedWars.plugin.getDescription().getName() + " v" + BedWars.plugin.getDescription().getVersion() + " \u00a77- \u00a7c Admin Commands");
                        s.sendMessage("");
                        this.sendSubCommands((Player)s);
                    }
                } else {
                    s.sendMessage("\u00a7f   bw safemode \u00a7eenable/ disable");
                }
            } else {
                if (s instanceof ConsoleCommandSender) {
                    s.sendMessage("\u00a7fNo console commands available atm.");
                    return true;
                }
                Bukkit.dispatchCommand((CommandSender)s, (String)(BedWars.mainCmd + " cmds"));
            }
            return true;
        }
        boolean commandFound = false;
        for (SubCommand sb : this.getSubCommands()) {
            if (!sb.getSubCommandName().equalsIgnoreCase(args[0]) || !sb.hasPermission(s)) continue;
            commandFound = sb.execute(Arrays.copyOfRange(args, 1, args.length), s);
        }
        if (!commandFound) {
            if (s instanceof Player) {
                s.sendMessage(Language.getMsg((Player)s, Messages.COMMAND_NOT_FOUND_OR_INSUFF_PERMS));
            } else {
                s.sendMessage(Language.getDefaultLanguage().m(Messages.COMMAND_NOT_FOUND_OR_INSUFF_PERMS));
            }
        }
        return true;
    }

    public static boolean isArenaGroup(String var) {
        if (BedWars.config.getYml().get("arenaGroups") != null) {
            return BedWars.config.getYml().getStringList("arenaGroups").contains(var);
        }
        return var.equalsIgnoreCase("default");
    }

    public static TextComponent createTC(String text, String suggest, String shot_text) {
        TextComponent tx = new TextComponent(text);
        tx.setClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, suggest));
        tx.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(shot_text).create()));
        return tx;
    }

    @Override
    public void addSubCommand(SubCommand subCommand) {
        subCommandList.add(subCommand);
    }

    @Override
    public void sendSubCommands(Player p) {
        for (int i = 0; i <= 20; ++i) {
            for (SubCommand sb : this.getSubCommands()) {
                if (sb.getPriority() != i || !sb.isShow() || !sb.canSee((CommandSender)p, BedWars.getAPI())) continue;
                p.spigot().sendMessage((BaseComponent)sb.getDisplayInfo());
            }
        }
    }

    public List<String> tabComplete(CommandSender s, String alias, String[] args, Location location) throws IllegalArgumentException {
        if (args.length == 1) {
            ArrayList<String> sub = new ArrayList<String>();
            for (SubCommand sb : this.getSubCommands()) {
                if (!sb.canSee(s, BedWars.getAPI())) continue;
                sub.add(sb.getSubCommandName());
            }
            return sub;
        }
        if (args.length == 2 && this.hasSubCommand(args[0]) && this.getSubCommand(args[0]).canSee(s, BedWars.getAPI())) {
            return this.getSubCommand(args[0]).getTabComplete();
        }
        return null;
    }

    @Override
    public List<SubCommand> getSubCommands() {
        return subCommandList;
    }

    public static MainCommand getInstance() {
        return instance;
    }

    public static boolean isLobbySet(Player p) {
        if (BedWars.getServerType() == ServerType.BUNGEE) {
            return true;
        }
        if (BedWars.config.getLobbyWorldName().isEmpty()) {
            if (p != null) {
                p.sendMessage("\u00a7c\u25aa \u00a77You have to set the lobby location first!");
            }
            return false;
        }
        return true;
    }

    @Override
    public boolean hasSubCommand(String name) {
        for (SubCommand sc : this.getSubCommands()) {
            if (!sc.getSubCommandName().equalsIgnoreCase(name)) continue;
            return true;
        }
        return false;
    }

    public SubCommand getSubCommand(String name) {
        for (SubCommand sc : this.getSubCommands()) {
            if (!sc.getSubCommandName().equalsIgnoreCase(name)) continue;
            return sc;
        }
        return null;
    }

    public static char getDot() {
        return dot;
    }

    static {
        dot = (char)254;
    }
}

