package advancedplugins.pm2.cv.models.core.model.rpc;

import advancedplugins.pm2.cv.models.api.ModelAPI;
import advancedplugins.pm2.cv.models.api.lod.AnimationLODHandler;
import advancedplugins.pm2.cv.models.api.model.rpc.IModelContainer;
import advancedplugins.pm2.cv.models.api.model.rpc.IPosition;
import advancedplugins.pm2.cv.models.api.model.rpc.IVisualModel;
import advancedplugins.pm2.cv.models.api.model.rpc.animation.rootmotion.RootMotionHandler;
import advancedplugins.pm2.cv.models.api.model.rpc.entity.BaseEntity;
import advancedplugins.pm2.cv.models.api.model.rpc.entity.Hitbox;
import advancedplugins.pm2.cv.models.api.model.rpc.events.AddModelEvent;
import advancedplugins.pm2.cv.models.api.model.rpc.events.RemoveModelEvent;
import advancedplugins.pm2.cv.models.api.model.rpc.joint.behavior.GlobalBehaviorData;
import advancedplugins.pm2.cv.models.api.model.rpc.joint.behavior.JointAction;
import advancedplugins.pm2.cv.models.api.model.rpc.joint.behavior.JointActionType;
import advancedplugins.pm2.cv.models.api.model.rpc.joint.manager.MountData;
import advancedplugins.pm2.cv.models.api.model.rpc.joint.type.JointBehaviorTypes;
import advancedplugins.pm2.cv.models.api.nms.entity.wrapper.BodyRotationController;
import advancedplugins.pm2.cv.models.api.utils.data.io.SavedData;
import advancedplugins.pm2.cv.models.api.utils.data.tracker.DataTracker;
import advancedplugins.pm2.cv.models.api.utils.math.MathUtils;
import advancedplugins.pm2.cv.models.core.model.rpc.joint.manager.MountDataImpl;
import advancedplugins.pm2.cv.models.core.model.rpc.visual.VisualModel;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import java.util.Map.Entry;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;
import lombok.Generated;
import org.bukkit.entity.Entity;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector3f;
import org.joml.Vector3fc;

public class ModelContainer implements IModelContainer {
   private final BaseEntity<?> base;
   private final AnimationLODHandler animationLodHandler;
   private final Map<String, IVisualModel> models = Maps.newConcurrentMap();
   private final Map<JointActionType<?>, GlobalBehaviorData> data = Maps.newConcurrentMap();
   private final Map<IModelContainer.Phase, Map<UUID, Function<IModelContainer, Boolean>>> tickTasks = Maps.newConcurrentMap();
   private final boolean initialized;
   private final List<Runnable> queuedTask = new ArrayList();
   private final DataTracker<Float> trueYHeadRot = new DataTracker(MathUtils::isSimilar);
   private final DataTracker<Float> trueXHeadRot = new DataTracker(MathUtils::isSimilar);
   private final DataTracker<Float> trueYBodyRot = new DataTracker(MathUtils::isSimilar);
   private final RootMotionHandler rootMotionHandler = new RootMotionHandler(this);
   private final IPosition position;
   private int tick;
   private boolean isBaseEntityVisible = true;
   private boolean destroyed;
   private boolean removed;
   private int hurtTick = 0;
   private boolean isModelRotationLocked;
   private int rotationTick = -1;
   private boolean shouldSave = true;
   private IVisualModel lastHitboxOverride = null;

   public ModelContainer(@NotNull BaseEntity<?> var1, @Nullable Consumer<IModelContainer> var2) {
      this.base = var1;
      this.animationLodHandler = new AnimationLODHandler(this);
      this.registerSelf();
      if (var2 != null) {
         var2.accept(this);
      }

      this.position = new Position(new Vector3f());
      synchronized(this.queuedTask) {
         this.queuedTask.forEach(Runnable::run);
         this.initialized = true;
      }
   }

