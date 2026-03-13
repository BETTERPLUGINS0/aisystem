package com.volmit.iris.util.data;

import com.volmit.iris.Iris;
import com.volmit.iris.core.IrisSettings;
import com.volmit.iris.core.link.Identifier;
import com.volmit.iris.core.link.data.DataType;
import com.volmit.iris.core.nms.INMS;
import com.volmit.iris.core.nms.container.BlockProperty;
import com.volmit.iris.core.nms.container.Pair;
import com.volmit.iris.core.service.ExternalDataSVC;
import com.volmit.iris.util.collection.KList;
import com.volmit.iris.util.collection.KMap;
import com.volmit.iris.util.data.registry.Materials;
import com.volmit.iris.util.scheduling.ChronoLatch;
import it.unimi.dsi.fastutil.ints.Int2IntMap;
import it.unimi.dsi.fastutil.ints.Int2IntOpenHashMap;
import it.unimi.dsi.fastutil.ints.IntOpenHashSet;
import it.unimi.dsi.fastutil.ints.IntSet;
import it.unimi.dsi.fastutil.ints.IntSets;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.data.BlockData;
import org.bukkit.block.data.Waterlogged;
import org.bukkit.block.data.type.Leaves;
import org.bukkit.block.data.type.PointedDripstone;
import org.bukkit.block.data.type.PointedDripstone.Thickness;

public class B {
   private static final KMap<String, BlockData> custom = new KMap();
   private static final Material AIR_MATERIAL;
   private static final Material SHORT_GRASS;
   private static final BlockData AIR;
   private static final IntSet foliageCache;
   private static final IntSet deepslateCache;
   private static final Int2IntMap normal2DeepslateCache;
   private static final Int2IntMap deepslate2NormalCache;
   private static final IntSet decorantCache;
   private static final IntSet storageCache;
   private static final IntSet storageChestCache;
   private static final IntSet litCache;
   private static final ChronoLatch clw;

   private static IntSet buildFoliageCache() {
      IntOpenHashSet var0 = new IntOpenHashSet();
      Arrays.stream(new Material[]{Material.POPPY, Material.DANDELION, Material.CORNFLOWER, Material.SWEET_BERRY_BUSH, Material.CRIMSON_ROOTS, Material.WARPED_ROOTS, Material.NETHER_SPROUTS, Material.ALLIUM, Material.AZURE_BLUET, Material.BLUE_ORCHID, Material.OXEYE_DAISY, Material.LILY_OF_THE_VALLEY, Material.WITHER_ROSE, Material.DARK_OAK_SAPLING, Material.ACACIA_SAPLING, Material.JUNGLE_SAPLING, Material.BIRCH_SAPLING, Material.SPRUCE_SAPLING, Material.OAK_SAPLING, Material.ORANGE_TULIP, Material.PINK_TULIP, Material.RED_TULIP, Material.WHITE_TULIP, Material.FERN, Material.LARGE_FERN, SHORT_GRASS, Material.TALL_GRASS}).forEach((var1) -> {
         var0.add(var1.ordinal());
      });
      return IntSets.unmodifiable(var0);
   }

   private static IntSet buildDeepslateCache() {
      IntOpenHashSet var0 = new IntOpenHashSet();
      Arrays.stream(new Material[]{Material.DEEPSLATE, Material.DEEPSLATE_BRICKS, Material.DEEPSLATE_BRICK_SLAB, Material.DEEPSLATE_BRICK_STAIRS, Material.DEEPSLATE_BRICK_WALL, Material.DEEPSLATE_TILE_SLAB, Material.DEEPSLATE_TILES, Material.DEEPSLATE_TILE_STAIRS, Material.DEEPSLATE_TILE_WALL, Material.CRACKED_DEEPSLATE_TILES, Material.DEEPSLATE_COAL_ORE, Material.DEEPSLATE_IRON_ORE, Material.DEEPSLATE_COPPER_ORE, Material.DEEPSLATE_DIAMOND_ORE, Material.DEEPSLATE_EMERALD_ORE, Material.DEEPSLATE_GOLD_ORE, Material.DEEPSLATE_LAPIS_ORE, Material.DEEPSLATE_REDSTONE_ORE}).forEach((var1) -> {
         var0.add(var1.ordinal());
      });
      return IntSets.unmodifiable(var0);
   }

