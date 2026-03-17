package advancedplugins.pm2.cv.models.core.model.rpc.joint.renderer;

import advancedplugins.pm2.cv.models.api.model.rpc.IVisualModel;
import advancedplugins.pm2.cv.models.api.model.rpc.joint.IJoint;
import advancedplugins.pm2.cv.models.api.model.rpc.joint.behavior.JointAction;
import advancedplugins.pm2.cv.models.api.model.rpc.joint.renderer.renderer.SegmentRenderer;
import advancedplugins.pm2.cv.models.api.model.rpc.joint.type.HeldItem;
import advancedplugins.pm2.cv.models.api.model.rpc.joint.type.JointBehaviorTypes;
import advancedplugins.pm2.cv.models.api.model.rpc.joint.type.Segment;
import advancedplugins.pm2.cv.models.api.nms.RenderParsers;
import advancedplugins.pm2.cv.models.api.nms.entity.EntityHandler;
import advancedplugins.pm2.cv.models.api.utils.data.tracker.CollectionDataTracker;
import advancedplugins.pm2.cv.models.api.utils.data.tracker.DataTracker;
import advancedplugins.pm2.cv.models.api.utils.data.tracker.UpdateDataTracker;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import lombok.Generated;
import org.bukkit.entity.ItemDisplay.ItemDisplayTransform;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.Nullable;
import org.joml.Quaternionf;
import org.joml.Vector3f;

public class SegmentRendererImpl extends AbstractBehaviorRenderer implements SegmentRenderer {
   private final EntityHandler entityHandler;
   private final Map<String, SegmentRenderer.Pivot> spawnQueue = new ConcurrentHashMap();
   private final Map<String, SegmentRenderer.Pivot> rendered = new ConcurrentHashMap();
   private final Map<String, SegmentRenderer.Pivot> destroyQueue = new ConcurrentHashMap();
   private final Map<String, IJoint> unhandled = new ConcurrentHashMap();
   private final Map<String, SegmentRenderer.Joint> joints = new ConcurrentHashMap();
   private boolean initialized;

   public SegmentRendererImpl(IVisualModel var1) {
      super(var1);
      this.entityHandler = this.nmsHandler.getEntityHandler();
   }

   public void initialize() {
      Iterator var1 = this.visualModel.getJoints().entrySet().iterator();

      Entry var2;
      while(var1.hasNext()) {
         var2 = (Entry)var1.next();
         this.createPivotOrJoint((String)var2.getKey(), (IJoint)var2.getValue(), this.unhandled);
      }

      var1 = this.unhandled.entrySet().iterator();

      while(var1.hasNext()) {
         var2 = (Entry)var1.next();
         this.createJoint((String)var2.getKey(), (IJoint)var2.getValue(), (Map)null);
      }

      this.unhandled.clear();
      this.initialized = true;
   }

   private void createPivotOrJoint(String var1, IJoint var2, Map<String, IJoint> var3) {
      if (!this.createPivot(var1, var2)) {
         this.createJoint(var1, var2, var3);
      }

   }

   private boolean createPivot(String var1, IJoint var2) {
      Optional var3 = var2.getJointAction(JointBehaviorTypes.SEGMENT);
      if (var3.isEmpty()) {
         return false;
      } else {
         JointAction var4 = (JointAction)var3.get();
         SegmentRendererImpl.PivotImpl var5 = new SegmentRendererImpl.PivotImpl(this.entityHandler.getNextEntityId());
         var5.getPosition().set(((Segment)var4).getWorldLocation());
         var5.getYaw().set((180.0F - var2.getYaw()) * 0.017453292F);
         this.spawnQueue.put(var1, var5);
         this.destroyQueue.remove(var1);
         return true;
      }
   }

