/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  fr.neatmonster.nocheatplus.checks.CheckType
 *  me.asofold.bpl.cncp.hooks.generic.ClassExemptionHook
 *  org.bukkit.Bukkit
 *  org.bukkit.entity.Player
 *  org.jetbrains.annotations.NotNull
 */
package net.advancedplugins.as.impl.effects.effects.hook.type;

import fr.neatmonster.nocheatplus.checks.CheckType;
import me.asofold.bpl.cncp.hooks.generic.ClassExemptionHook;
import net.advancedplugins.as.impl.effects.effects.EffectsHandler;
import net.advancedplugins.as.impl.effects.effects.hook.AntiCheatHook;
import net.advancedplugins.as.impl.utils.ASManager;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class CNCPHook
extends ClassExemptionHook
implements AntiCheatHook {
    public CNCPHook() {
        super("ae.");
    }

    @Override
    public boolean isPresent() {
        return Bukkit.getPluginManager().getPlugin("CompatNoCheatPlus") != null;
    }

    @Override
    public boolean isRunning() {
        return Bukkit.getPluginManager().isPluginEnabled("CompatNoCheatPlus");
    }

    @Override
    public void register() {
        if (this.isPresent() && this.isRunning()) {
            ASManager.getInstance().getLogger().info("Registering CompatNoCheatPlus Hook and enabling module...");
            EffectsHandler.getAntiCheatHooks().put("CompatNoCheatPlus", this);
        }
    }

    @Override
    public boolean exemptDebug(@NotNull Player player) {
        ASManager.getInstance().getLogger().info("Trying to exempt " + player.getName() + " from CompatNoCheatPlus..");
        return this.exempt(player);
    }

    @Override
    public boolean exempt(@NotNull Player player) {
        if (!this.isRunning()) {
            return false;
        }
        for (CheckType checkType : CheckType.values()) {
            this.man.addExemption(player, checkType);
        }
        return true;
    }

    @Override
    public void nonExempt(@NotNull Player player) {
        if (!this.isRunning()) {
            return;
        }
        for (CheckType checkType : CheckType.values()) {
            this.man.removeExemption(player, checkType);
        }
    }

    @Override
    public AntiCheatHook getHook() {
        return this;
    }

    public String getHookName() {
        return "Advanced Enchantments Hook";
    }

    public String getHookVersion() {
        return "1.0.1";
    }
}

