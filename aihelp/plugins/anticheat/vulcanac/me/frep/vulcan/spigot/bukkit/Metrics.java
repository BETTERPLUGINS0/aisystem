package me.frep.vulcan.spigot.bukkit;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.zip.GZIPOutputStream;
import javax.net.ssl.HttpsURLConnection;
import me.frep.vulcan.spigot.Vulcan_s;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.ServicePriority;

public class Metrics {
   private final ThreadFactory threadFactory = Metrics::lambda$new$0;
   private final ScheduledExecutorService scheduler;
   public static final int B_STATS_VERSION = 1;
   private static final String URL = "https://bStats.org/submitData/bukkit";
   private boolean enabled;
   private static boolean logFailedRequests;
   private static boolean logSentData;
   private static boolean logResponseStatusText;
   private static String serverUUID;
   private final Plugin plugin;
   private final int pluginId;
   private final List charts;
   private static final long a;

   public Metrics(Plugin var1, int var2) {
      this.scheduler = Executors.newScheduledThreadPool(1, this.threadFactory);
      this.charts = new ArrayList();
      if (var1 == null) {
         throw new IllegalArgumentException("Plugin cannot be null!");
      } else {
         this.plugin = var1;
         this.pluginId = var2;
         File var3 = new File(var1.getDataFolder().getParentFile(), "bStats");
         File var4 = new File(var3, "config.yml");
         YamlConfiguration var5 = YamlConfiguration.loadConfiguration(var4);

         try {
            if (!var5.isSet("serverUuid")) {
               var5.addDefault("enabled", true);
               var5.addDefault("serverUuid", UUID.randomUUID().toString());
               var5.addDefault("logFailedRequests", false);
               var5.addDefault("logSentData", false);
               var5.addDefault("logResponseStatusText", false);
               var5.options().header("bStats collects some data for plugin authors like how many servers are using their plugins.\nTo honor their work, you should not disable it.\nThis has nearly no effect on the server performance!\nCheck out https://bStats.org/ to learn more :)").copyDefaults(true);

               try {
                  var5.save(var4);
               } catch (IOException var11) {
               }
            }
         } catch (NoSuchFieldException var12) {
            throw a(var12);
         }

         this.enabled = var5.getBoolean("enabled", true);
         serverUUID = var5.getString("serverUuid");
         logFailedRequests = var5.getBoolean("logFailedRequests", false);
         logSentData = var5.getBoolean("logSentData", false);
         logResponseStatusText = var5.getBoolean("logResponseStatusText", false);
         if (this.enabled) {
            boolean var6 = false;
            Iterator var7 = Bukkit.getServicesManager().getKnownServices().iterator();

            while(var7.hasNext()) {
               Class var8 = (Class)var7.next();

               try {
                  String var10003 = Vulcan_s.c(var8, "B_STATS_VERSION");
                  String var10002 = "B_STATS_VERSION";
                  var8.getField(var10003);
                  var6 = true;
                  break;
               } catch (NoSuchFieldException var13) {
               }
            }

            try {
               Bukkit.getServicesManager().register(Metrics.class, this, var1, ServicePriority.Normal);
               if (!var6) {
                  this.startSubmitting();
               }
            } catch (IOException var10) {
               throw a(var10);
            }
         }

      }
   }

   public boolean isEnabled() {
      return this.enabled;
   }

   public void addCustomChart(Vulcan_i7 var1) {
      try {
         if (var1 == null) {
            throw new IllegalArgumentException("Chart cannot be null!");
         }
      } catch (IllegalArgumentException var2) {
         throw a(var2);
      }

      this.charts.add(var1);
   }

   private void startSubmitting() {
      Runnable var1 = this::lambda$startSubmitting$1;
      long var2 = (long)(60000.0D * (3.0D + Math.random() * 3.0D));
      long var4 = (long)(60000.0D * Math.random() * 30.0D);
      this.scheduler.schedule(var1, var2, TimeUnit.MILLISECONDS);
      this.scheduler.scheduleAtFixedRate(var1, var2 + var4, 1800000L, TimeUnit.MILLISECONDS);
   }

   public JsonObject getPluginData() {
      long var1 = a ^ 121433131440654L;
      long var3 = var1 ^ 12269068711971L;
      JsonObject var5 = new JsonObject();
      String var6 = this.plugin.getDescription().getName();
      String var7 = this.plugin.getDescription().getVersion();
      var5.addProperty("pluginName", var6);
      var5.addProperty("id", this.pluginId);
      var5.addProperty("pluginVersion", var7);
      JsonArray var8 = new JsonArray();
      Iterator var9 = this.charts.iterator();

      while(var9.hasNext()) {
         Vulcan_i7 var10 = (Vulcan_i7)var9.next();
         JsonObject var11 = Vulcan_i7.Vulcan_e(new Object[]{var3, var10});

         try {
            if (var11 == null) {
               continue;
            }
         } catch (IllegalArgumentException var12) {
            throw a(var12);
         }

         var8.add(var11);
      }

      var5.add("customCharts", var8);
      return var5;
   }

