package ac.grim.grimac.shaded.com.github.retrooper.packetevents.util.adventure;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.nbt.NBT;
import ac.grim.grimac.shaded.kyori.adventure.nbt.api.BinaryTagHolder;
import ac.grim.grimac.shaded.kyori.adventure.util.Codec;
import org.jspecify.annotations.NullMarked;

@NullMarked
public final class NbtTagHolder implements BinaryTagHolder {
   private final NBT tag;

   public NbtTagHolder(NBT tag) {
      this.tag = tag;
   }

   public String string() {
      return AdventureNbtUtil.toString(this.tag);
   }

   public <T, DX extends Exception> T get(Codec<T, String, DX, ?> codec) throws DX {
      return codec.decode(this.string());
   }

   public NBT getTag() {
      return this.tag;
   }
}
