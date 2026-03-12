package ac.grim.grimac.shaded.kyori.adventure.text.minimessage.tag.resolver;

import ac.grim.grimac.shaded.jetbrains.annotations.NotNull;
import ac.grim.grimac.shaded.jetbrains.annotations.Nullable;
import ac.grim.grimac.shaded.kyori.adventure.text.Component;
import ac.grim.grimac.shaded.kyori.adventure.text.minimessage.Context;
import ac.grim.grimac.shaded.kyori.adventure.text.minimessage.ParsingException;
import ac.grim.grimac.shaded.kyori.adventure.text.minimessage.internal.serializer.ClaimConsumer;
import ac.grim.grimac.shaded.kyori.adventure.text.minimessage.internal.serializer.SerializableResolver;
import ac.grim.grimac.shaded.kyori.adventure.text.minimessage.tag.Tag;
import java.util.Arrays;

final class SequentialTagResolver implements TagResolver, SerializableResolver {
   final TagResolver[] resolvers;

   SequentialTagResolver(@NotNull final TagResolver[] resolvers) {
      this.resolvers = resolvers;
   }

   @Nullable
   public Tag resolve(@NotNull final String name, @NotNull final ArgumentQueue arguments, @NotNull final Context ctx) throws ParsingException {
      ParsingException thrown = null;
      TagResolver[] var5 = this.resolvers;
      int var6 = var5.length;

      for(int var7 = 0; var7 < var6; ++var7) {
         TagResolver resolver = var5[var7];

         try {
            if (resolver.has(name)) {
               Tag placeholder = resolver.resolve(name, arguments, ctx);
               if (placeholder != null) {
                  return placeholder;
               }
            }
         } catch (ParsingException var11) {
            arguments.reset();
            if (thrown == null) {
               thrown = var11;
            } else {
               thrown.addSuppressed(var11);
            }
         } catch (Exception var12) {
            arguments.reset();
            ParsingException err = ctx.newException("Exception thrown while parsing <" + name + ">", var12, arguments);
            if (thrown == null) {
               thrown = err;
            } else {
               thrown.addSuppressed(err);
            }
         }
      }

      if (thrown != null) {
         throw thrown;
      } else {
         return null;
      }
   }

   public boolean has(@NotNull final String name) {
      TagResolver[] var2 = this.resolvers;
      int var3 = var2.length;

      for(int var4 = 0; var4 < var3; ++var4) {
         TagResolver resolver = var2[var4];
         if (resolver.has(name)) {
            return true;
         }
      }

      return false;
   }

   public void handle(@NotNull final Component serializable, @NotNull final ClaimConsumer consumer) {
      TagResolver[] var3 = this.resolvers;
      int var4 = var3.length;

      for(int var5 = 0; var5 < var4; ++var5) {
         TagResolver resolver = var3[var5];
         if (resolver instanceof SerializableResolver) {
            ((SerializableResolver)resolver).handle(serializable, consumer);
         }
      }

   }

   public boolean equals(@Nullable final Object other) {
      if (other == this) {
         return true;
      } else if (!(other instanceof SequentialTagResolver)) {
         return false;
      } else {
         SequentialTagResolver that = (SequentialTagResolver)other;
         return Arrays.equals(this.resolvers, that.resolvers);
      }
   }

   public int hashCode() {
      return Arrays.hashCode(this.resolvers);
   }
}
