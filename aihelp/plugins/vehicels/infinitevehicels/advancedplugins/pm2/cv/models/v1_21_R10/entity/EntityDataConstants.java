package advancedplugins.pm2.cv.models.v1_21_R10.entity;

import java.util.List;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData.DataValue;

class EntityDataConstants {
   static final List<DataValue<?>> AREA_EFFECT_CLOUD_DATA;
   static final List<DataValue<?>> SLIME_DATA;
   static final List<DataValue<?>> BAT_DATA;
   static final List<DataValue<?>> ARMOR_STAND_DATA;

   static {
      AREA_EFFECT_CLOUD_DATA = List.of(new DataValue(0, EntityDataSerializers.BYTE, (byte)32), new DataValue(1, EntityDataSerializers.INT, Integer.MAX_VALUE), new DataValue(8, EntityDataSerializers.FLOAT, 0.0F));
      SLIME_DATA = List.of(new DataValue(0, EntityDataSerializers.BYTE, (byte)32), new DataValue(1, EntityDataSerializers.INT, Integer.MAX_VALUE), new DataValue(16, EntityDataSerializers.INT, 2));
      BAT_DATA = List.of(new DataValue(0, EntityDataSerializers.BYTE, (byte)32), new DataValue(1, EntityDataSerializers.INT, Integer.MAX_VALUE));
      ARMOR_STAND_DATA = List.of(new DataValue(0, EntityDataSerializers.BYTE, (byte)32), new DataValue(1, EntityDataSerializers.INT, Integer.MAX_VALUE), new DataValue(15, EntityDataSerializers.BYTE, (byte)16));
   }
}
