/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.Bukkit
 *  org.bukkit.Material
 *  org.bukkit.entity.LivingEntity
 *  org.bukkit.entity.Player
 *  org.bukkit.event.Event
 *  org.bukkit.event.player.PlayerItemBreakEvent
 *  org.bukkit.event.player.PlayerItemDamageEvent
 *  org.bukkit.inventory.ItemStack
 *  org.bukkit.inventory.meta.Damageable
 *  org.bukkit.inventory.meta.ItemMeta
 *  org.jetbrains.annotations.Nullable
 */
package net.advancedplugins.as.impl.utils;

import net.advancedplugins.as.impl.utils.ASManager;
import net.advancedplugins.as.impl.utils.VanillaEnchants;
import net.advancedplugins.as.impl.utils.hooks.HookPlugin;
import net.advancedplugins.as.impl.utils.hooks.HooksHandler;
import net.advancedplugins.as.impl.utils.hooks.plugins.ItemsAdderHook;
import net.advancedplugins.as.impl.utils.nbt.utils.MinecraftVersion;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.player.PlayerItemBreakEvent;
import org.bukkit.event.player.PlayerItemDamageEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.Nullable;

public class ItemDurability {
    @Nullable
    private final LivingEntity itemHolder;
    private ItemStack item;
    private int dealtDamage = 0;
    private final boolean itemsAdder;

    public ItemDurability(ItemStack itemStack) {
        this(null, itemStack);
    }

    public ItemDurability(@Nullable LivingEntity livingEntity, ItemStack itemStack) {
        this.itemHolder = livingEntity;
        this.item = itemStack == null ? new ItemStack(Material.AIR) : itemStack;
        this.itemsAdder = HooksHandler.getHook(HookPlugin.ITEMSADDER) != null && ((ItemsAdderHook)HooksHandler.getHook(HookPlugin.ITEMSADDER)).isCustomItem(this.item);
    }

    public ItemStack getItemStack() {
        if (this.item.getAmount() == 0) {
            this.item.setAmount(1);
        }
        if (this.item.getType().getMaxDurability() == 0) {
            return this.item;
        }
        if (this.isBroken() && !ASManager.isUnbreakable(this.item)) {
            return new ItemStack(Material.AIR);
        }
        return this.item;
    }

    public int getUnbreakingLevel() {
        return this.item.getEnchantmentLevel(VanillaEnchants.displayNameToEnchant("unbreaking"));
    }

    public ItemDurability damageItem(int n) {
        try {
            if (!ASManager.isDamageable(this.item.getType()) || this.item.getType().name().contains("SKULL") || ASManager.isUnbreakable(this.item)) {
                return this;
            }
            if (n < 0) {
                this.healItem(n);
                return this;
            }
            if (this.itemHolder instanceof Player) {
                PlayerItemDamageEvent playerItemDamageEvent = new PlayerItemDamageEvent((Player)this.itemHolder, this.item, n);
                Bukkit.getPluginManager().callEvent((Event)playerItemDamageEvent);
                if (playerItemDamageEvent.isCancelled()) {
                    return this;
                }
                n = playerItemDamageEvent.getDamage();
            }
            int n2 = this.getMaxDurability();
            if (this.getDurability() + n > n2) {
                this.setDurability(n2);
                return this;
            }
            this.setDurability(this.getDurability() + n);
            this.dealtDamage += n;
        } catch (Exception exception) {
            // empty catch block
        }
        return this;
    }

    public boolean isBroken() {
        return this.getDurability() >= this.getMaxDurability();
    }

    public ItemDurability healItem(int n) {
        n = (short)Math.abs(n);
        if (!ASManager.isDamageable(this.item.getType())) {
            return this;
        }
        if (this.item.getType().name().contains("SKULL")) {
            return this;
        }
        if (this.itemHolder instanceof Player) {
            PlayerItemDamageEvent playerItemDamageEvent = new PlayerItemDamageEvent((Player)this.itemHolder, this.item, n);
            Bukkit.getPluginManager().callEvent((Event)playerItemDamageEvent);
            if (playerItemDamageEvent.isCancelled()) {
                return this;
            }
            n = playerItemDamageEvent.getDamage();
        }
        if (this.getDurability() - n < 0) {
            this.repairItem();
            return this;
        }
        this.setDurability(this.getDurability() - n);
        return this;
    }

    public ItemDurability handleDurabilityChange(int n) {
        if (n < 0) {
            return this.damageItem((short)(-n));
        }
        return this.healItem((short)n);
    }

    public int getMaxDurability() {
        if (this.itemsAdder) {
            return ((ItemsAdderHook)HooksHandler.getHook(HookPlugin.ITEMSADDER)).getCustomItemMaxDurability(this.item);
        }
        return this.item.getType().getMaxDurability();
    }

    public int getDurability() {
        if (this.itemsAdder) {
            return this.getMaxDurability() - ((ItemsAdderHook)HooksHandler.getHook(HookPlugin.ITEMSADDER)).getCustomItemDurability(this.item);
        }
        return this.item.getDurability();
    }

    public ItemDurability setDurability(int n) {
        if (this.itemsAdder) {
            this.item = ((ItemsAdderHook)HooksHandler.getHook(HookPlugin.ITEMSADDER)).setCustomItemDurability(this.item, n < this.getMaxDurability() ? this.getMaxDurability() - n : -1);
            return this;
        }
        if (n >= this.getMaxDurability() && this.itemHolder != null && this.itemHolder instanceof Player && this.item.getItemMeta() instanceof Damageable) {
            Bukkit.getPluginManager().callEvent((Event)new PlayerItemBreakEvent((Player)this.itemHolder, this.item));
        }
        this.setDurabilityVersionSave(n);
        return this;
    }

    private int getDurabilityVersionSafe() {
        if (MinecraftVersion.getVersionNumber() >= 1130) {
            ItemMeta itemMeta = this.item.getItemMeta();
            if (itemMeta instanceof Damageable) {
                Damageable damageable = (Damageable)itemMeta;
                return damageable.getDamage();
            }
            return -1;
        }
        return this.item.getDurability();
    }

    private void setDurabilityVersionSave(int n) {
        if (MinecraftVersion.getVersionNumber() >= 1130) {
            ItemMeta itemMeta = this.item.getItemMeta();
            if (itemMeta instanceof Damageable) {
                Damageable damageable = (Damageable)itemMeta;
                damageable.setDamage(n);
                this.item.setItemMeta(itemMeta);
            }
        } else {
            this.item.setDurability((short)n);
        }
    }

    public ItemDurability repairItem() {
        this.setDurability(0);
        return this;
    }

    public int getDealtDamage() {
        return this.dealtDamage;
    }
}

