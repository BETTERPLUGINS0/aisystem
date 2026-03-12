package ac.grim.grimac.shaded.kyori.adventure.text.minimessage.internal.serializer;

import ac.grim.grimac.shaded.jetbrains.annotations.NotNull;
import ac.grim.grimac.shaded.jetbrains.annotations.Nullable;
import ac.grim.grimac.shaded.kyori.adventure.text.Component;
import ac.grim.grimac.shaded.kyori.adventure.text.minimessage.Context;
import ac.grim.grimac.shaded.kyori.adventure.text.minimessage.ParsingException;
import ac.grim.grimac.shaded.kyori.adventure.text.minimessage.tag.Tag;
import ac.grim.grimac.shaded.kyori.adventure.text.minimessage.tag.resolver.ArgumentQueue;
import ac.grim.grimac.shaded.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import java.util.Set;
import java.util.function.BiFunction;
import java.util.function.Function;

class ComponentClaimingResolverImpl implements TagResolver, SerializableResolver.Single {
   @NotNull
   private final Set<String> names;
   @NotNull
   private final BiFunction<ArgumentQueue, Context, Tag> handler;
   @NotNull
   private final Function<Component, Emitable> componentClaim;

   ComponentClaimingResolverImpl(final Set<String> names, final BiFunction<ArgumentQueue, Context, Tag> handler, final Function<Component, Emitable> componentClaim) {
      this.names = names;
      this.handler = handler;
      this.componentClaim = componentClaim;
   }

   @Nullable
   public Tag resolve(@NotNull final String name, @NotNull final ArgumentQueue arguments, @NotNull final Context ctx) throws ParsingException {
      return !this.names.contains(name) ? null : (Tag)this.handler.apply(arguments, ctx);
   }

   public boolean has(@NotNull final String name) {
      return this.names.contains(name);
   }

   @Nullable
   public Emitable claimComponent(@NotNull final Component component) {
      return (Emitable)this.componentClaim.apply(component);
   }
}
