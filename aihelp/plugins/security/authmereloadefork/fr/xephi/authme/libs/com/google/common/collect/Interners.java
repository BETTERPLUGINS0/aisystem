package fr.xephi.authme.libs.com.google.common.collect;

import fr.xephi.authme.libs.com.google.common.annotations.GwtIncompatible;
import fr.xephi.authme.libs.com.google.common.annotations.VisibleForTesting;
import fr.xephi.authme.libs.com.google.common.base.Equivalence;
import fr.xephi.authme.libs.com.google.common.base.Function;
import fr.xephi.authme.libs.com.google.common.base.Preconditions;
import javax.annotation.CheckForNull;

@ElementTypesAreNonnullByDefault
@GwtIncompatible
public final class Interners {
   private Interners() {
   }

   public static Interners.InternerBuilder newBuilder() {
      return new Interners.InternerBuilder();
   }

   public static <E> Interner<E> newStrongInterner() {
      return newBuilder().strong().build();
   }

   @GwtIncompatible("java.lang.ref.WeakReference")
   public static <E> Interner<E> newWeakInterner() {
      return newBuilder().weak().build();
   }

   public static <E> Function<E, E> asFunction(Interner<E> interner) {
      return new Interners.InternerFunction((Interner)Preconditions.checkNotNull(interner));
   }

   private static class InternerFunction<E> implements Function<E, E> {
      private final Interner<E> interner;

      public InternerFunction(Interner<E> interner) {
         this.interner = interner;
      }

      public E apply(E input) {
         return this.interner.intern(input);
      }

      public int hashCode() {
         return this.interner.hashCode();
      }

      public boolean equals(@CheckForNull Object other) {
         if (other instanceof Interners.InternerFunction) {
            Interners.InternerFunction<?> that = (Interners.InternerFunction)other;
            return this.interner.equals(that.interner);
         } else {
            return false;
         }
      }
   }

   @VisibleForTesting
   static final class InternerImpl<E> implements Interner<E> {
      @VisibleForTesting
      final MapMakerInternalMap<E, MapMaker.Dummy, ?, ?> map;

      private InternerImpl(MapMaker mapMaker) {
         this.map = MapMakerInternalMap.createWithDummyValues(mapMaker.keyEquivalence(Equivalence.equals()));
      }

      public E intern(E sample) {
         MapMaker.Dummy sneaky;
         do {
            MapMakerInternalMap.InternalEntry entry = this.map.getEntry(sample);
            if (entry != null) {
               Object canonical = entry.getKey();
               if (canonical != null) {
                  return canonical;
               }
            }

            sneaky = (MapMaker.Dummy)this.map.putIfAbsent(sample, MapMaker.Dummy.VALUE);
         } while(sneaky != null);

         return sample;
      }

      // $FF: synthetic method
      InternerImpl(MapMaker x0, Object x1) {
         this(x0);
      }
   }

   public static class InternerBuilder {
      private final MapMaker mapMaker;
      private boolean strong;

      private InternerBuilder() {
         this.mapMaker = new MapMaker();
         this.strong = true;
      }

      public Interners.InternerBuilder strong() {
         this.strong = true;
         return this;
      }

      @GwtIncompatible("java.lang.ref.WeakReference")
      public Interners.InternerBuilder weak() {
         this.strong = false;
         return this;
      }

      public Interners.InternerBuilder concurrencyLevel(int concurrencyLevel) {
         this.mapMaker.concurrencyLevel(concurrencyLevel);
         return this;
      }

      public <E> Interner<E> build() {
         if (!this.strong) {
            this.mapMaker.weakKeys();
         }

         return new Interners.InternerImpl(this.mapMaker);
      }

      // $FF: synthetic method
      InternerBuilder(Object x0) {
         this();
      }
   }
}
