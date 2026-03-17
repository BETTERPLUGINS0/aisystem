package advancedplugins.pm2.cv.models.core.model.rpc.renderer.display;

import advancedplugins.pm2.cv.models.api.model.rpc.renderer.DisplayRenderer;
import advancedplugins.pm2.cv.models.api.utils.data.tracker.DataTracker;
import advancedplugins.pm2.cv.models.api.utils.data.tracker.UpdateDataTracker;
import java.util.UUID;
import lombok.Generated;
import org.bukkit.Location;
import org.joml.Vector3f;

public class DisplayHitboxImpl implements DisplayRenderer.Hitbox {
   private final int pivotId;
   private final UUID pivotUuid = UUID.randomUUID();
   private final int hitboxId;
   private final UUID hitboxUuid = UUID.randomUUID();
   private final int shadowId;
   private final UUID shadowUuid = UUID.randomUUID();
   private final DataTracker<Vector3f> position = new UpdateDataTracker(new Vector3f(), Vector3f::set);
   private final DataTracker<Float> width = new DataTracker();
   private final DataTracker<Float> height = new DataTracker();
   private final DataTracker<Float> shadowRadius = new DataTracker();
   private final DataTracker<Boolean> hitboxVisible = new DataTracker();
   private final DataTracker<Boolean> shadowVisible = new DataTracker();

   public DisplayHitboxImpl(int var1, int var2, int var3) {
      this.pivotId = var1;
      this.hitboxId = var2;
      this.shadowId = var3;
   }

   public void updatePosition(Location var1) {
      this.position.set((new Vector3f()).set(var1.getX(), var1.getY(), var1.getZ()));
   }

   public void clearDirty() {
      this.width.clearDirty();
      this.height.clearDirty();
      this.shadowRadius.clearDirty();
      this.hitboxVisible.clearDirty();
      this.shadowVisible.clearDirty();
   }

   @Generated
   public int getPivotId() {
      return this.pivotId;
   }

   @Generated
   public UUID getPivotUuid() {
      return this.pivotUuid;
   }

   @Generated
   public int getHitboxId() {
      return this.hitboxId;
   }

   @Generated
   public UUID getHitboxUuid() {
      return this.hitboxUuid;
   }

   @Generated
   public int getShadowId() {
      return this.shadowId;
   }

   @Generated
   public UUID getShadowUuid() {
      return this.shadowUuid;
   }

   @Generated
   public DataTracker<Vector3f> getPosition() {
      return this.position;
   }

   @Generated
   public DataTracker<Float> getWidth() {
      return this.width;
   }

   @Generated
   public DataTracker<Float> getHeight() {
      return this.height;
   }

   @Generated
   public DataTracker<Float> getShadowRadius() {
      return this.shadowRadius;
   }

   @Generated
   public DataTracker<Boolean> getHitboxVisible() {
      return this.hitboxVisible;
   }

   @Generated
   public DataTracker<Boolean> getShadowVisible() {
      return this.shadowVisible;
   }
}
