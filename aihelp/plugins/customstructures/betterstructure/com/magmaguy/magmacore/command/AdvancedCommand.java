/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  lombok.Generated
 *  org.bukkit.command.Command
 *  org.bukkit.command.CommandSender
 *  org.bukkit.plugin.java.JavaPlugin
 */
package com.magmaguy.magmacore.command;

import com.magmaguy.magmacore.command.CommandData;
import com.magmaguy.magmacore.command.SenderType;
import com.magmaguy.magmacore.command.arguments.ICommandArgument;
import com.magmaguy.magmacore.command.arguments.LiteralCommandArgument;
import com.magmaguy.magmacore.command.arguments.PlayerCommandArgument;
import com.magmaguy.magmacore.util.Logger;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.Generated;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;

public abstract class AdvancedCommand {
    private final List<String> aliases;
    private final boolean enabled = true;
    private final List<ICommandArgument> argumentsList = new ArrayList<ICommandArgument>();
    private final Map<String, Integer> argumentsMap = new HashMap<String, Integer>();
    private String usage;
    private String description;
    private String permission = "";
    private SenderType senderType = SenderType.ANY;

    public AdvancedCommand(List<String> aliases) {
        this.aliases = aliases;
    }

    public static Command toBukkitCommand(JavaPlugin plugin, AdvancedCommand delegate, String name, List<String> aliases) {
        return new AdvancedBukkitCommand(plugin, delegate, name, aliases);
    }

    public boolean aliasMatches(String potentialAlias) {
        for (String alias : this.aliases) {
            if (!alias.equals(potentialAlias)) continue;
            return true;
        }
        return false;
    }

    public boolean aliasStartMatches(String potentialAliasStart) {
        for (String alias : this.aliases) {
            if (!alias.startsWith(potentialAliasStart)) continue;
            return true;
        }
        return false;
    }

    protected void setPermission(String permission) {
        this.permission = permission;
    }

    protected void setDescription(String description) {
        this.description = description;
    }

    protected void setSenderType(SenderType senderType) {
        this.senderType = senderType;
    }

    protected void addLiteral(String key) {
        this.addArgument(key, new LiteralCommandArgument(key));
    }

    protected void addPlayerArgument(String key) {
        this.addArgument(key, new PlayerCommandArgument());
    }

    protected void addArgument(String key, ICommandArgument arg) {
        this.argumentsMap.put(key, this.argumentsList.size());
        this.argumentsList.add(arg);
    }

    protected void setUsage(String usage) {
        this.usage = usage;
    }

    public String getStringArgument(String key, CommandSender commandSender, String[] args2) {
        try {
            return args2[this.argumentsMap.get(key) + 1];
        } catch (Exception e) {
            Logger.sendMessage(commandSender, "Key " + key + " not found");
            return null;
        }
    }

    public Integer getIntegerArgument(String key, CommandSender commandSender, String[] args2) {
        try {
            return Integer.parseInt(args2[this.argumentsMap.get(key) + 1]);
        } catch (Exception e) {
            Logger.sendMessage(commandSender, "Key " + key + " not found");
            return null;
        }
    }

    public Double getDoubleArgument(String key, CommandSender commandSender, String[] args2) {
        try {
            return Double.parseDouble(args2[this.argumentsMap.get(key) + 1]);
        } catch (Exception e) {
            Logger.sendMessage(commandSender, "Key " + key + " not found");
            return null;
        }
    }

    public String getStringSequenceArgument(String key, CommandSender commandSender, String[] args2) {
        try {
            StringBuilder output = new StringBuilder();
            for (int i = this.argumentsMap.get(key) + 1; i < args2.length; ++i) {
                output.append(args2[i]).append(" ");
            }
            return output.toString();
        } catch (Exception e) {
            Logger.sendMessage(commandSender, "Key " + key + " not found");
            return null;
        }
    }

    private boolean validateArgument(int index, CommandSender sender) {
        if (this.argumentsList.size() <= index) {
            Logger.sendMessage(sender, "Incorrect usage of this command!");
            Logger.sendMessage(sender, this.description);
            return false;
        }
        return true;
    }

    public abstract void execute(CommandData var1);

    public List<String> onTabComplete(String[] args2) {
        int index = args2.length - 2;
        if (index < 0 || this.argumentsList.size() <= index) {
            return Collections.emptyList();
        }
        ICommandArgument iCommandArgument = this.argumentsList.get(index);
        if (iCommandArgument instanceof List) {
            List list = (List)((Object)iCommandArgument);
            return list;
        }
        if (!this.argumentsList.get(index).toString().isEmpty()) {
            return List.of((Object)this.argumentsList.get(index).toString());
        }
        return Collections.emptyList();
    }

    @Generated
    public List<String> getAliases() {
        return this.aliases;
    }

    @Generated
    public boolean isEnabled() {
        return this.enabled;
    }

    @Generated
    public List<ICommandArgument> getArgumentsList() {
        return this.argumentsList;
    }

    @Generated
    public Map<String, Integer> getArgumentsMap() {
        return this.argumentsMap;
    }

    @Generated
    public String getUsage() {
        return this.usage;
    }

    @Generated
    public String getDescription() {
        return this.description;
    }

    @Generated
    public String getPermission() {
        return this.permission;
    }

    @Generated
    public SenderType getSenderType() {
        return this.senderType;
    }

    private static class AdvancedBukkitCommand
    extends Command {
        private final AdvancedCommand delegate;
        private final JavaPlugin plugin;

        AdvancedBukkitCommand(JavaPlugin plugin, AdvancedCommand delegate, String name, List<String> aliases) {
            super(name, delegate.getDescription(), delegate.getUsage(), aliases);
            this.delegate = delegate;
            this.plugin = plugin;
            if (!delegate.getPermission().isBlank()) {
                this.setPermission(delegate.getPermission());
                this.setPermissionMessage("\u00a7cYou lack permission: " + delegate.getPermission());
            }
        }

        public boolean execute(CommandSender sender, String label, String[] args2) {
            this.delegate.execute(new CommandData(sender, args2, this.delegate));
            return true;
        }

        public List<String> tabComplete(CommandSender sender, String alias, String[] args2) throws IllegalArgumentException {
            return this.delegate.onTabComplete(args2);
        }
    }
}

