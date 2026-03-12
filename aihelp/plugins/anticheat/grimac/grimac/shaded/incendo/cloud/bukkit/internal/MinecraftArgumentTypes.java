package ac.grim.grimac.shaded.incendo.cloud.bukkit.internal;

import com.google.common.base.Suppliers;
import com.mojang.brigadier.arguments.ArgumentType;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.function.Supplier;
import org.apiguardian.api.API;
import org.apiguardian.api.API.Status;
import org.bukkit.NamespacedKey;
import org.checkerframework.checker.nullness.qual.NonNull;

@API(
   status = Status.INTERNAL,
   consumers = {"ac.grim.grimac.shaded.incendo.cloud.*"}
)
public final class MinecraftArgumentTypes {
   private static final MinecraftArgumentTypes.ArgumentTypeGetter ARGUMENT_TYPE_GETTER;

   private MinecraftArgumentTypes() {
   }

   public static Class<? extends ArgumentType<?>> getClassByKey(@NonNull final NamespacedKey key) throws IllegalArgumentException {
      return ARGUMENT_TYPE_GETTER.getClassByKey(key);
   }

   static {
      if (CraftBukkitReflection.classExists("org.bukkit.entity.Warden")) {
         ARGUMENT_TYPE_GETTER = new MinecraftArgumentTypes.ArgumentTypeGetterImpl();
      } else {
         ARGUMENT_TYPE_GETTER = new MinecraftArgumentTypes.LegacyArgumentTypeGetter();
      }

   }

   private interface ArgumentTypeGetter {
      Class<? extends ArgumentType<?>> getClassByKey(@NonNull NamespacedKey key) throws IllegalArgumentException;
   }

   private static final class ArgumentTypeGetterImpl implements MinecraftArgumentTypes.ArgumentTypeGetter {
      private final Supplier<Object> argumentRegistry;
      private final Map<?, ?> byClassMap;

      private ArgumentTypeGetterImpl() {
         this.argumentRegistry = Suppliers.memoize(() -> {
            return RegistryReflection.builtInRegistryByName("command_argument_type");
         });

         try {
            Field declaredField = CraftBukkitReflection.needMCClass("commands.synchronization.ArgumentTypeInfos").getDeclaredFields()[0];
            declaredField.setAccessible(true);
            this.byClassMap = (Map)declaredField.get((Object)null);
         } catch (ReflectiveOperationException var2) {
            throw new RuntimeException(var2);
         }
      }

      public Class<? extends ArgumentType<?>> getClassByKey(@NonNull final NamespacedKey key) throws IllegalArgumentException {
         Object argTypeInfo = RegistryReflection.get(this.argumentRegistry.get(), key.getNamespace() + ":" + key.getKey());
         Iterator var3 = this.byClassMap.entrySet().iterator();

         Entry entry;
         do {
            if (!var3.hasNext()) {
               throw new IllegalArgumentException(key.toString());
            }

            entry = (Entry)var3.next();
         } while(entry.getValue() != argTypeInfo);

         return (Class)entry.getKey();
      }

      // $FF: synthetic method
      ArgumentTypeGetterImpl(Object x0) {
         this();
      }
   }

   private static final class LegacyArgumentTypeGetter implements MinecraftArgumentTypes.ArgumentTypeGetter {
      private static final Constructor<?> MINECRAFT_KEY_CONSTRUCTOR;
      private static final Method ARGUMENT_REGISTRY_GET_BY_KEY_METHOD;
      private static final Field BY_CLASS_MAP_FIELD;

      private LegacyArgumentTypeGetter() {
      }

      public Class<? extends ArgumentType<?>> getClassByKey(@NonNull final NamespacedKey key) throws IllegalArgumentException {
         try {
            Object minecraftKey = MINECRAFT_KEY_CONSTRUCTOR.newInstance(key.getNamespace(), key.getKey());
            Object entry = ARGUMENT_REGISTRY_GET_BY_KEY_METHOD.invoke((Object)null, minecraftKey);
            if (entry == null) {
               throw new IllegalArgumentException(key.toString());
            } else {
               Map<Class<?>, Object> map = (Map)BY_CLASS_MAP_FIELD.get((Object)null);
               Iterator var5 = map.entrySet().iterator();

               Entry mapEntry;
               do {
                  if (!var5.hasNext()) {
                     throw new IllegalArgumentException(key.toString());
                  }

                  mapEntry = (Entry)var5.next();
               } while(mapEntry.getValue() != entry);

               return (Class)mapEntry.getKey();
            }
         } catch (ReflectiveOperationException var7) {
            throw new RuntimeException(var7);
         }
      }

      // $FF: synthetic method
      LegacyArgumentTypeGetter(Object x0) {
         this();
      }

      static {
         try {
            Class minecraftKey;
            Class argumentRegistry;
            if (CraftBukkitReflection.findMCClass("resources.ResourceLocation") != null) {
               minecraftKey = CraftBukkitReflection.needMCClass("resources.ResourceLocation");
               argumentRegistry = CraftBukkitReflection.needMCClass("commands.synchronization.ArgumentTypes");
            } else {
               minecraftKey = CraftBukkitReflection.needNMSClassOrElse("MinecraftKey", "net.minecraft.resources.MinecraftKey");
               argumentRegistry = CraftBukkitReflection.needNMSClassOrElse("ArgumentRegistry", "net.minecraft.commands.synchronization.ArgumentRegistry");
            }

            MINECRAFT_KEY_CONSTRUCTOR = minecraftKey.getConstructor(String.class, String.class);
            MINECRAFT_KEY_CONSTRUCTOR.setAccessible(true);
            ARGUMENT_REGISTRY_GET_BY_KEY_METHOD = (Method)Arrays.stream(argumentRegistry.getDeclaredMethods()).filter((method) -> {
               return method.getParameterCount() == 1;
            }).filter((method) -> {
               return minecraftKey.equals(method.getParameterTypes()[0]);
            }).findFirst().orElseThrow(NoSuchMethodException::new);
            ARGUMENT_REGISTRY_GET_BY_KEY_METHOD.setAccessible(true);
            BY_CLASS_MAP_FIELD = (Field)Arrays.stream(argumentRegistry.getDeclaredFields()).filter((field) -> {
               return Modifier.isStatic(field.getModifiers());
            }).filter((field) -> {
               return field.getType().equals(Map.class);
            }).filter((field) -> {
               ParameterizedType parameterizedType = (ParameterizedType)field.getGenericType();
               Type param = parameterizedType.getActualTypeArguments()[0];
               return !(param instanceof ParameterizedType) ? false : ((ParameterizedType)param).getRawType().equals(Class.class);
            }).findFirst().orElseThrow(NoSuchFieldException::new);
            BY_CLASS_MAP_FIELD.setAccessible(true);
         } catch (ReflectiveOperationException var2) {
            throw new ExceptionInInitializerError(var2);
         }
      }
   }
}
