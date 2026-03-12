package ac.grim.grimac.checks.impl.sprint;

import ac.grim.grimac.checks.Check;
import ac.grim.grimac.checks.CheckData;
import ac.grim.grimac.checks.type.PostPredictionCheck;
import ac.grim.grimac.player.GrimPlayer;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.event.PacketReceiveEvent;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.packettype.PacketType;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.potion.PotionTypes;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientEntityAction;
import ac.grim.grimac.utils.anticheat.update.PredictionComplete;

@CheckData(
   name = "SprintD",
   description = "Started sprinting while having blindness",
   setback = 5.0D,
   experimental = true
)
public class SprintD extends Check implements PostPredictionCheck {
   public boolean startedSprintingBeforeBlind = false;

   public SprintD(GrimPlayer player) {
      super(player);
   }

   public void onPacketReceive(PacketReceiveEvent event) {
      if (event.getPacketType() == PacketType.Play.Client.ENTITY_ACTION && (new WrapperPlayClientEntityAction(event)).getAction() == WrapperPlayClientEntityAction.Action.START_SPRINTING) {
         this.startedSprintingBeforeBlind = false;
      }

   }

   public void onPredictionComplete(PredictionComplete predictionComplete) {
      if (this.player.compensatedEntities.self.hasPotionEffect(PotionTypes.BLINDNESS)) {
         if (this.player.isSprinting && !this.startedSprintingBeforeBlind) {
            this.flagAndAlertWithSetback();
         } else {
            this.reward();
         }
      }

   }
}
