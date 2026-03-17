/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.Bukkit
 *  org.bukkit.Color
 *  org.bukkit.Material
 *  org.bukkit.enchantments.Enchantment
 *  org.bukkit.event.inventory.InventoryClickEvent
 *  org.bukkit.inventory.ItemFlag
 *  org.bukkit.inventory.ItemStack
 *  org.bukkit.inventory.meta.ItemMeta
 *  org.bukkit.inventory.meta.LeatherArmorMeta
 *  org.bukkit.persistence.PersistentDataContainer
 *  org.jetbrains.annotations.Contract
 *  org.jetbrains.annotations.NotNull
 *  org.jetbrains.annotations.Nullable
 */
package me.zombie_striker.qav.gui.builder.item;

import com.google.common.base.Preconditions;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.EnumSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import me.zombie_striker.qav.gui.components.GuiAction;
import me.zombie_striker.qav.gui.components.exception.GuiException;
import me.zombie_striker.qav.gui.components.util.ItemNbt;
import me.zombie_striker.qav.gui.components.util.Legacy;
import me.zombie_striker.qav.gui.components.util.VersionHelper;
import me.zombie_striker.qav.gui.guis.GuiItem;
import net.kyori.adventure.platform.bukkit.MinecraftComponentSerializer;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.gson.GsonComponentSerializer;
import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public abstract class BaseItemBuilder<B extends BaseItemBuilder<B>> {
    private static final EnumSet<Material> LEATHER_ARMOR = EnumSet.of(Material.LEATHER_HELMET, Material.LEATHER_CHESTPLATE, Material.LEATHER_LEGGINGS, Material.LEATHER_BOOTS);
    private static final Field DISPLAY_NAME_FIELD;
    private static final Field LORE_FIELD;
    private ItemStack itemStack;
    private ItemMeta meta;

    protected BaseItemBuilder(@NotNull ItemStack itemStack) {
        Preconditions.checkNotNull(itemStack, "Item can't be null!");
        this.itemStack = itemStack;
        this.meta = itemStack.hasItemMeta() ? itemStack.getItemMeta() : Bukkit.getItemFactory().getItemMeta(itemStack.getType());
    }

    @NotNull
    protected Object serializeComponent(@NotNull Component component) {
        if (VersionHelper.IS_ITEM_NAME_COMPONENT) {
            return MinecraftComponentSerializer.get().serialize(component);
        }
        return GsonComponentSerializer.gson().serialize(component);
    }

    @NotNull
    protected Component deserializeComponent(@NotNull Object object) {
        if (VersionHelper.IS_ITEM_NAME_COMPONENT) {
            return MinecraftComponentSerializer.get().deserialize(object);
        }
        return GsonComponentSerializer.gson().deserialize((String)object);
    }

    @NotNull
    @Contract(value="_ -> this")
    public B name(@NotNull Component component) {
        if (this.meta == null) {
            return (B)this;
        }
        if (VersionHelper.IS_COMPONENT_LEGACY) {
            this.meta.setDisplayName(Legacy.SERIALIZER.serialize(component));
            return (B)this;
        }
        try {
            DISPLAY_NAME_FIELD.set(this.meta, this.serializeComponent(component));
        } catch (IllegalAccessException illegalAccessException) {
            illegalAccessException.printStackTrace();
        }
        return (B)this;
    }

    @NotNull
    @Contract(value="_ -> this")
    public B amount(int n) {
        this.itemStack.setAmount(n);
        return (B)this;
    }

    @NotNull
    @Contract(value="_ -> this")
    public B lore(@Nullable Component @NotNull ... componentArray) {
        return this.lore(Arrays.asList(componentArray));
    }

    @NotNull
    @Contract(value="_ -> this")
    public B lore(@NotNull List<@Nullable Component> list) {
        if (this.meta == null) {
            return (B)this;
        }
        if (VersionHelper.IS_COMPONENT_LEGACY) {
            this.meta.setLore(list.stream().filter(Objects::nonNull).map(Legacy.SERIALIZER::serialize).collect(Collectors.toList()));
            return (B)this;
        }
        List list2 = list.stream().filter(Objects::nonNull).map(this::serializeComponent).collect(Collectors.toList());
        try {
            LORE_FIELD.set(this.meta, list2);
        } catch (IllegalAccessException illegalAccessException) {
            illegalAccessException.printStackTrace();
        }
        return (B)this;
    }

    @NotNull
    @Contract(value="_ -> this")
    public B lore(@NotNull Consumer<List<@Nullable Component>> consumer) {
        ArrayList<Component> arrayList;
        if (this.meta == null) {
            return (B)this;
        }
        if (VersionHelper.IS_COMPONENT_LEGACY) {
            List list = this.meta.getLore();
            arrayList = list == null ? new ArrayList() : list.stream().map(Legacy.SERIALIZER::deserialize).collect(Collectors.toList());
        } else {
            try {
                List list = (List)LORE_FIELD.get(this.meta);
                arrayList = list == null ? new ArrayList() : list.stream().map(this::deserializeComponent).collect(Collectors.toList());
            } catch (IllegalAccessException illegalAccessException) {
                arrayList = new ArrayList<Component>();
                illegalAccessException.printStackTrace();
            }
        }
        consumer.accept(arrayList);
        return this.lore(arrayList);
    }

    @NotNull
    @Contract(value="_, _, _ -> this")
    public B enchant(@NotNull Enchantment enchantment, int n, boolean bl) {
        this.meta.addEnchant(enchantment, n, bl);
        return (B)this;
    }

    @NotNull
    @Contract(value="_, _ -> this")
    public B enchant(@NotNull Enchantment enchantment, int n) {
        return this.enchant(enchantment, n, true);
    }

    @NotNull
    @Contract(value="_ -> this")
    public B enchant(@NotNull Enchantment enchantment) {
        return this.enchant(enchantment, 1, true);
    }

    @NotNull
    @Contract(value="_, _ -> this")
    public B enchant(@NotNull Map<Enchantment, Integer> map, boolean bl) {
        map.forEach((enchantment, n) -> this.enchant((Enchantment)enchantment, (int)n, bl));
        return (B)this;
    }

    @NotNull
    @Contract(value="_ -> this")
    public B enchant(@NotNull Map<Enchantment, Integer> map) {
        return this.enchant(map, true);
    }

    @NotNull
    @Contract(value="_ -> this")
    public B disenchant(@NotNull Enchantment enchantment) {
        this.itemStack.removeEnchantment(enchantment);
        return (B)this;
    }

    @NotNull
    @Contract(value="_ -> this")
    public B flags(@NotNull ItemFlag ... itemFlagArray) {
        this.meta.addItemFlags(itemFlagArray);
        return (B)this;
    }

    @NotNull
    @Contract(value=" -> this")
    public B unbreakable() {
        return this.unbreakable(true);
    }

    @NotNull
    @Contract(value="_ -> this")
    public B unbreakable(boolean bl) {
        if (VersionHelper.IS_UNBREAKABLE_LEGACY) {
            return this.setNbt("Unbreakable", bl);
        }
        this.meta.setUnbreakable(bl);
        return (B)this;
    }

    @NotNull
    @Contract(value=" -> this")
    public B glow() {
        return this.glow(true);
    }

    @NotNull
    @Contract(value="_ -> this")
    public B glow(boolean bl) {
        if (bl) {
            this.meta.addEnchant(Enchantment.LURE, 1, false);
            this.meta.addItemFlags(new ItemFlag[]{ItemFlag.HIDE_ENCHANTS});
            return (B)this;
        }
        for (Enchantment enchantment : this.meta.getEnchants().keySet()) {
            this.meta.removeEnchant(enchantment);
        }
        return (B)this;
    }

    @NotNull
    @Contract(value="_ -> this")
    public B pdc(@NotNull Consumer<PersistentDataContainer> consumer) {
        consumer.accept(this.meta.getPersistentDataContainer());
        return (B)this;
    }

    @NotNull
    @Contract(value="_ -> this")
    public B model(int n) {
        if (VersionHelper.IS_CUSTOM_MODEL_DATA) {
            this.meta.setCustomModelData(Integer.valueOf(n));
        }
        return (B)this;
    }

    @NotNull
    @Contract(value="_ -> this")
    public B color(@NotNull Color color) {
        if (LEATHER_ARMOR.contains(this.itemStack.getType())) {
            LeatherArmorMeta leatherArmorMeta = (LeatherArmorMeta)this.getMeta();
            leatherArmorMeta.setColor(color);
            this.setMeta((ItemMeta)leatherArmorMeta);
        }
        return (B)this;
    }

    @NotNull
    @Contract(value="_, _ -> this")
    public B setNbt(@NotNull String string, @NotNull String string2) {
        this.itemStack.setItemMeta(this.meta);
        this.itemStack = ItemNbt.setString(this.itemStack, string, string2);
        this.meta = this.itemStack.getItemMeta();
        return (B)this;
    }

    @NotNull
    @Contract(value="_, _ -> this")
    public B setNbt(@NotNull String string, boolean bl) {
        this.itemStack.setItemMeta(this.meta);
        this.itemStack = ItemNbt.setBoolean(this.itemStack, string, bl);
        this.meta = this.itemStack.getItemMeta();
        return (B)this;
    }

    @NotNull
    @Contract(value="_ -> this")
    public B removeNbt(@NotNull String string) {
        this.itemStack.setItemMeta(this.meta);
        this.itemStack = ItemNbt.removeTag(this.itemStack, string);
        this.meta = this.itemStack.getItemMeta();
        return (B)this;
    }

    @NotNull
    public ItemStack build() {
        this.itemStack.setItemMeta(this.meta);
        return this.itemStack;
    }

    @NotNull
    @Contract(value=" -> new")
    public GuiItem asGuiItem() {
        return new GuiItem(this.build());
    }

    @NotNull
    @Contract(value="_ -> new")
    public GuiItem asGuiItem(@NotNull GuiAction<InventoryClickEvent> guiAction) {
        return new GuiItem(this.build(), guiAction);
    }

    @NotNull
    protected ItemStack getItemStack() {
        return this.itemStack;
    }

    protected void setItemStack(@NotNull ItemStack itemStack) {
        this.itemStack = itemStack;
    }

    @NotNull
    protected ItemMeta getMeta() {
        return this.meta;
    }

    protected void setMeta(@NotNull ItemMeta itemMeta) {
        this.meta = itemMeta;
    }

    @Deprecated
    public B setName(@NotNull String string) {
        this.getMeta().setDisplayName(string);
        return (B)this;
    }

    @Deprecated
    public B setAmount(int n) {
        this.getItemStack().setAmount(n);
        return (B)this;
    }

    @Deprecated
    public B addLore(@NotNull String ... stringArray) {
        return this.addLore(Arrays.asList(stringArray));
    }

    @Deprecated
    public B addLore(@NotNull List<String> list) {
        List<String> list2 = this.getMeta().hasLore() ? this.getMeta().getLore() : new ArrayList();
        list2.addAll(list);
        return this.setLore(list2);
    }

    @Deprecated
    public B setLore(@NotNull String ... stringArray) {
        return this.setLore(Arrays.asList(stringArray));
    }

    @Deprecated
    public B setLore(@NotNull List<String> list) {
        this.getMeta().setLore(list);
        return (B)this;
    }

    @Deprecated
    public B addEnchantment(@NotNull Enchantment enchantment, int n, boolean bl) {
        this.getMeta().addEnchant(enchantment, n, bl);
        return (B)this;
    }

    @Deprecated
    public B addEnchantment(@NotNull Enchantment enchantment, int n) {
        return this.addEnchantment(enchantment, n, true);
    }

    @Deprecated
    public B addEnchantment(@NotNull Enchantment enchantment) {
        return this.addEnchantment(enchantment, 1, true);
    }

    @Deprecated
    public B removeEnchantment(@NotNull Enchantment enchantment) {
        this.getItemStack().removeEnchantment(enchantment);
        return (B)this;
    }

    @Deprecated
    public B addItemFlags(@NotNull ItemFlag ... itemFlagArray) {
        this.getMeta().addItemFlags(itemFlagArray);
        return (B)this;
    }

    @Deprecated
    public B setUnbreakable(boolean bl) {
        return this.unbreakable(bl);
    }

    static {
        try {
            Class<?> clazz = VersionHelper.craftClass("inventory.CraftMetaItem");
            DISPLAY_NAME_FIELD = clazz.getDeclaredField("displayName");
            DISPLAY_NAME_FIELD.setAccessible(true);
            LORE_FIELD = clazz.getDeclaredField("lore");
            LORE_FIELD.setAccessible(true);
        } catch (ClassNotFoundException | NoSuchFieldException reflectiveOperationException) {
            reflectiveOperationException.printStackTrace();
            throw new GuiException("Could not retrieve displayName nor lore field for ItemBuilder.");
        }
    }
}

