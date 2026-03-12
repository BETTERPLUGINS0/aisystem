package fr.xephi.authme.libs.net.kyori.adventure.text.minimessage.tag;

import fr.xephi.authme.libs.net.kyori.adventure.text.Component;
import fr.xephi.authme.libs.net.kyori.examination.ExaminableProperty;
import java.util.Objects;
import java.util.stream.Stream;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

final class InsertingImpl extends AbstractTag implements Inserting {
   private final boolean allowsChildren;
   private final Component value;

   InsertingImpl(final boolean allowsChildren, final Component value) {
      this.allowsChildren = allowsChildren;
      this.value = value;
   }

   public boolean allowsChildren() {
      return this.allowsChildren;
   }

   @NotNull
   public Component value() {
      return this.value;
   }

   public int hashCode() {
      return Objects.hash(new Object[]{this.allowsChildren, this.value});
   }

   public boolean equals(@Nullable final Object other) {
      if (this == other) {
         return true;
      } else if (!(other instanceof InsertingImpl)) {
         return false;
      } else {
         InsertingImpl that = (InsertingImpl)other;
         return this.allowsChildren == that.allowsChildren && Objects.equals(this.value, that.value);
      }
   }

   @NotNull
   public Stream<? extends ExaminableProperty> examinableProperties() {
      return Stream.of(ExaminableProperty.of("allowsChildren", this.allowsChildren), ExaminableProperty.of("value", (Object)this.value));
   }
}
