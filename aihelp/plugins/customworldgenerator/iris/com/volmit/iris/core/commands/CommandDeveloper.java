package com.volmit.iris.core.commands;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.volmit.iris.Iris;
import com.volmit.iris.core.ServerConfigurator;
import com.volmit.iris.core.loader.IrisData;
import com.volmit.iris.core.loader.ResourceLoader;
import com.volmit.iris.core.nms.INMS;
import com.volmit.iris.core.nms.container.StructurePlacement;
import com.volmit.iris.core.nms.datapack.DataVersion;
import com.volmit.iris.core.service.IrisEngineSVC;
import com.volmit.iris.core.tools.IrisPackBenchmarking;
import com.volmit.iris.core.tools.IrisToolbelt;
import com.volmit.iris.engine.framework.Engine;
import com.volmit.iris.engine.object.IrisDimension;
import com.volmit.iris.engine.object.IrisJigsawStructurePlacement;
import com.volmit.iris.engine.object.IrisObject;
import com.volmit.iris.engine.object.IrisPosition;
import com.volmit.iris.engine.object.annotations.Snippet;
import com.volmit.iris.util.collection.KList;
import com.volmit.iris.util.collection.KMap;
import com.volmit.iris.util.collection.KSet;
import com.volmit.iris.util.context.IrisContext;
import com.volmit.iris.util.decree.DecreeExecutor;
import com.volmit.iris.util.decree.DecreeOrigin;
import com.volmit.iris.util.decree.annotations.Decree;
import com.volmit.iris.util.decree.annotations.Param;
import com.volmit.iris.util.decree.specialhandlers.NullableDimensionHandler;
import com.volmit.iris.util.format.C;
import com.volmit.iris.util.format.Form;
import com.volmit.iris.util.io.CountingDataInputStream;
import com.volmit.iris.util.io.IO;
import com.volmit.iris.util.mantle.TectonicPlate;
import com.volmit.iris.util.math.M;
import com.volmit.iris.util.matter.Matter;
import com.volmit.iris.util.nbt.mca.MCAFile;
import com.volmit.iris.util.nbt.mca.MCAUtil;
import com.volmit.iris.util.parallel.BurstExecutor;
import com.volmit.iris.util.parallel.MultiBurst;
import com.volmit.iris.util.plugin.VolmitSender;
import com.volmit.iris.util.scheduling.jobs.Job;
import java.io.BufferedInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.RandomAccessFile;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.nio.file.Files;
import java.nio.file.OpenOption;
import java.sql.Date;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;
import net.jpountz.lz4.LZ4BlockInputStream;
import net.jpountz.lz4.LZ4BlockOutputStream;
import net.jpountz.lz4.LZ4FrameInputStream;
import net.jpountz.lz4.LZ4FrameOutputStream;
import org.apache.commons.lang.RandomStringUtils;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.World;
import org.bukkit.generator.WorldInfo;

@Decree(
   name = "Developer",
   origin = DecreeOrigin.BOTH,
   description = "Iris World Manager",
   aliases = {"dev"}
)
public class CommandDeveloper implements DecreeExecutor {
   private CommandTurboPregen turboPregen;
   private CommandLazyPregen lazyPregen;

   @Decree(
      description = "Get Loaded TectonicPlates Count",
      origin = DecreeOrigin.BOTH,
      sync = true
   )
   public void EngineStatus() {
      ((IrisEngineSVC)Iris.service(IrisEngineSVC.class)).engineStatus(this.sender());
   }

   @Decree(
      description = "Send a test exception to sentry"
   )
   public void Sentry() {
      Engine var1 = this.engine();
      if (var1 != null) {
         IrisContext.getOr(var1);
      }

      Iris.reportError(new Exception("This is a test"));
   }

