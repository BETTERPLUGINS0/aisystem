/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.Location
 *  org.bukkit.Material
 *  org.bukkit.block.Block
 *  org.bukkit.entity.Player
 *  org.bukkit.event.block.BlockBreakEvent
 *  org.bukkit.event.player.PlayerInteractEvent
 *  org.bukkit.plugin.java.JavaPlugin
 */
package net.advancedplugins.as.impl.effects.effects.effects.internal;

import net.advancedplugins.as.impl.effects.effects.EffectsHandler;
import net.advancedplugins.as.impl.effects.effects.actions.execution.ExecutionTask;
import net.advancedplugins.as.impl.effects.effects.effects.AdvancedEffect;
import net.advancedplugins.as.impl.utils.ASManager;
import net.advancedplugins.as.impl.utils.ReallyFastBlockHandler;
import net.advancedplugins.as.impl.utils.SchedulerUtils;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class SetBlockEffect
extends AdvancedEffect {
    public SetBlockEffect(JavaPlugin javaPlugin) {
        super(javaPlugin, "SET_BLOCK", "Set block to a type", "%e:<MATERIAL>");
        this.setBlockEffect(true);
    }

    @Override
    public boolean executeEffect(ExecutionTask executionTask, Location location, String[] stringArray) {
        Material material;
        Location location2;
        if (executionTask.getBuilder().getEvent() instanceof BlockBreakEvent) {
            location2 = location;
        } else if (executionTask.getBuilder().getEvent() instanceof PlayerInteractEvent) {
            material = (PlayerInteractEvent)executionTask.getBuilder().getEvent();
            if (material.getClickedBlock() == null) {
                return true;
            }
            location2 = stringArray.length == 4 ? new Location(material.getClickedBlock().getWorld(), (double)ASManager.parseInt(stringArray[1]), (double)ASManager.parseInt(stringArray[2]), (double)ASManager.parseInt(stringArray[3])) : material.getClickedBlock().getLocation();
        } else {
            this.warn("SET_BLOCK does not support this trigger.");
            return true;
        }
        material = Material.matchMaterial((String)stringArray[0]);
        if (material == null || !material.isBlock()) {
            return false;
        }
        if (!EffectsHandler.getProtection().canBreak(location2, (Player)executionTask.getBuilder().getMain())) {
            return true;
        }
        Material material2 = location2.getBlock().getType();
        SchedulerUtils.runTaskLater(() -> {
            Block block = location2.getBlock();
            if (block.getType() != material2 && !ASManager.isAir(block.getType())) {
                return;
            }
            ReallyFastBlockHandler reallyFastBlockHandler = ReallyFastBlockHandler.getForWorld(location2.getWorld());
            reallyFastBlockHandler.setType(material, block);
        }, 2L);
        return true;
    }
}

