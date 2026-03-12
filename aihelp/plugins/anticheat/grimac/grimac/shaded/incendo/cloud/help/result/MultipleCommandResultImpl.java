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
   from = "MultipleCommandResult",
   generator = "Immutables"
)
@Immutable
final class MultipleCommandResultImpl<C> implements MultipleCommandResult<C> {
   @NonNull
   private final HelpQuery<C> query;
   @NonNull
   private final String longestPath;
   @NonNull
   private final List<String> childSuggestions;

   private MultipleCommandResultImpl(@NonNull HelpQuery<C> query, @NonNull String longestPath, Iterable<String> childSuggestions) {
      this.query = (HelpQuery)Objects.requireNonNull(query, "query");
      this.longestPath = (String)Objects.requireNonNull(longestPath, "longestPath");
      this.childSuggestions = createUnmodifiableList(false, createSafeList(childSuggestions, true, false));
   }

   private MultipleCommandResultImpl(MultipleCommandResultImpl<C> original, @NonNull HelpQuery<C> query, @NonNull String longestPath, @NonNull List<String> childSuggestions) {
      this.query = query;
      this.longestPath = longestPath;
      this.childSuggestions = childSuggestions;
   }

   @NonNull
   public HelpQuery<C> query() {
      return this.query;
   }

   @NonNull
   public String longestPath() {
      return this.longestPath;
   }

   @NonNull
   public List<String> childSuggestions() {
      return this.childSuggestions;
   }

   public final MultipleCommandResultImpl<C> withQuery(HelpQuery<C> value) {
      if (this.query == value) {
         return this;
      } else {
         HelpQuery<C> newValue = (HelpQuery)Objects.requireNonNull(value, "query");
         return new MultipleCommandResultImpl(this, newValue, this.longestPath, this.childSuggestions);
      }
   }

   public final MultipleCommandResultImpl<C> withLongestPath(String value) {
      String newValue = (String)Objects.requireNonNull(value, "longestPath");
      return this.longestPath.equals(newValue) ? this : new MultipleCommandResultImpl(this, this.query, newValue, this.childSuggestions);
   }

   public final MultipleCommandResultImpl<C> withChildSuggestions(String... elements) {
      List<String> newValue = createUnmodifiableList(false, createSafeList(Arrays.asList(elements), true, false));
      return new MultipleCommandResultImpl(this, this.query, this.longestPath, newValue);
   }

   public final MultipleCommandResultImpl<C> withChildSuggestions(Iterable<String> elements) {
      if (this.childSuggestions == elements) {
         return this;
      } else {
         List<String> newValue = createUnmodifiableList(false, createSafeList(elements, true, false));
         return new MultipleCommandResultImpl(this, this.query, this.longestPath, newValue);
      }
   }

   public boolean equals(@Nullable Object another) {
      if (this == another) {
         return true;
      } else {
         return another instanceof MultipleCommandResultImpl && this.equalTo(0, (MultipleCommandResultImpl)another);
      }
   }

   private boolean equalTo(int synthetic, MultipleCommandResultImpl<?> another) {
      return this.query.equals(another.query) && this.longestPath.equals(another.longestPath) && this.childSuggestions.equals(another.childSuggestions);
   }

   public int hashCode() {
      int h = 5381;
      int h = h + (h << 5) + this.query.hashCode();
      h += (h << 5) + this.longestPath.hashCode();
      h += (h << 5) + this.childSuggestions.hashCode();
      return h;
   }

   public String toString() {
      return "MultipleCommandResult{query=" + this.query + ", longestPath=" + this.longestPath + ", childSuggestions=" + this.childSuggestions + "}";
   }

   public static <C> MultipleCommandResultImpl<C> of(@NonNull HelpQuery<C> query, @NonNull String longestPath, @NonNull List<String> childSuggestions) {
      return of(query, longestPath, (Iterable)childSuggestions);
   }

   public static <C> MultipleCommandResultImpl<C> of(@NonNull HelpQuery<C> query, @NonNull String longestPath, Iterable<String> childSuggestions) {
      return new MultipleCommandResultImpl(query, longestPath, childSuggestions);
   }

   public static <C> MultipleCommandResultImpl<C> copyOf(MultipleCommandResult<C> instance) {
      return instance instanceof MultipleCommandResultImpl ? (MultipleCommandResultImpl)instance : of(instance.query(), instance.longestPath(), instance.childSuggestions());
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
