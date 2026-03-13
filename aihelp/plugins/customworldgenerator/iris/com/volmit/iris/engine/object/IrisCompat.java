package com.volmit.iris.engine.object;

import com.google.gson.Gson;
import com.volmit.iris.Iris;
import com.volmit.iris.util.collection.KList;
import com.volmit.iris.util.data.B;
import com.volmit.iris.util.io.IO;
import com.volmit.iris.util.json.JSONObject;
import com.volmit.iris.util.scheduling.J;
import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import lombok.Generated;
import org.bukkit.Material;
import org.bukkit.block.data.BlockData;

public class IrisCompat {
   private KList<IrisCompatabilityBlockFilter> blockFilters = getDefaultBlockCompatabilityFilters();
   private KList<IrisCompatabilityItemFilter> itemFilters = getDefaultItemCompatabilityFilters();

   public static IrisCompat configured(File f) {
      IrisCompat var1 = new IrisCompat();
      String var2 = (new JSONObject((new Gson()).toJson(var1))).toString(4);
      J.attemptAsync(() -> {
         IO.writeAll(new File(var0.getParentFile(), "compat.default.json"), (Object)var2);
      });
      if (!var0.exists()) {
         J.a(() -> {
            try {
               IO.writeAll(var0, (Object)var2);
            } catch (IOException var3) {
               Iris.error("Failed to writeNodeData to compat file");
               Iris.reportError(var3);
            }

         });
      } else {
         try {
            IrisCompat var3 = (IrisCompat)(new Gson()).fromJson(IO.readAll(var0), IrisCompat.class);
            Iterator var4 = var3.getBlockFilters().iterator();

            while(var4.hasNext()) {
               IrisCompatabilityBlockFilter var5 = (IrisCompatabilityBlockFilter)var4.next();
               var1.getBlockFilters().add((Object)var5);
            }

            var4 = var3.getItemFilters().iterator();

            while(var4.hasNext()) {
               IrisCompatabilityItemFilter var7 = (IrisCompatabilityItemFilter)var4.next();
               var1.getItemFilters().add((Object)var7);
            }
         } catch (Throwable var6) {
            var6.printStackTrace();
            Iris.reportError(var6);
         }
      }

      return var1;
   }

   private static KList<IrisCompatabilityItemFilter> getDefaultItemCompatabilityFilters() {
      KList var0 = new KList();
      var0.add((Object)(new IrisCompatabilityItemFilter("NETHERITE_HELMET", "DIAMOND_HELMET")));
      var0.add((Object)(new IrisCompatabilityItemFilter("NETHERITE_CHESTPLATE", "DIAMOND_CHESTPLATE")));
      var0.add((Object)(new IrisCompatabilityItemFilter("NETHERITE_BOOTS", "DIAMOND_BOOTS")));
      var0.add((Object)(new IrisCompatabilityItemFilter("NETHERITE_LEGGINGS", "DIAMOND_LEGGINGS")));
      var0.add((Object)(new IrisCompatabilityItemFilter("MUSIC_DISC_PIGSTEP", "MUSIC_DISC_FAR")));
      var0.add((Object)(new IrisCompatabilityItemFilter("NETHERITE_SWORD", "DIAMOND_SWORD")));
      var0.add((Object)(new IrisCompatabilityItemFilter("NETHERITE_AXE", "DIAMOND_AXE")));
      var0.add((Object)(new IrisCompatabilityItemFilter("NETHERITE_PICKAXE", "DIAMOND_PICKAXE")));
      var0.add((Object)(new IrisCompatabilityItemFilter("NETHERITE_HOE", "DIAMOND_HOE")));
      var0.add((Object)(new IrisCompatabilityItemFilter("NETHERITE_SHOVEL", "DIAMOND_SHOVEL")));
      var0.add((Object)(new IrisCompatabilityItemFilter("NETHERITE_INGOT", "DIAMOND")));
      var0.add((Object)(new IrisCompatabilityItemFilter("PIGLIN_BANNER_PATTERN", "PORKCHOP")));
      var0.add((Object)(new IrisCompatabilityItemFilter("NETHERITE_SCRAP", "GOLD_INGOT")));
      var0.add((Object)(new IrisCompatabilityItemFilter("WARPED_FUNGUS_ON_A_STICK", "CARROT_ON_A_STICK")));
      var0.add((Object)(new IrisCompatabilityItemFilter("HONEY_BOTTLE", "GLASS_BOTTLE")));
      var0.add((Object)(new IrisCompatabilityItemFilter("HONEYCOMB", "GLASS")));
      var0.add((Object)(new IrisCompatabilityItemFilter("SWEET_BERRIES", "APPLE")));
      var0.add((Object)(new IrisCompatabilityItemFilter("SUSPICIOUS_STEW", "MUSHROOM_STEW")));
      var0.add((Object)(new IrisCompatabilityItemFilter("BLACK_DYE", "INK_SAC")));
      var0.add((Object)(new IrisCompatabilityItemFilter("WHITE_DYE", "BONE_MEAL")));
      var0.add((Object)(new IrisCompatabilityItemFilter("BROWN_DYE", "COCOA_BEANS")));
      var0.add((Object)(new IrisCompatabilityItemFilter("BLUE_DYE", "LAPIS_LAZULI")));
      var0.add((Object)(new IrisCompatabilityItemFilter("CROSSBOW", "BOW")));
      var0.add((Object)(new IrisCompatabilityItemFilter("FLOWER_BANNER_PATTERN", "CORNFLOWER")));
      var0.add((Object)(new IrisCompatabilityItemFilter("SKULL_BANNER_PATTERN", "BONE")));
      var0.add((Object)(new IrisCompatabilityItemFilter("GLOBE_BANNER_PATTERN", "WHEAT_SEEDS")));
      var0.add((Object)(new IrisCompatabilityItemFilter("MOJANG_BANNER_PATTERN", "DIRT")));
      var0.add((Object)(new IrisCompatabilityItemFilter("CREEPER_BANNER_PATTERN", "CREEPER_HEAD")));
      return var0;
   }

