package com.volmit.iris.util.misc;

import com.google.gson.JsonSyntaxException;
import com.volmit.iris.Iris;
import com.volmit.iris.core.IrisSettings;
import com.volmit.iris.core.nms.INMS;
import com.volmit.iris.core.safeguard.IrisSafeguard;
import com.volmit.iris.core.tools.IrisToolbelt;
import com.volmit.iris.engine.platform.PlatformChunkGenerator;
import com.volmit.iris.util.context.IrisContext;
import com.volmit.iris.util.json.JSONException;
import com.volmit.iris.util.kyori.adventure.audience.Audience;
import com.volmit.iris.util.kyori.adventure.platform.bukkit.BukkitAudiences;
import com.volmit.iris.util.kyori.adventure.text.serializer.ComponentSerializer;
import com.volmit.iris.util.metrics.bukkit.Metrics;
import com.volmit.iris.util.metrics.charts.DrilldownPie;
import com.volmit.iris.util.metrics.charts.SingleLineChart;
import com.volmit.iris.util.reflect.ShadeFix;
import com.volmit.iris.util.scheduling.J;
import com.volmit.iris.util.sentry.Attachments;
import com.volmit.iris.util.sentry.IrisLogger;
import com.volmit.iris.util.sentry.Sentry;
import com.volmit.iris.util.sentry.ServerID;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Bindings {
   public static void capture(Throwable throwable) {
      Sentry.captureException(var0);
   }

   public static void setupSentry() {
      IrisSettings.IrisSettingsSentry var0 = IrisSettings.get().getSentry();
      if (!var0.disableAutoReporting && !Sentry.isEnabled() && !Boolean.getBoolean("iris.suppressReporting")) {
         Iris.info("Enabling Sentry for anonymous error reporting. You can disable this in the settings.");
         Iris.info("Your server ID is: " + ServerID.ID);
         Sentry.init((var1) -> {
            var1.setDsn("http://4cdbb9ac953306529947f4ca1e8e6b26@sentry.volmit.com:8080/2");
            if (var0.debug) {
               var1.setLogger(new IrisLogger());
               var1.setDebug(true);
            }

            var1.setAttachServerName(false);
            var1.setEnableUncaughtExceptionHandler(false);
            var1.setRelease(Iris.instance.getDescription().getVersion());
            var1.setEnvironment("production");
            var1.setBeforeSend((var0x, var1x) -> {
               if (suppress(var0x.getThrowable())) {
                  return null;
               } else {
                  var0x.setTag("iris.safeguard", IrisSafeguard.mode().getId());
                  var0x.setTag("iris.nms", INMS.get().getClass().getCanonicalName());
                  IrisContext var2 = IrisContext.get();
                  if (var2 != null) {
                     var0x.getContexts().set("engine", var2.asContext());
                  }

                  var0x.getContexts().set("safeguard", IrisSafeguard.asContext());
                  return var0x;
               }
            });
         });
         Sentry.configureScope((var1) -> {
            if (var0.includeServerId) {
               var1.setUser(ServerID.asUser());
            }

            var1.addAttachment(Attachments.PLUGINS);
            var1.addAttachment(Attachments.SAFEGUARD);
            var1.setTag("server", Bukkit.getVersion());
            var1.setTag("server.type", Bukkit.getName());
            var1.setTag("server.api", Bukkit.getBukkitVersion());
            var1.setTag("iris.commit", "c31158578f8912e73244302ced03481353131ad6");
         });
      }
   }

   private static boolean suppress(Throwable e) {
      boolean var10000;
      label17: {
         if (var0 instanceof IllegalStateException) {
            IllegalStateException var1 = (IllegalStateException)var0;
            if ("zip file closed".equals(var1.getMessage())) {
               break label17;
            }
         }

         if (!(var0 instanceof JSONException) && !(var0 instanceof JsonSyntaxException)) {
            var10000 = false;
            return var10000;
         }
      }

      var10000 = true;
      return var10000;
   }

   public static void setupBstats(Iris plugin) {
      J.s(() -> {
         Metrics var1 = new Metrics(var0, 24220);
         var1.addCustomChart(new SingleLineChart("custom_dimensions", () -> {
            return Bukkit.getWorlds().stream().filter(IrisToolbelt::isIrisWorld).mapToInt((var0) -> {
               return 1;
            }).sum();
         }));
         var1.addCustomChart(new DrilldownPie("used_packs", () -> {
            return (Map)Bukkit.getWorlds().stream().map(IrisToolbelt::access).filter(Objects::nonNull).map(PlatformChunkGenerator::getEngine).filter(Objects::nonNull).collect(Collectors.toMap((var0) -> {
               return var0.getDimension().getLoadKey();
            }, (var0) -> {
               Long var1 = (Long)var0.getHash32().getNow((Object)null);
               if (var1 == null) {
                  return Map.of();
               } else {
                  int var2 = var0.getDimension().getVersion();
                  String var3 = Long.toHexString(var1);
                  return Map.of("v" + var2 + " (" + var3 + ")", 1);
               }
            }, (var0, var1) -> {
               HashMap var2 = new HashMap(var0);
               var1.forEach((var1x, var2x) -> {
                  var2.merge(var1x, var2x, Integer::sum);
               });
               return var2;
            }));
         }));
         var1.addCustomChart(new DrilldownPie("environment", () -> {
            return Map.of("production", Map.of("c31158578f8912e73244302ced03481353131ad6", 1));
         }));
         Objects.requireNonNull(var1);
         var0.postShutdown(var1::shutdown);
      });
   }

   public static class Adventure {
      private final BukkitAudiences audiences;

      public Adventure(Iris plugin) {
         ShadeFix.fix(ComponentSerializer.class);
         this.audiences = BukkitAudiences.create(var1);
      }

      public Audience player(Player player) {
         return this.audiences.player(var1);
      }

      public Audience sender(CommandSender sender) {
         return this.audiences.sender(var1);
      }
   }
}
