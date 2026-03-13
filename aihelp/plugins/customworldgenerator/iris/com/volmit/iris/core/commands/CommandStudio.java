package com.volmit.iris.core.commands;

import com.volmit.iris.Iris;
import com.volmit.iris.core.IrisSettings;
import com.volmit.iris.core.gui.NoiseExplorerGUI;
import com.volmit.iris.core.gui.VisionGUI;
import com.volmit.iris.core.loader.IrisData;
import com.volmit.iris.core.loader.ResourceLoader;
import com.volmit.iris.core.project.IrisProject;
import com.volmit.iris.core.service.ConversionSVC;
import com.volmit.iris.core.service.StudioSVC;
import com.volmit.iris.core.tools.IrisToolbelt;
import com.volmit.iris.engine.framework.Engine;
import com.volmit.iris.engine.object.InventorySlotType;
import com.volmit.iris.engine.object.IrisBiome;
import com.volmit.iris.engine.object.IrisBiomePaletteLayer;
import com.volmit.iris.engine.object.IrisDimension;
import com.volmit.iris.engine.object.IrisEntity;
import com.volmit.iris.engine.object.IrisGenerator;
import com.volmit.iris.engine.object.IrisInterpolator;
import com.volmit.iris.engine.object.IrisJigsawStructure;
import com.volmit.iris.engine.object.IrisNoiseGenerator;
import com.volmit.iris.engine.object.IrisObject;
import com.volmit.iris.engine.object.IrisObjectPlacement;
import com.volmit.iris.engine.object.IrisRegion;
import com.volmit.iris.engine.object.IrisScript;
import com.volmit.iris.engine.object.NoiseStyle;
import com.volmit.iris.engine.platform.PlatformChunkGenerator;
import com.volmit.iris.util.collection.KList;
import com.volmit.iris.util.collection.KMap;
import com.volmit.iris.util.collection.KSet;
import com.volmit.iris.util.decree.DecreeContext;
import com.volmit.iris.util.decree.DecreeExecutor;
import com.volmit.iris.util.decree.DecreeOrigin;
import com.volmit.iris.util.decree.annotations.Decree;
import com.volmit.iris.util.decree.annotations.Param;
import com.volmit.iris.util.format.C;
import com.volmit.iris.util.format.Form;
import com.volmit.iris.util.function.NoiseProvider;
import com.volmit.iris.util.interpolation.InterpolationMethod;
import com.volmit.iris.util.io.IO;
import com.volmit.iris.util.json.JSONArray;
import com.volmit.iris.util.json.JSONObject;
import com.volmit.iris.util.mantle.Mantle;
import com.volmit.iris.util.math.M;
import com.volmit.iris.util.math.Position2;
import com.volmit.iris.util.math.RNG;
import com.volmit.iris.util.math.Spiraler;
import com.volmit.iris.util.noise.CNG;
import com.volmit.iris.util.paper.PaperLib;
import com.volmit.iris.util.parallel.BurstExecutor;
import com.volmit.iris.util.parallel.MultiBurst;
import com.volmit.iris.util.parallel.SyncExecutor;
import com.volmit.iris.util.plugin.VolmitSender;
import com.volmit.iris.util.scheduling.J;
import com.volmit.iris.util.scheduling.O;
import com.volmit.iris.util.scheduling.PrecisionStopwatch;
import com.volmit.iris.util.scheduling.jobs.ParallelRadiusJob;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.attribute.FileTime;
import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.Date;
import java.util.Iterator;
import java.util.Objects;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Supplier;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.FluidCollisionMode;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.util.BlockVector;
import org.bukkit.util.Vector;

@Decree(
   name = "studio",
   aliases = {"std", "s"},
   description = "Studio Commands",
   studio = true
)
public class CommandStudio implements DecreeExecutor {
   private CommandFind find;
   private CommandEdit edit;

   public static String hrf(Duration duration) {
      return var0.toString().substring(2).replaceAll("(\\d[HMS])(?!$)", "$1 ").toLowerCase();
   }

   @Decree(
      description = "Download a project.",
      aliases = {"dl"}
   )
   public void download(@Param(name = "pack",description = "The pack to download",defaultValue = "overworld",aliases = {"project"}) String pack, @Param(name = "branch",description = "The branch to download from",defaultValue = "master") String branch, @Param(name = "overwrite",description = "Whether or not to overwrite the pack with the downloaded one",aliases = {"force"},defaultValue = "false") boolean overwrite) {
      (new CommandIris()).download(var1, var2, var3);
   }

   @Decree(
      description = "Open a new studio world",
      aliases = {"o"},
      sync = true
   )
   public void open(@Param(defaultValue = "default",description = "The dimension to open a studio for",aliases = {"dim"}) IrisDimension dimension, @Param(defaultValue = "1337",description = "The seed to generate the studio with",aliases = {"s"}) long seed) {
      VolmitSender var10000 = this.sender();
      String var10001 = String.valueOf(C.GREEN);
      var10000.sendMessage(var10001 + "Opening studio for the \"" + var1.getName() + "\" pack (seed: " + var2 + ")");
      ((StudioSVC)Iris.service(StudioSVC.class)).open(this.sender(), var2, var1.getLoadKey());
   }

   @Decree(
      description = "Open VSCode for a dimension",
      aliases = {"vsc", "edit"}
   )
   public void vscode(@Param(defaultValue = "default",description = "The dimension to open VSCode for",aliases = {"dim"}) IrisDimension dimension) {
      VolmitSender var10000 = this.sender();
      String var10001 = String.valueOf(C.GREEN);
      var10000.sendMessage(var10001 + "Opening VSCode for the \"" + var1.getName() + "\" pack");
      ((StudioSVC)Iris.service(StudioSVC.class)).openVSCode(this.sender(), var1.getLoadKey());
   }

