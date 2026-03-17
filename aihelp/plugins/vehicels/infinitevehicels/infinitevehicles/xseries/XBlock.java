package me.PM2.infinitevehicles.xseries;

import java.util.Collection;
import java.util.Collections;
import java.util.EnumMap;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.SkullType;
import org.bukkit.TreeSpecies;
import org.bukkit.block.Banner;
import org.bukkit.block.Bed;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.BlockState;
import org.bukkit.block.Skull;
import org.bukkit.block.data.Ageable;
import org.bukkit.block.data.BlockData;
import org.bukkit.block.data.Directional;
import org.bukkit.block.data.Levelled;
import org.bukkit.block.data.Lightable;
import org.bukkit.block.data.Openable;
import org.bukkit.block.data.Powerable;
import org.bukkit.block.data.Rotatable;
import org.bukkit.block.data.type.Cake;
import org.bukkit.block.data.type.EndPortalFrame;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.material.Colorable;
import org.bukkit.material.FlowerPot;
import org.bukkit.material.MaterialData;
import org.bukkit.material.Wool;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.ApiStatus.Experimental;

public final class XBlock {
   /** @deprecated */
   @Deprecated
   public static final Set<XMaterial> CROPS;
   /** @deprecated */
   @Deprecated
   public static final Set<XMaterial> DANGEROUS;
   public static final byte CAKE_SLICES = 6;
   private static final boolean ISFLAT;
   private static final Map<XMaterial, XMaterial> ITEM_TO_BLOCK;

   private XBlock() {
   }

   public static boolean isLit(Block var0) {
      if (ISFLAT) {
         if (!(var0.getBlockData() instanceof Lightable)) {
            return false;
         } else {
            Lightable var1 = (Lightable)var0.getBlockData();
            return var1.isLit();
         }
      } else {
         return isMaterial(var0, XBlock.LegacyBlockMaterial.REDSTONE_LAMP_ON, XBlock.LegacyBlockMaterial.REDSTONE_TORCH_ON, XBlock.LegacyBlockMaterial.BURNING_FURNACE);
      }
   }

   public static boolean isContainer(@Nullable Block var0) {
      return var0 != null && var0.getState() instanceof InventoryHolder;
   }

   public static void setLit(Block var0, boolean var1) {
      if (ISFLAT) {
         if (var0.getBlockData() instanceof Lightable) {
            BlockData var4 = var0.getBlockData();
            Lightable var3 = (Lightable)var4;
            var3.setLit(var1);
            var0.setBlockData(var4, false);
         }
      } else {
         String var2 = var0.getType().name();
         if (var2.endsWith("FURNACE")) {
            var0.setType(XBlock.LegacyBlockMaterial.BURNING_FURNACE.material);
         } else if (var2.startsWith("REDSTONE_LAMP")) {
            var0.setType(XBlock.LegacyBlockMaterial.REDSTONE_LAMP_ON.material);
         } else {
            var0.setType(XBlock.LegacyBlockMaterial.REDSTONE_TORCH_ON.material);
         }

      }
   }

   /** @deprecated */
   @Deprecated
   public static boolean isCrop(XMaterial var0) {
      return CROPS.contains(var0);
   }

   /** @deprecated */
   @Deprecated
   public static boolean isDangerous(XMaterial var0) {
      return DANGEROUS.contains(var0);
   }

   public static DyeColor getColor(Block var0) {
      if (ISFLAT) {
         if (!(var0.getBlockData() instanceof Colorable)) {
            return null;
         } else {
            Colorable var4 = (Colorable)var0.getBlockData();
            return var4.getColor();
         }
      } else {
         BlockState var1 = var0.getState();
         MaterialData var2 = var1.getData();
         if (var2 instanceof Wool) {
            Wool var3 = (Wool)var2;
            return var3.getColor();
         } else {
            return null;
         }
      }
   }

   /** @deprecated */
   @Deprecated
   public static boolean isCake(@Nullable Material var0) {
      if (!ISFLAT) {
         return var0 == XBlock.LegacyBlockMaterial.CAKE_BLOCK.material;
      } else {
         return var0 == Material.CAKE;
      }
   }

   /** @deprecated */
   @Deprecated
   public static boolean isWheat(@Nullable Material var0) {
      if (!ISFLAT) {
         return var0 == XBlock.LegacyBlockMaterial.CROPS.material;
      } else {
         return var0 == Material.WHEAT;
      }
   }

   /** @deprecated */
   @Deprecated
   public static boolean isSugarCane(@Nullable Material var0) {
      if (!ISFLAT) {
         return var0 == XBlock.LegacyBlockMaterial.SUGAR_CANE_BLOCK.material;
      } else {
         return var0 == Material.SUGAR_CANE;
      }
   }

   /** @deprecated */
   @Deprecated
   public static boolean isBeetroot(@Nullable Material var0) {
      if (ISFLAT) {
         return var0 == Material.BEETROOTS;
      } else {
         return var0 != null && var0 == XBlock.LegacyBlockMaterial.BEETROOT_BLOCK.material;
      }
   }

   /** @deprecated */
   @Deprecated
   public static boolean isNetherWart(@Nullable Material var0) {
      if (!ISFLAT) {
         return var0 == XBlock.LegacyBlockMaterial.NETHER_WARTS.material;
      } else {
         return var0 == Material.NETHER_WART;
      }
   }

   /** @deprecated */
   @Deprecated
   public static boolean isCarrot(@Nullable Material var0) {
      if (!ISFLAT) {
         return var0 == Material.CARROT;
      } else {
         return var0 == Material.CARROTS;
      }
   }

   /** @deprecated */
   @Deprecated
   public static boolean isMelon(@Nullable Material var0) {
      if (!ISFLAT) {
         return var0 == XBlock.LegacyBlockMaterial.MELON_BLOCK.material;
      } else {
         return var0 == Material.MELON;
      }
   }

   /** @deprecated */
   @Deprecated
   public static boolean isPotato(@Nullable Material var0) {
      if (!ISFLAT) {
         return var0 == Material.POTATO;
      } else {
         return var0 == Material.POTATOES;
      }
   }