   private void createJoint(String var1, IJoint var2, @Nullable Map<String, IJoint> var3) {
      if (var2.isRenderer() && var2.getPivot() != null) {
         SegmentRendererImpl.PivotImpl var4 = this.getPivot(var2.getPivot().getUniqueJointId());
         if (var4 == null) {
            if (var3 != null) {
               var3.put(var1, var2);
            }
         } else {
            SegmentRendererImpl.JointImpl var5 = new SegmentRendererImpl.JointImpl(this.entityHandler.getNextEntityId(), var4);
            var5.getGlowing().set(var2.isGlowing());
            var5.getGlowColor().set(var2.getGlowColor());
            var5.getBrightness().set(var2.getBrightness());
            var5.getVisibility().set(var2.isVisible());
            var5.getModel().set(var2.getModel());
            var5.getPosition().set(var2.getGlobalPosition().rotateY((Float)var4.yaw.get(), new Vector3f()));
            var5.getLeftRotation().set(var2.getGlobalLeftRotation().rotateLocalY((Float)var4.yaw.get(), new Quaternionf()));
            var5.getScale().set(var2.getGlobalScale());
            var5.getRightRotation().set(var2.getGlobalRightRotation());
            this.initializeSpecialBehaviorRender(var2, var5);
            this.joints.put(var1, var5);
            var4.spawnQueue.put(var1, var5);
            var4.destroyQueue.remove(var1);
            var4.passengers.add(var5.id);
         }
      }

   }

   private void initializeSpecialBehaviorRender(IJoint var1, SegmentRenderer.Joint var2) {
      var1.getJointAction(JointBehaviorTypes.ITEM).ifPresent((var1x) -> {
         var2.getDisplay().set(((HeldItem)var1x).getDisplay());
      });
      var1.getJointAction(JointBehaviorTypes.PLAYER_LIMB).ifPresent((var1x) -> {
         var2.getDisplay().set(ItemDisplayTransform.THIRDPERSON_RIGHTHAND);
      });
   }

   @Nullable
   private SegmentRendererImpl.PivotImpl getPivot(String var1) {
      SegmentRenderer.Pivot var2 = (SegmentRenderer.Pivot)this.getQueued(var1);
      if (var2 instanceof SegmentRendererImpl.PivotImpl) {
         SegmentRendererImpl.PivotImpl var3 = (SegmentRendererImpl.PivotImpl)var2;
         return var3;
      } else {
         return null;
      }
   }

   public void readJointData() {
      if (this.initialized) {
         this.destroyQueue.putAll(this.rendered);
         ArrayList var1 = new ArrayList();
         ArrayList var2 = new ArrayList();
         Iterator var3 = this.visualModel.getJoints().entrySet().iterator();

         Entry var4;
         while(var3.hasNext()) {
            var4 = (Entry)var3.next();
            String var5 = (String)var4.getKey();
            IJoint var6 = (IJoint)var4.getValue();
            SegmentRenderer.Pivot var7 = (SegmentRenderer.Pivot)this.getQueued(var5);
            if (var7 != null) {
               this.readPivotJoint(var5, var7, var6);
            } else {
               SegmentRenderer.Joint var8 = (SegmentRenderer.Joint)this.joints.get(var5);
               if (var8 != null) {
                  var1.add(() -> {
                     this.readDisplayJoint(var5, var8, var6);
                  });
               } else {
                  var2.add(() -> {
                     this.createPivotOrJoint(var5, var6, this.unhandled);
                  });
               }
            }
         }

         var1.forEach(Runnable::run);
         var2.forEach(Runnable::run);
         var3 = this.unhandled.entrySet().iterator();

         while(var3.hasNext()) {
            var4 = (Entry)var3.next();
            this.createJoint((String)var4.getKey(), (IJoint)var4.getValue(), (Map)null);
         }

         this.unhandled.clear();
         this.spawnQueue.forEach((var0, var1x) -> {
            var1x.getDestroyQueue().forEach((var1, var2) -> {
               var1x.getPassengers().remove(var2.getId());
            });
         });
         this.rendered.forEach((var0, var1x) -> {
            var1x.getDestroyQueue().forEach((var1, var2) -> {
               var1x.getPassengers().remove(var2.getId());
            });
         });
      }
   }

   private void readPivotJoint(String var1, SegmentRenderer.Pivot var2, IJoint var3) {
      Optional var4 = var3.getJointAction(JointBehaviorTypes.SEGMENT);
      var4.ifPresent((var4x) -> {
         var2.getPosition().set(var4x.getWorldLocation());
         var2.getYaw().set((180.0F - var3.getYaw()) * 0.017453292F);
         this.destroyQueue.remove(var1);
      });
   }

