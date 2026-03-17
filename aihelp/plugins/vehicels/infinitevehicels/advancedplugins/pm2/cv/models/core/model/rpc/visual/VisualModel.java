package advancedplugins.pm2.cv.models.core.model.rpc.visual;

import advancedplugins.pm2.cv.models.api.ModelAPI;
import advancedplugins.pm2.cv.models.api.model.rpc.IModelContainer;
import advancedplugins.pm2.cv.models.api.model.rpc.IVisualModel;
import advancedplugins.pm2.cv.models.api.model.rpc.animation.handler.AnimationHandler;
import advancedplugins.pm2.cv.models.api.model.rpc.entity.Hitbox;
import advancedplugins.pm2.cv.models.api.model.rpc.entity.data.IEntityData;
import advancedplugins.pm2.cv.models.api.model.rpc.generator.blueprint.BlueprintJoint;
import advancedplugins.pm2.cv.models.api.model.rpc.generator.blueprint.ModelBlueprint;
import advancedplugins.pm2.cv.models.api.model.rpc.joint.IJoint;
import advancedplugins.pm2.cv.models.api.model.rpc.joint.behavior.JointAction;
import advancedplugins.pm2.cv.models.api.model.rpc.joint.behavior.JointActionType;
import advancedplugins.pm2.cv.models.api.model.rpc.joint.manager.BehaviorManager;
import advancedplugins.pm2.cv.models.api.model.rpc.joint.renderer.BehaviorRenderer;
import advancedplugins.pm2.cv.models.api.model.rpc.renderer.ModelRenderer;
import advancedplugins.pm2.cv.models.api.nms.entity.EntityHandler;
import advancedplugins.pm2.cv.models.api.utils.callback.ExecutionCallback;
import advancedplugins.pm2.cv.models.api.utils.config.ConfigProperty;
import advancedplugins.pm2.cv.models.api.utils.data.io.SavedData;
import advancedplugins.pm2.cv.models.api.utils.logger.LogUtil;
import advancedplugins.pm2.cv.models.core.animation.handler.PriorityHandler;
import advancedplugins.pm2.cv.models.core.animation.handler.StateMachineHandler;
import advancedplugins.pm2.cv.models.core.model.rpc.joint.JointArchive;
import advancedplugins.pm2.cv.models.core.model.rpc.joint.JointImpl;
import advancedplugins.pm2.cv.models.core.model.rpc.renderer.DisplayRendererImpl;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;
import java.util.function.Function;
import lombok.Generated;
import org.bukkit.Color;
import org.bukkit.entity.Entity;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector3f;
import org.joml.Vector3fc;

public class VisualModel implements IVisualModel {
   private final ModelBlueprint blueprint;
   private final ModelRenderer modelRenderer;
   private final AnimationHandler animationHandler;
   private final JointArchive joints = new JointArchive();
   private final Map<String, IJoint> roots = Maps.newConcurrentMap();
   private final Map<JointActionType<?>, BehaviorManager<?>> behaviorManagers = new LinkedHashMap();
   private final Map<JointActionType<?>, BehaviorRenderer> behaviorRenderers = new LinkedHashMap();
   private final Vector3f scale = new Vector3f(1.0F, 1.0F, 1.0F);
   private final Vector3f hitboxScale = new Vector3f(1.0F, 1.0F, 1.0F);
   private final ExecutionCallback<IVisualModel.Scale> scaleExecutionCallback = new ExecutionCallback((var0) -> {
      return (var1, var2) -> {
         var0.forEach((var3) -> {
            var3.onScale(var1, var2);
         });
      };
   });
   private IModelContainer modelContainer;
   private boolean mainHitbox;
   private boolean generated;
   private boolean destroyed;
   private boolean removed;
   private boolean autoRendererInitialization = true;
   private boolean hitboxVisible = true;
   private boolean shadowVisible = true;
   private boolean canHurt = true;
   private Color defaultTint = Color.fromRGB(16777215);
   private Color damageTint = Color.fromRGB(16737894);
   private boolean wasMarkedHurt;
   private Boolean glowing;
   private Integer glowColor;
   private int blockLight = -1;
   private int skyLight = -1;
   private boolean lockPitch;
   private boolean lockYaw;