   @Decree(
      description = "Close an open studio project",
      aliases = {"x", "c"},
      sync = true
   )
   public void close() {
      if (!((StudioSVC)Iris.service(StudioSVC.class)).isProjectOpen()) {
         this.sender().sendMessage(String.valueOf(C.RED) + "No open studio projects.");
      } else {
         ((StudioSVC)Iris.service(StudioSVC.class)).close();
         this.sender().sendMessage(String.valueOf(C.GREEN) + "Project Closed.");
      }
   }

   @Decree(
      description = "Create a new studio project",
      aliases = {"+"},
      sync = true
   )
   public void create(@Param(description = "The name of this new Iris Project.") String name, @Param(description = "Copy the contents of an existing project in your packs folder and use it as a template in this new project.",contextual = true) IrisDimension template) {
      if (var2 != null) {
         ((StudioSVC)Iris.service(StudioSVC.class)).create(this.sender(), var1, var2.getLoadKey());
      } else {
         ((StudioSVC)Iris.service(StudioSVC.class)).create(this.sender(), var1);
      }

   }

   @Decree(
      description = "Get the version of a pack"
   )
   public void version(@Param(defaultValue = "default",description = "The dimension get the version of",aliases = {"dim"},contextual = true) IrisDimension dimension) {
      VolmitSender var10000 = this.sender();
      String var10001 = String.valueOf(C.GREEN);
      var10000.sendMessage(var10001 + "The \"" + var1.getName() + "\" pack has version: " + var1.getVersion());
   }

