package fr.xephi.authme.libs.net.kyori.adventure.nbt.api;

import fr.xephi.authme.libs.net.kyori.adventure.text.event.DataComponentValue;
import fr.xephi.authme.libs.net.kyori.adventure.util.Codec;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

public interface BinaryTagHolder extends DataComponentValue.TagSerializable {
   @NotNull
   static <T, EX extends Exception> BinaryTagHolder encode(@NotNull final T nbt, @NotNull final Codec<? super T, String, ?, EX> codec) throws EX {
      return new BinaryTagHolderImpl((String)codec.encode(nbt));
   }

   @NotNull
   static BinaryTagHolder binaryTagHolder(@NotNull final String string) {
      return new BinaryTagHolderImpl(string);
   }

   /** @deprecated */
   @Deprecated
   @ApiStatus.ScheduledForRemoval(
      inVersion = "5.0.0"
   )
   @NotNull
   static BinaryTagHolder of(@NotNull final String string) {
      return new BinaryTagHolderImpl(string);
   }

   @NotNull
   String string();

   @NotNull
   default BinaryTagHolder asBinaryTag() {
      return this;
   }

   @NotNull
   <T, DX extends Exception> T get(@NotNull final Codec<T, String, DX, ?> codec) throws DX;
}
