package fr.xephi.authme.libs.net.kyori.adventure.text.minimessage;

import fr.xephi.authme.libs.net.kyori.adventure.text.minimessage.tag.Tag;
import fr.xephi.authme.libs.net.kyori.adventure.text.minimessage.tag.resolver.ArgumentQueue;
import java.util.List;
import java.util.Objects;
import java.util.function.Supplier;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

final class ArgumentQueueImpl<T extends Tag.Argument> implements ArgumentQueue {
   private final Context context;
   final List<T> args;
   private int ptr = 0;

   ArgumentQueueImpl(final Context context, final List<T> args) {
      this.context = context;
      this.args = args;
   }

   @NotNull
   public T pop() {
      if (!this.hasNext()) {
         throw this.context.newException("Missing argument for this tag!", this);
      } else {
         return (Tag.Argument)this.args.get(this.ptr++);
      }
   }

   @NotNull
   public T popOr(@NotNull final String errorMessage) {
      Objects.requireNonNull(errorMessage, "errorMessage");
      if (!this.hasNext()) {
         throw this.context.newException(errorMessage, this);
      } else {
         return (Tag.Argument)this.args.get(this.ptr++);
      }
   }

   @NotNull
   public T popOr(@NotNull final Supplier<String> errorMessage) {
      Objects.requireNonNull(errorMessage, "errorMessage");
      if (!this.hasNext()) {
         throw this.context.newException((String)Objects.requireNonNull((String)errorMessage.get(), "errorMessage.get()"), this);
      } else {
         return (Tag.Argument)this.args.get(this.ptr++);
      }
   }

   @Nullable
   public T peek() {
      return this.hasNext() ? (Tag.Argument)this.args.get(this.ptr) : null;
   }

   public boolean hasNext() {
      return this.ptr < this.args.size();
   }

   public void reset() {
      this.ptr = 0;
   }

   public String toString() {
      return this.args.toString();
   }
}
