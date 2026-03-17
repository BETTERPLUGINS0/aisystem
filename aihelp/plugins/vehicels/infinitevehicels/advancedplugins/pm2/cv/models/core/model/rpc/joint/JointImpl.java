package advancedplugins.pm2.cv.models.core.model.rpc.joint;

import advancedplugins.pm2.cv.models.api.ModelAPI;
import advancedplugins.pm2.cv.models.api.model.rpc.IVisualModel;
import advancedplugins.pm2.cv.models.api.model.rpc.animation.rootmotion.RootMotion;
import advancedplugins.pm2.cv.models.api.model.rpc.entity.data.IEntityData;
import advancedplugins.pm2.cv.models.api.model.rpc.generator.blueprint.BlueprintJoint;
import advancedplugins.pm2.cv.models.api.model.rpc.joint.IJoint;
import advancedplugins.pm2.cv.models.api.model.rpc.joint.JointItems;
import advancedplugins.pm2.cv.models.api.model.rpc.joint.ManualAnimator;
import advancedplugins.pm2.cv.models.api.model.rpc.joint.behavior.JointAction;
import advancedplugins.pm2.cv.models.api.model.rpc.joint.behavior.JointActionType;
import advancedplugins.pm2.cv.models.api.model.rpc.joint.behavior.ProceduralType;
import advancedplugins.pm2.cv.models.api.utils.OffsetMode;
import advancedplugins.pm2.cv.models.api.utils.StepFlag;
import advancedplugins.pm2.cv.models.api.utils.data.io.SavedData;
import advancedplugins.pm2.cv.models.api.utils.data.tracker.DataTracker;
import advancedplugins.pm2.cv.models.api.utils.data.tracker.UpdateDataTracker;
import advancedplugins.pm2.cv.models.api.utils.math.MathUtils;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;
import lombok.Generated;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.joml.Quaternionf;
import org.joml.Vector3f;
import org.joml.Vector3fc;

public class JointImpl implements IJoint {
   @NotNull
   private final IVisualModel visualModel;
   @NotNull
   private final BlueprintJoint blueprintJoint;
   private final Map<String, IJoint> children = Maps.newConcurrentMap();
   private final Map<JointActionType<?>, JointAction> jointActions = new LinkedHashMap();
   private final JointItems itemModels = new JointItems();
   private final DataTracker<Vector3f> modelScale = new UpdateDataTracker(new Vector3f(), Vector3f::set);
   private final Set<ProceduralType> proceduralTypes = new HashSet();
   private final AtomicReference<Vector3f> trueGlobalPosition = new AtomicReference(new Vector3f());
   private final AtomicReference<Vector3f> trueCachePosition = new AtomicReference(new Vector3f());
   private final AtomicReference<Quaternionf> trueGlobalLeftRotation = new AtomicReference(new Quaternionf());
   private final AtomicReference<Quaternionf> trueCacheLeftRotation = new AtomicReference(new Quaternionf());
   private final AtomicReference<Vector3f> trueGlobalScale = new AtomicReference(new Vector3f());
   private final AtomicReference<Vector3f> trueCacheScale = new AtomicReference(new Vector3f(1.0F));
   private final AtomicReference<Quaternionf> trueGlobalRightRotation = new AtomicReference(new Quaternionf());
   private final AtomicReference<Vector3f> trueCacheRightRotation = new AtomicReference(new Vector3f());
   private final DataTracker<Byte> trackedStepFlags = new DataTracker();
   private ManualAnimator manualAnimator;
   private Vector3f cachedPosition = new Vector3f();
   private Vector3f cachedLeftRotation = new Vector3f();
   private Quaternionf cachedLeftQuaternion = new Quaternionf();
   private Vector3f cachedScale = new Vector3f();
   private Vector3f cachedRightRotation = new Vector3f();
   private Vector3f globalPosition = new Vector3f();
   private Quaternionf globalLeftRotation = new Quaternionf();
   private Vector3f globalScale = new Vector3f();
   @Nullable
   private Vector3f forcedScale = null;
   private Quaternionf globalRightRotation = new Quaternionf();
   private byte stepFlags;
   private String customId = null;
   @Nullable
   private IJoint parent;
   @Nullable
   private IJoint pivot;
   private boolean pivotOverride;
   private Location pivotLocation;
   private boolean isRenderer;
   private float yaw;
   private boolean hasGlobalRotation;
   private Color lastColor;
   private Color defaultTint;
   private Color damageTint;
   private boolean enchanted;
   private Boolean glowing;
   private Integer glowColor;
   private int blockLight = -1;
   private int skyLight = -1;
   private boolean visible = true;
   private boolean markedDestroy;
   private boolean isJointOnGround;

