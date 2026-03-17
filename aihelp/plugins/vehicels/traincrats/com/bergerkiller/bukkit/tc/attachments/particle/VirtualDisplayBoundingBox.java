package com.bergerkiller.bukkit.tc.attachments.particle;

import com.bergerkiller.bukkit.common.math.OrientedBoundingBox;
import com.bergerkiller.bukkit.common.math.Quaternion;
import com.bergerkiller.bukkit.common.utils.EntityUtil;
import com.bergerkiller.bukkit.common.utils.MaterialUtil;
import com.bergerkiller.bukkit.common.utils.MathUtil;
import com.bergerkiller.bukkit.common.wrappers.BlockData;
import com.bergerkiller.bukkit.common.wrappers.DataWatcher;
import com.bergerkiller.bukkit.common.wrappers.DataWatcher.Prototype;
import com.bergerkiller.bukkit.tc.Util;
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
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import org.bukkit.ChatColor;
import org.bukkit.entity.EntityType;
import org.bukkit.util.Vector;

public class VirtualDisplayBoundingBox extends VirtualBoundingBox {
   private final int mountEntityId = EntityUtil.getUniqueEntityId();
   private final List<VirtualDisplayBoundingBox.Line> lines = Arrays.asList(VirtualDisplayBoundingBox.Line.transform((t) -> {
      t.applyPosition(0.0D, 1.0D, 1.0D).applyScaleX(1.0D);
   }), VirtualDisplayBoundingBox.Line.transform((t) -> {
      t.applyPosition(0.0D, 0.0D, 1.0D).applyScaleX(1.0D);
   }), VirtualDisplayBoundingBox.Line.transform((t) -> {
      t.applyPosition(0.0D, 1.0D, 0.0D).applyScaleX(1.0D);
   }), VirtualDisplayBoundingBox.Line.transform((t) -> {
      t.applyPosition(0.0D, 0.0D, 0.0D).applyScaleX(1.0D);
   }), VirtualDisplayBoundingBox.Line.transform((t) -> {
      t.applyPosition(1.0D, 1.0D, 0.0D).applyScaleZ(1.0D);
   }), VirtualDisplayBoundingBox.Line.transform((t) -> {
      t.applyPosition(1.0D, 0.0D, 0.0D).applyScaleZ(1.0D);
   }), VirtualDisplayBoundingBox.Line.transform((t) -> {
      t.applyPosition(0.0D, 1.0D, 0.0D).applyScaleZ(1.0D);
   }), VirtualDisplayBoundingBox.Line.transform((t) -> {
      t.applyPosition(0.0D, 0.0D, 0.0D).applyScaleZ(1.0D);
   }), VirtualDisplayBoundingBox.Line.transform((t) -> {
      t.applyPosition(1.0D, 0.0D, 1.0D).applyScaleY(1.0D);
   }), VirtualDisplayBoundingBox.Line.transform((t) -> {
      t.applyPosition(1.0D, 0.0D, 0.0D).applyScaleY(1.0D);
   }), VirtualDisplayBoundingBox.Line.transform((t) -> {
      t.applyPosition(0.0D, 0.0D, 1.0D).applyScaleY(1.0D);
   }), VirtualDisplayBoundingBox.Line.transform((t) -> {
      t.applyPosition(0.0D, 0.0D, 0.0D).applyScaleY(1.0D);
   }));
   private final int[] lineEntityIds;
   private final List<UUID> lineEntityUUIDs;
   private final Vector position = new Vector();
   private final Vector size = new Vector();
   private final Quaternion rotation = new Quaternion();

   public VirtualDisplayBoundingBox(AttachmentManager manager) {
      super(manager);
      this.lineEntityIds = this.lines.stream().mapToInt((l) -> {
         return l.entityId;
      }).toArray();
      this.lineEntityUUIDs = (List)this.lines.stream().map((l) -> {
         return l.entityUUID;
      }).collect(Collectors.toList());
   }