   @Decree(
      description = "Dev cmd to fix all the broken objects caused by faulty shrinkwarp"
   )
   public void fixObjects(@Param(aliases = {"dimension"},description = "The dimension type to create the world with") IrisDimension type) {
      if (var1 == null) {
         this.sender().sendMessage("Type cant be null?");
      } else {
         final IrisData var2 = IrisData.get(Iris.instance.getDataFolder(new String[]{"packs", var1.getLoadKey()}));
         final ResourceLoader var3 = var2.getObjectLoader();
         final KMap var4 = new KMap();
         String[] var5 = var3.getPossibleKeys();
         final String[] var6 = var2.getJigsawPieceLoader().getPossibleKeys();
         VolmitSender var7 = this.sender();
         String var10001 = String.valueOf(C.IRIS);
         var7.sendMessage(var10001 + "Found " + var5.length + " objects in " + var1.getLoadKey());
         var10001 = String.valueOf(C.IRIS);
         var7.sendMessage(var10001 + "Found " + var6.length + " jigsaw pieces in " + var1.getLoadKey());
         final int var8 = var5.length;
         final AtomicInteger var9 = new AtomicInteger();
         final AtomicInteger var10 = new AtomicInteger();
         (new Job(this) {
            public String getName() {
               return "Fixing Objects";
            }

            public void execute() {
               Stream var10000 = (Stream)Arrays.stream(var6).parallel();
               ResourceLoader var10001 = var2.getJigsawPieceLoader();
               Objects.requireNonNull(var10001);
               var10000.map(var10001::load).filter(Objects::nonNull).forEach((var5) -> {
                  IrisPosition var6x = (IrisPosition)var4.compute(var5.getObject(), (var3x, var4x) -> {
                     if (var4x != null) {
                        return var4x;
                     } else {
                        IrisObject var5 = (IrisObject)var3.load(var3x);
                        if (var5 == null) {
                           return new IrisPosition();
                        } else {
                           var5.shrinkwrap();

                           try {
                              if (!var5.getShrinkOffset().isZero()) {
                                 var10.incrementAndGet();
                                 var5.write(var5.getLoadFile());
                              }

                              this.completeWork();
                           } catch (IOException var7) {
                              Iris.error("Failed to write object " + var5.getLoadKey());
                              var7.printStackTrace();
                              return new IrisPosition();
                           }

                           return new IrisPosition(var5.getShrinkOffset());
                        }
                     }
                  });
                  if (var6x.getX() != 0 || var6x.getY() != 0 || var6x.getZ() != 0) {
                     var5.getConnectors().forEach((var1) -> {
                        var1.setPosition(var1.getPosition().add(var6x));
                     });

                     try {
                        IO.writeAll(var5.getLoadFile(), (Object)var2.getGson().toJson(var5));
                     } catch (IOException var8x) {
                        Iris.error("Failed to write jigsaw piece " + var5.getLoadKey());
                        var8x.printStackTrace();
                     }

                  }
               });
               var10000 = ((Stream)Arrays.stream(var3.getPossibleKeys()).parallel()).filter((var1) -> {
                  return !var4.containsKey(var1);
               });
               var10001 = var3;
               Objects.requireNonNull(var10001);
               var10000.map(var10001::load).forEach((var2x) -> {
                  if (var2x == null) {
                     this.completeWork();
                  } else {
                     var2x.shrinkwrap();
                     if (var2x.getShrinkOffset().isZero()) {
                        this.completeWork();
                     } else {
                        try {
                           var2x.write(var2x.getLoadFile());
                           this.completeWork();
                           var10.incrementAndGet();
                        } catch (IOException var4x) {
                           Iris.error("Failed to write object " + var2x.getLoadKey());
                           var4x.printStackTrace();
                        }

                     }
                  }
               });
            }

            public void completeWork() {
               var9.incrementAndGet();
            }

            public int getTotalWork() {
               return var8;
            }

            public int getWorkCompleted() {
               return var9.get();
            }
         }).execute(var7, () -> {
            int var4 = var8 - var9.get();
            String var10001;
            if (var4 != 0) {
               var10001 = String.valueOf(C.IRIS);
               var7.sendMessage(var10001 + var4 + " objects failed!");
            }

            if (var10.get() != 0) {
               var10001 = String.valueOf(C.IRIS);
               var7.sendMessage(var10001 + var10.get() + " objects had their offsets changed!");
            } else {
               var7.sendMessage(String.valueOf(C.IRIS) + "No objects had their offsets changed!");
            }

         });
      }
   }

