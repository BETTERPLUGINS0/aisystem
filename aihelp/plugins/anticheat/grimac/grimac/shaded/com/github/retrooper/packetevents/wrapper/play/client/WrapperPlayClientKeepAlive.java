package ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.play.client;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.event.PacketReceiveEvent;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.manager.server.ServerVersion;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.packettype.PacketType;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.packettype.PacketTypeCommon;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.PacketWrapper;

public class WrapperPlayClientKeepAlive extends PacketWrapper<WrapperPlayClientKeepAlive> {
   private long id;

   public WrapperPlayClientKeepAlive(PacketReceiveEvent event) {
      super(event);
   }

   public WrapperPlayClientKeepAlive(long id) {
      super((PacketTypeCommon)PacketType.Play.Client.KEEP_ALIVE);
      this.id = id;
   }

   public void read() {
      if (this.serverVersion.isNewerThanOrEquals(ServerVersion.V_1_12_2)) {
         this.id = this.readLong();
      } else if (this.serverVersion.isNewerThanOrEquals(ServerVersion.V_1_8)) {
         this.id = (long)this.readVarInt();
      } else {
         this.id = (long)this.readInt();
      }

   }

   public void write() {
      if (this.serverVersion.isNewerThanOrEquals(ServerVersion.V_1_12_2)) {
         this.writeLong(this.id);
      } else if (this.serverVersion.isNewerThanOrEquals(ServerVersion.V_1_8)) {
         this.writeVarInt((int)this.id);
      } else {
         this.writeInt((int)this.id);
      }

   }

   public void copy(WrapperPlayClientKeepAlive wrapper) {
      this.id = wrapper.id;
   }

   public long getId() {
      return this.id;
   }

   public void setId(long id) {
      this.id = id;
   }
}