   public static BlockFace getDirection(Block var0) {
      if (ISFLAT) {
         if (!(var0.getBlockData() instanceof Directional)) {
            return BlockFace.SELF;
         } else {
            Directional var3 = (Directional)var0.getBlockData();
            return var3.getFacing();
         }
      } else {
         BlockState var1 = var0.getState();
         MaterialData var2 = var1.getData();
         return var2 instanceof org.bukkit.material.Directional ? ((org.bukkit.material.Directional)var2).getFacing() : BlockFace.SELF;
      }
   }

   public static boolean setDirection(Block var0, BlockFace var1) {
      if (ISFLAT) {
         if (!(var0.getBlockData() instanceof Directional)) {
            return false;
         } else {
            BlockData var4 = var0.getBlockData();
            Directional var5 = (Directional)var4;
            var5.setFacing(var1);
            var0.setBlockData(var4, false);
            return true;
         }
      } else {
         BlockState var2 = var0.getState();
         MaterialData var3 = var2.getData();
         if (var3 instanceof org.bukkit.material.Directional) {
            if (XMaterial.matchXMaterial(var0.getType()) == XMaterial.LADDER) {
               var1 = var1.getOppositeFace();
            }

            ((org.bukkit.material.Directional)var3).setFacingDirection(var1);
            var2.update(true);
            return true;
         } else {
            return false;
         }
      }
   }

