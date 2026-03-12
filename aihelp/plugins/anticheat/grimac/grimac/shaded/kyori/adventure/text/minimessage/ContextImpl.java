package ac.grim.grimac.shaded.kyori.adventure.text.minimessage;

import ac.grim.grimac.shaded.jetbrains.annotations.NotNull;
import ac.grim.grimac.shaded.jetbrains.annotations.Nullable;
import ac.grim.grimac.shaded.kyori.adventure.pointer.Pointered;
import ac.grim.grimac.shaded.kyori.adventure.text.Component;
import ac.grim.grimac.shaded.kyori.adventure.text.minimessage.internal.parser.ParsingExceptionImpl;
import ac.grim.grimac.shaded.kyori.adventure.text.minimessage.internal.parser.Token;
import ac.grim.grimac.shaded.kyori.adventure.text.minimessage.internal.parser.node.TagPart;
import ac.grim.grimac.shaded.kyori.adventure.text.minimessage.tag.Tag;
import ac.grim.grimac.shaded.kyori.adventure.text.minimessage.tag.resolver.ArgumentQueue;
import ac.grim.grimac.shaded.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.UnaryOperator;

class ContextImpl implements Context {
   private static final Token[] EMPTY_TOKEN_ARRAY = new Token[0];
   private final boolean strict;
   private final boolean emitVirtuals;
   private final Consumer<String> debugOutput;
   private String message;
   private final MiniMessage miniMessage;
   @Nullable
   private final Pointered target;
   private final TagResolver tagResolver;
   private final UnaryOperator<String> preProcessor;
   private final UnaryOperator<Component> postProcessor;

   ContextImpl(final boolean strict, final boolean emitVirtuals, final Consumer<String> debugOutput, final String message, final MiniMessage miniMessage, @Nullable final Pointered target, @Nullable final TagResolver extraTags, @Nullable final UnaryOperator<String> preProcessor, @Nullable final UnaryOperator<Component> postProcessor) {
      this.strict = strict;
      this.emitVirtuals = emitVirtuals;
      this.debugOutput = debugOutput;
      this.message = message;
      this.miniMessage = miniMessage;
      this.target = target;
      this.tagResolver = extraTags == null ? TagResolver.empty() : extraTags;
      this.preProcessor = preProcessor == null ? UnaryOperator.identity() : preProcessor;
      this.postProcessor = postProcessor == null ? UnaryOperator.identity() : postProcessor;
   }

   public boolean strict() {
      return this.strict;
   }

   public boolean emitVirtuals() {
      return this.emitVirtuals;
   }

   public Consumer<String> debugOutput() {
      return this.debugOutput;
   }

   @NotNull
   public String message() {
      return this.message;
   }

   void message(@NotNull final String message) {
      this.message = message;
   }

   @NotNull
   public TagResolver extraTags() {
      return this.tagResolver;
   }

   public UnaryOperator<Component> postProcessor() {
      return this.postProcessor;
   }

   public UnaryOperator<String> preProcessor() {
      return this.preProcessor;
   }

   @Nullable
   public Pointered target() {
      return this.target;
   }

   @NotNull
   public Pointered targetOrThrow() {
      if (this.target == null) {
         throw this.newException("A target is required for this deserialization attempt");
      } else {
         return this.target;
      }
   }

   @NotNull
   public <T extends Pointered> T targetAsType(@NotNull final Class<T> targetClass) {
      if (((Class)Objects.requireNonNull(targetClass, "targetClass")).isInstance(this.target)) {
         return (Pointered)targetClass.cast(this.target);
      } else {
         throw this.newException("A target with type " + targetClass.getSimpleName() + " is required for this deserialization attempt");
      }
   }

   @NotNull
   public Component deserialize(@NotNull final String message) {
      return this.deserializeWithOptionalTarget((String)Objects.requireNonNull(message, "message"), this.tagResolver);
   }

   @NotNull
   public Component deserialize(@NotNull final String message, @NotNull final TagResolver resolver) {
      Objects.requireNonNull(message, "message");
      TagResolver combinedResolver = TagResolver.builder().resolver(this.tagResolver).resolver(resolver).build();
      return this.deserializeWithOptionalTarget(message, combinedResolver);
   }

   @NotNull
   public Component deserialize(@NotNull final String message, @NotNull final TagResolver... resolvers) {
      Objects.requireNonNull(message, "message");
      TagResolver combinedResolver = TagResolver.builder().resolver(this.tagResolver).resolvers(resolvers).build();
      return this.deserializeWithOptionalTarget(message, combinedResolver);
   }

   @NotNull
   public ParsingException newException(@NotNull final String message) {
      return new ParsingExceptionImpl(message, this.message, (Throwable)null, false, EMPTY_TOKEN_ARRAY);
   }

   @NotNull
   public ParsingException newException(@NotNull final String message, @NotNull final ArgumentQueue tags) {
      return new ParsingExceptionImpl(message, this.message, (Throwable)null, false, tagsToTokens(((ArgumentQueueImpl)tags).args));
   }

   @NotNull
   public ParsingException newException(@NotNull final String message, @Nullable final Throwable cause, @NotNull final ArgumentQueue tags) {
      return new ParsingExceptionImpl(message, this.message, cause, false, tagsToTokens(((ArgumentQueueImpl)tags).args));
   }

   @NotNull
   private Component deserializeWithOptionalTarget(@NotNull final String message, @NotNull final TagResolver tagResolver) {
      return this.target != null ? this.miniMessage.deserialize(message, this.target, tagResolver) : this.miniMessage.deserialize(message, tagResolver);
   }

   private static Token[] tagsToTokens(final List<? extends Tag.Argument> tags) {
      Token[] tokens = new Token[tags.size()];
      int i = 0;

      for(int length = tokens.length; i < length; ++i) {
         tokens[i] = ((TagPart)tags.get(i)).token();
      }

      return tokens;
   }
}
