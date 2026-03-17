package com.bergerkiller.bukkit.tc.attachments.particle;

import com.bergerkiller.bukkit.common.math.Matrix4x4;
import com.bergerkiller.bukkit.common.math.Quaternion;
import com.bergerkiller.bukkit.common.utils.EntityUtil;
import com.bergerkiller.bukkit.common.utils.MaterialUtil;
import com.bergerkiller.bukkit.common.wrappers.BlockData;
import com.bergerkiller.bukkit.common.wrappers.DataWatcher;
import com.bergerkiller.bukkit.common.wrappers.DataWatcher.Prototype;
import com.bergerkiller.bukkit.tc.attachments.VirtualDisplayEntity;
import com.bergerkiller.bukkit.tc.attachments.api.AttachmentManager;
import com.bergerkiller.bukkit.tc.attachments.api.AttachmentViewer;
import com.bergerkiller.generated.net.minecraft.network.protocol.PacketHandle;
import com.bergerkiller.generated.net.minecraft.network.protocol.game.PacketPlayOutEntityDestroyHandle;
import com.bergerkiller.generated.net.minecraft.network.protocol.game.PacketPlayOutEntityMetadataHandle;
import com.bergerkiller.generated.net.minecraft.network.protocol.game.PacketPlayOutEntityTeleportHandle;
import com.bergerkiller.generated.net.minecraft.network.protocol.game.PacketPlayOutMountHandle;
import com.bergerkiller.generated.net.minecraft.network.protocol.game.PacketPlayOutSpawnEntityHandle;
import com.bergerkiller.generated.net.minecraft.network.protocol.game.PacketPlayOutSpawnEntityLivingHandle;
import com.bergerkiller.generated.net.minecraft.world.entity.DisplayHandle;
import com.bergerkiller.generated.net.minecraft.world.entity.EntityHandle;
import com.bergerkiller.generated.net.minecraft.world.entity.DisplayHandle.BlockDisplayHandle;
import java.util.UUID;
import org.bukkit.ChatColor;
import org.bukkit.entity.EntityType;
import org.bukkit.util.Vector;

public class VirtualDisplayTrainCoupler extends VirtualTrainCoupler {
   private static final double COUPLER_DIAMETER = 0.2D;
   private Vector position;
   private final int mountEntityId = EntityUtil.getUniqueEntityId();
   private final int entityId = EntityUtil.getUniqueEntityId();
   private final UUID entityUUID = UUID.randomUUID();
   private final DataWatcher metadata;
   private static final Prototype LINE_METADATA;

   public VirtualDisplayTrainCoupler(AttachmentManager manager) {
      super(manager);
      this.metadata = LINE_METADATA.create();
   }

   public void update(Matrix4x4 transform, double length) {
      this.position = transform.toVector();
      Vector v = new Vector(-0.1D, 0.0D, 0.0D);
      transform.getRotation().transformPoint(v);
      this.metadata.forceSet(DisplayHandle.DATA_LEFT_ROTATION, transform.getRotation());
      this.metadata.forceSet(DisplayHandle.DATA_TRANSLATION, v);
      this.metadata.forceSet(DisplayHandle.DATA_SCALE, new Vector(0.2D, 0.2D, length));
      this.metadata.forceSet(DisplayHandle.DATA_INTERPOLATION_START_DELTA_TICKS, 0);
   }

   public void updatePosition(Matrix4x4 transform) {
      throw new UnsupportedOperationException("Must specify a transform with length");
   }

   protected void applyGlowing(ChatColor color) {
      byte data = color != null ? 64 : 0;
      this.metadata.set(EntityHandle.DATA_FLAGS, Byte.valueOf((byte)data));
   }

   protected void applyGlowColorForViewer(AttachmentViewer viewer, ChatColor color) {
      viewer.updateGlowColor(this.entityUUID, color);
   }

