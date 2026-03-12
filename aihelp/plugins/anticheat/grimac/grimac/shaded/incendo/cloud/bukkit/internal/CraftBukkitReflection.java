package ac.grim.grimac.shaded.incendo.cloud.bukkit.internal;

import java.lang.reflect.Constructor;
import java.lang.reflect.Executable;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.function.Supplier;
import java.util.stream.Stream;
import org.apiguardian.api.API;
import org.apiguardian.api.API.Status;
import org.bukkit.Bukkit;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

@API(
   status = Status.INTERNAL,
   consumers = {"ac.grim.grimac.shaded.incendo.cloud.*"}
)
public final class CraftBukkitReflection {
   private static final String PREFIX_NMS = "net.minecraft.server";
   private static final String PREFIX_MC = "net.minecraft.";
   private static final String PREFIX_CRAFTBUKKIT = "org.bukkit.craftbukkit";
   private static final String CRAFT_SERVER = "CraftServer";
   private static final String CB_PKG_VERSION;
   public static final int MAJOR_REVISION;

   @SafeVarargs
   @Nullable
   public static <T> T firstNonNullOrNull(@NonNull final T... elements) {
      Object[] var1 = elements;
      int var2 = elements.length;

      for(int var3 = 0; var3 < var2; ++var3) {
         T element = var1[var3];
         if (element != null) {
            return element;
         }
      }

      return null;
   }

   @SafeVarargs
   public static <T> T firstNonNullOrThrow(final Supplier<String> errorMessage, final T... elements) {
      T t = firstNonNullOrNull(elements);
      if (t == null) {
         throw new IllegalArgumentException((String)errorMessage.get());
      } else {
         return t;
      }
   }

   @NonNull
   public static Class<?> needNMSClassOrElse(@NonNull final String nms, final String... classNames) throws RuntimeException {
      Class<?> nmsClass = findNMSClass(nms);
      return nmsClass != null ? nmsClass : (Class)firstNonNullOrThrow(() -> {
         return String.format("Cound't find the NMS class '%s', or any of the following fallbacks: %s", nms, Arrays.toString(classNames));
      }, (Class[])Arrays.stream(classNames).map(CraftBukkitReflection::findClass).toArray((x$0) -> {
         return new Class[x$0];
      }));
   }

   @NonNull
   public static Class<?> needMCClass(@NonNull final String name) throws RuntimeException {
      return needClass("net.minecraft." + name);
   }

   @NonNull
   public static Class<?> needNMSClass(@NonNull final String className) throws RuntimeException {
      return needClass("net.minecraft.server" + CB_PKG_VERSION + className);
   }

   @NonNull
   public static Class<?> needOBCClass(@NonNull final String className) throws RuntimeException {
      return needClass("org.bukkit.craftbukkit" + CB_PKG_VERSION + className);
   }

   @Nullable
   public static Class<?> findMCClass(@NonNull final String name) throws RuntimeException {
      return findClass("net.minecraft." + name);
   }

   @Nullable
   public static Class<?> findNMSClass(@NonNull final String className) throws RuntimeException {
      return findClass("net.minecraft.server" + CB_PKG_VERSION + className);
   }

   @Nullable
   public static Class<?> findOBCClass(@NonNull final String className) throws RuntimeException {
      return findClass("org.bukkit.craftbukkit" + CB_PKG_VERSION + className);
   }

   @NonNull
   public static Class<?> needClass(@NonNull final String className) throws RuntimeException {
      try {
         return Class.forName(className);
      } catch (ClassNotFoundException var2) {
         throw new RuntimeException(var2);
      }
   }

   @Nullable
   public static Class<?> findClass(@NonNull final String className) {
      try {
         return Class.forName(className);
      } catch (ClassNotFoundException var2) {
         return null;
      }
   }

   @NonNull
   public static Field needField(@NonNull final Class<?> holder, @NonNull final String name) throws RuntimeException {
      try {
         Field field = holder.getDeclaredField(name);
         field.setAccessible(true);
         return field;
      } catch (ReflectiveOperationException var3) {
         throw new RuntimeException(var3);
      }
   }

