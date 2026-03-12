package ac.grim.grimac.utils.latency;

import ac.grim.grimac.GrimAPI;
import ac.grim.grimac.api.PacketWorld;
import ac.grim.grimac.player.GrimPlayer;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.PacketEvents;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.manager.server.ServerVersion;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.entity.type.EntityTypes;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.player.ClientVersion;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.player.DiggingAction;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.player.User;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.world.BlockFace;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.world.chunk.BaseChunk;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.world.chunk.impl.v1_16.Chunk_v1_9;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.world.chunk.impl.v_1_18.Chunk_v1_18;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.world.chunk.palette.DataPalette;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.world.chunk.palette.ListPalette;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.world.chunk.palette.PaletteType;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.world.chunk.storage.LegacyFlexibleStorage;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.world.dimension.DimensionType;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.world.states.WrappedBlockState;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.world.states.defaulttags.BlockTags;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.world.states.enums.East;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.world.states.enums.Half;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.world.states.enums.North;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.world.states.enums.South;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.world.states.enums.West;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.world.states.type.StateType;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.world.states.type.StateTypes;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.world.states.type.StateValue;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.util.Vector3d;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.util.Vector3i;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.PacketWrapper;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientPlayerBlockPlacement;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientPlayerDigging;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientUseItem;
import ac.grim.grimac.shaded.fastutil.ints.Int2ObjectMap;
import ac.grim.grimac.shaded.fastutil.ints.Int2ObjectOpenHashMap;
import ac.grim.grimac.shaded.fastutil.longs.Long2ObjectMap;
import ac.grim.grimac.shaded.fastutil.longs.Long2ObjectOpenHashMap;
import ac.grim.grimac.shaded.fastutil.objects.Object2ObjectLinkedOpenHashMap;
import ac.grim.grimac.shaded.fastutil.objects.ObjectIterator;
import ac.grim.grimac.utils.change.BlockModification;
import ac.grim.grimac.utils.chunks.Column;
import ac.grim.grimac.utils.collisions.CollisionData;
import ac.grim.grimac.utils.collisions.datatypes.SimpleCollisionBox;
import ac.grim.grimac.utils.data.BlockPrediction;
import ac.grim.grimac.utils.data.Pair;
import ac.grim.grimac.utils.data.PistonData;
import ac.grim.grimac.utils.data.ShulkerData;
import ac.grim.grimac.utils.data.packetentity.PacketEntity;
import ac.grim.grimac.utils.data.packetentity.PacketEntityShulker;
import ac.grim.grimac.utils.math.GrimMath;
import ac.grim.grimac.utils.math.Vector3dm;
import ac.grim.grimac.utils.nmsutil.Collisions;
import ac.grim.grimac.utils.nmsutil.GetBoundingBox;
import ac.grim.grimac.utils.nmsutil.Materials;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.Map.Entry;
import lombok.Generated;

public class CompensatedWorld implements PacketWorld {
   public static final ClientVersion blockVersion = PacketEvents.getAPI().getServerManager().getVersion().toClientVersion();
   private static final WrappedBlockState airData;
   public final GrimPlayer player;
   public final Long2ObjectMap<Column> chunks;
   public final Set<PistonData> activePistons = new HashSet();
   public final Set<ShulkerData> openShulkerBoxes = new HashSet();
   private int minHeight = 0;
   private int maxHeight = 256;
   private final Long2ObjectOpenHashMap<BlockPrediction> originalServerBlocks = new Long2ObjectOpenHashMap();
   private List<Vector3i> currentlyChangedBlocks = new LinkedList();
   private final Int2ObjectMap<List<Vector3i>> serverIsCurrentlyProcessingThesePredictions = new Int2ObjectOpenHashMap();
   private final Object2ObjectLinkedOpenHashMap<Pair<Vector3i, DiggingAction>, Vector3d> unackedActions = new Object2ObjectLinkedOpenHashMap();
   private boolean isCurrentlyPredicting = false;
   public boolean isRaining = false;
   private final boolean noNegativeBlocks;

