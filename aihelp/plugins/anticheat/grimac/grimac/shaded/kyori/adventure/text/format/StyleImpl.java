package ac.grim.grimac.shaded.kyori.adventure.text.format;

import ac.grim.grimac.shaded.jetbrains.annotations.NotNull;
import ac.grim.grimac.shaded.jetbrains.annotations.Nullable;
import ac.grim.grimac.shaded.kyori.adventure.internal.Internals;
import ac.grim.grimac.shaded.kyori.adventure.key.Key;
import ac.grim.grimac.shaded.kyori.adventure.text.event.ClickEvent;
import ac.grim.grimac.shaded.kyori.adventure.text.event.HoverEvent;
import ac.grim.grimac.shaded.kyori.adventure.text.event.HoverEventSource;
import ac.grim.grimac.shaded.kyori.adventure.util.ARGBLike;
import ac.grim.grimac.shaded.kyori.examination.ExaminableProperty;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Stream;

final class StyleImpl implements Style {
   static final StyleImpl EMPTY;
   @Nullable
   final Key font;
   @Nullable
   final TextColor color;
   @Nullable
   final ShadowColor shadowColor;
   @NotNull
   final DecorationMap decorations;
   @Nullable
   final ClickEvent clickEvent;
   @Nullable
   final HoverEvent<?> hoverEvent;
   @Nullable
   final String insertion;

   StyleImpl(@Nullable final Key font, @Nullable final TextColor color, @Nullable final ShadowColor shadowColor, @NotNull final Map<TextDecoration, TextDecoration.State> decorations, @Nullable final ClickEvent clickEvent, @Nullable final HoverEvent<?> hoverEvent, @Nullable final String insertion) {
      this.font = font;
      this.color = color;
      this.shadowColor = shadowColor;
      this.decorations = DecorationMap.fromMap(decorations);
      this.clickEvent = clickEvent;
      this.hoverEvent = hoverEvent;
      this.insertion = insertion;
   }

   @Nullable
   public Key font() {
      return this.font;
   }

   @NotNull
   public Style font(@Nullable final Key font) {
      return Objects.equals(this.font, font) ? this : new StyleImpl(font, this.color, this.shadowColor, this.decorations, this.clickEvent, this.hoverEvent, this.insertion);
   }

   @Nullable
   public TextColor color() {
      return this.color;
   }

   @NotNull
   public Style color(@Nullable final TextColor color) {
      return Objects.equals(this.color, color) ? this : new StyleImpl(this.font, color, this.shadowColor, this.decorations, this.clickEvent, this.hoverEvent, this.insertion);
   }

   @NotNull
   public Style colorIfAbsent(@Nullable final TextColor color) {
      return (Style)(this.color == null ? this.color(color) : this);
   }

   @Nullable
   public ShadowColor shadowColor() {
      return this.shadowColor;
   }

   @NotNull
   public Style shadowColor(@Nullable final ARGBLike argb) {
      return Objects.equals(this.shadowColor, argb) ? this : new StyleImpl(this.font, this.color, argb == null ? null : ShadowColor.shadowColor(argb), this.decorations, this.clickEvent, this.hoverEvent, this.insertion);
   }

   @NotNull
   public Style shadowColorIfAbsent(@Nullable final ARGBLike argb) {
      return (Style)(this.shadowColor == null ? this.shadowColor(argb) : this);
   }

   public TextDecoration.State decoration(@NotNull final TextDecoration decoration) {
      TextDecoration.State state = this.decorations.get(decoration);
      if (state != null) {
         return state;
      } else {
         throw new IllegalArgumentException(String.format("unknown decoration '%s'", decoration));
      }
   }

   @NotNull
   public Style decoration(@NotNull final TextDecoration decoration, @NotNull final TextDecoration.State state) {
      Objects.requireNonNull(state, "state");
      return this.decoration(decoration) == state ? this : new StyleImpl(this.font, this.color, this.shadowColor, this.decorations.with(decoration, state), this.clickEvent, this.hoverEvent, this.insertion);
   }

