package fr.xephi.authme.libs.net.kyori.adventure.text;

import fr.xephi.authme.libs.net.kyori.adventure.builder.AbstractBuilder;
import fr.xephi.authme.libs.net.kyori.adventure.util.Buildable;
import fr.xephi.authme.libs.net.kyori.adventure.util.IntFunction2;
import fr.xephi.authme.libs.net.kyori.examination.Examinable;
import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.regex.MatchResult;
import java.util.regex.Pattern;
import org.intellij.lang.annotations.RegExp;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface TextReplacementConfig extends Buildable<TextReplacementConfig, TextReplacementConfig.Builder>, Examinable {
   @NotNull
   static TextReplacementConfig.Builder builder() {
      return new TextReplacementConfigImpl.Builder();
   }

   @NotNull
   Pattern matchPattern();

   @FunctionalInterface
   public interface Condition {
      @NotNull
      PatternReplacementResult shouldReplace(@NotNull final MatchResult result, final int matchCount, final int replaced);
   }

   public interface Builder extends AbstractBuilder<TextReplacementConfig>, Buildable.Builder<TextReplacementConfig> {
      @Contract("_ -> this")
      default TextReplacementConfig.Builder matchLiteral(final String literal) {
         return this.match(Pattern.compile(literal, 16));
      }

      @Contract("_ -> this")
      @NotNull
      default TextReplacementConfig.Builder match(@NotNull @RegExp final String pattern) {
         return this.match(Pattern.compile(pattern));
      }

      @Contract("_ -> this")
      @NotNull
      TextReplacementConfig.Builder match(@NotNull final Pattern pattern);

      @NotNull
      default TextReplacementConfig.Builder once() {
         return this.times(1);
      }

      @Contract("_ -> this")
      @NotNull
      default TextReplacementConfig.Builder times(final int times) {
         return this.condition((index, replaced) -> {
            return replaced < times ? PatternReplacementResult.REPLACE : PatternReplacementResult.STOP;
         });
      }

      @Contract("_ -> this")
      @NotNull
      default TextReplacementConfig.Builder condition(@NotNull final IntFunction2<PatternReplacementResult> condition) {
         return this.condition((result, matchCount, replaced) -> {
            return (PatternReplacementResult)condition.apply(matchCount, replaced);
         });
      }

      @Contract("_ -> this")
      @NotNull
      TextReplacementConfig.Builder condition(@NotNull final TextReplacementConfig.Condition condition);

      @Contract("_ -> this")
      @NotNull
      default TextReplacementConfig.Builder replacement(@NotNull final String replacement) {
         Objects.requireNonNull(replacement, "replacement");
         return this.replacement((builder) -> {
            return builder.content(replacement);
         });
      }

      @Contract("_ -> this")
      @NotNull
      default TextReplacementConfig.Builder replacement(@Nullable final ComponentLike replacement) {
         Component baked = ComponentLike.unbox(replacement);
         return this.replacement((result, input) -> {
            return baked;
         });
      }

      @Contract("_ -> this")
      @NotNull
      default TextReplacementConfig.Builder replacement(@NotNull final Function<TextComponent.Builder, ComponentLike> replacement) {
         Objects.requireNonNull(replacement, "replacement");
         return this.replacement((result, input) -> {
            return (ComponentLike)replacement.apply(input);
         });
      }

      @Contract("_ -> this")
      @NotNull
      TextReplacementConfig.Builder replacement(@NotNull final BiFunction<MatchResult, TextComponent.Builder, ComponentLike> replacement);
   }
}