   protected void sendSpawnPackets(AttachmentViewer viewer, Vector motion) {
      PacketPlayOutSpawnEntityHandle spawnPacket = PacketPlayOutSpawnEntityHandle.createNew();
      spawnPacket.setEntityId(this.entityId);
      spawnPacket.setEntityUUID(this.entityUUID);
      spawnPacket.setEntityType(VirtualDisplayEntity.BLOCK_DISPLAY_ENTITY_TYPE);
      spawnPacket.setPosX(this.position.getX() - motion.getX());
      spawnPacket.setPosY(this.position.getY() - motion.getY());
      spawnPacket.setPosZ(this.position.getZ() - motion.getZ());
      spawnPacket.setMotX(motion.getX());
      spawnPacket.setMotY(motion.getY());
      spawnPacket.setMotZ(motion.getZ());
      spawnPacket.setYaw(0.0F);
      spawnPacket.setPitch(0.0F);
      viewer.send((PacketHandle)spawnPacket);
      viewer.send((PacketHandle)this.createMetaPacket(true));
      PacketPlayOutSpawnEntityLivingHandle spawnPacket = PacketPlayOutSpawnEntityLivingHandle.createNew();
      spawnPacket.setEntityId(this.mountEntityId);
      spawnPacket.setEntityUUID(UUID.randomUUID());
      spawnPacket.setEntityType(EntityType.ARMOR_STAND);
      spawnPacket.setPosX(this.position.getX() - motion.getX());
      spawnPacket.setPosY(this.position.getY() - motion.getY());
      spawnPacket.setPosZ(this.position.getZ() - motion.getZ());
      spawnPacket.setMotX(motion.getX());
      spawnPacket.setMotY(motion.getY());
      spawnPacket.setMotZ(motion.getZ());
      spawnPacket.setYaw(0.0F);
      spawnPacket.setPitch(0.0F);
      spawnPacket.setHeadYaw(0.0F);
      viewer.sendEntityLivingSpawnPacket(spawnPacket, VirtualDisplayEntity.ARMORSTAND_MOUNT_METADATA);
      viewer.send((PacketHandle)PacketPlayOutMountHandle.createNew(this.mountEntityId, new int[]{this.entityId}));
   }

   private PacketPlayOutEntityMetadataHandle createMetaPacket(boolean includeUnchangedData) {
      return PacketPlayOutEntityMetadataHandle.createNew(this.entityId, this.metadata, includeUnchangedData);
   }

   protected void sendDestroyPackets(AttachmentViewer viewer) {
      viewer.send((PacketHandle)PacketPlayOutEntityDestroyHandle.createNewMultiple(new int[]{this.mountEntityId, this.entityId}));
   }

   public void syncPosition(boolean absolute) {
      this.broadcast(this.createMetaPacket(false));
      this.broadcast(PacketPlayOutEntityTeleportHandle.createNew(this.mountEntityId, this.position.getX(), this.position.getY(), this.position.getZ(), 0.0F, 0.0F, false));
   }

   public boolean containsEntityId(int entityId) {
      return entityId == this.mountEntityId;
   }

   static {
      LINE_METADATA = Prototype.build().setClientByteDefault(EntityHandle.DATA_FLAGS, 0).setClientDefault(DisplayHandle.DATA_TRANSLATION, new Vector()).setClientDefault(DisplayHandle.DATA_LEFT_ROTATION, new Quaternion()).setClientDefault(DisplayHandle.DATA_SCALE, new Vector(1, 1, 1)).setClientDefault(DisplayHandle.DATA_INTERPOLATION_DURATION, 0).set(DisplayHandle.DATA_INTERPOLATION_DURATION, 3).setClientDefault(DisplayHandle.DATA_INTERPOLATION_START_DELTA_TICKS, 0).setClientDefault(BlockDisplayHandle.DATA_BLOCK_STATE, BlockData.AIR).set(BlockDisplayHandle.DATA_BLOCK_STATE, BlockData.fromMaterial(MaterialUtil.getMaterial("LIGHT_GRAY_CONCRETE"))).create();
   }
}
