/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  net.advancedplugins.ae.api.AEAPI
 *  org.bukkit.Bukkit
 *  org.bukkit.Color
 *  org.bukkit.FireworkEffect
 *  org.bukkit.Material
 *  org.bukkit.NamespacedKey
 *  org.bukkit.Registry
 *  org.bukkit.attribute.Attribute
 *  org.bukkit.attribute.AttributeModifier
 *  org.bukkit.configuration.ConfigurationSection
 *  org.bukkit.enchantments.Enchantment
 *  org.bukkit.inventory.ItemFlag
 *  org.bukkit.inventory.ItemStack
 *  org.bukkit.inventory.meta.ArmorMeta
 *  org.bukkit.inventory.meta.EnchantmentStorageMeta
 *  org.bukkit.inventory.meta.FireworkEffectMeta
 *  org.bukkit.inventory.meta.ItemMeta
 *  org.bukkit.inventory.meta.LeatherArmorMeta
 *  org.bukkit.inventory.meta.SkullMeta
 *  org.bukkit.inventory.meta.trim.ArmorTrim
 *  org.bukkit.inventory.meta.trim.TrimMaterial
 *  org.bukkit.inventory.meta.trim.TrimPattern
 *  org.bukkit.persistence.PersistentDataHolder
 */
package net.advancedplugins.as.impl.utils.items;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import net.advancedplugins.ae.api.AEAPI;
import net.advancedplugins.as.impl.utils.ASManager;
import net.advancedplugins.as.impl.utils.SkullCreator;
import net.advancedplugins.as.impl.utils.hooks.HookPlugin;
import net.advancedplugins.as.impl.utils.hooks.HooksHandler;
import net.advancedplugins.as.impl.utils.items.ItemFlagFix;
import net.advancedplugins.as.impl.utils.nbt.NBTapi;
import net.advancedplugins.as.impl.utils.nbt.utils.MinecraftVersion;
import net.advancedplugins.as.impl.utils.pdc.PDCHandler;
import net.advancedplugins.as.impl.utils.text.Text;
import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.Registry;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ArmorMeta;
import org.bukkit.inventory.meta.EnchantmentStorageMeta;
import org.bukkit.inventory.meta.FireworkEffectMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.inventory.meta.trim.ArmorTrim;
import org.bukkit.inventory.meta.trim.TrimMaterial;
import org.bukkit.inventory.meta.trim.TrimPattern;
import org.bukkit.persistence.PersistentDataHolder;

public class ItemBuilder {
    private ItemStack is;
    private ItemMeta im;
    private ConfigurationSection section;
    private boolean glow = false;

    public ItemBuilder(Material material) {
        this(material, 1);
    }

    public ItemBuilder(ItemStack itemStack) {
        this.is = itemStack;
        this.im = itemStack.getItemMeta();
    }

    public ItemBuilder(ConfigurationSection configurationSection) {
        this.section = configurationSection;
        ItemStack itemStack = ASManager.matchMaterial(configurationSection.getString("type"), 1, 0);
        if (itemStack == null) {
            throw new IllegalArgumentException("Could create item from config section: " + configurationSection.getCurrentPath() + " because the type was null.");
        }
        String string = configurationSection.isString("name") ? Text.modify(configurationSection.getString("name")) : "";
        ArrayList<String> arrayList = configurationSection.isList("lore") ? configurationSection.getStringList("lore") : new ArrayList();
        int n = configurationSection.isInt("custom-model-data") ? configurationSection.getInt("custom-model-data") : 0;
        int n2 = configurationSection.isInt("amount") ? configurationSection.getInt("amount") : 1;
        int n3 = configurationSection.isInt("advanced-heads-id") ? configurationSection.getInt("advanced-heads-id") : 0;
        boolean bl = configurationSection.isBoolean("force-glow") && configurationSection.getBoolean("force-glow");
        String string2 = configurationSection.isString("owner") ? configurationSection.getString("owner") : null;
        ItemStack itemStack2 = new ItemStack(itemStack);
        ItemMeta itemMeta = itemStack2.getItemMeta();
        assert (itemMeta != null);
        itemMeta.setDisplayName(string);
        itemMeta.setLore(Text.modify(arrayList));
        if (n != 0) {
            itemMeta.setCustomModelData(Integer.valueOf(n));
        }
        itemStack2.setAmount(n2);
        itemStack2.setItemMeta(itemMeta);
        this.glow = bl;
        this.is = itemStack2;
        this.im = this.is.getItemMeta();
        if (n3 != 0 && Bukkit.getServer().getPluginManager().isPluginEnabled("AdvancedHeads")) {
            this.im = this.is.getItemMeta();
        }
        if (string2 != null) {
            this.is = SkullCreator.itemWithBase64(this.is, string2);
            this.im = this.is.getItemMeta();
        }
    }

