package com.bergerkiller.bukkit.tc.attachments.control;

import com.bergerkiller.bukkit.common.config.ConfigurationNode;
import com.bergerkiller.bukkit.common.map.MapTexture;
import com.bergerkiller.bukkit.common.math.Matrix4x4;
import com.bergerkiller.bukkit.common.math.OrientedBoundingBox;
import com.bergerkiller.bukkit.common.math.Quaternion;
import com.bergerkiller.bukkit.common.math.Vector3;
import com.bergerkiller.bukkit.common.utils.CommonUtil;
import com.bergerkiller.bukkit.common.utils.EntityUtil;
import com.bergerkiller.bukkit.common.utils.LogicUtil;
import com.bergerkiller.bukkit.common.wrappers.DataWatcher;
import com.bergerkiller.bukkit.tc.TrainCarts;
import com.bergerkiller.bukkit.tc.attachments.VirtualDisplayEntity;
import com.bergerkiller.bukkit.tc.attachments.api.Attachment;
import com.bergerkiller.bukkit.tc.attachments.api.AttachmentManager;
import com.bergerkiller.bukkit.tc.attachments.api.AttachmentType;
import com.bergerkiller.bukkit.tc.attachments.api.AttachmentViewer;
import com.bergerkiller.bukkit.tc.attachments.helper.HelperMethods;
import com.bergerkiller.bukkit.tc.attachments.particle.VirtualBoundingBox;
import com.bergerkiller.bukkit.tc.attachments.ui.MapWidgetSizeBox;
import com.bergerkiller.bukkit.tc.attachments.ui.menus.PositionMenu;
import com.bergerkiller.generated.net.minecraft.network.protocol.PacketHandle;
import com.bergerkiller.generated.net.minecraft.network.protocol.game.PacketPlayOutEntityDestroyHandle;
import com.bergerkiller.generated.net.minecraft.network.protocol.game.PacketPlayOutEntityMetadataHandle;
import com.bergerkiller.generated.net.minecraft.network.protocol.game.PacketPlayOutEntityTeleportHandle;
import com.bergerkiller.generated.net.minecraft.network.protocol.game.PacketPlayOutSpawnEntityHandle;
import com.bergerkiller.generated.net.minecraft.network.protocol.game.PacketPlayOutSpawnEntityLivingHandle;
import com.bergerkiller.generated.net.minecraft.world.entity.EntityAgeableHandle;
import com.bergerkiller.generated.net.minecraft.world.entity.EntityHandle;
import com.bergerkiller.generated.net.minecraft.world.entity.InteractionHandle;
import com.bergerkiller.generated.net.minecraft.world.entity.monster.EntitySlimeHandle;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.UUID;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

public class CartAttachmentHitBox extends CartAttachment {
   private static final Vector3 DEFAULT_SCALE = new Vector3(1.0D, 1.0D, 1.0D);
   public static final AttachmentType TYPE = new CartAttachmentHitBox.BaseHitBoxType() {
      public String getID() {
         return "HITBOX";
      }

      public Attachment createController(ConfigurationNode config) {
         return new CartAttachmentHitBox();
      }
   };
   private final OrientedBoundingBox bbox = new OrientedBoundingBox();
   private final Set<Player> nearbyViewers = new HashSet();
   private int hitboxEntityId = EntityUtil.getUniqueEntityId();
   private final UUID hitboxEntityUUID = UUID.randomUUID();
   private CartAttachmentHitBox.Box box = null;
   private double heightOffset = 0.0D;
   private double minSize;
   private CartAttachmentHitBox.SizeMode sizeMode;

   public CartAttachmentHitBox() {
      this.sizeMode = CartAttachmentHitBox.SizeMode.SMALLEST;
   }

