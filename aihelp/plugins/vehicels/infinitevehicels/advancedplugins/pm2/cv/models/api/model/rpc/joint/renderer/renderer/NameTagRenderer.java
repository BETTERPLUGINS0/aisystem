package advancedplugins.pm2.cv.models.api.model.rpc.joint.renderer.renderer;

import advancedplugins.pm2.cv.models.api.model.rpc.joint.renderer.BehaviorRenderer;
import advancedplugins.pm2.cv.models.api.utils.data.tracker.DataTracker;
import java.util.UUID;
import org.bukkit.entity.Display.Billboard;
import org.joml.Vector3f;

public interface NameTagRenderer extends BehaviorRenderer, RenderQueues<NameTagRenderer.NameTag> {
   public interface NameTag {
      int DEFAULT_BACKGROUND_COLOR = 1073741824;

      int getPivotId();

      UUID getPivotUuid();

      int getTagId();

      UUID getTagUuid();

      DataTracker<Vector3f> getPosition();

      DataTracker<String> getJsonString();

      DataTracker<Boolean> getVisibility();

      DataTracker<Integer> getBackgroundColor();

      DataTracker<Billboard> getBillboard();

      DataTracker<Byte> getTextOpacity();

      DataTracker<Integer> getLineWidth();

      DataTracker<Vector3f> getScale();

      DataTracker<Byte> getStyle();

      default boolean isDirty() {
         return this.getJsonString().isDirty() || this.getVisibility().isDirty() || this.getBillboard().isDirty() || this.getBackgroundColor().isDirty() || this.getTextOpacity().isDirty() || this.getLineWidth().isDirty() || this.getStyle().isDirty() || this.getScale().isDirty();
      }

      default void clearDirty() {
         this.getJsonString().clearDirty();
         this.getVisibility().clearDirty();
         this.getBillboard().clearDirty();
         this.getBackgroundColor().clearDirty();
         this.getTextOpacity().clearDirty();
         this.getLineWidth().clearDirty();
         this.getScale().clearDirty();
         this.getScale().clearDirty();
      }
   }
}
