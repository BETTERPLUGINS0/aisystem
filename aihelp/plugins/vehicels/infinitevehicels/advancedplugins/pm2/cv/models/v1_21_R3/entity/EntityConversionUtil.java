package advancedplugins.pm2.cv.models.v1_21_R3.entity;

import net.minecraft.world.entity.Entity;
import org.bukkit.craftbukkit.v1_21_R3.entity.CraftEntity;

public class EntityConversionUtil {
   public static Entity toNMS(org.bukkit.entity.Entity bukkitEntity) {
      return ((CraftEntity)var0).getHandle();
   }
}
