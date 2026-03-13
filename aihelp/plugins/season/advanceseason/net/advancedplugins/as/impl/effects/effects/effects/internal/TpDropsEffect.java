/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.Location
 *  org.bukkit.block.Block
 *  org.bukkit.enchantments.Enchantment
 *  org.bukkit.entity.LivingEntity
 *  org.bukkit.inventory.ItemStack
 *  org.bukkit.plugin.java.JavaPlugin
 */
package net.advancedplugins.as.impl.effects.effects.effects.internal;

import com.google.common.collect.ImmutableList;
import net.advancedplugins.as.impl.effects.effects.actions.execution.ExecutionTask;
import net.advancedplugins.as.impl.effects.effects.effects.AdvancedEffect;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.LivingEntity;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

public class TpDropsEffect
extends AdvancedEffect {
    private final ImmutableList<String> bannedMaterials = ((ImmutableList.Builder)((ImmutableList.Builder)((ImmutableList.Builder)((ImmutableList.Builder)ImmutableList.builder().add("MOB_SPAWNER")).add("CAKE")).add("GRASS")).add("DEAD_BUSH")).build();

    public TpDropsEffect(JavaPlugin javaPlugin) {
        super(javaPlugin, "TP_DROPS", "Teleport drops to inventory", "%e");
        this.setBlockEffect(true);
    }

    @Override
    public boolean executeEffect(ExecutionTask executionTask, LivingEntity livingEntity, String[] stringArray) {
        return super.executeEffect(executionTask, executionTask.getBuilder().getBlock().getLocation(), stringArray);
    }

    @Override
    public boolean executeEffect(ExecutionTask executionTask, Location location, String[] stringArray) {
        ItemStack itemStack = executionTask.getBuilder().getItem();
        Block block = location.getBlock();
        if (itemStack.getEnchantments().containsKey(Enchantment.SILK_TOUCH) && block.getType().name().equals("REINFORCED_DEEPSLATE")) {
            return true;
        }
        if (this.bannedMaterials.stream().anyMatch(string -> block.getType().name().equalsIgnoreCase((String)string))) {
            return true;
        }
        executionTask.getBuilder().getDrops().getSettings().setAddToInventory(true);
        executionTask.getBuilder().getDrops().getSettings().setBreakBlocks(true);
        return true;
    }
}

