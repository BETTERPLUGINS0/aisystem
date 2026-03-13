package com.volmit.iris.core.service;

import com.volmit.iris.Iris;
import com.volmit.iris.core.commands.CommandIris;
import com.volmit.iris.core.tools.IrisToolbelt;
import com.volmit.iris.engine.data.cache.AtomicCache;
import com.volmit.iris.util.collection.KMap;
import com.volmit.iris.util.decree.DecreeContext;
import com.volmit.iris.util.decree.DecreeSystem;
import com.volmit.iris.util.decree.virtual.VirtualDecreeCommand;
import com.volmit.iris.util.format.C;
import com.volmit.iris.util.plugin.IrisService;
import com.volmit.iris.util.plugin.VolmitSender;
import com.volmit.iris.util.scheduling.J;
import java.util.Locale;
import java.util.concurrent.CompletableFuture;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.server.ServerCommandEvent;

public class CommandSVC implements IrisService, DecreeSystem {
   private final KMap<String, CompletableFuture<String>> futures = new KMap();
   private final transient AtomicCache<VirtualDecreeCommand> commandCache = new AtomicCache();
   private CompletableFuture<String> consoleFuture = null;

   public void onEnable() {
      Iris.instance.getCommand("iris").setExecutor(this);
      J.a(() -> {
         DecreeContext.touch(Iris.getSender());

         try {
            this.getRoot().cacheAll();
         } finally {
            DecreeContext.remove();
         }

      });
   }

   public void onDisable() {
   }

   @EventHandler
   public void on(PlayerCommandPreprocessEvent e) {
      String var2 = var1.getMessage().startsWith("/") ? var1.getMessage().substring(1) : var1.getMessage();
      if (var2.startsWith("irisdecree ")) {
         String[] var3 = var2.split("\\Q \\E");
         CompletableFuture var4 = (CompletableFuture)this.futures.get(var3[1]);
         if (var4 != null) {
            var4.complete(var3[2]);
            var1.setCancelled(true);
            return;
         }
      }

      if ((var2.startsWith("locate ") || var2.startsWith("locatebiome ")) && IrisToolbelt.isIrisWorld(var1.getPlayer().getWorld())) {
         (new VolmitSender(var1.getPlayer())).sendMessage(String.valueOf(C.RED) + "Locating biomes & objects is disabled in Iris Worlds. Use /iris studio goto <biome>");
         var1.setCancelled(true);
      }

   }

   @EventHandler
   public void on(ServerCommandEvent e) {
      if (this.consoleFuture != null && !this.consoleFuture.isCancelled() && !this.consoleFuture.isDone() && !var1.getCommand().contains(" ")) {
         String var2 = var1.getCommand().trim().toLowerCase(Locale.ROOT);
         this.consoleFuture.complete(var2);
         var1.setCancelled(true);
      }

   }

   public VirtualDecreeCommand getRoot() {
      return (VirtualDecreeCommand)this.commandCache.aquireNastyPrint(() -> {
         return VirtualDecreeCommand.createRoot(new CommandIris());
      });
   }

   public void post(String password, CompletableFuture<String> future) {
      this.futures.put(var1, var2);
   }

   public void postConsole(CompletableFuture<String> future) {
      this.consoleFuture = var1;
   }
}
