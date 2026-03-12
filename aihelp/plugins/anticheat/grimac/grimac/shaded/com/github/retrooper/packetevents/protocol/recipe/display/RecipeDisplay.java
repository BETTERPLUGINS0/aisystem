package ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.recipe.display;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.util.mappings.IRegistry;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.PacketWrapper;

public abstract class RecipeDisplay<T extends RecipeDisplay<?>> {
   protected final RecipeDisplayType<T> type;

   public RecipeDisplay(RecipeDisplayType<T> type) {
      this.type = type;
   }

   public static RecipeDisplay<?> read(PacketWrapper<?> wrapper) {
      return ((RecipeDisplayType)wrapper.readMappedEntity((IRegistry)RecipeDisplayTypes.getRegistry())).read(wrapper);
   }

   public static <T extends RecipeDisplay<?>> void write(PacketWrapper<?> wrapper, T display) {
      wrapper.writeMappedEntity(display.getType());
      display.getType().write(wrapper, display);
   }

   public RecipeDisplayType<T> getType() {
      return this.type;
   }
}