   private void readDisplayJoint(String var1, SegmentRenderer.Joint var2, IJoint var3) {
      DataTracker var4 = var2.getRender();
      var4.set(!var3.isEffectivelyInvisible());
      SegmentRenderer.Pivot var5 = var2.getPivot();
      if ((Boolean)var4.get() || var4.isDirty()) {
         float var6 = (Float)var5.getYaw().get();
         var2.getStep().set(var3.pollModelScaleChanged() || var3.shouldStep() || var4.isDirty());
         var2.getPosition().set(var3.getGlobalPosition().rotateY(var6));
         var2.getLeftRotation().set(var3.getGlobalLeftRotation().rotateLocalY(var6, new Quaternionf()));
         var2.getScale().set(var3.getGlobalScale());
         var2.getRightRotation().set(var3.getGlobalRightRotation());
         var2.getVisibility().set(var3.isVisible());
         var2.getGlowing().set(var3.isGlowing());
         var2.getGlowColor().set(var3.getGlowColor());
         var2.getBrightness().set(var3.getBrightness());
         if (var3.getModelTracker().isDirty()) {
            var3.getModelTracker().clearDirty();
            var2.getModel().set(var3.getModel());
            var2.getModel().markDirty();
         }

         this.updateSpecialBehaviorRender(var3, var2);
      }

      var4.clearDirty();
      var5.getDestroyQueue().remove(var1);
   }

   private void updateSpecialBehaviorRender(IJoint var1, SegmentRenderer.Joint var2) {
      var1.getJointAction(JointBehaviorTypes.ITEM).ifPresent((var1x) -> {
         var2.getDisplay().set(((HeldItem)var1x).getDisplay());
      });
   }

   public void sendToClient(RenderParsers var1) {
      if (this.initialized) {
         this.destroyQueue.keySet().forEach((var1x) -> {
            SegmentRenderer.Pivot var2 = (SegmentRenderer.Pivot)this.rendered.remove(var1x);
            if (var2 != null) {
               var2.getSpawnQueue().forEach((var1, var2x) -> {
                  this.joints.remove(var1);
               });
               var2.getRendered().forEach((var1, var2x) -> {
                  this.joints.remove(var1);
               });
               var2.getDestroyQueue().forEach((var1, var2x) -> {
                  this.joints.remove(var1);
               });
            }

         });
         var1.getBehaviorParser(this).sendToClients(this);
         this.rendered.putAll(this.spawnQueue);
         this.spawnQueue.clear();
         this.destroyQueue.clear();
         this.rendered.forEach((var1x, var2) -> {
            var2.getDestroyQueue().forEach((var2x, var3) -> {
               var2.getRendered().remove(var2x);
               this.joints.remove(var2x);
            });
            var2.getRendered().putAll(var2.getSpawnQueue());
            var2.getSpawnQueue().clear();
         });
      }

   }

   public void destroy(RenderParsers var1) {
      if (this.initialized) {
         var1.getBehaviorParser(this).destroy(this);
      }

   }

   @Generated
   public Map<String, SegmentRenderer.Pivot> getSpawnQueue() {
      return this.spawnQueue;
   }

   @Generated
   public Map<String, SegmentRenderer.Pivot> getRendered() {
      return this.rendered;
   }

   @Generated
   public Map<String, SegmentRenderer.Pivot> getDestroyQueue() {
      return this.destroyQueue;
   }

   public static class PivotImpl implements SegmentRenderer.Pivot {
      private final Map<String, SegmentRenderer.Joint> spawnQueue = new ConcurrentHashMap();
      private final Map<String, SegmentRenderer.Joint> rendered = new ConcurrentHashMap();
      private final Map<String, SegmentRenderer.Joint> destroyQueue = new ConcurrentHashMap();
      private final int id;
      private final UUID uuid = UUID.randomUUID();
      private final DataTracker<Vector3f> position = new UpdateDataTracker(new Vector3f(), Vector3f::set);
      private final DataTracker<Float> yaw = new DataTracker();
      private final CollectionDataTracker<Integer> passengers = new CollectionDataTracker(new HashSet());

