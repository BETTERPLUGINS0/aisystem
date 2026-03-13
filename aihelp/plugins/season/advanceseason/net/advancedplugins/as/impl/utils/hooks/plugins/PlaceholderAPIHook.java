/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  me.clip.placeholderapi.PlaceholderAPI
 *  me.clip.placeholderapi.PlaceholderHook
 *  org.bukkit.OfflinePlayer
 *  org.bukkit.entity.Player
 */
package net.advancedplugins.as.impl.utils.hooks.plugins;

import me.clip.placeholderapi.PlaceholderAPI;
import me.clip.placeholderapi.PlaceholderHook;
import net.advancedplugins.as.impl.utils.hooks.HookPlugin;
import net.advancedplugins.as.impl.utils.hooks.PluginHookInstance;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

public class PlaceholderAPIHook
extends PluginHookInstance {
    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public String getName() {
        return HookPlugin.PLACEHOLDERAPI.getPluginName();
    }

    public String parsePlaceholder(OfflinePlayer offlinePlayer, String string) {
        return PlaceholderAPI.setPlaceholders((OfflinePlayer)offlinePlayer, (String)string);
    }

    public String parsePlaceholders(Player player, Player player2, String string) {
        string = this.parsePlaceholder((OfflinePlayer)player, string);
        return PlaceholderAPI.setRelationalPlaceholders((Player)player, (Player)player2, (String)string);
    }

    public boolean registerPlaceholder(String string, PlaceholderHook placeholderHook) {
        return PlaceholderAPI.registerPlaceholderHook((String)string, (PlaceholderHook)placeholderHook);
    }
}

