package com.bergerkiller.bukkit.tc.actions;

import com.bergerkiller.bukkit.tc.actions.registry.ActionRegistry;
import com.bergerkiller.bukkit.tc.controller.MinecartMember;
import com.bergerkiller.bukkit.tc.controller.components.ActionTracker;
import com.bergerkiller.bukkit.tc.controller.status.TrainStatus;
import com.bergerkiller.bukkit.tc.offline.train.format.OfflineDataBlock;
import com.bergerkiller.bukkit.tc.utils.LaunchFunction;
import com.bergerkiller.bukkit.tc.utils.LauncherConfig;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;

public class MemberActionLaunch extends MemberAction implements MovementAction {
   private static final double minVelocity = 0.001D;
   private static final double minLaunchVelocity = 0.05D;
   private double distanceoffset;
   private int timeoffset;
   private double targetvelocity;
   private double targetspeedlimit;
   private double distance;
   private double lastVelocity;
   private double lastspeedlimit;
   private LauncherConfig config;
   private LaunchFunction function;

   public MemberActionLaunch() {
      this.init(LauncherConfig.createDefault(), 0.0D);
   }

   public void init(LauncherConfig config, double targetvelocity) {
      this.init(config, targetvelocity, Double.NaN);
   }

   public void init(LauncherConfig config, double targetvelocity, double targetspeedlimit) {
      this.config = config;
      this.targetvelocity = targetvelocity;
      this.targetspeedlimit = targetspeedlimit;
      this.timeoffset = 0;
      this.distanceoffset = 0.0D;
      this.initFunction();
      if (this.config.hasDistance() && this.config.getDistance() < 0.001D) {
         this.config.setDuration(0);
      }

      this.distance = 0.0D;
      this.lastVelocity = 0.0D;
   }

   private void initFunction() {
      try {
         this.function = (LaunchFunction)this.config.getFunction().newInstance();
      } catch (Throwable var2) {
         this.getTrainCarts().getLogger().log(Level.SEVERE, "Unhandled error initializing launch function", var2);
         this.function = new LaunchFunction.Linear();
      }

   }

   /** @deprecated */
   @Deprecated
   public MemberActionLaunch(double targetdistance, double targetvelocity) {
      this();
      this.initDistance(targetdistance, targetvelocity);
   }

   public void initTime(int timeTicks, double targetvelocity) {
      LauncherConfig newConfig = new LauncherConfig();
      newConfig.setFunction(this.function.getClass());
      newConfig.setDuration(timeTicks);
      this.init(newConfig, targetvelocity);
   }

   public void initDistance(double targetdistance, double targetvelocity) {
      LauncherConfig newConfig = new LauncherConfig();
      newConfig.setFunction(this.function.getClass());
      newConfig.setDistance(targetdistance);
      this.init(newConfig, targetvelocity);
   }

   public void setFunction(Class<? extends LaunchFunction> function) {
      LauncherConfig newConfig = this.config.clone();
      newConfig.setFunction(function);
      this.init(newConfig, this.targetvelocity);
   }

   public List<TrainStatus> getStatusInfo() {
      return Collections.singletonList(new TrainStatus.Launching(this.targetvelocity, this.targetspeedlimit, this.config));
   }

   public void start() {
      this.lastVelocity = this.getMember().getRealSpeedLimited();
      this.lastspeedlimit = this.getGroup().getProperties().getSpeedLimit();
      if (!Double.isNaN(this.targetspeedlimit) && this.targetspeedlimit > this.lastspeedlimit) {
         this.getGroup().getProperties().setSpeedLimit(this.targetspeedlimit);
         this.lastspeedlimit = this.targetspeedlimit;
      }

      this.function.setMinimumVelocity(0.001D);
      this.function.setMaximumVelocity(Double.isNaN(this.targetspeedlimit) ? this.lastspeedlimit : Math.max(this.targetspeedlimit, this.lastspeedlimit));
      this.function.setVelocityRange(this.lastVelocity, Double.isNaN(this.targetspeedlimit) ? this.targetvelocity : Math.min(this.targetspeedlimit, this.targetvelocity));
      if (this.function.getStartVelocity() < 0.05D && this.function.getEndVelocity() < 0.05D) {
         this.function.setStartVelocity(0.05D);
      }

      this.function.configure(this.config);
   }

   public boolean isMovementSuppressed() {
      return true;
   }

   public double getTargetVelocity() {
      return this.targetvelocity;
   }

   public double getTargetDistance() {
      return this.config.getDistance();
   }

   protected void setTargetDistance(double distance) {
      this.config.setDistance(distance);
   }

   public double getDistance() {
      return this.distance;
   }

