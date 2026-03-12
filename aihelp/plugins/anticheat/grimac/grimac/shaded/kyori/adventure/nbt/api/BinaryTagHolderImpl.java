package ac.grim.grimac.shaded.kyori.adventure.nbt.api;

import ac.grim.grimac.shaded.jetbrains.annotations.NotNull;
import ac.grim.grimac.shaded.kyori.adventure.util.Codec;
import java.util.Objects;

final class BinaryTagHolderImpl implements BinaryTagHolder {
   private final String string;

   BinaryTagHolderImpl(final String string) {
      this.string = (String)Objects.requireNonNull(string, "string");
   }

   @NotNull
   public String string() {
      return this.string;
   }

   @NotNull
   public <T, DX extends Exception> T get(@NotNull final Codec<T, String, DX, ?> codec) throws DX {
      return codec.decode(this.string);
   }

   public int hashCode() {
      return 31 * this.string.hashCode();
   }

   public boolean equals(final Object that) {
      return !(that instanceof BinaryTagHolderImpl) ? false : this.string.equals(((BinaryTagHolderImpl)that).string);
   }

   public String toString() {
      return this.string;
   }
}
