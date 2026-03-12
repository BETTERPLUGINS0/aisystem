package ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.play.server;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.event.PacketSendEvent;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.manager.server.ServerVersion;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.packettype.PacketType;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.packettype.PacketTypeCommon;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.util.Vector3i;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.PacketWrapper;

public class WrapperPlayServerOpenSignEditor extends PacketWrapper<WrapperPlayServerOpenSignEditor> {
   private Vector3i position;
   private boolean isFrontText;

   public WrapperPlayServerOpenSignEditor(PacketSendEvent event) {
      super(event);
   }

   public WrapperPlayServerOpenSignEditor(Vector3i position, boolean isFrontText) {
      super((PacketTypeCommon)PacketType.Play.Server.OPEN_SIGN_EDITOR);
      this.position = position;
      this.isFrontText = isFrontText;
   }

   public void read() {
      if (this.serverVersion.isNewerThanOrEquals(ServerVersion.V_1_8)) {
         this.position = new Vector3i(this.readLong(), this.serverVersion);
      } else {
         int x = this.readInt();
         int y = this.readInt();
         int z = this.readInt();
         this.position = new Vector3i(x, y, z);
      }

      if (this.serverVersion.isNewerThanOrEquals(ServerVersion.V_1_20)) {
         this.isFrontText = this.readBoolean();
      } else {
         this.isFrontText = true;
      }

   }

   public void write() {
      if (this.serverVersion.isNewerThanOrEquals(ServerVersion.V_1_8)) {
         long positionVector = this.position.getSerializedPosition(this.serverVersion);
         this.writeLong(positionVector);
      } else {
         this.writeInt(this.position.x);
         this.writeInt(this.position.y);
         this.writeInt(this.position.z);
      }

      if (this.serverVersion.isNewerThanOrEquals(ServerVersion.V_1_20)) {
         this.writeBoolean(this.isFrontText);
      }

   }

   public void copy(WrapperPlayServerOpenSignEditor wrapper) {
      this.position = wrapper.position;
      this.isFrontText = wrapper.isFrontText;
   }

   public Vector3i getPosition() {
      return this.position;
   }

   public void setPosition(Vector3i position) {
      this.position = position;
   }

   public boolean isFrontText() {
      return this.isFrontText;
   }

   public void setFrontText(boolean frontText) {
      this.isFrontText = frontText;
   }
}