   @Experimental
   public static XMaterial getType(Block var0) {
      if (ISFLAT) {
         return XMaterial.matchXMaterial(var0.getType());
      } else {
         Material var1 = var0.getType();
         XBlock.LegacyMaterialGroup var2 = XBlock.LegacyMaterialGroup.getMaterial(var1.name());
         if (var2 == null) {
            return XMaterial.matchXMaterial(var0.getType());
         } else {
            byte var3 = var0.getData();
            Banner var15;
            DyeColor var16;
            switch(var2.ordinal()) {
            case 0:
            case 2:
               var15 = (Banner)var0.getState();
               var16 = var15.getBaseColor();
               switch(var16) {
               case WHITE:
                  return XMaterial.WHITE_BANNER;
               case ORANGE:
                  return XMaterial.ORANGE_BANNER;
               case MAGENTA:
                  return XMaterial.MAGENTA_BANNER;
               case LIGHT_BLUE:
                  return XMaterial.LIGHT_BLUE_BANNER;
               case YELLOW:
                  return XMaterial.YELLOW_BANNER;
               case LIME:
                  return XMaterial.LIME_BANNER;
               case PINK:
                  return XMaterial.PINK_BANNER;
               case GRAY:
                  return XMaterial.GRAY_BANNER;
               case LIGHT_GRAY:
                  return XMaterial.LIGHT_GRAY_BANNER;
               case CYAN:
                  return XMaterial.CYAN_BANNER;
               case PURPLE:
                  return XMaterial.PURPLE_BANNER;
               case BLUE:
                  return XMaterial.BLUE_BANNER;
               case BROWN:
                  return XMaterial.BROWN_BANNER;
               case GREEN:
                  return XMaterial.GREEN_BANNER;
               case RED:
                  return XMaterial.RED_BANNER;
               case BLACK:
                  return XMaterial.BLACK_BANNER;
               default:
                  throw new AssertionError("Unknown " + var2 + " type: " + var16);
               }
            case 1:
               var15 = (Banner)var0.getState();
               var16 = var15.getBaseColor();
               switch(var16) {
               case WHITE:
                  return XMaterial.WHITE_WALL_BANNER;
               case ORANGE:
                  return XMaterial.ORANGE_WALL_BANNER;
               case MAGENTA:
                  return XMaterial.MAGENTA_WALL_BANNER;
               case LIGHT_BLUE:
                  return XMaterial.LIGHT_BLUE_WALL_BANNER;
               case YELLOW:
                  return XMaterial.YELLOW_WALL_BANNER;
               case LIME:
                  return XMaterial.LIME_WALL_BANNER;
               case PINK:
                  return XMaterial.PINK_WALL_BANNER;
               case GRAY:
                  return XMaterial.GRAY_WALL_BANNER;
               case LIGHT_GRAY:
                  return XMaterial.LIGHT_GRAY_WALL_BANNER;
               case CYAN:
                  return XMaterial.CYAN_WALL_BANNER;
               case PURPLE:
                  return XMaterial.PURPLE_WALL_BANNER;
               case BLUE:
                  return XMaterial.BLUE_WALL_BANNER;
               case BROWN:
                  return XMaterial.BROWN_WALL_BANNER;
               case GREEN:
                  return XMaterial.GREEN_WALL_BANNER;
               case RED:
                  return XMaterial.RED_WALL_BANNER;
               case BLACK:
                  return XMaterial.BLACK_WALL_BANNER;
               default:
                  throw new AssertionError("Unknown " + var2 + " type: " + var16);
               }
            case 3:
            case 4:
            case 5:
            case 6:
            case 7:
            case 8:
            case 9:
            case 12:
            case 15:
            case 16:
            case 17:
            case 18:
            case 19:
            case 20:
            case 21:
            case 22:
            case 23:
            case 25:
            case 26:
            case 29:
            case 30:
            case 31:
            default:
               break;
            case 10:
               switch(var3) {
               case 0:
                  return XMaterial.QUARTZ_BLOCK;
               case 1:
                  return XMaterial.CHISELED_QUARTZ_BLOCK;
               case 2:
               case 3:
               case 4:
                  return XMaterial.QUARTZ_PILLAR;
               default:
                  throw new AssertionError("Unknown QUARTZ_BLOCK type: " + var3);
               }
            case 11:
               Skull var4 = (Skull)var0.getState();
               switch(var4.getSkullType()) {
               case SKELETON:
                  return XMaterial.SKELETON_SKULL;
               case WITHER:
                  return XMaterial.WITHER_SKELETON_SKULL;
               case ZOMBIE:
                  return XMaterial.ZOMBIE_HEAD;
               case PLAYER:
                  return XMaterial.PLAYER_HEAD;
               case CREEPER:
                  return XMaterial.CREEPER_HEAD;
               case DRAGON:
                  return XMaterial.DRAGON_HEAD;
               default:
                  throw new AssertionError("Unknown SKULL type: " + var4);
               }
            case 13:
               BlockState var9 = var0.getState();
               FlowerPot var10 = (FlowerPot)var9.getData();
               MaterialData var11 = var10.getContents();
               if (var11 == null) {
                  return XMaterial.FLOWER_POT;
               }

               Material var14 = var11.getItemType();
               byte var8 = var11.getData();
               String var12 = var14.name();
               byte var13 = -1;
               switch(var12.hashCode()) {
               case -1709492552:
                  if (var12.equals("SAPLING")) {
                     var13 = 6;
                  }
                  break;
               case -497164186:
                  if (var12.equals("RED_MUSHROOM")) {
                     var13 = 2;
                  }
                  break;
               case -469931898:
                  if (var12.equals("YELLOW_FLOWER")) {
                     var13 = 1;
                  }
                  break;
               case 26809437:
                  if (var12.equals("RED_ROSE")) {
                     var13 = 0;
                  }
                  break;
               case 1066212883:
                  if (var12.equals("LONG_GRASS")) {
                     var13 = 7;
                  }
                  break;
               case 1236741347:
                  if (var12.equals("DEAD_BUSH")) {
                     var13 = 5;
                  }
                  break;
               case 1868692833:
                  if (var12.equals("BROWN_MUSHROOM")) {
                     var13 = 3;
                  }
                  break;
               case 1980261421:
                  if (var12.equals("CACTUS")) {
                     var13 = 4;
                  }
               }

               switch(var13) {
               case 0:
                  switch(var8) {
                  case 0:
                     return XMaterial.POTTED_POPPY;
                  case 1:
                     return XMaterial.POTTED_BLUE_ORCHID;
                  case 2:
                     return XMaterial.POTTED_ALLIUM;
                  case 3:
                     return XMaterial.POTTED_AZURE_BLUET;
                  case 4:
                     return XMaterial.POTTED_RED_TULIP;
                  case 5:
                     return XMaterial.POTTED_ORANGE_TULIP;
                  case 6:
                     return XMaterial.POTTED_WHITE_TULIP;
                  case 7:
                     return XMaterial.POTTED_PINK_TULIP;
                  case 8:
                     return XMaterial.POTTED_OXEYE_DAISY;
                  }
               case 1:
                  return XMaterial.POTTED_DANDELION;
               case 2:
                  return XMaterial.POTTED_RED_MUSHROOM;
               case 3:
                  return XMaterial.POTTED_BROWN_MUSHROOM;
               case 4:
                  return XMaterial.POTTED_CACTUS;
               case 5:
                  return XMaterial.POTTED_DEAD_BUSH;
               case 6:
                  switch(var8) {
                  case 0:
                     return XMaterial.POTTED_OAK_SAPLING;
                  case 1:
                     return XMaterial.POTTED_SPRUCE_SAPLING;
                  case 2:
                     return XMaterial.POTTED_BIRCH_SAPLING;
                  case 3:
                     return XMaterial.POTTED_JUNGLE_SAPLING;
                  case 4:
                     return XMaterial.POTTED_ACACIA_SAPLING;
                  case 5:
                     return XMaterial.POTTED_DARK_OAK_SAPLING;
                  }
               case 7:
                  if (var8 == 2) {
                     return XMaterial.POTTED_FERN;
                  }
               default:
                  throw new AssertionError("Unknown potted flower type: " + var10 + " | " + var14 + " | " + var8);
               }
            case 14:
               if (var3 == 10) {
                  Block var7 = var0.getRelative(0, -1, 0);
                  if (var7.getType().name().equals("DOUBLE_PLANT")) {
                     var3 = var7.getData();
                  }
               }

               var3 = (byte)(var3 & 7);
               break;
            case 24:
               var3 = var0.getData();
               var3 = (byte)(var3 >> 2 & 3);
               break;
            case 27:
            case 28:
               if (!XMaterial.supports(12)) {
                  return XMaterial.RED_BED;
               }

               Bed var5 = (Bed)var0.getState();
               DyeColor var6 = var5.getColor();
               switch(var6) {
               case WHITE:
                  return XMaterial.WHITE_BED;
               case ORANGE:
                  return XMaterial.ORANGE_BED;
               case MAGENTA:
                  return XMaterial.MAGENTA_BED;
               case LIGHT_BLUE:
                  return XMaterial.LIGHT_BLUE_BED;
               case YELLOW:
                  return XMaterial.YELLOW_BED;
               case LIME:
                  return XMaterial.LIME_BED;
               case PINK:
                  return XMaterial.PINK_BED;
               case GRAY:
                  return XMaterial.GRAY_BED;
               case LIGHT_GRAY:
                  return XMaterial.LIGHT_GRAY_BED;
               case CYAN:
                  return XMaterial.CYAN_BED;
               case PURPLE:
                  return XMaterial.PURPLE_BED;
               case BLUE:
                  return XMaterial.BLUE_BED;
               case BROWN:
                  return XMaterial.BROWN_BED;
               case GREEN:
                  return XMaterial.GREEN_BED;
               case RED:
                  return XMaterial.RED_BED;
               case BLACK:
                  return XMaterial.BLACK_BED;
               default:
                  throw new AssertionError("Unkonwn " + var2 + " type: " + var6);
               }
            case 32:
               switch(var3) {
               case 0:
                  return XMaterial.OAK_PLANKS;
               case 1:
                  return XMaterial.SPRUCE_PLANKS;
               case 2:
                  return XMaterial.BIRCH_PLANKS;
               case 3:
                  return XMaterial.JUNGLE_PLANKS;
               case 4:
                  return XMaterial.ACACIA_PLANKS;
               case 5:
                  return XMaterial.DARK_OAK_PLANKS;
               default:
                  throw new AssertionError("Unknown WOOD_DOUBLE_STEP type: " + var3);
               }
            case 33:
            case 34:
            case 35:
            case 36:
               var3 = (byte)(var3 & 3);
               break;
            case 37:
               var3 = (byte)(var3 & 7);
               break;
            case 38:
               return XMaterial.BRICKS;
            case 39:
               if (var3 == 2) {
                  return XMaterial.OAK_SLAB;
               }
               break;
            case 40:
               switch(var3) {
               case 0:
                  return XMaterial.SMOOTH_STONE_SLAB;
               case 1:
                  return XMaterial.SANDSTONE;
               case 2:
                  return XMaterial.OAK_PLANKS;
               case 3:
                  return XMaterial.COBBLESTONE;
               case 4:
                  return XMaterial.BRICKS;
               case 5:
                  return XMaterial.STONE_BRICKS;
               case 6:
                  return XMaterial.NETHER_BRICKS;
               case 7:
                  return XMaterial.QUARTZ_BLOCK;
               default:
                  throw new AssertionError("Unknown STEP type: " + var3);
               }
            case 41:
               if (var3 == 0) {
                  return XMaterial.RED_SANDSTONE;
               }

               throw new AssertionError("Unknown DOUBLE_STONE_SLAB2 type: " + var3);
            }

            return (XMaterial)XMaterial.matchDefinedXMaterial(var1.name(), var3).orElseThrow(() -> {
               return new AssertionError("Unknown legacy block type:  | " + var3 + " | " + var1 + " | " + var2);
            });
         }
      }
   }

