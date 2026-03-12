package ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.recipe.data;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.recipe.Ingredient;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.PacketWrapper;
import ac.grim.grimac.shaded.jetbrains.annotations.ApiStatus;

@ApiStatus.Obsolete
public class SmithingTrimRecipeData implements RecipeData {
   private final Ingredient template;
   private final Ingredient base;
   private final Ingredient addition;

   public SmithingTrimRecipeData(Ingredient template, Ingredient base, Ingredient addition) {
      this.template = template;
      this.base = base;
      this.addition = addition;
   }

   public static SmithingTrimRecipeData read(PacketWrapper<?> wrapper) {
      Ingredient template = Ingredient.read(wrapper);
      Ingredient base = Ingredient.read(wrapper);
      Ingredient addition = Ingredient.read(wrapper);
      return new SmithingTrimRecipeData(template, base, addition);
   }

   public static void write(PacketWrapper<?> wrapper, SmithingTrimRecipeData data) {
      Ingredient.write(wrapper, data.template);
      Ingredient.write(wrapper, data.base);
      Ingredient.write(wrapper, data.addition);
   }

   public Ingredient getTemplate() {
      return this.template;
   }

   public Ingredient getBase() {
      return this.base;
   }

   public Ingredient getAddition() {
      return this.addition;
   }
}
