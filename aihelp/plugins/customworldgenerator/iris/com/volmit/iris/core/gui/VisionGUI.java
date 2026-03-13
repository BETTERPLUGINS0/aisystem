package com.volmit.iris.core.gui;

import com.volmit.iris.Iris;
import com.volmit.iris.core.gui.components.IrisRenderer;
import com.volmit.iris.core.gui.components.RenderType;
import com.volmit.iris.core.tools.IrisToolbelt;
import com.volmit.iris.engine.IrisComplex;
import com.volmit.iris.engine.framework.Engine;
import com.volmit.iris.engine.object.IrisBiome;
import com.volmit.iris.engine.object.IrisRegion;
import com.volmit.iris.engine.object.IrisWorld;
import com.volmit.iris.util.collection.KList;
import com.volmit.iris.util.collection.KMap;
import com.volmit.iris.util.collection.KSet;
import com.volmit.iris.util.data.registry.Attributes;
import com.volmit.iris.util.format.Form;
import com.volmit.iris.util.math.BlockPosition;
import com.volmit.iris.util.math.M;
import com.volmit.iris.util.math.RollingSequence;
import com.volmit.iris.util.scheduling.ChronoLatch;
import com.volmit.iris.util.scheduling.J;
import com.volmit.iris.util.scheduling.O;
import com.volmit.iris.util.scheduling.PrecisionStopwatch;
import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.Locale;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.BiFunction;
import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.event.MouseInputListener;
import org.bukkit.Location;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

public class VisionGUI extends JPanel implements MouseWheelListener, KeyListener, MouseMotionListener, MouseInputListener {
   private static final long serialVersionUID = 2094606939770332040L;
   private final KList<LivingEntity> lastEntities = new KList();
   private final KMap<String, Long> notifications = new KMap();
   private final ChronoLatch centities = new ChronoLatch(1000L);
   private final RollingSequence rs = new RollingSequence(512);
   private final O<Integer> m = new O();
   private final KMap<BlockPosition, BufferedImage> positions = new KMap();
   private final KMap<BlockPosition, BufferedImage> fastpositions = new KMap();
   private final KSet<BlockPosition> working = new KSet(new BlockPosition[0]);
   private final KSet<BlockPosition> workingfast = new KSet(new BlockPosition[0]);
   double tfps = 240.0D;
   int ltc = 3;
   private RenderType currentType;
   private boolean help;
   private boolean helpIgnored;
   private boolean shift;
   private Player player;
   private boolean debug;
   private boolean control;
   private boolean eco;
   private boolean lowtile;
   private boolean follow;
   private boolean alt;
   private IrisRenderer renderer;
   private IrisWorld world;
   private double velocity;
   private int lowq;
   private double scale;
   private double mscale;
   private int w;
   private int h;
   private double lx;
   private double lz;
   private double ox;
   private double oz;
   private double hx;
   private double hz;
   private double oxp;
   private double ozp;
   private Engine engine;
   private int tid;
   private final ExecutorService e;
   private final ExecutorService eh;
   private BufferedImage texture;

