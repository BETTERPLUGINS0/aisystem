package advancedplugins.pm2.cv.models.v1_20_R4.entity;

import java.util.List;
import net.minecraft.network.syncher.DataWatcherRegistry;
import net.minecraft.network.syncher.DataWatcher.b;
import net.minecraft.world.entity.Entity;
import org.bukkit.craftbukkit.v1_20_R3.entity.CraftEntity;

public class EntityUtils {
   public static final List<b<?>> DEFAULT_AREA_EFFECT_CLOUD_DATA;
   public static final List<b<?>> DEFAULT_SLIME_DATA;
   public static final List<b<?>> LEASH_SLIME_DATA;
   public static final List<b<?>> DEFAULT_ARMOR_STAND_DATA;

   public static Entity nms(org.bukkit.entity.Entity entity) {
      return ((CraftEntity)var0).getHandle();
   }

   static {
      DEFAULT_AREA_EFFECT_CLOUD_DATA = List.of(new b(0, DataWatcherRegistry.a, (byte)32), new b(1, DataWatcherRegistry.b, Integer.MAX_VALUE), new b(8, DataWatcherRegistry.d, 0.0F));
      DEFAULT_SLIME_DATA = List.of(new b(0, DataWatcherRegistry.a, (byte)32), new b(1, DataWatcherRegistry.b, Integer.MAX_VALUE), new b(16, DataWatcherRegistry.b, 2));
      LEASH_SLIME_DATA = List.of(new b(0, DataWatcherRegistry.a, (byte)32), new b(1, DataWatcherRegistry.b, Integer.MAX_VALUE), new b(16, DataWatcherRegistry.b, 0));
      DEFAULT_ARMOR_STAND_DATA = List.of(new b(0, DataWatcherRegistry.a, (byte)32), new b(1, DataWatcherRegistry.b, Integer.MAX_VALUE), new b(15, DataWatcherRegistry.a, (byte)16));
   }
}
