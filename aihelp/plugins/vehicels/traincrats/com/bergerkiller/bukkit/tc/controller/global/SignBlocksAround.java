package com.bergerkiller.bukkit.tc.controller.global;

import com.bergerkiller.bukkit.common.utils.FaceUtil;
import com.bergerkiller.bukkit.tc.utils.LongBlockCoordinates;
import java.util.EnumMap;
import java.util.function.LongUnaryOperator;
import org.bukkit.block.BlockFace;

abstract class SignBlocksAround {
   private static final EnumMap<BlockFace, SignBlocksAround> cache = new EnumMap(BlockFace.class);
   private SignBlocksAround opposite;
   private final LongUnaryOperator operator;
   private final BlockFace attachedFace;

   public static SignBlocksAround of(BlockFace attachedFace) {
      return (SignBlocksAround)cache.get(attachedFace);
   }

   private SignBlocksAround(BlockFace attachedFace) {
      this.attachedFace = attachedFace;
      this.operator = LongBlockCoordinates.shiftOperator(attachedFace);
   }

   public final BlockFace getAttachedFace() {
      return this.attachedFace;
   }

   public abstract void forAllNeighboursExceptDirection(long var1, SignController.Entry var3, SignBlocksAround.EntryBlockConsumer var4);

   public final void forAllBlocks(SignController.Entry entry, SignBlocksAround.EntryBlockConsumer consumer) {
      long blockKey = entry.blockKey;
      this.forAllNeighboursExceptDirection(blockKey, entry, consumer);
      long blockKeyNeighbour = this.operator.applyAsLong(blockKey);
      this.opposite.forAllNeighboursExceptDirection(blockKeyNeighbour, entry, consumer);
   }

   // $FF: synthetic method
   SignBlocksAround(BlockFace x0, Object x1) {
      this(x0);
   }

