package com.volmit.iris.core.service;

import com.volmit.iris.Iris;
import com.volmit.iris.core.link.ExternalDataProvider;
import com.volmit.iris.core.link.Identifier;
import com.volmit.iris.core.link.data.DataType;
import com.volmit.iris.core.nms.container.BlockProperty;
import com.volmit.iris.core.nms.container.Pair;
import com.volmit.iris.engine.framework.Engine;
import com.volmit.iris.util.collection.KList;
import com.volmit.iris.util.collection.KMap;
import com.volmit.iris.util.io.JarScanner;
import com.volmit.iris.util.plugin.IrisService;
import com.volmit.iris.util.scheduling.J;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.MissingResourceException;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import lombok.Generated;
import lombok.NonNull;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.block.data.BlockData;
import org.bukkit.entity.Entity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.server.PluginEnableEvent;
import org.bukkit.inventory.ItemStack;

public class ExternalDataSVC implements IrisService {
   private KList<ExternalDataProvider> providers = new KList();
   private KList<ExternalDataProvider> activeProviders = new KList();

   public void onEnable() {
      Iris.info("Loading ExternalDataProvider...");
      Bukkit.getPluginManager().registerEvents(this, Iris.instance);
      this.providers.addAll(createProviders());
      Iterator var1 = this.providers.iterator();

      while(var1.hasNext()) {
         ExternalDataProvider var2 = (ExternalDataProvider)var1.next();
         if (var2.isReady()) {
            this.activeProviders.add((Object)var2);
            var2.init();
            Iris.instance.registerListener(var2);
            Iris.info("Enabled ExternalDataProvider for %s.", var2.getPluginId());
         }
      }

   }

   public void onDisable() {
   }

   @EventHandler
   public void onPluginEnable(PluginEnableEvent e) {
      if (this.activeProviders.stream().noneMatch((var1x) -> {
         return var1.getPlugin().equals(var1x.getPlugin());
      })) {
         this.providers.stream().filter((var1x) -> {
            return var1x.isReady() && var1.getPlugin().equals(var1x.getPlugin());
         }).findFirst().ifPresent((var1x) -> {
            this.activeProviders.add((Object)var1x);
            var1x.init();
            Iris.instance.registerListener(var1x);
            Iris.info("Enabled ExternalDataProvider for %s.", var1x.getPluginId());
         });
      }

   }

   public void registerProvider(@NonNull ExternalDataProvider provider) {
      if (var1 == null) {
         throw new NullPointerException("provider is marked non-null but is null");
      } else {
         String var2 = var1.getPluginId();
         Stream var10000 = this.providers.stream().map(ExternalDataProvider::getPluginId);
         Objects.requireNonNull(var2);
         if (var10000.anyMatch(var2::equals)) {
            throw new IllegalArgumentException("A provider with the same plugin id already exists.");
         } else {
            this.providers.add((Object)var1);
            if (var1.isReady()) {
               this.activeProviders.add((Object)var1);
               var1.init();
               Iris.instance.registerListener(var1);
            }

         }
      }
   }

   public Optional<BlockData> getBlockData(final Identifier key) {
      Pair var2 = parseState(var1);
      Identifier var3 = (Identifier)var2.getA();
      Optional var4 = this.activeProviders.stream().filter((var1x) -> {
         return var1x.isValidProvider(var3, DataType.BLOCK);
      }).findFirst();
      if (var4.isEmpty()) {
         return Optional.empty();
      } else {
         try {
            return Optional.of(((ExternalDataProvider)var4.get()).getBlockData(var3, (KMap)var2.getB()));
         } catch (MissingResourceException var6) {
            String var10000 = var6.getMessage();
            Iris.error(var10000 + " - [" + var6.getClassName() + ":" + var6.getKey() + "]");
            return Optional.empty();
         }
      }
   }

   public Optional<List<BlockProperty>> getBlockProperties(final Identifier key) {
      Optional var2 = this.activeProviders.stream().filter((var1x) -> {
         return var1x.isValidProvider(var1, DataType.BLOCK);
      }).findFirst();
      if (var2.isEmpty()) {
         return Optional.empty();
      } else {
         try {
            return Optional.of(((ExternalDataProvider)var2.get()).getBlockProperties(var1));
         } catch (MissingResourceException var4) {
            String var10000 = var4.getMessage();
            Iris.error(var10000 + " - [" + var4.getClassName() + ":" + var4.getKey() + "]");
            return Optional.empty();
         }
      }
   }

   public Optional<ItemStack> getItemStack(Identifier key, KMap<String, Object> customNbt) {
      Optional var3 = this.activeProviders.stream().filter((var1x) -> {
         return var1x.isValidProvider(var1, DataType.ITEM);
      }).findFirst();
      if (var3.isEmpty()) {
         Iris.warn("No matching Provider found for modded material \"%s\"!", var1);
         return Optional.empty();
      } else {
         try {
            return Optional.of(((ExternalDataProvider)var3.get()).getItemStack(var1, var2));
         } catch (MissingResourceException var5) {
            String var10000 = var5.getMessage();
            Iris.error(var10000 + " - [" + var5.getClassName() + ":" + var5.getKey() + "]");
            return Optional.empty();
         }
      }
   }

   public void processUpdate(Engine engine, Block block, Identifier blockId) {
      Optional var4 = this.activeProviders.stream().filter((var1x) -> {
         return var1x.isValidProvider(var3, DataType.BLOCK);
      }).findFirst();
      if (var4.isEmpty()) {
         Iris.warn("No matching Provider found for modded material \"%s\"!", var3);
      } else {
         ((ExternalDataProvider)var4.get()).processUpdate(var1, var2, var3);
      }
   }

