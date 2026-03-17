package com.bergerkiller.bukkit.tc.attachments.control.schematic;

import com.bergerkiller.bukkit.common.bases.IntVector3;
import com.bergerkiller.bukkit.common.math.Matrix4x4;
import com.bergerkiller.bukkit.common.math.OrientedBoundingBox;
import com.bergerkiller.bukkit.common.math.Quaternion;
import com.bergerkiller.bukkit.common.math.Vector3;
import com.bergerkiller.bukkit.common.utils.EntityUtil;
import com.bergerkiller.bukkit.common.utils.MaterialUtil;
import com.bergerkiller.bukkit.common.utils.MathUtil;
import com.bergerkiller.bukkit.common.wrappers.BlockData;
import com.bergerkiller.bukkit.tc.Util;
import com.bergerkiller.bukkit.tc.attachments.VirtualDisplayEntity;
import com.bergerkiller.bukkit.tc.attachments.VirtualSpawnableObject;
import com.bergerkiller.bukkit.tc.attachments.api.AttachmentManager;
import com.bergerkiller.bukkit.tc.attachments.api.AttachmentViewer;
import com.bergerkiller.generated.net.minecraft.network.protocol.PacketHandle;
import com.bergerkiller.generated.net.minecraft.network.protocol.game.PacketPlayOutEntityDestroyHandle;
import com.bergerkiller.generated.net.minecraft.network.protocol.game.PacketPlayOutEntityTeleportHandle;
import com.bergerkiller.generated.net.minecraft.network.protocol.game.PacketPlayOutMountHandle;
import com.bergerkiller.generated.net.minecraft.network.protocol.game.PacketPlayOutSpawnEntityLivingHandle;
import com.bergerkiller.generated.net.minecraft.network.protocol.game.PacketPlayOutEntityHandle.PacketPlayOutRelEntityMoveHandle;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;
import org.bukkit.entity.EntityType;
import org.bukkit.util.Vector;

public class MovingSchematic extends VirtualSpawnableObject {
   private final int mountEntityId = EntityUtil.getUniqueEntityId();
   private final List<SingleSchematicBlock> blocks = new ArrayList();
   private final Vector livePos = new Vector();
   private final Vector syncPos = new Vector();
   private final Quaternion liveRot = new Quaternion();
   private IntVector3 blockBounds = new IntVector3(1, 1, 1);
   private final Vector scale = new Vector(1.0D, 1.0D, 1.0D);
   private final Vector origin = new Vector(0.0D, 0.0D, 0.0D);
   private final Vector spacing = new Vector(0.0D, 0.0D, 0.0D);
   private boolean hasSpacing = false;
   private boolean hasOrigin = false;
   private boolean hasClipping = true;
   private float bbSize = 1.5F;
   private int[] cachedBlockEntityIds = null;
   private boolean hasKnownPosition = false;

   public MovingSchematic(AttachmentManager manager) {
      super(manager);
   }

   public void setBlockBounds(IntVector3 blockBounds) {
      if (!this.blockBounds.equals(blockBounds)) {
         this.blockBounds = blockBounds;
         this.rescaleAllBlocks();
      }

   }

   public void addBlock(double x, double y, double z, BlockData blockData) {
      if (!MaterialUtil.ISAIR.get(blockData)) {
         SingleSchematicBlock block = new SingleSchematicBlock(x, y, z, blockData);
         this.blocks.add(block);
         this.cachedBlockEntityIds = null;
         if (this.hasKnownPosition) {
            if (this.hasSpacing) {
               block.setScaleAndSpacing(this.scale, this.origin, this.spacing);
            } else {
               block.setScaleZeroSpacing(this.scale, this.origin);
            }

            block.setClipBox(this.bbSize);
            block.sync(this.liveRot, Collections.emptyList());
            this.forAllViewers((v) -> {
               block.spawn(v, this.syncPos, new Vector(0.0D, 0.0D, 0.0D));
            });
         }
      }

   }

