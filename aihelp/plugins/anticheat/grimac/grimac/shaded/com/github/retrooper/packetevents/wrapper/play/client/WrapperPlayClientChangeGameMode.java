package ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.play.client;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.event.PacketReceiveEvent;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.packettype.PacketType;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.packettype.PacketTypeCommon;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.player.GameMode;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.PacketWrapper;

public class WrapperPlayClientChangeGameMode extends PacketWrapper<WrapperPlayClientChangeGameMode> {
   private GameMode gameMode;

   public WrapperPlayClientChangeGameMode(PacketReceiveEvent event) {
      super(event);
   }

   public WrapperPlayClientChangeGameMode(GameMode gameMode) {
      super((PacketTypeCommon)PacketType.Play.Client.CHANGE_GAME_MODE);
      this.gameMode = gameMode;
   }

   public void read() {
      this.gameMode = GameMode.getById(this.readVarInt());
   }

   public void write() {
      this.writeVarInt(this.gameMode.getId());
   }

   public void copy(WrapperPlayClientChangeGameMode wrapper) {
      this.gameMode = wrapper.gameMode;
   }

   public GameMode getGameMode() {
      return this.gameMode;
   }

   public void setGameMode(GameMode gamemode) {
      this.gameMode = gamemode;
   }
}
