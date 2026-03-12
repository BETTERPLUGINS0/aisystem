package ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.recipe;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.item.ItemStack;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.PacketWrapper;
import ac.grim.grimac.shaded.jetbrains.annotations.ApiStatus;

@ApiStatus.Obsolete
public class Ingredient {
   private final ItemStack[] options;

   public Ingredient(ItemStack... options) {
      this.options = options;
   }

   public static Ingredient read(PacketWrapper<?> wrapper) {
      ItemStack[] options = (ItemStack[])wrapper.readArray(PacketWrapper::readItemStack, ItemStack.class);
      return new Ingredient(options);
   }

   public static void write(PacketWrapper<?> wrapper, Ingredient ingredient) {
      wrapper.writeArray(ingredient.options, PacketWrapper::writeItemStack);
   }

   public ItemStack[] getOptions() {
      return this.options;
   }
}
