package fr.xephi.authme.libs.net.kyori.adventure.text.minimessage;

import fr.xephi.authme.libs.net.kyori.adventure.builder.AbstractBuilder;
import fr.xephi.authme.libs.net.kyori.adventure.pointer.Pointered;
import fr.xephi.authme.libs.net.kyori.adventure.text.Component;
import fr.xephi.authme.libs.net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import fr.xephi.authme.libs.net.kyori.adventure.text.minimessage.tree.Node;
import fr.xephi.authme.libs.net.kyori.adventure.text.serializer.ComponentSerializer;
import fr.xephi.authme.libs.net.kyori.adventure.util.PlatformAPI;
import java.util.function.Consumer;
import java.util.function.UnaryOperator;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface MiniMessage extends ComponentSerializer<Component, Component, String> {
   @NotNull
   static MiniMessage miniMessage() {
      return MiniMessageImpl.Instances.INSTANCE;
   }

   @NotNull
   String escapeTags(@NotNull final String input);

   @NotNull
   String escapeTags(@NotNull final String input, @NotNull final TagResolver tagResolver);

   @NotNull
   default String escapeTags(@NotNull final String input, @NotNull final TagResolver... tagResolvers) {
      return this.escapeTags(input, TagResolver.resolver(tagResolvers));
   }

   @NotNull
   String stripTags(@NotNull final String input);

   @NotNull
   String stripTags(@NotNull final String input, @NotNull final TagResolver tagResolver);

   @NotNull
   default String stripTags(@NotNull final String input, @NotNull final TagResolver... tagResolvers) {
      return this.stripTags(input, TagResolver.resolver(tagResolvers));
   }

   @NotNull
   Component deserialize(@NotNull final String input, @NotNull final Pointered target);

   @NotNull
   Component deserialize(@NotNull final String input, @NotNull final TagResolver tagResolver);

   @NotNull
   Component deserialize(@NotNull final String input, @NotNull final Pointered target, @NotNull final TagResolver tagResolver);

   @NotNull
   default Component deserialize(@NotNull final String input, @NotNull final TagResolver... tagResolvers) {
      return this.deserialize(input, TagResolver.resolver(tagResolvers));
   }

   @NotNull
   default Component deserialize(@NotNull final String input, @NotNull final Pointered target, @NotNull final TagResolver... tagResolvers) {
      return this.deserialize(input, target, TagResolver.resolver(tagResolvers));
   }

   @NotNull
   Node.Root deserializeToTree(@NotNull final String input);

   @NotNull
   Node.Root deserializeToTree(@NotNull final String input, @NotNull final Pointered target);

   @NotNull
   Node.Root deserializeToTree(@NotNull final String input, @NotNull final TagResolver tagResolver);

   @NotNull
   Node.Root deserializeToTree(@NotNull final String input, @NotNull final Pointered target, @NotNull final TagResolver tagResolver);

   @NotNull
   default Node.Root deserializeToTree(@NotNull final String input, @NotNull final TagResolver... tagResolvers) {
      return this.deserializeToTree(input, TagResolver.resolver(tagResolvers));
   }

   @NotNull
   default Node.Root deserializeToTree(@NotNull final String input, @NotNull final Pointered target, @NotNull final TagResolver... tagResolvers) {
      return this.deserializeToTree(input, target, TagResolver.resolver(tagResolvers));
   }

   boolean strict();

   @NotNull
   TagResolver tags();

   static MiniMessage.Builder builder() {
      return new MiniMessageImpl.BuilderImpl();
   }

   @PlatformAPI
   @ApiStatus.Internal
   public interface Provider {
      @PlatformAPI
      @ApiStatus.Internal
      @NotNull
      MiniMessage miniMessage();

      @PlatformAPI
      @ApiStatus.Internal
      @NotNull
      Consumer<MiniMessage.Builder> builder();
   }

   public interface Builder extends AbstractBuilder<MiniMessage> {
      @NotNull
      MiniMessage.Builder tags(@NotNull final TagResolver tags);

      @NotNull
      MiniMessage.Builder editTags(@NotNull final Consumer<TagResolver.Builder> adder);

      @NotNull
      MiniMessage.Builder strict(final boolean strict);

      @NotNull
      MiniMessage.Builder debug(@Nullable final Consumer<String> debugOutput);

      @NotNull
      MiniMessage.Builder postProcessor(@NotNull final UnaryOperator<Component> postProcessor);

      @NotNull
      MiniMessage.Builder preProcessor(@NotNull final UnaryOperator<String> preProcessor);

      @NotNull
      MiniMessage build();
   }
}