   private static Int2IntMap buildNormal2DeepslateCache() {
      Int2IntOpenHashMap var0 = new Int2IntOpenHashMap();
      var0.put(Material.COAL_ORE.ordinal(), Material.DEEPSLATE_COAL_ORE.ordinal());
      var0.put(Material.EMERALD_ORE.ordinal(), Material.DEEPSLATE_EMERALD_ORE.ordinal());
      var0.put(Material.DIAMOND_ORE.ordinal(), Material.DEEPSLATE_DIAMOND_ORE.ordinal());
      var0.put(Material.COPPER_ORE.ordinal(), Material.DEEPSLATE_COPPER_ORE.ordinal());
      var0.put(Material.GOLD_ORE.ordinal(), Material.DEEPSLATE_GOLD_ORE.ordinal());
      var0.put(Material.IRON_ORE.ordinal(), Material.DEEPSLATE_IRON_ORE.ordinal());
      var0.put(Material.LAPIS_ORE.ordinal(), Material.DEEPSLATE_LAPIS_ORE.ordinal());
      var0.put(Material.REDSTONE_ORE.ordinal(), Material.DEEPSLATE_REDSTONE_ORE.ordinal());
      return var0;
   }

   private static Int2IntMap buildDeepslate2NormalCache() {
      Int2IntOpenHashMap var0 = new Int2IntOpenHashMap();
      var0.put(Material.DEEPSLATE_COAL_ORE.ordinal(), Material.COAL_ORE.ordinal());
      var0.put(Material.DEEPSLATE_EMERALD_ORE.ordinal(), Material.EMERALD_ORE.ordinal());
      var0.put(Material.DEEPSLATE_DIAMOND_ORE.ordinal(), Material.DIAMOND_ORE.ordinal());
      var0.put(Material.DEEPSLATE_COPPER_ORE.ordinal(), Material.COPPER_ORE.ordinal());
      var0.put(Material.DEEPSLATE_GOLD_ORE.ordinal(), Material.GOLD_ORE.ordinal());
      var0.put(Material.DEEPSLATE_IRON_ORE.ordinal(), Material.IRON_ORE.ordinal());
      var0.put(Material.DEEPSLATE_LAPIS_ORE.ordinal(), Material.LAPIS_ORE.ordinal());
      var0.put(Material.DEEPSLATE_REDSTONE_ORE.ordinal(), Material.REDSTONE_ORE.ordinal());
      return var0;
   }

   private static IntSet buildDecorantCache() {
      IntOpenHashSet var0 = new IntOpenHashSet();
      Arrays.stream(new Material[]{SHORT_GRASS, Material.TALL_GRASS, Material.TALL_SEAGRASS, Material.FERN, Material.LARGE_FERN, Material.CORNFLOWER, Material.SUNFLOWER, Material.CHORUS_FLOWER, Material.POPPY, Material.DANDELION, Material.OXEYE_DAISY, Material.ORANGE_TULIP, Material.PINK_TULIP, Material.RED_TULIP, Material.WHITE_TULIP, Material.LILAC, Material.DEAD_BUSH, Material.SWEET_BERRY_BUSH, Material.ROSE_BUSH, Material.WITHER_ROSE, Material.ALLIUM, Material.BLUE_ORCHID, Material.LILY_OF_THE_VALLEY, Material.CRIMSON_FUNGUS, Material.WARPED_FUNGUS, Material.RED_MUSHROOM, Material.BROWN_MUSHROOM, Material.CRIMSON_ROOTS, Material.AZURE_BLUET, Material.WEEPING_VINES, Material.WEEPING_VINES_PLANT, Material.WARPED_ROOTS, Material.NETHER_SPROUTS, Material.TWISTING_VINES, Material.TWISTING_VINES_PLANT, Material.SUGAR_CANE, Material.WHEAT, Material.POTATOES, Material.CARROTS, Material.BEETROOTS, Material.NETHER_WART, Material.SEA_PICKLE, Material.SEAGRASS, Material.ACACIA_BUTTON, Material.BIRCH_BUTTON, Material.CRIMSON_BUTTON, Material.DARK_OAK_BUTTON, Material.JUNGLE_BUTTON, Material.OAK_BUTTON, Material.POLISHED_BLACKSTONE_BUTTON, Material.SPRUCE_BUTTON, Material.STONE_BUTTON, Material.WARPED_BUTTON, Material.TORCH, Material.SOUL_TORCH, Material.GLOW_LICHEN, Material.VINE, Material.SCULK_VEIN}).forEach((var1) -> {
         var0.add(var1.ordinal());
      });
      var0.addAll(foliageCache);
      return IntSets.unmodifiable(var0);
   }

