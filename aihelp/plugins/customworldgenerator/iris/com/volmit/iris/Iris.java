package com.volmit.iris;

import com.google.gson.JsonParseException;
import com.google.gson.JsonParser;
import com.volmit.iris.core.IrisSettings;
import com.volmit.iris.core.IrisWorlds;
import com.volmit.iris.core.ServerConfigurator;
import com.volmit.iris.core.link.IrisPapiExpansion;
import com.volmit.iris.core.link.MultiverseCoreLink;
import com.volmit.iris.core.loader.IrisData;
import com.volmit.iris.core.nms.INMS;
import com.volmit.iris.core.pregenerator.LazyPregenerator;
import com.volmit.iris.core.safeguard.IrisSafeguard;
import com.volmit.iris.core.service.StudioSVC;
import com.volmit.iris.core.tools.IrisToolbelt;
import com.volmit.iris.engine.EnginePanic;
import com.volmit.iris.engine.object.IrisCompat;
import com.volmit.iris.engine.object.IrisDimension;
import com.volmit.iris.engine.object.IrisWorld;
import com.volmit.iris.engine.platform.BukkitChunkGenerator;
import com.volmit.iris.engine.platform.PlatformChunkGenerator;
import com.volmit.iris.util.collection.KList;
import com.volmit.iris.util.collection.KMap;
import com.volmit.iris.util.exceptions.IrisException;
import com.volmit.iris.util.format.C;
import com.volmit.iris.util.function.NastyRunnable;
import com.volmit.iris.util.io.FileWatcher;
import com.volmit.iris.util.io.IO;
import com.volmit.iris.util.io.InstanceState;
import com.volmit.iris.util.io.JarScanner;
import com.volmit.iris.util.math.M;
import com.volmit.iris.util.math.RNG;
import com.volmit.iris.util.misc.Bindings;
import com.volmit.iris.util.misc.SlimJar;
import com.volmit.iris.util.parallel.MultiBurst;
import com.volmit.iris.util.plugin.IrisService;
import com.volmit.iris.util.plugin.VolmitPlugin;
import com.volmit.iris.util.plugin.VolmitSender;
import com.volmit.iris.util.plugin.chunk.ChunkTickets;
import com.volmit.iris.util.scheduling.J;
import com.volmit.iris.util.scheduling.Queue;
import com.volmit.iris.util.scheduling.ShurikenQueue;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Serializable;
import java.lang.annotation.Annotation;
import java.net.URL;
import java.util.Date;
import java.util.Iterator;
import java.util.Map;
import java.util.Objects;
import java.util.function.Predicate;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import lombok.NonNull;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.WorldCreator;
import org.bukkit.block.data.BlockData;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.generator.BiomeProvider;
import org.bukkit.generator.ChunkGenerator;
import org.bukkit.plugin.IllegalPluginAccessException;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class Iris extends VolmitPlugin implements Listener {
   private static final Queue<Runnable> syncJobs = new ShurikenQueue();
   public static Iris instance;
   public static Bindings.Adventure audiences;
   public static MultiverseCoreLink linkMultiverseCore;
   public static IrisCompat compat;
   public static FileWatcher configWatcher;
   public static ChunkTickets tickets;
   private static VolmitSender sender;
   private static Thread shutdownHook;
   private final KList<Runnable> postShutdown = new KList();
   private KMap<Class<? extends IrisService>, IrisService> services;

   public static VolmitSender getSender() {
      if (sender == null) {
         sender = new VolmitSender(Bukkit.getConsoleSender());
         sender.setTag(instance.getTag());
      }

      return sender;
   }

   public static <T> T service(Class<T> v0) {
      return instance.services.get(v0);
   }

   public static void callEvent(Event v0) {
      if (!v0.isAsynchronous()) {
         J.s(() -> {
            Bukkit.getPluginManager().callEvent(v0);
         });
      } else {
         Bukkit.getPluginManager().callEvent(v0);
      }

   }

   public static KList<Object> initialize(String v0, Class<? extends Annotation> v1) {
      JarScanner v2 = new JarScanner(instance.getJarFile(), v0);
      KList v3 = new KList();
      Objects.requireNonNull(v2);
      J.attempt(v2::scan);
      Iterator v4 = v2.getClasses().iterator();

      while(true) {
         Class v5;
         do {
            if (!v4.hasNext()) {
               return v3;
            }

            v5 = (Class)v4.next();
         } while(v1 != null && !v5.isAnnotationPresent(v1));

         try {
            v3.add((Object)v5.getDeclaredConstructor().newInstance());
         } catch (Throwable var7) {
         }
      }
   }

   public static KList<Class<?>> getClasses(String v0, Class<? extends Annotation> v1) {
      JarScanner v2 = new JarScanner(instance.getJarFile(), v0);
      KList v3 = new KList();
      Objects.requireNonNull(v2);
      J.attempt(v2::scan);
      Iterator v4 = v2.getClasses().iterator();

      while(true) {
         Class v5;
         do {
            if (!v4.hasNext()) {
               return v3;
            }

            v5 = (Class)v4.next();
         } while(v1 != null && !v5.isAnnotationPresent(v1));

         try {
            v3.add((Object)v5);
         } catch (Throwable var7) {
         }
      }
   }

   public static KList<Object> initialize(String v0) {
      return initialize(v0, (Class)null);
   }

   public static void sq(Runnable v0) {
      synchronized(syncJobs) {
         syncJobs.queue((Object)v0);
      }
   }

   public static File getTemp() {
      return instance.getDataFolder(new String[]{"cache", "temp"});
   }

   public static void msg(String v0) {
      try {
         getSender().sendMessage(v0);
      } catch (Throwable var4) {
         try {
            Logger var10000 = instance.getLogger();
            String var10001 = instance.getTag();
            var10000.info(var10001 + v0.replaceAll("(<([^>]+)>)", ""));
         } catch (Throwable var3) {
         }
      }

   }

   public static File getCached(String v0, String v1) {
      String v2 = IO.hash(v0 + "@" + v1);
      File v3 = instance.getDataFile(new String[]{"cache", v2.substring(0, 2), v2.substring(3, 5), v2});
      if (!v3.exists()) {
         try {
            BufferedInputStream v4 = new BufferedInputStream((new URL(v1)).openStream());

            try {
               FileOutputStream v5 = new FileOutputStream(v3);

               try {
                  byte[] v6 = new byte[1024];

                  int i7;
                  while((i7 = v4.read(v6, 0, 1024)) != -1) {
                     v5.write(v6, 0, i7);
                     verbose("Aquiring " + v0);
                  }
               } catch (Throwable var11) {
                  try {
                     v5.close();
                  } catch (Throwable var10) {
                     var11.addSuppressed(var10);
                  }

                  throw var11;
               }

               v5.close();
            } catch (Throwable var12) {
               try {
                  v4.close();
               } catch (Throwable var9) {
                  var12.addSuppressed(var9);
               }

               throw var12;
            }

            v4.close();
         } catch (IOException var13) {
            reportError(var13);
         }
      }

      return v3.exists() ? v3 : null;
   }

   public static String getNonCached(String v0, String v1) {
      String v2 = IO.hash(v0 + "*" + v1);
      File v3 = instance.getDataFile(new String[]{"cache", v2.substring(0, 2), v2.substring(3, 5), v2});

      try {
         BufferedInputStream v4 = new BufferedInputStream((new URL(v1)).openStream());

         try {
            FileOutputStream v5 = new FileOutputStream(v3);

            try {
               byte[] v6 = new byte[1024];

               int i7;
               while((i7 = v4.read(v6, 0, 1024)) != -1) {
                  v5.write(v6, 0, i7);
               }
            } catch (Throwable var12) {
               try {
                  v5.close();
               } catch (Throwable var11) {
                  var12.addSuppressed(var11);
               }

               throw var12;
            }

            v5.close();
         } catch (Throwable var13) {
            try {
               v4.close();
            } catch (Throwable var10) {
               var13.addSuppressed(var10);
            }

            throw var13;
         }

         v4.close();
      } catch (IOException var14) {
         reportError(var14);
      }

      try {
         return IO.readAll(v3);
      } catch (IOException var9) {
         reportError(var9);
         return "";
      }
   }

   public static File getNonCachedFile(String v0, String v1) {
      String v2 = IO.hash(v0 + "*" + v1);
      File v3 = instance.getDataFile(new String[]{"cache", v2.substring(0, 2), v2.substring(3, 5), v2});
      verbose("Download " + v0 + " -> " + v1);

      try {
         BufferedInputStream v4 = new BufferedInputStream((new URL(v1)).openStream());

         try {
            FileOutputStream v5 = new FileOutputStream(v3);

            try {
               byte[] v6 = new byte[1024];

               while(true) {
                  int i7;
                  if ((i7 = v4.read(v6, 0, 1024)) == -1) {
                     v5.flush();
                     break;
                  }

                  v5.write(v6, 0, i7);
               }
            } catch (Throwable var11) {
               try {
                  v5.close();
               } catch (Throwable var10) {
                  var11.addSuppressed(var10);
               }

               throw var11;
            }

            v5.close();
         } catch (Throwable var12) {
            try {
               v4.close();
            } catch (Throwable var9) {
               var12.addSuppressed(var9);
            }

            throw var12;
         }

         v4.close();
      } catch (IOException var13) {
         var13.printStackTrace();
         reportError(var13);
      }

      return v3;
   }

   public static void warn(String v0, Object... v1) {
      String var10000 = String.valueOf(C.YELLOW);
      msg(var10000 + String.format(v0, v1));
   }

   public static void error(String v0, Object... v1) {
      String var10000 = String.valueOf(C.RED);
      msg(var10000 + String.format(v0, v1));
   }

   public static void debug(String v0) {
      if (IrisSettings.get().getGeneral().isDebug()) {
         try {
            throw new RuntimeException();
         } catch (Throwable var4) {
            Throwable v1 = var4;

            try {
               Object v2 = v1.getStackTrace()[1].getClassName().split("\\Q.\\E");
               if (v2.length > 5) {
                  debug(v2[3] + "/" + v2[4] + "/" + v2[v2.length - 1], v1.getStackTrace()[1].getLineNumber(), v0);
               } else {
                  debug(v2[3] + "/" + v2[4], v1.getStackTrace()[1].getLineNumber(), v0);
               }
            } catch (Throwable var3) {
               debug("Origin", -1, v0);
            }

         }
      }
   }

   public static void debug(String v0, int i1, String v2) {
      if (IrisSettings.get().getGeneral().isDebug()) {
         if (IrisSettings.get().getGeneral().isUseConsoleCustomColors()) {
            msg("<gradient:#095fe0:#a848db>" + v0 + " <#bf3b76>" + i1 + "<reset> " + String.valueOf(C.LIGHT_PURPLE) + v2.replaceAll("\\Q<\\E", "[").replaceAll("\\Q>\\E", "]"));
         } else {
            String var10000 = String.valueOf(C.BLUE);
            msg(var10000 + v0 + ":" + String.valueOf(C.AQUA) + i1 + String.valueOf(C.RESET) + String.valueOf(C.LIGHT_PURPLE) + " " + v2.replaceAll("\\Q<\\E", "[").replaceAll("\\Q>\\E", "]"));
         }

      }
   }

   public static void verbose(String v0) {
      debug(v0);
   }

   public static void success(String v0) {
      String var10000 = String.valueOf(C.IRIS);
      msg(var10000 + v0);
   }

   public static void info(String v0, Object... v1) {
      String var10000 = String.valueOf(C.WHITE);
      msg(var10000 + String.format(v0, v1));
   }

   public static void later(NastyRunnable v0) {
      try {
         Bukkit.getScheduler().scheduleAsyncDelayedTask(instance, () -> {
            try {
               v0.run();
            } catch (Throwable var2) {
               var2.printStackTrace();
               reportError(var2);
            }

         }, (long)RNG.r.i(100, 1200));
      } catch (IllegalPluginAccessException var2) {
      }

   }

   public static int jobCount() {
      return syncJobs.size();
   }

   public static void clearQueues() {
      synchronized(syncJobs) {
         syncJobs.clear();
      }
   }

   public static int getJavaVersion() {
      String v0 = System.getProperty("java.version");
      if (v0.startsWith("1.")) {
         v0 = v0.substring(2, 3);
      } else {
         int i1 = v0.indexOf(".");
         if (i1 != -1) {
            v0 = v0.substring(0, i1);
         }
      }

      return Integer.parseInt(v0);
   }

   public static String getJava() {
      String v0 = System.getProperty("java.vm.name");
      String v1 = System.getProperty("java.vendor");
      String v2 = System.getProperty("java.vm.version");
      return String.format("%s %s (build %s)", v0, v1, v2);
   }

   public static void reportErrorChunk(int i0, int i1, Throwable v2, String v3) {
      if (IrisSettings.get().getGeneral().isDebug()) {
         File v4 = instance.getDataFile(new String[]{"debug", "chunk-errors", "chunk." + i0 + "." + i1 + ".txt"});
         if (!v4.exists()) {
            J.attempt(() -> {
               PrintWriter v2x = new PrintWriter(v4);
               v2x.println("Thread: " + Thread.currentThread().getName());
               Date var10001 = new Date(M.ms());
               v2x.println("First: " + String.valueOf(var10001));
               v2.printStackTrace(v2x);
               v2x.close();
            });
         }

         debug("Chunk " + i0 + "," + i1 + " Exception Logged: " + v2.getClass().getSimpleName() + ": " + String.valueOf(C.RESET) + String.valueOf(C.LIGHT_PURPLE) + v2.getMessage());
      }

   }

   public static void reportError(Throwable v0) {
      Bindings.capture(v0);
      if (IrisSettings.get().getGeneral().isDebug()) {
         String var10000 = v0.getClass().getCanonicalName();
         String v1 = var10000 + "-" + v0.getStackTrace()[0].getClassName() + "-" + v0.getStackTrace()[0].getLineNumber();
         if (v0.getCause() != null) {
            v1 = v1 + "-" + v0.getCause().getStackTrace()[0].getClassName() + "-" + v0.getCause().getStackTrace()[0].getLineNumber();
         }

         File v2 = instance.getDataFile(new String[]{"debug", "caught-exceptions", v1 + ".txt"});
         if (!v2.exists()) {
            J.attempt(() -> {
               PrintWriter v2x = new PrintWriter(v2);
               v2x.println("Thread: " + Thread.currentThread().getName());
               Date var10001 = new Date(M.ms());
               v2x.println("First: " + String.valueOf(var10001));
               v0.printStackTrace(v2x);
               v2x.close();
            });
         }

         var10000 = v0.getClass().getSimpleName();
         debug("Exception Logged: " + var10000 + ": " + String.valueOf(C.RESET) + String.valueOf(C.LIGHT_PURPLE) + v0.getMessage());
      }

   }

   public static void dump() {
      try {
         Iris var10000 = instance;
         String[] var10001 = new String[]{"dump", null};
         java.sql.Date var10004 = new java.sql.Date(M.ms());
         var10001[1] = "td-" + String.valueOf(var10004) + ".txt";
         Serializable v0 = var10000.getDataFile(var10001);
         FileOutputStream v1 = new FileOutputStream(v0);
         Map v2 = Thread.getAllStackTraces();
         PrintWriter v3 = new PrintWriter(v1);
         Iterator v4 = v2.keySet().iterator();

         while(v4.hasNext()) {
            Thread v5 = (Thread)v4.next();
            v3.println("========================================");
            String var11 = v5.getName();
            v3.println("Thread: '" + var11 + "' ID: " + v5.getId() + " STATUS: " + v5.getState().name());
            StackTraceElement[] v6 = (StackTraceElement[])v2.get(v5);
            int i7 = v6.length;

            for(int i8 = 0; i8 < i7; ++i8) {
               StackTraceElement v9 = v6[i8];
               v3.println("    @ " + v9.toString());
            }

            v3.println("========================================");
            v3.println();
            v3.println();
         }

         v3.println("[81187,84586,%%__PRODUCT__%%,%%__BUILTBYBIT__%%]");
         v3.close();
         info("DUMPED! See " + v0.getAbsolutePath());
      } catch (Throwable var10) {
         var10.printStackTrace();
      }

   }

   public static void panic() {
      EnginePanic.panic();
   }

   public static void addPanic(String v0, String v1) {
      EnginePanic.add(v0, v1);
   }

   public Iris() {
      instance = this;
      SlimJar.load();
   }

   private void enable() {
      this.services = new KMap();
      this.setupAudience();
      Bindings.setupSentry();
      initialize("com.volmit.iris.core.service").forEach((v1) -> {
         this.services.put(v1.getClass(), (IrisService)v1);
      });
      IO.delete(new File("iris"));
      compat = IrisCompat.configured(this.getDataFile(new String[]{"compat.json"}));
      ServerConfigurator.configure();
      IrisSafeguard.execute();
      getSender().setTag(this.getTag());
      IrisSafeguard.splash();
      tickets = new ChunkTickets();
      linkMultiverseCore = new MultiverseCoreLink();
      configWatcher = new FileWatcher(this.getDataFile(new String[]{"settings.json"}));
      this.services.values().forEach(IrisService::onEnable);
      this.services.values().forEach(this::registerListener);
      this.addShutdownHook();
      J.s(() -> {
         J.a(() -> {
            IO.delete(getTemp());
         });
         J.a(LazyPregenerator::loadLazyGenerators, 100);
         J.a(this::bstats);
         J.ar(this::checkConfigHotload, 60);
         J.sr(this::tickQueue, 0);
         J.s(this::setupPapi);
         J.a(ServerConfigurator::configure, 20);
         this.autoStartStudio();
         this.checkForBukkitWorlds((v0) -> {
            return true;
         });
         IrisToolbelt.retainMantleDataForSlice(String.class.getCanonicalName());
         IrisToolbelt.retainMantleDataForSlice(BlockData.class.getCanonicalName());
      });
   }

   public void addShutdownHook() {
      if (shutdownHook != null) {
         Runtime.getRuntime().removeShutdownHook(shutdownHook);
      }

      shutdownHook = new Thread(() -> {
         Bukkit.getWorlds().stream().map(IrisToolbelt::access).filter(Objects::nonNull).forEach(PlatformChunkGenerator::close);
         MultiBurst.burst.close();
         MultiBurst.ioBurst.close();
         this.services.clear();
      });
      Runtime.getRuntime().addShutdownHook(shutdownHook);
   }

   public void checkForBukkitWorlds(Predicate<String> this) {
      try {
         IrisWorlds.readBukkitWorlds().forEach((v2, v3) -> {
            try {
               if (Bukkit.getWorld(v2) != null || !v1.test(v2)) {
                  return;
               }

               info("Loading World: %s | Generator: %s", v2, v3);
               Object v4 = this.getDefaultWorldGenerator(v2, v3);
               IrisDimension v5 = loadDimension(v2, v3);

               assert v5 != null && v4 != null;

               info(String.valueOf(C.LIGHT_PURPLE) + "Preparing Spawn for " + v2 + "' using Iris:" + v3 + "...");
               WorldCreator v6 = (new WorldCreator(v2)).generator(v4).environment(v5.getEnvironment());
               INMS.get().createWorld(v6);
               info(String.valueOf(C.LIGHT_PURPLE) + "Loaded " + v2 + "!");
            } catch (Throwable var7) {
               error("Failed to load world " + v2 + "!");
               var7.printStackTrace();
            }

         });
      } catch (Throwable var3) {
         var3.printStackTrace();
         reportError(var3);
      }

   }

   private void autoStartStudio() {
      if (IrisSettings.get().getStudio().isAutoStartDefaultStudio()) {
         info("Starting up auto Studio!");

         try {
            Object v1 = (Player)(new KList(this.getServer().getOnlinePlayers())).getRandom();
            ((StudioSVC)service(StudioSVC.class)).open(v1 != null ? new VolmitSender(v1) : getSender(), 1337L, IrisSettings.get().getGenerator().getDefaultWorldType(), (v1x) -> {
               J.s(() -> {
                  Location v2 = v1x.getSpawnLocation();
                  Iterator v3 = this.getServer().getOnlinePlayers().iterator();

                  while(v3.hasNext()) {
                     Player v4 = (Player)v3.next();
                     v4.setGameMode(GameMode.SPECTATOR);
                     v4.teleport(v2);
                  }

               });
            });
         } catch (IrisException var2) {
            reportError(var2);
         }
      }

   }

   private void setupAudience() {
      try {
         audiences = new Bindings.Adventure(this);
      } catch (Throwable var2) {
         var2.printStackTrace();
         IrisSettings.get().getGeneral().setUseConsoleCustomColors(false);
         IrisSettings.get().getGeneral().setUseCustomColorsIngame(false);
         error("Failed to setup Adventure API... No custom colors :(");
      }

   }

   public void postShutdown(Runnable this) {
      this.postShutdown.add((Object)v1);
   }

   public void onEnable() {
      this.enable();
      super.onEnable();
      Bukkit.getPluginManager().registerEvents(this, this);
   }

   public void onDisable() {
      if (!IrisSafeguard.isForceShutdown()) {
         this.services.values().forEach(IrisService::onDisable);
         Bukkit.getScheduler().cancelTasks(this);
         HandlerList.unregisterAll(this);
         this.postShutdown.forEach(Runnable::run);
         super.onDisable();
         JarScanner var10000 = new JarScanner(instance.getJarFile(), "", false);
         J.attempt(var10000::scanAll);
      }
   }

   private void setupPapi() {
      if (Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null) {
         (new IrisPapiExpansion()).register();
      }

   }

   public void start() {
   }

   public void stop() {
   }

   public String getTag(String this) {
      return IrisSafeguard.mode().tag(v1);
   }

   private void checkConfigHotload() {
      if (configWatcher.checkModified()) {
         IrisSettings.invalidate();
         IrisSettings.get();
         configWatcher.checkModified();
         info("Hotloaded settings.json ");
      }

   }

   private void tickQueue() {
      synchronized(syncJobs) {
         if (syncJobs.hasNext()) {
            long j2 = M.ms();

            while(syncJobs.hasNext() && M.ms() - j2 < 25L) {
               try {
                  ((Runnable)syncJobs.next()).run();
               } catch (Throwable var6) {
                  var6.printStackTrace();
                  reportError(var6);
               }
            }

         }
      }
   }

   private void bstats() {
      if (IrisSettings.get().getGeneral().isPluginMetrics()) {
         Bindings.setupBstats(this);
      }

   }

   public boolean onCommand(CommandSender this, Command v1, String v2, String[] v3) {
      return super.onCommand(v1, v2, v3, v4);
   }

   public void imsg(CommandSender this, String v1) {
      String var10001 = String.valueOf(C.IRIS);
      v1.sendMessage(var10001 + "[" + String.valueOf(C.DARK_GRAY) + "Iris" + String.valueOf(C.IRIS) + "]" + String.valueOf(C.GRAY) + ": " + v2);
   }

   @Nullable
   public BiomeProvider getDefaultBiomeProvider(@NotNull String this, @Nullable String v1) {
      debug("Biome Provider Called for " + v1 + " using ID: " + v2);
      return super.getDefaultBiomeProvider(v1, v2);
   }

   public ChunkGenerator getDefaultWorldGenerator(String this, String v1) {
      debug("Default World Generator Called for " + v1 + " using ID: " + v2);
      if (v2 == null || v2.isEmpty()) {
         v2 = IrisSettings.get().getGenerator().getDefaultWorldType();
      }

      debug("Generator ID: " + v2 + " requested by bukkit/plugin");
      IrisDimension v3 = loadDimension(v1, v2);
      if (v3 == null) {
         throw new RuntimeException("Can't find dimension " + v2 + "!");
      } else {
         debug("Assuming IrisDimension: " + v3.getName());
         IrisWorld v4 = IrisWorld.builder().name(v1).seed(1337L).environment(v3.getEnvironment()).worldFolder(new File(Bukkit.getWorldContainer(), v1)).minHeight(v3.getMinHeight()).maxHeight(v3.getMaxHeight()).build();
         debug("Generator Config: " + v4.toString());
         File v5 = new File(v4.worldFolder(), "iris/pack");
         File[] v6 = v5.listFiles();
         if (v6 == null || v6.length == 0) {
            IO.delete(v5);
         }

         if (!v5.exists()) {
            v5.mkdirs();
            ((StudioSVC)service(StudioSVC.class)).installIntoWorld(getSender(), v3.getLoadKey(), v4.worldFolder());
         }

         return new BukkitChunkGenerator(v4, false, v5, v3.getLoadKey());
      }
   }

   @Nullable
   public static IrisDimension loadDimension(@NonNull String v0, @NonNull String v1) {
      if (v0 == null) {
         throw new NullPointerException("worldName is marked non-null but is null");
      } else if (v1 == null) {
         throw new NullPointerException("id is marked non-null but is null");
      } else {
         File v2 = new File(Bukkit.getWorldContainer(), String.join(File.separator, v0, "iris", "pack"));
         IrisDimension v3 = v2.isDirectory() ? (IrisDimension)IrisData.get(v2).getDimensionLoader().load(v1) : null;
         if (v3 == null) {
            v3 = IrisData.loadAnyDimension(v1, (IrisData)null);
         }

         if (v3 == null) {
            warn("Unable to find dimension type " + v1 + " Looking for online packs...");
            ((StudioSVC)service(StudioSVC.class)).downloadSearch(new VolmitSender(Bukkit.getConsoleSender()), v1, false);
            v3 = IrisData.loadAnyDimension(v1, (IrisData)null);
            if (v3 != null) {
               info("Resolved missing dimension, proceeding.");
            }
         }

         return v3;
      }
   }

   public void splash() {
      info("Server type & version: " + Bukkit.getName() + " v" + Bukkit.getVersion());
      info("Custom Biomes: " + INMS.get().countCustomBiomes());
      this.printPacks();
      IrisSafeguard.mode().trySplash();
   }

   private void printPacks() {
      File v1 = ((StudioSVC)service(StudioSVC.class)).getWorkspaceFolder();
      File[] v2 = v1.listFiles(File::isDirectory);
      if (v2 != null && v2.length != 0) {
         info("Custom Dimensions: " + v2.length);
         File[] v3 = v2;
         int i4 = v2.length;

         for(int i5 = 0; i5 < i4; ++i5) {
            File v6 = v3[i5];
            this.printPack(v6);
         }

      }
   }

   private void printPack(File this) {
      String v2 = v1.getName();
      String v3 = "???";

      try {
         FileReader v4 = new FileReader(new File(v1, "dimensions/" + v2 + ".json"));

         try {
            Object v5 = JsonParser.parseReader(v4).getAsJsonObject();
            if (v5.has("version")) {
               v3 = v5.get("version").getAsString();
            }
         } catch (Throwable var8) {
            try {
               v4.close();
            } catch (Throwable var7) {
               var8.addSuppressed(var7);
            }

            throw var8;
         }

         v4.close();
      } catch (JsonParseException | IOException var9) {
      }

      info("  " + v2 + " v" + v3);
   }

   public int getIrisVersion() {
      String v1 = instance.getDescription().getVersion();
      int i2 = v1.indexOf(45);
      if (i2 != -1) {
         String v3 = v1.substring(0, i2);
         v3 = v3.replaceAll("\\.", "");
         return Integer.parseInt(v3);
      } else {
         return -1;
      }
   }

   public int getMCVersion() {
      try {
         Serializable v1 = Bukkit.getVersion();
         Matcher v2 = Pattern.compile("\\(MC: ([\\d.]+)\\)").matcher(v1);
         if (v2.find()) {
            v1 = v2.group(1).replaceAll("\\.", "");
            long j3 = Long.parseLong(v1);
            return j3 > 2147483647L ? -1 : (int)j3;
         } else {
            return -1;
         }
      } catch (Exception var5) {
         return -1;
      }
   }

   static {
      try {
         InstanceState.updateInstanceId();
      } catch (Throwable var1) {
      }

   }
}
