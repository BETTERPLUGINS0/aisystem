package fr.xephi.authme.libs.net.kyori.adventure.text.minimessage.tag.standard;

import fr.xephi.authme.libs.net.kyori.adventure.text.format.TextDecoration;
import fr.xephi.authme.libs.net.kyori.adventure.text.minimessage.Context;
import fr.xephi.authme.libs.net.kyori.adventure.text.minimessage.internal.serializer.SerializableResolver;
import fr.xephi.authme.libs.net.kyori.adventure.text.minimessage.internal.serializer.StyleClaim;
import fr.xephi.authme.libs.net.kyori.adventure.text.minimessage.internal.serializer.TokenEmitter;
import fr.xephi.authme.libs.net.kyori.adventure.text.minimessage.tag.Tag;
import fr.xephi.authme.libs.net.kyori.adventure.text.minimessage.tag.resolver.ArgumentQueue;
import fr.xephi.authme.libs.net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.AbstractMap.SimpleImmutableEntry;
import java.util.Map.Entry;
import java.util.function.BiConsumer;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

final class DecorationTag {
   private static final String B = "b";
   private static final String I = "i";
   private static final String EM = "em";
   private static final String OBF = "obf";
   private static final String ST = "st";
   private static final String U = "u";
   public static final String REVERT = "!";
   static final Map<TextDecoration, TagResolver> RESOLVERS;
   static final TagResolver RESOLVER;

   static Entry<TextDecoration, Stream<TagResolver>> resolvers(final TextDecoration decoration, @Nullable final String shortName, @NotNull final String... secondaryAliases) {
      String canonicalName = (String)TextDecoration.NAMES.key(decoration);
      Set<String> names = new HashSet();
      names.add(canonicalName);
      if (shortName != null) {
         names.add(shortName);
      }

      Collections.addAll(names, secondaryAliases);
      return new SimpleImmutableEntry(decoration, Stream.concat(Stream.of(SerializableResolver.claimingStyle((Set)names, (args, ctx) -> {
         return create(decoration, args, ctx);
      }, claim(decoration, (state, emitter) -> {
         emit(canonicalName, shortName == null ? canonicalName : shortName, state, emitter);
      }))), names.stream().map((name) -> {
         return TagResolver.resolver("!" + name, createNegated(decoration));
      })));
   }

   private DecorationTag() {
   }

   static Tag create(final TextDecoration toApply, final ArgumentQueue args, final Context ctx) {
      boolean flag = !args.hasNext() || !args.pop().isFalse();
      return Tag.styling(toApply.withState(flag));
   }

   static Tag createNegated(final TextDecoration toApply) {
      return Tag.styling(toApply.withState(false));
   }

   @NotNull
   static StyleClaim<TextDecoration.State> claim(@NotNull final TextDecoration decoration, @NotNull final BiConsumer<TextDecoration.State, TokenEmitter> emitable) {
      Objects.requireNonNull(decoration, "decoration");
      return StyleClaim.claim("decoration_" + (String)TextDecoration.NAMES.key(decoration), (style) -> {
         return style.decoration(decoration);
      }, (state) -> {
         return state != TextDecoration.State.NOT_SET;
      }, emitable);
   }

   static void emit(@NotNull final String longName, @NotNull final String shortName, @NotNull final TextDecoration.State state, @NotNull final TokenEmitter emitter) {
      if (state == TextDecoration.State.FALSE) {
         emitter.tag("!" + longName);
      } else {
         emitter.tag(longName);
      }

   }

   static {
      RESOLVERS = (Map)Stream.of(resolvers(TextDecoration.OBFUSCATED, "obf"), resolvers(TextDecoration.BOLD, "b"), resolvers(TextDecoration.STRIKETHROUGH, "st"), resolvers(TextDecoration.UNDERLINED, "u"), resolvers(TextDecoration.ITALIC, "em", "i")).collect(Collectors.toMap(Entry::getKey, (ent) -> {
         return (TagResolver)((Stream)ent.getValue()).collect(TagResolver.toTagResolver());
      }, (l, r) -> {
         return TagResolver.builder().resolver(l).resolver(r).build();
      }, LinkedHashMap::new));
      RESOLVER = TagResolver.resolver((Iterable)RESOLVERS.values());
   }
}
