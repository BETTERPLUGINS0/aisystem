/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  net.advancedplugins.ae.utils.AManager
 *  org.bukkit.Location
 *  org.bukkit.Material
 *  org.bukkit.block.Block
 *  org.bukkit.entity.Entity
 *  org.bukkit.entity.FallingBlock
 *  org.bukkit.entity.LivingEntity
 *  org.bukkit.entity.Player
 *  org.bukkit.event.EventHandler
 *  org.bukkit.event.EventPriority
 *  org.bukkit.event.entity.EntityChangeBlockEvent
 *  org.bukkit.metadata.FixedMetadataValue
 *  org.bukkit.metadata.MetadataValue
 *  org.bukkit.plugin.Plugin
 *  org.bukkit.plugin.java.JavaPlugin
 */
package net.advancedplugins.as.impl.effects.effects.effects.internal;

import java.util.ArrayList;
import java.util.List;
import net.advancedplugins.ae.utils.AManager;
import net.advancedplugins.as.impl.effects.effects.EffectsHandler;
import net.advancedplugins.as.impl.effects.effects.actions.execution.ExecutionTask;
import net.advancedplugins.as.impl.effects.effects.effects.AdvancedEffect;
import net.advancedplugins.as.impl.utils.ASManager;
import net.advancedplugins.as.impl.utils.SchedulerUtils;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.FallingBlock;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.EntityChangeBlockEvent;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

public class SpawnBlocksEffect
extends AdvancedEffect {
    private final List<FallingBlock> blocks = new ArrayList<FallingBlock>();

    public SpawnBlocksEffect(JavaPlugin javaPlugin) {
        super(javaPlugin, "SPAWN_BLOCKS", "Spawn flood of falling blocks from above", "%e:<MATERIAL>:<DAMAGE>");
        this.addArgument(0, Material.class);
        this.addArgument(1, Double.class);
    }

    @Override
    public boolean executeEffect(ExecutionTask executionTask, LivingEntity livingEntity, String[] stringArray) {
        return this.executeEffect(executionTask, livingEntity.getLocation().add(0.0, 1.0, 0.0), stringArray);
    }

    @Override
    public boolean executeEffect(ExecutionTask executionTask, Location location, String[] stringArray) {
        if (stringArray.length < 2) {
            return false;
        }
        Material material = ASManager.getMaterial(stringArray[0]);
        if (material == null) {
            AManager.error((String[])new String[]{"Invalid material: " + stringArray[0]});
            return false;
        }
        int n = ASManager.parseInt(stringArray[1]);
        Block block = location.getBlock().getRelative(0, 5, 0);
        for (int i = -3; i <= 3; ++i) {
            for (int j = -3; j <= 3; ++j) {
                Block block2 = block.getRelative(i, 0, j);
                FallingBlock fallingBlock = block2.getWorld().spawnFallingBlock(block2.getLocation(), material.createBlockData());
                fallingBlock.setDropItem(false);
                fallingBlock.setMetadata("ae_damage", (MetadataValue)new FixedMetadataValue((Plugin)EffectsHandler.getInstance(), (Object)n));
                this.blocks.add(fallingBlock);
            }
        }
        return true;
    }

    @EventHandler(priority=EventPriority.LOW, ignoreCancelled=true)
    public void onFallingBlockLand(EntityChangeBlockEvent entityChangeBlockEvent) {
        FallingBlock fallingBlock;
        if (entityChangeBlockEvent.getEntity() instanceof FallingBlock && this.blocks.contains(fallingBlock = (FallingBlock)entityChangeBlockEvent.getEntity())) {
            this.blocks.remove(fallingBlock);
            Block block = entityChangeBlockEvent.getBlock();
            if (fallingBlock.hasMetadata("ae_damage")) {
                block.getWorld().getNearbyEntities(block.getLocation(), 1.0, 1.0, 1.0).stream().filter(entity -> entity instanceof Player).forEach(entity -> {
                    int n = ((MetadataValue)fallingBlock.getMetadata("ae_damage").get(0)).asInt();
                    ((Player)entity).damage((double)n, (Entity)fallingBlock);
                });
            }
            SchedulerUtils.runTaskLater(() -> block.setType(Material.AIR), 2L);
        }
    }
}

