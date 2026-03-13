package com.volmit.iris.util.sentry;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.volmit.iris.core.safeguard.IrisSafeguard;
import com.volmit.iris.util.collection.KMap;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.Callable;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;

public class Attachments {
   private static final Gson GSON = (new GsonBuilder()).disableHtmlEscaping().create();
   public static final Attachment PLUGINS = jsonProvider(Attachments::plugins, "plugins.json");
   public static final Attachment SAFEGUARD = jsonProvider(IrisSafeguard::asAttachment, "safeguard.json");

   public static Attachment json(Object object, String name) {
      return new Attachment(GSON.toJson(var0).getBytes(StandardCharsets.UTF_8), var1, "application/json", "event.attachment", true);
   }

   public static Attachment jsonProvider(Callable<Object> object, String name) {
      return new Attachment(() -> {
         return GSON.toJson(var0.call()).getBytes(StandardCharsets.UTF_8);
      }, var1, "application/json", "event.attachment", true);
   }

   private static KMap<String, Object> plugins() {
      KMap var0 = new KMap();
      KMap var1 = new KMap();
      PluginManager var2 = Bukkit.getPluginManager();
      Plugin[] var3 = var2.getPlugins();
      int var4 = var3.length;

      for(int var5 = 0; var5 < var4; ++var5) {
         Plugin var6 = var3[var5];
         if (var6.isEnabled()) {
            var0.put(var6.getName(), var6.getDescription().getVersion());
         } else {
            var1.put(var6.getName(), var6.getDescription().getVersion());
         }
      }

      return (new KMap()).qput("enabled", var0).qput("disabled", var1);
   }
}