   public static boolean setType(@NotNull Block var0, @Nullable XMaterial var1, boolean var2) {
      Objects.requireNonNull(var0, "Cannot set type of null block");
      if (var1 == null) {
         var1 = XMaterial.AIR;
      }

      XMaterial var3 = (XMaterial)ITEM_TO_BLOCK.get(var1);
      if (var3 != null) {
         var1 = var3;
      }

      Material var4 = var1.get();
      if (var4 == null) {
         return false;
      } else {
         String var5 = var4.name();
         SkullType var6 = getSkullType(var1);
         if (!ISFLAT && (var5.equals("SKULL_ITEM") || var6 != null)) {
            var4 = Material.valueOf("SKULL");
         }

         var0.setType(var4, var2);
         if (ISFLAT) {
            return false;
         } else {
            XBlock.LegacyBlockMaterial var7 = null;
            switch(var1) {
            case CAKE:
               var7 = XBlock.LegacyBlockMaterial.CAKE_BLOCK;
               break;
            case SUGAR_CANE:
               var7 = XBlock.LegacyBlockMaterial.SUGAR_CANE_BLOCK;
               break;
            case POTATOES:
            case POTATO:
               var7 = XBlock.LegacyBlockMaterial.POTATO;
               break;
            case CARROT:
            case CARROTS:
               var7 = XBlock.LegacyBlockMaterial.CARROT;
               break;
            case WHEAT_SEEDS:
            case WHEAT:
               var7 = XBlock.LegacyBlockMaterial.CROPS;
            }

            if (var7 != null) {
               var0.setType(var7.material, var2);
               return true;
            } else {
               XBlock.LegacyMaterialGroup var8 = XBlock.LegacyMaterialGroup.getMaterial(var5);
               if (var8 == XBlock.LegacyMaterialGroup.BANNER) {
                  var0.setType(XBlock.LegacyMaterialGroup.STANDING_BANNER.material, var2);
               }

               XBlock.LegacyMaterialGroup.Handling var9 = var8 == null ? null : var8.handling;
               BlockState var10 = var0.getState();
               boolean var11 = false;
               if (var9 == XBlock.LegacyMaterialGroup.Handling.COLORABLE) {
                  if (var10 instanceof Banner) {
                     Banner var12 = (Banner)var10;
                     String var13 = var1.name();
                     int var14 = var13.indexOf(95);
                     String var15 = var13.substring(0, var14);
                     if (var15.equals("LIGHT")) {
                        var15 = var13.substring(0, "LIGHT_".length() + 4);
                     }

                     var12.setBaseColor(DyeColor.valueOf(var15));
                  } else {
                     var10.setRawData(var1.getData());
                  }

                  var11 = true;
               } else if (var9 == XBlock.LegacyMaterialGroup.Handling.WOOD_SPECIES) {
                  String var20 = var1.name();
                  int var23 = var20.indexOf(95);
                  if (var23 < 0) {
                     return false;
                  }

                  String var24 = var20.substring(0, var23);
                  byte var17 = -1;
                  switch(var24.hashCode()) {
                  case -1842339390:
                     if (var24.equals("SPRUCE")) {
                        var17 = 2;
                     }
                     break;
                  case 78009:
                     if (var24.equals("OAK")) {
                        var17 = 0;
                     }
                     break;
                  case 2090870:
                     if (var24.equals("DARK")) {
                        var17 = 1;
                     }
                  }

                  TreeSpecies var25;
                  switch(var17) {
                  case 0:
                     var25 = TreeSpecies.GENERIC;
                     break;
                  case 1:
                     var25 = TreeSpecies.DARK_OAK;
                     break;
                  case 2:
                     var25 = TreeSpecies.REDWOOD;
                     break;
                  default:
                     try {
                        var25 = TreeSpecies.valueOf(var24);
                     } catch (IllegalArgumentException var19) {
                        throw new AssertionError("Unknown material " + var8 + " for wood species");
                     }
                  }

                  boolean var16 = false;
                  switch(var8.ordinal()) {
                  case 30:
                  case 32:
                     var10.setRawData(var25.getData());
                     var11 = true;
                     break;
                  case 31:
                  case 37:
                     var10.setRawData((byte)(var10.getRawData() & 8 | var25.getData()));
                     var11 = true;
                     break;
                  case 33:
                  case 35:
                     var16 = true;
                  case 34:
                  case 36:
                     switch(var25) {
                     case GENERIC:
                     case REDWOOD:
                     case BIRCH:
                     case JUNGLE:
                        if (!var16) {
                           throw new AssertionError("Invalid tree species " + var25 + " for block type" + var8 + ", use block type 2 instead");
                        }
                        break;
                     case ACACIA:
                     case DARK_OAK:
                        if (var16) {
                           throw new AssertionError("Invalid tree species " + var25 + " for block type 2 " + var8 + ", use block type instead");
                        }
                     }

                     var10.setRawData((byte)(var10.getRawData() & 12 | var25.getData() & 3));
                     var11 = true;
                     break;
                  default:
                     throw new AssertionError("Unknown block type " + var8 + " for tree species: " + var25);
                  }
               } else if (var1.getData() != 0) {
                  if (var6 != null) {
                     boolean var21 = var1.name().contains("WALL");
                     var10.setRawData((byte)(var21 ? 0 : 1));
                  } else {
                     var10.setRawData(var1.getData());
                  }

                  var11 = true;
               }

               if (var6 != null) {
                  Skull var22 = (Skull)var10;
                  var22.setSkullType(var6);
                  var11 = true;
               }

               if (var11) {
                  var10.update(true, var2);
               }

               return var11;
            }
         }
      }
   }

