package ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.recipe;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.item.type.ItemType;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.item.type.ItemTypes;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.mapper.MappedEntitySet;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.recipe.category.RecipeBookCategories;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.recipe.category.RecipeBookCategory;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.recipe.display.RecipeDisplay;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.util.mappings.IRegistry;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.PacketWrapper;
import ac.grim.grimac.shaded.jetbrains.annotations.Nullable;
import java.util.List;
import java.util.Objects;

public final class RecipeDisplayEntry {
   private RecipeDisplayId id;
   private RecipeDisplay<?> display;
   @Nullable
   private Integer group;
   private RecipeBookCategory category;
   @Nullable
   private List<MappedEntitySet<ItemType>> ingredients;

   public RecipeDisplayEntry(RecipeDisplayId id, RecipeDisplay<?> display, @Nullable Integer group, RecipeBookCategory category, @Nullable List<MappedEntitySet<ItemType>> ingredients) {
      this.id = id;
      this.display = display;
      this.group = group;
      this.category = category;
      this.ingredients = ingredients;
   }

   public static RecipeDisplayEntry read(PacketWrapper<?> wrapper) {
      RecipeDisplayId id = RecipeDisplayId.read(wrapper);
      RecipeDisplay<?> display = RecipeDisplay.read(wrapper);
      Integer group = wrapper.readNullableVarInt();
      RecipeBookCategory category = (RecipeBookCategory)wrapper.readMappedEntity((IRegistry)RecipeBookCategories.getRegistry());
      List<MappedEntitySet<ItemType>> ingredients = (List)wrapper.readOptional((ew) -> {
         return ew.readList((eww) -> {
            return MappedEntitySet.read(eww, ItemTypes.getRegistry());
         });
      });
      return new RecipeDisplayEntry(id, display, group, category, ingredients);
   }

   public static void write(PacketWrapper<?> wrapper, RecipeDisplayEntry entry) {
      RecipeDisplayId.write(wrapper, entry.id);
      RecipeDisplay.write(wrapper, entry.display);
      wrapper.writeNullableVarInt(entry.group);
      wrapper.writeMappedEntity(entry.category);
      wrapper.writeOptional(entry.ingredients, (ew, list) -> {
         ew.writeList(list, MappedEntitySet::write);
      });
   }

   public RecipeDisplayId getId() {
      return this.id;
   }

   public void setId(RecipeDisplayId id) {
      this.id = id;
   }

   public RecipeDisplay<?> getDisplay() {
      return this.display;
   }

   public void setDisplay(RecipeDisplay<?> display) {
      this.display = display;
   }

   @Nullable
   public Integer getGroup() {
      return this.group;
   }

   public void setGroup(@Nullable Integer group) {
      this.group = group;
   }

   public RecipeBookCategory getCategory() {
      return this.category;
   }

   public void setCategory(RecipeBookCategory category) {
      this.category = category;
   }

   @Nullable
   public List<MappedEntitySet<ItemType>> getIngredients() {
      return this.ingredients;
   }

   public void setIngredients(@Nullable List<MappedEntitySet<ItemType>> ingredients) {
      this.ingredients = ingredients;
   }

   public boolean equals(Object obj) {
      if (this == obj) {
         return true;
      } else if (!(obj instanceof RecipeDisplayEntry)) {
         return false;
      } else {
         RecipeDisplayEntry that = (RecipeDisplayEntry)obj;
         if (this.id != that.id) {
            return false;
         } else if (!this.display.equals(that.display)) {
            return false;
         } else if (!Objects.equals(this.group, that.group)) {
            return false;
         } else {
            return !this.category.equals(that.category) ? false : Objects.equals(this.ingredients, that.ingredients);
         }
      }
   }

   public int hashCode() {
      return Objects.hash(new Object[]{this.id, this.display, this.group, this.category, this.ingredients});
   }

   public String toString() {
      return "RecipeDisplayEntry{id=" + this.id + ", display=" + this.display + ", group=" + this.group + ", category=" + this.category + ", ingredients=" + this.ingredients + '}';
   }
}