   public boolean tick() {
      if (!this.initialized) {
         return true;
      } else {
         if (this.hurtTick > 0) {
            --this.hurtTick;
         }

         if (!this.isModelRotationLocked && this.base.isAlive()) {
            BodyRotationController var1 = this.base.getBodyRotationController();
            var1.tick();
            this.trueYHeadRot.set(var1.getYHeadRot());
            this.trueXHeadRot.set(var1.getXHeadRot());
            this.trueYBodyRot.set(var1.getYBodyRot());
            if (this.rotationTick == -1) {
               this.getPosition().setYHeadRot((Float)this.trueYHeadRot.get());
               this.getPosition().setXHeadRot((Float)this.trueXHeadRot.get());
               this.getPosition().setYBodyRot((Float)this.trueYBodyRot.get());
               this.rotationTick = 0;
            }

            if (!this.base.isWalking()) {
               this.getPosition().setYBodyRot((Float)this.trueYBodyRot.get());
            }

            if (this.trueYHeadRot.isDirty() || this.trueXHeadRot.isDirty() || this.trueYBodyRot.isDirty()) {
               this.rotationTick = 3;
               this.trueYHeadRot.clearDirty();
               this.trueXHeadRot.clearDirty();
               this.trueYBodyRot.clearDirty();
            }

            if (this.rotationTick > 0) {
               this.getPosition().setYHeadRot(MathUtils.rotLerp(this.getPosition().getYHeadRot(), (Float)this.trueYHeadRot.get(), (double)(1.0F / (float)this.rotationTick)));
               this.getPosition().setXHeadRot(MathUtils.rotLerp(this.getPosition().getXHeadRot(), (Float)this.trueXHeadRot.get(), (double)(1.0F / (float)this.rotationTick)));
               this.getPosition().setYBodyRot(MathUtils.rotLerp(this.getPosition().getYBodyRot(), (Float)this.trueYBodyRot.get(), (double)(1.0F / (float)this.rotationTick)));
               --this.rotationTick;
            }
         }

         ++this.tick;
         if (!this.removed && !this.base.isRemoved()) {
            if (this.base.isAlive()) {
               Iterator var5 = this.models.values().iterator();

               while(var5.hasNext()) {
                  IVisualModel var6 = (IVisualModel)var5.next();
                  var6.tick();
               }

               return true;
            } else {
               boolean var4 = true;
               Iterator var2 = this.models.values().iterator();

               while(var2.hasNext()) {
                  IVisualModel var3 = (IVisualModel)var2.next();
                  var3.tick();
                  if (var4) {
                     var4 = var3.getAnimationHandler().hasFinishedAllAnimations();
                  }
               }

               this.base.setForcedAlive(!var4);
               return !var4 && !this.base.getData().getTracking().isEmpty();
            }
         } else {
            return false;
         }
      }
   }

   public void destroy() {
      this.destroyed = true;
      this.animationLodHandler.destroy();
      this.models.forEach((var0, var1) -> {
         var1.destroy();
      });
      this.models.clear();
   }

   public void markRemoved() {
      this.removed = true;
   }

   public void restore() {
      this.removed = false;
   }

   public void queuePostInitTask(Runnable var1) {
      synchronized(this.queuedTask) {
         if (this.initialized) {
            var1.run();
         } else {
            this.queuedTask.add(var1);
         }

      }
   }

   public void markHurt() {
      this.hurtTick = 10;
   }

   public boolean shouldBeSaved() {
      return this.shouldSave;
   }

   public void setSaved(boolean var1) {
      this.shouldSave = var1;
   }

   public boolean isGlowing() {
      return this.base.isGlowing();
   }

   public int getGlowColor() {
      return this.base.getGlowColor();
   }

   public Optional<IVisualModel> addModel(@NotNull IVisualModel var1, boolean var2) {
      if (this.isDestroyed()) {
         throw new IllegalStateException("Modeled Entity has been destroyed!");
      } else if (var1.getModeledEntity() == null && !var1.isRemoved()) {
         AddModelEvent var3 = new AddModelEvent(this, var1);
         var3.setOverrideHitbox(var2);
         ModelAPI.callEvent(var3);
         if (var3.isCancelled()) {
            return Optional.empty();
         } else {
            var1.setRemoved(false);
            var1.setModeledEntity(this);
            var1.generateModel();
            Optional var4 = this.removeModel(var1.getBlueprint().getName());
            this.models.put(var1.getBlueprint().getName(), var1);
            var1.getMountManager().ifPresent((var1x) -> {
               GlobalBehaviorData var2 = this.getMountData();
               if (((MountData)var2).getMainMountManager() == null) {
                  ((MountData)var2).setMainMountManager(var1x);
               }

            });
            if (var2) {
               if (this.lastHitboxOverride != null) {
                  this.lastHitboxOverride.setMainHitbox(false);
               }

               var1.setMainHitbox(true);
               this.lastHitboxOverride = var1;
               Hitbox var5 = var1.getBlueprint().getMainHitbox();
               Vector3fc var6 = var1.getScale();
               Hitbox var7 = new Hitbox(var5.getWidth() * (double)var6.x(), var5.getHeight() * (double)var6.y(), var5.getDepth() * (double)var6.z(), var5.getEyeHeight() * (double)var6.y());
               this.base.getData().setCullHitbox(var7);
               Object var8 = this.base.getOriginal();
               if (var8 instanceof Entity) {
                  Entity var9 = (Entity)var8;
                  Vector3fc var10 = var1.getHitboxScale();
                  Hitbox var11 = new Hitbox(var5.getWidth() * (double)var10.x(), var5.getHeight() * (double)var10.y(), var5.getDepth() * (double)var10.z(), var5.getEyeHeight() * (double)var10.y());
                  ModelAPI.getEntityHandler().setHitbox(var9, var11);
               }
            }

            return var4;
         }
      } else {
         throw new IllegalStateException("Active Model already belongs to a different Modeled Entity");
      }
   }

