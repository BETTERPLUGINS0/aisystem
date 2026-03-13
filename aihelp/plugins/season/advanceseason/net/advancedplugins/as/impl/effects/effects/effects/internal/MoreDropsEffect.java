/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.Location
 *  org.bukkit.block.Block
 *  org.bukkit.entity.LivingEntity
 *  org.bukkit.event.entity.EntityDeathEvent
 *  org.bukkit.event.player.PlayerFishEvent
 *  org.bukkit.inventory.EntityEquipment
 *  org.bukkit.inventory.EquipmentSlot
 *  org.bukkit.inventory.ItemStack
 *  org.bukkit.plugin.Plugin
 *  org.bukkit.plugin.java.JavaPlugin
 */
package net.advancedplugins.as.impl.effects.effects.effects.internal;

import java.util.ArrayList;
import net.advancedplugins.as.impl.effects.armorutils.ArmorType;
import net.advancedplugins.as.impl.effects.effects.actions.execution.ExecutionTask;
import net.advancedplugins.as.impl.effects.effects.actions.handlers.DropsHandler;
import net.advancedplugins.as.impl.effects.effects.effects.AdvancedEffect;
import net.advancedplugins.as.impl.utils.ASManager;
import net.advancedplugins.as.impl.utils.abilities.DropsSettings;
import net.advancedplugins.as.impl.utils.hooks.HookPlugin;
import net.advancedplugins.as.impl.utils.hooks.HooksHandler;
import net.advancedplugins.as.impl.utils.hooks.plugins.MythicMobsHook;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.player.PlayerFishEvent;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

public class MoreDropsEffect
extends AdvancedEffect {
    public MoreDropsEffect(JavaPlugin javaPlugin) {
        super(javaPlugin, "MORE_DROPS", "Multiply drops", "%e:<AMOUNT>");
        this.addArgument(0, Integer.class);
    }

    @Override
    public boolean executeEffect(ExecutionTask executionTask, LivingEntity livingEntity, String[] stringArray) {
        if (executionTask.getBuilder().getEvent() instanceof EntityDeathEvent) {
            EntityDeathEvent entityDeathEvent = (EntityDeathEvent)executionTask.getBuilder().getEvent();
            if (HooksHandler.isEnabled(HookPlugin.MYTHICMOBS) && ((MythicMobsHook)HooksHandler.getHook(HookPlugin.MYTHICMOBS)).isMythicMob(entityDeathEvent.getEntity())) {
                return true;
            }
            ArrayList arrayList = new ArrayList(entityDeathEvent.getDrops());
            ArrayList<ItemStack> arrayList2 = new ArrayList<ItemStack>(entityDeathEvent.getDrops());
            EntityEquipment entityEquipment = entityDeathEvent.getEntity().getEquipment();
            if (entityEquipment != null) {
                for (EquipmentSlot equipmentSlot : EquipmentSlot.values()) {
                    ItemStack itemStack = entityEquipment.getItem(equipmentSlot);
                    arrayList.remove(itemStack);
                }
            }
            for (ItemStack itemStack : arrayList) {
                if (ArmorType.matchType(itemStack) != null || ASManager.isTool(itemStack.getType())) continue;
                for (int i = 0; i < ASManager.parseInt(stringArray[0]); ++i) {
                    arrayList2.add(itemStack);
                }
            }
            entityDeathEvent.getDrops().clear();
            ItemStack[] itemStackArray = arrayList2.toArray(new ItemStack[0]);
            ASManager.dropItem(entityDeathEvent.getEntity().getLocation(), itemStackArray);
            return true;
        }
        DropsSettings dropsSettings = executionTask.getBuilder().getDrops().getSettings();
        if (executionTask.getBuilder().getEvent() instanceof PlayerFishEvent) {
            dropsSettings.setDropsMultiplier(ASManager.parseInt(stringArray[0]));
            return true;
        }
        Block block = executionTask.getBuilder().getBlock();
        if (block == null) {
            return true;
        }
        return this.handleBlockDrops(executionTask, block.getLocation(), stringArray);
    }

    @Override
    public boolean executeEffect(ExecutionTask executionTask, Location location, String[] stringArray) {
        return this.handleBlockDrops(executionTask, location, stringArray);
    }

    private boolean handleBlockDrops(ExecutionTask executionTask, Location location, String[] stringArray) {
        Block block = location.getBlock();
        String string = block.getType().name();
        if (executionTask.getAbility().getWhitelist() != null && !executionTask.getAbility().getWhitelist().isEmpty() && !ASManager.contains(string, executionTask.getAbility().getWhitelist())) {
            return true;
        }
        if (executionTask.getAbility().getBlacklist() != null && !executionTask.getAbility().getBlacklist().isEmpty() && ASManager.contains(string, executionTask.getAbility().getBlacklist())) {
            return true;
        }
        DropsHandler dropsHandler = executionTask.getBuilder().getDrops();
        DropsSettings dropsSettings = dropsHandler.getSettings();
        try {
            if (!block.hasMetadata("non-natural")) {
                dropsSettings.setDropsMultiplier(ASManager.parseInt(stringArray[0]));
            } else {
                block.removeMetadata("non-natural", (Plugin)this.getPlugin());
            }
        } catch (Exception exception) {
            // empty catch block
        }
        dropsHandler.getSettings().setBreakBlocks(true);
        return true;
    }
}

