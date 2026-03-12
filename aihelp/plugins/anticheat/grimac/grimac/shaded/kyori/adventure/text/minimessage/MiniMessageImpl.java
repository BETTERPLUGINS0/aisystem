package ac.grim.grimac.shaded.kyori.adventure.text.minimessage;

import ac.grim.grimac.shaded.jetbrains.annotations.NotNull;
import ac.grim.grimac.shaded.jetbrains.annotations.Nullable;
import ac.grim.grimac.shaded.kyori.adventure.pointer.Pointered;
import ac.grim.grimac.shaded.kyori.adventure.text.Component;
import ac.grim.grimac.shaded.kyori.adventure.text.minimessage.internal.serializer.SerializableResolver;
import ac.grim.grimac.shaded.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import ac.grim.grimac.shaded.kyori.adventure.text.minimessage.tree.Node;
import ac.grim.grimac.shaded.kyori.adventure.util.Services;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.UnaryOperator;

final class MiniMessageImpl implements MiniMessage {
   private static final Optional<MiniMessage.Provider> SERVICE = Services.service(MiniMessage.Provider.class);
   static final Consumer<MiniMessage.Builder> BUILDER;
   static final UnaryOperator<String> DEFAULT_NO_OP;
   static final UnaryOperator<Component> DEFAULT_COMPACTING_METHOD;
   private final boolean strict;
   private final boolean emitVirtuals;
   @Nullable
   private final Consumer<String> debugOutput;
   private final UnaryOperator<Component> postProcessor;
   private final UnaryOperator<String> preProcessor;
   final MiniMessageParser parser;

   MiniMessageImpl(@NotNull final TagResolver resolver, final boolean strict, final boolean emitVirtuals, @Nullable final Consumer<String> debugOutput, @NotNull final UnaryOperator<String> preProcessor, @NotNull final UnaryOperator<Component> postProcessor) {
      this.parser = new MiniMessageParser(resolver);
      this.strict = strict;
      this.emitVirtuals = emitVirtuals;
      this.debugOutput = debugOutput;
      this.preProcessor = preProcessor;
      this.postProcessor = postProcessor;
   }

   @NotNull
   public Component deserialize(@NotNull final String input) {
      return this.parser.parseFormat(this.newContext(input, (Pointered)null, (TagResolver)null));
   }

   @NotNull
   public Component deserialize(@NotNull final String input, @NotNull final Pointered target) {
      return this.parser.parseFormat(this.newContext(input, (Pointered)Objects.requireNonNull(target, "target"), (TagResolver)null));
   }

   @NotNull
   public Component deserialize(@NotNull final String input, @NotNull final TagResolver tagResolver) {
      return this.parser.parseFormat(this.newContext(input, (Pointered)null, (TagResolver)Objects.requireNonNull(tagResolver, "tagResolver")));
   }

   @NotNull
   public Component deserialize(@NotNull final String input, @NotNull final Pointered target, @NotNull final TagResolver tagResolver) {
      return this.parser.parseFormat(this.newContext(input, (Pointered)Objects.requireNonNull(target, "target"), (TagResolver)Objects.requireNonNull(tagResolver, "tagResolver")));
   }

   @NotNull
   public Node.Root deserializeToTree(@NotNull final String input) {
      return this.parser.parseToTree(this.newContext(input, (Pointered)null, (TagResolver)null));
   }

   @NotNull
   public Node.Root deserializeToTree(@NotNull final String input, @NotNull final Pointered target) {
      return this.parser.parseToTree(this.newContext(input, (Pointered)Objects.requireNonNull(target, "target"), (TagResolver)null));
   }

   @NotNull
   public Node.Root deserializeToTree(@NotNull final String input, @NotNull final TagResolver tagResolver) {
      return this.parser.parseToTree(this.newContext(input, (Pointered)null, (TagResolver)Objects.requireNonNull(tagResolver, "tagResolver")));
   }

   @NotNull
   public Node.Root deserializeToTree(@NotNull final String input, @NotNull final Pointered target, @NotNull final TagResolver tagResolver) {
      return this.parser.parseToTree(this.newContext(input, (Pointered)Objects.requireNonNull(target, "target"), (TagResolver)Objects.requireNonNull(tagResolver, "tagResolver")));
   }

   @NotNull
   public String serialize(@NotNull final Component component) {
      return MiniMessageSerializer.serialize(component, this.serialResolver((TagResolver)null), this.strict);
   }

   private SerializableResolver serialResolver(@Nullable final TagResolver extraResolver) {
      if (extraResolver == null) {
         if (this.parser.tagResolver instanceof SerializableResolver) {
            return (SerializableResolver)this.parser.tagResolver;
         }
      } else {
         TagResolver combined = TagResolver.resolver(this.parser.tagResolver, extraResolver);
         if (combined instanceof SerializableResolver) {
            return (SerializableResolver)combined;
         }
      }

      return (SerializableResolver)TagResolver.empty();
   }

