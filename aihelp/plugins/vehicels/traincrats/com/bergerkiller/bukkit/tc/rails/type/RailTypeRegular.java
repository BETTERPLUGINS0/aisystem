package com.bergerkiller.bukkit.tc.rails.type;

import com.bergerkiller.bukkit.common.map.MapBlendMode;
import com.bergerkiller.bukkit.common.map.MapColorPalette;
import com.bergerkiller.bukkit.common.map.MapTexture;
import com.bergerkiller.bukkit.common.utils.BlockUtil;
import com.bergerkiller.bukkit.common.utils.FaceUtil;
import com.bergerkiller.bukkit.common.utils.MaterialUtil;
import com.bergerkiller.bukkit.common.utils.WorldUtil;
import com.bergerkiller.bukkit.common.wrappers.BlockData;
import com.bergerkiller.bukkit.tc.TCConfig;
import com.bergerkiller.bukkit.tc.TrainCarts;
import com.bergerkiller.bukkit.tc.Util;
import com.bergerkiller.bukkit.tc.controller.components.RailJunction;
import com.bergerkiller.bukkit.tc.controller.components.RailState;
import com.bergerkiller.bukkit.tc.editor.RailsTexture;
import com.bergerkiller.bukkit.tc.rails.logic.RailLogic;
import com.bergerkiller.bukkit.tc.rails.logic.RailLogicGround;
import com.bergerkiller.bukkit.tc.rails.logic.RailLogicHorizontal;
import com.bergerkiller.bukkit.tc.rails.logic.RailLogicSloped;
import com.bergerkiller.bukkit.tc.rails.logic.RailLogicVerticalSlopeNormalA;
import com.bergerkiller.bukkit.tc.rails.logic.RailLogicVerticalSlopeUpsideDownA;
import com.bergerkiller.bukkit.tc.utils.MinecartTrackLogic;
import java.util.Arrays;
import java.util.List;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.event.block.BlockPhysicsEvent;
import org.bukkit.material.Rails;

public class RailTypeRegular extends RailTypeHorizontal {
   public static BlockFace[] getPossibleDirections(BlockFace railDirection) {
      return FaceUtil.getFaces(railDirection.getOppositeFace());
   }

   public void onBlockPhysics(BlockPhysicsEvent event) {
      if (this.isUpsideDown(event.getBlock())) {
         event.setCancelled(true);
      }

   }

   public void onBlockPlaced(Block railsBlock) {
      if (this.isUpsideDown(railsBlock)) {
         MinecartTrackLogic logic = new MinecartTrackLogic(railsBlock);
         logic.update(railsBlock.isBlockIndirectlyPowered(), true);
      }

      Rails rails = BlockUtil.getRails(railsBlock);
      if (rails != null && !rails.isCurve() && !rails.isOnSlope()) {
         Block above = railsBlock.getRelative(BlockFace.UP);
         if ((Boolean)Util.ISVERTRAIL.get(above)) {
            BlockFace railDir = rails.getDirection();
            BlockFace dir = Util.getVerticalRailDirection(above);
            if (railDir != dir && railDir != dir.getOppositeFace()) {
               if (Util.getRailsBlock(railsBlock.getRelative(railDir)) != null) {
                  return;
               }

               if (Util.getRailsBlock(railsBlock.getRelative(railDir.getOppositeFace())) != null) {
                  return;
               }
            }

            if (BlockUtil.isSuffocating(railsBlock.getRelative(dir))) {
               rails.setDirection(dir, true);
               TrainCarts.plugin.setBlockDataWithoutBreaking(railsBlock, BlockData.fromMaterialData(rails));
            }
         }

         if (this.isUpsideDown(railsBlock, rails)) {
            BlockFace[] var10 = FaceUtil.AXIS;
            int var11 = var10.length;

            for(int var6 = 0; var6 < var11; ++var6) {
               BlockFace face = var10[var6];
               Block aboveAt = above.getRelative(face);
               if ((Boolean)Util.ISVERTRAIL.get(aboveAt)) {
                  rails.setDirection(face, true);
                  TrainCarts.plugin.setBlockDataWithoutBreaking(railsBlock, BlockData.fromMaterialData(rails));
                  break;
               }
            }
         }

      }
   }

