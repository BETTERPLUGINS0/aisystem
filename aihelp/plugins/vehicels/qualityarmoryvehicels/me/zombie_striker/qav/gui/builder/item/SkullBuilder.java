/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  com.mojang.authlib.GameProfile
 *  com.mojang.authlib.properties.Property
 *  org.bukkit.Bukkit
 *  org.bukkit.OfflinePlayer
 *  org.bukkit.inventory.ItemStack
 *  org.bukkit.inventory.meta.ItemMeta
 *  org.bukkit.inventory.meta.SkullMeta
 *  org.bukkit.profile.PlayerProfile
 *  org.bukkit.profile.PlayerTextures
 *  org.jetbrains.annotations.Contract
 *  org.jetbrains.annotations.NotNull
 */
package me.zombie_striker.qav.gui.builder.item;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import java.lang.reflect.Field;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.UUID;
import me.zombie_striker.qav.gui.builder.item.BaseItemBuilder;
import me.zombie_striker.qav.gui.components.exception.GuiException;
import me.zombie_striker.qav.gui.components.util.SkullUtil;
import me.zombie_striker.qav.gui.components.util.VersionHelper;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.profile.PlayerProfile;
import org.bukkit.profile.PlayerTextures;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

public final class SkullBuilder
extends BaseItemBuilder<SkullBuilder> {
    private static final Field PROFILE_FIELD;

    SkullBuilder() {
        super(SkullUtil.skull());
    }

    SkullBuilder(@NotNull ItemStack itemStack) {
        super(itemStack);
        if (!SkullUtil.isPlayerSkull(itemStack)) {
            throw new GuiException("SkullBuilder requires the material to be a PLAYER_HEAD/SKULL_ITEM!");
        }
    }

    @NotNull
    @Contract(value="_, _ -> this")
    public SkullBuilder texture(@NotNull String string, @NotNull UUID uUID) {
        if (!SkullUtil.isPlayerSkull(this.getItemStack())) {
            return this;
        }
        if (VersionHelper.IS_PLAYER_PROFILE_API) {
            String string2 = SkullUtil.getSkinUrl(string);
            if (string2 == null) {
                return this;
            }
            SkullMeta skullMeta = (SkullMeta)this.getMeta();
            PlayerProfile playerProfile = Bukkit.createPlayerProfile((UUID)uUID, (String)"");
            PlayerTextures playerTextures = playerProfile.getTextures();
            try {
                playerTextures.setSkin(new URL(string2));
            } catch (MalformedURLException malformedURLException) {
                malformedURLException.printStackTrace();
                return this;
            }
            playerProfile.setTextures(playerTextures);
            skullMeta.setOwnerProfile(playerProfile);
            this.setMeta((ItemMeta)skullMeta);
            return this;
        }
        if (PROFILE_FIELD == null) {
            return this;
        }
        SkullMeta skullMeta = (SkullMeta)this.getMeta();
        GameProfile gameProfile = new GameProfile(uUID, "");
        gameProfile.getProperties().put((Object)"textures", (Object)new Property("textures", string));
        try {
            PROFILE_FIELD.set(skullMeta, gameProfile);
        } catch (IllegalAccessException | IllegalArgumentException exception) {
            exception.printStackTrace();
        }
        this.setMeta((ItemMeta)skullMeta);
        return this;
    }

    @NotNull
    @Contract(value="_ -> this")
    public SkullBuilder texture(@NotNull String string) {
        return this.texture(string, UUID.randomUUID());
    }

    @NotNull
    @Contract(value="_ -> this")
    public SkullBuilder owner(@NotNull OfflinePlayer offlinePlayer) {
        if (!SkullUtil.isPlayerSkull(this.getItemStack())) {
            return this;
        }
        SkullMeta skullMeta = (SkullMeta)this.getMeta();
        if (VersionHelper.IS_SKULL_OWNER_LEGACY) {
            skullMeta.setOwner(offlinePlayer.getName());
        } else {
            skullMeta.setOwningPlayer(offlinePlayer);
        }
        this.setMeta((ItemMeta)skullMeta);
        return this;
    }

    static {
        Field field;
        try {
            SkullMeta skullMeta = (SkullMeta)SkullUtil.skull().getItemMeta();
            field = skullMeta.getClass().getDeclaredField("profile");
            field.setAccessible(true);
        } catch (NoSuchFieldException noSuchFieldException) {
            noSuchFieldException.printStackTrace();
            field = null;
        }
        PROFILE_FIELD = field;
    }
}

