package advancedplugins.pm2.cv.api.service;

import org.bukkit.World;
import org.bukkit.entity.Entity;

public interface EntityService extends Service {
   void addPassenger(Entity var1, Entity var2);

   Entity getEntityById(int var1, World var2);
}
