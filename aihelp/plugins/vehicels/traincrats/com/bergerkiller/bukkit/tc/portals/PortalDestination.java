package com.bergerkiller.bukkit.tc.portals;

import com.bergerkiller.bukkit.common.bases.IntVector3;
import com.bergerkiller.bukkit.common.utils.FaceUtil;
import com.bergerkiller.bukkit.common.utils.MaterialUtil;
import com.bergerkiller.bukkit.common.utils.WorldUtil;
import com.bergerkiller.bukkit.tc.Direction;
import com.bergerkiller.bukkit.tc.rails.type.RailType;
import java.util.HashSet;
import java.util.Iterator;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;

public class PortalDestination {
   private static final Material PORTAL_TYPE = MaterialUtil.getFirst(new String[]{"NETHER_PORTAL", "LEGACY_PORTAL"});
   private final Block railsBlock;
   private final BlockFace[] directions;

   public PortalDestination(Block railsBlock, BlockFace[] directions) {
      this.railsBlock = railsBlock;
      this.directions = directions;
   }

   public Block getRailsBlock() {
      return this.railsBlock;
   }

   public BlockFace[] getDirections() {
      return this.directions;
   }

   public boolean hasDirections() {
      return this.directions != null && this.directions.length > 0;
   }

   public String toString() {
      String s = "{";
      s = s + "world=" + this.railsBlock.getWorld().getName();
      s = s + ", x=" + this.railsBlock.getX();
      s = s + ", y=" + this.railsBlock.getY();
      s = s + ", z=" + this.railsBlock.getZ();
      s = s + ", dirs=[";
      boolean f = true;
      BlockFace[] var3 = this.directions;
      int var4 = var3.length;

      for(int var5 = 0; var5 < var4; ++var5) {
         BlockFace dir = var3[var5];
         if (f) {
            f = false;
         } else {
            s = s + ", ";
         }

         s = s + dir.name();
      }

      s = s + "]}";
      return s;
   }

   public static PortalDestination findDestinationAtNetherPortal(Block portalBlock, Direction direction) {
      if (portalBlock == null) {
         return null;
      } else {
         HashSet<IntVector3> blocks = new HashSet();
         IntVector3 pos = new IntVector3(portalBlock.getX(), portalBlock.getY(), portalBlock.getZ());
         World world = portalBlock.getWorld();
         discoverPortals(blocks, world, pos);
         if (blocks.isEmpty()) {
            return findDestination(portalBlock, portalBlock, direction);
         } else {
            int minX = pos.x;
            int minY = pos.y;
            int minZ = pos.z;
            int maxX = pos.x;
            int maxY = pos.y;
            int maxZ = pos.z;
            Iterator var11 = blocks.iterator();

            while(var11.hasNext()) {
               IntVector3 block = (IntVector3)var11.next();
               if (block.x < minX) {
                  minX = block.x;
               }

               if (block.x > maxX) {
                  maxX = block.x;
               }

               if (block.y < minY) {
                  minY = block.y;
               }

               if (block.y > maxY) {
                  maxY = block.y;
               }

               if (block.z < minZ) {
                  minZ = block.z;
               }

               if (block.z > maxZ) {
                  maxZ = block.z;
               }
            }

            return findDestination(world.getBlockAt(minX, minY, minZ), world.getBlockAt(maxX, maxY, maxZ), direction);
         }
      }
   }

   private static void discoverPortals(HashSet<IntVector3> blocks, World world, IntVector3 pos) {
      if (WorldUtil.getBlockData(world, pos).isType(PORTAL_TYPE) && blocks.add(pos)) {
         BlockFace[] var3 = FaceUtil.BLOCK_SIDES;
         int var4 = var3.length;

         for(int var5 = 0; var5 < var4; ++var5) {
            BlockFace face = var3[var5];
            discoverPortals(blocks, world, pos.add(face));
         }
      }

   }

   public static PortalDestination findDestination(Block regionMin, Block regionMax, Direction direction) {
      int dx = regionMax.getX() - regionMin.getX();
      int dy = regionMax.getY() - regionMin.getY();
      int dz = regionMax.getZ() - regionMin.getZ();
      BlockFace portalFacing;
      if (dx > dy && dz > dy) {
         portalFacing = BlockFace.UP;
      } else if (dx > dz) {
         portalFacing = BlockFace.SOUTH;
      } else {
         portalFacing = BlockFace.EAST;
      }

      BlockFace spawnDirection = direction.getDirection(portalFacing);
      PortalDestination dest = null;

      for(int y = regionMin.getY(); y <= regionMax.getY(); ++y) {
         for(int x = regionMin.getX(); x <= regionMax.getX(); ++x) {
            for(int z = regionMin.getZ(); z <= regionMax.getZ(); ++z) {
               Block block = regionMin.getWorld().getBlockAt(x, y, z);
               dest = findRailDestination(block, spawnDirection);
               if (dest != null) {
                  return dest;
               }
            }
         }
      }

      dest = findAgainst(regionMin, regionMax, spawnDirection);
      if (dest != null) {
         return dest;
      } else {
         dest = findAgainst(regionMin, regionMax, portalFacing);
         if (dest != null) {
            return dest;
         } else {
            dest = findAgainst(regionMin, regionMax, portalFacing.getOppositeFace());
            if (dest != null) {
               return dest;
            } else {
               return null;
            }
         }
      }
   }

   public static PortalDestination findAgainst(Block regionMin, Block regionMax, BlockFace spawnDirection) {
      int x1 = regionMin.getX();
      int y1 = regionMin.getY();
      int z1 = regionMin.getZ();
      int x2 = regionMax.getX();
      int y2 = regionMax.getY();
      int z2 = regionMax.getZ();
      if (spawnDirection.getModY() != 0) {
         y1 = y2 = (spawnDirection.getModY() > 0 ? regionMax.getY() : regionMin.getY()) + spawnDirection.getModY();
      } else if (spawnDirection.getModX() != 0) {
         x1 = x2 = (spawnDirection.getModX() > 0 ? regionMax.getX() : regionMin.getX()) + spawnDirection.getModX();
      } else {
         z1 = z2 = (spawnDirection.getModZ() > 0 ? regionMax.getZ() : regionMin.getZ()) + spawnDirection.getModZ();
      }

      for(int y = y1; y <= y2; ++y) {
         for(int x = x1; x <= x2; ++x) {
            for(int z = z1; z <= z2; ++z) {
               Block block = regionMin.getWorld().getBlockAt(x, y, z);
               PortalDestination dest = findRailDestination(block, spawnDirection);
               if (dest != null) {
                  return dest;
               }
            }
         }
      }

      return null;
   }

   public static PortalDestination findRailDestination(Block rails, BlockFace direction) {
      RailType railType = RailType.getType(rails);
      return railType != RailType.NONE ? new PortalDestination(rails, new BlockFace[]{direction}) : null;
   }
}
