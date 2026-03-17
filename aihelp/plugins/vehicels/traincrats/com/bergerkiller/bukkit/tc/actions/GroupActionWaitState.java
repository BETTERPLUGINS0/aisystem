package com.bergerkiller.bukkit.tc.actions;

public class GroupActionWaitState extends GroupActionWaitForever {
   private boolean stop = false;

   public boolean update() {
      return this.stop || super.update();
   }

   public void stop() {
      this.stop = true;
   }

   public boolean isMovementSuppressed() {
      return false;
   }
}