   public VisualModel(@NotNull ModelBlueprint var1, @Nullable Function<IVisualModel, ModelRenderer> var2, @Nullable Function<IVisualModel, AnimationHandler> var3) {
      this.blueprint = var1;
      Object var4 = var2 == null ? new DisplayRendererImpl(this) : (ModelRenderer)var2.apply(this);
      this.modelRenderer = (ModelRenderer)(var4 == null ? new DisplayRendererImpl(this) : var4);
      AnimationHandler var5 = var3 == null ? createDefaultHandler(this) : (AnimationHandler)var3.apply(this);
      this.animationHandler = var5 == null ? createDefaultHandler(this) : var5;
   }

   private static AnimationHandler createDefaultHandler(IVisualModel var0) {
      return (AnimationHandler)(ConfigProperty.USE_STATE_MACHINE.getBoolean() ? new StateMachineHandler(var0) : new PriorityHandler(var0));
   }

   public static IVisualModel fromData(SavedData var0) {
      try {
         return ModelAPI.create((String)var0.getString("blueprint"), (Function)null, (Function)((var1) -> {
            var1.setMainHitbox(var0.getBoolean("main_hitbox"));
            return (AnimationHandler)var0.getData("animation_handler").map((var1x) -> {
               return ModelAPI.getAnimationHandlerArchive().createHandler(var1, var1x);
            }).orElse((Object)null);
         }));
      } catch (Exception var2) {
         var2.printStackTrace();
         return null;
      }
   }

   public Map<JointActionType<?>, BehaviorManager<?>> getBehaviorManagers() {
      synchronized(this.behaviorManagers) {
         return ImmutableMap.copyOf(this.behaviorManagers);
      }
   }

   public Map<JointActionType<?>, BehaviorRenderer> getBehaviorRenderers() {
      synchronized(this.behaviorRenderers) {
         return ImmutableMap.copyOf(this.behaviorRenderers);
      }
   }

   public Vector3fc getScale() {
      return this.scale;
   }

   public void setScale(double var1) {
      this.scale.set(var1);
      ((IVisualModel.Scale)this.scaleExecutionCallback.invoker()).onScale(this, var1);
      if (this.mainHitbox && this.modelContainer != null) {
         Vector3fc var3 = this.getScale();
         Hitbox var4 = this.blueprint.getMainHitbox();
         Hitbox var5 = new Hitbox(var4.getWidth() * (double)var3.x(), var4.getHeight() * (double)var3.y(), var4.getDepth() * (double)var3.z(), var4.getEyeHeight() * (double)var3.y());
         this.modelContainer.getBase().getData().setCullHitbox(var5);
      }

   }

   public Vector3fc getHitboxScale() {
      return this.hitboxScale;
   }

   public void setHitboxScale(double var1) {
      this.hitboxScale.set(var1);
      if (this.mainHitbox && this.modelContainer != null) {
         Object var3 = this.modelContainer.getBase().getOriginal();
         if (var3 instanceof Entity) {
            Entity var4 = (Entity)var3;
            EntityHandler var5 = ModelAPI.getEntityHandler();
            Vector3fc var6 = this.getHitboxScale();
            Hitbox var7 = this.blueprint.getMainHitbox();
            Hitbox var8 = new Hitbox(var7.getWidth() * (double)var6.x(), var7.getHeight() * (double)var6.y(), var7.getDepth() * (double)var6.z(), var7.getEyeHeight() * (double)var6.y());
            var5.setHitbox(var4, var8);
         }
      }

   }

   public boolean tick() {
      if (!this.isDestroyed()) {
         if (this.modelContainer.getBase().getData().hasTracking()) {
            this.animationHandler.prepare();
            this.forManagers(BehaviorManager::preJointTick);
            this.forJoints(IJoint::tick);
            this.forManagers(BehaviorManager::postJointTick);
            this.forManagers(BehaviorManager::preScriptTick);
            this.animationHandler.tickGlobal();
            this.forManagers(BehaviorManager::postScriptTick);
            this.modelRenderer.readModelData();
            this.wasMarkedHurt = this.isMarkedHurt();
         } else {
            this.animationHandler.prepare();
            this.forJoints(IJoint::lazyTick);
            this.animationHandler.tickGlobal();
         }
      }

      return true;
   }

   public void destroy() {
      this.forJoints(IJoint::destroy);
      this.forManagers(BehaviorManager::onDestroy);
      this.joints.clear();
      this.getData().markModelGlowing(this, false);
      this.modelRenderer.dispose(ModelAPI.getNMSHandler().getGlobalParsers());
      this.destroyed = true;
   }

   public void initializeRenderer() {
      if (!this.modelRenderer.isReady()) {
         this.modelRenderer.init();
      }

   }

