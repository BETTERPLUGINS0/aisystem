package ac.grim.grimac.checks.impl.sprint;

import ac.grim.grimac.checks.Check;
import ac.grim.grimac.checks.CheckData;
import ac.grim.grimac.checks.type.PostPredictionCheck;
import ac.grim.grimac.player.GrimPlayer;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.event.PacketReceiveEvent;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.packettype.PacketType;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.player.ClientVersion;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientEntityAction;
import ac.grim.grimac.utils.anticheat.update.PredictionComplete;

@CheckData(
   name = "SprintE",
   description = "Sprinting while colliding with a wall",
   setback = 5.0D,
   experimental = true
)
public class SprintE extends Check implements PostPredictionCheck {
   private boolean startedSprintingThisTick;
   private boolean wasHardHorizontalCollision;

   public SprintE(GrimPlayer player) {
      super(player);
   }

   public void onPacketReceive(PacketReceiveEvent event) {
      if (event.getPacketType() == PacketType.Play.Client.ENTITY_ACTION && (new WrapperPlayClientEntityAction(event)).getAction() == WrapperPlayClientEntityAction.Action.START_SPRINTING) {
         this.startedSprintingThisTick = true;
      }

   }

   public void onPredictionComplete(PredictionComplete predictionComplete) {
      if (predictionComplete.isChecked()) {
         if (this.wasHardHorizontalCollision && !this.startedSprintingThisTick && !this.player.uncertaintyHandler.isNearGlitchyBlock && !this.player.inVehicle() && !this.player.uncertaintyHandler.lastVehicleSwitch.hasOccurredSince(0) && (!this.player.wasTouchingWater || this.player.getClientVersion().isOlderThan(ClientVersion.V_1_13)) && this.player.wasLastPredictionCompleteChecked) {
            if (this.player.isSprinting) {
               this.flagAndAlertWithSetback();
            } else {
               this.reward();
            }
         }

         this.wasHardHorizontalCollision = this.player.horizontalCollision && !this.player.softHorizontalCollision && this.player.wasLastPredictionCompleteChecked;
         this.startedSprintingThisTick = false;
      }
   }
}
