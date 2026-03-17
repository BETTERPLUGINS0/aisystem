package advancedplugins.pm2.cv.models.api.model.rpc.joint;

import advancedplugins.pm2.cv.models.api.model.rpc.IVisualModel;
import advancedplugins.pm2.cv.models.api.model.rpc.generator.blueprint.BlueprintJoint;
import advancedplugins.pm2.cv.models.api.model.rpc.joint.behavior.JointAction;
import advancedplugins.pm2.cv.models.api.model.rpc.joint.behavior.JointActionType;
import advancedplugins.pm2.cv.models.api.utils.OffsetMode;
import advancedplugins.pm2.cv.models.api.utils.StepFlag;
import advancedplugins.pm2.cv.models.api.utils.data.io.DataIO;
import advancedplugins.pm2.cv.models.api.utils.data.tracker.DataTracker;
import java.util.Map;
import java.util.Optional;
import me.coley.recaf.metadata.InsnComment;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.Nullable;
import org.joml.Quaternionf;
import org.joml.Vector3f;

public interface IJoint extends DataIO {
   String getUniqueJointId();

   String getJointId();

   String getCustomId();

   void setCustomId(String var1);

   IVisualModel getVisualModel();

   BlueprintJoint getBlueprintJoint();

   @Nullable
   IJoint getParent();

   void setParent(@Nullable IJoint var1);

   @Nullable
   IJoint getPivot();

   void setPivot(@Nullable IJoint var1);

   boolean isPivotOverride();

   Location getPivotLocation();

   void setPivotLocation(Location var1);

   Location calculatePivotLocation();

   Location getBaseLocation();

   Map<String, IJoint> getChildren();

   boolean isRenderer();

   void setRenderer(boolean var1);

   void tick();

   void lazyTick();

   boolean isMarkedDestroy();

   void destroy();

   float getYaw();

   void setYaw(float var1);

   void setModelScale(int var1);

   void calculateGlobalTransform();

   ManualAnimator getManualAnimator();

   void setManualAnimator(ManualAnimator var1);

   Vector3f getCachedPosition();

   @InsnComment(
      At_1 = "kXQVJ3M2IlSyBDcNR0V4c0SFtWV0xWVUNEbxI2QKBjYJJ0MZd0cSVWZ4lTStllQNNDWI92N"
   )
   void setCachedPosition(Vector3f this);

   Vector3f getGlobalPosition();

   void setGlobalPosition(Vector3f var1);

   Vector3f getTrueGlobalPosition();

   Vector3f getTrueCachedPosition();

   Vector3f getCachedLeftRotation();

   void setCachedLeftRotation(Vector3f var1);

   Quaternionf getCachedLeftQuaternion();

   void setCachedLeftQuaternion(Quaternionf var1);

   Quaternionf getGlobalLeftRotation();

   void setGlobalLeftRotation(Quaternionf var1);

   Quaternionf getTrueGlobalLeftRotation();

   Quaternionf getTrueCachedLeftRotation();

   Vector3f getCachedScale();

   void setCachedScale(Vector3f var1);

   Vector3f getGlobalScale();

   void setGlobalScale(Vector3f var1);

   void setForcedScale(Vector3f var1);

   Vector3f getTrueGlobalScale();

   Vector3f getTrueCachedScale();

   Vector3f getCachedRightRotation();

   void setCachedRightRotation(Vector3f var1);

   Quaternionf getGlobalRightRotation();

   void setGlobalRightRotation(Quaternionf var1);

   Quaternionf getTrueGlobalRightRotation();

   Vector3f getTrueCachedRightRotation();

   boolean hasGlobalRotation();

   void setHasGlobalRotation(boolean var1);

   /** @deprecated */
   @Deprecated
   ItemStack getModel();

   void setModel(BlueprintJoint var1);

   void setModel(ItemStack var1);

   JointItems getModels();

   void clearModel();

   DataTracker<JointItems> getModelTracker();

   Color getDefaultTint();

   void setDefaultTint(Color var1);

   Color getDamageTint();

   void setDamageTint(Color var1);

   boolean isEnchanted();

   void setEnchanted(boolean var1);

   boolean isVisible();

   void setVisible(boolean var1);

   boolean isGlowing();

   void setGlowing(@Nullable Boolean var1);

   int getGlowColor();

   void setGlowColor(@Nullable Integer var1);

   int getBlockLight();

   void setBlockLight(int var1);

   int getSkyLight();

   void setSkyLight(int var1);

   default int getBrightness() {
      int blockLight = this.getBlockLight();
      int skyLight = this.getSkyLight();
      if (blockLight < 0 && skyLight < 0) {
         return -1;
      } else {
         blockLight = Math.max(blockLight, 0);
         skyLight = Math.max(skyLight, 0);
         return blockLight << 4 | skyLight << 20;
      }
   }

   boolean isEffectivelyInvisible();

   boolean shouldStep();

   void markStep(StepFlag var1);

   boolean pollModelScaleChanged();

   boolean isRootJoint();

   void setJointOnGround(boolean var1);

   Location getLocationUnsafe();

   Location getLocation();

   Location getLocation(OffsetMode var1, Vector3f var2, boolean var3);

   void addJointAction(JointAction var1);

   boolean hasJointAction(JointActionType<?> var1);

   <T extends JointAction> Optional<T> getJointAction(JointActionType<T> var1);

   <T extends JointAction> Optional<T> removeJointAction(JointActionType<T> var1);

   Map<JointActionType<?>, JointAction> getImmutableJointActions();
}