   public void generateModel() {
      if (!this.generated) {
         this.generated = true;
         Iterator var1 = this.blueprint.getFlatMap().entrySet().iterator();

         while(var1.hasNext()) {
            Entry var2 = (Entry)var1.next();
            BlueprintJoint var3 = (BlueprintJoint)var2.getValue();
            IJoint var4 = var3.getParent() == null ? null : (IJoint)this.joints.get(var3.getParent().getName());
            JointImpl var5 = new JointImpl(this, var3);
            if (var4 != null) {
               var5.setParent(var4);
               if (var4.isPivotOverride()) {
                  var5.setPivot(var4);
               } else if (var4.getPivot() != null) {
                  var5.setPivot(var4.getPivot());
               }
            } else {
               this.roots.put(var5.getUniqueJointId(), var5);
            }

            Iterator var6 = var3.getCachedBehaviorProvider().entrySet().iterator();

            while(var6.hasNext()) {
               Entry var7 = (Entry)var6.next();
               JointActionType var8 = (JointActionType)var7.getKey();
               JointActionType.CachedProvider var9 = (JointActionType.CachedProvider)var7.getValue();
               this.getBehaviorManager(var8);
               this.getBehaviorRenderer(var8);
               var5.addJointAction(var9.create(var5));
            }

            this.joints.register(var5.getUniqueJointId(), var5);
         }

         if (this.autoRendererInitialization) {
            this.modelRenderer.init();
         }
      }

   }

   public void forceGenerateJoint(String var1, String var2, final BlueprintJoint var3) {
      IJoint var4 = var1 == null ? null : (IJoint)this.getJoint(var1).orElse((Object)null);
      ConcurrentHashMap var5 = new ConcurrentHashMap();
      LinkedList var6 = new LinkedList<BlueprintJoint>() {
         {
            this.add(var3);
         }
      };

      while(true) {
         while(!var6.isEmpty()) {
            BlueprintJoint var7 = (BlueprintJoint)var6.pop();
            var6.addAll(var7.getChildren().values());
            String var8 = var7.getName();
            String var9 = var2 + var8;
            if (this.joints.contains(var9)) {
               LogUtil.error("Unable to force generate custom joint: ID " + var9 + " already exists.");
            } else {
               IJoint var10 = var7.getParent() == null ? var4 : (IJoint)var5.getOrDefault(var7.getParent().getName(), var4);
               JointImpl var11 = new JointImpl(this, var7);
               var11.setCustomId(var9);
               if (var10 != null) {
                  var11.setParent(var10);
                  if (var10.isPivotOverride()) {
                     var11.setPivot(var10);
                  } else if (var10.getPivot() != null) {
                     var11.setPivot(var10.getPivot());
                  }
               } else {
                  this.roots.put(var11.getUniqueJointId(), var11);
               }

               Iterator var12 = var7.getCachedBehaviorProvider().entrySet().iterator();

               while(var12.hasNext()) {
                  Entry var13 = (Entry)var12.next();
                  JointActionType var14 = (JointActionType)var13.getKey();
                  JointActionType.CachedProvider var15 = (JointActionType.CachedProvider)var13.getValue();
                  this.getBehaviorManager(var14);
                  this.getBehaviorRenderer(var14);
                  var11.addJointAction(var15.create(var11));
               }

               var5.put(var8, var11);
               this.joints.register(var11.getUniqueJointId(), var11);
            }
         }

         return;
      }
   }

   public void removeJoint(String var1) {
      IJoint var2 = (IJoint)this.joints.remove(var1);
      if (var2 != null) {
         this.roots.remove(var1);
      }

   }

   public boolean canHurt() {
      return this.canHurt;
   }

   public boolean wasMarkedHurt() {
      return this.wasMarkedHurt;
   }

   public boolean isMarkedHurt() {
      return this.canHurt && this.modelContainer != null && this.modelContainer.getHurtTick() > 0;
   }

   public boolean isGlowing() {
      return this.glowing == null ? this.modelContainer.isGlowing() : this.glowing;
   }

   public void setGlowing(@Nullable Boolean var1) {
      this.glowing = var1;
      this.getData().markModelGlowing(this, this.glowing != null && this.glowing);
   }

   public int getGlowColor() {
      return this.glowColor == null ? this.modelContainer.getGlowColor() : this.glowColor;
   }

   public float getXHeadRot() {
      return this.lockPitch ? 0.0F : this.modelContainer.getXHeadRot();
   }

   public float getYHeadRot() {
      return this.lockYaw ? this.modelContainer.getYBodyRot() : this.modelContainer.getYHeadRot();
   }