    public ItemBuilder(Material material, int n) {
        this.is = new ItemStack(material, n);
        this.im = this.is.getItemMeta();
    }

    public ItemBuilder(Material material, int n, byte by) {
        this.is = new ItemStack(material, n, (short)by);
        this.im = this.is.getItemMeta();
    }

    public Optional<ConfigurationSection> getConfigSection() {
        if (this.section == null) {
            return Optional.empty();
        }
        return Optional.of(this.section);
    }

    public ItemBuilder setDurability(short s) {
        this.is.setDurability(s);
        return this;
    }

    public ItemBuilder setType(Material material) {
        this.is.setType(material);
        return this;
    }

    public ItemBuilder setName(String string) {
        this.im.setDisplayName(string);
        return this;
    }

    public ItemBuilder addUnsafeEnchantment(Enchantment enchantment, int n) {
        if (this.im instanceof EnchantmentStorageMeta) {
            EnchantmentStorageMeta enchantmentStorageMeta = (EnchantmentStorageMeta)this.im;
            enchantmentStorageMeta.addStoredEnchant(enchantment, n, true);
            this.im = enchantmentStorageMeta;
        } else {
            this.im.addEnchant(enchantment, n, true);
        }
        return this;
    }

    public ItemBuilder removeEnchantment(Enchantment enchantment) {
        if (this.im instanceof EnchantmentStorageMeta) {
            EnchantmentStorageMeta enchantmentStorageMeta = (EnchantmentStorageMeta)this.im;
            enchantmentStorageMeta.removeStoredEnchant(enchantment);
        } else {
            this.im.removeEnchant(enchantment);
        }
        return this;
    }

    public ItemBuilder setSkullOwner(String string) {
        try {
            SkullMeta skullMeta = (SkullMeta)this.im;
            skullMeta.setOwner(string);
        } catch (ClassCastException classCastException) {
            // empty catch block
        }
        return this;
    }

    public ItemBuilder addEnchantment(Enchantment enchantment, int n) {
        this.im.addEnchant(enchantment, n, true);
        return this;
    }

    public ItemBuilder addEnchantments(Map<Enchantment, Integer> map) {
        this.is.addEnchantments(map);
        return this;
    }

    public ItemBuilder setInfinityDurability() {
        this.is.setDurability((short)Short.MAX_VALUE);
        return this;
    }

    public ItemBuilder setLore(String ... stringArray) {
        this.im.setLore(Arrays.asList(stringArray));
        return this;
    }

    public ItemBuilder setLore(List<String> list) {
        this.im.setLore(list);
        this.is.setItemMeta(this.im);
        return this;
    }

    public ItemBuilder removeLoreLine(String string) {
        ArrayList arrayList = new ArrayList(this.im.getLore());
        if (!arrayList.contains(string)) {
            return this;
        }
        arrayList.remove(string);
        this.im.setLore(arrayList);
        return this;
    }

    public ItemBuilder removeLoreLine(int n) {
        ArrayList arrayList = new ArrayList(this.im.getLore());
        if (n < 0 || n > arrayList.size()) {
            return this;
        }
        arrayList.remove(n);
        this.im.setLore(arrayList);
        return this;
    }

    public ItemBuilder addLoreLine(String string) {
        ArrayList<String> arrayList = new ArrayList<String>();
        if (this.im.hasLore()) {
            arrayList = new ArrayList(this.im.getLore());
        }
        arrayList.add(string);
        this.im.setLore(arrayList);
        return this;
    }