   @Nullable
   public static Field findField(@NonNull final Class<?> holder, @NonNull final String name) throws RuntimeException {
      try {
         return needField(holder, name);
      } catch (RuntimeException var3) {
         return null;
      }
   }

   @NonNull
   public static Constructor<?> needConstructor(@NonNull final Class<?> holder, final Class<?>... parameters) {
      try {
         return holder.getDeclaredConstructor(parameters);
      } catch (NoSuchMethodException var3) {
         throw new RuntimeException(var3);
      }
   }

   @Nullable
   public static Constructor<?> findConstructor(@NonNull final Class<?> holder, final Class<?>... parameters) {
      try {
         return holder.getDeclaredConstructor(parameters);
      } catch (NoSuchMethodException var3) {
         return null;
      }
   }

   public static boolean classExists(@NonNull final String className) {
      return findClass(className) != null;
   }

   @Nullable
   public static Method findMethod(@NonNull final Class<?> holder, @NonNull final String name, final Class<?>... params) throws RuntimeException {
      try {
         return holder.getMethod(name, params);
      } catch (NoSuchMethodException var4) {
         return null;
      }
   }

   @NonNull
   public static Method needMethod(@NonNull final Class<?> holder, @NonNull final String name, final Class<?>... params) throws RuntimeException {
      try {
         return holder.getMethod(name, params);
      } catch (NoSuchMethodException var4) {
         throw new RuntimeException(var4);
      }
   }

   public static Stream<Method> streamMethods(@NonNull final Class<?> clazz) {
      return Arrays.stream(clazz.getDeclaredMethods());
   }

   public static Object invokeConstructorOrStaticMethod(final Executable executable, final Object... args) throws ReflectiveOperationException {
      if (executable instanceof Constructor) {
         return ((Constructor)executable).newInstance(args);
      } else if (!Modifier.isStatic(executable.getModifiers())) {
         throw new IllegalArgumentException("Method " + executable + " is not static.");
      } else {
         return ((Method)executable).invoke((Object)null, args);
      }
   }

   private CraftBukkitReflection() {
   }

   static {
      Class serverClass;
      if (Bukkit.getServer() == null) {
         serverClass = needClass("org.bukkit.craftbukkit.CraftServer");
      } else {
         serverClass = Bukkit.getServer().getClass();
      }

      String pkg = serverClass.getPackage().getName();
      String nmsVersion = pkg.substring(pkg.lastIndexOf(".") + 1);
      if (!nmsVersion.contains("_")) {
         int fallbackVersion = -1;
         if (Bukkit.getServer() != null) {
            try {
               Method getMinecraftVersion = serverClass.getDeclaredMethod("getMinecraftVersion");
               fallbackVersion = Integer.parseInt(getMinecraftVersion.invoke(Bukkit.getServer()).toString().split("\\.")[1]);
            } catch (Exception var13) {
            }
         } else {
            try {
               Class<?> sharedConstants = needClass("net.minecraft.SharedConstants");
               Method getCurrentVersion = sharedConstants.getDeclaredMethod("getCurrentVersion");
               Object currentVersion = getCurrentVersion.invoke((Object)null);
               Method getName = null;

               try {
                  getName = currentVersion.getClass().getDeclaredMethod("getName");
               } catch (NoSuchMethodException var11) {
               }

               if (getName == null) {
                  getName = currentVersion.getClass().getDeclaredMethod("name");
               }

               String versionName = (String)getName.invoke(currentVersion);

               try {
                  fallbackVersion = Integer.parseInt(versionName.split("\\.")[1]);
               } catch (Exception var10) {
               }
            } catch (ReflectiveOperationException var12) {
               throw new RuntimeException(var12);
            }
         }

         MAJOR_REVISION = fallbackVersion;
      } else {
         MAJOR_REVISION = Integer.parseInt(nmsVersion.split("_")[1]);
      }

      String name = serverClass.getName();
      name = name.substring("org.bukkit.craftbukkit".length());
      name = name.substring(0, name.length() - "CraftServer".length());
      CB_PKG_VERSION = name;
   }
}
