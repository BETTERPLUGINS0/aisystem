package advancedplugins.pm2.cv.models.v1_21_R1.entity.fake;

import advancedplugins.pm2.cv.models.api.model.nrpc.nms.FakeBlockDisplayEntity;
import advancedplugins.pm2.cv.models.api.model.nrpc.nms.FakeDisplayEntity;
import advancedplugins.pm2.cv.models.api.model.nrpc.nms.FakeDisplayEntityManager;
import advancedplugins.pm2.cv.models.api.model.nrpc.nms.FakeItemDisplayEntity;
import org.bukkit.Location;

public class FakeDisplayEntityManagerImpl extends FakeDisplayEntityManager {
   public <T extends FakeDisplayEntity> T spawn(Class<T> entityClass, Location initialSpawnLocation) {
      if (var1 == FakeItemDisplayEntity.class) {
         return new FakeItemDisplayEntityImpl(var2);
      } else if (var1 == FakeBlockDisplayEntity.class) {
         return new FakeBlockDisplayEntityImpl(var2);
      } else {
         throw new UnsupportedOperationException("Unsupported fake display entity type: " + var1.getName());
      }
   }
}