   @Decree(
      description = "Test"
   )
   public void mantle(@Param(defaultValue = "false") boolean plate, @Param(defaultValue = "21474836474") String name) {
      File var3 = Iris.instance.getDataFile(new String[]{"dump", "pv." + var2 + ".ttp.lz4b.bin"});
      File var4 = Iris.instance.getDataFile(new String[]{"dump", "pv." + var2 + ".section.bin"});
      if (var1) {
         try {
            CountingDataInputStream var5 = CountingDataInputStream.wrap(new BufferedInputStream(new FileInputStream(var3)));

            try {
               new TectonicPlate(1088, var5, true);
            } catch (Throwable var9) {
               if (var5 != null) {
                  try {
                     var5.close();
                  } catch (Throwable var8) {
                     var9.addSuppressed(var8);
                  }
               }

               throw var9;
            }

            if (var5 != null) {
               var5.close();
            }
         } catch (Throwable var10) {
            var10.printStackTrace();
         }
      } else {
         Matter.read(var4);
      }

      if (!TectonicPlate.hasError()) {
         long var10000 = (var1 ? var3 : var4).length();
         Iris.info("Read " + var10000 + " bytes from " + (var1 ? var3 : var4).getAbsolutePath());
      }

   }

   private void extractSection(File source, File target, long offset, int length) {
      RandomAccessFile var6 = new RandomAccessFile(var1, "r");
      byte[] var7 = new byte[var5];
      var6.seek(var3);
      var6.readFully(var7);
      var6.close();
      Files.write(var2.toPath(), var7, new OpenOption[0]);
   }

   @Decree(
      description = "Test"
   )
   public void dumpThreads() {
      try {
         Iris var10000 = Iris.instance;
         String[] var10001 = new String[]{"dump", null};
         Date var10004 = new Date(M.ms());
         var10001[1] = "td-" + String.valueOf(var10004) + ".txt";
         File var1 = var10000.getDataFile(var10001);
         FileOutputStream var2 = new FileOutputStream(var1);
         Map var3 = Thread.getAllStackTraces();
         PrintWriter var4 = new PrintWriter(var2);
         int var13 = Thread.activeCount();
         var4.println(var13 + "/" + var3.size());
         Runtime var5 = Runtime.getRuntime();
         var4.println("Memory:");
         var4.println("\tMax: " + var5.maxMemory());
         var4.println("\tTotal: " + var5.totalMemory());
         var4.println("\tFree: " + var5.freeMemory());
         var4.println("\tUsed: " + (var5.totalMemory() - var5.freeMemory()));
         Iterator var6 = var3.keySet().iterator();

         while(var6.hasNext()) {
            Thread var7 = (Thread)var6.next();
            var4.println("========================================");
            String var14 = var7.getName();
            var4.println("Thread: '" + var14 + "' ID: " + var7.getId() + " STATUS: " + var7.getState().name());
            StackTraceElement[] var8 = (StackTraceElement[])var3.get(var7);
            int var9 = var8.length;

            for(int var10 = 0; var10 < var9; ++var10) {
               StackTraceElement var11 = var8[var10];
               var4.println("    @ " + var11.toString());
            }

            var4.println("========================================");
            var4.println();
            var4.println();
         }

         var4.close();
         Iris.info("DUMPED! See " + var1.getAbsolutePath());
      } catch (Throwable var12) {
         var12.printStackTrace();
      }

   }

