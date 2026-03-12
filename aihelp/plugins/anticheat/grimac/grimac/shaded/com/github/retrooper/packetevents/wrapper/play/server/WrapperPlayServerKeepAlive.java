package ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.play.server;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.event.PacketSendEvent;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.manager.server.ServerVersion;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.packettype.PacketType;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.packettype.PacketTypeCommon;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.PacketWrapper;

public class WrapperPlayServerKeepAlive extends PacketWrapper<WrapperPlayServerKeepAlive> {
   private long id;

   public WrapperPlayServerKeepAlive(PacketSendEvent event) {
      super(event);
   }

   public WrapperPlayServerKeepAlive(long id) {
      super((PacketTypeCommon)PacketType.Play.Server.KEEP_ALIVE);
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
      if (this.serverVersion.isNewerThanOrEquals(ServerVersion.V_1_12)) {
         this.writeLong(this.id);
      } else if (this.serverVersion.isNewerThanOrEquals(ServerVersion.V_1_8)) {
         this.writeVarInt((int)this.id);
      } else {
         this.writeInt((int)this.id);
      }

   }

   public void copy(WrapperPlayServerKeepAlive wrapper) {
      this.id = wrapper.id;
   }

   public long getId() {
      return this.id;
   }

   public void setId(long id) {
      this.id = id;
   }
}