   private static KList<IrisCompatabilityBlockFilter> getDefaultBlockCompatabilityFilters() {
      KList var0 = new KList();
      var0.add((Object)(new IrisCompatabilityBlockFilter("CHAIN", "IRON_CHAIN")));
      var0.add((Object)(new IrisCompatabilityBlockFilter("GRASS", "SHORT_GRASS")));
      var0.add((Object)(new IrisCompatabilityBlockFilter("SHORT_GRASS", "GRASS")));
      var0.add((Object)(new IrisCompatabilityBlockFilter("WEEPING_VINES", "NETHER_FENCE")));
      var0.add((Object)(new IrisCompatabilityBlockFilter("WEEPING_VINES_PLANT", "NETHER_FENCE")));
      var0.add((Object)(new IrisCompatabilityBlockFilter("WARPED_WART_BLOCK", "NETHER_WART_BLOCK")));
      var0.add((Object)(new IrisCompatabilityBlockFilter("TWISTING_VINES", "BAMBOO")));
      var0.add((Object)(new IrisCompatabilityBlockFilter("TWISTING_VINES_PLANT", "BAMBOO")));
      var0.add((Object)(new IrisCompatabilityBlockFilter("TARGET", "COBBLESTONE")));
      var0.add((Object)(new IrisCompatabilityBlockFilter("SOUL_SOIL", "SOULSAND")));
      var0.add((Object)(new IrisCompatabilityBlockFilter("SOUL_TORCH", "TORCH")));
      var0.add((Object)(new IrisCompatabilityBlockFilter("SOUL_LANTERN", "LANTERN")));
      var0.add((Object)(new IrisCompatabilityBlockFilter("SOUL_FIRE", "FIRE")));
      var0.add((Object)(new IrisCompatabilityBlockFilter("SOUL_CAMPFIRE", "CAMPFIRE")));
      var0.add((Object)(new IrisCompatabilityBlockFilter("SHROOMLIGHT", "GLOWSTONE")));
      var0.add((Object)(new IrisCompatabilityBlockFilter("RESPAWN_ANCHOR", "OBSIDIAN")));
      var0.add((Object)(new IrisCompatabilityBlockFilter("NETHER_SPROUTS", "RED_MUSHROOM")));
      var0.add((Object)(new IrisCompatabilityBlockFilter("NETHER_GOLD_ORE", "GOLD_ORE")));
      var0.add((Object)(new IrisCompatabilityBlockFilter("LODESTONE", "STONE")));
      var0.add((Object)(new IrisCompatabilityBlockFilter("STRIPPED_WARPED_HYPHAE", "BROWN_MUSHROOM_BLOCK")));
      var0.add((Object)(new IrisCompatabilityBlockFilter("STRIPPED_CRIMSON_HYPHAE", "RED_MUSHROOM_BLOCK")));
      var0.add((Object)(new IrisCompatabilityBlockFilter("WARPED_HYPHAE", "MUSHROOM_STEM")));
      var0.add((Object)(new IrisCompatabilityBlockFilter("CRIMSON_HYPHAE", "RED_MUSHROOM_BLOCK")));
      var0.add((Object)(new IrisCompatabilityBlockFilter("GILDED_BLACKSTONE", "COBBLESTONE")));
      var0.add((Object)(new IrisCompatabilityBlockFilter("CRYING_OBSIDIAN", "OBSIDIAN")));
      var0.add((Object)(new IrisCompatabilityBlockFilter("STRIPPED_WARPED_STEM", "MUSHROOM_STEM")));
      var0.add((Object)(new IrisCompatabilityBlockFilter("STRIPPED_CRIMSON_STEM", "MUSHROOM_STEM")));
      var0.add((Object)(new IrisCompatabilityBlockFilter("WARPED_STEM", "MUSHROOM_STEM")));
      var0.add((Object)(new IrisCompatabilityBlockFilter("CRIMSON_STEM", "MUSHROOM_STEM")));
      var0.add((Object)(new IrisCompatabilityBlockFilter("CRIMSON_ROOTS", "RED_MUSHROOM")));
      var0.add((Object)(new IrisCompatabilityBlockFilter("WARPED_ROOTS", "BROWN_MUSHROOM")));
      var0.add((Object)(new IrisCompatabilityBlockFilter("CRIMSON_PLANKS", "OAK_PLANKS")));
      var0.add((Object)(new IrisCompatabilityBlockFilter("WARPED_PLANKS", "OAK_PLANKS")));
      var0.add((Object)(new IrisCompatabilityBlockFilter("WARPED_NYLIUM", "MYCELIUM")));
      var0.add((Object)(new IrisCompatabilityBlockFilter("CRIMSON_NYLIUM", "MYCELIUM")));
      var0.add((Object)(new IrisCompatabilityBlockFilter("WARPED_FUNGUS", "BROWN_MUSHROOM")));
      var0.add((Object)(new IrisCompatabilityBlockFilter("CRIMSON_FUNGUS", "RED_MUSHROOM")));
      var0.add((Object)(new IrisCompatabilityBlockFilter("CRACKED_NETHER_BRICKS", "NETHER_BRICKS")));
      var0.add((Object)(new IrisCompatabilityBlockFilter("CHISELED_NETHER_BRICKS", "NETHER_BRICKS")));
      var0.add((Object)(new IrisCompatabilityBlockFilter("NETHER_FENCE", "LEGACY_NETHER_FENCE")));
      var0.add((Object)(new IrisCompatabilityBlockFilter("IRON_CHAIN", "IRON_BARS")));
      var0.add((Object)(new IrisCompatabilityBlockFilter("NETHERITE_BLOCK", "QUARTZ_BLOCK")));
      var0.add((Object)(new IrisCompatabilityBlockFilter("BLACKSTONE", "COBBLESTONE")));
      var0.add((Object)(new IrisCompatabilityBlockFilter("BASALT", "STONE")));
      var0.add((Object)(new IrisCompatabilityBlockFilter("ANCIENT_DEBRIS", "NETHERRACK")));
      var0.add((Object)(new IrisCompatabilityBlockFilter("NETHERRACK", "LEGACY_NETHERRACK")));
      var0.add((Object)(new IrisCompatabilityBlockFilter("HONEY_BLOCK", "OAK_LEAVES")));
      var0.add((Object)(new IrisCompatabilityBlockFilter("BEEHIVE", "OAK_LEAVES")));
      var0.add((Object)(new IrisCompatabilityBlockFilter("BEE_NEST", "OAK_LEAVES")));
      var0.add((Object)(new IrisCompatabilityBlockFilter("GRANITE_WALL", "COBBLESTONE_WALL")));
      var0.add((Object)(new IrisCompatabilityBlockFilter("BLUE_ICE", "PACKED_ICE")));
      var0.add((Object)(new IrisCompatabilityBlockFilter("DIORITE_WALL", "COBBLESTONE_WALL")));
      var0.add((Object)(new IrisCompatabilityBlockFilter("ANDESITE_WALL", "COBBLESTONE_WALL")));
      var0.add((Object)(new IrisCompatabilityBlockFilter("SWEET_BERRY_BUSH", "GRASS")));
      var0.add((Object)(new IrisCompatabilityBlockFilter("STONECUTTER", "CRAFTING_TABLE")));
      var0.add((Object)(new IrisCompatabilityBlockFilter("SANDSTONE_STAIRS", "LEGACY_SANDSTONE_STAIRS")));
      var0.add((Object)(new IrisCompatabilityBlockFilter("SMOOTH_SANDSTONE_STAIRS", "LEGACY_SANDSTONE_STAIRS")));
      var0.add((Object)(new IrisCompatabilityBlockFilter("MOSSY_COBBLESTONE_STAIRS", "COBBLESTONE_STAIRS")));
      var0.add((Object)(new IrisCompatabilityBlockFilter("MOSSY_STONE_BRICK_STAIRS", "STONE_BRICK_STAIRS")));
      var0.add((Object)(new IrisCompatabilityBlockFilter("POLISHED_GRANITE_STAIRS", "COBBLESTONE_STAIRS")));
      var0.add((Object)(new IrisCompatabilityBlockFilter("GRANITE_STAIRS", "COBBLESTONE_STAIRS")));
      var0.add((Object)(new IrisCompatabilityBlockFilter("POLISHED_DIORITE_STAIRS", "COBBLESTONE_STAIRS")));
      var0.add((Object)(new IrisCompatabilityBlockFilter("DIORITE_STAIRS", "COBBLESTONE_STAIRS")));
      var0.add((Object)(new IrisCompatabilityBlockFilter("POLISHED_ANDESITE_STAIRS", "COBBLESTONE_STAIRS")));
      var0.add((Object)(new IrisCompatabilityBlockFilter("ANDESITE_STAIRS", "COBBLESTONE_STAIRS")));
      var0.add((Object)(new IrisCompatabilityBlockFilter("STONE_STAIRS", "COBBLESTONE_STAIRS")));
      var0.add((Object)(new IrisCompatabilityBlockFilter("END_STONE_BRICK_STAIRS", "LEGACY_SANDSTONE_STAIRS")));
      var0.add((Object)(new IrisCompatabilityBlockFilter("NETHER_BRICK_STAIRS", "LEGACY_NETHER_BRICK_STAIRS")));
      var0.add((Object)(new IrisCompatabilityBlockFilter("RED_NETHER_BRICK_STAIRS", "NETHER_BRICK_STAIRS")));
      var0.add((Object)(new IrisCompatabilityBlockFilter("SMOOTH_QUARTZ_STAIRS", "LEGACY_QUARTZ_STAIRS")));
      var0.add((Object)(new IrisCompatabilityBlockFilter("QUARTZ_STAIRS", "LEGACY_QUARTZ_STAIRS")));
      var0.add((Object)(new IrisCompatabilityBlockFilter("RED_SANDSTONE_STAIRS", "LEGACY_RED_SANDSTONE_STAIRS")));
      var0.add((Object)(new IrisCompatabilityBlockFilter("SMOOTH_RED_SANDSTONE_STAIRS", "LEGACY_RED_SANDSTONE_STAIRS")));
      var0.add((Object)(new IrisCompatabilityBlockFilter("STONE_SLAB", "SMOOTH_STONE_SLAB")));
      var0.add((Object)(new IrisCompatabilityBlockFilter("SMOKER", "FURNACE")));
      var0.add((Object)(new IrisCompatabilityBlockFilter("SMITHING_TABLE", "CRAFTING_TABLE")));
      var0.add((Object)(new IrisCompatabilityBlockFilter("END_STONE_BRICK_SLAB", "SANDSTONE_SLAB")));
      var0.add((Object)(new IrisCompatabilityBlockFilter("RED_NETHER_BRICK_SLAB", "NETHER_BRICK_SLAB")));
      var0.add((Object)(new IrisCompatabilityBlockFilter("SMOOTH_QUARTZ_SLAB", "QUARTZ_SLAB")));
      var0.add((Object)(new IrisCompatabilityBlockFilter("CUT_SANDSTONE_SLAB", "SANDSTONE_SLAB")));
      var0.add((Object)(new IrisCompatabilityBlockFilter("CUT_RED_SANDSTONE_SLAB", "RED_SANDSTONE_SLAB")));
      var0.add((Object)(new IrisCompatabilityBlockFilter("SMOOTH_RED_SANDSTONE_SLAB", "RED_SANDSTONE_SLAB")));
      var0.add((Object)(new IrisCompatabilityBlockFilter("SMOOTH_SANDSTONE_SLAB", "SANDSTONE_SLAB")));
      var0.add((Object)(new IrisCompatabilityBlockFilter("MOSSY_COBBLESTONE_SLAB", "COBBLESTONE_SLAB")));
      var0.add((Object)(new IrisCompatabilityBlockFilter("MOSSY_STONE_BRICK_SLAB", "STONE_BRICK_SLAB")));
      var0.add((Object)(new IrisCompatabilityBlockFilter("STONE_SLAB", "SMOOTH_STONE_SLAB")));
      var0.add((Object)(new IrisCompatabilityBlockFilter("ANDESITE_SLAB", "COBBLESTONE_SLAB")));
      var0.add((Object)(new IrisCompatabilityBlockFilter("ANDESITE_SLAB", "COBBLESTONE_SLAB")));
      var0.add((Object)(new IrisCompatabilityBlockFilter("DIORITE_SLAB", "COBBLESTONE_SLAB")));
      var0.add((Object)(new IrisCompatabilityBlockFilter("GRANITE_SLAB", "COBBLESTONE_SLAB")));
      var0.add((Object)(new IrisCompatabilityBlockFilter("POLISHED_ANDESITE_SLAB", "SMOOTH_STONE_SLAB")));
      var0.add((Object)(new IrisCompatabilityBlockFilter("POLISHED_DIORITE_SLAB", "SMOOTH_STONE_SLAB")));
      var0.add((Object)(new IrisCompatabilityBlockFilter("POLISHED_GRANITE_SLAB", "SMOOTH_STONE_SLAB")));
      var0.add((Object)(new IrisCompatabilityBlockFilter("WARPED_WALL_SIGN", "LEGACY_WALL_SIGN")));
      var0.add((Object)(new IrisCompatabilityBlockFilter("WARPED_SIGN", "LEGACY_SIGN_POST")));
      var0.add((Object)(new IrisCompatabilityBlockFilter("SPRUCE_WALL_SIGN", "LEGACY_WALL_SIGN")));
      var0.add((Object)(new IrisCompatabilityBlockFilter("SPRUCE_SIGN", "LEGACY_SIGN_POST")));
      var0.add((Object)(new IrisCompatabilityBlockFilter("OAK_WALL_SIGN", "LEGACY_WALL_SIGN")));
      var0.add((Object)(new IrisCompatabilityBlockFilter("OAK_SIGN", "LEGACY_SIGN_POST")));
      var0.add((Object)(new IrisCompatabilityBlockFilter("JUNGLE_WALL_SIGN", "LEGACY_WALL_SIGN")));
      var0.add((Object)(new IrisCompatabilityBlockFilter("JUNGLE_SIGN", "LEGACY_SIGN_POST")));
      var0.add((Object)(new IrisCompatabilityBlockFilter("DARK_OAK_WALL_SIGN", "LEGACY_WALL_SIGN")));
      var0.add((Object)(new IrisCompatabilityBlockFilter("DARK_OAK_SIGN", "LEGACY_SIGN_POST")));
      var0.add((Object)(new IrisCompatabilityBlockFilter("CRIMSON_WALL_SIGN", "LEGACY_WALL_SIGN")));
      var0.add((Object)(new IrisCompatabilityBlockFilter("CRIMSON_SIGN", "LEGACY_SIGN_POST")));
      var0.add((Object)(new IrisCompatabilityBlockFilter("BIRCH_WALL_SIGN", "LEGACY_WALL_SIGN")));
      var0.add((Object)(new IrisCompatabilityBlockFilter("BIRCH_SIGN", "LEGACY_SIGN_POST")));
      var0.add((Object)(new IrisCompatabilityBlockFilter("ACACIA_WALL_SIGN", "LEGACY_WALL_SIGN")));
      var0.add((Object)(new IrisCompatabilityBlockFilter("ACACIA_SIGN", "LEGACY_SIGN_POST")));
      var0.add((Object)(new IrisCompatabilityBlockFilter("SCAFFOLDING", "BIRCH_FENCE")));
      var0.add((Object)(new IrisCompatabilityBlockFilter("LECTERN", "BOOKSHELF")));
      var0.add((Object)(new IrisCompatabilityBlockFilter("LANTERN", "REDSTONE_LAMP")));
      var0.add((Object)(new IrisCompatabilityBlockFilter("JIGSAW", "AIR")));
      var0.add((Object)(new IrisCompatabilityBlockFilter("GRINDSTONE", "COBBLESTONE")));
      var0.add((Object)(new IrisCompatabilityBlockFilter("FLETCHING_TABLE", "CRAFTING_TABLE")));
      var0.add((Object)(new IrisCompatabilityBlockFilter("COMPOSTER", "CHEST")));
      var0.add((Object)(new IrisCompatabilityBlockFilter("CARTOGRAPHY_TABLE", "CRAFTING_TABLE")));
      var0.add((Object)(new IrisCompatabilityBlockFilter("CAMPFIRE", "DARK_OAK_SLAB")));
      var0.add((Object)(new IrisCompatabilityBlockFilter("BLAST_FURNACE", "FURNACE")));
      var0.add((Object)(new IrisCompatabilityBlockFilter("BELL", "REDSTONE_LAMP")));
      var0.add((Object)(new IrisCompatabilityBlockFilter("minecraft:barrel[facing=south]", "minecraft:hay_bale[axis=z]", true)));
      var0.add((Object)(new IrisCompatabilityBlockFilter("minecraft:barrel[facing=north]", "minecraft:hay_bale[axis=z]", true)));
      var0.add((Object)(new IrisCompatabilityBlockFilter("minecraft:barrel[facing=east]", "minecraft:hay_bale[axis=x]", true)));
      var0.add((Object)(new IrisCompatabilityBlockFilter("minecraft:barrel[facing=west]", "minecraft:hay_bale[axis=x]", true)));
      var0.add((Object)(new IrisCompatabilityBlockFilter("minecraft:barrel[facing=up]", "minecraft:hay_bale[axis=y]", true)));
      var0.add((Object)(new IrisCompatabilityBlockFilter("minecraft:barrel[facing=down]", "minecraft:hay_bale[axis=y]", true)));
      var0.add((Object)(new IrisCompatabilityBlockFilter("BAMBOO", "BIRCH_FENCE")));
      var0.add((Object)(new IrisCompatabilityBlockFilter("BAMBOO_SAPLING", "BIRCH_SAPLING")));
      var0.add((Object)(new IrisCompatabilityBlockFilter("POTTED_BAMBOO", "POTTED_BIRCH_SAPLING")));
      return var0;
   }

