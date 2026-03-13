package com.nisovin.shopkeepers.shopobjects.sign;

import com.nisovin.shopkeepers.util.java.Validate;
import java.util.function.Predicate;
import org.bukkit.Material;
import org.checkerframework.checker.nullness.qual.Nullable;

public enum SignType {
   OAK(Material.OAK_SIGN, Material.OAK_WALL_SIGN, Material.OAK_HANGING_SIGN, Material.OAK_WALL_HANGING_SIGN),
   SPRUCE(Material.SPRUCE_SIGN, Material.SPRUCE_WALL_SIGN, Material.SPRUCE_HANGING_SIGN, Material.SPRUCE_WALL_HANGING_SIGN),
   BIRCH(Material.BIRCH_SIGN, Material.BIRCH_WALL_SIGN, Material.BIRCH_HANGING_SIGN, Material.BIRCH_WALL_HANGING_SIGN),
   JUNGLE(Material.JUNGLE_SIGN, Material.JUNGLE_WALL_SIGN, Material.JUNGLE_HANGING_SIGN, Material.JUNGLE_WALL_HANGING_SIGN),
   ACACIA(Material.ACACIA_SIGN, Material.ACACIA_WALL_SIGN, Material.ACACIA_HANGING_SIGN, Material.ACACIA_WALL_HANGING_SIGN),
   DARK_OAK(Material.DARK_OAK_SIGN, Material.DARK_OAK_WALL_SIGN, Material.DARK_OAK_HANGING_SIGN, Material.DARK_OAK_WALL_HANGING_SIGN),
   CRIMSON(Material.CRIMSON_SIGN, Material.CRIMSON_WALL_SIGN, Material.CRIMSON_HANGING_SIGN, Material.CRIMSON_WALL_HANGING_SIGN),
   WARPED(Material.WARPED_SIGN, Material.WARPED_WALL_SIGN, Material.WARPED_HANGING_SIGN, Material.WARPED_WALL_HANGING_SIGN),
   MANGROVE(Material.MANGROVE_SIGN, Material.MANGROVE_WALL_SIGN, Material.MANGROVE_HANGING_SIGN, Material.MANGROVE_WALL_HANGING_SIGN),
   BAMBOO(Material.BAMBOO_SIGN, Material.BAMBOO_WALL_SIGN, Material.BAMBOO_HANGING_SIGN, Material.BAMBOO_WALL_HANGING_SIGN),
   CHERRY(Material.CHERRY_SIGN, Material.CHERRY_WALL_SIGN, Material.CHERRY_HANGING_SIGN, Material.CHERRY_WALL_HANGING_SIGN),
   PALE(Material.PALE_OAK_SIGN, Material.PALE_OAK_WALL_SIGN, Material.PALE_OAK_HANGING_SIGN, Material.PALE_OAK_WALL_HANGING_SIGN);

   public static final Predicate<SignType> IS_SUPPORTED = SignType::isSupported;
   public static final Predicate<SignType> IS_HANGING_SUPPORTED = SignType::isHangingSupported;
   @Nullable
   private final Material signMaterial;
   @Nullable
   private final Material wallSignMaterial;
   @Nullable
   private final Material hangingSignMaterial;
   @Nullable
   private final Material wallHangingSignMaterial;

   private SignType(@Nullable Material param3, @Nullable Material param4, @Nullable Material param5, @Nullable Material param6) {
      this.signMaterial = signMaterial;
      this.wallSignMaterial = wallSignMaterial;
      this.hangingSignMaterial = hangingSignMaterial;
      this.wallHangingSignMaterial = wallHangingSignMaterial;

      assert signMaterial != null ^ wallSignMaterial == null;

      assert hangingSignMaterial != null ^ wallHangingSignMaterial == null;

   }

   public boolean isSupported() {
      return this.signMaterial != null;
   }

   public boolean isHangingSupported() {
      return this.hangingSignMaterial != null;
   }

   @Nullable
   public Material getSignMaterial() {
      Validate.State.isTrue(this.isSupported(), "Unsupported sign type!");
      return this.signMaterial;
   }

   @Nullable
   public Material getWallSignMaterial() {
      Validate.State.isTrue(this.isSupported(), "Unsupported sign type!");
      return this.wallSignMaterial;
   }

   @Nullable
   public Material getSignMaterial(boolean wallSign) {
      return wallSign ? this.getWallSignMaterial() : this.getSignMaterial();
   }

   @Nullable
   public Material getHangingSignMaterial() {
      Validate.State.isTrue(this.isHangingSupported(), "Unsupported hanging sign type!");
      return this.hangingSignMaterial;
   }

   @Nullable
   public Material getWallHangingSignMaterial() {
      Validate.State.isTrue(this.isHangingSupported(), "Unsupported hanging sign type!");
      return this.wallHangingSignMaterial;
   }

   @Nullable
   public Material getHangingSignMaterial(boolean wallSign) {
      return wallSign ? this.getWallHangingSignMaterial() : this.getHangingSignMaterial();
   }

   // $FF: synthetic method
   private static SignType[] $values() {
      return new SignType[]{OAK, SPRUCE, BIRCH, JUNGLE, ACACIA, DARK_OAK, CRIMSON, WARPED, MANGROVE, BAMBOO, CHERRY, PALE};
   }
}
