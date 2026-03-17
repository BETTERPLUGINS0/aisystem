package com.bergerkiller.bukkit.tc.rails.type;

import com.bergerkiller.bukkit.common.bases.IntVector3;
import com.bergerkiller.bukkit.common.entity.type.CommonMinecart;
import com.bergerkiller.bukkit.common.utils.BlockUtil;
import com.bergerkiller.bukkit.common.utils.FaceUtil;
import com.bergerkiller.bukkit.common.utils.LogicUtil;
import com.bergerkiller.bukkit.tc.TCConfig;
import com.bergerkiller.bukkit.tc.Util;
import com.bergerkiller.bukkit.tc.controller.MinecartMember;
import com.bergerkiller.bukkit.tc.controller.components.RailPiece;
import java.util.HashSet;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.material.Rails;

public abstract class RailTypeHorizontal extends RailType {
   public abstract BlockFace getDirection(Block var1);

   public Block findMinecartPos(Block trackBlock) {
      return this.isUpsideDown(trackBlock) ? trackBlock.getRelative(BlockFace.DOWN) : trackBlock;
   }

   public boolean onBlockCollision(MinecartMember<?> member, Block railsBlock, Block hitBlock, BlockFace hitFace) {
      if (!super.onBlockCollision(member, railsBlock, hitBlock, hitFace)) {
         return false;
      } else {
         boolean upsideDown = this.isUpsideDown(railsBlock);
         Block posBlock = this.findMinecartPos(railsBlock);
         int dx = hitBlock.getX() - posBlock.getX();
         int dy = hitBlock.getY() - posBlock.getY();
         int dz = hitBlock.getZ() - posBlock.getZ();
         if (dx >= -1 && dx <= 1 && dy >= -1 && dy <= 1 && dz >= -1 && dz <= 1) {
            BlockFace hitToFace;
            Block above;
            if (upsideDown) {
               hitToFace = this.getDirection(railsBlock);
               Block blockFwd = posBlock.getRelative(hitToFace);
               if (!BlockUtil.equals(posBlock, hitBlock) && !BlockUtil.equals(blockFwd, hitBlock)) {
                  above = posBlock.getRelative(hitToFace.getOppositeFace());
                  if (BlockUtil.equals(above, hitBlock)) {
                     if (!member.isOnSlope()) {
                        return true;
                     }

                     if (!RailType.VERTICAL.isRail(posBlock.getRelative(BlockFace.DOWN)) && !RailType.VERTICAL.isRail(above.getRelative(hitToFace))) {
                        return true;
                     }
                  }

                  if (member.isOnSlope() && dx == 0 && dy == -1 && dz == 0) {
                     return true;
                  } else {
                     return member.isOnSlope() && hitToFace.getModX() == -dx && hitToFace.getModZ() == -dz && dy == -1;
                  }
               } else {
                  return true;
               }
            } else if (hitBlock.getY() < posBlock.getY()) {
               return false;
            } else {
               hitFace = FaceUtil.getDirection(hitBlock, posBlock, false);
               hitToFace = hitFace.getOppositeFace();
               BlockFace railDirection;
               if (posBlock.getY() == hitBlock.getY()) {
                  if (Math.abs(dx) > 0 && Math.abs(dz) > 0) {
                     railDirection = this.getDirection(railsBlock);
                     if (!FaceUtil.isSubCardinal(railDirection)) {
                        return false;
                     }

                     BlockFace f = FaceUtil.rotate(railDirection, 2);
                     BlockFace hitDir = null;
                     if (f.getModX() == dx && f.getModZ() == dz) {
                        hitDir = FaceUtil.rotate(railDirection, 3);
                     } else if (f.getModX() == -dx && f.getModZ() == -dz) {
                        hitDir = FaceUtil.rotate(railDirection, -3);
                     }

                     if (hitDir != null) {
                        Block dirBlock = railsBlock.getRelative(hitDir);
                        RailType dirRail = RailType.getType(dirBlock);
                        if (dirRail == RailType.NONE) {
                           dirBlock = dirBlock.getRelative(BlockFace.DOWN);
                           dirRail = RailType.getType(dirBlock);
                        }

                        if (dirRail == RailType.NONE) {
                           return true;
                        }

                        Block nextPosBlock = Util.getNextPos(dirBlock, hitDir);
                        if (nextPosBlock != null && hitBlock.equals(nextPosBlock)) {
                           return true;
                        }
                     }

                     return false;
                  }

                  BlockFace[] possible = this.getPossibleDirections(railsBlock);
                  if (!LogicUtil.contains(hitToFace, possible)) {
                     return false;
                  }
               }

               if (member.isOnSlope()) {
                  if (hitBlock.getX() == posBlock.getX() && hitBlock.getZ() == posBlock.getZ() && dy >= 2) {
                     return false;
                  }

                  railDirection = this.getDirection(railsBlock);
                  if (hitToFace == railDirection) {
                     if (Util.isVerticalAbove(posBlock, railDirection)) {
                        return false;
                     }

                     if (posBlock.getY() == hitBlock.getY()) {
                        above = hitBlock.getRelative(BlockFace.UP);
                        if (!BlockUtil.isSolid(above)) {
                           return false;
                        }
                     }
                  }

                  if (FaceUtil.isAlongX(railDirection) && dz != 0) {
                     return false;
                  }

                  if (FaceUtil.isAlongZ(railDirection) && dx != 0) {
                     return false;
                  }

                  if (!TCConfig.enableCeilingBlockCollision) {
                     IntVector3 diff = (new IntVector3(hitBlock)).subtract(posBlock.getX(), posBlock.getY(), posBlock.getZ());
                     if (diff.x == hitToFace.getModX() && diff.z == hitToFace.getModZ() && (diff.y > 1 || diff.y == 1 && railDirection != hitToFace)) {
                        return false;
                     }
                  }
               }

               return true;
            }
         } else {
            return false;
         }
      }
   }

