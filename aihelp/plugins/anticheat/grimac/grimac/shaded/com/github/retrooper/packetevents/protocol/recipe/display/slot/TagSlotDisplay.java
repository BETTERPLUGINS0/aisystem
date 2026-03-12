package ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.recipe.display.slot;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.resources.ResourceLocation;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.PacketWrapper;
import java.util.Objects;

public class TagSlotDisplay extends SlotDisplay<TagSlotDisplay> {
   private ResourceLocation itemTag;

   public TagSlotDisplay(ResourceLocation itemTag) {
      super(SlotDisplayTypes.TAG);
      this.itemTag = itemTag;
   }

   public static TagSlotDisplay read(PacketWrapper<?> wrapper) {
      ResourceLocation itemTag = wrapper.readIdentifier();
      return new TagSlotDisplay(itemTag);
   }

   public static void write(PacketWrapper<?> wrapper, TagSlotDisplay display) {
      wrapper.writeIdentifier(display.itemTag);
   }

   public ResourceLocation getItemTag() {
      return this.itemTag;
   }

   public void setItemTag(ResourceLocation itemTag) {
      this.itemTag = itemTag;
   }

   public boolean equals(Object obj) {
      if (this == obj) {
         return true;
      } else if (!(obj instanceof TagSlotDisplay)) {
         return false;
      } else {
         TagSlotDisplay that = (TagSlotDisplay)obj;
         return this.itemTag.equals(that.itemTag);
      }
   }

   public int hashCode() {
      return Objects.hashCode(this.itemTag);
   }

   public String toString() {
      return "TagSlotDisplay{itemTag=" + this.itemTag + '}';
   }
}
