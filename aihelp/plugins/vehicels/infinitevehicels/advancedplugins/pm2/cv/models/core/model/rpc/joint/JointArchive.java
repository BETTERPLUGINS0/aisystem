package advancedplugins.pm2.cv.models.core.model.rpc.joint;

import advancedplugins.pm2.cv.models.api.model.rpc.joint.IJoint;
import advancedplugins.pm2.cv.models.api.utils.archive.AbstractArchive;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class JointArchive extends AbstractArchive<IJoint> {
   protected Map<String, IJoint> mapSupplier() {
      return new ConcurrentHashMap();
   }
}
