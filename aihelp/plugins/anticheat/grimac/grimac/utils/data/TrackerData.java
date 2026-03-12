package ac.grim.grimac.utils.data;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.entity.type.EntityType;
import lombok.Generated;

public class TrackerData {
   private double x;
   private double y;
   private double z;
   private float xRot;
   private float yRot;
   private EntityType entityType;
   private int lastTransactionHung;
   private int legacyPointEightMountedUpon;

   public TrackerData(double x, double y, double z, float xRot, float yRot, EntityType entityType, int lastTransactionHung) {
      this.x = x;
      this.y = y;
      this.z = z;
      this.xRot = xRot;
      this.yRot = yRot;
      this.entityType = entityType;
      this.lastTransactionHung = lastTransactionHung;
   }

   @Generated
   public double getX() {
      return this.x;
   }

   @Generated
   public double getY() {
      return this.y;
   }

   @Generated
   public double getZ() {
      return this.z;
   }

   @Generated
   public float getXRot() {
      return this.xRot;
   }

   @Generated
   public float getYRot() {
      return this.yRot;
   }

   @Generated
   public EntityType getEntityType() {
      return this.entityType;
   }

   @Generated
   public int getLastTransactionHung() {
      return this.lastTransactionHung;
   }

   @Generated
   public int getLegacyPointEightMountedUpon() {
      return this.legacyPointEightMountedUpon;
   }

   @Generated
   public void setX(double x) {
      this.x = x;
   }

   @Generated
   public void setY(double y) {
      this.y = y;
   }

   @Generated
   public void setZ(double z) {
      this.z = z;
   }

   @Generated
   public void setXRot(float xRot) {
      this.xRot = xRot;
   }

   @Generated
   public void setYRot(float yRot) {
      this.yRot = yRot;
   }

   @Generated
   public void setEntityType(EntityType entityType) {
      this.entityType = entityType;
   }

   @Generated
   public void setLastTransactionHung(int lastTransactionHung) {
      this.lastTransactionHung = lastTransactionHung;
   }

   @Generated
   public void setLegacyPointEightMountedUpon(int legacyPointEightMountedUpon) {
      this.legacyPointEightMountedUpon = legacyPointEightMountedUpon;
   }

   @Generated
   public boolean equals(Object o) {
      if (o == this) {
         return true;
      } else if (!(o instanceof TrackerData)) {
         return false;
      } else {
         TrackerData other = (TrackerData)o;
         if (!other.canEqual(this)) {
            return false;
         } else if (Double.compare(this.getX(), other.getX()) != 0) {
            return false;
         } else if (Double.compare(this.getY(), other.getY()) != 0) {
            return false;
         } else if (Double.compare(this.getZ(), other.getZ()) != 0) {
            return false;
         } else if (Float.compare(this.getXRot(), other.getXRot()) != 0) {
            return false;
         } else if (Float.compare(this.getYRot(), other.getYRot()) != 0) {
            return false;
         } else if (this.getLastTransactionHung() != other.getLastTransactionHung()) {
            return false;
         } else if (this.getLegacyPointEightMountedUpon() != other.getLegacyPointEightMountedUpon()) {
            return false;
         } else {
            Object this$entityType = this.getEntityType();
            Object other$entityType = other.getEntityType();
            if (this$entityType == null) {
               if (other$entityType != null) {
                  return false;
               }
            } else if (!this$entityType.equals(other$entityType)) {
               return false;
            }

            return true;
         }
      }
   }

   @Generated
   protected boolean canEqual(Object other) {
      return other instanceof TrackerData;
   }

   @Generated
   public int hashCode() {
      int PRIME = true;
      int result = 1;
      long $x = Double.doubleToLongBits(this.getX());
      int result = result * 59 + (int)($x >>> 32 ^ $x);
      long $y = Double.doubleToLongBits(this.getY());
      result = result * 59 + (int)($y >>> 32 ^ $y);
      long $z = Double.doubleToLongBits(this.getZ());
      result = result * 59 + (int)($z >>> 32 ^ $z);
      result = result * 59 + Float.floatToIntBits(this.getXRot());
      result = result * 59 + Float.floatToIntBits(this.getYRot());
      result = result * 59 + this.getLastTransactionHung();
      result = result * 59 + this.getLegacyPointEightMountedUpon();
      Object $entityType = this.getEntityType();
      result = result * 59 + ($entityType == null ? 43 : $entityType.hashCode());
      return result;
   }

   @Generated
   public String toString() {
      double var10000 = this.getX();
      return "TrackerData(x=" + var10000 + ", y=" + this.getY() + ", z=" + this.getZ() + ", xRot=" + this.getXRot() + ", yRot=" + this.getYRot() + ", entityType=" + String.valueOf(this.getEntityType()) + ", lastTransactionHung=" + this.getLastTransactionHung() + ", legacyPointEightMountedUpon=" + this.getLegacyPointEightMountedUpon() + ")";
   }
}