   @NotNull
   public String escapeTags(@NotNull final String input) {
      return this.parser.escapeTokens(this.newContext(input, (Pointered)null, (TagResolver)null));
   }

   @NotNull
   public String escapeTags(@NotNull final String input, @NotNull final TagResolver tagResolver) {
      return this.parser.escapeTokens(this.newContext(input, (Pointered)null, tagResolver));
   }

   @NotNull
   public String stripTags(@NotNull final String input) {
      return this.parser.stripTokens(this.newContext(input, (Pointered)null, (TagResolver)null));
   }

   @NotNull
   public String stripTags(@NotNull final String input, @NotNull final TagResolver tagResolver) {
      return this.parser.stripTokens(this.newContext(input, (Pointered)null, tagResolver));
   }

   public boolean strict() {
      return this.strict;
   }

   @NotNull
   public TagResolver tags() {
      return this.parser.tagResolver;
   }

   @NotNull
   private ContextImpl newContext(@NotNull final String input, @Nullable final Pointered target, @Nullable final TagResolver resolver) {
      Objects.requireNonNull(input, "input");
      return new ContextImpl(this.strict, this.emitVirtuals, this.debugOutput, input, this, target, resolver, this.preProcessor, this.postProcessor);
   }

   static {
      BUILDER = (Consumer)SERVICE.map(MiniMessage.Provider::builder).orElseGet(() -> {
         return (builder) -> {
         };
      });
      DEFAULT_NO_OP = UnaryOperator.identity();
      DEFAULT_COMPACTING_METHOD = Component::compact;
   }

   static final class BuilderImpl implements MiniMessage.Builder {
      private TagResolver tagResolver;
      private boolean strict;
      private boolean emitVirtuals;
      private Consumer<String> debug;
      private UnaryOperator<Component> postProcessor;
      private UnaryOperator<String> preProcessor;

      BuilderImpl() {
         this.tagResolver = TagResolver.standard();
         this.strict = false;
         this.emitVirtuals = true;
         this.debug = null;
         this.postProcessor = MiniMessageImpl.DEFAULT_COMPACTING_METHOD;
         this.preProcessor = MiniMessageImpl.DEFAULT_NO_OP;
         MiniMessageImpl.BUILDER.accept(this);
      }

      BuilderImpl(final MiniMessageImpl serializer) {
         this();
         this.tagResolver = serializer.parser.tagResolver;
         this.strict = serializer.strict;
         this.debug = serializer.debugOutput;
         this.postProcessor = serializer.postProcessor;
         this.preProcessor = serializer.preProcessor;
      }

      @NotNull
      public MiniMessage.Builder tags(@NotNull final TagResolver tags) {
         this.tagResolver = (TagResolver)Objects.requireNonNull(tags, "tags");
         return this;
      }

      @NotNull
      public MiniMessage.Builder editTags(@NotNull final Consumer<TagResolver.Builder> adder) {
         Objects.requireNonNull(adder, "adder");
         TagResolver.Builder builder = TagResolver.builder().resolver(this.tagResolver);
         adder.accept(builder);
         this.tagResolver = builder.build();
         return this;
      }

      @NotNull
      public MiniMessage.Builder strict(final boolean strict) {
         this.strict = strict;
         return this;
      }

      @NotNull
      public MiniMessage.Builder emitVirtuals(final boolean emitVirtuals) {
         this.emitVirtuals = emitVirtuals;
         return this;
      }

      @NotNull
      public MiniMessage.Builder debug(@Nullable final Consumer<String> debugOutput) {
         this.debug = debugOutput;
         return this;
      }

      @NotNull
      public MiniMessage.Builder postProcessor(@NotNull final UnaryOperator<Component> postProcessor) {
         this.postProcessor = (UnaryOperator)Objects.requireNonNull(postProcessor, "postProcessor");
         return this;
      }

      @NotNull
      public MiniMessage.Builder preProcessor(@NotNull final UnaryOperator<String> preProcessor) {
         this.preProcessor = (UnaryOperator)Objects.requireNonNull(preProcessor, "preProcessor");
         return this;
      }

      @NotNull
      public MiniMessage build() {
         return new MiniMessageImpl(this.tagResolver, this.strict, this.emitVirtuals, this.debug, this.preProcessor, this.postProcessor);
      }
   }

   static final class Instances {
      static final MiniMessage INSTANCE;

      static {
         INSTANCE = (MiniMessage)MiniMessageImpl.SERVICE.map(MiniMessage.Provider::miniMessage).orElseGet(() -> {
            return new MiniMessageImpl(TagResolver.standard(), false, true, (Consumer)null, MiniMessageImpl.DEFAULT_NO_OP, MiniMessageImpl.DEFAULT_COMPACTING_METHOD);
         });
      }
   }
}
