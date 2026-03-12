package ac.grim.grimac.checks.impl.packetorder;

import ac.grim.grimac.api.config.ConfigManager;
import ac.grim.grimac.checks.Check;
import ac.grim.grimac.checks.CheckData;
import ac.grim.grimac.checks.type.PostPredictionCheck;
import ac.grim.grimac.player.GrimPlayer;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.event.PacketReceiveEvent;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.packettype.PacketType;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.player.ClientVersion;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.player.GameMode;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientInteractEntity;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientPlayerDigging;
import ac.grim.grimac.utils.anticheat.update.PredictionComplete;
import ac.grim.grimac.utils.nmsutil.BlockBreakSpeed;
import java.util.ArrayDeque;
import java.util.Iterator;

@CheckData(
   name = "PacketOrderI",
   experimental = true
)
public class PacketOrderI extends Check implements PostPredictionCheck {
   private boolean exemptPlacingWhileDigging;
   private boolean setback;
   private boolean digging;
   private final ArrayDeque<String> flags = new ArrayDeque();

   public PacketOrderI(GrimPlayer player) {
      super(player);
   }

   public void onPacketReceive(PacketReceiveEvent event) {
      boolean var10000;
      String verbose;
      if (event.getPacketType() == PacketType.Play.Client.INTERACT_ENTITY) {
         if ((new WrapperPlayClientInteractEntity(event)).getAction() == WrapperPlayClientInteractEntity.InteractAction.ATTACK) {
            if (this.player.packetOrderProcessor.isRightClicking() || this.player.packetOrderProcessor.isPicking() || this.player.packetOrderProcessor.isReleasing() || this.player.packetOrderProcessor.isDigging()) {
               var10000 = this.player.packetOrderProcessor.isRightClicking();
               verbose = "type=attack, rightClicking=" + var10000 + ", picking=" + this.player.packetOrderProcessor.isPicking() + ", releasing=" + this.player.packetOrderProcessor.isReleasing() + ", digging=" + this.player.packetOrderProcessor.isDigging();
               if (!this.player.canSkipTicks()) {
                  if (this.flagAndAlert(verbose) && this.shouldModifyPackets()) {
                     event.setCancelled(true);
                     this.player.onPacketCancel();
                  }
               } else {
                  this.flags.add(verbose);
               }
            }
         } else if (this.player.packetOrderProcessor.isReleasing() || this.player.packetOrderProcessor.isDigging()) {
            var10000 = this.player.packetOrderProcessor.isReleasing();
            verbose = "type=interact, releasing=" + var10000 + ", digging=" + this.player.packetOrderProcessor.isDigging();
            if (!this.player.canSkipTicks()) {
               if (this.flagAndAlert(verbose) && this.shouldModifyPackets()) {
                  event.setCancelled(true);
                  this.player.onPacketCancel();
               }
            } else {
               this.flags.add(verbose);
            }
         }
      }

      if ((event.getPacketType() == PacketType.Play.Client.PLAYER_BLOCK_PLACEMENT || event.getPacketType() == PacketType.Play.Client.USE_ITEM) && (this.player.packetOrderProcessor.isReleasing() || this.digging)) {
         var10000 = this.player.packetOrderProcessor.isReleasing();
         verbose = "type=place/use, releasing=" + var10000 + ", digging=" + this.digging;
         if (!this.player.canSkipTicks()) {
            if (this.flagAndAlert(verbose) && this.shouldModifyPackets()) {
               event.setCancelled(true);
               this.player.onPacketCancel();
            }
         } else {
            this.flags.add(verbose);
         }
      }

      if (event.getPacketType() == PacketType.Play.Client.PLAYER_DIGGING) {
         label123: {
            WrapperPlayClientPlayerDigging packet = new WrapperPlayClientPlayerDigging(event);
            switch(packet.getAction()) {
            case RELEASE_USE_ITEM:
               if (this.player.packetOrderProcessor.isAttacking() || this.player.packetOrderProcessor.isRightClicking() || this.player.packetOrderProcessor.isPicking() || this.player.packetOrderProcessor.isDigging()) {
                  var10000 = this.player.packetOrderProcessor.isAttacking();
                  String verbose = "type=release, attacking=" + var10000 + ", rightClicking=" + this.player.packetOrderProcessor.isRightClicking() + ", picking=" + this.player.packetOrderProcessor.isPicking() + ", digging=" + this.player.packetOrderProcessor.isDigging();
                  if (!this.player.canSkipTicks()) {
                     if (this.flagAndAlert(verbose)) {
                        this.setback = true;
                     }
                  } else {
                     this.flags.add(verbose);
                     this.setback = true;
                  }
               }
               break label123;
            case START_DIGGING:
               double damage = BlockBreakSpeed.getBlockDamage(this.player, this.player.compensatedWorld.getBlock(packet.getBlockPosition()));
               if (damage >= 1.0D || damage <= 0.0D && this.player.gamemode == GameMode.CREATIVE) {
                  return;
               }
            case CANCELLED_DIGGING:
            case FINISHED_DIGGING:
               break;
            default:
               break label123;
            }

            if (this.exemptPlacingWhileDigging || this.player.getClientVersion().isOlderThanOrEquals(ClientVersion.V_1_7_10)) {
               return;
            }

            this.digging = true;
         }
      }

      if (!this.player.cameraEntity.isSelf() || this.isTickPacket(event.getPacketType())) {
         this.digging = false;
      }

   }

   public void onPredictionComplete(PredictionComplete predictionComplete) {
      if (!this.player.canSkipTicks()) {
         if (this.setback) {
            this.setbackIfAboveSetbackVL();
            this.setback = false;
         }

      } else {
         if (this.player.isTickingReliablyFor(3)) {
            Iterator var2 = this.flags.iterator();

            while(var2.hasNext()) {
               String verbose = (String)var2.next();
               if (this.flagAndAlert(verbose) && this.setback) {
                  this.setbackIfAboveSetbackVL();
                  this.setback = false;
               }
            }
         }

         this.flags.clear();
         this.setback = false;
      }
   }

   public void onReload(ConfigManager config) {
      this.exemptPlacingWhileDigging = config.getBooleanElse(this.getConfigName() + ".exempt-placing-while-digging", false);
   }
}
