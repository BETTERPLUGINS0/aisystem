/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  net.advancedplugins.ae.api.AEAPI
 *  net.advancedplugins.ae.enchanthandler.enchantments.AdvancedEnchantment
 *  org.bukkit.inventory.ItemStack
 */
package net.advancedplugins.as.impl.utils.hooks.plugins;

import java.util.HashMap;
import java.util.List;
import net.advancedplugins.ae.api.AEAPI;
import net.advancedplugins.ae.enchanthandler.enchantments.AdvancedEnchantment;
import net.advancedplugins.as.impl.utils.hooks.PluginHookInstance;
import org.bukkit.inventory.ItemStack;

public class AdvancedEnchantmentsHook
extends PluginHookInstance {
    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public String getName() {
        return "AdvancedEnchantments";
    }

    public HashMap<String, Integer> getEnchantmentsOnItem(ItemStack itemStack) {
        return AEAPI.getEnchantmentsOnItem((ItemStack)itemStack);
    }

    public List<String> getAllEnchantments() {
        return AEAPI.getAllEnchantments();
    }

    public AdvancedEnchantment getInstance(String string) {
        return AEAPI.getEnchantmentInstance((String)string);
    }
}

