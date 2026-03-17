package com.bergerkiller.bukkit.tc.attachments.control.seat.spectator;

import com.bergerkiller.bukkit.common.math.Matrix4x4;
import com.bergerkiller.bukkit.tc.Util;
import com.bergerkiller.bukkit.tc.attachments.FakePlayerSpawner;
import com.bergerkiller.bukkit.tc.attachments.VirtualEntity;
import com.bergerkiller.bukkit.tc.attachments.api.AttachmentManager;
import com.bergerkiller.bukkit.tc.attachments.api.AttachmentViewer;
import com.bergerkiller.bukkit.tc.attachments.control.CartAttachmentSeat;
import com.bergerkiller.bukkit.tc.attachments.control.seat.FirstPersonViewMode;
import com.bergerkiller.bukkit.tc.attachments.control.seat.FirstPersonViewSpectator;
import com.bergerkiller.bukkit.tc.attachments.control.seat.SeatedEntityHead;
import com.bergerkiller.generated.net.minecraft.network.protocol.PacketHandle;
import com.bergerkiller.generated.net.minecraft.world.entity.EntityHandle;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

class FirstPersonSpectatedEntityPlayerStanding extends FirstPersonSpectatedEntity {
   private PitchSwappedEntity<FirstPersonSpectatedEntityPlayerStanding.FakeVirtualPlayer> fakePlayer;
   private FirstPersonSpectatedEntityPlayerStanding.BlindRespawn blindRespawn = null;
   private final ItemStack skullItem;

   public FirstPersonSpectatedEntityPlayerStanding(CartAttachmentSeat seat, FirstPersonViewSpectator view, AttachmentViewer player) {
      super(seat, view, player);
      if (view.getLiveMode() == FirstPersonViewMode.HEAD) {
         this.skullItem = SeatedEntityHead.createSkullItem(player.getPlayer());
      } else {
         this.skullItem = null;
      }

   }

   public void start(Matrix4x4 eyeTransform) {
      this.fakePlayer = PitchSwappedEntity.create(this.player, new FirstPersonSpectatedEntityPlayerStanding.FakeVirtualPlayer(this.seat.getManager(), FakePlayerSpawner.NO_NAMETAG), new FirstPersonSpectatedEntityPlayerStanding.FakeVirtualPlayer(this.seat.getManager(), FakePlayerSpawner.NO_NAMETAG_SECONDARY), new FirstPersonSpectatedEntityPlayerStanding.FakeVirtualPlayer(this.seat.getManager(), FakePlayerSpawner.NO_NAMETAG_TERTIARY));
      this.fakePlayer.beforeSwap((swapped) -> {
         if (this.blindRespawn == null) {
            if (this.view.getLiveMode() == FirstPersonViewMode.HEAD) {
               this.player.sendSilent((PacketHandle)Util.createPlayerEquipmentPacket(((FirstPersonSpectatedEntityPlayerStanding.FakeVirtualPlayer)this.fakePlayer.entity).getEntityId(), EquipmentSlot.HEAD, (ItemStack)null));
               this.player.sendSilent((PacketHandle)Util.createPlayerEquipmentPacket(swapped.getEntityId(), EquipmentSlot.HEAD, this.skullItem));
            } else {
               this.fakePlayer.swapVisibility(swapped);
            }
         }

      });
      this.fakePlayer.spawn(eyeTransform, this.seat.calcMotion());
      this.blindRespawn = new FirstPersonSpectatedEntityPlayerStanding.BlindRespawn();
      this.blindRespawn.spawn(eyeTransform);
   }

   public void stop() {
      if (this.blindRespawn != null) {
         this.blindRespawn.despawn();
         this.blindRespawn = null;
      }

      this.fakePlayer.destroy();
   }

   public void updatePosition(Matrix4x4 eyeTransform) {
      if (this.blindRespawn != null) {
         if (System.currentTimeMillis() > this.blindRespawn.timeout) {
            this.fakePlayer.spectateFrom(this.blindRespawn.spectated.getEntityId());
            if (this.view.getLiveMode() == FirstPersonViewMode.HEAD) {
               this.player.sendSilent((PacketHandle)Util.createPlayerEquipmentPacket(((FirstPersonSpectatedEntityPlayerStanding.FakeVirtualPlayer)this.fakePlayer.entity).getEntityId(), EquipmentSlot.HEAD, this.skullItem));
            } else {
               ((FirstPersonSpectatedEntityPlayerStanding.FakeVirtualPlayer)this.fakePlayer.entity).getMetaData().setFlag(EntityHandle.DATA_FLAGS, 32, false);
            }

            this.blindRespawn.despawn();
            this.blindRespawn = null;
         } else {
            this.blindRespawn.updatePosition(eyeTransform);
         }
      }

      this.fakePlayer.updatePosition(eyeTransform);
   }

   public void syncPosition(boolean absolute) {
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
         this.spectated = new VirtualEntity(FirstPersonSpectatedEntityPlayerStanding.this.seat.getManager());
         this.spectated.setEntityType(EntityType.VILLAGER);
         this.spectated.setSyncMode(VirtualEntity.SyncMode.NORMAL);
         this.spectated.setUseMinecartInterpolation(FirstPersonSpectatedEntityPlayerStanding.this.seat.isMinecartInterpolation());
         this.spectated.setRelativeOffset(0.0D, -1.62D, 0.0D);
         this.spectated.getMetaData().set(EntityHandle.DATA_FLAGS, (byte)32);
         this.spectated.getMetaData().set(EntityHandle.DATA_NO_GRAVITY, true);
         this.timeout = System.currentTimeMillis() + 300L;
      }

      public void spawn(Matrix4x4 eyeTransform) {
         this.spectated.updatePosition(eyeTransform);
         this.spectated.syncPosition(true);
         this.spectated.spawn(FirstPersonSpectatedEntityPlayerStanding.this.player, FirstPersonSpectatedEntityPlayerStanding.this.seat.calcMotion());
         this.spectated.forceSyncRotation();
         FirstPersonSpectatedEntityPlayerStanding.this.player.getVehicleMountController().startSpectating(this.spectated.getEntityId());
      }

      public void despawn() {
         FirstPersonSpectatedEntityPlayerStanding.this.player.getVehicleMountController().stopSpectating(this.spectated.getEntityId());
         this.spectated.destroy(FirstPersonSpectatedEntityPlayerStanding.this.player);
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

      public FakeVirtualPlayer(AttachmentManager manager, FakePlayerSpawner fakeplayer) {
         super(manager);
         this.fakePlayer = fakeplayer;
         this.setEntityType(EntityType.PLAYER);
         this.setSyncMode(VirtualEntity.SyncMode.NORMAL);
         this.setRelativeOffset(0.0D, -1.62D, 0.0D);
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
