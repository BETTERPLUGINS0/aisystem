package com.bergerkiller.bukkit.tc.actions;

import com.bergerkiller.bukkit.tc.actions.registry.ActionRegistry;
import com.bergerkiller.bukkit.tc.controller.components.ActionTracker;
import com.bergerkiller.bukkit.tc.controller.status.TrainStatus;
import com.bergerkiller.bukkit.tc.offline.train.format.OfflineDataBlock;
import java.io.DataInputStream;
import java.io.IOException;
import java.util.Collections;
import java.util.List;

public class GroupActionWaitTicks extends GroupActionWaitForever {
   private int ticks;

   public GroupActionWaitTicks(int ticks) {
      this.ticks = ticks;
   }

   public int getRemainingTicks() {
      return this.ticks;
   }

   public List<TrainStatus> getStatusInfo() {
      return this.ticks > 0 ? Collections.singletonList(new TrainStatus.WaitingForDuration((long)(this.ticks * 50))) : Collections.emptyList();
   }

   public boolean update() {
      if (this.ticks <= 0) {
         return true;
      } else {
         --this.ticks;
         return super.update();
      }
   }

   public static class Serializer implements ActionRegistry.Serializer<GroupActionWaitTicks> {
      public boolean save(GroupActionWaitTicks action, OfflineDataBlock data, ActionTracker tracker) throws IOException {
         data.addChild("wait-ticks", (stream) -> {
            stream.writeInt(action.getRemainingTicks());
         });
         return true;
      }

      public GroupActionWaitTicks load(OfflineDataBlock data, ActionTracker tracker) throws IOException {
         DataInputStream stream = data.findChildOrThrow("wait-ticks").readData();

         int ticks;
         try {
            ticks = stream.readInt();
         } catch (Throwable var8) {
            if (stream != null) {
               try {
                  stream.close();
               } catch (Throwable var7) {
                  var8.addSuppressed(var7);
               }
            }

            throw var8;
         }

         if (stream != null) {
            stream.close();
         }

         return new GroupActionWaitTicks(ticks);
      }
   }
}