   @Decree(
      description = "Generate Iris structures for all loaded datapack structures"
   )
   public void generateStructures(@Param(description = "The pack to add the generated structures to",aliases = {"pack"},defaultValue = "null",customHandler = NullableDimensionHandler.class) IrisDimension dimension, @Param(description = "Ignore existing structures",defaultValue = "false") boolean force) {
      try {
         KMap var3 = INMS.get().collectStructures();
         if (var3.isEmpty()) {
            this.sender().sendMessage(String.valueOf(C.IRIS) + "No structures found");
         } else {
            VolmitSender var10000 = this.sender();
            String var10001 = String.valueOf(C.IRIS);
            var10000.sendMessage(var10001 + "Found " + var3.size() + " structures");
            JsonObject var11 = new JsonObject();
            File var4;
            IrisData var5;
            Object var6;
            Map var7;
            File var8;
            if (var1 == null) {
               var4 = Iris.instance.getDataFolder(new String[]{"structures"});
               IO.delete(var4);
               var5 = IrisData.get(var4);
               var6 = Set.of();
               var7 = Map.of();
               var8 = new File(var4, "structures.json");
            } else {
               var5 = var1.getLoader();
               var4 = var5.getDataFolder();
               var6 = new KSet(var5.getJigsawStructureLoader().getPossibleKeys());
               var11 = (JsonObject)var5.getGson().fromJson(IO.readAll(var1.getLoadFile()), JsonObject.class);
               var7 = (Map)Optional.ofNullable(var11.getAsJsonArray("jigsawStructures")).map((var1x) -> {
                  return (Map)var1x.asList().stream().filter(JsonElement::isJsonPrimitive).collect(Collectors.toMap((var1) -> {
                     return ((IrisJigsawStructurePlacement)var5.getGson().fromJson(var1, IrisJigsawStructurePlacement.class)).getStructure();
                  }, (var0) -> {
                     return Set.of(var0.getAsString());
                  }, KSet::merge));
               }).orElse(Map.of());
               var8 = var1.getLoadFile();
            }

            File var9 = new File(var4, "jigsaw-structures");
            File var10 = new File(var4, "snippet/" + ((Snippet)IrisJigsawStructurePlacement.class.getAnnotation(Snippet.class)).value());
            Gson var12 = var5.getGson();
            JsonArray var13 = (JsonArray)Optional.ofNullable(var11.getAsJsonArray("jigsawStructures")).orElse(new JsonArray(var3.size()));
            var3.forEach((var8x, var9x) -> {
               String var10000 = var8x.namespace();
               String var10x = "datapack/" + var10000 + "/" + var8x.key();
               if (!var6.contains(var10x) || var2) {
                  List var11 = var9x.structures();
                  JsonObject var12x = var9x.toJson(var10x);
                  String var10001;
                  VolmitSender var21;
                  if (var12x != null && !var11.isEmpty()) {
                     File var13x = new File(var10, var10x + ".json");

                     try {
                        IO.writeAll(var13x, (Object)var12.toJson(var12x));
                     } catch (IOException var20) {
                        var21 = this.sender();
                        var10001 = String.valueOf(C.RED);
                        var21.sendMessage(var10001 + "Failed to generate snippet for " + String.valueOf(var8x));
                        var20.printStackTrace();
                        return;
                     }

                     Set var14 = (Set)var7.getOrDefault(var10x, Set.of(var10x));
                     var13.asList().removeIf((var1) -> {
                        return var14.contains((var1.isJsonObject() ? var1.getAsJsonObject().get("structure") : var1).getAsString());
                     });
                     var13.add("snippet/" + var10x);
                     String var15;
                     if (var11.size() > 1) {
                        KList var16 = new KList();

                        for(int var17 = 0; var17 < var11.size(); ++var17) {
                           List var18 = ((StructurePlacement.Structure)var11.get(var17)).tags();
                           if (var17 == 0) {
                              var16.addAll(var18);
                           } else {
                              var16.removeIf((var1) -> {
                                 return !var18.contains(var1);
                              });
                           }
                        }

                        var15 = var16.isNotEmpty() ? "#" + (String)var16.getFirst() : ((StructurePlacement.Structure)var11.getFirst()).key();
                     } else {
                        var15 = ((StructurePlacement.Structure)var11.getFirst()).key();
                     }

                     JsonArray var22 = new JsonArray();
                     if (var11.size() > 1) {
                        Stream var23 = var11.stream().flatMap((var0) -> {
                           String[] var1 = new String[var0.weight()];
                           Arrays.fill(var1, var0.key());
                           return Arrays.stream(var1);
                        });
                        Objects.requireNonNull(var22);
                        var23.forEach(var22::add);
                     } else {
                        var22.add(var15);
                     }

                     var12x = new JsonObject();
                     var12x.addProperty("structureKey", var15);
                     var12x.add("datapackStructures", var22);
                     File var24 = new File(var9, var10x + ".json");
                     var24.getParentFile().mkdirs();

                     try {
                        IO.writeAll(var24, (Object)var12.toJson(var12x));
                     } catch (IOException var19) {
                        var19.printStackTrace();
                     }

                  } else {
                     var21 = this.sender();
                     var10001 = String.valueOf(C.RED);
                     var21.sendMessage(var10001 + "Failed to generate hook for " + String.valueOf(var8x));
                  }
               }
            });
            var11.add("jigsawStructures", var13);
            IO.writeAll(var8, (Object)var12.toJson(var11));
            var5.hotloaded();
         }
      } catch (Throwable var14) {
         throw var14;
      }
   }

   @Decree(
      description = "Test"
   )
   public void packBenchmark(@Param(description = "The pack to bench",aliases = {"pack"},defaultValue = "overworld") IrisDimension dimension, @Param(description = "Radius in regions",defaultValue = "2048") int radius, @Param(description = "Open GUI while benchmarking",defaultValue = "false") boolean gui) {
      new IrisPackBenchmarking(var1, var2, var3);
   }

