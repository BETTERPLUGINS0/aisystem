package me.SuperRonanCraft.BetterRTP.player.rtp;

import java.util.concurrent.CompletableFuture;
import me.SuperRonanCraft.BetterRTP.BetterRTP;
import me.SuperRonanCraft.BetterRTP.lib.paperlib.PaperLib;
import me.SuperRonanCraft.BetterRTP.references.customEvents.RTP_FailedEvent;
import me.SuperRonanCraft.BetterRTP.references.customEvents.RTP_FindLocationEvent;
import me.SuperRonanCraft.BetterRTP.references.helpers.HelperRTP_Check;
import me.SuperRonanCraft.BetterRTP.references.rtpinfo.QueueData;
import me.SuperRonanCraft.BetterRTP.references.rtpinfo.QueueHandler;
import me.SuperRonanCraft.BetterRTP.references.rtpinfo.RandomLocation;
import me.SuperRonanCraft.BetterRTP.references.rtpinfo.worlds.WorldPlayer;
import me.SuperRonanCraft.BetterRTP.versions.AsyncHandler;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class RTPPlayer {
   private final Player player;
   private final RTP settings;
   WorldPlayer worldPlayer;
   RTP_TYPE type;
   int attempts;

   RTPPlayer(Player player, RTP settings, WorldPlayer worldPlayer, RTP_TYPE type) {
      this.player = player;
      this.settings = settings;
      this.worldPlayer = worldPlayer;
      this.type = type;
   }

   void randomlyTeleport(CommandSender sendi) {
      if (this.attempts >= this.settings.maxAttempts) {
         this.metMax(sendi, this.player);
      } else {
         RTP_FindLocationEvent event = new RTP_FindLocationEvent(this);
         Bukkit.getServer().getPluginManager().callEvent(event);
         if (event.isCancelled()) {
            this.randomlyTeleport(sendi);
            ++this.attempts;
            return;
         }

         AsyncHandler.async(() -> {
            Location loc;
            if (event.getLocation() != null) {
               loc = event.getLocation();
            } else {
               QueueData queueData = QueueHandler.getRandomAsync(this.worldPlayer);
               if (queueData != null) {
                  loc = queueData.getLocation();
               } else {
                  loc = RandomLocation.generateLocation(this.worldPlayer);
               }
            }

            ++this.attempts;
            AsyncHandler.sync(() -> {
               try {
                  CompletableFuture<Chunk> chunk = PaperLib.getChunkAtAsync(loc);
                  chunk.thenAccept((result) -> {
                     this.attempt(sendi, loc);
                  });
               } catch (IllegalStateException var4) {
                  this.attempt(sendi, loc);
               } catch (Throwable var5) {
               }

            });
         });
      }

   }

   private void attempt(CommandSender sendi, Location loc) {
      Location tpLoc = RandomLocation.getSafeLocation(this.worldPlayer.getWorldtype(), this.worldPlayer.getWorld(), loc, this.worldPlayer.getMinY(), this.worldPlayer.getMaxY(), this.worldPlayer.getBiomes());
      if (tpLoc != null && checkDepends(tpLoc)) {
         tpLoc.add(0.5D, 0.0D, 0.5D);
         if (this.getPl().getEco().charge(this.player, this.worldPlayer)) {
            if (this.worldPlayer.getPlayerInfo().isApplyCooldown() && HelperRTP_Check.applyCooldown(this.player)) {
               this.getPl().getCooldowns().add(this.player, this.worldPlayer.getWorld());
            }

            tpLoc.setYaw(this.player.getLocation().getYaw());
            tpLoc.setPitch(this.player.getLocation().getPitch());
            AsyncHandler.sync(() -> {
               this.settings.teleport.sendPlayer(sendi, this.player, tpLoc, this.worldPlayer, this.attempts, this.type);
            });
         } else {
            if (this.worldPlayer.getPlayerInfo().applyCooldown) {
               this.getPl().getCooldowns().removeCooldown(this.player, this.worldPlayer.getWorld());
            }

            this.getPl().getPInfo().getRtping().remove(this.player);
         }
      } else {
         this.randomlyTeleport(sendi);
         QueueHandler.remove(loc);
      }

   }

   private void metMax(CommandSender sendi, Player p) {
      this.settings.teleport.failedTeleport(p, sendi);
      this.getPl().getCooldowns().removeCooldown(p, this.worldPlayer.getWorld());
      this.getPl().getPInfo().getRtping().remove(p);
      Bukkit.getServer().getPluginManager().callEvent(new RTP_FailedEvent(this));
   }

   public static boolean checkDepends(Location loc) {
      return RTPPluginValidation.checkLocation(loc);
   }

   private BetterRTP getPl() {
      return BetterRTP.getInstance();
   }

   public Player getPlayer() {
      return this.player;
   }

   public WorldPlayer getWorldPlayer() {
      return this.worldPlayer;
   }

   public RTP_TYPE getType() {
      return this.type;
   }

   public int getAttempts() {
      return this.attempts;
   }
}