   private JsonObject getServerData() {
      int var1;
      try {
         Method var2 = Class.forName("org.bukkit.Server").getMethod("getOnlinePlayers");

         int var10000;
         label33: {
            try {
               if (var2.getReturnType().equals(Collection.class)) {
                  var10000 = ((Collection)var2.invoke(Bukkit.getServer())).size();
                  break label33;
               }
            } catch (Exception var12) {
               throw a(var12);
            }

            var10000 = ((Player[])((Player[])var2.invoke(Bukkit.getServer()))).length;
         }

         var1 = var10000;
      } catch (Exception var13) {
         var1 = Bukkit.getOnlinePlayers().size();
      }

      byte var15;
      label26: {
         try {
            if (Bukkit.getOnlineMode()) {
               var15 = 1;
               break label26;
            }
         } catch (Exception var11) {
            throw a(var11);
         }

         var15 = 0;
      }

      byte var14 = var15;
      String var3 = Bukkit.getVersion();
      String var4 = Bukkit.getName();
      String var5 = System.getProperty("java.version");
      String var6 = System.getProperty("os.name");
      String var7 = System.getProperty("os.arch");
      String var8 = System.getProperty("os.version");
      int var9 = Runtime.getRuntime().availableProcessors();
      JsonObject var10 = new JsonObject();
      var10.addProperty("serverUUID", serverUUID);
      var10.addProperty("playerAmount", var1);
      var10.addProperty("onlineMode", Integer.valueOf(var14));
      var10.addProperty("bukkitVersion", var3);
      var10.addProperty("bukkitName", var4);
      var10.addProperty("javaVersion", var5);
      var10.addProperty("osName", var6);
      var10.addProperty("osArch", var7);
      var10.addProperty("osVersion", var8);
      var10.addProperty("coreCount", var9);
      return var10;
   }

   private void submitData() {
      JsonObject var1 = this.getServerData();
      JsonArray var2 = new JsonArray();
      Iterator var3 = Bukkit.getServicesManager().getKnownServices().iterator();

      while(var3.hasNext()) {
         Class var4 = (Class)var3.next();

         try {
            String var10003 = Vulcan_s.c(var4, "B_STATS_VERSION");
            String var10002 = "B_STATS_VERSION";
            var4.getField(var10003);
            Iterator var5 = Bukkit.getServicesManager().getRegistrations(var4).iterator();

            while(var5.hasNext()) {
               RegisteredServiceProvider var6 = (RegisteredServiceProvider)var5.next();

               try {
                  Class var10000 = var6.getService();
                  Class[] var18 = new Class[0];
                  Object var7 = var10000.getMethod(Vulcan_s.b("getPluginData", var10000, var18), var18).invoke(var6.getProvider());

                  try {
                     if (var7 instanceof JsonObject) {
                        var2.add((JsonObject)var7);
                        continue;
                     }
                  } catch (ClassNotFoundException var14) {
                     throw a(var14);
                  }

                  try {
                     Class var17 = Class.forName("org.json.simple.JSONObject");
                     if (var7.getClass().isAssignableFrom(var17)) {
                        Method var9 = var17.getDeclaredMethod("toJSONString");
                        var9.setAccessible(true);
                        String var10 = (String)var9.invoke(var7);
                        JsonObject var11 = (new JsonParser()).parse(var10).getAsJsonObject();
                        var2.add(var11);
                     }
                  } catch (ClassNotFoundException var13) {
                     ClassNotFoundException var8 = var13;

                     try {
                        if (logFailedRequests) {
                           this.plugin.getLogger().log(Level.SEVERE, "Encountered unexpected exception", var8);
                        }
                     } catch (ClassNotFoundException var12) {
                        throw a(var12);
                     }
                  }
               } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException | NullPointerException var15) {
               }
            }
         } catch (NoSuchFieldException var16) {
         }
      }