   public static SkullType getSkullType(XMaterial var0) {
      switch(var0) {
      case PLAYER_HEAD:
      case PLAYER_WALL_HEAD:
         return SkullType.PLAYER;
      case DRAGON_HEAD:
      case DRAGON_WALL_HEAD:
         return SkullType.DRAGON;
      case ZOMBIE_HEAD:
      case ZOMBIE_WALL_HEAD:
         return SkullType.ZOMBIE;
      case CREEPER_HEAD:
      case CREEPER_WALL_HEAD:
         return SkullType.CREEPER;
      case SKELETON_SKULL:
      case SKELETON_WALL_SKULL:
         return SkullType.SKELETON;
      case WITHER_SKELETON_SKULL:
      case WITHER_SKELETON_WALL_SKULL:
         return SkullType.WITHER;
      case PIGLIN_HEAD:
      case PIGLIN_WALL_HEAD:
         return SkullType.PIGLIN;
      default:
         return null;
      }
   }

   public static boolean setType(@NotNull Block var0, @Nullable XMaterial var1) {
      return setType(var0, var1, true);
   }

   public static int getAge(Block var0) {
      if (ISFLAT) {
         if (!(var0.getBlockData() instanceof Ageable)) {
            return 0;
         } else {
            Ageable var3 = (Ageable)var0.getBlockData();
            return var3.getAge();
         }
      } else {
         BlockState var1 = var0.getState();
         MaterialData var2 = var1.getData();
         return var2.getData();
      }
   }

   public static void setAge(Block var0, int var1) {
      if (ISFLAT) {
         if (!(var0.getBlockData() instanceof Ageable)) {
            return;
         }

         BlockData var2 = var0.getBlockData();
         Ageable var3 = (Ageable)var2;
         var3.setAge(var1);
         var0.setBlockData(var2, false);
      }

      BlockState var4 = var0.getState();
      MaterialData var5 = var4.getData();
      var5.setData((byte)var1);
      var4.update(true);
   }

   public static boolean setColor(Block var0, DyeColor var1) {
      if (ISFLAT) {
         String var6 = var0.getType().name();
         int var3 = var6.indexOf(95);
         if (var3 == -1) {
            return false;
         } else {
            String var4 = var6.substring(var3 + 1);
            Material var5 = Material.getMaterial(var1.name() + '_' + var4);
            if (var5 == null) {
               return false;
            } else {
               var0.setType(var5);
               return true;
            }
         }
      } else {
         BlockState var2 = var0.getState();
         var2.setRawData(var1.getWoolData());
         var2.update(true);
         return false;
      }
   }

   public static boolean setFluidLevel(Block var0, int var1) {
      if (ISFLAT) {
         if (!(var0.getBlockData() instanceof Levelled)) {
            return false;
         } else {
            BlockData var4 = var0.getBlockData();
            Levelled var5 = (Levelled)var4;
            var5.setLevel(var1);
            var0.setBlockData(var4, false);
            return true;
         }
      } else {
         BlockState var2 = var0.getState();
         MaterialData var3 = var2.getData();
         var3.setData((byte)var1);
         var2.update(true);
         return false;
      }
   }

   public static int getFluidLevel(Block var0) {
      if (ISFLAT) {
         if (!(var0.getBlockData() instanceof Levelled)) {
            return -1;
         } else {
            Levelled var3 = (Levelled)var0.getBlockData();
            return var3.getLevel();
         }
      } else {
         BlockState var1 = var0.getState();
         MaterialData var2 = var1.getData();
         return var2.getData();
      }
   }

   public static boolean isWaterStationary(Block var0) {
      return ISFLAT ? getFluidLevel(var0) < 7 : var0.getType() == XBlock.LegacyBlockMaterial.STATIONARY_WATER.material;
   }

   /** @deprecated */
   @Deprecated
   public static boolean isWater(Material var0) {
      return var0 == Material.WATER || var0 == XBlock.LegacyBlockMaterial.STATIONARY_WATER.material;
   }

   /** @deprecated */
   @Deprecated
   public static boolean isLava(Material var0) {
      return var0 == Material.LAVA || var0 == XBlock.LegacyBlockMaterial.STATIONARY_LAVA.material;
   }

   /** @deprecated */
   @Deprecated
   public static boolean isOneOf(Block var0, Collection<String> var1) {
      if (var1 != null && !var1.isEmpty()) {
         String var2 = var0.getType().name();
         XMaterial var3 = XMaterial.matchXMaterial(var0.getType());
         Iterator var4 = var1.iterator();

         while(var4.hasNext()) {
            String var5 = (String)var4.next();
            String var6 = var5.toUpperCase(Locale.ENGLISH);
            if (var6.startsWith("CONTAINS:")) {
               var5 = XMaterial.format(var6.substring(9));
               if (var2.contains(var5)) {
                  return true;
               }
            } else if (var6.startsWith("REGEX:")) {
               var5 = var5.substring(6);
               if (var2.matches(var5)) {
                  return true;
               }
            } else {
               Optional var7 = XMaterial.matchXMaterial(var5);
               if (var7.isPresent() && isSimilar(var0, (XMaterial)var7.get())) {
                  return true;
               }
            }
         }

         return false;
      } else {
         return false;
      }
   }