   private static IntSet buildLitCache() {
      IntOpenHashSet var0 = new IntOpenHashSet();
      Arrays.stream(new Material[]{Material.GLOWSTONE, Material.AMETHYST_CLUSTER, Material.SMALL_AMETHYST_BUD, Material.MEDIUM_AMETHYST_BUD, Material.LARGE_AMETHYST_BUD, Material.END_ROD, Material.SOUL_SAND, Material.TORCH, Material.REDSTONE_TORCH, Material.SOUL_TORCH, Material.REDSTONE_WALL_TORCH, Material.WALL_TORCH, Material.SOUL_WALL_TORCH, Material.LANTERN, Material.CANDLE, Material.JACK_O_LANTERN, Material.REDSTONE_LAMP, Material.MAGMA_BLOCK, Material.LIGHT, Material.SHROOMLIGHT, Material.SEA_LANTERN, Material.SOUL_LANTERN, Material.FIRE, Material.SOUL_FIRE, Material.SEA_PICKLE, Material.BREWING_STAND, Material.REDSTONE_ORE}).forEach((var1) -> {
         var0.add(var1.ordinal());
      });
      return IntSets.unmodifiable(var0);
   }

   private static IntSet buildStorageCache() {
      IntOpenHashSet var0 = new IntOpenHashSet();
      Arrays.stream(new Material[]{Material.CHEST, Material.SMOKER, Material.TRAPPED_CHEST, Material.SHULKER_BOX, Material.WHITE_SHULKER_BOX, Material.ORANGE_SHULKER_BOX, Material.MAGENTA_SHULKER_BOX, Material.LIGHT_BLUE_SHULKER_BOX, Material.YELLOW_SHULKER_BOX, Material.LIME_SHULKER_BOX, Material.PINK_SHULKER_BOX, Material.GRAY_SHULKER_BOX, Material.LIGHT_GRAY_SHULKER_BOX, Material.CYAN_SHULKER_BOX, Material.PURPLE_SHULKER_BOX, Material.BLUE_SHULKER_BOX, Material.BROWN_SHULKER_BOX, Material.GREEN_SHULKER_BOX, Material.RED_SHULKER_BOX, Material.BLACK_SHULKER_BOX, Material.BARREL, Material.DISPENSER, Material.DROPPER, Material.HOPPER, Material.FURNACE, Material.BLAST_FURNACE}).forEach((var1) -> {
         var0.add(var1.ordinal());
      });
      return IntSets.unmodifiable(var0);
   }

   public static BlockData toDeepSlateOre(BlockData block, BlockData ore) {
      int var2 = var1.getMaterial().ordinal();
      if (isDeepSlate(var0)) {
         if (normal2DeepslateCache.containsKey(var2)) {
            return Material.values()[normal2DeepslateCache.get(var2)].createBlockData();
         }
      } else if (deepslate2NormalCache.containsKey(var2)) {
         return Material.values()[deepslate2NormalCache.get(var2)].createBlockData();
      }

      return var1;
   }

   public static boolean isDeepSlate(BlockData blockData) {
      return deepslateCache.contains(var0.getMaterial().ordinal());
   }

   public static boolean isOre(BlockData blockData) {
      return var0.getMaterial().name().endsWith("_ORE");
   }

