package fr.xephi.authme.libs.net.kyori.adventure.util;

import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

public interface Codec<D, E, DX extends Throwable, EX extends Throwable> {
   @NotNull
   static <D, E, DX extends Throwable, EX extends Throwable> Codec<D, E, DX, EX> codec(@NotNull final Codec.Decoder<D, E, DX> decoder, @NotNull final Codec.Encoder<D, E, EX> encoder) {
      return new Codec<D, E, DX, EX>() {
         @NotNull
         public D decode(@NotNull final E encoded) throws DX {
            return decoder.decode(encoded);
         }

         @NotNull
         public E encode(@NotNull final D decoded) throws EX {
            return encoder.encode(decoded);
         }
      };
   }

   /** @deprecated */
   @Deprecated
   @ApiStatus.ScheduledForRemoval(
      inVersion = "5.0.0"
   )
   @NotNull
   static <D, E, DX extends Throwable, EX extends Throwable> Codec<D, E, DX, EX> of(@NotNull final Codec.Decoder<D, E, DX> decoder, @NotNull final Codec.Encoder<D, E, EX> encoder) {
      return new Codec<D, E, DX, EX>() {
         @NotNull
         public D decode(@NotNull final E encoded) throws DX {
            return decoder.decode(encoded);
         }

         @NotNull
         public E encode(@NotNull final D decoded) throws EX {
            return encoder.encode(decoded);
         }
      };
   }

   @NotNull
   D decode(@NotNull final E encoded) throws DX;

   @NotNull
   E encode(@NotNull final D decoded) throws EX;

   public interface Encoder<D, E, X extends Throwable> {
      @NotNull
      E encode(@NotNull final D decoded) throws X;
   }

   public interface Decoder<D, E, X extends Throwable> {
      @NotNull
      D decode(@NotNull final E encoded) throws X;
   }
}
