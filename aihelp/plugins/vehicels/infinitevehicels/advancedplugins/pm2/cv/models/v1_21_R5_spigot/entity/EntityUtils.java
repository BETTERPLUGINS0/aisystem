package advancedplugins.pm2.cv.models.v1_21_R5_spigot.entity;

import java.util.List;
import net.minecraft.network.syncher.DataWatcher.c;
import net.minecraft.world.entity.Entity;

public class EntityUtils extends EntityConversionUtil {
   public static final List<c<?>> DEFAULT_AREA_EFFECT_CLOUD_DATA;
   public static final List<c<?>> DEFAULT_SLIME_DATA;
   public static final List<c<?>> DEFAULT_BAT_DATA;
   public static final List<c<?>> DEFAULT_ARMOR_STAND_DATA;

   public static Entity nms(org.bukkit.entity.Entity var0) {
      return toNMS(var0);
   }

   static {
      DEFAULT_AREA_EFFECT_CLOUD_DATA = EntityDataConstants.AREA_EFFECT_CLOUD_DATA;
      DEFAULT_SLIME_DATA = EntityDataConstants.SLIME_DATA;
      DEFAULT_BAT_DATA = EntityDataConstants.BAT_DATA;
      DEFAULT_ARMOR_STAND_DATA = EntityDataConstants.ARMOR_STAND_DATA;
   }
}
