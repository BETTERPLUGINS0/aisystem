package fr.xephi.authme.libs.net.kyori.adventure.chat;

import fr.xephi.authme.libs.net.kyori.adventure.identity.Identified;
import fr.xephi.authme.libs.net.kyori.adventure.identity.Identity;
import fr.xephi.authme.libs.net.kyori.adventure.text.Component;
import fr.xephi.authme.libs.net.kyori.adventure.text.ComponentLike;
import fr.xephi.authme.libs.net.kyori.examination.Examinable;
import fr.xephi.authme.libs.net.kyori.examination.ExaminableProperty;
import java.time.Instant;
import java.util.stream.Stream;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@ApiStatus.NonExtendable
public interface SignedMessage extends Identified, Examinable {
   @Contract(
      value = "_ -> new",
      pure = true
   )
   @NotNull
   static SignedMessage.Signature signature(final byte[] signature) {
      return new SignedMessageImpl.SignatureImpl(signature);
   }

   @Contract(
      value = "_, _ -> new",
      pure = true
   )
   @NotNull
   static SignedMessage system(@NotNull final String message, @Nullable final ComponentLike unsignedContent) {
      return new SignedMessageImpl(message, ComponentLike.unbox(unsignedContent));
   }

   @Contract(
      pure = true
   )
   @NotNull
   Instant timestamp();

   @Contract(
      pure = true
   )
   long salt();

   @Contract(
      pure = true
   )
   @Nullable
   SignedMessage.Signature signature();

   @Contract(
      pure = true
   )
   @Nullable
   Component unsignedContent();

   @Contract(
      pure = true
   )
   @NotNull
   String message();

   @Contract(
      pure = true
   )
   default boolean isSystem() {
      return this.identity() == Identity.nil();
   }

   @Contract(
      pure = true
   )
   default boolean canDelete() {
      return this.signature() != null;
   }

   @NotNull
   default Stream<? extends ExaminableProperty> examinableProperties() {
      return Stream.of(ExaminableProperty.of("timestamp", (Object)this.timestamp()), ExaminableProperty.of("salt", this.salt()), ExaminableProperty.of("signature", (Object)this.signature()), ExaminableProperty.of("unsignedContent", (Object)this.unsignedContent()), ExaminableProperty.of("message", this.message()));
   }

   @ApiStatus.NonExtendable
   public interface Signature extends Examinable {
      @Contract(
         pure = true
      )
      byte[] bytes();

      @NotNull
      default Stream<? extends ExaminableProperty> examinableProperties() {
         return Stream.of(ExaminableProperty.of("bytes", this.bytes()));
      }
   }
}
