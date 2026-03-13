/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.Location
 *  org.bukkit.block.Block
 *  org.bukkit.entity.LivingEntity
 *  org.bukkit.inventory.ItemStack
 *  org.bukkit.plugin.java.JavaPlugin
 */
package net.advancedplugins.as.impl.effects.effects.effects.internal;

import java.util.Iterator;
import net.advancedplugins.as.impl.effects.effects.actions.execution.ExecutionTask;
import net.advancedplugins.as.impl.effects.effects.effects.AdvancedEffect;
import net.advancedplugins.as.impl.effects.effects.settings.SettingValues;
import net.advancedplugins.as.impl.utils.ASManager;
import net.advancedplugins.as.impl.utils.BreakWholeTree;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.LivingEntity;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

public class BreakTreeEffect
extends AdvancedEffect {
    public BreakTreeEffect(JavaPlugin javaPlugin) {
        super(javaPlugin, "BREAK_TREE", "Break a whole tree at once", "%e:<MAX_LOGS>:<MAX_LEAVES>");
        this.addArgument(0, Integer.class);
        this.addArgument(1, Integer.class);
        this.setBlockEffect(true);
        this.setExemptFromAC(true);
    }

    @Override
    public boolean executeEffect(ExecutionTask executionTask, LivingEntity livingEntity, String[] stringArray) {
        return this.executeEffect(executionTask, executionTask.getBuilder().getBlock().getLocation(), stringArray);
    }

    @Override
    public boolean executeEffect(ExecutionTask executionTask, Location location, String[] stringArray) {
        Block block = location.getBlock();
        if (!ASManager.isLog(block.getType())) {
            return true;
        }
        int n = stringArray.length > 0 ? ASManager.parseInt(stringArray[0], -1) : -1;
        int n2 = stringArray.length > 1 ? ASManager.parseInt(stringArray[1], -1) : -1;
        Iterator<Block> iterator = new BreakWholeTree(block, n, n2).get().iterator();
        while (iterator.hasNext()) {
            Block block2 = iterator.next();
            if (block2.equals((Object)block)) continue;
            if (block2.hasMetadata("AE_Placed")) {
                iterator.remove();
                continue;
            }
            if (SettingValues.isBreakTreeRespectPlayerPlacedBlocks() && block2.hasMetadata("non-natural")) {
                iterator.remove();
                continue;
            }
            executionTask.getBuilder().getDrops().addDrops(block2, new ItemStack(block2.getType()));
        }
        executionTask.getBuilder().getDrops().getSettings().setBreakBlocks(true);
        return true;
    }
}

