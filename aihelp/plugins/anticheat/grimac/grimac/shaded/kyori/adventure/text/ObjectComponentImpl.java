package ac.grim.grimac.shaded.kyori.adventure.text;

import ac.grim.grimac.shaded.jetbrains.annotations.NotNull;
import ac.grim.grimac.shaded.jetbrains.annotations.Nullable;
import ac.grim.grimac.shaded.kyori.adventure.internal.Internals;
import ac.grim.grimac.shaded.kyori.adventure.text.format.Style;
import ac.grim.grimac.shaded.kyori.adventure.text.object.ObjectContents;
import java.util.List;
import java.util.Objects;

final class ObjectComponentImpl extends AbstractComponent implements ObjectComponent {
   private final ObjectContents contents;

   private ObjectComponentImpl(@NotNull final List<Component> children, @NotNull final Style style, @NotNull final ObjectContents contents) {
      super(children, style);
      this.contents = contents;
   }

   @NotNull
   public ObjectContents contents() {
      return this.contents;
   }

   @NotNull
   public ObjectComponent contents(@NotNull final ObjectContents contents) {
      return create(this.children, this.style, contents);
   }

   public boolean equals(@Nullable final Object other) {
      if (this == other) {
         return true;
      } else if (!(other instanceof ObjectComponent)) {
         return false;
      } else if (!super.equals(other)) {
         return false;
      } else {
         ObjectComponentImpl that = (ObjectComponentImpl)other;
         return Objects.equals(this.contents, that.contents());
      }
   }

   public int hashCode() {
      int result = super.hashCode();
      result = 31 * result + this.contents.hashCode();
      return result;
   }

   public String toString() {
      return Internals.toString(this);
   }

   @NotNull
   public ObjectComponent.Builder toBuilder() {
      return new ObjectComponentImpl.BuilderImpl(this);
   }

   @NotNull
   static ObjectComponentImpl create(@NotNull final List<? extends ComponentLike> children, @NotNull final Style style, @NotNull final ObjectContents objectContents) {
      return new ObjectComponentImpl(ComponentLike.asComponents(children, IS_NOT_EMPTY), (Style)Objects.requireNonNull(style, "style"), (ObjectContents)Objects.requireNonNull(objectContents, "contents"));
   }

   @NotNull
   public ObjectComponent children(@NotNull final List<? extends ComponentLike> children) {
      return create(children, this.style, this.contents);
   }

   @NotNull
   public ObjectComponent style(@NotNull final Style style) {
      return create(this.children, style, this.contents);
   }

   static final class BuilderImpl extends AbstractComponentBuilder<ObjectComponent, ObjectComponent.Builder> implements ObjectComponent.Builder {
      private ObjectContents objectContents;

      BuilderImpl() {
      }

      BuilderImpl(@NotNull final ObjectComponent component) {
         super(component);
         this.objectContents = component.contents();
      }

      @NotNull
      public ObjectComponent.Builder contents(@NotNull final ObjectContents objectContents) {
         this.objectContents = (ObjectContents)Objects.requireNonNull(objectContents, "contents");
         return this;
      }

      @NotNull
      public ObjectComponent build() {
         if (this.objectContents == null) {
            throw new IllegalStateException("contents must be set");
         } else {
            return ObjectComponentImpl.create(this.children, this.buildStyle(), this.objectContents);
         }
      }
   }
}
