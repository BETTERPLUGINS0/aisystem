package com.volmit.iris.core.service;

import com.volmit.iris.Iris;
import com.volmit.iris.core.IrisSettings;
import com.volmit.iris.core.loader.IrisData;
import com.volmit.iris.core.tools.IrisToolbelt;
import com.volmit.iris.engine.framework.Engine;
import com.volmit.iris.engine.platform.PlatformChunkGenerator;
import com.volmit.iris.util.board.BoardManager;
import com.volmit.iris.util.board.BoardProvider;
import com.volmit.iris.util.board.BoardSettings;
import com.volmit.iris.util.board.ScoreDirection;
import com.volmit.iris.util.collection.KMap;
import com.volmit.iris.util.format.C;
import com.volmit.iris.util.format.Form;
import com.volmit.iris.util.plugin.IrisService;
import com.volmit.iris.util.scheduling.J;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import lombok.Generated;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class BoardSVC implements IrisService, BoardProvider {
   private final KMap<Player, BoardSVC.PlayerBoard> boards = new KMap();
   private ScheduledExecutorService executor;
   private BoardManager manager;

   public void onEnable() {
      this.executor = Executors.newScheduledThreadPool(0, Thread.ofVirtual().factory());
      this.manager = new BoardManager(Iris.instance, BoardSettings.builder().boardProvider(this).scoreDirection(ScoreDirection.DOWN).build());
   }

   public void onDisable() {
      this.executor.shutdownNow();
      this.manager.onDisable();
      this.boards.clear();
   }

   @EventHandler
   public void on(PlayerChangedWorldEvent e) {
      J.s(() -> {
         this.updatePlayer(var1.getPlayer());
      });
   }

   @EventHandler
   public void on(PlayerJoinEvent e) {
      J.s(() -> {
         this.updatePlayer(var1.getPlayer());
      });
   }

   @EventHandler
   public void on(PlayerQuitEvent e) {
      this.remove(var1.getPlayer());
   }

   public void updatePlayer(Player p) {
      if (IrisToolbelt.isIrisStudioWorld(var1.getWorld())) {
         this.manager.remove(var1);
         this.manager.setup(var1);
      } else {
         this.remove(var1);
      }

   }

   private void remove(Player player) {
      this.manager.remove(var1);
      BoardSVC.PlayerBoard var2 = (BoardSVC.PlayerBoard)this.boards.remove(var1);
      if (var2 != null) {
         var2.task.cancel(true);
      }

   }

   public String getTitle(Player player) {
      return String.valueOf(C.GREEN) + "Iris";
   }

   public List<String> getLines(Player player) {
      return ((BoardSVC.PlayerBoard)this.boards.computeIfAbsent(var1, (var1x) -> {
         return new BoardSVC.PlayerBoard(this, var1x);
      })).lines;
   }

   public class PlayerBoard {
      private final Player player;
      private final ScheduledFuture<?> task;
      private volatile List<String> lines;

      public PlayerBoard(final BoardSVC this$0, Player player) {
         this.player = var2;
         this.lines = new ArrayList();
         this.task = var1.executor.scheduleAtFixedRate(this::tick, 0L, 1L, TimeUnit.SECONDS);
      }

      private void tick() {
         if (((StudioSVC)Iris.service(StudioSVC.class)).isProjectOpen()) {
            this.update();
         }
      }

      public void update() {
         World var1 = this.player.getWorld();
         Location var2 = this.player.getLocation();
         PlatformChunkGenerator var3 = IrisToolbelt.access(var1);
         if (var3 != null) {
            Engine var4 = var3.getEngine();
            if (var4 != null) {
               int var5 = var2.getBlockX();
               int var6 = var2.getBlockY() - var1.getMinHeight();
               int var7 = var2.getBlockZ();
               ArrayList var8 = new ArrayList(this.lines.size());
               var8.add("&7&m                   ");
               String var10001 = String.valueOf(C.GREEN);
               var8.add(var10001 + "Speed" + String.valueOf(C.GRAY) + ":  " + Form.f(var4.getGeneratedPerSecond(), 0) + "/s " + Form.duration(1000.0D / var4.getGeneratedPerSecond(), 0));
               var10001 = String.valueOf(C.AQUA);
               var8.add(var10001 + "Cache" + String.valueOf(C.GRAY) + ": " + Form.f(IrisData.cacheSize()));
               var10001 = String.valueOf(C.AQUA);
               var8.add(var10001 + "Mantle" + String.valueOf(C.GRAY) + ": " + var4.getMantle().getLoadedRegionCount());
               if (IrisSettings.get().getGeneral().debug) {
                  var10001 = String.valueOf(C.LIGHT_PURPLE);
                  var8.add(var10001 + "Carving" + String.valueOf(C.GRAY) + ": " + var4.getMantle().isCarved(var5, var6, var7));
               }

               var8.add("&7&m                   ");
               var10001 = String.valueOf(C.AQUA);
               var8.add(var10001 + "Region" + String.valueOf(C.GRAY) + ": " + var4.getRegion(var5, var7).getName());
               var10001 = String.valueOf(C.AQUA);
               var8.add(var10001 + "Biome" + String.valueOf(C.GRAY) + ":  " + var4.getBiomeOrMantle(var5, var6, var7).getName());
               var10001 = String.valueOf(C.AQUA);
               var8.add(var10001 + "Height" + String.valueOf(C.GRAY) + ": " + Math.round((float)var4.getHeight(var5, var7)));
               var10001 = String.valueOf(C.AQUA);
               var8.add(var10001 + "Slope" + String.valueOf(C.GRAY) + ":  " + Form.f((Double)var4.getComplex().getSlopeStream().get((double)var5, (double)var7), 2));
               var10001 = String.valueOf(C.AQUA);
               var8.add(var10001 + "BUD/s" + String.valueOf(C.GRAY) + ": " + Form.f(var4.getBlockUpdatesPerSecond()));
               var8.add("&7&m                   ");
               this.lines = var8;
            }
         }
      }

      @Generated
      public Player getPlayer() {
         return this.player;
      }

      @Generated
      public ScheduledFuture<?> getTask() {
         return this.task;
      }

      @Generated
      public List<String> getLines() {
         return this.lines;
      }

      @Generated
      public void setLines(final List<String> lines) {
         this.lines = var1;
      }

      @Generated
      public boolean equals(final Object o) {
         if (var1 == this) {
            return true;
         } else if (!(var1 instanceof BoardSVC.PlayerBoard)) {
            return false;
         } else {
            BoardSVC.PlayerBoard var2 = (BoardSVC.PlayerBoard)var1;
            if (!var2.canEqual(this)) {
               return false;
            } else {
               label47: {
                  Player var3 = this.getPlayer();
                  Player var4 = var2.getPlayer();
                  if (var3 == null) {
                     if (var4 == null) {
                        break label47;
                     }
                  } else if (var3.equals(var4)) {
                     break label47;
                  }

                  return false;
               }

               ScheduledFuture var5 = this.getTask();
               ScheduledFuture var6 = var2.getTask();
               if (var5 == null) {
                  if (var6 != null) {
                     return false;
                  }
               } else if (!var5.equals(var6)) {
                  return false;
               }

               List var7 = this.getLines();
               List var8 = var2.getLines();
               if (var7 == null) {
                  if (var8 != null) {
                     return false;
                  }
               } else if (!var7.equals(var8)) {
                  return false;
               }

               return true;
            }
         }
      }

      @Generated
      protected boolean canEqual(final Object other) {
         return var1 instanceof BoardSVC.PlayerBoard;
      }

      @Generated
      public int hashCode() {
         boolean var1 = true;
         byte var2 = 1;
         Player var3 = this.getPlayer();
         int var6 = var2 * 59 + (var3 == null ? 43 : var3.hashCode());
         ScheduledFuture var4 = this.getTask();
         var6 = var6 * 59 + (var4 == null ? 43 : var4.hashCode());
         List var5 = this.getLines();
         var6 = var6 * 59 + (var5 == null ? 43 : var5.hashCode());
         return var6;
      }

      @Generated
      public String toString() {
         String var10000 = String.valueOf(this.getPlayer());
         return "BoardSVC.PlayerBoard(player=" + var10000 + ", task=" + String.valueOf(this.getTask()) + ", lines=" + String.valueOf(this.getLines()) + ")";
      }
   }
}
