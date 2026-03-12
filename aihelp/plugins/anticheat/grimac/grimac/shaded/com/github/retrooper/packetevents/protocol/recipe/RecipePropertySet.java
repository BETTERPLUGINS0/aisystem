package ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.recipe;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.item.type.ItemType;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.item.type.ItemTypes;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.resources.ResourceLocation;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.util.mappings.IRegistry;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.PacketWrapper;
import java.util.LinkedHashSet;
import java.util.Set;

public final class RecipePropertySet {
   public static final ResourceLocation SMITHING_BASE = ResourceLocation.minecraft("smithing_base");
   public static final ResourceLocation SMITHING_TEMPLATE = ResourceLocation.minecraft("smithing_template");
   public static final ResourceLocation SMITHING_ADDITION = ResourceLocation.minecraft("smithing_addition");
   public static final ResourceLocation FURNACE_INPUT = ResourceLocation.minecraft("furnace_input");
   public static final ResourceLocation BLAST_FURNACE_INPUT = ResourceLocation.minecraft("blast_furnace_input");
   public static final ResourceLocation SMOKER_INPUT = ResourceLocation.minecraft("smoker_input");
   public static final ResourceLocation CAMPFIRE_INPUT = ResourceLocation.minecraft("campfire_input");
   private Set<ItemType> items;

   public RecipePropertySet(Set<ItemType> items) {
      this.items = items;
   }

   public static RecipePropertySet read(PacketWrapper<?> wrapper) {
      LinkedHashSet<ItemType> items = (LinkedHashSet)wrapper.readCollection(LinkedHashSet::new, (ew) -> {
         return (ItemType)ew.readMappedEntity((IRegistry)ItemTypes.getRegistry());
      });
      return new RecipePropertySet(items);
   }

   public static void write(PacketWrapper<?> wrapper, RecipePropertySet set) {
      wrapper.writeCollection(set.items, PacketWrapper::writeMappedEntity);
   }

   public Set<ItemType> getItems() {
      return this.items;
   }

   public void setItems(Set<ItemType> items) {
      this.items = items;
   }
}