   public VisionGUI(JFrame frame) {
      this.currentType = RenderType.BIOME;
      this.help = true;
      this.helpIgnored = false;
      this.shift = false;
      this.player = null;
      this.debug = false;
      this.control = false;
      this.eco = false;
      this.lowtile = false;
      this.follow = false;
      this.alt = false;
      this.velocity = 0.0D;
      this.lowq = 12;
      this.scale = 128.0D;
      this.mscale = 4.0D;
      this.w = 0;
      this.h = 0;
      this.lx = 0.0D;
      this.lz = 0.0D;
      this.ox = 0.0D;
      this.oz = 0.0D;
      this.hx = 0.0D;
      this.hz = 0.0D;
      this.oxp = 0.0D;
      this.ozp = 0.0D;
      this.tid = 0;
      this.e = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors(), (var1x) -> {
         ++this.tid;
         Thread var2 = new Thread(var1x);
         var2.setName("Iris HD Renderer " + this.tid);
         var2.setPriority(1);
         var2.setUncaughtExceptionHandler((var0, var1) -> {
            Iris.info("Exception encountered in " + var0.getName());
            var1.printStackTrace();
         });
         return var2;
      });
      this.eh = Executors.newFixedThreadPool(this.ltc, (var1x) -> {
         ++this.tid;
         Thread var2 = new Thread(var1x);
         var2.setName("Iris Renderer " + this.tid);
         var2.setPriority(5);
         var2.setUncaughtExceptionHandler((var0, var1) -> {
            Iris.info("Exception encountered in " + var0.getName());
            var1.printStackTrace();
         });
         return var2;
      });
      this.m.set(8);
      this.rs.put(1.0D);
      this.addMouseWheelListener(this);
      this.addMouseMotionListener(this);
      this.addMouseListener(this);
      var1.addKeyListener(this);
      J.a(() -> {
         J.sleep(10000L);
         if (!this.helpIgnored && this.help) {
            this.help = false;
         }

      });
      var1.addWindowListener(new WindowAdapter() {
         public void windowClosing(WindowEvent windowEvent) {
            VisionGUI.this.e.shutdown();
            VisionGUI.this.eh.shutdown();
         }
      });
   }

   private static void createAndShowGUI(Engine r, int s, IrisWorld world) {
      JFrame var3 = new JFrame("Vision");
      VisionGUI var4 = new VisionGUI(var3);
      var4.world = var2;
      var4.engine = var0;
      var4.renderer = new IrisRenderer(var0);
      var3.add(var4);
      var3.setSize(1440, 820);
      var3.setVisible(true);
      File var5 = Iris.getCached("Iris Icon", "https://raw.githubusercontent.com/VolmitSoftware/Iris/master/icon.png");
      if (var5 != null) {
         try {
            var4.texture = ImageIO.read(var5);
            var3.setIconImage(ImageIO.read(var5));
         } catch (IOException var7) {
            Iris.reportError(var7);
         }
      }

   }

   public static void launch(Engine g, int i) {
      J.a(() -> {
         createAndShowGUI(var0, var1, var0.getWorld());
      });
   }

   public boolean updateEngine() {
      if (this.engine.isClosed() && this.world.hasRealWorld()) {
         try {
            this.engine = IrisToolbelt.access(this.world.realWorld()).getEngine();
            return !this.engine.isClosed();
         } catch (Throwable var2) {
         }
      }

      return false;
   }

   public void mouseMoved(MouseEvent e) {
      Point var2 = var1.getPoint();
      this.lx = var2.getX();
      this.lz = var2.getY();
   }

   public void mouseDragged(MouseEvent e) {
      Point var2 = var1.getPoint();
      this.ox += (this.lx - var2.getX()) * this.scale;
      this.oz += (this.lz - var2.getY()) * this.scale;
      this.lx = var2.getX();
      this.lz = var2.getY();
   }

   public int getColor(double wx, double wz) {
      BiFunction var5 = (var0, var1x) -> {
         return Color.black.getRGB();
      };
      switch(this.currentType) {
      case BIOME:
      case DECORATOR_LOAD:
      case OBJECT_LOAD:
      case LAYER_LOAD:
         var5 = (var1x, var2) -> {
            return ((IrisBiome)this.engine.getComplex().getTrueBiomeStream().get(var1x, var2)).getColor(this.engine, this.currentType).getRGB();
         };
         break;
      case BIOME_LAND:
         var5 = (var1x, var2) -> {
            return ((IrisBiome)this.engine.getComplex().getLandBiomeStream().get(var1x, var2)).getColor(this.engine, this.currentType).getRGB();
         };
         break;
      case BIOME_SEA:
         var5 = (var1x, var2) -> {
            return ((IrisBiome)this.engine.getComplex().getSeaBiomeStream().get(var1x, var2)).getColor(this.engine, this.currentType).getRGB();
         };
         break;
      case REGION:
         var5 = (var1x, var2) -> {
            return ((IrisRegion)this.engine.getComplex().getRegionStream().get(var1x, var2)).getColor(this.engine.getComplex(), this.currentType).getRGB();
         };
         break;
      case CAVE_LAND:
         var5 = (var1x, var2) -> {
            return ((IrisBiome)this.engine.getComplex().getCaveBiomeStream().get(var1x, var2)).getColor(this.engine, this.currentType).getRGB();
         };
         break;
      case HEIGHT:
         var5 = (var1x, var2) -> {
            return Color.getHSBColor(((Double)this.engine.getComplex().getHeightStream().get(var1x, var2)).floatValue(), 100.0F, 100.0F).getRGB();
         };
      }

      return (Integer)var5.apply(var1, var3);
   }

   public void notify(String s) {
      this.notifications.put(var1, M.ms() + 2500L);
   }

   public void keyTyped(KeyEvent e) {
   }

   public void keyPressed(KeyEvent e) {
      if (var1.getKeyCode() == 16) {
         this.shift = true;
      }

      if (var1.getKeyCode() == 17) {
         this.control = true;
      }

      if (var1.getKeyCode() == 59) {
         this.debug = true;
      }

      if (var1.getKeyCode() == 47) {
         this.help = true;
         this.helpIgnored = true;
      }

      if (var1.getKeyCode() == 18) {
         this.alt = true;
      }

   }

   public void keyReleased(KeyEvent e) {
      if (var1.getKeyCode() == 59) {
         this.debug = false;
      }

      if (var1.getKeyCode() == 16) {
         this.shift = false;
      }

      if (var1.getKeyCode() == 17) {
         this.control = false;
      }

      if (var1.getKeyCode() == 47) {
         this.help = false;
         this.helpIgnored = true;
      }

      if (var1.getKeyCode() == 18) {
         this.alt = false;
      }

      if (var1.getKeyCode() == 70) {
         this.follow = !this.follow;
         if (this.player != null && this.follow) {
            this.notify("Following " + this.player.getName() + ". Press F to disable");
         } else if (this.follow) {
            this.notify("Can't follow, no one is in the world");
            this.follow = false;
         } else {
            this.notify("Follow Off");
         }

      } else if (var1.getKeyCode() == 82) {
         this.dump();
         this.notify("Refreshing Chunks");
      } else if (var1.getKeyCode() == 80) {
         this.lowtile = !this.lowtile;
         this.dump();
         this.notify("Rendering " + (this.lowtile ? "Low" : "High") + " Quality Tiles");
      } else if (var1.getKeyCode() == 69) {
         this.eco = !this.eco;
         this.dump();
         this.notify("Using " + (this.eco ? "60" : "Uncapped") + " FPS Limit");
      } else if (var1.getKeyCode() == 61) {
         this.mscale += 0.044D * this.mscale * -3.0D;
         this.mscale = Math.max(this.mscale, 1.0E-5D);
         this.dump();
      } else if (var1.getKeyCode() == 45) {
         this.mscale += 0.044D * this.mscale * 3.0D;
         this.mscale = Math.max(this.mscale, 1.0E-5D);
         this.dump();
      } else if (var1.getKeyCode() == 92) {
         this.mscale = 1.0D;
         this.dump();
         this.notify("Zoom Reset");
      } else {
         int var2 = this.currentType.ordinal();
         RenderType[] var3 = RenderType.values();
         int var4 = var3.length;

         String var10001;
         for(int var5 = 0; var5 < var4; ++var5) {
            RenderType var6 = var3[var5];
            if (var1.getKeyChar() == String.valueOf(var6.ordinal() + 1).charAt(0) && var6.ordinal() != var2) {
               this.currentType = var6;
               this.dump();
               var10001 = this.currentType.name().toLowerCase();
               this.notify("Rendering " + Form.capitalizeWords(var10001.replaceAll("\\Q_\\E", " ")));
               return;
            }
         }

         if (var1.getKeyCode() == 77) {
            this.currentType = RenderType.values()[(var2 + 1) % RenderType.values().length];
            var10001 = this.currentType.name().toLowerCase();
            this.notify("Rendering " + Form.capitalizeWords(var10001.replaceAll("\\Q_\\E", " ")));
            this.dump();
         }

      }
   }

   private void dump() {
      this.positions.clear();
      this.fastpositions.clear();
   }

   public BufferedImage getTile(KSet<BlockPosition> fg, int div, int x, int z, O<Integer> m) {
      BlockPosition var6 = new BlockPosition((int)this.mscale, Math.floorDiv(var3, var2), Math.floorDiv(var4, var2));
      var1.add(var6);
      if (this.positions.containsKey(var6)) {
         return (BufferedImage)this.positions.get(var6);
      } else {
         double var7;
         double var9;
         if (this.fastpositions.containsKey(var6)) {
            if (!this.working.contains(var6) && this.working.size() < 9) {
               var5.set((Integer)var5.get() - 1);
               if ((Integer)var5.get() >= 0 && this.velocity < 50.0D) {
                  this.working.add(var6);
                  var7 = this.mscale;
                  var9 = this.scale;
                  this.e.submit(() -> {
                     PrecisionStopwatch var9x = PrecisionStopwatch.start();
                     BufferedImage var10 = this.renderer.render((double)var3 * this.mscale, (double)var4 * this.mscale, (double)var2 * this.mscale, var2 / (this.lowtile ? 3 : 1), this.currentType);
                     this.rs.put(var9x.getMilliseconds());
                     this.working.remove(var6);
                     if (var7 == this.mscale && var9 == this.scale) {
                        this.positions.put(var6, var10);
                     }

                  });
               }
            }

            return (BufferedImage)this.fastpositions.get(var6);
         } else if (!this.workingfast.contains(var6) && this.workingfast.size() <= Runtime.getRuntime().availableProcessors()) {
            this.workingfast.add(var6);
            var7 = this.mscale;
            var9 = this.scale;
            this.eh.submit(() -> {
               PrecisionStopwatch var9x = PrecisionStopwatch.start();
               BufferedImage var10 = this.renderer.render((double)var3 * this.mscale, (double)var4 * this.mscale, (double)var2 * this.mscale, var2 / this.lowq, this.currentType);
               this.rs.put(var9x.getMilliseconds());
               this.workingfast.remove(var6);
               if (var7 == this.mscale && var9 == this.scale) {
                  this.fastpositions.put(var6, var10);
               }

            });
            return null;
         } else {
            return null;
         }
      }
   }

   private double getWorldX(double screenX) {
      return this.mscale * var1 + this.oxp / this.scale * this.mscale;
   }

   private double getWorldZ(double screenZ) {
      return this.mscale * var1 + this.ozp / this.scale * this.mscale;
   }

   private double getScreenX(double x) {
      return var1 / this.mscale - this.oxp / this.scale;
   }

   private double getScreenZ(double z) {
      return var1 / this.mscale - this.ozp / this.scale;
   }

   public void paint(Graphics gx) {
      if (this.engine.isClosed()) {
         EventQueue.invokeLater(() -> {
            try {
               this.setVisible(false);
            } catch (Throwable var2) {
            }

         });
      } else {
         if (this.updateEngine()) {
            this.dump();
         }

         if (this.ox < this.oxp) {
            this.velocity = Math.abs(this.ox - this.oxp) * 0.36D;
            this.oxp -= this.velocity;
         }

         if (this.ox > this.oxp) {
            this.velocity = Math.abs(this.oxp - this.ox) * 0.36D;
            this.oxp += this.velocity;
         }

         if (this.oz < this.ozp) {
            this.velocity = Math.abs(this.oz - this.ozp) * 0.36D;
            this.ozp -= this.velocity;
         }

         if (this.oz > this.ozp) {
            this.velocity = Math.abs(this.ozp - this.oz) * 0.36D;
            this.ozp += this.velocity;
         }

         if (this.lx < this.hx) {
            this.hx -= Math.abs(this.lx - this.hx) * 0.36D;
         }

         if (this.lx > this.hx) {
            this.hx += Math.abs(this.hx - this.lx) * 0.36D;
         }

         if (this.lz < this.hz) {
            this.hz -= Math.abs(this.lz - this.hz) * 0.36D;
         }

         if (this.lz > this.hz) {
            this.hz += Math.abs(this.hz - this.lz) * 0.36D;
         }

         if (Math.abs(this.lx - this.hx) < 0.5D) {
            this.hx = this.lx;
         }

         if (Math.abs(this.lz - this.hz) < 0.5D) {
            this.hz = this.lz;
         }

         if (this.centities.flip()) {
            J.s(() -> {
               synchronized(this.lastEntities) {
                  this.lastEntities.clear();
                  this.lastEntities.addAll(this.world.getEntitiesByClass(LivingEntity.class));
               }
            });
         }

         this.lowq = Math.max(Math.min((int)M.lerp(8.0D, 28.0D, this.velocity / 1000.0D), 28), 8);
         PrecisionStopwatch var2 = PrecisionStopwatch.start();
         Graphics2D var3 = (Graphics2D)var1;
         this.w = this.getWidth();
         this.h = this.getHeight();
         double var4 = this.scale;
         this.scale = (double)this.w / 12.0D;
         if (this.scale != var4) {
            this.positions.clear();
         }

         KSet var6 = new KSet(new BlockPosition[0]);
         int var7 = (int)this.scale;
         var3.setColor(Color.white);
         var3.clearRect(0, 0, this.w, this.h);
         double var8 = this.oxp / this.scale;
         double var10 = this.ozp / this.scale;
         this.m.set(3);

         for(int var12 = 0; var12 < Math.max(this.w, this.h); var12 += var7) {
            for(int var13 = -var7; var13 < this.w + var7; var13 += var7) {
               for(int var14 = -var7; var14 < this.h + var7; var14 += var7) {
                  int var15 = var13 - this.w / 2;
                  int var16 = var14 - this.h / 2;
                  if (var15 * var15 + var16 * var16 <= var12 * var12) {
                     int var17 = (int)(Math.floor((var8 + (double)var13) / (double)var7) * (double)var7);
                     int var18 = (int)(Math.floor((var10 + (double)var14) / (double)var7) * (double)var7);
                     BufferedImage var19 = this.getTile(var6, var7, var17, var18, this.m);
                     if (var19 != null) {
                        int var20 = Math.floorMod((int)Math.floor(var8), var7);
                        int var21 = Math.floorMod((int)Math.floor(var10), var7);
                        var3.drawImage(var19, var13 - var20, var14 - var21, var7, var7, (var0, var1x, var2x, var3x, var4x, var5) -> {
                           return true;
                        });
                     }
                  }
               }
            }
         }

         var2.end();
         Iterator var22 = this.positions.k().iterator();

         while(var22.hasNext()) {
            BlockPosition var23 = (BlockPosition)var22.next();
            if (!var6.contains(var23)) {
               this.positions.remove(var23);
            }
         }

         this.hanleFollow();
         this.renderOverlays(var3);
         if (this.isVisible()) {
            if (this.getParent().isVisible()) {
               if (this.getParent().getParent().isVisible()) {
                  J.a(() -> {
                     J.sleep(this.eco ? 15L : 1L);
                     this.repaint();
                  });
               }
            }
         }
      }
   }

   private void hanleFollow() {
      if (this.follow && this.player != null) {
         this.animateTo(this.player.getLocation().getX(), this.player.getLocation().getZ());
      }

   }

   private void renderOverlays(Graphics2D g) {
      this.renderPlayer(var1);
      if (this.help) {
         this.renderOverlayHelp(var1);
      } else if (this.debug) {
         this.renderOverlayDebug(var1);
      }

      this.renderOverlayLegend(var1);
      this.renderHoverOverlay(var1, this.shift);
      if (!this.notifications.isEmpty()) {
         this.renderNotification(var1);
      }

   }

   private void renderOverlayLegend(Graphics2D g) {
      KList var2 = new KList();
      var2.add((Object)("Zoom: " + Form.pc(this.mscale, 0)));
      String var10001 = Form.f((int)this.mscale * this.w);
      var2.add((Object)("Blocks: " + var10001 + " by " + Form.f((int)this.mscale * this.h)));
      var2.add((Object)("BPP: " + Form.f(this.mscale, 1)));
      var10001 = this.currentType.name().toLowerCase();
      var2.add((Object)("Render Mode: " + Form.capitalizeWords(var10001.replaceAll("\\Q_\\E", " "))));
      this.drawCardBR(var1, var2);
   }

   private void renderNotification(Graphics2D g) {
      this.drawCardCB(var1, this.notifications.k());
      Iterator var2 = this.notifications.k().iterator();

      while(var2.hasNext()) {
         String var3 = (String)var2.next();
         if (M.ms() > (Long)this.notifications.get(var3)) {
            this.notifications.remove(var3);
         }
      }

   }

   private void renderPlayer(Graphics2D g) {
      Player var2 = null;
      Iterator var3 = this.world.getPlayers().iterator();

      while(var3.hasNext()) {
         Player var4 = (Player)var3.next();
         var2 = var4;
         this.renderPosition(var1, var4.getLocation().getX(), var4.getLocation().getZ());
      }

      synchronized(this.lastEntities) {
         double var13 = Double.MAX_VALUE;
         LivingEntity var6 = null;
         Iterator var7 = this.lastEntities.iterator();

         while(var7.hasNext()) {
            LivingEntity var8 = (LivingEntity)var7.next();
            if (!(var8 instanceof Player)) {
               this.renderMobPosition(var1, var8, var8.getLocation().getX(), var8.getLocation().getZ());
               if (this.shift) {
                  double var9 = var8.getLocation().distanceSquared(new Location(var8.getWorld(), this.getWorldX(this.hx), var8.getLocation().getY(), this.getWorldZ(this.hz)));
                  if (var9 < var13) {
                     var13 = var9;
                     var6 = var8;
                  }
               }
            }
         }

         if (var6 != null && this.shift) {
            var1.setColor(Color.red);
            var1.fillRoundRect((int)this.getScreenX(var6.getLocation().getX()) - 10, (int)this.getScreenZ(var6.getLocation().getZ()) - 10, 20, 20, 20, 20);
            KList var14 = new KList();
            String var10001 = Form.capitalizeWords(var6.getType().name().toLowerCase(Locale.ROOT).replaceAll("\\Q_\\E", " "));
            var14.add((Object)(var10001 + var6.getEntityId()));
            int var15 = var6.getLocation().getBlockX();
            var14.add((Object)("Pos: " + var15 + ", " + var6.getLocation().getBlockY() + ", " + var6.getLocation().getBlockZ()));
            var14.add((Object)("UUID: " + String.valueOf(var6.getUniqueId())));
            double var16 = var6.getHealth();
            var14.add((Object)("HP: " + var16 + " / " + var6.getAttribute(Attributes.MAX_HEALTH).getValue()));
            this.drawCardTR(var1, var14);
         }
      }

      this.player = var2;
   }

   private void animateTo(double wx, double wz) {
      double var5 = this.getWorldX((double)(this.getWidth() / 2));
      double var7 = this.getWorldZ((double)(this.getHeight() / 2));
      this.ox += (var1 - var5) / this.mscale * this.scale;
      this.oz += (var3 - var7) / this.mscale * this.scale;
   }

   private void renderPosition(Graphics2D g, double x, double z) {
      if (this.texture != null) {
         var1.drawImage(this.texture, (int)this.getScreenX(var2), (int)this.getScreenZ(var4), 66, 66, (var0, var1x, var2x, var3, var4x, var5) -> {
            return true;
         });
      } else {
         var1.setColor(Color.darkGray);
         var1.fillRoundRect((int)this.getScreenX(var2) - 15, (int)this.getScreenZ(var4) - 15, 30, 30, 15, 15);
         var1.setColor(Color.cyan.darker().darker());
         var1.fillRoundRect((int)this.getScreenX(var2) - 10, (int)this.getScreenZ(var4) - 10, 20, 20, 10, 10);
      }

   }

   private void renderMobPosition(Graphics2D g, LivingEntity e, double x, double z) {
      var1.setColor(Color.red.darker().darker());
      var1.fillRoundRect((int)this.getScreenX(var3) - 2, (int)this.getScreenZ(var5) - 2, 4, 4, 4, 4);
   }

   private void renderHoverOverlay(Graphics2D g, boolean detailed) {
      IrisBiome var3 = (IrisBiome)this.engine.getComplex().getTrueBiomeStream().get(this.getWorldX(this.hx), this.getWorldZ(this.hz));
      IrisRegion var4 = (IrisRegion)this.engine.getComplex().getRegionStream().get(this.getWorldX(this.hx), this.getWorldZ(this.hz));
      KList var5 = new KList();
      var5.add((Object)("Biome: " + var3.getName()));
      String var10001 = var4.getName();
      var5.add((Object)("Region: " + var10001 + "(" + var4.getLoadKey() + ")"));
      int var6 = (int)this.getWorldX(this.hx);
      var5.add((Object)("Block " + var6 + ", " + (int)this.getWorldZ(this.hz)));
      if (var2) {
         var6 = (int)this.getWorldX(this.hx) >> 4;
         var5.add((Object)("Chunk " + var6 + ", " + ((int)this.getWorldZ(this.hz) >> 4)));
         var6 = (int)this.getWorldX(this.hx) >> 4 >> 5;
         var5.add((Object)("Region " + var6 + ", " + ((int)this.getWorldZ(this.hz) >> 4 >> 5)));
         var5.add((Object)("Key: " + var3.getLoadKey()));
         var5.add((Object)("File: " + String.valueOf(var3.getLoadFile())));
      }

      this.drawCardAt((float)this.hx, (float)this.hz, 0.0D, 0.0D, var1, var5);
   }

   private void renderOverlayDebug(Graphics2D g) {
      KList var2 = new KList();
      var2.add((Object)("Velocity: " + (int)this.velocity));
      String var10001 = Form.f((int)this.getWorldX((double)(this.getWidth() / 2)));
      var2.add((Object)("Center Pos: " + var10001 + ", " + Form.f((int)this.getWorldZ((double)(this.getHeight() / 2)))));
      this.drawCardBL(var1, var2);
   }

   private void renderOverlayHelp(Graphics2D g) {
      KList var2 = new KList();
      var2.add((Object)"/ to show this help screen");
      var2.add((Object)"R to repaint the screen");
      var2.add((Object)"F to follow first player");
      var2.add((Object)"+/- to Change Zoom");
      var2.add((Object)"\\ to reset zoom to 1");
      var2.add((Object)"M to cycle render modes");
      var2.add((Object)"P to toggle Tile Quality Mode");
      var2.add((Object)"E to toggle Eco FPS Mode");
      int var3 = 0;
      RenderType[] var4 = RenderType.values();
      int var5 = var4.length;

      for(int var6 = 0; var6 < var5; ++var6) {
         RenderType var7 = var4[var6];
         ++var3;
         var2.add((Object)(var3 + " to view " + Form.capitalizeWords(var7.name().toLowerCase().replaceAll("\\Q_\\E", " "))));
      }

      var2.add((Object)"Shift for additional biome details (at cursor)");
      var2.add((Object)"CTRL + Click to teleport to location");
      var2.add((Object)"ALT + Click to open biome in VSCode");
      this.drawCardTL(var1, var2);
   }

   private void drawCardTL(Graphics2D g, KList<String> text) {
      this.drawCardAt(0.0F, 0.0F, 0.0D, 0.0D, var1, var2);
   }

   private void drawCardBR(Graphics2D g, KList<String> text) {
      this.drawCardAt((float)this.getWidth(), (float)this.getHeight(), 1.0D, 1.0D, var1, var2);
   }

   private void drawCardBL(Graphics2D g, KList<String> text) {
      this.drawCardAt(0.0F, (float)this.getHeight(), 0.0D, 1.0D, var1, var2);
   }

   private void drawCardTR(Graphics2D g, KList<String> text) {
      this.drawCardAt((float)this.getWidth(), 0.0F, 1.0D, 0.0D, var1, var2);
   }

   private void open() {
      IrisComplex var1 = this.engine.getComplex();
      File var2 = null;
      switch(this.currentType) {
      case BIOME:
      case DECORATOR_LOAD:
      case OBJECT_LOAD:
      case LAYER_LOAD:
      case HEIGHT:
         var2 = ((IrisBiome)var1.getTrueBiomeStream().get(this.getWorldX(this.hx), this.getWorldZ(this.hz))).openInVSCode();
         break;
      case BIOME_LAND:
         var2 = ((IrisBiome)var1.getLandBiomeStream().get(this.getWorldX(this.hx), this.getWorldZ(this.hz))).openInVSCode();
         break;
      case BIOME_SEA:
         var2 = ((IrisBiome)var1.getSeaBiomeStream().get(this.getWorldX(this.hx), this.getWorldZ(this.hz))).openInVSCode();
         break;
      case REGION:
         var2 = ((IrisRegion)var1.getRegionStream().get(this.getWorldX(this.hx), this.getWorldZ(this.hz))).openInVSCode();
         break;
      case CAVE_LAND:
         var2 = ((IrisBiome)var1.getCaveBiomeStream().get(this.getWorldX(this.hx), this.getWorldZ(this.hz))).openInVSCode();
      }

      this.notify("Opening " + var2.getPath() + " in VSCode");
   }

   private void teleport() {
      J.s(() -> {
         if (this.player != null) {
            int var1 = (int)this.getWorldX(this.hx);
            int var2 = (int)this.getWorldZ(this.hz);
            int var3 = (Integer)this.engine.getComplex().getRoundedHeighteightStream().get((double)var1, (double)var2);
            this.player.teleport(new Location(this.player.getWorld(), (double)var1, (double)var3, (double)var2));
            this.notify("Teleporting to " + var1 + ", " + var3 + ", " + var2);
         } else {
            this.notify("No player in world, can't teleport.");
         }

      });
   }

   private void drawCardCB(Graphics2D g, KList<String> text) {
      this.drawCardAt((float)(this.getWidth() / 2), (float)this.getHeight(), 0.5D, 1.0D, var1, var2);
   }

   private void drawCardCT(Graphics2D g, KList<String> text) {
      this.drawCardAt((float)(this.getWidth() / 2), 0.0F, 0.5D, 0.0D, var1, var2);
   }

   private void drawCardAt(float x, float y, double pushX, double pushZ, Graphics2D g, KList<String> text) {
      var7.setFont(new Font("Hevetica", 1, 16));
      int var9 = 0;
      int var10 = 0;

      String var12;
      for(Iterator var11 = var8.iterator(); var11.hasNext(); var10 = Math.max(var10, var7.getFontMetrics().stringWidth(var12))) {
         var12 = (String)var11.next();
         var9 += var7.getFontMetrics().getHeight();
      }

      var10 += 28;
      var9 += 14;
      int var16 = (int)((double)(var10 + 26) * var3);
      int var17 = (int)((double)(var9 + 26) * var5);
      var7.setColor(Color.darkGray);
      var7.fillRect((int)var1 + 7 + 2 - var16, (int)var2 + 12 + 2 - var17, var10 + 7, var9);
      var7.setColor(Color.gray);
      var7.fillRect((int)var1 + 7 + 1 - var16, (int)var2 + 12 + 1 - var17, var10 + 7, var9);
      var7.setColor(Color.white);
      var7.fillRect((int)var1 + 7 - var16, (int)var2 + 12 - var17, var10 + 7, var9);
      var7.setColor(Color.black);
      int var13 = 0;
      Iterator var14 = var8.iterator();

      while(var14.hasNext()) {
         String var15 = (String)var14.next();
         float var10002 = var1 + 14.0F - (float)var16;
         float var10003 = var2 + 14.0F - (float)var17;
         ++var13;
         var7.drawString(var15, var10002, var10003 + (float)(var13 * var7.getFontMetrics().getHeight()));
      }

   }

   public void mouseWheelMoved(MouseWheelEvent e) {
      int var2 = var1.getWheelRotation();
      if (!var1.isControlDown()) {
         double var3 = this.mscale;
         double var5 = var3 + 0.25D * var3 * (double)var2;
         var5 = Math.max(var5, 1.0E-5D);
         if (var5 != var3) {
            this.positions.clear();
            this.fastpositions.clear();
            Point var7 = var1.getPoint();
            double var8 = var7.getX();
            double var10 = var7.getY();
            double var12 = this.scale * (var3 / var5 * (var8 + this.oxp / this.scale) - var8);
            double var14 = this.scale * (var3 / var5 * (var10 + this.ozp / this.scale) - var10);
            this.mscale = var5;
            this.oxp = var12;
            this.ozp = var14;
            this.ox = this.oxp;
            this.oz = this.ozp;
         }
      }
   }

   public void mouseClicked(MouseEvent e) {
      if (this.control) {
         this.teleport();
      } else if (this.alt) {
         this.open();
      }

   }

   public void mousePressed(MouseEvent e) {
   }

   public void mouseReleased(MouseEvent e) {
   }

   public void mouseEntered(MouseEvent e) {
   }

   public void mouseExited(MouseEvent e) {
   }
}