   public CompensatedWorld(GrimPlayer player) {
      this.player = player;
      this.chunks = new Long2ObjectOpenHashMap(81, 0.5F);
      this.noNegativeBlocks = player.getClientVersion().isOlderThanOrEquals(ClientVersion.V_1_16_4);
   }

   public void startPredicting() {
      if (!this.player.getClientVersion().isOlderThanOrEquals(ClientVersion.V_1_18_2)) {
         this.isCurrentlyPredicting = true;
      }
   }

   public void handlePredictionConfirmation(int prediction) {
      ObjectIterator it = this.serverIsCurrentlyProcessingThesePredictions.int2ObjectEntrySet().iterator();

      while(it.hasNext()) {
         Entry<Integer, List<Vector3i>> iter = (Entry)it.next();
         if ((Integer)iter.getKey() > prediction) {
            break;
         }

         this.applyBlockChanges((List)iter.getValue());
         it.remove();
      }

   }

   public void handleBlockBreakAck(Vector3i blockPos, int blockState, DiggingAction action, boolean accepted) {
      if (accepted && action == DiggingAction.START_DIGGING && this.unackedActions.containsKey(new Pair(blockPos, action))) {
         this.unackedActions.remove(new Pair(blockPos, action));
      } else {
         this.player.sendTransaction();
         this.player.latencyUtils.addRealTimeTask(this.player.lastTransactionSent.get(), () -> {
            Vector3d playerPos = (Vector3d)this.unackedActions.remove(new Pair(blockPos, action));
            this.handleAck(blockPos, blockState, playerPos);
         });
      }

      this.player.latencyUtils.addRealTimeTask(this.player.lastTransactionSent.get(), () -> {
         while(this.unackedActions.size() >= 50) {
            this.unackedActions.removeFirst();
         }

      });
   }

   private void applyBlockChanges(List<Vector3i> toApplyBlocks) {
      this.player.sendTransaction();
      this.player.latencyUtils.addRealTimeTask(this.player.lastTransactionSent.get(), () -> {
         toApplyBlocks.forEach((vector3i) -> {
            BlockPrediction predictionData = (BlockPrediction)this.originalServerBlocks.get(vector3i.getSerializedPosition());
            if (predictionData != null && predictionData.getForBlockUpdate() == toApplyBlocks) {
               this.originalServerBlocks.remove(vector3i.getSerializedPosition());
               this.handleAck(vector3i, predictionData.getOriginalBlockId(), predictionData.getPlayerPosition());
            }

         });
      });
   }

   private void handleAck(Vector3i vector3i, int originalBlockId, Vector3d playerPosition) {
      if (this.getBlock(vector3i).getGlobalId() != originalBlockId) {
         this.player.blockHistory.add(new BlockModification(this.getBlock(vector3i), WrappedBlockState.getByGlobalId(originalBlockId), vector3i, GrimAPI.INSTANCE.getTickManager().currentTick, BlockModification.Cause.HANDLE_NETTY_SYNC_TRANSACTION));
         this.updateBlock(vector3i.getX(), vector3i.getY(), vector3i.getZ(), originalBlockId);
         WrappedBlockState state = WrappedBlockState.getByGlobalId(blockVersion, originalBlockId);
         if (playerPosition != null && CollisionData.getData(state.getType()).getMovementCollisionBox(this.player, this.player.getClientVersion(), state, vector3i.getX(), vector3i.getY(), vector3i.getZ()).isIntersected(this.player.boundingBox)) {
            this.player.lastX = this.player.x;
            this.player.lastY = this.player.y;
            this.player.lastZ = this.player.z;
            this.player.x = playerPosition.getX();
            this.player.y = playerPosition.getY();
            this.player.z = playerPosition.getZ();
            this.player.boundingBox = GetBoundingBox.getCollisionBoxForPlayer(this.player, this.player.x, this.player.y, this.player.z);
         }
      }

   }

