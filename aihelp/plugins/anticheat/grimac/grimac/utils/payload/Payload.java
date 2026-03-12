package ac.grim.grimac.utils.payload;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.netty.buffer.ByteBufHelper;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.netty.buffer.UnpooledByteBufAllocationHelper;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.PacketWrapper;
import ac.grim.grimac.shaded.jetbrains.annotations.ApiStatus;
import ac.grim.grimac.shaded.jetbrains.annotations.NotNull;

public interface Payload {
   @ApiStatus.NonExtendable
   default byte[] write() {
      Object buffer = UnpooledByteBufAllocationHelper.buffer();
      this.write(PacketWrapper.createUniversalPacketWrapper(buffer));
      return ByteBufHelper.array(buffer);
   }

   @ApiStatus.OverrideOnly
   void write(PacketWrapper<?> var1);

   @NotNull
   static PacketWrapper<?> wrapper(byte[] data) {
      return PacketWrapper.createUniversalPacketWrapper(UnpooledByteBufAllocationHelper.wrappedBuffer(data));
   }
}