   public void onLoad(ConfigurationNode config) {
      Vector3 size = (Vector3)LogicUtil.fixNull(this.getConfiguredPosition().size, DEFAULT_SCALE);
      this.bbox.setSize(new Vector(size.x, size.y, size.z));
      this.heightOffset = 0.5D * size.y;
      this.minSize = Math.min(Math.min(size.x, size.y), size.z);
      CartAttachmentHitBox.SizeMode newSizeMode = CartAttachmentHitBox.SizeMode.fromSize(this.minSize);
      Iterator var4;
      AttachmentViewer viewer;
      if (newSizeMode != this.sizeMode && !this.nearbyViewers.isEmpty()) {
         var4 = this.getAttachmentViewers().iterator();

         while(var4.hasNext()) {
            viewer = (AttachmentViewer)var4.next();
            this.despawnHitBoxForViewer(viewer);
         }

         this.sizeMode = newSizeMode;
         var4 = this.getAttachmentViewers().iterator();

         while(var4.hasNext()) {
            viewer = (AttachmentViewer)var4.next();
            this.updateHitBoxForViewer(viewer);
         }
      } else {
         this.sizeMode = newSizeMode;
         var4 = this.getAttachmentViewers().iterator();

         while(var4.hasNext()) {
            viewer = (AttachmentViewer)var4.next();
            if (viewer.supportsDisplayEntities()) {
               this.updateInteractionMeta(viewer);
               this.updateHitBoxForViewer(viewer);
            }
         }
      }

   }

   public void makeVisible(Player viewer) {
      this.makeVisible(AttachmentViewer.fallback(viewer));
   }

   public void makeHidden(Player viewer) {
      this.makeHidden(AttachmentViewer.fallback(viewer));
   }

   public void makeVisible(AttachmentViewer viewer) {
      if (this.box != null) {
         this.box.makeVisible(viewer);
      }

      this.updateHitBoxForViewer(viewer);
   }

   public boolean containsEntityId(int id) {
      return id == this.hitboxEntityId;
   }

   private Vector getPOVLocationBottom(Player player) {
      Location eyeLoc = player.getEyeLocation();
      Vector eyePosition = eyeLoc.toVector();
      Vector eyeDirection = eyeLoc.getDirection();
      double distanceToBox = this.bbox.hitTest(eyePosition, eyeDirection);
      if (distanceToBox == Double.MAX_VALUE) {
         eyeDirection = this.bbox.getPosition().clone().subtract(eyePosition).normalize();
         if (Double.isNaN(eyeDirection.getX())) {
            return this.bbox.getPosition();
         }

         distanceToBox = this.bbox.hitTest(eyePosition, eyeDirection);
         if (distanceToBox == Double.MAX_VALUE) {
            return this.bbox.getPosition();
         }
      }

      if (distanceToBox > 6.0D) {
         return null;
      } else {
         return distanceToBox == 0.0D ? eyePosition : eyePosition.add(eyeDirection.multiply(distanceToBox + 0.5D * this.sizeMode.size));
      }
   }

   public void makeHidden(AttachmentViewer viewer) {
      if (this.box != null) {
         this.box.makeHidden(viewer);
      }

      this.despawnHitBoxForViewer(viewer);
   }

   public OrientedBoundingBox getBoundingBox() {
      return this.bbox;
   }

   public void setBoxColor(ChatColor color) {
      if (color != null) {
         if (this.box == null) {
            this.box = new CartAttachmentHitBox.Box(this.getManager(), this.bbox);
            this.box.entity.setGlowColor(color);
            Iterator var2 = this.getAttachmentViewers().iterator();

            while(var2.hasNext()) {
               AttachmentViewer viewer = (AttachmentViewer)var2.next();
               this.box.makeVisible(viewer);
            }
         } else {
            this.box.entity.setGlowColor(color);
         }
      } else if (this.box != null) {
         this.box.entity.setGlowColor((ChatColor)null);
         this.box.tickLastHidden = CommonUtil.getServerTicks();
      }

   }

   public void onFocus() {
      this.setBoxColor(HelperMethods.getFocusGlowColor(this));
   }

   public void onBlur() {
      this.setBoxColor((ChatColor)null);
   }

   public void onTick() {
      if (this.box != null && !this.isFocused() && CommonUtil.getServerTicks() - this.box.tickLastHidden > 40) {
         this.box.entity.destroyForAll();
         this.box = null;
      }

   }

   public void onTransformChanged(Matrix4x4 transform) {
      Quaternion orientation = transform.getRotation();
      this.bbox.setPosition(transform.toVector().add(orientation.upVector().multiply(this.heightOffset)));
      this.bbox.setOrientation(orientation);
      if (this.box != null) {
         this.box.update(this.bbox);
      }

   }