   public OrientedBoundingBox createBBOX() {
      Vector bbSize = new Vector(((double)this.blockBounds.x + this.spacing.getX() * (double)(this.blockBounds.x - 1)) * this.scale.getX(), ((double)this.blockBounds.y + this.spacing.getY() * (double)(this.blockBounds.y - 1)) * this.scale.getY(), ((double)this.blockBounds.z + this.spacing.getZ() * (double)(this.blockBounds.z - 1)) * this.scale.getZ());
      Vector position = this.livePos.clone().add(this.liveRot.upVector().multiply(0.5D * bbSize.getY()));
      if (this.hasOrigin) {
         Vector offset = this.origin.clone();
         this.liveRot.transformPoint(offset);
         position.subtract(offset);
      }

      return new OrientedBoundingBox(position, bbSize, this.liveRot);
   }

   public Matrix4x4 createOriginPointTransform() {
      Matrix4x4 transform = Matrix4x4.translation(this.livePos);
      transform.rotate(this.liveRot);
      transform.translate(this.origin);
      return transform;
   }

   public void resendMounts() {
      if (this.hasKnownPosition) {
         this.broadcast(PacketPlayOutMountHandle.createNew(this.mountEntityId, this.getBlockEntityIds()));
      }

   }

   private int[] getBlockEntityIds() {
      int[] ids = this.cachedBlockEntityIds;
      if (ids == null) {
         this.cachedBlockEntityIds = ids = this.blocks.stream().mapToInt(SingleSchematicBlock::getEntityId).toArray();
      }

      return ids;
   }

   public boolean containsEntityId(int entityId) {
      return entityId == this.mountEntityId;
   }

   public void setHasClipping(boolean clipping) {
      if (this.hasClipping != clipping) {
         this.hasClipping = clipping;
         this.rescaleAllBlocks();
      }

   }

   public void setScale(Vector3 scale) {
      if (this.scale.getX() != scale.x || this.scale.getY() != scale.y || this.scale.getZ() != scale.z) {
         MathUtil.setVector(this.scale, scale.x, scale.y, scale.z);
         this.rescaleAllBlocks();
      }

   }

   public void setScale(Vector scale) {
      if (this.scale.getX() != scale.getX() || this.scale.getY() != scale.getY() || this.scale.getZ() != scale.getZ()) {
         MathUtil.setVector(this.scale, scale);
         this.rescaleAllBlocks();
      }

   }

   public void setSpacing(Vector spacing) {
      if (this.spacing.getX() != spacing.getX() || this.spacing.getY() != spacing.getY() || this.spacing.getZ() != spacing.getZ()) {
         MathUtil.setVector(this.spacing, spacing);
         this.hasSpacing = spacing.getX() != 0.0D || spacing.getY() != 0.0D || spacing.getZ() != 0.0D;
         this.rescaleAllBlocks();
      }

   }

   public void setOrigin(Vector origin) {
      if (this.origin.getX() != origin.getX() || this.origin.getY() != origin.getY() || this.origin.getZ() != origin.getZ()) {
         MathUtil.setVector(this.origin, origin);
         this.hasOrigin = origin.getX() != 0.0D || origin.getY() != 0.0D || origin.getZ() != 0.0D;
         this.rescaleAllBlocks();
      }

   }

   public boolean hasOrigin() {
      return this.hasOrigin;
   }

   public Vector getOrigin() {
      return this.origin;
   }

   private void rescaleAllBlocks() {
      float newBBSize = this.hasClipping ? (float)(1.41421356274619D * Util.absMaxAxis(this.blockBounds.toVector().add(this.spacing).multiply(this.scale))) : 0.0F;
      boolean clipChanged = this.bbSize != newBBSize;
      this.bbSize = newBBSize;
      if (this.hasKnownPosition) {
         Float bbSize = this.bbSize;
         Iterator var4;
         SingleSchematicBlock block;
         if (this.hasSpacing) {
            for(var4 = this.blocks.iterator(); var4.hasNext(); block.sync(this.liveRot, this.getViewers())) {
               block = (SingleSchematicBlock)var4.next();
               block.setScaleAndSpacing(this.scale, this.origin, this.spacing);
               if (clipChanged) {
                  block.setClipBox(bbSize);
               }
            }
         } else {
            for(var4 = this.blocks.iterator(); var4.hasNext(); block.sync(this.liveRot, this.getViewers())) {
               block = (SingleSchematicBlock)var4.next();
               block.setScaleZeroSpacing(this.scale, this.origin);
               if (clipChanged) {
                  block.setClipBox(bbSize);
               }
            }
         }
      }

   }

