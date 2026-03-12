package fr.xephi.authme.libs.net.kyori.adventure.text.minimessage;

import fr.xephi.authme.libs.net.kyori.adventure.pointer.Pointered;
import fr.xephi.authme.libs.net.kyori.adventure.text.Component;
import fr.xephi.authme.libs.net.kyori.adventure.text.minimessage.internal.parser.ParsingExceptionImpl;
import fr.xephi.authme.libs.net.kyori.adventure.text.minimessage.internal.parser.Token;
import fr.xephi.authme.libs.net.kyori.adventure.text.minimessage.internal.parser.node.TagPart;
import fr.xephi.authme.libs.net.kyori.adventure.text.minimessage.tag.Tag;
import fr.xephi.authme.libs.net.kyori.adventure.text.minimessage.tag.resolver.ArgumentQueue;
import fr.xephi.authme.libs.net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.UnaryOperator;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

class ContextImpl implements Context {
   private static final Token[] EMPTY_TOKEN_ARRAY = new Token[0];
   private final boolean strict;
   private final Consumer<String> debugOutput;
   private String message;
   private final MiniMessage miniMessage;
   @Nullable
   private final Pointered target;
   private final TagResolver tagResolver;
   private final UnaryOperator<String> preProcessor;
   private final UnaryOperator<Component> postProcessor;

   ContextImpl(final boolean strict, final Consumer<String> debugOutput, final String message, final MiniMessage miniMessage, @Nullable final Pointered target, @Nullable final TagResolver extraTags, @Nullable final UnaryOperator<String> preProcessor, @Nullable final UnaryOperator<Component> postProcessor) {
      this.strict = strict;
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
      return this.target;
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
      return this.miniMessage.deserialize((String)Objects.requireNonNull(message, "message"), this.tagResolver);
   }

   @NotNull
   public Component deserialize(@NotNull final String message, @NotNull final TagResolver resolver) {
      return this.miniMessage.deserialize((String)Objects.requireNonNull(message, "message"), TagResolver.builder().resolver(this.tagResolver).resolver((TagResolver)Objects.requireNonNull(resolver, "resolver")).build());
   }

   @NotNull
   public Component deserialize(@NotNull final String message, @NotNull final TagResolver... resolvers) {
      return this.miniMessage.deserialize((String)Objects.requireNonNull(message, "message"), TagResolver.builder().resolver(this.tagResolver).resolvers((TagResolver[])Objects.requireNonNull(resolvers, "resolvers")).build());
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

   private static Token[] tagsToTokens(final List<? extends Tag.Argument> tags) {
      Token[] tokens = new Token[tags.size()];
      int i = 0;

      for(int length = tokens.length; i < length; ++i) {
         tokens[i] = ((TagPart)tags.get(i)).token();
      }

      return tokens;
   }
}
