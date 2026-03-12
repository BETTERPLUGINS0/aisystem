package ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.recipe.data;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.manager.server.ServerVersion;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.item.ItemStack;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.recipe.Ingredient;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.PacketWrapper;
import ac.grim.grimac.shaded.jetbrains.annotations.ApiStatus;
import ac.grim.grimac.shaded.jetbrains.annotations.UnknownNullability;

@ApiStatus.Obsolete
public class SmithingRecipeData implements RecipeData {
   @UnknownNullability
   private final Ingredient template;
   private final Ingredient base;
   private final Ingredient addition;
   private final ItemStack result;

   /** @deprecated */
   @Deprecated
   public SmithingRecipeData(Ingredient base, Ingredient addition, ItemStack result) {
      this((Ingredient)null, base, addition, result);
   }

   public SmithingRecipeData(@UnknownNullability Ingredient template, Ingredient base, Ingredient addition, ItemStack result) {
      this.template = template;
      this.base = base;
      this.addition = addition;
      this.result = result;
   }

   public static SmithingRecipeData read(PacketWrapper<?> wrapper) {
      return read(wrapper, false);
   }

   public static SmithingRecipeData read(PacketWrapper<?> wrapper, boolean legacy) {
      Ingredient template = !wrapper.getServerVersion().isNewerThanOrEquals(ServerVersion.V_1_20) && (!wrapper.getServerVersion().isNewerThanOrEquals(ServerVersion.V_1_19_4) || legacy) ? null : Ingredient.read(wrapper);
      Ingredient base = Ingredient.read(wrapper);
      Ingredient addition = Ingredient.read(wrapper);
      ItemStack result = wrapper.readItemStack();
      return new SmithingRecipeData(template, base, addition, result);
   }

   public static void write(PacketWrapper<?> wrapper, SmithingRecipeData data) {
      write(wrapper, data, false);
   }

   public static void write(PacketWrapper<?> wrapper, SmithingRecipeData data, boolean legacy) {
      if (wrapper.getServerVersion().isNewerThanOrEquals(ServerVersion.V_1_20) || wrapper.getServerVersion().isNewerThanOrEquals(ServerVersion.V_1_19_4) && !legacy) {
         Ingredient.write(wrapper, data.template);
      }

      Ingredient.write(wrapper, data.base);
      Ingredient.write(wrapper, data.addition);
      wrapper.writeItemStack(data.result);
   }

   @UnknownNullability
   public Ingredient getTemplate() {
      return this.template;
   }

   public Ingredient getBase() {
      return this.base;
   }

   public Ingredient getAddition() {
      return this.addition;
   }

   public ItemStack getResult() {
      return this.result;
   }
}
