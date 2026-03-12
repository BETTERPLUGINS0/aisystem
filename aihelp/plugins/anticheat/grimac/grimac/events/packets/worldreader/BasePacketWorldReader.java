package ac.grim.grimac.events.packets.worldreader;

import ac.grim.grimac.GrimAPI;
import ac.grim.grimac.player.GrimPlayer;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.event.PacketListenerAbstract;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.event.PacketListenerPriority;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.event.PacketSendEvent;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.packettype.PacketType;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.world.chunk.BaseChunk;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.util.Vector3i;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.PacketWrapper;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerAcknowledgeBlockChanges;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerAcknowledgePlayerDigging;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerBlockChange;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerChangeGameState;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerChunkData;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerChunkDataBulk;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerMultiBlockChange;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerUnloadChunk;
import ac.grim.grimac.utils.chunks.Column;
import ac.grim.grimac.utils.data.TeleportData;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;

public class BasePacketWorldReader extends PacketListenerAbstract {
   public BasePacketWorldReader() {
      super(PacketListenerPriority.HIGH);
   }

   public void onPacketSend(PacketSendEvent event) {
      if (event.getPacketType() == PacketType.Play.Server.UNLOAD_CHUNK) {
         WrapperPlayServerUnloadChunk unloadChunk = new WrapperPlayServerUnloadChunk(event);
         GrimPlayer player = GrimAPI.INSTANCE.getPlayerDataManager().getPlayer(event.getUser());
         if (player == null) {
            return;
         }

         this.unloadChunk(player, unloadChunk.getChunkX(), unloadChunk.getChunkZ());
      }

      GrimPlayer player;
      if (event.getPacketType() == PacketType.Play.Server.MAP_CHUNK_BULK) {
         player = GrimAPI.INSTANCE.getPlayerDataManager().getPlayer(event.getUser());
         if (player == null) {
            return;
         }

         this.handleMapChunkBulk(player, event);
      }

      if (event.getPacketType() == PacketType.Play.Server.CHUNK_DATA) {
         player = GrimAPI.INSTANCE.getPlayerDataManager().getPlayer(event.getUser());
         if (player == null) {
            return;
         }

         this.handleMapChunk(player, event);
      }

      if (event.getPacketType() == PacketType.Play.Server.BLOCK_CHANGE) {
         player = GrimAPI.INSTANCE.getPlayerDataManager().getPlayer(event.getUser());
         if (player == null) {
            return;
         }

         this.handleBlockChange(player, event);
      }

      if (event.getPacketType() == PacketType.Play.Server.MULTI_BLOCK_CHANGE) {
         player = GrimAPI.INSTANCE.getPlayerDataManager().getPlayer(event.getUser());
         if (player == null) {
            return;
         }

         this.handleMultiBlockChange(player, event);
      }

      if (event.getPacketType() == PacketType.Play.Server.ACKNOWLEDGE_BLOCK_CHANGES) {
         player = GrimAPI.INSTANCE.getPlayerDataManager().getPlayer(event.getUser());
         if (player == null) {
            return;
         }

         WrapperPlayServerAcknowledgeBlockChanges changes = new WrapperPlayServerAcknowledgeBlockChanges(event);
         player.compensatedWorld.handlePredictionConfirmation(changes.getSequence());
      }

      if (event.getPacketType() == PacketType.Play.Server.ACKNOWLEDGE_PLAYER_DIGGING) {
         player = GrimAPI.INSTANCE.getPlayerDataManager().getPlayer(event.getUser());
         if (player == null) {
            return;
         }

         WrapperPlayServerAcknowledgePlayerDigging ack = new WrapperPlayServerAcknowledgePlayerDigging(event);
         player.compensatedWorld.handleBlockBreakAck(ack.getBlockPosition(), ack.getBlockId(), ack.getAction(), ack.isSuccessful());
      }

      if (event.getPacketType() == PacketType.Play.Server.CHANGE_GAME_STATE) {
         player = GrimAPI.INSTANCE.getPlayerDataManager().getPlayer(event.getUser());
         if (player == null) {
            return;
         }

         WrapperPlayServerChangeGameState newState = new WrapperPlayServerChangeGameState(event);
         player.latencyUtils.addRealTimeTask(player.lastTransactionSent.get(), () -> {
            if (newState.getReason() == WrapperPlayServerChangeGameState.Reason.BEGIN_RAINING) {
               player.compensatedWorld.isRaining = true;
            } else if (newState.getReason() == WrapperPlayServerChangeGameState.Reason.END_RAINING) {
               player.compensatedWorld.isRaining = false;
            } else if (newState.getReason() == WrapperPlayServerChangeGameState.Reason.RAIN_LEVEL_CHANGE) {
               player.compensatedWorld.isRaining = newState.getValue() > 0.2F;
            }

         });
      }

   }

