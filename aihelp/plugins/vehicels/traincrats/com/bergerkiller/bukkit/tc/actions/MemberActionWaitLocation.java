package com.bergerkiller.bukkit.tc.actions;

import com.bergerkiller.bukkit.common.offline.OfflineWorld;
import com.bergerkiller.bukkit.common.utils.StreamUtil;
import com.bergerkiller.bukkit.tc.actions.registry.ActionRegistry;
import com.bergerkiller.bukkit.tc.controller.components.ActionTracker;
import com.bergerkiller.bukkit.tc.offline.train.format.OfflineDataBlock;
import java.io.DataInputStream;
import java.io.IOException;
import org.bukkit.Location;

public class MemberActionWaitLocation extends MemberAction implements WaitAction {
   private final Location dest;
   private final double radiussquared;

   public MemberActionWaitLocation(Location dest) {
      this(dest, 1.0D);
   }

   public MemberActionWaitLocation(Location dest, double radius) {
      this.dest = dest;
      this.radiussquared = radius * radius;
   }

   public Location getTargetLocation() {
      return this.dest;
   }

   public double getRadius() {
      return Math.sqrt(this.radiussquared);
   }

   public boolean update() {
      return this.getWorld() == this.dest.getWorld() && this.getEntity().loc.distanceSquared(this.dest) <= this.radiussquared;
   }

   public boolean isMovementSuppressed() {
      return true;
   }

   public static class Serializer implements ActionRegistry.Serializer<MemberActionWaitLocation> {
      public boolean save(MemberActionWaitLocation action, OfflineDataBlock data, ActionTracker tracker) throws IOException {
         data.addChild("wait-location", (stream) -> {
            Location loc = action.getTargetLocation();
            StreamUtil.writeUUID(stream, loc.getWorld().getUID());
            stream.writeDouble(loc.getX());
            stream.writeDouble(loc.getY());
            stream.writeDouble(loc.getZ());
            stream.writeDouble(action.getRadius());
         });
         return true;
      }

      public MemberActionWaitLocation load(OfflineDataBlock data, ActionTracker tracker) throws IOException {
         DataInputStream stream = data.findChildOrThrow("wait-location").readData();

         Location target;
         double radius;
         try {
            OfflineWorld world = OfflineWorld.of(StreamUtil.readUUID(stream));
            if (!world.isLoaded()) {
               throw new IllegalStateException("Wait target world is not loaded");
            }

            double x = stream.readDouble();
            double y = stream.readDouble();
            double z = stream.readDouble();
            target = new Location(world.getLoadedWorld(), x, y, z);
            radius = stream.readDouble();
         } catch (Throwable var15) {
            if (stream != null) {
               try {
                  stream.close();
               } catch (Throwable var14) {
                  var15.addSuppressed(var14);
               }
            }

            throw var15;
         }

         if (stream != null) {
            stream.close();
         }

         return new MemberActionWaitLocation(target, radius);
      }
   }
}