   public void handleBlockBreakPrediction(WrapperPlayClientPlayerDigging digging) {
      if (this.player.getClientVersion().isNewerThanOrEquals(ClientVersion.V_1_14_4) && this.player.getClientVersion().isOlderThanOrEquals(ClientVersion.V_1_18_2)) {
         this.unackedActions.put(new Pair(digging.getBlockPosition(), digging.getAction()), new Vector3d(this.player.x, this.player.y, this.player.z));
      }

   }

   public void stopPredicting(PacketWrapper<?> wrapper) {
      if (!this.player.getClientVersion().isOlderThanOrEquals(ClientVersion.V_1_18_2)) {
         this.isCurrentlyPredicting = false;
         if (!this.currentlyChangedBlocks.isEmpty()) {
            List<Vector3i> toApplyBlocks = this.currentlyChangedBlocks;
            this.currentlyChangedBlocks = new LinkedList();
            if (PacketEvents.getAPI().getServerManager().getVersion().isNewerThanOrEquals(ServerVersion.V_1_19)) {
               int confirmationId = 0;
               if (wrapper instanceof WrapperPlayClientPlayerBlockPlacement) {
                  confirmationId = ((WrapperPlayClientPlayerBlockPlacement)wrapper).getSequence();
               } else if (wrapper instanceof WrapperPlayClientUseItem) {
                  confirmationId = ((WrapperPlayClientUseItem)wrapper).getSequence();
               } else if (wrapper instanceof WrapperPlayClientPlayerDigging) {
                  confirmationId = ((WrapperPlayClientPlayerDigging)wrapper).getSequence();
               }

               this.serverIsCurrentlyProcessingThesePredictions.put(confirmationId, toApplyBlocks);
            } else {
               GrimAPI.INSTANCE.getScheduler().getGlobalRegionScheduler().run(GrimAPI.INSTANCE.getGrimPlugin(), () -> {
                  this.player.runSafely(() -> {
                     this.applyBlockChanges(toApplyBlocks);
                  });
               });
            }

         }
      }
   }

   public static long chunkPositionToLong(int x, int z) {
      return ((long)x & 4294967295L) << 32 | (long)z & 4294967295L;
   }

   public boolean isNearHardEntity(SimpleCollisionBox playerBox) {
      ObjectIterator var2 = this.player.compensatedEntities.entityMap.values().iterator();

      while(true) {
         PacketEntity entity;
         SimpleCollisionBox box;
         do {
            if (!var2.hasNext()) {
               Iterator var6 = this.openShulkerBoxes.iterator();

               while(var6.hasNext()) {
                  ShulkerData data = (ShulkerData)var6.next();
                  box = data.getCollision();
                  if (playerBox.isCollided(box)) {
                     return true;
                  }
               }

               var6 = this.activePistons.iterator();

               while(var6.hasNext()) {
                  PistonData data = (PistonData)var6.next();
                  Iterator var9 = data.boxes.iterator();

                  while(var9.hasNext()) {
                     SimpleCollisionBox box = (SimpleCollisionBox)var9.next();
                     if (playerBox.isCollided(box)) {
                        return true;
                     }
                  }
               }

               return false;
            }

            entity = (PacketEntity)var2.next();
         } while(!entity.isBoat && entity.type != EntityTypes.SHULKER && !entity.isHappyGhast);

         if (this.player.compensatedEntities.self.getRiding() != entity) {
            box = entity.getPossibleCollisionBoxes();
            if (box.isIntersected(playerBox)) {
               return true;
            }
         }
      }
   }

   private static BaseChunk create() {
      if (PacketEvents.getAPI().getServerManager().getVersion().isNewerThanOrEquals(ServerVersion.V_1_18)) {
         return new Chunk_v1_18();
      } else {
         return PacketEvents.getAPI().getServerManager().getVersion().isNewerThanOrEquals(ServerVersion.V_1_16) ? new Chunk_v1_9(0, DataPalette.createForChunk()) : new Chunk_v1_9(0, new DataPalette(new ListPalette(4), new LegacyFlexibleStorage(4, 4096), PaletteType.CHUNK));
      }
   }

