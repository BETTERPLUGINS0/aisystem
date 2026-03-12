package ac.grim.grimac.utils.data;

import ac.grim.grimac.utils.math.Vector3dm;
import lombok.Generated;

public class SetBackData {
   private final TeleportData teleportData;
   private final float xRot;
   private final float yRot;
   private final Vector3dm velocity;
   private final boolean vehicle;
   private boolean isComplete = false;
   private boolean isPlugin;
   private int ticksComplete = 0;

   public SetBackData(TeleportData teleportData, float xRot, float yRot, Vector3dm velocity, boolean vehicle, boolean isPlugin) {
      this.teleportData = teleportData;
      this.xRot = xRot;
      this.yRot = yRot;
      this.velocity = velocity;
      this.vehicle = vehicle;
      this.isPlugin = isPlugin;
   }

   public void tick() {
      if (this.isComplete) {
         ++this.ticksComplete;
      }

   }

   @Generated
   public TeleportData getTeleportData() {
      return this.teleportData;
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
   public Vector3dm getVelocity() {
      return this.velocity;
   }

   @Generated
   public boolean isVehicle() {
      return this.vehicle;
   }

   @Generated
   public boolean isComplete() {
      return this.isComplete;
   }

   @Generated
   public boolean isPlugin() {
      return this.isPlugin;
   }

   @Generated
   public int getTicksComplete() {
      return this.ticksComplete;
   }

   @Generated
   public void setComplete(boolean isComplete) {
      this.isComplete = isComplete;
   }

   @Generated
   public void setPlugin(boolean isPlugin) {
      this.isPlugin = isPlugin;
   }

   @Generated
   public void setTicksComplete(int ticksComplete) {
      this.ticksComplete = ticksComplete;
   }

   @Generated
   public String toString() {
      String var10000 = String.valueOf(this.getTeleportData());
      return "SetBackData(teleportData=" + var10000 + ", xRot=" + this.getXRot() + ", yRot=" + this.getYRot() + ", velocity=" + String.valueOf(this.getVelocity()) + ", vehicle=" + this.isVehicle() + ", isComplete=" + this.isComplete() + ", isPlugin=" + this.isPlugin() + ", ticksComplete=" + this.getTicksComplete() + ")";
   }
}
