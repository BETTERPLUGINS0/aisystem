package me.SuperRonanCraft.BetterRTP.references.rtpinfo;

import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.concurrent.CompletableFuture;
import me.SuperRonanCraft.BetterRTP.BetterRTP;
import me.SuperRonanCraft.BetterRTP.lib.paperlib.PaperLib;
import me.SuperRonanCraft.BetterRTP.references.rtpinfo.worlds.RTPWorld;
import me.SuperRonanCraft.BetterRTP.references.rtpinfo.worlds.WORLD_TYPE;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.ChunkSnapshot;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Biome;
import org.bukkit.block.Block;

public class RandomLocation {
   public static Location generateLocation(RTPWorld rtpWorld) {
      Location loc;
      switch(rtpWorld.getShape()) {
      case CIRCLE:
         loc = generateRound(rtpWorld);
         break;
      case SQUARE:
      default:
         loc = generateSquare(rtpWorld);
      }

      return loc;
   }

   private static Location generateSquare(RTPWorld rtpWorld) {
      int min = rtpWorld.getMinRadius();
      int max = rtpWorld.getMaxRadius() - min;
      int quadrant = (new Random()).nextInt(4);

      int x;
      int z;
      try {
         switch(quadrant) {
         case 0:
            x = (new Random()).nextInt(max) + min;
            z = (new Random()).nextInt(max) + min;
            break;
         case 1:
            x = -(new Random()).nextInt(max) - min;
            z = -((new Random()).nextInt(max) + min);
            break;
         case 2:
            x = -(new Random()).nextInt(max) - min;
            z = (new Random()).nextInt(max) + min;
            break;
         default:
            x = (new Random()).nextInt(max) + min;
            z = -((new Random()).nextInt(max) + min);
         }
      } catch (IllegalArgumentException var7) {
         var7.printStackTrace();
         BetterRTP.getInstance().getLogger().warning("A bounding location was negative! Please check your config only has positive x/z for max/min radius!");
         BetterRTP.getInstance().getLogger().warning("Max: " + rtpWorld.getMaxRadius() + " Min: " + rtpWorld.getMinRadius());
         return null;
      }

      x += rtpWorld.getCenterX();
      z += rtpWorld.getCenterZ();
      return new Location(rtpWorld.getWorld(), (double)x, 69.0D, (double)z);
   }

   private static Location generateRound(RTPWorld rtpWorld) {
      int min = rtpWorld.getMinRadius();
      int max = rtpWorld.getMaxRadius() - min;
      double area = 3.141592653589793D * (double)(max - min) * (double)(max + min);
      double subArea = area * (new Random()).nextDouble();
      double r = Math.sqrt(subArea / 3.141592653589793D + (double)(min * min));
      double theta = (r - (double)((int)r)) * 2.0D * 3.141592653589793D;
      int x = (int)(r * Math.cos(theta));
      int z = (int)(r * Math.sin(theta));
      x += rtpWorld.getCenterX();
      z += rtpWorld.getCenterZ();
      return new Location(rtpWorld.getWorld(), (double)x, 69.0D, (double)z);
   }

   public static Location getSafeLocation(WORLD_TYPE type, World world, Location loc, int minY, int maxY, List<String> biomes) {
      switch(type) {
      case NETHER:
         return getLocAtNether(loc.getBlockX(), loc.getBlockZ(), minY, maxY, world, biomes);
      case NORMAL:
      default:
         return getLocAtNormal(loc.getBlockX(), loc.getBlockZ(), minY, maxY, world, biomes);
      }
   }

   private static Location getLocAtNormal(int x, int z, int minY, int maxY, World world, List<String> biomes) {
      Block b = getHighestBlock(x, z, world);
      if (!b.getType().isSolid() && !badBlock(b.getType().name(), x, z, world, (List)null)) {
         b = world.getBlockAt(x, b.getY() - 1, z);
      }

      return b.getY() >= minY && b.getY() <= maxY && !badBlock(b.getType().name(), x, z, world, biomes) ? new Location(world, (double)x, (double)(b.getY() + 1), (double)z) : null;
   }

   public static Block getHighestBlock(int x, int z, World world) {
      Block b = world.getHighestBlockAt(x, z);
      if (b.getType().toString().endsWith("AIR")) {
         b = world.getBlockAt(x, b.getY() - 1, z);
      }

      return b;
   }

   private static Location getLocAtNether(int x, int z, int minY, int maxY, World world, List<String> biomes) {
      for(int y = minY + 1; y < maxY; ++y) {
         Block block_current = world.getBlockAt(x, y, z);
         if (block_current.getType().name().endsWith("AIR") || !block_current.getType().isSolid()) {
            String block;
            if (!block_current.getType().name().endsWith("AIR") && !block_current.getType().isSolid()) {
               block = block_current.getType().name();
               if (badBlock(block, x, z, world, (List)null)) {
                  continue;
               }
            }

            block = world.getBlockAt(x, y - 1, z).getType().name();
            if (!block.endsWith("AIR") && world.getBlockAt(x, y + 1, z).getType().name().endsWith("AIR") && !badBlock(block, x, z, world, biomes)) {
               return new Location(world, (double)x, (double)y, (double)z);
            }
         }
      }

      return null;
   }

   public static boolean badBlock(String block, int x, int z, World world, List<String> biomes) {
      Iterator var5 = BetterRTP.getInstance().getRTP().getBlockList().iterator();

      String currentBlock;
      do {
         if (!var5.hasNext()) {
            if (biomes != null && !biomes.isEmpty()) {
               String biomeCurrent = world.getBiome(x, z).name();
               Iterator var9 = biomes.iterator();

               String biome;
               do {
                  if (!var9.hasNext()) {
                     return true;
                  }

                  biome = (String)var9.next();
               } while(!biomeCurrent.toUpperCase().contains(biome.toUpperCase()));

               return false;
            }

            return false;
         }

         currentBlock = (String)var5.next();
      } while(!currentBlock.equalsIgnoreCase(block));

      return true;
   }

   public static void runChunkTest() {
      BetterRTP.getInstance().getLogger().info("---------------- Starting chunk test!");
      World world = Bukkit.getWorld("world");
      cacheChunkAt(world, 32, -32, -32, -32);
   }

   private static void cacheTask(World world, int goal, int start, int xat, int zat) {
      ++zat;
      if (zat > goal) {
         zat = start;
         ++xat;
      }

      if (xat <= goal) {
         cacheChunkAt(world, goal, start, xat, zat);
      }

   }

   private static void cacheChunkAt(World world, int goal, int start, int xat, int zat) {
      CompletableFuture<Chunk> task = PaperLib.getChunkAtAsync(new Location(world, (double)(xat * 16), 0.0D, (double)(zat * 16)));
      task.thenAccept((chunk) -> {
         try {
            ChunkSnapshot snapshot = chunk.getChunkSnapshot(true, true, false);
            int maxy = snapshot.getHighestBlockYAt(8, 8);
            Biome biome = snapshot.getBiome(8, 8);
            BetterRTP.getInstance().getDatabaseHandler().getDatabaseChunks().addChunk(chunk, maxy, biome);
         } catch (Throwable var4) {
            var4.printStackTrace();
            throw new RuntimeException();
         }
      }).thenRun(() -> {
         cacheTask(world, goal, start, xat, zat);
      });
   }
}
