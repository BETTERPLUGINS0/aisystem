package ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.component.builtin.item;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.resources.ResourceLocation;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.PacketWrapper;
import java.util.Objects;

public class ItemModel {
   private ResourceLocation modelLocation;

   public ItemModel(ResourceLocation modelLocation) {
      this.modelLocation = modelLocation;
   }

   public static ItemModel read(PacketWrapper<?> wrapper) {
      return new ItemModel(wrapper.readIdentifier());
   }

   public static void write(PacketWrapper<?> wrapper, ItemModel model) {
      wrapper.writeIdentifier(model.modelLocation);
   }

   public ResourceLocation getModelLocation() {
      return this.modelLocation;
   }

   public void setModelLocation(ResourceLocation modelLocation) {
      this.modelLocation = modelLocation;
   }

   public boolean equals(Object obj) {
      if (this == obj) {
         return true;
      } else if (!(obj instanceof ItemModel)) {
         return false;
      } else {
         ItemModel itemModel = (ItemModel)obj;
         return this.modelLocation.equals(itemModel.modelLocation);
      }
   }

   public int hashCode() {
      return Objects.hashCode(this.modelLocation);
   }

   public String toString() {
      return "ItemModel{modelLocation=" + this.modelLocation + '}';
   }
}
