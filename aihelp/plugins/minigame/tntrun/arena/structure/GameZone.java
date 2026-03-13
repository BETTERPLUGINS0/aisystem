package tntrun.arena.structure;

import java.util.ConcurrentModificationException;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.BlockState;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.NumberConversions;
import tntrun.TNTRun;
import tntrun.arena.Arena;

public class GameZone {
   private HashSet<Block> blockstodestroy = new HashSet();
   private LinkedList<BlockState> blocks = new LinkedList();
   public Arena arena;
   private final int SCAN_DEPTH = 1;
   private static double PLAYER_BOUNDINGBOX_ADD = 0.3D;
   private final int MAX_BLOCKS_PER_TICK = 10;

   public GameZone(Arena arena) {
      this.arena = arena;
   }

   public void destroyBlock(Location loc) {
      int y = loc.getBlockY() + 1;
      final Block block = null;

      for(int i = 0; i <= 1; ++i) {
         block = this.getBlockUnderPlayer(y, loc);
         --y;
         if (block != null) {
            break;
         }
      }

      if (block != null && !this.blockstodestroy.contains(block)) {
         this.blockstodestroy.add(block);
         (new BukkitRunnable() {
            public void run() {
               if (GameZone.this.arena.getStatusManager().isArenaRunning()) {
                  GameZone.this.blockstodestroy.remove(block);
                  TNTRun.getInstance().getSound().BLOCK_BREAK(block);
                  GameZone.this.removeGLBlocks(block);
               }

            }
         }).runTaskLater(TNTRun.getInstance(), (long)this.arena.getStructureManager().getGameLevelDestroyDelay());
      }

   }

   public void regenNow() {
      Iterator bsi = this.blocks.iterator();

      while(bsi.hasNext()) {
         BlockState bs = (BlockState)bsi.next();
         bs.update(true);
         bsi.remove();
      }

   }

   private void removeGLBlocks(Block block) {
      this.blocks.add(block.getState());
      this.saveBlock(block);
      block = block.getRelative(BlockFace.DOWN);
      this.blocks.add(block.getState());
      this.saveBlock(block);
   }

   private Block getBlockUnderPlayer(int y, Location location) {
      GameZone.PlayerPosition loc = new GameZone.PlayerPosition(location.getX(), y, location.getZ());
      Block b11 = loc.getBlock(location.getWorld(), PLAYER_BOUNDINGBOX_ADD, -PLAYER_BOUNDINGBOX_ADD);
      if (b11.getType() != Material.AIR && b11.getType() != Material.LIGHT) {
         return b11;
      } else {
         Block b12 = loc.getBlock(location.getWorld(), -PLAYER_BOUNDINGBOX_ADD, PLAYER_BOUNDINGBOX_ADD);
         if (b12.getType() != Material.AIR && b12.getType() != Material.LIGHT) {
            return b12;
         } else {
            Block b21 = loc.getBlock(location.getWorld(), PLAYER_BOUNDINGBOX_ADD, PLAYER_BOUNDINGBOX_ADD);
            if (b21.getType() != Material.AIR && b21.getType() != Material.LIGHT) {
               return b21;
            } else {
               Block b22 = loc.getBlock(location.getWorld(), -PLAYER_BOUNDINGBOX_ADD, -PLAYER_BOUNDINGBOX_ADD);
               return b22.getType() != Material.AIR && b22.getType() != Material.LIGHT ? b22 : null;
            }
         }
      }
   }

   private void saveBlock(Block b) {
      b.setType(Material.AIR);
   }

   public int regen() {
      final Iterator<BlockState> bsit = this.blocks.iterator();
      (new BukkitRunnable() {
         public void run() {
            for(int i = 10; i >= 0; --i) {
               if (bsit.hasNext()) {
                  try {
                     BlockState bs = (BlockState)bsit.next();
                     bs.update(true);
                     bsit.remove();
                  } catch (ConcurrentModificationException var3) {
                  }
               } else {
                  this.cancel();
               }
            }

         }
      }).runTaskTimer(TNTRun.getInstance(), 0L, 1L);
      this.blockstodestroy.clear();
      return this.arena.getStructureManager().getRegenerationDelay();
   }

   private static class PlayerPosition {
      private double x;
      private int y;
      private double z;

      public PlayerPosition(double x, int y, double z) {
         this.x = x;
         this.y = y;
         this.z = z;
      }

      public Block getBlock(World world, double addx, double addz) {
         return world.getBlockAt(NumberConversions.floor(this.x + addx), this.y, NumberConversions.floor(this.z + addz));
      }
   }
}
