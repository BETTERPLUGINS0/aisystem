package advancedplugins.pm2.cv.models.api.model.rpc.joint.renderer.renderer;

import advancedplugins.pm2.cv.models.api.model.rpc.joint.renderer.BehaviorRenderer;
import advancedplugins.pm2.cv.models.api.utils.data.tracker.CollectionDataTracker;
import advancedplugins.pm2.cv.models.api.utils.data.tracker.DataTracker;
import java.util.UUID;
import org.bukkit.entity.ItemDisplay.ItemDisplayTransform;
import org.bukkit.inventory.ItemStack;
import org.joml.Quaternionf;
import org.joml.Vector3f;

public interface SegmentRenderer extends BehaviorRenderer, RenderQueues<SegmentRenderer.Pivot> {
   public interface Pivot extends RenderQueues<SegmentRenderer.Joint> {
      int getId();

      UUID getUuid();

      void clearDirty();

      DataTracker<Vector3f> getPosition();

      DataTracker<Float> getYaw();

      CollectionDataTracker<Integer> getPassengers();
   }

   public interface Joint {
      int getId();

      SegmentRenderer.Pivot getPivot();

      UUID getUuid();

      default boolean isDirty() {
         return this.isTransformDirty() || this.isRenderDirty();
      }

      default boolean isTransformDirty() {
         return this.getPosition().isDirty() || this.getLeftRotation().isDirty() || this.getScale().isDirty() || this.getRightRotation().isDirty();
      }

      default boolean isRenderDirty() {
         return this.getModel().isDirty() || this.getDisplay().isDirty() || this.getVisibility().isDirty() || this.getGlowing().isDirty() || this.getGlowColor().isDirty() || this.getBrightness().isDirty() || this.getStep().isDirty();
      }

      default void clearDirty() {
         this.getStep().clearDirty();
         this.getPosition().clearDirty();
         this.getLeftRotation().clearDirty();
         this.getScale().clearDirty();
         this.getRightRotation().clearDirty();
         this.getModel().clearDirty();
         this.getDisplay().clearDirty();
         this.getVisibility().clearDirty();
         this.getGlowing().clearDirty();
         this.getGlowColor().clearDirty();
         this.getBrightness().clearDirty();
      }

      DataTracker<Boolean> getRender();

      DataTracker<Boolean> getStep();

      DataTracker<Vector3f> getPosition();

      DataTracker<Quaternionf> getLeftRotation();

      DataTracker<Vector3f> getScale();

      DataTracker<Quaternionf> getRightRotation();

      DataTracker<ItemStack> getModel();

      DataTracker<ItemDisplayTransform> getDisplay();

      DataTracker<Boolean> getVisibility();

      DataTracker<Boolean> getGlowing();

      DataTracker<Integer> getGlowColor();

      DataTracker<Integer> getBrightness();
   }
}