   private static IntSet buildStorageChestCache() {
      IntOpenHashSet var0 = new IntOpenHashSet(storageCache);
      var0.remove(Material.SMOKER.ordinal());
      var0.remove(Material.FURNACE.ordinal());
      var0.remove(Material.BLAST_FURNACE.ordinal());
      return IntSets.unmodifiable(var0);
   }

   public static boolean canPlaceOnto(Material mat, Material onto) {
      if ((var1.equals(Material.CRIMSON_NYLIUM) || var1.equals(Material.WARPED_NYLIUM)) && (var0.equals(Material.CRIMSON_FUNGUS) || var0.equals(Material.CRIMSON_ROOTS) || var0.equals(Material.WARPED_FUNGUS) || var0.equals(Material.WARPED_ROOTS))) {
         return true;
      } else if (isFoliage(var0) && !isFoliagePlantable(var1)) {
         return false;
      } else if (!var1.equals(Material.AIR) && !var1.equals(getMaterial("CAVE_AIR")) && !var1.equals(getMaterial("VOID_AIR"))) {
         if (var1.equals(Material.GRASS_BLOCK) && var0.equals(Material.DEAD_BUSH)) {
            return false;
         } else if (var1.equals(Material.DIRT_PATH) && !var0.isSolid()) {
            return false;
         } else {
            return !var1.equals(Material.ACACIA_LEAVES) && !var1.equals(Material.BIRCH_LEAVES) && !var1.equals(Material.DARK_OAK_LEAVES) && !var1.equals(Material.JUNGLE_LEAVES) && !var1.equals(Material.OAK_LEAVES) && !var1.equals(Material.SPRUCE_LEAVES) ? true : var0.isSolid();
         }
      } else {
         return false;
      }
   }

   public static boolean isFoliagePlantable(BlockData d) {
      return var0.getMaterial().equals(Material.GRASS_BLOCK) || var0.getMaterial().equals(Material.MOSS_BLOCK) || var0.getMaterial().equals(Material.ROOTED_DIRT) || var0.getMaterial().equals(Material.DIRT) || var0.getMaterial().equals(Material.COARSE_DIRT) || var0.getMaterial().equals(Material.PODZOL);
   }

   public static boolean isFoliagePlantable(Material d) {
      return var0.equals(Material.GRASS_BLOCK) || var0.equals(Material.MOSS_BLOCK) || var0.equals(Material.DIRT) || var0.equals(Material.TALL_GRASS) || var0.equals(Material.TALL_SEAGRASS) || var0.equals(Material.LARGE_FERN) || var0.equals(Material.SUNFLOWER) || var0.equals(Material.PEONY) || var0.equals(Material.LILAC) || var0.equals(Material.ROSE_BUSH) || var0.equals(Material.ROOTED_DIRT) || var0.equals(Material.COARSE_DIRT) || var0.equals(Material.PODZOL);
   }

   public static boolean isWater(BlockData b) {
      return var0.getMaterial().equals(Material.WATER);
   }

   public static BlockData getAir() {
      return AIR;
   }

   public static Material getMaterialOrNull(String bdx) {
      try {
         return Material.valueOf(var0.trim().toUpperCase());
      } catch (Throwable var2) {
         Iris.reportError(var2);
         if (clw.flip()) {
            Iris.warn("Unknown Material: " + var0);
         }

         return null;
      }
   }

   public static Material getMaterial(String bdx) {
      Material var1 = getMaterialOrNull(var0);
      return var1 == null ? AIR_MATERIAL : var1;
   }

   public static boolean isSolid(BlockData mat) {
      return var0 == null ? false : var0.getMaterial().isSolid();
   }

