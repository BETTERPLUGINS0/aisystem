package com.bergerkiller.bukkit.tc.attachments;

import com.bergerkiller.bukkit.common.Common;
import com.bergerkiller.bukkit.common.math.Quaternion;
import com.bergerkiller.bukkit.common.utils.LogicUtil;
import com.bergerkiller.bukkit.common.wrappers.DataWatcher;
import com.bergerkiller.bukkit.common.wrappers.ItemDisplayMode;
import com.bergerkiller.bukkit.common.wrappers.DataWatcher.Prototype;
import com.bergerkiller.bukkit.tc.Util;
import com.bergerkiller.bukkit.tc.attachments.api.AttachmentManager;
import com.bergerkiller.generated.net.minecraft.world.entity.EntityHandle;
import com.bergerkiller.generated.net.minecraft.world.entity.DisplayHandle.ItemDisplayHandle;
import com.bergerkiller.generated.net.minecraft.world.entity.decoration.EntityArmorStandHandle;
import org.bukkit.inventory.ItemStack;

public class VirtualDisplayItemEntity extends VirtualDisplayEntity {
   private static final DataWatcher MOUNT_METADATA = new DataWatcher();
   public static final Prototype ITEM_DISPLAY_METADATA;
   public static final boolean IS_YAW_FLIPPED;
   private ItemDisplayMode mode;
   private ItemStack item;
   private double clip;
   private boolean appliedClip;

   public VirtualDisplayItemEntity(AttachmentManager manager) {
      super(manager, ITEM_DISPLAY_ENTITY_TYPE, ITEM_DISPLAY_METADATA.create());
      this.mode = ItemDisplayMode.HEAD;
      this.item = null;
      this.clip = 0.0D;
      this.appliedClip = false;
   }

   public ItemStack getItem() {
      return this.item;
   }

   public ItemDisplayMode getMode() {
      return this.mode;
   }

   public void setItem(ItemDisplayMode mode, ItemStack item) {
      if (mode == null) {
         throw new IllegalArgumentException("Null dispay mode specified. Invalid transform type?");
      } else {
         if (!LogicUtil.bothNullOrEqual(item, this.item) || this.mode != mode) {
            this.item = item;
            this.mode = mode;
            this.metadata.set(ItemDisplayHandle.DATA_ITEM_STACK, item);
            this.metadata.set(ItemDisplayHandle.DATA_ITEM_DISPLAY_MODE, mode);
            this.syncMeta();
         }

      }
   }

   protected void onScaleUpdated() {
      super.onScaleUpdated();
      this.applyClip();
   }

   protected void onRotationUpdated(Quaternion rotation) {
      if (IS_YAW_FLIPPED) {
         rotation.rotateYFlip();
      }

   }

   public void setClip(double clip) {
      if (this.clip != clip) {
         this.clip = clip;
         this.applyClip();
      }

   }

   private void applyClip() {
      if (this.clip != 0.0D) {
         this.appliedClip = true;
         float f = (float)(this.clip * 1.41421356274619D * Util.absMaxAxis(this.scale));
         this.metadata.set(ItemDisplayHandle.DATA_WIDTH, f);
         this.metadata.set(ItemDisplayHandle.DATA_HEIGHT, f);
      } else if (this.appliedClip) {
         this.metadata.set(ItemDisplayHandle.DATA_WIDTH, 0.0F);
         this.metadata.set(ItemDisplayHandle.DATA_HEIGHT, 0.0F);
      }

   }

   static {
      MOUNT_METADATA.set(EntityHandle.DATA_NO_GRAVITY, true);
      MOUNT_METADATA.set(EntityHandle.DATA_FLAGS, -96);
      MOUNT_METADATA.set(EntityArmorStandHandle.DATA_ARMORSTAND_FLAGS, (byte)25);
      ITEM_DISPLAY_METADATA = BASE_DISPLAY_METADATA.modify().setClientDefault(ItemDisplayHandle.DATA_ITEM_DISPLAY_MODE, ItemDisplayMode.NONE).set(ItemDisplayHandle.DATA_ITEM_DISPLAY_MODE, ItemDisplayMode.HEAD).setClientDefault(ItemDisplayHandle.DATA_ITEM_STACK, (Object)null).create();
      IS_YAW_FLIPPED = Common.evaluateMCVersion("<=", "1.19.4");
   }
}