      var1.add("plugins", var2);
      (new Thread(this::lambda$submitData$2)).start();
   }

   private static void sendData(Plugin var0, JsonObject var1) {
      try {
         if (var1 == null) {
            throw new IllegalArgumentException("Data cannot be null!");
         }
      } catch (Throwable var55) {
         throw a(var55);
      }

      try {
         if (Bukkit.isPrimaryThread()) {
            throw new IllegalAccessException("This method must not be called from the main thread!");
         }
      } catch (Throwable var47) {
         throw a(var47);
      }

      try {
         if (logSentData) {
            var0.getLogger().info("Sending data to bStats: " + var1);
         }
      } catch (Throwable var54) {
         throw a(var54);
      }

      HttpsURLConnection var2 = (HttpsURLConnection)(new URL("https://bStats.org/submitData/bukkit")).openConnection();
      byte[] var3 = compress(var1.toString());
      var2.setRequestMethod("POST");
      var2.addRequestProperty("Accept", "application/json");
      var2.addRequestProperty("Connection", "close");
      var2.addRequestProperty("Content-Encoding", "gzip");
      var2.addRequestProperty("Content-Length", String.valueOf(var3.length));
      var2.setRequestProperty("Content-Type", "application/json");
      var2.setRequestProperty("User-Agent", "MC-Server/1");
      var2.setDoOutput(true);
      DataOutputStream var4 = new DataOutputStream(var2.getOutputStream());
      Throwable var5 = null;
      boolean var41 = false;

      try {
         var41 = true;
         var4.write(var3);
         var41 = false;
      } catch (Throwable var46) {
         var5 = var46;
         throw var46;
      } finally {
         if (var41) {
            label320: {
               label319: {
                  try {
                     if (var4 == null) {
                        break label320;
                     }

                     if (var5 == null) {
                        break label319;
                     }
                  } catch (Throwable var48) {
                     throw a(var48);
                  }

                  try {
                     var4.close();
                  } catch (Throwable var43) {
                     var5.addSuppressed(var43);
                  }
                  break label320;
               }

               var4.close();
            }

         }
      }

      if (var4 != null) {
         if (var5 != null) {
            try {
               var4.close();
            } catch (Throwable var45) {
               var5.addSuppressed(var45);
            }
         } else {
            var4.close();
         }
      }

      StringBuilder var56 = new StringBuilder();
      BufferedReader var57 = new BufferedReader(new InputStreamReader(var2.getInputStream()));
      Throwable var6 = null;

      try {
         while(true) {
            String var7;
            String var10000 = var7 = var57.readLine();

            try {
               if (var10000 == null) {
                  break;
               }

               var56.append(var7);
            } catch (Throwable var51) {
               throw a(var51);
            }
         }
      } catch (Throwable var52) {
         var6 = var52;
         throw var52;
      } finally {
         label333: {
            label332: {
               try {
                  if (var57 == null) {
                     break label333;
                  }

                  if (var6 == null) {
                     break label332;
                  }
               } catch (Throwable var50) {
                  throw a(var50);
               }

               try {
                  var57.close();
               } catch (Throwable var42) {
                  var6.addSuppressed(var42);
               }
               break label333;
            }

            var57.close();
         }

      }

      try {
         if (logResponseStatusText) {
            var0.getLogger().info("Sent data to bStats and received response: " + var56);
         }

      } catch (Throwable var44) {
         throw a(var44);
      }
   }

   private static byte[] compress(String var0) {
      try {
         if (var0 == null) {
            return null;
         }
      } catch (Throwable var19) {
         throw a(var19);
      }

      ByteArrayOutputStream var1 = new ByteArrayOutputStream();
      GZIPOutputStream var2 = new GZIPOutputStream(var1);
      Throwable var3 = null;
      boolean var13 = false;

      try {
         var13 = true;
         var2.write(var0.getBytes(StandardCharsets.UTF_8));
         var13 = false;
      } catch (Throwable var16) {
         var3 = var16;
         throw var16;
      } finally {
         if (var13) {
            label97: {
               label96: {
                  try {
                     if (var2 == null) {
                        break label97;
                     }

                     if (var3 != null) {
                        break label96;
                     }
                  } catch (Throwable var17) {
                     throw a(var17);
                  }

                  var2.close();
                  break label97;
               }

               try {
                  var2.close();
               } catch (Throwable var14) {
                  var3.addSuppressed(var14);
               }
            }

         }
      }

      if (var2 != null) {
         if (var3 != null) {
            try {
               var2.close();
            } catch (Throwable var15) {
               var3.addSuppressed(var15);
            }
         } else {
            var2.close();
         }
      }

      return var1.toByteArray();
   }

   private void lambda$submitData$2(JsonObject var1) {
      try {
         sendData(this.plugin, var1);
      } catch (Exception var4) {
         Exception var2 = var4;

         try {
            if (logFailedRequests) {
               this.plugin.getLogger().log(Level.WARNING, "Could not submit plugin stats of " + this.plugin.getName(), var2);
            }
         } catch (Exception var3) {
            throw a(var3);
         }
      }

   }

   private void lambda$startSubmitting$1() {
      try {
         if (!this.plugin.isEnabled()) {
            this.scheduler.shutdown();
            return;
         }
      } catch (IllegalArgumentException var1) {
         throw a(var1);
      }

      Bukkit.getScheduler().runTask(this.plugin, this::submitData);
   }

   private static Thread lambda$new$0(Runnable var0) {
      return new Thread(var0, "bStats-Metrics");
   }

   static boolean access$100() {
      return logFailedRequests;
   }

   static {
      // $FF: Couldn't be decompiled
   }

   private static Throwable a(Throwable var0) {
      return var0;
   }
}
