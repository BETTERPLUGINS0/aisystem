package me.PM2.infinitevehicles.xseries.profiles.objects;

import java.util.Objects;
import me.PM2.infinitevehicles.xseries.profiles.PlayerProfiles;
import me.PM2.infinitevehicles.xseries.profiles.ProfilesCore;
import me.PM2.infinitevehicles.xseries.profiles.exceptions.InvalidProfileContainerException;
import me.PM2.infinitevehicles.xseries.profiles.gameprofile.MojangGameProfile;
import me.PM2.infinitevehicles.xseries.profiles.gameprofile.XGameProfile;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.block.Skull;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.ApiStatus.Internal;

@Internal
public abstract class ProfileContainer<T> implements Profileable {
   @NotNull
   public abstract void setProfile(@Nullable MojangGameProfile var1);

   @NotNull
   public abstract T getObject();

   public boolean isReady() {
      return true;
   }

   public final String toString() {
      return this.getClass().getSimpleName() + '[' + this.getObject() + ']';
   }

   public static final class BlockStateProfileContainer extends ProfileContainer<Skull> {
      private final Skull state;

      public BlockStateProfileContainer(Skull var1) {
         this.state = (Skull)Objects.requireNonNull(var1, "Skull BlockState is null");
      }

      public void setProfile(@Nullable MojangGameProfile var1) {
         try {
            ProfilesCore.CraftSkull_profile$setter.invoke(this.state, PlayerProfiles.toResolvableProfile(var1));
         } catch (Throwable var3) {
            throw new IllegalStateException("Unable to set profile " + var1 + " to " + this.state, var3);
         }
      }

      public Skull getObject() {
         return this.state;
      }

      public MojangGameProfile getProfile() {
         try {
            return XGameProfile.of(PlayerProfiles.fromResolvableProfile(ProfilesCore.CraftSkull_profile$getter.invoke(this.state)));
         } catch (Throwable var2) {
            throw new IllegalStateException("Unable to get profile fr om blockstate: " + this.state, var2);
         }
      }
   }

   public static final class BlockProfileContainer extends ProfileContainer<Block> implements DelegateProfileable {
      private final Block block;

      public BlockProfileContainer(Block var1) {
         this.block = (Block)Objects.requireNonNull(var1, "Block is null");
      }

      private Skull getBlockState() {
         BlockState var1 = this.block.getState();
         if (!(var1 instanceof Skull)) {
            throw new InvalidProfileContainerException(this.block, "Block can't contain texture: " + this.block);
         } else {
            return (Skull)var1;
         }
      }

      public void setProfile(@Nullable MojangGameProfile var1) {
         Skull var2 = this.getBlockState();
         (new ProfileContainer.BlockStateProfileContainer(var2)).setProfile(var1);
         var2.update(true);
      }

      public Block getObject() {
         return this.block;
      }

      public Profileable getDelegateProfile() {
         return new ProfileContainer.BlockStateProfileContainer(this.getBlockState());
      }
   }

   public static final class ItemMetaProfileContainer extends ProfileContainer<ItemMeta> {
      private final ItemMeta meta;

      public ItemMetaProfileContainer(SkullMeta var1) {
         this.meta = (ItemMeta)Objects.requireNonNull(var1, "ItemMeta is null");
      }

      public void setProfile(@Nullable MojangGameProfile var1) {
         try {
            ProfilesCore.CraftMetaSkull_profile$setter.invoke(this.meta, PlayerProfiles.toResolvableProfile(var1));
         } catch (Throwable var3) {
            throw new IllegalStateException("Unable to set profile " + var1 + " to " + this.meta, var3);
         }
      }

      public ItemMeta getObject() {
         return this.meta;
      }

      public MojangGameProfile getProfile() {
         try {
            return XGameProfile.of(PlayerProfiles.fromResolvableProfile(ProfilesCore.CraftMetaSkull_profile$getter.invoke((SkullMeta)this.meta)));
         } catch (Throwable var2) {
            throw new IllegalStateException("Failed to get profile from item meta: " + this.meta, var2);
         }
      }
   }

   public static final class ItemStackProfileContainer extends ProfileContainer<ItemStack> implements DelegateProfileable {
      private final ItemStack itemStack;

      public ItemStackProfileContainer(ItemStack var1) {
         this.itemStack = (ItemStack)Objects.requireNonNull(var1, "ItemStack is null");
      }

      private ProfileContainer.ItemMetaProfileContainer getMetaContainer(ItemMeta var1) {
         if (!(var1 instanceof SkullMeta)) {
            throw new InvalidProfileContainerException(this.itemStack, "Item can't contain texture: " + this.itemStack);
         } else {
            return new ProfileContainer.ItemMetaProfileContainer((SkullMeta)var1);
         }
      }

      public void setProfile(@Nullable MojangGameProfile var1) {
         ItemMeta var2 = this.itemStack.getItemMeta();
         this.getMetaContainer(var2).setProfile(var1);
         this.itemStack.setItemMeta(var2);
      }

      public ItemStack getObject() {
         return this.itemStack;
      }

      public Profileable getDelegateProfile() {
         return this.getMetaContainer(this.itemStack.getItemMeta());
      }
   }
}
