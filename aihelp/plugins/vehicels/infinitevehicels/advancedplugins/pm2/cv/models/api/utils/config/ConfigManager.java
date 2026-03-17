package advancedplugins.pm2.cv.models.api.utils.config;

import com.google.common.collect.Sets;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

public class ConfigManager {
   private final JavaPlugin plugin;
   private final Map<String, Object> config = new LinkedHashMap();
   private final Set<Runnable> updater = Sets.newConcurrentHashSet();
   private FileConfiguration file;

   public ConfigManager(JavaPlugin var1) {
      this.plugin = var1;
      this.file = var1.getConfig();
   }

   public void register(Property var1) {
      if (var1.getDef() != null) {
         this.register(var1.getPath(), var1.getDef());
      }

   }

   public void register(String var1, @NotNull Object var2) {
      this.config.put(var1, this.file.get(var1, var2));
   }

   public void registerReferenceUpdate(Runnable var1) {
      this.updater.add(var1);
      var1.run();
   }

   public void reload() {
      this.plugin.reloadConfig();
      this.file = this.plugin.getConfig();
      this.config.replaceAll((var1, var2) -> {
         return this.file.get(var1, this.config.get(var1));
      });
   }

   public void updateReferences() {
      this.updater.forEach(Runnable::run);
   }

   public <T> T get(String var1) {
      try {
         return this.config.get(var1);
      } catch (ClassCastException var3) {
         return null;
      }
   }

   public <T> T get(Property var1) {
      return this.get(var1.getPath());
   }

   public int getInt(Property var1) {
      return (Integer)this.get(var1);
   }

   public double getDouble(Property var1) {
      Object var2 = this.get(var1);
      if (var2 instanceof Number) {
         Number var3 = (Number)var2;
         return var3.doubleValue();
      } else {
         return 0.0D;
      }
   }

   public String getString(Property var1) {
      return (String)this.get(var1);
   }

   public boolean getBoolean(Property var1) {
      return (Boolean)this.get(var1);
   }

   public List<String> getStringList(Property var1) {
      return (List)this.get(var1);
   }

   public void save() {
      FileConfiguration var1 = this.file;
      Objects.requireNonNull(var1);
      Map var10000 = this.config;
      Objects.requireNonNull(var1);
      var10000.forEach(var1::set);
   }
}
