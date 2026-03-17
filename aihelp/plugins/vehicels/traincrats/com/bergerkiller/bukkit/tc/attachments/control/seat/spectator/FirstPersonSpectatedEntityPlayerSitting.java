package com.bergerkiller.bukkit.tc.attachments.control.seat.spectator;

import com.bergerkiller.bukkit.common.controller.VehicleMountController;
import com.bergerkiller.bukkit.common.math.Matrix4x4;
import com.bergerkiller.bukkit.tc.Util;
import com.bergerkiller.bukkit.tc.attachments.FakePlayerSpawner;
import com.bergerkiller.bukkit.tc.attachments.VirtualEntity;
import com.bergerkiller.bukkit.tc.attachments.api.AttachmentManager;
import com.bergerkiller.bukkit.tc.attachments.api.AttachmentViewer;
import com.bergerkiller.bukkit.tc.attachments.control.CartAttachmentSeat;
import com.bergerkiller.bukkit.tc.attachments.control.seat.FirstPersonViewMode;
import com.bergerkiller.bukkit.tc.attachments.control.seat.FirstPersonViewSpectator;
import com.bergerkiller.bukkit.tc.attachments.control.seat.SeatedEntity;
import com.bergerkiller.bukkit.tc.attachments.control.seat.SeatedEntityHead;
import com.bergerkiller.generated.net.minecraft.network.protocol.PacketHandle;
import com.bergerkiller.generated.net.minecraft.network.protocol.game.PacketPlayOutMountHandle;
import com.bergerkiller.generated.net.minecraft.network.protocol.game.PacketPlayOutUpdateAttributesHandle;
import com.bergerkiller.generated.net.minecraft.world.entity.EntityHandle;
import com.bergerkiller.generated.net.minecraft.world.entity.EntityLivingHandle;
import com.bergerkiller.generated.net.minecraft.world.entity.decoration.EntityArmorStandHandle;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

class FirstPersonSpectatedEntityPlayerSitting extends FirstPersonSpectatedEntity {
   private static final VirtualEntity[] NO_FAKE_MOUNTS = new VirtualEntity[0];
   private VirtualEntity[] fakeMounts;
   private PitchSwappedEntity<FirstPersonSpectatedEntityPlayerSitting.FakeVirtualPlayer> fakePlayer;
   private FirstPersonSpectatedEntityPlayerSitting.BlindRespawn blindRespawn;
   private final ItemStack skullItem;

   public FirstPersonSpectatedEntityPlayerSitting(CartAttachmentSeat seat, FirstPersonViewSpectator view, AttachmentViewer player) {
      super(seat, view, player);
      this.fakeMounts = NO_FAKE_MOUNTS;
      this.blindRespawn = null;
      if (view.getLiveMode() == FirstPersonViewMode.HEAD) {
         this.skullItem = SeatedEntityHead.createSkullItem(player.getPlayer());
      } else {
         this.skullItem = null;
      }

   }

   public void start(Matrix4x4 eyeTransform) {
      this.fakePlayer = PitchSwappedEntity.create(this.player, new FirstPersonSpectatedEntityPlayerSitting.FakeVirtualPlayer(this.seat.getManager(), FakePlayerSpawner.NO_NAMETAG), new FirstPersonSpectatedEntityPlayerSitting.FakeVirtualPlayer(this.seat.getManager(), FakePlayerSpawner.NO_NAMETAG_SECONDARY), new FirstPersonSpectatedEntityPlayerSitting.FakeVirtualPlayer(this.seat.getManager(), FakePlayerSpawner.NO_NAMETAG_TERTIARY));
      this.fakePlayer.beforeSwap((swapped) -> {
         if (this.blindRespawn == null) {
            if (this.view.getLiveMode() == FirstPersonViewMode.HEAD) {
               this.player.sendSilent((PacketHandle)Util.createPlayerEquipmentPacket(((FirstPersonSpectatedEntityPlayerSitting.FakeVirtualPlayer)this.fakePlayer.entity).getEntityId(), EquipmentSlot.HEAD, (ItemStack)null));
               this.player.sendSilent((PacketHandle)Util.createPlayerEquipmentPacket(swapped.getEntityId(), EquipmentSlot.HEAD, this.skullItem));
            } else {
               this.fakePlayer.swapVisibility(swapped);
            }
         }

      });
      this.fakePlayer.spawn(eyeTransform, this.seat.calcMotion());
      if (this.seat.firstPerson.getEyePosition().isDefault() && this.seat.seated.getDisplayMode() != SeatedEntity.DisplayMode.HEAD && this.seat.seated.getDisplayMode() != SeatedEntity.DisplayMode.INVISIBLE && PacketPlayOutMountHandle.T.isAvailable()) {
         this.mountInVehicle();
      } else {
         this.prepareFakeMounts(eyeTransform);
      }

      this.blindRespawn = new FirstPersonSpectatedEntityPlayerSitting.BlindRespawn();
      this.blindRespawn.spawn(eyeTransform);
   }

