package com.bergerkiller.bukkit.tc.attachments.control.schematic;

import com.bergerkiller.bukkit.common.math.Quaternion;
import com.bergerkiller.bukkit.common.utils.EntityUtil;
import com.bergerkiller.bukkit.common.utils.MathUtil;
import com.bergerkiller.bukkit.common.wrappers.BlockData;
import com.bergerkiller.bukkit.common.wrappers.DataWatcher;
import com.bergerkiller.bukkit.common.wrappers.DataWatcher.Prototype;
import com.bergerkiller.bukkit.tc.attachments.VirtualDisplayEntity;
import com.bergerkiller.bukkit.tc.attachments.api.AttachmentViewer;
import com.bergerkiller.generated.net.minecraft.network.protocol.PacketHandle;
import com.bergerkiller.generated.net.minecraft.network.protocol.game.PacketPlayOutEntityMetadataHandle;
import com.bergerkiller.generated.net.minecraft.network.protocol.game.PacketPlayOutSpawnEntityHandle;
import com.bergerkiller.generated.net.minecraft.world.entity.DisplayHandle;
import com.bergerkiller.generated.net.minecraft.world.entity.DisplayHandle.BlockDisplayHandle;
import java.util.Iterator;
import java.util.UUID;
import org.bukkit.util.Vector;

class SingleSchematicBlock {
   private final double x;
   private final double y;
   private final double z;
   private double sx;
   private double sy;
   private double sz;
   private final Vector translation;
   private final int entityId;
   private final UUID entityUUID;
   private final DataWatcher metadata;
   private static final Prototype BLOCK_METADATA;

   public SingleSchematicBlock(double x, double y, double z, BlockData blockData) {
      this.x = x;
      this.y = y;
      this.z = z;
      this.sx = x;
      this.sy = y;
      this.sz = z;
      this.translation = new Vector(x, y, z);
      this.entityId = EntityUtil.getUniqueEntityId();
      this.entityUUID = UUID.randomUUID();
      this.metadata = BLOCK_METADATA.create();
      this.metadata.set(BlockDisplayHandle.DATA_BLOCK_STATE, blockData);
   }

   public int getEntityId() {
      return this.entityId;
   }

   public void setScaleAndSpacing(Vector scale, Vector origin, Vector spacing) {
      this.sx = scale.getX() * (this.x + spacing.getX() * (this.x + 0.5D)) - origin.getX();
      this.sy = scale.getY() * (this.y + spacing.getY() * this.y) - origin.getY();
      this.sz = scale.getZ() * (this.z + spacing.getZ() * (this.z + 0.5D)) - origin.getZ();
      this.metadata.set(DisplayHandle.DATA_SCALE, scale);
   }

   public void setScaleZeroSpacing(Vector scale, Vector origin) {
      this.sx = scale.getX() * this.x - origin.getX();
      this.sy = scale.getY() * this.y - origin.getY();
      this.sz = scale.getZ() * this.z - origin.getZ();
      this.metadata.set(DisplayHandle.DATA_SCALE, scale);
   }

   public void setClipBox(Float bb) {
      this.metadata.set(DisplayHandle.DATA_WIDTH, bb);
      this.metadata.set(DisplayHandle.DATA_HEIGHT, bb);
   }

   public void sync(Quaternion rotation, Iterable<AttachmentViewer> viewers) {
      Vector translation = this.translation;
      MathUtil.setVector(translation, this.sx, this.sy, this.sz);
      rotation.transformPoint(translation);
      this.metadata.forceSet(DisplayHandle.DATA_TRANSLATION, translation);
      this.metadata.forceSet(DisplayHandle.DATA_LEFT_ROTATION, rotation);
      this.metadata.forceSet(DisplayHandle.DATA_INTERPOLATION_START_DELTA_TICKS, 0);
      Iterator<AttachmentViewer> iter = viewers.iterator();
      if (iter.hasNext()) {
         PacketPlayOutEntityMetadataHandle packet = PacketPlayOutEntityMetadataHandle.createNew(this.entityId, this.metadata, false);

         do {
            ((AttachmentViewer)iter.next()).send((PacketHandle)packet);
         } while(iter.hasNext());
      }

   }

   public void spawn(AttachmentViewer viewer, Vector position, Vector motion) {
      PacketPlayOutSpawnEntityHandle spawnPacket = PacketPlayOutSpawnEntityHandle.createNew();
      spawnPacket.setEntityId(this.entityId);
      spawnPacket.setEntityUUID(this.entityUUID);
      spawnPacket.setEntityType(VirtualDisplayEntity.BLOCK_DISPLAY_ENTITY_TYPE);
      spawnPacket.setPosX(position.getX());
      spawnPacket.setPosY(position.getY());
      spawnPacket.setPosZ(position.getZ());
      spawnPacket.setMotX(motion.getX());
      spawnPacket.setMotY(motion.getY());
      spawnPacket.setMotZ(motion.getZ());
      spawnPacket.setYaw(0.0F);
      spawnPacket.setPitch(0.0F);
      viewer.send((PacketHandle)spawnPacket);
      viewer.send((PacketHandle)PacketPlayOutEntityMetadataHandle.createNew(this.entityId, this.metadata, true));
   }

   static {
      BLOCK_METADATA = VirtualDisplayEntity.BASE_DISPLAY_METADATA.modify().set(DisplayHandle.DATA_WIDTH, 1.5F).set(DisplayHandle.DATA_HEIGHT, 1.5F).setClientDefault(BlockDisplayHandle.DATA_BLOCK_STATE, BlockData.AIR).create();
   }
}
