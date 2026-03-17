package com.bergerkiller.bukkit.tc.attachments.control.seat;

import com.bergerkiller.bukkit.common.math.Matrix4x4;
import com.bergerkiller.bukkit.common.wrappers.DataWatcher;
import com.bergerkiller.bukkit.tc.attachments.api.AttachmentViewer;
import com.bergerkiller.bukkit.tc.attachments.control.CartAttachmentSeat;
import com.bergerkiller.generated.net.minecraft.network.protocol.PacketHandle;
import com.bergerkiller.generated.net.minecraft.network.protocol.game.PacketPlayOutEntityMetadataHandle;
import com.bergerkiller.generated.net.minecraft.world.entity.EntityHandle;
import org.bukkit.util.Vector;

public class SeatedEntityInvisible extends SeatedEntity {
   public SeatedEntityInvisible(CartAttachmentSeat seat) {
      super(seat);
   }

   public Vector getThirdPersonCameraOffset() {
      return new Vector(0.0D, 0.0D, 0.0D);
   }

   public Vector getFirstPersonCameraOffset() {
      return new Vector(0.0D, 0.0D, 0.0D);
   }

   public void makeVisible(AttachmentViewer viewer) {
      if (!this.isPlayer() && !this.isDummyPlayerDisplayed()) {
         if (!this.isEmpty()) {
            DataWatcher metaTmp = new DataWatcher();
            metaTmp.set(EntityHandle.DATA_FLAGS, (byte)32);
            viewer.send((PacketHandle)PacketPlayOutEntityMetadataHandle.createNew(this.entity.getEntityId(), metaTmp, true));
            viewer.getVehicleMountController().mount(this.spawnVehicleMount(viewer), this.entity.getEntityId());
         }
      } else if (this.entity != viewer.getPlayer() && !this.isDummyPlayerDisplayed()) {
         this.hideRealPlayer(viewer);
      }

   }

   public void makeHidden(AttachmentViewer viewer) {
      if (!this.isPlayer() && !this.isDummyPlayerDisplayed()) {
         if (!this.isEmpty()) {
            viewer.getVehicleMountController().unmount(this.parentMountId, this.entity.getEntityId());
            this.despawnVehicleMount(viewer);
            DataWatcher metaTmp = EntityHandle.fromBukkit(this.entity).getDataWatcher();
            viewer.send((PacketHandle)PacketPlayOutEntityMetadataHandle.createNew(this.entity.getEntityId(), metaTmp, true));
         }
      } else if (this.entity != viewer.getPlayer() && !this.isDummyPlayerDisplayed()) {
         this.showRealPlayer(viewer);
      }

   }

   public void updatePosition(Matrix4x4 transform) {
      this.updateVehicleMountPosition(transform);
   }

   public void syncPosition(boolean absolute) {
      this.syncVehicleMountPosition(absolute);
   }

   public void updateFocus(boolean focused) {
   }

   public boolean containsEntityId(int entityId) {
      return false;
   }
}
