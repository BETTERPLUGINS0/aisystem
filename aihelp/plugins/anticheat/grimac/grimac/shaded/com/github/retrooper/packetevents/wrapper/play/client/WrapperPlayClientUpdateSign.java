package ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.play.client;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.event.PacketReceiveEvent;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.manager.server.ServerVersion;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.packettype.PacketType;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.packettype.PacketTypeCommon;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.util.Vector3i;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.PacketWrapper;

public class WrapperPlayClientUpdateSign extends PacketWrapper<WrapperPlayClientUpdateSign> {
   private Vector3i blockPosition;
   private String[] textLines;
   private boolean isFrontText;

   public WrapperPlayClientUpdateSign(PacketReceiveEvent event) {
      super(event);
   }

   public WrapperPlayClientUpdateSign(Vector3i blockPosition, String[] textLines, boolean isFrontText) {
      super((PacketTypeCommon)PacketType.Play.Client.UPDATE_SIGN);
      this.blockPosition = blockPosition;
      this.textLines = textLines;
      this.isFrontText = isFrontText;
   }

   public void read() {
      int i;
      if (this.serverVersion.isNewerThanOrEquals(ServerVersion.V_1_8)) {
         this.blockPosition = new Vector3i(this.readLong(), this.serverVersion);
      } else {
         i = this.readInt();
         int y = this.readShort();
         int z = this.readInt();
         this.blockPosition = new Vector3i(i, y, z);
      }

      if (this.serverVersion.isNewerThanOrEquals(ServerVersion.V_1_20)) {
         this.isFrontText = this.readBoolean();
      } else {
         this.isFrontText = true;
      }

      this.textLines = new String[4];

      for(i = 0; i < 4; ++i) {
         this.textLines[i] = this.readString(384);
      }

   }

   public void write() {
      if (this.serverVersion.isNewerThanOrEquals(ServerVersion.V_1_8)) {
         long positionVector = this.blockPosition.getSerializedPosition(this.serverVersion);
         this.writeLong(positionVector);
      } else {
         this.writeInt(this.blockPosition.x);
         this.writeShort(this.blockPosition.y);
         this.writeInt(this.blockPosition.z);
      }

      if (this.serverVersion.isNewerThanOrEquals(ServerVersion.V_1_20)) {
         this.writeBoolean(this.isFrontText);
      }

      for(int i = 0; i < 4; ++i) {
         this.writeString(this.textLines[i]);
      }

   }

   public void copy(WrapperPlayClientUpdateSign wrapper) {
      this.blockPosition = wrapper.blockPosition;
      this.isFrontText = wrapper.isFrontText;
      this.textLines = wrapper.textLines;
   }

   public Vector3i getBlockPosition() {
      return this.blockPosition;
   }

   public void setBlockPosition(Vector3i blockPosition) {
      this.blockPosition = blockPosition;
   }

   public String[] getTextLines() {
      return this.textLines;
   }

   public void setTextLines(String[] textLines) {
      this.textLines = textLines;
   }

   public boolean isFrontText() {
      return this.isFrontText;
   }

   public void setFrontText(boolean frontText) {
      this.isFrontText = frontText;
   }
}