   static {
      cache.put(BlockFace.SELF, new SignBlocksAround(BlockFace.SELF) {
         public void forAllNeighboursExceptDirection(long blockKey, SignController.Entry entry, SignBlocksAround.EntryBlockConsumer consumer) {
            consumer.accept(entry, blockKey);
            consumer.accept(entry, LongBlockCoordinates.shiftUp(blockKey));
            consumer.accept(entry, LongBlockCoordinates.shiftDown(blockKey));
            consumer.accept(entry, LongBlockCoordinates.shiftEast(blockKey));
            consumer.accept(entry, LongBlockCoordinates.shiftWest(blockKey));
            consumer.accept(entry, LongBlockCoordinates.shiftSouth(blockKey));
            consumer.accept(entry, LongBlockCoordinates.shiftNorth(blockKey));
         }
      });
      ((SignBlocksAround)cache.get(BlockFace.SELF)).opposite = new SignBlocksAround(BlockFace.SELF) {
         public void forAllNeighboursExceptDirection(long blockKey, SignController.Entry entry, SignBlocksAround.EntryBlockConsumer consumer) {
         }
      };
      cache.put(BlockFace.NORTH, new SignBlocksAround(BlockFace.NORTH) {
         public void forAllNeighboursExceptDirection(long blockKey, SignController.Entry entry, SignBlocksAround.EntryBlockConsumer consumer) {
            consumer.accept(entry, blockKey);
            consumer.accept(entry, LongBlockCoordinates.shiftUp(blockKey));
            consumer.accept(entry, LongBlockCoordinates.shiftDown(blockKey));
            consumer.accept(entry, LongBlockCoordinates.shiftEast(blockKey));
            consumer.accept(entry, LongBlockCoordinates.shiftWest(blockKey));
            consumer.accept(entry, LongBlockCoordinates.shiftSouth(blockKey));
         }
      });
      cache.put(BlockFace.EAST, new SignBlocksAround(BlockFace.EAST) {
         public void forAllNeighboursExceptDirection(long blockKey, SignController.Entry entry, SignBlocksAround.EntryBlockConsumer consumer) {
            consumer.accept(entry, blockKey);
            consumer.accept(entry, LongBlockCoordinates.shiftUp(blockKey));
            consumer.accept(entry, LongBlockCoordinates.shiftDown(blockKey));
            consumer.accept(entry, LongBlockCoordinates.shiftWest(blockKey));
            consumer.accept(entry, LongBlockCoordinates.shiftSouth(blockKey));
            consumer.accept(entry, LongBlockCoordinates.shiftNorth(blockKey));
         }
      });
      cache.put(BlockFace.SOUTH, new SignBlocksAround(BlockFace.SOUTH) {
         public void forAllNeighboursExceptDirection(long blockKey, SignController.Entry entry, SignBlocksAround.EntryBlockConsumer consumer) {
            consumer.accept(entry, blockKey);
            consumer.accept(entry, LongBlockCoordinates.shiftUp(blockKey));
            consumer.accept(entry, LongBlockCoordinates.shiftDown(blockKey));
            consumer.accept(entry, LongBlockCoordinates.shiftEast(blockKey));
            consumer.accept(entry, LongBlockCoordinates.shiftWest(blockKey));
            consumer.accept(entry, LongBlockCoordinates.shiftNorth(blockKey));
         }
      });
      cache.put(BlockFace.WEST, new SignBlocksAround(BlockFace.WEST) {
         public void forAllNeighboursExceptDirection(long blockKey, SignController.Entry entry, SignBlocksAround.EntryBlockConsumer consumer) {
            consumer.accept(entry, blockKey);
            consumer.accept(entry, LongBlockCoordinates.shiftUp(blockKey));
            consumer.accept(entry, LongBlockCoordinates.shiftDown(blockKey));
            consumer.accept(entry, LongBlockCoordinates.shiftEast(blockKey));
            consumer.accept(entry, LongBlockCoordinates.shiftSouth(blockKey));
            consumer.accept(entry, LongBlockCoordinates.shiftNorth(blockKey));
         }
      });
      cache.put(BlockFace.UP, new SignBlocksAround(BlockFace.UP) {
         public void forAllNeighboursExceptDirection(long blockKey, SignController.Entry entry, SignBlocksAround.EntryBlockConsumer consumer) {
            consumer.accept(entry, blockKey);
            consumer.accept(entry, LongBlockCoordinates.shiftDown(blockKey));
            consumer.accept(entry, LongBlockCoordinates.shiftEast(blockKey));
            consumer.accept(entry, LongBlockCoordinates.shiftWest(blockKey));
            consumer.accept(entry, LongBlockCoordinates.shiftSouth(blockKey));
            consumer.accept(entry, LongBlockCoordinates.shiftNorth(blockKey));
         }
      });
      cache.put(BlockFace.DOWN, new SignBlocksAround(BlockFace.DOWN) {
         public void forAllNeighboursExceptDirection(long blockKey, SignController.Entry entry, SignBlocksAround.EntryBlockConsumer consumer) {
            consumer.accept(entry, blockKey);
            consumer.accept(entry, LongBlockCoordinates.shiftUp(blockKey));
            consumer.accept(entry, LongBlockCoordinates.shiftEast(blockKey));
            consumer.accept(entry, LongBlockCoordinates.shiftWest(blockKey));
            consumer.accept(entry, LongBlockCoordinates.shiftSouth(blockKey));
            consumer.accept(entry, LongBlockCoordinates.shiftNorth(blockKey));
         }
      });
      BlockFace[] var0 = FaceUtil.BLOCK_SIDES;
      int var1 = var0.length;

      int var2;
      BlockFace other;
      for(var2 = 0; var2 < var1; ++var2) {
         other = var0[var2];
         ((SignBlocksAround)cache.get(other)).opposite = (SignBlocksAround)cache.get(other.getOppositeFace());
      }

      var0 = BlockFace.values();
      var1 = var0.length;

      for(var2 = 0; var2 < var1; ++var2) {
         other = var0[var2];
         if (!cache.containsKey(other)) {
            cache.put(other, (SignBlocksAround)cache.get(BlockFace.SELF));
         }
      }

   }

   @FunctionalInterface
   public interface EntryBlockConsumer {
      void accept(SignController.Entry var1, long var2);
   }
}
