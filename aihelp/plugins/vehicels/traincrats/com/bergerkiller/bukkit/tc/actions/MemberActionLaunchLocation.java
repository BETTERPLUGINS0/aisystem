package com.bergerkiller.bukkit.tc.actions;

import com.bergerkiller.bukkit.common.entity.type.CommonMinecart;
import com.bergerkiller.bukkit.common.offline.OfflineWorld;
import com.bergerkiller.bukkit.common.utils.FaceUtil;
import com.bergerkiller.bukkit.common.utils.StreamUtil;
import com.bergerkiller.bukkit.tc.controller.components.ActionTracker;
import com.bergerkiller.bukkit.tc.offline.train.format.OfflineDataBlock;
import java.io.DataInputStream;
import java.io.IOException;
import org.bukkit.Location;

public class MemberActionLaunchLocation extends MemberActionLaunchDirection implements MovementAction {
   private final Location target;

   public MemberActionLaunchLocation(double targetvelocity, Location target) {
      this.initDistance(0.0D, targetvelocity);
      this.target = target.clone();
   }

   public Location getTargetLocation() {
      return this.target;
   }

   public void bind() {
      super.bind();
      this.setTargetDistance(((CommonMinecart)this.getMember().getEntity()).loc.distance(this.target));
      this.setDirection(this.getMember().getDirection());
   }

   public void start() {
      super.setDirection(FaceUtil.getDirection(this.getEntity().getLocation(), this.target, false));
      double d = this.getEntity().loc.xz.distance(this.target);
      d += (double)Math.abs(this.target.getBlockY() - this.getEntity().loc.y.block());
      super.setTargetDistance(d);
      super.start();
   }

   public static class Serializer extends MemberActionLaunchDirection.BaseSerializer<MemberActionLaunchLocation> {
      public boolean save(MemberActionLaunchLocation action, OfflineDataBlock data, ActionTracker tracker) throws IOException {
         super.save((MemberActionLaunchDirection)action, data, tracker);
         data.addChild("launch-location", (stream) -> {
            Location loc = action.getTargetLocation();
            StreamUtil.writeUUID(stream, loc.getWorld().getUID());
            stream.writeDouble(loc.getX());
            stream.writeDouble(loc.getY());
            stream.writeDouble(loc.getZ());
         });
         return true;
      }

      public MemberActionLaunchLocation create(OfflineDataBlock data) throws IOException {
         DataInputStream stream = data.findChildOrThrow("launch-location").readData();

         Location target;
         try {
            OfflineWorld world = OfflineWorld.of(StreamUtil.readUUID(stream));
            if (!world.isLoaded()) {
               throw new IllegalStateException("Launch target world is not loaded");
            }

            double x = stream.readDouble();
            double y = stream.readDouble();
            double z = stream.readDouble();
            target = new Location(world.getLoadedWorld(), x, y, z);
         } catch (Throwable var12) {
            if (stream != null) {
               try {
                  stream.close();
               } catch (Throwable var11) {
                  var12.addSuppressed(var11);
               }
            }

            throw var12;
         }

         if (stream != null) {
            stream.close();
         }

         return new MemberActionLaunchLocation(0.0D, target);
      }
   }
}
