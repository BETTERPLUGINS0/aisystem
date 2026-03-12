package fr.xephi.authme.libs.com.google.common.base;

import fr.xephi.authme.libs.com.google.common.annotations.Beta;
import fr.xephi.authme.libs.com.google.common.annotations.GwtCompatible;
import fr.xephi.authme.libs.com.google.errorprone.annotations.DoNotMock;
import java.io.Serializable;
import java.util.Iterator;
import java.util.Set;
import javax.annotation.CheckForNull;

@DoNotMock("Use Optional.of(value) or Optional.absent()")
@ElementTypesAreNonnullByDefault
@GwtCompatible(
   serializable = true
)
public abstract class Optional<T> implements Serializable {
   private static final long serialVersionUID = 0L;

   public static <T> Optional<T> absent() {
      return Absent.withType();
   }

   public static <T> Optional<T> of(T reference) {
      return new Present(Preconditions.checkNotNull(reference));
   }

   public static <T> Optional<T> fromNullable(@CheckForNull T nullableReference) {
      return (Optional)(nullableReference == null ? absent() : new Present(nullableReference));
   }

   @CheckForNull
   public static <T> Optional<T> fromJavaUtil(@CheckForNull java.util.Optional<T> javaUtilOptional) {
      return javaUtilOptional == null ? null : fromNullable(javaUtilOptional.orElse((Object)null));
   }

   @CheckForNull
   public static <T> java.util.Optional<T> toJavaUtil(@CheckForNull Optional<T> googleOptional) {
      return googleOptional == null ? null : googleOptional.toJavaUtil();
   }

   public java.util.Optional<T> toJavaUtil() {
      return java.util.Optional.ofNullable(this.orNull());
   }

   Optional() {
   }

   public abstract boolean isPresent();

   public abstract T get();

   public abstract T or(T var1);

   public abstract Optional<T> or(Optional<? extends T> var1);

   @Beta
   public abstract T or(Supplier<? extends T> var1);

   @CheckForNull
   public abstract T orNull();

   public abstract Set<T> asSet();

   public abstract <V> Optional<V> transform(Function<? super T, V> var1);

   public abstract boolean equals(@CheckForNull Object var1);

   public abstract int hashCode();

   public abstract String toString();

   @Beta
   public static <T> Iterable<T> presentInstances(final Iterable<? extends Optional<? extends T>> optionals) {
      Preconditions.checkNotNull(optionals);
      return new Iterable<T>() {
         public Iterator<T> iterator() {
            return new AbstractIterator<T>() {
               private final Iterator<? extends Optional<? extends T>> iterator = (Iterator)Preconditions.checkNotNull(optionals.iterator());

               @CheckForNull
               protected T computeNext() {
                  while(true) {
                     if (this.iterator.hasNext()) {
                        Optional<? extends T> optional = (Optional)this.iterator.next();
                        if (!optional.isPresent()) {
                           continue;
                        }

                        return optional.get();
                     }

                     return this.endOfData();
                  }
               }
            };
         }
      };
   }
}
