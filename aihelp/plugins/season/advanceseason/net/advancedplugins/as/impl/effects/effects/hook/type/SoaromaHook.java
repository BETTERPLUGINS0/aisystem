/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  me.korbsti.soaromaac.api.SoaromaFlagEvent
 *  net.advancedplugins.ae.utils.YamlFile
 *  org.bukkit.Bukkit
 *  org.bukkit.entity.Player
 *  org.bukkit.event.EventHandler
 *  org.bukkit.event.EventPriority
 *  org.bukkit.event.Listener
 *  org.bukkit.plugin.Plugin
 *  org.jetbrains.annotations.NotNull
 */
package net.advancedplugins.as.impl.effects.effects.hook.type;

import me.korbsti.soaromaac.api.SoaromaFlagEvent;
import net.advancedplugins.ae.utils.YamlFile;
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

public class SoaromaHook
implements AntiCheatHook {
    @Override
    public boolean isPresent() {
        return Bukkit.getPluginManager().getPlugin("SoaromaSAC") != null;
    }

    @Override
    public boolean isRunning() {
        return Bukkit.getPluginManager().isPluginEnabled("SoaromaSAC");
    }

    @Override
    public void register() {
        if (this.isPresent() && this.isRunning()) {
            ASManager.getInstance().getLogger().info("Registering SoaromaSAC Hook and enabling module...");
            if (!new YamlFile("../SoaromaSAC/main.yml").getBoolean("other.enableAPI")) {
                ASManager.getInstance().getLogger().severe("You need to set 'other.enableAPI' to 'true' in SoaromaSAC's main.yml file. If you don't do this, you *will* get false positives from AE.");
                return;
            }
            Bukkit.getPluginManager().registerEvents((Listener)new SoaromaViolationEventListener(), (Plugin)ASManager.getInstance());
            EffectsHandler.getAntiCheatHooks().put("SoaromaSAC", this);
        }
    }

    @Override
    public boolean exemptDebug(@NotNull Player player) {
        ASManager.getInstance().getLogger().info("Trying to exempt " + player.getName() + " from SoaromaSAC.");
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

    private static class SoaromaViolationEventListener
    implements Listener {
        private SoaromaViolationEventListener() {
        }

        @EventHandler(priority=EventPriority.HIGH, ignoreCancelled=true)
        public void onSoaromaFlagEvent(SoaromaFlagEvent soaromaFlagEvent) {
            if (AntiCheatHook.generalExemptions.contains(soaromaFlagEvent.getFlaggedPlayer().getUniqueId())) {
                soaromaFlagEvent.setDisabler(10);
            }
        }
    }
}

