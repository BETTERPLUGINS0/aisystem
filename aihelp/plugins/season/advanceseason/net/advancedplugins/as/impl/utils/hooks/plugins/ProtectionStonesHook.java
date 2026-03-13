/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  dev.espi.protectionstones.ProtectionStones
 *  org.bukkit.block.Block
 *  org.jetbrains.annotations.NotNull
 */
package net.advancedplugins.as.impl.utils.hooks.plugins;

import dev.espi.protectionstones.ProtectionStones;
import net.advancedplugins.as.impl.utils.hooks.HookPlugin;
import net.advancedplugins.as.impl.utils.hooks.PluginHookInstance;
import org.bukkit.block.Block;
import org.jetbrains.annotations.NotNull;

public class ProtectionStonesHook
extends PluginHookInstance {
    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public String getName() {
        return HookPlugin.PROTECTIONSTONES.getPluginName();
    }

    public boolean isProtectionStone(@NotNull Block block) {
        return ProtectionStones.isProtectBlock((Block)block);
    }
}

