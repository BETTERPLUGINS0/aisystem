package com.volmit.iris.util.uniques;

import com.volmit.iris.engine.object.NoiseStyle;
import com.volmit.iris.util.collection.KList;
import com.volmit.iris.util.collection.KMap;
import com.volmit.iris.util.format.Form;
import com.volmit.iris.util.function.NoiseProvider;
import com.volmit.iris.util.interpolation.InterpolationMethod;
import com.volmit.iris.util.interpolation.IrisInterpolation;
import com.volmit.iris.util.math.RNG;
import com.volmit.iris.util.noise.CNG;
import com.volmit.iris.util.parallel.BurstExecutor;
import com.volmit.iris.util.scheduling.ChronoLatch;
import com.volmit.iris.util.scheduling.J;
import com.volmit.iris.util.scheduling.PrecisionStopwatch;
import com.volmit.iris.util.stream.ProceduralStream;
import com.volmit.iris.util.uniques.features.UFInterpolator;
import com.volmit.iris.util.uniques.features.UFNOOP;
import com.volmit.iris.util.uniques.features.UFWarpedBackground;
import com.volmit.iris.util.uniques.features.UFWarpedCircle;
import com.volmit.iris.util.uniques.features.UFWarpedDisc;
import com.volmit.iris.util.uniques.features.UFWarpedDots;
import com.volmit.iris.util.uniques.features.UFWarpedLines;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.PrintStream;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