   public void update(OrientedBoundingBox boundingBox) {
      MathUtil.setVector(this.position, boundingBox.getPosition());
      MathUtil.setVector(this.size, boundingBox.getSize());
      this.rotation.setTo(boundingBox.getOrientation());
      double minSize = 0.02D * Util.absMinAxis(this.size);
      double lineThickness = Math.min(0.3D, minSize);
      Iterator var6 = this.lines.iterator();

      while(var6.hasNext()) {
         VirtualDisplayBoundingBox.Line line = (VirtualDisplayBoundingBox.Line)var6.next();
         VirtualDisplayBoundingBox.LineTransformer transformer = new VirtualDisplayBoundingBox.LineTransformer(line.metadata, lineThickness);
         line.transform.accept(transformer);
      }

   }

   protected void sendSpawnPackets(AttachmentViewer viewer, Vector motion) {
      Iterator var3 = this.lines.iterator();

      while(var3.hasNext()) {
         VirtualDisplayBoundingBox.Line line = (VirtualDisplayBoundingBox.Line)var3.next();
         line.spawn(viewer, this.position, motion);
      }

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
      viewer.send((PacketHandle)PacketPlayOutMountHandle.createNew(this.mountEntityId, this.lineEntityIds));
   }

   protected void sendDestroyPackets(AttachmentViewer viewer) {
      int[] ids = Arrays.copyOf(this.lineEntityIds, this.lineEntityIds.length + 1);
      ids[ids.length - 1] = this.mountEntityId;
      viewer.send((PacketHandle)PacketPlayOutEntityDestroyHandle.createNewMultiple(ids));
   }

   protected void applyGlowing(ChatColor color) {
      byte data = color != null ? 64 : 0;
      Iterator var3 = this.lines.iterator();

      while(var3.hasNext()) {
         VirtualDisplayBoundingBox.Line line = (VirtualDisplayBoundingBox.Line)var3.next();
         line.metadata.set(EntityHandle.DATA_FLAGS, Byte.valueOf((byte)data));
      }

   }

   protected void applyGlowColorForViewer(AttachmentViewer viewer, ChatColor color) {
      viewer.updateGlowColor((Iterable)this.lineEntityUUIDs, color);
   }

   public void syncPosition(boolean absolute) {
      Iterator var2 = this.lines.iterator();

      while(var2.hasNext()) {
         VirtualDisplayBoundingBox.Line line = (VirtualDisplayBoundingBox.Line)var2.next();
         this.broadcast(line.createMetaPacket(false));
      }

      this.broadcast(PacketPlayOutEntityTeleportHandle.createNew(this.mountEntityId, this.position.getX(), this.position.getY(), this.position.getZ(), 0.0F, 0.0F, false));
   }

   public boolean containsEntityId(int entityId) {
      return entityId == this.mountEntityId;
   }

   private static class Line {
      private final Consumer<VirtualDisplayBoundingBox.LineTransformer> transform;
      public final int entityId;
      public final UUID entityUUID;
      private final DataWatcher metadata;
      private static final Prototype LINE_METADATA;

      public static VirtualDisplayBoundingBox.Line transform(Consumer<VirtualDisplayBoundingBox.LineTransformer> transform) {
         return new VirtualDisplayBoundingBox.Line(transform);
      }

      private Line(Consumer<VirtualDisplayBoundingBox.LineTransformer> transform) {
         this.transform = transform;
         this.entityId = EntityUtil.getUniqueEntityId();
         this.entityUUID = UUID.randomUUID();
         this.metadata = LINE_METADATA.create();
      }

      public PacketPlayOutEntityMetadataHandle createMetaPacket(boolean includeUnchangedData) {
         return PacketPlayOutEntityMetadataHandle.createNew(this.entityId, this.metadata, includeUnchangedData);
      }

