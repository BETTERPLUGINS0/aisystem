package me.PM2.infinitevehicles.xseries.profiles.builder;

import me.PM2.infinitevehicles.xseries.XMaterial;
import me.PM2.infinitevehicles.xseries.profiles.PlayerProfiles;
import me.PM2.infinitevehicles.xseries.profiles.gameprofile.MojangGameProfile;
import me.PM2.infinitevehicles.xseries.profiles.objects.ProfileContainer;
import me.PM2.infinitevehicles.xseries.profiles.objects.ProfileInputType;
import me.PM2.infinitevehicles.xseries.profiles.objects.Profileable;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.block.Skull;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

public final class XSkull {
   private static final MojangGameProfile DEFAULT_PROFILE;

   @NotNull
   @Contract(
      value = "-> new",
      pure = true
   )
   public static ProfileInstruction<ItemStack> createItem() {
      return of(XMaterial.PLAYER_HEAD.parseItem());
   }

   @NotNull
   @Contract(
      value = "_ -> new",
      pure = true
   )
   public static ProfileInstruction<ItemStack> of(@NotNull ItemStack var0) {
      return new ProfileInstruction(new ProfileContainer.ItemStackProfileContainer(var0));
   }

   @NotNull
   @Contract(
      value = "_ -> new",
      pure = true
   )
   public static ProfileInstruction<ItemMeta> of(@NotNull ItemMeta var0) {
      return new ProfileInstruction(new ProfileContainer.ItemMetaProfileContainer((SkullMeta)var0));
   }

   @NotNull
   @Contract(
      value = "_ -> new",
      pure = true
   )
   public static ProfileInstruction<Block> of(@NotNull Block var0) {
      return new ProfileInstruction(new ProfileContainer.BlockProfileContainer(var0));
   }

   @NotNull
   @Contract(
      value = "_ -> new",
      pure = true
   )
   public static ProfileInstruction<Skull> of(@NotNull BlockState var0) {
      return new ProfileInstruction(new ProfileContainer.BlockStateProfileContainer((Skull)var0));
   }

   @NotNull
   @Contract(
      value = "-> new",
      pure = true
   )
   protected static Profileable getDefaultProfile() {
      return Profileable.of(DEFAULT_PROFILE.copy(), false);
   }

   static {
      DEFAULT_PROFILE = PlayerProfiles.signXSeries(ProfileInputType.BASE64.getProfile("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYzEwNTkxZTY5MDllNmEyODFiMzcxODM2ZTQ2MmQ2N2EyYzc4ZmEwOTUyZTkxMGYzMmI0MWEyNmM0OGMxNzU3YyJ9fX0="));
   }
}