   private void mountInVehicle() {
      VehicleMountController vmc = this.player.getVehicleMountController();
      int vehicleId = this.view.prepareVehicleEntityId();
      ((FirstPersonSpectatedEntityPlayerSitting.FakeVirtualPlayer)this.fakePlayer.entity).mount(vmc, vehicleId);
      ((FirstPersonSpectatedEntityPlayerSitting.FakeVirtualPlayer)this.fakePlayer.entityAlt).mount(vmc, vehicleId);
      ((FirstPersonSpectatedEntityPlayerSitting.FakeVirtualPlayer)this.fakePlayer.entityAltFlip).mount(vmc, vehicleId);
   }

   private void prepareFakeMounts(Matrix4x4 baseTransform) {
      VehicleMountController vmc = this.player.getVehicleMountController();
      if (PacketPlayOutMountHandle.T.isAvailable()) {
         VirtualEntity fakeMount = this.createFakeMount(baseTransform);
         ((FirstPersonSpectatedEntityPlayerSitting.FakeVirtualPlayer)this.fakePlayer.entity).mount(vmc, fakeMount.getEntityId());
         ((FirstPersonSpectatedEntityPlayerSitting.FakeVirtualPlayer)this.fakePlayer.entityAlt).mount(vmc, fakeMount.getEntityId());
         ((FirstPersonSpectatedEntityPlayerSitting.FakeVirtualPlayer)this.fakePlayer.entityAltFlip).mount(vmc, fakeMount.getEntityId());
         this.fakeMounts = new VirtualEntity[]{fakeMount};
      } else {
         VirtualEntity[] fakeMounts = new VirtualEntity[]{this.createFakeMount(baseTransform), this.createFakeMount(baseTransform), this.createFakeMount(baseTransform)};
         ((FirstPersonSpectatedEntityPlayerSitting.FakeVirtualPlayer)this.fakePlayer.entity).mount(vmc, fakeMounts[0].getEntityId());
         ((FirstPersonSpectatedEntityPlayerSitting.FakeVirtualPlayer)this.fakePlayer.entityAlt).mount(vmc, fakeMounts[1].getEntityId());
         ((FirstPersonSpectatedEntityPlayerSitting.FakeVirtualPlayer)this.fakePlayer.entityAltFlip).mount(vmc, fakeMounts[2].getEntityId());
         this.fakeMounts = fakeMounts;
      }

   }

   private VirtualEntity createFakeMount(Matrix4x4 baseTransform) {
      VirtualEntity fakeMount = new VirtualEntity(this.seat.getManager());
      fakeMount.setEntityType(EntityType.ARMOR_STAND);
      fakeMount.setSyncMode(VirtualEntity.SyncMode.SEAT);
      fakeMount.setUseMinecartInterpolation(this.seat.isMinecartInterpolation());
      fakeMount.setByViewerPositionAdjustment((viewer, pos) -> {
         pos.setY(pos.getY() - viewer.getArmorStandButtOffset() - 1.0D);
      });
      fakeMount.updatePosition(baseTransform);
      fakeMount.getMetaData().set(EntityHandle.DATA_FLAGS, (byte)32);
      fakeMount.getMetaData().set(EntityHandle.DATA_NO_GRAVITY, true);
      fakeMount.getMetaData().set(EntityLivingHandle.DATA_HEALTH, 10.0F);
      fakeMount.getMetaData().set(EntityArmorStandHandle.DATA_ARMORSTAND_FLAGS, (byte)25);
      fakeMount.syncPosition(true);
      fakeMount.spawn(this.player, this.seat.calcMotion());
      this.player.send((PacketHandle)PacketPlayOutUpdateAttributesHandle.createZeroMaxHealth(fakeMount.getEntityId()));
      return fakeMount;
   }

   public void stop() {
      if (this.blindRespawn != null) {
         this.blindRespawn.despawn();
         this.blindRespawn = null;
      }

      VehicleMountController vmc = this.player.getVehicleMountController();
      ((FirstPersonSpectatedEntityPlayerSitting.FakeVirtualPlayer)this.fakePlayer.entity).unmount(vmc);
      ((FirstPersonSpectatedEntityPlayerSitting.FakeVirtualPlayer)this.fakePlayer.entityAlt).unmount(vmc);
      ((FirstPersonSpectatedEntityPlayerSitting.FakeVirtualPlayer)this.fakePlayer.entityAltFlip).unmount(vmc);
      VirtualEntity[] var2 = this.fakeMounts;
      int var3 = var2.length;

      for(int var4 = 0; var4 < var3; ++var4) {
         VirtualEntity fakeMount = var2[var4];
         fakeMount.destroy(this.player);
      }

      this.fakePlayer.destroy();
   }

