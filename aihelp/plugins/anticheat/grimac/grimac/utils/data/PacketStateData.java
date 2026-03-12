package ac.grim.grimac.utils.data;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.player.InteractionHand;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.util.Vector3d;
import lombok.Generated;

public class PacketStateData {
   public boolean packetPlayerOnGround = false;
   public boolean lastPacketWasTeleport = false;
   public boolean cancelDuplicatePacket;
   public boolean lastPacketWasOnePointSeventeenDuplicate = false;
   public boolean lastTransactionPacketWasValid = false;
   public int lastSlotSelected;
   public InteractionHand itemInUseHand;
   public long lastRiptide;
   public boolean tryingToRiptide;
   public int slowedByUsingItemTransaction;
   public boolean receivedSteerVehicle;
   public boolean didLastLastMovementIncludePosition;
   public boolean didLastMovementIncludePosition;
   public boolean didSendMovementBeforeTickEnd;
   public KnownInput knownInput;
   public Vector3d lastClaimedPosition;
   public float lastHealth;
   public float lastSaturation;
   public int lastFood;
   public boolean lastServerTransWasValid;
   private int slowedByUsingItemSlot;
   public boolean sendingBundlePacket;
   public boolean horseInteractCausedForcedRotation;

   public PacketStateData() {
      this.itemInUseHand = InteractionHand.MAIN_HAND;
      this.lastRiptide = 0L;
      this.tryingToRiptide = false;
      this.slowedByUsingItemTransaction = Integer.MIN_VALUE;
      this.receivedSteerVehicle = false;
      this.didLastLastMovementIncludePosition = false;
      this.didLastMovementIncludePosition = false;
      this.didSendMovementBeforeTickEnd = false;
      this.knownInput = KnownInput.DEFAULT;
      this.lastClaimedPosition = new Vector3d(0.0D, 0.0D, 0.0D);
      this.lastServerTransWasValid = false;
      this.slowedByUsingItemSlot = Integer.MIN_VALUE;
      this.horseInteractCausedForcedRotation = false;
   }

   public void setSlowedByUsingItem(boolean slowedByUsingItem) {
      this.slowedByUsingItemSlot = slowedByUsingItem ? this.lastSlotSelected : Integer.MIN_VALUE;
   }

   public boolean isSlowedByUsingItem() {
      return this.slowedByUsingItemSlot != Integer.MIN_VALUE;
   }

   @Generated
   public int getSlowedByUsingItemSlot() {
      return this.slowedByUsingItemSlot;
   }
}
