package advancedplugins.pm2.cv.models.core.model.rpc.renderer;

import advancedplugins.pm2.cv.models.api.ModelAPI;
import advancedplugins.pm2.cv.models.api.model.rpc.IModelContainer;
import advancedplugins.pm2.cv.models.api.model.rpc.IVisualModel;
import advancedplugins.pm2.cv.models.api.model.rpc.entity.BaseEntity;
import advancedplugins.pm2.cv.models.api.model.rpc.generator.blueprint.ModelBlueprint;
import advancedplugins.pm2.cv.models.api.model.rpc.joint.IJoint;
import advancedplugins.pm2.cv.models.api.model.rpc.joint.JointItems;
import advancedplugins.pm2.cv.models.api.model.rpc.joint.behavior.JointAction;
import advancedplugins.pm2.cv.models.api.model.rpc.joint.manager.BehaviorManager;
import advancedplugins.pm2.cv.models.api.model.rpc.joint.renderer.BehaviorRenderer;
import advancedplugins.pm2.cv.models.api.model.rpc.joint.renderer.renderer.HeldItemRenderer;
import advancedplugins.pm2.cv.models.api.model.rpc.joint.type.HeldItem;
import advancedplugins.pm2.cv.models.api.model.rpc.joint.type.JointBehaviorTypes;
import advancedplugins.pm2.cv.models.api.model.rpc.renderer.DisplayRenderer;
import advancedplugins.pm2.cv.models.api.nms.RenderParsers;
import advancedplugins.pm2.cv.models.api.nms.entity.EntityHandler;
import advancedplugins.pm2.cv.models.api.utils.data.tracker.DataTracker;
import advancedplugins.pm2.cv.models.core.model.rpc.renderer.display.DisplayHitboxImpl;
import advancedplugins.pm2.cv.models.core.model.rpc.renderer.display.DisplayJointImpl;
import advancedplugins.pm2.cv.models.core.model.rpc.renderer.display.DisplayPivotImpl;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;
import lombok.Generated;
import org.bukkit.Location;
import org.bukkit.entity.ItemDisplay.ItemDisplayTransform;
import org.joml.Quaternionf;
import org.joml.Vector3f;
import org.joml.Vector3fc;

public class DisplayRendererImpl implements DisplayRenderer {
   private final IVisualModel visualModel;
   private final EntityHandler entityHandler;
   private final Map<String, DisplayRenderer.Joint> spawnQueue = new ConcurrentHashMap();
   private final Map<String, DisplayRenderer.Joint> rendered = new ConcurrentHashMap();
   private final Map<String, DisplayRenderer.Joint> destroyQueue = new ConcurrentHashMap();
   private final Set<UUID> fullUpdate = new HashSet();
   private DisplayPivotImpl pivot;
   private DisplayHitboxImpl hitbox;
   private boolean initialized;
   private boolean firstSpawned;

   public DisplayRendererImpl(IVisualModel var1) {
      this.visualModel = var1;
      this.entityHandler = ModelAPI.getEntityHandler();
   }

   public void init() {
      IModelContainer var1 = this.visualModel.getModeledEntity();
      BaseEntity var2 = var1.getBase();
      Location var3 = var2.getLocation();
      ModelBlueprint var4 = this.visualModel.getBlueprint();
      Vector3fc var5 = this.visualModel.getScale();
      Vector3fc var6 = this.visualModel.getHitboxScale();
      advancedplugins.pm2.cv.models.api.model.rpc.entity.Hitbox var7 = var4.getMainHitbox();
      float var8 = (float)var7.getEyeHeight() * var5.y();
      float var9 = (float)var7.getHeight() * var6.y();
      float var10 = (float)var7.getMaxWidth() * var6.x();
      this.pivot = new DisplayPivotImpl(this.entityHandler.getNextEntityId());
      this.pivot.updatePosition(var3, var8);
      this.pivot.getYaw().set((180.0F - var1.getYBodyRot()) * 0.017453292F);
      Iterator var11 = this.visualModel.getJoints().entrySet().iterator();

      while(var11.hasNext()) {
         Entry var12 = (Entry)var11.next();
         this.create((String)var12.getKey(), (IJoint)var12.getValue(), var8);
      }

      this.forBehaviorRenderer((var1x) -> {
         var1x.setModelRenderer(this);
         var1x.initialize();
      });
      this.hitbox = new DisplayHitboxImpl(this.entityHandler.getNextEntityId(), this.entityHandler.getNextEntityId(), this.entityHandler.getNextEntityId());
      this.hitbox.updatePosition(var3);
      this.hitbox.getHeight().set(var9);
      this.hitbox.getWidth().set(var10);
      this.hitbox.getShadowRadius().set(var4.getShadowRadius() * var5.x());
      this.hitbox.getHitboxVisible().set(this.visualModel.isHitboxVisible());
      this.hitbox.getShadowVisible().set(this.visualModel.isShadowVisible());
      ModelAPI.getInteractionTracker().setModelRelay(this.hitbox.getHitboxId(), this.visualModel);
      this.initialized = true;
      this.firstSpawned = true;
   }

