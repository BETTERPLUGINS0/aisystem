package com.volmit.iris.core.gui;

import com.volmit.iris.Iris;
import com.volmit.iris.core.IrisSettings;
import com.volmit.iris.core.pregenerator.IrisPregenerator;
import com.volmit.iris.core.pregenerator.PregenListener;
import com.volmit.iris.core.pregenerator.PregenTask;
import com.volmit.iris.core.pregenerator.PregeneratorMethod;
import com.volmit.iris.engine.framework.Engine;
import com.volmit.iris.util.collection.KList;
import com.volmit.iris.util.format.Form;
import com.volmit.iris.util.format.MemoryMonitor;
import com.volmit.iris.util.function.Consumer2;
import com.volmit.iris.util.mantle.Mantle;
import com.volmit.iris.util.math.M;
import com.volmit.iris.util.math.Position2;
import com.volmit.iris.util.scheduling.ChronoLatch;
import com.volmit.iris.util.scheduling.J;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.util.Iterator;
import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.locks.ReentrantLock;
import java.util.function.Consumer;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class PregeneratorJob implements PregenListener {
   private static final Color COLOR_EXISTS = parseColor("#4d7d5b");
   private static final Color COLOR_BLACK = parseColor("#4d7d5b");
   private static final Color COLOR_MANTLE = parseColor("#3c2773");
   private static final Color COLOR_GENERATING = parseColor("#66967f");
   private static final Color COLOR_NETWORK = parseColor("#a863c2");
   private static final Color COLOR_NETWORK_GENERATING = parseColor("#836b8c");
   private static final Color COLOR_GENERATED = parseColor("#65c295");
   private static final Color COLOR_CLEANED = parseColor("#34eb93");
   private static final AtomicReference<PregeneratorJob> instance = new AtomicReference();
   private final MemoryMonitor monitor;
   private final PregenTask task;
   private final boolean saving;
   private final KList<Consumer<Double>> onProgress = new KList();
   private final KList<Runnable> whenDone = new KList();
   private final IrisPregenerator pregenerator;
   private final Position2 min;
   private final Position2 max;
   private final ChronoLatch cl;
   private final Engine engine;
   private final ExecutorService service;
   private JFrame frame;
   private PregeneratorJob.PregenRenderer renderer;
   private int rgc;
   private String[] info;

   public PregeneratorJob(PregenTask task, PregeneratorMethod method, Engine engine) {
      this.cl = new ChronoLatch(TimeUnit.MINUTES.toMillis(1L));
      this.rgc = 0;
      instance.updateAndGet((var1x) -> {
         if (var1x != null) {
            var1x.pregenerator.close();
            var1x.close();
         }

         return this;
      });
      this.engine = var3;
      this.monitor = new MemoryMonitor(50);
      this.saving = false;
      this.info = new String[]{"Initializing..."};
      this.task = var1;
      this.pregenerator = new IrisPregenerator(var1, var2, this);
      this.max = new Position2(0, 0);
      this.min = new Position2(Integer.MAX_VALUE, Integer.MAX_VALUE);
      var1.iterateAllChunks((var1x, var2x) -> {
         this.min.setX(Math.min(var1x, this.min.getX()));
         this.min.setZ(Math.min(var2x, this.min.getZ()));
         this.max.setX(Math.max(var1x, this.max.getX()));
         this.max.setZ(Math.max(var2x, this.max.getZ()));
      });
      if (IrisSettings.get().getGui().isUseServerLaunchedGuis() && var1.isGui()) {
         this.open();
      }

      Thread var4 = new Thread(() -> {
         J.sleep(1000L);
         this.pregenerator.start();
      }, "Iris Pregenerator");
      var4.setPriority(1);
      var4.start();
      this.service = Executors.newVirtualThreadPerTaskExecutor();
   }

   public static boolean shutdownInstance() {
      PregeneratorJob var0 = (PregeneratorJob)instance.get();
      if (var0 == null) {
         return false;
      } else {
         IrisPregenerator var10000 = var0.pregenerator;
         Objects.requireNonNull(var10000);
         J.a(var10000::close);
         return true;
      }
   }

   public static PregeneratorJob getInstance() {
      return (PregeneratorJob)instance.get();
   }

   public static boolean pauseResume() {
      PregeneratorJob var0 = (PregeneratorJob)instance.get();
      if (var0 == null) {
         return false;
      } else {
         if (isPaused()) {
            var0.pregenerator.resume();
         } else {
            var0.pregenerator.pause();
         }

         return true;
      }
   }

   public static boolean isPaused() {
      PregeneratorJob var0 = (PregeneratorJob)instance.get();
      return var0 == null ? true : var0.paused();
   }

   private static Color parseColor(String c) {
      String var1 = (var0.startsWith("#") ? var0 : "#" + var0).trim();

      try {
         return Color.decode(var1);
      } catch (Throwable var3) {
         Iris.reportError(var3);
         Iris.error("Error Parsing 'color', (" + var0 + ")");
         return Color.RED;
      }
   }

   public Mantle getMantle() {
      return this.pregenerator.getMantle();
   }

   public PregeneratorJob onProgress(Consumer<Double> c) {
      this.onProgress.add((Object)var1);
      return this;
   }

   public PregeneratorJob whenDone(Runnable r) {
      this.whenDone.add((Object)var1);
      return this;
   }

   public void drawRegion(int x, int z, Color color) {
      J.a(() -> {
         this.task.iterateChunks(var1, var2, (var2x, var3x) -> {
            this.draw(var2x, var3x, var3);
            J.sleep(3L);
         });
      });
   }

   public void draw(int x, int z, Color color) {
      try {
         if (this.renderer != null && this.frame != null && this.frame.isVisible()) {
            this.renderer.func.accept(new Position2(var1, var2), var3);
         }
      } catch (Throwable var5) {
         Iris.error("Failed to draw pregen");
      }

   }

   public void stop() {
      J.a(() -> {
         this.pregenerator.close();
         this.close();
         instance.compareAndSet(this, (Object)null);
      });
   }

   public void close() {
      J.a(() -> {
         try {
            this.monitor.close();
            J.sleep(3000L);
            this.frame.setVisible(false);
         } catch (Throwable var2) {
            Iris.error("Error closing pregen gui");
         }

      });
   }

   public void open() {
      J.a(() -> {
         try {
            this.frame = new JFrame("Pregen View");
            this.renderer = new PregeneratorJob.PregenRenderer();
            this.frame.addKeyListener(this.renderer);
            this.renderer.l = new ReentrantLock();
            this.renderer.frame = this.frame;
            this.renderer.job = this;
            this.renderer.func = (var1, var2x) -> {
               this.renderer.l.lock();
               this.renderer.order.add((Object)(() -> {
                  this.renderer.draw(var1, var2x, this.renderer.bg);
               }));
               this.renderer.l.unlock();
            };
            this.frame.add(this.renderer);
            this.frame.setSize(1000, 1000);
            this.frame.setVisible(true);
         } catch (Throwable var2) {
            Iris.error("Error opening pregen gui");
         }

      });
   }

   public void onTick(double chunksPerSecond, double chunksPerMinute, double regionsPerMinute, double percent, long generated, long totalChunks, long chunksRemaining, long eta, long elapsed, String method, boolean cached) {
      String[] var10001 = new String[5];
      String var10004 = this.paused() ? "PAUSED" : (this.saving ? "Saving... " : "Generating");
      var10001[0] = var10004 + " " + Form.f(var9) + " of " + Form.f(var11) + " (" + Form.pc(var7, 0) + " Complete)";
      var10001[1] = "Speed: " + (var20 ? "Cached " : "") + Form.f(var1, 0) + " Chunks/s, " + Form.f(var5, 1) + " Regions/m, " + Form.f(var3, 0) + " Chunks/m";
      var10004 = Form.duration(var15, 2);
      var10001[2] = var10004 + " Remaining  (" + Form.duration(var17, 2) + " Elapsed)";
      var10001[3] = "Generation Method: " + var19;
      var10004 = Form.memSize(this.monitor.getUsedBytes(), 2);
      var10001[4] = "Memory: " + var10004 + " (" + Form.pc(this.monitor.getUsagePercent(), 0) + ") Pressure: " + Form.memSize(this.monitor.getPressure(), 0) + "/s";
      this.info = var10001;
      Iterator var21 = this.onProgress.iterator();

      while(var21.hasNext()) {
         Consumer var22 = (Consumer)var21.next();
         var22.accept(var7);
      }

   }

   public void onChunkGenerating(int x, int z) {
      this.draw(var1, var2, COLOR_GENERATING);
   }

   public void onChunkGenerated(int x, int z, boolean cached) {
      if (this.renderer != null && this.frame != null && this.frame.isVisible()) {
         this.service.submit(() -> {
            if (this.engine != null) {
               this.draw(var1, var2, this.engine.draw((double)((var1 << 4) + 8), (double)((var2 << 4) + 8)));
            } else {
               this.draw(var1, var2, COLOR_GENERATED);
            }
         });
      }
   }

   public void onRegionGenerated(int x, int z) {
      this.shouldGc();
      ++this.rgc;
   }

   private void shouldGc() {
      if (this.cl.flip() && this.rgc > 16) {
         System.gc();
      }

   }

   public void onRegionGenerating(int x, int z) {
   }

   public void onChunkCleaned(int x, int z) {
   }

   public void onRegionSkipped(int x, int z) {
   }

   public void onNetworkStarted(int x, int z) {
      this.drawRegion(var1, var2, COLOR_NETWORK);
   }

   public void onNetworkFailed(int x, int z) {
   }

   public void onNetworkReclaim(int revert) {
   }

   public void onNetworkGeneratedChunk(int x, int z) {
      this.draw(var1, var2, COLOR_NETWORK_GENERATING);
   }

   public void onNetworkDownloaded(int x, int z) {
      this.drawRegion(var1, var2, COLOR_NETWORK);
   }

   public void onClose() {
      this.close();
      instance.compareAndSet(this, (Object)null);
      this.whenDone.forEach(Runnable::run);
      this.service.shutdownNow();
   }

   public void onSaving() {
   }

   public void onChunkExistsInRegionGen(int x, int z) {
      if (this.engine != null) {
         this.draw(var1, var2, this.engine.draw((double)((var1 << 4) + 8), (double)((var2 << 4) + 8)));
      } else {
         this.draw(var1, var2, COLOR_EXISTS);
      }
   }

   private Position2 getMax() {
      return this.max;
   }

   private Position2 getMin() {
      return this.min;
   }

   private boolean paused() {
      return this.pregenerator.paused();
   }

   private String[] getProgress() {
      return this.info;
   }

   public static class PregenRenderer extends JPanel implements KeyListener {
      private static final long serialVersionUID = 2094606939770332040L;
      private final KList<Runnable> order = new KList();
      private final int res = 512;
      private final BufferedImage image = new BufferedImage(512, 512, 1);
      Graphics2D bg;
      private PregeneratorJob job;
      private ReentrantLock l;
      private Consumer2<Position2, Color> func;
      private JFrame frame;

      public void paint(int x, int z, Color c) {
         this.func.accept(new Position2(var1, var2), var3);
      }

      public void paint(Graphics gx) {
         Graphics2D var2 = (Graphics2D)var1;
         this.bg = (Graphics2D)this.image.getGraphics();
         this.l.lock();

         while(this.order.isNotEmpty()) {
            try {
               ((Runnable)this.order.pop()).run();
            } catch (Throwable var10) {
               Iris.reportError(var10);
            }
         }

         this.l.unlock();
         var2.drawImage(this.image, 0, 0, this.getParent().getWidth(), this.getParent().getHeight(), (var0, var1x, var2x, var3x, var4x, var5x) -> {
            return true;
         });
         var2.setColor(Color.WHITE);
         var2.setFont(new Font("Hevetica", 1, 13));
         String[] var3 = this.job.getProgress();
         int var4 = var2.getFontMetrics().getHeight() + 5;
         int var5 = 20;
         if (this.job.paused()) {
            var2.drawString("PAUSED", 20, var5 += var4);
            var2.drawString("Press P to Resume", 20, var5 + var4);
         } else {
            String[] var6 = var3;
            int var7 = var3.length;

            for(int var8 = 0; var8 < var7; ++var8) {
               String var9 = var6[var8];
               var2.drawString(var9, 20, var5 += var4);
            }

            var2.drawString("Press P to Pause", 20, var5 + var4);
         }

         J.sleep(IrisSettings.get().getGui().isMaximumPregenGuiFPS() ? 4L : 250L);
         this.repaint();
      }

      private void draw(Position2 p, Color c, Graphics2D bg) {
         double var4 = M.lerpInverse((double)this.job.getMin().getX(), (double)this.job.getMax().getX(), (double)var1.getX());
         double var6 = M.lerpInverse((double)this.job.getMin().getZ(), (double)this.job.getMax().getZ(), (double)var1.getZ());
         double var8 = M.lerpInverse((double)this.job.getMin().getX(), (double)this.job.getMax().getX(), (double)(var1.getX() + 1));
         double var10 = M.lerpInverse((double)this.job.getMin().getZ(), (double)this.job.getMax().getZ(), (double)(var1.getZ() + 1));
         int var12 = (int)M.lerp(0.0D, 512.0D, var4);
         int var13 = (int)M.lerp(0.0D, 512.0D, var6);
         int var14 = (int)M.lerp(0.0D, 512.0D, var8);
         int var15 = (int)M.lerp(0.0D, 512.0D, var10);
         var3.setColor(var2);
         var3.fillRect(var12, var13, var14 - var12, var15 - var13);
      }

      public void keyTyped(KeyEvent e) {
      }

      public void keyPressed(KeyEvent e) {
      }

      public void keyReleased(KeyEvent e) {
         if (var1.getKeyCode() == 80) {
            PregeneratorJob.pauseResume();
         }

      }

      public void close() {
         this.frame.setVisible(false);
      }
   }
}