   public boolean isSlopeUpwardsTo(Block railsBlock, BlockFace direction) {
      if (!TCConfig.allowUpsideDownRails) {
         return false;
      } else {
         Rails rails = Util.getRailsRO(railsBlock);
         return rails != null && rails.isOnSlope() && rails.getDirection() == direction;
      }
   }

   public boolean isUpsideDown(Block railsBlock) {
      return this.isUpsideDown(railsBlock, (Rails)null);
   }

   protected boolean isUpsideDown(Block railsBlock, Rails rails) {
      if (!TCConfig.allowUpsideDownRails) {
         return false;
      } else {
         Block blockAbove = railsBlock.getRelative(BlockFace.UP);
         if (!Util.isUpsideDownRailSupport(blockAbove)) {
            return false;
         } else if (BlockUtil.canSupportTop(railsBlock.getRelative(BlockFace.DOWN))) {
            return false;
         } else {
            if (rails == null) {
               rails = Util.getRailsRO(railsBlock);
            }

            if (rails == null) {
               return false;
            } else if (rails.isOnSlope()) {
               Block nextBlock = railsBlock.getRelative(rails.getDirection().getOppositeFace());
               BlockData nextBlockData = WorldUtil.getBlockData(nextBlock);
               if (!nextBlockData.isSuffocating(nextBlock)) {
                  RailType railType = RailType.getType(nextBlock, nextBlockData);
                  if (railType == RailType.NONE) {
                     return false;
                  }
               }

               return true;
            } else {
               return true;
            }
         }
      }
   }

   public static Block getNextPos(Block currentTrack, BlockFace currentDirection, BlockFace railDirection, boolean sloped) {
      return getNextPos(currentTrack, currentDirection, railDirection, sloped, false);
   }

   public static Block getNextPos(Block currentTrack, BlockFace currentDirection, BlockFace railDirection, boolean sloped, boolean upsideDown) {
      Block result;
      if (FaceUtil.isSubCardinal(railDirection)) {
         BlockFace[] possible = FaceUtil.getFaces(railDirection.getOppositeFace());
         boolean isSimpleForward = false;
         BlockFace[] var8 = possible;
         int var9 = possible.length;

         for(int var10 = 0; var10 < var9; ++var10) {
            BlockFace newdir = var8[var10];
            if (newdir == currentDirection) {
               isSimpleForward = true;
               break;
            }
         }

         if (isSimpleForward) {
            result = currentTrack.getRelative(currentDirection);
         } else {
            BlockFace dir;
            if (FaceUtil.isVertical(currentDirection)) {
               dir = possible[1];
               result = currentTrack.getRelative(dir);
            } else {
               dir = currentDirection.getOppositeFace();
               BlockFace nextDir;
               if (possible[0].equals(dir)) {
                  nextDir = possible[1];
               } else if (possible[1].equals(dir)) {
                  nextDir = possible[0];
               } else if (possible[0] != BlockFace.SOUTH && possible[0] != BlockFace.EAST) {
                  nextDir = possible[1];
               } else {
                  nextDir = possible[0];
               }

               result = currentTrack.getRelative(nextDir);
            }
         }
      } else if (sloped) {
         Block above;
         if (railDirection != currentDirection && currentDirection != BlockFace.UP) {
            above = currentTrack.getRelative(BlockFace.DOWN);
            if (upsideDown && (Boolean)Util.ISVERTRAIL.get(above)) {
               result = currentTrack;
            } else {
               result = currentTrack.getRelative(railDirection.getOppositeFace());
            }
         } else {
            above = currentTrack.getRelative(BlockFace.UP);
            if (!RailType.VERTICAL.isRail(above) || currentDirection != BlockFace.UP && Util.getVerticalRailDirection(above) != currentDirection) {
               result = above.getRelative(railDirection);
            } else {
               result = above;
            }
         }
      } else if (railDirection != currentDirection && railDirection.getOppositeFace() != currentDirection) {
         result = currentTrack.getRelative(railDirection);
      } else {
         result = currentTrack.getRelative(currentDirection);
      }

      if (upsideDown) {
         result = result.getRelative(BlockFace.DOWN);
      }

      return result;
   }

   public boolean isRail(BlockData blockData) {
      return blockData.isType(RailTypeRegular.RailMaterials.REGULAR);
   }

