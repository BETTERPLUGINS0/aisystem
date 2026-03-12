package ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.recipe.display;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.recipe.display.slot.SlotDisplay;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.PacketWrapper;
import java.util.List;
import java.util.Objects;

public class ShapedCraftingRecipeDisplay extends RecipeDisplay<ShapedCraftingRecipeDisplay> {
   private int width;
   private int height;
   private List<SlotDisplay<?>> ingredients;
   private SlotDisplay<?> result;
   private SlotDisplay<?> craftingStation;

   public ShapedCraftingRecipeDisplay(int width, int height, List<SlotDisplay<?>> ingredients, SlotDisplay<?> result, SlotDisplay<?> craftingStation) {
      super(RecipeDisplayTypes.CRAFTING_SHAPED);
      this.width = width;
      this.height = height;
      this.ingredients = ingredients;
      this.result = result;
      this.craftingStation = craftingStation;
   }

   public static ShapedCraftingRecipeDisplay read(PacketWrapper<?> wrapper) {
      int width = wrapper.readVarInt();
      int height = wrapper.readVarInt();
      List<SlotDisplay<?>> ingredients = wrapper.readList(SlotDisplay::read);
      SlotDisplay<?> result = SlotDisplay.read(wrapper);
      SlotDisplay<?> craftingStation = SlotDisplay.read(wrapper);
      return new ShapedCraftingRecipeDisplay(width, height, ingredients, result, craftingStation);
   }

   public static void write(PacketWrapper<?> wrapper, ShapedCraftingRecipeDisplay display) {
      wrapper.writeVarInt(display.width);
      wrapper.writeVarInt(display.height);
      wrapper.writeList(display.ingredients, SlotDisplay::write);
      SlotDisplay.write(wrapper, display.result);
      SlotDisplay.write(wrapper, display.craftingStation);
   }

   public int getWidth() {
      return this.width;
   }

   public void setWidth(int width) {
      this.width = width;
   }

   public int getHeight() {
      return this.height;
   }

   public void setHeight(int height) {
      this.height = height;
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
      } else if (!(obj instanceof ShapedCraftingRecipeDisplay)) {
         return false;
      } else {
         ShapedCraftingRecipeDisplay that = (ShapedCraftingRecipeDisplay)obj;
         if (this.width != that.width) {
            return false;
         } else if (this.height != that.height) {
            return false;
         } else if (!this.ingredients.equals(that.ingredients)) {
            return false;
         } else {
            return !this.result.equals(that.result) ? false : this.craftingStation.equals(that.craftingStation);
         }
      }
   }

   public int hashCode() {
      return Objects.hash(new Object[]{this.width, this.height, this.ingredients, this.result, this.craftingStation});
   }

   public String toString() {
      return "ShapedCraftingRecipeDisplay{width=" + this.width + ", height=" + this.height + ", ingredients=" + this.ingredients + ", result=" + this.result + ", craftingStation=" + this.craftingStation + '}';
   }
}
