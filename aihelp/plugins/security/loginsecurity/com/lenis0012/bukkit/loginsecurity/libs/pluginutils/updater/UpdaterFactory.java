package com.lenis0012.bukkit.loginsecurity.libs.pluginutils.updater;

import java.time.Duration;
import java.util.Comparator;
import java.util.ServiceLoader;
import java.util.Set;
import java.util.stream.StreamSupport;
import org.bukkit.plugin.Plugin;

public interface UpdaterFactory {
   Updater getUpdater(Plugin var1);

   boolean isCompatible(Plugin var1);

   UpdaterFactory withChannel(UpdateChannel var1);

   UpdaterFactory withFrequency(Duration var1);

   Set<UpdaterFactory.Capability> capabilities();

   static UpdaterFactory provideBest(Plugin plugin, ClassLoader loader) {
      ServiceLoader<UpdaterFactory> serviceLoader = ServiceLoader.load(UpdaterFactory.class, loader);
      return (UpdaterFactory)StreamSupport.stream(serviceLoader.spliterator(), false).filter((factory) -> {
         return factory.isCompatible(plugin);
      }).max(Comparator.comparingInt((factory) -> {
         return factory.capabilities().size();
      })).orElseThrow(() -> {
         return new IllegalStateException("No updater implementation found");
      });
   }

   public static enum Capability {
      VERSION_CHECK,
      CHANNELS,
      COMPATIBILITY_CHECK,
      WEBLINK,
      DOWNLOAD,
      DEV_BUILDS;
   }
}
