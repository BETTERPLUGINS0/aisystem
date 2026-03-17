package com.bergerkiller.bukkit.tc.actions;

import com.bergerkiller.bukkit.tc.actions.registry.ActionRegistry;
import com.bergerkiller.bukkit.tc.controller.components.ActionTracker;
import com.bergerkiller.bukkit.tc.offline.train.format.OfflineDataBlock;
import java.io.DataInputStream;
import java.io.IOException;

public class MemberActionWaitDistance extends MemberAction implements WaitAction {
   private double distance;

   public MemberActionWaitDistance(double distance) {
      this.distance = distance;
   }

   public double getDistance() {
      return this.distance;
   }

   public boolean update() {
      this.distance -= this.getEntity().getMovedXZDistance();
      return this.distance <= 0.0D;
   }

   public boolean isMovementSuppressed() {
      return true;
   }

   public static class Serializer implements ActionRegistry.Serializer<MemberActionWaitDistance> {
      public boolean save(MemberActionWaitDistance action, OfflineDataBlock data, ActionTracker tracker) throws IOException {
         data.addChild("wait-distance", (stream) -> {
            stream.writeDouble(action.getDistance());
         });
         return true;
      }

      public MemberActionWaitDistance load(OfflineDataBlock data, ActionTracker tracker) throws IOException {
         DataInputStream stream = data.findChildOrThrow("wait-distance").readData();

         double distance;
         try {
            distance = stream.readDouble();
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

         return new MemberActionWaitDistance(distance);
      }
   }
}