   public <T extends JointAction> Optional<BehaviorManager<T>> getBehaviorManager(JointActionType<T> var1) {
      JointActionType.BehaviorManagerProvider var2 = var1.getBehaviorManagerProvider();
      if (var2 == null) {
         return Optional.empty();
      } else {
         synchronized(this.behaviorManagers) {
            return Optional.ofNullable((BehaviorManager)this.behaviorManagers.computeIfAbsent(var1, (var3) -> {
               BehaviorManager var4 = var2.create(this, var1);
               if (var4 == null) {
                  return null;
               } else {
                  var4.onCreate();
                  return var4;
               }
            }));
         }
      }
   }

   public Optional<BehaviorRenderer> getBehaviorRenderer(JointActionType<?> var1) {
      synchronized(this.behaviorRenderers) {
         BehaviorRenderer var3 = (BehaviorRenderer)this.behaviorRenderers.get(var1);
         if (var3 != null) {
            return Optional.of(var3);
         } else {
            var3 = var1.getRenderType().createBehaviorRenderer(this);
            if (var3 != null) {
               this.behaviorRenderers.put(var1, var3);
            }

            return Optional.ofNullable(var3);
         }
      }
   }

   private void forJoints(Consumer<IJoint> var1) {
      Iterator var2 = this.joints.getValues().iterator();

      while(var2.hasNext()) {
         IJoint var3 = (IJoint)var2.next();
         if (var3.getParent() == null) {
            var1.accept(var3);
         }
      }

   }

   private void forManagers(Consumer<BehaviorManager<?>> var1) {
      synchronized(this.behaviorManagers) {
         Iterator var3 = this.behaviorManagers.values().iterator();

         while(var3.hasNext()) {
            BehaviorManager var4 = (BehaviorManager)var3.next();
            var1.accept(var4);
         }

      }
   }

   public void save(SavedData var1) {
      var1.putString("blueprint", this.blueprint.getName());
      var1.putFloat("render_scale", this.getScale().x());
      var1.putFloat("hitbox_scale", this.getHitboxScale().x());
      var1.putBoolean("can_hurt", this.canHurt());
      var1.putInt("default_tint", this.defaultTint.asRGB());
      var1.putInt("damage_tint", this.damageTint.asRGB());
      var1.putBoolean("lock_pitch", this.lockPitch);
      var1.putBoolean("lock_yaw", this.lockYaw);
      var1.putBoolean("hitbox_visible", this.hitboxVisible);
      var1.putBoolean("shadow_visible", this.shadowVisible);
      var1.putBoolean("main_hitbox", this.mainHitbox);
      var1.putBoolean("glowing", this.glowing);
      var1.putInt("glow_color", this.glowColor);
      var1.putInt("block_light", this.blockLight);
      var1.putInt("sky_light", this.skyLight);
      HashSet var2 = new HashSet();
      SavedData var3 = new SavedData();
      Iterator var4 = this.blueprint.getFlatMap().keySet().iterator();

      while(var4.hasNext()) {
         String var5 = (String)var4.next();
         IJoint var6 = (IJoint)this.joints.get(var5);
         if (var6 == null) {
            var2.add(var5);
         } else {
            var6.save().ifPresent((var2x) -> {
               var3.putData(var5, var2x);
            });
         }
      }

      var1.putList("removed", var2);
      var1.putData("default_joints", var3);
      this.animationHandler.save().ifPresent((var1x) -> {
         var1.putData("animation_handler", var1x);
      });
   }

   public void load(SavedData var1) {
      this.setScale((double)var1.getFloat("render_scale"));
      this.setHitboxScale((double)var1.getFloat("hitbox_scale"));
      this.setCanHurt(var1.getBoolean("can_hurt"));
      this.setDefaultTint(Color.fromRGB(var1.getInt("default_tint")));
      this.setDamageTint(Color.fromRGB(var1.getInt("damage_tint")));
      this.setLockPitch(var1.getBoolean("lock_pitch"));
      this.setLockYaw(var1.getBoolean("lock_yaw"));
      this.setHitboxVisible(var1.getBoolean("hitbox_visible"));
      this.setShadowVisible(var1.getBoolean("shadow_visible"));
      this.setGlowing(var1.getBoolean("glowing"));
      this.setGlowColor(var1.getInt("glow_color"));
      this.setBlockLight(var1.getInt("block_light", -1));
      this.setSkyLight(var1.getInt("sky_light", -1));
      Iterator var2 = var1.getList("removed").iterator();

      while(var2.hasNext()) {
         Object var3 = var2.next();
         String var4 = (String)var3;
         this.removeJoint(var4);
      }

      var1.getData("default_joints").ifPresent((var1x) -> {
         Iterator var2 = var1x.keySet().iterator();

         while(var2.hasNext()) {
            String var3 = (String)var2.next();
            this.getJoint(var3).ifPresent((var2x) -> {
               Optional var3x = var1x.getData(var3);
               Objects.requireNonNull(var2x);
               Objects.requireNonNull(var2x);
               var3x.ifPresent(var2x::load);
            });
         }

      });
   }