   public void updateBlock(Vector3i pos, WrappedBlockState state) {
      this.updateBlock(pos.getX(), pos.getY(), pos.getZ(), state.getGlobalId());
   }

   public void updateBlock(int x, int y, int z, int combinedID) {
      Vector3i asVector = new Vector3i(x, y, z);
      BlockPrediction prediction = (BlockPrediction)this.originalServerBlocks.get(asVector.getSerializedPosition());
      if (this.isCurrentlyPredicting) {
         if (prediction == null) {
            this.originalServerBlocks.put(asVector.getSerializedPosition(), new BlockPrediction(this.currentlyChangedBlocks, asVector, this.getBlock(asVector).getGlobalId(), new Vector3d(this.player.x, this.player.y, this.player.z)));
         } else {
            prediction.setForBlockUpdate(this.currentlyChangedBlocks);
         }

         this.currentlyChangedBlocks.add(asVector);
      }

      if (!this.isCurrentlyPredicting && prediction != null) {
         prediction.setOriginalBlockId(combinedID);
      } else {
         Column column = this.getChunk(x >> 4, z >> 4);
         int offsetY = y - this.minHeight;
         if (column != null) {
            if (column.chunks().length <= offsetY >> 4 || offsetY >> 4 < 0) {
               return;
            }

            BaseChunk chunk = column.chunks()[offsetY >> 4];
            if (chunk == null) {
               chunk = create();
               column.chunks()[offsetY >> 4] = chunk;
               chunk.set((ClientVersion)null, 0, 0, 0, 0);
            }

            this.player.pointThreeEstimator.handleChangeBlock(x, y, z, chunk.get(blockVersion, x & 15, offsetY & 15, z & 15));
            chunk.set((ClientVersion)null, x & 15, offsetY & 15, z & 15, combinedID);
            this.player.pointThreeEstimator.handleChangeBlock(x, y, z, WrappedBlockState.getByGlobalId(blockVersion, combinedID));
         }

      }
   }

   public void tickOpenable(int blockX, int blockY, int blockZ) {
      WrappedBlockState data = this.getBlock(blockX, blockY, blockZ);
      StateType type = data.getType();
      if (Materials.isClientSideOpenableDoor(type, this.player.getClientVersion())) {
         WrappedBlockState otherDoor = this.getBlock(blockX, blockY + (data.getHalf() == Half.LOWER ? 1 : -1), blockZ);
         if (PacketEvents.getAPI().getServerManager().getVersion().isNewerThanOrEquals(ServerVersion.V_1_13)) {
            if (BlockTags.DOORS.contains(otherDoor.getType())) {
               otherDoor.setOpen(!otherDoor.isOpen());
               this.updateBlock(blockX, blockY + (data.getHalf() == Half.LOWER ? 1 : -1), blockZ, otherDoor.getGlobalId());
            }

            data.setOpen(!data.isOpen());
            this.updateBlock(blockX, blockY, blockZ, data.getGlobalId());
         } else if (data.getHalf() == Half.LOWER) {
            data.setOpen(!data.isOpen());
            this.updateBlock(blockX, blockY, blockZ, data.getGlobalId());
         } else if (BlockTags.DOORS.contains(otherDoor.getType()) && otherDoor.getHalf() == Half.LOWER) {
            otherDoor.setOpen(!otherDoor.isOpen());
            this.updateBlock(blockX, blockY - 1, blockZ, otherDoor.getGlobalId());
         }
      } else if (!Materials.isClientSideOpenableTrapdoor(type, this.player.getClientVersion()) && !BlockTags.FENCE_GATES.contains(type)) {
         if (BlockTags.BUTTONS.contains(type)) {
            data.setPowered(true);
         }
      } else {
         data.setOpen(!data.isOpen());
         this.updateBlock(blockX, blockY, blockZ, data.getGlobalId());
      }

   }

