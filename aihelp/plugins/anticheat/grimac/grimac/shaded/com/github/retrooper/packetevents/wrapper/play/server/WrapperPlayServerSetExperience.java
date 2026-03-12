package ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.play.server;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.event.PacketSendEvent;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.manager.server.ServerVersion;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.packettype.PacketType;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.packettype.PacketTypeCommon;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.PacketWrapper;

public class WrapperPlayServerSetExperience extends PacketWrapper<WrapperPlayServerSetExperience> {
   private float experienceBar;
   private int level;
   private int totalExperience;

   public WrapperPlayServerSetExperience(PacketSendEvent event) {
      super(event);
   }

   public WrapperPlayServerSetExperience(float experienceBar, int level, int totalExperience) {
      super((PacketTypeCommon)PacketType.Play.Server.SET_EXPERIENCE);
      this.experienceBar = experienceBar;
      this.level = level;
      this.totalExperience = totalExperience;
   }

   public void read() {
      this.experienceBar = this.readFloat();
      if (this.serverVersion.isOlderThanOrEquals(ServerVersion.V_1_7_10)) {
         this.level = this.readShort();
         this.totalExperience = this.readShort();
      } else {
         this.level = this.readVarInt();
         this.totalExperience = this.readVarInt();
      }

   }

   public void write() {
      this.writeFloat(this.experienceBar);
      if (this.serverVersion.isOlderThanOrEquals(ServerVersion.V_1_7_10)) {
         this.writeShort(this.level);
         this.writeShort(this.totalExperience);
      } else {
         this.writeVarInt(this.level);
         this.writeVarInt(this.totalExperience);
      }

   }

   public void copy(WrapperPlayServerSetExperience wrapper) {
      this.experienceBar = wrapper.experienceBar;
      this.level = wrapper.level;
      this.totalExperience = wrapper.totalExperience;
   }

   public float getExperienceBar() {
      return this.experienceBar;
   }

   public void setExperienceBar(float experienceBar) {
      this.experienceBar = experienceBar;
   }

   public int getLevel() {
      return this.level;
   }

   public void setLevel(int level) {
      this.level = level;
   }

   public int getTotalExperience() {
      return this.totalExperience;
   }

   public void setTotalExperience(int totalExperience) {
      this.totalExperience = totalExperience;
   }
}
