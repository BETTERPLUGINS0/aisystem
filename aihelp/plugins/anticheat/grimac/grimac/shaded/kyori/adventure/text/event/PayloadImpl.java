package ac.grim.grimac.shaded.kyori.adventure.text.event;

import ac.grim.grimac.shaded.jetbrains.annotations.NotNull;
import ac.grim.grimac.shaded.kyori.adventure.dialog.DialogLike;
import ac.grim.grimac.shaded.kyori.adventure.internal.Internals;
import ac.grim.grimac.shaded.kyori.adventure.key.Key;
import ac.grim.grimac.shaded.kyori.adventure.nbt.api.BinaryTagHolder;
import ac.grim.grimac.shaded.kyori.examination.ExaminableProperty;
import java.util.Objects;
import java.util.stream.Stream;

abstract class PayloadImpl implements ClickEvent.Payload {
   public String toString() {
      return Internals.toString(this);
   }

   static final class CustomImpl extends PayloadImpl implements ClickEvent.Payload.Custom {
      private final Key key;
      private final BinaryTagHolder nbt;

      CustomImpl(@NotNull final Key key, @NotNull final BinaryTagHolder nbt) {
         this.key = key;
         this.nbt = nbt;
      }

      @NotNull
      public Key key() {
         return this.key;
      }

      @NotNull
      public String data() {
         return this.nbt.string();
      }

      @NotNull
      public BinaryTagHolder nbt() {
         return this.nbt;
      }

      @NotNull
      public Stream<? extends ExaminableProperty> examinableProperties() {
         return Stream.of(ExaminableProperty.of("key", (Object)this.key), ExaminableProperty.of("nbt", (Object)this.nbt));
      }

      public boolean equals(final Object other) {
         if (this == other) {
            return true;
         } else if (other != null && this.getClass() == other.getClass()) {
            PayloadImpl.CustomImpl that = (PayloadImpl.CustomImpl)other;
            return Objects.equals(this.key, that.key) && Objects.equals(this.nbt, that.nbt);
         } else {
            return false;
         }
      }

      public int hashCode() {
         int result = this.key.hashCode();
         result = 31 * result + this.nbt.hashCode();
         return result;
      }
   }

   static final class DialogImpl extends PayloadImpl implements ClickEvent.Payload.Dialog {
      private final DialogLike dialogLike;

      DialogImpl(@NotNull final DialogLike dialogLike) {
         this.dialogLike = dialogLike;
      }

      @NotNull
      public DialogLike dialog() {
         return this.dialogLike;
      }

      @NotNull
      public Stream<? extends ExaminableProperty> examinableProperties() {
         return Stream.of(ExaminableProperty.of("dialog", (Object)this.dialogLike));
      }

      public boolean equals(final Object other) {
         if (this == other) {
            return true;
         } else if (other != null && this.getClass() == other.getClass()) {
            PayloadImpl.DialogImpl that = (PayloadImpl.DialogImpl)other;
            return Objects.equals(this.dialogLike, that.dialogLike);
         } else {
            return false;
         }
      }

      public int hashCode() {
         return this.dialogLike.hashCode();
      }
   }

   static final class IntImpl extends PayloadImpl implements ClickEvent.Payload.Int {
      private final int integer;

      IntImpl(final int integer) {
         this.integer = integer;
      }

      public int integer() {
         return this.integer;
      }

      @NotNull
      public Stream<? extends ExaminableProperty> examinableProperties() {
         return Stream.of(ExaminableProperty.of("integer", this.integer));
      }

      public boolean equals(final Object other) {
         if (this == other) {
            return true;
         } else if (other != null && this.getClass() == other.getClass()) {
            PayloadImpl.IntImpl that = (PayloadImpl.IntImpl)other;
            return Objects.equals(this.integer, that.integer);
         } else {
            return false;
         }
      }

      public int hashCode() {
         return this.integer;
      }
   }

   static final class TextImpl extends PayloadImpl implements ClickEvent.Payload.Text {
      private final String value;

      TextImpl(@NotNull final String value) {
         this.value = value;
      }

      @NotNull
      public String value() {
         return this.value;
      }

      @NotNull
      public Stream<? extends ExaminableProperty> examinableProperties() {
         return Stream.of(ExaminableProperty.of("value", this.value));
      }

      public boolean equals(final Object other) {
         if (this == other) {
            return true;
         } else if (other != null && this.getClass() == other.getClass()) {
            PayloadImpl.TextImpl that = (PayloadImpl.TextImpl)other;
            return Objects.equals(this.value, that.value);
         } else {
            return false;
         }
      }

      public int hashCode() {
         return this.value.hashCode();
      }
   }
}
