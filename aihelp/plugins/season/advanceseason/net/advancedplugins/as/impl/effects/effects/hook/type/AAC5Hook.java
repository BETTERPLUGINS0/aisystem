/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  me.konsolas.aac.api.AACAPI
 *  me.konsolas.aac.api.AACExemption
 *  org.bukkit.Bukkit
 *  org.bukkit.entity.Player
 *  org.jetbrains.annotations.NotNull
 */
package net.advancedplugins.as.impl.effects.effects.hook.type;

import java.util.Objects;
import me.konsolas.aac.api.AACAPI;
import me.konsolas.aac.api.AACExemption;
import net.advancedplugins.as.impl.effects.effects.EffectsHandler;
import net.advancedplugins.as.impl.effects.effects.hook.AntiCheatHook;
import net.advancedplugins.as.impl.utils.ASManager;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class AAC5Hook
implements AntiCheatHook {
    @Override
    public boolean isPresent() {
        return Bukkit.getPluginManager().getPlugin("AAC5") != null;
    }

    @Override
    public boolean isRunning() {
        return Bukkit.getPluginManager().isPluginEnabled("AAC5");
    }

    @Override
    public void register() {
        if (this.isPresent() && this.isRunning()) {
            ASManager.getInstance().getLogger().info("Registering AAC5 Hook and enabling module...");
            EffectsHandler.getAntiCheatHooks().put("AAC5", this);
        }
    }

    @Override
    public boolean exemptDebug(@NotNull Player player) {
        ASManager.getInstance().getLogger().info("Trying to exempt " + player.getName() + " from AAC 5.");
        return this.exempt(player);
    }

    @Override
    public boolean exempt(@NotNull Player player) {
        if (!this.isRunning()) {
            return false;
        }
        AACExemption aACExemption = new AACExemption("AdvancedEnchantments Enchantment");
        ((AACAPI)Bukkit.getServicesManager().load(AACAPI.class)).addExemption(player, aACExemption);
        aac5Exemptions.put(player.getUniqueId(), aACExemption);
        return true;
    }

    @Override
    public void nonExempt(@NotNull Player player) {
        try {
            Objects.requireNonNull((AACAPI)Bukkit.getServicesManager().load(AACAPI.class)).removeExemption(player, (AACExemption)aac5Exemptions.remove(player.getUniqueId()));
        } catch (Exception exception) {
            // empty catch block
        }
    }

    @Override
    public AntiCheatHook getHook() {
        return this;
    }
}

