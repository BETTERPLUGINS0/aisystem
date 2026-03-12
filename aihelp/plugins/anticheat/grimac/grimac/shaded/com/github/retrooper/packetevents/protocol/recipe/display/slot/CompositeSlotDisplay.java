package ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.recipe.display.slot;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.PacketWrapper;
import java.util.List;
import java.util.Objects;

public class CompositeSlotDisplay extends SlotDisplay<CompositeSlotDisplay> {
   private List<SlotDisplay<?>> contents;

   public CompositeSlotDisplay(List<SlotDisplay<?>> contents) {
      super(SlotDisplayTypes.COMPOSITE);
      this.contents = contents;
   }

   public static CompositeSlotDisplay read(PacketWrapper<?> wrapper) {
      List<SlotDisplay<?>> contents = wrapper.readList(SlotDisplay::read);
      return new CompositeSlotDisplay(contents);
   }

   public static void write(PacketWrapper<?> wrapper, CompositeSlotDisplay display) {
      wrapper.writeList(display.contents, SlotDisplay::write);
   }

   public List<SlotDisplay<?>> getContents() {
      return this.contents;
   }

   public void setContents(List<SlotDisplay<?>> contents) {
      this.contents = contents;
   }

   public boolean equals(Object obj) {
      if (this == obj) {
         return true;
      } else if (!(obj instanceof CompositeSlotDisplay)) {
         return false;
      } else {
         CompositeSlotDisplay that = (CompositeSlotDisplay)obj;
         return this.contents.equals(that.contents);
      }
   }

   public int hashCode() {
      return Objects.hashCode(this.contents);
   }

   public String toString() {
      return "CompositeSlotDisplay{contents=" + this.contents + '}';
   }
}
