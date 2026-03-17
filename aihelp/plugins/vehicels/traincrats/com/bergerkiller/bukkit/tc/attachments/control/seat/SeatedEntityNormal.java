package com.bergerkiller.bukkit.tc.attachments.control.seat;

import com.bergerkiller.bukkit.common.controller.VehicleMountController;
import com.bergerkiller.bukkit.common.math.Matrix4x4;
import com.bergerkiller.bukkit.common.math.Quaternion;
import com.bergerkiller.bukkit.common.utils.EntityUtil;
import com.bergerkiller.bukkit.common.utils.MathUtil;
import com.bergerkiller.bukkit.common.wrappers.DataWatcher;
import com.bergerkiller.bukkit.tc.TCConfig;
import com.bergerkiller.bukkit.tc.attachments.FakePlayerSpawner;
import com.bergerkiller.bukkit.tc.attachments.VirtualEntity;
import com.bergerkiller.bukkit.tc.attachments.api.AttachmentViewer;
import com.bergerkiller.bukkit.tc.attachments.control.CartAttachmentSeat;
import com.bergerkiller.generated.net.minecraft.network.protocol.PacketHandle;
import com.bergerkiller.generated.net.minecraft.network.protocol.game.PacketPlayOutEntityDestroyHandle;
import com.bergerkiller.generated.net.minecraft.network.protocol.game.PacketPlayOutEntityMetadataHandle;
import com.bergerkiller.generated.net.minecraft.world.entity.EntityHandle;
import java.util.Collection;
import java.util.Iterator;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

class SeatedEntityNormal extends SeatedEntity {
   protected boolean _upsideDown = false;
   protected int _fakeEntityId = -1;
   protected boolean _fake = false;
   private VirtualEntity _upsideDownVehicle = null;

   public SeatedEntityNormal(CartAttachmentSeat seat) {
      super(seat);
   }

   public boolean isUpsideDown() {
      return this._upsideDown;
   }

   public void setUpsideDown(boolean upsideDown) {
      this._upsideDown = upsideDown;
   }

   public boolean isFake() {
      return this._fake;
   }

   public void setFake(boolean fake) {
      this._fake = fake;
   }

   public void refreshUpsideDownMetadata(AttachmentViewer viewer, boolean upsideDown) {
      if (!this.isEmpty() && !this.isPlayer() && !this.isDummyPlayer()) {
         DataWatcher metaTmp;
         if (upsideDown) {
            metaTmp = new DataWatcher();
            metaTmp.set(EntityHandle.DATA_CUSTOM_NAME, FakePlayerSpawner.UPSIDEDOWN.getPlayerName());
            metaTmp.set(EntityHandle.DATA_CUSTOM_NAME_VISIBLE, false);
            PacketPlayOutEntityMetadataHandle metaPacket = PacketPlayOutEntityMetadataHandle.createNew(this.entity.getEntityId(), metaTmp, true);
            viewer.send((PacketHandle)metaPacket);
         } else {
            metaTmp = EntityHandle.fromBukkit(this.entity).getDataWatcher();
            viewer.send((PacketHandle)PacketPlayOutEntityMetadataHandle.createNew(this.entity.getEntityId(), metaTmp, true));
         }

      }
   }

   private void makeFakePlayerVisible(AttachmentViewer viewer) {
      if (this._fakeEntityId == -1) {
         this._fakeEntityId = EntityUtil.getUniqueEntityId();
      }

      Vector fpp_pos = this.seat.getTransform().toVector();
      FakePlayerSpawner.FakePlayerPosition fpp = FakePlayerSpawner.FakePlayerPosition.create(fpp_pos.getX(), fpp_pos.getY(), fpp_pos.getZ(), this.orientation.getPassengerYaw(), this.orientation.getPassengerPitch(), this.orientation.getPassengerHeadYaw());
      VehicleMountController vmc = viewer.getVehicleMountController();
      if (this._upsideDown) {
         if (this._upsideDownVehicle == null) {
            this._upsideDownVehicle = this.createPassengerVehicle();
            this._upsideDownVehicle.addRelativeOffset(0.0D, -0.65D, 0.0D);
            this._upsideDownVehicle.updatePosition(this.seat.getTransform(), new Vector(0.0D, (double)this.orientation.getMountYaw(), 0.0D));
            this._upsideDownVehicle.syncPosition(true);
         }

         this._upsideDownVehicle.spawn(viewer, this.seat.calcMotion());
         FakePlayerSpawner.UPSIDEDOWN.spawnPlayer(viewer, (Player)this.entity, this._fakeEntityId, fpp, this::applyFakePlayerMetadata);
         vmc.mount(this._upsideDownVehicle.getEntityId(), this._fakeEntityId);
      } else {
         FakePlayerSpawner.NO_NAMETAG.spawnPlayer(viewer, (Player)this.entity, this._fakeEntityId, fpp, this::applyFakePlayerMetadata);
         vmc.mount(this.parentMountId, this._fakeEntityId);
      }

      if (this.seat.isRotationLocked()) {
         this.orientation.sendLockedRotations(viewer, this._fakeEntityId);
      }

   }