   public void tickPlayerInPistonPushingArea() {
      this.player.uncertaintyHandler.tick();
      if (this.player.boundingBox != null) {
         SimpleCollisionBox expandedBB = GetBoundingBox.getBoundingBoxFromPosAndSize(this.player, this.player.lastX, this.player.lastY, this.player.lastZ, 0.001F, 0.001F);
         expandedBB.expandToAbsoluteCoordinates(this.player.x, this.player.y, this.player.z);
         SimpleCollisionBox playerBox = expandedBB.copy().expand(1.0D);
         double modX = 0.0D;
         double modY = 0.0D;
         double modZ = 0.0D;
         Iterator var9 = this.activePistons.iterator();

         while(true) {
            PistonData data;
            label66:
            do {
               while(var9.hasNext()) {
                  data = (PistonData)var9.next();
                  Iterator var11 = data.boxes.iterator();

                  while(var11.hasNext()) {
                     SimpleCollisionBox box = (SimpleCollisionBox)var11.next();
                     if (playerBox.isCollided(box)) {
                        modX = Math.max(modX, Math.abs((double)data.direction.getModX() * 0.51D));
                        modY = Math.max(modY, Math.abs((double)data.direction.getModY() * 0.51D));
                        modZ = Math.max(modZ, Math.abs((double)data.direction.getModZ() * 0.51D));
                        playerBox.expandMax(modX, modY, modZ);
                        playerBox.expandMin(modX * -1.0D, modY * -1.0D, modZ * -1.0D);
                        continue label66;
                     }
                  }
               }

               var9 = this.openShulkerBoxes.iterator();

               while(var9.hasNext()) {
                  ShulkerData data = (ShulkerData)var9.next();
                  SimpleCollisionBox shulkerCollision = data.getCollision();
                  BlockFace direction;
                  if (data.entity == null) {
                     WrappedBlockState state = this.getBlock(data.blockPos.getX(), data.blockPos.getY(), data.blockPos.getZ());
                     direction = state.getFacing();
                  } else {
                     direction = ((PacketEntityShulker)data.entity).facing.getOppositeFace();
                  }

                  if (direction == null) {
                     direction = BlockFace.UP;
                  }

                  if (direction.getModX() != -1 && direction.getModY() != -1 && direction.getModZ() != -1) {
                     shulkerCollision.expandMax((double)direction.getModZ(), (double)direction.getModY(), (double)direction.getModZ());
                  } else {
                     shulkerCollision.expandMin((double)direction.getModX(), (double)direction.getModY(), (double)direction.getModZ());
                  }

                  if (playerBox.isCollided(shulkerCollision)) {
                     modX = Math.max(modX, Math.abs((double)direction.getModX() * 0.51D));
                     modY = Math.max(modY, Math.abs((double)direction.getModY() * 0.51D));
                     modZ = Math.max(modZ, Math.abs((double)direction.getModZ() * 0.51D));
                     playerBox.expandMax(modX, modY, modZ);
                     playerBox.expandMin(modX, modY, modZ);
                     this.player.uncertaintyHandler.isSteppingNearShulker = true;
                  }
               }

               this.player.uncertaintyHandler.pistonX.add(modX);
               this.player.uncertaintyHandler.pistonY.add(modY);
               this.player.uncertaintyHandler.pistonZ.add(modZ);
               this.removeInvalidPistonLikeStuff(0);
               return;
            } while(!data.hasSlimeBlock && (!data.hasHoneyBlock || !this.player.getClientVersion().isOlderThan(ClientVersion.V_1_15_2)));

            this.player.uncertaintyHandler.slimePistonBounces.add(data.direction);
         }
      }
   }

