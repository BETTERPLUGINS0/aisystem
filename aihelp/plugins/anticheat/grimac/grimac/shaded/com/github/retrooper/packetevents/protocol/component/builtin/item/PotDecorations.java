package ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.component.builtin.item;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.item.type.ItemType;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.item.type.ItemTypes;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.mapper.MappedEntity;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.PacketWrapper;
import ac.grim.grimac.shaded.jetbrains.annotations.Nullable;
import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Queue;

public class PotDecorations {
   @Nullable
   private ItemType back;
   @Nullable
   private ItemType left;
   @Nullable
   private ItemType right;
   @Nullable
   private ItemType front;

   private PotDecorations(Queue<Optional<ItemType>> items) {
      this(items.isEmpty() ? null : (ItemType)((Optional)items.remove()).orElse((Object)null), items.isEmpty() ? null : (ItemType)((Optional)items.remove()).orElse((Object)null), items.isEmpty() ? null : (ItemType)((Optional)items.remove()).orElse((Object)null), items.isEmpty() ? null : (ItemType)((Optional)items.remove()).orElse((Object)null));
   }

   public PotDecorations(@Nullable ItemType back, @Nullable ItemType left, @Nullable ItemType right, @Nullable ItemType front) {
      this.back = back;
      this.left = left;
      this.right = right;
      this.front = front;
   }

   private List<Optional<ItemType>> asList() {
      return Arrays.asList(Optional.ofNullable(this.back), Optional.ofNullable(this.left), Optional.ofNullable(this.right), Optional.ofNullable(this.front));
   }

   private static Optional<ItemType> readItem(PacketWrapper<?> wrapper) {
      ItemType type = (ItemType)wrapper.readMappedEntity(ItemTypes::getById);
      return type == ItemTypes.BRICK ? Optional.empty() : Optional.of(type);
   }

   public static PotDecorations read(PacketWrapper<?> wrapper) {
      Queue<Optional<ItemType>> items = (Queue)wrapper.readCollection(ArrayDeque::new, PotDecorations::readItem);
      return new PotDecorations(items);
   }

   private static void writeItem(PacketWrapper<?> wrapper, Optional<ItemType> type) {
      wrapper.writeMappedEntity((MappedEntity)type.orElse(ItemTypes.BRICK));
   }

   public static void write(PacketWrapper<?> wrapper, PotDecorations decorations) {
      wrapper.writeList(decorations.asList(), PotDecorations::writeItem);
   }

   @Nullable
   public ItemType getBack() {
      return this.back;
   }

   public void setBack(@Nullable ItemType back) {
      this.back = back;
   }

   @Nullable
   public ItemType getLeft() {
      return this.left;
   }

   public void setLeft(@Nullable ItemType left) {
      this.left = left;
   }

   @Nullable
   public ItemType getRight() {
      return this.right;
   }

   public void setRight(@Nullable ItemType right) {
      this.right = right;
   }

   @Nullable
   public ItemType getFront() {
      return this.front;
   }

   public void setFront(@Nullable ItemType front) {
      this.front = front;
   }

   public boolean equals(Object obj) {
      if (this == obj) {
         return true;
      } else if (!(obj instanceof PotDecorations)) {
         return false;
      } else {
         PotDecorations that = (PotDecorations)obj;
         if (!Objects.equals(this.back, that.back)) {
            return false;
         } else if (!Objects.equals(this.left, that.left)) {
            return false;
         } else {
            return !Objects.equals(this.right, that.right) ? false : Objects.equals(this.front, that.front);
         }
      }
   }

   public int hashCode() {
      return Objects.hash(new Object[]{this.back, this.left, this.right, this.front});
   }
}
