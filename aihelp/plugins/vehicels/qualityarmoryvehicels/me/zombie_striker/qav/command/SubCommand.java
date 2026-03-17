/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.ChatColor
 *  org.bukkit.command.CommandSender
 *  org.jetbrains.annotations.NotNull
 *  org.jetbrains.annotations.Nullable
 */
package me.zombie_striker.qav.command;

import java.util.ArrayList;
import java.util.List;
import me.zombie_striker.qav.command.QAVCommand;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public abstract class SubCommand {
    protected QAVCommand command;

    public SubCommand(QAVCommand qAVCommand) {
        this.command = qAVCommand;
    }

    public abstract String getName();

    @Nullable
    public abstract String getDescription(@NotNull CommandSender var1);

    public QAVCommand getCommand() {
        return this.command;
    }

    public abstract void perform(CommandSender var1, String[] var2);

    public List<String> complete(CommandSender commandSender, String[] stringArray) {
        return new ArrayList<String>();
    }

    public void help(CommandSender commandSender) {
        if (this.getDescription(commandSender) == null) {
            return;
        }
        commandSender.sendMessage("/QAV " + this.getName() + ChatColor.GRAY + this.getDescription(commandSender));
    }

    public String toString() {
        return this.getName();
    }
}

