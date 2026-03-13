package com.volmit.iris.core.tools;

import com.volmit.iris.Iris;
import com.volmit.iris.core.pregenerator.PregenTask;
import com.volmit.iris.engine.framework.Engine;
import com.volmit.iris.engine.object.IrisDimension;
import com.volmit.iris.util.collection.KList;
import com.volmit.iris.util.collection.KMap;
import com.volmit.iris.util.exceptions.IrisException;
import com.volmit.iris.util.format.Form;
import com.volmit.iris.util.io.IO;
import com.volmit.iris.util.scheduling.J;
import com.volmit.iris.util.scheduling.PrecisionStopwatch;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.Clock;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Iterator;
import org.bukkit.Bukkit;
import org.bukkit.World;

public class IrisPackBenchmarking {
   private static final ThreadLocal<IrisPackBenchmarking> instance = new ThreadLocal();
   private final PrecisionStopwatch stopwatch = new PrecisionStopwatch();
   private final IrisDimension dimension;
   private final int radius;
   private final boolean gui;

   public IrisPackBenchmarking(IrisDimension dimension, int radius, boolean gui) {
      this.dimension = var1;
      this.radius = var2;
      this.gui = var3;
      this.runBenchmark();
   }

   public static IrisPackBenchmarking getInstance() {
      return (IrisPackBenchmarking)instance.get();
   }

   private void runBenchmark() {
      Thread.ofVirtual().name("PackBenchmarking").start(() -> {
         Iris.info("Setting up benchmark environment ");
         IO.delete(new File(Bukkit.getWorldContainer(), "benchmark"));
         this.createBenchmark();

         while(!IrisToolbelt.isIrisWorld(Bukkit.getWorld("benchmark"))) {
            J.sleep(1000L);
            Iris.debug("Iris PackBenchmark: Waiting...");
         }

         Iris.info("Starting Benchmark!");
         this.stopwatch.begin();
         this.startBenchmark();
      });
   }

   public void finishedBenchmark(KList<Integer> cps) {
      try {
         String var2 = Form.duration((long)this.stopwatch.getMilliseconds());
         Engine var3 = IrisToolbelt.access(Bukkit.getWorld("benchmark")).getEngine();
         Iris.info("-----------------");
         Iris.info("Results:");
         Iris.info("- Total time: " + var2);
         Iris.info("- Average CPS: " + this.calculateAverage(var1));
         Iris.info("  - Median CPS: " + this.calculateMedian(var1));
         Iris.info("  - Highest CPS: " + this.findHighest(var1));
         Iris.info("  - Lowest CPS: " + this.findLowest(var1));
         Iris.info("-----------------");
         Iris.info("Creating a report..");
         Iris var10000 = Iris.instance;
         String[] var10001 = new String[]{"packbenchmarks", null};
         String var10004 = this.dimension.getName();
         var10001[1] = var10004 + " " + LocalDateTime.now(Clock.systemDefaultZone()).toString().replace(':', '-') + ".txt";
         File var4 = var10000.getDataFile(var10001);
         KMap var5 = var3.getMetrics().pull();

         try {
            FileWriter var6 = new FileWriter(var4);

            try {
               var6.write("-----------------\n");
               var6.write("Results:\n");
               var6.write("Dimension: " + this.dimension.getName() + "\n");
               var6.write("- Date of Benchmark: " + String.valueOf(LocalDateTime.now(Clock.systemDefaultZone())) + "\n");
               var6.write("\n");
               var6.write("Metrics");
               Iterator var7 = var5.k().iterator();

               while(true) {
                  if (!var7.hasNext()) {
                     var6.write("- " + String.valueOf(var5));
                     var6.write("Benchmark: " + String.valueOf(LocalDateTime.now(Clock.systemDefaultZone())) + "\n");
                     var6.write("- Total time: " + var2 + "\n");
                     double var15 = this.calculateAverage(var1);
                     var6.write("- Average CPS: " + var15 + "\n");
                     var15 = this.calculateMedian(var1);
                     var6.write("  - Median CPS: " + var15 + "\n");
                     int var16 = this.findHighest(var1);
                     var6.write("  - Highest CPS: " + var16 + "\n");
                     var16 = this.findLowest(var1);
                     var6.write("  - Lowest CPS: " + var16 + "\n");
                     var6.write("-----------------\n");
                     Iris.info("Finished generating a report!");
                     break;
                  }

                  String var8 = (String)var7.next();
                  double var9 = (Double)var5.get(var8);
                  var6.write("- " + var8 + ": " + var9);
               }
            } catch (Throwable var12) {
               try {
                  var6.close();
               } catch (Throwable var11) {
                  var12.addSuppressed(var11);
               }

               throw var12;
            }

            var6.close();
         } catch (IOException var13) {
            Iris.error("An error occurred writing to the file.");
            var13.printStackTrace();
         }

         J.s(() -> {
            World var0 = Bukkit.getWorld("benchmark");
            if (var0 != null) {
               IrisToolbelt.evacuate(var0);
               Bukkit.unloadWorld(var0, true);
            }
         });
         this.stopwatch.end();
      } catch (Exception var14) {
         Iris.error("Something has gone wrong!");
         var14.printStackTrace();
      }

   }

   private void createBenchmark() {
      try {
         IrisToolbelt.createWorld().dimension(this.dimension.getLoadKey()).name("benchmark").seed(1337L).studio(false).benchmark(true).create();
      } catch (IrisException var2) {
         throw new RuntimeException(var2);
      }
   }

   private void startBenchmark() {
      try {
         instance.set(this);
         IrisToolbelt.pregenerate(PregenTask.builder().gui(this.gui).radiusX(this.radius).radiusZ(this.radius).build(), Bukkit.getWorld("benchmark"));
      } finally {
         instance.remove();
      }

   }

   private double calculateAverage(KList<Integer> list) {
      double var2 = 0.0D;

      int var5;
      for(Iterator var4 = var1.iterator(); var4.hasNext(); var2 += (double)var5) {
         var5 = (Integer)var4.next();
      }

      return var2 / (double)var1.size();
   }

   private double calculateMedian(KList<Integer> list) {
      Collections.sort(var1);
      int var2 = var1.size() / 2;
      return var1.size() % 2 == 1 ? (double)(Integer)var1.get(var2) : (double)((Integer)var1.get(var2 - 1) + (Integer)var1.get(var2)) / 2.0D;
   }

   private int findLowest(KList<Integer> list) {
      return (Integer)Collections.min(var1);
   }

   private int findHighest(KList<Integer> list) {
      return (Integer)Collections.max(var1);
   }
}