   public void onMove(boolean absolute) {
      if (this.box != null) {
         this.box.sync();
      }

      Iterator var2 = this.getAttachmentViewers().iterator();

      while(var2.hasNext()) {
         AttachmentViewer viewer = (AttachmentViewer)var2.next();
         this.updateHitBoxForViewer(viewer);
      }

   }

   private void updateHitBoxForViewer(AttachmentViewer viewer) {
      Vector pos = this.getPOVLocationBottom(viewer.getPlayer());
      if (pos == null) {
         this.despawnHitBoxForViewer(viewer);
      } else {
         boolean usesInteractionEntity = viewer.supportsDisplayEntities();
         if (usesInteractionEntity) {
            pos.setY(pos.getY() - 0.5D * this.minSize);
         } else {
            pos.setY(pos.getY() - 0.5D * this.sizeMode.size);
         }

         if (this.nearbyViewers.add(viewer.getPlayer())) {
            if (usesInteractionEntity) {
               PacketPlayOutSpawnEntityHandle packet = PacketPlayOutSpawnEntityHandle.createNew();
               packet.setEntityId(this.hitboxEntityId);
               packet.setEntityUUID(this.hitboxEntityUUID);
               packet.setEntityType(VirtualDisplayEntity.INTERACTION_ENTITY_TYPE);
               packet.setPosX(pos.getX());
               packet.setPosY(pos.getY());
               packet.setPosZ(pos.getZ());
               viewer.send((PacketHandle)packet);
               this.updateInteractionMeta(viewer);
            } else {
               PacketPlayOutSpawnEntityLivingHandle packet = PacketPlayOutSpawnEntityLivingHandle.createNew();
               packet.setEntityId(this.hitboxEntityId);
               packet.setEntityUUID(this.hitboxEntityUUID);
               packet.setEntityType(this.sizeMode.type);
               packet.setPosX(pos.getX());
               packet.setPosY(pos.getY());
               packet.setPosZ(pos.getZ());
               DataWatcher meta = new DataWatcher();
               meta.set(EntityHandle.DATA_FLAGS, -96);
               meta.set(EntityHandle.DATA_NO_GRAVITY, true);
               this.sizeMode.apply(meta);
               viewer.sendEntityLivingSpawnPacket(packet, meta);
               viewer.sendDisableCollision(this.hitboxEntityUUID);
            }
         } else {
            PacketPlayOutEntityTeleportHandle packet = PacketPlayOutEntityTeleportHandle.createNew(this.hitboxEntityId, pos.getX(), pos.getY(), pos.getZ(), 0.0F, 0.0F, false);
            viewer.send((PacketHandle)packet);
         }

      }
   }

   private void updateInteractionMeta(AttachmentViewer viewer) {
      DataWatcher meta = new DataWatcher();
      meta.set(InteractionHandle.DATA_WIDTH, (float)this.minSize);
      meta.set(InteractionHandle.DATA_HEIGHT, (float)this.minSize);
      viewer.send((PacketHandle)PacketPlayOutEntityMetadataHandle.createNew(this.hitboxEntityId, meta, true));
   }

   private void despawnHitBoxForViewer(AttachmentViewer viewer) {
      if (this.nearbyViewers.remove(viewer.getPlayer())) {
         viewer.send((PacketHandle)PacketPlayOutEntityDestroyHandle.createNewSingle(this.hitboxEntityId));
      }

   }

   private static class Box {
      public final VirtualBoundingBox entity;
      private int tickLastHidden = 0;

      public Box(AttachmentManager manager, OrientedBoundingBox bbox) {
         this.entity = VirtualBoundingBox.create(manager);
         this.entity.update(bbox);
      }

      public void update(OrientedBoundingBox bbox) {
         this.entity.update(bbox);
      }

      public void sync() {
         this.entity.syncPosition(true);
      }

      public void makeVisible(AttachmentViewer viewer) {
         this.entity.spawn(viewer, new Vector(0.0D, 0.0D, 0.0D));
      }

      public void makeHidden(AttachmentViewer viewer) {
         this.entity.destroy(viewer);
      }
   }

