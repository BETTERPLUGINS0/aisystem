/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  lombok.Generated
 *  org.bukkit.command.Command
 *  org.bukkit.command.CommandExecutor
 *  org.bukkit.command.CommandSender
 *  org.bukkit.command.TabCompleter
 *  org.bukkit.entity.Player
 *  org.bukkit.plugin.java.JavaPlugin
 */
package com.magmaguy.magmacore.command;

import com.magmaguy.magmacore.command.AdvancedCommand;
import com.magmaguy.magmacore.command.CommandData;
import com.magmaguy.magmacore.command.SenderType;
import com.magmaguy.magmacore.command.arguments.ICommandArgument;
import com.magmaguy.magmacore.command.arguments.LiteralCommandArgument;
import com.magmaguy.magmacore.util.Logger;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import lombok.Generated;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class CommandManager
implements CommandExecutor,
TabCompleter {
    private static final HashSet<CommandManager> commandManagers = new HashSet();
    public final List<AdvancedCommand> commands = new ArrayList<AdvancedCommand>();
    private final String commandExtension;

    public CommandManager(JavaPlugin javaPlugin, String commandExtension) {
        javaPlugin.getCommand(commandExtension).setExecutor((CommandExecutor)this);
        this.commandExtension = commandExtension;
        commandManagers.add(this);
    }

    public static void shutdown() {
        commandManagers.forEach(CommandManager::clearAllCommands);
        commandManagers.clear();
    }

    public void clearAllCommands() {
        this.commands.clear();
    }

    public void registerCommand(AdvancedCommand command) {
        this.commands.add(command);
    }

    public void unregisterCommand(Command command) {
        this.commands.remove(command);
    }

    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args2) {
        if (args2.length == 0) {
            for (AdvancedCommand command2 : this.commands) {
                if (!command2.getAliases().isEmpty()) continue;
                command2.execute(new CommandData(sender, args2, command2));
                return true;
            }
            this.commands.forEach(command -> sender.sendMessage(command.getUsage()));
            return true;
        }
        for (AdvancedCommand command3 : this.commands) {
            if (!command3.isEnabled() || !command3.getAliases().contains(args2[0]) || args2.length != command3.getArgumentsList().size() + 1) continue;
            boolean valid = true;
            for (int i = 0; i < command3.getArgumentsList().size(); ++i) {
                if (!command3.getArgumentsList().get(i).isLiteral() || ((LiteralCommandArgument)command3.getArgumentsList().get(i)).getLiteral().equals(args2[i + 1])) continue;
                valid = false;
                break;
            }
            if (!valid) continue;
            if (command3.getSenderType() == SenderType.PLAYER && !(sender instanceof Player)) {
                Logger.sendMessage(sender, "This command must be run as a player!");
                return false;
            }
            if (!this.permissionCheck(sender, command3)) {
                Logger.sendMessage(sender, "You do not have permission to run this command!");
                return false;
            }
            command3.execute(new CommandData(sender, args2, command3));
            return true;
        }
        ArrayList<AdvancedCommand> suggestions = new ArrayList<AdvancedCommand>();
        block3: for (AdvancedCommand command4 : this.commands) {
            if (!command4.isEnabled()) continue;
            for (String alias : command4.getAliases()) {
                if (!alias.toLowerCase().startsWith(args2[0].toLowerCase())) continue;
                suggestions.add(command4);
                continue block3;
            }
        }
        if (!suggestions.isEmpty()) {
            Logger.sendMessage(sender, "Unknown command! Did you mean one of the following?");
            for (AdvancedCommand suggestion : suggestions) {
                sender.sendMessage(" " + suggestion.getUsage());
            }
        } else {
            Logger.sendMessage(sender, "Unknown command!");
        }
        return false;
    }

    public List<String> onTabComplete(CommandSender sender, Command cmd, String label, String[] args2) {
        return this.tabCompleteRestOfArguments(sender, args2);
    }

    private List<String> tabCompleteRestOfArguments(CommandSender sender, String[] args2) {
        if (args2[0] == null) {
            return List.of();
        }
        ArrayList<String> completions = new ArrayList<String>();
        if (args2.length == 1) {
            for (AdvancedCommand command : this.commands) {
                if (!command.aliasStartMatches(args2[0])) continue;
                completions.addAll(command.getAliases());
            }
            return completions;
        }
        for (AdvancedCommand command : this.commands) {
            if (!command.aliasMatches(args2[0]) || !command.isEnabled() || !this.permissionCheck(sender, command)) continue;
            int currentArgumentIndex = args2.length - 2;
            String currentArgument = args2[args2.length - 1];
            if (currentArgumentIndex >= command.getArgumentsList().size()) continue;
            boolean argumentsSoFarValid = true;
            for (int i = 0; i < currentArgumentIndex; ++i) {
                ICommandArgument argDef = command.getArgumentsList().get(i);
                if (argDef.matchesInput(args2[i + 1])) continue;
                argumentsSoFarValid = false;
                break;
            }
            if (!argumentsSoFarValid) continue;
            completions.addAll(command.getArgumentsList().get(currentArgumentIndex).getSuggestions(sender, currentArgument));
        }
        return completions;
    }

    private boolean permissionCheck(CommandSender commandSender, AdvancedCommand command) {
        return commandSender.hasPermission(command.getPermission()) || command.getPermission().equalsIgnoreCase("") || command.getPermission().equalsIgnoreCase(this.commandExtension + ".");
    }

    @Generated
    public static HashSet<CommandManager> getCommandManagers() {
        return commandManagers;
    }
}

