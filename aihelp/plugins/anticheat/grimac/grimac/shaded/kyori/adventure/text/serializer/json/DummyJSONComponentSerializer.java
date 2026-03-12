package ac.grim.grimac.shaded.kyori.adventure.text.serializer.json;

import ac.grim.grimac.shaded.jetbrains.annotations.NotNull;
import ac.grim.grimac.shaded.jetbrains.annotations.Nullable;
import ac.grim.grimac.shaded.kyori.adventure.text.Component;
import ac.grim.grimac.shaded.kyori.option.OptionState;
import java.util.function.Consumer;

final class DummyJSONComponentSerializer implements JSONComponentSerializer {
   static final JSONComponentSerializer INSTANCE = new DummyJSONComponentSerializer();
   private static final String UNSUPPORTED_MESSAGE = "No JsonComponentSerializer implementation found\n\nAre you missing an implementation artifact like adventure-text-serializer-gson?\nIs your environment configured in a way that causes ServiceLoader to malfunction?";

   @NotNull
   public Component deserialize(@NotNull final String input) {
      throw new UnsupportedOperationException("No JsonComponentSerializer implementation found\n\nAre you missing an implementation artifact like adventure-text-serializer-gson?\nIs your environment configured in a way that causes ServiceLoader to malfunction?");
   }

   @NotNull
   public String serialize(@NotNull final Component component) {
      throw new UnsupportedOperationException("No JsonComponentSerializer implementation found\n\nAre you missing an implementation artifact like adventure-text-serializer-gson?\nIs your environment configured in a way that causes ServiceLoader to malfunction?");
   }

   static final class BuilderImpl implements JSONComponentSerializer.Builder {
      @NotNull
      public JSONComponentSerializer.Builder options(@NotNull final OptionState flags) {
         return this;
      }

      @NotNull
      public JSONComponentSerializer.Builder editOptions(@NotNull final Consumer<OptionState.Builder> optionEditor) {
         return this;
      }

      /** @deprecated */
      @Deprecated
      @NotNull
      public JSONComponentSerializer.Builder downsampleColors() {
         return this;
      }

      @NotNull
      public JSONComponentSerializer.Builder legacyHoverEventSerializer(@Nullable final LegacyHoverEventSerializer serializer) {
         return this;
      }

      /** @deprecated */
      @Deprecated
      @NotNull
      public JSONComponentSerializer.Builder emitLegacyHoverEvent() {
         return this;
      }

      @NotNull
      public JSONComponentSerializer build() {
         return DummyJSONComponentSerializer.INSTANCE;
      }
   }
}
