/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  lombok.Generated
 *  org.bukkit.Color
 *  org.bukkit.Material
 *  org.bukkit.enchantments.Enchantment
 *  org.bukkit.inventory.ItemFlag
 *  org.bukkit.inventory.ItemStack
 *  org.bukkit.inventory.meta.Damageable
 *  org.bukkit.inventory.meta.ItemMeta
 *  org.bukkit.inventory.meta.LeatherArmorMeta
 *  org.bukkit.inventory.meta.PotionMeta
 *  org.bukkit.potion.PotionType
 */
package nl.sbdeveloper.vehiclesplus.utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.UnaryOperator;
import lombok.Generated;
import nl.sbdeveloper.vehiclesplus.libs.nbtapi.NBT;
import nl.sbdeveloper.vehiclesplus.libs.nbtapi.iface.ReadWriteItemNBT;
import nl.sbdeveloper.vehiclesplus.libs.xseries.XMaterial;
import nl.sbdeveloper.vehiclesplus.libs.xseries.profiles.builder.XSkull;
import nl.sbdeveloper.vehiclesplus.libs.xseries.profiles.objects.ProfileInputType;
import nl.sbdeveloper.vehiclesplus.libs.xseries.profiles.objects.Profileable;
import nl.sbdeveloper.vehiclesplus.utils.nms.ReflectionUtil;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.PotionType;
import org.jetbrains.annotations.NotNull;

public class ItemBuilder {
    private final ItemStack itemStack;

    public ItemBuilder(@NotNull Material material) {
        this.itemStack = new ItemStack(material);
    }

    public ItemBuilder(@NotNull ItemStack itemStack) {
        this.itemStack = itemStack.clone();
    }

    public ItemBuilder(@NotNull XMaterial xMaterial) {
        ItemStack itemStack = xMaterial.parseItem();
        if (itemStack == null) {
            throw new IllegalArgumentException("Received invalid / unsupported XMaterial: " + xMaterial.name());
        }
        this.itemStack = itemStack;
    }

    private void applyToMeta(UnaryOperator<ItemMeta> unaryOperator) {
        this.itemStack.setItemMeta((ItemMeta)unaryOperator.apply(this.itemStack.getItemMeta()));
    }

    public ItemBuilder amount(int n) {
        this.itemStack.setAmount(n);
        return this;
    }

    public ItemBuilder displayname(@NotNull String string) {
        this.applyToMeta(itemMeta -> {
            itemMeta.setDisplayName(string);
            return itemMeta;
        });
        return this;
    }

    public ItemBuilder lore(@NotNull String ... stringArray) {
        this.applyToMeta(itemMeta -> {
            ArrayList arrayList = itemMeta.getLore();
            if (arrayList == null) {
                arrayList = new ArrayList();
            }
            for (String string : stringArray) {
                String[] stringArray2 = string.split("[\\r\\n]+");
                Collections.addAll(arrayList, stringArray2);
            }
            itemMeta.setLore(arrayList);
            return itemMeta;
        });
        return this;
    }

    public ItemBuilder lore(@NotNull List<String> list) {
        this.lore((String[])list.toArray(String[]::new));
        return this;
    }

    public ItemBuilder flag(@NotNull ItemFlag ... itemFlagArray) {
        this.applyToMeta(itemMeta -> {
            itemMeta.addItemFlags(itemFlagArray);
            return itemMeta;
        });
        return this;
    }

    public ItemBuilder hideAllFlags() {
        return this.flag(ItemFlag.values());
    }

    public ItemBuilder customModelData(int n, Function<ItemBuilder, ItemBuilder> function) {
        if (!XMaterial.supports(14)) {
            return function.apply(this);
        }
        this.applyToMeta(itemMeta -> {
            itemMeta.setCustomModelData(Integer.valueOf(n));
            return itemMeta;
        });
        return this;
    }

    public ItemBuilder enchant(@NotNull Map<Enchantment, Integer> map) {
        this.itemStack.addEnchantments(map);
        return this;
    }

    public ItemBuilder enchant(@NotNull Enchantment enchantment, int n) {
        this.itemStack.addEnchantment(enchantment, n);
        return this;
    }

    public ItemBuilder durability(int n) {
        if (XMaterial.supports(13)) {
            this.applyToMeta(itemMeta -> {
                if (!(itemMeta instanceof Damageable)) {
                    return itemMeta;
                }
                ((Damageable)itemMeta).setDamage(n);
                return itemMeta;
            });
        } else {
            this.itemStack.setDurability((short)n);
        }
        return this;
    }

    public ItemBuilder unbreakable() {
        return this.unbreakable(true);
    }

    public ItemBuilder unbreakable(boolean bl) {
        this.applyToMeta(itemMeta -> {
            itemMeta.setUnbreakable(bl);
            return itemMeta;
        });
        return this;
    }

    public ItemBuilder glow() {
        this.itemStack.addUnsafeEnchantment(Enchantment.LURE, 0);
        this.flag(ItemFlag.HIDE_ENCHANTS);
        return this;
    }

    public ItemBuilder skullTexture(@NotNull String string) {
        this.applyToMeta(itemMeta -> {
            itemMeta = XSkull.of(itemMeta).profile(Profileable.of(ProfileInputType.USERNAME, string)).apply();
            return itemMeta;
        });
        return this;
    }

    public ItemBuilder armorColor(@NotNull Color color) {
        this.applyToMeta(itemMeta -> {
            if (!(itemMeta instanceof LeatherArmorMeta)) {
                return itemMeta;
            }
            ((LeatherArmorMeta)itemMeta).setColor(color);
            return itemMeta;
        });
        return this;
    }

    public ItemBuilder potionEffect(@NotNull PotionType potionType) {
        if (!this.itemStack.getType().name().contains("POTION")) {
            throw new UnsupportedOperationException("ItemStack is not a potion! (Type: " + this.itemStack.getType().name() + ")");
        }
        this.applyToMeta(itemMeta -> {
            if (!(itemMeta instanceof PotionMeta)) {
                return itemMeta;
            }
            PotionMeta potionMeta = (PotionMeta)itemMeta;
            try {
                potionMeta.setBasePotionType(potionType);
            } catch (NoSuchMethodError noSuchMethodError) {
                Class<?> clazz = ReflectionUtil.getClass("org.bukkit.potion.PotionData");
                Object object = ReflectionUtil.callDeclaredConstructor(clazz, potionType);
                ReflectionUtil.callDeclaredMethod(potionMeta, "setBasePotionData", clazz, object);
            }
            return itemMeta;
        });
        return this;
    }

    public ItemBuilder applyNBT(Consumer<ReadWriteItemNBT> consumer) {
        NBT.modify(this.itemStack, consumer);
        return this;
    }

    public <T> ItemBuilder applyNBT(Function<ReadWriteItemNBT, T> function) {
        NBT.modify(this.itemStack, function);
        return this;
    }

    @Generated
    public ItemStack getItemStack() {
        return this.itemStack;
    }
}

