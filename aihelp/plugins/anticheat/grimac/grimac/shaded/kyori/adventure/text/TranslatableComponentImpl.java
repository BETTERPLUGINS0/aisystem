package ac.grim.grimac.shaded.kyori.adventure.text;

import ac.grim.grimac.shaded.jetbrains.annotations.NotNull;
import ac.grim.grimac.shaded.jetbrains.annotations.Nullable;
import ac.grim.grimac.shaded.kyori.adventure.internal.Internals;
import ac.grim.grimac.shaded.kyori.adventure.text.format.Style;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

final class TranslatableComponentImpl extends AbstractComponent implements TranslatableComponent {
   private final String key;
   @Nullable
   private final String fallback;
   private final List<TranslationArgument> args;

   static TranslatableComponent create(@NotNull final List<Component> children, @NotNull final Style style, @NotNull final String key, @Nullable final String fallback, @NotNull final ComponentLike[] args) {
      Objects.requireNonNull(args, "args");
      return create(children, style, key, fallback, Arrays.asList(args));
   }

   static TranslatableComponent create(@NotNull final List<? extends ComponentLike> children, @NotNull final Style style, @NotNull final String key, @Nullable final String fallback, @NotNull final List<? extends ComponentLike> args) {
      return new TranslatableComponentImpl(ComponentLike.asComponents(children, IS_NOT_EMPTY), (Style)Objects.requireNonNull(style, "style"), (String)Objects.requireNonNull(key, "key"), fallback, asArguments(args));
   }

   TranslatableComponentImpl(@NotNull final List<Component> children, @NotNull final Style style, @NotNull final String key, @Nullable final String fallback, @NotNull final List<TranslationArgument> args) {
      super(children, style);
      this.key = key;
      this.fallback = fallback;
      this.args = args;
   }

   @NotNull
   public String key() {
      return this.key;
   }

   @NotNull
   public TranslatableComponent key(@NotNull final String key) {
      return (TranslatableComponent)(Objects.equals(this.key, key) ? this : create(this.children, this.style, key, this.fallback, this.args));
   }

   /** @deprecated */
   @Deprecated
   @NotNull
   public List<Component> args() {
      return ComponentLike.asComponents(this.args);
   }

   @NotNull
   public List<TranslationArgument> arguments() {
      return this.args;
   }

   @NotNull
   public TranslatableComponent arguments(@NotNull final ComponentLike... args) {
      return create(this.children, this.style, this.key, this.fallback, args);
   }

   @NotNull
   public TranslatableComponent arguments(@NotNull final List<? extends ComponentLike> args) {
      return create(this.children, this.style, this.key, this.fallback, args);
   }

   @Nullable
   public String fallback() {
      return this.fallback;
   }

   @NotNull
   public TranslatableComponent fallback(@Nullable final String fallback) {
      return create(this.children, this.style, this.key, fallback, this.args);
   }

   @NotNull
   public TranslatableComponent children(@NotNull final List<? extends ComponentLike> children) {
      return create(children, this.style, this.key, this.fallback, this.args);
   }

   @NotNull
   public TranslatableComponent style(@NotNull final Style style) {
      return create(this.children, style, this.key, this.fallback, this.args);
   }

   public boolean equals(@Nullable final Object other) {
      if (this == other) {
         return true;
      } else if (!(other instanceof TranslatableComponent)) {
         return false;
      } else if (!super.equals(other)) {
         return false;
      } else {
         TranslatableComponent that = (TranslatableComponent)other;
         return Objects.equals(this.key, that.key()) && Objects.equals(this.fallback, that.fallback()) && Objects.equals(this.args, that.arguments());
      }
   }

   public int hashCode() {
      int result = super.hashCode();
      result = 31 * result + this.key.hashCode();
      result = 31 * result + Objects.hashCode(this.fallback);
      result = 31 * result + this.args.hashCode();
      return result;
   }

   public String toString() {
      return Internals.toString(this);
   }

   @NotNull
   public TranslatableComponent.Builder toBuilder() {
      return new TranslatableComponentImpl.BuilderImpl(this);
   }

   static List<TranslationArgument> asArguments(@NotNull final List<? extends ComponentLike> likes) {
      if (likes.isEmpty()) {
         return Collections.emptyList();
      } else {
         List<TranslationArgument> ret = new ArrayList(likes.size());

         for(int i = 0; i < likes.size(); ++i) {
            ComponentLike like = (ComponentLike)likes.get(i);
            if (like == null) {
               throw new NullPointerException("likes[" + i + "]");
            }

            if (like instanceof TranslationArgument) {
               ret.add((TranslationArgument)like);
            } else if (like instanceof TranslationArgumentLike) {
               ret.add((TranslationArgument)Objects.requireNonNull(((TranslationArgumentLike)like).asTranslationArgument(), "likes[" + i + "].asTranslationArgument()"));
            } else {
               ret.add(TranslationArgument.component(like));
            }
         }

         return Collections.unmodifiableList(ret);
      }
   }

   static final class BuilderImpl extends AbstractComponentBuilder<TranslatableComponent, TranslatableComponent.Builder> implements TranslatableComponent.Builder {
      @Nullable
      private String key;
      @Nullable
      private String fallback;
      private List<TranslationArgument> args = Collections.emptyList();

      BuilderImpl() {
      }

      BuilderImpl(@NotNull final TranslatableComponent component) {
         super(component);
         this.key = component.key();
         this.args = component.arguments();
         this.fallback = component.fallback();
      }

      @NotNull
      public TranslatableComponent.Builder key(@NotNull final String key) {
         this.key = key;
         return this;
      }

      @NotNull
      public TranslatableComponent.Builder arguments(@NotNull final ComponentLike... args) {
         Objects.requireNonNull(args, "args");
         return args.length == 0 ? this.arguments(Collections.emptyList()) : this.arguments(Arrays.asList(args));
      }

      @NotNull
      public TranslatableComponent.Builder arguments(@NotNull final List<? extends ComponentLike> args) {
         this.args = TranslatableComponentImpl.asArguments((List)Objects.requireNonNull(args, "args"));
         return this;
      }

      @NotNull
      public TranslatableComponent.Builder fallback(@Nullable final String fallback) {
         this.fallback = fallback;
         return this;
      }

      @NotNull
      public TranslatableComponent build() {
         if (this.key == null) {
            throw new IllegalStateException("key must be set");
         } else {
            return TranslatableComponentImpl.create(this.children, this.buildStyle(), this.key, this.fallback, this.args);
         }
      }
   }
}
