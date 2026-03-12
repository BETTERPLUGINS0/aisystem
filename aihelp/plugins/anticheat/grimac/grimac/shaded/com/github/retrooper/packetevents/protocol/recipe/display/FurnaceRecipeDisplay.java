package ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.recipe.display;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.recipe.display.slot.SlotDisplay;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.PacketWrapper;
import java.util.Objects;

public class FurnaceRecipeDisplay extends RecipeDisplay<FurnaceRecipeDisplay> {
   private SlotDisplay<?> ingredient;
   private SlotDisplay<?> fuel;
   private SlotDisplay<?> result;
   private SlotDisplay<?> craftingStation;
   private int duration;
   private float experience;

   public FurnaceRecipeDisplay(SlotDisplay<?> ingredient, SlotDisplay<?> fuel, SlotDisplay<?> result, SlotDisplay<?> craftingStation, int duration, float experience) {
      super(RecipeDisplayTypes.FURNACE);
      this.ingredient = ingredient;
      this.fuel = fuel;
      this.result = result;
      this.craftingStation = craftingStation;
      this.duration = duration;
      this.experience = experience;
   }

   public static FurnaceRecipeDisplay read(PacketWrapper<?> wrapper) {
      SlotDisplay<?> ingredient = SlotDisplay.read(wrapper);
      SlotDisplay<?> fuel = SlotDisplay.read(wrapper);
      SlotDisplay<?> result = SlotDisplay.read(wrapper);
      SlotDisplay<?> craftingStation = SlotDisplay.read(wrapper);
      int duration = wrapper.readVarInt();
      float experience = wrapper.readFloat();
      return new FurnaceRecipeDisplay(ingredient, fuel, result, craftingStation, duration, experience);
   }

   public static void write(PacketWrapper<?> wrapper, FurnaceRecipeDisplay display) {
      SlotDisplay.write(wrapper, display.ingredient);
      SlotDisplay.write(wrapper, display.fuel);
      SlotDisplay.write(wrapper, display.result);
      SlotDisplay.write(wrapper, display.craftingStation);
      wrapper.writeVarInt(display.duration);
      wrapper.writeFloat(display.experience);
   }

   public SlotDisplay<?> getIngredient() {
      return this.ingredient;
   }

   public void setIngredient(SlotDisplay<?> ingredient) {
      this.ingredient = ingredient;
   }

   public SlotDisplay<?> getFuel() {
      return this.fuel;
   }

   public void setFuel(SlotDisplay<?> fuel) {
      this.fuel = fuel;
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

   public int getDuration() {
      return this.duration;
   }

   public void setDuration(int duration) {
      this.duration = duration;
   }

   public float getExperience() {
      return this.experience;
   }

   public void setExperience(float experience) {
      this.experience = experience;
   }

   public boolean equals(Object obj) {
      if (this == obj) {
         return true;
      } else if (!(obj instanceof FurnaceRecipeDisplay)) {
         return false;
      } else {
         FurnaceRecipeDisplay that = (FurnaceRecipeDisplay)obj;
         if (this.duration != that.duration) {
            return false;
         } else if (Float.compare(that.experience, this.experience) != 0) {
            return false;
         } else if (!this.ingredient.equals(that.ingredient)) {
            return false;
         } else if (!this.fuel.equals(that.fuel)) {
            return false;
         } else {
            return !this.result.equals(that.result) ? false : this.craftingStation.equals(that.craftingStation);
         }
      }
   }

   public int hashCode() {
      return Objects.hash(new Object[]{this.ingredient, this.fuel, this.result, this.craftingStation, this.duration, this.experience});
   }

   public String toString() {
      return "FurnaceRecipeDisplay{ingredient=" + this.ingredient + ", fuel=" + this.fuel + ", result=" + this.result + ", craftingStation=" + this.craftingStation + ", duration=" + this.duration + ", experience=" + this.experience + '}';
   }
}