   @NotNull
   public Style decorationIfAbsent(@NotNull final TextDecoration decoration, final TextDecoration.State state) {
      Objects.requireNonNull(state, "state");
      TextDecoration.State oldState = this.decorations.get(decoration);
      if (oldState == TextDecoration.State.NOT_SET) {
         return new StyleImpl(this.font, this.color, this.shadowColor, this.decorations.with(decoration, state), this.clickEvent, this.hoverEvent, this.insertion);
      } else if (oldState != null) {
         return this;
      } else {
         throw new IllegalArgumentException(String.format("unknown decoration '%s'", decoration));
      }
   }

   @NotNull
   public Map<TextDecoration, TextDecoration.State> decorations() {
      return this.decorations;
   }

   @NotNull
   public Style decorations(@NotNull final Map<TextDecoration, TextDecoration.State> decorations) {
      return new StyleImpl(this.font, this.color, this.shadowColor, DecorationMap.merge(decorations, this.decorations), this.clickEvent, this.hoverEvent, this.insertion);
   }

   @Nullable
   public ClickEvent clickEvent() {
      return this.clickEvent;
   }

   @NotNull
   public Style clickEvent(@Nullable final ClickEvent event) {
      return new StyleImpl(this.font, this.color, this.shadowColor, this.decorations, event, this.hoverEvent, this.insertion);
   }

   @Nullable
   public HoverEvent<?> hoverEvent() {
      return this.hoverEvent;
   }

   @NotNull
   public Style hoverEvent(@Nullable final HoverEventSource<?> source) {
      return new StyleImpl(this.font, this.color, this.shadowColor, this.decorations, this.clickEvent, HoverEventSource.unbox(source), this.insertion);
   }

   @Nullable
   public String insertion() {
      return this.insertion;
   }

   @NotNull
   public Style insertion(@Nullable final String insertion) {
      return Objects.equals(this.insertion, insertion) ? this : new StyleImpl(this.font, this.color, this.shadowColor, this.decorations, this.clickEvent, this.hoverEvent, insertion);
   }

   @NotNull
   public Style merge(@NotNull final Style that, @NotNull final Style.Merge.Strategy strategy, @NotNull final Set<Style.Merge> merges) {
      if (nothingToMerge(that, strategy, merges)) {
         return this;
      } else if (this.isEmpty() && Style.Merge.hasAll(merges)) {
         return that;
      } else {
         Style.Builder builder = this.toBuilder();
         builder.merge(that, strategy, merges);
         return builder.build();
      }
   }

   @NotNull
   public Style unmerge(@NotNull final Style that) {
      if (this.isEmpty()) {
         return this;
      } else {
         Style.Builder builder = new StyleImpl.BuilderImpl(this);
         if (Objects.equals(this.font(), that.font())) {
            builder.font((Key)null);
         }

         if (Objects.equals(this.color(), that.color())) {
            builder.color((TextColor)null);
         }

         if (Objects.equals(this.shadowColor(), that.shadowColor())) {
            builder.shadowColor((ARGBLike)null);
         }

         int i = 0;

         for(int length = DecorationMap.DECORATIONS.length; i < length; ++i) {
            TextDecoration decoration = DecorationMap.DECORATIONS[i];
            if (this.decoration(decoration) == that.decoration(decoration)) {
               builder.decoration(decoration, TextDecoration.State.NOT_SET);
            }
         }

         if (Objects.equals(this.clickEvent(), that.clickEvent())) {
            builder.clickEvent((ClickEvent)null);
         }

         if (Objects.equals(this.hoverEvent(), that.hoverEvent())) {
            builder.hoverEvent((HoverEventSource)null);
         }

         if (Objects.equals(this.insertion(), that.insertion())) {
            builder.insertion((String)null);
         }

         return builder.build();
      }
   }

   static boolean nothingToMerge(@NotNull final Style mergeFrom, @NotNull final Style.Merge.Strategy strategy, @NotNull final Set<Style.Merge> merges) {
      if (strategy == Style.Merge.Strategy.NEVER) {
         return true;
      } else if (mergeFrom.isEmpty()) {
         return true;
      } else {
         return merges.isEmpty();
      }
   }

   public boolean isEmpty() {
      return this == EMPTY;
   }

   @NotNull
   public Style.Builder toBuilder() {
      return new StyleImpl.BuilderImpl(this);
   }

