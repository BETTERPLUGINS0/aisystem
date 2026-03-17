/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  com.mojang.authlib.GameProfile
 *  org.bukkit.block.Block
 *  org.bukkit.block.BlockState
 *  org.bukkit.block.Skull
 *  org.bukkit.inventory.ItemStack
 *  org.bukkit.inventory.meta.ItemMeta
 *  org.bukkit.inventory.meta.SkullMeta
 *  org.jetbrains.annotations.Contract
 *  org.jetbrains.annotations.NotNull
 */
package me.zombie_striker.qav.util.xseries.profiles.builder;

import com.google.common.collect.Multimap;
import com.mojang.authlib.GameProfile;
import me.zombie_striker.qav.util.xseries.XMaterial;
import me.zombie_striker.qav.util.xseries.profiles.PlayerProfiles;
import me.zombie_striker.qav.util.xseries.profiles.builder.ProfileInstruction;
import me.zombie_striker.qav.util.xseries.profiles.objects.ProfileContainer;
import me.zombie_striker.qav.util.xseries.profiles.objects.ProfileInputType;
import me.zombie_striker.qav.util.xseries.profiles.objects.Profileable;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.block.Skull;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

public final class XSkull {
    private static final GameProfile DEFAULT_PROFILE = PlayerProfiles.signXSeries(ProfileInputType.BASE64.getProfile("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYzEwNTkxZTY5MDllNmEyODFiMzcxODM2ZTQ2MmQ2N2EyYzc4ZmEwOTUyZTkxMGYzMmI0MWEyNmM0OGMxNzU3YyJ9fX0="));

    @NotNull
    @Contract(value="-> new", pure=true)
    public static ProfileInstruction<ItemStack> createItem() {
        return XSkull.of(XMaterial.PLAYER_HEAD.parseItem());
    }

    @NotNull
    @Contract(value="_ -> new", pure=true)
    public static ProfileInstruction<ItemStack> of(@NotNull ItemStack itemStack) {
        return new ProfileInstruction<ItemStack>(new ProfileContainer.ItemStackProfileContainer(itemStack));
    }

    @NotNull
    @Contract(value="_ -> new", pure=true)
    public static ProfileInstruction<ItemMeta> of(@NotNull ItemMeta itemMeta) {
        return new ProfileInstruction<ItemMeta>(new ProfileContainer.ItemMetaProfileContainer((SkullMeta)itemMeta));
    }

    @NotNull
    @Contract(value="_ -> new", pure=true)
    public static ProfileInstruction<Block> of(@NotNull Block block) {
        return new ProfileInstruction<Block>(new ProfileContainer.BlockProfileContainer(block));
    }

    @NotNull
    @Contract(value="_ -> new", pure=true)
    public static ProfileInstruction<Skull> of(@NotNull BlockState blockState) {
        return new ProfileInstruction<Skull>(new ProfileContainer.BlockStateProfileContainer((Skull)blockState));
    }

    @NotNull
    @Contract(value="-> new", pure=true)
    protected static Profileable getDefaultProfile() {
        GameProfile gameProfile = PlayerProfiles.createGameProfile(DEFAULT_PROFILE.getId(), DEFAULT_PROFILE.getName());
        gameProfile.getProperties().putAll((Multimap)DEFAULT_PROFILE.getProperties());
        return Profileable.of(gameProfile);
    }
}

