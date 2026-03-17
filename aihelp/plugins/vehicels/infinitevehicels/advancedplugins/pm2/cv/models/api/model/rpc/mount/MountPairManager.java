package advancedplugins.pm2.cv.models.api.model.rpc.mount;

import advancedplugins.pm2.cv.models.api.model.rpc.IVisualModel;
import advancedplugins.pm2.cv.models.api.model.rpc.joint.manager.MountManager;
import advancedplugins.pm2.cv.models.api.model.rpc.mount.controller.MountController;
import advancedplugins.pm2.cv.models.api.nms.entity.wrapper.MoveController;
import com.google.common.collect.Maps;
import it.unimi.dsi.fastutil.Pair;
import java.util.Iterator;
import java.util.Map;
import java.util.UUID;
import org.bukkit.entity.Entity;
import org.jetbrains.annotations.Nullable;

public class MountPairManager {
   private final Map<UUID, Pair<IVisualModel, MountController>> mountedPair = Maps.newConcurrentMap();

   public void updatePassengerPosition() {
      Iterator var1 = this.mountedPair.values().iterator();

      while(var1.hasNext()) {
         Pair var2 = (Pair)var1.next();
         MoveController var3 = ((IVisualModel)var2.left()).getModeledEntity().getBase().getMoveController();
         ((MountController)var2.right()).updateRiderPosition(var3);
      }

   }

   public void registerMountedPair(Entity var1, IVisualModel var2, MountController var3) {
      this.mountedPair.put(var1.getUniqueId(), Pair.of(var2, var3));
   }

   public void unregisterMountedPair(UUID var1) {
      this.mountedPair.remove(var1);
   }

   @Nullable
   public Pair<IVisualModel, MountController> get(UUID var1) {
      return (Pair)this.mountedPair.get(var1);
   }

   public IVisualModel getMountedPair(UUID var1) {
      Pair var2 = this.get(var1);
      return var2 == null ? null : (IVisualModel)var2.left();
   }

   public MountController getController(UUID var1) {
      Pair var2 = this.get(var1);
      return var2 == null ? null : (MountController)var2.right();
   }

   public void tryDismount(Entity var1) {
      IVisualModel var2 = this.getMountedPair(var1.getUniqueId());
      if (var2 != null) {
         var2.getMountManager().ifPresent((var1x) -> {
            ((MountManager)var1x).dismountRider(var1);
         });
      }

   }
}
