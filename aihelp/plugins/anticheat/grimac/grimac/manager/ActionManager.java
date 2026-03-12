package ac.grim.grimac.manager;

import ac.grim.grimac.checks.Check;
import ac.grim.grimac.checks.type.PacketCheck;
import ac.grim.grimac.player.GrimPlayer;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.event.PacketReceiveEvent;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.packettype.PacketType;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientInteractEntity;
import lombok.Generated;

public class ActionManager extends Check implements PacketCheck {
   private boolean attacking = false;
   private long lastAttack = 0L;

   public ActionManager(GrimPlayer player) {
      super(player);
   }

   public void onPacketReceive(PacketReceiveEvent event) {
      if (event.getPacketType() == PacketType.Play.Client.INTERACT_ENTITY) {
         WrapperPlayClientInteractEntity action = new WrapperPlayClientInteractEntity(event);
         if (action.getAction() == WrapperPlayClientInteractEntity.InteractAction.ATTACK) {
            this.player.totalFlyingPacketsSent = 0;
            this.attacking = true;
            this.lastAttack = System.currentTimeMillis();
         }
      } else if (this.isTickPacketIncludingNonMovement(event.getPacketType())) {
         ++this.player.totalFlyingPacketsSent;
         this.attacking = false;
      }

   }

   public boolean hasAttackedSince(long time) {
      return System.currentTimeMillis() - this.lastAttack < time;
   }

   @Generated
   public boolean isAttacking() {
      return this.attacking;
   }

   @Generated
   public long getLastAttack() {
      return this.lastAttack;
   }
}