   public void handleMapChunkBulk(GrimPlayer player, PacketSendEvent event) {
      WrapperPlayServerChunkDataBulk chunkData = new WrapperPlayServerChunkDataBulk(event);

      for(int i = 0; i < chunkData.getChunks().length; ++i) {
         this.addChunkToCache(event, player, chunkData.getChunks()[i], true, chunkData.getX()[i], chunkData.getZ()[i]);
      }

   }

   public void handleMapChunk(GrimPlayer player, PacketSendEvent event) {
      WrapperPlayServerChunkData chunkData = new WrapperPlayServerChunkData(event);
      this.addChunkToCache(event, player, chunkData.getColumn().getChunks(), chunkData.getColumn().isFullChunk(), chunkData.getColumn().getX(), chunkData.getColumn().getZ());
      event.setLastUsedWrapper((PacketWrapper)null);
   }

   public void addChunkToCache(PacketSendEvent event, GrimPlayer player, BaseChunk[] chunks, boolean isGroundUp, int chunkX, int chunkZ) {
      double chunkCenterX = (double)((chunkX << 4) + 8);
      double chunkCenterZ = (double)((chunkZ << 4) + 8);
      boolean shouldPostTrans = Math.abs(player.x - chunkCenterX) < 16.0D && Math.abs(player.z - chunkCenterZ) < 16.0D;
      Iterator var12 = player.getSetbackTeleportUtil().pendingTeleports.iterator();

      while(true) {
         TeleportData teleports;
         do {
            if (!var12.hasNext()) {
               if (shouldPostTrans) {
                  List var10000 = event.getTasksAfterSend();
                  Objects.requireNonNull(player);
                  var10000.add(player::sendTransaction);
               }

               if (isGroundUp) {
                  Column column = new Column(chunkX, chunkZ, chunks, player.lastTransactionSent.get());
                  player.compensatedWorld.addToCache(column, chunkX, chunkZ);
               } else {
                  player.latencyUtils.addRealTimeTask(player.lastTransactionSent.get(), () -> {
                     Column existingColumn = player.compensatedWorld.getChunk(chunkX, chunkZ);
                     if (existingColumn != null) {
                        existingColumn.mergeChunks(chunks);
                     }
                  });
               }

               return;
            }

            teleports = (TeleportData)var12.next();
         } while(teleports.getFlags().getMask() != 0);

         shouldPostTrans = shouldPostTrans || Math.abs(teleports.getLocation().getX() - chunkCenterX) < 16.0D && Math.abs(teleports.getLocation().getZ() - chunkCenterZ) < 16.0D;
      }
   }

   public void unloadChunk(GrimPlayer player, int x, int z) {
      if (player != null) {
         player.compensatedWorld.removeChunkLater(x, z);
      }
   }

   public void handleBlockChange(GrimPlayer player, PacketSendEvent event) {
      WrapperPlayServerBlockChange blockChange = new WrapperPlayServerBlockChange(event);
      int range = 16;
      Vector3i blockPosition = blockChange.getBlockPosition();
      if (Math.abs((double)blockPosition.getX() - player.x) < (double)range && Math.abs((double)blockPosition.getY() - player.y) < (double)range && Math.abs((double)blockPosition.getZ() - player.z) < (double)range && player.lastTransSent + 2L < System.currentTimeMillis()) {
         player.sendTransaction();
      }

      player.latencyUtils.addRealTimeTask(player.lastTransactionSent.get(), () -> {
         player.compensatedWorld.updateBlock(blockPosition.getX(), blockPosition.getY(), blockPosition.getZ(), blockChange.getBlockId());
      });
   }

   public void handleMultiBlockChange(GrimPlayer player, PacketSendEvent event) {
      WrapperPlayServerMultiBlockChange multiBlockChange = new WrapperPlayServerMultiBlockChange(event);
      int range = 16;
      WrapperPlayServerMultiBlockChange.EncodedBlock[] blocks = multiBlockChange.getBlocks();
      WrapperPlayServerMultiBlockChange.EncodedBlock[] var6 = blocks;
      int var7 = blocks.length;

      for(int var8 = 0; var8 < var7; ++var8) {
         WrapperPlayServerMultiBlockChange.EncodedBlock blockChange = var6[var8];
         if (Math.abs((double)blockChange.getX() - player.x) < (double)range && Math.abs((double)blockChange.getY() - player.y) < (double)range && Math.abs((double)blockChange.getZ() - player.z) < (double)range && player.lastTransSent + 2L < System.currentTimeMillis()) {
            player.sendTransaction();
            break;
         }
      }

      player.latencyUtils.addRealTimeTask(player.lastTransactionSent.get(), () -> {
         WrapperPlayServerMultiBlockChange.EncodedBlock[] var2 = blocks;
         int var3 = blocks.length;

         for(int var4 = 0; var4 < var3; ++var4) {
            WrapperPlayServerMultiBlockChange.EncodedBlock blockChange = var2[var4];
            player.compensatedWorld.updateBlock(blockChange.getX(), blockChange.getY(), blockChange.getZ(), blockChange.getBlockId());
         }

      });
   }
}
