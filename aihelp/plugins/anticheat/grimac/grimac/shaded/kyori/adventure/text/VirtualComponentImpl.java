package ac.grim.grimac.shaded.kyori.adventure.text;

import ac.grim.grimac.shaded.jetbrains.annotations.NotNull;
import ac.grim.grimac.shaded.jetbrains.annotations.Nullable;
import ac.grim.grimac.shaded.kyori.adventure.text.format.Style;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

final class VirtualComponentImpl<C> extends TextComponentImpl implements VirtualComponent {
   private final Class<C> contextType;
   private final VirtualComponentRenderer<C> renderer;

   static <C> VirtualComponent createVirtual(@NotNull final Class<C> contextType, @NotNull final VirtualComponentRenderer<C> renderer) {
      return createVirtual(contextType, renderer, Collections.emptyList(), Style.empty());
   }

   static <C> VirtualComponent createVirtual(@NotNull final Class<C> contextType, @NotNull final VirtualComponentRenderer<C> renderer, final List<? extends ComponentLike> children, final Style style) {
      List<Component> filteredChildren = ComponentLike.asComponents(children, IS_NOT_EMPTY);
      return new VirtualComponentImpl(filteredChildren, style, "", contextType, renderer);
   }

   private VirtualComponentImpl(@NotNull final List<Component> children, @NotNull final Style style, @NotNull final String content, @NotNull final Class<C> contextType, @NotNull final VirtualComponentRenderer<C> renderer) {
      super(children, style, content);
      this.contextType = contextType;
      this.renderer = renderer;
   }

   VirtualComponent create0(@NotNull final List<? extends ComponentLike> children, @NotNull final Style style, @NotNull final String content) {
      return new VirtualComponentImpl(ComponentLike.asComponents(children, IS_NOT_EMPTY), style, content, this.contextType, this.renderer);
   }

   @NotNull
   public Class<C> contextType() {
      return this.contextType;
   }

   @NotNull
   public VirtualComponentRenderer<C> renderer() {
      return this.renderer;
   }

   @NotNull
   public String content() {
      return this.renderer.fallbackString();
   }

   @NotNull
   public TextComponent.Builder toBuilder() {
      return new VirtualComponentImpl.BuilderImpl(this);
   }

   public boolean equals(@Nullable final Object other) {
      if (this == other) {
         return true;
      } else if (!(other instanceof VirtualComponentImpl)) {
         return false;
      } else if (!super.equals(other)) {
         return false;
      } else {
         VirtualComponentImpl<?> that = (VirtualComponentImpl)other;
         return Objects.equals(this.contextType, that.contextType) && Objects.equals(this.renderer, that.renderer);
      }
   }

   public int hashCode() {
      int result = super.hashCode();
      result = 31 * result + this.contextType.hashCode();
      result = 31 * result + this.renderer.hashCode();
      return result;
   }

   static final class BuilderImpl<C> extends TextComponentImpl.BuilderImpl {
      private final Class<C> contextType;
      private final VirtualComponentRenderer<C> renderer;

      BuilderImpl(final VirtualComponentImpl<C> other) {
         super(other);
         this.contextType = other.contextType();
         this.renderer = other.renderer();
      }

      @NotNull
      public TextComponent build() {
         return VirtualComponentImpl.createVirtual(this.contextType, this.renderer, this.children, this.buildStyle());
      }
   }
}
