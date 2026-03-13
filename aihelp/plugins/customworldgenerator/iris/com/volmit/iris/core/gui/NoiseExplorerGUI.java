package com.volmit.iris.core.gui;

import com.volmit.iris.Iris;
import com.volmit.iris.core.IrisSettings;
import com.volmit.iris.core.events.IrisEngineHotloadEvent;
import com.volmit.iris.engine.object.NoiseStyle;
import com.volmit.iris.util.collection.KList;
import com.volmit.iris.util.function.Function2;
import com.volmit.iris.util.math.M;
import com.volmit.iris.util.math.RNG;
import com.volmit.iris.util.math.RollingSequence;
import com.volmit.iris.util.noise.CNG;
import com.volmit.iris.util.parallel.BurstExecutor;
import com.volmit.iris.util.parallel.MultiBurst;
import com.volmit.iris.util.scheduling.J;
import com.volmit.iris.util.scheduling.PrecisionStopwatch;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.concurrent.locks.ReentrantLock;
import java.util.function.Supplier;
import javax.imageio.ImageIO;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;
import javax.swing.JViewport;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class NoiseExplorerGUI extends JPanel implements MouseWheelListener, Listener {
   private static final long serialVersionUID = 2094606939770332040L;
   static JComboBox<String> combo;
   static boolean hd = false;
   static double ascale = 10.0D;
   static double oxp = 0.0D;
   static double ozp = 0.0D;
   static double mxx = 0.0D;
   static double mzz = 0.0D;
   static boolean down = false;
   RollingSequence r = new RollingSequence(20);
   boolean colorMode;
   double scale;
   CNG cng;
   MultiBurst gx;
   ReentrantLock l;
   BufferedImage img;
   int w;
   int h;
   Function2<Double, Double, Double> generator;
   Supplier<Function2<Double, Double, Double>> loader;
   double ox;
   double oz;
   double mx;
   double mz;
   double lx;
   double lz;
   double t;
   double tz;

   public NoiseExplorerGUI() {
      this.colorMode = IrisSettings.get().getGui().colorMode;
      this.scale = 1.0D;
      this.cng = NoiseStyle.STATIC.create(new RNG(RNG.r.nextLong()));
      this.gx = MultiBurst.burst;
      this.l = new ReentrantLock();
      this.w = 0;
      this.h = 0;
      this.ox = 0.0D;
      this.oz = 0.0D;
      this.mx = 0.0D;
      this.mz = 0.0D;
      this.lx = Double.MAX_VALUE;
      this.lz = Double.MAX_VALUE;
      Iris.instance.registerListener(this);
      this.addMouseWheelListener(this);
      this.addMouseMotionListener(new MouseMotionListener() {
         public void mouseMoved(MouseEvent e) {
            Point var2 = var1.getPoint();
            NoiseExplorerGUI.this.lx = var2.getX();
            NoiseExplorerGUI.this.lz = var2.getY();
            NoiseExplorerGUI.this.mx = NoiseExplorerGUI.this.lx;
            NoiseExplorerGUI.this.mz = NoiseExplorerGUI.this.lz;
         }

         public void mouseDragged(MouseEvent e) {
            Point var2 = var1.getPoint();
            NoiseExplorerGUI var10000 = NoiseExplorerGUI.this;
            var10000.ox += (NoiseExplorerGUI.this.lx - var2.getX()) * NoiseExplorerGUI.this.scale;
            var10000 = NoiseExplorerGUI.this;
            var10000.oz += (NoiseExplorerGUI.this.lz - var2.getY()) * NoiseExplorerGUI.this.scale;
            NoiseExplorerGUI.this.lx = var2.getX();
            NoiseExplorerGUI.this.lz = var2.getY();
         }
      });
   }

   private static void createAndShowGUI(Supplier<Function2<Double, Double, Double>> loader, String genName) {
      JFrame var2 = new JFrame("Noise Explorer: " + var1);
      NoiseExplorerGUI var3 = new NoiseExplorerGUI();
      var2.setDefaultCloseOperation(1);
      JLayeredPane var4 = new JLayeredPane();
      var3.setSize(new Dimension(1440, 820));
      var4.add(var3, 1, 0);
      var3.loader = var0;
      var3.generator = (Function2)var0.get();
      var2.add(var4);
      File var5 = Iris.getCached("Iris Icon", "https://raw.githubusercontent.com/VolmitSoftware/Iris/master/icon.png");
      if (var5 != null) {
         try {
            var2.setIconImage(ImageIO.read(var5));
         } catch (IOException var7) {
            Iris.reportError(var7);
         }
      }

      var2.setSize(1440, 820);
      var2.setVisible(true);
   }

   private static void createAndShowGUI() {
      JFrame var0 = new JFrame("Noise Explorer");
      final NoiseExplorerGUI var1 = new NoiseExplorerGUI();
      var0.setDefaultCloseOperation(1);
      KList var2 = (new KList(NoiseStyle.values())).toStringList().sort();
      combo = new JComboBox((String[])var2.toArray(new String[0]));
      combo.setSelectedItem("STATIC");
      combo.setFocusable(false);
      combo.addActionListener((var1x) -> {
         String var2 = (String)((JComboBox)var1x.getSource()).getSelectedItem();
         NoiseStyle var3 = NoiseStyle.valueOf(var2);
         var1.cng = var3.create(RNG.r.nextParallelRNG(RNG.r.imax()));
      });
      combo.setSize(500, 30);
      JLayeredPane var3 = new JLayeredPane();
      var1.setSize(new Dimension(1440, 820));
      var3.add(var1, 1, 0);
      var3.add(combo, 2, 0);
      var0.add(var3);
      File var4 = Iris.getCached("Iris Icon", "https://raw.githubusercontent.com/VolmitSoftware/Iris/master/icon.png");
      if (var4 != null) {
         try {
            var0.setIconImage(ImageIO.read(var4));
         } catch (IOException var6) {
            Iris.reportError(var6);
         }
      }

      var0.setSize(1440, 820);
      var0.setVisible(true);
      var0.addWindowListener(new WindowAdapter() {
         public void windowClosing(WindowEvent windowEvent) {
            Iris.instance.unregisterListener(var1);
         }
      });
   }

   public static void launch(Supplier<Function2<Double, Double, Double>> gen, String genName) {
      EventQueue.invokeLater(() -> {
         createAndShowGUI(var0, var1);
      });
   }

   public static void launch() {
      EventQueue.invokeLater(NoiseExplorerGUI::createAndShowGUI);
   }

   @EventHandler
   public void on(IrisEngineHotloadEvent e) {
      if (this.generator != null) {
         this.generator = (Function2)this.loader.get();
      }

   }

   public void mouseWheelMoved(MouseWheelEvent e) {
      int var2 = var1.getWheelRotation();
      if (var1.isControlDown()) {
         this.t += 0.0025D * this.t * (double)var2;
      } else {
         this.scale += 0.044D * this.scale * (double)var2;
         this.scale = Math.max(this.scale, 1.0E-5D);
      }
   }

   public void paint(Graphics g) {
      if (this.scale < ascale) {
         ascale -= Math.abs(this.scale - ascale) * 0.16D;
      }

      if (this.scale > ascale) {
         ascale += Math.abs(ascale - this.scale) * 0.16D;
      }

      if (this.t < this.tz) {
         this.tz -= Math.abs(this.t - this.tz) * 0.29D;
      }

      if (this.t > this.tz) {
         this.tz += Math.abs(this.tz - this.t) * 0.29D;
      }

      if (this.ox < oxp) {
         oxp -= Math.abs(this.ox - oxp) * 0.16D;
      }

      if (this.ox > oxp) {
         oxp += Math.abs(oxp - this.ox) * 0.16D;
      }

      if (this.oz < ozp) {
         ozp -= Math.abs(this.oz - ozp) * 0.16D;
      }

      if (this.oz > ozp) {
         ozp += Math.abs(ozp - this.oz) * 0.16D;
      }

      if (this.mx < mxx) {
         mxx -= Math.abs(this.mx - mxx) * 0.16D;
      }

      if (this.mx > mxx) {
         mxx += Math.abs(mxx - this.mx) * 0.16D;
      }

      if (this.mz < mzz) {
         mzz -= Math.abs(this.mz - mzz) * 0.16D;
      }

      if (this.mz > mzz) {
         mzz += Math.abs(mzz - this.mz) * 0.16D;
      }

      PrecisionStopwatch var2 = PrecisionStopwatch.start();
      int var3 = hd ? 1 : ((Double)M.clip(this.r.getAverage() / 12.0D, 2.0D, 128.0D)).intValue();
      var3 = down ? var3 * 4 : var3;
      boolean var4 = true;
      if (var1 instanceof Graphics2D) {
         Graphics2D var5 = (Graphics2D)var1;
         if (this.getParent().getWidth() != this.w || this.getParent().getHeight() != this.h) {
            this.w = this.getParent().getWidth();
            this.h = this.getParent().getHeight();
            this.img = null;
         }

         if (this.img == null) {
            this.img = new BufferedImage(this.w / var3, this.h / var3, 1);
         }

         BurstExecutor var6 = this.gx.burst(this.w);

         for(int var7 = 0; var7 < this.w / var3; ++var7) {
            var6.queue(() -> {
               for(int var3x = 0; var3x < this.h / var3; ++var3x) {
                  double var4 = this.generator != null ? (Double)this.generator.apply((double)(var7 * var3) * ascale + oxp, (double)(var3x * var3) * ascale + ozp) : this.cng.noise((double)(var7 * var3) * ascale + oxp, (double)(var3x * var3) * ascale + ozp);
                  var4 = var4 > 1.0D ? 1.0D : (var4 < 0.0D ? 0.0D : var4);

                  try {
                     Color var6 = this.colorMode ? Color.getHSBColor((float)(0.6660000085830688D - var4 * 0.6660000085830688D), 1.0F, (float)(1.0D - var4 * 0.800000011920929D)) : Color.getHSBColor(0.0F, 0.0F, (float)var4);
                     int var7x = var6.getRGB();
                     this.img.setRGB(var7, var3x, var7x);
                  } catch (Throwable var8) {
                  }
               }

            });
         }

         var6.complete();
         var5.drawImage(this.img, 0, 0, this.getParent().getWidth() * var3, this.getParent().getHeight() * var3, (var0, var1x, var2x, var3x, var4x, var5x) -> {
            return true;
         });
      }

      var2.end();
      ++this.t;
      this.r.put(var2.getMilliseconds());
      if (this.isVisible()) {
         if (this.getParent().isVisible()) {
            if (this.getParent().getParent().isVisible()) {
               EventQueue.invokeLater(() -> {
                  J.sleep((long)Math.max(0.0D, 32.0D - this.r.getAverage()));
                  this.repaint();
               });
            }
         }
      }
   }

   static class HandScrollListener extends MouseAdapter {
      private static final Point pp = new Point();

      public void mouseDragged(MouseEvent e) {
         JViewport var2 = (JViewport)var1.getSource();
         JComponent var3 = (JComponent)var2.getView();
         Point var4 = var1.getPoint();
         Point var5 = var2.getViewPosition();
         var5.translate(pp.x - var4.x, pp.y - var4.y);
         var3.scrollRectToVisible(new Rectangle(var5, var2.getSize()));
         pp.setLocation(var4);
      }

      public void mousePressed(MouseEvent e) {
         pp.setLocation(var1.getPoint());
      }
   }
}
