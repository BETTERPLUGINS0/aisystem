/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  com.mojang.authlib.GameProfile
 *  com.mojang.authlib.properties.Property
 *  org.bukkit.Material
 *  org.bukkit.OfflinePlayer
 *  org.bukkit.inventory.ItemStack
 *  org.bukkit.inventory.meta.ItemMeta
 *  org.bukkit.inventory.meta.SkullMeta
 *  org.jetbrains.annotations.Contract
 *  org.jetbrains.annotations.NotNull
 */
package me.zombie_striker.qav.gui.builder.item;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import java.lang.reflect.Field;
import java.util.UUID;
import me.zombie_striker.qav.gui.builder.item.BannerBuilder;
import me.zombie_striker.qav.gui.builder.item.BaseItemBuilder;
import me.zombie_striker.qav.gui.builder.item.BookBuilder;
import me.zombie_striker.qav.gui.builder.item.FireworkBuilder;
import me.zombie_striker.qav.gui.builder.item.MapBuilder;
import me.zombie_striker.qav.gui.builder.item.SkullBuilder;
import me.zombie_striker.qav.gui.components.util.SkullUtil;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

public class ItemBuilder
extends BaseItemBuilder<ItemBuilder> {
    ItemBuilder(@NotNull ItemStack itemStack) {
        super(itemStack);
    }

    @NotNull
    @Contract(value="_ -> new")
    public static ItemBuilder from(@NotNull ItemStack itemStack) {
        return new ItemBuilder(itemStack);
    }

    @NotNull
    @Contract(value="_ -> new")
    public static ItemBuilder from(@NotNull Material material) {
        return new ItemBuilder(new ItemStack(material));
    }

    @NotNull
    @Contract(value=" -> new")
    public static BannerBuilder banner() {
        return new BannerBuilder();
    }

    @NotNull
    @Contract(value="_ -> new")
    public static BannerBuilder banner(@NotNull ItemStack itemStack) {
        return new BannerBuilder(itemStack);
    }

    @NotNull
    @Contract(value="_ -> new")
    public static BookBuilder book(@NotNull ItemStack itemStack) {
        return new BookBuilder(itemStack);
    }

    @NotNull
    @Contract(value=" -> new")
    public static FireworkBuilder firework() {
        return new FireworkBuilder(new ItemStack(Material.FIREWORK_ROCKET));
    }

    @NotNull
    @Contract(value="_ -> new")
    public static FireworkBuilder firework(@NotNull ItemStack itemStack) {
        return new FireworkBuilder(itemStack);
    }

    @NotNull
    @Contract(value=" -> new")
    public static MapBuilder map() {
        return new MapBuilder();
    }

    @NotNull
    @Contract(value="_ -> new")
    public static MapBuilder map(@NotNull ItemStack itemStack) {
        return new MapBuilder(itemStack);
    }

    @NotNull
    @Contract(value=" -> new")
    public static SkullBuilder skull() {
        return new SkullBuilder();
    }

    @NotNull
    @Contract(value="_ -> new")
    public static SkullBuilder skull(@NotNull ItemStack itemStack) {
        return new SkullBuilder(itemStack);
    }

    @NotNull
    @Contract(value=" -> new")
    public static FireworkBuilder star() {
        return new FireworkBuilder(new ItemStack(Material.FIREWORK_STAR));
    }

    @NotNull
    @Contract(value="_ -> new")
    public static FireworkBuilder star(@NotNull ItemStack itemStack) {
        return new FireworkBuilder(itemStack);
    }

    @Deprecated
    public ItemBuilder setSkullTexture(@NotNull String string) {
        if (!SkullUtil.isPlayerSkull(this.getItemStack())) {
            return this;
        }
        SkullMeta skullMeta = (SkullMeta)this.getMeta();
        GameProfile gameProfile = new GameProfile(UUID.randomUUID(), null);
        gameProfile.getProperties().put((Object)"textures", (Object)new Property("textures", string));
        try {
            Field field = skullMeta.getClass().getDeclaredField("profile");
            field.setAccessible(true);
            field.set(skullMeta, gameProfile);
        } catch (IllegalAccessException | IllegalArgumentException | NoSuchFieldException exception) {
            exception.printStackTrace();
        }
        this.setMeta((ItemMeta)skullMeta);
        return this;
    }

    @Deprecated
    public ItemBuilder setSkullOwner(@NotNull OfflinePlayer offlinePlayer) {
        if (!SkullUtil.isPlayerSkull(this.getItemStack())) {
            return this;
        }
        SkullMeta skullMeta = (SkullMeta)this.getMeta();
        skullMeta.setOwningPlayer(offlinePlayer);
        this.setMeta((ItemMeta)skullMeta);
        return this;
    }
}