   @NotNull
   public Stream<? extends ExaminableProperty> examinableProperties() {
      return Stream.concat(this.decorations.examinableProperties(), Stream.of(ExaminableProperty.of("color", (Object)this.color), ExaminableProperty.of("shadowColor", (Object)this.shadowColor), ExaminableProperty.of("clickEvent", (Object)this.clickEvent), ExaminableProperty.of("hoverEvent", (Object)this.hoverEvent), ExaminableProperty.of("insertion", this.insertion), ExaminableProperty.of("font", (Object)this.font)));
   }

   @NotNull
   public String toString() {
      return Internals.toString(this);
   }

   public boolean equals(@Nullable final Object other) {
      if (this == other) {
         return true;
      } else if (!(other instanceof StyleImpl)) {
         return false;
      } else {
         StyleImpl that = (StyleImpl)other;
         return Objects.equals(this.color, that.color) && this.decorations.equals(that.decorations) && Objects.equals(this.shadowColor, that.shadowColor) && Objects.equals(this.clickEvent, that.clickEvent) && Objects.equals(this.hoverEvent, that.hoverEvent) && Objects.equals(this.insertion, that.insertion) && Objects.equals(this.font, that.font);
      }
   }

   public int hashCode() {
      int result = Objects.hashCode(this.color);
      result = 31 * result + Objects.hashCode(this.shadowColor);
      result = 31 * result + this.decorations.hashCode();
      result = 31 * result + Objects.hashCode(this.clickEvent);
      result = 31 * result + Objects.hashCode(this.hoverEvent);
      result = 31 * result + Objects.hashCode(this.insertion);
      result = 31 * result + Objects.hashCode(this.font);
      return result;
   }

   static {
      EMPTY = new StyleImpl((Key)null, (TextColor)null, (ShadowColor)null, DecorationMap.EMPTY, (ClickEvent)null, (HoverEvent)null, (String)null);
   }

   static final class BuilderImpl implements Style.Builder {
      @Nullable
      Key font;
      @Nullable
      TextColor color;
      @Nullable
      ShadowColor shadowColor;
      DecorationMap decorations;
      @Nullable
      ClickEvent clickEvent;
      @Nullable
      HoverEvent<?> hoverEvent;
      @Nullable
      String insertion;

      BuilderImpl() {
         this.decorations = DecorationMap.EMPTY;
      }

      BuilderImpl(@NotNull final StyleImpl style) {
         this.color = style.color;
         this.shadowColor = style.shadowColor;
         this.decorations = style.decorations;
         this.clickEvent = style.clickEvent;
         this.hoverEvent = style.hoverEvent;
         this.insertion = style.insertion;
         this.font = style.font;
      }

      @NotNull
      public Style.Builder font(@Nullable final Key font) {
         this.font = font;
         return this;
      }

      @NotNull
      public Style.Builder color(@Nullable final TextColor color) {
         this.color = color;
         return this;
      }

      @NotNull
      public Style.Builder colorIfAbsent(@Nullable final TextColor color) {
         if (this.color == null) {
            this.color = color;
         }

         return this;
      }

      @NotNull
      public Style.Builder shadowColor(@Nullable final ARGBLike argb) {
         this.shadowColor = argb == null ? null : ShadowColor.shadowColor(argb);
         return this;
      }

      @NotNull
      public Style.Builder shadowColorIfAbsent(@Nullable final ARGBLike argb) {
         if (this.shadowColor == null) {
            this.shadowColor = argb == null ? null : ShadowColor.shadowColor(argb);
         }

         return this;
      }

      @NotNull
      public Style.Builder decoration(@NotNull final TextDecoration decoration, @NotNull final TextDecoration.State state) {
         Objects.requireNonNull(state, "state");
         Objects.requireNonNull(decoration, "decoration");
         this.decorations = this.decorations.with(decoration, state);
         return this;
      }

      @NotNull
      public Style.Builder decorationIfAbsent(@NotNull final TextDecoration decoration, final TextDecoration.State state) {
         Objects.requireNonNull(state, "state");
         TextDecoration.State oldState = this.decorations.get(decoration);
         if (oldState == TextDecoration.State.NOT_SET) {
            this.decorations = this.decorations.with(decoration, state);
         }

         if (oldState != null) {
            return this;
         } else {
            throw new IllegalArgumentException(String.format("unknown decoration '%s'", decoration));
         }
      }

