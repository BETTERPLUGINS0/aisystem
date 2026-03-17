package advancedplugins.pm2.cv.models.v1_20_R6.entity.fake;

import advancedplugins.pm2.cv.models.api.model.nrpc.nms.FakeBlockDisplayEntity;
import advancedplugins.pm2.cv.models.api.model.nrpc.nms.FakeDisplayEntity;
import advancedplugins.pm2.cv.models.api.model.nrpc.nms.FakeDisplayEntityManager;
import advancedplugins.pm2.cv.models.api.model.nrpc.nms.FakeItemDisplayEntity;
import java.util.Arrays;
import org.bukkit.Location;

public class FakeDisplayEntityManagerImpl extends FakeDisplayEntityManager {
   public <T extends FakeDisplayEntity> T spawn(Class<T> entityClass, Location initialSpawnLocation) {
      if (Arrays.stream(var1.getAnnotatedInterfaces()).anyMatch((var0) -> {
         return var0 instanceof FakeItemDisplayEntity;
      })) {
         return new FakeItemDisplayEntityImpl(var2);
      } else if (Arrays.stream(var1.getAnnotatedInterfaces()).anyMatch((var0) -> {
         return var0 instanceof FakeBlockDisplayEntity;
      })) {
         return new FakeBlockDisplayEntityImpl(var2);
      } else {
         throw new UnsupportedOperationException("Unsupported fake display entity type: " + var1.getName());
      }
   }
}
