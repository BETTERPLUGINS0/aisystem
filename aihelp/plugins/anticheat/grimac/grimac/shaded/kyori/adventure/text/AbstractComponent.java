package ac.grim.grimac.shaded.kyori.adventure.text;

import ac.grim.grimac.shaded.jetbrains.annotations.ApiStatus;
import ac.grim.grimac.shaded.jetbrains.annotations.Debug;
import ac.grim.grimac.shaded.jetbrains.annotations.NotNull;
import ac.grim.grimac.shaded.jetbrains.annotations.Nullable;
import ac.grim.grimac.shaded.kyori.adventure.text.format.Style;
import ac.grim.grimac.shaded.kyori.examination.ExaminableProperty;
import ac.grim.grimac.shaded.kyori.examination.string.StringExaminer;
import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

/** @deprecated */
@Deprecated
@ApiStatus.ScheduledForRemoval(
   inVersion = "5.0.0"
)
@Debug.Renderer(
   text = "this.debuggerString()",
   childrenArray = "this.children().toArray()",
   hasChildren = "!this.children().isEmpty()"
)
public abstract class AbstractComponent implements Component {
   protected final List<Component> children;
   protected final Style style;

   protected AbstractComponent(@NotNull final List<? extends ComponentLike> children, @NotNull final Style style) {
      this.children = ComponentLike.asComponents(children, IS_NOT_EMPTY);
      this.style = style;
   }

   @NotNull
   public final List<Component> children() {
      return this.children;
   }

   @NotNull
   public final Style style() {
      return this.style;
   }

   public boolean equals(@Nullable final Object other) {
      if (this == other) {
         return true;
      } else if (!(other instanceof AbstractComponent)) {
         return false;
      } else {
         AbstractComponent that = (AbstractComponent)other;
         return Objects.equals(this.children, that.children) && Objects.equals(this.style, that.style);
      }
   }

   public int hashCode() {
      int result = this.children.hashCode();
      result = 31 * result + this.style.hashCode();
      return result;
   }

   public abstract String toString();

   private String debuggerString() {
      Stream<? extends ExaminableProperty> examinablePropertiesWithoutChildren = this.examinableProperties().filter((property) -> {
         return !property.name().equals("children");
      });
      return (String)StringExaminer.simpleEscaping().examine(this.examinableName(), examinablePropertiesWithoutChildren);
   }
}