   public static void setCakeSlices(Block var0, int var1) {
      if (!isCake(var0.getType())) {
         throw new IllegalArgumentException("Block is not a cake: " + var0.getType());
      } else if (ISFLAT) {
         BlockData var5 = var0.getBlockData();
         Cake var6 = (Cake)var5;
         int var4 = var6.getMaximumBites() - (var6.getBites() + var1);
         if (var4 > 0) {
            var6.setBites(var4);
            var0.setBlockData(var5);
         } else {
            var0.breakNaturally();
         }

      } else {
         BlockState var2 = var0.getState();
         org.bukkit.material.Cake var3 = (org.bukkit.material.Cake)var2.getData();
         if (var1 > 0) {
            var3.setSlicesRemaining(var1);
            var2.update(true);
         } else {
            var0.breakNaturally();
         }

      }
   }

   public static int addCakeSlices(Block var0, int var1) {
      if (!isCake(var0.getType())) {
         throw new IllegalArgumentException("Block is not a cake: " + var0.getType());
      } else {
         int var4;
         if (ISFLAT) {
            BlockData var6 = var0.getBlockData();
            Cake var7 = (Cake)var6;
            var4 = var7.getBites() - var1;
            int var5 = var7.getMaximumBites() - var4;
            if (var5 > 0) {
               var7.setBites(var4);
               var0.setBlockData(var6);
               return var5;
            } else {
               var0.breakNaturally();
               return 0;
            }
         } else {
            BlockState var2 = var0.getState();
            org.bukkit.material.Cake var3 = (org.bukkit.material.Cake)var2.getData();
            var4 = var3.getSlicesRemaining() + var1;
            if (var4 > 0) {
               var3.setSlicesRemaining(var4);
               var2.update(true);
               return var4;
            } else {
               var0.breakNaturally();
               return 0;
            }
         }
      }
   }

   public static void setEnderPearlOnFrame(Block var0, boolean var1) {
      BlockState var2 = var0.getState();
      if (ISFLAT) {
         BlockData var3 = var2.getBlockData();
         EndPortalFrame var4 = (EndPortalFrame)var3;
         var4.setEye(var1);
         var2.setBlockData(var3);
      } else {
         var2.setRawData((byte)(var1 ? 4 : 0));
      }

      var2.update(true);
   }

   public static boolean isSimilar(Block var0, XMaterial var1) {
      if (var1 == getType(var0)) {
         return true;
      } else {
         Material var2 = var0.getType();
         if (var1.name().endsWith("_BED") && !XMaterial.supports(12)) {
            return var2 == XBlock.LegacyBlockMaterial.BED_BLOCK.material || var2 == XBlock.LegacyBlockMaterial.BED.material;
         } else {
            switch(var1) {
            case CAKE:
               return isCake(var2);
            case SUGAR_CANE:
               if (!ISFLAT) {
                  return var2 == XBlock.LegacyBlockMaterial.SUGAR_CANE_BLOCK.material;
               }

               return var2 == Material.SUGAR_CANE;
            case POTATOES:
            case POTATO:
               if (!ISFLAT) {
                  return var2 == Material.POTATO;
               }

               return var2 == Material.POTATOES;
            case CARROT:
            case CARROTS:
               if (!ISFLAT) {
                  return var2 == Material.CARROT;
               }

               return var2 == Material.CARROTS;
            case WHEAT_SEEDS:
            case WHEAT:
               if (!ISFLAT) {
                  return var2 == XBlock.LegacyBlockMaterial.CROPS.material;
               }

               return var2 == Material.WHEAT;
            case PLAYER_HEAD:
            case PLAYER_WALL_HEAD:
            case DRAGON_HEAD:
            case DRAGON_WALL_HEAD:
            case ZOMBIE_HEAD:
            case ZOMBIE_WALL_HEAD:
            case CREEPER_HEAD:
            case CREEPER_WALL_HEAD:
            case SKELETON_SKULL:
            case SKELETON_WALL_SKULL:
            case WITHER_SKELETON_SKULL:
            case WITHER_SKELETON_WALL_SKULL:
            case PIGLIN_HEAD:
            case PIGLIN_WALL_HEAD:
            default:
               return false;
            case NETHER_WART:
            case NETHER_WART_BLOCK:
               if (!ISFLAT) {
                  return var2 == XBlock.LegacyBlockMaterial.NETHER_WARTS.material;
               }

               return var2 == Material.NETHER_WART;
            case MELON:
            case MELON_SLICE:
               if (!ISFLAT) {
                  return var2 == XBlock.LegacyBlockMaterial.MELON_BLOCK.material;
               }

               return var2 == Material.MELON;
            case BEETROOT:
            case BEETROOT_SEEDS:
            case BEETROOTS:
               if (!ISFLAT) {
                  return var2 == XBlock.LegacyBlockMaterial.BEETROOT_BLOCK.material;
               }

               return var2 == Material.BEETROOTS;
            case WATER:
               return var2 == Material.WATER || var2 == XBlock.LegacyBlockMaterial.STATIONARY_WATER.material;
            case LAVA:
               return var2 == Material.LAVA || var2 == XBlock.LegacyBlockMaterial.STATIONARY_LAVA.material;
            case AIR:
            case CAVE_AIR:
            case VOID_AIR:
               return isAir(var2);
            }
         }
      }
   }

   public static boolean isAir(@Nullable Material var0) {
      if (ISFLAT) {
         switch(var0) {
         case AIR:
         case CAVE_AIR:
         case VOID_AIR:
            return true;
         default:
            return false;
         }
      } else {
         return var0 == Material.AIR;
      }
   }

   public static boolean isPowered(Block var0) {
      if (ISFLAT) {
         if (!(var0.getBlockData() instanceof Powerable)) {
            return false;
         } else {
            Powerable var2 = (Powerable)var0.getBlockData();
            return var2.isPowered();
         }
      } else {
         String var1 = var0.getType().name();
         if (var1.startsWith("REDSTONE_COMPARATOR")) {
            return var0.getType() == XBlock.LegacyBlockMaterial.REDSTONE_COMPARATOR_ON.material;
         } else {
            return false;
         }
      }
   }

