package ac.grim.grimac.shaded.kyori.adventure.text.format;

import ac.grim.grimac.shaded.jetbrains.annotations.NotNull;
import ac.grim.grimac.shaded.jetbrains.annotations.Nullable;
import ac.grim.grimac.shaded.kyori.adventure.util.Index;
import ac.grim.grimac.shaded.kyori.adventure.util.TriState;
import java.util.Objects;

public enum TextDecoration implements StyleBuilderApplicable, TextFormat {
   OBFUSCATED("obfuscated"),
   BOLD("bold"),
   STRIKETHROUGH("strikethrough"),
   UNDERLINED("underlined"),
   ITALIC("italic");

   public static final Index<String, TextDecoration> NAMES = Index.create(TextDecoration.class, (constant) -> {
      return constant.name;
   });
   private final String name;

   private TextDecoration(final String name) {
      this.name = name;
   }

   /** @deprecated */
   @Deprecated
   @NotNull
   public final TextDecorationAndState as(final boolean state) {
      return this.withState(state);
   }

   /** @deprecated */
   @Deprecated
   @NotNull
   public final TextDecorationAndState as(@NotNull final TextDecoration.State state) {
      return this.withState(state);
   }

   @NotNull
   public final TextDecorationAndState withState(final boolean state) {
      return new TextDecorationAndStateImpl(this, TextDecoration.State.byBoolean(state));
   }

   @NotNull
   public final TextDecorationAndState withState(@NotNull final TextDecoration.State state) {
      return new TextDecorationAndStateImpl(this, state);
   }

   @NotNull
   public final TextDecorationAndState withState(@NotNull final TriState state) {
      return new TextDecorationAndStateImpl(this, TextDecoration.State.byTriState(state));
   }

   public void styleApply(@NotNull final Style.Builder style) {
      style.decorate(this);
   }

   @NotNull
   public String toString() {
      return this.name;
   }

   // $FF: synthetic method
   private static TextDecoration[] $values() {
      return new TextDecoration[]{OBFUSCATED, BOLD, STRIKETHROUGH, UNDERLINED, ITALIC};
   }

   public static enum State {
      NOT_SET("not_set"),
      FALSE("false"),
      TRUE("true");

      private final String name;

      private State(final String name) {
         this.name = name;
      }

      public String toString() {
         return this.name;
      }

      @NotNull
      public static TextDecoration.State byBoolean(final boolean flag) {
         return flag ? TRUE : FALSE;
      }

      @NotNull
      public static TextDecoration.State byBoolean(@Nullable final Boolean flag) {
         return flag == null ? NOT_SET : byBoolean(flag);
      }

      @NotNull
      public static TextDecoration.State byTriState(@NotNull final TriState flag) {
         Objects.requireNonNull(flag);
         switch(flag) {
         case TRUE:
            return TRUE;
         case FALSE:
            return FALSE;
         case NOT_SET:
            return NOT_SET;
         default:
            throw new IllegalArgumentException("Unable to turn TriState: " + flag + " into a TextDecoration.State");
         }
      }

      // $FF: synthetic method
      private static TextDecoration.State[] $values() {
         return new TextDecoration.State[]{NOT_SET, FALSE, TRUE};
      }
   }
}
