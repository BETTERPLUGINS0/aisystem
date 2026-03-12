package ac.grim.grimac.utils.data;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.PacketWrapper;

public record BlockPlaceSnapshot(PacketWrapper<?> wrapper, boolean sneaking) {
   public BlockPlaceSnapshot(PacketWrapper<?> wrapper, boolean sneaking) {
      this.wrapper = wrapper;
      this.sneaking = sneaking;
   }

   public PacketWrapper<?> wrapper() {
      return this.wrapper;
   }

   public boolean sneaking() {
      return this.sneaking;
   }
}
