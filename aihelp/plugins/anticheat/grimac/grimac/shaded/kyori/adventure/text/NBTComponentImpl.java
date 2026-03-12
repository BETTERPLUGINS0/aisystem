package ac.grim.grimac.shaded.kyori.adventure.text;

import ac.grim.grimac.shaded.jetbrains.annotations.NotNull;
import ac.grim.grimac.shaded.jetbrains.annotations.Nullable;
import ac.grim.grimac.shaded.kyori.adventure.text.format.Style;
import java.util.List;
import java.util.Objects;

abstract class NBTComponentImpl<C extends NBTComponent<C, B>, B extends NBTComponentBuilder<C, B>> extends AbstractComponent implements NBTComponent<C, B> {
   static final boolean INTERPRET_DEFAULT = false;
   final String nbtPath;
   final boolean interpret;
   @Nullable
   final Component separator;

   NBTComponentImpl(@NotNull final List<Component> children, @NotNull final Style style, final String nbtPath, final boolean interpret, @Nullable final Component separator) {
      super(children, style);
      this.nbtPath = nbtPath;
      this.interpret = interpret;
      this.separator = separator;
   }

   @NotNull
   public String nbtPath() {
      return this.nbtPath;
   }

   public boolean interpret() {
      return this.interpret;
   }

   public boolean equals(@Nullable final Object other) {
      if (this == other) {
         return true;
      } else if (!(other instanceof NBTComponent)) {
         return false;
      } else if (!super.equals(other)) {
         return false;
      } else {
         NBTComponent<?, ?> that = (NBTComponent)other;
         return Objects.equals(this.nbtPath, that.nbtPath()) && this.interpret == that.interpret() && Objects.equals(this.separator, that.separator());
      }
   }

   public int hashCode() {
      int result = super.hashCode();
      result = 31 * result + this.nbtPath.hashCode();
      result = 31 * result + Boolean.hashCode(this.interpret);
      result = 31 * result + Objects.hashCode(this.separator);
      return result;
   }
}
