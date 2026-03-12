package fr.xephi.authme.libs.net.kyori.adventure.nbt;

import fr.xephi.authme.libs.net.kyori.examination.ExaminableProperty;
import java.util.stream.Stream;
import org.jetbrains.annotations.Debug;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Debug.Renderer(
   text = "\"\\\"\" + this.value + \"\\\"\"",
   hasChildren = "false"
)
final class StringBinaryTagImpl extends AbstractBinaryTag implements StringBinaryTag {
   private final String value;

   StringBinaryTagImpl(final String value) {
      this.value = value;
   }

   @NotNull
   public String value() {
      return this.value;
   }

   public boolean equals(@Nullable final Object other) {
      if (this == other) {
         return true;
      } else if (other != null && this.getClass() == other.getClass()) {
         StringBinaryTagImpl that = (StringBinaryTagImpl)other;
         return this.value.equals(that.value);
      } else {
         return false;
      }
   }

   public int hashCode() {
      return this.value.hashCode();
   }

   @NotNull
   public Stream<? extends ExaminableProperty> examinableProperties() {
      return Stream.of(ExaminableProperty.of("value", this.value));
   }
}