   public void removeInvalidPistonLikeStuff(int transactionId) {
      if (transactionId != 0) {
         this.activePistons.removeIf((data) -> {
            return data.lastTransactionSent < transactionId;
         });
         this.openShulkerBoxes.removeIf((data) -> {
            return data.isClosing && data.lastTransactionSent < transactionId;
         });
      } else {
         this.activePistons.removeIf(PistonData::tickIfGuaranteedFinished);
         this.openShulkerBoxes.removeIf(ShulkerData::tickIfGuaranteedFinished);
      }

      this.openShulkerBoxes.removeIf((box) -> {
         if (box.blockPos != null) {
            return !Materials.isShulker(this.getBlock(box.blockPos).getType());
         } else {
            return !this.player.compensatedEntities.entityMap.containsValue(box.entity);
         }
      });
   }

   public WrappedBlockState getBlock(Vector3i position) {
      return this.getBlock(position.x, position.y, position.z);
   }

   public WrappedBlockState getBlock(int x, int y, int z) {
      if (this.noNegativeBlocks && y < 0) {
         return airData;
      } else {
         try {
            Column column = this.getChunk(x >> 4, z >> 4);
            y -= this.minHeight;
            if (column == null || y < 0 || y >> 4 >= column.chunks().length) {
               return airData;
            }

            BaseChunk chunk = column.chunks()[y >> 4];
            if (chunk != null) {
               return chunk.get(blockVersion, x & 15, y & 15, z & 15);
            }
         } catch (Exception var6) {
         }

         return airData;
      }
   }

   public int getBlockStateId(int x, int y, int z) {
      if (this.noNegativeBlocks && y < 0) {
         return -1;
      } else {
         try {
            Column column = this.getChunk(x >> 4, z >> 4);
            y -= this.minHeight;
            if (column == null || y < 0 || y >> 4 >= column.chunks().length) {
               return -2;
            }

            BaseChunk chunk = column.chunks()[y >> 4];
            if (chunk != null) {
               return chunk.getBlockId(x & 15, y & 15, z & 15);
            }
         } catch (Exception var6) {
         }

         return -3;
      }
   }

   public int getRawPowerAtState(BlockFace face, int x, int y, int z) {
      WrappedBlockState block = this.getBlock(x, y, z);
      if (block.getType() == StateTypes.REDSTONE_BLOCK) {
         return 15;
      } else if (block.getType() == StateTypes.DETECTOR_RAIL) {
         return block.isPowered() ? 15 : 0;
      } else if (block.getType() == StateTypes.REDSTONE_TORCH) {
         return face != BlockFace.UP && block.isLit() ? 15 : 0;
      } else if (block.getType() == StateTypes.REDSTONE_WIRE) {
         BlockFace needed = face.getOppositeFace();
         BlockFace badOne = needed.getCW();
         BlockFace badTwo = needed.getCCW();
         boolean isPowered = false;
         if (PacketEvents.getAPI().getServerManager().getVersion().isNewerThanOrEquals(ServerVersion.V_1_13)) {
            switch(needed) {
            case DOWN:
               isPowered = true;
               break;
            case NORTH:
               isPowered = block.getNorth() == North.TRUE;
               if (!isPowered || badOne != BlockFace.NORTH && badTwo != BlockFace.NORTH) {
                  break;
               }

               return 0;
            case SOUTH:
               isPowered = block.getSouth() == South.TRUE;
               if (!isPowered || badOne != BlockFace.SOUTH && badTwo != BlockFace.SOUTH) {
                  break;
               }

               return 0;
            case WEST:
               isPowered = block.getWest() == West.TRUE;
               if (!isPowered || badOne != BlockFace.WEST && badTwo != BlockFace.WEST) {
                  break;
               }

               return 0;
            case EAST:
               isPowered = block.getEast() == East.TRUE;
               if (isPowered && (badOne == BlockFace.EAST || badTwo == BlockFace.EAST)) {
                  return 0;
               }
            }
         } else {
            isPowered = true;
         }

         return isPowered ? block.getPower() : 0;
      } else if (block.getType() == StateTypes.REDSTONE_WALL_TORCH) {
         return block.getFacing() != face && block.isLit() ? 15 : 0;
      } else if (block.getType() == StateTypes.DAYLIGHT_DETECTOR) {
         return block.getPower();
      } else if (block.getType() == StateTypes.OBSERVER) {
         return block.getFacing() == face && block.isPowered() ? 15 : 0;
      } else if (block.getType() != StateTypes.REPEATER) {
         if (block.getType() == StateTypes.LECTERN) {
            return block.isPowered() ? 15 : 0;
         } else {
            return block.getType() == StateTypes.TARGET ? block.getPower() : 0;
         }
      } else {
         return block.getFacing() == face && block.isPowered() ? 15 : 0;
      }
   }

