package ac.grim.grimac.checks.impl.badpackets;

import ac.grim.grimac.checks.Check;
import ac.grim.grimac.checks.CheckData;
import ac.grim.grimac.checks.type.PacketCheck;
import ac.grim.grimac.player.GrimPlayer;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.event.PacketReceiveEvent;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.attribute.Attributes;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.entity.type.EntityTypes;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.packettype.PacketType;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.player.ClientVersion;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientInteractEntity;
import ac.grim.grimac.utils.data.packetentity.PacketEntity;

@CheckData(
   name = "BadPacketsT"
)
public class BadPacketsT extends Check implements PacketCheck {
   private final double maxHorizontalDisplacement;
   private final double minVerticalDisplacement;
   private final double maxVerticalDisplacement;

   public BadPacketsT(GrimPlayer player) {
      super(player);
      double expansion = player.getClientVersion().isOlderThan(ClientVersion.V_1_9) ? 0.1D : 0.0D;
      this.maxHorizontalDisplacement = 0.3001D + expansion;
      this.minVerticalDisplacement = -1.0E-4D - expansion;
      this.maxVerticalDisplacement = 1.8001D + expansion;
   }

   public void onPacketReceive(PacketReceiveEvent event) {
      if (event.getPacketType().equals(PacketType.Play.Client.INTERACT_ENTITY)) {
         WrapperPlayClientInteractEntity wrapper = new WrapperPlayClientInteractEntity(event);
         wrapper.getTarget().ifPresent((targetVector) -> {
            PacketEntity packetEntity = this.player.compensatedEntities.getEntity(wrapper.getEntityId());
            if (packetEntity != null) {
               if (EntityTypes.PLAYER.equals(packetEntity.type)) {
                  float scale = (float)packetEntity.getAttributeValue(Attributes.SCALE);
                  if (!((double)targetVector.y > this.minVerticalDisplacement * (double)scale) || !((double)targetVector.y < this.maxVerticalDisplacement * (double)scale) || !((double)Math.abs(targetVector.x) < this.maxHorizontalDisplacement * (double)scale) || !((double)Math.abs(targetVector.z) < this.maxHorizontalDisplacement * (double)scale)) {
                     String verbose = String.format("%.5f/%.5f/%.5f", targetVector.x, targetVector.y, targetVector.z);
                     this.flagAndAlert(verbose);
                  }
               }
            }
         });
      }

   }
}
