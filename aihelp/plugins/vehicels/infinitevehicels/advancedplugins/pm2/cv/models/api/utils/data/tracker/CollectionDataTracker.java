package advancedplugins.pm2.cv.models.api.utils.data.tracker;

import java.util.Collection;
import java.util.Iterator;
import org.jetbrains.annotations.NotNull;

public class CollectionDataTracker<T> extends DataTracker<Collection<T>> implements Collection<T> {
   public CollectionDataTracker(Collection<T> var1) {
      super((Object)var1);
   }

   public int size() {
      return ((Collection)this.value).size();
   }

   public boolean isEmpty() {
      return ((Collection)this.value).isEmpty();
   }

   public boolean contains(Object var1) {
      return ((Collection)this.value).contains(var1);
   }

   @NotNull
   public Iterator<T> iterator() {
      return ((Collection)this.value).iterator();
   }

   @NotNull
   public Object[] toArray() {
      return ((Collection)this.value).toArray();
   }

   @NotNull
   public <T1> T1[] toArray(@NotNull T1[] var1) {
      return ((Collection)this.value).toArray(var1);
   }

   public boolean add(T var1) {
      boolean var2 = ((Collection)this.value).add(var1);
      this.isDirty |= var2;
      return var2;
   }

   public boolean remove(Object var1) {
      boolean var2 = ((Collection)this.value).remove(var1);
      this.isDirty |= var2;
      return var2;
   }

   public boolean containsAll(@NotNull Collection<?> var1) {
      return ((Collection)this.value).containsAll(var1);
   }

   public boolean addAll(@NotNull Collection<? extends T> var1) {
      boolean var2 = ((Collection)this.value).addAll(var1);
      this.isDirty |= var2;
      return var2;
   }

   public boolean removeAll(@NotNull Collection<?> var1) {
      boolean var2 = ((Collection)this.value).removeAll(var1);
      this.isDirty |= var2;
      return var2;
   }

   public boolean retainAll(@NotNull Collection<?> var1) {
      boolean var2 = ((Collection)this.value).retainAll(var1);
      this.isDirty |= var2;
      return var2;
   }

   public void clear() {
      if (!((Collection)this.value).isEmpty()) {
         ((Collection)this.value).clear();
         this.isDirty = true;
      }

   }
}
