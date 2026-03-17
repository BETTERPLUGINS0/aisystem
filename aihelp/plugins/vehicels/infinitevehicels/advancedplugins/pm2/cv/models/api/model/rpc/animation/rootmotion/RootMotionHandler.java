package advancedplugins.pm2.cv.models.api.model.rpc.animation.rootmotion;

import advancedplugins.pm2.cv.models.api.model.rpc.IModelContainer;
import advancedplugins.pm2.cv.models.api.model.rpc.joint.IJoint;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import org.bukkit.util.Vector;

public class RootMotionHandler {
   private final IModelContainer modelContainer;
   private final Queue<RootMotion> velocityQueue = new ConcurrentLinkedQueue();
   private IJoint rootJoint;
   private double baseWeight = 1.0D;
   private double weight = 0.0D;
   private boolean override;
   private int queued;

   public RootMotionHandler(IModelContainer var1) {
      this.modelContainer = var1;
   }

   public void queueVelocity(RootMotion var1) {
      this.queued = 2;
      this.velocityQueue.add(var1);
   }

   public boolean isQueued() {
      return this.queued > 0;
   }

   public RootMotionDelta calculateRootMotion(Vector var1) {
      if (this.isQueued()) {
         --this.queued;
      }

      if (!this.velocityQueue.isEmpty() && this.rootJoint != null) {
         double var2 = this.baseWeight + this.weight;
         RootMotion var4 = (RootMotion)this.velocityQueue.poll();
         var1.multiply(this.override ? 0.0D : this.baseWeight / var2);
         Vector var5 = var4.delta().multiply(this.override ? 1.0D : this.weight / var2);
         return new RootMotionDelta(var5, var4.jointOnGround());
      } else {
         return null;
      }
   }

   public IModelContainer getModeledEntity() {
      return this.modelContainer;
   }

   public Queue<RootMotion> getVelocityQueue() {
      return this.velocityQueue;
   }

   public IJoint getRootJoint() {
      return this.rootJoint;
   }

   public void setRootJoint(IJoint var1) {
      this.rootJoint = var1;
   }

   public double getBaseWeight() {
      return this.baseWeight;
   }

   public void setBaseWeight(double var1) {
      this.baseWeight = var1;
   }

   public double getWeight() {
      return this.weight;
   }

   public void setWeight(double var1) {
      this.weight = var1;
   }

   public boolean isOverride() {
      return this.override;
   }

   public void setOverride(boolean var1) {
      this.override = var1;
   }
}
