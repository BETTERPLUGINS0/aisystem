package advancedplugins.pm2.cv.models.api.model.rpc.mount.controller;

import advancedplugins.pm2.cv.models.api.model.rpc.IVisualModel;
import advancedplugins.pm2.cv.models.api.model.rpc.joint.type.Mount;
import advancedplugins.pm2.cv.models.api.nms.entity.wrapper.LookController;
import advancedplugins.pm2.cv.models.api.nms.entity.wrapper.MoveController;
import org.bukkit.entity.Entity;
import org.joml.Vector3f;

public interface MountController {
   Entity getEntity();

   MountController.MountInput getInput();

   void setInput(MountController.MountInput var1);

   Mount getMount();

   void setCanDamageMount(boolean var1);

   boolean canDamageMount();

   void setCanInteractMount(boolean var1);

   boolean canInteractMount();

   void updateDriverMovement(MoveController var1, IVisualModel var2);

   void updatePassengerMovement(MoveController var1, IVisualModel var2);

   default void updateRiderPosition(MoveController controller) {
      Vector3f pos = this.getMount().getGlobalLocation();
      controller.movePassenger(this.getEntity(), (double)pos.x, (double)pos.y, (double)pos.z);
   }

   void updateDirection(LookController var1, IVisualModel var2);

   public static class MountInput {
      private Float side;
      private Float front;
      private boolean forward;
      private boolean backward;
      private boolean left;
      private boolean right;
      private boolean jump;
      private boolean sneak;
      private boolean sprint;
      private boolean updated;

      public MountInput() {
         this(false, false, false, false, false, false, false);
      }

      public MountInput(float var1, float var2, boolean var3, boolean var4) {
         this.side = var1;
         this.front = var2;
         this.jump = var3;
         this.sneak = var4;
      }

      public MountInput(boolean var1, boolean var2, boolean var3, boolean var4, boolean var5, boolean var6, boolean var7) {
         this.forward = var1;
         this.backward = var2;
         this.left = var3;
         this.right = var4;
         this.jump = var5;
         this.sneak = var6;
         this.sprint = var7;
      }

      public float getFront() {
         if (this.front != null) {
            return this.front;
         } else {
            return this.forward ? 1.0F : (this.backward ? -1.0F : 0.0F);
         }
      }

      public void setFront(Float var1) {
         this.front = var1;
      }

      public float getSide() {
         if (this.side != null) {
            return this.side;
         } else {
            return this.left ? 1.0F : (this.right ? -1.0F : 0.0F);
         }
      }

      public void setSide(Float var1) {
         this.side = var1;
      }

      public boolean isForward() {
         return this.forward;
      }

      public void setForward(boolean var1) {
         this.forward = var1;
      }

      public boolean isBackward() {
         return this.backward;
      }

      public void setBackward(boolean var1) {
         this.backward = var1;
      }

      public boolean isLeft() {
         return this.left;
      }

      public void setLeft(boolean var1) {
         this.left = var1;
      }

      public boolean isRight() {
         return this.right;
      }

      public void setRight(boolean var1) {
         this.right = var1;
      }

      public boolean isJump() {
         return this.jump;
      }

      public void setJump(boolean var1) {
         this.jump = var1;
      }

      public boolean isSneak() {
         return this.sneak;
      }

      public void setSneak(boolean var1) {
         this.sneak = var1;
      }

      public boolean isSprint() {
         return this.sprint;
      }

      public void setSprint(boolean var1) {
         this.sprint = var1;
      }

      public boolean isUpdated() {
         return this.updated;
      }

      public void setUpdated(boolean var1) {
         this.updated = var1;
      }
   }
}
