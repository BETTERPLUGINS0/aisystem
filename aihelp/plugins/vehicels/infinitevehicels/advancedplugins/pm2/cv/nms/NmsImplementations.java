package advancedplugins.pm2.cv.nms;

import advancedplugins.pm2.cv.api.enums.MinecraftVersion;
import advancedplugins.pm2.cv.api.service.BlockInfoService;
import advancedplugins.pm2.cv.api.service.EmptyChunkGeneratorService;
import advancedplugins.pm2.cv.api.service.EntityService;
import advancedplugins.pm2.cv.api.service.TexturedHeadService;
import advancedplugins.pm2.cv.damage.DamageHitbox;
import advancedplugins.pm2.cv.fake.armorstand.FakeArmorStandHandle;
import advancedplugins.pm2.cv.fake.display.FakeDisplayBlockHandle;
import advancedplugins.pm2.cv.fake.display.FakeDisplayItemHandle;
import advancedplugins.pm2.cv.fake.display.FakeDisplayTextHandle;
import advancedplugins.pm2.cv.locator.ArmorStandLocator;
import advancedplugins.pm2.cv.service.PacketService;
import gnu.trove.map.hash.THashMap;
import java.lang.reflect.InvocationTargetException;
import java.util.Collections;
import java.util.Map;
import java.util.Set;
import org.apache.commons.lang.StringUtils;

public final class NmsImplementations {
   private static final String IMPLEMENTATION_PATH_FORMAT = "implementation.%s.%s.%s";
   private static final Map<Class<?>, String> IMPLEMENTATION_PACKAGE_MAP = new THashMap();
   private static final Map<Class<?>, Class<?>> IMPLEMENTATION_MAP = new THashMap();
   private static final Map<Class<?>, Object> CACHED_INSTANCES = new THashMap();

   private static void registerSuper(Class<?> superClass, String subPackage) {
      IMPLEMENTATION_PACKAGE_MAP.put(var0, var1);
   }

   public static Set<Class<?>> getTypes() {
      return Collections.unmodifiableSet(IMPLEMENTATION_PACKAGE_MAP.keySet());
   }

   public static Class<?> getImplementation(Class<?> clazz) {
      String var1 = (String)IMPLEMENTATION_PACKAGE_MAP.get(var0);
      if (StringUtils.isBlank(var1)) {
         throw new IllegalArgumentException();
      } else {
         Class var2 = (Class)IMPLEMENTATION_MAP.get(var0);
         if (var2 == null) {
            try {
               var2 = Class.forName(String.format("implementation.%s.%s.%s", MinecraftVersion.getVersion().getPackageName(), var1, var0.getSimpleName()));
            } catch (ClassNotFoundException var4) {
               throw new IllegalArgumentException(var4);
            }

            IMPLEMENTATION_MAP.put(var0, var2);
         }

         return var2;
      }
   }

   public static <T> T getSingleInstanceImplementation(Class<T> clazz) {
      Object var1 = CACHED_INSTANCES.get(var0);
      if (var1 != null) {
         return var0.cast(var1);
      } else {
         Class var2;
         try {
            var2 = getImplementation(var0);
         } catch (NoClassDefFoundError var6) {
            var6.printStackTrace();
            return null;
         }

         try {
            var1 = var2.getDeclaredConstructor().newInstance();
            CACHED_INSTANCES.put(var0, var1);
            return var0.cast(var1);
         } catch (IllegalAccessException | InvocationTargetException | InstantiationException var4) {
            throw new RuntimeException(var4);
         } catch (NoSuchMethodException var5) {
            throw new IllegalStateException("single instance implementation was expected to have a non-args constructor");
         }
      }
   }

   static {
      registerSuper(PacketService.class, "service");
      registerSuper(BlockInfoService.class, "service");
      registerSuper(EmptyChunkGeneratorService.class, "service");
      registerSuper(TexturedHeadService.class, "service");
      registerSuper(EntityService.class, "service");
      registerSuper(ArmorStandLocator.class, "locator");
      registerSuper(FakeArmorStandHandle.class, "fake");
      registerSuper(FakeDisplayBlockHandle.class, "fake");
      registerSuper(FakeDisplayItemHandle.class, "fake");
      registerSuper(FakeDisplayTextHandle.class, "fake");
      registerSuper(DamageHitbox.class, "damage");
   }
}
