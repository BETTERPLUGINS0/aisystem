package ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.play.server;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.event.PacketSendEvent;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.packettype.PacketType;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.packettype.PacketTypeCommon;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.world.chunk.LightData;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.PacketWrapper;

public class WrapperPlayServerUpdateLight extends PacketWrapper<WrapperPlayServerUpdateLight> {
   private int x;
   private int z;
   private LightData lightData;

   public WrapperPlayServerUpdateLight(PacketSendEvent event) {
      super(event);
   }

   public WrapperPlayServerUpdateLight(int x, int z, LightData lightData) {
      super((PacketTypeCommon)PacketType.Play.Server.UPDATE_LIGHT);
      this.x = x;
      this.z = z;
      this.lightData = lightData;
   }

   public void read() {
      this.x = this.readVarInt();
      this.z = this.readVarInt();
      this.lightData = LightData.read(this);
   }

   public void write() {
      this.writeVarInt(this.x);
      this.writeVarInt(this.z);
      LightData.write(this, this.lightData);
   }

   public int getX() {
      return this.x;
   }

   public void setX(int x) {
      this.x = x;
   }

   public int getZ() {
      return this.z;
   }

   public void setZ(int z) {
      this.z = z;
   }

   public LightData getLightData() {
      return this.lightData;
   }

   public void setLightData(LightData lightData) {
      this.lightData = lightData;
   }

   public void copy(WrapperPlayServerUpdateLight wrapper) {
      this.x = wrapper.x;
      this.z = wrapper.z;
      this.lightData = wrapper.lightData.clone();
   }
}
