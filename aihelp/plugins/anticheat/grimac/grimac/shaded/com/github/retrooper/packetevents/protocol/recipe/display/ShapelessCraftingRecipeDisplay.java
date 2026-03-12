package ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.recipe.display;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.recipe.display.slot.SlotDisplay;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.PacketWrapper;
import java.util.List;
import java.util.Objects;

public class ShapelessCraftingRecipeDisplay extends RecipeDisplay<ShapelessCraftingRecipeDisplay> {
   private List<SlotDisplay<?>> ingredients;
   private SlotDisplay<?> result;
   private SlotDisplay<?> craftingStation;

   public ShapelessCraftingRecipeDisplay(List<SlotDisplay<?>> ingredients, SlotDisplay<?> result, SlotDisplay<?> craftingStation) {
      super(RecipeDisplayTypes.CRAFTING_SHAPELESS);
      this.ingredients = ingredients;
      this.result = result;
      this.craftingStation = craftingStation;
   }

   public static ShapelessCraftingRecipeDisplay read(PacketWrapper<?> wrapper) {
      List<SlotDisplay<?>> ingredients = wrapper.readList(SlotDisplay::read);
      SlotDisplay<?> result = SlotDisplay.read(wrapper);
      SlotDisplay<?> craftingStation = SlotDisplay.read(wrapper);
      return new ShapelessCraftingRecipeDisplay(ingredients, result, craftingStation);
   }

   public static void write(PacketWrapper<?> wrapper, ShapelessCraftingRecipeDisplay display) {
      wrapper.writeList(display.ingredients, SlotDisplay::write);
      SlotDisplay.write(wrapper, display.result);
      SlotDisplay.write(wrapper, display.craftingStation);
   }

   public List<SlotDisplay<?>> getIngredients() {
      return this.ingredients;
   }

   public void setIngredients(List<SlotDisplay<?>> ingredients) {
      this.ingredients = ingredients;
   }

   public SlotDisplay<?> getResult() {
      return this.result;
   }

   public void setResult(SlotDisplay<?> result) {
      this.result = result;
   }

   public SlotDisplay<?> getCraftingStation() {
      return this.craftingStation;
   }

   public void setCraftingStation(SlotDisplay<?> craftingStation) {
      this.craftingStation = craftingStation;
   }

   public boolean equals(Object obj) {
      if (this == obj) {
         return true;
      } else if (!(obj instanceof ShapelessCraftingRecipeDisplay)) {
         return false;
      } else {
         ShapelessCraftingRecipeDisplay that = (ShapelessCraftingRecipeDisplay)obj;
         if (!this.ingredients.equals(that.ingredients)) {
            return false;
         } else {
            return !this.result.equals(that.result) ? false : this.craftingStation.equals(that.craftingStation);
         }
      }
   }

   public int hashCode() {
      return Objects.hash(new Object[]{this.ingredients, this.result, this.craftingStation});
   }

   public String toString() {
      return "ShapelessCraftingRecipeDisplay{ingredients=" + this.ingredients + ", result=" + this.result + ", craftingStation=" + this.craftingStation + '}';
   }
}
