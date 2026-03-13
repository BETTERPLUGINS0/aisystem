/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  io.github.thebusybiscuit.slimefun4.utils.SlimefunUtils
 *  me.mrCookieSlime.Slimefun.api.BlockStorage
 *  org.bukkit.Location
 *  org.bukkit.World
 *  org.bukkit.block.Block
 *  org.bukkit.entity.Player
 *  org.bukkit.inventory.ItemStack
 */
package net.advancedplugins.as.impl.utils.hooks.plugins;

import io.github.thebusybiscuit.slimefun4.utils.SlimefunUtils;
import java.util.Collection;
import me.mrCookieSlime.Slimefun.api.BlockStorage;
import net.advancedplugins.as.impl.utils.hooks.HookPlugin;
import net.advancedplugins.as.impl.utils.hooks.PluginHookInstance;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class SlimeFunHook
extends PluginHookInstance {
    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public String getName() {
        return HookPlugin.SLIMEFUN.getPluginName();
    }

    public boolean canBuild(Player player, Location location) {
        return BlockStorage.check((Block)location.getBlock()) == null;
    }

    public boolean isSlimefunItem(Location location) {
        return BlockStorage.check((Location)location) != null;
    }

    public boolean isSlimefunItem(Block block) {
        return BlockStorage.check((Block)block) != null;
    }

    public boolean hasSoulbound(ItemStack itemStack, World world) {
        return SlimefunUtils.isSoulbound((ItemStack)itemStack, (World)world);
    }

    public Collection<ItemStack> getDrops(Location location) {
        return BlockStorage.check((Location)location).getDrops();
    }
}