   public static BlockData getOrNull(String bdxf, boolean warn) {
      try {
         String var2 = var0.trim();
         if (!custom.isEmpty() && custom.containsKey(var2)) {
            return (BlockData)custom.get(var2);
         } else {
            if (var2.startsWith("minecraft:cauldron[level=")) {
               var2 = var2.replaceAll("\\Q:cauldron[\\E", ":water_cauldron[");
            }

            if (var2.equals("minecraft:grass_path")) {
               return Material.DIRT_PATH.createBlockData();
            } else {
               BlockData var3 = parseBlockData(var2, var1);
               if (var3 == null && var1) {
                  if (clw.flip()) {
                     Iris.warn("Unknown Block Data '" + var2 + "'");
                  }

                  return AIR;
               } else {
                  return var3;
               }
            }
         }
      } catch (Throwable var4) {
         Iris.reportError(var4);
         if (clw.flip()) {
            Iris.warn("Unknown Block Data '" + var0 + "'");
         }

         return null;
      }
   }

   public static BlockData getNoCompat(String bdxf) {
      BlockData var1 = getOrNull(var0, true);
      return var1 != null ? var1 : AIR;
   }

   public static BlockData get(String bdxf) {
      if (var0.contains(":")) {
         return var0.startsWith("minecraft:") ? Iris.compat.getBlock(var0) : getNoCompat(var0);
      } else {
         return Iris.compat.getBlock(var0);
      }
   }

   private static synchronized BlockData createBlockData(String s, boolean warn) {
      try {
         return Bukkit.createBlockData(var0);
      } catch (IllegalArgumentException var3) {
         if (var0.contains("[")) {
            return createBlockData(var0.split("\\Q[\\E")[0], var1);
         } else {
            if (var1) {
               Iris.error("Can't find block data for " + var0);
            }

            return null;
         }
      }
   }

   private static BlockData parseBlockData(String ix, boolean warn) {
      try {
         BlockData var2 = null;
         if (!var0.startsWith("minecraft:") && var0.contains(":")) {
            Identifier var18 = Identifier.fromString(var0);
            Optional var17 = ((ExternalDataSVC)Iris.service(ExternalDataSVC.class)).getBlockData(var18);
            Iris.debug("Loading block data " + String.valueOf(var18));
            if (var17.isPresent()) {
               var2 = (BlockData)var17.get();
            }
         }

         if (var2 == null) {
            try {
               var2 = createBlockData(var0.toLowerCase(), var1);
            } catch (Throwable var15) {
               var15.printStackTrace();
            }
         }

         if (var2 == null) {
            try {
               var2 = createBlockData("minecraft:" + var0.toLowerCase(), var1);
            } catch (Throwable var14) {
            }
         }

         if (var2 == null) {
            try {
               var2 = Material.valueOf(var0.toUpperCase()).createBlockData();
            } catch (Throwable var13) {
            }
         }

         if (var2 == null) {
            return null;
         } else {
            if (var2 instanceof Leaves && IrisSettings.get().getGenerator().isPreventLeafDecay()) {
               ((Leaves)var2).setPersistent(true);
            } else if (var2 instanceof Leaves) {
               ((Leaves)var2).setPersistent(false);
            }

            return var2;
         }
      } catch (Throwable var16) {
         if (clw.flip()) {
            Iris.warn("Unknown Block Data: " + var0);
         }

         String var3 = var0.contains(":") ? var0.split(":")[1].toLowerCase() : var0.toLowerCase();
         String var4 = var3.contains("[") ? var3.split("\\Q[\\E")[1].split("\\Q]\\E")[0] : "";
         HashMap var5 = new HashMap();
         if (!var4.equals("")) {
            Arrays.stream(var4.split(",")).forEach((var1x) -> {
               var5.put(var1x.split("=")[0], var1x.split("=")[1]);
            });
         }

         var3 = var3.split("\\Q[\\E")[0];
         byte var7 = -1;
         switch(var3.hashCode()) {
         case -582643067:
            if (var3.equals("concrete")) {
               var7 = 2;
            }
            break;
         case 3655349:
            if (var3.equals("wool")) {
               var7 = 3;
            }
            break;
         case 85518306:
            if (var3.equals("cauldron")) {
               var7 = 0;
            }
            break;
         case 963706126:
            if (var3.equals("grass_path")) {
               var7 = 1;
            }
            break;
         case 1766545119:
            if (var3.equals("beetroots")) {
               var7 = 4;
            }
         }

         String var8;
         switch(var7) {
         case 0:
            var3 = "water_cauldron";
            break;
         case 1:
            var3 = "dirt_path";
            break;
         case 2:
            var3 = "white_concrete";
            break;
         case 3:
            var3 = "white_wool";
            break;
         case 4:
            if (var5.containsKey("age")) {
               var8 = (String)var5.get("age");
               byte var10 = -1;
               switch(var8.hashCode()) {
               case 49:
                  if (var8.equals("1")) {
                     var10 = 4;
                  }
                  break;
               case 50:
                  if (var8.equals("2")) {
                     var10 = 5;
                  }
                  break;
               case 51:
                  if (var8.equals("3")) {
                     var10 = 1;
                  }
                  break;
               case 52:
                  if (var8.equals("4")) {
                     var10 = 2;
                  }
                  break;
               case 53:
                  if (var8.equals("5")) {
                     var10 = 3;
                  }
               case 54:
               default:
                  break;
               case 55:
                  if (var8.equals("7")) {
                     var10 = 0;
                  }
               }

               switch(var10) {
               case 0:
                  var8 = "3";
                  break;
               case 1:
               case 2:
               case 3:
                  var8 = "2";
                  break;
               case 4:
               case 5:
                  var8 = "1";
               }

               var5.put("age", var8);
            }
         }

         HashMap var6 = new HashMap();
         Iterator var19 = var5.keySet().iterator();

         while(var19.hasNext()) {
            var8 = (String)var19.next();

            try {
               String var9 = var3 + "[" + var8 + "=" + (String)var5.get(var8) + "]";
               createBlockData(var9, var1);
               var6.put(var8, (String)var5.get(var8));
            } catch (IllegalArgumentException var12) {
            }
         }

         var4 = (String)var6.entrySet().stream().map((var0x) -> {
            String var10000 = (String)var0x.getKey();
            return var10000 + "=" + (String)var0x.getValue();
         }).collect(Collectors.joining(","));
         if (!var4.equals("")) {
            var4 = "[" + var4 + "]";
         }

         String var20 = var3 + var4;
         Iris.debug("Converting " + var0 + " to " + var20);

         try {
            return createBlockData(var20, var1);
         } catch (Throwable var11) {
            Iris.reportError(var11);
            return null;
         }
      }
   }

