package ac.grim.grimac.checks.impl.groundspoof;

import ac.grim.grimac.checks.Check;
import ac.grim.grimac.checks.CheckData;
import ac.grim.grimac.checks.type.PacketCheck;
import ac.grim.grimac.player.GrimPlayer;
import ac.grim.grimac.predictionengine.GhostBlockDetector;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.event.PacketReceiveEvent;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.packettype.PacketType;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientPlayerFlying;
import ac.grim.grimac.utils.collisions.datatypes.SimpleCollisionBox;
import ac.grim.grimac.utils.nmsutil.Collisions;
import ac.grim.grimac.utils.nmsutil.GetBoundingBox;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@CheckData(
   name = "NoFall",
   setback = 10.0D
)
public class NoFall extends Check implements PacketCheck {
   public boolean flipPlayerGroundStatus = false;

   public NoFall(GrimPlayer player) {
      super(player);
   }

   public void onPacketReceive(PacketReceiveEvent event) {
      WrapperPlayClientPlayerFlying wrapper;
      if (event.getPacketType() == PacketType.Play.Client.PLAYER_FLYING || event.getPacketType() == PacketType.Play.Client.PLAYER_ROTATION) {
         if (this.player.getSetbackTeleportUtil().insideUnloadedChunk()) {
            return;
         }

         if (this.player.getSetbackTeleportUtil().blockOffsets) {
            return;
         }

         wrapper = new WrapperPlayClientPlayerFlying(event);
         if (wrapper.isOnGround() && !wrapper.hasPositionChanged() && !this.isNearGround(wrapper.isOnGround())) {
            if (!GhostBlockDetector.isGhostBlock(this.player)) {
               this.flagAndAlertWithSetback();
            }

            if (this.shouldModifyPackets()) {
               wrapper.setOnGround(false);
               event.markForReEncode(true);
            }
         }
      }

      if (WrapperPlayClientPlayerFlying.isFlying(event.getPacketType())) {
         wrapper = new WrapperPlayClientPlayerFlying(event);
         if (this.flipPlayerGroundStatus) {
            this.flipPlayerGroundStatus = false;
            if (this.shouldModifyPackets()) {
               wrapper.setOnGround(!wrapper.isOnGround());
               event.markForReEncode(true);
            }
         }

         if (this.player.packetStateData.lastPacketWasTeleport && this.shouldModifyPackets()) {
            wrapper.setOnGround(false);
            event.markForReEncode(true);
         }
      }

   }

   private boolean isNearGround(boolean onGround) {
      if (onGround) {
         SimpleCollisionBox feetBB = GetBoundingBox.getBoundingBoxFromPosAndSize(this.player, this.player.x, this.player.y, this.player.z, 0.6F, 0.001F);
         feetBB.expand(this.player.getMovementThreshold());
         return this.checkForBoxes(feetBB);
      } else {
         return true;
      }
   }

   private boolean checkForBoxes(SimpleCollisionBox playerBB) {
      List<SimpleCollisionBox> boxes = new ArrayList();
      Collisions.getCollisionBoxes(this.player, playerBB, boxes, false);
      Iterator var3 = boxes.iterator();

      SimpleCollisionBox box;
      do {
         if (!var3.hasNext()) {
            return this.player.compensatedWorld.isNearHardEntity(playerBB.copy().expand(4.0D));
         }

         box = (SimpleCollisionBox)var3.next();
      } while(!playerBB.collidesVertically(box));

      return true;
   }
}
