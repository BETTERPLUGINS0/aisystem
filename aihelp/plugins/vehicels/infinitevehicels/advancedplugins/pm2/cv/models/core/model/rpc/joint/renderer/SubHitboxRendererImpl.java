package advancedplugins.pm2.cv.models.core.model.rpc.joint.renderer;

import advancedplugins.pm2.cv.models.api.model.rpc.IVisualModel;
import advancedplugins.pm2.cv.models.api.model.rpc.joint.IJoint;
import advancedplugins.pm2.cv.models.api.model.rpc.joint.behavior.JointAction;
import advancedplugins.pm2.cv.models.api.model.rpc.joint.renderer.renderer.SubHitboxRenderer;
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
import org.joml.Vector3f;

public class SubHitboxRendererImpl extends AbstractBehaviorRenderer implements SubHitboxRenderer {
   private final Map<String, SubHitboxRenderer.SubHitbox> spawnQueue = new ConcurrentHashMap();
   private final Map<String, SubHitboxRenderer.SubHitbox> rendered = new ConcurrentHashMap();
   private final Map<String, SubHitboxRenderer.SubHitbox> destroyQueue = new ConcurrentHashMap();
   private boolean initialized;

   public SubHitboxRendererImpl(IVisualModel var1) {
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
      Optional var3 = var2.getJointAction(JointBehaviorTypes.SUB_HITBOX);
      if (var3.isPresent()) {
         JointAction var4 = (JointAction)var3.get();
         if (!((advancedplugins.pm2.cv.models.api.model.rpc.joint.type.SubHitbox)var4).isOBB()) {
            SubHitboxRendererImpl.SubHitboxImpl var5 = new SubHitboxRendererImpl.SubHitboxImpl(this.nmsHandler.getEntityHandler().getNextEntityId(), ((advancedplugins.pm2.cv.models.api.model.rpc.joint.type.SubHitbox)var4).getHitboxId(), UUID.randomUUID(), UUID.randomUUID());
            var5.position.set(((advancedplugins.pm2.cv.models.api.model.rpc.joint.type.SubHitbox)var4).getLocation());
            var5.width.set(((advancedplugins.pm2.cv.models.api.model.rpc.joint.type.SubHitbox)var4).getDimension().x);
            var5.height.set(((advancedplugins.pm2.cv.models.api.model.rpc.joint.type.SubHitbox)var4).getDimension().y);
            this.spawnQueue.put(var1, var5);
            this.destroyQueue.remove(var1);
         }
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
            SubHitboxRenderer.SubHitbox var5 = (SubHitboxRenderer.SubHitbox)this.getQueued(var3);
            if (var5 != null) {
               this.read(var3, var5, var4);
            } else {
               this.create(var3, var4);
            }
         }
      }

   }

   private void read(String var1, SubHitboxRenderer.SubHitbox var2, IJoint var3) {
      Optional var4 = var3.getJointAction(JointBehaviorTypes.SUB_HITBOX);
      var4.ifPresent((var3x) -> {
         var2.getPosition().set(var3x.getLocation());
         var2.getWidth().set(var3x.getDimension().x);
         var2.getHeight().set(var3x.getDimension().y);
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

   @Generated
   public Map<String, SubHitboxRenderer.SubHitbox> getSpawnQueue() {
      return this.spawnQueue;
   }

   @Generated
   public Map<String, SubHitboxRenderer.SubHitbox> getRendered() {
      return this.rendered;
   }

   @Generated
   public Map<String, SubHitboxRenderer.SubHitbox> getDestroyQueue() {
      return this.destroyQueue;
   }

   @Generated
   public boolean isInitialized() {
      return this.initialized;
   }

   @Generated
   public void setInitialized(boolean var1) {
      this.initialized = var1;
   }

   public static class SubHitboxImpl implements SubHitboxRenderer.SubHitbox {
      private final int pivotId;
      private final int hitboxId;
      private final UUID pivotUuid;
      private final UUID hitboxUuid;
      private final DataTracker<Vector3f> position = new UpdateDataTracker(new Vector3f(), Vector3f::set);
      private final DataTracker<Float> width = new DataTracker();
      private final DataTracker<Float> height = new DataTracker();

      public SubHitboxImpl(int var1, int var2, UUID var3, UUID var4) {
         this.pivotId = var1;
         this.hitboxId = var2;
         this.pivotUuid = var3;
         this.hitboxUuid = var4;
      }

      @Generated
      public int getPivotId() {
         return this.pivotId;
      }

      @Generated
      public int getHitboxId() {
         return this.hitboxId;
      }

      @Generated
      public UUID getPivotUuid() {
         return this.pivotUuid;
      }

      @Generated
      public UUID getHitboxUuid() {
         return this.hitboxUuid;
      }

      @Generated
      public DataTracker<Vector3f> getPosition() {
         return this.position;
      }

      @Generated
      public DataTracker<Float> getWidth() {
         return this.width;
      }

      @Generated
      public DataTracker<Float> getHeight() {
         return this.height;
      }
   }
}
