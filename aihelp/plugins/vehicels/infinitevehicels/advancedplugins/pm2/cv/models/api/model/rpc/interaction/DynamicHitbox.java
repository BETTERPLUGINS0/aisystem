package advancedplugins.pm2.cv.models.api.model.rpc.interaction;

import advancedplugins.pm2.cv.models.api.ModelAPI;
import advancedplugins.pm2.cv.models.api.utils.data.tracker.DataTracker;
import java.util.UUID;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

public class DynamicHitbox {
   private static final int pivotId = ModelAPI.getEntityHandler().getNextEntityId();
   private static final UUID pivotUUID = UUID.randomUUID();
   private static final int hitboxId = ModelAPI.getEntityHandler().getNextEntityId();
   private static final UUID hitboxUUID = UUID.randomUUID();
   private final Player player;
   private final DataTracker<Vector> positionTracker = new DataTracker();
   private int target;

   public DynamicHitbox(Player var1, Vector var2) {
      this.player = var1;
      this.positionTracker.set(var2);
      ModelAPI.getEntityHandler().spawnDynamicHitbox(this);
      this.positionTracker.clearDirty();
   }

   public static int getPivotId() {
      return pivotId;
   }

   public static UUID getPivotUUID() {
      return pivotUUID;
   }

   public static int getHitboxId() {
      return hitboxId;
   }

   public static UUID getHitboxUUID() {
      return hitboxUUID;
   }

   public void update(Vector var1) {
      this.positionTracker.set(var1.setY(var1.getY()));
      if (this.positionTracker.isDirty()) {
         ModelAPI.getEntityHandler().updateDynamicHitbox(this);
         this.positionTracker.clearDirty();
      }

   }

   public void destroy() {
      ModelAPI.getEntityHandler().destroyDynamicHitbox(this);
   }

   public Player getPlayer() {
      return this.player;
   }

   public DataTracker<Vector> getPositionTracker() {
      return this.positionTracker;
   }

   public int getTarget() {
      return this.target;
   }

   public void setTarget(int var1) {
      this.target = var1;
   }
}
