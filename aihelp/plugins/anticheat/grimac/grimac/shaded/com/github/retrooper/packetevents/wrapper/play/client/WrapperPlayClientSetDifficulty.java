package ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.play.client;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.event.PacketReceiveEvent;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.manager.server.ServerVersion;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.packettype.PacketType;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.packettype.PacketTypeCommon;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.world.Difficulty;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.PacketWrapper;

public class WrapperPlayClientSetDifficulty extends PacketWrapper<WrapperPlayClientSetDifficulty> {
   private Difficulty difficulty;

   public WrapperPlayClientSetDifficulty(PacketReceiveEvent event) {
      super(event);
   }

   public WrapperPlayClientSetDifficulty(Difficulty difficulty) {
      super((PacketTypeCommon)PacketType.Play.Client.SET_DIFFICULTY);
      this.difficulty = difficulty;
   }

   public void read() {
      if (this.serverVersion.isNewerThanOrEquals(ServerVersion.V_1_21_6)) {
         this.difficulty = (Difficulty)this.readEnum(Difficulty.class);
      } else {
         this.difficulty = Difficulty.getById(this.readUnsignedByte());
      }

   }

   public void write() {
      if (this.serverVersion.isNewerThanOrEquals(ServerVersion.V_1_21_6)) {
         this.writeEnum(this.difficulty);
      } else {
         this.writeByte(this.difficulty.getId());
      }

   }

   public void copy(WrapperPlayClientSetDifficulty wrapper) {
      this.difficulty = wrapper.difficulty;
   }

   public Difficulty getDifficulty() {
      return this.difficulty;
   }

   public void setDifficulty(Difficulty difficulty) {
      this.difficulty = difficulty;
   }
}
