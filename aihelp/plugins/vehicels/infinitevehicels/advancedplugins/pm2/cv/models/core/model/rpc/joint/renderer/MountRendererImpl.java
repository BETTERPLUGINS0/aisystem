package advancedplugins.pm2.cv.models.core.model.rpc.joint.renderer;

import advancedplugins.pm2.cv.models.api.model.rpc.IVisualModel;
import advancedplugins.pm2.cv.models.api.model.rpc.joint.IJoint;
import advancedplugins.pm2.cv.models.api.model.rpc.joint.behavior.JointAction;
import advancedplugins.pm2.cv.models.api.model.rpc.joint.renderer.renderer.MountRenderer;
import advancedplugins.pm2.cv.models.api.model.rpc.joint.type.JointBehaviorTypes;
import advancedplugins.pm2.cv.models.api.nms.RenderParsers;
import advancedplugins.pm2.cv.models.api.utils.data.tracker.CollectionDataTracker;
import advancedplugins.pm2.cv.models.api.utils.data.tracker.DataTracker;
import advancedplugins.pm2.cv.models.api.utils.data.tracker.UpdateDataTracker;
import advancedplugins.pm2.cv.models.api.utils.math.MathUtils;
import java.util.Collection;
import java.util.HashSet;
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

public class MountRendererImpl extends AbstractBehaviorRenderer implements MountRenderer {
   private final Map<String, MountRenderer.Mount> spawnQueue = new ConcurrentHashMap();
   private final Map<String, MountRenderer.Mount> rendered = new ConcurrentHashMap();
   private final Map<String, MountRenderer.Mount> destroyQueue = new ConcurrentHashMap();
   private boolean initialized;

   public MountRendererImpl(IVisualModel var1) {
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
      Optional var3 = var2.getJointAction(JointBehaviorTypes.MOUNT);
      if (var3.isPresent()) {
         JointAction var4 = (JointAction)var3.get();
         MountRendererImpl.MountImpl var5 = new MountRendererImpl.MountImpl(this.nmsHandler.getEntityHandler().getNextEntityId(), UUID.randomUUID(), this.nmsHandler.getEntityHandler().getNextEntityId(), UUID.randomUUID());
         var5.position.set(((advancedplugins.pm2.cv.models.api.model.rpc.joint.type.Mount)var4).getGlobalLocation());
         var5.yaw.set(MathUtils.rotToByte(var2.getYaw()));
         var5.health.set(var2.getVisualModel().getModeledEntity().getBase().getHealth());
         var5.maxHealth.set(var2.getVisualModel().getModeledEntity().getBase().getMaxHealth());
         Iterator var6 = ((advancedplugins.pm2.cv.models.api.model.rpc.joint.type.Mount)var4).getPassengers().iterator();

         while(var6.hasNext()) {
            Entity var7 = (Entity)var6.next();
            ((Collection)var5.passengers.get()).add(var7.getEntityId());
         }

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
            MountRenderer.Mount var5 = (MountRenderer.Mount)this.getQueued(var3);
            if (var5 != null) {
               this.read(var3, var5, var4);
            } else {
               this.create(var3, var4);
            }
         }
      }

   }

   private void read(String var1, MountRenderer.Mount var2, IJoint var3) {
      var2.getYaw().set(MathUtils.rotToByte(var3.getYaw()));
      var2.getHealth().set(var3.getVisualModel().getModeledEntity().getBase().getHealth());
      var2.getMaxHealth().set(var3.getVisualModel().getModeledEntity().getBase().getMaxHealth());
      Optional var4 = var3.getJointAction(JointBehaviorTypes.MOUNT);
      var4.ifPresent((var3x) -> {
         var2.getPosition().set(var3x.getGlobalLocation());
         HashSet var4 = new HashSet();
         Iterator var5 = var3x.getPassengers().iterator();

         while(var5.hasNext()) {
            Entity var6 = (Entity)var5.next();
            var4.add(var6.getEntityId());
         }

         var2.getPassengers().retainAll(var4);
         var2.getPassengers().addAll(var4);
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
   public Map<String, MountRenderer.Mount> getSpawnQueue() {
      return this.spawnQueue;
   }

   @Generated
   public Map<String, MountRenderer.Mount> getRendered() {
      return this.rendered;
   }

   @Generated
   public Map<String, MountRenderer.Mount> getDestroyQueue() {
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

   public static class MountImpl implements MountRenderer.Mount {
      private final int pivotId;
      private final UUID pivotUuid;
      private final int mountId;
      private final UUID mountUuid;
      private final DataTracker<Vector3f> position = new UpdateDataTracker(new Vector3f(), Vector3f::set);
      private final DataTracker<Float> health = new DataTracker(20.0F);
      private final DataTracker<Float> maxHealth = new DataTracker(20.0F);
      private final DataTracker<Byte> yaw = new DataTracker((byte)0);
      private final CollectionDataTracker<Integer> passengers = new CollectionDataTracker(new HashSet());

      public MountImpl(int var1, UUID var2, int var3, UUID var4) {
         this.pivotId = var1;
         this.pivotUuid = var2;
         this.mountId = var3;
         this.mountUuid = var4;
      }

      @Generated
      public int getPivotId() {
         return this.pivotId;
      }

      @Generated
      public UUID getPivotUuid() {
         return this.pivotUuid;
      }

      @Generated
      public int getMountId() {
         return this.mountId;
      }

      @Generated
      public UUID getMountUuid() {
         return this.mountUuid;
      }

      @Generated
      public DataTracker<Vector3f> getPosition() {
         return this.position;
      }

      @Generated
      public DataTracker<Float> getHealth() {
         return this.health;
      }

      @Generated
      public DataTracker<Float> getMaxHealth() {
         return this.maxHealth;
      }

      @Generated
      public DataTracker<Byte> getYaw() {
         return this.yaw;
      }

      @Generated
      public CollectionDataTracker<Integer> getPassengers() {
         return this.passengers;
      }
   }
}