   @Decree(
      description = "Upgrade to another Minecraft version"
   )
   public void upgrade(@Param(description = "The version to upgrade to",defaultValue = "latest") DataVersion version) {
      VolmitSender var10000 = this.sender();
      String var10001 = String.valueOf(C.GREEN);
      var10000.sendMessage(var10001 + "Upgrading to " + var1.getVersion() + "...");
      ServerConfigurator.installDataPacks(var1.get(), false);
      var10000 = this.sender();
      var10001 = String.valueOf(C.GREEN);
      var10000.sendMessage(var10001 + "Done upgrading! You can now update your server version to " + var1.getVersion());
   }

   @Decree(
      description = "test"
   )
   public void mca(@Param(description = "String") String world) {
      try {
         File[] var2 = (new File(var1, "region")).listFiles((var0, var1x) -> {
            return var1x.endsWith(".mca");
         });
         File[] var3 = var2;
         int var4 = var2.length;

         for(int var5 = 0; var5 < var4; ++var5) {
            File var6 = var3[var5];
            MCAFile var7 = MCAUtil.read(var6);
         }
      } catch (Exception var8) {
         var8.printStackTrace();
      }

   }

   @Decree(
      description = "UnloadChunks for good reasons."
   )
   public void unloadchunks() {
      ArrayList var1 = new ArrayList();
      int var2 = 0;
      Iterator var3 = Bukkit.getWorlds().iterator();

      World var4;
      while(var3.hasNext()) {
         var4 = (World)var3.next();

         try {
            if (IrisToolbelt.access(var4).getEngine() != null) {
               var1.add(var4);
            }
         } catch (Exception var9) {
         }
      }

      var3 = var1.iterator();

      while(var3.hasNext()) {
         var4 = (World)var3.next();
         Chunk[] var5 = var4.getLoadedChunks();
         int var6 = var5.length;

         for(int var7 = 0; var7 < var6; ++var7) {
            Chunk var8 = var5[var7];
            if (var8.isLoaded()) {
               var8.unload();
               ++var2;
            }
         }
      }

      Iris.info(String.valueOf(C.IRIS) + "Chunks Unloaded: " + var2);
   }

   @Decree
   public void objects(@Param(defaultValue = "overworld") IrisDimension dimension) {
      ResourceLoader var2 = var1.getLoader().getObjectLoader();
      VolmitSender var3 = this.sender();
      String[] var4 = var2.getPossibleKeys();
      BurstExecutor var5 = MultiBurst.burst.burst(var4.length);
      AtomicInteger var6 = new AtomicInteger();
      String[] var7 = var4;
      int var8 = var4.length;

      for(int var9 = 0; var9 < var8; ++var9) {
         String var10 = var7[var9];
         var5.queue(() -> {
            if (var2.load(var10) == null) {
               var6.incrementAndGet();
            }

         });
      }

      var5.complete();
      String var10001 = String.valueOf(C.RED);
      var3.sendMessage(var10001 + "Failed to load " + var6.get() + " of " + var4.length + " objects");
   }

   @Decree(
      description = "Test",
      aliases = {"ip"}
   )
   public void network() {
      try {
         Enumeration var1 = NetworkInterface.getNetworkInterfaces();
         Iterator var2 = Collections.list(var1).iterator();

         while(var2.hasNext()) {
            NetworkInterface var3 = (NetworkInterface)var2.next();
            Iris.info("Display Name: %s", var3.getDisplayName());
            Enumeration var4 = var3.getInetAddresses();
            Iterator var5 = Collections.list(var4).iterator();

            while(var5.hasNext()) {
               InetAddress var6 = (InetAddress)var5.next();
               Iris.info("IP: %s", var6.getHostAddress());
            }
         }
      } catch (Exception var7) {
         var7.printStackTrace();
      }

   }