   private void create(String var1, IJoint var2, float var3) {
      if (var2.isRenderer() && var2.getPivot() == null) {
         JointItems var4 = var2.getModels();
         DisplayJointImpl var5 = new DisplayJointImpl();
         var5.getGlowing().set(var2.isGlowing());
         var5.getGlowColor().set(var2.getGlowColor());
         var5.getBrightness().set(var2.getBrightness());
         var5.getVisibility().set(var2.isVisible());
         var5.getPosition().set(var2.getGlobalPosition().add(0.0F, -var3, 0.0F, new Vector3f()).rotateY((Float)this.pivot.getYaw().get()));
         var5.getLeftRotation().set(var2.getGlobalLeftRotation().rotateLocalY((Float)this.pivot.getYaw().get(), new Quaternionf()));
         var5.getScale().set(var2.getGlobalScale());
         var5.getRightRotation().set(var2.getGlobalRightRotation());
         var5.getDisplay().set(ItemDisplayTransform.HEAD);
         var5.getSnapshotHandler().recordSnapshot();
         var5.setJoint(var2);
         this.initializeSpecialBehaviorRender(var2, var5);
         this.spawnQueue.put(var1, var5);
         this.destroyQueue.remove(var1);
      }

   }

   public List<DisplayRenderer.Joint> getAllJoints() {
      ArrayList var1 = new ArrayList(this.rendered.values());
      var1.addAll(this.spawnQueue.values());
      var1.addAll(this.destroyQueue.values());
      return var1;
   }

   private void initializeSpecialBehaviorRender(IJoint var1, DisplayRenderer.Joint var2) {
      var1.getJointAction(JointBehaviorTypes.ITEM).ifPresent((var1x) -> {
         var2.getDisplay().set(((HeldItem)var1x).getDisplay());
      });
      var1.getJointAction(JointBehaviorTypes.PLAYER_LIMB).ifPresent((var1x) -> {
         var2.getDisplay().set(ItemDisplayTransform.THIRDPERSON_RIGHTHAND);
      });
   }

   public void readModelData() {
      if (this.initialized) {
         IModelContainer var1 = this.visualModel.getModeledEntity();
         BaseEntity var2 = var1.getBase();
         Location var3 = var2.getLocation();
         ModelBlueprint var4 = this.visualModel.getBlueprint();
         Vector3fc var5 = this.visualModel.getScale();
         Vector3fc var6 = this.visualModel.getHitboxScale();
         advancedplugins.pm2.cv.models.api.model.rpc.entity.Hitbox var7 = var4.getMainHitbox();
         float var8 = (float)var7.getEyeHeight() * var5.y();
         float var9 = (float)var7.getHeight() * var6.y();
         float var10 = (float)var7.getMaxWidth() * var6.x();
         this.pivot.updatePosition(var3, var8);
         this.pivot.getYaw().set((180.0F - var1.getYBodyRot()) * 0.017453292F);
         this.destroyQueue.putAll(this.rendered);
         Iterator var11 = this.visualModel.getJoints().entrySet().iterator();

         while(var11.hasNext()) {
            Entry var12 = (Entry)var11.next();
            DisplayRenderer.Joint var13 = (DisplayRenderer.Joint)this.getQueued((String)var12.getKey());
            if (var13 == null) {
               this.create((String)var12.getKey(), (IJoint)var12.getValue(), var8);
            } else {
               this.read((String)var12.getKey(), var13, (IJoint)var12.getValue(), var8);
            }
         }

         this.destroyQueue.values().iterator().forEachRemaining((var1x) -> {
            var1x.getModel().values().iterator().forEachRemaining((var1) -> {
               this.pivot.getPassengers().remove(var1.getId());
            });
         });
         this.forBehaviorRenderer(BehaviorRenderer::readJointData);
         this.hitbox.updatePosition(var3);
         this.hitbox.getHeight().set(var9);
         this.hitbox.getWidth().set(var10);
         this.hitbox.getShadowRadius().set(var4.getShadowRadius() * var5.x());
         this.hitbox.getHitboxVisible().set(this.visualModel.isHitboxVisible());
         this.hitbox.getShadowVisible().set(this.visualModel.isShadowVisible());
      }

   }

   private void read(String var1, DisplayRenderer.Joint var2, IJoint var3, float var4) {
      DataTracker var5 = var2.getRender();
      var5.set(!var3.isEffectivelyInvisible());
      if ((Boolean)var5.get() || var5.isDirty()) {
         var2.getStep().set(var3.pollModelScaleChanged() || var3.shouldStep() || var5.isDirty());
         var2.getPosition().set(var3.getGlobalPosition().add(0.0F, -var4, 0.0F, new Vector3f()).rotateY((Float)this.pivot.getYaw().get()));
         var2.getLeftRotation().set(var3.getGlobalLeftRotation().rotateLocalY((Float)this.pivot.getYaw().get(), new Quaternionf()));
         var2.getScale().set(var3.getGlobalScale());
         var2.getRightRotation().set(var3.getGlobalRightRotation());
         var2.getVisibility().set(var3.isVisible());
         var2.getGlowing().set(var3.isGlowing());
         var2.getGlowColor().set(var3.getGlowColor());
         var2.getBrightness().set(var3.getBrightness());
         var2.getSnapshotHandler().recordSnapshot();
         JointItems var6 = var3.getModels();
         if (var6.isDirty()) {
            var6.clearDirty();
            var2.updateJointData(this.entityHandler, this.pivot, var6);
         }

         this.updateSpecialBehaviorRender(var3, var2);
      }

      var5.clearDirty();
      this.destroyQueue.remove(var1);
   }

