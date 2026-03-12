package ac.grim.grimac.shaded.incendo.cloud.bukkit.internal;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import org.apiguardian.api.API;
import org.apiguardian.api.API.Status;
import org.checkerframework.checker.nullness.qual.Nullable;

@API(
   status = Status.INTERNAL,
   consumers = {"ac.grim.grimac.shaded.incendo.cloud.*"}
)
public final class CommandBuildContextSupplier {
   private static final Class<?> COMMAND_BUILD_CONTEXT_CLASS = CraftBukkitReflection.needMCClass("commands.CommandBuildContext");
   @Nullable
   private static final Constructor<?> COMMAND_BUILD_CONTEXT_CTR;
   @Nullable
   private static final Method CREATE_CONTEXT_METHOD;
   @Nullable
   private static final Method GET_WORLD_DATA_METHOD;
   @Nullable
   private static final Method GET_FEATURE_FLAGS_METHOD;
   private static final Class<?> REG_ACC_CLASS;
   private static final Class<?> MC_SERVER_CLASS = CraftBukkitReflection.needNMSClassOrElse("MinecraftServer", "net.minecraft.server.MinecraftServer");
   private static final Method GET_SERVER_METHOD;
   private static final Method REGISTRY_ACCESS;

   private CommandBuildContextSupplier() {
   }

   public static Object commandBuildContext() {
      Object server;
      if (COMMAND_BUILD_CONTEXT_CTR != null) {
         try {
            server = GET_SERVER_METHOD.invoke((Object)null);
            return COMMAND_BUILD_CONTEXT_CTR.newInstance(REGISTRY_ACCESS.invoke(server));
         } catch (ReflectiveOperationException var3) {
            throw new RuntimeException(var3);
         }
      } else if (CREATE_CONTEXT_METHOD != null && GET_WORLD_DATA_METHOD != null && GET_FEATURE_FLAGS_METHOD != null) {
         try {
            server = GET_SERVER_METHOD.invoke((Object)null);
            Object worldData = GET_WORLD_DATA_METHOD.invoke(server);
            Object flags = GET_FEATURE_FLAGS_METHOD.invoke(worldData);
            return CREATE_CONTEXT_METHOD.invoke((Object)null, REGISTRY_ACCESS.invoke(server), flags);
         } catch (ReflectiveOperationException var4) {
            throw new RuntimeException(var4);
         }
      } else {
         throw new IllegalStateException();
      }
   }

   static {
      Constructor ctr;
      try {
         ctr = COMMAND_BUILD_CONTEXT_CLASS.getDeclaredConstructors()[0];
      } catch (Exception var5) {
         ctr = null;
      }

      COMMAND_BUILD_CONTEXT_CTR = ctr;
      if (COMMAND_BUILD_CONTEXT_CTR == null) {
         List<Method> matchingFactoryMethods = (List)Arrays.stream(COMMAND_BUILD_CONTEXT_CLASS.getDeclaredMethods()).filter((it) -> {
            return it.getParameterCount() == 2 && COMMAND_BUILD_CONTEXT_CLASS.isAssignableFrom(it.getReturnType()) && Modifier.isStatic(it.getModifiers());
         }).collect(Collectors.toList());
         if (matchingFactoryMethods.size() == 1) {
            CREATE_CONTEXT_METHOD = (Method)matchingFactoryMethods.get(0);
         } else {
            if (matchingFactoryMethods.size() <= 1) {
               throw new IllegalStateException("Could not find CommandBuildContext factory method");
            }

            CREATE_CONTEXT_METHOD = (Method)matchingFactoryMethods.get(1);
         }

         Class<?> worldDataCls = (Class)CraftBukkitReflection.firstNonNullOrThrow(() -> {
            return "Could not find WorldData class";
         }, CraftBukkitReflection.findMCClass("world.level.storage.SaveData"), CraftBukkitReflection.findMCClass("world.level.storage.WorldData"));
         GET_WORLD_DATA_METHOD = (Method)Arrays.stream(MC_SERVER_CLASS.getDeclaredMethods()).filter((it) -> {
            return it.getParameterCount() == 0 && !Modifier.isStatic(it.getModifiers()) && it.getReturnType().equals(worldDataCls);
         }).findFirst().orElseThrow(() -> {
            return new IllegalStateException("Could not find MinecraftServer#getWorldData method");
         });
         Class<?> featureFlagSetCls = CraftBukkitReflection.needMCClass("world.flag.FeatureFlagSet");
         GET_FEATURE_FLAGS_METHOD = (Method)Arrays.stream(worldDataCls.getDeclaredMethods()).filter((it) -> {
            return it.getParameterCount() == 0 && it.getReturnType().equals(featureFlagSetCls) && !Modifier.isStatic(it.getModifiers());
         }).findFirst().orElseThrow(() -> {
            return new IllegalStateException("Could not find enabledFeatures method");
         });
      } else {
         CREATE_CONTEXT_METHOD = null;
         GET_WORLD_DATA_METHOD = null;
         GET_FEATURE_FLAGS_METHOD = null;
      }

      REG_ACC_CLASS = COMMAND_BUILD_CONTEXT_CTR != null ? COMMAND_BUILD_CONTEXT_CTR.getParameterTypes()[0] : CREATE_CONTEXT_METHOD.getParameterTypes()[0];
      REGISTRY_ACCESS = (Method)Arrays.stream(MC_SERVER_CLASS.getDeclaredMethods()).filter((m) -> {
         return REG_ACC_CLASS.isAssignableFrom(m.getReturnType());
      }).findFirst().orElseThrow(() -> {
         return new IllegalStateException("Cannot find MinecraftServer#registryAccess");
      });

      try {
         GET_SERVER_METHOD = MC_SERVER_CLASS.getDeclaredMethod("getServer");
      } catch (NoSuchMethodException var4) {
         throw new RuntimeException(var4);
      }
   }
}
