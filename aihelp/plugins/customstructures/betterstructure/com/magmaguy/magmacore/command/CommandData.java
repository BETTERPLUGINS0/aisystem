/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  lombok.Generated
 *  org.bukkit.command.CommandSender
 *  org.bukkit.entity.Player
 */
package com.magmaguy.magmacore.command;

import com.magmaguy.magmacore.command.AdvancedCommand;
import lombok.Generated;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandData {
    private final String[] args;
    private final CommandSender commandSender;
    private final AdvancedCommand advancedCommand;

    public CommandData(CommandSender commandSender, String[] args2, AdvancedCommand advancedCommand) {
        this.commandSender = commandSender;
        this.args = args2;
        this.advancedCommand = advancedCommand;
    }

    public Player getPlayerSender() {
        return (Player)this.commandSender;
    }

    public String getStringArgument(String key) {
        return this.advancedCommand.getStringArgument(key, this.commandSender, this.args);
    }

    public Integer getIntegerArgument(String key) {
        return this.advancedCommand.getIntegerArgument(key, this.commandSender, this.args);
    }

    public Double getDoubleArgument(String key) {
        return this.advancedCommand.getDoubleArgument(key, this.commandSender, this.args);
    }

    public String getStringSequenceArgument(String key) {
        return this.advancedCommand.getStringSequenceArgument(key, this.commandSender, this.args);
    }

    @Generated
    public String[] getArgs() {
        return this.args;
    }

    @Generated
    public CommandSender getCommandSender() {
        return this.commandSender;
    }
}

