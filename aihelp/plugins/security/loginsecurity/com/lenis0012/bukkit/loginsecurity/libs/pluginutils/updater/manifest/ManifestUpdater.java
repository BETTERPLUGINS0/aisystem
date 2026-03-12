package com.lenis0012.bukkit.loginsecurity.libs.pluginutils.updater.manifest;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.lenis0012.bukkit.loginsecurity.libs.pluginutils.updater.AbstractUpdater;
import com.lenis0012.bukkit.loginsecurity.libs.pluginutils.updater.UpdateChannel;
import com.lenis0012.bukkit.loginsecurity.libs.pluginutils.updater.Version;
import com.lenis0012.bukkit.loginsecurity.libs.pluginutils.updater.VersionNumber;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.time.Duration;
import java.util.Iterator;
import java.util.Locale;
import org.bukkit.plugin.Plugin;

public class ManifestUpdater extends AbstractUpdater {
   private final JsonParser jsonParser = new JsonParser();
   private final String manifestUrl;
   private final UpdateChannel channel;

   public ManifestUpdater(Plugin plugin, Duration frequency, String manifestUrl, UpdateChannel channel) {
      super(plugin, frequency);
      this.manifestUrl = manifestUrl;
      this.channel = channel;
   }

   protected Version fetchLatestVersion() {
      JsonElement json = this.readJsonFromURL(this.manifestUrl);
      if (json == null) {
         return null;
      } else if (!json.isJsonArray()) {
         return null;
      } else {
         Iterator var2 = json.getAsJsonArray().iterator();

         Version version;
         do {
            if (!var2.hasNext()) {
               return null;
            }

            JsonElement versionElement = (JsonElement)var2.next();
            version = this.parseVersion(versionElement.getAsJsonObject());
         } while(!this.isCompatible(version, this.channel));

         return version;
      }
   }

   private Version parseVersion(JsonObject info) {
      return Version.builder().versionNumber(VersionNumber.of(info.get("version").getAsString())).downloadUrl(info.has("downloadUrl") ? info.get("downloadUrl").getAsString() : null).changelogUrl(info.has("changelogUrl") ? info.get("changelogUrl").getAsString() : null).channel(info.has("channel") ? UpdateChannel.valueOf(info.get("channel").getAsString().toUpperCase(Locale.ROOT)) : UpdateChannel.STABLE).minMinecraftVersion(info.has("minMcVersion") ? VersionNumber.of(info.get("minMcVersion").getAsString()) : null).maxMinecraftVersion(info.has("maxMcVersion") ? VersionNumber.of(info.get("maxMcVersion").getAsString()) : null).build();
   }

   protected JsonElement readJsonFromURL(String downloadURL) {
      BufferedReader reader = null;

      URLConnection connection;
      try {
         URL url = new URL(downloadURL);
         connection = url.openConnection();
         connection.setUseCaches(false);
         connection.addRequestProperty("User-Agent", this.getClass().getSimpleName() + "/v1 (by lenis0012)");
         reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
         StringBuilder builder = new StringBuilder();

         String line;
         while((line = reader.readLine()) != null) {
            builder.append(line);
         }

         JsonElement var7 = this.jsonParser.parse(builder.toString());
         return var7;
      } catch (IOException var17) {
         this.verboseLog("Failed to read JSON from URL: " + downloadURL, var17);
         connection = null;
      } finally {
         if (reader != null) {
            try {
               reader.close();
            } catch (IOException var16) {
            }
         }

      }

      return connection;
   }
}