   public static void setPowered(Block var0, boolean var1) {
      if (ISFLAT) {
         if (var0.getBlockData() instanceof Powerable) {
            BlockData var4 = var0.getBlockData();
            Powerable var3 = (Powerable)var4;
            var3.setPowered(var1);
            var0.setBlockData(var4, false);
         }
      } else {
         String var2 = var0.getType().name();
         if (var2.startsWith("REDSTONE_COMPARATOR")) {
            var0.setType(XBlock.LegacyBlockMaterial.REDSTONE_COMPARATOR_ON.material);
         }

      }
   }

   public static boolean isOpen(Block var0) {
      if (ISFLAT) {
         if (!(var0.getBlockData() instanceof Openable)) {
            return false;
         } else {
            Openable var3 = (Openable)var0.getBlockData();
            return var3.isOpen();
         }
      } else {
         BlockState var1 = var0.getState();
         if (!(var1 instanceof org.bukkit.material.Openable)) {
            return false;
         } else {
            org.bukkit.material.Openable var2 = (org.bukkit.material.Openable)var1.getData();
            return var2.isOpen();
         }
      }
   }

   public static void setOpened(Block var0, boolean var1) {
      if (ISFLAT) {
         if (var0.getBlockData() instanceof Openable) {
            BlockData var4 = var0.getBlockData();
            Openable var5 = (Openable)var4;
            var5.setOpen(var1);
            var0.setBlockData(var4, false);
         }
      } else {
         BlockState var2 = var0.getState();
         if (var2 instanceof org.bukkit.material.Openable) {
            org.bukkit.material.Openable var3 = (org.bukkit.material.Openable)var2.getData();
            var3.setOpen(var1);
            var2.setData((MaterialData)var3);
            var2.update(true, true);
         }
      }
   }

   public static BlockFace getRotation(Block var0) {
      if (ISFLAT) {
         BlockData var1 = var0.getBlockData();
         if (var1 instanceof Rotatable) {
            return ((Rotatable)var1).getRotation();
         }

         if (var1 instanceof Directional) {
            return ((Directional)var1).getFacing();
         }
      } else {
         BlockState var3 = var0.getState();
         if (var3 instanceof Skull) {
            return ((Skull)var3).getRotation();
         }

         MaterialData var2 = var3.getData();
         if (var2 instanceof org.bukkit.material.Directional) {
            return ((org.bukkit.material.Directional)var2).getFacing();
         }
      }

      return null;
   }

   public static void setRotation(Block var0, BlockFace var1) {
      if (ISFLAT) {
         BlockData var2 = var0.getBlockData();
         if (var2 instanceof Rotatable) {
            ((Rotatable)var2).setRotation(var1);
         } else if (var2 instanceof Directional) {
            ((Directional)var2).setFacing(var1);
         }

         var0.setBlockData(var2, false);
      } else {
         BlockState var5 = var0.getState();
         if (var5 instanceof Skull) {
            ((Skull)var5).setRotation(var1);
         } else {
            MaterialData var3 = var5.getData();
            if (!(var3 instanceof org.bukkit.material.Directional)) {
               return;
            }

            org.bukkit.material.Directional var4 = (org.bukkit.material.Directional)var3;
            var4.setFacingDirection(var1);
         }

         var5.update(true, true);
      }

   }

   private static boolean isMaterial(Block var0, XBlock.LegacyBlockMaterial... var1) {
      Material var2 = var0.getType();
      XBlock.LegacyBlockMaterial[] var3 = var1;
      int var4 = var1.length;

      for(int var5 = 0; var5 < var4; ++var5) {
         XBlock.LegacyBlockMaterial var6 = var3[var5];
         if (var2 == var6.material) {
            return true;
         }
      }

      return false;
   }

   static {
      CROPS = Collections.unmodifiableSet(EnumSet.of(XMaterial.CARROT, XMaterial.CARROTS, XMaterial.POTATO, XMaterial.POTATOES, XMaterial.NETHER_WART, XMaterial.PUMPKIN_SEEDS, XMaterial.WHEAT_SEEDS, XMaterial.WHEAT, XMaterial.MELON_SEEDS, XMaterial.BEETROOT_SEEDS, XMaterial.BEETROOTS, XMaterial.SUGAR_CANE, XMaterial.BAMBOO_SAPLING, XMaterial.BAMBOO, XMaterial.CHORUS_PLANT, XMaterial.KELP, XMaterial.KELP_PLANT, XMaterial.SEA_PICKLE, XMaterial.BROWN_MUSHROOM, XMaterial.RED_MUSHROOM, XMaterial.MELON_STEM, XMaterial.PUMPKIN_STEM, XMaterial.COCOA, XMaterial.COCOA_BEANS));
      DANGEROUS = Collections.unmodifiableSet(EnumSet.of(XMaterial.MAGMA_BLOCK, XMaterial.LAVA, XMaterial.CAMPFIRE, XMaterial.FIRE, XMaterial.SOUL_FIRE));
      ISFLAT = XMaterial.supports(13);
      ITEM_TO_BLOCK = new EnumMap(XMaterial.class);
      ITEM_TO_BLOCK.put(XMaterial.MELON_SLICE, XMaterial.MELON_STEM);
      ITEM_TO_BLOCK.put(XMaterial.MELON_SEEDS, XMaterial.MELON_STEM);
      ITEM_TO_BLOCK.put(XMaterial.CARROT_ON_A_STICK, XMaterial.CARROTS);
      ITEM_TO_BLOCK.put(XMaterial.GOLDEN_CARROT, XMaterial.CARROTS);
      ITEM_TO_BLOCK.put(XMaterial.CARROT, XMaterial.CARROTS);
      ITEM_TO_BLOCK.put(XMaterial.POTATO, XMaterial.POTATOES);
      ITEM_TO_BLOCK.put(XMaterial.BAKED_POTATO, XMaterial.POTATOES);
      ITEM_TO_BLOCK.put(XMaterial.POISONOUS_POTATO, XMaterial.POTATOES);
      ITEM_TO_BLOCK.put(XMaterial.PUMPKIN_SEEDS, XMaterial.PUMPKIN_STEM);
      ITEM_TO_BLOCK.put(XMaterial.PUMPKIN_PIE, XMaterial.PUMPKIN);
   }

