package ac.grim.grimac.shaded.maps.weak;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

public class OptionalWeak<W extends Weak<W>> {
   private static final OptionalWeak EMPTY = new OptionalWeak(Optional.empty());
   private final Optional<W> inner;

   public static <T extends Weak<T>> OptionalWeak<T> of(T val) {
      return new OptionalWeak(Optional.ofNullable(val));
   }

   public static <T extends Weak<T>> OptionalWeak<T> empty() {
      return EMPTY;
   }

   private OptionalWeak(Optional<W> inner) {
      this.inner = inner.filter((d) -> {
         return d.isPresent();
      });
   }

   public W get() {
      return (Weak)this.inner.get();
   }

   public boolean isPresent() {
      return this.inner.isPresent();
   }

   public void ifPresent(Consumer<? super W> consumer) {
      this.inner.ifPresent(consumer);
   }

   public OptionalWeak<W> filter(Predicate<? super W> predicate) {
      Objects.requireNonNull(predicate);
      if (!this.isPresent()) {
         return this;
      } else {
         return predicate.test(this.get()) ? this : empty();
      }
   }

   public <U> Optional<U> map(Function<? super W, ? extends U> mapper) {
      return this.inner.map(mapper);
   }

   public <U> Optional<U> flatMap(Function<? super W, Optional<U>> mapper) {
      return this.inner.flatMap(mapper);
   }

   public W orElse(W other) {
      return (Weak)this.inner.orElse(other);
   }

   public W orElseGet(Supplier<? extends W> other) {
      return (Weak)this.inner.orElseGet(other);
   }

   public <X extends Throwable> W orElseThrow(Supplier<? extends X> exceptionSupplier) throws X {
      return (Weak)this.inner.orElseThrow(exceptionSupplier);
   }

   public Optional<Object> asObject() {
      return this.inner.map((d) -> {
         return d.asObject();
      });
   }

   public <T> Optional<T> as(Class<T> type) {
      return this.inner.filter((d) -> {
         return d.is(type);
      }).map((d) -> {
         return d.as(type);
      });
   }

   public Optional<String> asString() {
      return this.as(String.class);
   }

   public <T> Optional<List<T>> asList() {
      return this.inner.filter((d) -> {
         return d.isList();
      }).map((d) -> {
         return d.asList();
      });
   }

   public <K, V> Optional<Map<K, V>> asMap() {
      return this.inner.filter((d) -> {
         return d.isMap();
      }).map((d) -> {
         return d.asMap();
      });
   }

   public ConverterMaybe convert() {
      return new ConverterMaybe(this.inner);
   }

   public boolean equals(Object o) {
      if (this == o) {
         return true;
      } else if (o != null && this.getClass() == o.getClass()) {
         OptionalWeak<?> that = (OptionalWeak)o;
         return Objects.equals(this.inner, that.inner);
      } else {
         return false;
      }
   }

   public int hashCode() {
      return Objects.hashCode(this.inner);
   }

   public String toString() {
      return this.inner.toString();
   }
}
