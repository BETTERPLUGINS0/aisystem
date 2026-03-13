/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  de.jpx3.intave.access.check.event.IntaveViolationEvent
 *  de.jpx3.intave.access.check.event.IntaveViolationEvent$Reaction
 *  org.bukkit.Bukkit
 *  org.bukkit.entity.Player
 *  org.bukkit.event.EventHandler
 *  org.bukkit.event.EventPriority
 *  org.bukkit.event.Listener
 *  org.bukkit.plugin.Plugin
 *  org.jetbrains.annotations.NotNull
 */
package net.advancedplugins.as.impl.effects.effects.hook.type;

import de.jpx3.intave.access.check.event.IntaveViolationEvent;
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

public class IntaveHook
implements AntiCheatHook {
    @Override
    public boolean isPresent() {
        return Bukkit.getPluginManager().getPlugin("Intave") != null;
    }

    @Override
    public boolean isRunning() {
        return Bukkit.getPluginManager().isPluginEnabled("Intave");
    }

    @Override
    public void register() {
        if (this.isPresent() && this.isRunning()) {
            ASManager.getInstance().getLogger().info("Registering Intave Hook and enabling module...");
            Bukkit.getPluginManager().registerEvents((Listener)new IntaveViolationEventListener(), (Plugin)ASManager.getInstance());
            EffectsHandler.getAntiCheatHooks().put("Intave", this);
        }
    }

    @Override
    public boolean exemptDebug(@NotNull Player player) {
        ASManager.getInstance().getLogger().info("Trying to exempt " + player.getName() + " from Intave.");
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

    private static class IntaveViolationEventListener
    implements Listener {
        private IntaveViolationEventListener() {
        }

        @EventHandler(priority=EventPriority.HIGH, ignoreCancelled=true)
        public void onVulcanFlagEvent(IntaveViolationEvent intaveViolationEvent) {
            if (AntiCheatHook.generalExemptions.contains(intaveViolationEvent.player().getUniqueId())) {
                intaveViolationEvent.suggestReaction(IntaveViolationEvent.Reaction.IGNORE);
            }
        }
    }
}

