package ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.recipe.display;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.recipe.display.slot.SlotDisplay;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.PacketWrapper;
import java.util.Objects;

public class SmithingRecipeDisplay extends RecipeDisplay<SmithingRecipeDisplay> {
   private SlotDisplay<?> template;
   private SlotDisplay<?> base;
   private SlotDisplay<?> addition;
   private SlotDisplay<?> result;
   private SlotDisplay<?> craftingStation;

   public SmithingRecipeDisplay(SlotDisplay<?> template, SlotDisplay<?> base, SlotDisplay<?> addition, SlotDisplay<?> result, SlotDisplay<?> craftingStation) {
      super(RecipeDisplayTypes.SMITHING);
      this.template = template;
      this.base = base;
      this.addition = addition;
      this.result = result;
      this.craftingStation = craftingStation;
   }

   public static SmithingRecipeDisplay read(PacketWrapper<?> wrapper) {
      SlotDisplay<?> template = SlotDisplay.read(wrapper);
      SlotDisplay<?> base = SlotDisplay.read(wrapper);
      SlotDisplay<?> addition = SlotDisplay.read(wrapper);
      SlotDisplay<?> result = SlotDisplay.read(wrapper);
      SlotDisplay<?> craftingStation = SlotDisplay.read(wrapper);
      return new SmithingRecipeDisplay(template, base, addition, result, craftingStation);
   }

   public static void write(PacketWrapper<?> wrapper, SmithingRecipeDisplay display) {
      SlotDisplay.write(wrapper, display.template);
      SlotDisplay.write(wrapper, display.base);
      SlotDisplay.write(wrapper, display.addition);
      SlotDisplay.write(wrapper, display.result);
      SlotDisplay.write(wrapper, display.craftingStation);
   }

   public SlotDisplay<?> getTemplate() {
      return this.template;
   }

   public void setTemplate(SlotDisplay<?> template) {
      this.template = template;
   }

   public SlotDisplay<?> getBase() {
      return this.base;
   }

   public void setBase(SlotDisplay<?> base) {
      this.base = base;
   }

   public SlotDisplay<?> getAddition() {
      return this.addition;
   }

   public void setAddition(SlotDisplay<?> addition) {
      this.addition = addition;
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
      } else if (!(obj instanceof SmithingRecipeDisplay)) {
         return false;
      } else {
         SmithingRecipeDisplay that = (SmithingRecipeDisplay)obj;
         if (!this.template.equals(that.template)) {
            return false;
         } else if (!this.base.equals(that.base)) {
            return false;
         } else if (!this.addition.equals(that.addition)) {
            return false;
         } else {
            return !this.result.equals(that.result) ? false : this.craftingStation.equals(that.craftingStation);
         }
      }
   }

   public int hashCode() {
      return Objects.hash(new Object[]{this.template, this.base, this.addition, this.result, this.craftingStation});
   }

   public String toString() {
      return "SmithingRecipeDisplay{template=" + this.template + ", base=" + this.base + ", addition=" + this.addition + ", result=" + this.result + ", craftingStation=" + this.craftingStation + '}';
   }
}
