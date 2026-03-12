package fr.xephi.authme.libs.net.kyori.adventure.text.minimessage.tag.resolver;

import fr.xephi.authme.libs.net.kyori.adventure.text.minimessage.Context;
import fr.xephi.authme.libs.net.kyori.adventure.text.minimessage.ParsingException;
import fr.xephi.authme.libs.net.kyori.adventure.text.minimessage.internal.TagInternals;
import fr.xephi.authme.libs.net.kyori.adventure.text.minimessage.tag.Tag;
import fr.xephi.authme.libs.net.kyori.adventure.text.minimessage.tag.TagPattern;
import fr.xephi.authme.libs.net.kyori.adventure.text.minimessage.tag.standard.StandardTags;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Objects;
import java.util.Set;
import java.util.function.BiFunction;
import java.util.stream.Collector;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface TagResolver {
   @NotNull
   static TagResolver.Builder builder() {
      return new TagResolverBuilderImpl();
   }

   @NotNull
   static TagResolver standard() {
      return StandardTags.defaults();
   }

   @NotNull
   static TagResolver empty() {
      return EmptyTagResolver.INSTANCE;
   }

   @NotNull
   static TagResolver.Single resolver(@TagPattern @NotNull final String name, @NotNull final Tag tag) {
      TagInternals.assertValidTagName(name);
      return new SingleResolver(name, (Tag)Objects.requireNonNull(tag, "tag"));
   }

   @NotNull
   static TagResolver resolver(@TagPattern @NotNull final String name, @NotNull final BiFunction<ArgumentQueue, Context, Tag> handler) {
      return resolver(Collections.singleton(name), handler);
   }

   @NotNull
   static TagResolver resolver(@NotNull final Set<String> names, @NotNull final BiFunction<ArgumentQueue, Context, Tag> handler) {
      Set<String> ownNames = new HashSet(names);
      Iterator var3 = ownNames.iterator();

      while(var3.hasNext()) {
         String name = (String)var3.next();
         TagInternals.assertValidTagName(name);
      }

      Objects.requireNonNull(handler, "handler");
      return new TagResolver() {
         @Nullable
         public Tag resolve(@NotNull final String name, @NotNull final ArgumentQueue arguments, @NotNull final Context ctx) throws ParsingException {
            return !names.contains(name) ? null : (Tag)handler.apply(arguments, ctx);
         }

         public boolean has(@NotNull final String name) {
            return names.contains(name);
         }
      };
   }

   @NotNull
   static TagResolver resolver(@NotNull final TagResolver... resolvers) {
      return ((TagResolver[])Objects.requireNonNull(resolvers, "resolvers")).length == 1 ? (TagResolver)Objects.requireNonNull(resolvers[0], "resolvers must not contain null elements") : builder().resolvers(resolvers).build();
   }

   @NotNull
   static TagResolver resolver(@NotNull final Iterable<? extends TagResolver> resolvers) {
      if (resolvers instanceof Collection) {
         int size = ((Collection)resolvers).size();
         if (size == 0) {
            return empty();
         }

         if (size == 1) {
            return (TagResolver)Objects.requireNonNull((TagResolver)resolvers.iterator().next(), "resolvers must not contain null elements");
         }
      }

      return builder().resolvers(resolvers).build();
   }

   @NotNull
   static TagResolver caching(@NotNull final TagResolver.WithoutArguments resolver) {
      return (TagResolver)(resolver instanceof CachingTagResolver ? resolver : new CachingTagResolver((TagResolver.WithoutArguments)Objects.requireNonNull(resolver, "resolver")));
   }

   @NotNull
   static Collector<TagResolver, ?, TagResolver> toTagResolver() {
      return TagResolverBuilderImpl.COLLECTOR;
   }

   @Nullable
   Tag resolve(@TagPattern @NotNull final String name, @NotNull final ArgumentQueue arguments, @NotNull final Context ctx) throws ParsingException;

   boolean has(@NotNull final String name);

   public interface Builder {
      @NotNull
      TagResolver.Builder tag(@TagPattern @NotNull final String name, @NotNull final Tag tag);

      @NotNull
      default TagResolver.Builder tag(@TagPattern @NotNull final String name, @NotNull final BiFunction<ArgumentQueue, Context, Tag> handler) {
         return this.tag(Collections.singleton(name), handler);
      }

      @NotNull
      default TagResolver.Builder tag(@NotNull final Set<String> names, @NotNull final BiFunction<ArgumentQueue, Context, Tag> handler) {
         return this.resolver(TagResolver.resolver(names, handler));
      }

      @NotNull
      TagResolver.Builder resolver(@NotNull final TagResolver resolver);

      @NotNull
      TagResolver.Builder resolvers(@NotNull final TagResolver... resolvers);

      @NotNull
      TagResolver.Builder resolvers(@NotNull final Iterable<? extends TagResolver> resolvers);

      @NotNull
      default TagResolver.Builder caching(@NotNull final TagResolver.WithoutArguments dynamic) {
         return this.resolver(TagResolver.caching(dynamic));
      }

      @NotNull
      TagResolver build();
   }

   @FunctionalInterface
   public interface WithoutArguments extends TagResolver {
      @Nullable
      Tag resolve(@TagPattern @NotNull final String name);

      default boolean has(@NotNull final String name) {
         return this.resolve(name) != null;
      }

      @Nullable
      default Tag resolve(@TagPattern @NotNull final String name, @NotNull final ArgumentQueue arguments, @NotNull final Context ctx) throws ParsingException {
         Tag resolved = this.resolve(name);
         if (resolved != null && arguments.hasNext()) {
            throw ctx.newException("Tag '<" + name + ">' does not accept any arguments");
         } else {
            return resolved;
         }
      }
   }

   @ApiStatus.NonExtendable
   public interface Single extends TagResolver.WithoutArguments {
      @NotNull
      String key();

      @NotNull
      Tag tag();

      @Nullable
      default Tag resolve(@TagPattern @NotNull final String name) {
         return this.has(name) ? this.tag() : null;
      }

      default boolean has(@NotNull final String name) {
         return name.equalsIgnoreCase(this.key());
      }
   }
}
