/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.command.CommandSender
 *  org.bukkit.entity.Player
 *  org.bukkit.plugin.java.JavaPlugin
 *  org.jetbrains.annotations.Nullable
 */
package net.advancedplugins.as.impl.utils.commands;

import java.util.function.Function;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.Nullable;

public abstract class Command<T extends CommandSender> {
    protected final JavaPlugin plugin;
    private final String permission;
    private final boolean isConsole;
    private Function<T, String> noPermission = commandSender -> "&cYou do not have permission to do this.";
    private Function<T, String> notOnline = commandSender -> "&cPlayer is not online.";

    public Command(JavaPlugin javaPlugin, String string, boolean bl) {
        this.plugin = javaPlugin;
        this.permission = string;
        this.isConsole = bl;
    }

    public abstract void onExecute(T var1, String[] var2);

    public void middleMan(CommandSender commandSender, String[] stringArray) {
        this.onExecute(commandSender, stringArray);
    }

    @Nullable
    public String getPermission() {
        return this.permission;
    }

    public boolean isConsole() {
        return this.isConsole;
    }

    public void noPermissionLang(Function<T, String> function) {
        this.noPermission = function;
    }

    public void setNotOnlineLang(Function<T, String> function) {
        this.notOnline = function;
    }

    public String getNoPermissionLang(CommandSender commandSender) {
        return this.noPermission.apply(commandSender);
    }

    public String getNotOnlineLang(CommandSender commandSender) {
        return this.notOnline.apply(commandSender);
    }

    public boolean isPlayerOnline(CommandSender commandSender, Player player) {
        if (player == null || !player.isOnline()) {
            commandSender.sendMessage(this.notOnline.apply(commandSender));
            return false;
        }
        return true;
    }
}

