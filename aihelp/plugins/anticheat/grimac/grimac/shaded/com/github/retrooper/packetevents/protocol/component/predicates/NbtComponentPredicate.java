package ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.component.predicates;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.nbt.NBT;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.PacketWrapper;
import ac.grim.grimac.shaded.jetbrains.annotations.ApiStatus;
import java.util.Objects;

@ApiStatus.Experimental
public class NbtComponentPredicate implements IComponentPredicate {
   private final NBT tag;

   public NbtComponentPredicate(NBT tag) {
      this.tag = tag;
   }

   public static NbtComponentPredicate read(PacketWrapper<?> wrapper) {
      NBT tag = wrapper.readNBTRaw();
      return new NbtComponentPredicate(tag);
   }

   public static void write(PacketWrapper<?> wrapper, NbtComponentPredicate predicate) {
      wrapper.writeNBTRaw(predicate.tag);
   }

   public NBT getTag() {
      return this.tag;
   }

   public boolean equals(Object obj) {
      if (!(obj instanceof NbtComponentPredicate)) {
         return false;
      } else {
         NbtComponentPredicate that = (NbtComponentPredicate)obj;
         return this.tag.equals(that.tag);
      }
   }

   public int hashCode() {
      return Objects.hashCode(this.tag);
   }
}
