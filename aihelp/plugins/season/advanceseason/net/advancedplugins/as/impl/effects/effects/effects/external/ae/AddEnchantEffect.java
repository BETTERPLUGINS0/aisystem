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
import net.advancedplugins.as.impl.utils.ASManager;
import net.advancedplugins.as.impl.utils.VanillaEnchants;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.LivingEntity;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;

public class AddEnchantEffect
extends AdvancedEffect {
    public AddEnchantEffect(JavaPlugin javaPlugin) {
        super(javaPlugin, "ADD_ENCHANT", "Add enchant to item", "%e:<ENCHANT>:<LEVEL>");
    }

    @Override
    public boolean executeEffect(ExecutionTask executionTask, LivingEntity livingEntity, String[] stringArray) {
        ItemStack itemStack = executionTask.getBuilder().getItem();
        String string = stringArray[0];
        int n = ASManager.parseInt(stringArray[1], 1);
        Enchantment enchantment = VanillaEnchants.displayNameToEnchant(string);
        if (enchantment != null) {
            ItemMeta itemMeta = itemStack.getItemMeta();
            itemMeta.addEnchant(enchantment, n, true);
            itemStack.setItemMeta(itemMeta);
        } else {
            if (!AEAPI.isAnEnchantment((String)string)) {
                return false;
            }
            itemStack = AEAPI.applyEnchant((String)string, (int)n, (ItemStack)itemStack);
        }
        executionTask.getBuilder().setItem(itemStack);
        return true;
    }
}

