package ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.play.server;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.event.PacketSendEvent;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.manager.server.ServerVersion;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.packettype.PacketType;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.packettype.PacketTypeCommon;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.PacketWrapper;

public class WrapperPlayServerDisplayScoreboard extends PacketWrapper<WrapperPlayServerDisplayScoreboard> {
   private int position;
   private String scoreName;

   public WrapperPlayServerDisplayScoreboard(PacketSendEvent event) {
      super(event);
   }

   public WrapperPlayServerDisplayScoreboard(int position, String scoreName) {
      super((PacketTypeCommon)PacketType.Play.Server.DISPLAY_SCOREBOARD);
      this.position = position;
      this.scoreName = scoreName;
   }

   public void read() {
      if (this.serverVersion.isNewerThanOrEquals(ServerVersion.V_1_20_2)) {
         this.position = this.readVarInt();
      } else {
         this.position = this.readByte();
      }

      if (this.serverVersion.isNewerThanOrEquals(ServerVersion.V_1_18)) {
         this.scoreName = this.readString();
      } else {
         this.scoreName = this.readString(16);
      }

   }

   public void write() {
      if (this.serverVersion.isNewerThanOrEquals(ServerVersion.V_1_20_2)) {
         this.writeVarInt(this.position);
      } else {
         this.writeByte(this.position);
      }

      if (this.serverVersion.isNewerThanOrEquals(ServerVersion.V_1_18)) {
         this.writeString(this.scoreName);
      } else {
         this.writeString(this.scoreName, 16);
      }

   }

   public void copy(WrapperPlayServerDisplayScoreboard wrapper) {
      this.position = wrapper.position;
      this.scoreName = wrapper.scoreName;
   }

   public int getPosition() {
      return this.position;
   }

   public void setPosition(int position) {
      this.position = position;
   }

   public String getScoreName() {
      return this.scoreName;
   }

   public void setScoreName(String scoreName) {
      this.scoreName = scoreName;
   }
}
