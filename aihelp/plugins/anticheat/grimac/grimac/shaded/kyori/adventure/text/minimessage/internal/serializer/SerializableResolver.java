package ac.grim.grimac.shaded.kyori.adventure.text.minimessage.internal.serializer;

import ac.grim.grimac.shaded.jetbrains.annotations.NotNull;
import ac.grim.grimac.shaded.jetbrains.annotations.Nullable;
import ac.grim.grimac.shaded.kyori.adventure.text.Component;
import ac.grim.grimac.shaded.kyori.adventure.text.minimessage.Context;
import ac.grim.grimac.shaded.kyori.adventure.text.minimessage.internal.TagInternals;
import ac.grim.grimac.shaded.kyori.adventure.text.minimessage.tag.Tag;
import ac.grim.grimac.shaded.kyori.adventure.text.minimessage.tag.resolver.ArgumentQueue;
import ac.grim.grimac.shaded.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Objects;
import java.util.Set;
import java.util.function.BiFunction;
import java.util.function.Function;

public interface SerializableResolver {
   @NotNull
   static TagResolver claimingComponent(@NotNull final String name, @NotNull final BiFunction<ArgumentQueue, Context, Tag> handler, @NotNull final Function<Component, Emitable> componentClaim) {
      return claimingComponent(Collections.singleton(name), handler, componentClaim);
   }

   @NotNull
   static TagResolver claimingComponent(@NotNull final Set<String> names, @NotNull final BiFunction<ArgumentQueue, Context, Tag> handler, @NotNull final Function<Component, Emitable> componentClaim) {
      Set<String> ownNames = new HashSet(names);
      Iterator var4 = ownNames.iterator();

      while(var4.hasNext()) {
         String name = (String)var4.next();
         TagInternals.assertValidTagName(name);
      }

      Objects.requireNonNull(handler, "handler");
      return new ComponentClaimingResolverImpl(ownNames, handler, componentClaim);
   }

   @NotNull
   static TagResolver claimingStyle(@NotNull final String name, @NotNull final BiFunction<ArgumentQueue, Context, Tag> handler, @NotNull final StyleClaim<?> styleClaim) {
      return claimingStyle(Collections.singleton(name), handler, styleClaim);
   }

   @NotNull
   static TagResolver claimingStyle(@NotNull final Set<String> names, @NotNull final BiFunction<ArgumentQueue, Context, Tag> handler, @NotNull final StyleClaim<?> styleClaim) {
      Set<String> ownNames = new HashSet(names);
      Iterator var4 = ownNames.iterator();

      while(var4.hasNext()) {
         String name = (String)var4.next();
         TagInternals.assertValidTagName(name);
      }

      Objects.requireNonNull(handler, "handler");
      return new StyleClaimingResolverImpl(ownNames, handler, styleClaim);
   }

   void handle(@NotNull final Component serializable, @NotNull final ClaimConsumer consumer);

   public interface Single extends SerializableResolver {
      default void handle(@NotNull final Component serializable, @NotNull final ClaimConsumer consumer) {
         StyleClaim<?> style = this.claimStyle();
         Emitable component;
         if (style != null && !consumer.styleClaimed(style.claimKey())) {
            component = style.apply(serializable.style());
            if (component != null) {
               consumer.style(style.claimKey(), component);
            }
         }

         if (!consumer.componentClaimed()) {
            component = this.claimComponent(serializable);
            if (component != null) {
               consumer.component(component);
            }
         }

      }

      @Nullable
      default StyleClaim<?> claimStyle() {
         return null;
      }

      @Nullable
      default Emitable claimComponent(@NotNull final Component component) {
         return null;
      }
   }
}