public class UniqueRenderer {
   static final List<UFeature> backgrounds = List.of(new UFWarpedBackground());
   static final List<UFeature> interpolators = List.of(new UFInterpolator(), new UFNOOP());
   static final List<UFeature> features = List.of(new UFWarpedLines(), new UFWarpedDisc(), new UFWarpedDots(), new UFWarpedCircle());
   static UniqueRenderer renderer;
   private final String seed;
   private final ProceduralStream<RNG> spatialSeed;
   private final int width;
   private final int height;
   private final KMap<String, String> writing = new KMap();
   private final ExecutorService executor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors() * 2);
   private final KList<NoiseStyle> sortedStyles = new KList();
   private final KList<InterpolationMethod> sortedInterpolators = new KList();
   int cores = Runtime.getRuntime().availableProcessors();

   public UniqueRenderer(String seed, int width, int height) {
      renderer = this;
      this.computeNoiseStyles(3000.0D, 2.0D);
      this.computeInterpolationMethods(3000.0D, 2.0D);
      this.seed = var1;
      this.width = var2;
      this.height = var3;
      this.spatialSeed = NoiseStyle.FRACTAL_WATER.stream(new RNG(var1)).convert((var1x) -> {
         return new RNG(Math.round((double)var1.hashCode() + var1x * 9.34321234E8D));
      });
      (new Thread(() -> {
         while(true) {
            J.sleep(5000L);
            if (!this.writing.isEmpty()) {
               System.out.println(Form.repeat("\n", 60));
               PrintStream var10000 = System.out;
               String var10001 = Form.memSize(Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory(), 2);
               var10000.println(var10001 + " of " + Form.memSize(Runtime.getRuntime().totalMemory(), 2));
               KMap var1 = this.writing.copy();

               String var3;
               String var4;
               String var5;
               for(Iterator var2 = this.writing.k().sort().iterator(); var2.hasNext(); System.out.println(var4 + " " + var3 + " => " + var5)) {
                  var3 = (String)var2.next();
                  var4 = "";
                  var5 = (String)this.writing.get(var3);
                  if (var5.contains("%")) {
                     String var6 = var5.split("\\Q%\\E")[0];

                     try {
                        var4 = this.drawProgress(Double.valueOf((double)Integer.parseInt(var6.substring(var6.length() - 2))) / 100.0D, 30);
                     } catch (Throwable var12) {
                        try {
                           var4 = this.drawProgress(Double.valueOf((double)Integer.parseInt(var6.substring(var6.length() - 1))) / 100.0D, 30);
                        } catch (Throwable var11) {
                           try {
                              var4 = this.drawProgress(Double.valueOf((double)Integer.parseInt(var6.substring(var6.length() - 3))) / 100.0D, 30);
                           } catch (Throwable var10) {
                           }
                        }
                     }
                  }
               }
            }
         }
      })).start();
   }

   public UMeta renderFrameBuffer(long id, double t) {
      UMeta var5 = new UMeta();
      var5.setId(var1);
      var5.setTime(var3);
      RNG var6 = (RNG)this.spatialSeed.get((double)var1, (double)var1 + (double)(var1 * var1) % ((double)var1 / 3.0D));
      RNG var7 = (RNG)this.spatialSeed.get((double)var1, (double)(-var1) + (double)(var1 * var1) % ((double)var1 / 4.0D));
      BufferedImage var8 = new BufferedImage(this.width, this.height, 2);
      BufferedImage var9 = new BufferedImage(this.width, this.height, 2);
      UBufferedImage var10 = new UBufferedImage(var8);
      UBufferedImage var11 = new UBufferedImage(var9);
      ChronoLatch var12 = new ChronoLatch(250L);
      UFeature var13 = (UFeature)var6.pick(backgrounds);
      UFeature var14 = (UFeature)var6.pick(interpolators);
      UFeature var15 = (UFeature)var6.pick(features);
      UFeature var16 = (UFeature)var6.pick(interpolators);
      UFeatureMeta var17 = new UFeatureMeta();
      UFeatureMeta var18 = new UFeatureMeta();
      UFeatureMeta var19 = new UFeatureMeta();
      UFeatureMeta var20 = new UFeatureMeta();
      var13.render(var10, var7, var3, (var7x) -> {
         if (var12.flip()) {
            KMap var10000 = this.writing;
            String var10001 = "#" + var1 + ":" + var3;
            String var10002 = Form.pc(var7x / 4.0D);
            var10000.put(var10001, var10002 + " [" + var13.getClass().getSimpleName() + " " + Form.pc(var7x) + "]");
         }

      }, var17);
      var17.setFeature(var13.getClass().getSimpleName());
      var5.registerFeature("background", var17);
      var14.render(var10, var6, var3, (var7x) -> {
         if (var12.flip()) {
            KMap var10000 = this.writing;
            String var10001 = "#" + var1 + ":" + var3;
            String var10002 = Form.pc(0.25D + var7x / 4.0D);
            var10000.put(var10001, var10002 + " [" + var14.getClass().getSimpleName() + " " + Form.pc(var7x) + "]");
         }

      }, var19);
      var19.setFeature(var14.getClass().getSimpleName());
      var5.registerFeature("backgroundInterpolator", var19);
      var15.render(var11, var6, var3, (var7x) -> {
         if (var12.flip()) {
            KMap var10000 = this.writing;
            String var10001 = "#" + var1 + ":" + var3;
            String var10002 = Form.pc(0.5D + var7x / 4.0D);
            var10000.put(var10001, var10002 + " [" + var15.getClass().getSimpleName() + " " + Form.pc(var7x) + "]");
         }

      }, var18);
      var18.setFeature(var15.getClass().getSimpleName());
      var5.registerFeature("foreground", var18);
      this.overlay(var11, var9, var10);
      var16.render(var10, var6, var3, (var7x) -> {
         if (var12.flip()) {
            KMap var10000 = this.writing;
            String var10001 = "#" + var1 + ":" + var3;
            String var10002 = Form.pc(0.75D + var7x / 4.0D);
            var10000.put(var10001, var10002 + " [" + var14.getClass().getSimpleName() + " " + Form.pc(var7x) + "]");
         }

      }, var20);
      var20.setFeature(var16.getClass().getSimpleName());
      var5.registerFeature("foregroundInterpolator", var20);
      this.overlay(var11, var9, var10);
      var5.setImage(var8);
      this.writing.remove("#" + var1 + ":" + var3);
      return var5;
   }

   private void overlay(UImage layer, BufferedImage layerBuf, UImage onto) {
      for(int var4 = 0; var4 < var3.getWidth(); ++var4) {
         for(int var5 = 0; var5 < var3.getHeight(); ++var5) {
            if (var2.getRGB(var4, var5) != 0) {
               var3.set(var4, var5, var1.get(var4, var5));
            }
         }
      }

   }

   private String drawProgress(double progress, int len) {
      int var5 = (int)Math.round(var1 * (double)var3);
      int var4 = var3 - var5;
      String var10000 = Form.repeat("=", var5);
      return "[" + var10000 + Form.repeat(" ", var4) + "]";
   }

   private void computeNoiseStyles(double time, double scope) {
      KList var5 = new KList(NoiseStyle.values());
      var5.remove(NoiseStyle.FLAT);
      KMap var6 = new KMap();
      double var9 = var1 / (double)var5.size();
      System.out.println("Running Noise Style Benchmark for " + Form.duration(var1, 0) + ".");
      PrintStream var10000 = System.out;
      int var10001 = var5.size();
      var10000.println("Benchmarking " + var10001 + " + Noise Styles for " + Form.duration(var9, 1) + " each.");
      System.out.println();
      Iterator var11 = var5.iterator();

      NoiseStyle var12;
      while(var11.hasNext()) {
         var12 = (NoiseStyle)var11.next();
         int var13 = 0;
         CNG var14 = var12.create(new RNG("renderspeedtest"));
         PrecisionStopwatch var15 = PrecisionStopwatch.start();

         for(double var16 = 0.0D; var15.getMilliseconds() < var9; ++var13) {
            var14.noise(var16, -var16 * 2.0D);
            var16 += 0.1D;
            var16 *= 1.25D;
         }

         var6.put(var12, var13);
      }

      var11 = var6.sortKNumber().iterator();

      String var22;
      while(var11.hasNext()) {
         var12 = (NoiseStyle)var11.next();
         var10000 = System.out;
         var22 = Form.capitalizeWords(var12.name().toLowerCase(Locale.ROOT).replaceAll("\\Q_\\E", " "));
         var10000.println(var22 + " => " + Form.f((Integer)var6.get(var12)));
      }

      System.out.println();
      int var18 = (int)Math.max(1.0D, var3 * (double)var6.size());
      var10000 = System.out;
      var22 = Form.pc(var3);
      var10000.println("Choosing the fastest " + var22 + " styles (" + var18 + ")");
      Iterator var19 = var6.sortKNumber().reverse().iterator();

      while(var19.hasNext()) {
         NoiseStyle var20 = (NoiseStyle)var19.next();
         if (var18-- <= 0) {
            break;
         }

         this.sortedStyles.add((Object)var20);
         var10000 = System.out;
         var22 = var20.name().toLowerCase(Locale.ROOT);
         var10000.println("- " + Form.capitalizeWords(var22.replaceAll("\\Q_\\E", " ")));
      }

   }

   private void computeInterpolationMethods(double time, double scope) {
      KList var5 = new KList(InterpolationMethod.values());
      var5.remove(InterpolationMethod.NONE);
      KMap var6 = new KMap();
      double var9 = var1 / (double)var5.size();
      System.out.println("Running Interpolation Method Benchmark for " + Form.duration(var1, 0) + ".");
      PrintStream var10000 = System.out;
      int var10001 = var5.size();
      var10000.println("Benchmarking " + var10001 + " + Interpolation Methods for " + Form.duration(var9, 1) + " each.");
      System.out.println();
      RNG var11 = new RNG("renderspeedtestinterpolation");
      CNG var12 = NoiseStyle.SIMPLEX.create(var11);
      NoiseProvider var13 = (var1x, var3x) -> {
         return var12.noise(var1x, var3x);
      };
      Iterator var14 = var5.iterator();

      InterpolationMethod var15;
      while(var14.hasNext()) {
         var15 = (InterpolationMethod)var14.next();
         int var16 = 0;
         PrecisionStopwatch var17 = PrecisionStopwatch.start();

         for(double var18 = 0.0D; var17.getMilliseconds() < var9; ++var16) {
            IrisInterpolation.getNoise(var15, (int)var18, (int)(-var18 * 2.225D), var11.d(4.0D, 64.0D), var13);
            var12.noise(var18, -var18 * 2.0D);
            ++var18;
            var18 *= 1.25D;
         }

         var6.put(var15, var16);
      }

      var14 = var6.sortKNumber().iterator();

      String var24;
      while(var14.hasNext()) {
         var15 = (InterpolationMethod)var14.next();
         var10000 = System.out;
         var24 = Form.capitalizeWords(var15.name().toLowerCase(Locale.ROOT).replaceAll("\\Q_\\E", " "));
         var10000.println(var24 + " => " + Form.f((Integer)var6.get(var15)));
      }

      System.out.println();
      int var20 = (int)Math.max(1.0D, var3 * (double)var6.size());
      var10000 = System.out;
      var24 = Form.pc(var3);
      var10000.println("Choosing the fastest " + var24 + " interpolators (" + var20 + ")");
      Iterator var21 = var6.sortKNumber().reverse().iterator();

      while(var21.hasNext()) {
         InterpolationMethod var22 = (InterpolationMethod)var21.next();
         if (var20-- <= 0) {
            break;
         }

         this.sortedInterpolators.add((Object)var22);
         var10000 = System.out;
         var24 = var22.name().toLowerCase(Locale.ROOT);
         var10000.println("- " + Form.capitalizeWords(var24.replaceAll("\\Q_\\E", " ")));
      }

   }

   public void writeCollectionFrames(File folder, int fromId, int toId) {
      var1.mkdirs();
      BurstExecutor var4 = new BurstExecutor(this.executor, Math.min(var3 - var2, 1000));
      var4.setMulticore(true);
      AtomicInteger var5 = new AtomicInteger(0);
      int var6 = var3 - var2;

      for(int var7 = var2; var7 <= var3; ++var7) {
         var4.queue(() -> {
            this.writing.put("!#[" + var2 + "-" + var3 + "] Collection", var5.get() + " of " + var6 + " (" + Form.pc((double)var5.get() / (double)var6, 0) + ")");
            this.writeFrame(new File(var1, var7 + ".png"), (long)var7, 0.0D);
            var5.incrementAndGet();
            this.writing.put("!#[" + var2 + "-" + var3 + "] Collection", var5.get() + " of " + var6 + " (" + Form.pc((double)var5.get() / (double)var6, 0) + ")");
         });
      }

      var4.complete();
      this.writing.remove("!#[" + var2 + "-" + var3 + "] Collection");
   }

   public void writeFrame(File destination, long id, double t) {
      try {
         this.renderFrameBuffer(var2, var4).export(var1);
      } catch (Throwable var7) {
         var7.printStackTrace();
      }

   }

   public void report(String s) {
      System.out.println(var1);
   }

   public KList<NoiseStyle> getStyles() {
      return this.sortedStyles;
   }

   public List<InterpolationMethod> getInterpolators() {
      return this.sortedInterpolators;
   }
}
