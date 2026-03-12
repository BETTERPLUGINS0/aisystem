package ac.grim.grimac.utils.data;

import ac.grim.grimac.shaded.jetbrains.annotations.Contract;
import lombok.Generated;

public final class RotationData {
   private final float yaw;
   private final float pitch;
   private final int transaction;
   private boolean isAccepted;

   @Contract(
      mutates = "this"
   )
   public void accept() {
      this.isAccepted = true;
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
   public int getTransaction() {
      return this.transaction;
   }

   @Generated
   public boolean isAccepted() {
      return this.isAccepted;
   }

   @Generated
   public RotationData(float yaw, float pitch, int transaction) {
      this.yaw = yaw;
      this.pitch = pitch;
      this.transaction = transaction;
   }

   @Generated
   public boolean equals(Object o) {
      if (o == this) {
         return true;
      } else if (!(o instanceof RotationData)) {
         return false;
      } else {
         RotationData other = (RotationData)o;
         if (Float.compare(this.getYaw(), other.getYaw()) != 0) {
            return false;
         } else if (Float.compare(this.getPitch(), other.getPitch()) != 0) {
            return false;
         } else if (this.getTransaction() != other.getTransaction()) {
            return false;
         } else {
            return this.isAccepted() == other.isAccepted();
         }
      }
   }

   @Generated
   public int hashCode() {
      int PRIME = true;
      int result = 1;
      int result = result * 59 + Float.floatToIntBits(this.getYaw());
      result = result * 59 + Float.floatToIntBits(this.getPitch());
      result = result * 59 + this.getTransaction();
      result = result * 59 + (this.isAccepted() ? 79 : 97);
      return result;
   }

   @Generated
   public String toString() {
      float var10000 = this.getYaw();
      return "RotationData(yaw=" + var10000 + ", pitch=" + this.getPitch() + ", transaction=" + this.getTransaction() + ", isAccepted=" + this.isAccepted() + ")";
   }
}