    public ItemBuilder addLoreLine(String string, int n) {
        ArrayList<String> arrayList = new ArrayList<String>(this.im.getLore());
        arrayList.set(n, string);
        this.im.setLore(arrayList);
        return this;
    }

    public ItemBuilder setColor(Color color) {
        if (this.im instanceof LeatherArmorMeta) {
            LeatherArmorMeta leatherArmorMeta = (LeatherArmorMeta)this.im;
            leatherArmorMeta.setColor(color);
        } else if (this.im instanceof FireworkEffectMeta) {
            FireworkEffectMeta fireworkEffectMeta = (FireworkEffectMeta)this.im;
            fireworkEffectMeta.setEffect(FireworkEffect.builder().withColor(color).build());
        }
        return this;
    }

    public ItemBuilder setItemFlags(ItemFlag ... itemFlagArray) {
        this.im.addItemFlags(itemFlagArray);
        return this;
    }

    public ItemBuilder setArmorTrim(String string, String string2) {
        ArmorMeta armorMeta = (ArmorMeta)this.im;
        TrimMaterial trimMaterial = (TrimMaterial)Registry.TRIM_MATERIAL.get(NamespacedKey.minecraft((String)string.toLowerCase()));
        TrimPattern trimPattern = (TrimPattern)Registry.TRIM_PATTERN.get(NamespacedKey.minecraft((String)string2.toLowerCase()));
        armorMeta.setTrim(new ArmorTrim(trimMaterial, trimPattern));
        return this;
    }

    public ItemBuilder addAttribute(Attribute attribute, AttributeModifier attributeModifier) {
        this.im.addAttributeModifier(attribute, attributeModifier);
        return this;
    }

    public ItemBuilder setAmount(int n) {
        this.is.setAmount(n);
        return this;
    }

    public ItemBuilder addItemFlag(ItemFlag ... itemFlagArray) {
        ItemFlagFix.fix(this.im);
        this.im.addItemFlags(itemFlagArray);
        return this;
    }

    public ItemBuilder setCustomModelData(Integer n) {
        if (MinecraftVersion.getVersionNumber() >= 1140) {
            this.im.setCustomModelData(n);
        }
        return this;
    }

    public ItemBuilder setItemModel(String string) {
        if (MinecraftVersion.getVersionNumber() >= 1212) {
            this.im.setItemModel(NamespacedKey.fromString((String)string));
        }
        return this;
    }

    public ItemBuilder addNBTTag(String string, String string2) {
        this.is.setItemMeta(this.im);
        this.is = NBTapi.addNBTTag(string, string2, this.is);
        this.im = this.is.getItemMeta();
        return this;
    }

    public ItemBuilder addPDC(String string, String string2) {
        PDCHandler.setString((PersistentDataHolder)this.im, string, string2);
        return this;
    }

    public ItemBuilder setGlowing(boolean bl) {
        this.glow = bl;
        return this;
    }

    public ItemMeta getItemMeta() {
        return this.im;
    }

    public ItemStack toItemStack() {
        this.is.setItemMeta(this.im);
        if (this.glow) {
            this.is = ASManager.makeItemGlow(this.is);
        }
        return this.is;
    }

    public ItemBuilder setUnbreakable(boolean bl) {
        if (MinecraftVersion.getVersionNumber() >= 1110) {
            assert (this.im != null);
            this.im.setUnbreakable(bl);
        }
        return this;
    }

    public ItemBuilder addCustomEnchantment(String string, int n) {
        this.is.setItemMeta(this.im);
        if (!HooksHandler.isEnabled(HookPlugin.ADVANCEDENCHANTMENTS)) {
            return this;
        }
        this.is = AEAPI.applyEnchant((String)string, (int)n, (ItemStack)this.is);
        this.im = this.is.getItemMeta();
        return this;
    }

    public ItemBuilder setCustomNBT(String string) {
        Bukkit.getUnsafe().modifyItemStack(this.is, string);
        return this;
    }

    public void addLoreLines(List<String> list) {
        ArrayList<String> arrayList = new ArrayList<String>();
        if (this.im.hasLore()) {
            arrayList = new ArrayList(this.im.getLore());
        }
        arrayList.addAll(Text.modify(list));
        this.im.setLore(arrayList);
    }
}

