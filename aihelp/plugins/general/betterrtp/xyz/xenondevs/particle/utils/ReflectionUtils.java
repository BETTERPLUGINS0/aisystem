package xyz.xenondevs.particle.utils;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.net.URISyntaxException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import xyz.xenondevs.particle.ParticleConstants;

public final class ReflectionUtils {
   private static final String NET_MINECRAFT_SERVER_PACKAGE_PATH;
   private static final String CRAFT_BUKKIT_PACKAGE_PATH;
   public static final double MINECRAFT_VERSION;
   private static final Class<?> PLUGIN_CLASS_LOADER_CLASS = getClassSafe("org.bukkit.plugin.java.PluginClassLoader");
   private static final Field PLUGIN_CLASS_LOADER_PLUGIN_FIELD;
   public static final PlayerConnectionCache PLAYER_CONNECTION_CACHE;
   private static Plugin plugin;
   private static final ZipFile zipFile;

   public static Plugin getPlugin() {
      return plugin;
   }

   public static void setPlugin(Plugin plugin) {
      boolean wasNull = ReflectionUtils.plugin == null;
      ReflectionUtils.plugin = plugin;
      if (wasNull) {
         PLAYER_CONNECTION_CACHE.registerListener();
      }

   }

   public static Class<?> getClassSafe(String path) {
      try {
         return Class.forName(path);
      } catch (Exception var2) {
         return null;
      }
   }

   public static String getNMSPath(String path) {
      return getNetMinecraftServerPackagePath() + "." + path;
   }

   public static Class<?> getNMSClass(String path) {
      return getClassSafe(getNMSPath(path));
   }

   public static String getCraftBukkitPath(String path) {
      return getCraftBukkitPackagePath() + "." + path;
   }

   public static Class<?> getCraftBukkitClass(String path) {
      return getClassSafe(getCraftBukkitPath(path));
   }

   public static Method getMethodOrNull(Class targetClass, String methodName, Class<?>... parameterTypes) {
      try {
         return targetClass.getMethod(methodName, parameterTypes);
      } catch (Exception var4) {
         return null;
      }
   }

   public static Field getFieldOrNull(Class targetClass, String fieldName, boolean declared) {
      try {
         return declared ? targetClass.getDeclaredField(fieldName) : targetClass.getField(fieldName);
      } catch (Exception var4) {
         return null;
      }
   }

   public static Constructor getConstructorOrNull(Class targetClass, Class... parameterTypes) {
      try {
         return targetClass.getConstructor(parameterTypes);
      } catch (Exception var3) {
         return null;
      }
   }

   public static boolean existsClass(String path) {
      try {
         Class.forName(path);
         return true;
      } catch (Exception var2) {
         return false;
      }
   }

   public static Object readField(Class targetClass, String fieldName, Object object) {
      return targetClass != null && fieldName != null ? readField(getFieldOrNull(targetClass, fieldName, false), object) : null;
   }

   public static <T> T readField(Field field, Object object) {
      if (field == null) {
         return null;
      } else {
         try {
            return field.get(object);
         } catch (Exception var3) {
            return null;
         }
      }
   }

   public static Object readDeclaredField(Class targetClass, String fieldName, Object object) {
      return targetClass != null && fieldName != null ? readDeclaredField(getFieldOrNull(targetClass, fieldName, true), object) : null;
   }

   public static <T> T readDeclaredField(Field field, Object object) {
      if (field == null) {
         return null;
      } else {
         field.setAccessible(true);

         try {
            return field.get(object);
         } catch (Exception var3) {
            return null;
         }
      }
   }

   public static void writeDeclaredField(Class targetClass, String fieldName, Object object, Object value) {
      if (targetClass != null && fieldName != null) {
         writeDeclaredField(getFieldOrNull(targetClass, fieldName, true), object, value);
      }
   }

   public static void writeDeclaredField(Field field, Object object, Object value) {
      if (field != null) {
         field.setAccessible(true);

         try {
            field.set(object, value);
         } catch (Exception var4) {
         }

      }
   }

   public static void writeField(Class targetClass, String fieldName, Object object, Object value) {
      if (targetClass != null && fieldName != null) {
         writeField(getFieldOrNull(targetClass, fieldName, false), object, value);
      }
   }

