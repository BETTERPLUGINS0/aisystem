package advancedplugins.pm2.cv.models.api.utils.data.tracker;

import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class MapDataTracker<T, U> extends DataTracker<Map<T, U>> implements Map<T, U> {
   public MapDataTracker(Map<T, U> var1) {
      super((Object)var1);
   }

   public int size() {
      return ((Map)this.value).size();
   }

   public boolean isEmpty() {
      return ((Map)this.value).isEmpty();
   }

   public boolean containsKey(Object var1) {
      return ((Map)this.value).containsKey(var1);
   }

   public boolean containsValue(Object var1) {
      return ((Map)this.value).containsValue(var1);
   }

   public U get(Object var1) {
      return ((Map)this.value).get(var1);
   }

   @Nullable
   public U put(T var1, U var2) {
      Object var3 = ((Map)this.value).put(var1, var2);
      this.isDirty |= var3 != var2;
      return var3;
   }

   public U remove(Object var1) {
      if (!this.isDirty) {
         this.isDirty = ((Map)this.value).containsKey(var1);
      }

      return ((Map)this.value).remove(var1);
   }

   public void putAll(@NotNull Map<? extends T, ? extends U> var1) {
      if (!this.isDirty) {
         label24: {
            Iterator var2 = var1.entrySet().iterator();

            Entry var3;
            Object var4;
            do {
               if (!var2.hasNext()) {
                  break label24;
               }

               var3 = (Entry)var2.next();
               var4 = ((Map)this.value).get(var3.getKey());
            } while(var4 != null && var4 == var3.getValue());

            this.isDirty = true;
         }
      }

      ((Map)this.value).putAll(var1);
   }

   public void clear() {
      if (!((Map)this.value).isEmpty()) {
         ((Map)this.value).clear();
         this.isDirty = true;
      }

   }

   @NotNull
   public Set<T> keySet() {
      return ((Map)this.value).keySet();
   }

   @NotNull
   public Collection<U> values() {
      return ((Map)this.value).values();
   }

   @NotNull
   public Set<Entry<T, U>> entrySet() {
      return ((Map)this.value).entrySet();
   }
}
