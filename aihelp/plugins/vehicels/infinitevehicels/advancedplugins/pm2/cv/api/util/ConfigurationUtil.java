package advancedplugins.pm2.cv.api.util;

import advancedplugins.pm2.cv.api.enums.MinecraftVersion;
import advancedplugins.pm2.cv.api.interfaces.ConfigurationSectionWritable;
import advancedplugins.pm2.cv.api.interfaces.IDeyed;
import advancedplugins.pm2.cv.api.interfaces.Named;
import advancedplugins.pm2.cv.api.util.reflection.EnumReflection;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import me.PM2.infinitevehicles.math.geometry.euclidean.threed.Vector3D;
import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ConfigurationUtil {
   private static final Map<Class<?>, ConfigurationUtil.LibraryObjectSingleEntryConfigurationWrapper<?>> SINGLE_ENTRY_LIBRARY_OBJECT_WRAPPERS = new HashMap();
   private static final Map<Class<?>, ConfigurationUtil.LibraryObjectConfigurationWrapper<?>> LIBRARY_OBJECT_WRAPPERS = new HashMap();

   public static <T> T loadLibrarySingleEntryObject(@NotNull Class<T> var0, @NotNull ConfigurationSection var1, @NotNull String var2) {
      ConfigurationUtil.LibraryObjectSingleEntryConfigurationWrapper var3 = (ConfigurationUtil.LibraryObjectSingleEntryConfigurationWrapper)SINGLE_ENTRY_LIBRARY_OBJECT_WRAPPERS.get(var0);
      Object var4 = var3.load(var1, var2);
      return var4 != null ? var0.cast(var4) : null;
   }

   public static <T> T loadLibraryObject(@NotNull Class<T> var0, @NotNull ConfigurationSection var1) {
      ConfigurationUtil.LibraryObjectConfigurationWrapper var2 = (ConfigurationUtil.LibraryObjectConfigurationWrapper)LIBRARY_OBJECT_WRAPPERS.get(var0);
      Object var3 = var2.load(var1);
      return var3 != null ? var0.cast(var3) : null;
   }

   public static <T> T loadLibraryObject(@NotNull Class<T> var0, @NotNull ConfigurationSection var1, @NotNull String var2) {
      ConfigurationSection var3 = var1.getConfigurationSection(var2);
      return var3 != null ? loadLibraryObject(var0, var3) : null;
   }

   @Nullable
   public static <T extends Enum<T>> T loadEnum(@NotNull Class<T> var0, @NotNull ConfigurationSection var1, @NotNull String var2, boolean var3) {
      String var4 = var1.getString(var2, "");
      if (var4.equalsIgnoreCase("REDSTONE") && MinecraftVersion.isAtLeastVersion(MinecraftVersion.MC1_20_R4)) {
         var4 = "DUST";
      }

      return StringUtils.isNotBlank(var4) ? EnumReflection.getEnumConstant(var0, var3 ? var4.trim().toUpperCase() : var4.trim()) : null;
   }

   @Nullable
   public static <T extends Enum<T>> T loadEnum(@NotNull Class<T> var0, @NotNull ConfigurationSection var1, @NotNull String var2) {
      return loadEnum(var0, var1, var2, true);
   }

   public static <T> void writeSingleEntryLibraryObject(@NotNull Class<T> var0, @NotNull T var1, @NotNull ConfigurationSection var2, @NotNull String var3) {
      ConfigurationUtil.LibraryObjectSingleEntryConfigurationWrapper var4 = (ConfigurationUtil.LibraryObjectSingleEntryConfigurationWrapper)SINGLE_ENTRY_LIBRARY_OBJECT_WRAPPERS.get(var0);
      var4.write(var1, var2, var3);
   }

   public static <T> void writeLibraryObject(@NotNull Class<T> var0, @NotNull T var1, @NotNull ConfigurationSection var2) {
      ConfigurationUtil.LibraryObjectConfigurationWrapper var3 = (ConfigurationUtil.LibraryObjectConfigurationWrapper)LIBRARY_OBJECT_WRAPPERS.get(var0);
      var3.write(var1, var2);
   }

   public static void writeEnum(@NotNull Enum<?> var0, @NotNull ConfigurationSection var1, @NotNull String var2) {
      var1.set(var2, var0.name());
   }

   public static void writeNullable(@Nullable Object var0, @NotNull ConfigurationSection var1, @NotNull String var2) {
      var1.set(var2, var0);
   }

   public static void writeConfigurationSectionWritables(@NotNull ConfigurationSection var0, @NotNull Collection<? extends ConfigurationSectionWritable> var1, @NotNull String var2) {
      int var3 = 0;

      for(Iterator var4 = var1.iterator(); var4.hasNext(); ++var3) {
         ConfigurationSectionWritable var5 = (ConfigurationSectionWritable)var4.next();
         if (var5 instanceof Named) {
            var5.write(var0.createSection(((Named)var5).getName()));
         } else if (var5 instanceof IDeyed) {
            var5.write(var0.createSection(((IDeyed)var5).getId()));
         } else {
            var5.write(var0.createSection(var2 + var3));
         }
      }

   }

   @NotNull
   public static Set<ConfigurationSection> getConfigurationSectionsAfter(@NotNull ConfigurationSection var0, @NotNull String var1, boolean var2) {
      ConfigurationSection var3 = var0.getConfigurationSection(var1);
      return (Set)(var3 != null ? getConfigurationSections(var3, var2) : new HashSet());
   }

   @NotNull
   public static Set<ConfigurationSection> getConfigurationSections(@NotNull ConfigurationSection var0, boolean var1) {
      Set var2 = var0.getKeys(var1);
      HashSet var3 = new HashSet();
      Iterator var4 = var2.iterator();

      while(var4.hasNext()) {
         String var5 = (String)var4.next();
         ConfigurationSection var6 = var0.getConfigurationSection(var5);
         if (var6 != null) {
            var3.add(var6);
         }
      }

      return var3;
   }

   public static void writeLocation(Location var0, ConfigurationSection var1, String var2) {
      var1.set(var2 + ".world", var0.getWorld().getName());
      var1.set(var2 + ".x", var0.getX());
      var1.set(var2 + ".y", var0.getY());
      var1.set(var2 + ".z", var0.getZ());
      var1.set(var2 + ".pitch", (double)var0.getPitch());
      var1.set(var2 + ".yaw", (double)var0.getYaw());
   }

   public static Location loadLocation(ConfigurationSection var0, String var1) {
      String var8 = var0.getString(var1 + ".world");
      if (var8 == null) {
         throw new RuntimeException("World not valid!");
      } else {
         World var11 = Bukkit.getWorld(var8);
         if (var11 == null) {
            throw new RuntimeException("World not valid!");
         } else {
            double var2 = var0.getDouble(var1 + ".x");
            double var4 = var0.getDouble(var1 + ".y");
            double var6 = var0.getDouble(var1 + ".z");
            float var9 = (float)var0.getDouble(var1 + ".pitch");
            float var10 = (float)var0.getDouble(var1 + ".yaw");
            return new Location(var11, var2, var4, var6, var9, var10);
         }
      }
   }

   public static void writeVector(Vector var0, ConfigurationSection var1, String var2) {
      var1.set(var2 + ".x", var0.getX());
      var1.set(var2 + ".y", var0.getY());
      var1.set(var2 + ".z", var0.getZ());
   }

   public static Vector loadVector(ConfigurationSection var0, String var1) {
      double var2 = var0.getDouble(var1 + ".x");
      double var4 = var0.getDouble(var1 + ".y");
      double var6 = var0.getDouble(var1 + ".z");
      return new Vector(var2, var4, var6);
   }

   static {
      SINGLE_ENTRY_LIBRARY_OBJECT_WRAPPERS.put(UUID.class, new ConfigurationUtil.LibraryObjectSingleEntryConfigurationWrapper<UUID>() {
         public UUID load(@NotNull ConfigurationSection var1, @NotNull String var2) {
            try {
               String var3 = var1.getString(var2);
               return UUID.fromString(var3 != null ? var3 : "");
            } catch (IllegalArgumentException var4) {
               throw new InvalidConfigurationException(var4);
            }
         }

         public void write(@NotNull UUID var1, @NotNull ConfigurationSection var2, @NotNull String var3) {
            var2.set(var3, var1.toString());
         }
      });
      LIBRARY_OBJECT_WRAPPERS.put(Vector3D.class, new ConfigurationUtil.LibraryObjectConfigurationWrapper<Vector3D>() {
         public Vector3D load(@NotNull ConfigurationSection var1) {
            double var2 = var1.getDouble("x");
            double var4 = var1.getDouble("y");
            double var6 = var1.getDouble("z");
            return new Vector3D(var2, var4, var6);
         }

         public void write(@NotNull Vector3D var1, @NotNull ConfigurationSection var2) {
            var2.set("x", var1.getX());
            var2.set("y", var1.getY());
            var2.set("z", var1.getZ());
         }
      });
   }

   private interface LibraryObjectSingleEntryConfigurationWrapper<T> {
      T load(@NotNull ConfigurationSection var1, @NotNull String var2) throws InvalidConfigurationException;

      void write(@NotNull T var1, @NotNull ConfigurationSection var2, @NotNull String var3);
   }

   private interface LibraryObjectConfigurationWrapper<T> {
      T load(@NotNull ConfigurationSection var1) throws InvalidConfigurationException;

      void write(@NotNull T var1, @NotNull ConfigurationSection var2);
   }
}
