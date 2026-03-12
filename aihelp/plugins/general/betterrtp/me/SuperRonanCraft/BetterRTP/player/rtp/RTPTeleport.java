package me.SuperRonanCraft.BetterRTP.player.rtp;

import java.util.Arrays;
import me.SuperRonanCraft.BetterRTP.BetterRTP;
import me.SuperRonanCraft.BetterRTP.lib.paperlib.PaperLib;
import me.SuperRonanCraft.BetterRTP.player.rtp.effects.RTPEffect_Titles;
import me.SuperRonanCraft.BetterRTP.player.rtp.effects.RTPEffects;
import me.SuperRonanCraft.BetterRTP.references.PermissionNode;
import me.SuperRonanCraft.BetterRTP.references.customEvents.RTP_TeleportEvent;
import me.SuperRonanCraft.BetterRTP.references.customEvents.RTP_TeleportPostEvent;
import me.SuperRonanCraft.BetterRTP.references.customEvents.RTP_TeleportPreEvent;
import me.SuperRonanCraft.BetterRTP.references.messages.MessagesCore;
import me.SuperRonanCraft.BetterRTP.references.rtpinfo.worlds.WorldPlayer;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class RTPTeleport {
   private final RTPEffects effects = new RTPEffects();

   void load() {
      this.effects.load();
   }

   void sendPlayer(final CommandSender sendi, final Player p, Location location, final WorldPlayer wPlayer, final int attempts, final RTP_TYPE type) throws NullPointerException {
      final Location oldLoc = p.getLocation();
      this.loadingTeleport(p, sendi);

      try {
         RTP_TeleportEvent event = new RTP_TeleportEvent(p, location, wPlayer.getWorldtype());
         this.getPl().getServer().getPluginManager().callEvent(event);
         final Location loc = event.getLocation();
         PaperLib.teleportAsync(p, loc).thenRun(new BukkitRunnable() {
            public void run() {
               RTPTeleport.this.afterTeleport(p, loc, wPlayer, attempts, oldLoc, type);
               if (sendi != p) {
                  RTPTeleport.this.sendSuccessMsg(sendi, p.getName(), loc, wPlayer, false, attempts);
               }

               RTPTeleport.this.getPl().getPInfo().getRtping().remove(p);
               if (type == RTP_TYPE.JOIN && BetterRTP.getInstance().getSettings().isRtpOnFirstJoin_SetAsRespawn()) {
                  p.setBedSpawnLocation(loc, true);
               }

            }
         });
      } catch (Exception var10) {
         this.getPl().getPInfo().getRtping().remove(p);
         var10.printStackTrace();
      }

   }

   public void afterTeleport(Player p, Location loc, WorldPlayer wPlayer, int attempts, Location oldLoc, RTP_TYPE type) {
      this.effects.getSounds().playTeleport(p);
      this.effects.getParticles().display(p);
      this.effects.getPotions().giveEffects(p);
      this.effects.getTitles().showTitle(RTPEffect_Titles.RTP_TITLE_TYPE.TELEPORT, p, loc, attempts, 0);
      if (this.effects.getTitles().sendMsg(RTPEffect_Titles.RTP_TITLE_TYPE.TELEPORT)) {
         this.sendSuccessMsg(p, p.getName(), loc, wPlayer, true, attempts);
      }

      this.getPl().getServer().getPluginManager().callEvent(new RTP_TeleportPostEvent(p, loc, oldLoc, wPlayer, type));
   }

   public boolean beforeTeleportInstant(CommandSender sendi, Player p) {
      RTP_TeleportPreEvent event = new RTP_TeleportPreEvent(p);
      this.getPl().getServer().getPluginManager().callEvent(event);
      if (!event.isCancelled()) {
         this.effects.getSounds().playDelay(p);
         this.effects.getTitles().showTitle(RTPEffect_Titles.RTP_TITLE_TYPE.NODELAY, p, p.getLocation(), 0, 0);
         if (this.effects.getTitles().sendMsg(RTPEffect_Titles.RTP_TITLE_TYPE.NODELAY)) {
            MessagesCore.SUCCESS_TELEPORT.send(sendi);
         }
      }

      return event.isCancelled();
   }

   public boolean beforeTeleportDelay(Player p, int delay) {
      RTP_TeleportPreEvent event = new RTP_TeleportPreEvent(p);
      this.getPl().getServer().getPluginManager().callEvent(event);
      if (!event.isCancelled()) {
         this.effects.getSounds().playDelay(p);
         this.effects.getTitles().showTitle(RTPEffect_Titles.RTP_TITLE_TYPE.DELAY, p, p.getLocation(), 0, delay);
         if (this.effects.getTitles().sendMsg(RTPEffect_Titles.RTP_TITLE_TYPE.DELAY)) {
            MessagesCore.DELAY.send(p, (Object)delay);
         }
      }

      return event.isCancelled();
   }

   public void cancelledTeleport(Player p) {
      this.effects.getTitles().showTitle(RTPEffect_Titles.RTP_TITLE_TYPE.CANCEL, p, p.getLocation(), 0, 0);
      if (this.effects.getTitles().sendMsg(RTPEffect_Titles.RTP_TITLE_TYPE.CANCEL)) {
         MessagesCore.MOVED.send(p);
      }

   }

   private void loadingTeleport(Player p, CommandSender sendi) {
      this.effects.getTitles().showTitle(RTPEffect_Titles.RTP_TITLE_TYPE.LOADING, p, p.getLocation(), 0, 0);
      if (this.effects.getTitles().sendMsg(RTPEffect_Titles.RTP_TITLE_TYPE.LOADING) && this.sendStatusMessage()) {
         if (p == sendi) {
            MessagesCore.SUCCESS_LOADING.send(sendi);
         }

         MessagesCore.SUCCESS_LOADING.send(p);
      }

   }

   public void failedTeleport(Player p, CommandSender sendi) {
      this.effects.getTitles().showTitle(RTPEffect_Titles.RTP_TITLE_TYPE.FAILED, p, p.getLocation(), 0, 0);
      if (this.effects.getTitles().sendMsg(RTPEffect_Titles.RTP_TITLE_TYPE.FAILED)) {
         if (p == sendi) {
            MessagesCore.FAILED_NOTSAFE.send(p, (Object)BetterRTP.getInstance().getRTP().maxAttempts);
         } else {
            MessagesCore.OTHER_NOTSAFE.send(sendi, Arrays.asList(BetterRTP.getInstance().getRTP().maxAttempts, p.getName()));
         }
      }

   }

   private void sendSuccessMsg(CommandSender sendi, String player, Location loc, WorldPlayer wPlayer, boolean sameAsPlayer, int attempts) {
      if (sameAsPlayer) {
         if (wPlayer.getPrice() != 0 && !PermissionNode.BYPASS_ECONOMY.check(sendi)) {
            MessagesCore.SUCCESS_PAID.send(sendi, Arrays.asList(loc, wPlayer, attempts));
         } else {
            MessagesCore.SUCCESS_BYPASS.send(sendi, Arrays.asList(loc, attempts));
         }
      } else {
         MessagesCore.OTHER_SUCCESS.send(sendi, Arrays.asList(loc, player, attempts));
      }

   }

   private boolean sendStatusMessage() {
      return this.getPl().getSettings().isStatusMessages();
   }

   private BetterRTP getPl() {
      return BetterRTP.getInstance();
   }
}