   public Optional<IVisualModel> removeModel(String var1) {
      assert !this.isDestroyed() : "Modeled Entity has been destroyed!";

      IVisualModel var2 = (IVisualModel)this.models.get(var1);
      if (var2 == null) {
         return Optional.empty();
      } else {
         RemoveModelEvent var3 = new RemoveModelEvent(this, var2);
         ModelAPI.callEvent(var3);
         if (var3.isCancelled()) {
            return Optional.empty();
         } else {
            this.models.remove(var1);
            var2.setRemoved(true);
            return Optional.of(var2);
         }
      }
   }

   public Optional<IVisualModel> getModel(@Nullable String var1) {
      return Optional.ofNullable(var1 == null ? null : (IVisualModel)this.models.get(var1));
   }

   public Map<String, IVisualModel> getModels() {
      return ImmutableMap.copyOf(this.models);
   }

   public <T extends JointAction> GlobalBehaviorData getOrCreateGlobalBehaviorData(JointActionType<T> var1, Supplier<GlobalBehaviorData> var2) {
      return (GlobalBehaviorData)this.data.computeIfAbsent(var1, (var1x) -> {
         return (GlobalBehaviorData)var2.get();
      });
   }

   public <T extends JointAction> GlobalBehaviorData getGlobalBehaviorData(JointActionType<T> var1) {
      return (GlobalBehaviorData)this.data.get(var1);
   }

   public <T extends JointAction> GlobalBehaviorData removeGlobalBehaviorData(JointActionType<T> var1) {
      return (GlobalBehaviorData)this.data.remove(var1);
   }

   public Map<JointActionType<?>, GlobalBehaviorData> getAllGlobalBehaviorData(JointActionType<?> var1) {
      return ImmutableMap.copyOf(this.data);
   }

   public <T extends GlobalBehaviorData & MountData> T getMountData() {
      GlobalBehaviorData var1 = this.getOrCreateGlobalBehaviorData(JointBehaviorTypes.MOUNT, MountDataImpl::new);
      if (var1 instanceof MountData) {
         return var1;
      } else {
         throw new ClassCastException("MountData expected, but got " + var1.getClass().getSimpleName());
      }
   }

   public UUID registerTickTask(IModelContainer.Phase var1, Function<IModelContainer, Boolean> var2) {
      UUID var3 = UUID.randomUUID();
      ((Map)this.tickTasks.computeIfAbsent(var1, (var0) -> {
         return Maps.newConcurrentMap();
      })).put(var3, var2);
      return var3;
   }

   public UUID registerTickTask(IModelContainer.Phase var1, Consumer<IModelContainer> var2) {
      return this.registerTickTask(var1, (var1x) -> {
         var2.accept(var1x);
         return false;
      });
   }

   public void removeTickTask(UUID var1) {
      Iterator var2 = this.tickTasks.entrySet().iterator();

      Entry var3;
      do {
         if (!var2.hasNext()) {
            return;
         }

         var3 = (Entry)var2.next();
      } while(((Map)var3.getValue()).remove(var1) == null);

   }

   public void runTickTasks(IModelContainer.Phase var1) {
      Map var2 = (Map)this.tickTasks.get(var1);
      if (var2 != null) {
         HashSet var3 = new HashSet();
         var2.forEach((var2x, var3x) -> {
            if ((Boolean)var3x.apply(this)) {
               var3.add(var2x);
            }

         });
         Objects.requireNonNull(var2);
         Objects.requireNonNull(var2);
         var3.forEach(var2::remove);
      }

   }

