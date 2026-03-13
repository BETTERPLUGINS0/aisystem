/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  com.bgsoftware.superiorskyblock.api.SuperiorSkyblockAPI
 *  org.bukkit.block.Block
 */
package net.advancedplugins.as.impl.utils.hooks.plugins;

import com.bgsoftware.superiorskyblock.api.SuperiorSkyblockAPI;
import net.advancedplugins.as.impl.utils.hooks.HookPlugin;
import net.advancedplugins.as.impl.utils.hooks.PluginHookInstance;
import org.bukkit.block.Block;

public class SuperiorSkyblock2Hook
extends PluginHookInstance {
    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public String getName() {
        return HookPlugin.SUPERIORSKYBLOCK2.getPluginName();
    }

    public int getStackedAmount(Block block) {
        return SuperiorSkyblockAPI.getStackedBlocks().getStackedBlockAmount(block);
    }

    public boolean isStackedBlock(Block block) {
        return this.getStackedAmount(block) > 1;
    }
}

