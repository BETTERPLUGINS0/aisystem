package advancedplugins.pm2.cv.models.core.model.rpc.joint.renderer;

import advancedplugins.pm2.cv.models.api.model.rpc.IVisualModel;
import advancedplugins.pm2.cv.models.api.model.rpc.joint.IJoint;
import advancedplugins.pm2.cv.models.api.model.rpc.joint.behavior.JointAction;
import advancedplugins.pm2.cv.models.api.model.rpc.joint.renderer.renderer.LeashRenderer;
import advancedplugins.pm2.cv.models.api.model.rpc.joint.type.JointBehaviorTypes;
import advancedplugins.pm2.cv.models.api.nms.RenderParsers;
import advancedplugins.pm2.cv.models.api.utils.data.tracker.DataTracker;
import advancedplugins.pm2.cv.models.api.utils.data.tracker.UpdateDataTracker;
import java.util.Iterator;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import lombok.Generated;
import org.bukkit.entity.Entity;
import org.joml.Vector3f;

public class LeashRendererImpl extends AbstractBehaviorRenderer implements LeashRenderer {
   private final Map<String, LeashRenderer.Leash> spawnQueue = new ConcurrentHashMap();
   private final Map<String, LeashRenderer.Leash> rendered = new ConcurrentHashMap();
   private final Map<String, LeashRenderer.Leash> destroyQueue = new ConcurrentHashMap();
   private boolean initialized;

   public LeashRendererImpl(IVisualModel var1) {
      super(var1);
   }

   public void initialize() {
      Iterator var1 = this.visualModel.getJoints().entrySet().iterator();

      while(var1.hasNext()) {
         Entry var2 = (Entry)var1.next();
         String var3 = (String)var2.getKey();
         IJoint var4 = (IJoint)var2.getValue();
         this.create(var3, var4);
      }

      this.initialized = true;
   }

   private void create(String var1, IJoint var2) {
      Optional var3 = var2.getJointAction(JointBehaviorTypes.LEASH);
      if (var3.isPresent()) {
         JointAction var4 = (JointAction)var3.get();
         LeashRendererImpl.LeashImpl var5 = new LeashRendererImpl.LeashImpl(this.nmsHandler.getEntityHandler().getNextEntityId(), ((advancedplugins.pm2.cv.models.api.model.rpc.joint.type.Leash)var4).getId());
         var5.position.set(((advancedplugins.pm2.cv.models.api.model.rpc.joint.type.Leash)var4).getLocation());
         var5.connected.set(this.getConnectedId((advancedplugins.pm2.cv.models.api.model.rpc.joint.type.Leash)var4));
         this.spawnQueue.put(var1, var5);
         this.destroyQueue.remove(var1);
      }

   }

   public void readJointData() {
      if (this.initialized) {
         this.destroyQueue.putAll(this.rendered);
         Iterator var1 = this.visualModel.getJoints().entrySet().iterator();

         while(var1.hasNext()) {
            Entry var2 = (Entry)var1.next();
            String var3 = (String)var2.getKey();
            IJoint var4 = (IJoint)var2.getValue();
            LeashRenderer.Leash var5 = (LeashRenderer.Leash)this.getQueued(var3);
            if (var5 != null) {
               this.read(var3, var5, var4);
            } else {
               this.create(var3, var4);
            }
         }
      }

   }

   private void read(String var1, LeashRenderer.Leash var2, IJoint var3) {
      Optional var4 = var3.getJointAction(JointBehaviorTypes.LEASH);
      var4.ifPresent((var3x) -> {
         var2.getPosition().set(var3x.getLocation());
         var2.getConnected().set(this.getConnectedId(var3x));
         this.destroyQueue.remove(var1);
      });
   }

   public void sendToClient(RenderParsers var1) {
      if (this.initialized) {
         Set var2 = this.destroyQueue.keySet();
         Map var3 = this.rendered;
         Objects.requireNonNull(var3);
         Objects.requireNonNull(var3);
         var2.forEach(var3::remove);
         var1.getBehaviorParser(this).sendToClients(this);
         this.rendered.putAll(this.spawnQueue);
         this.spawnQueue.clear();
         this.destroyQueue.clear();
      }

   }

   public void destroy(RenderParsers var1) {
      if (this.initialized) {
         var1.getBehaviorParser(this).destroy(this);
      }

   }

   private int getConnectedId(advancedplugins.pm2.cv.models.api.model.rpc.joint.type.Leash var1) {
      Entity var2 = var1.getConnectedEntity();
      if (var2 != null) {
         return var2.getEntityId();
      } else {
         JointAction var3 = (JointAction)var1.getConnectedLeash();
         return var3 != null ? ((advancedplugins.pm2.cv.models.api.model.rpc.joint.type.Leash)var3).getId() : -1;
      }
   }

   @Generated
   public Map<String, LeashRenderer.Leash> getSpawnQueue() {
      return this.spawnQueue;
   }

   @Generated
   public Map<String, LeashRenderer.Leash> getRendered() {
      return this.rendered;
   }

   @Generated
   public Map<String, LeashRenderer.Leash> getDestroyQueue() {
      return this.destroyQueue;
   }

   public static class LeashImpl implements LeashRenderer.Leash {
      private final int pivotId;
      private final UUID pivotUUID = UUID.randomUUID();
      private final int leashId;
      private final UUID leastUUID = UUID.randomUUID();
      private final DataTracker<Vector3f> position = new UpdateDataTracker(new Vector3f(), Vector3f::set);
      private final DataTracker<Integer> connected = new DataTracker();

      public LeashImpl(int var1, int var2) {
         this.pivotId = var1;
         this.leashId = var2;
      }

      @Generated
      public int getPivotId() {
         return this.pivotId;
      }

      @Generated
      public UUID getPivotUUID() {
         return this.pivotUUID;
      }

      @Generated
      public int getLeashId() {
         return this.leashId;
      }

      @Generated
      public UUID getLeastUUID() {
         return this.leastUUID;
      }

      @Generated
      public DataTracker<Vector3f> getPosition() {
         return this.position;
      }

      @Generated
      public DataTracker<Integer> getConnected() {
         return this.connected;
      }
   }
}