   public static void writeField(Field field, Object object, Object value) {
      if (field != null) {
         try {
            field.set(object, value);
         } catch (Exception var4) {
         }

      }
   }

   public static String getNetMinecraftServerPackagePath() {
      return NET_MINECRAFT_SERVER_PACKAGE_PATH;
   }

   public static String getCraftBukkitPackagePath() {
      return CRAFT_BUKKIT_PACKAGE_PATH;
   }

   public static Object getMinecraftKey(String key) {
      if (key == null) {
         return null;
      } else {
         try {
            return ParticleConstants.MINECRAFT_KEY_CONSTRUCTOR.newInstance(key);
         } catch (Exception var2) {
            return null;
         }
      }
   }

   public static Object createVector3fa(float x, float y, float z) {
      try {
         return ParticleConstants.VECTOR_3FA_CONSTRUCTOR.newInstance(x, y, z);
      } catch (Exception var4) {
         return null;
      }
   }

   public static Object createBlockPosition(Location location) {
      try {
         return ParticleConstants.BLOCK_POSITION_CONSTRUCTOR.newInstance(location.getBlockX(), location.getBlockY(), location.getBlockZ());
      } catch (Exception var2) {
         return null;
      }
   }

   public static Object getEntityHandle(Entity entity) {
      if (entity != null && ParticleConstants.CRAFT_ENTITY_CLASS.isAssignableFrom(entity.getClass())) {
         try {
            return ParticleConstants.CRAFT_ENTITY_GET_HANDLE_METHOD.invoke(entity);
         } catch (Exception var2) {
            return null;
         }
      } else {
         return null;
      }
   }

   public static Object getPlayerHandle(Player player) {
      if (player != null && player.getClass() == ParticleConstants.CRAFT_PLAYER_CLASS) {
         try {
            return ParticleConstants.CRAFT_PLAYER_GET_HANDLE_METHOD.invoke(player);
         } catch (Exception var2) {
            return null;
         }
      } else {
         return null;
      }
   }

   public static Object getPlayerConnection(Player target) {
      try {
         return readField(ParticleConstants.ENTITY_PLAYER_PLAYER_CONNECTION_FIELD, getPlayerHandle(target));
      } catch (Exception var2) {
         return null;
      }
   }

   public static void sendPacket(Player player, Object packet) {
      try {
         Object connection = PLAYER_CONNECTION_CACHE.getConnection(player);
         ParticleConstants.PLAYER_CONNECTION_SEND_PACKET_METHOD.invoke(connection, packet);
      } catch (Exception var3) {
      }

   }

   public static InputStream getResourceStreamSafe(String resource) {
      ZipEntry entry = zipFile.getEntry(resource);
      if (entry == null) {
         return null;
      } else {
         try {
            return zipFile.getInputStream(entry);
         } catch (IOException var3) {
            return null;
         }
      }
   }

   static {
      PLUGIN_CLASS_LOADER_PLUGIN_FIELD = getFieldOrNull(PLUGIN_CLASS_LOADER_CLASS, "plugin", true);
      String serverPath = Bukkit.getServer().getClass().getPackage().getName();
      String version = serverPath.substring(serverPath.lastIndexOf(".") + 1);
      String bukkitVersion = Bukkit.getBukkitVersion();
      int dashIndex = bukkitVersion.indexOf("-");
      MINECRAFT_VERSION = Double.parseDouble(bukkitVersion.substring(2, dashIndex > -1 ? bukkitVersion.indexOf("-") : bukkitVersion.length()));
      NET_MINECRAFT_SERVER_PACKAGE_PATH = "net.minecraft" + (MINECRAFT_VERSION < 17.0D ? ".server." + version : "");
      CRAFT_BUKKIT_PACKAGE_PATH = "org.bukkit.craftbukkit." + version;
      plugin = (Plugin)readDeclaredField(PLUGIN_CLASS_LOADER_PLUGIN_FIELD, ReflectionUtils.class.getClassLoader());
      PLAYER_CONNECTION_CACHE = new PlayerConnectionCache();

      try {
         zipFile = new ZipFile(ReflectionUtils.class.getProtectionDomain().getCodeSource().getLocation().toURI().getPath());
      } catch (URISyntaxException | IOException var5) {
         throw new IllegalStateException("Error while finding zip file", var5);
      }
   }
}