   @Decree(
      description = "Test the compression algorithms"
   )
   public void compression(@Param(description = "base IrisWorld") World world, @Param(description = "raw TectonicPlate File") String path, @Param(description = "Algorithm to Test") String algorithm, @Param(description = "Amount of Tests") int amount, @Param(description = "Is versioned",defaultValue = "false") boolean versioned) {
      if (!IrisToolbelt.isIrisWorld(var1)) {
         VolmitSender var10000 = this.sender();
         String var10001 = String.valueOf(C.RED);
         var10000.sendMessage(var10001 + "This is not an Iris world. Iris worlds: " + String.join(", ", Bukkit.getServer().getWorlds().stream().filter(IrisToolbelt::isIrisWorld).map(WorldInfo::getName).toList()));
      } else {
         File var6 = new File(var2);
         if (var6.exists()) {
            Engine var7 = IrisToolbelt.access(var1).getEngine();
            if (var7 != null) {
               int var8 = var7.getTarget().getHeight();
               ExecutorService var9 = Executors.newFixedThreadPool(1);
               VolmitSender var10 = this.sender();
               var9.submit(() -> {
                  try {
                     CountingDataInputStream var7 = CountingDataInputStream.wrap(new FileInputStream(var6));
                     TectonicPlate var8x = new TectonicPlate(var8, var7, var5);
                     var7.close();
                     double var9 = 0.0D;
                     double var11 = 0.0D;
                     long var13 = 0L;
                     File var15 = new File("tmp");
                     var15.mkdirs();

                     for(int var16 = 0; var16 < var4; ++var16) {
                        String var10003 = RandomStringUtils.randomAlphanumeric(10);
                        File var17 = new File(var15, var10003 + "." + var3 + ".bin");
                        DataOutputStream var18 = this.createOutput(var17, var3);
                        long var19 = System.currentTimeMillis();
                        var8x.write(var18);
                        var18.close();
                        var9 += (double)(System.currentTimeMillis() - var19);
                        if (var13 == 0L) {
                           var13 = var17.length();
                        }

                        var19 = System.currentTimeMillis();
                        CountingDataInputStream var21 = this.createInput(var17, var3);
                        new TectonicPlate(var8, var21, true);
                        var21.close();
                        var11 += (double)(System.currentTimeMillis() - var19);
                        var17.delete();
                     }

                     IO.delete(var15);
                     var10.sendMessage(var3 + " is " + Form.fileSize(var13) + " big after compression");
                     var10.sendMessage(var3 + " Took " + var11 / (double)var4 + "ms to read");
                     var10.sendMessage(var3 + " Took " + var9 / (double)var4 + "ms to write");
                  } catch (Throwable var22) {
                     var22.printStackTrace();
                  }

               });
               var9.shutdown();
            } else {
               Iris.info(String.valueOf(C.RED) + "Engine is null!");
            }

         }
      }
   }

   private CountingDataInputStream createInput(File file, String algorithm) {
      FileInputStream var3 = new FileInputStream(var1);
      byte var5 = -1;
      switch(var2.hashCode()) {
      case 3189082:
         if (var2.equals("gzip")) {
            var5 = 0;
         }
         break;
      case 3336380:
         if (var2.equals("lz4b")) {
            var5 = 2;
         }
         break;
      case 3336384:
         if (var2.equals("lz4f")) {
            var5 = 1;
         }
      }

      Object var10000;
      switch(var5) {
      case 0:
         var10000 = new GZIPInputStream(var3);
         break;
      case 1:
         var10000 = new LZ4FrameInputStream(var3);
         break;
      case 2:
         var10000 = new LZ4BlockInputStream(var3);
         break;
      default:
         throw new IllegalStateException("Unexpected value: " + var2);
      }

      return CountingDataInputStream.wrap((InputStream)var10000);
   }

   private DataOutputStream createOutput(File file, String algorithm) {
      FileOutputStream var3 = new FileOutputStream(var1);
      DataOutputStream var10000 = new DataOutputStream;
      byte var5 = -1;
      switch(var2.hashCode()) {
      case 3189082:
         if (var2.equals("gzip")) {
            var5 = 0;
         }
         break;
      case 3336380:
         if (var2.equals("lz4b")) {
            var5 = 2;
         }
         break;
      case 3336384:
         if (var2.equals("lz4f")) {
            var5 = 1;
         }
      }

      Object var10002;
      switch(var5) {
      case 0:
         var10002 = new GZIPOutputStream(var3);
         break;
      case 1:
         var10002 = new LZ4FrameOutputStream(var3);
         break;
      case 2:
         var10002 = new LZ4BlockOutputStream(var3);
         break;
      default:
         throw new IllegalStateException("Unexpected value: " + var2);
      }

      var10000.<init>((OutputStream)var10002);
      return var10000;
   }
}
