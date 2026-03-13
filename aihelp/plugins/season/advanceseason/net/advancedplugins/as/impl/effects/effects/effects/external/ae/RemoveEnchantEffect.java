/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  net.advancedplugins.ae.api.AEAPI
 *  org.bukkit.enchantments.Enchantment
 *  org.bukkit.entity.LivingEntity
 *  org.bukkit.inventory.ItemStack
 *  org.bukkit.inventory.meta.ItemMeta
 *  org.bukkit.plugin.java.JavaPlugin
 */
package net.advancedplugins.as.impl.effects.effects.effects.external.ae;

import net.advancedplugins.ae.api.AEAPI;
import net.advancedplugins.as.impl.effects.effects.actions.execution.ExecutionTask;
import net.advancedplugins.as.impl.effects.effects.effects.AdvancedEffect;
import net.advancedplugins.as.impl.utils.VanillaEnchants;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.LivingEntity;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;

public class RemoveEnchantEffect
extends AdvancedEffect {
    public RemoveEnchantEffect(JavaPlugin javaPlugin) {
        super(javaPlugin, "REMOVE_ENCHANT", "Remove enchant from item", "%e:<ENCHANT>");
    }

    @Override
    public boolean executeEffect(ExecutionTask executionTask, LivingEntity livingEntity, String[] stringArray) {
        ItemStack itemStack = executionTask.getBuilder().getItem();
        String string = stringArray[0];
        Enchantment enchantment = VanillaEnchants.displayNameToEnchant(string, false);
        if (enchantment != null) {
            ItemMeta itemMeta = itemStack.getItemMeta();
            itemMeta.removeEnchant(enchantment);
            itemStack.setItemMeta(itemMeta);
        } else {
            if (!AEAPI.isAnEnchantment((String)string)) {
                return false;
            }
            itemStack = AEAPI.removeEnchantment((ItemStack)itemStack, (String)string);
        }
        RemoveEnchantEffect.updateItem(executionTask.getBuilder(), livingEntity, itemStack, executionTask.getBuilder().getItemType());
        return true;
    }
}

