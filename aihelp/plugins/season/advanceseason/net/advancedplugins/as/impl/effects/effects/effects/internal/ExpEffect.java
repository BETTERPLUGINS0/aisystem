/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.Location
 *  org.bukkit.enchantments.Enchantment
 *  org.bukkit.entity.ExperienceOrb
 *  org.bukkit.entity.LivingEntity
 *  org.bukkit.event.block.BlockBreakEvent
 *  org.bukkit.plugin.java.JavaPlugin
 */
package net.advancedplugins.as.impl.effects.effects.effects.internal;

import net.advancedplugins.as.impl.effects.effects.actions.execution.ExecutionTask;
import net.advancedplugins.as.impl.effects.effects.effects.AdvancedEffect;
import net.advancedplugins.as.impl.effects.effects.settings.SettingValues;
import net.advancedplugins.as.impl.utils.abilities.DropsSettings;
import net.advancedplugins.as.impl.utils.hooks.HookPlugin;
import net.advancedplugins.as.impl.utils.hooks.HooksHandler;
import net.advancedplugins.as.impl.utils.hooks.plugins.ItemsAdderHook;
import org.bukkit.Location;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.ExperienceOrb;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class ExpEffect
extends AdvancedEffect {
    public ExpEffect(JavaPlugin javaPlugin) {
        super(javaPlugin, "EXP", "Drop Experience Orbs", "%e:<AMOUNT>");
        this.addArgument(0, Integer.class);
    }

    @Override
    public boolean executeEffect(ExecutionTask executionTask, LivingEntity livingEntity, String[] stringArray) {
        return this.executeEffect(executionTask, livingEntity.getLocation(), stringArray);
    }

    @Override
    public boolean executeEffect(ExecutionTask executionTask, Location location, String[] stringArray) {
        if (HooksHandler.isEnabled(HookPlugin.ITEMSADDER) && ((ItemsAdderHook)HooksHandler.getHook(HookPlugin.ITEMSADDER)).isCustomBlock(executionTask.getBuilder().getBlock())) {
            return true;
        }
        int n = (int)Double.parseDouble(stringArray[0]);
        if (executionTask.getBuilder().getItem() != null && executionTask.getBuilder().getItem().containsEnchantment(Enchantment.SILK_TOUCH) && !SettingValues.isExpEffectIgnoreSilktouch()) {
            return true;
        }
        if (executionTask.getBuilder().getEvent() instanceof BlockBreakEvent) {
            DropsSettings dropsSettings = executionTask.getBuilder().getDrops().getSettings();
            dropsSettings.setDropExpAmount(dropsSettings.getDropExpAmount() + n);
        } else {
            ExperienceOrb experienceOrb = (ExperienceOrb)location.getWorld().spawn(location, ExperienceOrb.class);
            experienceOrb.setExperience(n);
        }
        return true;
    }
}

