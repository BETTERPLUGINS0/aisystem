package advancedplugins.pm2.cv.models.api.model.rpc.joint.type;

import advancedplugins.pm2.cv.models.api.model.rpc.joint.behavior.JointAction;
import org.bukkit.entity.Entity;
import org.joml.Vector3f;

public interface Leash {
   boolean isMainLeash();

   int getId();

   Vector3f getLocation();

   void connect(Entity var1);

   <T extends Leash & JointAction> void connect(T var1);

   void disconnect();

   Entity getConnectedEntity();

   <T extends Leash & JointAction> T getConnectedLeash();
}
