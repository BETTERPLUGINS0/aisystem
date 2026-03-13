package com.volmit.iris.engine.framework;

import com.volmit.iris.Iris;
import com.volmit.iris.core.events.IrisEngineHotloadEvent;
import com.volmit.iris.util.format.C;
import com.volmit.iris.util.plugin.VolmitSender;
import java.util.Iterator;
import java.util.concurrent.atomic.AtomicBoolean;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.world.ChunkLoadEvent;
import org.bukkit.event.world.ChunkUnloadEvent;
import org.bukkit.event.world.WorldSaveEvent;
import org.bukkit.event.world.WorldUnloadEvent;

public abstract class EngineAssignedWorldManager extends EngineAssignedComponent implements EngineWorldManager, Listener {
   private final int taskId;
   protected AtomicBoolean ignoreTP = new AtomicBoolean(false);

   public EngineAssignedWorldManager() {
      super((Engine)null, (String)null);
      this.taskId = -1;
   }

   public EngineAssignedWorldManager(Engine engine) {
      super(var1, "World");
      Iris.instance.registerListener(this);
      this.taskId = Bukkit.getScheduler().scheduleSyncRepeatingTask(Iris.instance, this::onTick, 0L, 0L);
   }

   @EventHandler
   public void on(IrisEngineHotloadEvent e) {
      Iterator var2 = var1.getEngine().getWorld().getPlayers().iterator();

      while(var2.hasNext()) {
         Player var3 = (Player)var2.next();
         var3.playSound(var3.getLocation(), Sound.BLOCK_AMETHYST_BLOCK_BREAK, 1.0F, 1.8F);
         VolmitSender var4 = new VolmitSender(var3);
         var4.sendTitle(String.valueOf(C.IRIS) + "Engine " + String.valueOf(C.AQUA) + "<font:minecraft:uniform>Hotloaded", 70, 60, 410);
      }

   }

   @EventHandler
   public void on(WorldSaveEvent e) {
      if (var1.getWorld().equals(this.getTarget().getWorld().realWorld())) {
         this.getEngine().save();
      }

   }

   @EventHandler
   public void on(WorldUnloadEvent e) {
      if (var1.getWorld().equals(this.getTarget().getWorld().realWorld())) {
         this.getEngine().close();
      }

   }

   @EventHandler
   public void on(BlockBreakEvent e) {
      if (var1.getPlayer().getWorld().equals(this.getTarget().getWorld().realWorld())) {
         this.onBlockBreak(var1);
      }

   }

   @EventHandler
   public void on(BlockPlaceEvent e) {
      if (var1.getPlayer().getWorld().equals(this.getTarget().getWorld().realWorld())) {
         this.onBlockPlace(var1);
      }

   }

   @EventHandler
   public void on(ChunkLoadEvent e) {
      if (var1.getChunk().getWorld().equals(this.getTarget().getWorld().realWorld())) {
         this.onChunkLoad(var1.getChunk(), var1.isNewChunk());
      }

   }

   @EventHandler
   public void on(ChunkUnloadEvent e) {
      if (var1.getChunk().getWorld().equals(this.getTarget().getWorld().realWorld())) {
         this.onChunkUnload(var1.getChunk());
      }

   }

   public void close() {
      super.close();
      Iris.instance.unregisterListener(this);
      Bukkit.getScheduler().cancelTask(this.taskId);
   }
}
