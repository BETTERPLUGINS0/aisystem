package fr.xephi.authme.libs.net.kyori.adventure.text.minimessage.tag.resolver;

import fr.xephi.authme.libs.net.kyori.adventure.text.minimessage.internal.TagInternals;
import fr.xephi.authme.libs.net.kyori.adventure.text.minimessage.tag.Tag;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collector;
import org.jetbrains.annotations.NotNull;

final class TagResolverBuilderImpl implements TagResolver.Builder {
   static final Collector<TagResolver, TagResolver.Builder, TagResolver> COLLECTOR = Collector.of(TagResolver::builder, TagResolver.Builder::resolver, (left, right) -> {
      return TagResolver.builder().resolvers(left.build(), right.build());
   }, TagResolver.Builder::build);
   private final Map<String, Tag> replacements = new HashMap();
   private final List<TagResolver> resolvers = new ArrayList();

   @NotNull
   public TagResolver.Builder tag(@NotNull final String name, @NotNull final Tag tag) {
      TagInternals.assertValidTagName((String)Objects.requireNonNull(name, "name"));
      this.replacements.put(name, (Tag)Objects.requireNonNull(tag, "tag"));
      return this;
   }

   @NotNull
   public TagResolver.Builder resolver(@NotNull final TagResolver resolver) {
      if (resolver instanceof SequentialTagResolver) {
         this.resolvers(((SequentialTagResolver)resolver).resolvers, false);
      } else if (!this.consumePotentialMappable(resolver)) {
         this.popMap();
         this.resolvers.add((TagResolver)Objects.requireNonNull(resolver, "resolver"));
      }

      return this;
   }

   @NotNull
   public TagResolver.Builder resolvers(@NotNull final TagResolver... resolvers) {
      return this.resolvers(resolvers, true);
   }

   @NotNull
   private TagResolver.Builder resolvers(@NotNull final TagResolver[] resolvers, final boolean forwards) {
      boolean popped = false;
      Objects.requireNonNull(resolvers, "resolvers");
      if (forwards) {
         TagResolver[] var4 = resolvers;
         int var5 = resolvers.length;

         for(int var6 = 0; var6 < var5; ++var6) {
            TagResolver resolver = var4[var6];
            popped = this.single(resolver, popped);
         }
      } else {
         for(int i = resolvers.length - 1; i >= 0; --i) {
            popped = this.single(resolvers[i], popped);
         }
      }

      return this;
   }

   @NotNull
   public TagResolver.Builder resolvers(@NotNull final Iterable<? extends TagResolver> resolvers) {
      boolean popped = false;

      TagResolver resolver;
      for(Iterator var3 = ((Iterable)Objects.requireNonNull(resolvers, "resolvers")).iterator(); var3.hasNext(); popped = this.single(resolver, popped)) {
         resolver = (TagResolver)var3.next();
      }

      return this;
   }

   private boolean single(final TagResolver resolver, final boolean popped) {
      if (resolver instanceof SequentialTagResolver) {
         this.resolvers(((SequentialTagResolver)resolver).resolvers, false);
      } else if (!this.consumePotentialMappable(resolver)) {
         if (!popped) {
            this.popMap();
         }

         this.resolvers.add((TagResolver)Objects.requireNonNull(resolver, "resolvers[?]"));
         return true;
      }

      return false;
   }

   private void popMap() {
      if (!this.replacements.isEmpty()) {
         this.resolvers.add(new MapTagResolver(new HashMap(this.replacements)));
         this.replacements.clear();
      }

   }

   private boolean consumePotentialMappable(final TagResolver resolver) {
      return resolver instanceof MappableResolver ? ((MappableResolver)resolver).contributeToMap(this.replacements) : false;
   }

   @NotNull
   public TagResolver build() {
      this.popMap();
      if (this.resolvers.size() == 0) {
         return EmptyTagResolver.INSTANCE;
      } else if (this.resolvers.size() == 1) {
         return (TagResolver)this.resolvers.get(0);
      } else {
         TagResolver[] resolvers = (TagResolver[])this.resolvers.toArray(new TagResolver[0]);
         Collections.reverse(Arrays.asList(resolvers));
         return new SequentialTagResolver(resolvers);
      }
   }
}
