package advancedplugins.pm2.cv.models.api.model.rpc.renderer;

import advancedplugins.pm2.cv.models.api.lod.JointSnapshotHandler;
import advancedplugins.pm2.cv.models.api.model.rpc.Tickable;
import advancedplugins.pm2.cv.models.api.model.rpc.joint.IJoint;
import advancedplugins.pm2.cv.models.api.model.rpc.joint.JointItems;
import advancedplugins.pm2.cv.models.api.model.rpc.joint.renderer.renderer.RenderQueues;
import advancedplugins.pm2.cv.models.api.nms.entity.EntityHandler;
import advancedplugins.pm2.cv.models.api.utils.data.UpdateScheme;
import advancedplugins.pm2.cv.models.api.utils.data.tracker.CollectionDataTracker;
import advancedplugins.pm2.cv.models.api.utils.data.tracker.DataTracker;
import java.util.Map;
import java.util.UUID;
import org.bukkit.entity.ItemDisplay.ItemDisplayTransform;
import org.bukkit.inventory.ItemStack;
import org.joml.Quaternionf;
import org.joml.Vector3f;

public interface DisplayRenderer extends ModelRenderer, RenderQueues<DisplayRenderer.Joint>, Tickable {
   int getTick();

   DisplayRenderer.Pivot getPivot();

   DisplayRenderer.Hitbox getHitbox();

   void pushUpdate(UUID var1);

   boolean pollUpdate(UUID var1);

   public interface Pivot {
      int getId();

      UUID getUuid();

      void clearDirty();

      DataTracker<Vector3f> getPosition();

      CollectionDataTracker<Integer> getPassengers();
   }

   public interface Joint {
      default boolean isDirty() {
         return this.isTransformDirty() || this.isRenderDirty() || this.isModelDirty();
      }

      default boolean isSkippable() {
         return this.isTransformDirty() && !this.isRenderDirty() && !this.isModelDirty();
      }

      default boolean isTransformDirty() {
         return this.getPosition().isDirty() || this.getLeftRotation().isDirty() || this.getScale().isDirty() || this.getRightRotation().isDirty();
      }

      default boolean isRenderDirty() {
         return this.getDisplay().isDirty() || this.getVisibility().isDirty() || this.getGlowing().isDirty() || this.getGlowColor().isDirty() || this.getBrightness().isDirty() || this.getStep().isDirty();
      }

      default boolean isModelDirty() {
         return this.getModelUpdateScheme().hasUpdate();
      }

      default void clearDirty() {
         this.getStep().clearDirty();
         this.getPosition().clearDirty();
         this.getLeftRotation().clearDirty();
         this.getScale().clearDirty();
         this.getRightRotation().clearDirty();
         this.getModelUpdateScheme().reset();
         this.getDisplay().clearDirty();
         this.getVisibility().clearDirty();
         this.getGlowing().clearDirty();
         this.getGlowColor().clearDirty();
         this.getBrightness().clearDirty();
      }

      JointSnapshotHandler getSnapshotHandler();

      DataTracker<Boolean> getRender();

      DataTracker<Boolean> getStep();

      DataTracker<Vector3f> getPosition();

      DataTracker<Quaternionf> getLeftRotation();

      DataTracker<Vector3f> getScale();

      DataTracker<Quaternionf> getRightRotation();

      Map<Integer, DisplayRenderer.JointData> getModel();

      UpdateScheme<DisplayRenderer.JointData> getModelUpdateScheme();

      DataTracker<ItemDisplayTransform> getDisplay();

      DataTracker<Boolean> getVisibility();

      DataTracker<Boolean> getGlowing();

      DataTracker<Integer> getGlowColor();

      DataTracker<Integer> getBrightness();

      IJoint getJoint();

      void updateJointData(EntityHandler var1, DisplayRenderer.Pivot var2, JointItems var3);
   }

   public interface JointData {
      int getId();

      UUID getUuid();

      DisplayRenderer.Joint getJoint();

      DataTracker<ItemStack> getModel();

      DataTracker<Vector3f> getPosition();

      DataTracker<Vector3f> getScale();

      DataTracker<Quaternionf> getRotation();

      int getModelHash();
   }

   public interface Hitbox {
      int getPivotId();

      UUID getPivotUuid();

      int getHitboxId();

      UUID getHitboxUuid();

      int getShadowId();

      UUID getShadowUuid();

      void clearDirty();

      DataTracker<Vector3f> getPosition();

      DataTracker<Float> getWidth();

      DataTracker<Float> getHeight();

      DataTracker<Float> getShadowRadius();

      DataTracker<Boolean> getHitboxVisible();

      DataTracker<Boolean> getShadowVisible();

      default boolean isHitboxDirty() {
         return this.getWidth().isDirty() || this.getHeight().isDirty();
      }

      default boolean isHitboxVisible() {
         return (Boolean)this.getHitboxVisible().get();
      }

      default boolean isShadowVisible() {
         return (Boolean)this.getShadowVisible().get();
      }

      default boolean isPivotVisible() {
         return this.isHitboxVisible() || this.isShadowVisible();
      }
   }
}
