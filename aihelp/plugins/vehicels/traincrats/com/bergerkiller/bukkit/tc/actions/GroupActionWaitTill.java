package com.bergerkiller.bukkit.tc.actions;

import com.bergerkiller.bukkit.tc.actions.registry.ActionRegistry;
import com.bergerkiller.bukkit.tc.controller.components.ActionTracker;
import com.bergerkiller.bukkit.tc.controller.status.TrainStatus;
import com.bergerkiller.bukkit.tc.offline.train.format.OfflineDataBlock;
import java.io.DataInputStream;
import java.io.IOException;
import java.util.Collections;
import java.util.List;

public class GroupActionWaitTill extends GroupActionWaitForever {
   private long finishtime;

   public GroupActionWaitTill(long finishtime) {
      this.setTime(finishtime);
   }

   protected void setTime(long finishtime) {
      this.finishtime = finishtime;
   }

   public long getTime() {
      return this.finishtime;
   }

   public List<TrainStatus> getStatusInfo() {
      long remaining = this.finishtime - System.currentTimeMillis();
      return remaining > 0L ? Collections.singletonList(new TrainStatus.WaitingForDuration(remaining)) : Collections.emptyList();
   }

   public boolean update() {
      return this.finishtime <= System.currentTimeMillis() || super.update();
   }

   public static class Serializer implements ActionRegistry.Serializer<GroupActionWaitTill> {
      public boolean save(GroupActionWaitTill action, OfflineDataBlock data, ActionTracker tracker) throws IOException {
         data.addChild("wait-till", (stream) -> {
            stream.writeLong(action.getTime());
         });
         return true;
      }

      public GroupActionWaitTill load(OfflineDataBlock data, ActionTracker tracker) throws IOException {
         DataInputStream stream = data.findChildOrThrow("wait-till").readData();

         long time;
         try {
            time = stream.readLong();
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

         return new GroupActionWaitTill(time);
      }
   }
}
