package com.bergerkiller.bukkit.tc.actions;

import com.bergerkiller.bukkit.tc.actions.registry.ActionRegistry;
import com.bergerkiller.bukkit.tc.controller.components.ActionTracker;
import com.bergerkiller.bukkit.tc.controller.status.TrainStatus;
import com.bergerkiller.bukkit.tc.offline.train.format.OfflineDataBlock;
import java.io.IOException;
import java.util.Collections;
import java.util.List;

public class GroupActionWaitForever extends GroupAction implements WaitAction {
   public boolean update() {
      this.getGroup().stop();
      return false;
   }

   public boolean isMovementSuppressed() {
      return true;
   }

   public List<TrainStatus> getStatusInfo() {
      return Collections.singletonList(new TrainStatus.WaitingForever());
   }

   public static class Serializer implements ActionRegistry.Serializer<GroupActionWaitForever> {
      public boolean save(GroupActionWaitForever action, OfflineDataBlock data, ActionTracker tracker) throws IOException {
         return true;
      }

      public GroupActionWaitForever load(OfflineDataBlock data, ActionTracker tracker) throws IOException {
         return new GroupActionWaitForever();
      }
   }
}
