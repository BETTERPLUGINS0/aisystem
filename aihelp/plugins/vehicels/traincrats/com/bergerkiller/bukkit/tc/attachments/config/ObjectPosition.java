package com.bergerkiller.bukkit.tc.attachments.config;

import com.bergerkiller.bukkit.common.config.ConfigurationNode;
import com.bergerkiller.bukkit.common.math.Matrix4x4;
import com.bergerkiller.bukkit.common.math.Vector3;
import com.bergerkiller.bukkit.tc.attachments.api.AttachmentAnchor;
import com.bergerkiller.bukkit.tc.attachments.api.AttachmentManager;
import com.bergerkiller.bukkit.tc.attachments.api.AttachmentType;

public class ObjectPosition {
   public AttachmentAnchor anchor;
   public Vector3 position;
   public Vector3 rotation;
   public Vector3 size;
   public Matrix4x4 transform;
   private boolean _isDefault;
   private boolean _isIdentity;

   public ObjectPosition() {
      this.anchor = AttachmentAnchor.DEFAULT;
      this.position = new Vector3();
      this.rotation = new Vector3();
      this.size = new Vector3();
      this.transform = new Matrix4x4();
      this._isDefault = true;
      this._isIdentity = true;
   }

   public void reset() {
      this._isDefault = true;
      this._isIdentity = true;
      this.position.x = 0.0D;
      this.position.y = 0.0D;
      this.position.z = 0.0D;
      this.rotation.x = 0.0D;
      this.rotation.y = 0.0D;
      this.rotation.z = 0.0D;
      this.size.x = 1.0D;
      this.size.y = 1.0D;
      this.size.z = 1.0D;
      this.transform.setIdentity();
      this.anchor = AttachmentAnchor.DEFAULT;
   }

   public static boolean isDefaultSeatParent(ConfigurationNode config) {
      if (!config.isEmpty()) {
         if (config.contains("anchor")) {
            String name = (String)config.get("anchor", AttachmentAnchor.DEFAULT.getName());
            if (name.equals(AttachmentAnchor.SEAT_PARENT.getName())) {
               return true;
            }
         }

         return false;
      } else {
         return true;
      }
   }

   public void load(ObjectPosition source) {
      this.anchor = source.anchor;
      this.position = source.position;
      this.rotation = source.rotation;
      this.size = source.size;
      this.transform.set(source.transform);
      this._isDefault = source._isDefault;
      this._isIdentity = source._isIdentity;
   }

   public void load(Class<? extends AttachmentManager> managerType, AttachmentType attachmentType, ConfigurationNode config) {
      if (config != null && !config.isEmpty()) {
         if (config.contains("anchor")) {
            this.anchor = AttachmentAnchor.find(managerType, attachmentType, (String)config.get("anchor", AttachmentAnchor.DEFAULT.getName()));
         } else {
            this.anchor = AttachmentAnchor.DEFAULT;
         }

         this._isDefault = false;
         this.position.x = (Double)config.get("posX", 0.0D);
         this.position.y = (Double)config.get("posY", 0.0D);
         this.position.z = (Double)config.get("posZ", 0.0D);
         this.rotation.x = (Double)config.get("rotX", 0.0D);
         this.rotation.y = (Double)config.get("rotY", 0.0D);
         this.rotation.z = (Double)config.get("rotZ", 0.0D);
         this.size.x = (Double)config.getOrDefault("sizeX", 1.0D);
         this.size.y = (Double)config.getOrDefault("sizeY", 1.0D);
         this.size.z = (Double)config.getOrDefault("sizeZ", 1.0D);
         this._isIdentity = this.position.x == 0.0D && this.position.y == 0.0D && this.position.z == 0.0D && this.rotation.x == 0.0D && this.rotation.y == 0.0D && this.rotation.z == 0.0D;
         this.initTransform();
      } else {
         this.reset();
      }
   }

   public void initTransform() {
      this.transform.setIdentity();
      this.transform.translate(this.position);
      this.transform.rotateYawPitchRoll(this.rotation);
   }

   public boolean isDefault() {
      return this._isDefault;
   }

   public boolean isIdentity() {
      return this._isIdentity;
   }
}