      public PivotImpl(int var1) {
         this.id = var1;
      }

      public void clearDirty() {
         this.yaw.clearDirty();
         this.passengers.clearDirty();
      }

      @Generated
      public Map<String, SegmentRenderer.Joint> getSpawnQueue() {
         return this.spawnQueue;
      }

      @Generated
      public Map<String, SegmentRenderer.Joint> getRendered() {
         return this.rendered;
      }

      @Generated
      public Map<String, SegmentRenderer.Joint> getDestroyQueue() {
         return this.destroyQueue;
      }

      @Generated
      public int getId() {
         return this.id;
      }

      @Generated
      public UUID getUuid() {
         return this.uuid;
      }

      @Generated
      public DataTracker<Vector3f> getPosition() {
         return this.position;
      }

      @Generated
      public DataTracker<Float> getYaw() {
         return this.yaw;
      }

      @Generated
      public CollectionDataTracker<Integer> getPassengers() {
         return this.passengers;
      }
   }

   public static class JointImpl implements SegmentRenderer.Joint {
      private final int id;
      private final SegmentRenderer.Pivot pivot;
      private final UUID uuid = UUID.randomUUID();
      private final DataTracker<Boolean> render = new DataTracker(true);
      private final DataTracker<Boolean> step = new DataTracker(false);
      private final DataTracker<Vector3f> position = new UpdateDataTracker(new Vector3f(), Vector3f::set);
      private final DataTracker<Quaternionf> leftRotation = new UpdateDataTracker(new Quaternionf(), Quaternionf::set);
      private final DataTracker<Vector3f> scale = new UpdateDataTracker(new Vector3f(), Vector3f::set);
      private final DataTracker<Quaternionf> rightRotation = new UpdateDataTracker(new Quaternionf(), Quaternionf::set);
      private final DataTracker<ItemStack> model = new DataTracker();
      private final DataTracker<ItemDisplayTransform> display;
      private final DataTracker<Boolean> visibility;
      private final DataTracker<Boolean> glowing;
      private final DataTracker<Integer> glowColor;
      private final DataTracker<Integer> brightness;

      public JointImpl(int var1, SegmentRenderer.Pivot var2) {
         this.display = new DataTracker(ItemDisplayTransform.NONE);
         this.visibility = new DataTracker(true);
         this.glowing = new DataTracker(false);
         this.glowColor = new DataTracker(-1);
         this.brightness = new DataTracker(-1);
         this.id = var1;
         this.pivot = var2;
      }

      @Generated
      public int getId() {
         return this.id;
      }

      @Generated
      public SegmentRenderer.Pivot getPivot() {
         return this.pivot;
      }

      @Generated
      public UUID getUuid() {
         return this.uuid;
      }

      @Generated
      public DataTracker<Boolean> getRender() {
         return this.render;
      }

      @Generated
      public DataTracker<Boolean> getStep() {
         return this.step;
      }

      @Generated
      public DataTracker<Vector3f> getPosition() {
         return this.position;
      }

      @Generated
      public DataTracker<Quaternionf> getLeftRotation() {
         return this.leftRotation;
      }

      @Generated
      public DataTracker<Vector3f> getScale() {
         return this.scale;
      }

      @Generated
      public DataTracker<Quaternionf> getRightRotation() {
         return this.rightRotation;
      }

      @Generated
      public DataTracker<ItemStack> getModel() {
         return this.model;
      }

      @Generated
      public DataTracker<ItemDisplayTransform> getDisplay() {
         return this.display;
      }

      @Generated
      public DataTracker<Boolean> getVisibility() {
         return this.visibility;
      }

      @Generated
      public DataTracker<Boolean> getGlowing() {
         return this.glowing;
      }

      @Generated
      public DataTracker<Integer> getGlowColor() {
         return this.glowColor;
      }

      @Generated
      public DataTracker<Integer> getBrightness() {
         return this.brightness;
      }
   }
}
