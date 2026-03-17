package com.bergerkiller.bukkit.tc.attachments.particle;

import com.bergerkiller.bukkit.common.Common;
import com.bergerkiller.bukkit.common.collections.octree.DoubleOctree.Entry;
import com.bergerkiller.bukkit.common.math.Quaternion;
import com.bergerkiller.bukkit.common.utils.EntityUtil;
import com.bergerkiller.bukkit.common.utils.PacketUtil;
import com.bergerkiller.bukkit.common.wrappers.DataWatcher;
import com.bergerkiller.bukkit.tc.Util;
import com.bergerkiller.generated.net.minecraft.network.protocol.game.PacketPlayOutEntityDestroyHandle;
import com.bergerkiller.generated.net.minecraft.network.protocol.game.PacketPlayOutEntityEquipmentHandle;
import com.bergerkiller.generated.net.minecraft.network.protocol.game.PacketPlayOutEntityMetadataHandle;
import com.bergerkiller.generated.net.minecraft.network.protocol.game.PacketPlayOutEntityTeleportHandle;
import com.bergerkiller.generated.net.minecraft.network.protocol.game.PacketPlayOutSpawnEntityHandle;
import com.bergerkiller.generated.net.minecraft.world.entity.EntityHandle;
import com.bergerkiller.generated.net.minecraft.world.entity.decoration.EntityArmorStandHandle;
import java.util.Iterator;
import java.util.UUID;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

public class VirtualArrowItem {
   private int entityId;
   private boolean glowing = false;
   private double posX;
   private double posY;
   private double posZ;
   private Vector rotation;
   private ItemStack item;

   private VirtualArrowItem(int entityId) {
      this.entityId = entityId;
   }

   public static VirtualArrowItem create(int entityId) {
      return new VirtualArrowItem(entityId);
   }

   public boolean hasEntityId() {
      return this.entityId != -1;
   }

   public VirtualArrowItem glowing(boolean glowing) {
      this.glowing = glowing;
      return this;
   }

   public VirtualArrowItem item(ItemStack item) {
      this.item = item;
      return this;
   }

   public VirtualArrowItem position(Entry<?> position, Quaternion orientation) {
      this.rotation = Util.getArmorStandPose(orientation);
      this.rotation.setX(this.rotation.getX() - 90.0D);
      this.posX = position.getX() + 0.315D;
      this.posY = position.getY() - 1.35D;
      this.posZ = position.getZ();
      Vector upVector = new Vector(0.05D, -0.05D, -0.56D);
      orientation.transformPoint(upVector);
      this.posX += upVector.getX();
      this.posY += upVector.getY();
      this.posZ += upVector.getZ();
      return this;
   }

   public VirtualArrowItem position(Vector position, Quaternion orientation) {
      this.rotation = Util.getArmorStandPose(orientation);
      this.rotation.setX(this.rotation.getX() - 90.0D);
      this.posX = position.getX() + 0.315D;
      this.posY = position.getY() - 1.35D;
      this.posZ = position.getZ();
      Vector upVector = new Vector(0.05D, -0.05D, -0.56D);
      orientation.transformPoint(upVector);
      this.posX += upVector.getX();
      this.posY += upVector.getY();
      this.posZ += upVector.getZ();
      return this;
   }

   public VirtualArrowItem move(Iterable<Player> viewers) {
      if (this.entityId != -1) {
         PacketPlayOutEntityTeleportHandle tpPacket = PacketPlayOutEntityTeleportHandle.createNew(this.entityId, this.posX, this.posY, this.posZ, 0.0F, 0.0F, false);
         DataWatcher metadata = new DataWatcher();
         metadata.set(EntityArmorStandHandle.DATA_POSE_ARM_RIGHT, this.rotation);
         PacketPlayOutEntityMetadataHandle metaPacket = PacketPlayOutEntityMetadataHandle.createNew(this.entityId, metadata, true);
         Iterator var5 = viewers.iterator();

         while(var5.hasNext()) {
            Player viewer = (Player)var5.next();
            PacketUtil.sendPacket(viewer, tpPacket);
            PacketUtil.sendPacket(viewer, metaPacket);
         }
      }

      return this;
   }

   public VirtualArrowItem updateItem(Player viewer) {
      if (this.entityId != -1) {
         PacketPlayOutEntityEquipmentHandle equipPacket = Util.createNonPlayerEquipmentPacket(this.entityId, EquipmentSlot.HAND, this.item);
         PacketUtil.sendPacket(viewer, equipPacket);
      }

      return this;
   }

   public VirtualArrowItem updateGlowing(Player viewer) {
      if (this.entityId != -1) {
         DataWatcher metadata = new DataWatcher();
         metadata.setByte(EntityHandle.DATA_FLAGS, 160);
         metadata.setFlag(EntityHandle.DATA_FLAGS, 1, Common.evaluateMCVersion(">", "1.8"));
         metadata.setByte(EntityArmorStandHandle.DATA_ARMORSTAND_FLAGS, 20);
         if (this.glowing) {
            metadata.setFlag(EntityHandle.DATA_FLAGS, 64, true);
         }

         PacketPlayOutEntityMetadataHandle metaPacket = PacketPlayOutEntityMetadataHandle.createNew(this.entityId, metadata, true);
         PacketUtil.sendPacket(viewer, metaPacket);
      }

      return this;
   }

   public int spawn(Player viewer) {
      if (this.entityId == -1) {
         this.entityId = EntityUtil.getUniqueEntityId();
      }

      PacketPlayOutSpawnEntityHandle spawnPacket = PacketPlayOutSpawnEntityHandle.createNew();
      spawnPacket.setEntityId(this.entityId);
      spawnPacket.setEntityUUID(UUID.randomUUID());
      spawnPacket.setEntityType(EntityType.ARMOR_STAND);
      spawnPacket.setPosX(this.posX);
      spawnPacket.setPosY(this.posY);
      spawnPacket.setPosZ(this.posZ);
      PacketUtil.sendPacket(viewer, spawnPacket);
      DataWatcher metadata = new DataWatcher();
      metadata.set(EntityHandle.DATA_NO_GRAVITY, true);
      metadata.setByte(EntityHandle.DATA_FLAGS, 160);
      metadata.setFlag(EntityHandle.DATA_FLAGS, 1, Common.evaluateMCVersion(">", "1.8"));
      metadata.setByte(EntityArmorStandHandle.DATA_ARMORSTAND_FLAGS, 28);
      if (this.glowing) {
         metadata.setFlag(EntityHandle.DATA_FLAGS, 64, true);
      }

      metadata.set(EntityArmorStandHandle.DATA_POSE_ARM_RIGHT, this.rotation);
      PacketPlayOutEntityMetadataHandle metaPacket = PacketPlayOutEntityMetadataHandle.createNew(this.entityId, metadata, true);
      PacketUtil.sendPacket(viewer, metaPacket);
      PacketPlayOutEntityEquipmentHandle equipPacket = Util.createNonPlayerEquipmentPacket(this.entityId, EquipmentSlot.HAND, this.item);
      PacketUtil.sendPacket(viewer, equipPacket);
      return this.entityId;
   }

   public void destroy(Player viewer) {
      if (this.entityId != -1) {
         PacketUtil.sendPacket(viewer, PacketPlayOutEntityDestroyHandle.createNewSingle(this.entityId));
      }

   }
}