   private void syncBlockPositions() {
      if (this.hasKnownPosition) {
         Iterator var1 = this.blocks.iterator();

         while(var1.hasNext()) {
            SingleSchematicBlock block = (SingleSchematicBlock)var1.next();
            block.sync(this.liveRot, this.getViewers());
         }
      }

   }

   protected void sendSpawnPackets(AttachmentViewer viewer, Vector motion) {
      PacketPlayOutSpawnEntityLivingHandle spawnPacket = PacketPlayOutSpawnEntityLivingHandle.createNew();
      spawnPacket.setEntityId(this.mountEntityId);
      spawnPacket.setEntityUUID(UUID.randomUUID());
      spawnPacket.setEntityType(EntityType.ARMOR_STAND);
      spawnPacket.setPosX(this.syncPos.getX() - motion.getX());
      spawnPacket.setPosY(this.syncPos.getY() - motion.getY());
      spawnPacket.setPosZ(this.syncPos.getZ() - motion.getZ());
      spawnPacket.setMotX(motion.getX());
      spawnPacket.setMotY(motion.getY());
      spawnPacket.setMotZ(motion.getZ());
      spawnPacket.setYaw(0.0F);
      spawnPacket.setPitch(0.0F);
      spawnPacket.setHeadYaw(0.0F);
      viewer.sendEntityLivingSpawnPacket(spawnPacket, VirtualDisplayEntity.ARMORSTAND_MOUNT_METADATA);
      Iterator var5 = this.blocks.iterator();

      while(var5.hasNext()) {
         SingleSchematicBlock block = (SingleSchematicBlock)var5.next();
         block.spawn(viewer, this.syncPos, motion);
      }

      viewer.send((PacketHandle)PacketPlayOutMountHandle.createNew(this.mountEntityId, this.getBlockEntityIds()));
   }

   protected void sendDestroyPackets(AttachmentViewer viewer) {
      int[] ids = this.getBlockEntityIds();
      ids = Arrays.copyOf(ids, ids.length + 1);
      ids[ids.length - 1] = this.mountEntityId;
      viewer.send((PacketHandle)PacketPlayOutEntityDestroyHandle.createNewMultiple(ids));
   }

   public void updatePosition(Matrix4x4 transform) {
      MathUtil.setVector(this.livePos, transform.toVector());
      this.liveRot.setTo(transform.getRotation());
      if (this.hasOrigin) {
         Vector offset = this.origin.clone();
         this.liveRot.transformPoint(offset);
         this.livePos.add(offset);
      }

      if (!this.hasKnownPosition) {
         this.hasKnownPosition = true;
         MathUtil.setVector(this.syncPos, this.livePos);
         this.rescaleAllBlocks();
      }

   }

   public void syncPosition(boolean absolute) {
      this.syncBlockPositions();
      double dx = 0.0D;
      double dy = 0.0D;
      double dz = 0.0D;
      if (!absolute) {
         dx = this.livePos.getX() - this.syncPos.getX();
         dy = this.livePos.getY() - this.syncPos.getY();
         dz = this.livePos.getZ() - this.syncPos.getZ();
         double abs_delta = Math.max(Math.max(Math.abs(dx), Math.abs(dy)), Math.abs(dz));
         absolute = abs_delta > 8.0D;
      }

      if (absolute) {
         MathUtil.setVector(this.syncPos, this.livePos);
         this.broadcast(PacketPlayOutEntityTeleportHandle.createNew(this.mountEntityId, this.syncPos.getX(), this.syncPos.getY(), this.syncPos.getZ(), 0.0F, 0.0F, false));
      } else {
         PacketPlayOutRelEntityMoveHandle packet = PacketPlayOutRelEntityMoveHandle.createNew(this.mountEntityId, dx, dy, dz, false);
         MathUtil.addToVector(this.syncPos, packet.getDeltaX(), packet.getDeltaY(), packet.getDeltaZ());
         this.broadcast(packet);
      }

   }
}
