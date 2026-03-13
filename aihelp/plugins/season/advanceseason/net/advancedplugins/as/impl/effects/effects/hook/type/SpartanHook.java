/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  me.vagdedes.spartan.api.API
 *  me.vagdedes.spartan.api.PlayerViolationEvent
 *  me.vagdedes.spartan.api.system.Enums$HackType
 *  org.bukkit.Bukkit
 *  org.bukkit.entity.Player
 *  org.bukkit.event.EventHandler
 *  org.bukkit.event.EventPriority
 *  org.bukkit.event.Listener
 *  org.bukkit.plugin.Plugin
 *  org.jetbrains.annotations.NotNull
 */
package net.advancedplugins.as.impl.effects.effects.hook.type;

import me.vagdedes.spartan.api.API;
import me.vagdedes.spartan.api.PlayerViolationEvent;
import me.vagdedes.spartan.api.system.Enums;
import net.advancedplugins.as.impl.effects.effects.EffectsHandler;
import net.advancedplugins.as.impl.effects.effects.hook.AntiCheatHook;
import net.advancedplugins.as.impl.utils.ASManager;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

public class SpartanHook
implements AntiCheatHook {
    @Override
    public boolean isPresent() {
        return Bukkit.getPluginManager().getPlugin("Spartan") != null;
    }

    @Override
    public boolean isRunning() {
        return Bukkit.getPluginManager().isPluginEnabled("Spartan");
    }

    @Override
    public void register() {
        if (this.isPresent() && this.isRunning()) {
            ASManager.getInstance().getLogger().info("Registering Spartan Hook and enabling module...");
            Bukkit.getPluginManager().registerEvents((Listener)new SpartanViolationEvent(), (Plugin)ASManager.getInstance());
            EffectsHandler.getAntiCheatHooks().put("Spartan", this);
        }
    }

    @Override
    public boolean exemptDebug(@NotNull Player player) {
        ASManager.getInstance().getLogger().info("Trying to exempt " + player.getName() + " from Spartan.");
        return this.exempt(player);
    }

    @Override
    public boolean exempt(@NotNull Player player) {
        if (!this.isRunning()) {
            return false;
        }
        try {
            for (Enums.HackType hackType : Enums.HackType.values()) {
                API.cancelCheck((Player)player, (Enums.HackType)hackType, (int)20);
            }
            return generalExemptions.add(player.getUniqueId());
        } catch (Exception exception) {
            return false;
        }
    }

    @Override
    public void nonExempt(@NotNull Player player) {
        generalExemptions.remove(player.getUniqueId());
    }

    @Override
    public AntiCheatHook getHook() {
        return this;
    }

    private static class SpartanViolationEvent
    implements Listener {
        private SpartanViolationEvent() {
        }

        @EventHandler(priority=EventPriority.HIGH, ignoreCancelled=true)
        public void onSpartanViolationEvent(@NotNull PlayerViolationEvent playerViolationEvent) {
            if (AntiCheatHook.generalExemptions.contains(playerViolationEvent.getPlayer().getUniqueId())) {
                playerViolationEvent.setCancelled(true);
            }
        }
    }
}

