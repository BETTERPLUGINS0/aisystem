package com.lenis0012.bukkit.loginsecurity.libs.pluginutils.updater.manifest;

import com.lenis0012.bukkit.loginsecurity.libs.pluginutils.updater.UpdateChannel;
import com.lenis0012.bukkit.loginsecurity.libs.pluginutils.updater.Updater;
import com.lenis0012.bukkit.loginsecurity.libs.pluginutils.updater.UpdaterFactory;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;

public class ManifestUpdaterFactory implements UpdaterFactory {
   private Duration frequency = Duration.ofHours(3L);
   private UpdateChannel channel;

   public ManifestUpdaterFactory() {
      this.channel = UpdateChannel.STABLE;
   }

   public Updater getUpdater(Plugin plugin) {
      String manifestUrl;
      try {
         InputStreamReader reader = new InputStreamReader(plugin.getResource("plugin.yml"), StandardCharsets.UTF_8);

         try {
            YamlConfiguration config = YamlConfiguration.loadConfiguration(reader);
            manifestUrl = config.getString("manifest-url");
         } catch (Throwable var7) {
            try {
               reader.close();
            } catch (Throwable var6) {
               var7.addSuppressed(var6);
            }

            throw var7;
         }

         reader.close();
      } catch (Exception var8) {
         throw new IllegalStateException("Failed to load plugin.yml", var8);
      }

      return new ManifestUpdater(plugin, this.frequency, manifestUrl, this.channel);
   }

   public boolean isCompatible(Plugin plugin) {
      try {
         InputStreamReader reader = new InputStreamReader(plugin.getResource("plugin.yml"), StandardCharsets.UTF_8);

         boolean var5;
         label35: {
            try {
               YamlConfiguration config = YamlConfiguration.loadConfiguration(reader);
               String url = config.getString("manifest-url");
               if (url == null) {
                  var5 = false;
                  break label35;
               }

               new URL(url);
               var5 = true;
            } catch (Throwable var7) {
               try {
                  reader.close();
               } catch (Throwable var6) {
                  var7.addSuppressed(var6);
               }

               throw var7;
            }

            reader.close();
            return var5;
         }

         reader.close();
         return var5;
      } catch (Exception var8) {
         return false;
      }
   }

   public UpdaterFactory withChannel(UpdateChannel channel) {
      this.channel = channel;
      return this;
   }

   public UpdaterFactory withFrequency(Duration updateInterval) {
      this.frequency = updateInterval;
      return this;
   }

   public Set<UpdaterFactory.Capability> capabilities() {
      return new HashSet(Arrays.asList(UpdaterFactory.Capability.VERSION_CHECK, UpdaterFactory.Capability.COMPATIBILITY_CHECK, UpdaterFactory.Capability.CHANNELS, UpdaterFactory.Capability.WEBLINK, UpdaterFactory.Capability.DOWNLOAD));
   }
}
