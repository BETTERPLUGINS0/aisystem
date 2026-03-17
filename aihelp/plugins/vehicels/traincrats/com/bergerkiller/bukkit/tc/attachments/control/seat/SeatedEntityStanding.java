package com.bergerkiller.bukkit.tc.attachments.control.seat;

import com.bergerkiller.bukkit.common.controller.VehicleMountController;
import com.bergerkiller.bukkit.common.math.Matrix4x4;
import com.bergerkiller.bukkit.common.utils.EntityUtil;
import com.bergerkiller.bukkit.tc.attachments.FakePlayerSpawner;
import com.bergerkiller.bukkit.tc.attachments.api.AttachmentViewer;
import com.bergerkiller.bukkit.tc.attachments.control.CartAttachmentSeat;
import com.bergerkiller.generated.net.minecraft.network.protocol.PacketHandle;
import com.bergerkiller.generated.net.minecraft.network.protocol.game.PacketPlayOutEntityDestroyHandle;
import com.bergerkiller.generated.net.minecraft.network.protocol.game.PacketPlayOutEntityTeleportHandle;
import java.util.Iterator;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

class SeatedEntityStanding extends SeatedEntityNormal {
   public SeatedEntityStanding(CartAttachmentSeat seat) {
      super(seat);
      this._fake = true;
   }

   public Vector getThirdPersonCameraOffset() {
      return new Vector(0.0D, 2.2D, 0.0D);
   }

   public Vector getFirstPersonCameraOffset() {
      return new Vector(0.0D, 1.62D, 0.0D);
   }

   public boolean isFirstPersonCameraFake() {
      return false;
   }

   protected boolean detectFake(boolean new_isUpsideDown, FirstPersonViewMode new_firstPersonMode) {
      return true;
   }

   private void makeFakePlayerVisible(AttachmentViewer viewer) {
      if (this._fakeEntityId == -1) {
         this._fakeEntityId = EntityUtil.getUniqueEntityId();
      }

      Vector fpp_pos = this.seat.getTransform().toVector();
      FakePlayerSpawner.FakePlayerPosition fpp = FakePlayerSpawner.FakePlayerPosition.create(fpp_pos.getX(), fpp_pos.getY(), fpp_pos.getZ(), this.orientation.getPassengerYaw(), this.orientation.getPassengerPitch(), this.orientation.getPassengerHeadYaw());
      if (this._upsideDown) {
         FakePlayerSpawner.UPSIDEDOWN.spawnPlayer(viewer, (Player)this.entity, this._fakeEntityId, fpp, this::applyFakePlayerMetadata);
      } else {
         FakePlayerSpawner.NO_NAMETAG.spawnPlayer(viewer, (Player)this.entity, this._fakeEntityId, fpp, this::applyFakePlayerMetadata);
      }

   }

   private void makeFakePlayerInvisible(AttachmentViewer viewer) {
      VehicleMountController vmc = viewer.getVehicleMountController();
      viewer.send((PacketHandle)PacketPlayOutEntityDestroyHandle.createNewSingle(this._fakeEntityId));
      vmc.remove(this._fakeEntityId);
   }

   public void makeVisible(AttachmentViewer viewer) {
      if (this.isDummyPlayer() && this.isEmpty()) {
         this.makeFakePlayerVisible(viewer);
      } else if (this.isPlayer()) {
         if (this.entity != viewer.getPlayer()) {
            this.hideRealPlayer(viewer);
         }

         this.makeFakePlayerVisible(viewer);
      }
   }

   public void makeHidden(AttachmentViewer viewer) {
      if (this.isDummyPlayer() && this.isEmpty()) {
         this.makeFakePlayerInvisible(viewer);
      } else if (this.isPlayer()) {
         this.makeFakePlayerInvisible(viewer);
         if (this.entity != viewer.getPlayer()) {
            this.showRealPlayer(viewer);
         }

      }
   }

   public void updatePosition(Matrix4x4 transform) {
      super.updatePosition(transform);
      Vector pos = transform.toVector();
      if (this.isUpsideDown()) {
         pos.setY(pos.getY() - 1.95D);
      }

      Iterator var3 = this.seat.getAttachmentViewers().iterator();

      while(true) {
         AttachmentViewer viewer;
         do {
            if (!var3.hasNext()) {
               return;
            }

            viewer = (AttachmentViewer)var3.next();
         } while(viewer.getPlayer() == this.entity && !this.seat.firstPerson.getLiveMode().hasFakePlayer());

         PacketPlayOutEntityTeleportHandle p = PacketPlayOutEntityTeleportHandle.createNew(this._fakeEntityId, pos.getX(), pos.getY(), pos.getZ(), this.orientation.getPassengerYaw(), this.orientation.getPassengerPitch(), false);
         viewer.send((PacketHandle)p);
      }
   }

   public void syncPosition(boolean absolute) {
   }

   public void updateFocus(boolean focused) {
   }
}
