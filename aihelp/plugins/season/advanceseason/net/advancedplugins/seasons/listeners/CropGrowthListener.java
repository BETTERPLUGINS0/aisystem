/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.Material
 *  org.bukkit.block.Block
 *  org.bukkit.block.BlockFace
 *  org.bukkit.block.data.Ageable
 *  org.bukkit.block.data.Bisected$Half
 *  org.bukkit.block.data.BlockData
 *  org.bukkit.block.data.type.PitcherCrop
 *  org.bukkit.event.EventHandler
 *  org.bukkit.event.Listener
 *  org.bukkit.event.block.BlockGrowEvent
 *  org.bukkit.plugin.Plugin
 *  org.bukkit.scheduler.BukkitRunnable
 */
package net.advancedplugins.seasons.listeners;

import net.advancedplugins.seasons.Core;
import net.advancedplugins.seasons.handlers.CropsHandler;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.Ageable;
import org.bukkit.block.data.Bisected;
import org.bukkit.block.data.BlockData;
import org.bukkit.block.data.type.PitcherCrop;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockGrowEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

public class CropGrowthListener
implements Listener {
    private final CropsHandler cropsHandler;

    @EventHandler
    public void onGrow(BlockGrowEvent blockGrowEvent) {
        Block block = blockGrowEvent.getBlock();
        if (!(block.getBlockData() instanceof Ageable)) {
            return;
        }
        Ageable ageable = (Ageable)block.getBlockData();
        if (!Core.getWorldHandler().isWorldEnabled(block.getWorld().getName())) {
            return;
        }
        if (ageable.getLightEmission() < 0) {
            return;
        }
        int n = ageable.getAge();
        int n2 = ageable.getMaximumAge();
        final int n3 = this.cropsHandler.getBlockGrowth(block, n, n2);
        final Block block2 = block.getRelative(BlockFace.UP);
        if (!this.canGrowUp(block, n3)) {
            return;
        }
        ageable.setAge(n3);
        blockGrowEvent.getNewState().setBlockData((BlockData)ageable);
        if (ageable instanceof PitcherCrop && n3 >= 3) {
            this.setTop(block2, n3);
            new BukkitRunnable(){

                public void run() {
                    CropGrowthListener.this.setTop(block2, n3);
                }
            }.runTaskLater((Plugin)Core.getInstance(), 1L);
        }
    }

    private void setTop(Block block, int n) {
        PitcherCrop pitcherCrop = (PitcherCrop)Material.PITCHER_CROP.createBlockData();
        pitcherCrop.setHalf(Bisected.Half.TOP);
        pitcherCrop.setAge(n);
        block.setBlockData((BlockData)pitcherCrop);
    }

    private boolean canGrowUp(Block block, int n) {
        BlockData blockData = block.getBlockData();
        Block block2 = block.getRelative(BlockFace.UP);
        if (n >= 3 && blockData instanceof PitcherCrop) {
            Material material = block2.getType();
            return material.isAir() || material == Material.PITCHER_CROP;
        }
        return true;
    }

    public CropGrowthListener(CropsHandler cropsHandler) {
        this.cropsHandler = cropsHandler;
    }
}