      @NotNull
      public Style.Builder clickEvent(@Nullable final ClickEvent event) {
         this.clickEvent = event;
         return this;
      }

      @NotNull
      public Style.Builder hoverEvent(@Nullable final HoverEventSource<?> source) {
         this.hoverEvent = HoverEventSource.unbox(source);
         return this;
      }

      @NotNull
      public Style.Builder insertion(@Nullable final String insertion) {
         this.insertion = insertion;
         return this;
      }

      @NotNull
      public Style.Builder merge(@NotNull final Style that, @NotNull final Style.Merge.Strategy strategy, @NotNull final Set<Style.Merge> merges) {
         Objects.requireNonNull(that, "style");
         Objects.requireNonNull(strategy, "strategy");
         Objects.requireNonNull(merges, "merges");
         if (StyleImpl.nothingToMerge(that, strategy, merges)) {
            return this;
         } else {
            if (merges.contains(Style.Merge.COLOR)) {
               TextColor color = that.color();
               if (color != null && (strategy == Style.Merge.Strategy.ALWAYS || strategy == Style.Merge.Strategy.IF_ABSENT_ON_TARGET && this.color == null)) {
                  this.color(color);
               }
            }

            if (merges.contains(Style.Merge.SHADOW_COLOR)) {
               ShadowColor shadowColor = that.shadowColor();
               if (shadowColor != null && (strategy == Style.Merge.Strategy.ALWAYS || strategy == Style.Merge.Strategy.IF_ABSENT_ON_TARGET && this.shadowColor == null)) {
                  this.shadowColor(shadowColor);
               }
            }

            if (merges.contains(Style.Merge.DECORATIONS)) {
               int i = 0;

               for(int length = DecorationMap.DECORATIONS.length; i < length; ++i) {
                  TextDecoration decoration = DecorationMap.DECORATIONS[i];
                  TextDecoration.State state = that.decoration(decoration);
                  if (state != TextDecoration.State.NOT_SET) {
                     if (strategy == Style.Merge.Strategy.ALWAYS) {
                        this.decoration(decoration, state);
                     } else if (strategy == Style.Merge.Strategy.IF_ABSENT_ON_TARGET) {
                        this.decorationIfAbsent(decoration, state);
                     }
                  }
               }
            }

            if (merges.contains(Style.Merge.EVENTS)) {
               ClickEvent clickEvent = that.clickEvent();
               if (clickEvent != null && (strategy == Style.Merge.Strategy.ALWAYS || strategy == Style.Merge.Strategy.IF_ABSENT_ON_TARGET && this.clickEvent == null)) {
                  this.clickEvent(clickEvent);
               }

               HoverEvent<?> hoverEvent = that.hoverEvent();
               if (hoverEvent != null && (strategy == Style.Merge.Strategy.ALWAYS || strategy == Style.Merge.Strategy.IF_ABSENT_ON_TARGET && this.hoverEvent == null)) {
                  this.hoverEvent(hoverEvent);
               }
            }

            if (merges.contains(Style.Merge.INSERTION)) {
               String insertion = that.insertion();
               if (insertion != null && (strategy == Style.Merge.Strategy.ALWAYS || strategy == Style.Merge.Strategy.IF_ABSENT_ON_TARGET && this.insertion == null)) {
                  this.insertion(insertion);
               }
            }

            if (merges.contains(Style.Merge.FONT)) {
               Key font = that.font();
               if (font != null && (strategy == Style.Merge.Strategy.ALWAYS || strategy == Style.Merge.Strategy.IF_ABSENT_ON_TARGET && this.font == null)) {
                  this.font(font);
               }
            }

            return this;
         }
      }

      @NotNull
      public StyleImpl build() {
         return this.isEmpty() ? StyleImpl.EMPTY : new StyleImpl(this.font, this.color, this.shadowColor, this.decorations, this.clickEvent, this.hoverEvent, this.insertion);
      }

      private boolean isEmpty() {
         return this.color == null && this.shadowColor == null && this.decorations == DecorationMap.EMPTY && this.clickEvent == null && this.hoverEvent == null && this.insertion == null && this.font == null;
      }
   }
}
