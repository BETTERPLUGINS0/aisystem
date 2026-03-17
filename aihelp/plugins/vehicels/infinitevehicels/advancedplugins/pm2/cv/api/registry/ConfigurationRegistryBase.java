package advancedplugins.pm2.cv.api.registry;

import advancedplugins.pm2.cv.api.interfaces.IDeyed;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Iterator;
import java.util.Set;
import java.util.stream.Stream;
import org.bukkit.configuration.file.YamlConfiguration;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public abstract class ConfigurationRegistryBase<T extends IDeyed> extends RegistryBase<T> implements ConfigurationRegistry<T> {
   @NotNull
   protected abstract File getFolder();

   public void load() {
      File var1 = this.getFolder();
      if (var1.exists()) {
         try {
            Stream var2 = Files.walk(var1.toPath());

            try {
               var2.filter((var0) -> {
                  String var1 = var0.getFileName().toString().toLowerCase();
                  return var1.endsWith(".yml") || var1.endsWith(".yaml");
               }).forEach((var1x) -> {
                  File var2 = var1x.toFile();

                  try {
                     IDeyed var3 = this.loadEntry(var2);
                     if (var3 != null) {
                        this.register(var3);
                     }
                  } catch (Exception var4) {
                     var4.printStackTrace();
                  }

               });
            } catch (Throwable var6) {
               if (var2 != null) {
                  try {
                     var2.close();
                  } catch (Throwable var5) {
                     var6.addSuppressed(var5);
                  }
               }

               throw var6;
            }

            if (var2 != null) {
               var2.close();
            }
         } catch (IOException var7) {
            var7.printStackTrace();
         }
      }

   }

   protected abstract T loadEntry(File var1);

   public void reload() {
      try {
         this.saveDefaults();
      } catch (IOException var2) {
         var2.printStackTrace();
      }

      this.entries.clear();
      this.load();
   }

   @Nullable
   protected abstract Set<T> getDefaults();

   public void saveDefaults() {
      Set var1 = this.getDefaults();
      File var2 = this.getFolder();
      if (var1 != null && !var1.isEmpty() && !var2.exists()) {
         if (!var2.mkdirs()) {
            throw new IllegalStateException("couldn't create " + var2.getName() + " folder");
         } else {
            Iterator var3 = var1.iterator();

            while(var3.hasNext()) {
               IDeyed var4 = (IDeyed)var3.next();

               try {
                  this.saveDefault(var4, new File(var2, var4.getId() + ".yml"));
               } catch (Exception var6) {
                  var6.printStackTrace();
               }
            }

         }
      }
   }

   protected void saveDefault(T var1, File var2) {
      if (!var2.exists() && !var2.createNewFile()) {
         throw new IllegalStateException("couldn't create configuration file for default entry (" + var2.getName() + ")");
      } else {
         YamlConfiguration var3 = YamlConfiguration.loadConfiguration(var2);
         this.writeEntry(var1, var3);
         var3.save(var2);
      }
   }

   protected abstract void writeEntry(@NotNull T var1, YamlConfiguration var2);
}