   public BlockData getBlock(String n) {
      String var2 = var1;
      int var3 = 16;
      BlockData var4 = B.getOrNull(var1, false);
      if (var4 != null) {
         return var4;
      } else {
         while(var3-- > 0) {
            String var5 = var2;
            if (var2.contains("[")) {
               var5 = var2.split("\\Q[\\E")[0];
            }

            if (var5.contains(":")) {
               var5 = var5.split("\\Q:\\E", 2)[1];
            }

            Iterator var6 = this.blockFilters.iterator();

            IrisCompatabilityBlockFilter var7;
            do {
               if (!var6.hasNext()) {
                  Iris.error("Can't find block data for " + var1);
                  return B.getNoCompat("STONE");
               }

               var7 = (IrisCompatabilityBlockFilter)var6.next();
            } while(!var7.getWhen().equalsIgnoreCase(var7.isExact() ? var2 : var5));

            BlockData var8 = var7.getReplace();
            if (var8 != null) {
               return var8;
            }

            var2 = var7.getSupplement();
         }

         Iris.error("Can't find block data for " + var1);
         return B.getNoCompat("STONE");
      }
   }

   public Material getItem(String n) {
      String var2 = var1;
      int var3 = 16;
      Material var4 = B.getMaterialOrNull(var1);
      if (var4 != null) {
         return var4;
      } else {
         int var5 = 64;

         label65:
         while(true) {
            if (var5 < 0) {
               return B.getMaterial("STONE");
            }

            --var5;
            if (var3-- <= 0) {
               break;
            }

            Iterator var6 = this.itemFilters.iterator();

            IrisCompatabilityItemFilter var7;
            do {
               if (!var6.hasNext()) {
                  break label65;
               }

               var7 = (IrisCompatabilityItemFilter)var6.next();
            } while(!var7.getWhen().equalsIgnoreCase(var2));

            Material var8 = var7.getReplace();
            if (var8 != null) {
               return var8;
            }

            var2 = var7.getSupplement();
         }

         var2 = var1;
         BlockData var10 = B.getOrNull(var1, false);
         if (var10 != null) {
            return var10.getMaterial();
         } else {
            IrisCompatabilityBlockFilter var12;
            for(var5 = 64; var5 >= 0; var2 = var12.getSupplement()) {
               --var5;
               if (var3-- <= 0) {
                  return B.getMaterial("STONE");
               }

               Iterator var11 = this.blockFilters.iterator();

               do {
                  if (!var11.hasNext()) {
                     return B.getMaterial("STONE");
                  }

                  var12 = (IrisCompatabilityBlockFilter)var11.next();
               } while(!var12.getWhen().equalsIgnoreCase(var2));

               BlockData var9 = var12.getReplace();
               if (var9 != null) {
                  return var9.getMaterial();
               }
            }

            return B.getMaterial("STONE");
         }
      }
   }