   public JointImpl(@NotNull IVisualModel var1, @NotNull BlueprintJoint var2) {
      this.visualModel = var1;
      this.blueprintJoint = var2;
      this.globalPosition.set(var2.getRotatedGlobalPosition());
      this.globalLeftRotation.set(var2.getGlobalQuaternion());
      this.modelScale.set((new Vector3f(var2.getModelScale())).mul((float)var2.getScale()));
      this.globalScale.set((Vector3fc)this.modelScale.get());
      this.isRenderer = var2.isRenderer();
      if (this.isRenderer) {
         this.setModel(var2);
      } else {
         BlueprintJoint var3 = var2.getDupeTarget();
         if (var3 != null && var3.isRenderer()) {
            this.setModel(var3);
            this.isRenderer = true;
         }
      }

      this.setVisible(var2.isRenderByDefault());
      ((Vector3f)this.trueGlobalPosition.get()).set(this.globalPosition);
      ((Quaternionf)this.trueGlobalLeftRotation.get()).set(this.globalLeftRotation);
      ((Vector3f)this.trueGlobalScale.get()).set(this.globalScale);
      ((Quaternionf)this.trueGlobalRightRotation.get()).set(this.globalRightRotation);
   }

   public String getUniqueJointId() {
      return this.getCustomId() == null ? this.getJointId() : this.getCustomId();
   }

   public String getJointId() {
      return this.blueprintJoint.getName();
   }

   public Location calculatePivotLocation() {
      if (this.pivot != null) {
         Location var1 = this.pivot.getPivotLocation();
         if (var1 != null) {
            return var1.clone();
         }
      }

      return this.getBaseLocation();
   }

   public Location getBaseLocation() {
      return this.visualModel.getModeledEntity().getBase().getLocation().clone();
   }

