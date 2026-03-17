package advancedplugins.pm2.cv.models.v1_21_R10.entity;

import java.util.List;
import net.minecraft.network.syncher.SynchedEntityData.DataValue;
import net.minecraft.world.entity.Entity;

public class EntityUtils extends EntityConversionUtil {
   public static final List<DataValue<?>> DEFAULT_AREA_EFFECT_CLOUD_DATA;
   public static final List<DataValue<?>> DEFAULT_SLIME_DATA;
   public static final List<DataValue<?>> DEFAULT_BAT_DATA;
   public static final List<DataValue<?>> DEFAULT_ARMOR_STAND_DATA;

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