   public int getDirectSignalAtState(BlockFace face, int x, int y, int z) {
      WrappedBlockState block = this.getBlock(x, y, z);
      if (block.getType() == StateTypes.DETECTOR_RAIL) {
         boolean isPowered = block.hasProperty(StateValue.POWERED) && block.isPowered();
         return face == BlockFace.UP && isPowered ? 15 : 0;
      } else if (block.getType() == StateTypes.REDSTONE_TORCH) {
         return face != BlockFace.UP && block.isLit() ? 15 : 0;
      } else if (block.getType() != StateTypes.LEVER && !BlockTags.BUTTONS.contains(block.getType())) {
         if (block.getType() == StateTypes.REDSTONE_WALL_TORCH) {
            return face == BlockFace.DOWN && block.isPowered() ? 15 : 0;
         } else if (block.getType() == StateTypes.LECTERN) {
            return face == BlockFace.UP && block.isPowered() ? 15 : 0;
         } else if (block.getType() == StateTypes.OBSERVER) {
            return block.getFacing() == face && block.isPowered() ? 15 : 0;
         } else if (block.getType() == StateTypes.REPEATER) {
            return block.getFacing() == face && block.isPowered() ? 15 : 0;
         } else if (block.getType() != StateTypes.REDSTONE_WIRE) {
            return 0;
         } else {
            BlockFace needed = face.getOppositeFace();
            BlockFace badOne = needed.getCW();
            BlockFace badTwo = needed.getCCW();
            boolean isPowered = false;
            switch(needed) {
            case DOWN:
            case UP:
            default:
               break;
            case NORTH:
               isPowered = block.getNorth() == North.TRUE;
               if (!isPowered || badOne != BlockFace.NORTH && badTwo != BlockFace.NORTH) {
                  break;
               }

               return 0;
            case SOUTH:
               isPowered = block.getSouth() == South.TRUE;
               if (isPowered && (badOne == BlockFace.SOUTH || badTwo == BlockFace.SOUTH)) {
                  return 0;
               }
               break;
            case WEST:
               isPowered = block.getWest() == West.TRUE;
               if (isPowered && (badOne == BlockFace.WEST || badTwo == BlockFace.WEST)) {
                  return 0;
               }
               break;
            case EAST:
               isPowered = block.getEast() == East.TRUE;
               if (isPowered && (badOne == BlockFace.EAST || badTwo == BlockFace.EAST)) {
                  return 0;
               }
            }

            return isPowered ? block.getPower() : 0;
         }
      } else {
         return block.getFacing().getOppositeFace() == face && block.isPowered() ? 15 : 0;
      }
   }

   public Column getChunk(int chunkX, int chunkZ) {
      long chunkPosition = chunkPositionToLong(chunkX, chunkZ);
      return (Column)this.chunks.get(chunkPosition);
   }

   public boolean isChunkLoaded(int chunkX, int chunkZ) {
      long chunkPosition = chunkPositionToLong(chunkX, chunkZ);
      return this.chunks.containsKey(chunkPosition);
   }

   public void addToCache(Column chunk, int chunkX, int chunkZ) {
      long chunkPosition = chunkPositionToLong(chunkX, chunkZ);
      this.player.latencyUtils.addRealTimeTask(this.player.lastTransactionSent.get(), () -> {
         this.chunks.put(chunkPosition, chunk);
      });
   }

