package fr.xephi.authme.libs.net.kyori.adventure.text;

import fr.xephi.authme.libs.net.kyori.adventure.internal.Internals;
import fr.xephi.authme.libs.net.kyori.adventure.internal.properties.AdventureProperties;
import fr.xephi.authme.libs.net.kyori.adventure.text.format.Style;
import fr.xephi.authme.libs.net.kyori.adventure.util.Nag;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.VisibleForTesting;

final class TextComponentImpl extends AbstractComponent implements TextComponent {
   private static final boolean WARN_WHEN_LEGACY_FORMATTING_DETECTED;
   @VisibleForTesting
   static final char SECTION_CHAR = '§';
   static final TextComponent EMPTY;
   static final TextComponent NEWLINE;
   static final TextComponent SPACE;
   private final String content;

   static TextComponent create(@NotNull final List<? extends ComponentLike> children, @NotNull final Style style, @NotNull final String content) {
      List<Component> filteredChildren = ComponentLike.asComponents(children, IS_NOT_EMPTY);
      return (TextComponent)(filteredChildren.isEmpty() && style.isEmpty() && content.isEmpty() ? Component.empty() : new TextComponentImpl(filteredChildren, (Style)Objects.requireNonNull(style, "style"), (String)Objects.requireNonNull(content, "content")));
   }

   @NotNull
   private static TextComponent createDirect(@NotNull final String content) {
      return new TextComponentImpl(Collections.emptyList(), Style.empty(), content);
   }

   TextComponentImpl(@NotNull final List<Component> children, @NotNull final Style style, @NotNull final String content) {
      super(children, style);
      this.content = content;
      if (WARN_WHEN_LEGACY_FORMATTING_DETECTED) {
         LegacyFormattingDetected nag = this.warnWhenLegacyFormattingDetected();
         if (nag != null) {
            Nag.print(nag);
         }
      }

   }

   @VisibleForTesting
   @Nullable
   final LegacyFormattingDetected warnWhenLegacyFormattingDetected() {
      return this.content.indexOf(167) != -1 ? new LegacyFormattingDetected(this) : null;
   }

   @NotNull
   public String content() {
      return this.content;
   }

   @NotNull
   public TextComponent content(@NotNull final String content) {
      return (TextComponent)(Objects.equals(this.content, content) ? this : create(this.children, this.style, content));
   }

   @NotNull
   public TextComponent children(@NotNull final List<? extends ComponentLike> children) {
      return create(children, this.style, this.content);
   }

   @NotNull
   public TextComponent style(@NotNull final Style style) {
      return create(this.children, style, this.content);
   }

   public boolean equals(@Nullable final Object other) {
      if (this == other) {
         return true;
      } else if (!(other instanceof TextComponentImpl)) {
         return false;
      } else if (!super.equals(other)) {
         return false;
      } else {
         TextComponentImpl that = (TextComponentImpl)other;
         return Objects.equals(this.content, that.content);
      }
   }

   public int hashCode() {
      int result = super.hashCode();
      result = 31 * result + this.content.hashCode();
      return result;
   }

   public String toString() {
      return Internals.toString(this);
   }

   @NotNull
   public TextComponent.Builder toBuilder() {
      return new TextComponentImpl.BuilderImpl(this);
   }

   static {
      WARN_WHEN_LEGACY_FORMATTING_DETECTED = Boolean.TRUE.equals(AdventureProperties.TEXT_WARN_WHEN_LEGACY_FORMATTING_DETECTED.value());
      EMPTY = createDirect("");
      NEWLINE = createDirect("\n");
      SPACE = createDirect(" ");
   }

   static final class BuilderImpl extends AbstractComponentBuilder<TextComponent, TextComponent.Builder> implements TextComponent.Builder {
      private String content = "";

      BuilderImpl() {
      }

      BuilderImpl(@NotNull final TextComponent component) {
         super(component);
         this.content = component.content();
      }

      @NotNull
      public TextComponent.Builder content(@NotNull final String content) {
         this.content = (String)Objects.requireNonNull(content, "content");
         return this;
      }

      @NotNull
      public String content() {
         return this.content;
      }

      @NotNull
      public TextComponent build() {
         return this.isEmpty() ? Component.empty() : TextComponentImpl.create(this.children, this.buildStyle(), this.content);
      }

      private boolean isEmpty() {
         return this.content.isEmpty() && this.children.isEmpty() && !this.hasStyle();
      }
   }
}
