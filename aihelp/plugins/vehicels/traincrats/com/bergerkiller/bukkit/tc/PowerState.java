package com.bergerkiller.bukkit.tc;

import com.bergerkiller.bukkit.common.utils.BlockUtil;
import com.bergerkiller.bukkit.common.utils.FaceUtil;
import com.bergerkiller.bukkit.common.utils.MaterialUtil;
import com.bergerkiller.bukkit.common.utils.WorldUtil;
import com.bergerkiller.bukkit.common.wrappers.BlockData;
import java.util.EnumMap;
import java.util.Locale;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.material.Diode;
import org.bukkit.material.Lever;
import org.bukkit.material.MaterialData;
import org.bukkit.material.PressureSensor;
import org.bukkit.material.Redstone;
import org.bukkit.material.RedstoneTorch;
import org.bukkit.material.RedstoneWire;

public enum PowerState {
   ON,
   OFF,
   NONE;

   private static EnumMap<BlockFace, String> redstoneWireSideKey = new EnumMap(BlockFace.class);

   private static boolean isDistractingColumn(Block main, BlockFace face) {
      Block side = main.getRelative(face);
      BlockData side_data = WorldUtil.getBlockData(side);
      if ((Boolean)MaterialUtil.ISPOWERSOURCE.get(side_data)) {
         return true;
      } else {
         if (side_data.isType(Material.AIR)) {
            if ((Boolean)MaterialUtil.ISPOWERSOURCE.get(side.getRelative(BlockFace.DOWN))) {
               return true;
            }
         } else if (MaterialUtil.ISDIODE.get(side_data)) {
            BlockFace facing = BlockUtil.getFacing(side);
            return facing == face;
         }

         BlockData up_data = WorldUtil.getBlockData(main.getRelative(BlockFace.UP));
         return up_data.isType(Material.AIR) ? (Boolean)MaterialUtil.ISPOWERSOURCE.get(side.getRelative(BlockFace.UP)) : false;
      }
   }

   private static boolean isDistracted(Block wire, BlockFace face) {
      return isDistractingColumn(wire, FaceUtil.rotate(face, -2)) || isDistractingColumn(wire, FaceUtil.rotate(face, 2));
   }

   public static PowerState get(Block block, BlockFace from) {
      return get(block, from, PowerState.Options.SIGN);
   }

   public static PowerState get(Block block, BlockFace from, boolean useSignLogic) {
      return get(block, from, useSignLogic ? PowerState.Options.SIGN : PowerState.Options.FAR);
   }

   public static PowerState get(Block block, BlockFace from, PowerState.Options options) {
      Block fromBlock = block.getRelative(from);
      BlockData fromBlockInfo = WorldUtil.getBlockData(fromBlock);
      MaterialData fromBlockData = fromBlockInfo.getMaterialData();
      if (fromBlockData instanceof RedstoneTorch) {
         if (!options.isNextToSign() && from != BlockFace.DOWN) {
            return NONE;
         } else {
            return ((RedstoneTorch)fromBlockData).isPowered() ? ON : OFF;
         }
      } else if (fromBlockData instanceof Diode && !FaceUtil.isVertical(from)) {
         Diode diode = (Diode)fromBlockData;
         if (diode.getFacing().getOppositeFace() == from) {
            return fromBlockInfo.isType(MaterialUtil.getMaterial("LEGACY_DIODE_BLOCK_ON")) ? ON : OFF;
         } else {
            return NONE;
         }
      } else if (fromBlockData instanceof RedstoneWire) {
         if (options == PowerState.Options.SIGN_CONNECT_WIRE && !FaceUtil.isVertical(from)) {
            String sideKey = (String)redstoneWireSideKey.get(from.getOppositeFace());
            if (sideKey != null) {
               BlockData updated = fromBlockInfo.setState(sideKey, "side");
               if (fromBlockInfo != updated) {
                  WorldUtil.setBlockDataFast(fromBlock, updated);
               }
            }
         }

         if (!options.isNextToSign() && from != BlockFace.UP && (from == BlockFace.DOWN || isDistracted(fromBlock, from))) {
            return NONE;
         } else {
            return ((RedstoneWire)fromBlockData).isPowered() ? ON : OFF;
         }
      } else if (fromBlockData instanceof Lever && !options.isNextToSign()) {
         return NONE;
      } else {
         if (fromBlockInfo.isPowerSource()) {
            if (fromBlockData instanceof Redstone) {
               return ((Redstone)fromBlockData).isPowered() ? ON : OFF;
            }

            if (fromBlockData instanceof PressureSensor) {
               return ((PressureSensor)fromBlockData).isPressed() ? ON : OFF;
            }
         }

         if (options.isNextToSign() && BlockUtil.getAttachedFace(block) == from) {
            PowerState state = NONE;
            BlockFace[] var7 = FaceUtil.BLOCK_SIDES;
            int var8 = var7.length;

            for(int var9 = 0; var9 < var8; ++var9) {
               BlockFace attFace = var7[var9];
               if (attFace != from.getOppositeFace()) {
                  PowerState attState = get(fromBlock, attFace, PowerState.Options.FAR);
                  if (attState != NONE) {
                     state = attState;
                     if (attState == ON) {
                        break;
                     }
                  }
               }
            }

            return state;
         } else {
            return NONE;
         }
      }
   }

   public static boolean isSignPowered(Block signBlock) {
      return isSignPowered(signBlock, false);
   }

   public static boolean isSignPowered(Block signBlock, boolean inverted) {
      return isSignPowered(signBlock, PowerState.Options.SIGN_CONNECT_WIRE, inverted);
   }

   public static boolean isSignPowered(Block signBlock, PowerState.Options options) {
      return isSignPowered(signBlock, options, false);
   }

   public static boolean isSignPowered(Block signBlock, PowerState.Options options, boolean inverted) {
      boolean result;
      BlockFace[] var4;
      int var5;
      int var6;
      BlockFace face;
      if (inverted) {
         result = true;
         var4 = FaceUtil.BLOCK_SIDES;
         var5 = var4.length;

         for(var6 = 0; var6 < var5; ++var6) {
            face = var4[var6];
            result &= get(signBlock, face, options) != ON;
         }

         return result;
      } else {
         result = false;
         var4 = FaceUtil.BLOCK_SIDES;
         var5 = var4.length;

         for(var6 = 0; var6 < var5; ++var6) {
            face = var4[var6];
            result |= get(signBlock, face, options).hasPower();
         }

         return result;
      }
   }

   public boolean hasPower() {
      switch(this) {
      case ON:
         return true;
      default:
         return false;
      }
   }

   // $FF: synthetic method
   private static PowerState[] $values() {
      return new PowerState[]{ON, OFF, NONE};
   }

   static {
      BlockFace[] var0 = FaceUtil.AXIS;
      int var1 = var0.length;

      for(int var2 = 0; var2 < var1; ++var2) {
         BlockFace face = var0[var2];
         redstoneWireSideKey.put(face, face.name().toLowerCase(Locale.ENGLISH));
      }

   }

   public static enum Options {
      FAR(false),
      SIGN(true),
      SIGN_CONNECT_WIRE(true);

      private final boolean isNextToSign;

      private Options(boolean isNextToSign) {
         this.isNextToSign = isNextToSign;
      }

      public boolean isNextToSign() {
         return this.isNextToSign;
      }

      // $FF: synthetic method
      private static PowerState.Options[] $values() {
         return new PowerState.Options[]{FAR, SIGN, SIGN_CONNECT_WIRE};
      }
   }
}
