package advancedplugins.pm2.cv.models.api.model.nrpc.nms;

import org.bukkit.Location;

public abstract class FakeDisplayEntityManager {
   public abstract <T extends FakeDisplayEntity> T spawn(Class<T> var1, Location var2);
}
