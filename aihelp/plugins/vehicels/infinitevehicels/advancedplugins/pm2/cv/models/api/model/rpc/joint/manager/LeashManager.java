package advancedplugins.pm2.cv.models.api.model.rpc.joint.manager;

import advancedplugins.pm2.cv.models.api.model.rpc.joint.behavior.JointAction;
import advancedplugins.pm2.cv.models.api.model.rpc.joint.type.Leash;
import java.util.Map;
import java.util.Optional;
import org.bukkit.entity.Entity;

public interface LeashManager {
   <T extends Leash & JointAction> void registerLeash(T var1);

   <T extends Leash & JointAction> Map<String, T> getMainLeashes();

   <T extends Leash & JointAction> Map<String, T> getLeashes();

   <T extends Leash & JointAction> Optional<T> getLeash(String var1);

   void connectMainLeashes(Entity var1);

   void connectMainLeashes(String var1);

   void disconnectMainLeashes();

   void connectLeash(Entity var1, String var2);

   void connectLeash(String var1, String var2);

   void disconnect(String var1);

   Entity getLeashHolder(String var1);

   <T extends Leash & JointAction> T getLeashConnection(String var1);
}