   public void tick() {
      this.stepFlags = 0;
      this.trackedStepFlags.clearDirty();
      this.tryTintModel();
      this.cachedPosition.set(0.0F, 0.0F, 0.0F);
      this.cachedLeftRotation.set(0.0F, 0.0F, 0.0F);
      this.cachedLeftQuaternion.set(0.0F, 0.0F, 0.0F, 1.0F);
      this.cachedScale.set(1.0F, 1.0F, 1.0F);
      this.cachedRightRotation.set(0.0F, 0.0F, 0.0F);
      this.forBehaviors(JointAction::preAnimation);
      if (this.manualAnimator == null) {
         if (this.proceduralTypes.contains(ProceduralType.ANIMATION)) {
            this.forBehaviors(JointAction::onAnimation);
         } else {
            this.visualModel.getAnimationHandler().updateJoint(this);
         }

         ((Quaternionf)this.trueCacheLeftRotation.get()).set(MathUtils.toQuaternion(this.cachedLeftRotation));
      } else {
         this.manualAnimator.animate(this);
         ((Quaternionf)this.trueCacheLeftRotation.get()).set(this.cachedLeftQuaternion);
      }

      this.forBehaviors(JointAction::postAnimation);
      ((Vector3f)this.trueCachePosition.get()).set(this.cachedPosition);
      ((Vector3f)this.trueCacheScale.get()).set(this.cachedScale);
      ((Vector3f)this.trueCacheRightRotation.get()).set(this.cachedRightRotation);
      this.forBehaviors(JointAction::preGlobalCalculation);
      if (this.proceduralTypes.contains(ProceduralType.TRANSFORM)) {
         this.forBehaviors(JointAction::onGlobalCalculation);
      } else {
         this.calculateGlobalTransform();
      }

      this.forBehaviors(JointAction::postGlobalCalculation);
      this.yaw = this.pivot == null ? this.visualModel.getModeledEntity().getYBodyRot() : this.pivot.getYaw();
      this.forBehaviors((var1) -> {
         this.yaw = var1.onUpdateYaw(this.yaw);
      });
      this.yaw = MathUtils.wrapDegree(this.yaw);
      if (!this.children.isEmpty()) {
         this.forBehaviors(JointAction::preChildCalculation);
         this.children.values().forEach(IJoint::tick);
         this.forBehaviors(JointAction::postChildCalculation);
      }

      this.globalScale.mul((Vector3fc)this.modelScale.get()).mul(this.visualModel.getScale());
      this.globalPosition.mul(this.visualModel.getScale());
      this.forBehaviors(JointAction::onFinalize);
      if (this.isRootJoint()) {
         if (!MathUtils.isSimilar(((Vector3f)this.trueCachePosition.get()).lengthSquared(), 0.0F)) {
            this.queueVelocity(new RootMotion(new Vector3f((Vector3fc)this.trueCachePosition.get()), this.getVisualModel().getModeledEntity().getYHeadRot(), this.isJointOnGround));
         }

         this.globalPosition.zero();
      }

      this.trackedStepFlags.set(this.stepFlags);
      ((Vector3f)this.trueGlobalPosition.get()).set(this.globalPosition);
      ((Quaternionf)this.trueGlobalLeftRotation.get()).set(this.globalLeftRotation);
      ((Vector3f)this.trueGlobalScale.get()).set(this.globalScale);
      ((Quaternionf)this.trueGlobalRightRotation.get()).set(this.globalRightRotation);
   }

   public void lazyTick() {
      this.yaw = this.pivot == null ? this.visualModel.getModeledEntity().getYBodyRot() : this.pivot.getYaw();
      this.forBehaviors((var1) -> {
         this.yaw = var1.onUpdateYaw(this.yaw);
      });
      this.yaw = MathUtils.wrapDegree(this.yaw);
      if (!this.children.isEmpty()) {
         this.children.values().forEach(IJoint::lazyTick);
      }

   }

   public void destroy() {
      this.markedDestroy = true;
      this.jointActions.values().forEach(JointAction::onRemove);
      this.children.values().forEach(IJoint::destroy);
      this.children.clear();
      this.getData().markJointGlowing(this, false);
      if (this.parent != null && !this.parent.isMarkedDestroy()) {
         this.parent.getChildren().remove(this.getUniqueJointId());
      }

      this.visualModel.removeJoint(this.getUniqueJointId());
   }

   public void calculateGlobalTransform() {
      this.cachedPosition.add(this.blueprintJoint.getLocalPosition());
      Vector3f var1 = this.cachedLeftRotation.add(this.blueprintJoint.getLocalRotation(), new Vector3f());
      Quaternionf var2 = this.cachedLeftQuaternion.mul(MathUtils.toQuaternion(var1), new Quaternionf());
      if (this.parent != null) {
         Vector3f var3 = this.parent.getGlobalPosition();
         Quaternionf var4 = this.parent.getGlobalLeftRotation();
         Vector3f var5 = this.parent.getGlobalScale();
         if (!this.isRootJoint()) {
            var3.add(this.cachedPosition.mul(var5).rotate(var4), this.globalPosition);
         }

         if (!this.hasGlobalRotation()) {
            var4.mul(var2, this.globalLeftRotation);
         } else {
            this.globalLeftRotation.set(var2);
         }

         var5.mul(this.cachedScale, this.globalScale);
      } else {
         if (!this.isRootJoint()) {
            this.globalPosition.set(this.cachedPosition);
         }

         this.globalLeftRotation.set(var2);
         this.globalScale.set(this.cachedScale);
      }

   }

   public Vector3f getTrueGlobalPosition() {
      return (Vector3f)this.trueGlobalPosition.get();
   }