   public Location getSpawnLocation(Block railsBlock, BlockFace orientation) {
      BlockFace[] faces = this.getPossibleDirections(railsBlock);
      if (faces != null && faces.length >= 2) {
         if (faces[0] == faces[1].getOppositeFace()) {
            if (orientation != faces[0] && orientation != faces[1]) {
               orientation = faces[0];
            }
         } else {
            BlockFace direction = FaceUtil.combine(faces[0], faces[1]);
            direction = FaceUtil.rotate(direction, 2);
            int diff_a = FaceUtil.getFaceYawDifference(direction, orientation);
            int diff_b = FaceUtil.getFaceYawDifference(direction.getOppositeFace(), orientation);
            if (diff_a < diff_b) {
               orientation = direction;
            } else {
               orientation = direction.getOppositeFace();
            }
         }
      }

      Location at = this.findMinecartPos(railsBlock).getLocation();
      at.setDirection(FaceUtil.faceToVector(orientation));
      at.setYaw(at.getYaw() - 90.0F);
      if (this.isUpsideDown(railsBlock)) {
         at.add(0.5D, 0.9375D, 0.5D);
         at.setPitch(-180.0F);
      } else {
         at.add(0.5D, 0.0625D, 0.5D);
         at.setPitch(0.0F);
      }

      return at;
   }

   public boolean isHeadOnCollision(MinecartMember<?> member, Block railsBlock, Block hitBlock) {
      if (super.isHeadOnCollision(member, railsBlock, hitBlock)) {
         return true;
      } else {
         Block minecartPos = this.findMinecartPos(railsBlock);
         IntVector3 delta = (new IntVector3(hitBlock)).subtract(new IntVector3(minecartPos));
         BlockFace direction = FaceUtil.getDirection(((CommonMinecart)member.getEntity()).getVelocity(), false);
         if (delta.x == direction.getModX() && delta.z == direction.getModZ()) {
            return true;
         } else if (member.isOnSlope() && delta.x == 0 && delta.z == 0 && delta.y == 1 && direction == this.getDirection(railsBlock)) {
            return true;
         } else {
            return delta.x == 0 && delta.z == 0 && delta.y == 0 && this.isUpsideDown(railsBlock);
         }
      }
   }

   public BlockFace getSignColumnDirection(Block railsBlock) {
      return this.isUpsideDown(railsBlock) ? BlockFace.UP : BlockFace.DOWN;
   }

   public BlockFace[] getSignTriggerDirections(Block railBlock, Block signBlock, BlockFace signFacing) {
      signFacing = Util.snapFace(signFacing);
      RailPiece rail = RailPiece.create(this, railBlock);
      HashSet<BlockFace> watchedFaces = new HashSet(4);
      if (FaceUtil.isSubCardinal(signFacing)) {
         BlockFace[] faces = FaceUtil.getFaces(signFacing);
         BlockFace[] var7 = faces;
         int var8 = faces.length;

         int var9;
         BlockFace face;
         for(var9 = 0; var9 < var8; ++var9) {
            face = var7[var9];
            if (Util.isConnectedRailsFrom(rail, face)) {
               watchedFaces.add(face.getOppositeFace());
            }
         }

         if (watchedFaces.isEmpty()) {
            var7 = faces;
            var8 = faces.length;

            for(var9 = 0; var9 < var8; ++var9) {
               face = var7[var9];
               if (Util.isConnectedRailsFrom(rail, face.getOppositeFace())) {
                  watchedFaces.add(face);
               }
            }
         }
      } else {
         Rails rails = Util.getRailsRO(railBlock);
         if (rails != null && rails.isOnSlope()) {
            watchedFaces.add(BlockFace.UP);
            watchedFaces.add(BlockFace.DOWN);
         }

         if (Util.isConnectedRailsFrom(rail, signFacing)) {
            watchedFaces.add(signFacing.getOppositeFace());
         } else if (Util.isConnectedRailsFrom(rail, signFacing.getOppositeFace())) {
            watchedFaces.add(signFacing);
         } else {
            watchedFaces.add(FaceUtil.rotate(signFacing, -2));
            watchedFaces.add(FaceUtil.rotate(signFacing, 2));
         }
      }

      return (BlockFace[])watchedFaces.toArray(new BlockFace[watchedFaces.size()]);
   }

   public Block findRail(Block pos) {
      if (this.isRail(pos)) {
         return pos;
      } else {
         Block tmp = pos.getRelative(0, -1, 0);
         if (this.isRail(tmp) && !this.isUpsideDown(tmp)) {
            return tmp;
         } else {
            tmp = pos.getRelative(0, 1, 0);
            if (this.isRail(tmp) && this.isUpsideDown(tmp)) {
               return tmp;
            } else {
               tmp = pos.getRelative(0, 2, 0);
               return this.isRail(tmp) && this.isUpsideDown(tmp) ? tmp : null;
            }
         }
      }
   }
}
