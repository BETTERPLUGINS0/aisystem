package com.bergerkiller.bukkit.tc.actions;

import com.bergerkiller.bukkit.common.entity.type.CommonMinecart;
import com.bergerkiller.bukkit.common.utils.FaceUtil;
import com.bergerkiller.bukkit.tc.Util;
import com.bergerkiller.bukkit.tc.controller.components.ActionTracker;
import com.bergerkiller.bukkit.tc.offline.train.format.OfflineDataBlock;
import com.bergerkiller.bukkit.tc.utils.LauncherConfig;
import java.io.DataInputStream;
import java.io.IOException;
import org.bukkit.block.BlockFace;
import org.bukkit.util.Vector;

public class MemberActionLaunchDirection extends MemberActionLaunch implements MovementAction {
   private BlockFace direction;
   private Vector directionVector;
   private boolean isFaceDirection;
   private boolean directionWasCorrected;

   public MemberActionLaunchDirection() {
      this.direction = BlockFace.SELF;
      this.directionVector = new Vector();
      this.isFaceDirection = true;
      this.directionWasCorrected = false;
   }

   /** @deprecated */
   @Deprecated
   public MemberActionLaunchDirection(double targetdistance, double targetvelocity, BlockFace direction) {
      this.setDirection(direction);
      this.initDistance(targetdistance, targetvelocity, direction);
   }

   public void init(LauncherConfig config, double targetvelocity, double targetspeedlimit, BlockFace direction) {
      this.setDirection(direction);
      this.init(config, targetvelocity, targetspeedlimit);
   }

   public void init(LauncherConfig config, double targetvelocity, double targetspeedlimit, Vector direction) {
      this.setDirection(direction);
      this.init(config, targetvelocity, targetspeedlimit);
   }

   public void init(LauncherConfig config, double targetvelocity, BlockFace direction) {
      this.setDirection(direction);
      this.init(config, targetvelocity);
   }

   public void init(LauncherConfig config, double targetvelocity, Vector direction) {
      this.setDirection(direction);
      this.init(config, targetvelocity);
   }

   public void initTime(int timeTicks, double targetvelocity, BlockFace direction) {
      this.setDirection(direction);
      this.initTime(timeTicks, targetvelocity);
   }

   public void initTime(int timeTicks, double targetvelocity, Vector direction) {
      this.setDirection(direction);
      this.initTime(timeTicks, targetvelocity);
   }

   public void initDistance(double targetdistance, double targetvelocity, BlockFace direction) {
      this.setDirection(direction);
      this.initDistance(targetdistance, targetvelocity);
   }

   public void initDistance(double targetdistance, double targetvelocity, Vector direction) {
      this.setDirection(direction);
      this.initDistance(targetdistance, targetvelocity);
   }

   public void setDirection(BlockFace direction) {
      if (direction == null) {
         throw new IllegalArgumentException("Direction is null");
      } else {
         this.direction = direction;
         this.directionVector = direction == BlockFace.SELF ? new Vector() : FaceUtil.faceToVector(direction).normalize();
         this.isFaceDirection = true;
      }
   }

   public void setDirection(Vector direction) {
      if (direction == null) {
         throw new IllegalArgumentException("Direction is null");
      } else {
         this.direction = FaceUtil.vectorToBlockFace(direction, true);
         this.directionVector = direction;
         this.isFaceDirection = false;
      }
   }

   public BlockFace getDirection() {
      return this.direction;
   }

   public Vector getDirectionVector() {
      return this.directionVector;
   }

   public boolean isFaceDirection() {
      return this.isFaceDirection;
   }

   public void setDirectionCorrected(boolean corrected) {
      this.directionWasCorrected = corrected;
   }

   public boolean isDirectionCorrected() {
      return this.directionWasCorrected;
   }

   public boolean update() {
      boolean success = super.update();
      if (!this.directionWasCorrected) {
         Vector vel = ((CommonMinecart)this.getMember().getEntity()).getVelocity();
         if (vel.lengthSquared() > 1.0E-20D) {
            this.directionWasCorrected = true;
            if (vel.dot(this.directionVector) < 0.0D) {
               this.getGroup().reverse();
            }
         }
      }

      return success;
   }

   public abstract static class BaseSerializer<T extends MemberActionLaunchDirection> extends MemberActionLaunch.BaseSerializer<T> {
      public boolean save(T action, OfflineDataBlock data, ActionTracker tracker) throws IOException {
         super.save((MemberActionLaunch)action, data, tracker);
         data.addChild("launch-direction", (stream) -> {
            Util.writeVariableLengthInt(stream, action.getDirection().ordinal());
            stream.writeBoolean(action.isDirectionCorrected());
         });
         if (!action.isFaceDirection()) {
            data.addChild("launch-direction-vector", (stream) -> {
               Vector v = action.getDirectionVector();
               stream.writeDouble(v.getX());
               stream.writeDouble(v.getY());
               stream.writeDouble(v.getZ());
            });
         }

         return true;
      }

      public T load(OfflineDataBlock data, ActionTracker tracker) throws IOException {
         T action = (MemberActionLaunchDirection)super.load(data, tracker);
         DataInputStream stream = data.findChildOrThrow("launch-direction").readData();

         try {
            int blockFaceOrd = Util.readVariableLengthInt(stream);
            BlockFace[] faces = BlockFace.values();
            action.setDirection(blockFaceOrd >= 0 && blockFaceOrd < faces.length ? faces[blockFaceOrd] : BlockFace.NORTH);
            action.setDirectionCorrected(stream.readBoolean());
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

         data.tryReadChild("launch-direction-vector", (streamx) -> {
            Vector v = new Vector(streamx.readDouble(), streamx.readDouble(), streamx.readDouble());
            action.setDirection(v);
         });
         return action;
      }
   }

   public static class Serializer extends MemberActionLaunchDirection.BaseSerializer<MemberActionLaunchDirection> {
      public MemberActionLaunchDirection create(OfflineDataBlock data) throws IOException {
         return new MemberActionLaunchDirection();
      }
   }
}
