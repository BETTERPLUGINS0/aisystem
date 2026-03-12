package ac.grim.grimac.shaded.incendo.cloud.suggestion;

import ac.grim.grimac.shaded.incendo.cloud.context.CommandContext;
import ac.grim.grimac.shaded.incendo.cloud.context.CommandInput;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import javax.annotation.CheckReturnValue;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import javax.annotation.concurrent.Immutable;
import org.apiguardian.api.API;
import org.apiguardian.api.API.Status;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.immutables.value.Generated;

@ParametersAreNonnullByDefault
@CheckReturnValue
@API(
   status = Status.INTERNAL,
   consumers = {"ac.grim.grimac.shaded.incendo.cloud.*"}
)
@Generated(
   from = "Suggestions",
   generator = "Immutables"
)
@Immutable
final class SuggestionsImpl<C, S extends Suggestion> implements Suggestions<C, S> {
   @NonNull
   private final CommandContext<C> commandContext;
   @NonNull
   private final List<S> list;
   @NonNull
   private final CommandInput commandInput;

   private SuggestionsImpl(@NonNull CommandContext<C> commandContext, Iterable<? extends S> list, @NonNull CommandInput commandInput) {
      this.commandContext = (CommandContext)Objects.requireNonNull(commandContext, "commandContext");
      this.list = createUnmodifiableList(false, createSafeList(list, true, false));
      this.commandInput = (CommandInput)Objects.requireNonNull(commandInput, "commandInput");
   }

   private SuggestionsImpl(SuggestionsImpl<C, S> original, @NonNull CommandContext<C> commandContext, @NonNull List<S> list, @NonNull CommandInput commandInput) {
      this.commandContext = commandContext;
      this.list = list;
      this.commandInput = commandInput;
   }

   @NonNull
   public CommandContext<C> commandContext() {
      return this.commandContext;
   }

   @NonNull
   public List<S> list() {
      return this.list;
   }

   @NonNull
   public CommandInput commandInput() {
      return this.commandInput;
   }

   public final SuggestionsImpl<C, S> withCommandContext(CommandContext<C> value) {
      if (this.commandContext == value) {
         return this;
      } else {
         CommandContext<C> newValue = (CommandContext)Objects.requireNonNull(value, "commandContext");
         return new SuggestionsImpl(this, newValue, this.list, this.commandInput);
      }
   }

   @SafeVarargs
   public final SuggestionsImpl<C, S> withList(S... elements) {
      List<S> newValue = createUnmodifiableList(false, createSafeList(Arrays.asList(elements), true, false));
      return new SuggestionsImpl(this, this.commandContext, newValue, this.commandInput);
   }

   public final SuggestionsImpl<C, S> withList(Iterable<? extends S> elements) {
      if (this.list == elements) {
         return this;
      } else {
         List<S> newValue = createUnmodifiableList(false, createSafeList(elements, true, false));
         return new SuggestionsImpl(this, this.commandContext, newValue, this.commandInput);
      }
   }

   public final SuggestionsImpl<C, S> withCommandInput(CommandInput value) {
      if (this.commandInput == value) {
         return this;
      } else {
         CommandInput newValue = (CommandInput)Objects.requireNonNull(value, "commandInput");
         return new SuggestionsImpl(this, this.commandContext, this.list, newValue);
      }
   }

   public boolean equals(@Nullable Object another) {
      if (this == another) {
         return true;
      } else {
         return another instanceof SuggestionsImpl && this.equalTo(0, (SuggestionsImpl)another);
      }
   }

   private boolean equalTo(int synthetic, SuggestionsImpl<?, ?> another) {
      return this.commandContext.equals(another.commandContext) && this.list.equals(another.list) && this.commandInput.equals(another.commandInput);
   }

   public int hashCode() {
      int h = 5381;
      int h = h + (h << 5) + this.commandContext.hashCode();
      h += (h << 5) + this.list.hashCode();
      h += (h << 5) + this.commandInput.hashCode();
      return h;
   }

   public String toString() {
      return "Suggestions{commandContext=" + this.commandContext + ", list=" + this.list + ", commandInput=" + this.commandInput + "}";
   }

   public static <C, S extends Suggestion> SuggestionsImpl<C, S> of(@NonNull CommandContext<C> commandContext, @NonNull List<S> list, @NonNull CommandInput commandInput) {
      return of(commandContext, (Iterable)list, commandInput);
   }

   public static <C, S extends Suggestion> SuggestionsImpl<C, S> of(@NonNull CommandContext<C> commandContext, Iterable<? extends S> list, @NonNull CommandInput commandInput) {
      return new SuggestionsImpl(commandContext, list, commandInput);
   }

   public static <C, S extends Suggestion> SuggestionsImpl<C, S> copyOf(Suggestions<C, S> instance) {
      return instance instanceof SuggestionsImpl ? (SuggestionsImpl)instance : of(instance.commandContext(), instance.list(), instance.commandInput());
   }

   private static <T> List<T> createSafeList(Iterable<? extends T> iterable, boolean checkNulls, boolean skipNulls) {
      ArrayList list;
      if (iterable instanceof Collection) {
         int size = ((Collection)iterable).size();
         if (size == 0) {
            return Collections.emptyList();
         }

         list = new ArrayList(size);
      } else {
         list = new ArrayList();
      }

      Iterator var6 = iterable.iterator();

      while(true) {
         Object element;
         do {
            if (!var6.hasNext()) {
               return list;
            }

            element = var6.next();
         } while(skipNulls && element == null);

         if (checkNulls) {
            Objects.requireNonNull(element, "element");
         }

         list.add(element);
      }
   }

   private static <T> List<T> createUnmodifiableList(boolean clone, List<T> list) {
      switch(list.size()) {
      case 0:
         return Collections.emptyList();
      case 1:
         return Collections.singletonList(list.get(0));
      default:
         if (clone) {
            return Collections.unmodifiableList(new ArrayList(list));
         } else {
            if (list instanceof ArrayList) {
               ((ArrayList)list).trimToSize();
            }

            return Collections.unmodifiableList(list);
         }
      }
   }
}