   public void updatePosition(Matrix4x4 eyeTransform) {
      if (this.blindRespawn != null) {
         if (System.currentTimeMillis() > this.blindRespawn.timeout) {
            this.fakePlayer.spectateFrom(this.blindRespawn.spectated.getEntityId());
            if (this.view.getLiveMode() == FirstPersonViewMode.HEAD) {
               this.player.sendSilent((PacketHandle)Util.createPlayerEquipmentPacket(((FirstPersonSpectatedEntityPlayerSitting.FakeVirtualPlayer)this.fakePlayer.entity).getEntityId(), EquipmentSlot.HEAD, this.skullItem));
            } else {
               ((FirstPersonSpectatedEntityPlayerSitting.FakeVirtualPlayer)this.fakePlayer.entity).getMetaData().setFlag(EntityHandle.DATA_FLAGS, 32, false);
            }

            this.blindRespawn.despawn();
            this.blindRespawn = null;
         } else {
            this.blindRespawn.updatePosition(eyeTransform);
         }
      }

      this.fakePlayer.updatePosition(eyeTransform);
      VirtualEntity[] var2 = this.fakeMounts;
      int var3 = var2.length;

      for(int var4 = 0; var4 < var3; ++var4) {
         VirtualEntity fakeMount = var2[var4];
         fakeMount.updatePosition(eyeTransform);
      }

   }

   public void syncPosition(boolean absolute) {
      VirtualEntity[] var2 = this.fakeMounts;
      int var3 = var2.length;

      for(int var4 = 0; var4 < var3; ++var4) {
         VirtualEntity fakeMount = var2[var4];
         fakeMount.syncPosition(absolute);
      }

      this.fakePlayer.syncPosition(absolute);
      if (this.blindRespawn != null) {
         this.blindRespawn.syncPosition(absolute);
      }

   }

   public VirtualEntity getCurrentEntity() {
      return this.fakePlayer.entity;
   }

   private class BlindRespawn {
      public final VirtualEntity spectated;
      public final long timeout;

      public BlindRespawn() {
         this.spectated = new VirtualEntity(FirstPersonSpectatedEntityPlayerSitting.this.seat.getManager());
         this.spectated.setEntityType(EntityType.VILLAGER);
         this.spectated.setSyncMode(VirtualEntity.SyncMode.NORMAL);
         this.spectated.setUseMinecartInterpolation(FirstPersonSpectatedEntityPlayerSitting.this.seat.isMinecartInterpolation());
         this.spectated.setRelativeOffset(0.0D, -1.62D, 0.0D);
         this.spectated.getMetaData().set(EntityHandle.DATA_FLAGS, (byte)32);
         this.spectated.getMetaData().set(EntityHandle.DATA_NO_GRAVITY, true);
         this.timeout = System.currentTimeMillis() + 300L;
      }

      public void spawn(Matrix4x4 eyeTransform) {
         this.spectated.updatePosition(eyeTransform);
         this.spectated.syncPosition(true);
         this.spectated.spawn(FirstPersonSpectatedEntityPlayerSitting.this.player, FirstPersonSpectatedEntityPlayerSitting.this.seat.calcMotion());
         this.spectated.forceSyncRotation();
         FirstPersonSpectatedEntityPlayerSitting.this.player.getVehicleMountController().startSpectating(this.spectated.getEntityId());
      }

      public void despawn() {
         FirstPersonSpectatedEntityPlayerSitting.this.player.getVehicleMountController().stopSpectating(this.spectated.getEntityId());
         this.spectated.destroy(FirstPersonSpectatedEntityPlayerSitting.this.player);
      }

      public void updatePosition(Matrix4x4 eyeTransform) {
         this.spectated.updatePosition(eyeTransform);
      }

      public void syncPosition(boolean absolute) {
         this.spectated.syncPosition(absolute);
      }
   }

   private static class FakeVirtualPlayer extends VirtualEntity {
      public final FakePlayerSpawner fakePlayer;
      public int mountedVehicleId = -1;

      public FakeVirtualPlayer(AttachmentManager manager, FakePlayerSpawner fakeplayer) {
         super(manager);
         this.fakePlayer = fakeplayer;
         this.setEntityType(EntityType.PLAYER);
         this.setSyncMode(VirtualEntity.SyncMode.NORMAL);
         this.mountedVehicleId = -1;
      }

      public void mount(VehicleMountController vmc, int mountedVehicleId) {
         this.mountedVehicleId = mountedVehicleId;
         vmc.mount(mountedVehicleId, this.getEntityId());
      }

      public void unmount(VehicleMountController vmc) {
         if (this.mountedVehicleId != -1) {
            vmc.unmount(this.mountedVehicleId, this.getEntityId());
            this.mountedVehicleId = -1;
         }

      }

      protected void sendSpawnPackets(AttachmentViewer viewer, Vector motion) {
         FakePlayerSpawner.FakePlayerPosition orientation = FakePlayerSpawner.FakePlayerPosition.create(this.getPosX(), this.getPosY(), this.getPosZ(), (float)this.getYawPitchRoll().getY(), this.getLivePitch(), (float)this.getYawPitchRoll().getY());
         this.addViewerWithoutSpawning(viewer);
         this.fakePlayer.spawnPlayer(viewer, viewer.getPlayer(), this.getEntityId(), orientation, (meta) -> {
            meta.setFlag(EntityHandle.DATA_FLAGS, 32, true);
            meta.set(EntityHandle.DATA_NO_GRAVITY, true);
            this.metaData = meta;
         });
      }
   }
}
