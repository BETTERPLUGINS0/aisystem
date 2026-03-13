/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  github.scarsz.discordsrv.DiscordSRV
 *  org.bukkit.entity.Player
 *  org.bukkit.event.Event
 *  org.jetbrains.annotations.NotNull
 */
package net.advancedplugins.as.impl.utils.hooks.plugins;

import github.scarsz.discordsrv.DiscordSRV;
import net.advancedplugins.as.impl.utils.hooks.HookPlugin;
import net.advancedplugins.as.impl.utils.hooks.PluginHookInstance;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.jetbrains.annotations.NotNull;

public class DiscordSRVHook
extends PluginHookInstance {
    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public String getName() {
        return HookPlugin.DISCORDSRV.getPluginName();
    }

    public void processChatMessage(@NotNull Player player, @NotNull String string, @NotNull String string2, @NotNull Event event) {
        DiscordSRV.getPlugin().processChatMessage(player, string, string2, false, event);
    }

    public void processGlobalChatMessage(@NotNull Player player, @NotNull String string, @NotNull Event event) {
        this.processChatMessage(player, string, DiscordSRV.getPlugin().getOptionalChannel("global"), event);
    }
}

