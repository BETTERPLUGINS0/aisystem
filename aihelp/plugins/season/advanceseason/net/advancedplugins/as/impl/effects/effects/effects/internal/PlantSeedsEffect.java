/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.Material
 *  org.bukkit.block.Block
 *  org.bukkit.block.BlockFace
 *  org.bukkit.entity.LivingEntity
 *  org.bukkit.entity.Player
 *  org.bukkit.event.Event
 *  org.bukkit.event.block.BlockBreakEvent
 *  org.bukkit.event.player.PlayerInteractEvent
 *  org.bukkit.inventory.Inventory
 *  org.bukkit.plugin.java.JavaPlugin
 */
package net.advancedplugins.as.impl.effects.effects.effects.internal;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;
import net.advancedplugins.as.impl.effects.effects.EffectsHandler;
import net.advancedplugins.as.impl.effects.effects.actions.execution.ExecutionTask;
import net.advancedplugins.as.impl.effects.effects.effects.AdvancedEffect;
import net.advancedplugins.as.impl.effects.effects.effects.utils.PlantSeedsType;
import net.advancedplugins.as.impl.utils.ASManager;
import net.advancedplugins.as.impl.utils.ReallyFastBlockHandler;
import net.advancedplugins.as.impl.utils.SchedulerUtils;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.plugin.java.JavaPlugin;

public class PlantSeedsEffect
extends AdvancedEffect {
    public PlantSeedsEffect(JavaPlugin javaPlugin) {
        super(javaPlugin, "PLANT_SEEDS", "Plant seeds in radius", "%e:<RADIUS>:[SEEDS]");
        this.addArgument(0, Integer.class);
        this.addArgument(1, Material.class);
    }

    @Override
    public boolean executeEffect(ExecutionTask executionTask, LivingEntity livingEntity, String[] stringArray) {
        SchedulerUtils.runTaskLater(() -> {
            int n;
            PlantSeedsType plantSeedsType;
            Player player = (Player)livingEntity;
            Event event = executionTask.getBuilder().getEvent();
            PlantSeedsType plantSeedsType2 = plantSeedsType = stringArray.length > 1 ? PlantSeedsType.matchType(stringArray[1].replace("WHEAT_", "")) : PlantSeedsType.SEEDS;
            if (plantSeedsType == null) {
                return;
            }
            Block block2 = null;
            if (event instanceof BlockBreakEvent) {
                block2 = ((BlockBreakEvent)event).getBlock();
            } else if (event instanceof PlayerInteractEvent) {
                block2 = ((PlayerInteractEvent)event).getClickedBlock();
            }
            if (block2 == null) {
                block2 = player.getLocation().getBlock();
            }
            int n2 = ASManager.parseInt(stringArray[0]);
            int n3 = ASManager.getAmount(player, plantSeedsType.getSeedsMaterial());
            Set<Object> set = new HashSet();
            int n4 = 1;
            for (Block block3 : ASManager.getBlocksFlat(block2, n2)) {
                Block block4;
                if (n4 > n3) break;
                if (block3.getType() == plantSeedsType.getBlockRequired()) {
                    block4 = block3.getRelative(BlockFace.UP);
                } else {
                    if (block3.getRelative(BlockFace.DOWN).getType() != plantSeedsType.getBlockRequired()) continue;
                    block4 = block3;
                }
                if (!ASManager.isAir(block4)) continue;
                set.add(block4);
                ++n4;
            }
            if ((n = (set = set.stream().filter(block -> EffectsHandler.getProtection().canBreak(block.getLocation(), player)).collect(Collectors.toSet())).size()) == 0 || !ASManager.hasAmount(player, plantSeedsType.getSeedsMaterial(), n)) {
                return;
            }
            ASManager.removeItems((Inventory)player.getInventory(), plantSeedsType.getSeedsMaterial(), n);
            ReallyFastBlockHandler.getForWorld(player.getWorld()).setType(plantSeedsType.getNewBlockType(), set.toArray(new Block[0]));
        });
        return true;
    }
}