   public BlockFace[] getPossibleDirections(Block trackBlock) {
      Rails rails = Util.getRailsRO(trackBlock);
      if (rails == null) {
         return new BlockFace[0];
      } else {
         return rails.isOnSlope() && Util.isVerticalAbove(trackBlock, rails.getDirection()) ? new BlockFace[]{rails.getDirection().getOppositeFace(), BlockFace.UP} : getPossibleDirections(rails.getDirection());
      }
   }

   public List<RailJunction> getJunctions(Block railBlock) {
      Rails rails = Util.getRailsRO(railBlock);
      return rails != null && !rails.isOnSlope() ? Arrays.asList(new RailJunction("n", RailLogicHorizontal.get(BlockFace.NORTH).getPath().getStartPosition()), new RailJunction("e", RailLogicHorizontal.get(BlockFace.EAST).getPath().getEndPosition()), new RailJunction("s", RailLogicHorizontal.get(BlockFace.SOUTH).getPath().getEndPosition()), new RailJunction("w", RailLogicHorizontal.get(BlockFace.WEST).getPath().getStartPosition())) : super.getJunctions(railBlock);
   }

   public void switchJunction(Block railBlock, RailJunction from, RailJunction to) {
      TrainCarts.plugin.getSignController().suppressRedstonePhysicsDuring(() -> {
         BlockUtil.setRails(railBlock, juncToFace(from), juncToFace(to));
      });
   }

   private static final BlockFace juncToFace(RailJunction junction) {
      String var1 = junction.name();
      byte var2 = -1;
      switch(var1.hashCode()) {
      case 101:
         if (var1.equals("e")) {
            var2 = 1;
         }
         break;
      case 110:
         if (var1.equals("n")) {
            var2 = 0;
         }
         break;
      case 115:
         if (var1.equals("s")) {
            var2 = 2;
         }
         break;
      case 119:
         if (var1.equals("w")) {
            var2 = 3;
         }
      }

      switch(var2) {
      case 0:
         return BlockFace.NORTH;
      case 1:
         return BlockFace.EAST;
      case 2:
         return BlockFace.SOUTH;
      case 3:
         return BlockFace.WEST;
      default:
         return BlockFace.NORTH;
      }
   }

   public RailLogicHorizontal getLogicForRails(Block railsBlock, Rails rails, BlockFace enterFace) {
      BlockFace direction = rails.getDirection();
      boolean upsideDown = this.isUpsideDown(railsBlock, rails);
      if (rails.isOnSlope()) {
         if (Util.isVerticalAbove(railsBlock, direction)) {
            return RailLogicVerticalSlopeNormalA.get(direction);
         } else {
            return (RailLogicHorizontal)(upsideDown && Util.isVerticalBelow(railsBlock, direction.getOppositeFace()) ? RailLogicVerticalSlopeUpsideDownA.get(direction) : RailLogicSloped.get(direction, upsideDown));
         }
      } else {
         if (rails.isCurve()) {
            BlockFace[] faces = FaceUtil.getFaces(direction);
            if (enterFace == faces[0].getOppositeFace() || enterFace == faces[1].getOppositeFace()) {
               return RailLogicHorizontal.get(enterFace);
            }
         } else {
            BlockFace sideFace = FaceUtil.rotate(direction, 2);
            if (enterFace == sideFace || enterFace == sideFace.getOppositeFace()) {
               BlockFace curvedDir = FaceUtil.combine(enterFace, direction.getOppositeFace());
               return RailLogicHorizontal.get(curvedDir);
            }
         }

         return RailLogicHorizontal.get(direction, upsideDown);
      }
   }

   public RailLogic getLogic(RailState state) {
      Rails rails = Util.getRailsRO(state.railBlock());
      return (RailLogic)(rails == null ? RailLogicGround.INSTANCE : this.getLogicForRails(state.railBlock(), rails, state.enterFace()));
   }

   public BlockFace getDirection(Block railsBlock) {
      Rails rails = Util.getRailsRO(railsBlock);
      return rails == null ? BlockFace.SELF : rails.getDirection();
   }

