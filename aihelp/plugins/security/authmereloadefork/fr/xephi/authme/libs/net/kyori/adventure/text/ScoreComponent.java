package fr.xephi.authme.libs.net.kyori.adventure.text;

import fr.xephi.authme.libs.net.kyori.examination.ExaminableProperty;
import java.util.stream.Stream;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface ScoreComponent extends BuildableComponent<ScoreComponent, ScoreComponent.Builder>, ScopedComponent<ScoreComponent> {
   @NotNull
   String name();

   @Contract(
      pure = true
   )
   @NotNull
   ScoreComponent name(@NotNull final String name);

   @NotNull
   String objective();

   @Contract(
      pure = true
   )
   @NotNull
   ScoreComponent objective(@NotNull final String objective);

   /** @deprecated */
   @Deprecated
   @Nullable
   String value();

   /** @deprecated */
   @Deprecated
   @Contract(
      pure = true
   )
   @NotNull
   ScoreComponent value(@Nullable final String value);

   @NotNull
   default Stream<? extends ExaminableProperty> examinableProperties() {
      return Stream.concat(Stream.of(ExaminableProperty.of("name", this.name()), ExaminableProperty.of("objective", this.objective()), ExaminableProperty.of("value", this.value())), BuildableComponent.super.examinableProperties());
   }

   public interface Builder extends ComponentBuilder<ScoreComponent, ScoreComponent.Builder> {
      @Contract("_ -> this")
      @NotNull
      ScoreComponent.Builder name(@NotNull final String name);

      @Contract("_ -> this")
      @NotNull
      ScoreComponent.Builder objective(@NotNull final String objective);

      /** @deprecated */
      @Deprecated
      @Contract("_ -> this")
      @NotNull
      ScoreComponent.Builder value(@Nullable final String value);
   }
}