      public void spawn(AttachmentViewer viewer, Vector position, Vector motion) {
         PacketPlayOutSpawnEntityHandle spawnPacket = PacketPlayOutSpawnEntityHandle.createNew();
         spawnPacket.setEntityId(this.entityId);
         spawnPacket.setEntityUUID(this.entityUUID);
         spawnPacket.setEntityType(VirtualDisplayEntity.BLOCK_DISPLAY_ENTITY_TYPE);
         spawnPacket.setPosX(position.getX() - motion.getX());
         spawnPacket.setPosY(position.getY() - motion.getY());
         spawnPacket.setPosZ(position.getZ() - motion.getZ());
         spawnPacket.setMotX(motion.getX());
         spawnPacket.setMotY(motion.getY());
         spawnPacket.setMotZ(motion.getZ());
         spawnPacket.setYaw(0.0F);
         spawnPacket.setPitch(0.0F);
         viewer.send((PacketHandle)spawnPacket);
         viewer.send((PacketHandle)this.createMetaPacket(true));
      }

      static {
         LINE_METADATA = Prototype.build().setClientByteDefault(EntityHandle.DATA_FLAGS, 0).setClientDefault(DisplayHandle.DATA_TRANSLATION, new Vector()).setClientDefault(DisplayHandle.DATA_LEFT_ROTATION, new Quaternion()).setClientDefault(DisplayHandle.DATA_SCALE, new Vector(1, 1, 1)).setClientDefault(DisplayHandle.DATA_INTERPOLATION_DURATION, 0).set(DisplayHandle.DATA_INTERPOLATION_DURATION, 3).setClientDefault(DisplayHandle.DATA_INTERPOLATION_START_DELTA_TICKS, 0).setClientDefault(BlockDisplayHandle.DATA_BLOCK_STATE, BlockData.AIR).set(BlockDisplayHandle.DATA_BLOCK_STATE, BlockData.fromMaterial(MaterialUtil.getMaterial("BLACK_CONCRETE"))).create();
      }
   }

   private class LineTransformer {
      public final DataWatcher metadata;
      public final double lineThickness;

      public LineTransformer(DataWatcher metadata, double lineThickness) {
         this.metadata = metadata;
         this.lineThickness = lineThickness;
      }

      public VirtualDisplayBoundingBox.LineTransformer applyPosition(double tx, double ty, double tz) {
         Vector v = new Vector((-0.5D + tx) * VirtualDisplayBoundingBox.this.size.getX() - tx * this.lineThickness, (-0.5D + ty) * VirtualDisplayBoundingBox.this.size.getY() - ty * this.lineThickness, (-0.5D + tz) * VirtualDisplayBoundingBox.this.size.getZ() - tz * this.lineThickness);
         VirtualDisplayBoundingBox.this.rotation.transformPoint(v);
         this.metadata.forceSet(DisplayHandle.DATA_LEFT_ROTATION, VirtualDisplayBoundingBox.this.rotation);
         this.metadata.forceSet(DisplayHandle.DATA_TRANSLATION, v);
         this.metadata.forceSet(DisplayHandle.DATA_INTERPOLATION_START_DELTA_TICKS, 0);
         return this;
      }

      public VirtualDisplayBoundingBox.LineTransformer applyScaleX(double x) {
         this.metadata.forceSet(DisplayHandle.DATA_SCALE, new Vector(VirtualDisplayBoundingBox.this.size.getX() * x, this.lineThickness, this.lineThickness));
         return this;
      }

      public VirtualDisplayBoundingBox.LineTransformer applyScaleY(double y) {
         this.metadata.forceSet(DisplayHandle.DATA_SCALE, new Vector(this.lineThickness, VirtualDisplayBoundingBox.this.size.getY() * y, this.lineThickness));
         return this;
      }

      public VirtualDisplayBoundingBox.LineTransformer applyScaleZ(double z) {
         this.metadata.forceSet(DisplayHandle.DATA_SCALE, new Vector(this.lineThickness, this.lineThickness, VirtualDisplayBoundingBox.this.size.getZ() * z));
         return this;
      }
   }
}