   public Vector3f getTrueCachedPosition() {
      return (Vector3f)this.trueCachePosition.get();
   }

   public Quaternionf getTrueGlobalLeftRotation() {
      return (Quaternionf)this.trueGlobalLeftRotation.get();
   }

   public Quaternionf getTrueCachedLeftRotation() {
      return (Quaternionf)this.trueCacheLeftRotation.get();
   }

   public Vector3f getTrueGlobalScale() {
      return (Vector3f)this.trueGlobalScale.get();
   }

   public Vector3f getTrueCachedScale() {
      return (Vector3f)this.trueCacheScale.get();
   }

   public Quaternionf getTrueGlobalRightRotation() {
      return (Quaternionf)this.trueGlobalRightRotation.get();
   }

   public Vector3f getTrueCachedRightRotation() {
      return (Vector3f)this.trueCacheRightRotation.get();
   }

   public boolean hasGlobalRotation() {
      return this.hasGlobalRotation;
   }

   public ItemStack getModel() {
      return this.itemModels.getFirst();
   }

   public void setModel(BlueprintJoint var1) {
      this.itemModels.update(var1, this.getColor());
   }

   public void setModel(ItemStack var1) {
      if (!this.itemModels.isEqual(Set.of(var1))) {
         this.itemModels.clear();
         this.itemModels.add(var1);
      }

   }

   public JointItems getModels() {
      return this.itemModels;
   }

   public void clearModel() {
      this.itemModels.clear();
   }

   public DataTracker<JointItems> getModelTracker() {
      return this.itemModels.getTracker();
   }

   public Color getDefaultTint() {
      return this.defaultTint == null ? this.visualModel.getDefaultTint() : this.defaultTint;
   }

   public Color getDamageTint() {
      return this.damageTint == null ? this.visualModel.getDamageTint() : this.damageTint;
   }

   public boolean isGlowing() {
      return this.glowing == null ? this.visualModel.isGlowing() : this.glowing;
   }

   public void setGlowing(@Nullable Boolean var1) {
      this.glowing = var1;
      this.getData().markJointGlowing(this, this.glowing != null && this.glowing);
   }

   public int getGlowColor() {
      return this.glowColor == null ? this.visualModel.getGlowColor() : this.glowColor;
   }

   public int getBlockLight() {
      return this.blockLight == -1 ? this.visualModel.getBlockLight() : this.blockLight;
   }

   public int getSkyLight() {
      return this.skyLight == -1 ? this.visualModel.getSkyLight() : this.skyLight;
   }

   public boolean isEffectivelyInvisible() {
      return !this.isRenderer || (double)((Vector3f)this.modelScale.get()).lengthSquared() < 1.0E-5D || (double)this.globalScale.lengthSquared() < 1.0E-5D;
   }

   public boolean shouldStep() {
      return (Byte)this.trackedStepFlags.get() == 0 && this.trackedStepFlags.isDirty();
   }

   public void markStep(StepFlag var1) {
      this.stepFlags = var1.setStep(this.stepFlags, true);
   }

   public boolean pollModelScaleChanged() {
      if (this.modelScale.isDirty()) {
         this.modelScale.clearDirty();
         return true;
      } else {
         return false;
      }
   }

   public boolean isRootJoint() {
      return this.visualModel.getModeledEntity().getRootMotionHandler().getRootJoint() == this;
   }

   public Location getLocationUnsafe() {
      return this.getLocation(this.getGlobalPosition());
   }

   public Location getLocation() {
      return this.getLocation(this.getTrueGlobalPosition());
   }

   private Location getLocation(Vector3f var1) {
      float var2 = (180.0F - this.getYaw()) * 0.017453292F;
      var1 = var1.rotateY(var2, new Vector3f());
      Location var3 = this.isPivotOverride() ? this.getPivotLocation() : this.calculatePivotLocation();
      return var3.clone().add((double)var1.x, (double)var1.y, (double)var1.z);
   }

