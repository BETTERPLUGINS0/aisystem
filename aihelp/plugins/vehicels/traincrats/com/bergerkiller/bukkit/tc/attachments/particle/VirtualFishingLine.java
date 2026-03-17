package com.bergerkiller.bukkit.tc.attachments.particle;

import com.bergerkiller.bukkit.common.utils.DebugUtil;
import com.bergerkiller.bukkit.common.utils.EntityUtil;
import com.bergerkiller.bukkit.common.wrappers.DataWatcher;
import com.bergerkiller.bukkit.tc.attachments.FakePlayerSpawner;
import com.bergerkiller.bukkit.tc.attachments.api.AttachmentViewer;
import com.bergerkiller.generated.net.minecraft.network.protocol.PacketHandle;
import com.bergerkiller.generated.net.minecraft.network.protocol.game.PacketPlayOutEntityDestroyHandle;
import com.bergerkiller.generated.net.minecraft.network.protocol.game.PacketPlayOutEntityMetadataHandle;
import com.bergerkiller.generated.net.minecraft.network.protocol.game.PacketPlayOutEntityTeleportHandle;
import com.bergerkiller.generated.net.minecraft.network.protocol.game.PacketPlayOutSpawnEntityHandle;
import com.bergerkiller.generated.net.minecraft.network.protocol.game.PacketPlayOutSpawnEntityLivingHandle;
import com.bergerkiller.generated.net.minecraft.world.entity.EntityHandle;
import com.bergerkiller.generated.net.minecraft.world.entity.projectile.EntityFishingHookHandle;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.OptionalInt;
import java.util.UUID;
import java.util.stream.IntStream;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

public class VirtualFishingLine {
   private static final VirtualFishingLine.Offsets OFFSETS_1_8 = new VirtualFishingLine.Offsets(new Vector(0.35D, -1.17D, -0.8D), new Vector(0.35D, -1.04D, -0.8D), new Vector(0.0D, -0.49D, 0.0D));
   private static final VirtualFishingLine.Offsets OFFSETS_1_11 = new VirtualFishingLine.Offsets(new Vector(-0.35D, -1.17D, -0.8D), new Vector(-0.35D, -1.04D, -0.8D), new Vector(0.0D, -0.49D, 0.0D));
   private static final VirtualFishingLine.Offsets OFFSETS_1_20_2 = new VirtualFishingLine.Offsets(new Vector(-0.35D, -1.17D, -0.8D), new Vector(-0.35D, -0.807D, -0.8D), new Vector(0.0D, -0.49D, 0.0D));
   private final int hookedEntityId;
   private final int holderEntityId;
   private final int holderPlayerEntityId;
   private final int hookEntityId;

   public VirtualFishingLine() {
      this(false);
   }

   public VirtualFishingLine(boolean useViewerAsHolder) {
      this.hookedEntityId = EntityUtil.getUniqueEntityId();
      this.holderEntityId = useViewerAsHolder ? -1 : EntityUtil.getUniqueEntityId();
      this.holderPlayerEntityId = useViewerAsHolder ? -1 : EntityUtil.getUniqueEntityId();
      this.hookEntityId = EntityUtil.getUniqueEntityId();
   }

   private VirtualFishingLine.Offsets offsets(AttachmentViewer viewer) {
      if (viewer.evaluateGameVersion(">=", "1.20.2")) {
         return OFFSETS_1_20_2;
      } else {
         return viewer.evaluateGameVersion(">=", "1.11") ? OFFSETS_1_11 : OFFSETS_1_8;
      }
   }

   public void spawn(Player viewer, Vector positionA, Vector positionB) {
      this.spawn(AttachmentViewer.fallback(viewer), positionA, positionB);
   }

   public void spawn(AttachmentViewer viewer, Vector positionA, Vector positionB) {
      this.spawnWithoutLine(viewer, positionA, positionB);
      this.spawnLine(viewer, positionA, positionB);
   }

   public void spawnWithoutLine(AttachmentViewer viewer, Vector positionA, Vector positionB) {
      ArrayList<UUID> uuids = new ArrayList(3);
      this.spawnWithoutLineCollectUUIDs(viewer, positionA, positionB, uuids);
      viewer.sendDisableCollision((Iterable)uuids);
   }

