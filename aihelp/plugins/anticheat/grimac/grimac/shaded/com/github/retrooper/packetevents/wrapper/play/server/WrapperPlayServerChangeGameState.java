package ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.play.server;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.event.PacketSendEvent;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.packettype.PacketType;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.packettype.PacketTypeCommon;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.PacketWrapper;

public class WrapperPlayServerChangeGameState extends PacketWrapper<WrapperPlayServerChangeGameState> {
   private WrapperPlayServerChangeGameState.Reason reason;
   private float value;

   public WrapperPlayServerChangeGameState(PacketSendEvent event) {
      super(event);
   }

   public WrapperPlayServerChangeGameState(int reason, float value) {
      super((PacketTypeCommon)PacketType.Play.Server.CHANGE_GAME_STATE);
      this.reason = WrapperPlayServerChangeGameState.Reason.VALUES[reason];
      this.value = value;
   }

   public WrapperPlayServerChangeGameState(WrapperPlayServerChangeGameState.Reason reason, float value) {
      super((PacketTypeCommon)PacketType.Play.Server.CHANGE_GAME_STATE);
      this.reason = reason;
      this.value = value;
   }

   public void read() {
      this.reason = WrapperPlayServerChangeGameState.Reason.getById(this.readUnsignedByte());
      this.value = this.readFloat();
   }

   public void write() {
      this.writeByte(this.reason.ordinal());
      this.writeFloat(this.value);
   }

   public void copy(WrapperPlayServerChangeGameState wrapper) {
      this.reason = wrapper.reason;
      this.value = wrapper.value;
   }

   public WrapperPlayServerChangeGameState.Reason getReason() {
      return this.reason;
   }

   public void setReason(WrapperPlayServerChangeGameState.Reason reason) {
      this.reason = reason;
   }

   public float getValue() {
      return this.value;
   }

   public void setValue(float value) {
      this.value = value;
   }

   public static enum Reason {
      NO_RESPAWN_BLOCK_AVAILABLE,
      END_RAINING,
      BEGIN_RAINING,
      CHANGE_GAME_MODE,
      WIN_GAME,
      DEMO_EVENT,
      ARROW_HIT_PLAYER,
      RAIN_LEVEL_CHANGE,
      THUNDER_LEVEL_CHANGE,
      PLAY_PUFFER_FISH_STING_SOUND,
      PLAY_ELDER_GUARDIAN_MOB_APPEARANCE,
      ENABLE_RESPAWN_SCREEN,
      LIMITED_CRAFTING,
      START_LOADING_CHUNKS;

      private static final WrapperPlayServerChangeGameState.Reason[] VALUES = values();

      public static WrapperPlayServerChangeGameState.Reason getById(int index) {
         return VALUES[index];
      }

      // $FF: synthetic method
      private static WrapperPlayServerChangeGameState.Reason[] $values() {
         return new WrapperPlayServerChangeGameState.Reason[]{NO_RESPAWN_BLOCK_AVAILABLE, END_RAINING, BEGIN_RAINING, CHANGE_GAME_MODE, WIN_GAME, DEMO_EVENT, ARROW_HIT_PLAYER, RAIN_LEVEL_CHANGE, THUNDER_LEVEL_CHANGE, PLAY_PUFFER_FISH_STING_SOUND, PLAY_ELDER_GUARDIAN_MOB_APPEARANCE, ENABLE_RESPAWN_SCREEN, LIMITED_CRAFTING, START_LOADING_CHUNKS};
      }
   }
}