   public Location getSpawnLocation(Block railsBlock, BlockFace orientation) {
      Rails rails = Util.getRailsRO(railsBlock);
      if (rails == null) {
         return super.getSpawnLocation(railsBlock, orientation);
      } else {
         BlockFace dir = FaceUtil.getRailsCartDirection(rails.getDirection());
         if (FaceUtil.getFaceYawDifference(dir.getOppositeFace(), orientation) < 90) {
            dir = dir.getOppositeFace();
         }

         Location result = super.getSpawnLocation(railsBlock, dir);
         if (rails.isOnSlope()) {
            result.setPitch(result.getPitch() - 45.0F);
            result.setY(result.getY() + 0.5D);
         }

         return result;
      }
   }

   public RailsTexture getRailsTexture(Block railsBlock) {
      Rails rails = Util.getRailsRO(railsBlock);
      if (rails == null) {
         return super.getRailsTexture(railsBlock);
      } else {
         BlockFace direction = rails.getDirection();
         MapTexture top;
         MapTexture top;
         if (!FaceUtil.isSubCardinal(direction)) {
            MapTexture front;
            if (rails.isOnSlope()) {
               front = this.getResource(rails, "side");
               top = this.getResource(rails, "top");
               top = top.clone();
               top.setBlendMode(MapBlendMode.MULTIPLY).fill(MapColorPalette.getColor(160, 160, 160));
               return (new RailsTexture()).set(direction.getOppositeFace(), top).set(direction, MapTexture.flipV(top)).set(BlockFace.UP, MapTexture.rotate(top, -FaceUtil.faceToYaw(direction))).set(BlockFace.DOWN, MapTexture.rotate(top, FaceUtil.faceToYaw(direction))).setOpposites(FaceUtil.rotate(direction, 2), front);
            } else {
               front = this.getResource(rails, "front");
               top = this.getResource(rails, "side");
               top = MapTexture.rotate(this.getResource(rails, "top"), FaceUtil.faceToYaw(direction));
               MapTexture back = top.clone();
               back.setBlendMode(MapBlendMode.MULTIPLY).fill(MapColorPalette.getColor(160, 160, 160));
               return (new RailsTexture()).set(BlockFace.UP, top).set(BlockFace.DOWN, back).setOpposites(direction, front).setOpposites(FaceUtil.rotate(direction, 2), top);
            }
         } else {
            int yaw = 45 - FaceUtil.faceToYaw(direction);
            top = MapTexture.rotate(this.getResource(rails, "top"), yaw);
            top = top.clone();
            top.setBlendMode(MapBlendMode.MULTIPLY).fill(MapColorPalette.getColor(160, 160, 160));
            top = MapTexture.flipV(top);
            RailsTexture result = (new RailsTexture()).set(BlockFace.UP, top).set(BlockFace.DOWN, top);
            BlockFace[] var8 = FaceUtil.AXIS;
            int var9 = var8.length;

            for(int var10 = 0; var10 < var9; ++var10) {
               BlockFace face = var8[var10];
               result.set(face, MapTexture.rotate(top, FaceUtil.faceToYaw(face)));
            }

            return result;
         }
      }
   }

   public String toString() {
      return this.getClass().getSimpleName();
   }

   protected String getRailsTexturePath(Rails rails, String name) {
      if (rails.isCurve()) {
         return "com/bergerkiller/bukkit/tc/textures/rails/regular_curved_" + name + ".png";
      } else {
         return rails.isOnSlope() ? "com/bergerkiller/bukkit/tc/textures/rails/regular_sloped_" + name + ".png" : "com/bergerkiller/bukkit/tc/textures/rails/regular_straight_" + name + ".png";
      }
   }

   private MapTexture getResource(Rails rails, String name) {
      return MapTexture.loadPluginResource(TrainCarts.plugin, this.getRailsTexturePath(rails, name));
   }

   protected static class RailMaterials {
      public static final Material REGULAR = MaterialUtil.getFirst(new String[]{"RAIL", "LEGACY_RAILS"});
      public static final Material DETECTOR;
      public static final Material POWERED;
      public static final Material ACTIVATOR;

      static {
         DETECTOR = Material.DETECTOR_RAIL;
         POWERED = Material.POWERED_RAIL;
         ACTIVATOR = Material.ACTIVATOR_RAIL;
      }
   }
}
