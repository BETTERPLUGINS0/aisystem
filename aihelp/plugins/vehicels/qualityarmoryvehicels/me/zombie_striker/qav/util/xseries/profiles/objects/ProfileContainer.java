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
 *  org.jetbrains.annotations.ApiStatus$Internal
 *  org.jetbrains.annotations.NotNull
 *  org.jetbrains.annotations.Nullable
 */
package me.zombie_striker.qav.util.xseries.profiles.objects;

import com.mojang.authlib.GameProfile;
import java.util.Objects;
import me.zombie_striker.qav.util.xseries.profiles.PlayerProfiles;
import me.zombie_striker.qav.util.xseries.profiles.ProfilesCore;
import me.zombie_striker.qav.util.xseries.profiles.exceptions.InvalidProfileContainerException;
import me.zombie_striker.qav.util.xseries.profiles.objects.DelegateProfileable;
import me.zombie_striker.qav.util.xseries.profiles.objects.Profileable;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.block.Skull;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@ApiStatus.Internal
public abstract class ProfileContainer<T>
implements Profileable {
    @NotNull
    public abstract void setProfile(@Nullable GameProfile var1);

    @NotNull
    public abstract T getObject();

    public final String toString() {
        return this.getClass().getSimpleName() + '[' + this.getObject() + ']';
    }

    public static final class BlockStateProfileContainer
    extends ProfileContainer<Skull> {
        private final Skull state;

        public BlockStateProfileContainer(Skull skull) {
            this.state = Objects.requireNonNull(skull, "Skull BlockState is null");
        }

        @Override
        public void setProfile(GameProfile gameProfile) {
            try {
                ProfilesCore.CraftSkull_profile$setter.invoke(this.state, PlayerProfiles.wrapProfile(gameProfile));
            } catch (Throwable throwable) {
                throw new IllegalStateException("Unable to set profile " + gameProfile + " to " + this.state, throwable);
            }
        }

        @Override
        public Skull getObject() {
            return this.state;
        }

        @Override
        public GameProfile getProfile() {
            try {
                return PlayerProfiles.unwrapProfile(ProfilesCore.CraftSkull_profile$getter.invoke(this.state));
            } catch (Throwable throwable) {
                throw new IllegalStateException("Unable to get profile fr om blockstate: " + this.state, throwable);
            }
        }
    }

    public static final class BlockProfileContainer
    extends ProfileContainer<Block>
    implements DelegateProfileable {
        private final Block block;

        public BlockProfileContainer(Block block) {
            this.block = Objects.requireNonNull(block, "Block is null");
        }

        private Skull getBlockState() {
            BlockState blockState = this.block.getState();
            if (!(blockState instanceof Skull)) {
                throw new InvalidProfileContainerException(this.block, "Block can't contain texture: " + this.block);
            }
            return (Skull)blockState;
        }

        @Override
        public void setProfile(GameProfile gameProfile) {
            Skull skull = this.getBlockState();
            new BlockStateProfileContainer(skull).setProfile(gameProfile);
            skull.update(true);
        }

        @Override
        public Block getObject() {
            return this.block;
        }

        @Override
        public Profileable getDelegateProfile() {
            return new BlockStateProfileContainer(this.getBlockState());
        }
    }

    public static final class ItemMetaProfileContainer
    extends ProfileContainer<ItemMeta> {
        private final ItemMeta meta;

        public ItemMetaProfileContainer(SkullMeta skullMeta) {
            this.meta = (ItemMeta)Objects.requireNonNull(skullMeta, "ItemMeta is null");
        }

        @Override
        public void setProfile(GameProfile gameProfile) {
            try {
                ProfilesCore.CraftMetaSkull_profile$setter.invoke(this.meta, PlayerProfiles.wrapProfile(gameProfile));
            } catch (Throwable throwable) {
                throw new IllegalStateException("Unable to set profile " + gameProfile + " to " + this.meta, throwable);
            }
        }

        @Override
        public ItemMeta getObject() {
            return this.meta;
        }

        @Override
        public GameProfile getProfile() {
            try {
                return PlayerProfiles.unwrapProfile(ProfilesCore.CraftMetaSkull_profile$getter.invoke((SkullMeta)this.meta));
            } catch (Throwable throwable) {
                throw new IllegalStateException("Failed to get profile from item meta: " + this.meta, throwable);
            }
        }
    }

    public static final class ItemStackProfileContainer
    extends ProfileContainer<ItemStack>
    implements DelegateProfileable {
        private final ItemStack itemStack;

        public ItemStackProfileContainer(ItemStack itemStack) {
            this.itemStack = Objects.requireNonNull(itemStack, "ItemStack is null");
        }

        private ItemMetaProfileContainer getMetaContainer(ItemMeta itemMeta) {
            if (!(itemMeta instanceof SkullMeta)) {
                throw new InvalidProfileContainerException(this.itemStack, "Item can't contain texture: " + this.itemStack);
            }
            return new ItemMetaProfileContainer((SkullMeta)itemMeta);
        }

        @Override
        public void setProfile(GameProfile gameProfile) {
            ItemMeta itemMeta = this.itemStack.getItemMeta();
            this.getMetaContainer(itemMeta).setProfile(gameProfile);
            this.itemStack.setItemMeta(itemMeta);
        }

        @Override
        public ItemStack getObject() {
            return this.itemStack;
        }

        @Override
        public Profileable getDelegateProfile() {
            return this.getMetaContainer(this.itemStack.getItemMeta());
        }
    }
}

