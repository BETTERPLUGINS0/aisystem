package ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.play.server;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.event.PacketSendEvent;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.packettype.PacketType;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.packettype.PacketTypeCommon;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.PacketWrapper;
import ac.grim.grimac.shaded.jetbrains.annotations.Nullable;

public class WrapperPlayServerResetScore extends PacketWrapper<WrapperPlayServerResetScore> {
   private String targetName;
   @Nullable
   private String objective;

   public WrapperPlayServerResetScore(PacketSendEvent event) {
      super(event);
   }

   public WrapperPlayServerResetScore(String targetName, @Nullable String objective) {
      super((PacketTypeCommon)PacketType.Play.Server.RESET_SCORE);
      this.targetName = targetName;
      this.objective = objective;
   }

   public void read() {
      this.targetName = this.readString();
      this.objective = (String)this.readOptional(PacketWrapper::readString);
   }

   public void write() {
      this.writeString(this.targetName);
      this.writeOptional(this.objective, PacketWrapper::writeString);
   }

   public void copy(WrapperPlayServerResetScore wrapper) {
      this.targetName = wrapper.targetName;
      this.objective = wrapper.objective;
   }

   public String getTargetName() {
      return this.targetName;
   }

   public void setTargetName(String targetName) {
      this.targetName = targetName;
   }

   @Nullable
   public String getObjective() {
      return this.objective;
   }

   public void setObjective(@Nullable String objective) {
      this.objective = objective;
   }
}
