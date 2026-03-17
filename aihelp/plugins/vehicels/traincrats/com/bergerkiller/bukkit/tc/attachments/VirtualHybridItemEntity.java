package com.bergerkiller.bukkit.tc.attachments;

import com.bergerkiller.bukkit.common.math.Matrix4x4;
import com.bergerkiller.bukkit.common.wrappers.Brightness;
import com.bergerkiller.bukkit.tc.attachments.api.AttachmentManager;
import com.bergerkiller.bukkit.tc.attachments.api.AttachmentViewer;
import com.bergerkiller.bukkit.tc.attachments.config.transform.HybridItemTransformType;
import org.bukkit.ChatColor;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

public class VirtualHybridItemEntity extends VirtualSpawnableObject {
   private HybridItemTransformType transformType;
   private ItemStack item;
   private Matrix4x4 transform;
   private double clip;
   private boolean useMinecartInterpolation;
   private Brightness brightness;
   private final Matrix4x4 tmpArmorstandTransform;
   private final Matrix4x4 tmpDisplayTransform;
   private VirtualArmorStandItemEntity armorstand;
   private VirtualDisplayItemEntity display;

   public VirtualHybridItemEntity(AttachmentManager manager) {
      super(manager);
      this.transformType = HybridItemTransformType.ARMORSTAND_HEAD;
      this.item = null;
      this.transform = null;
      this.clip = 0.0D;
      this.useMinecartInterpolation = false;
      this.brightness = Brightness.UNSET;
      this.tmpArmorstandTransform = Matrix4x4.identity();
      this.tmpDisplayTransform = Matrix4x4.identity();
      this.armorstand = null;
      this.display = null;
   }

   public boolean containsEntityId(int entityId) {
      if (this.display != null && this.display.containsEntityId(entityId)) {
         return true;
      } else {
         return this.armorstand != null && this.armorstand.containsEntityId(entityId);
      }
   }

   public void setItem(HybridItemTransformType transformType, ItemStack item) {
      this.transformType = transformType;
      this.item = item;
      if (this.armorstand != null) {
         this.armorstand.setItem(transformType.armorStandTransform(), item);
      }

      if (this.display != null) {
         this.display.setScale(transformType.displayScale());
         this.display.setItem(transformType.displayMode(), item);
      }

   }

   public void setClip(double clip) {
      this.clip = clip;
      if (this.display != null) {
         this.display.setClip(clip);
      }

   }

   public void setBrightness(Brightness brightness) {
      this.brightness = brightness;
      if (this.display != null) {
         this.display.setBrightness(brightness);
      }

   }

   protected void sendSpawnPackets(AttachmentViewer viewer, Vector motion) {
      if (viewer.supportsDisplayEntities()) {
         if (this.display == null) {
            if (this.transform == null) {
               throw new IllegalStateException("Spawn called before updatePosition");
            }

            this.display = new VirtualDisplayItemEntity(this.manager);
            this.display.setGlowColor(this.getGlowColor());
            this.display.setUseMinecartInterpolation(this.useMinecartInterpolation);
            this.display.setClip(this.clip);
            this.display.setScale(this.transformType.displayScale());
            this.display.setBrightness(this.brightness);
            this.display.setItem(this.transformType.displayMode(), this.item);
            this.display.updatePosition(this.transformType.transformDisplay(this.tmpDisplayTransform, this.transform));
            this.display.syncPosition(true);
         }

         this.display.spawn(viewer, motion);
      } else {
         if (this.armorstand == null) {
            if (this.transform == null) {
               throw new IllegalStateException("Spawn called before updatePosition");
            }

            this.armorstand = new VirtualArmorStandItemEntity(this.manager);
            this.armorstand.setGlowColor(this.getGlowColor());
            this.armorstand.setUseMinecartInterpolation(this.useMinecartInterpolation);
            this.armorstand.setItem(this.transformType.armorStandTransform(), this.item);
            this.armorstand.updatePosition(this.transformType.transformArmorStand(this.tmpArmorstandTransform, this.transform));
            this.armorstand.syncPosition(true);
         }

         this.armorstand.spawn(viewer, motion);
      }

   }

   protected void sendDestroyPackets(AttachmentViewer viewer) {
      if (viewer.supportsDisplayEntities()) {
         if (this.display != null) {
            this.display.destroy(viewer);
         }
      } else if (this.armorstand != null) {
         this.armorstand.destroy(viewer);
      }

   }

   protected void applyGlowing(ChatColor color) {
      if (this.display != null) {
         this.display.setGlowColor(color);
      }

      if (this.armorstand != null) {
         this.armorstand.setGlowColor(color);
      }

   }

   public void setUseMinecartInterpolation(boolean use) {
      this.useMinecartInterpolation = use;
      if (this.display != null) {
         this.display.setUseMinecartInterpolation(use);
      }

      if (this.armorstand != null) {
         this.armorstand.setUseMinecartInterpolation(use);
      }

   }

   public void updatePosition(Matrix4x4 transform) {
      this.transform = transform;
      if (this.display != null) {
         this.display.updatePosition(this.transformType.transformDisplay(this.tmpDisplayTransform, transform));
      }

      if (this.armorstand != null) {
         this.armorstand.updatePosition(this.transformType.transformArmorStand(this.tmpArmorstandTransform, transform));
      }

   }

   public void syncPosition(boolean absolute) {
      if (this.display != null) {
         this.display.syncPosition(absolute);
      }

      if (this.armorstand != null) {
         this.armorstand.syncPosition(absolute);
      }

   }
}