   @Decree(
      name = "regen",
      description = "Regenerate nearby chunks.",
      aliases = {"rg"},
      sync = true,
      origin = DecreeOrigin.PLAYER
   )
   public void regen(@Param(name = "radius",description = "The radius of nearby cunks",defaultValue = "5") int radius) {
      World var2 = this.player().getWorld();
      if (!IrisToolbelt.isIrisWorld(var2)) {
         this.sender().sendMessage(String.valueOf(C.RED) + "You must be in an Iris World to use regen!");
      }

      VolmitSender var3 = this.sender();
      Location var4 = this.player().getLocation().clone();
      J.a(() -> {
         final PlatformChunkGenerator var5 = IrisToolbelt.access(var2);
         Engine var6 = var5.getEngine();
         DecreeContext.touch(var3);

         try {
            final SyncExecutor var7 = new SyncExecutor(20);

            try {
               ExecutorService var8 = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());

               try {
                  final int var9 = var4.getBlockX() >> 4;
                  final int var10 = var4.getBlockZ() >> 4;
                  int var11 = var6.getMantle().getRadius();
                  final Mantle var12 = var6.getMantle().getMantle();
                  final KMap var13 = new KMap();
                  ParallelRadiusJob var14 = (new ParallelRadiusJob(this, Integer.MAX_VALUE, var8) {
                     protected void execute(int rX, int rZ) {
                        if (Math.abs(var1) <= var4 && Math.abs(var2) <= var4) {
                           var12.deleteChunk(var1 + var9, var2 + var10);
                        } else {
                           var1 += var9;
                           var2 += var10;
                           var13.put(new Position2(var1, var2), var12.getChunk(var1, var2));
                           var12.deleteChunk(var1, var2);
                        }
                     }

                     public String getName() {
                        return "Preparing Mantle";
                     }
                  }).retarget(var1 + var11, 0, 0);
                  CountDownLatch var15 = new CountDownLatch(1);
                  VolmitSender var10001 = this.sender();
                  Objects.requireNonNull(var15);
                  var14.execute(var10001, var15::countDown);
                  var15.await();
                  ParallelRadiusJob var16 = (new ParallelRadiusJob(this, Integer.MAX_VALUE, var8) {
                     protected void execute(int x, int z) {
                        var5.injectChunkReplacement(var1, var1x, var2, var7);
                     }

                     public String getName() {
                        return "Regenerating";
                     }
                  }).retarget(var1, var9, var10);
                  CountDownLatch var17 = new CountDownLatch(1);
                  var10001 = this.sender();
                  Objects.requireNonNull(var17);
                  var16.execute(var10001, var17::countDown);
                  var17.await();
                  var13.forEach((var1x, var2x) -> {
                     var12.getChunk(var1x.getX(), var1x.getZ()).copyFrom(var2x);
                  });
               } catch (Throwable var27) {
                  if (var8 != null) {
                     try {
                        var8.close();
                     } catch (Throwable var26) {
                        var27.addSuppressed(var26);
                     }
                  }

                  throw var27;
               }

               if (var8 != null) {
                  var8.close();
               }
            } catch (Throwable var28) {
               try {
                  var7.close();
               } catch (Throwable var25) {
                  var28.addSuppressed(var25);
               }

               throw var28;
            }

            var7.close();
         } catch (Throwable var29) {
            this.sender().sendMessage("Error while regenerating chunks");
            var29.printStackTrace();
         } finally {
            DecreeContext.remove();
         }

      });
   }

   @Decree(
      description = "Convert objects in the \"convert\" folder"
   )
   public void convert() {
      ((ConversionSVC)Iris.service(ConversionSVC.class)).check(this.sender());
   }

   @Decree(
      description = "Execute a script",
      aliases = {"run"},
      origin = DecreeOrigin.PLAYER
   )
   public void execute(@Param(description = "The script to run") IrisScript script) {
      this.engine().getExecution().execute(var1.getLoadKey());
   }

   @Decree(
      description = "Open the noise explorer (External GUI)",
      aliases = {"nmap", "n"}
   )
   public void noise() {
      if (!this.noGUI()) {
         this.sender().sendMessage(String.valueOf(C.GREEN) + "Opening Noise Explorer!");
         NoiseExplorerGUI.launch();
      }
   }

   @Decree(
      description = "Charges all spawners in the area",
      aliases = {"zzt"},
      origin = DecreeOrigin.PLAYER
   )
   public void charge() {
      if (!IrisToolbelt.isIrisWorld(this.world())) {
         this.sender().sendMessage(String.valueOf(C.RED) + "You must be in an Iris world to charge spawners!");
      } else {
         this.sender().sendMessage(String.valueOf(C.GREEN) + "Charging spawners!");
         this.engine().getWorldManager().chargeEnergy();
      }
   }

   @Decree(
      description = "Preview noise gens (External GUI)",
      aliases = {"generator", "gen"}
   )
   public void explore(@Param(description = "The generator to explore",contextual = true) IrisGenerator generator, @Param(description = "The seed to generate with",defaultValue = "12345") long seed) {
      if (!this.noGUI()) {
         this.sender().sendMessage(String.valueOf(C.GREEN) + "Opening Noise Explorer!");
         Supplier var4 = () -> {
            return var1 == null ? (var0, var1x) -> {
               return 0.0D;
            } : (var3, var4) -> {
               return var1.getHeight(var3, var4, (new RNG(var2)).nextParallelRNG(3245).lmax());
            };
         };
         NoiseExplorerGUI.launch(var4, "Custom Generator");
      }
   }

   @Decree(
      description = "Hotload a studio",
      aliases = {"reload", "h"}
   )
   public void hotload() {
      if (!((StudioSVC)Iris.service(StudioSVC.class)).isProjectOpen()) {
         this.sender().sendMessage(String.valueOf(C.RED) + "No studio world open!");
      } else {
         ((StudioSVC)Iris.service(StudioSVC.class)).getActiveProject().getActiveProvider().getEngine().hotload();
         this.sender().sendMessage(String.valueOf(C.GREEN) + "Hotloaded");
      }
   }

   @Decree(
      description = "Show loot if a chest were right here",
      origin = DecreeOrigin.PLAYER,
      sync = true
   )
   public void loot(@Param(description = "Fast insertion of items in virtual inventory (may cause performance drop)",defaultValue = "false") boolean fast, @Param(description = "Whether or not to append to the inventory currently open (if false, clears opened inventory)",defaultValue = "true") boolean add) {
      if (!this.noStudio()) {
         KList var3 = this.engine().getLootTables(RNG.r, this.player().getLocation().getBlock());
         Inventory var4 = Bukkit.createInventory((InventoryHolder)null, 54);

         try {
            this.engine().addItems(true, var4, RNG.r, var3, InventorySlotType.STORAGE, this.player().getWorld(), this.player().getLocation().getBlockX(), this.player().getLocation().getBlockY(), this.player().getLocation().getBlockZ(), 1);
         } catch (Throwable var9) {
            Iris.reportError(var9);
            VolmitSender var10000 = this.sender();
            String var10001 = String.valueOf(C.RED);
            var10000.sendMessage(var10001 + "Cannot add items to virtual inventory because of: " + var9.getMessage());
            return;
         }

         O var5 = new O();
         var5.set(-1);
         VolmitSender var6 = this.sender();
         Player var7 = this.player();
         Engine var8 = this.engine();
         var5.set(Bukkit.getScheduler().scheduleSyncRepeatingTask(Iris.instance, () -> {
            if (!var7.getOpenInventory().getType().equals(InventoryType.CHEST)) {
               Bukkit.getScheduler().cancelTask((Integer)var5.get());
               var6.sendMessage(String.valueOf(C.GREEN) + "Opened inventory!");
            } else {
               if (!var2) {
                  var4.clear();
               }

               var8.addItems(true, var4, new RNG((long)RNG.r.imax()), var3, InventorySlotType.STORAGE, var7.getWorld(), var7.getLocation().getBlockX(), var7.getLocation().getBlockY(), var7.getLocation().getBlockZ(), 1);
            }
         }, 0L, var1 ? 5L : 35L));
         this.sender().sendMessage(String.valueOf(C.GREEN) + "Opening inventory now!");
         this.player().openInventory(var4);
      }
   }

   @Decree(
      description = "Calculate the chance for each region to generate",
      origin = DecreeOrigin.PLAYER
   )
   public void regions(@Param(description = "The radius in chunks",defaultValue = "500") int radius) {
      Engine var2 = this.engine();
      if (var2 == null) {
         this.sender().sendMessage(String.valueOf(C.RED) + "Only works in an Iris world!");
      } else {
         VolmitSender var3 = this.sender();
         Player var4 = this.player();
         Thread.ofVirtual().start(() -> {
            int var4x = var1 * 2;
            KMap var5 = new KMap();
            var2.getDimension().getRegions().forEach((var1x) -> {
               var5.put(var1x, new AtomicInteger(0));
            });
            MultiBurst var6 = new MultiBurst("Region Sampler");
            BurstExecutor var7 = var6.burst(var1 * var1);
            var3.sendMessage(String.valueOf(C.GRAY) + "Generating data...");
            Location var8 = var4.getLocation();
            int var9 = var4x * var4x;
            AtomicInteger var10 = new AtomicInteger(0);
            int var11 = J.ar(() -> {
               var3.sendProgress((double)var10.get() / (double)var9, "Finding regions");
            }, 0);
            (new Spiraler(var4x, var4x, (var4xx, var5x) -> {
               var7.queue(() -> {
                  IrisRegion var5xx = var2.getRegion((var4xx << 4) + 8, (var5x << 4) + 8);
                  ((AtomicInteger)var5.computeIfAbsent(var5xx.getLoadKey(), (var0) -> {
                     return new AtomicInteger(0);
                  })).incrementAndGet();
                  var10.incrementAndGet();
               });
            })).setOffset(var8.getBlockX(), var8.getBlockZ()).drain();
            var7.complete();
            var6.close();
            J.car(var11);
            var3.sendMessage(String.valueOf(C.GREEN) + "Done!");
            ResourceLoader var12 = var2.getData().getRegionLoader();
            var5.forEach((var3x, var4xx) -> {
               String var10001 = String.valueOf(C.GREEN);
               var3.sendMessage(var10001 + var3x + ": " + ((IrisRegion)var12.load(var3x)).getRarity() + " / " + Form.f((double)var4xx.get() / (double)var9 * 100.0D, 2) + "%");
            });
         });
      }
   }

   @Decree(
      description = "Get all structures in a radius of chunks",
      aliases = {"dist"},
      origin = DecreeOrigin.PLAYER
   )
   public void distances(@Param(description = "The radius in chunks") int radius) {
      Engine var2 = this.engine();
      if (var2 == null) {
         this.sender().sendMessage(String.valueOf(C.RED) + "Only works in an Iris world!");
      } else {
         VolmitSender var3 = this.sender();
         int var4 = var1 * 2;
         KMap var5 = new KMap();
         MultiBurst var6 = new MultiBurst("Distance Sampler");
         BurstExecutor var7 = var6.burst(var1 * var1);
         var3.sendMessage(String.valueOf(C.GRAY) + "Generating data...");
         Location var8 = this.player().getLocation();
         int var9 = var4 * var4;
         AtomicInteger var10 = new AtomicInteger(0);
         int var11 = J.ar(() -> {
            var3.sendProgress((double)var10.get() / (double)var9, "Finding structures");
         }, 0);
         (new Spiraler(var4, var4, (var4x, var5x) -> {
            var7.queue(() -> {
               IrisJigsawStructure var5xx = var2.getStructureAt(var4x, var5x);
               if (var5xx != null) {
                  ((KList)var5.computeIfAbsent(var5xx.getLoadKey(), (var0) -> {
                     return new KList();
                  })).add((Object)(new Position2(var4x, var5x)));
               }

               var10.incrementAndGet();
            });
         })).setOffset(var8.getBlockX(), var8.getBlockZ()).drain();
         var7.complete();
         var6.close();
         J.car(var11);
         Iterator var12 = var5.keySet().iterator();

         while(var12.hasNext()) {
            String var13 = (String)var12.next();
            KList var14 = (KList)var5.get(var13);
            KList var15 = new KList(var14.size() - 1);

            for(int var16 = 0; var16 < var14.size(); ++var16) {
               Position2 var17 = (Position2)var14.get(var16);
               double var18 = 2.147483647E9D;
               Iterator var20 = var14.iterator();

               while(var20.hasNext()) {
                  Position2 var21 = (Position2)var20.next();
                  if (!var21.equals(var17)) {
                     var18 = Math.min(var18, Math.sqrt(Math.pow((double)(var17.getX() - var21.getX()), 2.0D) + Math.pow((double)(var17.getZ() - var21.getZ()), 2.0D)));
                  }
               }

               if (var18 != 2.147483647E9D) {
                  var15.add((Object)Math.round(var18 * 16.0D));
               }
            }

            long[] var27 = new long[var15.size()];

            for(int var28 = 0; var28 < var15.size(); ++var28) {
               var27[var28] = (Long)var15.get(var28);
            }

            Arrays.sort(var27);
            long var29 = var27.length > 0 ? var27[0] : 0L;
            long var19 = var27.length > 0 ? var27[var27.length - 1] : 0L;
            long var26 = Arrays.stream(var27).sum();
            long var23 = var27.length > 0 ? Math.round((double)var26 / (double)var27.length) : 0L;
            String var25 = "%s: %s => min: %s/max: %s -> avg: %s".formatted(new Object[]{var13, var14.size(), var29, var19, var23});
            var3.sendMessage(var25);
         }

         if (var5.isEmpty()) {
            var3.sendMessage(String.valueOf(C.RED) + "No data found!");
         } else {
            var3.sendMessage(String.valueOf(C.GREEN) + "Done!");
         }

      }
   }

   @Decree(
      description = "Render a world map (External GUI)",
      aliases = {"render"}
   )
   public void map(@Param(name = "world",description = "The world to open the generator for",contextual = true) World world) {
      if (!this.noGUI()) {
         if (!IrisToolbelt.isIrisWorld(var1)) {
            this.sender().sendMessage(String.valueOf(C.RED) + "You need to be in or specify an Iris-generated world!");
         } else {
            VisionGUI.launch(IrisToolbelt.access(var1).getEngine(), 0);
            this.sender().sendMessage(String.valueOf(C.GREEN) + "Opening map!");
         }
      }
   }

   @Decree(
      description = "Package a dimension into a compressed format",
      aliases = {"package"}
   )
   public void pkg(@Param(name = "dimension",description = "The dimension pack to compress",contextual = true,defaultValue = "default") IrisDimension dimension, @Param(name = "obfuscate",description = "Whether or not to obfuscate the pack",defaultValue = "false") boolean obfuscate, @Param(name = "minify",description = "Whether or not to minify the pack",defaultValue = "true") boolean minify) {
      ((StudioSVC)Iris.service(StudioSVC.class)).compilePackage(this.sender(), var1.getLoadKey(), var2, var3);
   }

   @Decree(
      description = "Profiles the performance of a dimension",
      origin = DecreeOrigin.PLAYER
   )
   public void profile(@Param(description = "The dimension to profile",contextual = true,defaultValue = "default") IrisDimension dimension) {
      File var2 = var1.getLoadFile().getParentFile().getParentFile();
      File var3 = Iris.instance.getDataFile(new String[]{"profile.txt"});
      new IrisProject(var2);
      IrisData var5 = IrisData.get(var2);
      KList var6 = new KList();
      KMap var7 = new KMap();
      KMap var8 = new KMap();
      KMap var9 = new KMap();
      KMap var10 = new KMap();
      KMap var11 = new KMap();
      this.sender().sendMessage("Calculating Performance Metrics for Noise generators");
      NoiseStyle[] var12 = NoiseStyle.values();
      int var13 = var12.length;

      int var14;
      int var18;
      for(var14 = 0; var14 < var13; ++var14) {
         NoiseStyle var15 = var12[var14];
         CNG var16 = var15.create(new RNG((long)var15.hashCode()));

         for(int var17 = 0; var17 < 3000; ++var17) {
            var16.noise((double)var17, (double)(var17 + 1000), (double)(var17 * var17));
            var16.noise((double)var17, (double)(-var17));
         }

         PrecisionStopwatch var44 = PrecisionStopwatch.start();

         for(var18 = 0; var18 < 100000; ++var18) {
            var16.noise((double)var18, (double)(var18 + 1000), (double)(var18 * var18));
            var16.noise((double)var18, (double)(-var18));
         }

         var7.put(var15, var44.getMilliseconds());
      }

      var6.add((Object)"Noise Style Performance Impacts: ");
      Iterator var26 = var7.sortKNumber().iterator();

      String var10001;
      while(var26.hasNext()) {
         NoiseStyle var28 = (NoiseStyle)var26.next();
         var10001 = var28.name();
         var6.add((Object)(var10001 + ": " + String.valueOf(var7.get(var28))));
      }

      var6.add((Object)"");
      this.sender().sendMessage("Calculating Interpolator Timings...");
      InterpolationMethod[] var27 = InterpolationMethod.values();
      var13 = var27.length;

      for(var14 = 0; var14 < var13; ++var14) {
         InterpolationMethod var34 = var27[var14];
         IrisInterpolator var38 = new IrisInterpolator();
         var38.setFunction(var34);
         var38.setHorizontalScale(8.0D);
         NoiseProvider var45 = (var0, var2x) -> {
            return Math.random();
         };

         for(var18 = 0; var18 < 3000; ++var18) {
            var38.interpolate(var18, -var18, var45);
         }

         PrecisionStopwatch var51 = PrecisionStopwatch.start();

         for(int var19 = 0; var19 < 100000; ++var19) {
            var38.interpolate(var19 + 10000, -var19 - 100000, var45);
         }

         var8.put(var34, var51.getMilliseconds());
      }

      var6.add((Object)"Noise Interpolator Performance Impacts: ");
      var26 = var8.sortKNumber().iterator();

      while(var26.hasNext()) {
         InterpolationMethod var30 = (InterpolationMethod)var26.next();
         var10001 = var30.name();
         var6.add((Object)(var10001 + ": " + String.valueOf(var8.get(var30))));
      }

      var6.add((Object)"");
      this.sender().sendMessage("Processing Generator Scores: ");
      KMap var29 = new KMap();
      String[] var31 = var5.getGeneratorLoader().getPossibleKeys();
      var14 = var31.length;

      double var20;
      int var22;
      Iterator var23;
      int var35;
      for(var35 = 0; var35 < var14; ++var35) {
         String var40 = var31[var35];
         KList var47 = new KList();
         IrisGenerator var53 = (IrisGenerator)var5.getGeneratorLoader().load(var40);
         KList var54 = var53.getAllComposites();
         var20 = 0.0D;
         var22 = 0;
         var23 = var54.iterator();

         while(var23.hasNext()) {
            IrisNoiseGenerator var24 = (IrisNoiseGenerator)var23.next();
            ++var22;
            var20 += (Double)var7.get(var24.getStyle().getStyle());
            var47.add((Object)("Composite Noise Style " + var22 + " " + var24.getStyle().getStyle().name() + ": " + String.valueOf(var7.get(var24.getStyle().getStyle()))));
         }

         var20 += (Double)var8.get(var53.getInterpolator().getFunction());
         var10001 = var53.getInterpolator().getFunction().name();
         var47.add((Object)("Interpolator " + var10001 + ": " + String.valueOf(var8.get(var53.getInterpolator().getFunction()))));
         var9.put(var40, var20);
         var29.put(var40, var47);
      }

      var6.add((Object)"Project Generator Performance Impacts: ");
      Iterator var32 = var9.sortKNumber().iterator();

      while(var32.hasNext()) {
         String var36 = (String)var32.next();
         var6.add((Object)(var36 + ": " + String.valueOf(var9.get(var36))));
         ((KList)var29.get(var36)).forEach((var1x) -> {
            var6.add((Object)("  " + var1x));
         });
      }

      var6.add((Object)"");
      KMap var33 = new KMap();
      String[] var37 = var5.getBiomeLoader().getPossibleKeys();
      var35 = var37.length;

      int var42;
      String var49;
      for(var42 = 0; var42 < var35; ++var42) {
         var49 = var37[var42];
         KList var55 = new KList();
         IrisBiome var56 = (IrisBiome)var5.getBiomeLoader().load(var49);
         var20 = 0.0D;
         var22 = 0;
         var23 = var56.getLayers().iterator();

         while(var23.hasNext()) {
            IrisBiomePaletteLayer var62 = (IrisBiomePaletteLayer)var23.next();
            ++var22;
            var20 += (Double)var7.get(var62.getStyle().getStyle());
            var55.add((Object)("Palette Layer " + var22 + ": " + String.valueOf(var7.get(var62.getStyle().getStyle()))));
         }

         var20 += (Double)var7.get(var56.getBiomeStyle().getStyle());
         Object var46 = var7.get(var56.getBiomeStyle().getStyle());
         var55.add((Object)("Biome Style: " + String.valueOf(var46)));
         var20 += (Double)var7.get(var56.getChildStyle().getStyle());
         var46 = var7.get(var56.getChildStyle().getStyle());
         var55.add((Object)("Child Style: " + String.valueOf(var46)));
         var10.put(var49, var20);
         var33.put(var49, var55);
      }

      var6.add((Object)"Project Biome Performance Impacts: ");
      Iterator var39 = var10.sortKNumber().iterator();

      String var41;
      while(var39.hasNext()) {
         var41 = (String)var39.next();
         var6.add((Object)(var41 + ": " + String.valueOf(var10.get(var41))));
         ((KList)var33.get(var41)).forEach((var1x) -> {
            var6.add((Object)("  " + var1x));
         });
      }

      var6.add((Object)"");
      var37 = var5.getRegionLoader().getPossibleKeys();
      var35 = var37.length;

      double var58;
      for(var42 = 0; var42 < var35; ++var42) {
         var49 = var37[var42];
         IrisRegion var57 = (IrisRegion)var5.getRegionLoader().load(var49);
         var58 = 0.0D;
         var58 += (Double)var7.get(var57.getLakeStyle().getStyle());
         var58 += (Double)var7.get(var57.getRiverStyle().getStyle());
         var11.put(var49, var58);
      }

      var6.add((Object)"Project Region Performance Impacts: ");
      var39 = var11.sortKNumber().iterator();

      while(var39.hasNext()) {
         var41 = (String)var39.next();
         var6.add((Object)(var41 + ": " + String.valueOf(var11.get(var41))));
      }

      var6.add((Object)"");
      double var43 = 0.0D;

      double var52;
      for(Iterator var48 = var10.v().iterator(); var48.hasNext(); var43 += var52) {
         var52 = (Double)var48.next();
      }

      var43 /= (double)var10.size();
      double var50 = 0.0D;

      for(Iterator var59 = var9.v().iterator(); var59.hasNext(); var50 += var58) {
         var58 = (Double)var59.next();
      }

      var50 /= (double)var9.size();
      var43 += var50;
      double var60 = 0.0D;

      double var21;
      for(Iterator var61 = var11.v().iterator(); var61.hasNext(); var60 += var21) {
         var21 = (Double)var61.next();
      }

      var60 /= (double)var11.size();
      var43 += var60;
      var6.add((Object)("Average Score: " + var43));
      this.sender().sendMessage("Score: " + Form.duration(var43, 0));

      try {
         IO.writeAll(var3, (Object)var6.toString("\n"));
      } catch (IOException var25) {
         Iris.reportError(var25);
         var25.printStackTrace();
      }

      VolmitSender var10000 = this.sender();
      var10001 = String.valueOf(C.GREEN);
      var10000.sendMessage(var10001 + "Done! " + var3.getPath());
   }

   @Decree(
      description = "Spawn an Iris entity",
      aliases = {"summon"},
      origin = DecreeOrigin.PLAYER
   )
   public void spawn(@Param(description = "The entity to spawn") IrisEntity entity, @Param(description = "The location to spawn the entity at",contextual = true) Vector location) {
      if (!IrisToolbelt.isIrisWorld(this.player().getWorld())) {
         this.sender().sendMessage(String.valueOf(C.RED) + "You have to be in an Iris world to spawn entities properly. Trying to spawn the best we can do.");
      }

      var1.spawn(this.engine(), new Location(this.world(), var2.getX(), var2.getY(), var2.getZ()));
   }

   @Decree(
      description = "Teleport to the active studio world",
      aliases = {"stp"},
      origin = DecreeOrigin.PLAYER,
      sync = true
   )
   public void tpstudio() {
      if (!((StudioSVC)Iris.service(StudioSVC.class)).isProjectOpen()) {
         this.sender().sendMessage(String.valueOf(C.RED) + "No studio world is open!");
      } else if (IrisToolbelt.isIrisWorld(this.world()) && this.engine().isStudio()) {
         this.sender().sendMessage(String.valueOf(C.RED) + "You are already in a studio world!");
      } else {
         this.sender().sendMessage(String.valueOf(C.GREEN) + "Sending you to the studio world!");
         Player var1 = this.player();
         PaperLib.teleportAsync(this.player(), ((StudioSVC)Iris.service(StudioSVC.class)).getActiveProject().getActiveProvider().getTarget().getWorld().spawnLocation()).thenRun(() -> {
            var1.setGameMode(GameMode.SPECTATOR);
         });
      }
   }

   @Decree(
      description = "Update your dimension projects VSCode workspace"
   )
   public void update(@Param(description = "The dimension to update the workspace of",contextual = true,defaultValue = "default") IrisDimension dimension) {
      VolmitSender var10000 = this.sender();
      String var10001 = String.valueOf(C.GOLD);
      var10000.sendMessage(var10001 + "Updating Code Workspace for " + var1.getName() + "...");
      if ((new IrisProject(var1.getLoader().getDataFolder())).updateWorkspace()) {
         var10000 = this.sender();
         var10001 = String.valueOf(C.GREEN);
         var10000.sendMessage(var10001 + "Updated Code Workspace for " + var1.getName());
      } else {
         var10000 = this.sender();
         var10001 = String.valueOf(C.RED);
         var10000.sendMessage(var10001 + "Invalid project: " + var1.getName() + ". Try deleting the code-workspace file and try again.");
      }

   }

   @Decree(
      aliases = {"find-objects"},
      description = "Get information about nearby structures"
   )
   public void objects() {
      if (!IrisToolbelt.isIrisWorld(this.player().getWorld())) {
         this.sender().sendMessage(String.valueOf(C.RED) + "You must be in an Iris world");
      } else {
         World var1 = this.player().getWorld();
         if (!IrisToolbelt.isIrisWorld(var1)) {
            this.sender().sendMessage("You must be in an iris world.");
         } else {
            KList var2 = new KList();
            int var3 = this.player().getLocation().getChunk().getX();
            int var4 = this.player().getLocation().getChunk().getZ();

            int var7;
            try {
               Location var5 = this.player().getTargetBlockExact(48, FluidCollisionMode.NEVER).getLocation();
               int var6 = var5.getChunk().getX();
               var7 = var5.getChunk().getZ();
               (new Spiraler(3, 3, (var4x, var5x) -> {
                  var2.addIfMissing(var1.getChunkAt(var4x + var6, var5x + var7));
               })).drain();
            } catch (Throwable var21) {
               Iris.reportError(var21);
            }

            (new Spiraler(3, 3, (var4x, var5x) -> {
               var2.addIfMissing(var1.getChunkAt(var4x + var3, var5x + var4));
            })).drain();
            this.sender().sendMessage("Capturing IGenData from " + var2.size() + " nearby chunks.");

            try {
               Iris var10000 = Iris.instance;
               String[] var10001 = new String[1];
               long var10004 = M.ms();
               var10001[0] = "reports/" + var10004 + ".txt";
               File var24 = var10000.getDataFile(var10001);
               PrintWriter var25 = new PrintWriter(var24);
               var25.println("=== Iris Chunk Report ===");
               var25.println("== General Info ==");
               String var36 = Iris.instance.getDescription().getVersion();
               var25.println("Iris Version: " + var36);
               var36 = Bukkit.getBukkitVersion();
               var25.println("Bukkit Version: " + var36);
               var36 = Bukkit.getVersion();
               var25.println("MC Version: " + var36);
               var25.println("PaperSpigot: " + (PaperLib.isPaper() ? "Yup!" : "Nope!"));
               Date var38 = new Date();
               var25.println("Report Captured At: " + String.valueOf(var38));
               var25.println("Chunks: (" + var2.size() + "): ");
               Iterator var26 = var2.iterator();

               while(var26.hasNext()) {
                  Chunk var8 = (Chunk)var26.next();
                  int var40 = var8.getX();
                  var25.println("- [" + var40 + ", " + var8.getZ() + "]");
               }

               boolean var27 = false;
               long var28 = 0L;
               String var10 = "No idea...";

               try {
                  File[] var11 = (File[])Objects.requireNonNull((new File(var1.getWorldFolder(), "region")).listFiles());
                  int var12 = var11.length;

                  for(int var13 = 0; var13 < var12; ++var13) {
                     File var14 = var11[var13];
                     if (var14.isFile()) {
                        var28 += var14.length();
                     }
                  }
               } catch (Throwable var22) {
                  Iris.reportError(var22);
               }

               try {
                  FileTime var29 = (FileTime)Files.getAttribute(var1.getWorldFolder().toPath(), "creationTime");
                  var10 = hrf(Duration.of(M.ms() - var29.toMillis(), ChronoUnit.MILLIS));
               } catch (IOException var20) {
                  Iris.reportError(var20);
               }

               KList var30 = new KList();
               KList var31 = new KList();
               KMap var32 = new KMap();
               Iterator var33 = var2.iterator();

               while(var33.hasNext()) {
                  Chunk var15 = (Chunk)var33.next();

                  for(int var16 = 0; var16 < 16; var16 += 3) {
                     for(int var17 = 0; var17 < 16; var17 += 3) {
                        assert this.engine() != null;

                        IrisBiome var18 = this.engine().getSurfaceBiome(var15.getX() * 16 + var16, var15.getZ() * 16 + var17);
                        IrisBiome var19 = this.engine().getCaveBiome(var15.getX() * 16 + var16, var15.getZ() * 16 + var17);
                        var36 = var18.getName();
                        var30.addIfMissing(var36 + " [" + Form.capitalize(var18.getInferredType().name().toLowerCase()) + "]  (" + var18.getLoadFile().getName() + ")");
                        var36 = var19.getName();
                        var31.addIfMissing(var36 + " (" + var19.getLoadFile().getName() + ")");
                        this.exportObjects(var18, var25, this.engine(), var32);
                        this.exportObjects(var19, var25, this.engine(), var32);
                     }
                  }
               }

               var7 = ((String[])Objects.requireNonNull((new File(var1.getWorldFolder().getPath() + "/region")).list())).length;
               var25.println();
               var25.println("== World Info ==");
               var25.println("World Name: " + var1.getName());
               var25.println("Age: " + var10);
               var25.println("Folder: " + var1.getWorldFolder().getPath());
               var25.println("Regions: " + Form.f(var7));
               var25.println("Chunks: max. " + Form.f(var7 * 32 * 32));
               var25.println("World Size: min. " + Form.fileSize(var28));
               var25.println();
               var25.println("== Biome Info ==");
               var25.println("Found " + var30.size() + " Biome(s): ");
               var33 = var30.iterator();

               String var34;
               while(var33.hasNext()) {
                  var34 = (String)var33.next();
                  var25.println("- " + var34);
               }

               var25.println();
               var25.println("== Object Info ==");
               var33 = var32.k().iterator();

               while(var33.hasNext()) {
                  var34 = (String)var33.next();
                  var25.println("- " + var34);
                  Iterator var35 = ((KMap)var32.get(var34)).k().iterator();

                  while(var35.hasNext()) {
                     String var37 = (String)var35.next();
                     var25.println("  @ " + var37);
                     Iterator var39 = ((KList)((KMap)var32.get(var34)).get(var37)).iterator();

                     while(var39.hasNext()) {
                        String var41 = (String)var39.next();
                        var25.println("    * " + var41);
                     }
                  }
               }

               var25.println();
               var25.close();
               this.sender().sendMessage("Reported to: " + var24.getPath());
            } catch (FileNotFoundException var23) {
               var23.printStackTrace();
               Iris.reportError(var23);
            }

         }
      }
   }

   private void exportObjects(IrisBiome bb, PrintWriter pw, Engine g, KMap<String, KMap<String, KList<String>>> objects) {
      String var10000 = var1.getName();
      String var5 = var10000 + " [" + Form.capitalize(var1.getInferredType().name().toLowerCase()) + "]  (" + var1.getLoadFile().getName() + ")";
      int var6 = 0;
      KSet var7 = new KSet(new String[0]);
      Iterator var8 = var1.getObjects().iterator();

      label34:
      while(var8.hasNext()) {
         IrisObjectPlacement var9 = (IrisObjectPlacement)var8.next();
         ++var6;
         String var10 = "Placement #" + var6 + " (" + var9.getPlace().size() + " possible objects)";
         Iterator var11 = var9.getPlace().iterator();

         while(true) {
            String var13;
            while(true) {
               if (!var11.hasNext()) {
                  continue label34;
               }

               String var12 = (String)var11.next();
               var13 = var12 + ": [ERROR] Failed to find object!";

               try {
                  if (!var7.contains(var12)) {
                     File var14 = var3.getData().getObjectLoader().findFile(var12);
                     BlockVector var15 = IrisObject.sampleSize(var14);
                     var13 = var12 + ": size=[" + var15.getBlockX() + "," + var15.getBlockY() + "," + var15.getBlockZ() + "] location=[" + var14.getPath() + "]";
                     var7.add(var12);
                     break;
                  }
               } catch (Throwable var16) {
                  Iris.reportError(var16);
                  break;
               }
            }

            ((KList)((KMap)var4.computeIfAbsent(var5, (var0) -> {
               return new KMap();
            })).computeIfAbsent(var10, (var0) -> {
               return new KList();
            })).addIfMissing(var13);
         }
      }

   }

   private boolean noGUI() {
      if (!IrisSettings.get().getGui().isUseServerLaunchedGuis()) {
         this.sender().sendMessage(String.valueOf(C.RED) + "You must have server launched GUIs enabled in the settings!");
         return true;
      } else {
         return false;
      }
   }

   private boolean noStudio() {
      if (!this.sender().isPlayer()) {
         this.sender().sendMessage(String.valueOf(C.RED) + "Players only!");
         return true;
      } else if (!((StudioSVC)Iris.service(StudioSVC.class)).isProjectOpen()) {
         this.sender().sendMessage(String.valueOf(C.RED) + "No studio world is open!");
         return true;
      } else if (!this.engine().isStudio()) {
         this.sender().sendMessage(String.valueOf(C.RED) + "You must be in a studio world!");
         return true;
      } else {
         return false;
      }
   }

   public void files(File clean, KList<File> files) {
      if (var1.isDirectory()) {
         File[] var3 = var1.listFiles();
         int var4 = var3.length;

         for(int var5 = 0; var5 < var4; ++var5) {
            File var6 = var3[var5];
            this.files(var6, var2);
         }
      } else if (var1.getName().endsWith(".json")) {
         try {
            var2.add((Object)var1);
         } catch (Throwable var7) {
            Iris.reportError(var7);
            Iris.error("Failed to beautify " + var1.getAbsolutePath() + " You may have errors in your json!");
         }
      }

   }

   private void fixBlocks(JSONObject obj) {
      Iterator var2 = var1.keySet().iterator();

      while(var2.hasNext()) {
         String var3 = (String)var2.next();
         Object var4 = var1.get(var3);
         if (var3.equals("block") && var4 instanceof String && !var4.toString().trim().isEmpty() && !var4.toString().contains(":")) {
            var1.put(var3, (Object)("minecraft:" + String.valueOf(var4)));
         }

         if (var4 instanceof JSONObject) {
            this.fixBlocks((JSONObject)var4);
         } else if (var4 instanceof JSONArray) {
            this.fixBlocks((JSONArray)var4);
         }
      }

   }

   private void fixBlocks(JSONArray obj) {
      for(int var2 = 0; var2 < var1.length(); ++var2) {
         Object var3 = var1.get(var2);
         if (var3 instanceof JSONObject) {
            this.fixBlocks((JSONObject)var3);
         } else if (var3 instanceof JSONArray) {
            this.fixBlocks((JSONArray)var3);
         }
      }

   }
}
