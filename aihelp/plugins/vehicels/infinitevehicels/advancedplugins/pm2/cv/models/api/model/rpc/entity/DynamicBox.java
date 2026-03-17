package advancedplugins.pm2.cv.models.api.model.rpc.entity;

import advancedplugins.pm2.cv.models.api.utils.math.Box;
import org.bukkit.util.BoundingBox;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;

public class DynamicBox implements Box {
   private final BoundingBox box = new BoundingBox();

   public void reset() {
      this.box.resize(0.0D, 0.0D, 0.0D, 0.0D, 0.0D, 0.0D);
   }

   public void expand(@NotNull BoundingBox var1) {
      this.box.union(var1);
   }

   @NotNull
   public BoundingBox createBoundingBox(Vector var1) {
      return this.box.clone().shift(var1);
   }
}
