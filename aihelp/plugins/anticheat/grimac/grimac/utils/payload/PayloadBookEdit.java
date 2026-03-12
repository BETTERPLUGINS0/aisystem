package ac.grim.grimac.utils.payload;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.item.ItemStack;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.PacketWrapper;
import ac.grim.grimac.shaded.jetbrains.annotations.NotNull;

public record PayloadBookEdit(@NotNull ItemStack itemStack) implements Payload {
   public PayloadBookEdit(byte[] data) {
      this(Payload.wrapper(data).readItemStack());
   }

   public PayloadBookEdit(@NotNull ItemStack itemStack) {
      this.itemStack = itemStack;
   }

   public void write(PacketWrapper<?> wrapper) {
      wrapper.writeItemStack(this.itemStack);
   }

   @NotNull
   public ItemStack itemStack() {
      return this.itemStack;
   }
}