   private void updateSpecialBehaviorRender(IJoint var1, DisplayRenderer.Joint var2) {
      var1.getJointAction(JointBehaviorTypes.ITEM).ifPresent((var1x) -> {
         var2.getDisplay().set(((HeldItem)var1x).getDisplay());
      });
   }

   public void dispatch(RenderParsers var1) {
      if (this.initialized) {
         this.forManagers(BehaviorManager::preJointRender);
         this.forBehavior(JointAction::preRender);
         Set var2 = this.destroyQueue.keySet();
         Map var3 = this.rendered;
         Objects.requireNonNull(var3);
         Objects.requireNonNull(var3);
         var2.forEach(var3::remove);
         var1.getModelParser(this).dispatch(this);
         this.rendered.putAll(this.spawnQueue);
         this.spawnQueue.clear();
         this.destroyQueue.clear();
         this.forBehaviorRenderer((var1x) -> {
            var1x.sendToClient(var1);
         });
         this.forBehavior(JointAction::onRender);
         this.forBehavior(JointAction::postRender);
         this.forManagers(BehaviorManager::postJointRenderer);
      }

   }

   public void dispose(RenderParsers var1) {
      if (this.initialized) {
         this.forBehaviorRenderer((var1x) -> {
            var1x.destroy(var1);
         });
         var1.getModelParser(this).dispose(this);
         ModelAPI.getInteractionTracker().removeModelRelay(this.hitbox.getHitboxId());
      }

   }

   public boolean pollFirstSpawn() {
      if (!this.firstSpawned) {
         return false;
      } else {
         this.firstSpawned = false;
         return true;
      }
   }

   private void forManagers(Consumer<BehaviorManager<?>> var1) {
      Iterator var2 = this.visualModel.getBehaviorManagers().values().iterator();

      while(var2.hasNext()) {
         BehaviorManager var3 = (BehaviorManager)var2.next();
         var1.accept(var3);
      }

   }

   private void forBehavior(Consumer<JointAction> var1) {
      Iterator var2 = this.visualModel.getBlueprint().getJoints().keySet().iterator();

      while(var2.hasNext()) {
         String var3 = (String)var2.next();
         Optional var4 = this.visualModel.getJoint(var3);
         var4.ifPresent((var1x) -> {
            var1x.getImmutableJointActions().values().forEach(var1);
         });
      }

   }

   private void forBehaviorRenderer(Consumer<BehaviorRenderer> var1) {
      Iterator var2 = this.visualModel.getBehaviorRenderers().values().iterator();

      while(var2.hasNext()) {
         BehaviorRenderer var3 = (BehaviorRenderer)var2.next();
         if (!(var3 instanceof HeldItemRenderer)) {
            var1.accept(var3);
         }
      }

   }

   public boolean tick() {
      return true;
   }

   public int getTick() {
      return this.visualModel.getModeledEntity().getTick();
   }

   public void pushUpdate(UUID var1) {
      this.fullUpdate.add(var1);
   }

   public boolean pollUpdate(UUID var1) {
      return this.fullUpdate.remove(var1);
   }

   public IVisualModel getActiveModel() {
      return this.visualModel;
   }

   public boolean isReady() {
      return this.initialized;
   }

   @Generated
   public IVisualModel getVisualModel() {
      return this.visualModel;
   }

   @Generated
   public EntityHandler getEntityHandler() {
      return this.entityHandler;
   }

   @Generated
   public Map<String, DisplayRenderer.Joint> getSpawnQueue() {
      return this.spawnQueue;
   }

   @Generated
   public Map<String, DisplayRenderer.Joint> getRendered() {
      return this.rendered;
   }

   @Generated
   public Map<String, DisplayRenderer.Joint> getDestroyQueue() {
      return this.destroyQueue;
   }

   @Generated
   public Set<UUID> getFullUpdate() {
      return this.fullUpdate;
   }

   @Generated
   public DisplayPivotImpl getPivot() {
      return this.pivot;
   }

   @Generated
   public DisplayHitboxImpl getHitbox() {
      return this.hitbox;
   }

   @Generated
   public boolean isInitialized() {
      return this.initialized;
   }

   @Generated
   public boolean isFirstSpawned() {
      return this.firstSpawned;
   }

   @Generated
   public void setPivot(DisplayPivotImpl var1) {
      this.pivot = var1;
   }

   @Generated
   public void setHitbox(DisplayHitboxImpl var1) {
      this.hitbox = var1;
   }

   @Generated
   public void setInitialized(boolean var1) {
      this.initialized = var1;
   }

   @Generated
   public void setFirstSpawned(boolean var1) {
      this.firstSpawned = var1;
   }
}