   void spawnWithoutLineCollectUUIDs(AttachmentViewer viewer, Vector positionA, Vector positionB, List<UUID> uuids) {
      VirtualFishingLine.Offsets OFFSET = this.offsets(viewer);
      if (this.holderPlayerEntityId != -1) {
         FakePlayerSpawner.NO_NAMETAG_RANDOM.spawnPlayerSimple(viewer, viewer.getPlayer(), this.holderPlayerEntityId, (spawnPacketx) -> {
            spawnPacketx.setPosX(positionA.getX() + OFFSET.PLAYER.getX());
            spawnPacketx.setPosY(positionA.getY() + OFFSET.PLAYER.getY());
            spawnPacketx.setPosZ(positionA.getZ() + OFFSET.PLAYER.getZ());
         }, (metax) -> {
            metax.set(EntityHandle.DATA_NO_GRAVITY, true);
            metax.setFlag(EntityHandle.DATA_FLAGS, 32, true);
         });
      }

      UUID uuid;
      PacketPlayOutSpawnEntityLivingHandle spawnPacket;
      DataWatcher meta;
      if (this.holderEntityId != -1) {
         uuid = UUID.randomUUID();
         uuids.add(uuid);
         spawnPacket = PacketPlayOutSpawnEntityLivingHandle.createNew();
         spawnPacket.setEntityId(this.holderEntityId);
         spawnPacket.setEntityUUID(uuid);
         spawnPacket.setEntityType(EntityType.SILVERFISH);
         spawnPacket.setPosX(positionA.getX() + OFFSET.HOLDER.getX());
         spawnPacket.setPosY(positionA.getY() + OFFSET.HOLDER.getY());
         spawnPacket.setPosZ(positionA.getZ() + OFFSET.HOLDER.getZ());
         meta = new DataWatcher();
         meta.set(EntityHandle.DATA_NO_GRAVITY, true);
         meta.setFlag(EntityHandle.DATA_FLAGS, 32, true);
         viewer.sendEntityLivingSpawnPacket(spawnPacket, meta);
         viewer.getVehicleMountController().mount(this.holderEntityId, this.holderPlayerEntityId);
      }

      uuid = UUID.randomUUID();
      uuids.add(uuid);
      spawnPacket = PacketPlayOutSpawnEntityLivingHandle.createNew();
      spawnPacket.setEntityId(this.hookedEntityId);
      spawnPacket.setEntityUUID(uuid);
      spawnPacket.setEntityType(EntityType.SILVERFISH);
      spawnPacket.setPosX(positionB.getX() + OFFSET.HOOKED.getX());
      spawnPacket.setPosY(positionB.getY() + OFFSET.HOOKED.getY());
      spawnPacket.setPosZ(positionB.getZ() + OFFSET.HOOKED.getZ());
      meta = new DataWatcher();
      meta.set(EntityHandle.DATA_NO_GRAVITY, true);
      meta.setFlag(EntityHandle.DATA_FLAGS, 32, true);
      viewer.sendEntityLivingSpawnPacket(spawnPacket, meta);
   }

   public void update(Iterable<Player> viewers, Vector positionA, Vector positionB) {
      this.updateViewers(AttachmentViewer.fallbackIterable(viewers), positionA, positionB);
   }

   public void updateViewers(Iterable<AttachmentViewer> viewers, Vector positionA, Vector positionB) {
      Iterator var4 = viewers.iterator();

      while(var4.hasNext()) {
         AttachmentViewer viewer = (AttachmentViewer)var4.next();
         VirtualFishingLine.Offsets OFFSET = this.offsets(viewer);
         PacketPlayOutEntityTeleportHandle packet;
         if (this.holderEntityId != -1) {
            packet = PacketPlayOutEntityTeleportHandle.createNew(this.holderEntityId, positionA.getX() + OFFSET.HOLDER.getX(), positionA.getY() + OFFSET.HOLDER.getY(), positionA.getZ() + OFFSET.HOLDER.getZ(), 0.0F, 0.0F, false);
            viewer.send((PacketHandle)packet);
         }

         packet = PacketPlayOutEntityTeleportHandle.createNew(this.hookedEntityId, positionB.getX() + OFFSET.HOOKED.getX(), positionB.getY() + OFFSET.HOOKED.getY(), positionB.getZ() + OFFSET.HOOKED.getZ(), 0.0F, 0.0F, false);
         viewer.send((PacketHandle)packet);
      }

   }

