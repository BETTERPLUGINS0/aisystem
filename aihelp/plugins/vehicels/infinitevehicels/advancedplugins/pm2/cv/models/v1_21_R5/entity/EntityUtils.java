package advancedplugins.pm2.cv.models.v1_21_R5.entity;

import java.util.List;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData.DataValue;
import net.minecraft.world.entity.Entity;
import org.bukkit.craftbukkit.entity.CraftEntity;

public class EntityUtils {
   public static final List<DataValue<?>> DEFAULT_AREA_EFFECT_CLOUD_DATA;
   public static final List<DataValue<?>> DEFAULT_SLIME_DATA;
   public static final List<DataValue<?>> DEFAULT_BAT_DATA;
   public static final List<DataValue<?>> DEFAULT_ARMOR_STAND_DATA;

   public static Entity nms(org.bukkit.entity.Entity var0) {
      return ((CraftEntity)var0).getHandle();
   }

   static {
      DEFAULT_AREA_EFFECT_CLOUD_DATA = List.of(new DataValue(0, EntityDataSerializers.BYTE, (byte)32), new DataValue(1, EntityDataSerializers.INT, Integer.MAX_VALUE), new DataValue(8, EntityDataSerializers.FLOAT, 0.0F));
      DEFAULT_SLIME_DATA = List.of(new DataValue(0, EntityDataSerializers.BYTE, (byte)32), new DataValue(1, EntityDataSerializers.INT, Integer.MAX_VALUE), new DataValue(16, EntityDataSerializers.INT, 2));
      DEFAULT_BAT_DATA = List.of(new DataValue(0, EntityDataSerializers.BYTE, (byte)32), new DataValue(1, EntityDataSerializers.INT, Integer.MAX_VALUE));
      DEFAULT_ARMOR_STAND_DATA = List.of(new DataValue(0, EntityDataSerializers.BYTE, (byte)32), new DataValue(1, EntityDataSerializers.INT, Integer.MAX_VALUE), new DataValue(15, EntityDataSerializers.BYTE, (byte)16));
   }
}