   public static boolean isStorage(BlockData mat) {
      return storageCache.contains(var0.getMaterial().ordinal());
   }

   public static boolean isStorageChest(BlockData mat) {
      return storageChestCache.contains(var0.getMaterial().ordinal());
   }

   public static boolean isLit(BlockData mat) {
      return litCache.contains(var0.getMaterial().ordinal());
   }

   public static boolean isUpdatable(BlockData mat) {
      return isStorage(var0) || var0 instanceof PointedDripstone && ((PointedDripstone)var0).getThickness().equals(Thickness.TIP);
   }

   public static boolean isFoliage(Material d) {
      return foliageCache.contains(var0.ordinal());
   }

   public static boolean isFoliage(BlockData d) {
      return isFoliage(var0.getMaterial());
   }

   public static boolean isDecorant(BlockData m) {
      return decorantCache.contains(var0.getMaterial().ordinal());
   }

   public static KList<BlockData> get(KList<String> find) {
      KList var1 = new KList();
      Iterator var2 = var0.iterator();

      while(var2.hasNext()) {
         String var3 = (String)var2.next();
         BlockData var4 = get(var3);
         if (var4 != null) {
            var1.add((Object)var4);
         }
      }

      return var1;
   }

   public static boolean isFluid(BlockData d) {
      return var0.getMaterial().equals(Material.WATER) || var0.getMaterial().equals(Material.LAVA);
   }

   public static boolean isAirOrFluid(BlockData d) {
      return isAir(var0) || isFluid(var0);
   }

   public static boolean isAir(BlockData d) {
      if (var0 == null) {
         return true;
      } else {
         return var0.getMaterial().equals(Material.AIR) || var0.getMaterial().equals(Material.CAVE_AIR) || var0.getMaterial().equals(Material.VOID_AIR);
      }
   }

