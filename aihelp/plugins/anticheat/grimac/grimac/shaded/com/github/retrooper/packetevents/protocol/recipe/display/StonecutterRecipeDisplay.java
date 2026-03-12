package ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.recipe.display;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.recipe.display.slot.SlotDisplay;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.PacketWrapper;
import java.util.Objects;

public class StonecutterRecipeDisplay extends RecipeDisplay<StonecutterRecipeDisplay> {
   private SlotDisplay<?> input;
   private SlotDisplay<?> result;
   private SlotDisplay<?> craftingStation;

   public StonecutterRecipeDisplay(SlotDisplay<?> input, SlotDisplay<?> result, SlotDisplay<?> craftingStation) {
      super(RecipeDisplayTypes.STONECUTTER);
      this.input = input;
      this.result = result;
      this.craftingStation = craftingStation;
   }

   public static StonecutterRecipeDisplay read(PacketWrapper<?> wrapper) {
      SlotDisplay<?> input = SlotDisplay.read(wrapper);
      SlotDisplay<?> result = SlotDisplay.read(wrapper);
      SlotDisplay<?> craftingStation = SlotDisplay.read(wrapper);
      return new StonecutterRecipeDisplay(input, result, craftingStation);
   }

   public static void write(PacketWrapper<?> wrapper, StonecutterRecipeDisplay display) {
      SlotDisplay.write(wrapper, display.input);
      SlotDisplay.write(wrapper, display.result);
      SlotDisplay.write(wrapper, display.craftingStation);
   }

   public SlotDisplay<?> getInput() {
      return this.input;
   }

   public void setInput(SlotDisplay<?> input) {
      this.input = input;
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
      } else if (!(obj instanceof StonecutterRecipeDisplay)) {
         return false;
      } else {
         StonecutterRecipeDisplay that = (StonecutterRecipeDisplay)obj;
         if (!this.input.equals(that.input)) {
            return false;
         } else {
            return !this.result.equals(that.result) ? false : this.craftingStation.equals(that.craftingStation);
         }
      }
   }

   public int hashCode() {
      return Objects.hash(new Object[]{this.input, this.result, this.craftingStation});
   }

   public String toString() {
      return "StonecutterRecipeDisplay{input=" + this.input + ", result=" + this.result + ", craftingStation=" + this.craftingStation + '}';
   }
}
