package ac.grim.grimac.manager.player.handlers;

import ac.grim.grimac.GrimAPI;
import ac.grim.grimac.api.handler.ResyncHandler;
import ac.grim.grimac.platform.api.world.PlatformChunk;
import ac.grim.grimac.platform.api.world.PlatformWorld;
import ac.grim.grimac.player.GrimPlayer;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.PacketEvents;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.manager.server.ServerVersion;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.util.Vector3i;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.PacketWrapper;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerAcknowledgeBlockChanges;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerBlockChange;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerMultiBlockChange;
import lombok.Generated;

public class DefaultResyncHandler implements ResyncHandler {
   private final GrimPlayer player;

   private static void resyncPositions(GrimPlayer player, int minBlockX, int mY, int minBlockZ, int maxBlockX, int mxY, int maxBlockZ) {
      if (player.compensatedWorld.isChunkLoaded(minBlockX >> 4, minBlockZ >> 4) && player.compensatedWorld.isChunkLoaded(minBlockX >> 4, maxBlockZ >> 4) && player.compensatedWorld.isChunkLoaded(maxBlockX >> 4, minBlockZ >> 4) && player.compensatedWorld.isChunkLoaded(maxBlockX >> 4, maxBlockZ >> 4)) {
         if (player.platformPlayer != null) {
            PlatformWorld world = player.platformPlayer.getWorld();
            GrimAPI.INSTANCE.getScheduler().getRegionScheduler().execute(GrimAPI.INSTANCE.getGrimPlugin(), world, minBlockX >> 4, minBlockZ >> 4, () -> {
               if (player.getSetbackTeleportUtil().hasAcceptedSpawnTeleport) {
                  if (world.isChunkLoaded(minBlockX >> 4, minBlockZ >> 4) && world.isChunkLoaded(minBlockX >> 4, maxBlockZ >> 4) && world.isChunkLoaded(maxBlockX >> 4, minBlockZ >> 4) && world.isChunkLoaded(maxBlockX >> 4, maxBlockZ >> 4)) {
                     int minSection = player.compensatedWorld.getMinHeight() >> 4;
                     int minBlock = minSection << 4;
                     int maxBlock = player.compensatedWorld.getMaxHeight() - 1;
                     int minBlockY = Math.max(minBlock, mY);
                     int maxBlockY = Math.min(maxBlock, mxY);
                     int minChunkX = minBlockX >> 4;
                     int maxChunkX = maxBlockX >> 4;
                     int minChunkY = minBlockY >> 4;
                     int maxChunkY = maxBlockY >> 4;
                     int minChunkZ = minBlockZ >> 4;
                     int maxChunkZ = maxBlockZ >> 4;

                     for(int currChunkZ = minChunkZ; currChunkZ <= maxChunkZ; ++currChunkZ) {
                        int minZ = currChunkZ == minChunkZ ? minBlockZ & 15 : 0;
                        int maxZ = currChunkZ == maxChunkZ ? maxBlockZ & 15 : 15;

                        for(int currChunkX = minChunkX; currChunkX <= maxChunkX; ++currChunkX) {
                           int minX = currChunkX == minChunkX ? minBlockX & 15 : 0;
                           int maxX = currChunkX == maxChunkX ? maxBlockX & 15 : 15;
                           PlatformChunk chunk = world.getChunkAt(currChunkX, currChunkZ);

                           for(int currChunkY = minChunkY; currChunkY <= maxChunkY; ++currChunkY) {
                              int minY = currChunkY == minChunkY ? minBlockY & 15 : 0;
                              int maxY = currChunkY == maxChunkY ? maxBlockY & 15 : 15;
                              int totalBlocks = (maxX - minX + 1) * (maxZ - minZ + 1) * (maxY - minY + 1);
                              WrapperPlayServerMultiBlockChange.EncodedBlock[] encodedBlocks = new WrapperPlayServerMultiBlockChange.EncodedBlock[totalBlocks];
                              int blockIndex = 0;

                              for(int currZ = minZ; currZ <= maxZ; ++currZ) {
                                 for(int currX = minX; currX <= maxX; ++currX) {
                                    for(int currY = minY; currY <= maxY; ++currY) {
                                       int blockId = chunk.getBlockID(currX, currY | currChunkY << 4, currZ);
                                       encodedBlocks[blockIndex++] = new WrapperPlayServerMultiBlockChange.EncodedBlock(blockId, currX, currY | currChunkY << 4, currZ);
                                    }
                                 }
                              }

                              WrapperPlayServerMultiBlockChange packet = new WrapperPlayServerMultiBlockChange(new Vector3i(currChunkX, currChunkY, currChunkZ), true, encodedBlocks);
                              player.runSafely(() -> {
                                 player.user.sendPacket((PacketWrapper)packet);
                              });
                           }
                        }
                     }

                  }
               }
            });
         }
      }
   }

   private static void resyncPosition(GrimPlayer player, int x, int y, int z, int sequence) {
      if (player.platformPlayer != null) {
         int chunkX = x >> 4;
         int chunkZ = z >> 4;
         if (player.compensatedWorld.isChunkLoaded(chunkX, chunkZ)) {
            PlatformWorld world = player.platformPlayer.getWorld();
            GrimAPI.INSTANCE.getScheduler().getRegionScheduler().execute(GrimAPI.INSTANCE.getGrimPlugin(), world, chunkX, chunkZ, () -> {
               if (player.platformPlayer.isOnline() && player.getSetbackTeleportUtil().hasAcceptedSpawnTeleport) {
                  if (!(player.platformPlayer.distanceSquared((double)x, (double)y, (double)z) >= 4096.0D)) {
                     if (world.isChunkLoaded(chunkX, chunkZ)) {
                        int blockId = world.getChunkAt(chunkX, chunkZ).getBlockID(x & 15, y, z & 15);
                        player.runSafely(() -> {
                           player.user.sendPacket((PacketWrapper)(new WrapperPlayServerBlockChange(new Vector3i(x, y, z), blockId)));
                           if (PacketEvents.getAPI().getServerManager().getVersion().isNewerThanOrEquals(ServerVersion.V_1_19)) {
                              player.user.sendPacket((PacketWrapper)(new WrapperPlayServerAcknowledgeBlockChanges(sequence)));
                           }

                        });
                     }
                  }
               }
            });
         }
      }
   }

   public void resyncPosition(int x, int y, int z, int sequence) {
      resyncPosition(this.player, x, y, z, sequence);
   }

   public void resync(int minBlockX, int minBlockY, int minBlockZ, int maxBlockX, int maxBlockY, int maxBlockZ) {
      resyncPositions(this.player, minBlockX, minBlockY, minBlockZ, maxBlockX, maxBlockY, maxBlockZ);
   }

   @Generated
   public DefaultResyncHandler(GrimPlayer player) {
      this.player = player;
   }
}
