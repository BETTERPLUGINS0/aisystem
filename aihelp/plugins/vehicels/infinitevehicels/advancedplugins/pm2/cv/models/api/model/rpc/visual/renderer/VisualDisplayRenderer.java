package advancedplugins.pm2.cv.models.api.model.rpc.visual.renderer;

import advancedplugins.pm2.cv.models.api.utils.data.tracker.DataTracker;
import java.util.UUID;
import org.bukkit.inventory.ItemStack;
import org.joml.Quaternionf;
import org.joml.Vector3f;

public interface VisualDisplayRenderer extends VisualRenderer {
   boolean isRespawnRequired();

   void setRespawnRequired(boolean var1);

   VisualDisplayRenderer.RendererVisualModel getVisualModel();

   public interface RendererVisualModel {
      int getPivotId();

      UUID getPivotUuid();

      int getModelId();

      UUID getModelUuid();

      default void clearModelDirty() {
         this.getPosition().clearDirty();
         this.getLeftRotation().clearDirty();
         this.getScale().clearDirty();
         this.getModel().clearDirty();
      }

      default boolean isModelDirty() {
         return this.getPosition().isDirty() || this.getLeftRotation().isDirty() || this.getScale().isDirty() || this.getModel().isDirty();
      }

      DataTracker<Vector3f> getOrigin();

      DataTracker<Vector3f> getPosition();

      DataTracker<Quaternionf> getLeftRotation();

      DataTracker<Vector3f> getScale();

      DataTracker<ItemStack> getModel();
   }
}