   public Location getLocation(OffsetMode var1, Vector3f var2, boolean var3) {
      float var4 = (180.0F - this.getYaw()) * 0.017453292F;
      Vector3f var5 = this.getTrueGlobalPosition();
      Quaternionf var6 = this.getTrueGlobalLeftRotation();
      if (var3) {
         var2.mul(this.visualModel.getScale());
      }

      Vector3f var10000;
      switch(var1) {
      case LOCAL:
         var10000 = var5.add(var2.rotate(var6), new Vector3f()).rotateY(var4);
         break;
      case MODEL:
         var10000 = var5.add(var2, new Vector3f()).rotateY(var4);
         break;
      case GLOBAL:
         var10000 = var5.rotateY(var4, new Vector3f()).add(var2);
         break;
      default:
         throw new MatchException((String)null, (Throwable)null);
      }

      var5 = var10000;
      Location var7 = this.isPivotOverride() ? this.getPivotLocation() : this.calculatePivotLocation();
      return var7.clone().add((double)var5.x, (double)var5.y, (double)var5.z);
   }

   public void addJointAction(JointAction var1) {
      this.jointActions.put(var1.getType(), var1);
      var1.onApply();
      this.proceduralTypes.addAll(var1.getType().getProceduralTypes());
      this.pivotOverride |= var1.getType().isPivot();
   }

   public boolean hasJointAction(JointActionType<?> var1) {
      return this.jointActions.containsKey(var1);
   }

   public <T extends JointAction> Optional<T> getJointAction(JointActionType<T> var1) {
      JointAction var2 = (JointAction)this.jointActions.get(var1);
      return Optional.ofNullable(var2);
   }

   public <T extends JointAction> Optional<T> removeJointAction(JointActionType<T> var1) {
      JointAction var2 = (JointAction)this.jointActions.remove(var1);
      if (var2 != null) {
         var2.onRemove();
      }

      return Optional.ofNullable(var2);
   }

   public Map<JointActionType<?>, JointAction> getImmutableJointActions() {
      return ImmutableMap.copyOf(this.jointActions);
   }

   private void forBehaviors(Consumer<JointAction> var1) {
      this.jointActions.values().iterator().forEachRemaining(var1);
   }

   private void tryTintModel() {
      Color var1 = this.getColor();
      if (var1 != this.lastColor) {
         this.lastColor = var1;
         AtomicBoolean var2 = new AtomicBoolean();
         Consumer var3 = (var2x) -> {
            if (ModelAPI.getNMSHandler().colorStack(var2x, var1)) {
               var2.set(true);
            }

         };
         Objects.requireNonNull(var2);
         JointItems var10000 = this.itemModels;
         Objects.requireNonNull(var2);
         var10000.forEach(var3, var2::get);
      }

   }

   private Color getColor() {
      return this.visualModel.isMarkedHurt() ? this.getDamageTint() : this.getDefaultTint();
   }

   public void save(SavedData var1) {
      if (this.defaultTint != null) {
         var1.putInt("default_tint", this.defaultTint.asRGB());
      }

      if (this.damageTint != null) {
         var1.putInt("damage_tint", this.damageTint.asRGB());
      }

      if (this.blueprintJoint.isRenderByDefault() != this.isVisible()) {
         var1.putBoolean("visible", this.isVisible());
      }

      if (this.isEnchanted()) {
         var1.putBoolean("enchant", true);
      }

      if (this.glowing != null) {
         var1.putBoolean("glowing", this.glowing);
      }

      if (this.glowColor != null) {
         var1.putInt("glow_color", this.glowColor);
      }

      if (this.blockLight > 0) {
         var1.putInt("block_light", this.blockLight);
      }

      if (this.skyLight > 0) {
         var1.putInt("sky_light", this.skyLight);
      }

      this.forBehaviors((var1x) -> {
         var1x.save().ifPresent((var2) -> {
            var1.putData(var1x.getType().getId(), var2);
         });
      });
   }

