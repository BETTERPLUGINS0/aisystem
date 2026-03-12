package ac.grim.grimac.shaded.incendo.cloud.help.result;

import ac.grim.grimac.shaded.incendo.cloud.help.HelpQuery;
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
   from = "IndexCommandResult",
   generator = "Immutables"
)
@Immutable
final class IndexCommandResultImpl<C> implements IndexCommandResult<C> {
   @NonNull
   private final HelpQuery<C> query;
   @NonNull
   private final List<CommandEntry<C>> entries;

   private IndexCommandResultImpl(@NonNull HelpQuery<C> query, Iterable<? extends CommandEntry<C>> entries) {
      this.query = (HelpQuery)Objects.requireNonNull(query, "query");
      this.entries = createUnmodifiableList(false, createSafeList(entries, true, false));
   }

   private IndexCommandResultImpl(IndexCommandResultImpl<C> original, @NonNull HelpQuery<C> query, @NonNull List<CommandEntry<C>> entries) {
      this.query = query;
      this.entries = entries;
   }

   @NonNull
   public HelpQuery<C> query() {
      return this.query;
   }

   @NonNull
   public List<CommandEntry<C>> entries() {
      return this.entries;
   }

   public final IndexCommandResultImpl<C> withQuery(HelpQuery<C> value) {
      if (this.query == value) {
         return this;
      } else {
         HelpQuery<C> newValue = (HelpQuery)Objects.requireNonNull(value, "query");
         return new IndexCommandResultImpl(this, newValue, this.entries);
      }
   }

   @SafeVarargs
   public final IndexCommandResultImpl<C> withEntries(CommandEntry<C>... elements) {
      List<CommandEntry<C>> newValue = createUnmodifiableList(false, createSafeList(Arrays.asList(elements), true, false));
      return new IndexCommandResultImpl(this, this.query, newValue);
   }

   public final IndexCommandResultImpl<C> withEntries(Iterable<? extends CommandEntry<C>> elements) {
      if (this.entries == elements) {
         return this;
      } else {
         List<CommandEntry<C>> newValue = createUnmodifiableList(false, createSafeList(elements, true, false));
         return new IndexCommandResultImpl(this, this.query, newValue);
      }
   }

   public boolean equals(@Nullable Object another) {
      if (this == another) {
         return true;
      } else {
         return another instanceof IndexCommandResultImpl && this.equalTo(0, (IndexCommandResultImpl)another);
      }
   }

   private boolean equalTo(int synthetic, IndexCommandResultImpl<?> another) {
      return this.query.equals(another.query) && this.entries.equals(another.entries);
   }

   public int hashCode() {
      int h = 5381;
      int h = h + (h << 5) + this.query.hashCode();
      h += (h << 5) + this.entries.hashCode();
      return h;
   }

   public String toString() {
      return "IndexCommandResult{query=" + this.query + ", entries=" + this.entries + "}";
   }

   public static <C> IndexCommandResultImpl<C> of(@NonNull HelpQuery<C> query, @NonNull List<CommandEntry<C>> entries) {
      return of(query, (Iterable)entries);
   }

   public static <C> IndexCommandResultImpl<C> of(@NonNull HelpQuery<C> query, Iterable<? extends CommandEntry<C>> entries) {
      return new IndexCommandResultImpl(query, entries);
   }

   public static <C> IndexCommandResultImpl<C> copyOf(IndexCommandResult<C> instance) {
      return instance instanceof IndexCommandResultImpl ? (IndexCommandResultImpl)instance : of(instance.query(), instance.entries());
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
