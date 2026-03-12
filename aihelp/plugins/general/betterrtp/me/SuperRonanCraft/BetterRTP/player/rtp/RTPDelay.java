package me.SuperRonanCraft.BetterRTP.player.rtp;

import me.SuperRonanCraft.BetterRTP.BetterRTP;
import me.SuperRonanCraft.BetterRTP.lib.folialib.wrapper.task.WrappedTask;
import me.SuperRonanCraft.BetterRTP.references.customEvents.RTP_CancelledEvent;
import me.SuperRonanCraft.BetterRTP.versions.AsyncHandler;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerMoveEvent;

class RTPDelay implements Listener {
   private WrappedTask task;
   private final boolean cancelOnMove;
   private final boolean cancelOnDamage;
   private final RTPPlayer rtp;

   RTPDelay(CommandSender sendi, RTPPlayer rtp, int delay, boolean cancelOnMove, boolean cancelOnDamage) {
      this.cancelOnMove = cancelOnMove;
      this.cancelOnDamage = cancelOnDamage;
      this.rtp = rtp;
      this.delay(sendi, delay);
   }

   private void delay(CommandSender sendi, int delay) {
      if (!this.getPl().getRTP().getTeleport().beforeTeleportDelay(this.rtp.getPlayer(), delay)) {
         this.task = AsyncHandler.syncLater(this.run(sendi, this), (long)delay * 20L);
         if (this.cancelOnMove || this.cancelOnDamage) {
            Bukkit.getPluginManager().registerEvents(this, BetterRTP.getInstance());
         }
      }

   }

   @EventHandler
   private void event(PlayerMoveEvent e) {
      if (this.cancelOnMove && e.getPlayer().equals(this.rtp.getPlayer()) && e.getTo() != null && (e.getTo().getBlockX() != e.getFrom().getBlockX() || e.getTo().getBlockY() != e.getFrom().getBlockY() || e.getTo().getBlockZ() != e.getFrom().getBlockZ())) {
         this.cancel();
      }

   }

   @EventHandler
   private void event(EntityDamageEvent e) {
      if (this.cancelOnDamage && e.getEntity() instanceof Player && e.getEntity().equals(this.rtp.getPlayer())) {
         this.cancel();
      }

   }

   private void cancel() {
      if (this.task != null) {
         this.task.cancel();
      }

      HandlerList.unregisterAll(this);
      this.getPl().getRTP().getTeleport().cancelledTeleport(this.rtp.getPlayer());
      this.getPl().getCooldowns().removeCooldown(this.rtp.getPlayer(), this.rtp.worldPlayer.getWorld());
      this.getPl().getPInfo().getRtping().remove(this.rtp.getPlayer());
      Bukkit.getServer().getPluginManager().callEvent(new RTP_CancelledEvent(this.rtp.getPlayer()));
   }

   private Runnable run(CommandSender sendi, RTPDelay cls) {
      return () -> {
         HandlerList.unregisterAll(cls);
         if (this.getPl().getPInfo().getRtping().containsKey(this.rtp.getPlayer())) {
            this.rtp.randomlyTeleport(sendi);
         }

      };
   }

   private BetterRTP getPl() {
      return BetterRTP.getInstance();
   }
}
