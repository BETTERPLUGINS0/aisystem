package com.bergerkiller.bukkit.tc.actions;

import com.bergerkiller.bukkit.tc.actions.registry.ActionRegistry;
import com.bergerkiller.bukkit.tc.controller.components.ActionTracker;
import com.bergerkiller.bukkit.tc.offline.train.format.OfflineDataBlock;
import java.io.DataInputStream;
import java.io.IOException;

public class GroupActionWaitDelay extends GroupActionWaitTill implements WaitAction {
   private long delay;

   public GroupActionWaitDelay(long delayMS) {
      super(System.currentTimeMillis() + delayMS);
      this.delay = delayMS;
   }

   public long getRemainingDelay() {
      return this.hasActionStarted() ? Math.max(0L, this.getTime() - System.currentTimeMillis()) : this.delay;
   }

   public void start() {
      this.setTime(System.currentTimeMillis() + this.delay);
   }

   public static class Serializer implements ActionRegistry.Serializer<GroupActionWaitDelay> {
      public boolean save(GroupActionWaitDelay action, OfflineDataBlock data, ActionTracker tracker) throws IOException {
         data.addChild("wait-delay", (stream) -> {
            stream.writeLong(action.getRemainingDelay());
         });
         return true;
      }

      public GroupActionWaitDelay load(OfflineDataBlock data, ActionTracker tracker) throws IOException {
         DataInputStream stream = data.findChildOrThrow("wait-delay").readData();

         long delay;
         try {
            delay = stream.readLong();
         } catch (Throwable var9) {
            if (stream != null) {
               try {
                  stream.close();
               } catch (Throwable var8) {
                  var9.addSuppressed(var8);
               }
            }

            throw var9;
         }

         if (stream != null) {
            stream.close();
         }

         return new GroupActionWaitDelay(delay);
      }
   }
}
