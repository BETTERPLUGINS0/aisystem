package com.volmit.iris.core;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.volmit.iris.Iris;
import com.volmit.iris.core.loader.IrisData;
import com.volmit.iris.core.loader.IrisRegistrant;
import com.volmit.iris.engine.data.cache.AtomicCache;
import com.volmit.iris.engine.object.IrisDimension;
import com.volmit.iris.util.collection.KMap;
import com.volmit.iris.util.io.IO;
import com.volmit.iris.util.misc.ServerProperties;
import java.io.File;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.lang.reflect.Type;
import java.util.Iterator;
import java.util.Objects;
import java.util.stream.Stream;
import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;

public class IrisWorlds {
   private static final AtomicCache<IrisWorlds> cache = new AtomicCache();
   private static final Gson GSON = (new GsonBuilder()).setPrettyPrinting().create();
   private static final Type TYPE = TypeToken.getParameterized(KMap.class, new Type[]{String.class, String.class}).getType();
   private final KMap<String, String> worlds;
   private volatile boolean dirty = false;

   private IrisWorlds(KMap<String, String> worlds) {
      this.worlds = var1;
      readBukkitWorlds().forEach(this::put0);
      this.save();
   }

   public static IrisWorlds get() {
      return (IrisWorlds)cache.aquire(() -> {
         File var0 = Iris.instance.getDataFile(new String[]{"worlds.json"});
         if (!var0.exists()) {
            return new IrisWorlds(new KMap());
         } else {
            try {
               String var1 = IO.readAll(var0);
               KMap var2 = (KMap)GSON.fromJson(var1, TYPE);
               return new IrisWorlds((KMap)Objects.requireNonNullElseGet(var2, KMap::new));
            } catch (Throwable var3) {
               Iris.error("Failed to load worlds.json!");
               var3.printStackTrace();
               Iris.reportError(var3);
               return new IrisWorlds(new KMap());
            }
         }
      });
   }

   public void put(String name, String type) {
      this.put0(var1, var2);
      this.save();
   }

   private void put0(String name, String type) {
      String var3 = (String)this.worlds.put(var1, var2);
      if (!var2.equals(var3)) {
         this.dirty = true;
      }

   }

   public KMap<String, String> getWorlds() {
      this.clean();
      return readBukkitWorlds().put(this.worlds);
   }

   public Stream<IrisData> getPacks() {
      return this.getDimensions().map(IrisRegistrant::getLoader).filter(Objects::nonNull);
   }

   public Stream<IrisDimension> getDimensions() {
      return this.getWorlds().entrySet().stream().map((var0) -> {
         return Iris.loadDimension((String)var0.getKey(), (String)var0.getValue());
      }).filter(Objects::nonNull);
   }

   public void clean() {
      this.dirty = this.worlds.entrySet().removeIf((var0) -> {
         File var10002 = Bukkit.getWorldContainer();
         String var10003 = (String)var0.getKey();
         return !(new File(var10002, var10003 + "/iris/pack/dimensions/" + (String)var0.getValue() + ".json")).exists();
      });
   }

   public synchronized void save() {
      this.clean();
      if (this.dirty) {
         try {
            IO.write(Iris.instance.getDataFile(new String[]{"worlds.json"}), OutputStreamWriter::new, (var1) -> {
               GSON.toJson(this.worlds, TYPE, var1);
            });
            this.dirty = false;
         } catch (IOException var2) {
            Iris.error("Failed to save worlds.json!");
            var2.printStackTrace();
            Iris.reportError(var2);
         }

      }
   }

   public static KMap<String, String> readBukkitWorlds() {
      YamlConfiguration var0 = YamlConfiguration.loadConfiguration(ServerProperties.BUKKIT_YML);
      ConfigurationSection var1 = var0.getConfigurationSection("worlds");
      if (var1 == null) {
         return new KMap();
      } else {
         KMap var2 = new KMap();
         Iterator var3 = var1.getKeys(false).iterator();

         while(true) {
            String var4;
            String var6;
            while(true) {
               String var5;
               do {
                  if (!var3.hasNext()) {
                     return var2;
                  }

                  var4 = (String)var3.next();
                  var5 = var1.getString(var4 + ".generator");
               } while(var5 == null);

               if (var5.equalsIgnoreCase("iris")) {
                  var6 = IrisSettings.get().getGenerator().getDefaultWorldType();
                  break;
               }

               if (var5.startsWith("Iris:")) {
                  var6 = var5.substring(5);
                  break;
               }
            }

            var2.put(var4, var6);
         }
      }
   }
}
