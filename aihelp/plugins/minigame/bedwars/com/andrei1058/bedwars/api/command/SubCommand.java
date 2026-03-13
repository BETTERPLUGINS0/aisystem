/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  net.md_5.bungee.api.chat.TextComponent
 *  org.bukkit.command.CommandSender
 *  org.bukkit.command.ConsoleCommandSender
 *  org.bukkit.entity.Player
 */
package com.andrei1058.bedwars.api.command;

import com.andrei1058.bedwars.api.BedWars;
import com.andrei1058.bedwars.api.command.ParentCommand;
import java.util.List;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

public abstract class SubCommand {
    private String name;
    private boolean show = false;
    private ParentCommand parent;
    private int priority = 20;
    private TextComponent displayInfo;
    private boolean arenaSetupCommand = false;
    private String permission = "";

    public SubCommand(ParentCommand parent, String name) {
        this.name = name;
        this.parent = parent;
        parent.addSubCommand(this);
    }

    public abstract boolean execute(String[] var1, CommandSender var2);

    public String getSubCommandName() {
        return this.name;
    }

    public void showInList(boolean value) {
        this.show = value;
    }

    public void setDisplayInfo(TextComponent displayInfo) {
        this.displayInfo = displayInfo;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public ParentCommand getParent() {
        return this.parent;
    }

    public int getPriority() {
        return this.priority;
    }

    public TextComponent getDisplayInfo() {
        return this.displayInfo;
    }

    public void setArenaSetupCommand(boolean arenaSetupCommand) {
        this.arenaSetupCommand = arenaSetupCommand;
    }

    public boolean isArenaSetupCommand() {
        return this.arenaSetupCommand;
    }

    public boolean isShow() {
        return this.show;
    }

    public void setPermission(String permission) {
        this.permission = permission;
    }

    public boolean hasPermission(CommandSender p) {
        return this.permission.isEmpty() || p.hasPermission("bw.*") || p.hasPermission(this.permission);
    }

    public boolean canSee(CommandSender sender, BedWars api) {
        if (sender instanceof ConsoleCommandSender) {
            return false;
        }
        if (this.isArenaSetupCommand() && api.isInSetupSession(((Player)sender).getUniqueId())) {
            return true;
        }
        if (!this.isArenaSetupCommand() && api.isInSetupSession(((Player)sender).getUniqueId())) {
            return false;
        }
        return !this.isArenaSetupCommand() && this.hasPermission(sender);
    }

    public abstract List<String> getTabComplete();
}

