package com.bergerkiller.bukkit.tc.utils;

import com.bergerkiller.bukkit.common.Common;
import com.bergerkiller.bukkit.common.Logging;
import com.bergerkiller.bukkit.common.utils.CommonUtil;
import com.bergerkiller.bukkit.common.utils.WorldUtil;
import com.bergerkiller.bukkit.common.wrappers.BlockData;
import com.bergerkiller.bukkit.tc.TrainCarts;
import com.bergerkiller.generated.net.minecraft.world.level.block.state.IBlockDataHandle;
import com.bergerkiller.mountiplex.reflection.util.FastConstructor;
import com.bergerkiller.mountiplex.reflection.util.FastMethod;
import java.util.logging.Level;
import org.bukkit.block.Block;
import org.bukkit.event.block.BlockPhysicsEvent;

public abstract class BlockPhysicsEventDataAccessor {
   public static BlockPhysicsEventDataAccessor INSTANCE;

   public abstract BlockData get(BlockPhysicsEvent var1);

   public abstract BlockPhysicsEvent createEvent(Block var1, BlockData var2);

   static {
      try {
         INSTANCE = new BlockPhysicsEventDataAccessor.BlockPhysicsEventDataAccessorEventField();
      } catch (Throwable var3) {
         try {
            if (Common.evaluateMCVersion(">=", "1.13")) {
               INSTANCE = new BlockPhysicsEventDataAccessor.BlockPhysicsEventDataAccessorDefaultModern();
            } else {
               INSTANCE = new BlockPhysicsEventDataAccessor.BlockPhysicsEventDataAccessorDefaultLegacy();
            }
         } catch (Throwable var2) {
            TrainCarts.plugin.getLogger().log(Level.SEVERE, "Failed to initialize block physics event data accessor", var3);
            INSTANCE = new BlockPhysicsEventDataAccessor.BlockPhysicsEventDataAccessorFallback();
         }
      }

   }

   private static final class BlockPhysicsEventDataAccessorEventField extends BlockPhysicsEventDataAccessor.BlockPhysicsEventDataAccessorDefaultModern {
      private final FastMethod<Object> blockDataGetter;
      private final FastMethod<Object> blockDataGetState;

      public BlockPhysicsEventDataAccessorEventField() throws Throwable {
         Class<?> cbd = CommonUtil.getClass("org.bukkit.craftbukkit.block.data.CraftBlockData");
         this.blockDataGetter = new FastMethod(BlockPhysicsEvent.class.getDeclaredMethod("getChangedBlockData"));
         this.blockDataGetState = new FastMethod(cbd.getDeclaredMethod("getState"));
         this.blockDataGetter.forceInitialization();
         this.blockDataGetState.forceInitialization();
      }

      public BlockData get(BlockPhysicsEvent event) {
         try {
            Object bukkit_blockdata = this.blockDataGetter.invoke(event);
            Object iblockdata = this.blockDataGetState.invoke(bukkit_blockdata);
            return BlockData.fromBlockData(iblockdata);
         } catch (Throwable var4) {
            Logging.LOGGER_REFLECTION.log(Level.SEVERE, "BlockPhysicsEvent getChangedBlockData failed", var4);
            BlockPhysicsEventDataAccessor.INSTANCE = new BlockPhysicsEventDataAccessor.BlockPhysicsEventDataAccessorFallback();
            return WorldUtil.getBlockData(event.getBlock());
         }
      }
   }

   private static class BlockPhysicsEventDataAccessorDefaultModern extends BlockPhysicsEventDataAccessor {
      private final FastMethod<Object> toBukkitBlockData;
      private final FastConstructor<BlockPhysicsEvent> eventConstructor;

      public BlockPhysicsEventDataAccessorDefaultModern() throws Throwable {
         Class<?> bd = CommonUtil.getClass("org.bukkit.block.data.BlockData");
         Class<?> cbd = CommonUtil.getClass("org.bukkit.craftbukkit.block.data.CraftBlockData");
         this.toBukkitBlockData = new FastMethod(cbd.getDeclaredMethod("fromData", IBlockDataHandle.T.getType()));
         this.eventConstructor = new FastConstructor(BlockPhysicsEvent.class.getConstructor(Block.class, bd));
         this.toBukkitBlockData.forceInitialization();
         this.eventConstructor.forceInitialization();
      }

      public BlockData get(BlockPhysicsEvent event) {
         return WorldUtil.getBlockData(event.getBlock());
      }

      public BlockPhysicsEvent createEvent(Block block, BlockData blockData) {
         return (BlockPhysicsEvent)this.eventConstructor.newInstance(block, this.toBukkitBlockData.invoke((Object)null, blockData.getData()));
      }
   }

   private static final class BlockPhysicsEventDataAccessorDefaultLegacy extends BlockPhysicsEventDataAccessor {
      private final FastConstructor<BlockPhysicsEvent> eventConstructor;

      public BlockPhysicsEventDataAccessorDefaultLegacy() throws Throwable {
         this.eventConstructor = new FastConstructor(BlockPhysicsEvent.class.getDeclaredConstructor(Block.class, Integer.TYPE));
      }

      public BlockData get(BlockPhysicsEvent event) {
         return WorldUtil.getBlockData(event.getBlock());
      }

      public BlockPhysicsEvent createEvent(Block block, BlockData blockData) {
         return (BlockPhysicsEvent)this.eventConstructor.newInstance(block, blockData.getType().getId());
      }
   }

   private static final class BlockPhysicsEventDataAccessorFallback extends BlockPhysicsEventDataAccessor {
      private BlockPhysicsEventDataAccessorFallback() {
      }

      public BlockData get(BlockPhysicsEvent event) {
         return WorldUtil.getBlockData(event.getBlock());
      }

      public BlockPhysicsEvent createEvent(Block block, BlockData blockData) {
         throw new UnsupportedOperationException("Error initializing handler");
      }

      // $FF: synthetic method
      BlockPhysicsEventDataAccessorFallback(Object x0) {
         this();
      }
   }
}
