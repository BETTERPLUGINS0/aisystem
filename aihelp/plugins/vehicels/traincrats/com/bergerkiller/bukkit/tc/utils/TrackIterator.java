package com.bergerkiller.bukkit.tc.utils;

import com.bergerkiller.bukkit.common.bases.IntVector3;
import com.bergerkiller.bukkit.common.utils.BlockUtil;
import com.bergerkiller.bukkit.common.utils.FaceUtil;
import com.bergerkiller.bukkit.tc.Util;
import com.bergerkiller.bukkit.tc.controller.components.RailPiece;
import com.bergerkiller.bukkit.tc.rails.type.RailType;
import java.util.HashSet;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Set;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.material.Rails;
import org.bukkit.util.Vector;

/** @deprecated */
@Deprecated
public class TrackIterator implements Iterator<Block> {
   private final int maxdistance;
   private final boolean onlyInLoadedChunks;
   private TrackMovingPoint movingPoint;
   private int distance;
   private double cartDistance;
   private Set<IntVector3> coordinates;

   public TrackIterator(Block startblock, BlockFace direction) {
      this(startblock, direction, false);
   }

   public TrackIterator(Block startblock, BlockFace direction, boolean onlyInLoadedChunks) {
      this(startblock, direction, 16000, onlyInLoadedChunks);
   }

   public TrackIterator(Block startblock, BlockFace direction, int maxdistance, boolean onlyInLoadedChunks) {
      this.coordinates = new HashSet();
      this.maxdistance = maxdistance;
      this.onlyInLoadedChunks = onlyInLoadedChunks;
      this.reset(startblock, direction);
   }

   public static TrackIterator createFinder(Block startBlock, BlockFace direction, Block destination) {
      int maxDistance = BlockUtil.getManhattanDistance(startBlock, destination, true) + 2;
      return new TrackIterator(startBlock, direction, maxDistance, false);
   }

   public static boolean canReach(Block rail, BlockFace direction, Block destination) {
      return createFinder(rail, direction, destination).tryFind(destination);
   }

   public static boolean isConnected(Block rail1, Block rail2, boolean bothways) {
      if (rail1 != null && rail2 != null) {
         if (BlockUtil.equals(rail1, rail2)) {
            return true;
         } else {
            RailType rail1type = RailType.getType(rail1);
            RailType rail2type = RailType.getType(rail2);
            if (rail1type != RailType.NONE && rail2type != RailType.NONE) {
               BlockFace[] rail1dirs = rail1type.getPossibleDirections(rail1);
               BlockFace[] rail2dirs = rail2type.getPossibleDirections(rail2);
               if (rail1dirs.length != 0 && rail2dirs.length != 0) {
                  Block pos1 = rail1type.findMinecartPos(rail1);
                  Block pos2 = rail1type.findMinecartPos(rail2);
                  BlockFace dir1 = getPreferredDirection(rail1dirs, pos1, pos2);
                  BlockFace dir2 = getPreferredDirection(rail2dirs, pos2, pos1);
                  int maxDistance = BlockUtil.getManhattanDistance(pos1, pos2, true) + 2;
                  TrackIterator iter = new TrackIterator((Block)null, (BlockFace)null, maxDistance, false);
                  if (bothways) {
                     return iter.canReach(rail1, rail2, rail1dirs, dir1) && iter.canReach(rail2, rail1, rail2dirs, dir2);
                  } else {
                     return iter.canReach(rail1, rail2, rail1dirs, dir1) || iter.canReach(rail2, rail1, rail2dirs, dir2);
                  }
               } else {
                  return false;
               }
            } else {
               return false;
            }
         }
      } else {
         return false;
      }
   }

   private static BlockFace getPreferredDirection(BlockFace[] directions, Block from, Block to) {
      BlockFace[] var4 = directions;
      int var5 = directions.length;

      for(int var6 = 0; var6 < var5; ++var6) {
         BlockFace dir = var4[var6];
         boolean preferred;
         if (FaceUtil.isVertical(dir)) {
            preferred = dir == Util.getVerticalFace(to.getY() > from.getY());
         } else {
            preferred = dir == FaceUtil.getDirection(from, to, false);
         }

         if (preferred) {
            return dir;
         }
      }

      return directions[0];
   }

   public TrackIterator reset(Block startBlock, BlockFace startDirection) {
      this.coordinates.clear();
      this.distance = 0;
      this.cartDistance = 0.0D;
      this.movingPoint = new TrackMovingPoint(startBlock, startDirection);
      return this;
   }

   public int getDistance() {
      return this.distance;
   }

   public double getCartDistance() {
      return this.cartDistance;
   }

   public boolean hasNext() {
      return this.movingPoint.hasNext() && this.distance <= this.maxdistance;
   }

   private void genNextBlock() {
      if (this.onlyInLoadedChunks) {
         int x = (int)((double)this.movingPoint.current.getX() + this.movingPoint.currentDirection.getX());
         int z = (int)((double)this.movingPoint.current.getZ() + this.movingPoint.currentDirection.getZ());
         if (!this.movingPoint.current.getWorld().isChunkLoaded(x >> 4, z >> 4)) {
            this.movingPoint.next(false);
            return;
         }
      }

      this.movingPoint.next();
      if (this.movingPoint.hasNext() && !this.coordinates.add(new IntVector3(this.movingPoint.next))) {
         this.movingPoint.clearNext();
      }

   }

   public void stop() {
      this.movingPoint.clearNext();
   }

   public Vector currentDirection() {
      return this.movingPoint.currentDirection;
   }

   public Location currentLocation() {
      return this.movingPoint.currentLocation;
   }

   public Block current() {
      return this.movingPoint.current;
   }

   public RailPiece currentRailPiece() {
      return this.movingPoint.currentRailPiece;
   }

   public RailType currentRailType() {
      return this.movingPoint.currentRail;
   }

   public Rails currentRails() {
      return BlockUtil.getRails(this.current());
   }

   public Vector peekNextDirection() {
      return this.movingPoint.nextDirection;
   }

   public Block peekNext() {
      return this.movingPoint.next;
   }

   public Block next() {
      if (!this.hasNext()) {
         throw new NoSuchElementException("No next track is available");
      } else {
         Vector oldDirection = this.currentDirection();
         this.genNextBlock();
         Vector newDirection = this.currentDirection();
         ++this.distance;
         if (Math.abs(oldDirection.dot(newDirection)) >= 0.999999D) {
            ++this.cartDistance;
         } else {
            this.cartDistance += 0.707106781D;
         }

         return this.current();
      }
   }

   public void remove() {
      throw new UnsupportedOperationException("TrackIterator.remove is not supported");
   }

   public boolean tryFind(Block railsBlock) {
      while(true) {
         if (this.hasNext()) {
            if (!BlockUtil.equals(this.next(), railsBlock)) {
               continue;
            }

            return true;
         }

         return false;
      }
   }

   private boolean canReach(Block rail, Block destination, BlockFace[] faces, BlockFace preferredFace) {
      BlockFace[] var5 = faces;
      int var6 = faces.length;

      int var7;
      BlockFace face;
      for(var7 = 0; var7 < var6; ++var7) {
         face = var5[var7];
         if (face == preferredFace) {
            if (this.reset(rail, face).tryFind(destination)) {
               return true;
            }
            break;
         }
      }

      var5 = faces;
      var6 = faces.length;

      for(var7 = 0; var7 < var6; ++var7) {
         face = var5[var7];
         if (face != preferredFace && this.reset(rail, face).tryFind(destination)) {
            return true;
         }
      }

      return false;
   }
}
