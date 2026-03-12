package ac.grim.grimac.shaded.incendo.cloud.bukkit.internal;

import ac.grim.grimac.shaded.geantyref.GenericTypeReflector;
import java.lang.reflect.Executable;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.WildcardType;
import java.util.Arrays;
import java.util.Objects;
import org.apiguardian.api.API;
import org.apiguardian.api.API.Status;
import org.checkerframework.checker.nullness.qual.Nullable;

@API(
   status = Status.INTERNAL,
   consumers = {"ac.grim.grimac.shaded.incendo.cloud.*"}
)
public final class RegistryReflection {
   @Nullable
   public static final Field REGISTRY_REGISTRY;
   @Nullable
   public static final Method REGISTRY_GET;
   @Nullable
   public static final Method REGISTRY_KEY;
   private static final Class<?> IDENTIFIER_CLASS = CraftBukkitReflection.needNMSClassOrElse("MinecraftKey", "net.minecraft.resources.MinecraftKey", "net.minecraft.resources.ResourceLocation", "net.minecraft.resources.Identifier");
   private static final Class<?> RESOURCE_KEY_CLASS = CraftBukkitReflection.needNMSClassOrElse("ResourceKey", "net.minecraft.resources.ResourceKey");
   private static final Executable NEW_RESOURCE_LOCATION;
   private static final Executable CREATE_REGISTRY_RESOURCE_KEY;

   private RegistryReflection() {
   }

   public static Object registryKey(final String registryName) {
      Objects.requireNonNull(CREATE_REGISTRY_RESOURCE_KEY, "CREATE_REGISTRY_RESOURCE_KEY");

      try {
         Object resourceLocation = createResourceLocation(registryName);
         return CraftBukkitReflection.invokeConstructorOrStaticMethod(CREATE_REGISTRY_RESOURCE_KEY, resourceLocation);
      } catch (ReflectiveOperationException var2) {
         throw new RuntimeException(var2);
      }
   }

   public static Object get(final Object registry, final String resourceLocation) {
      Objects.requireNonNull(REGISTRY_GET, "REGISTRY_GET");

      try {
         return REGISTRY_GET.invoke(registry, createResourceLocation(resourceLocation));
      } catch (ReflectiveOperationException var3) {
         throw new RuntimeException(var3);
      }
   }

   public static Object builtInRegistryByName(final String name) {
      Objects.requireNonNull(REGISTRY_REGISTRY, "REGISTRY_REGISTRY");

      try {
         return get(REGISTRY_REGISTRY.get((Object)null), name);
      } catch (ReflectiveOperationException var2) {
         throw new RuntimeException(var2);
      }
   }

   public static Object createResourceLocation(final String str) {
      try {
         return CraftBukkitReflection.invokeConstructorOrStaticMethod(NEW_RESOURCE_LOCATION, str);
      } catch (ReflectiveOperationException var2) {
         throw new RuntimeException(var2);
      }
   }

   private static Field registryRegistryField(final Class<?> registryClass) {
      return (Field)Arrays.stream(registryClass.getDeclaredFields()).filter((it) -> {
         return it.getType().equals(registryClass);
      }).findFirst().orElseGet(() -> {
         return registryRegistryFieldFromBuiltInRegistries(registryClass);
      });
   }

   private static Field registryRegistryFieldFromBuiltInRegistries(final Class<?> registryClass) {
      Class<?> builtInRegistriesClass = CraftBukkitReflection.needMCClass("core.registries.BuiltInRegistries");
      return (Field)Arrays.stream(builtInRegistriesClass.getDeclaredFields()).filter((it) -> {
         if (it.getType().equals(registryClass) && Modifier.isStatic(it.getModifiers())) {
            Type genericType = it.getGenericType();
            if (!(genericType instanceof ParameterizedType)) {
               return false;
            } else {
               Type valueType;
               for(valueType = ((ParameterizedType)genericType).getActualTypeArguments()[0]; valueType instanceof WildcardType; valueType = ((WildcardType)valueType).getUpperBounds()[0]) {
               }

               return GenericTypeReflector.erase(valueType).equals(registryClass);
            }
         } else {
            return false;
         }
      }).findFirst().orElseThrow(() -> {
         return new IllegalStateException("Could not find Registry Registry field");
      });
   }

   static {
      if (CraftBukkitReflection.MAJOR_REVISION < 17) {
         REGISTRY_REGISTRY = null;
         REGISTRY_GET = null;
         REGISTRY_KEY = null;
         NEW_RESOURCE_LOCATION = null;
         CREATE_REGISTRY_RESOURCE_KEY = null;
      } else {
         Class<?> registryClass = (Class)CraftBukkitReflection.firstNonNullOrThrow(() -> {
            return "Registry";
         }, CraftBukkitReflection.findMCClass("core.IRegistry"), CraftBukkitReflection.findMCClass("core.Registry"));
         REGISTRY_REGISTRY = registryRegistryField(registryClass);
         REGISTRY_REGISTRY.setAccessible(true);
         REGISTRY_GET = (Method)Arrays.stream(registryClass.getDeclaredMethods()).filter((it) -> {
            return it.getParameterCount() == 1 && it.getParameterTypes()[0].equals(IDENTIFIER_CLASS) && it.getReturnType().equals(Object.class);
         }).findFirst().orElseThrow(() -> {
            return new IllegalStateException("Could not find Registry#get(Identifier)");
         });
         Class<?> resourceKeyClass = CraftBukkitReflection.needMCClass("resources.ResourceKey");
         REGISTRY_KEY = (Method)Arrays.stream(registryClass.getDeclaredMethods()).filter((m) -> {
            return m.getParameterCount() == 0 && m.getReturnType().equals(resourceKeyClass);
         }).findFirst().orElse((Object)null);
         NEW_RESOURCE_LOCATION = (Executable)CraftBukkitReflection.firstNonNullOrThrow(() -> {
            return "Could not find Identifier#parse(String) or Identifier#<init>(String)";
         }, CraftBukkitReflection.findConstructor(IDENTIFIER_CLASS, String.class), CraftBukkitReflection.findMethod(IDENTIFIER_CLASS, "parse", String.class), CraftBukkitReflection.findMethod(IDENTIFIER_CLASS, "a", String.class));
         CREATE_REGISTRY_RESOURCE_KEY = (Executable)CraftBukkitReflection.firstNonNullOrThrow(() -> {
            return "Could not find ResourceKey#createRegistryKey(Identifier)";
         }, CraftBukkitReflection.findMethod(RESOURCE_KEY_CLASS, "createRegistryKey", IDENTIFIER_CLASS), CraftBukkitReflection.findMethod(RESOURCE_KEY_CLASS, "a", IDENTIFIER_CLASS));
      }

   }
}
