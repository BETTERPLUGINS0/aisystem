package fr.xephi.authme.libs.com.google.common.util.concurrent;

import fr.xephi.authme.libs.com.google.common.annotations.GwtCompatible;
import fr.xephi.authme.libs.com.google.common.collect.ImmutableCollection;
import fr.xephi.authme.libs.com.google.common.collect.Lists;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import javax.annotation.CheckForNull;

@ElementTypesAreNonnullByDefault
@GwtCompatible(
   emulated = true
)
abstract class CollectionFuture<V, C> extends AggregateFuture<V, C> {
   @CheckForNull
   private List<CollectionFuture.Present<V>> values;

   CollectionFuture(ImmutableCollection<? extends ListenableFuture<? extends V>> futures, boolean allMustSucceed) {
      super(futures, allMustSucceed, true);
      List<CollectionFuture.Present<V>> values = futures.isEmpty() ? Collections.emptyList() : Lists.newArrayListWithCapacity(futures.size());

      for(int i = 0; i < futures.size(); ++i) {
         ((List)values).add((Object)null);
      }

      this.values = (List)values;
   }

   final void collectOneValue(int index, @ParametricNullness V returnValue) {
      List<CollectionFuture.Present<V>> localValues = this.values;
      if (localValues != null) {
         localValues.set(index, new CollectionFuture.Present(returnValue));
      }

   }

   final void handleAllCompleted() {
      List<CollectionFuture.Present<V>> localValues = this.values;
      if (localValues != null) {
         this.set(this.combine(localValues));
      }

   }

   void releaseResources(AggregateFuture.ReleaseResourcesReason reason) {
      super.releaseResources(reason);
      this.values = null;
   }

   abstract C combine(List<CollectionFuture.Present<V>> var1);

   private static final class Present<V> {
      V value;

      Present(V value) {
         this.value = value;
      }
   }

   static final class ListFuture<V> extends CollectionFuture<V, List<V>> {
      ListFuture(ImmutableCollection<? extends ListenableFuture<? extends V>> futures, boolean allMustSucceed) {
         super(futures, allMustSucceed);
         this.init();
      }

      public List<V> combine(List<CollectionFuture.Present<V>> values) {
         List<V> result = Lists.newArrayListWithCapacity(values.size());
         Iterator var3 = values.iterator();

         while(var3.hasNext()) {
            CollectionFuture.Present<V> element = (CollectionFuture.Present)var3.next();
            result.add(element != null ? element.value : null);
         }

         return Collections.unmodifiableList(result);
      }
   }
}