   public StateType getBlockType(double x, double y, double z) {
      return this.getBlock((int)Math.floor(x), (int)Math.floor(y), (int)Math.floor(z)).getType();
   }

   public WrappedBlockState getBlock(double x, double y, double z) {
      return this.getBlock((int)Math.floor(x), (int)Math.floor(y), (int)Math.floor(z));
   }

   public double getFluidLevelAt(int x, int y, int z) {
      return Math.max(this.getWaterFluidLevelAt(x, y, z), this.getLavaFluidLevelAt(x, y, z));
   }

   public boolean isWaterSourceBlock(int x, int y, int z) {
      WrappedBlockState bukkitBlock = this.getBlock(x, y, z);
      return Materials.isWaterSource(this.player.getClientVersion(), bukkitBlock);
   }

   public boolean containsLiquid(SimpleCollisionBox var0) {
      return Collisions.hasMaterial(this.player, var0, (data) -> {
         return Materials.isWater(this.player.getClientVersion(), (WrappedBlockState)data.first()) || ((WrappedBlockState)data.first()).getType() == StateTypes.LAVA;
      });
   }

   public double getLavaFluidLevelAt(int x, int y, int z) {
      WrappedBlockState magicBlockState = this.getBlock(x, y, z);
      WrappedBlockState magicBlockStateAbove = this.getBlock(x, y + 1, z);
      if (magicBlockState.getType() != StateTypes.LAVA) {
         return 0.0D;
      } else if (magicBlockStateAbove.getType() == StateTypes.LAVA) {
         return 1.0D;
      } else {
         int level = magicBlockState.getLevel();
         return level >= 8 ? 0.8888888955116272D : (double)((float)(8 - level) / 9.0F);
      }
   }

   public boolean containsLava(SimpleCollisionBox var0) {
      return Collisions.hasMaterial(this.player, var0, (data) -> {
         return ((WrappedBlockState)data.first()).getType() == StateTypes.LAVA;
      });
   }

   public double getWaterFluidLevelAt(double x, double y, double z) {
      return this.getWaterFluidLevelAt(GrimMath.floor(x), GrimMath.floor(y), GrimMath.floor(z));
   }

   public double getWaterFluidLevelAt(int x, int y, int z) {
      WrappedBlockState wrappedBlock = this.getBlock(x, y, z);
      boolean isWater = Materials.isWater(this.player.getClientVersion(), wrappedBlock);
      if (!isWater) {
         return 0.0D;
      } else if (Materials.isWater(this.player.getClientVersion(), this.getBlock(x, y + 1, z))) {
         return 1.0D;
      } else if (wrappedBlock.getType() == StateTypes.WATER) {
         int level = wrappedBlock.getLevel();
         return (level & 8) == 8 ? 0.8888888955116272D : (double)((float)(8 - level) / 9.0F);
      } else {
         return 0.8888888955116272D;
      }
   }

   public void removeChunkLater(int chunkX, int chunkZ) {
      long chunkPosition = chunkPositionToLong(chunkX, chunkZ);
      this.player.latencyUtils.addRealTimeTask(this.player.lastTransactionSent.get(), () -> {
         this.chunks.remove(chunkPosition);
      });
   }

   public void setDimension(DimensionType dimension, User user) {
      if (!PacketEvents.getAPI().getServerManager().getVersion().isOlderThan(ServerVersion.V_1_17)) {
         this.minHeight = dimension.getMinY();
         this.maxHeight = this.minHeight + dimension.getHeight();
      }
   }

   public WrappedBlockState getBlock(Vector3dm aboveCCWPos) {
      return this.getBlock(aboveCCWPos.getX(), aboveCCWPos.getY(), aboveCCWPos.getZ());
   }

   @Generated
   public int getMinHeight() {
      return this.minHeight;
   }

   @Generated
   public int getMaxHeight() {
      return this.maxHeight;
   }

   static {
      airData = WrappedBlockState.getByGlobalId(blockVersion, 0);
   }
}