   public void save(SavedData var1) {
      var1.putBoolean("base_visible", this.isBaseEntityVisible());
      var1.putBoolean("rotation_locked", this.isModelRotationLocked());
      ArrayList var2 = new ArrayList();
      Iterator var3 = this.models.values().iterator();

      while(var3.hasNext()) {
         IVisualModel var4 = (IVisualModel)var3.next();
         Optional var5 = var4.save();
         Objects.requireNonNull(var2);
         Objects.requireNonNull(var2);
         var5.ifPresent(var2::add);
      }

      var1.putList("models", var2);
      this.base.save().ifPresent((var1x) -> {
         var1.putData("base_entity", var1x);
      });
   }

   public void load(SavedData var1) {
      this.setBaseEntityVisible(var1.getBoolean("base_visible"));
      this.setModelRotationLocked(var1.getBoolean("rotation_locked"));
      List var2 = var1.getList("models", SavedData.class);
      Iterator var3 = var2.iterator();

      while(var3.hasNext()) {
         SavedData var4 = (SavedData)var3.next();
         IVisualModel var5 = VisualModel.fromData(var4);
         if (var5 != null) {
            var5.setAutoRendererInitialization(false);
            this.addModel(var5, var5.isMainHitbox()).ifPresent(IVisualModel::destroy);
            var5.load(var4);
            var5.initializeRenderer();
         }
      }

      Optional var6 = var1.getData("base_entity");
      BaseEntity var7 = this.base;
      Objects.requireNonNull(var7);
      Objects.requireNonNull(var7);
      var6.ifPresent(var7::load);
   }

   public void setBaseEntityVisible(boolean var1) {
      if (this.isBaseEntityVisible() != var1) {
         this.isBaseEntityVisible = var1;
         this.base.setVisible(var1);
      }

   }

   public float getYHeadRot() {
      return this.getPosition().getYHeadRot();
   }

   public float getXHeadRot() {
      return this.getPosition().getXHeadRot();
   }

   public float getYBodyRot() {
      return this.getPosition().getYBodyRot();
   }

   @Generated
   public BaseEntity<?> getBase() {
      return this.base;
   }

   @Generated
   public AnimationLODHandler getAnimationLodHandler() {
      return this.animationLodHandler;
   }

   @Generated
   public Map<JointActionType<?>, GlobalBehaviorData> getData() {
      return this.data;
   }

   @Generated
   public Map<IModelContainer.Phase, Map<UUID, Function<IModelContainer, Boolean>>> getTickTasks() {
      return this.tickTasks;
   }

   @Generated
   public boolean isInitialized() {
      return this.initialized;
   }

   @Generated
   public List<Runnable> getQueuedTask() {
      return this.queuedTask;
   }

   @Generated
   public DataTracker<Float> getTrueYHeadRot() {
      return this.trueYHeadRot;
   }

   @Generated
   public DataTracker<Float> getTrueXHeadRot() {
      return this.trueXHeadRot;
   }

   @Generated
   public DataTracker<Float> getTrueYBodyRot() {
      return this.trueYBodyRot;
   }

   @Generated
   public RootMotionHandler getRootMotionHandler() {
      return this.rootMotionHandler;
   }

   @Generated
   public IPosition getPosition() {
      return this.position;
   }

   @Generated
   public int getTick() {
      return this.tick;
   }

   @Generated
   public boolean isBaseEntityVisible() {
      return this.isBaseEntityVisible;
   }

   @Generated
   public boolean isDestroyed() {
      return this.destroyed;
   }

   @Generated
   public boolean isRemoved() {
      return this.removed;
   }

   @Generated
   public int getHurtTick() {
      return this.hurtTick;
   }

   @Generated
   public boolean isModelRotationLocked() {
      return this.isModelRotationLocked;
   }

   @Generated
   public int getRotationTick() {
      return this.rotationTick;
   }

   @Generated
   public boolean isShouldSave() {
      return this.shouldSave;
   }

   @Generated
   public IVisualModel getLastHitboxOverride() {
      return this.lastHitboxOverride;
   }

   @Generated
   public void setTick(int var1) {
      this.tick = var1;
   }

   @Generated
   public void setDestroyed(boolean var1) {
      this.destroyed = var1;
   }

   @Generated
   public void setRemoved(boolean var1) {
      this.removed = var1;
   }

   @Generated
   public void setHurtTick(int var1) {
      this.hurtTick = var1;
   }

   @Generated
   public void setModelRotationLocked(boolean var1) {
      this.isModelRotationLocked = var1;
   }

   @Generated
   public void setRotationTick(int var1) {
      this.rotationTick = var1;
   }

   @Generated
   public void setShouldSave(boolean var1) {
      this.shouldSave = var1;
   }

   @Generated
   public void setLastHitboxOverride(IVisualModel var1) {
      this.lastHitboxOverride = var1;
   }
}