   public void load(SavedData var1) {
      Integer var2 = var1.getInt("default_tint");
      if (var2 != null) {
         this.setDefaultTint(Color.fromRGB(var2));
      }

      Integer var3 = var1.getInt("damage_tint");
      if (var3 != null) {
         this.setDamageTint(Color.fromRGB(var3));
      }

      Boolean var4 = var1.getBoolean("visible");
      if (var4 != null) {
         this.setVisible(var4);
      }

      if (var1.getBoolean("enchant", false)) {
         this.setEnchanted(true);
      }

      this.setGlowing(var1.getBoolean("glowing"));
      this.setGlowColor(var1.getInt("glow_color"));
      this.setBlockLight(var1.getInt("block_light", -1));
      this.setSkyLight(var1.getInt("sky_light", -1));
      this.forBehaviors((var1x) -> {
         Optional var2 = var1.getData(var1x.getType().getId());
         Objects.requireNonNull(var1x);
         Objects.requireNonNull(var1x);
         var2.ifPresent(var1x::load);
      });
   }

   private IEntityData getData() {
      return this.visualModel.getModeledEntity().getBase().getData();
   }

   private void queueVelocity(RootMotion var1) {
      this.visualModel.getModeledEntity().getRootMotionHandler().queueVelocity(var1);
   }

   @NotNull
   public IVisualModel getVisualModel() {
      return this.visualModel;
   }

   @NotNull
   public BlueprintJoint getBlueprintJoint() {
      return this.blueprintJoint;
   }

   public void setModelScale(int var1) {
      this.modelScale.set(new Vector3f((float)var1));
   }

   @Nullable
   public IJoint getParent() {
      return this.parent;
   }

   public void setParent(@Nullable IJoint var1) {
      if (this.parent != null) {
         this.parent.getChildren().remove(this.getUniqueJointId());
      }

      this.parent = var1;
      if (this.parent != null) {
         this.parent.getChildren().put(this.getUniqueJointId(), this);
      }

      this.forBehaviors((var1x) -> {
         var1x.onParentSwap(this.parent);
      });
   }

   @Nullable
   public IJoint getPivot() {
      return this.pivot;
   }

   public void setPivot(@Nullable IJoint var1) {
      this.pivot = var1;
   }

   public void setEnchanted(boolean var1) {
      if (this.isEnchanted() != var1) {
         this.enchanted = var1;
         this.itemModels.forEach((var1x) -> {
            if (var1) {
               var1x.addUnsafeEnchantment(Enchantment.VANISHING_CURSE, 1);
            } else {
               var1x.removeEnchantment(Enchantment.VANISHING_CURSE);
            }

         }, true);
      }

   }

   @Generated
   public Map<String, IJoint> getChildren() {
      return this.children;
   }

   @Generated
   public Map<JointActionType<?>, JointAction> getJointActions() {
      return this.jointActions;
   }

   @Generated
   public JointItems getItemModels() {
      return this.itemModels;
   }

   @Generated
   public DataTracker<Vector3f> getModelScale() {
      return this.modelScale;
   }

   @Generated
   public Set<ProceduralType> getProceduralTypes() {
      return this.proceduralTypes;
   }

   @Generated
   public AtomicReference<Vector3f> getTrueCachePosition() {
      return this.trueCachePosition;
   }

   @Generated
   public AtomicReference<Quaternionf> getTrueCacheLeftRotation() {
      return this.trueCacheLeftRotation;
   }

   @Generated
   public AtomicReference<Vector3f> getTrueCacheScale() {
      return this.trueCacheScale;
   }

   @Generated
   public AtomicReference<Vector3f> getTrueCacheRightRotation() {
      return this.trueCacheRightRotation;
   }

   @Generated
   public DataTracker<Byte> getTrackedStepFlags() {
      return this.trackedStepFlags;
   }

   @Generated
   public ManualAnimator getManualAnimator() {
      return this.manualAnimator;
   }

   @Generated
   public Vector3f getCachedPosition() {
      return this.cachedPosition;
   }

   @Generated
   public Vector3f getCachedLeftRotation() {
      return this.cachedLeftRotation;
   }

   @Generated
   public Quaternionf getCachedLeftQuaternion() {
      return this.cachedLeftQuaternion;
   }

   @Generated
   public Vector3f getCachedScale() {
      return this.cachedScale;
   }

