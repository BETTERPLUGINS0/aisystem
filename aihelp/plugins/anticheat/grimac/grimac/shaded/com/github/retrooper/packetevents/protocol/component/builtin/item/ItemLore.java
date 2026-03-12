package ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.component.builtin.item;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.PacketWrapper;
import ac.grim.grimac.shaded.kyori.adventure.text.Component;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class ItemLore {
   public static final ItemLore EMPTY = new ItemLore(Collections.emptyList());
   private List<Component> lines;

   public ItemLore(List<Component> lines) {
      this.lines = lines;
   }

   public static ItemLore read(PacketWrapper<?> wrapper) {
      List<Component> lines = wrapper.readList(PacketWrapper::readComponent);
      return new ItemLore(lines);
   }

   public static void write(PacketWrapper<?> wrapper, ItemLore lore) {
      wrapper.writeList(lore.lines, PacketWrapper::writeComponent);
   }

   public void addLine(Component line) {
      this.lines.add(line);
   }

   public List<Component> getLines() {
      return this.lines;
   }

   public void setLines(List<Component> lines) {
      this.lines = lines;
   }

   public boolean equals(Object obj) {
      if (this == obj) {
         return true;
      } else if (!(obj instanceof ItemLore)) {
         return false;
      } else {
         ItemLore itemLore = (ItemLore)obj;
         return this.lines.equals(itemLore.lines);
      }
   }

   public int hashCode() {
      return Objects.hash(new Object[]{this.lines});
   }

   public String toString() {
      return "ItemLore{lines=" + this.lines + '}';
   }
}
