package com.bergerkiller.bukkit.tc.attachments.control.seat;

import com.bergerkiller.bukkit.tc.attachments.api.AttachmentViewer;
import com.bergerkiller.bukkit.tc.attachments.control.CartAttachmentSeat;
import com.bergerkiller.bukkit.tc.utils.PlayerVelocityController;
import com.bergerkiller.generated.net.minecraft.network.protocol.PacketHandle;
import com.bergerkiller.generated.net.minecraft.network.protocol.game.ClientboundPlayerRotationPacketHandle;
import org.bukkit.util.Vector;

public class FirstPersonViewStanding extends FirstPersonViewDefault {
   private PlayerVelocityController _velocityControl = null;

   public FirstPersonViewStanding(CartAttachmentSeat seat, AttachmentViewer player) {
      super(seat, player);
   }

   public boolean isFakeCameraUsed() {
      return false;
   }

   public void makeVisible(AttachmentViewer viewer, boolean isReload) {
      if (!isReload && this.seat.isRotationLocked()) {
         FirstPersonView.HeadRotation rot = FirstPersonView.HeadRotation.compute(this.getEyeTransform()).ensureLevel();
         viewer.send((PacketHandle)ClientboundPlayerRotationPacketHandle.createAbsolute(rot.yaw, rot.pitch));
      }

      this.updateVelocityControl();
   }

   public void makeHidden(AttachmentViewer viewer, boolean isReload) {
      if (this._velocityControl != null) {
         this._velocityControl.stop();
         this._velocityControl = null;
      }

   }

   public void onTick() {
      this.updateVelocityControl();
      super.onTick();
   }

   private void updateVelocityControl() {
      if (this._velocityControl == null) {
         this._velocityControl = new PlayerVelocityController(this.player.getPlayer());
         this._velocityControl.translateVehicleSteer(true);
      }

      Vector pos;
      if (this._eyePosition.isDefault()) {
         pos = this.seat.getTransform().toVector();
      } else {
         pos = this.getEyeTransform().toVector();
         pos.setY(pos.getY() - 1.62D);
      }

      this._velocityControl.setPosition(pos);
   }
}