   private static enum SizeMode {
      SLIME_SZ8(8),
      SLIME_SZ7(7),
      SLIME_SZ6(6),
      SLIME_SZ5(5),
      SLIME_SZ4(4),
      SLIME_SZ3(3),
      SLIME_SZ2(2),
      PIG(0.9D, EntityType.PIG, false),
      SLIME_SZ1(1),
      BABY_PIG(0.45D, EntityType.PIG, true),
      RABBIT(0.4D, EntityType.RABBIT, false),
      BABY_RABBIT(0.2D, EntityType.RABBIT, true);

      public final double size;
      public final EntityType type;
      public final boolean baby;
      public final int slimeSize;
      public static final CartAttachmentHitBox.SizeMode SMALLEST = values()[values().length - 1];

      private SizeMode(int slimeSize) {
         this.size = (double)(2.04F * 0.255F * (float)slimeSize);
         this.type = EntityType.SLIME;
         this.baby = false;
         this.slimeSize = slimeSize;
      }

      private SizeMode(double size, EntityType type, boolean baby) {
         this.size = size;
         this.type = type;
         this.baby = baby;
         this.slimeSize = 0;
      }

      public void apply(DataWatcher datawatcher) {
         if (this.baby) {
            datawatcher.set(EntityAgeableHandle.DATA_IS_BABY, true);
         } else if (this.slimeSize != 0) {
            datawatcher.set(EntitySlimeHandle.DATA_SIZE, this.slimeSize);
         }

      }

      public static CartAttachmentHitBox.SizeMode fromSize(double minSize) {
         CartAttachmentHitBox.SizeMode[] var2 = values();
         int var3 = var2.length;

         for(int var4 = 0; var4 < var3; ++var4) {
            CartAttachmentHitBox.SizeMode mode = var2[var4];
            if (mode.size <= minSize) {
               return mode;
            }
         }

         return SMALLEST;
      }

      // $FF: synthetic method
      private static CartAttachmentHitBox.SizeMode[] $values() {
         return new CartAttachmentHitBox.SizeMode[]{SLIME_SZ8, SLIME_SZ7, SLIME_SZ6, SLIME_SZ5, SLIME_SZ4, SLIME_SZ3, SLIME_SZ2, PIG, SLIME_SZ1, BABY_PIG, RABBIT, BABY_RABBIT};
      }
   }

   protected abstract static class BaseHitBoxType implements AttachmentType {
      public double getSortPriority() {
         return 1.0D;
      }

      public MapTexture getIcon(ConfigurationNode config) {
         return MapTexture.loadPluginResource(TrainCarts.plugin, "com/bergerkiller/bukkit/tc/textures/attachments/hitbox.png");
      }

      public void migrateConfiguration(ConfigurationNode config) {
         if (config.isNode("size")) {
            ConfigurationNode size = config.getNode("size");
            config.set("position.sizeX", size.get("x", 1.0D));
            config.set("position.sizeY", size.get("y", 1.0D));
            config.set("position.sizeZ", size.get("z", 1.0D));
            size.remove();
         }

      }

      public void createPositionMenu(PositionMenu.Builder builder) {
         builder.addRow((menu) -> {
            return (new MapWidgetSizeBox() {
               public void onAttached() {
                  super.onAttached();
                  this.setSize((Double)menu.getPositionConfigValue("sizeX", CartAttachmentHitBox.DEFAULT_SCALE.x), (Double)menu.getPositionConfigValue("sizeY", CartAttachmentHitBox.DEFAULT_SCALE.y), (Double)menu.getPositionConfigValue("sizeZ", CartAttachmentHitBox.DEFAULT_SCALE.z));
               }

               public void onSizeChanged() {
                  menu.updatePositionConfig((config) -> {
                     config.set("sizeX", this.x.getValue());
                     config.set("sizeY", this.y.getValue());
                     config.set("sizeZ", this.z.getValue());
                  });
               }
            }).setBounds(25, 0, menu.getSliderWidth(), 35);
         }).addLabel(0, 3, "Size X").addLabel(0, 15, "Size Y").addLabel(0, 27, "Size Z").setSpacingAbove(3);
      }
   }
}