   public boolean update() {
      if (this.getMember().isDerailed() && !this.getMember().isMovingVerticalOnly()) {
         this.onLaunchingDone(false);
         return true;
      } else if (this.function.isInstantaneous()) {
         this.onLaunchingDone(true);
         return true;
      } else {
         if (this.lastspeedlimit != this.getGroup().getProperties().getSpeedLimit()) {
            this.lastspeedlimit = this.getGroup().getProperties().getSpeedLimit();
            this.targetspeedlimit = Double.NaN;
            this.function.setMaximumVelocity(this.lastspeedlimit);
            this.function.setVelocityRange(this.lastVelocity, this.targetvelocity);
            this.timeoffset = this.elapsedTicks();
            this.distanceoffset = this.distance;
            if (this.config.hasDuration()) {
               this.config.setDuration(this.config.getDuration() - this.timeoffset);
            } else if (this.config.hasDistance()) {
               this.config.setDistance(this.config.getDistance() - this.distanceoffset);
            }

            this.function.configure(this.config);
         }

         if (this.distance != 0.0D) {
            Iterator var1 = this.getGroup().iterator();

            while(var1.hasNext()) {
               MinecartMember<?> mm = (MinecartMember)var1.next();
               if (mm.getRealSpeed() < 0.001D && this.lastVelocity > 0.01D) {
                  this.onLaunchingDone(false);
                  return true;
               }
            }
         }

         int time = this.elapsedTicks() - this.timeoffset;
         if (time > this.function.getTotalTime()) {
            this.onLaunchingDone(true);
            return true;
         } else {
            this.lastVelocity = this.function.getDistance(time) - this.distance + this.distanceoffset;
            this.getGroup().setForwardForce(this.lastVelocity / (double)this.getGroup().getUpdateStepCount());
            if (this.isFullTick()) {
               this.distance += this.lastVelocity;
            }

            return false;
         }
      }
   }

   private void onLaunchingDone(boolean successful) {
      if (!Double.isNaN(this.targetspeedlimit) && this.targetspeedlimit < this.lastspeedlimit) {
         this.getGroup().getProperties().setSpeedLimit(this.targetspeedlimit);
      }

      if (successful) {
         this.getGroup().setForwardForce(this.targetvelocity / (double)this.getGroup().getUpdateStepCount());
      }

   }

   protected static void saveStateTo(DataOutputStream stream, MemberActionLaunch action) throws IOException {
      stream.writeDouble(action.distanceoffset);
      stream.writeInt(action.timeoffset);
      stream.writeDouble(action.targetvelocity);
      stream.writeDouble(action.targetspeedlimit);
      stream.writeDouble(action.distance);
      stream.writeDouble(action.lastVelocity);
      stream.writeDouble(action.lastspeedlimit);
      action.config.writeTo(stream);
      stream.writeDouble(action.function.getMinimumVelocity());
      stream.writeDouble(action.function.getMaximumVelocity());
      stream.writeDouble(action.function.getStartVelocity());
      stream.writeDouble(action.function.getEndVelocity());
   }

   protected static void loadStateFrom(DataInputStream stream, MemberActionLaunch action) throws IOException {
      action.distanceoffset = stream.readDouble();
      action.timeoffset = stream.readInt();
      action.targetvelocity = stream.readDouble();
      action.targetspeedlimit = stream.readDouble();
      action.distance = stream.readDouble();
      action.lastVelocity = stream.readDouble();
      action.lastspeedlimit = stream.readDouble();
      action.config = LauncherConfig.readFrom(stream);
      action.initFunction();
      action.function.setMinimumVelocity(stream.readDouble());
      action.function.setMaximumVelocity(stream.readDouble());
      action.function.setStartVelocity(stream.readDouble());
      action.function.setEndVelocity(stream.readDouble());
      action.function.configure(action.config);
   }

   public abstract static class BaseSerializer<T extends MemberActionLaunch> implements ActionRegistry.Serializer<T> {
      public boolean save(T action, OfflineDataBlock data, ActionTracker tracker) throws IOException {
         data.addChild("launch-state", (stream) -> {
            MemberActionLaunch.saveStateTo(stream, action);
         });
         return true;
      }

      public abstract T create(OfflineDataBlock var1) throws IOException;

      public T load(OfflineDataBlock data, ActionTracker tracker) throws IOException {
         T action = this.create(data);
         DataInputStream stream = data.findChildOrThrow("launch-state").readData();

         try {
            MemberActionLaunch.loadStateFrom(stream, action);
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

         return action;
      }
   }

   public static class Serializer extends MemberActionLaunch.BaseSerializer<MemberActionLaunch> {
      public MemberActionLaunch create(OfflineDataBlock data) throws IOException {
         return new MemberActionLaunch();
      }
   }
}
