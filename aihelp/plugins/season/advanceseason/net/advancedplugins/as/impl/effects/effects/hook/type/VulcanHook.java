/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  me.frep.vulcan.api.event.VulcanFlagEvent
 *  me.frep.vulcan.api.event.VulcanSetbackEvent
 *  org.bukkit.Bukkit
 *  org.bukkit.entity.Player
 *  org.bukkit.event.EventHandler
 *  org.bukkit.event.EventPriority
 *  org.bukkit.event.Listener
 *  org.bukkit.plugin.Plugin
 *  org.jetbrains.annotations.NotNull
 */
package net.advancedplugins.as.impl.effects.effects.hook.type;

import java.util.UUID;
import me.frep.vulcan.api.event.VulcanFlagEvent;
import me.frep.vulcan.api.event.VulcanSetbackEvent;
import net.advancedplugins.as.impl.effects.effects.EffectsHandler;
import net.advancedplugins.as.impl.effects.effects.effects.internal.LavaWalkerEffect;
import net.advancedplugins.as.impl.effects.effects.effects.internal.WaterWalkerEffect;
import net.advancedplugins.as.impl.effects.effects.hook.AntiCheatHook;
import net.advancedplugins.as.impl.utils.ASManager;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

public class VulcanHook
implements AntiCheatHook {
    @Override
    public boolean isPresent() {
        return Bukkit.getPluginManager().getPlugin("Vulcan") != null;
    }

    @Override
    public boolean isRunning() {
        return Bukkit.getPluginManager().isPluginEnabled("Vulcan");
    }

    @Override
    public void register() {
        if (this.isPresent() && this.isRunning()) {
            ASManager.getInstance().getLogger().info("Registering Vulcan Hook and enabling module...");
            Bukkit.getPluginManager().registerEvents((Listener)new VulcanViolationEvent(), (Plugin)ASManager.getInstance());
            EffectsHandler.getAntiCheatHooks().put("Vulcan", this);
        }
    }

    @Override
    public boolean exemptDebug(@NotNull Player player) {
        ASManager.getInstance().getLogger().info("Trying to exempt " + player.getName() + " from Vulcan.");
        return this.exempt(player);
    }

    @Override
    public boolean exempt(@NotNull Player player) {
        if (!this.isRunning()) {
            return false;
        }
        return generalExemptions.add(player.getUniqueId());
    }

    @Override
    public void nonExempt(@NotNull Player player) {
        generalExemptions.remove(player.getUniqueId());
    }

    @Override
    public AntiCheatHook getHook() {
        return this;
    }

    private static class VulcanViolationEvent
    implements Listener {
        private VulcanViolationEvent() {
        }

        @EventHandler(priority=EventPriority.HIGH, ignoreCancelled=true)
        public void onVulcanSetbackEvent(VulcanSetbackEvent vulcanSetbackEvent) {
            if (AntiCheatHook.generalExemptions.contains(vulcanSetbackEvent.getPlayer().getUniqueId())) {
                vulcanSetbackEvent.setCancelled(true);
            }
        }

        @EventHandler(priority=EventPriority.HIGH, ignoreCancelled=true)
        public void onVulcanFlagEvent(VulcanFlagEvent vulcanFlagEvent) {
            UUID uUID = vulcanFlagEvent.getPlayer().getUniqueId();
            if (AntiCheatHook.generalExemptions.contains(uUID)) {
                vulcanFlagEvent.setCancelled(true);
            }
            if (WaterWalkerEffect.ACTIVATED_USERS.containsKey(uUID) || LavaWalkerEffect.ACTIVATED_USERS.containsKey(uUID) && vulcanFlagEvent.getCheck().getName().equals("fastbreak")) {
                vulcanFlagEvent.setCancelled(true);
            }
        }
    }
}

