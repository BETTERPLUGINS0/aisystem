package ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.component.builtin.item;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.nbt.NBTList;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.nbt.NBTString;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.resources.ResourceLocation;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.PacketWrapper;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;

public class ItemRecipes {
   private List<ResourceLocation> recipes;

   public ItemRecipes(List<ResourceLocation> recipes) {
      this.recipes = recipes;
   }

   public static ItemRecipes read(PacketWrapper<?> wrapper) {
      NBTList<?> recipes = (NBTList)wrapper.readNBTRaw();
      int size = recipes.size();
      List<ResourceLocation> recipeKeys = new ArrayList(size);

      for(int i = 0; i < size; ++i) {
         NBTString tag = (NBTString)recipes.getTag(i);
         recipeKeys.add(new ResourceLocation(tag.getValue()));
      }

      return new ItemRecipes(recipeKeys);
   }

   public static void write(PacketWrapper<?> wrapper, ItemRecipes recipes) {
      NBTList<NBTString> recipesTag = NBTList.createStringList();
      Iterator var3 = recipes.recipes.iterator();

      while(var3.hasNext()) {
         ResourceLocation recipeKey = (ResourceLocation)var3.next();
         recipesTag.addTag(new NBTString(recipeKey.toString()));
      }

      wrapper.writeNBTRaw(recipesTag);
   }

   public void addRecipe(ResourceLocation recipeKey) {
      this.recipes.add(recipeKey);
   }

   public void removeRecipe(ResourceLocation recipeKey) {
      this.recipes.remove(recipeKey);
   }

   public List<ResourceLocation> getRecipes() {
      return this.recipes;
   }

   public void setRecipes(List<ResourceLocation> recipes) {
      this.recipes = recipes;
   }

   public boolean equals(Object obj) {
      if (this == obj) {
         return true;
      } else if (!(obj instanceof ItemRecipes)) {
         return false;
      } else {
         ItemRecipes that = (ItemRecipes)obj;
         return this.recipes.equals(that.recipes);
      }
   }

   public int hashCode() {
      return Objects.hashCode(this.recipes);
   }
}
