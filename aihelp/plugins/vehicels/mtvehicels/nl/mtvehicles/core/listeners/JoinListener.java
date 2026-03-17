/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.command.CommandSender
 *  org.bukkit.event.EventHandler
 *  org.bukkit.event.player.PlayerJoinEvent
 */
package nl.mtvehicles.core.listeners;

import nl.mtvehicles.core.infrastructure.dataconfig.DefaultConfig;
import nl.mtvehicles.core.infrastructure.models.MTVListener;
import nl.mtvehicles.core.infrastructure.modules.ConfigModule;
import nl.mtvehicles.core.infrastructure.modules.VersionModule;
import nl.mtvehicles.core.infrastructure.utils.PluginUpdater;
import nl.mtvehicles.core.infrastructure.utils.TextUtils;
import nl.mtvehicles.core.movement.MovementManager;
import org.bukkit.command.CommandSender;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerJoinEvent;

public class JoinListener
extends MTVListener {
    @EventHandler
    public void onJoinEventPlayer(PlayerJoinEvent event) {
        this.event = event;
        this.player = event.getPlayer();
        MovementManager.MovementSelector(this.player);
        if (ConfigModule.secretSettings.getMessagesLanguage().contains("ns") && (this.player.hasPermission("mtvehicles.language") || this.player.hasPermission("mtvehicles.admin"))) {
            this.player.sendMessage(TextUtils.colorize("&cHey! You have not changed the language of the plugin yet. Do this by executing &4/vehicle language&c!"));
        }
        if (!this.player.hasPermission("mtvehicles.update") || !((Boolean)ConfigModule.defaultConfig.get(DefaultConfig.Option.AUTO_UPDATE)).booleanValue()) {
            return;
        }
        if (!VersionModule.isDevRelease) {
            PluginUpdater.checkNewVersion((CommandSender)this.player);
        }
    }
}