   public Entity spawnMob(Location location, Identifier mobId) {
      Optional var3 = this.activeProviders.stream().filter((var1x) -> {
         return var1x.isValidProvider(var2, DataType.ENTITY);
      }).findFirst();
      if (var3.isEmpty()) {
         Iris.warn("No matching Provider found for modded mob \"%s\"!", var2);
         return null;
      } else {
         try {
            return ((ExternalDataProvider)var3.get()).spawnMob(var1, var2);
         } catch (MissingResourceException var5) {
            String var10000 = var5.getMessage();
            Iris.error(var10000 + " - [" + var5.getClassName() + ":" + var5.getKey() + "]");
            return null;
         }
      }
   }

   public Collection<Identifier> getAllIdentifiers(DataType dataType) {
      return this.activeProviders.stream().flatMap((var1x) -> {
         return var1x.getTypes(var1).stream();
      }).toList();
   }

   public Collection<Pair<Identifier, List<BlockProperty>>> getAllBlockProperties() {
      return this.activeProviders.stream().flatMap((var0) -> {
         return var0.getTypes(DataType.BLOCK).stream().map((var1) -> {
            return new Pair(var1, var0.getBlockProperties(var1));
         });
      }).toList();
   }

   public static Pair<Identifier, KMap<String, String>> parseState(Identifier key) {
      if (var0.key().contains("[") && var0.key().contains("]")) {
         String var1 = var0.key().split("\\Q[\\E")[1].split("\\Q]\\E")[0];
         KMap var2 = new KMap();
         if (!var1.isEmpty()) {
            Arrays.stream(var1.split(",")).forEach((var1x) -> {
               var2.put(var1x.split("=")[0], var1x.split("=")[1]);
            });
         }

         return new Pair(new Identifier(var0.namespace(), var0.key().split("\\Q[\\E")[0]), var2);
      } else {
         return new Pair(var0, new KMap());
      }
   }

   public static Identifier buildState(Identifier key, KMap<String, String> state) {
      if (var1.isEmpty()) {
         return var0;
      } else {
         String var2 = (String)var1.entrySet().stream().map((var0x) -> {
            String var10000 = (String)var0x.getKey();
            return var10000 + "=" + (String)var0x.getValue();
         }).collect(Collectors.joining(",", var0.key() + "[", "]"));
         return new Identifier(var0.namespace(), var2);
      }
   }

   private static KList<ExternalDataProvider> createProviders() {
      JarScanner var0 = new JarScanner(Iris.instance.getJarFile(), "com.volmit.iris.core.link.data", false);
      Objects.requireNonNull(var0);
      J.attempt(var0::scan);
      KList var1 = new KList();
      Iterator var2 = var0.getClasses().iterator();

      while(var2.hasNext()) {
         Class var3 = (Class)var2.next();
         if (ExternalDataProvider.class.isAssignableFrom(var3)) {
            try {
               ExternalDataProvider var4 = (ExternalDataProvider)var3.getDeclaredConstructor().newInstance();
               if (var4.getPlugin() != null) {
                  Iris.info(var4.getPluginId() + " found, loading " + var3.getSimpleName() + "...");
               }

               var1.add((Object)var4);
            } catch (Throwable var5) {
            }
         }
      }

      return var1;
   }

   @Generated
   public KList<ExternalDataProvider> getProviders() {
      return this.providers;
   }

   @Generated
   public KList<ExternalDataProvider> getActiveProviders() {
      return this.activeProviders;
   }

   @Generated
   public void setProviders(final KList<ExternalDataProvider> providers) {
      this.providers = var1;
   }

   @Generated
   public void setActiveProviders(final KList<ExternalDataProvider> activeProviders) {
      this.activeProviders = var1;
   }

   @Generated
   public boolean equals(final Object o) {
      if (var1 == this) {
         return true;
      } else if (!(var1 instanceof ExternalDataSVC)) {
         return false;
      } else {
         ExternalDataSVC var2 = (ExternalDataSVC)var1;
         if (!var2.canEqual(this)) {
            return false;
         } else {
            KList var3 = this.getProviders();
            KList var4 = var2.getProviders();
            if (var3 == null) {
               if (var4 != null) {
                  return false;
               }
            } else if (!var3.equals(var4)) {
               return false;
            }

            KList var5 = this.getActiveProviders();
            KList var6 = var2.getActiveProviders();
            if (var5 == null) {
               if (var6 != null) {
                  return false;
               }
            } else if (!var5.equals(var6)) {
               return false;
            }

            return true;
         }
      }
   }

   @Generated
   protected boolean canEqual(final Object other) {
      return var1 instanceof ExternalDataSVC;
   }

   @Generated
   public int hashCode() {
      boolean var1 = true;
      byte var2 = 1;
      KList var3 = this.getProviders();
      int var5 = var2 * 59 + (var3 == null ? 43 : var3.hashCode());
      KList var4 = this.getActiveProviders();
      var5 = var5 * 59 + (var4 == null ? 43 : var4.hashCode());
      return var5;
   }

   @Generated
   public String toString() {
      String var10000 = String.valueOf(this.getProviders());
      return "ExternalDataSVC(providers=" + var10000 + ", activeProviders=" + String.valueOf(this.getActiveProviders()) + ")";
   }
}
