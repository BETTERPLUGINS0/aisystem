/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.Location
 *  org.bukkit.block.Block
 *  org.bukkit.entity.LivingEntity
 *  org.bukkit.plugin.java.JavaPlugin
 */
package net.advancedplugins.as.impl.effects.effects.effects.internal;

import net.advancedplugins.as.impl.effects.effects.actions.execution.ExecutionTask;
import net.advancedplugins.as.impl.effects.effects.effects.AdvancedEffect;
import net.advancedplugins.as.impl.effects.effects.settings.SettingValues;
import net.advancedplugins.as.impl.utils.ASManager;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.LivingEntity;
import org.bukkit.plugin.java.JavaPlugin;

public class BreakBlockEffect
extends AdvancedEffect {
    public BreakBlockEffect(JavaPlugin javaPlugin) {
        super(javaPlugin, "BREAK_BLOCK", "Break block", "%e");
        this.setBlockEffect(true);
        this.setExemptFromAC(true);
    }

    @Override
    public boolean executeEffect(ExecutionTask executionTask, LivingEntity livingEntity, String[] stringArray) {
        return true;
    }

    @Override
    public boolean executeEffect(ExecutionTask executionTask, Location location, String[] stringArray) {
        Block block = location.getBlock();
        String string = block.getType().name();
        if (ASManager.contains(string, SettingValues.getBannedMaterials())) {
            return true;
        }
        if (executionTask.getAbility().getWhitelist() != null && !executionTask.getAbility().getWhitelist().isEmpty() && !ASManager.contains(string, executionTask.getAbility().getWhitelist())) {
            return true;
        }
        if (executionTask.getAbility().getBlacklist() != null && !executionTask.getAbility().getBlacklist().isEmpty() && ASManager.contains(string, executionTask.getAbility().getBlacklist())) {
            return true;
        }
        executionTask.getBuilder().getDrops().getSettings().setTool(executionTask.getBuilder().getItem());
        if (executionTask.getBuilder().getDrops().getAllBlocks().contains(location.getBlock())) {
            return true;
        }
        executionTask.getBuilder().getDrops().getSettings().setBreakBlocks(true);
        executionTask.getBuilder().getDrops().addDrops(location.getBlock(), location.getBlock().getDrops(executionTask.getBuilder().getItem()));
        return true;
    }
}

