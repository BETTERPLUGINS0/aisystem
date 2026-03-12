package emanondev.itemedit;

import emanondev.itemedit.utility.VersionUtils;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.EnumSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class YMLConfig extends YamlConfiguration {
   private final JavaPlugin plugin;
   private final File file;
   private final String fileName;
   private boolean multiThreadSupport = false;

   public YMLConfig(@NotNull JavaPlugin plugin, @NotNull String fileName) {
      this.plugin = plugin;
      fileName = fixName(fileName);
      this.fileName = fileName;
      this.file = new File(plugin.getDataFolder(), fileName);
      this.reload();
      if (VersionUtils.hasFoliaAPI()) {
         this.multiThreadSupport = true;
      }

   }

   public static String fixName(@NotNull String name) {
      if (name.isEmpty()) {
         throw new IllegalArgumentException("YAML file must have a name!");
      } else {
         if (!name.endsWith(".yml")) {
            name = name + ".yml";
         }

         return name;
      }
   }

   public boolean reload() {
      boolean existed = this.file.exists();
      if (!this.file.exists()) {
         if (!this.file.getParentFile().exists() && !this.file.getParentFile().mkdirs()) {
            (new Exception("unable to create parent folder")).printStackTrace();
         }

         if (this.plugin.getResource(this.fileName.replace('\\', '/')) != null) {
            this.plugin.saveResource(this.fileName, true);
         } else {
            try {
               if (!this.file.createNewFile()) {
                  (new Exception("unable to create file")).printStackTrace();
               }
            } catch (IOException var4) {
               var4.printStackTrace();
            }
         }
      }

      try {
         this.load(this.file);
      } catch (Exception var3) {
         var3.printStackTrace();
      }

      InputStream resource = this.plugin.getResource(this.fileName.replace('\\', '/'));
      if (resource != null) {
         this.setDefaults(YamlConfiguration.loadConfiguration(new InputStreamReader(resource, StandardCharsets.UTF_8)));
      }

      return existed;
   }

   public void save() {
      try {
         this.save(this.file);
      } catch (Exception var2) {
         var2.printStackTrace();
      }

   }

   @NotNull
   public Set<String> getKeys(@NotNull String path) {
      ConfigurationSection section = this.getConfigurationSection(path);
      return (Set)(section == null ? new LinkedHashSet() : section.getKeys(false));
   }

   @Contract("_, !null, _ -> !null")
   public <T> T load(String path, T def, Class<T> clazz) {
      Object value = this.get(path, (Object)null);
      if (value == null) {
         value = this.getDefaults() == null ? null : this.getDefaults().get(path);
         if (value == null) {
            if (def == null) {
               return null;
            } else {
               this.set(path, def);
               this.save();
               return def;
            }
         } else if (!clazz.isInstance(value)) {
            this.set(path, def);
            this.save();
            return def;
         } else {
            this.set(path, value);
            this.save();
            return value;
         }
      } else if (!clazz.isInstance(value)) {
         this.set(path, def);
         this.save();
         return def;
      } else {
         return value;
      }
   }

   @Contract("_, !null, _ -> !null")
   public <T> T get(String path, T def, Class<T> clazz) {
      Object value = this.get(path);
      if (value == null) {
         return def;
      } else {
         return !clazz.isInstance(value) ? def : value;
      }
   }

   @Contract("_, !null -> !null")
   @Nullable
   public Double loadDouble(@NotNull String path, @Nullable Double def) {
      Number val = (Number)this.load(path, def, Number.class);
      return val == null ? null : val.doubleValue();
   }

   @Contract("_, !null -> !null")
   @Nullable
   public Boolean loadBoolean(@NotNull String path, @Nullable Boolean def) {
      return (Boolean)this.load(path, def, Boolean.class);
   }

   /** @deprecated */
   @Deprecated
   public int loadInt(@NotNull String path, @Nullable Integer def) {
      Number val = (Number)this.load(path, def, Number.class);
      return val == null ? 0 : val.intValue();
   }

   @Contract("_, !null -> !null")
   @Nullable
   public Integer loadInteger(@NotNull String path, @Nullable Integer def) {
      Number val = (Number)this.load(path, def, Number.class);
      return val == null ? null : val.intValue();
   }

   @Contract("_, !null -> !null")
   @Nullable
   public Integer getInteger(@NotNull String path, @Nullable Integer def) {
      Number val = (Number)this.load(path, def, Number.class);
      return val == null ? null : val.intValue();
   }

   @Contract("_, !null -> !null")
   @Nullable
   public Long loadLong(@NotNull String path, @Nullable Long def) {
      Number val = (Number)this.load(path, def, Number.class);
      return val == null ? null : val.longValue();
   }

   @Contract("_, !null, _, _ -> !null")
   @Nullable
   public String loadMessage(@NotNull String path, @Nullable String def, boolean color, String... args) {
      return this.loadMessage(path, def, (Player)null, color, args);
   }

   @Contract("_, !null, _ -> !null")
   @Nullable
   public String loadMessage(@NotNull String path, @Nullable String def, String... args) {
      return this.loadMessage(path, def, (Player)null, true, args);
   }

   @Contract("_, !null, _, _, _ -> !null")
   @Nullable
   public String loadMessage(@NotNull String path, @Nullable String def, @Nullable Player target, boolean color, String... args) {
      if (args.length > 0 && VersionUtils.isVersionAfter(1, 18, 1) && this.getComments(path).isEmpty()) {
         StringBuilder build = new StringBuilder();

         for(int i = 0; i < args.length; i += 2) {
            build.append(args[i]).append(" ");
         }

         this.setComments(path, Collections.singletonList(build.substring(0, build.length() - 1)));
      }

      return UtilsString.fix((String)this.load(path, def, String.class), target, color, args);
   }

   @Contract("_, !null, _, _, _ -> !null")
   @Nullable
   public String getMessage(@NotNull String path, @Nullable String def, @Nullable Player target, boolean color, String... args) {
      if (args.length > 0 && VersionUtils.isVersionAfter(1, 18, 1) && this.getComments(path).isEmpty()) {
         StringBuilder build = new StringBuilder();

         for(int i = 0; i < args.length; i += 2) {
            build.append(args[i]).append(" ");
         }

         this.setComments(path, Collections.singletonList(build.substring(0, build.length() - 1)));
      }

      return UtilsString.fix((String)this.get(path, def, String.class), target, color, args);
   }

   @Contract("_, !null, _, _ -> !null")
   @Nullable
   public String getMessage(@NotNull String path, @Nullable String def, boolean color, String... args) {
      return this.getMessage(path, def, (Player)null, color, args);
   }

   @Contract("_, !null, _ -> !null")
   @Nullable
   public String getMessage(@NotNull String path, @Nullable String def, String... args) {
      return this.getMessage(path, def, (Player)null, true, args);
   }

   @Contract("_, !null, _ -> !null")
   @Nullable
   public List<String> loadMultiMessage(@NotNull String path, @Nullable List<String> def, String... holders) {
      return this.loadMultiMessage(path, def, (Player)null, true, holders);
   }

   @Contract("_, !null, _, _ -> !null")
   @Nullable
   public List<String> loadMultiMessage(@NotNull String path, @Nullable List<String> def, boolean color, String... holders) {
      return this.loadMultiMessage(path, def, (Player)null, color, holders);
   }

   @Contract("_, !null, _, _, _ -> !null")
   @Nullable
   public List<String> loadMultiMessage(@NotNull String path, @Nullable List<String> def, @Nullable Player target, boolean color, String... holders) {
      if (holders.length > 0 && VersionUtils.isVersionAfter(1, 18, 1) && this.getComments(path).isEmpty()) {
         if (this.contains(path + "_HOLDERS")) {
            this.set(path + "_HOLDERS", (Object)null);
         }

         StringBuilder build = new StringBuilder();

         for(int i = 0; i < holders.length; i += 2) {
            build.append(holders[i]).append(" ");
         }

         this.setComments(path, Collections.singletonList(build.substring(0, build.length() - 1)));
      }

      try {
         return UtilsString.fix((List)this.load(path, def, List.class), target, color, holders);
      } catch (Exception var8) {
         var8.printStackTrace();
         return UtilsString.fix(def, target, color, holders);
      }
   }

   @Contract("_, !null, _ -> !null")
   @Nullable
   public List<String> getMultiMessage(@NotNull String path, @Nullable List<String> def, String... holders) {
      return this.getMultiMessage(path, def, (Player)null, true, holders);
   }

   @Contract("_, !null, _, _ -> !null")
   @Nullable
   public List<String> getMultiMessage(@NotNull String path, @Nullable List<String> def, boolean color, String... holders) {
      return this.getMultiMessage(path, def, (Player)null, color, holders);
   }

   @Contract("_, !null, _, _, _ -> !null")
   @Nullable
   public List<String> getMultiMessage(@NotNull String path, @Nullable List<String> def, @Nullable Player target, boolean color, String... holders) {
      if (holders.length > 0 && VersionUtils.isVersionAfter(1, 18, 1) && this.getComments(path).isEmpty()) {
         if (this.contains(path + "_HOLDERS")) {
            this.set(path + "_HOLDERS", (Object)null);
         }

         StringBuilder build = new StringBuilder();

         for(int i = 0; i < holders.length; i += 2) {
            build.append(holders[i]).append(" ");
         }

         this.setComments(path, Collections.singletonList(build.substring(0, build.length() - 1)));
      }

      try {
         return UtilsString.fix((List)this.get(path, def, List.class), target, color, holders);
      } catch (Exception var8) {
         var8.printStackTrace();
         return UtilsString.fix(def, target, color, holders);
      }
   }

   @Contract("_, !null -> !null")
   @Nullable
   public ItemStack loadItemStack(@NotNull String path, @Nullable ItemStack def) {
      return (ItemStack)this.load(path, def, ItemStack.class);
   }

   @Contract("_, !null, _ -> !null")
   @Nullable
   public <T extends Enum<T>> T loadEnum(@NotNull String path, @Nullable T def, @NotNull Class<T> clazz) {
      return this.stringToEnum(this.loadMessage(path, def == null ? null : def.name(), false), def, clazz, path);
   }

   @Contract("_, !null, _ -> !null")
   @Nullable
   public <T extends Enum<T>> T getEnum(@NotNull String path, @Nullable T def, @NotNull Class<T> clazz) {
      return this.stringToEnum(this.getString(path, def == null ? null : def.name()), def, clazz, path);
   }

   @Contract("_, !null, _, _ -> !null")
   @Nullable
   private <T extends Enum<T>> T stringToEnum(@Nullable String value, @Nullable T def, @NotNull Class<T> clazz, @NotNull String errorPath) {
      try {
         return value != null && !value.isEmpty() ? Enum.valueOf(clazz, value) : def;
      } catch (IllegalArgumentException var8) {
         try {
            return Enum.valueOf(clazz, value.toUpperCase());
         } catch (IllegalArgumentException var7) {
            var7.printStackTrace();
            (new IllegalArgumentException(this.getError(errorPath) + "; can't find value for '" + value + "' from enum '" + clazz.getName() + "' using default")).printStackTrace();
            return def;
         }
      }
   }

   @NotNull
   public <T extends Enum<T>> List<T> loadEnumList(@NotNull String path, @Nullable Collection<T> def, @NotNull Class<T> clazz) {
      return (List)this.stringListToEnumCollection(new ArrayList(), this.loadMultiMessage(path, this.enumCollectionToStringList(def), false), clazz, path);
   }

   @NotNull
   public <T extends Enum<T>> EnumSet<T> loadEnumSet(@NotNull String path, @Nullable Collection<T> def, @NotNull Class<T> clazz) {
      return (EnumSet)this.stringListToEnumCollection(EnumSet.noneOf(clazz), this.loadMultiMessage(path, this.enumCollectionToStringList(def), false), clazz, path);
   }

   @Contract("!null -> !null; null -> null")
   private <T extends Enum<T>> ArrayList<String> enumCollectionToStringList(@Nullable Collection<T> enums) {
      if (enums == null) {
         return null;
      } else {
         ArrayList<String> list = new ArrayList();
         Iterator var3 = enums.iterator();

         while(var3.hasNext()) {
            T enumValue = (Enum)var3.next();
            list.add(enumValue.name());
         }

         return list;
      }
   }

   private <T extends Enum<T>, K extends Collection<T>> K stringListToEnumCollection(K destination, Collection<String> from, Class<T> clazz, String errPath) {
      if (from != null && !from.isEmpty()) {
         Iterator var5 = from.iterator();

         while(var5.hasNext()) {
            String value = (String)var5.next();
            T val = this.stringToEnum(value, (Enum)null, clazz, errPath);
            if (val != null) {
               destination.add(val);
            }
         }

         return destination;
      } else {
         return destination;
      }
   }

   @Contract("_, !null -> !null")
   @Nullable
   public <T> Map<String, T> loadMap(@NotNull String path, @Nullable Map<String, T> def) {
      try {
         if (!this.contains(path)) {
            this.set(path, def);
            this.save();
            return def;
         } else {
            Map subMap = ((ConfigurationSection)this.get(path)).getValues(true);

            try {
               return subMap;
            } catch (Exception var9) {
               Map<String, T> result = new LinkedHashMap();
               Iterator var5 = subMap.keySet().iterator();

               while(var5.hasNext()) {
                  String key = (String)var5.next();

                  try {
                     result.put(key, subMap.get(key));
                  } catch (Exception var8) {
                     var8.printStackTrace();
                  }
               }

               return result;
            }
         }
      } catch (Exception var10) {
         var10.printStackTrace();
         return def;
      }
   }

   @Contract("_, !null -> !null")
   @Nullable
   public Material loadMaterial(@NotNull String path, @Nullable Material def) {
      return (Material)this.loadEnum(path, def, Material.class);
   }

   @NotNull
   public List<Material> loadMaterialList(@NotNull String path, @Nullable Collection<Material> def) {
      return this.loadEnumList(path, def, Material.class);
   }

   @NotNull
   public EnumSet<Material> loadMaterialSet(@NotNull String path, @Nullable Collection<Material> def) {
      return this.loadEnumSet(path, def, Material.class);
   }

   @NotNull
   public ItemFlag[] loadItemFlags(@NotNull String path, ItemFlag[] def) {
      return (ItemFlag[])this.loadEnumSet(path, def == null ? null : Arrays.asList(def), ItemFlag.class).toArray(new ItemFlag[0]);
   }

   @NotNull
   private String getError(String path) {
      return "Value has wrong type or wrong value at '" + path + ":' on file " + this.file.getName();
   }

   @Contract("_, !null -> !null")
   @Nullable
   public Object get(@NotNull String path, @Nullable Object def) {
      if (this.multiThreadSupport) {
         synchronized(this) {
            return super.get(path, def);
         }
      } else {
         return super.get(path, def);
      }
   }

   public void set(@NotNull String path, @Nullable Object value) {
      if (this.multiThreadSupport) {
         synchronized(this) {
            super.set(path, value);
         }
      } else {
         super.set(path, value);
      }
   }

   @NotNull
   public ConfigurationSection createSection(@NotNull String path) {
      if (this.multiThreadSupport) {
         synchronized(this) {
            return super.createSection(path);
         }
      } else {
         return super.createSection(path);
      }
   }

   protected void mapChildrenKeys(@NotNull Set<String> output, @NotNull ConfigurationSection section, boolean deep) {
      if (this.multiThreadSupport) {
         synchronized(this) {
            super.mapChildrenKeys(output, section, deep);
         }
      } else {
         super.mapChildrenKeys(output, section, deep);
      }
   }

   protected void mapChildrenValues(@NotNull Map<String, Object> output, @NotNull ConfigurationSection section, boolean deep) {
      if (this.multiThreadSupport) {
         synchronized(this) {
            super.mapChildrenValues(output, section, deep);
         }
      } else {
         super.mapChildrenValues(output, section, deep);
      }
   }

   @NotNull
   public List<String> getComments(@NotNull String path) {
      if (this.multiThreadSupport) {
         synchronized(this) {
            return super.getComments(path);
         }
      } else {
         return super.getComments(path);
      }
   }

   @NotNull
   public List<String> getInlineComments(@NotNull String path) {
      if (this.multiThreadSupport) {
         synchronized(this) {
            return super.getInlineComments(path);
         }
      } else {
         return super.getInlineComments(path);
      }
   }

   public void setComments(@NotNull String path, @Nullable List<String> comments) {
      if (this.multiThreadSupport) {
         synchronized(this) {
            super.setComments(path, comments);
         }
      } else {
         super.setComments(path, comments);
      }
   }

   public void setInlineComments(@NotNull String path, @Nullable List<String> comments) {
      if (this.multiThreadSupport) {
         synchronized(this) {
            super.setInlineComments(path, comments);
         }
      } else {
         super.setInlineComments(path, comments);
      }
   }

   public JavaPlugin getPlugin() {
      return this.plugin;
   }

   public File getFile() {
      return this.file;
   }

   public String getFileName() {
      return this.fileName;
   }

   public boolean isMultiThreadSupport() {
      return this.multiThreadSupport;
   }

   public void setMultiThreadSupport(boolean multiThreadSupport) {
      this.multiThreadSupport = multiThreadSupport;
   }
}