   @Generated
   public Vector3f getCachedRightRotation() {
      return this.cachedRightRotation;
   }

   @Generated
   public Vector3f getGlobalPosition() {
      return this.globalPosition;
   }

   @Generated
   public Quaternionf getGlobalLeftRotation() {
      return this.globalLeftRotation;
   }

   @Generated
   public Vector3f getGlobalScale() {
      return this.globalScale;
   }

   @Nullable
   @Generated
   public Vector3f getForcedScale() {
      return this.forcedScale;
   }

   @Generated
   public Quaternionf getGlobalRightRotation() {
      return this.globalRightRotation;
   }

   @Generated
   public byte getStepFlags() {
      return this.stepFlags;
   }

   @Generated
   public String getCustomId() {
      return this.customId;
   }

   @Generated
   public boolean isPivotOverride() {
      return this.pivotOverride;
   }

   @Generated
   public Location getPivotLocation() {
      return this.pivotLocation;
   }

   @Generated
   public boolean isRenderer() {
      return this.isRenderer;
   }

   @Generated
   public float getYaw() {
      return this.yaw;
   }

   @Generated
   public boolean isHasGlobalRotation() {
      return this.hasGlobalRotation;
   }

   @Generated
   public Color getLastColor() {
      return this.lastColor;
   }

   @Generated
   public boolean isEnchanted() {
      return this.enchanted;
   }

   @Generated
   public Boolean getGlowing() {
      return this.glowing;
   }

   @Generated
   public boolean isVisible() {
      return this.visible;
   }

   @Generated
   public boolean isMarkedDestroy() {
      return this.markedDestroy;
   }

   @Generated
   public boolean isJointOnGround() {
      return this.isJointOnGround;
   }

   @Generated
   public void setManualAnimator(ManualAnimator var1) {
      this.manualAnimator = var1;
   }

   @Generated
   public void setCachedPosition(Vector3f var1) {
      this.cachedPosition = var1;
   }

   @Generated
   public void setCachedLeftRotation(Vector3f var1) {
      this.cachedLeftRotation = var1;
   }

   @Generated
   public void setCachedLeftQuaternion(Quaternionf var1) {
      this.cachedLeftQuaternion = var1;
   }

   @Generated
   public void setCachedScale(Vector3f var1) {
      this.cachedScale = var1;
   }

   @Generated
   public void setCachedRightRotation(Vector3f var1) {
      this.cachedRightRotation = var1;
   }

   @Generated
   public void setGlobalPosition(Vector3f var1) {
      this.globalPosition = var1;
   }

   @Generated
   public void setGlobalLeftRotation(Quaternionf var1) {
      this.globalLeftRotation = var1;
   }

   @Generated
   public void setGlobalScale(Vector3f var1) {
      this.globalScale = var1;
   }

   @Generated
   public void setForcedScale(@Nullable Vector3f var1) {
      this.forcedScale = var1;
   }

   @Generated
   public void setGlobalRightRotation(Quaternionf var1) {
      this.globalRightRotation = var1;
   }

   @Generated
   public void setStepFlags(byte var1) {
      this.stepFlags = var1;
   }

   @Generated
   public void setCustomId(String var1) {
      this.customId = var1;
   }

   @Generated
   public void setPivotOverride(boolean var1) {
      this.pivotOverride = var1;
   }

   @Generated
   public void setPivotLocation(Location var1) {
      this.pivotLocation = var1;
   }

   @Generated
   public void setRenderer(boolean var1) {
      this.isRenderer = var1;
   }

   @Generated
   public void setYaw(float var1) {
      this.yaw = var1;
   }

   @Generated
   public void setHasGlobalRotation(boolean var1) {
      this.hasGlobalRotation = var1;
   }

   @Generated
   public void setLastColor(Color var1) {
      this.lastColor = var1;
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
   public void setVisible(boolean var1) {
      this.visible = var1;
   }

   @Generated
   public void setMarkedDestroy(boolean var1) {
      this.markedDestroy = var1;
   }

   @Generated
   public void setJointOnGround(boolean var1) {
      this.isJointOnGround = var1;
   }
}
