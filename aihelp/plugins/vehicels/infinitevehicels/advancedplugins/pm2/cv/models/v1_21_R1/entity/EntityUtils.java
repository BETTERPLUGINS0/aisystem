package advancedplugins.pm2.cv.models.v1_21_R1.entity;

import java.util.List;
import net.minecraft.network.syncher.DataWatcherRegistry;
import net.minecraft.network.syncher.DataWatcher.c;
import net.minecraft.world.entity.Entity;
import org.bukkit.craftbukkit.v1_21_R1.entity.CraftEntity;

public class EntityUtils {
   public static final List<c<?>> DEFAULT_AREA_EFFECT_CLOUD_DATA;
   public static final List<c<?>> DEFAULT_SLIME_DATA;
   public static final List<c<?>> DEFAULT_BAT_DATA;
   public static final List<c<?>> DEFAULT_ARMOR_STAND_DATA;

   public static Entity nms(org.bukkit.entity.Entity entity) {
      return ((CraftEntity)var0).getHandle();
   }

   static {
      DEFAULT_AREA_EFFECT_CLOUD_DATA = List.of(new c(0, DataWatcherRegistry.a, (byte)32), new c(1, DataWatcherRegistry.b, Integer.MAX_VALUE), new c(8, DataWatcherRegistry.d, 0.0F));
      DEFAULT_SLIME_DATA = List.of(new c(0, DataWatcherRegistry.a, (byte)32), new c(1, DataWatcherRegistry.b, Integer.MAX_VALUE), new c(16, DataWatcherRegistry.b, 2));
      DEFAULT_BAT_DATA = List.of(new c(0, DataWatcherRegistry.a, (byte)32), new c(1, DataWatcherRegistry.b, Integer.MAX_VALUE));
      DEFAULT_ARMOR_STAND_DATA = List.of(new c(0, DataWatcherRegistry.a, (byte)32), new c(1, DataWatcherRegistry.b, Integer.MAX_VALUE), new c(15, DataWatcherRegistry.a, (byte)16));
   }
}