   protected void applyFakePlayerMetadata(DataWatcher metadata) {
      metadata.setFlag(EntityHandle.DATA_FLAGS, 128, false);
      metadata.setFlag(EntityHandle.DATA_FLAGS, 64, this.isDummyPlayer() && this.seat.isFocused());
   }

   private void makeFakePlayerInvisible(AttachmentViewer viewer) {
      VehicleMountController vmc = viewer.getVehicleMountController();
      viewer.send((PacketHandle)PacketPlayOutEntityDestroyHandle.createNewSingle(this._fakeEntityId));
      vmc.remove(this._fakeEntityId);
      if (this._upsideDown && this._upsideDownVehicle != null) {
         this._upsideDownVehicle.destroy(viewer);
         vmc.remove(this._upsideDownVehicle.getEntityId());
      }

   }

   public Vector getThirdPersonCameraOffset() {
      return new Vector(0.0D, 1.6D, 0.0D);
   }

   public Vector getFirstPersonCameraOffset() {
      return new Vector(0.0D, 1.0D, 0.0D);
   }

   public void makeVisible(AttachmentViewer viewer) {
      this.spawnVehicleMount(viewer);
      if (this.isDummyPlayer() && this.isEmpty()) {
         this.makeFakePlayerVisible(viewer);
      } else if (this.entity == viewer.getPlayer()) {
         this.makeFakePlayerVisible(viewer);
      } else if (this._fake && this.isPlayer()) {
         viewer.getVehicleMountController().despawn(this.entity.getEntityId());
         this.makeFakePlayerVisible(viewer);
      } else if (!this.isEmpty()) {
         if (this._upsideDown) {
            this.refreshUpsideDownMetadata(viewer, true);
         }

         viewer.getVehicleMountController().mount(this.parentMountId, this.entity.getEntityId());
      }

   }

   public void makeHidden(AttachmentViewer viewer) {
      if (this.isDummyPlayer() && this.isEmpty()) {
         this.makeFakePlayerInvisible(viewer);
      } else if (this.entity == viewer.getPlayer()) {
         this.makeFakePlayerInvisible(viewer);
      } else if (this._fake && this.isPlayer()) {
         this.makeFakePlayerInvisible(viewer);
         this.showRealPlayer(viewer);
      } else if (!this.isEmpty()) {
         if (this._upsideDown) {
            this.refreshUpsideDownMetadata(viewer, false);
         }

         viewer.getVehicleMountController().unmount(this.parentMountId, this.entity.getEntityId());
      }

      this.despawnVehicleMount(viewer);
   }

   protected boolean detectFake(boolean new_isUpsideDown, FirstPersonViewMode new_firstPersonMode) {
      boolean noNametag = this.displayMode == SeatedEntity.DisplayMode.NO_NAMETAG;
      return this.isDummyPlayer() || this.isPlayer() && (noNametag || new_isUpsideDown || new_firstPersonMode.hasFakePlayer());
   }