   private static enum LegacyBlockMaterial {
      CAKE_BLOCK,
      CROPS,
      SUGAR_CANE_BLOCK,
      BEETROOT_BLOCK,
      NETHER_WARTS,
      MELON_BLOCK,
      BED,
      BED_BLOCK,
      CARROT,
      POTATO,
      BURNING_FURNACE,
      STATIONARY_WATER,
      STATIONARY_LAVA,
      REDSTONE_LAMP_ON,
      REDSTONE_LAMP_OFF,
      REDSTONE_TORCH_ON,
      REDSTONE_TORCH_OFF,
      REDSTONE_COMPARATOR_ON,
      REDSTONE_COMPARATOR_OFF;

      @Nullable
      private final Material material = Material.getMaterial(this.name());

      // $FF: synthetic method
      private static XBlock.LegacyBlockMaterial[] $values() {
         return new XBlock.LegacyBlockMaterial[]{CAKE_BLOCK, CROPS, SUGAR_CANE_BLOCK, BEETROOT_BLOCK, NETHER_WARTS, MELON_BLOCK, BED, BED_BLOCK, CARROT, POTATO, BURNING_FURNACE, STATIONARY_WATER, STATIONARY_LAVA, REDSTONE_LAMP_ON, REDSTONE_LAMP_OFF, REDSTONE_TORCH_ON, REDSTONE_TORCH_OFF, REDSTONE_COMPARATOR_ON, REDSTONE_COMPARATOR_OFF};
      }
   }

   private static enum LegacyMaterialGroup {
      STANDING_BANNER(XBlock.LegacyMaterialGroup.Handling.COLORABLE),
      WALL_BANNER(XBlock.LegacyMaterialGroup.Handling.COLORABLE),
      BANNER(XBlock.LegacyMaterialGroup.Handling.COLORABLE),
      CARPET(XBlock.LegacyMaterialGroup.Handling.COLORABLE),
      WOOL(XBlock.LegacyMaterialGroup.Handling.COLORABLE),
      STAINED_CLAY(XBlock.LegacyMaterialGroup.Handling.COLORABLE),
      STAINED_GLASS(XBlock.LegacyMaterialGroup.Handling.COLORABLE),
      STAINED_GLASS_PANE(XBlock.LegacyMaterialGroup.Handling.COLORABLE),
      THIN_GLASS(XBlock.LegacyMaterialGroup.Handling.COLORABLE),
      STONE,
      QUARTZ_BLOCK,
      SKULL,
      RED_ROSE,
      FLOWER_POT,
      DOUBLE_PLANT,
      LONG_GRASS,
      DIRT,
      SAND,
      SANDSTONE,
      RED_SANDSTONE,
      SPONGE,
      PRISMARINE,
      CONCRETE,
      CONCRETE_POWDER,
      ANVIL,
      SMOOTH_BRICK,
      COBBLE_WALL,
      BED,
      BED_BLOCK,
      MONSTER_EGGS,
      WOOD(XBlock.LegacyMaterialGroup.Handling.WOOD_SPECIES),
      WOOD_STEP(XBlock.LegacyMaterialGroup.Handling.WOOD_SPECIES),
      WOOD_DOUBLE_STEP(XBlock.LegacyMaterialGroup.Handling.WOOD_SPECIES),
      LEAVES(XBlock.LegacyMaterialGroup.Handling.WOOD_SPECIES),
      LEAVES_2(XBlock.LegacyMaterialGroup.Handling.WOOD_SPECIES),
      LOG(XBlock.LegacyMaterialGroup.Handling.WOOD_SPECIES),
      LOG_2(XBlock.LegacyMaterialGroup.Handling.WOOD_SPECIES),
      SAPLING(XBlock.LegacyMaterialGroup.Handling.WOOD_SPECIES),
      BRICK,
      STEP,
      DOUBLE_STEP,
      DOUBLE_STONE_SLAB2;

      private static final Map<String, XBlock.LegacyMaterialGroup> LOOKUP = new HashMap();
      private final Material material;
      private final XBlock.LegacyMaterialGroup.Handling handling;

      private LegacyMaterialGroup(XBlock.LegacyMaterialGroup.Handling param3) {
         this.material = Material.getMaterial(this.name());
         this.handling = var3;
      }

      private LegacyMaterialGroup() {
         this((XBlock.LegacyMaterialGroup.Handling)null);
      }

      private static XBlock.LegacyMaterialGroup getMaterial(String var0) {
         return (XBlock.LegacyMaterialGroup)LOOKUP.get(var0);
      }

      // $FF: synthetic method
      private static XBlock.LegacyMaterialGroup[] $values() {
         return new XBlock.LegacyMaterialGroup[]{STANDING_BANNER, WALL_BANNER, BANNER, CARPET, WOOL, STAINED_CLAY, STAINED_GLASS, STAINED_GLASS_PANE, THIN_GLASS, STONE, QUARTZ_BLOCK, SKULL, RED_ROSE, FLOWER_POT, DOUBLE_PLANT, LONG_GRASS, DIRT, SAND, SANDSTONE, RED_SANDSTONE, SPONGE, PRISMARINE, CONCRETE, CONCRETE_POWDER, ANVIL, SMOOTH_BRICK, COBBLE_WALL, BED, BED_BLOCK, MONSTER_EGGS, WOOD, WOOD_STEP, WOOD_DOUBLE_STEP, LEAVES, LEAVES_2, LOG, LOG_2, SAPLING, BRICK, STEP, DOUBLE_STEP, DOUBLE_STONE_SLAB2};
      }

      static {
         XBlock.LegacyMaterialGroup[] var0 = values();
         int var1 = var0.length;

         for(int var2 = 0; var2 < var1; ++var2) {
            XBlock.LegacyMaterialGroup var3 = var0[var2];
            LOOKUP.put(var3.name(), var3);
         }

      }

      private static enum Handling {
         XMaterial,
         COLORABLE,
         WOOD_SPECIES;

         // $FF: synthetic method
         private static XBlock.LegacyMaterialGroup.Handling[] $values() {
            return new XBlock.LegacyMaterialGroup.Handling[]{XMaterial, COLORABLE, WOOD_SPECIES};
         }
      }
   }
}
