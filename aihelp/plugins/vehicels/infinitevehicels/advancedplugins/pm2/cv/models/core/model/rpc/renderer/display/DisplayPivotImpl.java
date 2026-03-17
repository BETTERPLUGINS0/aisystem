package advancedplugins.pm2.cv.models.core.model.rpc.renderer.display;

import advancedplugins.pm2.cv.models.api.model.rpc.renderer.DisplayRenderer;
import advancedplugins.pm2.cv.models.api.utils.data.tracker.CollectionDataTracker;
import advancedplugins.pm2.cv.models.api.utils.data.tracker.DataTracker;
import advancedplugins.pm2.cv.models.api.utils.data.tracker.UpdateDataTracker;
import java.util.HashSet;
import java.util.UUID;
import lombok.Generated;
import org.bukkit.Location;
import org.joml.Vector3f;

public class DisplayPivotImpl implements DisplayRenderer.Pivot {
   private final int id;
   private final UUID uuid = UUID.randomUUID();
   private final DataTracker<Vector3f> position = new UpdateDataTracker(new Vector3f(), Vector3f::set);
   private final DataTracker<Float> yaw = new DataTracker();
   private final CollectionDataTracker<Integer> passengers = new CollectionDataTracker(new HashSet());

   public DisplayPivotImpl(int var1) {
      this.id = var1;
   }

   public void updatePosition(Location var1, float var2) {
      this.position.set((new Vector3f()).set(var1.getX(), var1.getY() + (double)var2, var1.getZ()));
   }

   public void clearDirty() {
      this.yaw.clearDirty();
      this.passengers.clearDirty();
   }

   @Generated
   public int getId() {
      return this.id;
   }

   @Generated
   public UUID getUuid() {
      return this.uuid;
   }

   @Generated
   public DataTracker<Vector3f> getPosition() {
      return this.position;
   }

   @Generated
   public DataTracker<Float> getYaw() {
      return this.yaw;
   }

   @Generated
   public CollectionDataTracker<Integer> getPassengers() {
      return this.passengers;
   }
}