   @Generated
   public KList<IrisCompatabilityBlockFilter> getBlockFilters() {
      return this.blockFilters;
   }

   @Generated
   public KList<IrisCompatabilityItemFilter> getItemFilters() {
      return this.itemFilters;
   }

   @Generated
   public void setBlockFilters(final KList<IrisCompatabilityBlockFilter> blockFilters) {
      this.blockFilters = var1;
   }

   @Generated
   public void setItemFilters(final KList<IrisCompatabilityItemFilter> itemFilters) {
      this.itemFilters = var1;
   }

   @Generated
   public boolean equals(final Object o) {
      if (var1 == this) {
         return true;
      } else if (!(var1 instanceof IrisCompat)) {
         return false;
      } else {
         IrisCompat var2 = (IrisCompat)var1;
         if (!var2.canEqual(this)) {
            return false;
         } else {
            KList var3 = this.getBlockFilters();
            KList var4 = var2.getBlockFilters();
            if (var3 == null) {
               if (var4 != null) {
                  return false;
               }
            } else if (!var3.equals(var4)) {
               return false;
            }

            KList var5 = this.getItemFilters();
            KList var6 = var2.getItemFilters();
            if (var5 == null) {
               if (var6 != null) {
                  return false;
               }
            } else if (!var5.equals(var6)) {
               return false;
            }

            return true;
         }
      }
   }

   @Generated
   protected boolean canEqual(final Object other) {
      return var1 instanceof IrisCompat;
   }

   @Generated
   public int hashCode() {
      boolean var1 = true;
      byte var2 = 1;
      KList var3 = this.getBlockFilters();
      int var5 = var2 * 59 + (var3 == null ? 43 : var3.hashCode());
      KList var4 = this.getItemFilters();
      var5 = var5 * 59 + (var4 == null ? 43 : var4.hashCode());
      return var5;
   }

   @Generated
   public String toString() {
      String var10000 = String.valueOf(this.getBlockFilters());
      return "IrisCompat(blockFilters=" + var10000 + ", itemFilters=" + String.valueOf(this.getItemFilters()) + ")";
   }
}
