package advancedplugins.pm2.cv.models.core.model.rpc.joint.manager;

import advancedplugins.pm2.cv.models.api.model.rpc.IVisualModel;
import advancedplugins.pm2.cv.models.api.model.rpc.joint.behavior.JointAction;
import advancedplugins.pm2.cv.models.api.model.rpc.joint.behavior.JointActionType;
import advancedplugins.pm2.cv.models.api.model.rpc.joint.manager.AbstractBehaviorManager;
import advancedplugins.pm2.cv.models.api.model.rpc.joint.manager.LeashManager;
import advancedplugins.pm2.cv.models.api.model.rpc.joint.type.Leash;
import advancedplugins.pm2.cv.models.core.model.rpc.joint.manager.behavior.LeashImpl;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import org.bukkit.entity.Entity;

public class LeashManagerImpl extends AbstractBehaviorManager<LeashImpl> implements LeashManager {
   private final Map<String, ?> main = new LinkedHashMap();
   private final Map<String, ?> leashes = new LinkedHashMap();

   public LeashManagerImpl(IVisualModel var1, JointActionType<LeashImpl> var2) {
      super(var1, var2);
   }

   public <T extends Leash & JointAction> void registerLeash(T var1) {
      this.getLeashes().put(((JointAction)var1).getJoint().getUniqueJointId(), (JointAction)var1);
      if (var1.isMainLeash()) {
         this.getMainLeashes().put(((JointAction)var1).getJoint().getUniqueJointId(), (JointAction)var1);
      }

   }

   public <T extends Leash & JointAction> Map<String, T> getMainLeashes() {
      return this.main;
   }

   public <T extends Leash & JointAction> Map<String, T> getLeashes() {
      return this.leashes;
   }

   public <T extends Leash & JointAction> Optional<T> getLeash(String var1) {
      return Optional.ofNullable((Leash)this.getLeashes().get(var1));
   }

   public void connectMainLeashes(Entity var1) {
      this.getMainLeashes().values().forEach((var1x) -> {
         ((Leash)var1x).connect(var1);
      });
   }

   public void connectMainLeashes(String var1) {
      this.getLeash(var1).ifPresent((var1x) -> {
         this.getMainLeashes().values().forEach((var1) -> {
            ((Leash)var1).connect((Leash)var1x);
         });
      });
   }

   public void disconnectMainLeashes() {
      this.getMainLeashes().values().forEach((var0) -> {
         ((Leash)var0).disconnect();
      });
   }

   public void connectLeash(Entity var1, String var2) {
      this.getLeash(var2).ifPresent((var1x) -> {
         ((Leash)var1x).connect(var1);
      });
   }

   public void connectLeash(String var1, String var2) {
      this.getLeash(var2).ifPresent((var2x) -> {
         Optional var10000 = this.getLeash(var1);
         Leash var10001 = (Leash)var2x;
         Objects.requireNonNull((Leash)var2x);
         var10000.ifPresent((var1x) -> {
            var10001.connect((Leash)var1x);
         });
      });
   }

   public void disconnect(String var1) {
      this.getLeash(var1).ifPresent((var0) -> {
         ((Leash)var0).disconnect();
      });
   }

   public Entity getLeashHolder(String var1) {
      return (Entity)this.getLeash(var1).map((var0) -> {
         return ((Leash)var0).getConnectedEntity();
      }).orElse((Object)null);
   }

   public <T extends Leash & JointAction> T getLeashConnection(String var1) {
      return (Leash)this.getLeash(var1).map((var0) -> {
         return (JointAction)((Leash)var0).getConnectedLeash();
      }).orElse((Object)null);
   }
}