   public void destroy(Player viewer) {
      this.destroy(AttachmentViewer.fallback(viewer));
   }

   public void destroy(AttachmentViewer viewer) {
      int[] entityIds = IntStream.of(new int[]{this.hookedEntityId, this.holderEntityId, this.holderPlayerEntityId, this.hookEntityId}).filter((id) -> {
         return id != -1;
      }).toArray();
      if (PacketPlayOutEntityDestroyHandle.canDestroyMultiple()) {
         viewer.send((PacketHandle)PacketPlayOutEntityDestroyHandle.createNewMultiple(entityIds));
      } else {
         int[] var3 = entityIds;
         int var4 = entityIds.length;

         for(int var5 = 0; var5 < var4; ++var5) {
            int entityId = var3[var5];
            viewer.send((PacketHandle)PacketPlayOutEntityDestroyHandle.createNewSingle(entityId));
         }
      }

   }

   public void spawnLine(AttachmentViewer viewer, Vector positionA, Vector positionB) {
      PacketPlayOutSpawnEntityHandle spawnPacket = PacketPlayOutSpawnEntityHandle.createNew();
      spawnPacket.setEntityId(this.hookEntityId);
      spawnPacket.setEntityUUID(UUID.randomUUID());
      spawnPacket.setEntityType(EntityType.FISHING_HOOK);
      spawnPacket.setPosX(positionB.getX());
      spawnPacket.setPosY(positionB.getY() - 0.25D);
      spawnPacket.setPosZ(positionB.getZ());
      spawnPacket.setExtraData(this.holderPlayerEntityId == -1 ? viewer.getEntityId() : this.holderPlayerEntityId);
      viewer.send((PacketHandle)spawnPacket);
      DataWatcher meta = new DataWatcher();
      meta.set(EntityFishingHookHandle.DATA_HOOKED_ENTITY_ID, OptionalInt.of(this.hookedEntityId));
      meta.setFlag(EntityHandle.DATA_FLAGS, 32, true);
      PacketPlayOutEntityMetadataHandle metaPacket = PacketPlayOutEntityMetadataHandle.createNew(this.hookEntityId, meta, true);
      viewer.send((PacketHandle)metaPacket);
   }

   public void destroyLine(AttachmentViewer viewer) {
      viewer.send((PacketHandle)PacketPlayOutEntityDestroyHandle.createNewSingle(this.hookEntityId));
   }

   private static class Offsets {
      public final Vector PLAYER;
      public final Vector HOLDER;
      public final Vector HOOKED;

      public Offsets(Vector OFFSET_PLAYER, Vector OFFSET_HOLDER, Vector OFFSET_HOOKED) {
         this.PLAYER = OFFSET_PLAYER;
         this.HOLDER = OFFSET_HOLDER;
         this.HOOKED = OFFSET_HOOKED;
      }

      public VirtualFishingLine.Offsets debug() {
         return new VirtualFishingLine.Offsets(new Vector(DebugUtil.getDoubleValue("ax", this.PLAYER.getX()), DebugUtil.getDoubleValue("ay", this.PLAYER.getY()), DebugUtil.getDoubleValue("az", this.PLAYER.getZ())), new Vector(DebugUtil.getDoubleValue("bx", this.HOLDER.getX()), DebugUtil.getDoubleValue("by", this.HOLDER.getY()), DebugUtil.getDoubleValue("bz", this.HOLDER.getZ())), new Vector(DebugUtil.getDoubleValue("cx", this.HOOKED.getX()), DebugUtil.getDoubleValue("cy", this.HOOKED.getY()), DebugUtil.getDoubleValue("cz", this.HOOKED.getZ())));
      }
   }
}
