package ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.packettype.PacketTypeCommon;
import ac.grim.grimac.shaded.jetbrains.annotations.Nullable;

public class PacketTypeData {
   @Nullable
   private PacketTypeCommon packetType;
   private int nativePacketId;

   public PacketTypeData(@Nullable PacketTypeCommon packetType, int nativePacketId) {
      this.packetType = packetType;
      this.nativePacketId = nativePacketId;
   }

   @Nullable
   public PacketTypeCommon getPacketType() {
      return this.packetType;
   }

   public int getNativePacketId() {
      return this.nativePacketId;
   }

   public void setPacketType(@Nullable PacketTypeCommon packetType) {
      this.packetType = packetType;
   }

   public void setNativePacketId(int nativePacketId) {
      this.nativePacketId = nativePacketId;
   }
}
