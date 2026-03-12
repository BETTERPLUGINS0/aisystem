package ac.grim.grimac.checks.impl.packetorder;

import ac.grim.grimac.checks.Check;
import ac.grim.grimac.checks.type.PacketCheck;
import ac.grim.grimac.player.GrimPlayer;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.event.PacketReceiveEvent;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.packettype.PacketType;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.packettype.PacketTypeCommon;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.player.ClientVersion;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.world.BlockFace;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientClickWindow;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientClientStatus;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientEntityAction;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientInteractEntity;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientPlayerBlockPlacement;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientPlayerDigging;
import ac.grim.grimac.shaded.jetbrains.annotations.Contract;
import ac.grim.grimac.utils.math.GrimMath;
import lombok.Generated;

public final class PacketOrderProcessor extends Check implements PacketCheck {
   private boolean openingInventory;
   private boolean swapping;
   private boolean dropping;
   private boolean interacting;
   private boolean attacking;
   private boolean releasing;
   private boolean digging;
   private boolean sprinting;
   private boolean sneaking;
   private boolean placing;
   private boolean using;
   private boolean picking;
   private boolean clickingInInventory;
   private boolean closingInventory;
   private boolean quickMoveClicking;
   private boolean pickUpClicking;
   private boolean leavingBed;
   private boolean startingToGlide;
   private boolean jumpingWithMount;

   public PacketOrderProcessor(GrimPlayer player) {
      super(player);
   }

   public void onPacketReceive(PacketReceiveEvent event) {
      PacketTypeCommon packetType = event.getPacketType();
      if (packetType == PacketType.Play.Client.CLIENT_STATUS && (new WrapperPlayClientClientStatus(event)).getAction() == WrapperPlayClientClientStatus.Action.OPEN_INVENTORY_ACHIEVEMENT) {
         this.openingInventory = true;
      }

      if (packetType == PacketType.Play.Client.INTERACT_ENTITY) {
         if ((new WrapperPlayClientInteractEntity(event)).getAction() == WrapperPlayClientInteractEntity.InteractAction.ATTACK) {
            this.attacking = true;
         } else {
            this.interacting = true;
         }
      }

      if (packetType == PacketType.Play.Client.PLAYER_DIGGING) {
         switch((new WrapperPlayClientPlayerDigging(event)).getAction()) {
         case SWAP_ITEM_WITH_OFFHAND:
            this.swapping = true;
            break;
         case DROP_ITEM:
         case DROP_ITEM_STACK:
            this.dropping = true;
            break;
         case RELEASE_USE_ITEM:
            this.releasing = true;
            break;
         case FINISHED_DIGGING:
         case CANCELLED_DIGGING:
         case START_DIGGING:
            this.digging = true;
         }
      }

      if (packetType == PacketType.Play.Client.ENTITY_ACTION) {
         switch((new WrapperPlayClientEntityAction(event)).getAction()) {
         case START_SPRINTING:
         case STOP_SPRINTING:
            if (!this.player.inVehicle()) {
               this.sprinting = true;
            }
            break;
         case STOP_SNEAKING:
         case START_SNEAKING:
            this.sneaking = true;
            break;
         case LEAVE_BED:
            this.leavingBed = true;
            break;
         case START_FLYING_WITH_ELYTRA:
            this.startingToGlide = true;
            break;
         case OPEN_HORSE_INVENTORY:
            this.openingInventory = true;
            break;
         case START_JUMPING_WITH_HORSE:
         case STOP_JUMPING_WITH_HORSE:
            this.jumpingWithMount = true;
         }
      }

      if (packetType == PacketType.Play.Client.USE_ITEM) {
         this.using = true;
      }

      if (packetType == PacketType.Play.Client.PLAYER_BLOCK_PLACEMENT) {
         if ((new WrapperPlayClientPlayerBlockPlacement(event)).getFace() == BlockFace.OTHER) {
            this.using = true;
         } else {
            this.placing = true;
         }
      }

      if (packetType == PacketType.Play.Client.PICK_ITEM) {
         this.picking = true;
      }

      if (packetType == PacketType.Play.Client.CLICK_WINDOW) {
         this.clickingInInventory = true;
         switch((new WrapperPlayClientClickWindow(event)).getWindowClickType()) {
         case QUICK_MOVE:
            this.quickMoveClicking = true;
            break;
         case PICKUP:
         case PICKUP_ALL:
            this.pickUpClicking = true;
         }
      }

      if (packetType == PacketType.Play.Client.CLOSE_WINDOW) {
         this.closingInventory = true;
      }

      if (!this.player.cameraEntity.isSelf() || this.isTickPacket(packetType) || this.player.getClientVersion().isOlderThan(ClientVersion.V_1_21_2) && !this.player.compensatedWorld.isChunkLoaded(GrimMath.floor(this.player.x) >> 4, GrimMath.floor(this.player.z) >> 4)) {
         this.openingInventory = false;
         this.swapping = false;
         this.dropping = false;
         this.attacking = false;
         this.interacting = false;
         this.releasing = false;
         this.digging = false;
         this.placing = false;
         this.using = false;
         this.picking = false;
         this.sprinting = false;
         this.sneaking = false;
         this.clickingInInventory = false;
         this.closingInventory = false;
         this.quickMoveClicking = false;
         this.pickUpClicking = false;
         this.leavingBed = false;
         this.startingToGlide = false;
         this.jumpingWithMount = false;
      }

   }

   @Contract(
      pure = true
   )
   public boolean isRightClicking() {
      return this.placing || this.using || this.interacting;
   }

   @Generated
   public boolean isOpeningInventory() {
      return this.openingInventory;
   }

   @Generated
   public boolean isSwapping() {
      return this.swapping;
   }

   @Generated
   public boolean isDropping() {
      return this.dropping;
   }

   @Generated
   public boolean isInteracting() {
      return this.interacting;
   }

   @Generated
   public boolean isAttacking() {
      return this.attacking;
   }

   @Generated
   public boolean isReleasing() {
      return this.releasing;
   }

   @Generated
   public boolean isDigging() {
      return this.digging;
   }

   @Generated
   public boolean isSprinting() {
      return this.sprinting;
   }

   @Generated
   public boolean isSneaking() {
      return this.sneaking;
   }

   @Generated
   public boolean isPlacing() {
      return this.placing;
   }

   @Generated
   public boolean isUsing() {
      return this.using;
   }

   @Generated
   public boolean isPicking() {
      return this.picking;
   }

   @Generated
   public boolean isClickingInInventory() {
      return this.clickingInInventory;
   }

   @Generated
   public boolean isClosingInventory() {
      return this.closingInventory;
   }

   @Generated
   public boolean isQuickMoveClicking() {
      return this.quickMoveClicking;
   }

   @Generated
   public boolean isPickUpClicking() {
      return this.pickUpClicking;
   }

   @Generated
   public boolean isLeavingBed() {
      return this.leavingBed;
   }

   @Generated
   public boolean isStartingToGlide() {
      return this.startingToGlide;
   }

   @Generated
   public boolean isJumpingWithMount() {
      return this.jumpingWithMount;
   }
}
