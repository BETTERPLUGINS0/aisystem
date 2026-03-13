/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.Bukkit
 *  org.bukkit.Material
 *  org.bukkit.entity.LivingEntity
 *  org.bukkit.entity.Player
 *  org.bukkit.inventory.ItemStack
 *  org.bukkit.plugin.Plugin
 *  org.bukkit.plugin.java.JavaPlugin
 */
package net.advancedplugins.as.impl.effects.effects.effects.internal;

import java.util.ArrayList;
import java.util.Arrays;
import net.advancedplugins.as.impl.effects.effects.EffectsHandler;
import net.advancedplugins.as.impl.effects.effects.actions.execution.ExecutionTask;
import net.advancedplugins.as.impl.effects.effects.effects.AdvancedEffect;
import net.advancedplugins.as.impl.effects.effects.effects.utils.PumpkinDeathListener;
import net.advancedplugins.as.impl.utils.ASManager;
import net.advancedplugins.as.impl.utils.nbt.utils.MinecraftVersion;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

public class PumpkinEffect
extends AdvancedEffect {
    public PumpkinEffect(JavaPlugin javaPlugin) {
        super(javaPlugin, "PUMPKIN", "Put a pumpkin on entity's head", "%e:<TICKS>");
    }

    @Override
    public boolean executeEffect(ExecutionTask executionTask, LivingEntity livingEntity, String[] stringArray) {
        String string;
        ItemStack itemStack = livingEntity.getEquipment().getHelmet() == null ? null : livingEntity.getEquipment().getHelmet().clone();
        String string2 = string = MinecraftVersion.isNew() ? "CARVED_PUMPKIN" : "PUMPKIN";
        if (ASManager.isValid(itemStack) && itemStack.getType().name().equalsIgnoreCase(string)) {
            return true;
        }
        ItemStack itemStack2 = new ItemStack(ASManager.matchMaterial(string, 1, 0));
        livingEntity.getEquipment().setHelmet(MinecraftVersion.isNew() ? itemStack2 : new ItemStack(Material.PUMPKIN, 1, 4));
        PumpkinDeathListener pumpkinDeathListener = new PumpkinDeathListener(itemStack, itemStack2.getType(), ASManager.parseInt(stringArray[0]), livingEntity);
        Bukkit.getScheduler().runTaskLater((Plugin)EffectsHandler.getInstance(), () -> {
            if (!pumpkinDeathListener.playerDied) {
                if (livingEntity instanceof Player) {
                    Player player = (Player)livingEntity;
                    ArrayList<ItemStack> arrayList = new ArrayList<ItemStack>();
                    arrayList.addAll(Arrays.asList(player.getInventory().getArmorContents()));
                    arrayList.addAll(Arrays.asList(player.getInventory().getContents()));
                    for (ItemStack itemStack3 : arrayList) {
                        if (itemStack3 == null || itemStack3.getType() != itemStack2.getType()) continue;
                        itemStack3.setAmount(itemStack3.getAmount() - 1);
                        break;
                    }
                }
                livingEntity.getEquipment().setHelmet(itemStack);
            }
        }, (long)ASManager.parseInt(stringArray[0]));
        return true;
    }
}

