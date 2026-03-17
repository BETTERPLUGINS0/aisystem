package com.bergerkiller.bukkit.tc.actions;

import com.bergerkiller.bukkit.tc.TrainCarts;
import org.bukkit.block.Block;

public class BlockAction extends Action {
   private final TrainCarts traincarts;
   private final Block block;

   public BlockAction(TrainCarts traincarts, Block block) {
      this.traincarts = traincarts;
      this.block = block;
   }

   public TrainCarts getTrainCarts() {
      return this.traincarts;
   }

   public Block getBlock() {
      return this.block;
   }
}