   public void updateMode(boolean silent) {
      FirstPersonViewMode new_firstPersonMode = FirstPersonViewMode.DEFAULT;
      boolean new_isFake;
      boolean new_isUpsideDown;
      if (!this.isDisplayed()) {
         new_isFake = false;
         new_isUpsideDown = false;
      } else if (this.seat.getTransform() == null && this.isDummyPlayer()) {
         new_isFake = true;
         new_isUpsideDown = false;
         silent = true;
      } else {
         Quaternion rotation = this.seat.getTransform().getRotation();
         double selfPitch = rotation.getPitch();
         new_isUpsideDown = this.isUpsideDown();
         if (MathUtil.getAngleDifference(selfPitch, 180.0D) < 89.0D) {
            new_isUpsideDown = true;
         } else if (MathUtil.getAngleDifference(selfPitch, 0.0D) < 89.0D) {
            new_isUpsideDown = false;
         }

         new_firstPersonMode = this.seat.firstPerson.getMode();
         if (new_firstPersonMode == FirstPersonViewMode.DYNAMIC) {
            if (TCConfig.enableSeatThirdPersonView && this.isPlayer() && Math.abs(selfPitch) > 70.0D) {
               new_firstPersonMode = FirstPersonViewMode.THIRD_P;
            } else {
               new_firstPersonMode = FirstPersonViewMode.DEFAULT;
            }
         }

         new_isFake = this.detectFake(new_isUpsideDown, new_firstPersonMode);
      }

      if (silent) {
         this.setFake(new_isFake);
         this.setUpsideDown(new_isUpsideDown);
         this.seat.firstPerson.setLiveMode(new_firstPersonMode);
      } else {
         if (new_isFake != this.isFake() || this.isPlayer() && new_isUpsideDown != this.isUpsideDown()) {
            boolean refreshFPV = this.seat.firstPerson.doesViewModeChangeRequireReset(new_firstPersonMode);
            Entity entity = this.getEntity();
            Collection<AttachmentViewer> viewers = this.seat.getAttachmentViewersSynced();
            Iterator var8 = viewers.iterator();

            while(true) {
               AttachmentViewer viewer;
               do {
                  if (!var8.hasNext()) {
                     this.setFake(new_isFake);
                     this.setUpsideDown(new_isUpsideDown);
                     this.seat.firstPerson.setLiveMode(new_firstPersonMode);
                     var8 = viewers.iterator();

                     while(true) {
                        do {
                           if (!var8.hasNext()) {
                              return;
                           }

                           viewer = (AttachmentViewer)var8.next();
                        } while(!refreshFPV && viewer.getPlayer() == entity);

                        this.seat.makeVisibleImpl(viewer, true);
                     }
                  }

                  viewer = (AttachmentViewer)var8.next();
               } while(!refreshFPV && viewer.getPlayer() == entity);

               this.seat.makeHiddenImpl(viewer, true);
            }
         } else {
            if (new_isUpsideDown != this.isUpsideDown()) {
               this.setUpsideDown(new_isUpsideDown);
               if (!this.isEmpty()) {
                  Iterator var10 = this.seat.getAttachmentViewersSynced().iterator();

                  while(var10.hasNext()) {
                     AttachmentViewer viewer = (AttachmentViewer)var10.next();
                     this.refreshUpsideDownMetadata(viewer, new_isUpsideDown);
                  }
               }
            }

            if (new_firstPersonMode != this.seat.firstPerson.getLiveMode()) {
               Collection<AttachmentViewer> viewers = this.seat.getAttachmentViewersSynced();
               if (this.isPlayer() && viewers.contains(this.seat.firstPerson.player)) {
                  this.seat.makeHiddenImpl(this.seat.firstPerson.player, true);
                  this.seat.firstPerson.setLiveMode(new_firstPersonMode);
                  this.seat.makeVisibleImpl(this.seat.firstPerson.player, true);
               } else {
                  this.seat.firstPerson.setLiveMode(new_firstPersonMode);
               }
            }
         }

      }
   }

   public boolean containsEntityId(int entityId) {
      return entityId == this._fakeEntityId;
   }

   public void updatePosition(Matrix4x4 transform) {
      if (this.isDisplayed()) {
         int entityId = this._fake ? this._fakeEntityId : (this.entity == null ? -1 : this.entity.getEntityId());
         this.orientation.synchronizeNormal(this.seat, transform, this, entityId);
      }

      this.updateVehicleMountPosition(transform);
      if (this._upsideDownVehicle != null) {
         this._upsideDownVehicle.updatePosition(transform, new Vector(0.0D, (double)this.orientation.getMountYaw(), 0.0D));
      }

   }

   public void syncPosition(boolean absolute) {
      this.syncVehicleMountPosition(absolute);
      if (this._upsideDownVehicle != null) {
         this._upsideDownVehicle.syncPosition(absolute);
      }

   }

   public void updateFocus(boolean focused) {
      if (this._fakeEntityId != -1 && this.isDisplayed()) {
         DataWatcher metadata;
         if (this.isPlayer()) {
            metadata = EntityUtil.getDataWatcher(this.entity).clone();
         } else {
            metadata = new DataWatcher();
            metadata.set(EntityHandle.DATA_FLAGS, (byte)0);
         }

         this.applyFakePlayerMetadata(metadata);
         PacketPlayOutEntityMetadataHandle packet = PacketPlayOutEntityMetadataHandle.createNew(this._fakeEntityId, metadata, true);
         Iterator var4 = this.seat.getAttachmentViewers().iterator();

         while(var4.hasNext()) {
            AttachmentViewer viewer = (AttachmentViewer)var4.next();
            viewer.send((PacketHandle)packet);
         }
      }

   }
}
