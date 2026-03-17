package advancedplugins.pm2.cv.models.core.model.rpc;

import advancedplugins.pm2.cv.models.api.model.rpc.IPosition;
import lombok.Generated;
import org.joml.Vector3f;

public class Position implements IPosition {
   private final Vector3f xyz;
   private final Vector3f rotation;
   private final Vector3f scale;
   private final Vector3f origin;
   private final float yaw;
   private final float pitch;
   private float yHeadRot;
   private float xHeadRot;
   private float yBodyRot;

   public Position(Vector3f var1, Vector3f var2, Vector3f var3, Vector3f var4, float var5, float var6) {
      this.yHeadRot = 0.0F;
      this.xHeadRot = 0.0F;
      this.yBodyRot = 0.0F;
      this.xyz = var1;
      this.rotation = var2;
      this.scale = var3;
      this.origin = var4;
      this.yaw = var5;
      this.pitch = var6;
   }

   public Position(Vector3f var1, Vector3f var2, Vector3f var3) {
      this(var1, var2, var3, new Vector3f(), 0.0F, 0.0F);
   }

   public Position(Vector3f var1) {
      this(var1, new Vector3f(), new Vector3f(), new Vector3f(), 0.0F, 0.0F);
   }

   public Vector3f getXYZ() {
      return this.xyz;
   }

   public void setXYZ(Vector3f var1) {
      this.xyz.set(var1);
   }

   public void setOrigin(Vector3f var1) {
      this.origin.set(var1);
   }

   public void setOrigin(double var1, double var3, double var5) {
      this.origin.set(var1, var3, var5);
   }

   public void setXYZ(double var1, double var3, double var5) {
      this.xyz.set(var1, var3, var5);
   }

   public void setRotation(double var1, double var3, double var5) {
      this.rotation.set(var1, var3, var5);
   }

   public void setRotation(Vector3f var1) {
      this.rotation.set(var1);
   }

   public void setScale(double var1, double var3, double var5) {
      this.scale.set(var1, var3, var5);
   }

   public void setScale(Vector3f var1) {
      this.scale.set(var1);
   }

   @Generated
   public Vector3f getRotation() {
      return this.rotation;
   }

   @Generated
   public Vector3f getScale() {
      return this.scale;
   }

   @Generated
   public Vector3f getOrigin() {
      return this.origin;
   }

   @Generated
   public float getYaw() {
      return this.yaw;
   }

   @Generated
   public float getPitch() {
      return this.pitch;
   }

   @Generated
   public float getYHeadRot() {
      return this.yHeadRot;
   }

   @Generated
   public float getXHeadRot() {
      return this.xHeadRot;
   }

   @Generated
   public float getYBodyRot() {
      return this.yBodyRot;
   }

   @Generated
   public void setYHeadRot(float var1) {
      this.yHeadRot = var1;
   }

   @Generated
   public void setXHeadRot(float var1) {
      this.xHeadRot = var1;
   }

   @Generated
   public void setYBodyRot(float var1) {
      this.yBodyRot = var1;
   }
}