   public static synchronized String[] getBlockTypes() {
      KList var0 = new KList();
      Material[] var1 = Material.values();
      int var2 = var1.length;

      for(int var3 = 0; var3 < var2; ++var3) {
         Material var4 = var1[var3];
         if (var4.isBlock()) {
            String var5 = var4.createBlockData().getAsString(true);
            if (var5.contains("[")) {
               var5 = var5.split("\\Q[\\E")[0];
            }

            var0.add((Object)var5);
         }
      }

      Iterator var6 = ((ExternalDataSVC)Iris.service(ExternalDataSVC.class)).getAllIdentifiers(DataType.BLOCK).iterator();

      while(var6.hasNext()) {
         Identifier var7 = (Identifier)var6.next();
         var0.add((Object)var7.toString());
      }

      var0.addAll(custom.k());
      return (String[])var0.toArray(new String[0]);
   }

   public static synchronized KMap<List<String>, List<BlockProperty>> getBlockStates() {
      KMap var0 = new KMap();
      INMS.get().getBlockProperties().forEach((var1x, var2x) -> {
         ((List)var0.computeIfAbsent(var2x, (var0x) -> {
            return new KList();
         })).add(var1x.getKey().toString());
      });
      List var1 = (List)var0.computeIfAbsent(new KList(0), (var0x) -> {
         return new KList();
      });
      Iterator var2 = ((ExternalDataSVC)Iris.service(ExternalDataSVC.class)).getAllBlockProperties().iterator();

      while(var2.hasNext()) {
         Pair var3 = (Pair)var2.next();
         if (((List)var3.getB()).isEmpty()) {
            var1.add(((Identifier)var3.getA()).toString());
         } else {
            ((List)var0.computeIfAbsent((List)var3.getB(), (var0x) -> {
               return new KList();
            })).add(((Identifier)var3.getA()).toString());
         }
      }

      var1.addAll(custom.k());
      KMap var4 = new KMap();
      var0.forEach((var1x, var2x) -> {
         List var3 = (List)var4.put(var2x, var1x);
         if (var3 != null) {
            String var10000 = String.valueOf(var2x);
            Iris.error("Duplicate block state: " + var10000 + " (" + String.valueOf(var3) + " and " + String.valueOf(var1x) + ")");
         }

      });
      return var4;
   }

   public static String[] getItemTypes() {
      KList var0 = new KList();
      Material[] var1 = Material.values();
      int var2 = var1.length;

      for(int var3 = 0; var3 < var2; ++var3) {
         Material var4 = var1[var3];
         String var5 = var4.name().toLowerCase().trim();
         var0.add((Object)var5);
      }

      Iterator var6 = ((ExternalDataSVC)Iris.service(ExternalDataSVC.class)).getAllIdentifiers(DataType.ITEM).iterator();

      while(var6.hasNext()) {
         Identifier var7 = (Identifier)var6.next();
         var0.add((Object)var7.toString());
      }

      return (String[])var0.toArray(new String[0]);
   }

   public static boolean isWaterLogged(BlockData b) {
      return var0 instanceof Waterlogged && ((Waterlogged)var0).isWaterlogged();
   }

   public static void registerCustomBlockData(String namespace, String key, BlockData blockData) {
      custom.put(var0 + ":" + var1, var2);
   }

   public static boolean isVineBlock(BlockData data) {
      boolean var10000;
      switch(var0.getMaterial()) {
      case VINE:
      case SCULK_VEIN:
      case GLOW_LICHEN:
         var10000 = true;
         break;
      default:
         var10000 = false;
      }

      return var10000;
   }

   static {
      AIR_MATERIAL = Material.AIR;
      SHORT_GRASS = Materials.GRASS;
      AIR = AIR_MATERIAL.createBlockData();
      foliageCache = buildFoliageCache();
      deepslateCache = buildDeepslateCache();
      normal2DeepslateCache = buildNormal2DeepslateCache();
      deepslate2NormalCache = buildDeepslate2NormalCache();
      decorantCache = buildDecorantCache();
      storageCache = buildStorageCache();
      storageChestCache = buildStorageChestCache();
      litCache = buildLitCache();
      clw = new ChronoLatch(1000L);
   }
}
