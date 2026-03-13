/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.apache.commons.lang.math.NumberUtils
 *  org.bukkit.Bukkit
 *  org.bukkit.OfflinePlayer
 *  org.bukkit.command.Command
 *  org.bukkit.command.CommandExecutor
 *  org.bukkit.command.CommandMap
 *  org.bukkit.command.CommandSender
 *  org.bukkit.command.ConsoleCommandSender
 *  org.bukkit.command.PluginCommand
 *  org.bukkit.command.TabCompleter
 *  org.bukkit.entity.Player
 *  org.bukkit.plugin.java.JavaPlugin
 *  org.bukkit.util.StringUtil
 */
package net.advancedplugins.as.impl.utils.commands;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import net.advancedplugins.as.impl.utils.commands.SimpleCommand;
import net.advancedplugins.as.impl.utils.commands.SubCommand;
import net.advancedplugins.as.impl.utils.commands.argument.ArgumentHandler;
import net.advancedplugins.as.impl.utils.commands.argument.ArgumentType;
import net.advancedplugins.as.impl.utils.text.Text;
import org.apache.commons.lang.math.NumberUtils;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandMap;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.command.PluginCommand;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.StringUtil;

public class CommandBase
implements CommandExecutor,
TabCompleter {
    private final JavaPlugin plugin;
    private Set<SimpleCommand<? extends CommandSender>> commands = Sets.newHashSet();

    public CommandBase(JavaPlugin javaPlugin) {
        this.plugin = javaPlugin;
        this.registerArgumentTypes();
    }

    public void registerCommand(SimpleCommand<? super CommandSender> simpleCommand) {
        PluginCommand pluginCommand = this.plugin.getCommand(simpleCommand.getCommand());
        if (pluginCommand == null) {
            Bukkit.getLogger().log(Level.WARNING, "Failed to load the command " + simpleCommand.getCommand());
            return;
        }
        pluginCommand.setExecutor((CommandExecutor)this);
        this.commands.add(simpleCommand);
    }

    public void registerCommandOverride(SimpleCommand<? super CommandSender> simpleCommand) {
        Field field;
        try {
            field = Bukkit.getServer().getClass().getDeclaredField("commandMap");
            field.setAccessible(true);
            CommandMap commandMap = (CommandMap)field.get(Bukkit.getServer());
            commandMap.getCommand(simpleCommand.getCommand()).unregister(commandMap);
        } catch (Exception exception) {
            // empty catch block
        }
        field = this.plugin.getCommand(simpleCommand.getCommand());
        if (field == null) {
            Bukkit.getLogger().log(Level.WARNING, "Failed to load the command " + simpleCommand.getCommand());
            return;
        }
        field.setExecutor(this);
        this.commands.add(simpleCommand);
    }

    public void registerCommand(SimpleCommand<? super CommandSender> simpleCommand, List<String> list) {
        PluginCommand pluginCommand = this.plugin.getCommand(simpleCommand.getCommand());
        if (pluginCommand == null) {
            Bukkit.getLogger().log(Level.WARNING, "Failed to load the command " + simpleCommand.getCommand());
            return;
        }
        pluginCommand.setAliases(list);
        pluginCommand.setExecutor((CommandExecutor)this);
        this.commands.add(simpleCommand);
    }

    public CommandBase registerArgumentType(Class<?> clazz, ArgumentType<?> argumentType) {
        ArgumentHandler.register(clazz, argumentType);
        return this;
    }

    public synchronized boolean onCommand(CommandSender commandSender, Command command, String string, String[] stringArray) {
        String string2 = command.getName();
        for (SimpleCommand<? extends CommandSender> simpleCommand : this.commands) {
            if (!simpleCommand.getCommand().equalsIgnoreCase(string2)) continue;
            if (simpleCommand.getPermission() != null && !simpleCommand.getPermission().isEmpty() && !commandSender.hasPermission(simpleCommand.getPermission())) {
                Text.sendMessage(commandSender, simpleCommand.getNoPermissionLang(commandSender));
                return true;
            }
            if (!simpleCommand.isConsole() && commandSender instanceof ConsoleCommandSender) {
                commandSender.sendMessage("The console can not execute this command.");
                return true;
            }
            if (simpleCommand.getSubCommands().isEmpty() && simpleCommand.getArgumentsSize() > stringArray.length) {
                simpleCommand.sendUsage(commandSender);
                return true;
            }
            if (stringArray.length == 0) {
                simpleCommand.middleMan(commandSender, stringArray);
                return true;
            }
            SubCommand subCommand = null;
            for (SubCommand subCommand2 : simpleCommand.getSubCommands()) {
                if ((stringArray.length <= subCommand2.getArgumentsSize() || !subCommand2.isEndless()) && (subCommand2.getArgumentsSize() > stringArray.length || !subCommand2.isMatch(stringArray))) continue;
                subCommand = subCommand2;
                break;
            }
            if (subCommand == null) {
                simpleCommand.middleMan(commandSender, stringArray);
                return true;
            }
            if (!(subCommand.doesInheritPermission() || subCommand.getPermission() == null || commandSender.hasPermission(subCommand.getPermission()) || simpleCommand.getPermission() == null || simpleCommand.getPermission().isEmpty())) {
                Text.sendMessage(commandSender, subCommand.getNoPermissionLang(commandSender));
                return true;
            }
            if (!subCommand.isConsole() && commandSender instanceof ConsoleCommandSender) {
                commandSender.sendMessage("The console can not execute this command.");
                return true;
            }
            subCommand.middleMan(commandSender, stringArray);
        }
        return true;
    }

    public List<String> onTabComplete(CommandSender commandSender, Command command, String string, String[] stringArray) {
        ArrayList<String> arrayList = Lists.newArrayList();
        String string2 = command.getName();
        for (SimpleCommand<? extends CommandSender> simpleCommand : this.commands) {
            if (!simpleCommand.getCommand().equalsIgnoreCase(string2) || simpleCommand.getPermission() != null && !simpleCommand.getPermission().isEmpty() && !commandSender.hasPermission(simpleCommand.getPermission()) || !simpleCommand.isConsole() && commandSender instanceof ConsoleCommandSender || stringArray.length == 0) continue;
            if (simpleCommand.getSubCommands().isEmpty()) {
                arrayList.addAll(simpleCommand.tabCompletionSuggestion(commandSender, stringArray.length - 1));
                continue;
            }
            HashSet<SubCommand> hashSet = Sets.newHashSet();
            for (SubCommand subCommand : simpleCommand.getSubCommands()) {
                if (!subCommand.isMatchUntilIndex(stringArray, stringArray.length - 1)) continue;
                hashSet.add(subCommand);
            }
            if (hashSet.isEmpty()) continue;
            for (SubCommand subCommand : hashSet) {
                if (!subCommand.doesInheritPermission() && subCommand.getPermission() != null && !commandSender.hasPermission(subCommand.getPermission()) && simpleCommand.getPermission() != null && !simpleCommand.getPermission().isEmpty() || !subCommand.isConsole() && commandSender instanceof ConsoleCommandSender) continue;
                arrayList.addAll(subCommand.tabCompletionSuggestion(commandSender, stringArray.length - 1));
            }
        }
        ArrayList arrayList2 = new ArrayList();
        StringUtil.copyPartialMatches((String)stringArray[stringArray.length - 1], arrayList, (Collection)arrayList2);
        Collections.sort(arrayList2);
        return arrayList2;
    }

    private void registerArgumentTypes() {
        this.registerArgumentType(String.class, string -> string).registerArgumentType(Player.class, Bukkit::getPlayerExact).registerArgumentType(OfflinePlayer.class, Bukkit::getOfflinePlayer).registerArgumentType(Integer.class, string -> NumberUtils.isNumber((String)string) ? Integer.parseInt(string) : 0).registerArgumentType(Boolean.class, string -> string.equalsIgnoreCase("true") || (string.equalsIgnoreCase("false") ? Boolean.valueOf(false) : null) != false);
    }

    public Set<SimpleCommand<? extends CommandSender>> getCommands() {
        return this.commands;
    }
}

