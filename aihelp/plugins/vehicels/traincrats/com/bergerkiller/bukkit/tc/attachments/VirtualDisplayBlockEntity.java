package com.bergerkiller.bukkit.tc.attachments;

import com.bergerkiller.bukkit.common.math.Quaternion;
import com.bergerkiller.bukkit.common.wrappers.BlockData;
import com.bergerkiller.bukkit.common.wrappers.DataWatcher.Prototype;
import com.bergerkiller.bukkit.tc.Util;
import com.bergerkiller.bukkit.tc.attachments.api.AttachmentManager;
import com.bergerkiller.generated.net.minecraft.world.entity.DisplayHandle;
import com.bergerkiller.generated.net.minecraft.world.entity.DisplayHandle.BlockDisplayHandle;
import org.bukkit.util.Vector;

public class VirtualDisplayBlockEntity extends VirtualDisplayEntity {
   private BlockData blockData = null;
   public static final Prototype BLOCK_DISPLAY_METADATA;

   public VirtualDisplayBlockEntity(AttachmentManager manager) {
      super(manager, BLOCK_DISPLAY_ENTITY_TYPE, BLOCK_DISPLAY_METADATA.create());
   }

   protected void onScaleUpdated() {
      super.onScaleUpdated();
      float bb = (float)(1.41421356274619D * Util.absMaxAxis(this.scale));
      this.metadata.set(DisplayHandle.DATA_WIDTH, bb);
      this.metadata.set(DisplayHandle.DATA_HEIGHT, bb);
   }

   protected Vector computeTranslation(Quaternion rotation) {
      Vector s = this.getScale();
      Vector v = new Vector(-0.5D, 0.0D, -0.5D);
      v.setX(v.getX() * s.getX());
      v.setY(v.getY() * s.getY());
      v.setZ(v.getZ() * s.getZ());
      rotation.transformPoint(v);
      return v;
   }

   public BlockData getBlockData() {
      return this.blockData;
   }

   public void setBlockData(BlockData blockData) {
      if (this.blockData != blockData) {
         this.blockData = blockData;
         this.metadata.set(BlockDisplayHandle.DATA_BLOCK_STATE, blockData);
         this.syncMeta();
      }

   }

   static {
      BLOCK_DISPLAY_METADATA = BASE_DISPLAY_METADATA.modify().set(DisplayHandle.DATA_WIDTH, 1.4142135F).set(DisplayHandle.DATA_HEIGHT, 1.4142135F).create();
   }
}
