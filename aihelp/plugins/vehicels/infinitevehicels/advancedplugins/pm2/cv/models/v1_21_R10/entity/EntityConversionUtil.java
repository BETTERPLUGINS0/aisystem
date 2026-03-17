package advancedplugins.pm2.cv.models.v1_21_R10.entity;

import net.minecraft.world.entity.Entity;
import org.bukkit.craftbukkit.v1_21_R7.entity.CraftEntity;

public class EntityConversionUtil {
   public static Entity toNMS(org.bukkit.entity.Entity var0) {
      return ((CraftEntity)var0).getHandle();
   }
}
