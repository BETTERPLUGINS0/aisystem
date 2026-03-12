package ac.grim.grimac.utils.payload;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.PacketWrapper;
import ac.grim.grimac.shaded.jetbrains.annotations.NotNull;

public record PayloadItemName(@NotNull String itemName) implements Payload {
   public PayloadItemName(byte[] data) {
      this(Payload.wrapper(data).readString());
   }

   public PayloadItemName(@NotNull String itemName) {
      this.itemName = itemName;
   }

   public void write(PacketWrapper<?> wrapper) {
      wrapper.writeString(this.itemName);
   }

   @NotNull
   public String itemName() {
      return this.itemName;
   }
}