   private IEntityData getData() {
      return this.modelContainer.getBase().getData();
   }

   public IModelContainer getModeledEntity() {
      return this.modelContainer;
   }

   public void setModeledEntity(IModelContainer var1) {
      this.modelContainer = var1;
   }

   public ExecutionCallback<IVisualModel.Scale> getScaleCallback() {
      return this.scaleExecutionCallback;
   }

   public Map<String, IJoint> getJoints() {
      return this.joints.readOnly();
   }

   @Generated
   public ModelBlueprint getBlueprint() {
      return this.blueprint;
   }

   @Generated
   public ModelRenderer getModelRenderer() {
      return this.modelRenderer;
   }

   @Generated
   public AnimationHandler getAnimationHandler() {
      return this.animationHandler;
   }

   @Generated
   public Map<String, IJoint> getRoots() {
      return this.roots;
   }

   @Generated
   public ExecutionCallback<IVisualModel.Scale> getScaleExecutionCallback() {
      return this.scaleExecutionCallback;
   }

   @Generated
   public IModelContainer getModelContainer() {
      return this.modelContainer;
   }

   @Generated
   public boolean isMainHitbox() {
      return this.mainHitbox;
   }

   @Generated
   public boolean isGenerated() {
      return this.generated;
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
   public boolean isAutoRendererInitialization() {
      return this.autoRendererInitialization;
   }

   @Generated
   public boolean isHitboxVisible() {
      return this.hitboxVisible;
   }

   @Generated
   public boolean isShadowVisible() {
      return this.shadowVisible;
   }

   @Generated
   public boolean isCanHurt() {
      return this.canHurt;
   }

   @Generated
   public Color getDefaultTint() {
      return this.defaultTint;
   }

   @Generated
   public Color getDamageTint() {
      return this.damageTint;
   }

   @Generated
   public boolean isWasMarkedHurt() {
      return this.wasMarkedHurt;
   }

   @Generated
   public Boolean getGlowing() {
      return this.glowing;
   }

   @Generated
   public int getBlockLight() {
      return this.blockLight;
   }

   @Generated
   public int getSkyLight() {
      return this.skyLight;
   }

   @Generated
   public boolean isLockPitch() {
      return this.lockPitch;
   }

   @Generated
   public boolean isLockYaw() {
      return this.lockYaw;
   }

   @Generated
   public void setModelContainer(IModelContainer var1) {
      this.modelContainer = var1;
   }

   @Generated
   public void setMainHitbox(boolean var1) {
      this.mainHitbox = var1;
   }

   @Generated
   public void setGenerated(boolean var1) {
      this.generated = var1;
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
   public void setAutoRendererInitialization(boolean var1) {
      this.autoRendererInitialization = var1;
   }

   @Generated
   public void setHitboxVisible(boolean var1) {
      this.hitboxVisible = var1;
   }

   @Generated
   public void setShadowVisible(boolean var1) {
      this.shadowVisible = var1;
   }

   @Generated
   public void setCanHurt(boolean var1) {
      this.canHurt = var1;
   }

   @Generated
   public void setDefaultTint(Color var1) {
      this.defaultTint = var1;
   }

   @Generated
   public void setDamageTint(Color var1) {
      this.damageTint = var1;
   }

   @Generated
   public void setWasMarkedHurt(boolean var1) {
      this.wasMarkedHurt = var1;
   }

   @Generated
   public void setGlowColor(Integer var1) {
      this.glowColor = var1;
   }

   @Generated
   public void setBlockLight(int var1) {
      this.blockLight = var1;
   }

   @Generated
   public void setSkyLight(int var1) {
      this.skyLight = var1;
   }

   @Generated
   public void setLockPitch(boolean var1) {
      this.lockPitch = var1;
   }

   @Generated
   public void setLockYaw(boolean var1) {
      this.lockYaw = var1;
   }
}
