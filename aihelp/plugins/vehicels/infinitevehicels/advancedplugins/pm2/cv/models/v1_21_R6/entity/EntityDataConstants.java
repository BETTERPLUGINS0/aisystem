package advancedplugins.pm2.cv.models.v1_21_R6.entity;

import java.util.List;
import net.minecraft.network.syncher.DataWatcherRegistry;
import net.minecraft.network.syncher.DataWatcher.c;

class EntityDataConstants {
   static final List<c<?>> AREA_EFFECT_CLOUD_DATA;
   static final List<c<?>> SLIME_DATA;
   static final List<c<?>> BAT_DATA;
   static final List<c<?>> ARMOR_STAND_DATA;

   static {
      AREA_EFFECT_CLOUD_DATA = List.of(new c(0, DataWatcherRegistry.a, (byte)32), new c(1, DataWatcherRegistry.b, Integer.MAX_VALUE), new c(8, DataWatcherRegistry.d, 0.0F));
      SLIME_DATA = List.of(new c(0, DataWatcherRegistry.a, (byte)32), new c(1, DataWatcherRegistry.b, Integer.MAX_VALUE), new c(16, DataWatcherRegistry.b, 2));
      BAT_DATA = List.of(new c(0, DataWatcherRegistry.a, (byte)32), new c(1, DataWatcherRegistry.b, Integer.MAX_VALUE));
      ARMOR_STAND_DATA = List.of(new c(0, DataWatcherRegistry.a, (byte)32), new c(1, DataWatcherRegistry.b, Integer.MAX_VALUE), new c(15, DataWatcherRegistry.a, (byte)16));
   }
}
