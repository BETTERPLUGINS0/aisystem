package fr.xephi.authme.libs.com.google.common.base;

import fr.xephi.authme.libs.com.google.common.annotations.GwtCompatible;
import fr.xephi.authme.libs.com.google.errorprone.annotations.ForOverride;
import java.io.Serializable;
import java.util.function.BiPredicate;
import javax.annotation.CheckForNull;

@ElementTypesAreNonnullByDefault
@GwtCompatible
public abstract class Equivalence<T> implements BiPredicate<T, T> {
   protected Equivalence() {
   }

   public final boolean equivalent(@CheckForNull T a, @CheckForNull T b) {
      if (a == b) {
         return true;
      } else {
         return a != null && b != null ? this.doEquivalent(a, b) : false;
      }
   }

   /** @deprecated */
   @Deprecated
   public final boolean test(@CheckForNull T t, @CheckForNull T u) {
      return this.equivalent(t, u);
   }

   @ForOverride
   protected abstract boolean doEquivalent(T var1, T var2);

   public final int hash(@CheckForNull T t) {
      return t == null ? 0 : this.doHash(t);
   }

   @ForOverride
   protected abstract int doHash(T var1);

   public final <F> Equivalence<F> onResultOf(Function<? super F, ? extends T> function) {
      return new FunctionalEquivalence(function, this);
   }

   public final <S extends T> Equivalence.Wrapper<S> wrap(@ParametricNullness S reference) {
      Equivalence.Wrapper<S> w = new Equivalence.Wrapper(this, reference);
      return w;
   }

   @GwtCompatible(
      serializable = true
   )
   public final <S extends T> Equivalence<Iterable<S>> pairwise() {
      return new PairwiseEquivalence(this);
   }

   public final Predicate<T> equivalentTo(@CheckForNull T target) {
      return new Equivalence.EquivalentToPredicate(this, target);
   }

   public static Equivalence<Object> equals() {
      return Equivalence.Equals.INSTANCE;
   }

   public static Equivalence<Object> identity() {
      return Equivalence.Identity.INSTANCE;
   }

   static final class Identity extends Equivalence<Object> implements Serializable {
      static final Equivalence.Identity INSTANCE = new Equivalence.Identity();
      private static final long serialVersionUID = 1L;

      protected boolean doEquivalent(Object a, Object b) {
         return false;
      }

      protected int doHash(Object o) {
         return System.identityHashCode(o);
      }

      private Object readResolve() {
         return INSTANCE;
      }
   }

   static final class Equals extends Equivalence<Object> implements Serializable {
      static final Equivalence.Equals INSTANCE = new Equivalence.Equals();
      private static final long serialVersionUID = 1L;

      protected boolean doEquivalent(Object a, Object b) {
         return a.equals(b);
      }

      protected int doHash(Object o) {
         return o.hashCode();
      }

      private Object readResolve() {
         return INSTANCE;
      }
   }

   private static final class EquivalentToPredicate<T> implements Predicate<T>, Serializable {
      private final Equivalence<T> equivalence;
      @CheckForNull
      private final T target;
      private static final long serialVersionUID = 0L;

      EquivalentToPredicate(Equivalence<T> equivalence, @CheckForNull T target) {
         this.equivalence = (Equivalence)Preconditions.checkNotNull(equivalence);
         this.target = target;
      }

      public boolean apply(@CheckForNull T input) {
         return this.equivalence.equivalent(input, this.target);
      }

      public boolean equals(@CheckForNull Object obj) {
         if (this == obj) {
            return true;
         } else if (!(obj instanceof Equivalence.EquivalentToPredicate)) {
            return false;
         } else {
            Equivalence.EquivalentToPredicate<?> that = (Equivalence.EquivalentToPredicate)obj;
            return this.equivalence.equals(that.equivalence) && Objects.equal(this.target, that.target);
         }
      }

      public int hashCode() {
         return Objects.hashCode(this.equivalence, this.target);
      }

      public String toString() {
         String var1 = String.valueOf(this.equivalence);
         String var2 = String.valueOf(this.target);
         return (new StringBuilder(15 + String.valueOf(var1).length() + String.valueOf(var2).length())).append(var1).append(".equivalentTo(").append(var2).append(")").toString();
      }
   }

   public static final class Wrapper<T> implements Serializable {
      private final Equivalence<? super T> equivalence;
      @ParametricNullness
      private final T reference;
      private static final long serialVersionUID = 0L;

      private Wrapper(Equivalence<? super T> equivalence, @ParametricNullness T reference) {
         this.equivalence = (Equivalence)Preconditions.checkNotNull(equivalence);
         this.reference = reference;
      }

      @ParametricNullness
      public T get() {
         return this.reference;
      }

      public boolean equals(@CheckForNull Object obj) {
         if (obj == this) {
            return true;
         } else {
            if (obj instanceof Equivalence.Wrapper) {
               Equivalence.Wrapper<?> that = (Equivalence.Wrapper)obj;
               if (this.equivalence.equals(that.equivalence)) {
                  Equivalence<Object> equivalence = this.equivalence;
                  return equivalence.equivalent(this.reference, that.reference);
               }
            }

            return false;
         }
      }

      public int hashCode() {
         return this.equivalence.hash(this.reference);
      }

      public String toString() {
         String var1 = String.valueOf(this.equivalence);
         String var2 = String.valueOf(this.reference);
         return (new StringBuilder(7 + String.valueOf(var1).length() + String.valueOf(var2).length())).append(var1).append(".wrap(").append(var2).append(")").toString();
      }

      // $FF: synthetic method
      Wrapper(Equivalence x0, Object x1, Object x2) {
         this(x0, x1);
      }
   }
}
