/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.configuration.ConfigurationSection
 *  org.bukkit.inventory.Inventory
 *  org.bukkit.inventory.ItemStack
 */
package net.advancedplugins.as.impl.utils.menus.item;

import java.util.Arrays;
import net.advancedplugins.as.impl.utils.ASManager;
import net.advancedplugins.as.impl.utils.VanillaEnchants;
import net.advancedplugins.as.impl.utils.items.ConfigItemCreator;
import net.advancedplugins.as.impl.utils.items.ItemBuilder;
import net.advancedplugins.as.impl.utils.items.ItemFlagFix;
import net.advancedplugins.as.impl.utils.text.Replace;
import net.advancedplugins.as.impl.utils.text.Replacer;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class AdvancedMenuItem {
    private ConfigurationSection section;
    private Replace replace;
    private int[] slots;
    private String action = null;
    private ItemStack item;
    private boolean glow = false;
    private int amount = 0;
    private String data;

    public AdvancedMenuItem(String string, ConfigurationSection configurationSection, Replace replace) {
        this.slots = ASManager.getSlots(string);
        this.replace = replace;
        this.section = configurationSection;
        if (configurationSection.contains("action")) {
            this.action = configurationSection.getString("action");
        }
    }

    public AdvancedMenuItem(int n, ConfigurationSection configurationSection, Replace replace) {
        this.slots = new int[]{n};
        this.replace = replace;
        this.section = configurationSection;
        if (configurationSection.contains("action")) {
            this.action = configurationSection.getString("action");
        }
    }

    public AdvancedMenuItem(int[] nArray, ConfigurationSection configurationSection, Replace replace) {
        this.slots = nArray;
        this.replace = replace;
        this.section = configurationSection;
        if (configurationSection.contains("action")) {
            this.action = configurationSection.getString("action");
        }
    }

    public AdvancedMenuItem(ItemStack itemStack) {
        this.item = itemStack;
    }

    public void addToInventory(Inventory inventory) {
        ItemStack itemStack = this.getItem();
        boolean bl = this.slots.length > 1;
        for (int n : this.slots) {
            if (!bl) {
                inventory.setItem(n, itemStack);
                continue;
            }
            if (!ASManager.isAir(inventory.getItem(n))) continue;
            inventory.setItem(n, itemStack);
        }
    }

    public AdvancedMenuItem setGlow() {
        this.glow = true;
        return this;
    }

    public AdvancedMenuItem setAmount(int n) {
        this.amount = n;
        return this;
    }

    public ItemStack getItem() {
        if (this.item != null) {
            return this.item;
        }
        ItemStack itemStack = ConfigItemCreator.fromConfigSection(this.section, "", this.replace == null ? null : ((Replacer)this.replace.apply(new Replacer())).getPlaceholders(), null);
        if (this.glow) {
            ItemBuilder itemBuilder = new ItemBuilder(itemStack);
            itemBuilder.addUnsafeEnchantment(VanillaEnchants.displayNameToEnchant("FORTUNE"), 1);
            itemBuilder.addItemFlag(ItemFlagFix.hideAllAttributes());
            itemStack = itemBuilder.toItemStack();
        }
        if (this.amount != 0) {
            itemStack.setAmount(this.amount);
        }
        return itemStack;
    }

    public AdvancedMenuItem setData(String string) {
        this.data = string;
        return this;
    }

    public String getSlots() {
        return Arrays.toString(this.slots);
    }

    public AdvancedMenuItem setSlots(int ... nArray) {
        this.slots = nArray;
        return this;
    }

    public String getAction() {
        return this.action;
    }

    public String getData() {
        return this.data;
    }
}

